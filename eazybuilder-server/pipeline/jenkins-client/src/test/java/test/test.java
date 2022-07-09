package test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {

	public static void main(String[] args) {
		 
		
		String url="http://localhost:8080/ci/queue/item/123/";
	 
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

//		System.out.println("查询成功:{}"+url.substring(url.indexOf("/queue/item")));
//		System.out.println("查询成功:{}"+url.substring(url.lastIndexOf("queue/item")));
		String imageTag="TEST_2022022_202202261445";
   	 String newTag=imageTag.substring(0,imageTag.lastIndexOf("_"))+"_"+df.format(new Date());
   	 System.out.println(newTag);


	}

}
