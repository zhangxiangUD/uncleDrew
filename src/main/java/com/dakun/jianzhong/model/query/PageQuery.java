package com.dakun.jianzhong.model.query;

/**
 * <p>User: wangjie
 * <p>Date: 9/28/2017
 */
public class PageQuery {

    public final static String PAGE_FIELD = "page";
    public static final String SIZE_FIELD = "size";

    private Integer page;

    private Integer size;

    public boolean isPaging() {
        return page != null && size != null;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
