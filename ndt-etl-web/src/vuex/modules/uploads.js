// import * as types from '../mutation-types'
import { services } from '../api'
const FETCH_UPLOADS = 'FETCH_UPLOADS'
const FETCH_SOURCE = 'FETCH_SOURCE'
const FETCH_UPLOADS_WITH_SHEETS = 'FETCH_UPLOADS_WITH_SHEETS'

const getters = {
  fetchUploadsData: state => state.main,
  fetchSourceData: state => state.source,
  fetchUploadsWithSheetsData: state => state.uploadsWithSheets
}

const state = {
  main: {
    paging: {
      current: 0,
      total: 0,
      page_size: 15
    },
    uploads: []
  },
  source: {
  },
  uploadsWithSheets: {
    paging: {
      current: 0,
      total: 0,
      page_size: 15
    },
    uploads: []
  }
}

const mutations = {
  [FETCH_UPLOADS] (state, data) {
    if (data && data.paging) {
      if (typeof data.paging.page_size === 'undefined') {
        data.paging.page_size = 15
      }
    }
    state.main = data
  },
  [FETCH_SOURCE] (state, data) {
    if (typeof data.paging.page_size === 'undefined') {
      data.paging.page_size = 15
    }
    state.source = data
  },
  [FETCH_UPLOADS_WITH_SHEETS] (state, data) {
    if (typeof data.paging.page_size === 'undefined') {
      data.paging.page_size = 15
    }
    data.uploads.forEach((upload) => {
      if (upload.sheets[0].id === null) {
        upload.sheets = []
      }
    })
    state.uploadsWithSheets = data
  }
}

const actions = {
  [FETCH_UPLOADS] ({commit}, payload) {
    return services.uploads.list(payload)
    .then((response) => {
      // commit(FETCH_DATA, res.paging)
      commit(FETCH_UPLOADS, response)
    })
  },
  [FETCH_SOURCE] ({commit}, pagesize) {
    return services.uploads.getSource(pagesize)
    .then((response) => {
      commit(FETCH_SOURCE, response)
    })
  },
  [FETCH_UPLOADS_WITH_SHEETS] ({commit}, payload) {
    return services.uploads.listWithSheets(payload)
    .then((response) => {
      // commit(FETCH_DATA, res.paging)
      commit(FETCH_UPLOADS_WITH_SHEETS, response)
    })
  }
}

export default {
  getters,
  state,
  mutations,
  actions
}
