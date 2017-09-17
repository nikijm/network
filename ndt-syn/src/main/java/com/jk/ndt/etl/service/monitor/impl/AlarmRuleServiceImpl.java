package com.jk.ndt.etl.service.monitor.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.dao.monitor.AlarmRuleDao;
import com.jk.ndt.etl.entity.monitor.AlarmLogs;
import com.jk.ndt.etl.entity.monitor.AlarmRuleBean;
import com.jk.ndt.etl.entity.monitor.ServerHost;
import com.jk.ndt.etl.service.monitor.AlarmRuleService;
import com.jk.ndt.etl.service.monitor.ServerHostService;

/**
 * 
 * @ClassName: ServerInfoDao
 * @Description: 监控报警规则的实体类
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
@Service("alarmRuleService")
public class AlarmRuleServiceImpl implements AlarmRuleService {

	@Autowired
	AlarmRuleDao alarmRuleDao;
	@Autowired
	ServerHostService serverHostService;
    
	@Override
	public List<AlarmRuleBean> getAlarmRuleList(Integer serverId, Integer type, Integer status) {
		AlarmRuleBean alarmRuleBean = new AlarmRuleBean();
		if (serverId != null && serverId != 0) {
			alarmRuleBean.setMachineAndId(serverId);
		}
		if (type != null && type != 0) {
			alarmRuleBean.setType(type);
		}
		if (status != null && status != 0) {
			alarmRuleBean.setIsUse(status);
		}
		List<AlarmRuleBean> alarmRuleList = alarmRuleDao.getAlarmRuleList(alarmRuleBean);
		List<AlarmRuleBean> alarmRuleListNew = new ArrayList<>();
		for (AlarmRuleBean alarmRuleBean2 : alarmRuleList) {

			Long recentTrigger = alarmRuleBean2.getRecentTrigger();
			if (recentTrigger == null) {
				alarmRuleBean2.setRecentTriggerView(null);
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String format = sdf.format(new Date(recentTrigger));
				alarmRuleBean2.setRecentTriggerView(format);
			}
			ServerHost oneServerHost = serverHostService.getOneServerHost(alarmRuleBean2.getMachineAndId());
			alarmRuleBean2.setDescription(oneServerHost.getDescription());
			alarmRuleListNew.add(alarmRuleBean2);
		}
	  return alarmRuleListNew;
	}

	@Override
	public List<AlarmRuleBean> listAlarmIdByServerId(Integer serverId) {
		List<AlarmRuleBean> AlarmRuleBean = alarmRuleDao.listAlarmIdByServerId(serverId);
		return AlarmRuleBean;
	}

	@Override
	public AlarmRuleBean getAlarmRuleById(Integer alarmRuleId) {

		AlarmRuleBean alarmRuleBean = alarmRuleDao.getAlarmRuleByIdNotStatus(alarmRuleId);
		if (alarmRuleBean == null) {
			return null;
		}
		Integer expressionType = alarmRuleBean.getExpressionType();
		if (expressionType == Constant.EXPRESSION_TYPE_LT) {
			String expression = alarmRuleBean.getExpression();
			String string = expression.split("lt;")[1];
			alarmRuleBean.setContent(string);
		} else if (expressionType == Constant.EXPRESSION_TYPE_LT) {
			String expression = alarmRuleBean.getExpression();
			String string = expression.split("gt;")[1];
			alarmRuleBean.setContent(string);
		} else if (expressionType == Constant.EXPRESSION_TYPE_CONTAIN) {
			String expression = alarmRuleBean.getExpression();
			String string = expression.split("p")[1];
			alarmRuleBean.setContent(string);
		}
		return alarmRuleBean;
	}

	@Override
	public void saveOneAlarmRule(AlarmRuleBean alarmRuleBean, Integer[] machineType) {

		saveRule(alarmRuleBean, machineType);
	}

	private void saveRule(AlarmRuleBean alarmRuleBean, Integer[] machineType) {
		for (Integer integer : machineType) {
			StringBuilder sb = new StringBuilder();
			alarmRuleBean.setMachineAndId(integer);
			Integer type = alarmRuleBean.getType();
			if (type == Constant.HOSTNAME_TYPE_CPU) {
				alarmRuleBean.setMonitorResource("cpu");
				sb.append("cpu_size_");
			} else if (type == Constant.HOSTNAME_TYPE_MEMORY) {
				alarmRuleBean.setMonitorResource("内存");
				sb.append("memory_size_");
			} else if (type == Constant.HOSTNAME_TYPE_DISK) {
				alarmRuleBean.setMonitorResource("磁盘");
				sb.append("disk_size_");
			} else if (type == Constant.HOSTNAME_TYPE_LOG) {
				alarmRuleBean.setMonitorResource("日志");
				sb.append("log_contain_");
			}
			Integer expressionType = alarmRuleBean.getExpressionType();
			if (expressionType == Constant.EXPRESSION_TYPE_LT) {
				sb.append("lt;" + alarmRuleBean.getContent());
				alarmRuleBean.setExpression(sb.toString());
			} else if (expressionType == Constant.EXPRESSION_TYPE_GT) {
				sb.append("gt;" + alarmRuleBean.getContent());
				alarmRuleBean.setExpression(sb.toString());
			} else if (expressionType == Constant.EXPRESSION_TYPE_CONTAIN) {
				sb.append("p" + alarmRuleBean.getContent() + "p");
				alarmRuleBean.setExpression(sb.toString());
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			alarmRuleBean.setCreateTime(sdf.format(new Date()));
			alarmRuleBean.setUseCount(0);
			alarmRuleDao.addOneAlarmRule(alarmRuleBean);
		}
	}

	@Override
	public AlarmRuleBean getAlarmRuleByModuleAndType(AlarmRuleBean alarmRuleBean) {
		AlarmRuleBean alarmRuleByModuleAndType = alarmRuleDao.getAlarmRuleByModuleAndType(alarmRuleBean);
		return alarmRuleByModuleAndType;
	}

	@Override
	public AlarmRuleBean getAlarmRuleByContent(String content) {
		AlarmRuleBean alarmRuleBean = new AlarmRuleBean();
		content = "p" + content + "p";
		alarmRuleBean.setContent(content);
		AlarmRuleBean alarmRuleBeanC = alarmRuleDao.getAlarmRuleByContent(alarmRuleBean);
		return alarmRuleBeanC;
	}

	@Override
	public void updateOneAlarmRule(AlarmRuleBean alarmRuleBean, Integer[] machineType) {
		updateRule(alarmRuleBean, machineType);
	}

	private void updateRule(AlarmRuleBean alarmRuleBean, Integer[] machineType) {
		StringBuilder sb = new StringBuilder();
		
		Integer type = alarmRuleBean.getType();
		if (type == 1) {
			alarmRuleBean.setMonitorResource("cpu");
			sb.append("cpu_size_");
		} else if (type == 2) {
			alarmRuleBean.setMonitorResource("内存");
			sb.append("memory_size_");
		} else if (type == 3) {
			alarmRuleBean.setMonitorResource("磁盘");
			sb.append("disk_size_");
		} else if (type == 4) {
			alarmRuleBean.setMonitorResource("日志");
			sb.append("log_contain_");
		}
		Integer expressionType = alarmRuleBean.getExpressionType();
		if (expressionType == 1) {
			sb.append("lt;" + alarmRuleBean.getContent());
			alarmRuleBean.setExpression(sb.toString());
		} else if (expressionType == 2) {
			sb.append("gt;" + alarmRuleBean.getContent());
			alarmRuleBean.setExpression(sb.toString());
		} else if (expressionType == 3) {
			sb.append("p" + alarmRuleBean.getContent() + "p");
			alarmRuleBean.setExpression(sb.toString());
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		alarmRuleBean.setCreateTime(sdf.format(new Date()));
		alarmRuleDao.updateOneAlarmRule(alarmRuleBean);
	}

	@Override
	public void updateAlarmRule(AlarmRuleBean alarmRule) {
		alarmRuleDao.updateAlarmRule(alarmRule);

	}

	@Override
	public void insertAlarmLogs(AlarmLogs alarmLogs) {
		alarmRuleDao.insertAlarmLogs(alarmLogs);
	}

	@Override
	public void updateAlarmLogs(AlarmLogs alarmLogs2) {
		alarmRuleDao.updateAlarmLogs(alarmLogs2);

	}

	@Override
	public List<AlarmLogs> getRepeatSendAlarmMailList() {
		List<AlarmLogs> alarmLogsList = alarmRuleDao.getRepeatSendAlarmMailList();
		return alarmLogsList;
	}

	@Override
	public void deleteAlarmRule(Integer id) {
		alarmRuleDao.deleteAlarmRule(id);
	}
}
