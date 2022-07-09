package com.eazybuilder.ci.controller.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlRootElement(name="Environment")
public class UFTEnv {

	@XmlElement(name="Variable")
	public List<Variable> variable;
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType
	public static class Variable{
		@XmlElement(name="Name")
		private String Name;
		@XmlElement(name="Value")
		private String Value;
		public String getName() {
			return Name;
		}
		public void setName(String name) {
			Name = name;
		}
		public String getValue() {
			return Value;
		}
		public void setValue(String value) {
			Value = value;
		}
	}

}
