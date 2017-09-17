// import * as types from '../mutation-types'
import { services } from '../api'
const FETCH_SHEETS = 'FETCH_SHEETS'
const FETCH_LISTSCLEAN = 'FETCH_LISTSCLEAN'
const FETCH_LISTCACHED = 'FETCH_LISTCACHED'
const FETCH_SHEET = 'FETCH_SHEET'
const FETCH_TLIST = 'FETCH_TLIST'
const FETCH_LISTTRANSFORM = 'FETCH_LISTTRANSFORM'
const FETCH_UNCLEAN = 'FETCH_UNCLEAN'
const FETCH_CLEANED = 'FETCH_CLEANED'
const FETCH_LISTVERIFY = 'FETCH_LISTVERIFY'
const FETCH_LOGS = 'FETCH_LOGS'
const FETCH_MIRRORS = 'FETCH_MIRRORS'
const FETCH_MIRRORSTABLE = 'FETCH_MIRRORSTABLE'

const getters = {
  fetchSheetsData: state => state.all,
  fetchListsCleanData: state => state.main,
  fetchCachedData: state => state.cached,
  fetchSheetData: state => state.sheet,
  fetchListData: state => state.sheetList,
  fetchTransformData: state => state.transform,
  fetchOriginalData: state => state.originalData,
  fetchVerifyData: state => state.verify,
  fetchCleanedData: state => state.cleanedData,
  fetchLogsData: state => state.logs,
  fetchMirrorsData: state => state.mirrors,
  fetchMirrorsTableData: state => state.mirrorsTable
}

const state = {
  main: {
    paging: {
      current: 0,
      total: 0,
      page_size: 15
    },
    sheets: []
  },
  all: {
    paging: {
      current: 0,
      total: 0,
      page_size: 15
    },
    sheets: []
  },
  cached: {
    paging: {
      current: 0,
      total: 0,
      page_size: 15
    },
    uploads: []
  },
  sheet: {
    columns: [],
    id: 0,
    status: '',
    upload: {
      user: {}
    }
  },
  sheetList: {
    paging: {
      current: 0,
      total: 0,
      page_size: 15
    },
    data: []
  },
  transform: {
    paging: {
      current: 0,
      total: 0,
      page_size: 15
    },
    sheets: []
  },
  verify: {
    paging: {
      current: 0,
      total: 0,
      page_size: 15
    },
    sheets: []
  },
  originalData: {
    paging: {
      current: 0,
      total: 0,
      page_size: 15
    },
    data: []
  },
  cleanedData: {
    paging: {
      current: 0,
      total: 0,
      page_size: 15
    },
    data: []
  },
  logs: {},
  mirrorsTable: {
    paging: {
      current: 0,
      total: 0,
      page_size: 15
    },
    data: []
  },
  mirrors: {
    name: '',
    columns: [{
      name: ''
    }]
  }
}

const mutations = {
  [FETCH_SHEETS] (state, data) {
    state.all = data
  },
  [FETCH_LISTSCLEAN] (state, data) {
    state.main = data
  },
  /**
   * 数据缓存
   */
  [FETCH_LISTCACHED] (state, data) {
    if (typeof data.paging.page_size === 'undefined') {
      data.paging.page_size = 15
    }
    state.cached = data
  },
  /**
   * 获取单个工作表
   */
  [FETCH_SHEET] (state, data) {
    state.sheet = data
  },
  /**
   * 获取单个工作表数据
   */
  [FETCH_TLIST] (state, data) {
    if (typeof data.paging.page_size === 'undefined') {
      data.paging.page_size = 15
    }
    state.sheetList = data
  },
  /**
   * 获取数据转换工作表数据
   */
  [FETCH_LISTTRANSFORM] (state, data) {
    if (typeof data.paging.page_size === 'undefined') {
      data.paging.page_size = 15
    }
    state.transform = data
  },
  /**
   * 获取数据校验工作表数据
   */
  [FETCH_LISTVERIFY] (state, data) {
    if (typeof data.paging.page_size === 'undefined') {
      data.paging.page_size = 15
    }
    state.verify = data
  },
  /**
   * 获取单个工作表未清洗数据
   */
  [FETCH_UNCLEAN] (state, data) {
    if (typeof data.paging.page_size === 'undefined') {
      data.paging.page_size = 15
    }
    state.originalData = data
  },
  /**
   * 获取单个工作表清洗后数据
   */
  [FETCH_CLEANED] (state, data) {
    if (typeof data.paging.page_size === 'undefined') {
      data.paging.page_size = 15
    }
    state.cleanedData = data
  },
  /**
   * 获取单个工作表的操作日志
   */
  [FETCH_LOGS] (state, data) {
    state.logs = data
  },
  /**
   * 获取镜像表
   */
  [FETCH_MIRRORS] (state, data) {
    state.mirrors = data
  },
  [FETCH_MIRRORSTABLE] (state, data) {
    if (typeof data.paging.page_size === 'undefined') {
      data.paging.page_size = 15
    }
    state.mirrorsTable = data
  }
}

