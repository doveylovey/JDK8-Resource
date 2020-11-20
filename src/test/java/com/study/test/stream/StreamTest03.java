package com.study.test.stream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class StreamTest03 {
    private static List<User> users;

    static {
        users = Arrays.asList(
                new User("张三", 1000),
                new User("张三", 1100),
                new User("张三", 1200),
                new User("李四", 1000),
                new User("李四", 1100),
                new User("王五", 2500),
                new User("赵六", 1800)
        );
    }

    public static void removeDuplicateUser(List<User> users) {
        System.out.println("=====>>>>>传统方式去重……");
        Set<User> set = new TreeSet<>(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getUsername().compareTo(o2.getUsername());
            }
        });
        set.addAll(users);
        new ArrayList<>(set).forEach(System.out::println);
    }

    public static void removeDuplicateUserByLambda(List<User> users) {
        System.out.println("=====>>>>>Lambda 方式去重……");
        Set<User> set = new TreeSet<>((user1, user2) -> user1.getUsername().compareTo(user2.getUsername()));
        set.addAll(users);
        new ArrayList<>(set).forEach(System.out::println);
    }

    public static void removeDuplicateUserByLambda2(List<User> users) {
        System.out.println("=====>>>>>Lambda 方式去重2……");
        Set<User> set = new TreeSet<>(Comparator.comparing(User::getUsername));
        set.addAll(users);
        new ArrayList<>(set).forEach(System.out::println);
    }

    @Test
    public void test01() {
        removeDuplicateUser(users);
        removeDuplicateUserByLambda(users);
        removeDuplicateUserByLambda2(users);
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class User {
    private String username;
    private int money;
}