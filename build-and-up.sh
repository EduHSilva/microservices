#!/bin/bash

set -e

cd "$(dirname "$0")"

echo "Building images and starting containers..."
docker compose up --build
