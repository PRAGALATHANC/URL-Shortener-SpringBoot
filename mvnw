#!/bin/sh
set -e
BASE_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
MAVEN_VERSION="3.9.11"
MAVEN_HOME="$BASE_DIR/.mvn/apache-maven-$MAVEN_VERSION"
if [ ! -x "$MAVEN_HOME/bin/mvn" ]; then
  ARCHIVE="$BASE_DIR/.mvn/apache-maven-$MAVEN_VERSION-bin.zip"
  URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.zip"
  echo "Maven $MAVEN_VERSION is not installed locally. Downloading it..."
  if command -v curl >/dev/null 2>&1; then
    curl -fL "$URL" -o "$ARCHIVE"
  elif command -v wget >/dev/null 2>&1; then
    wget -O "$ARCHIVE" "$URL"
  else
    echo "Error: curl or wget is required to download Maven." >&2
    exit 1
  fi
  command -v unzip >/dev/null 2>&1 || { echo "Error: unzip is required." >&2; exit 1; }
  unzip -q "$ARCHIVE" -d "$BASE_DIR/.mvn"
  rm -f "$ARCHIVE"
fi
exec "$MAVEN_HOME/bin/mvn" -f "$BASE_DIR/pom.xml" "$@"
