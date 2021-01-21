package com.study.test.file;

import org.junit.Test;

import java.io.File;

/**
 * File 类的测试用例
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年01月21日
 */
public class FileTests01 {
    @Test
    public void test01() {
        String pathName = "C:\\Users\\Administrator\\Desktop\\bk_image";
        // 创建一个代表目录的 File 对象
        File src = new File(pathName);
        if (src.exists()) {
            // 判断传入的 File 对象是否存在
            File[] files = src.listFiles();// 得到 File 数组
            for (File file : files) {
                // 遍历所有的子目录和文件
                if (file.isFile()) {
                    String srcName = file.getName();
                    if (srcName.endsWith(".dat")) {
                        String newName = srcName.substring(0, srcName.indexOf(".dat")) + ".png";
                        boolean rename = file.renameTo(new File(pathName + "\\" + newName));
                        System.out.println("文件：" + file + " 重命名结果：" + rename);
                    }
                }
            }
        } else {
            System.out.println("路径 " + pathName + " 不存在！");
        }
    }
}
