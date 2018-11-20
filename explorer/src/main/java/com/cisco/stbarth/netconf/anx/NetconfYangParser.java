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

import com.cisco.stbarth.netconf.anc.Netconf;
import com.cisco.stbarth.netconf.anc.NetconfClient;
import com.cisco.stbarth.netconf.anc.NetconfException;
import com.cisco.stbarth.netconf.anc.NetconfSession;
import com.cisco.stbarth.netconf.anc.XMLElement;
import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.opendaylight.yangtools.yang.common.Revision;
import org.opendaylight.yangtools.yang.model.api.ModuleImport;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.model.parser.api.YangSyntaxErrorException;
import org.opendaylight.yangtools.yang.model.repo.api.*;
import org.opendaylight.yangtools.yang.model.repo.spi.PotentialSchemaSource;
import org.opendaylight.yangtools.yang.model.repo.spi.SchemaSourceProvider;
import org.opendaylight.yangtools.yang.model.repo.util.InMemorySchemaSourceCache;
import org.opendaylight.yangtools.yang.parser.repo.SharedSchemaRepository;
import org.opendaylight.yangtools.yang.parser.rfc7950.repo.ASTSchemaSource;
import org.opendaylight.yangtools.yang.parser.rfc7950.repo.TextToASTTransformer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Wrapper for ODL yangtools parser
 */
public class NetconfYangParser implements SchemaSourceProvider<YangTextSchemaSource> {
    private HashMap<SourceIdentifier,YangTextSchemaSource> sources = new HashMap<>();
    private SharedSchemaRepository repository = new SharedSchemaRepository("yang-context-resolver");
    private SchemaContext schemaContext;
    private InMemorySchemaSourceCache<ASTSchemaSource> cache = InMemorySchemaSourceCache.createSoftCache(repository, ASTSchemaSource.class);
    private List<String> warnings = new LinkedList<>();
    private String cacheDirectory;

    public interface RetrieverCallback {
        void onSchema(int iteration, String identifier, String version, Exception e);
    }

    NetconfYangParser() {
        //System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
        repository.registerSchemaSourceListener(TextToASTTransformer.create(repository, repository));
    }

    public void setCacheDirectory(String cacheDirectory) {
        this.cacheDirectory = cacheDirectory;
    }

    public Collection<YangTextSchemaSource> getSources() {
        return sources.values();
    }

    public Map<String,String> getAvailableSchemas(NetconfSession session) throws NetconfException {
        // Use NETCONF monitoring to query available schemas for retriving from device
        HashMap<String,String> schemas = new HashMap<>();
        session.get(
                Arrays.asList(new XMLElement(Netconf.NS_NETCONF_MONITORING, "netconf-state").withChild("schemas")))
                        .withoutNamespaces().find("netconf-state/schemas/schema")
                        .forEach(x -> schemas.putIfAbsent(x.getText("identifier"), x.getText("version")));
        return schemas;
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
                RevisionSourceIdentifier.create(identifier, Revision.ofNullable(version)), byteSource);
        ASTSchemaSource ast = TextToASTTransformer.transformText(source);
        SourceIdentifier actualIdentifier = ast.getIdentifier();

        // Fixup YANG source identifier if the provided YANG model has a different actual identifier
        if (!source.getIdentifier().equals(actualIdentifier))
            source = YangTextSchemaSource.delegateForByteSource(actualIdentifier, byteSource);

        cache.schemaSourceEncountered(ast);

        sources.put(source.getIdentifier(), source);
        repository.registerSchemaSource(this, PotentialSchemaSource.create(
                source.getIdentifier(), YangTextSchemaSource.class, PotentialSchemaSource.Costs.IMMEDIATE.getValue()));
    }
    
    public void retrieveSchemas(NetconfSession session, Map<String, String> schemas, RetrieverCallback callback)
            throws SchemaSourceException, IOException, YangSyntaxErrorException, NetconfException {
        int iteration = 0;
        for (Map.Entry<String, String> entry : schemas.entrySet()) {
            String identifier = entry.getKey();
            String version = entry.getValue();

            try {
                byte[] yangData = null;
                File cacheFile = new File(cacheDirectory, String.format("%s@%s.yang", identifier, version));

                if (cacheDirectory != null && cacheFile.isFile())
                    yangData = Files.readAllBytes(cacheFile.toPath());

                if (yangData == null) {
                    yangData = session.getSchema(identifier, version, "yang").getText().getBytes(StandardCharsets.UTF_8);

                    if (cacheDirectory != null)
                        Files.write(cacheFile.toPath(), yangData);
                }

                registerSource(identifier, version, yangData);
                callback.onSchema(++iteration, identifier, version, null);
            } catch (NetconfException.RPCException f) {
                addWarning(String.format("Failed to get schema for %s@%s (%s)\n",
                        identifier, version, f.getMessage()));
                callback.onSchema(++iteration, identifier, version, f);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onSchema(++iteration, identifier, version, e);
            }
        }
    }

    public void parse() {
        SchemaContextFactory factory = repository.createSchemaContextFactory(SchemaContextFactoryConfiguration
                .builder().setFilter(SchemaSourceFilter.ALWAYS_ACCEPT).build());
        Collection<SourceIdentifier> requiredSources = new HashSet<>(sources.keySet());

        // Workaround for NCS, it is not exporting some essential models so we provide those manually);
        if (requiredSources.stream().map(SourceIdentifier::getName).anyMatch("tailf-ncs-common"::equals) &&
                !requiredSources.stream().map(SourceIdentifier::getName).anyMatch("tailf-common"::equals)) {
            try {
                ClassLoader classLoader = this.getClass().getClassLoader();
                registerSource("tailf-common", "2017-08-23",
                        classLoader.getResource("tailf-common.yang"));
                registerSource("tailf-meta-extensions", "2017-03-08",
                        classLoader.getResource("tailf-meta-extensions.yang"));
                registerSource("tailf-cli-extensions", "2017-08-23",
                        classLoader.getResource("tailf-cli-extensions.yang"));
            } catch (SchemaSourceException | IOException | YangSyntaxErrorException e) {
                e.printStackTrace();
            }
        }

        // Parse all available YANG models, if we fail remove failed model and retry with remaining models
        while (!requiredSources.isEmpty()) {
            try {
                schemaContext = factory.createSchemaContext(requiredSources).get();
                break;
            } catch (InterruptedException | ExecutionException f) {
                f.printStackTrace();
				if (f.getCause() instanceof SchemaResolutionException) {
                    SchemaResolutionException e = (SchemaResolutionException)f.getCause();
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
                }
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
	public ListenableFuture<? extends YangTextSchemaSource> getSource(SourceIdentifier sourceIdentifier) {
        YangTextSchemaSource source = sources.get(sourceIdentifier);
        return source != null ? Futures.immediateFuture(source) : Futures.immediateFailedFuture(
                new MissingSchemaSourceException("URL for " + sourceIdentifier + " not registered", sourceIdentifier));
	}
}
