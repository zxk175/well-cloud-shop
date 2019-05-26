package com.zxk175.well.common.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashMap;

/**
 * @author zxk175
 * @since 2019/04/02 14:19
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class PageBeanDTO extends HashMap<String, Object> {

    // 是否有上一页
    private boolean hasPre;

    // 是否有下一页
    private boolean hasNext;

    // 当前页码
    private Long page;

    // 每页记录数
    private Long size;


    public boolean isHasPre() {
        return page > 1;
    }

    public boolean isHasNext() {
        return page < getPageTotal();
    }

    public Long getPageTotal() {
        Long size = this.size;
        if (size == 0) {
            return 0L;
        }

        Long total = (Long) get("total");
        long pageTotal = total / getSize();
        if (total % getSize() > 0) {
            pageTotal++;
        }

        return pageTotal;
    }
}
