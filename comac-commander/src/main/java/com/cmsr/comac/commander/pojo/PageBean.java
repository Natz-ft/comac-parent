package com.cmsr.comac.commander.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @param <T>
 * @author ztw
 */
public class PageBean<T> implements Serializable {
    /**
     * 当前页列表
     */
    private List<T> data;

    /**
     * 首页
     */
    private int firstPage = 1;

    /**
     * 上一页
     */
    private int prePage;

    /**
     * 当前页
     */
    private int pageNum;

    /**
     * 下一页
     */
    private int nextPage;

    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 总记录数
     */
    private int count;

    /**
     * 每页多少条
     */
    private int pageSize;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getPrePage() {
        prePage = this.pageNum - 1;
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int curPage) {
        this.pageNum = curPage;
    }

    public int getNextPage() {
        nextPage = this.pageNum + 1;
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotalPage() {
        totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public PageBean() {
    }

    public PageBean(List<T> data, int firstPage, int prePage, int pageNum, int nextPage, int totalPage, int count, int pageSize) {
        this.data = data;
        this.firstPage = firstPage;
        this.prePage = prePage;
        this.pageNum = pageNum;
        this.nextPage = nextPage;
        this.totalPage = totalPage;
        this.count = count;
        this.pageSize = pageSize;
    }
}
