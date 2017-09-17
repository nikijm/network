import axios from 'axios'

export default {
  getValidators () {
    return axios.get('/api/rules/validators')
    .then((response) => response.data)
  },
  getOperations () {
    return axios.get('/api/rules/operations')
    .then((response) => response.data)
  },
  /**
   * 测试字符串操作
   *
   * @param input 输入字符串
   * @param operations 字符串操作数组
   */
  testOperations (payload) {
    return axios.post('/api/rules/operations/test', payload)
    .then((response) => response.data)
  },
  /**
   * 规则模板列表
   *
   * @param  page: 第几页
   * @param  search: 搜索文本，规则模板名
   */
  getTemplates (payload) {
    return axios.get('/api/ruleSuits', { params: payload })
    // return axios.get('/api/rules/templates', { params: payload })
    .then((response) => response.data)
  },
  /**
   * 新建规则模板
   */
  saveTemplate (payload) {
    // return axios.post('/api/rules/templates', payload)
    return axios.post('/api/ruleSuits', payload)
    .then((response) => response.data)
  },
  /**
   * 获取单个规则模板
   */
  getTemplate (id) {
    return axios.get('/api/rules/templates/' + id)
    .then((response) => response.data)
  },
  /**
   * 更新单个模板规则
   */
  updateTemplate (id, payload) {
    // return axios.put('/api/rules/templates/' + id, payload)
    return axios.put('/api/ruleSuits/' + id, payload)
    .then((response) => response.data)
  },
  /**
   * 删除单个模板规则
   */
  deleteTemplate (id) {
    // return axios.delete('/api/rules/templates/' + id)
    return axios.delete('/api/ruleSuits/' + id)
    .then((response) => response.data)
  },
  /**
   * 根据sheets表的columns查询规则模板
   */
  queryTemplate (columnsJSON) {
    return axios.post('/api/rules/templates/query', {columnsJSON: columnsJSON})
    .then((response) => response.data)
  },
  /*
   * 获取规则处理器列表以及规则模板类型选项
   */
  getRuleOps () {
    return axios.get('/api/ruleOps')
    .then((response) => response.data)
  },
  /**
   * 删除单个模板
   */
  deleteRuleOps (id) {
    return axios.delete('/api/ruleOps/' + id)
    .then((response) => response.data)
  },
  /**
   * 修改单个模板
   * 1 验证
   * 2 清洗
   * 3 校验
   */
  reviseRuleOps (payload) {
    return axios.put('/api/ruleOps/' + payload.id, payload)
    .then((response) => response.data)
  },
  /**
   * 上传单个模板
   *
   */
  addRuleOps (payload) {
    return axios.post('/api/ruleOps/', payload)
    .then((response) => response.data)
  }
}
