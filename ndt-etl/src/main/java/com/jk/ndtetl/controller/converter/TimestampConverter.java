package com.jk.ndtetl.controller.converter;

import java.sql.Timestamp;

import org.springframework.core.convert.converter.Converter;

/**
 * Timestamp日期类型转换
 * 
 * @ClassName: TimestampConverter
 * @author lianhanwen
 * @date 2017年7月13日 下午3:38:33
 *
 */
public class TimestampConverter implements Converter<Object, Long> {

    @Override
    public Long convert(Object source) {
        if (source instanceof Timestamp) {
            long timestamp = Timestamp.valueOf(source.toString()).getTime();
            return timestamp;
        }
        return null;
    }

}