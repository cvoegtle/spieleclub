#!/bin/bash
set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$DIR"

PROJECT="${PROJECT:-spieleclub-paderborn-test}"

export JAVA_HOME="${JAVA_HOME:-/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home}"

echo "Building application with Maven (including GWT) on Java 21..."
/Users/cv/Library/apache-maven-3.9.9/bin/mvn clean package

echo "Deploying to App Engine project: $PROJECT..."
gcloud app deploy target/spieleclub-1.0-SNAPSHOT/WEB-INF/appengine-web.xml --project="$PROJECT" "$@"