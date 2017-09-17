package com.jk.ndt.etl.entity.presystem;

import java.io.Serializable;
import java.util.Date;

public class BusinessSync implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long adClientId;
	private Long adOrgId;
	private Long businesssyncId;
	private String orgCode;
	private String orgName;
	private String businessCode;
	private String businessName;
	private String workflowId;
	private String workflowName;
	private Long adTableId;
	private String serviceUrl;
	private Date created;
	private Long createdBy;
	private Date updated;
	private Long updatedBy;
	private String isActive;

	public Long getAdClientId() {
		return adClientId;
	}

	public void setAdClientId(Long adClientId) {
		this.adClientId = adClientId;
	}

	public Long getAdOrgId() {
		return adOrgId;
	}

	public void setAdOrgId(Long adOrgId) {
		this.adOrgId = adOrgId;
	}

	public Long getBusinesssyncId() {
		return businesssyncId;
	}

	public void setBusinesssyncId(Long businesssyncId) {
		this.businesssyncId = businesssyncId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public Long getAdTableId() {
		return adTableId;
	}

	public void setAdTableId(Long adTableId) {
		this.adTableId = adTableId;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}
