package com.jk.ndt.etl.entity.vo.getcreditdata;

import com.alibaba.fastjson.annotation.JSONField;

public class DataBean {

	@JSONField(name = "CreditIndex")
	private CreditIndexBean creditIndex;

	public CreditIndexBean getCreditIndex() {
		return creditIndex;
	}

	public void setCreditIndex(CreditIndexBean creditIndex) {
		this.creditIndex = creditIndex;
	}

}
