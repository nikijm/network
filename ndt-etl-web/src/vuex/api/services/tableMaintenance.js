import axios from 'axios'

export default {
  getTableMaintenance (category) {
    return axios.get('/api/tableDefs' + '?category=' + category)
    .then((response) => response.data)
  },
  postTable (request) {
    return axios.post('/api/dbmeta', request)
    .then((response) => response.data)
  },
  putField (request) {
    return axios.post('/api/dbmeta/colunms', request)
    .then((response) => response.data)
  },
  modify (request) {
    return axios.put('/api/tableDefs/' + request.etlTableId)
    .then((response) => response.data)
  }
}
