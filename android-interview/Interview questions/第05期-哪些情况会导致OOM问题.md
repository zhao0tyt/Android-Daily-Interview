## 第 05 期

#### Android的 java程序为什么容易出现OOM？
这个问题要涉及到android系统的设置方面了，主要因为Android系统对 dalvik
的 vm heapsize 作了硬性限制，当java进程申请的java空间超过阈值时，就会抛出OOM异常（这个阈值可以是48M、24M、16M等，视机型而定）。  
可以通过命令查看此值：  
`adb shell getprop | grep dalvik.vm.heapgrowthlimit`  
也可在代码中通过api来获取该数值  
`ActivityManager activityManager =(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE); activityManager.getMemoryClass();`

以上方法会返回以M为单位的数字，不同的系统平台或设备上的值都不太一样。
而查看一个进程的内存使用情况，可以通过dumpsys meminfo命令可以查看，如adb shell dumpsys meminfo com.test.wonder ,后面为应用的包名。

也就是说，程序发生OMM并不表示RAM不足，而是因为程序申请的 java heap 对象超过了 dalvik vm heapgrowthlimit 。所以即使在RAM充足的情况下，也可能发生OOM问题。
#### 哪些情况下会导致OOM问题？
1、过多的内存泄漏
2、加载大图
3、创建大量线程
> [**哪些情况下会导致OOM问题？**](https://github.com/Moosphan/Android-Daily-Interview/issues/5)
