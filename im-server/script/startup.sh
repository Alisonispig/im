#!/bin/bash
cd `dirname  $0`
APP_ENTRY=org.example.ImServer
CP=./:./lib/*
java -Xverify:none -XX:+HeapDumpOnOutOfMemoryError -Dtio.default.read.buffer.size=512 -XX:HeapDumpPath=./java-im-server-pid.hprof -Dglobal.config.path=/data/ -cp $CP  $APP_ENTRY