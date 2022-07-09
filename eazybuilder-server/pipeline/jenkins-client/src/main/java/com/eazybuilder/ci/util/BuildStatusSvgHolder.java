package com.eazybuilder.ci.util;

public class BuildStatusSvgHolder {

	public static final String SVG_ABORTED="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"111.0\" height=\"20\">\n" + 
			"    <linearGradient id=\"a\" x2=\"0\" y2=\"100%\">\n" + 
			"        <stop offset=\"0\" stop-color=\"#bbb\" stop-opacity=\".1\"/>\n" + 
			"        <stop offset=\"1\" stop-opacity=\".1\"/>\n" + 
			"    </linearGradient>\n" + 
			"    <rect rx=\"3\" width=\"111.0\" height=\"20\" fill=\"#555\"/>\n" + 
			"    <rect rx=\"0\" x=\"47.0\" width=\"4\" height=\"20\" fill=\"#9f9f9f\"/>\n" + 
			"    <rect rx=\"3\" x=\"47.0\" width=\"64.0\" height=\"20\" fill=\"#9f9f9f\"/>\n" + 
			"    \n" + 
			"    <rect rx=\"3\" width=\"111.0\" height=\"20\" fill=\"url(#a)\"/>\n" + 
			"    <g fill=\"#fff\" text-anchor=\"middle\" font-family=\"DejaVu Sans,Verdana,Geneva,sans-serif\" font-size=\"11\">\n" + 
			"        <text x=\"24.5\" y=\"15\" fill=\"#010101\" fill-opacity=\".3\">build</text>\n" + 
			"        <text x=\"24.5\" y=\"14\">build</text>\n" + 
			"        <text x=\"78.0\" y=\"15\" fill=\"#010101\" fill-opacity=\".3\">aborted</text>\n" + 
			"        <text x=\"78.0\" y=\"14\">aborted</text>\n" + 
			"    </g>\n" + 
			"</svg>\n" + 
			"";
	
