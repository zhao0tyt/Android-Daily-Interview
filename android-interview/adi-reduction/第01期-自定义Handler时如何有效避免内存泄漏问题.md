## 第 01 期

> [**自定义Handler时如何有效避免内存泄漏问题？**](https://github.com/Moosphan/Android-Daily-Interview/issues/1)

在 Android 开发过程中，**`Handler`** 是我们经常接触到的概念，它一般有两个主要作用：

- 推送未来某个时间点将要执行的 `Message` 或者 `Runnable` 到消息队列。
- 让某一个行为在其他线程中执行。

#### Handler 为什么会发生内存泄漏？

我们先来看一下 Handler 出现内存泄漏问题的一种场景：

```java
public class SampleActivity extends Activity {

  private final Handler mLeakyHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      // ...
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Post a message and delay its execution for 10 minutes.
    mLeakyHandler.postDelayed(new Runnable() {
      @Override
      public void run() { /* ... */ }
    }, 1000 * 60 * 10);

    // Go back to the previous Activity.
    finish();
  }
}
```

一般非静态内部类持有外部类的引用的情况下，造成外部类在使用完成后不能被系统回收内存，从而造成内存泄漏。这里 Handler 持有外部类 Activity 的引用，一旦 Activity 被销毁，而此时 Handler 依然持有 Activity 引用，而该handler实例可能被MessageQueue引用着,就会造成内存泄漏。我们常说的 Handler 内存泄漏，其实泄漏的通常是含生命周期的组件，如：Activity、Fragment等。我们在使用这些组件时，由于使用了**非静态内部类**，可能发生静态内部类的实例持有的对象生命周期大于其外部类（如 Activity），从而会导致内存泄漏问题。

#### 如何避免？

将 Handler 以静态内部类的形式声明，然后通过弱引用的方式让 Handler 持有外部类 Activity 的引用，这样就可以避免内存泄漏问题了：

```java
public class SampleActivity extends Activity {

  /**
   * Instances of static inner classes do not hold an implicit
   * reference to their outer class.
   */
  private static class MyHandler extends Handler {
    private final WeakReference<SampleActivity> mActivity;

    public MyHandler(SampleActivity activity) {
      mActivity = new WeakReference<SampleActivity>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
      SampleActivity activity = mActivity.get();
      if (activity != null) {
        // ...
      }
    }
  }

  private final MyHandler mHandler = new MyHandler(this);

  /**
   * Instances of anonymous classes do not hold an implicit
   * reference to their outer class when they are "static".
   */
  private static final Runnable sRunnable = new Runnable() {
      @Override
      public void run() { /* ... */ }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Post a message and delay its execution for 10 minutes.
    mHandler.postDelayed(sRunnable, 1000 * 60 * 10);

    // Go back to the previous Activity.
    finish();
  }
}
```

#### 其它解决方式？

由于本题考查的是自定义 Handler 的解决方式，同时这也是官方推荐的方式。当然，我们也可以通过组件的生命周期来及时清除正在进行的消息。
保证Activity被finish()时该线程的消息队列没有这个Activity的handler内部类的引用。一个补救的办法就是在该类需要回收的时候，手动地把消息队列中的消息清空：mHandler.removeCallbacksAndMessages(null);

```java
@override void onDestroy() {
        super.onDestroy();
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }
```

----------------------

#### 涉及知识点

- java中，非静态的内部类和匿名内部类都会隐式的持有一个外部类的引用。静态内部类则不会持有外部类的引用。

- | 强引用(Strong Reference)   | 默认引用。如果一个对象具有强引用，垃圾回收器绝不会回收它。在内存空 间不足时，Java虚拟机宁愿抛出OutOfMemory的错误，使程序异常终止，也不会强引用的对象来解决内存不足问题。 |
| -------------------------- | ------------------------------------------------------------ |
| 软引用（SoftReference）    | 如果内存空间足够，垃圾回收器就不会回收它，如果内存空间不足了，就会回收这些对象的内存。 |
| 弱引用（WeakReference      | 在垃圾回收器一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。 |
| 虚引用（PhantomReference） | 如果一个对象仅持有虚引用，那么它就和没有任何引用一样，在任何时候都可能被垃圾回收。 |

