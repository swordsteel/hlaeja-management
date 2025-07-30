#!/bin/bash

# Define variables for paths
TAILWIND_CLI="tailwindcss"
CONFIG_PATH="./tailwindcss/tailwind.config.js"
INPUT_CSS="./tailwindcss/input.css"
OUTPUT_CSS="./src/main/resources/static/css/tailwind.css"

# Check if watch mode is requested
if [ "$1" = "watch" ]; then
    echo "Running Tailwind CSS in watch mode..."
    $TAILWIND_CLI --config "$CONFIG_PATH" -i "$INPUT_CSS" -o "$OUTPUT_CSS" --watch
else
    echo "Building Tailwind CSS with minification..."
    $TAILWIND_CLI --config "$CONFIG_PATH" -i "$INPUT_CSS" -o "$OUTPUT_CSS" --minify
fi

# Check for errors
if [ $? -ne 0 ]; then
    echo "Error: Tailwind CSS build failed!"
    exit $?
fi

echo "Tailwind CSS build completed successfully!"
