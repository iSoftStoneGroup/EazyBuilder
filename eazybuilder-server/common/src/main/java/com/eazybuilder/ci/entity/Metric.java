package com.eazybuilder.ci.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.eazybuilder.ci.constant.MetricType;

/**
 * 质量统计信息
 */
@Entity
@Table(name="CI_METRIC")
public class Metric {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(10)")
	private String id;
	/**
	 * 统计项目,eg: 单元测试覆盖率,BUG-阻断,BUG-重要 等等
	 */
	@Enumerated(EnumType.STRING)
	private MetricType type;
	/**
	 * 统计值
	 */
	private String val;
	/**
	 * 统计时间
	 */
	private long time=System.currentTimeMillis();
	
	@ManyToOne
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonIgnore
	private Pipeline pipeline;
	
	private String attachmentId;
	
	private String link;
	
	private Boolean isRed=false;

	private boolean assertSucceed;


	private Double thresholdVal;
	
	
	public Metric() {
	}
	
	public Metric(MetricType type, String val, Pipeline pipeline) {
		this.type = type;
		this.val = val;
		this.pipeline = pipeline;
	}
	public Metric(MetricType type, String val, Pipeline pipeline,String attachmentId) {
		this.type = type;
		this.val = val;
		this.pipeline = pipeline;
		this.attachmentId=attachmentId;
	}
	
	public Metric(MetricType type, String val, Pipeline pipeline,String attachmentId,String link) {
		this.type = type;
		this.val = val;
		this.pipeline = pipeline;
		this.attachmentId=attachmentId;
		this.link=link;
	}
	
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public Pipeline getPipeline() {
		return pipeline;
	}
	public void setPipeline(Pipeline pipeline) {
		this.pipeline = pipeline;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public MetricType getType() {
		return type;
	}
	public void setType(MetricType type) {
		this.type = type;
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Boolean getIsRed() {
		return isRed;
	}

	public void setIsRed(Boolean isRed) {
		this.isRed = isRed;
	}

	public boolean isAssertSucceed() {
		return assertSucceed;
	}

	public void setAssertSucceed(boolean assertSucceed) {
		this.assertSucceed = assertSucceed;
	}

	public boolean  compareWitchThresholdVal(Double thresholdVal){
		return this.getType().compare(thresholdVal,Double.valueOf(this.getVal()));
	}

	public Double getThresholdVal() {
		return thresholdVal;
	}

	public void setThresholdVal(Double thresholdVal) {
		this.thresholdVal = thresholdVal;
	}
}
