FROM openjdk:13
MAINTAINER 473302042@qq.com
ADD im-server/target/im-server-jar-with-dependencies.jar /
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
EXPOSE 8088 9326
ENTRYPOINT ["sh","/startup.sh"]