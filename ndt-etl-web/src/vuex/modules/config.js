import { services } from '../api'

const FETCH_SINGLECONFIGMACHINE = 'FETCH_SINGLECONFIGMACHINE'
const FETCH_TOTALCONFIGMACHINE = 'FETCH_TOTALCONFIGMACHINE'

const state = {
  singleConfigMachine: {},
  totalConfigMachine: {}
}

const getters = {
  singleConfigMachine: state => state.singleConfigMachine, //  单条访问时的数据
  totalConfigMachine: state => state.totalConfigMachine //  含有所有服务器的服务器组
}

const mutations = {
  [FETCH_SINGLECONFIGMACHINE] (state, data) {
    state.singleConfigMachine = data.data
  },
  [FETCH_TOTALCONFIGMACHINE] (state, data) {
    state.totalConfigMachine = data
  }
}

const actions = {
  fetchSingleConfigMachine: ({ commit }, req) => {
    // API request
//  console.log(req)
    return services.config.getSingleConfigMachine(req)
    .then((response) => {
//    console.log(response)
      commit(FETCH_SINGLECONFIGMACHINE, response.data)
      return response
    })
    .catch((error) => {
//    console.error(error)
      return error.response
    })
  },
  fetchTotalConfigMachine: ({ commit }) => {
    // API request
    return services.config.getTotalConfigMachine()
    .then((response) => {
//    console.log(response)
      commit(FETCH_TOTALCONFIGMACHINE, response.data)
      return response
    })
    .catch((error) => {
//    console.error(error)
      return error.response
    })
  },
  modifyConfigMachine: ({ commit }, req) => {
    // API request
    return services.config.postModifyConfigMachine(req)
    .then((response) => {
      return response
    })
    .catch((error) => {
//    console.error(error)
      return error.response
    })
  },
  createConfigMachine: ({ commit }, req) => {
    // API request
    return services.config.postCreateConfigMachine(req)
    .then((response) => {
      return response
    })
    .catch((error) => {
//    console.error(error)
      return error.response
    })
  },
  deleteConfigMachine: ({ commit }, req) => {
    // API request
    console.log(req)
    return services.config.deleteConfigMachine2(req)
    .then((response) => {
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
