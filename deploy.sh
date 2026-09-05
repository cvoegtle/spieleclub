#!/bin/bash
set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$DIR"

export JAVA_HOME="${JAVA_HOME:-/Users/cv/Library/Java/JavaVirtualMachines/temurin-17.0.15/Contents/Home}"
echo "Building project with Maven on Java 17..."
/Users/cv/Library/apache-maven-3.9.9/bin/mvn clean package

echo "Setting gcloud project..."
gcloud config set project spieleclub-paderborn-hdr

echo "Deploying to App Engine..."
gcloud app deploy target/spieleclub-1.0-SNAPSHOT/WEB-INF/appengine-web.xml "$@"