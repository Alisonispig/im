#!/bin/bash
APP_ENTRY=org.example.ImServer
CP=.:/data/config:/data/lib/*
java -Xverify:none -XX:+HeapDumpOnOutOfMemoryError -Dtio.default.read.buffer.size=512 -XX:HeapDumpPath=./java-im-server-pid.hprof -cp "$CP" -Dglobal.config.path=/data/ $APP_ENTRY