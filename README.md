##  信使  简洁的IM

### 🤩核心目标: 完成单机10W+的可用项目

注意: 大部分的思路和内容来自J-IM,有人会问为什么不直接使用J-IM,其一在于J-IM大部分更加偏向底层,而且对于我来讲,主要目的在于学习Tio和项目思路,或许这样更有成就感.其二
从头开始的代码更有掌控力,无论代码好与坏,并且在这个过程中遇到的问题和累积的经验更有价值.其三长期目标是实现带UI的界面.消息体和设计思路不相同会对前端实现有较大影响

### 🤦‍♀️技术栈

核心Tio,包括http和socket都是tio. 文件存储使用了minio, 缓存使用redis, 后续的想法是将redis换成mangodb, 同时解决缓存和数据存储的问题

### 😯演示地址

[演示地址](http://106.12.118.54:8888/#/)

用户名密码随意输入即可,可自动创建账号.仅演示目的.服务器的内容不定期清理

### 使用

运行ImServer下Main方法

### 打包

运行 mvn clean package
拷贝Target下 Im-Server目录
Windows下执行startup.bat方法,Linux执行startup.sh

### 🤣蓝图 
更新不及时访问 [在线文档](https://www.yuque.com/docs/share/8d1a4d1d-954d-478c-b23d-511d4558eed9)
![屏幕](https://images.gitee.com/uploads/images/2021/1023/200545_15db0dc8_1446263.png "屏幕截图.png")

### 🎉前端UI
目前有一些初略的想法,基于electron和web实现,已有一些雏形,但短期目标还是集中在实现功能上面

### 基础功能展示截图
![基础功能展示](https://images.gitee.com/uploads/images/2021/1023/172511_3aa221d9_1446263.png "屏幕截图.png")

### 交流群(交友投分，切磨箴视)
![QQ](https://images.gitee.com/uploads/images/2021/1011/000709_d88c0f1a_1446263.png "屏幕截图.png")