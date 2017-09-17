import axios from 'axios'

export default {
  getSingleService (request) {
//  console.log(request)
    return axios.get('/api/servicemanagers/' + request)
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  },
  getTotalService (request) {
//  console.log(request)
    return axios.get('/api/servicemanagers')
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  },
  startService (request) {
//  console.log(request)
    return axios.put('/api/servicemanagers/' + request + '/start')
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  },
  stopService (request) {
    return axios.put('/api/servicemanagers/' + request + '/stop')
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  }
}
//  http://192.168.1.37:8080/etl/api/alarmrule/update
