rem -Xms64m -Xmx2048m

@echo off
setlocal & pushd
set APP_ENTRY=org.example.ImServer
set BASE=%~dp0
set CP=%BASE%\config;%BASE%\lib\*
java -Xverify:none -XX:+HeapDumpOnOutOfMemoryError -Dtio.default.read.buffer.size=512 -XX:HeapDumpPath=c:/java-im-server-pid.hprof -cp "%CP%" %APP_ENTRY%
endlocal & popd
