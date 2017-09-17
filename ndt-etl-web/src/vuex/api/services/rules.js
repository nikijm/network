import axios from 'axios'

export default {
  getRules (request) {
    return axios.get('/api/alarmrules', { params: request })
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  },
  postRulesMachine (request) {
    return axios.get('/api/serverHosts', { params: request })
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  },
  postCreateNewRules (request) {
    return axios.post('/api/alarmrules', request)
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  },
  postSingleRule (request) {
    return axios.get('/api/alarmrules/' + request)
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  },
  postModifyRules (request) {
    return axios.put('/api/alarmrules/' + request.num, request.ajaxData, {'Content-Type': 'application/json'})
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  },
  postDeleteRule (request) {
    return axios.delete('/api/alarmrules/' + request)
          .then((response) => Promise.resolve(response))
          .catch((error) => Promise.reject(error))
  }
}
