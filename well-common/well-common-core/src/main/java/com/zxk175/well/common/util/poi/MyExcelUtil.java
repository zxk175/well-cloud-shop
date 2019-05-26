package com.zxk175.well.common.util.poi;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.zxk175.well.common.excel.MyExcelListener;

import java.io.InputStream;
import java.util.List;

/**
 * @author zxk175
 * @since 2019/04/09 18:02
 */

public class MyExcelUtil<T> {

    public List<T> readExcel(InputStream inputStream, Class<? extends BaseRowModel> clazz) {
        MyExcelListener<T> listener = new MyExcelListener<>();
        ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX, listener);
        excelReader.read(new Sheet(1, 1, clazz));

        return listener.getDataList();
    }
}
