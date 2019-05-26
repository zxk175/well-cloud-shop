package com.zxk175.well.common.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author zxk175
 * @since 2019/04/09 17:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenuModel extends BaseRowModel implements Serializable {

    @ExcelProperty(index = 0)
    private String name;

    @ExcelProperty(index = 1)
    private String parentName;

    @ExcelProperty(index = 2)
    private String icon;

    @ExcelProperty(index = 3)
    private String url;

    @ExcelProperty(index = 4)
    private String perms;

    @ExcelProperty(index = 5)
    private String type;

    @ExcelProperty(index = 6)
    private String sort;

    @ExcelProperty(index = 7)
    private String state;
}
