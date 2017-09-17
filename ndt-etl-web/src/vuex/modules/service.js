import { services } from '../api'

const FETCH_TOTALSERVICE = 'FETCH_TOTALSERVICE'
const FETCH_SINGLESERVICE = 'FETCH_SINGLESERVICE'
const SETSERVETIMER = 'SETSERVETIMER'
const state = {
  totalService: {},
  singleService: {},
  serveTimer: 0
}

const getters = {
  totalService: state => state.totalService,
  singleService: state => state.singleService,
  serveTimer: state => state.serveTimer
}

const mutations = {
  [FETCH_TOTALSERVICE] (state, data) {
    state.totalService = data
  },
  [FETCH_SINGLESERVICE] (state, data) {
    state.singleService = data.data
  },
  [SETSERVETIMER] (state, data) {
    state.serveTimer = data
  }
}

const actions = {
  fetchTotalService: ({ commit }, req) => {
    // API request
//  console.log(req)
    return services.service.getTotalService(req)
    .then((response) => {
//    console.log(response)
      commit(FETCH_TOTALSERVICE, response.data)
      return response
    })
    .catch((error) => {
//    console.error(error)
      return error.response
    })
  },
  fetchSingleService: ({ commit }, req) => {
    // API request
//  console.log(req)
    return services.service.getSingleService(req)
    .then((response) => {
//    console.log(response)
      commit(FETCH_SINGLESERVICE, response.data)
      return response
    })
    .catch((error) => {
//    console.error(error)
      return error.response
    })
  },
  startService: ({ commit }, req) => {
    // API request
    return services.service.startService(req)
    .then((response) => {
      return response
    })
    .catch((error) => {
//    console.error(error)
      return error.response
    })
  },
  stopService: ({ commit }, req) => {
    // API request
    return services.service.stopService(req)
    .then((response) => {
      console.log(response)
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
