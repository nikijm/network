import axios from 'axios'

export default {
  /**
   * 获取所有数据文件类型
   */
  getDataFileTypes (request = {}) {
    let url = '/api/dataFileTypes'
    if (request.orgId) {
      url = '/api/dataFileTypes?orgId=' + request.orgId
    }
    return axios.get(url)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  /**
   * 添加数据文件类型
   */
  addDataFileType (request = {}) {
    return axios.post('/api/dataFileTypes', request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  /**
   * 修改数据文件类型
   */
  updateDataFileType (request = {}) {
    return axios.put('/api/dataFileTypes/' + request.etlFileTypeId, request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  /**
   * 删除数据文件类型
   */
  deleteDataFileType (request = {}) {
    return axios.delete('/api/dataFileTypes/' + request.id, request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  /**
   * 获取生成缺省
   */
  getFieldAlign (request = {}) {
    return axios.get('/api/createDefaultFieldsAlign/' + request.id)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  /**
   * 获取业务类型
   */
  getBusinessList (request = {}) {
    return axios.get('/api/tableDefs', request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  /**
   * 获取源表类型
   */
  getSourceSheetType (request = {}) {
    return axios.get('/api/ruleUses?category=' + request.category, request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  /**
   * 获取规则列映射
   */
  getRuleUses (request = {}) {
    return axios.get('/api/ruleUses', {params: request})
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  /**
   * 修改规则列映射
   */
  updateRuleUses (request = {}) {
    return axios.post('/api/ruleUses', request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  /**
   * 左边 获取任务调度总数据
   */
  getDataFiles (request = {}) {
    return axios.get('/api/dataFiles', {params: request})
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  // 调度 左边错误信息
  errorMessage (request = {}) {
    return axios.get('/api/dataFileLog/' + request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  // 调度 缓存数据
  getallseehtsCachesData (request = {}) {
    var newObj = {
      current: request.current
    }
    return axios.get('/api/dataFile/' + request.id + '/caches', {params: newObj})
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  // 调度 数据对比
  getComparesData (request = {}) {
    return axios.get('/api/compares/' + request.Params.id + '?category=' + request.Params.category, {params: request})
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
   // 调度 规则
  getRuleUsers (request = {}) {
    return axios.get('/api/dataFile/' + request.id + '/ruleUses' + '?category=' + request.category, request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  // 调度 左边启动单个任务
  getOneDataFiles (request = {}) {
    return axios.get('/api/schedule/start', {params: request})
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  // 调度 右边启动单个任务
  startOneDataFiles (request = {}) {
    return axios.get('/api/schedule/restart', {params: request})
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  // 调度 右边暂停单个任务
  pauseOneDataFiles (request = {}) {
    return axios.get('/api/schedule/supend', {params: request})
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  // 调度 右边停止单个任务
  stopOneDataFiles (request = {}) {
    return axios.get('/api/schedule/stop', {params: request})
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
   // 调度 右边总开关
  changeAllDataFiles (request = {}) {
    return axios.get('/api/schedule/opetask', {params: request})
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  // 右边 任务调度进程展示数据
  getDataDispatchs (request = {}) {
    return axios.get('/api/dataFile/dispatchs', request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  }
}
