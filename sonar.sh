#!/bin/bash

if ! docker container exec sonarqube echo 'SonarQube is ready'; then
    if ! docker container start sonarqube; then
        if ! docker run -d --name sonarqube -p 9000:9000 sonarqube:community; then
            exit 1
        fi
    fi
fi

echo "Starting analysis..."

./gradlew -Dsonar.host.url=http://localhost:9000 -Dsonar.verbose=true --stacktrace -Dsonar.login=sqa_1cd5674467eca6fb2e29d60e8911cbc00700a7dc sonar
