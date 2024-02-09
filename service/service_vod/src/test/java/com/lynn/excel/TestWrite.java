package com.lynn.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestWrite {

    public static void main(String[] args) {
        //设置文件名称和路径
        String fileName = "F:\\Java\\项目\\硅谷课堂\\test.xlsx";

        //调用写方法
        EasyExcel.write(fileName,User.class)
                .sheet("写操作")
                .doWrite(data());

    }

    //循环设置要添加的数据，最终封装到list集合中
    private static List<User> data() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User data = new User();
            data.setId(i);
            data.setName("张三"+i);
            list.add(data);
        }
        return list;
    }
}
