FROM debian:buster-slim
LABEL maintainer "Steven Barth <stbarth@cisco.com>"

COPY anc /src/anc/
COPY explorer /src/explorer/
COPY grpc /src/grpc/
COPY pom.xml /src/

ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
RUN mkdir -p /usr/share/man/man1 && apt update && \
    apt -y --no-install-recommends install openjdk-11-jdk-headless jetty9 maven && \
    cd /src && mvn package javadoc:javadoc && \
    cp /src/explorer/target/*.war /var/lib/jetty9/webapps/ROOT.war && \
    cp -a /src/anc/target/site/apidocs /var/lib/jetty9/webapps/ && mkdir /usr/share/yangcache && \
    rm -rf /var/lib/jetty9/webapps/root && cd / && rm -r /src /root/.m2 && \
    apt remove -y openjdk-11-jdk-headless maven && apt -y autoremove

WORKDIR /
EXPOSE 8080
CMD ["/usr/share/jetty9/bin/jetty.sh", "run"]
