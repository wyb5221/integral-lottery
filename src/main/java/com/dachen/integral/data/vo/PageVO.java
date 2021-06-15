package com.dachen.integral.data.vo;

import java.util.List;

/**
 * Created by Administrator on 2017/3/8.
 */
public class PageVO<T> {

    // 总页数
    protected int pageCount;

    /**
     * 封装返回的业务数据
     */
    protected List<T> pageData;

    /**
     * 返回的页码
     */
    protected int pageIndex = 0;

    /**
     * 每页数据大小
     */
    protected int pageSize = 15;

    protected int start;

    /**
     * 总记录数
     */
    protected Long total = 0L;


    public PageVO() {
        super();
    }

    public PageVO(Integer pageIndex, Integer pageSize) {
        super();
        if (pageSize != null && pageSize >= 0) {
            this.pageSize = pageSize;
        }
        if (pageIndex != null && pageIndex >= 0) {
            this.pageIndex = pageIndex;
        }
    }

    public PageVO(List<T> pageData, Long total, Integer pageIndex,
                  Integer pageSize) {
        this.pageData = pageData;
        this.pageIndex = pageIndex;
        this.total = total;
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount = (int) (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
    }

    public List<T> getPageData() {
        return pageData;
    }

    public void setPageData(List<T> pageData) {
        this.pageData = pageData;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return start = (pageIndex * pageSize);
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }


}
