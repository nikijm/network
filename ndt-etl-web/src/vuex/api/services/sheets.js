import axios from 'axios'

export default {
  /**
   * 所有工作表列表
   *
   * @param  page: 第几页
   * @param  search: 搜索文本，用户名或文件名
   */
  list (listData) {
    return axios.get('/api/sheets', { params: listData })
    .then((response) => response.data)
  },
  listsClean (listData) {
    return axios.get('/api/sheets?filter=cached', { params: listData })
    .then((response) => response.data)
  },
  /**
   * 数据缓存列表
   *
   * @param  page: 第几页
   * @param  search: 搜索文本，用户名或文件名
   */
  listsCached (payload) {
    return axios.get('/api/uploads?filter=uncached', { params: payload })
    .then((response) => response.data)
  },
  /**
   *
   * 缓存文档
   *
   * @param id: 文档id
   */
  saveCache (id) {
    return axios.post('/api/uploads/' + id + '/cache')
    .then((response) => response.data)
  },
  /**
   *
   * 获取单个工作表
   *
   * id: 工作表id
   */
  getSheets (id) {
    return axios.get('/api/sheets/' + id)
    .then((response) => response.data)
  },
  /**
   *
   * 获取单个工作表数据
   *
   * id: 工作表id
   */
  getSheetsList (payload) {
    return axios.get('/api/sheets/' + payload.id + '/data?page=' + payload.page + '&limit=')
    .then((response) => response.data)
  },
  /**
   *
   * 获取数据转换工作表数据
   *
   *@param  page: 第几页
   *@param  search: 搜索文本，用户名或文件名
   */
  listTransform (payload) {
    return axios.get('/api/sheets?filter=cleaned', { params: payload })
    .then((response) => response.data)
  },
  /**
   *
   * 获取数据校验工作表数据
   *
   *@param  page: 第几页
   *@param  search: 搜索文本，用户名或文件名
   */
  listVerify (payload) {
    return axios.get('/api/sheets?filter=transformed', { params: payload })
    .then((response) => response.data)
  },
  /**
   *
   * 获取镜像业务表
   *
   */
  getMirrors () {
    return axios.get('/api/mirrors')
    .then((response) => response.data)
  },
  /**
   *
   * 获取单个工作表未清洗数据
   *
   * id: 工作表id
   * page: 第几页
   */
  getOriginalData (payload) {
    return axios.get('/api/sheets/' + payload.id + '/originalData?page=' + payload.page)
    .then((response) => response.data)
  },
  /**
   *
   * 获取单个工作表清洗后数据
   *
   * id: 工作表id
   * page: 第几页
   */
  getCleanedData (payload) {
    return axios.get('/api/sheets/' + payload.id + '/cleanedData?page=' + payload.page)
    .then((response) => response.data)
  },
  /**
   *  验证
   */
  validate (id, payload) {
    return axios.post('/api/sheets/' + id + '/validate', payload)
    .then((response) => response.data)
  },
  /**
   *  清洗
   */
  clean (id, payload) {
    return axios.post('/api/sheets/' + id + '/clean', payload)
    .then((response) => response.data)
  },
  /**
   *  转换
   */
  transform (id, payload) {
    return axios.post('/api/sheets/' + id + '/transform', payload)
    .then((response) => response.data)
  },
  /**
   *  校验
   */
  verify (id, payload) {
    return axios.post('/api/sheets/' + id + '/verify', payload)
    .then((response) => response.data)
  },
  /**
   *  查询数据
   */
  queryData (id, payload) {
    return axios.post('/api/sheets/' + id + '/data/query', payload)
    .then((response) => response.data)
  },
  /**
   *  获取镜像业务表数据
   */
  getMirrorsTableData (id, payload) {
    return axios.post('/api/mirrors/' + id + '/query', payload)
    .then((response) => response.data)
  },
  /**
   * 获取单个工作表的操作日志
   *
   * id: 工作表id
   */
  getLogs (id) {
    return axios.get('/api/sheets/' + id + '/logs')
    .then((response) => response.data)
  },
  /**
   *  回退到未验证状态
   */
  revertToUnvalidated (id) {
    return axios.post('/api/sheets/' + id + '/revertToUnvalidated')
    .then((response) => response.data)
  },
  /**
   *  回退到已清洗状态
   */
  revertToCleaned (id) {
    return axios.post('/api/sheets/' + id + '/revertToCleaned')
    .then((response) => response.data)
  },
  /**
   *  废弃工作表
   */
  trash (trashData) {
    return axios.post('/api/sheets/' + trashData.id + '/trash', {note: trashData.note})
    .then((response) => response.data)
  }
}
