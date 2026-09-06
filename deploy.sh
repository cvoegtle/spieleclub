#!/bin/bash
set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$DIR"

PROJECT="${PROJECT:-spieleclub-paderborn-test}"

export JAVA_HOME="${JAVA_HOME:-/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home}"

echo "Building GWT client modules..."
CP_FILE="$(mktemp)"
/Users/cv/Library/apache-maven-3.9.9/bin/mvn -q dependency:build-classpath -Dmdep.outputFile="$CP_FILE"
java -cp "src:$(cat "$CP_FILE")" com.google.gwt.dev.Compiler -war web -style OBFUSCATED -sourceLevel 17 de.spieleclub.spieleclub_capture de.spieleclub.spieleclub_analysis
rm -f "$CP_FILE"

echo "Building WAR with Maven on Java 21..."
/Users/cv/Library/apache-maven-3.9.9/bin/mvn clean package

echo "Deploying to App Engine project: $PROJECT..."
gcloud app deploy target/spieleclub-1.0-SNAPSHOT/WEB-INF/appengine-web.xml --project="$PROJECT" "$@"