# 2DoList
# 1.概要
## 1.1 项目介绍
面对时间，有的人怨天尤人，老是抱怨时间不够用；有的人却能轻松驾驭时间，取得生活和事业上的成功。对于我们每个人而言，同样是每天24小时，为什么有的人安排得井然有序，应对自如；而有的人从早忙到晚，废寝忘食，却依然有忙不完的事情。究其原因，效率是其中的一个重要因素，而效率的背后确是时间利用的差异，也就是时间管理的能力。
- 项目背景：普遍工作效率低下问题
- 针对对象：自控力差、效率低的用户
- 项目目的：提高用户效率
- 项目组成：硬件端+服务器+软件客户端（Android + iOS)
- 职责担任：担任Android开发成员
## 1.2 客户端功能介绍
1. 登录注册
2. 清单设置：需要设置的内容有（事件的标题、事件的具体内容、是否使用番茄工作法（若使用则需要填写开始时间与结束时间，因为这样才能使用工作法））
3. 到时提醒功能
4. 添加好友
5. 会议组功能：用户可以通过创建一个会议组并将自己的好友添加到该会议组中，会议组创建成功后，会议组所有者可以向会议组中的成员发送任务，该任务也将会以任务清单的形式添加到会议组其他成员中。
# 2.功能展示
## 添加事件&好友&会议组
<img src="https://upload-images.jianshu.io/upload_images/2536154-730aa96c028a70c7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240" title="会议组" width="250px" > <img src="https://upload-images.jianshu.io/upload_images/2536154-b135b7e9d02471dd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240" title="添加事件" width="250px" > <img src="https://upload-images.jianshu.io/upload_images/2536154-15ac6e39d8bd2822.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240" title="好友" width="250px" >
# 3 开发环境及技术支持
## 3.1 开发环境及运行平台
- Android Studio 2.3.3
- JDK 1.8,java语言开发
## 3.1.2 运行环境
- Android平台系列手机
- minSDK>=18
## 3.2 技术支持
1. 界面设计
    * 遵从Google Material Design设计。
2. 网络数据交互
    * OkHttp3：网络请求的优秀开源框架
    * Retrofit2+RxJava：简化网络请求API与主子线程调度
3. 本地数据存储
    * SharedPreference：Android 自带简单本地存储API。
    * GreenDao：轻量高效数据库。
4. 代码解耦
    * MVP设计模式：业务分为3个层次，M-Model，V-View，P-presnter，通过p进行中转达到解耦。
    * Dagger2：依赖注入框架，减少模块之间的依赖
