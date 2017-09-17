package com.jk.ndtetl.schedule.conf;

import java.math.BigDecimal;
import java.util.Date;

import com.jk.ndtetl.system.User;

public class TaskDetailPojo {

	private int percent = 0;// 百分比
	private int totalCount = 0;// 文件总记录数
	private int currentCount = 0;// 当前处理的条数
	private String fileId = null;// 文件ID
	private String fileName = null;//文件名字
	private boolean isSuspend = false;// 是否暂停
	private boolean isStop = false;//是否停止
	private long createDate = 0;
	private boolean isSecound = false;//是否是二次启动
	private User user = null;//操作用户
	int successCount = 0;//当前成功的条数
	int failedCount = 0;//当前失败的条数
	int i = 0;//当前循环的次数
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public TaskDetailPojo() {
		Date date = new Date();
		createDate = date.getTime();
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public boolean isSuspend() {
		return isSuspend;
	}

	public void setSuspend(boolean isSuspend) {
		this.isSuspend = isSuspend;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public int getPercents() {
		if (totalCount == 0) {
			return 0;
		}
		if (totalCount < currentCount) {
			return 0;
		}
		BigDecimal b = new BigDecimal((double)currentCount / (double)totalCount);
		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return (int) (f1 * 100);
	}
	
	public boolean isStop() {
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isSecound() {
		return isSecound;
	}

	public void setSecound(boolean isSecound) {
		this.isSecound = isSecound;
	}
}
