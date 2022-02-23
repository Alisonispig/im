package org.example.util;

public class TestUtil2 {

//    public static void main(String[] args) {
//        String uploadObjectUrl = MinIoUtils.getUploadObjectUrl("a.png");
//    }

    public static void main(String[] args) {
        MinIoUtils minIoUtils = new MinIoUtils();
        try {
            minIoUtils.init();
            System.out.println("连接完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
