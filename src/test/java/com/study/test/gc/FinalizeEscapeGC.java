package com.study.test.gc;

public class FinalizeEscapeGC {
    public static FinalizeEscapeGC SAVE_HOOK = null;

    /**
     * 当对象不可达时，也并不是就可以宣告这个对象死亡了，还有对象的最后一次自我救赎 —— finalize。
     * finalize 是一个方法名，Java 允许使用 finalize 方法在垃圾收集器将对象从内存中清除出去之前做必要的清理工作。
     * 在这个操作中如果对象被重新引用，对象就可以活过来。判断对象是否有必要执行该方法主要有以下两个依据：
     * 1、对象有没有覆盖 finalize 方法；
     * 2、对象已覆盖 finalize 方法，检查 finalize 方法是否被虚拟机调用过，如果已被调用，就不需要再次执行
     * 如果该对象有必要执行 finalize 方法，则该对象将被放置到 F-Queue 中，由虚拟机的单独线程 Finalizer 执行。
     * 注意：finalize 方法只能被执行一次，如果面临下一次回收，finalize 方法将不会被执行。
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("执行了 finalize() 方法 ……");
        FinalizeEscapeGC.SAVE_HOOK = this;
    }

    public static void main(String[] args) throws InterruptedException {
        SAVE_HOOK = new FinalizeEscapeGC();

        // 对象第一次成功拯救自己
        SAVE_HOOK = null;
        System.gc();
        // 因为 finalize() 优先级很低，所以暂停 0.5 秒等待它
        Thread.sleep(500);
        if (SAVE_HOOK != null) {
            System.out.println("对象仍然存活。");
        } else {
            System.out.println("对象死亡！！！");
        }

        // 下面这段代码与上面完全相同，却在自救中失败了
        // 因为一个对象的 finalize() 只会被系统自动调用一次，如果面临下一次回收，它的 finalize() 不会被再次执行，只有被回收。
        // 由于 finalize() 运行代价高昂，不确定性大，无法保证各个对象的调用顺序，因此不要使用
        SAVE_HOOK = null;
        System.gc();
        // 因为 finalize 优先级很低，所以暂停 0.5 秒等待它
        Thread.sleep(500);
        if (SAVE_HOOK != null) {
            System.out.println("对象仍然存活。");
        } else {
            System.out.println("对象死亡！！！");
        }
    }
}
