FROM tomcat:9-jre8-alpine
LABEL maintainer "Steven Barth <stbarth@cisco.com>"

RUN apk --no-cache add --virtual .builddep maven openjdk8

COPY anc /src/anc/
COPY explorer /src/explorer/
COPY grpc /src/grpc/
COPY pom.xml /src/

RUN cd /src && mvn package javadoc:javadoc && cp /src/explorer/target/*.war /usr/local/tomcat/webapps/ROOT.war && \
    cp -a /src/anc/target/site/apidocs /usr/local/tomcat/webapps/ && mkdir /usr/local/yangcache && \
    rm -rf /usr/local/tomcat/webapps/ROOT && cd / && rm -r /src /root/.m2 && apk del .builddep

EXPOSE 8080
