#!/bin/bash

set -e

cd "$(dirname "$0")"

echo "Building images and starting containers..."
#mvn dependency:purge-local-repository
docker compose up --build
