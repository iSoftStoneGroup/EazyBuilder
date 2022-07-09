package com.eazybuilder.ci.entity.report;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Summary {
	private List<String> headers=Lists.newArrayList();
	private List<String[]> rows=Lists.newArrayList();
	private Map<String,String[]> dataMap=Maps.newHashMap();
	
	public static Summary create(){
		return new Summary();
	}
	
	public void addHeader(String header){
		headers.add(header);
	}
	
	public Summary headers(String...headers){
		for(String header:headers){
			this.headers.add(header);
		}
		return this;
	}
	
	public Summary addRow(String...row){
		this.rows.add(row);
		return this;
	}
	
	public List<String> getHeaders() {
		return headers;
	}
	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}
	public List<String[]> getRows() {
		return rows;
	}
	public void setRows(List<String[]> rows) {
		this.rows = rows;
	}

	public Map<String, String[]> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, String[]> dataMap) {
		this.dataMap = dataMap;
	}
	
	public void convertDataMap(){
		for(int i=0;i<headers.size();i++){
			List<String> cols=Lists.newArrayList();
			for(String[] row:rows){
				cols.add(row[i]);
			}
			dataMap.put(headers.get(i), cols.toArray(new String[0]));
		}
	}
}
