package com.bjpowernode.crm.vo;

import java.util.List;

public class PaginationVO<T> {

    private int total;
    private List<T> dataList ;

    public void setTotal(int total) {
        this.total = total;
    }
    public void setDataList(List<T> dataList){
        this.dataList = dataList;
    }

    public int getTotal() {
        return total;
    }
    public List<T> getDataList(){
        return dataList;
    }
}
