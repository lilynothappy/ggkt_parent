package com.lynn.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestRead {

    public static void main(String[] args) {
        //设置文件名称和路径
        String fileName = "F:\\Java\\项目\\硅谷课堂\\test.xlsx";

        //调用读方法
        EasyExcel.read(fileName,User.class,new ExcelListener())
                .sheet().doRead();
        //sheet默认读一个
    }

}
