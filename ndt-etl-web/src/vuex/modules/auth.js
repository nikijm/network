import { services } from '../api'
// mutation-types
const GETALLRESOURCESLIST = 'GETALLRESOURCESLIST' // 所有资源
const UPDATEONEROLELIST = 'UPDATEONEROLELIST' // 更新一个角色的资源表
const GETALLROLELIST = 'GETALLROLELIST' // 获取所有角色
const state = {
  resourcesList: '',
  roleList: [],
  msg: ''
}

const getters = {
  allResourcesList: state => state.resourcesList,
  allRoleList: state => state.roleList
}

const mutations = {
  /**
   * 获取所有角色的mutations
   */
  [GETALLROLELIST] (state, payload) {
    state.roleList = payload
  },

  /**
   * 获取所有资源的mutations
   */
  [GETALLRESOURCESLIST] (state, payload) {
    if (!payload.length) {
      return
    }
    payload.forEach(function (item) {
      item.checkGroup = [false, false, false, false]
      item.original_operations = []
    })
    for (let i = 0, j = payload.length; i < j; i++) {
      let option = payload[i].availableOperations
      if (!option || typeof option !== 'string') {
        option = ''
      }
      payload[i].availableOperations = option.split(' ')
      payload[i].original_operations = payload[i].availableOperations
    }
    state.resourcesList = JSON.stringify(payload)
  },
  /**
   * 更新单个角色的mutations
   */
  [UPDATEONEROLELIST] (state, payload) {
    state.msg = payload.name
  }
}

const actions = {
  /**
   * 获取所有角色列表
   */
  getAllRoleList ({commit}, payload) {
    return services.auth.getAllRoleList(payload)
    .then((response) => {
      commit(GETALLRESOURCESLIST, response.data.resources)
      return response
    })
    .catch((error) => {
      return error.response
    })
  },

  /**
   * 获取所有资源列表
   */
  getAllResourcesList ({commit}, payload) {
    return services.auth.getAllResourcesList()
    .then((response) => {
      commit(GETALLRESOURCESLIST, response.data.resources)
      return response
    })
    .catch((error) => {
      return error.response
    })
  },

  /**
   * 更新一个角色的资源表
   */
  updateOneRoleList ({commit}, payload) {
    return services.auth.updateOneRoleList(payload)
      .then((response) => {
        commit(UPDATEONEROLELIST, response.data)
        return response
      })
      .catch((error) => {
        return error.response
      })
  },

  /**
   * 新增一个角色
   */
  addOneRole ({commit}, payload) {
    return services.auth.addOneRole(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },

  /**
   * 修改单个角色名称
   */
  updateOneRoleName ({commit}, payload) {
    return services.auth.updateOneRoleName(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },

  /**
   * 删除单个角色
   */
  deleteOneRole ({commit}, payload) {
    return services.auth.deleteOneRole(payload)
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
