import axios from 'axios'

export default {

  getTotalLogs (request) {
//  console.log(request)
    return axios.get('/api/logmanagers', { params: request })
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  },
  getSingleLogs (request) {
//  console.log(request)
    return axios.get('/api/logmanagers/' + request)
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  }
}
//  http://192.168.1.37:8080/etl/api/alarmrule/update
