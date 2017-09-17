import axios from 'axios'

export default {
  getOrganization (request = {
  }) {
    return axios.get('/api/organizations?', { params: request })
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  getOutUser (request = {
  }) {
    return axios.get('/api/users?externalz=1', { params: request })
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  getAdmin (request = {
  }) {
    return axios.get('/api/users', { params: request })
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  getRoles (request = {
  }) {
    return axios.get('/api/roles?page_size=0', request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  addAdmin (request = {
  }) {
    return axios.post('/api/users', request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  updataAdmin (request = {
  }) {
    return axios.put('/api/users/' + request.id, request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  userLogin (request = {
  }) {
    return axios.post('/api/auth/session', request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  /**
   * 外部用户登录
   */
  userCommonLogin (request = {
  }) {
    return axios.post('/api/user/session', request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  userLoginOut (request = {
  }) {
    return axios.delete('/api/auth/session', request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  deleteAdmin (request = {
  }) {
    return axios.delete('/api/users/' + request.id, request)
    .then((response) => Promise.resolve(response))
    .catch((error) => Promise.reject(error))
  },
  addUsers (request = {
  }) {
    return axios.post('/api/users?externalz=1', request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  updataUsers (request = {
  }) {
    return axios.put('/api/users/' + request.id, request)
    .then((response) => Promise.resolve(response))
    .catch((error) => Promise.reject(error))
  },
  deleteUsers (request = {
  }) {
    return axios.delete('/api/users/' + request.id, request)
    .then((response) => Promise.resolve(response))
    .catch((error) => Promise.reject(error))
  },
  addOrg (request = {
  }) {
    return axios.post('/api/organizations', request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  upload (formdata) {
    return axios.post('/api/organizations', formdata)
    .then((response) => response.data)
  },
  updateOrg (request) {
    return axios.put('/api/organizations/' + request.orgId, request)
    .then((response) => Promise.resolve(response))
    .catch((error) => Promise.reject(error))
  },
  uploadLogo (request) {
    return axios.post('/api/organizations/' + request.id + '/logo', request.formdata2)
    .then((response) => Promise.resolve(response))
    .catch((error) => Promise.reject(error))
  },
  deleteOrg (request = {
  }) {
    return axios.delete('/api/organizations/' + request.id, request)
    .then((response) => Promise.resolve(response))
    .catch((error) => Promise.reject(error))
  }
}
