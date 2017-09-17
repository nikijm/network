import axios from 'axios'

export default {
  /**
   * 上传文档列表
   *
   * @param  page: 第几页
   * @param  search: 搜索文本，用户名或文件名
   */
  list (payload) {
    return axios.get('/api/uploads', { params: payload })
    .then((response) => response.data)
  },
  /**
   * 上传文档列表，withSheets
   *
   * @param  page: 第几页
   * @param  search: 搜索文本，用户名或文件名
   */
  listWithSheets (payload) {
    return axios.get('/api/uploadsWithSheets', { params: payload })
    .then((response) => response.data)
  },
  /**
   * 获取单个文档对应的工作表
   *
   * @param uploadId 上传文档id
   */
  getUploadSheets (uploadId) {
    return axios.get('/api/uploads/' + uploadId + '/sheets')
    .then((response) => response.data)
  },
  /**
   *上传文件
   *
   * @param file：文件
   * @param etlDatafileId：文件类型
   *
   */
  upload (formdata) {
    return axios.post('/api/uploads', formdata)
    .then((response) => response.data)
  },
  /*
   *上传文件类型
   *
   */
  getFileTypes () {
    return axios.get('/api/dataFileTypes')
    .then((response) => response.data)
  }
}
