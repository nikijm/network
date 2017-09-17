import { services } from '../api'

const FETCH_TOTALLOGS = 'FETCH_TOTALLOGS'
const FETCH_SINGLELOGS = 'FETCH_SINGLELOGS'
const state = {
  totalLogs: [],
  singleLogs: {},
  logsPaging: {}
}

const getters = {
  totalLogs: state => state.totalLogs,
  logsPaging: state => state.logsPaging,
  singleLogs: state => state.singleLogs
}

const mutations = {
  [FETCH_TOTALLOGS] (state, data) {
    state.totalLogs = data.data
    state.logsPaging = data.paging
  },
  [FETCH_SINGLELOGS] (state, data) {
    state.singleLogs = data.data
  }
}

const actions = {
  fetchTotalLogs: ({ commit }, req) => {
    // API request
//  console.log(req)
    return services.logs.getTotalLogs(req)
    .then((response) => {
//    console.log(response)
      commit(FETCH_TOTALLOGS, response.data)
      return response
    })
    .catch((error) => {
//    console.error(error)
      return error.response
    })
  },
  fetchSingleLogs: ({ commit }, req) => {
    // API request
//  console.log(req)
    return services.logs.getSingleLogs(req)
    .then((response) => {
//    console.log(response)
      commit(FETCH_SINGLELOGS, response.data)
      return response
    })
    .catch((error) => {
//    console.error(error)
      return error.response
    })
  }
}

export default {
  state,
  actions,
  getters,
  mutations
}
