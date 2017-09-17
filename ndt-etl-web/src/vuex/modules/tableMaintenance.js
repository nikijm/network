import { services } from '../api'
const FETCH_TABLEMAINTENANCE = 'FETCH_TABLEMAINTENANCE'

const getters = {
  fetchTableMaintenceData: state => state.main
}

const state = {
  main: {
  }
}

const mutations = {
  [FETCH_TABLEMAINTENANCE] (state, data) {
    state.main = data
  }
}

const actions = {
  [FETCH_TABLEMAINTENANCE] ({commit}, category) {
    return services.tableMaintenance.getTableMaintenance(category)
    .then((response) => {
      commit(FETCH_TABLEMAINTENANCE, response)
    })
  }
}

export default {
  getters,
  state,
  mutations,
  actions
}
