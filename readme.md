## 今日头条App
> 作为一个Web开发者，正好学校学习Android课程，所以跟着一起学习Android开发
，作为我个人来说，学习一门语言不是看多少的书，而是首先上网学习基础知识，来github学习前辈们的项目源码，
所以趁着这个机会，我开了一款类似今日头条的App，利用爬虫抓包实现一些数据的显示，整个过程让我了解到了android的网络请求
方面的诸多知识.

### 个人简介
- 个人网站:https://laravelchen.com/
- QQ群：296424369（Web开发交流群）

### 技术栈
- 使用Joup和Okhttp进行网络数据的爬取
- 使用Recyclerview和PageFragment和自定义数据类型进行新闻数据的显示
- 基于Retrofit和RxJava打的的链式网络库
- 支持rxJava链式操作
- 对于接口数据的获取我采用Novate框架，使用简单
- 对于用户的登录这块，按照标准流程应该是遵循OAuth2.0的流程，之后有时间进行更改
- 图片的显示使用ImageLoader
- 头像的显示使用CircleImageView
- 日期选择器使用的是仿ios的PickerView
- 弹窗的显示使用的是materialedittext
- 视频播放采用ijkplayer神器级播放器
- 整个界面的大部分控件使用的是Material Design设计规范

### 功能
- 爬取今日头条的实时数据
- 利用Recyclerview和PageFragment和自定义数据类型的结合进行显示数据
- 支持查看具体的列表内容，包括视频等
- 支持查看图片并且支持保存图片到本地图库(爬取图片的url是难点)
- 支持实时刷新获取数据
- 支持视频播放(爬取视频url非常难)
- 用户接口数据采用PHP写的
- 支持用户注册登录
- 用户收藏新闻
- 支持主题换肤
- 支持用户个人设置等
- 支持用户分享新闻，支持QQ,微信，QQ空间等
- 第三方登录(暂未开发)
- 上传头像(暂未开发)
- 全局搜索(可以利用爬虫进行搜索，暂未开发)

### 技术难点
你仔细看的话可能发现我在写图片和视频的时候都加上了比较难的字样，过程也确实如此，因为头条新闻的官网不像大部分的网站那样是将url直接
写在html中，因为这样主要太容易被爬取链接了，所以设计师将url全部通过script的形式写入html中，这样就容易被爬取到。但是上有政策，下
有对策，哈哈！我们可以通过网络请求将网页的所有前端代码爬取到，然后通过删选找寻自己想要的部分即可，相册的url的获取这样其实就可以了，
具体代码可以直接在项目源码中看到，但是对于视频的url可不这么轻松，在script中只有视频的video_id所以必须再次拼接api获取相应的video信息，
但是获取到之后，这个coding者真是太坑爹了！将视频的url加密的，我的天!不得不吐槽一下！幸好github上面有大神提前解决过此事，我便依葫芦画瓢
的将次加密过后的链接解码了，之后便可以播放视频了！那么，具体的代码请在项目源码中查看!有问题请在issue中提出！我会尽可能解决 ！

### 安装测试
#### 1.使用git进行下载项目
```
git clone https://github.com/LaravelChen/TouTiao.git
```
#### 2.根据你具体的android sdk的版本在gradle里面进行设置即可.


### 部分效果图:
![Alt text](https://github.com/LaravelChen/TouTiao/raw/master/image/home.gif)

![Alt text](https://github.com/LaravelChen/TouTiao/raw/master/image/photo.gif)

![Alt text](https://github.com/LaravelChen/TouTiao/raw/master/image/video.gif)

![Alt text](https://github.com/LaravelChen/TouTiao/raw/master/image/login.gif)

![Alt text](https://github.com/LaravelChen/TouTiao/raw/master/image/info.gif)

![Alt text](https://github.com/LaravelChen/TouTiao/raw/master/image/logout.gif)

> 最后如果您觉得对您的学习有帮助的话可以给一个star！谢谢！，有什么问题可以在issue中提出，我会及时回复!




