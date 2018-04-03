#!/bin/sh
exec mvn -e -f explorer/pom.xml compile jetty:run