const actions = {
  [FETCH_SHEETS] ({commit}, listData) {
    return services.sheets.list(listData)
    .then((response) => {
      let res = response
      commit(FETCH_SHEETS, res)
    })
  },
  [FETCH_LISTSCLEAN] ({commit}, listData) {
    return services.sheets.listsClean(listData)
    .then((response) => {
      let res = response
      commit(FETCH_LISTSCLEAN, res)
    })
  },
  [FETCH_LISTCACHED] ({commit}, payload) {
    return services.sheets.listsCached(payload)
    .then((response) => {
      let res = response
      commit(FETCH_LISTCACHED, res)
    })
  },
  /**
   * 获取单个工作表
   */
  [FETCH_SHEET] ({commit}, id) {
    return services.sheets.getSheets(id)
    .then((response) => {
      let res = response
      commit(FETCH_SHEET, res)
    })
  },
  /**
   * 获取单个工作表数据
   */
  [FETCH_TLIST] ({commit}, payload) {
    return services.sheets.getSheetsList(payload)
    .then((response) => {
      let res = response
      commit(FETCH_TLIST, res)
    })
  },
  /**
   * 获取数据转换工作表数据
   */
  [FETCH_LISTTRANSFORM] ({commit}, payload) {
    return services.sheets.listTransform(payload)
    .then((response) => {
      let res = response
      commit(FETCH_LISTTRANSFORM, res)
    })
  },
  /**
   * 获取数据校验工作表数据
   */
  [FETCH_LISTVERIFY] ({commit}, payload) {
    return services.sheets.listVerify(payload)
    .then((response) => {
      let res = response
      commit(FETCH_LISTVERIFY, res)
    })
  },
  /**
   * 获取单个工作表未清洗数据
   */
  [FETCH_UNCLEAN] ({commit}, payload) {
    return services.sheets.getOriginalData(payload)
    .then((response) => {
      let res = response
      commit(FETCH_UNCLEAN, res)
    })
  },
  /**
   * 获取单个工作表清洗后数据
   */
  [FETCH_CLEANED] ({commit}, payload) {
    return services.sheets.getCleanedData(payload)
    .then((response) => {
      let res = response
      commit(FETCH_CLEANED, res)
    })
  },
  /**
   * 获取单个工作表的操作日志
   */
  [FETCH_LOGS] ({commit}, id) {
    return services.sheets.getLogs(id)
    .then((response) => {
      let res = response
      commit(FETCH_LOGS, res)
    })
  },
  /**
   * 获取镜像表
   */
  [FETCH_MIRRORS] ({commit}) {
    return services.sheets.getMirrors()
    .then((response) => {
      let res = response
      commit(FETCH_MIRRORS, res)
    })
  },
  [FETCH_MIRRORSTABLE] ({commit}, payload) {
    return services.sheets.getMirrorsTableData(payload)
    .then((response) => {
      let res = response
      commit(FETCH_MIRRORSTABLE, res)
    })
  }

}

export default {
  getters,
  state,
  mutations,
  actions
}
