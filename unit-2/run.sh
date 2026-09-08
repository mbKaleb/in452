#!/bin/zsh
# Compile and run the MovieLens Database Explorer.
set -e
cd "$(dirname "$0")"

CP="lib/mssql-jdbc-12.8.1.jre11.jar"
BUILD="build"

mkdir -p "$BUILD"
javac -d "$BUILD" -cp "$CP" MovieLensDB.java MovieLensGUI.java
java -cp "$BUILD:$CP" MovieLensGUI
