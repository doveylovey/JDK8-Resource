package com.study.test.gc;

public class GcRootsTests {
    // 参考 https://www.cnblogs.com/rumenz/articles/14099927.html
    // 可作为 GC Roots 的对象包括以下几种：
    // 1、虚拟机栈(栈帧中的本地变量表)中引用的对象
    // 2、方法区中静态属性引用的对象
    // 3、方法去中常量引用的对象
    // 4、本地方法栈中(即一般所说的 Native 方法)引用的对象
}

class GcRoots01 {
    public static void main(String[] args) {
        // 虚拟机栈中引用的对象：a 是栈帧中的本地变量，a 就是 GC Root。
        // 由于 a = null，a 与 new GcRoots01() 对象断开了链接，所以对象会被回收
        GcRoots01 a = new GcRoots01();
        a = null;
    }
}

class GcRoots02 {
    // 方法区类的静态成员引用的对象：栈帧中的本地变量 a = null，由于 a 断开了与 GC Root 对象的联系，所以 a 对象会被回收。
    // 由于给 GcRoots02 的成员变量 r 赋值了变量的引用，并且成员变量 r 是静态的，所以 r 就是一个 GC Root 对象，所以 r 指向的对象不会被回收
    public static GcRoots02 r;

    public static void main(String[] args) {
        GcRoots02 a = new GcRoots02();
        a.r = new GcRoots02();
        a = null;
    }
}

class GcRoots03 {
    // 方法区常量引用的对象：常量 r 引用的对象不会因为 a 引用的对象的回收而被回收
    public static final GcRoots03 r = new GcRoots03();

    public static void main(String[] args) {
        GcRoots03 a = new GcRoots03();
        a = null;
    }
}

// 本地方法栈中 JNI 引用的对象：
// 本地方法就是非 Java 语言实现的方法，可能由 C 或 Python 等其他语言实现，Java 通过 JNI 来调用本地方法，而本地方法是以库文
// 件(在 Windows 平台上是 DLL 文件形式，在 Unix 机器上是 SO 文件形式)的形式存放的。通过调用本地的库文件的内部方法，
// 使 JAVA 可以实现和本地机器的紧密联系，调用系统级的各接口方法，当调用 Java 方法时，虚拟机会创建一个栈桢并压入 Java 栈，
// 而当它调用的是本地方法时，虚拟机会保持 Java 栈不变，不会在 Java 栈祯中压入新的祯，虚拟机只是简单地动态连接并直接调用指定的本地方法。
//JNIEXPORT void JNICALL Java_com_pecuyu_jnirefdemo_MainActivity_newStringNative(JNIEnv *env, jobject instance，jstring jmsg) {
//   ...
//   // 缓存 String 的 class
//   jclass jc = (*env)->FindClass(env, STRING_PATH);
//}

// 回收方法区：虽然永久代的垃圾回收效率极低，但是也会回收，主要回收两部分内容：废弃常量、无用的类。
// 废弃常量的回收和堆中的对象类似，也是看是否有引用。无用的类的回收需要满足以下3个条件：
// 1、该类所有的实例已被回收，也就是 Java 堆中不存在该类的任何实例
// 2、加载该类的 ClassLoader 已被回收
// 3、该类对应的 java.lang.Class 对象没有任何地方被引用，无法在任何地方通过反射访问该类的任何实例
// 即使满足条件也只是可被回收，并不像对象一定会被回收，是否对类进行回收虚拟机有自己的参数控制。在大量使用反射、动态代理、CGLib 等字节码框架、
// 动态生成 JSP 以及 OSGi 这类频繁自定义 ClassLoader 的场景都需要虚拟机具备自动卸载类的功能，以保证永久代不会溢出。