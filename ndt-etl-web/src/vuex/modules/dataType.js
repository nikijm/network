import { services } from '../api'

const state = {
  // Orgs: JSON.parse(sessionStorage.getItem('typeData'))
}

const getters = {
  // fetchDatasType: state => state.Orgs
}

const mutations = {
}

const actions = {
  /**
   * 获取所有数据文件类型维护
   * @param commit
   * @param payload
   * @returns {Promise.<T>}
   */
  getDataFileTypes: ({commit}, payload) => {
    return services.dataService.getDataFileTypes(payload)
    .then((response) => {
      return response
    })
    .catch((error) => {
      return error.response
    })
  },
  /**
   * 增加数据文件类型
   * @param commit
   * @param payload
   * @returns {Promise.<T>}
   */
  addDataFileType: ({commit}, payload) => {
    return services.dataService.addDataFileType(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  /**
   * 修改数据文件类型
   * @param commit
   * @param payload
   * @returns {Promise.<T>}
   */
  updateDataFileType: ({commit}, payload) => {
    return services.dataService.updateDataFileType(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  /**
   * 删除数据文件类型
   * @param commit
   * @param payload
   * @returns {Promise.<T>}
   */
  deleteDataFileType: ({commit}, payload) => {
    return services.dataService.deleteDataFileType(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  /**
   * 获取生成缺省的action
   * @param commit
   * @param payload
   * @returns {Promise.<T>}
   */
  getFieldAlign: ({commit}, payload) => {
    return services.dataService.getFieldAlign(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  /**
   * 获取源表类型的action
   * @param commit
   * @param payload
   * @returns {Promise.<T>}
   */
  getSourceSheetType: ({commit}, payload) => {
    return services.dataService.getSourceSheetType(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  /**
   * 获取规则列映射的action
   * @param commit
   * @param payload
   * @returns {Promise.<T>}
   */
  getRuleUses: ({commit}, payload) => {
    return services.dataService.getRuleUses(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  /**
   * 修改规则列映射
   * @param commit
   * @param payload
   * @returns {Promise.<T>}
   */
  updateRuleUses: ({commit}, payload) => {
    return services.dataService.updateRuleUses(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  /**
   * 获取任务调度总数据
   * @param commit
   * @param payload
   * @returns {Promise.<T>}
   */
  getDataFiles: ({commit}, payload) => {
    return services.dataService.getDataFiles(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  getOneDataFiles: ({commit}, payload) => {
    return services.dataService.getOneDataFiles(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  startOneDataFiles: ({commit}, payload) => {
    return services.dataService.startOneDataFiles(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  pauseOneDataFiles: ({commit}, payload) => {
    return services.dataService.pauseOneDataFiles(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  stopOneDataFiles: ({commit}, payload) => {
    return services.dataService.stopOneDataFiles(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  changeAllDataFiles: ({commit}, payload) => {
    return services.dataService.changeAllDataFiles(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  getDataDispatchs: ({commit}, payload) => {
    return services.dataService.getDataDispatchs(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  // 错误信息提示
  errorMessage: ({commit}, payload) => {
    return services.dataService.errorMessage(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  getallseehtsCachesData: ({commit}, payload) => {
    return services.dataService.getallseehtsCachesData(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  getComparesData: ({commit}, payload) => {
    return services.dataService.getComparesData(payload)
      .then((response) => {
        return response
      })
      .catch((error) => {
        return error.response
      })
  },
  getRuleUsers: ({commit}, payload) => {
    return services.dataService.getRuleUsers(payload)
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
  actions,
  getters,
  mutations
}
