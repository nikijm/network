import { services } from '../api'
// mutation-types
const FETCH_DATA = 'FETCH_DATA'
const getters = {
  totalDemo: state => state.main
}

const state = {
  main: []
}

const mutations = {
  [FETCH_DATA] (state, data) {
    state.main = data
  }
}

const actions = {
  [FETCH_DATA] ({commit}, payload) {
    return services.demo.postDemo()
    .then((response) => {
      commit(FETCH_DATA, response.data)
      return response
    })
    .catch((error) => {
      console.error(error)
      return error.response
    })
  }
}

export default {
  getters,
  state,
  mutations,
  actions
}
