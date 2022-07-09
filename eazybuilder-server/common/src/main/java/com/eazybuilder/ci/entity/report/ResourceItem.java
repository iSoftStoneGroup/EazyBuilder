package com.eazybuilder.ci.entity.report;

import java.io.InputStream;

public class ResourceItem {

	private String id;
	private String name;
	private byte[] data;
	private InputStream dataStream;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public InputStream getDataStream() {
		return dataStream;
	}
	public void setDataStream(InputStream dataStream) {
		this.dataStream = dataStream;
	}
}
