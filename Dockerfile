FROM jetty:alpine
LABEL maintainer "Steven Barth <stbarth@cisco.com>"
COPY . /src
USER root
RUN apk --no-cache add --virtual build-dependencies maven openjdk8 \
    && cd /src && mvn package javadoc:javadoc && cp /src/explorer/target/*.war /var/lib/jetty/webapps/ROOT.war \
    && cp -a /src/anc/target/site/apidocs /var/lib/jetty/webapps/ && cd / && rm -r /src /root/.m2 && apk del build-dependencies
EXPOSE 8080
