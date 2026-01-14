#!/bin/sh

mvn clean package
cp target/augmentation*.jar .
