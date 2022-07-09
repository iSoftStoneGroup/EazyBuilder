package com.eazybuilder.ci.dto;

import java.util.List;

public class Response {
	private int numFound;
	private List<Doc> docs;
	
	public int getNumFound() {
		return numFound;
	}
	public void setNumFound(int numFound) {
		this.numFound = numFound;
	}
	public List<Doc> getDocs() {
		return docs;
	}
	public void setDocs(List<Doc> docs) {
		this.docs = docs;
	}
}
