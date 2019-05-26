package com.zxk175.well.common.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zxk175
 * @since 2019/04/09 18:02
 */

public class MyExcelListener<T> extends AnalysisEventListener<T> {

    private List<T> dataList = new ArrayList<>();


    @Override
    public void invoke(T object, AnalysisContext context) {
        dataList.add(object);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 解析结束销毁不用的资源
    }

    public List<T> getDataList() {
        return dataList;
    }
}
