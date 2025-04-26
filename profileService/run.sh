#!/bin/bash

# Check if GITHUB_TOKEN is set
if [ -z "$GITHUB_TOKEN" ]; then
    echo "Error: GITHUB_TOKEN environment variable is not set"
    exit 1
fi

# Run the application
./mvnw spring-boot:run 