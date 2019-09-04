package com.zxk175.well.base.bean.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author zxk175
 * @since 2019/04/02 14:19
 */
@Data
@Accessors(chain = true)
public class PageBeanDto<T> implements Serializable {

    /**
     * 当前页
     */
    private Long page;
    /**
     * 每页记录数
     */
    private Long size;
    /**
     * 总页数
     */
    private Long pages;
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 数据列表
     */
    private T records;
    /**
     * 是否有上一页
     */
    private Boolean hasPre;
    /**
     * 是否有下一页
     */
    private Boolean hasNext;


    public Boolean getHasPre() {
        return this.page > 1;
    }

    public Boolean getHasNext() {
        return this.page < getPages();
    }

    public Long getPages() {
        long size = getSize();
        if (size == 0) {
            return 0L;
        }

        long pageTotal = getTotal() / size;
        if (getTotal() % size > 0) {
            pageTotal++;
        }

        return pageTotal;
    }
}
