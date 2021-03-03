### GC 日志
GC 日志是一个很重要的工具，它准确记录了每一次 GC 的执行时间和执行结果，通过分析 GC 日志可以优化堆设置和 GC 设置，或者改进应用程序的对象分配模式。

JVM 的 GC 日志的主要参数包括如下几个：
- -XX:+PrintGC：输出 GC 日志
- -XX:+PrintGCDetails：输出 GC 的详细日志
- -XX:+PrintGCTimeStamps：输出 GC 的时间戳(以基准时间的形式)
- -XX:+PrintGCDateStamps：输出 GC 的时间戳(以日期的形式，如 2013-05-04T21:53:59.234+0800)
- -XX:+PrintHeapAtGC：在进行 GC 的前后打印出堆的信息
- -Xloggc:../logs/gc.log：日志文件的输出路径

### Java 虚拟机
- JVM总结之内存区域：https://www.cnblogs.com/aheizi/p/7104320.html
- Java虚拟机你只要看这一篇就够了：https://blog.csdn.net/qq_41701956/article/details/81664921


### Java 内存结构
- 完全图解JVM Class文件结构：https://segmentfault.com/a/1190000016011932
- Java class文件结构(规范篇)：https://www.jianshu.com/p/68520593b999
- 从一个class文件深入理解Java字节码结构：https://blog.csdn.net/u011810352/article/details/80316870
- 栈帧中局部变量表、操作数栈、动态链接、方法出口的理解：https://www.pianshen.com/article/22921622751/
- java虚拟机栈的内部结构(局部变量表、操作数栈、动态链接、方法返回地址)：https://blog.csdn.net/See_Csdn_/article/details/108972454
- Java虚拟机—栈帧、操作数栈和局部变量表：https://zhuanlan.zhihu.com/p/45354152


### Java 垃圾回收
查看 jvm 当前使用的垃圾收集器：java -XX:+PrintCommandLineFlags -version。
UseParallelGC 代表新生代使用的是 Parallel Scavenge 进行垃圾收集的，而老年代使用的是 Parallel Old 进行垃圾收集的。

- Java的垃圾收集器：https://www.cnblogs.com/yjc1605961523/p/12427658.html
- 垃圾收集算法：https://www.cnblogs.com/jing99/p/6071700.html
- JVM垃圾收集器详解：https://www.jb51.net/article/105581.htm
- 深入理解 Java垃圾收集器：https://blog.csdn.net/qq_36711757/article/details/80470820
- java垃圾收集器：https://blog.csdn.net/sunjin9418/article/details/79603651
- Java虚拟机垃圾回收(一)基础：回收哪些内存/对象、引用计数算法、可达性分析算法、finalize()方法、HotSpot实现分析：https://blog.csdn.net/tjiyu/article/details/53982412
- Java虚拟机垃圾回收(二)垃圾回收算法：标记-清除算法、复制算法、标记-整理算法、分代收集算法、火车算法：https://blog.csdn.net/tjiyu/article/details/53983064
- Java虚拟机垃圾回收(三)7种垃圾收集器：https://www.cnblogs.com/cxxjohnson/p/8625713.html
- 深入理解java垃圾回收机制：https://www.cnblogs.com/sunniest/p/4575144.html
- Java 垃圾回收机制原理：https://blog.csdn.net/wuzhixiu007/article/details/80116560
- JVM总结之GC：https://www.cnblogs.com/aheizi/p/7105732.html
- JVM总结之命令行工具：https://www.cnblogs.com/aheizi/p/7106972.html
- GC垃圾收集器详解（一）：https://my.oschina.net/u/4276852/blog/4181148
- 垃圾收集器：https://www.cnblogs.com/wxgblogs/p/5655534.html
- 垃圾收集器与内存分配策略：https://blog.csdn.net/wsyw126/article/details/62334387
- 《深入理解JVM》第三章 垃圾收集器与内存分配策略（垃圾收集器）：https://blog.csdn.net/qq_39148187/article/details/81811972
