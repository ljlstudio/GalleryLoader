# GalleryLoader
相册加载器、gallery、图片选择

1.基于RxJava 实现的相册加载器，能更好更快的加载本地大量相册图片并展示
2.内部提供相册展示页，也可以直接使用加载器回调数据供上层使用

# 集成步骤

**Step1:** 


项目根目录build文件中配置jitpack maven
```
 repositories {
        maven { url 'https://jitpack.io' }
    }
```


**Step2:**
app目录build文件中配置引用

```
     implementation 'com.github.ljlstudio:GalleryLoader:tag' ..tag为最新release 版本（1.0.2）
```
