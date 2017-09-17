package com.jk.ndtetl.mon;

/**
 * 
 * @ClassName: ServerInfoDao
 * @Description: 自动创建监控信息的表头实体类
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public class MonitorTableInfo {

    private String id = "ID"; // 表的ID

    private String serverId = "SERVERID"; // 主机的ID

    private String cpuusage = "CPUUSAGE"; // cpu的使用率

    private String memoryusage = "MEMORYUSAGE"; // 内存的使用率

    private String diskusage = "DISKUSAGE"; // 磁盘的使用率

    private String createtime = "CREATETIME"; // 创建的时间

    private String calculate = "CALCULATE"; // 时间搓

    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCpuusage() {
        return cpuusage;
    }

    public void setCpuusage(String cpuusage) {
        this.cpuusage = cpuusage;
    }

    public String getMemoryusage() {
        return memoryusage;
    }

    public void setMemoryusage(String memoryusage) {
        this.memoryusage = memoryusage;
    }

    public String getDiskusage() {
        return diskusage;
    }

    public void setDiskusage(String diskusage) {
        this.diskusage = diskusage;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCalculate() {
        return calculate;
    }

    public void setCalculate(String calculate) {
        this.calculate = calculate;
    }

}
