#!/bin/sh
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
if command -v gradle >/dev/null 2>&1; then exec gradle "$@"; fi
echo "Gradle is not installed locally. Install Gradle 8.11.1 or use Android Studio to build this project." >&2
exit 1
