#!/bin/bash
cd /data/
java -Xverify:none -XX:+HeapDumpOnOutOfMemoryError -Dtio.default.read.buffer.size=512 -XX:HeapDumpPath=./java-im-server-pid.hprof -cp ./:./lib/* org.example.ImServer