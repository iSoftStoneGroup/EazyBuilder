package com.eazybuilder.ci.ant;

import java.io.File;

import com.eazybuilder.ci.ant.util.CentralMavenSearchService;
import com.eazybuilder.ci.ant.util.NexusSearchService;

public class Main {

	public static void main(String[] args) {
		if(args==null||args.length<1){
			System.err.println("usage: java -jar legacy-ant-adapter.jar <ANT PROJECT PATH> <src path> <lib path> <charset>");
			System.exit(1);
		}
		String projectHome=args[0];
		File home=new File(projectHome);
		if(!home.exists()){
			System.err.println("given ant project home: "+projectHome+" not exist");
			System.exit(1);
		}
		try {
			String srcPath=null;
			String libPath=null;
			String charset=null;
			String jdk=null;
			if(args.length==3){
				srcPath=args[1];
				libPath=args[2];
			}
			if(args.length==4){
				srcPath=args[1];
				libPath=args[2];
				charset=args[3];
			}
			if(args.length==5){
				srcPath=args[1];
				libPath=args[2];
				charset=args[3];
				jdk=args[4];
			}
			
			AntProjectToMavenAdapter.getInstance().convertAntProject(home,srcPath,libPath,charset,jdk);
			System.exit(0);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
}
