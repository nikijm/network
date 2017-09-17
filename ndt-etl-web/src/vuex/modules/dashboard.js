import { services } from '../api'
// 0-2分别代表cpu，memory，disk数据
const FETCH_DASHBOARD0 = 'FETCH_DASHBOARD0'
const FETCH_DASHBOARD1 = 'FETCH_DASHBOARD1'
const FETCH_DASHBOARD2 = 'FETCH_DASHBOARD2'
const SETTIMER = 'SETTIMER'

const state = {
  dashboard0: [],
  dashboard1: [],
  dashboard2: [],
  timerArr: 0
}

const getters = {
  totalDashboard0: state => state.dashboard0,
  totalDashboard1: state => state.dashboard1,
  totalDashboard2: state => state.dashboard2,
  timerArr: state => state.timerArr
}

const mutations = {
  [FETCH_DASHBOARD0] (state, data) {
//  console.log(0)
    state.dashboard0 = data
  },
  [FETCH_DASHBOARD1] (state, data) {
//  console.log(1)
    state.dashboard1 = data
  },
  [FETCH_DASHBOARD2] (state, data) {
//  console.log(2)
    state.dashboard2 = data
  },
  [SETTIMER] (state, data) {
    state.timerArr = data
  }
}

const actions = {
  fetchDashboard: ({ commit }, info) => {
    // API request
//  info['token'] = 'vvqjm5ppUQ1YqMBFcOLQF5TUMaUIpvqWUNRg6fZS'
    return services.dashboard.postDashboard(info)
    .then((response) => {
//    console.log(response)
      if (info.monitorCompunent === 'cpu') {
        commit(FETCH_DASHBOARD0, response.data)
        return response
      } else if (info.monitorCompunent === 'memory') {
        commit(FETCH_DASHBOARD1, response.data)
        return response
      } else if (info.monitorCompunent === 'disk') {
        commit(FETCH_DASHBOARD2, response.data)
        return response
      }
    })
    .catch((error) => {
//    console.error(error)
      return error.response
    })
  },
  clearDashboard: ({ commit }, num) => {
    return services.dashboard.clearMemory(num)
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
