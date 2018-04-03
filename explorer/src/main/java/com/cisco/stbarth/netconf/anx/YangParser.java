/**
 * Copyright (c) 2018 Cisco Systems
 * 
 * Author: Steven Barth <stbarth@cisco.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cisco.stbarth.netconf.anx;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.Futures;
import org.opendaylight.yangtools.yang.model.api.ModuleImport;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.model.parser.api.YangSyntaxErrorException;
import org.opendaylight.yangtools.yang.model.repo.api.*;
import org.opendaylight.yangtools.yang.model.repo.spi.PotentialSchemaSource;
import org.opendaylight.yangtools.yang.model.repo.spi.SchemaSourceProvider;
import org.opendaylight.yangtools.yang.model.repo.util.InMemorySchemaSourceCache;
import org.opendaylight.yangtools.yang.parser.repo.SharedSchemaRepository;
import org.opendaylight.yangtools.yang.parser.util.ASTSchemaSource;
import org.opendaylight.yangtools.yang.parser.util.TextToASTTransformer;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Wrapper for ODL yangtools parser
 */
public class YangParser implements SchemaSourceProvider<YangTextSchemaSource> {
    private HashMap<SourceIdentifier,YangTextSchemaSource> sources;
    private SharedSchemaRepository repository;
    private SchemaContext schemaContext;
    private InMemorySchemaSourceCache<ASTSchemaSource> cache;
    private List<String> warnings;
    private Map<String,String> blacklist;

    YangParser() {
        //System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
        sources = new HashMap<>();
        repository = new SharedSchemaRepository("yang-context-resolver");
        repository.registerSchemaSourceListener(TextToASTTransformer.create(repository, repository));
        cache = InMemorySchemaSourceCache.createSoftCache(repository, ASTSchemaSource.class);
        warnings = new LinkedList<>();
        blacklist = new HashMap<>();

        // Some Openconfig YANG models use YANG 1.1 feature, but claim to be YANG 1.0
        // exclude those, since they mess up the parser
        blacklist.put("openconfig-telemetry", "2017-02-20");
        blacklist.put("openconfig-bgp", "2016-06-06");
        blacklist.put("openconfig-bgp-policy", "2016-06-06");
        blacklist.put("openconfig-mpls-rsvp", "2016-12-15");
        blacklist.put("openconfig-mpls-te", "2016-12-15");
        blacklist.put("openconfig-if-ip", "2016-12-22");
    }

    public Collection<YangTextSchemaSource> getSources() {
        return sources.values();
    }

    public void registerSource(String identifier, String version, URL url)
            throws SchemaSourceException, IOException, YangSyntaxErrorException {
        registerSource(identifier, version, Resources.asByteSource(url));
    }

    public void registerSource(String identifier, String version, byte[] bytes)
            throws SchemaSourceException, IOException, YangSyntaxErrorException {
        registerSource(identifier, version, YangTextSchemaSource.wrap(bytes));
    }

    public void registerSource(String identifier, String version, ByteSource byteSource)
            throws SchemaSourceException, IOException, YangSyntaxErrorException {
        YangTextSchemaSource source = YangTextSchemaSource.delegateForByteSource(
                RevisionSourceIdentifier.create(identifier, version), byteSource);
        ASTSchemaSource ast = TextToASTTransformer.transformText(source);
        SourceIdentifier actualIdentifier = ast.getIdentifier();


        // Apply blacklist
        String blacklistVersion = blacklist.get(identifier);
        if (blacklistVersion != null && version.compareTo(blacklistVersion) < 0) {
            warnings.add(String.format("%s@%s blacklisted: not YANG compliant", identifier, version));
            System.err.printf("%TF %TT: %s\n", System.currentTimeMillis(), System.currentTimeMillis(),
                    warnings.get(warnings.size() - 1));
            return;
        }

        // Fixup YANG source identifier if the provided YANG model has a different actual identifier
        if (!source.getIdentifier().equals(actualIdentifier))
            source = YangTextSchemaSource.delegateForByteSource(actualIdentifier, byteSource);

        cache.schemaSourceEncountered(ast);

        sources.put(source.getIdentifier(), source);
        repository.registerSchemaSource(this, PotentialSchemaSource.create(
                source.getIdentifier(), YangTextSchemaSource.class, PotentialSchemaSource.Costs.IMMEDIATE.getValue()));
    }

    public void parse() {
        SchemaContextFactory factory = repository.createSchemaContextFactory(SchemaSourceFilter.ALWAYS_ACCEPT);
        Collection<SourceIdentifier> requiredSources = new HashSet<>(sources.keySet());
        // Parse all available YANG models, if we fail remove failed model and retry with remaining models
        while (!requiredSources.isEmpty()) {
            try {
                schemaContext = factory.createSchemaContext(requiredSources).checkedGet();
                break;
            } catch (SchemaResolutionException e) {
                if (!e.getUnsatisfiedImports().isEmpty()) {
                    for (SourceIdentifier id : e.getUnsatisfiedImports().keySet()) {
                        String imports = e.getUnsatisfiedImports().get(id).stream()
                                .map(ModuleImport::getModuleName).collect(Collectors.joining(", "));
                        warnings.add(String.format("%s tries to imports missing model: %s",
                                id.toYangFilename(), imports));
                        System.err.printf("%TF %TT: %s\n", System.currentTimeMillis(), System.currentTimeMillis(),
                                warnings.get(warnings.size() - 1));
                    }
                }

                if (e.getFailedSource() != null) {
                    requiredSources.remove(e.getFailedSource());
                    warnings.add(String.format("%s failed to parse as a valid YANG model",
                            e.getFailedSource().toYangFilename()));
                    System.err.printf("%TF %TT: %s\n", System.currentTimeMillis(), System.currentTimeMillis(),
                            warnings.get(warnings.size() - 1));
                } else {
                    requiredSources = new HashSet<>(e.getResolvedSources());
                }

                e.printStackTrace();
            }
        }
    }

    public SchemaContext getSchemaContext() {
        return schemaContext;
    }

    public Collection<String> getWarnings() {
        return warnings;
    }

    public void addWarning(String warning) {
        warnings.add(warning);
    }

    @Override
    public CheckedFuture<? extends YangTextSchemaSource, SchemaSourceException> getSource(SourceIdentifier sourceIdentifier) {
        YangTextSchemaSource source = sources.get(sourceIdentifier);
        return source != null ? Futures.immediateCheckedFuture(source) : Futures.immediateFailedCheckedFuture(
                new MissingSchemaSourceException("URL for " + sourceIdentifier + " not registered", sourceIdentifier));
    }
}
