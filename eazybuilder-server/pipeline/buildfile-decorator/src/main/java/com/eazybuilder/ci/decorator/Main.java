package com.eazybuilder.ci.decorator;

import com.eazybuilder.ci.maven.POMProcessor;
import com.eazybuilder.ci.npm.NodeProjectProcessor;

public class Main {

	public static void main(String[] args) {
		if(args!=null&&args.length==2){
			String type=args[0];
			String workspace=args[1];
			try {
				switch(type){
				case "maven":
					POMProcessor.getInstance().processPOM(workspace);
					System.out.println(Error.SUCCESS);
					return;
				case "node":
					new NodeProjectProcessor().process(workspace);
					System.out.println(Error.SUCCESS);
					return;
				case "ant":
				default:
					System.out.println(Error.ILLEGAL_PARAM);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(Error.INTERNAL_ERROR);
				return;
			}
			
		}else{
			System.out.println(Error.ILLEGAL_PARAM);
			return;
		}
	}
}
