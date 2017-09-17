package com.jk.ndtetl.rule;

public interface StringOperation  extends IClean{
	String doClean(String value, CleanParam params);
	String doCleans(String value,CleanParam[] params);
}
