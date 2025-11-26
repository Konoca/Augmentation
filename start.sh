#!/bin/sh

mvn clean install
mvn exec:java -Dexec.mainClass="com.konoca.App"
