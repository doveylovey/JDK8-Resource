package com.study.test.temp;

import lombok.Data;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * transient 关键字的作用。https://blog.csdn.net/lovelichao12/article/details/74726213
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年02月23日
 */
public class TransientTests {
    @Test
    public void test01() {
        // 一个对象只要实现了 java.io.Serializable 接口，那么这个对象就可以被序列化，java 的这种序列化模式为开发者提供了很多便利，
        // 开发者可以不必关系具体序列化的过程，只要这个类实现了 java.io.Serializable 接口，这个类的所有属性和方法都会自动序列化。
        // 然而在实际开发过程中会遇到这样的问题：这个类的有些属性需要序列化，而其他属性不需要被序列化。
        // 打个比方，如果一个用户有一些敏感信息(如：密码，银行卡号等)，为了安全起见，不希望在网络操作(主要涉及到序列化操作，本地序列化缓存也适用)中被传输，
        // 这些信息对应的变量就可以加上 transient 关键字。换句话说，这个字段的生命周期仅存于调用者的内存中而不会写到磁盘里持久化。

        // 总之，java 中的 transient 关键字为开发者提供了便利，只需要实现 java.io.Serializable 接口，将不需要序列化的属性前添加关键字 transient，
        // 序列化对象的时候，这个属性就不会序列化到指定的目的地中。
        User user = new User();
        user.setUsername("测试用户名");
        user.setPasswd("测试密码");
        System.out.println("read before Serializable: ");
        System.out.println("username: " + user.getUsername());
        System.err.println("password: " + user.getPasswd());
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("d:/user.txt"));
            os.writeObject(user); // 将User对象写进文件
            os.flush();
            os.close();

            ObjectInputStream is = new ObjectInputStream(new FileInputStream("d:/user.txt"));
            user = (User) is.readObject(); // 从流中读取User的数据
            is.close();
            System.out.println("read after Serializable: ");
            System.out.println("username: " + user.getUsername());
            System.err.println("password: " + user.getPasswd());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

@Data
class User implements Serializable {
    private static final long serialVersionUID = -7263827939447425124L;

    private String username;
    private transient String passwd;
}