package com.eazybuilder.ci.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Table;

import com.eazybuilder.ci.constant.MetricType;

@Entity
@Table(name="CI_WARN_RULE")
public class WarnRule implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7164913580126557777L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(8)")
	private String id;
	
	private MetricType metricType;
	/**
	 * 阈值-min
	 */
	private Double thresholdMin;
	
	/**
	 * 阈值-max
	 */
	private Double thresholdMax;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MetricType getMetricType() {
		return metricType;
	}

	public void setMetricType(MetricType metricType) {
		this.metricType = metricType;
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
}
