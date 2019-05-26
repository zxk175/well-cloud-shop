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
public class SysUserModel extends BaseRowModel implements Serializable {

    @ExcelProperty(index = 0)
    private String userName;

    @ExcelProperty(index = 2)
    private String mobile;

    @ExcelProperty(index = 3)
    private String password;

    @ExcelProperty(index = 4)
    private Integer identity;

    @ExcelProperty(index = 5)
    private Integer state;
}
