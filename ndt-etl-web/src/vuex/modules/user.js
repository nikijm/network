/* 用户的相关操作 */
import { services } from '../api'

const ADMIMS = 'ADMIMS' // 管理员列表
const GETMENUS = 'GETMENUS' // 获取菜单 登录
const GETROLElIST = 'GETROLElIST' // 获取角色列表
const OUTUSER = 'OUTUSER' // 外部用户列表
const ORGANIZATION = 'ORGANIZATION' // 获取组织机构列表

const state = {
  admin: [],
  menus: [],
  roles: [],
  outuser: [],
  organizations: {
    organizations: [],
    paging: []
  }
}
const getters = {
  allAdmin: state => state.admin,
  menusList: state => state.menus,
  allroles: state => state.roles,
  allOutUser: state => state.outuser,
  allorganizations: state => state.organizations
}
const mutations = {
  [ADMIMS] (state, payload) {
    state.admin = payload
  },
  [GETMENUS] (state, payload) {
    state.menus = payload
  },
  [GETROLElIST] (state, payload) {
    state.roles = payload
  },
  [OUTUSER] (state, payload) {
    state.outuser = payload
  },
  [ORGANIZATION] (state, payload) {
    state.organizations = payload
  }
}
const actions = {
    // 组织机构
  getOrganization ({commit}, payload) {
    return services.user.getOrganization(payload)
      .then((response) => {
        commit(ORGANIZATION, response.data)
        return response
      })
      .catch((error) => {
        commit(ORGANIZATION, error.response.data)
        return error.response
      })
  },
  // 外部用户
  getOutUser ({commit}, payload) {
    return services.user.getOutUser(payload)
      .then((response) => {
        // console.log('users', response)
        commit(OUTUSER, response.data)
        return response
      })
      .catch((error) => {
        commit(OUTUSER, error.response.data)
        return error.response
      })
  },
  // 管理员列表
  getAdmin ({commit}, payload) {
    return services.user.getAdmin(payload)
      .then((response) => {
        commit(ADMIMS, response.data)
        return response
      })
      .catch((error) => {
        commit(ADMIMS, error.response.data)
        return error.response
      })
  },
  /* 获取角色列表 */
  getRoles ({commit}, payload) {
    return services.user.getRoles(payload)
      .then((response) => {
        commit(GETROLElIST, response.data.roles)
        return response
      })
      .catch((error) => {
        commit(GETROLElIST, error.response.data)
        return error.response
      })
  },
  // 管理员增改删
  addAdmin ({commit}, payload) {
    return services.user.addAdmin(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  updataAdmin ({commit}, payload) {
    return services.user.updataAdmin(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  deleteAdmin ({commit}, payload) {
    return services.user.deleteAdmin(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  // 外部用户增改删
  addUsers ({commit}, payload) {
    return services.user.addUsers(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  updataUsers ({commit}, payload) {
    return services.user.updataUsers(payload)
    .then((response) => {
      return response
    })
    .catch((error) => {
      return error.response
    })
  },
  deleteUsers ({commit}, payload) {
    return services.user.deleteUsers(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  // 组织机构增改删
  addOrg ({commit}, payload) {
    return services.user.addOrg(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  updateOrg ({commit}, payload) {
    return services.user.updateOrg(payload)
    .then((response) => {
      return response
    })
    .catch((error) => {
      return error.response
    })
  },
  uploadLogo ({commit}, payload) {
    return services.user.uploadLogo(payload)
    .then((response) => {
      return response
    })
    .catch((error) => {
      return error.response
    })
  },
  deleteOrg ({commit}, payload) {
    return services.user.deleteOrg(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  /**
   * 登录的action
   */
  userLogin ({commit}, payload) {
    return services.user.userLogin(payload)
      .then((response) => {
        let menusArr = []
        if (response && response.data) {
          menusArr = menusArr.concat(response.data.user.menus)
          // console.log('menusArr', menusArr)
          sessionStorage.setItem('menuStr', JSON.stringify({menusArr: menusArr}))
          commit(GETMENUS, menusArr)
        }
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  /**
   * 外部用户登录的action
   */
  userCommonLogin ({commit}, payload) {
    return services.user.userCommonLogin(payload)
      .then((response) => {
        let menusArr = []
        if (response && response.data) {
          menusArr = menusArr.concat(response.data.user.menus)
          sessionStorage.setItem('menuStr', JSON.stringify({menusArr: menusArr}))
          commit(GETMENUS, menusArr)
        }
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  /**
   * 用户退出action
   */
  userLoginOut ({commit}, payload) {
    payload = {
      token: sessionStorage.getItem('token')
    }
    return services.user.userLoginOut(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
