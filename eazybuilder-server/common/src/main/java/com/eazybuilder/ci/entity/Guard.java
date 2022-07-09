package com.eazybuilder.ci.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="CI_GUARD")
public class Guard implements Serializable{


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(8)")
	private String id;

	private String name;


	private ThresholdType guardType;
	/**
	 * 阈值-min
	 */
	private Double thresholdMin;
	
	/**
	 * 阈值-max
	 */
	private Double thresholdMax;
	
	private String level;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getThresholdMin() {
		return thresholdMin;
	}

	public void setThresholdMin(Double thresholdMin) {
		this.thresholdMin = thresholdMin;
	}

	public Double getThresholdMax() {
		return thresholdMax;
	}

	public void setThresholdMax(Double thresholdMax) {
		this.thresholdMax = thresholdMax;
	}


	public ThresholdType getGuardType() {
		return guardType;
	}

	public void setGuardType(ThresholdType guardType) {
		this.guardType = guardType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

}
