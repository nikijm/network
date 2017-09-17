package com.jk.ndtetl.rule.service;

import com.jk.ndtetl.rule.CleanParam;
import com.jk.ndtetl.rule.IClean;
public interface IVerifyRuleService extends IClean{
    boolean verify(String value, CleanParam[] param);
}
