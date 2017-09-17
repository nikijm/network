package com.jk.ndtetl.schedule.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.jk.ndtetl.schedule.conf.TaskDetailPojo;
import com.jk.ndtetl.schedule.exception.TaskException;

public class ScheduleUtil {
	
	public static TaskDetailPojo createVo(int percent,int totalCount,int currentCount,String fileId,boolean isSuspend,String fileName){
		TaskDetailPojo vo = new TaskDetailPojo();
		try {
			vo.setPercent(percent);
			vo.setCurrentCount(currentCount);
			vo.setFileId(fileId);
			vo.setSuspend(isSuspend);
			vo.setTotalCount(totalCount);
			vo.setFileName(fileName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return vo;
	}
	
	public static void clear(Map<String,TaskDetailPojo> map) throws TaskException{
		try {
			long nowTime = new Date().getTime();
			String[] keys = map.keySet().toArray(new String[map.size()]);
			for (String key : keys) {
				TaskDetailPojo vo = map.get(key);
				long start = vo.getCreateDate();
				if((nowTime - start)>(1800*1000)){
					map.remove(key);
					continue;
				}
				if(vo.getPercents() != 100){
					continue;
				}
				if((nowTime - start)>(10*1000)){
					map.remove(key);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new TaskException("ETL9999","Make a unknown Exception when clear data.");
		}
	}
	
	/**
	 * 将日期转换成字符串
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
	
	/**
	 * 查找目标串在原串中的位置
	 * 
	 * @param source 
	 * @param target
	 * @return
	 */
	public static int indexOf(byte[] source, byte[] target) {
		return indexOf(source, target, 0);
	}
	
	public static int indexOf(byte[] source, byte[] target, int fromIndex) {
		return indexOf(source, 0, source.length, target, 0, target.length,
					   fromIndex);
	}
	
	private static int indexOf(byte[] source, int sourceOffset, int sourceCount,
							  byte[] target, int targetOffset, int targetCount,
							  int fromIndex) {
		if(fromIndex >= sourceCount) {
			return targetCount == 0 ? sourceCount : -1;
		}
		if(fromIndex < 0) {
			fromIndex = 0;
		}
		if(targetCount == 0) {
			return fromIndex;
		}

		byte first = target[targetOffset];
		int max = sourceOffset + (sourceCount - targetCount);
		for(int i = sourceOffset + fromIndex; i <= max; i++) {
			/* Look for first character. */
			if(source[i] != first) {
				while(++i <= max && source[i] != first);
			}

			/* Found first character, now look at the rest of v2 */
			if(i <= max) {
				int j = i + 1;
				int end = j + targetCount - 1;
				for(int k = targetOffset + 1; j < end && source[j] == target[k];
					j++, k++);
				if(j == end) {
					/* Found whole string. */
					return i - sourceOffset;
				}
			}
		}
		return -1;
	}
	
	/**
	 * 将目标串插在源串values位置后
	 * @param pos
	 * @param sources
	 * @param target
	 * @return
	 */
	public static byte[] setValue(int i,byte[] sources,byte[] target){
		byte[] retBytes = new byte[sources.length + target.length];
		System.arraycopy(sources, 0, retBytes, 0, i);
		System.arraycopy(target, 0, retBytes, i , target.length);
		System.arraycopy(sources, i, retBytes, i + target.length, sources.length-i);
		return retBytes;
	}
	public static void main(String[] args) {
//		System.out.println(new String(setValue(1, "AAB".getBytes(), "12".getBytes())));
		System.out.println(indexOf("AACCCABABCCCCCCC".getBytes(), "A".getBytes(), 8));
	}
}
