package com.cly;

public class Test {

    public static void main(String[] args) {
        String path = "/log/api/code";
        System.out.println(path.matches("^/log/api/.*$"));
    }
}
