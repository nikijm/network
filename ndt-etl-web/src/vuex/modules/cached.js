import { services } from '../api'
// const FETCH_CACHES = 'FETCH_CACHES'
// const FETCH_CLEANS = 'FETCH_CLEANS'
// const FETCH_CONVERTS = 'FETCH_CONVERTS'
// const FETCH_VERITYS = 'FETCH_VERITYS'
// // const FETCH_DATA = 'FETCH_DATA'
// // const FETCH_ORG = 'FETCH_ORG'
// const state = {
//   CachesData: [],
//   CleansData: [],
//   ConvertsData: [],
//   VeritysData: []
//   // DatasType: sessionStorage.getItem('typeData') ? JSON.parse(sessionStorage.getItem('typeData')).bussinessTypes : [],
//   // Orgs: sessionStorage.getItem('typeData') ? JSON.parse(sessionStorage.getItem('typeData')).orgs : []
// }
// const getters = {
//   // fetchDatasType: state => state.DatasType,
//   // fetchOrgs: state => state.Orgs,
//   fetchCachesData: state => state.CachesData,
//   fetchCleansData: state => state.CleansData,
//   fetchConvertsData: state => state.ConvertsData,
//   fetchVeritysData: state => state.VeritysData
// }
// const mutations = {
//   [FETCH_CACHES] (state, payload) {
//     state.CachesData = payload
//   },
//   [FETCH_CLEANS] (state, payload) {
//     state.CleansData = payload
//   },
//   [FETCH_CONVERTS] (state, payload) {
//     state.ConvertsData = payload
//   },
//   [FETCH_VERITYS] (state, payload) {
//     state.VeritysData = payload
//   }
//   // [FETCH_DATA] (state, payload) {
//   //   state.DatasType = payload
//   // },
//   // [FETCH_ORG] (state, payload) {
//   //   state.Orgs = payload
//   // }
// }
const actions = {
  getCachesData ({commit}, payload) {
    return services.cached.getCachesData(payload)
      .then((response) => {
        // commit(FETCH_CACHES, response.data)
        return response.data
      })
      .catch((error) => {
        // commit(FETCH_CACHES, error.response.data)
        return error.response
      })
  },
  getCleansData ({commit}, payload) {
    return services.cached.getCleansData(payload)
    .then((response) => {
      // console.log('response', response)
      // commit(FETCH_CLEANS, response.data)
      return response.data
    })
    .catch((error) => {
      // commit(FETCH_CLEANS, error.response.data)
      return error.response
    })
  },
  getConvertsData ({commit}, payload) {
    return services.cached.getConvertsData(payload)
    .then((response) => {
      // console.log('response', response)
      // commit(FETCH_CONVERTS, response.data)
      return response.data
    })
    .catch((error) => {
      // commit(FETCH_CONVERTS, error.response.data)
      return error.response
    })
  },
  getVeritysData ({commit}, payload) {
    return services.cached.getVeritysData(payload)
    .then((response) => {
      // console.log('response', response)
      // commit(FETCH_VERITYS, response.data)
      return response.data
    })
    .catch((error) => {
      // commit(FETCH_VERITYS, error.response.data)
      return error.response
    })
  }
}
export default {
  // state,
  // getters,
  // mutations,
  actions
}

