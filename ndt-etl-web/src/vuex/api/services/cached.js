import axios from 'axios'
export default {
  getCachesData (request = {
  }) {
    return axios.get('/api/caches', {params: request})
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  getCleansData (request = {
  }) {
    return axios.get('/api/cleans', {params: request})
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  getConvertsData (request = {
  }) {
    return axios.get('/api/converts', {params: request})
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  },
  getVeritysData (request = {
  }) {
    return axios.get('/api/veritys', {params: request})
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  }
}
