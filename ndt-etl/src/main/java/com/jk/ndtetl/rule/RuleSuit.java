package com.jk.ndtetl.rule;

import com.jk.ndtetl.BaseEntity;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

public class RuleSuit extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	@NotNull(message = "排序不能为空")
	private Integer sequence;

	@NotBlank(message = "名称不能为空")
	private String name;
    //规则模板的描述
	private String description;
    //规则处理器的json串
	private String rules;

	//由于模版可以选择多个不同类型的处理器，故此处废弃该字段
//	private String opType;
	//规则处理器集合
	private List<RuleOp> ruleOps;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRules() {
		return rules;
	}
	public void setRules(String rules) {
		this.rules = rules;
	}
	
//	public String getOpType() {
//		return opType;
//	}
//	public void setOpType(String opType) {
//		this.opType = opType;
//	}

	public List<RuleOp> getRuleOps() {
		return ruleOps;
	}

	public void setRuleOps(List<RuleOp> ruleOps) {
		this.ruleOps = ruleOps;
	}
}
