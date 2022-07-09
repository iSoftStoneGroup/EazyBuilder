package com.eazybuilder.ci.util;

import org.apache.commons.lang3.StringUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public class PinyinUtil {

	
	public final static String getPinyin(String chineseChars) throws Exception{
		HanyuPinyinOutputFormat format=new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		
		return PinyinHelper.toHanYuPinyinString(chineseChars, format, "", false);
	}
	public final static String getPinyinFirstChar(String chineseChars) throws Exception{
		HanyuPinyinOutputFormat format=new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		
		String[] pinyin=PinyinHelper.toHanYuPinyinString(chineseChars+" ", format, "_", false).split("_");
		StringBuilder sb=new StringBuilder();
		for(String words:pinyin){
			if(StringUtils.isNotBlank(words)){
				sb.append(words.charAt(0));
			}
		}
		return sb.toString();
	}
}
