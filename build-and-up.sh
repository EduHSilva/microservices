#!/bin/bash

echo "🔧 Executando mvn clean install..."
cd service-discover
mvn clean install -DskipTests
cd ..
cd users
mvn clean install -DskipTests
cd ..
cd gateway
mvn clean install -DskipTests
cd ..
cd payments
mvn clean install -DskipTests
cd ..
cd email
mvn clean install -DskipTests
cd ..
cd crm
mvn clean install -DskipTests
cd ..

echo "🐳 Subindo containers..."
docker compose up --build