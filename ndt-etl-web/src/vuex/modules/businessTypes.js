import { services } from '../api'
// mutation-types
const GETBUSINESSTYPES = 'GETBUSINESSTYPES' // 所有资源
const state = {
  businessTypes: ''
}

const getters = {
  allBusinessTypes: state => state.businessTypes
}

const mutations = {
  /**
   * 获取所有业务类型的mutation
   */
  [GETBUSINESSTYPES] (state, payload) {
    state.businessTypes = payload
  }
}

const actions = {
  /**
   * 获取所有业务类型的action
   */
  getBusinessTypes ({commit}, payload) {
    return services.auth.getBusinessTypes(payload)
      .then((response) => {
        commit(GETBUSINESSTYPES, response.data)
        return response
      })
      .catch((error) => {
        return error.response
      })
  },

  /**
   * 修稿业务类型的action
   */
  updateBusinessTypes ({commit}, payload) {
    return services.auth.updateBusinessTypes(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },

  /**
   * 新增业务类型的action
   */
  addBusinessTypes ({commit}, payload) {
    return services.auth.addBusinessTypes(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },

  /**
   * 新增业务类型的action
   */
  delBusinessTypes ({commit}, payload) {
    return services.auth.delBusinessTypes(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },

  /**
   * 获取业务类型的action
   */
  getBusinessList ({commit}, payload) {
    return services.dataService.getBusinessList(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
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
