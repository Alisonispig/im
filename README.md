##  信使  简洁轻量的即时通讯工具

### 😯 演示地址

文档地址: [https://www.o0o0oo.com/](https://www.o0o0oo.com/)

演示地址: [https://chat.o0o0oo.com/](https://chat.o0o0oo.com/)

前端地址：[https://gitee.com/LiLongLong719/im-web](https://gitee.com/LiLongLong719/im-web)

演示账号 a/a 仅演示目的.服务器的内容不定期清理(作者叫礼貌,可以找我聊天哦)

### 🏞️ 展示截图

![功能展示](doc/20220722202239.jpg)

### 🤩 核心目标: 完成单机10万+的可用项目

注意: 早期的思路和内容来自J-IM
1. 单机十万+的目标来自Tio的性能测试,据称可以实现单机百万连接,我寻思着我十万应该问题不大
2. 从头开始,没有使用J-IM的代码更有掌控力,无论代码好与坏,并且在这个过程中遇到的问题和累积的经验更有价值.(主要是J-IM的代码有些看不懂)
3. 前端的代码需要有一个与之匹配的后端,否则的话可能是一种灾难

### 🤦‍ 技术栈

核心Tio,包括http和socket都是tio.没有引入spring系列，所以大部分的内容都需要自己封装，好处是启动快，体积小。 弊端就是方方面面都需要自己考虑。

从登录开始所有的交互全部使用socket io，除minio分片上传使用了http外，目前没有其他使用http的地方。 

文件存储使用了minio. 一个标准的数据存储,很香

缓存和数据存储使用mangodb, 同时解决缓存和数据存储的问题（据说挺快的，没有做大量尝试）

### 🚗 使用

运行ImServer下Main方法

### 🎁 打包

运行 mvn clean package

### 🥶 安装教程

> 教程来自windows docker， linux同理
1. 安装minio
```
docker run -d -p 9000:9000 -p 9001:9001 --name minio -v /home/minio/data:/data -v /home/minio/cert:/root/.minio -e "MINIO_ROOT_USER=AKIAIOSFODNN7EXAMPLE" -e "MINIO_ROOT_PASSWORD=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY" --restart=always quay.io/minio/minio server /data --console-address ":9001"
```
2. 安装MongoDB
```
docker run --name mongo --restart=always -p 27017:27017 -v /home/mongodb:/data/db -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=密码  -d mongo:latest mongod --auth
```
3. 安装nginx， 作者使用nginxWebui
```
# Linux
docker run -itd -v /home/nginxWebUI:/home/nginxWebUI -e BOOT_OPTIONS="--server.port=8080" --privileged=true --restart=always --net=host cym1102/nginxwebui:latest
```
4. 配置minio https访问（此处以自签名为例）下载签名工具 [签名工具](https://github.com/minio/certgen) 
> 使用公网证书可忽略生成的步骤，直接讲证书放进去即可
```
# 172.17.0.3 是docker minio内部IP， 需要特别注意这个IP在映射出端口的情况下必须写
certgen-windows-amd64.exe -ecdsa-curve P256 -host 127.0.0.1,localhost,172.17.0.3,192.168.0.103
```
放置于抛出的 E:/docker/minio/cert 目录下，完整目录为 E:/docker/minio/cert/certs 重启容器
5. 配置JDK证书信任，否则会抛出下列错误
```
javax.net.ssl.SSLHandshakeException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
	at java.base/sun.security.ssl.Alert.createSSLException(Alert.java:131)
	at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:371)
	at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:314)
```
在JDK bin目录下运行
```
.\keytool -import -alias sslServer_03 -file D:\key\新建文件夹\public.crt -keystore ..\lib\security\cacerts -storepass changeit -v
```

### 🐧 特别鸣谢
[J-IM](https://gitee.com/xchao/j-im) 提供的似乎

### ⚒️ 交流
在使用过程中有任何问题和想法，请给我提 [Issue](https://gitee.com/LiLongLong719/im/issues)。
你也可以在Issue查看别人提的问题和给出解决方案。

可以通过演示系统中添加会话 礼貌 联系到我

想了解最新进展,可以通过微信与我沟通
![添加微信，备注信使](doc/2022年4月18日.jpg)
