import { services } from '../api'

const FETCH_RULES = 'FETCH_RULES'
const FETCH_SINGLERULE = 'FETCH_SINGLERULE'
const FETCH_RULESMACHINE = 'FETCH_RULESMACHINE'
const FETCH_RULESMACHINE2 = 'FETCH_RULESMACHINE2'

const state = {
  main: [],
  singleRule: {},
  ruleMachine: [{ 'description': '所有服务器', 'id': '0' }],
  ruleMachine2: []
}

const getters = {
  totalRules2: state => state.main,
  singleRule: state => state.singleRule, //  单条访问时的数据
  totalRules2Machine: state => state.ruleMachine, //  含有所有服务器的机器组
  totalRules2Machine2: state => state.ruleMachine2//  不含有所有服务器的机器组
}

const mutations = {
  [FETCH_RULES] (state, data) {
    state.main = data
  },
  [FETCH_SINGLERULE] (state, data) {
    state.singleRule = data
  },
  [FETCH_RULESMACHINE] (state, data) {
    state.ruleMachine = [{ 'description': '所有服务器', 'id': '0' }] // 每次确保不会被之前的干扰
    state.ruleMachine = state.ruleMachine.concat(data.hostlist)
  },
  [FETCH_RULESMACHINE2] (state, data) {
    state.ruleMachine2 = data.hostlist
  }
}

const actions = {
  fetchRules2: ({ commit }, req) => {
    // API request
//  console.log(req)
    return services.rules.getRules(req)
    .then((response) => {
//    console.log(response)
      commit(FETCH_RULES, response.data)
      return response
    })
    .catch((error) => {
//    console.error(error)
      return error.response
    })
  },
  fetchRulesMachine: ({ commit }) => {
    // API request
    return services.rules.postRulesMachine()
    .then((response) => {
//    console.log(response)
      commit(FETCH_RULESMACHINE, response.data)
      return response
    })
    .catch((error) => {
//    console.error(error)
      return error.response
    })
  },
  fetchRulesMachine2: ({ commit }) => {
    // API request
    return services.rules.postRulesMachine()
    .then((response) => {
//    console.log(response)
      commit(FETCH_RULESMACHINE2, response.data)
      return response
    })
    .catch((error) => {
//    console.error(error)
      return error.response
    })
  },
  createNewRules: ({ commit }, req) => {
    // API request
    return services.rules.postCreateNewRules(req)
    .then((response) => {
      return response
    })
    .catch((error) => {
//    console.error(error)
      return error.response
    })
  },
  modifyRules: ({ commit }, req) => {
    // API request
    return services.rules.postModifyRules(req)
    .then((response) => {
      return response
    })
    .catch((error) => {
//    console.error(error)
      return error.response
    })
  },
  fetchSingleRule: ({ commit }, req) => {
    // API request
    return services.rules.postSingleRule(req)
    .then((response) => {
//    console.log(response)
      commit(FETCH_SINGLERULE, response.data)
      return response
    })
    .catch((error) => {
//    console.error(error)
      return error.response
    })
  },
  fetchDeleteRule: ({ commit }, req) => {
    // API request
    return services.rules.postDeleteRule(req)
    .then((response) => {
//    console.log(response)
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
