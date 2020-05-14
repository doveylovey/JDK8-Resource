equals()和hashCode()区别及联系
============================

## Java 中对 equals() 和 hashCode() 的规定
- 同一对象多次调用 hashCode() 总是返回相同的整型值
- 若 a.equals(b) 为 true，则一定有 a.hashCode() 等于 b.hashCode()
- 若 a.equals(b) 为 false，则 a.hashCode() 不一定等于 b.hashCode()。若 a.hashCode() 总是不等于 b.hashCode()，则会提高 hashtable 的性能
- 若 a.hashCode() 等于 b.hashCode()，则 a.equals(b) 可能为 true 也可能为 false
- 若 a.hashCode() 不等于 b.hashCode()，则 a.equals(b) 为 false

## 关于 equals() 和 hashCode() 的重要规范
- 规范1：若重写了 equals(Object obj) 方法，则有必要重写 hashCode() 方法，以确保通过 equals(Object obj) 方法判断结果为 true 的两个对象具备相等的 hashCode() 返回值。即：如果两个对象相同，那它们的 hashCode 也应该相等。
- 【注意】这个只是规范，如果非要写一个类让 equals(Object obj) 返回 true 而 hashCode() 返回不相等的值，编译和运行都不会报错，不过这样就违反了 Java 规范，程序也就埋下了BUG。
- 规范2：如果 equals(Object obj) 返回 false，即两个对象"不相同"，并不要求这两个对象调用 hashCode() 得到两个不同的数。即：如果两个对象不相同，那它们的 hashCode 可能相同。 

## 为什么覆盖 equals() 时总要覆盖 hashCode()
一个很常见的错误根源在于没有覆盖 hashCode() 方法。在每个覆盖了 equals() 方法的类中，也必须覆盖 hashCode()，如果不这样做的话，就违反了 Object.hashCode() 的通用约定，从而导致该类无法结合所有基于散列的集合一起正常运作，这样的集合包括 HashMap、HashSet 和 Hashtable 等。
