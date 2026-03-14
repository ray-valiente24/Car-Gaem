#!/bin/bash

clear
echo "================================="
echo "  Car Gaem 26.1 - Unix Launcher"
echo "================================="
echo

# Check for javac
if ! command -v javac >/dev/null 2>&1; then
    echo "JDK not found!"
    echo
    echo "Debian/Ubuntu: sudo apt install default-jdk"
    echo "Arch Linux: sudo pacman -S jdk-openjdk"
    echo "Fedora: sudo dnf install java-latest-openjdk-devel"
    echo "macOS (Homebrew): brew install openjdk"
    echo
    exit 1
fi

# Create bin folder if missing
[ ! -d "bin" ] && mkdir bin

echo "Compiling Java source..."
echo

javac -d bin src/main/*.java

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo
echo "Compilation successful!"
echo "Launching game..."
echo

java -cp bin main.Main