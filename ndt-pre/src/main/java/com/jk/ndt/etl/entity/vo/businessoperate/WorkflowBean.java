package com.jk.ndt.etl.entity.vo.businessoperate;

import com.alibaba.fastjson.annotation.JSONField;

public class WorkflowBean {
	@JSONField(name = "NodeCode")
	private String nodeCode;

	@JSONField(name = "NodeName")
	private String nodeName;

	@JSONField(name = "OperatorCode")
	private String operatorCode;

	@JSONField(name = "OperatorName")
	private String operatorName;

	@JSONField(name = "DocAction")
	private String docAction;

	@JSONField(name = "ActionDescription")
	private String actionDescription;

	@JSONField(name = "GrantAmout")
	private String grantAmout;

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getDocAction() {
		return docAction;
	}

	public void setDocAction(String docAction) {
		this.docAction = docAction;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}

	public String getGrantAmout() {
		return grantAmout;
	}

	public void setGrantAmout(String grantAmout) {
		this.grantAmout = grantAmout;
	}

}
