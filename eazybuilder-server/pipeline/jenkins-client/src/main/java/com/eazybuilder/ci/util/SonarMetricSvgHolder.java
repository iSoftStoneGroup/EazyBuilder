package com.eazybuilder.ci.util;

public class SonarMetricSvgHolder {

	private String val;
	private String type;

	public SonarMetricSvgHolder(String val, String type) {
		this.val = val;
		this.type = type;
	}

	public String getVal() {
		return val;
	}

	public String getType() {
		return type;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSONAR_BADGES() {
		int center_deviation;
		int center_space;
		int right_deviation;
		int right_space;
		int typeLength = this.type.length();
		int valLength = this.val.length();
		if( typeLength <= 5){
			center_deviation = 34;
			center_space = 26;
		}else if(typeLength == 6){
			center_deviation = 30;
			center_space = 34;
		}else if(typeLength == 7){
			center_deviation = 30;
			center_space = 38;
		}else if(typeLength == 8){
			center_deviation = 26;
			center_space = 40;
		}else{
			center_deviation = 22;
			center_space = 50;
		}
		switch (valLength){
			case 1:
				right_deviation = 84;
				right_space = 8;
				break;
			case 2:
				right_deviation = 80;
				right_space = 10;
				break;
			case 3:
				right_deviation = 78;
				right_space = 15;
				break;
			default:
				right_deviation = 76;
				right_space = 20;
		}
		SONAR_BADGES = "<svg xmlns=\"http://www.w3.org/2000/svg\" height=\"20\" width=\"100\">\r\n"
				+ "    <!-- SONARQUBE MEASURE -->\r\n" + "    <linearGradient id=\"b\" x2=\"0\" y2=\"100%\">\r\n"
				+ "        <stop offset=\"0\" stop-color=\"#bbb\" stop-opacity=\".1\"/>\r\n"
				+ "        <stop offset=\"1\" stop-opacity=\".1\"/>\r\n" + "    </linearGradient>\r\n"
				+ "    <clipPath id=\"a\">\r\n"
				+ "        <rect width=\"100\" height=\"20\" rx=\"3\" fill=\"#fff\"/>\r\n" + "    </clipPath>\r\n"
				+ "    <g clip-path=\"url(#a)\">\r\n" + "        <rect fill=\"#555\" height=\"20\" width=\"80\"/>\r\n"
				+ "        <rect fill=\"#999999\" height=\"20\" width=\"26\" x=\"74\"/>\r\n"
				+ "        <rect fill=\"url(#b)\" height=\"20\" width=\"100\"/>\r\n" + "    </g>\r\n"
				+ "    <g fill=\"#fff\" font-family=\"DejaVu Sans,Verdana,Geneva,sans-serif\" font-size=\"7\" text-anchor=\"left\">\r\n"
				+ "        <text x=\"26\" y=\"15\" textLength=\"50\" fill=\"#010101\" fill-opacity=\".3\"></text>\r\n"
				+ "        <text x=\""+center_deviation+"\" y=\"14\" textLength=\""+center_space+"\">" + this.type + "</text>\r\n"
				+ "        <text x=\"82\" y=\"15\" textLength=\"33\" fill=\"#010101\" fill-opacity=\".3\">"
				+ "</text>\r\n" + "        <text x=\""+right_deviation+"\" y=\"14\" textLength=\""+right_space+"\">" + this.val + "</text>\r\n"
				+ "    </g>\r\n" + "    <g fill=\"#4e9bcd\">\r\n"
				+ "        <path d=\"M17.975 16.758h-.815c0-6.557-5.411-11.893-12.062-11.893v-.813c7.102 0 12.877 5.7 12.877 12.706z\"/>\r\n"
				+ "        <path d=\"M18.538 12.386c-.978-4.116-4.311-7.55-8.49-8.748l.186-.65c4.411 1.266 7.93 4.895 8.964 9.243zm.626-3.856a12.48 12.48 0 0 0-4.832-5.399l.282-.464a13.031 13.031 0 0 1 5.044 5.63z\"/>\r\n"
				+ "    </g>\r\n" + "</svg>";

		return SONAR_BADGES;
	}

	public void setSONAR_BADGES(String sONAR_BADGES) {
		SONAR_BADGES = sONAR_BADGES;
	}

	public String SONAR_BADGES;

}
