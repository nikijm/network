import axios from 'axios'

export default {
//  postDashboard (request) {
//    console.log(request)
//    return axios.post('http://hb.huidang2105.com/profile', request)
//          .then((response) => Promise.resolve(response))
//          .catch((error) => Promise.reject(error))
//  }
  postDashboard (request) {
//  console.log(request)
    return axios.get('/api/monitor/machines/' + request.serverName + '/' + request.monitorCompunent + '?period=' + request.data.filter_by)
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  },
  clearMemory (request) {
    return axios.delete('/api/monitor/machines/' + request + '/memory')
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  }
//  简写的访问技术，实现简单的跨域功能api/monitor/machine/3/clearMemory
}
//  http://192.168.1.37:8080/etl/api/alarmrule/update
