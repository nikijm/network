import axios from 'axios'

export default {
  getSingleConfigMachine (request) {
//  console.log(request)
    return axios.get('/api/serverHosts/' + request)
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  },
  getTotalConfigMachine (request) {
//  console.log(request)
    return axios.get('/api/serverHosts')
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  },
  postModifyConfigMachine (request) {
//  console.log(request)
    return axios.put('/api/serverHosts/' + request.num, request.ajaxData)
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  },
  postCreateConfigMachine (request) {
//  console.log(request)
    return axios.post('/api/serverHosts', request)
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  },
  deleteConfigMachine2 (request) {
    return axios.delete('/api/serverHosts/' + request)
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  }
}
//  http://192.168.1.37:8080/etl/api/alarmrule/update
