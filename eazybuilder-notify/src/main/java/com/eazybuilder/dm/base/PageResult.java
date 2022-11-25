package com.eazybuilder.dm.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class PageResult<T> {
	@SuppressWarnings("rawtypes")
	@JsonIgnore
	public static final PageResult EMPTY_RESULT=create(0, null);
	
	private long total=0;
	
    private List<T> rows;
    
    public static <T> PageResult<T> create(long total,List<T> rows){
    	PageResult<T> result=new PageResult<>();
    	result.setTotal(total);
    	result.setRows(rows==null?new ArrayList<T>():rows);
    	return result;
    }
    
    
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
    
    
}