	public static final String SVG_FAILING="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"101.0\" height=\"20\">\n" + 
			"    <linearGradient id=\"a\" x2=\"0\" y2=\"100%\">\n" + 
			"        <stop offset=\"0\" stop-color=\"#bbb\" stop-opacity=\".1\"/>\n" + 
			"        <stop offset=\"1\" stop-opacity=\".1\"/>\n" + 
			"    </linearGradient>\n" + 
			"    <rect rx=\"3\" width=\"101.0\" height=\"20\" fill=\"#555\"/>\n" + 
			"    <rect rx=\"0\" x=\"47.0\" width=\"4\" height=\"20\" fill=\"#e05d44\"/>\n" + 
			"    <rect rx=\"3\" x=\"47.0\" width=\"54.0\" height=\"20\" fill=\"#e05d44\"/>\n" + 
			"    \n" + 
			"    <rect rx=\"3\" width=\"101.0\" height=\"20\" fill=\"url(#a)\"/>\n" + 
			"    <g fill=\"#fff\" text-anchor=\"middle\" font-family=\"DejaVu Sans,Verdana,Geneva,sans-serif\" font-size=\"11\">\n" + 
			"        <text x=\"24.5\" y=\"15\" fill=\"#010101\" fill-opacity=\".3\">build</text>\n" + 
			"        <text x=\"24.5\" y=\"14\">build</text>\n" + 
			"        <text x=\"73.0\" y=\"15\" fill=\"#010101\" fill-opacity=\".3\">failing</text>\n" + 
			"        <text x=\"73.0\" y=\"14\">failing</text>\n" + 
			"    </g>\n" + 
			"</svg>";
	public static final String SVG_NOTRUN="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"108.0\" height=\"20\">\n" + 
			"    <linearGradient id=\"a\" x2=\"0\" y2=\"100%\">\n" + 
			"        <stop offset=\"0\" stop-color=\"#bbb\" stop-opacity=\".1\"/>\n" + 
			"        <stop offset=\"1\" stop-opacity=\".1\"/>\n" + 
			"    </linearGradient>\n" + 
			"    <rect rx=\"3\" width=\"108.0\" height=\"20\" fill=\"#555\"/>\n" + 
			"    <rect rx=\"0\" x=\"47.0\" width=\"4\" height=\"20\" fill=\"#9f9f9f\"/>\n" + 
			"    <rect rx=\"3\" x=\"47.0\" width=\"61.0\" height=\"20\" fill=\"#9f9f9f\"/>\n" + 
			"    \n" + 
			"    <rect rx=\"3\" width=\"108.0\" height=\"20\" fill=\"url(#a)\"/>\n" + 
			"    <g fill=\"#fff\" text-anchor=\"middle\" font-family=\"DejaVu Sans,Verdana,Geneva,sans-serif\" font-size=\"11\">\n" + 
			"        <text x=\"24.5\" y=\"15\" fill=\"#010101\" fill-opacity=\".3\">build</text>\n" + 
			"        <text x=\"24.5\" y=\"14\">build</text>\n" + 
			"        <text x=\"76.5\" y=\"15\" fill=\"#010101\" fill-opacity=\".3\">not run</text>\n" + 
			"        <text x=\"76.5\" y=\"14\">not run</text>\n" + 
			"    </g>\n" + 
			"</svg>\n" + 
			"";
	public static final String SVG_PASSING="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"110.0\" height=\"20\">\n" + 
			"    <linearGradient id=\"a\" x2=\"0\" y2=\"100%\">\n" + 
			"        <stop offset=\"0\" stop-color=\"#bbb\" stop-opacity=\".1\"/>\n" + 
			"        <stop offset=\"1\" stop-opacity=\".1\"/>\n" + 
			"    </linearGradient>\n" + 
			"    <rect rx=\"3\" width=\"110.0\" height=\"20\" fill=\"#555\"/>\n" + 
			"    <rect rx=\"0\" x=\"47.0\" width=\"4\" height=\"20\" fill=\"#44cc11\"/>\n" + 
			"    <rect rx=\"3\" x=\"47.0\" width=\"63.0\" height=\"20\" fill=\"#44cc11\"/>\n" + 
			"    \n" + 
			"    <rect rx=\"3\" width=\"110.0\" height=\"20\" fill=\"url(#a)\"/>\n" + 
			"    <g fill=\"#fff\" text-anchor=\"middle\" font-family=\"DejaVu Sans,Verdana,Geneva,sans-serif\" font-size=\"11\">\n" + 
			"        <text x=\"24.5\" y=\"15\" fill=\"#010101\" fill-opacity=\".3\">build</text>\n" + 
			"        <text x=\"24.5\" y=\"14\">build</text>\n" + 
			"        <text x=\"77.5\" y=\"15\" fill=\"#010101\" fill-opacity=\".3\">passing</text>\n" + 
			"        <text x=\"77.5\" y=\"14\">passing</text>\n" + 
			"    </g>\n" + 
			"</svg>\n" + 
			"";
	public static final String SVG_RUNNING="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"114.0\" height=\"20\">\n" + 
			"    <linearGradient id=\"a\" x2=\"0\" y2=\"100%\">\n" + 
			"        <stop offset=\"0\" stop-color=\"#bbb\" stop-opacity=\".1\"/>\n" + 
			"        <stop offset=\"1\" stop-opacity=\".1\"/>\n" + 
			"    </linearGradient>\n" + 
			"    <rect rx=\"3\" width=\"114.0\" height=\"20\" fill=\"#555\"/>\n" + 
			"    <rect rx=\"0\" x=\"47.0\" width=\"4\" height=\"20\" fill=\"#9f9f9f\"/>\n" + 
			"    <rect rx=\"3\" x=\"47.0\" width=\"67.0\" height=\"20\" fill=\"#9f9f9f\"/>\n" + 
			"    <rect rx=\"0\" x=\"47.0\" width=\"63.0\" height=\"20\" fill=\"#007ec6\"/>\n" + 
			"    <rect rx=\"0\" x=\"47.0\" width=\"63.0\" height=\"20\" fill=\"black\" fill-opacity=\"0.0\">\n" + 
			"        <animate attributeName=\"fill-opacity\" values=\"0.0;0.5;0.0\" dur=\"2s\" repeatCount=\"indefinite\" />\n" + 
			"    </rect>\n" + 
			"\n" + 
			"    <rect rx=\"3\" width=\"114.0\" height=\"20\" fill=\"url(#a)\"/>\n" + 
			"    <g fill=\"#fff\" text-anchor=\"middle\" font-family=\"DejaVu Sans,Verdana,Geneva,sans-serif\" font-size=\"11\">\n" + 
			"        <text x=\"24.5\" y=\"15\" fill=\"#010101\" fill-opacity=\".3\">build</text>\n" + 
			"        <text x=\"24.5\" y=\"14\">build</text>\n" + 
			"        <text x=\"79.5\" y=\"15\" fill=\"#010101\" fill-opacity=\".3\">running</text>\n" + 
			"        <text x=\"79.5\" y=\"14\">running</text>\n" + 
			"    </g>\n" + 
			"</svg>\n" + 
			"";
}
