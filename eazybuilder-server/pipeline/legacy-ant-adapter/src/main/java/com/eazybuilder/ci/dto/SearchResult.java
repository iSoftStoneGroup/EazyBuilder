package com.eazybuilder.ci.dto;

public class SearchResult {

	private ResponseHeader responseHeader;
	private Response response;
	
	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}
	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	
	public  Doc getDoc(){
		if(responseHeader.getStatus()!=0||response.getNumFound()!=1){
			return null;
		}
		return response.getDocs().get(0);
	}
}
