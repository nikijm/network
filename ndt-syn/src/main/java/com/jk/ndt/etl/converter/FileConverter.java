package com.jk.ndt.etl.converter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.jk.ndt.etl.converter.model.DataTable;

/**
 * 
 * @ClassName: FileConverter
 * @Description: 文件转换接口，将上传的文件内容读取出来
 * @author fangwei
 * @date 2017年5月13日 下午1:55:43
 *
 */
public interface FileConverter {
	/**
	 * 
	 * @Description: 根据文件路径转换文件为List对象
	 * @author fangwei
	 * @date 2017年5月13日 下午2:10:51
	 * @param filePath
	 * @return 一个虚拟的table列表
	 * @throws IOException 
	 */
	List<DataTable> convert(String filePath) throws Exception;

	/**
	 * 
	 * @Description: 根据文件对象转换文件为List对象
	 * @author fangwei
	 * @date 2017年5月13日 下午2:10:41
	 * @param file
	 * @return 一个虚拟的table列表
	 */
	List<DataTable> convert(File file) throws Exception;;
}
