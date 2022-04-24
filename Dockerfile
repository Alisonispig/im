FROM openjdk:13
MAINTAINER 473302042@qq.com
ADD im-server-jar-with-dependencies.jar im-server-jar-with-dependencies.jar
ADD public.crt public.crt
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' > /etc/timezone
RUN keytool -import -alias sslServer_03 -file public.crt -keystore /usr/java/openjdk-13/lib/security/cacerts -storepass changeit -v -noprompt
EXPOSE 8088 9326
ENTRYPOINT ["java","-XX:+HeapDumpOnOutOfMemoryError","-Dtio.default.read.buffer.size=512","-XX:HeapDumpPath=./java-im-server-pid.hprof","-DENV=prod","-jar","im-server-jar-with-dependencies.jar"]
