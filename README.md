##  信使  简洁的IM

### 😯演示地址

文档地址: [https://www.o0o0oo.com/](https://www.o0o0oo.com/)

演示地址: [https://chat.o0o0oo.com/](https://chat.o0o0oo.com/)

前端地址：[https://gitee.com/LiLongLong719/im-web](https://gitee.com/LiLongLong719/im-web)

演示账号 a/a 仅演示目的.服务器的内容不定期清理

### 展示截图

![功能展示](doc/20220418005013.jpg)

### 🤩核心目标: 完成单机10W+的可用项目

注意: 早期的思路和内容来自J-IM,有人会问为什么不直接使用J-IM,
1. 其一在于J-IM大部分更加偏向底层,而且对于我来讲,主要目的在于学习Tio和项目思路,或许这样更有成就感.
2. 其二 从头开始的代码更有掌控力,无论代码好与坏,并且在这个过程中遇到的问题和累积的经验更有价值.
3. 其三长期目标是实现带UI的界面.消息体和设计思路不相同会对前端实现有较大影响

### 🤦‍♀️技术栈

核心Tio,包括http和socket都是tio.没有引入spring系列，所以大部分的内容都需要自己封装，好处是启动快，体积小。弊端就是方方面面都需要自己考虑。从登录开始所有的交互全部使用socket，除minio分片上传使用了http外，目前没有其他使用http的地方。 文件存储使用了minio, 缓存使用mangodb, 同时解决缓存和数据存储的问题（据说挺快的，没有做大量尝试）

### 使用

运行ImServer下Main方法

### 打包

运行 mvn clean package
拷贝Target下 Im-Server目录
Windows下执行startup.bat方法,Linux执行startup.sh
 
### 🤣蓝图  
[在线文档](https://www.yuque.com/docs/share/8d1a4d1d-954d-478c-b23d-511d4558eed9)

### 🎉前端UI
目前有一些初略的想法,基于electron和web实现,已有一些雏形,但短期目标还是集中在实现功能上面

### 安装教程
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

# Windows
docker run -itd -p 8080:8080 -p 8888:8888 -v E:/docker/nginxWebUI:/home/nginxWebUI -e BOOT_OPTIONS="--server.port=8080" --privileged=true  cym1102/nginxwebui:latest
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
6. 配置nginx
```
# 参考配置如下
http {
  include mime.types;
  default_type application/octet-stream;
  keepalive_timeout 75s;
  gzip on;
  gzip_min_length 4k;
  gzip_comp_level 4;
  client_max_body_size 1024m;
  client_header_buffer_size 32k;
  client_body_buffer_size 8m;
  server_names_hash_bucket_size 512;
  proxy_headers_hash_max_size 51200;
  proxy_headers_hash_bucket_size 6400;
  gzip_types application/javascript application/x-javascript text/javascript text/css application/json application/xml;
  error_log /home/nginxWebUI/log/error.log;
  map $http_upgrade $connection_upgrade {
    default upgrade;
    '' close;
  }
  access_log /home/nginxWebUI/log/access.log;
  server {
    listen 8888 ssl http2;
    ssl_certificate /home/nginxWebUI/cert/public.crt;
    ssl_certificate_key /home/nginxWebUI/cert/private.key;
    ssl_protocols TLSv1 TLSv1.1 TLSv1.2 TLSv1.3;
    location / {
      proxy_pass http://192.168.0.103:8081;
      proxy_set_header Host $host;
      proxy_set_header X-Real-IP $remote_addr;
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_set_header X-Forwarded-Host $http_host;
      proxy_set_header X-Forwarded-Port $server_port;
      proxy_set_header X-Forwarded-Proto $scheme;
      proxy_http_version 1.1;
      proxy_set_header Upgrade $http_upgrade;
      proxy_set_header Connection "upgrade";
    }
    location /ws {
      proxy_pass http://192.168.0.103:9326;
      proxy_set_header Host $host;
      proxy_set_header X-Real-IP $remote_addr;
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_set_header X-Forwarded-Host $http_host;
      proxy_set_header X-Forwarded-Port $server_port;
      proxy_set_header X-Forwarded-Proto $scheme;
      proxy_http_version 1.1;
      proxy_set_header Upgrade $http_upgrade;
      proxy_set_header Connection "upgrade";
    }
    location /api/ {
      proxy_pass http://192.168.0.103:8088/;
      proxy_set_header Host $host;
      proxy_set_header X-Real-IP $remote_addr;
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_set_header X-Forwarded-Host $http_host;
      proxy_set_header X-Forwarded-Port $server_port;
      proxy_set_header X-Forwarded-Proto $scheme;
    }
    location /courier/ {
      proxy_pass https://192.168.0.103:9000/courier/;
      proxy_connect_timeout 300;
      proxy_http_version 1.1;
      proxy_set_header Connection "";
      chunked_transfer_encoding off;
      proxy_set_header X-Real-IP $remote_addr;
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_set_header X-Forwarded-Proto $scheme;
      proxy_set_header Host $http_host;
    }
  }
}

```

### 特别鸣谢
J-IM

### 交流

![添加微信，备注信使](doc/2022年4月18日.jpg)
