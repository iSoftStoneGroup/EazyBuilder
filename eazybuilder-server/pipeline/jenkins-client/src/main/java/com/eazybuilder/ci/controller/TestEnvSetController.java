package com.eazybuilder.ci.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.controller.vo.UFTEnv;
import com.eazybuilder.ci.entity.test.Env;
import com.eazybuilder.ci.entity.test.EnvSet;
import com.eazybuilder.ci.service.TestEnvSetService;

@RestController
@RequestMapping("/api/testEnvSet")
public class TestEnvSetController extends CRUDRestController<TestEnvSetService, EnvSet>{

	@RequestMapping(value="/import",method=RequestMethod.POST)
	public List<Env> importEnvExcel(@RequestParam("uploadfile")MultipartFile request) throws Exception{
		Workbook wb=getWorkbook(request);
		if(wb!=null){
			return parseFromExcel(wb);
		}else{
			return parseFromXml(request);
		}
		
	}

	private List<Env> parseFromXml(MultipartFile request) {
		if(request.getOriginalFilename().endsWith("xml")){
			try (InputStream is=request.getInputStream()){
				JAXBContext ctx=JAXBContext.newInstance(UFTEnv.class);
				UFTEnv env=(UFTEnv) ctx.createUnmarshaller().unmarshal(is);
				List<Env> envs=Lists.newArrayList();
				if(env!=null&&env.variable!=null){
					env.variable.forEach(ev->{
						envs.add(new Env(ev.getName(),ev.getValue()));
					});
				}
				return envs;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private List<Env> parseFromExcel(Workbook wb) {
		List<Env> envs=Lists.newArrayList();
		if(wb!=null){
			Sheet sheet=wb.getSheetAt(0);
			int rowNum=1;
			Row row=null;
			while((row=sheet.getRow(rowNum))!=null){
				Env env=new Env();
				env.setId(UUID.randomUUID().toString());
				env.setKey(getText(row.getCell(0)));
				env.setVal(getText(row.getCell(1)));
				envs.add(env);
				rowNum++;
			};
		}
		return envs;
	}

	private String getText(Cell cell) {
		switch(cell.getCellType()){
		case Cell.CELL_TYPE_NUMERIC:
			cell.setCellType(Cell.CELL_TYPE_STRING);
			String temp = cell.getStringCellValue();
			if (temp.indexOf(".") > -1) {
				return String.valueOf(new Double(temp)).trim();
			} else {
				return temp.trim();
			}
		case Cell.CELL_TYPE_BLANK:
			return "";
		default:
			return cell.getStringCellValue();
		}
	}

	private Workbook getWorkbook(MultipartFile request) throws IOException {
		Workbook wb=null;
		if(request.getOriginalFilename().endsWith("xlsx")){
			wb=new XSSFWorkbook(request.getInputStream());
		}else if(request.getOriginalFilename().endsWith("xls")){
			wb=new HSSFWorkbook(request.getInputStream());
		}
		return wb;
	}
}
