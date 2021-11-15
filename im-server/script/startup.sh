#!/bin/bash
java -XX:+HeapDumpOnOutOfMemoryError -Dtio.default.read.buffer.size=512 -XX:HeapDumpPath=./java-im-server-pid.hprof -Dglobal.config.path=/data -jar im-server-jar-with-dependencies.jar