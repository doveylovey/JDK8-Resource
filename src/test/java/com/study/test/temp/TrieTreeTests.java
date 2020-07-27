package com.study.test.temp;

import lombok.Data;
import lombok.ToString;

import java.util.LinkedList;

/**
 * 字典树测试
 */
public class TrieTreeTests {
    /**
     * 定义节点类
     */
    @Data
    @ToString
    static class TrieNode {
        /**
         * 子节点
         */
        private LinkedList<TrieNode> childrenList;
        /**
         * 节点数据
         */
        private char nodeData;
        /**
         * 频率
         */
        private int freq;
        /**
         * 是否叶子节点
         */
        private boolean leaf;

        /**
         * 更新节点频率
         *
         * @param step 数量：为正就增加，为负就减少
         */
        public void addFreq(int step) {
            this.freq += step;
        }

        public TrieNode(char nodeData) {
            this.childrenList = new LinkedList<>();
            this.nodeData = nodeData;
            this.freq = 0;
            this.leaf = false;
        }

        /**
         * 根据关键字查找节点信息
         *
         * @param key 关键字
         * @return 节点信息
         */
        public TrieNode childNode(char key) {
            if (null != childrenList) {
                for (TrieNode tn : childrenList) {
                    if (tn.getNodeData() == key) {
                        return tn;
                    }
                }
            }
            return null;
        }

        /**
         * 定义 Trie 树类
         */
        static class TrieTree {
            /**
             * 根节点
             */
            private TrieNode root;

            public TrieTree() {
                // 根节点初始为一个空字符
                this.root = new TrieNode(' ');
            }

            /**
             * 根据关键字查询节点是否存在
             *
             * @param word
             * @return
             */
            public boolean searchExist(String word) {
                TrieNode current = root;
                for (int i = 0; i < word.length(); i++) {
                    char charAt = word.charAt(i);
                    TrieNode child = current.childNode(charAt);
                    if (null == child) {
                        return false;
                    } else {
                        current = child;
                    }
                }
                if (current.isLeaf()) {
                    return true;
                } else {
                    return false;
                }
            }

            /**
             * 根据关键字查找节点
             *
             * @param word
             * @return
             */
            public TrieNode searchNode(String word) {
                TrieNode current = root;
                for (int i = 0; i < word.length(); i++) {
                    char charAt = word.charAt(i);
                    TrieNode child = current.childNode(charAt);
                    if (null == child) {
                        return null;
                    } else {
                        current = child;
                    }
                }
                if (current.isLeaf()) {
                    return current;
                } else {
                    return null;
                }
            }

            /**
             * 新增节点
             *
             * @param word
             * @return
             */
            public void insertNode(String word) {
                TrieNode current = root;
                for (int i = 0; i < word.length(); i++) {
                    char charAt = word.charAt(i);
                    TrieNode child = current.childNode(charAt);
                    if (null != child) {
                        current = child;
                    } else {
                        TrieNode trieNode = new TrieNode(charAt);
                        current.getChildrenList().add(trieNode);
                        current = current.childNode(charAt);
                    }
                    current.addFreq(1);
                }
                current.setLeaf(true);
            }

            /**
             * 删除节点
             *
             * @param word
             * @return
             */
            public void removeNode(String word) {
                if (!searchExist(word)) {
                    return;
                }
                TrieNode current = root;
                for (char charAt : word.toCharArray()) {
                    TrieNode trieNode = current.childNode(charAt);
                    if (trieNode.getFreq() == 1) {
                        current.getChildrenList().remove(trieNode);
                        return;
                    } else {
                        trieNode.addFreq(-1);
                        current = trieNode;
                    }
                }
                current.setLeaf(false);
            }

            /**
             * 获取关键字的词频
             *
             * @param word
             * @return
             */
            public int getFreq(String word) {
                TrieNode trieNode = searchNode(word);
                if (null != trieNode) {
                    return trieNode.getFreq();
                } else {
                    return 0;
                }
            }

            public static void main(String[] args) {
                String sentence = "today is good day what is you name at facebook how do you do what are you doing here";
                TrieTree trieTree = new TrieTree();
                for (String str : sentence.split(" ")) {
                    // 插入节点，构建 Trie 树
                    trieTree.insertNode(str);
                }
                // 打印 TrieTree
                System.out.println("当前 trieTree 为：" + trieTree);

                // 判断 facebook 是否存在
                System.out.println("判断 facebook 是否存在：" + trieTree.searchExist("facebook"));
                // 统计 do 的词频
                System.out.println("统计 do 的词频：" + trieTree.getFreq("do"));

                // 判断 today 是否存在
                System.out.println("移除 today 前：" + trieTree.searchExist("today"));
                // 移除 today
                trieTree.removeNode("today");
                // 判断 today 是否存在
                System.out.println("移除 today 后：" + trieTree.searchExist("today"));
            }
        }
    }
}
