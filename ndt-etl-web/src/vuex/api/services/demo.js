import axios from 'axios'

export default {
  postDemo (request = {
  }) {
    return axios.get('http://jsonplaceholder.typicode.com/users', request)
      .then((response) => Promise.resolve(response))
      .catch((error) => Promise.reject(error))
  }
}
