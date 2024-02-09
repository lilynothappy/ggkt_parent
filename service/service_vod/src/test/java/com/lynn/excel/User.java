package com.lynn.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class User {

    //设置表头名称
    @ExcelProperty(value = "用户编号",index = 0)
    private int id;

    //设置表头名称
    @ExcelProperty(value = "用户名称",index = 1)
    private String name;
}
