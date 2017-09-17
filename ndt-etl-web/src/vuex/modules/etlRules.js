import { services } from '../api'

const FETCH_VALIDATORS = 'FETCH_VALIDATORS'
const FETCH_OPERATIONS = 'FETCH_OPERATIONS'
const FETCH_TEMPLATES = 'FETCH_TEMPLATES'
const FETCH_TEMPLATE = 'FETCH_TEMPLATE'
const FETCH_RULEOPS = 'FETCH_RULEOPS'

const state = {
  main: [],
  operations: [],
  templates: {
    paging: {
      current: 0,
      total: 0,
      page_size: 15
    },
    templates: []
  },
  template: {},
  ruleOps: {}
}

const getters = {
  fetchValidatorsData: state => state.main,
  fetchOperationsData: state => state.operations,
  fetchTemplatesData: state => state.templates,
  fetchTemplateData: state => state.template,
  fetchRuleOpsData: state => state.ruleOps
}

const mutations = {
  [FETCH_VALIDATORS] (state, data) {
    state.main = data
  },
  [FETCH_OPERATIONS] (state, data) {
    state.operations = data
  },
  [FETCH_TEMPLATES] (state, data) {
    if (typeof data.paging.page_size === 'undefined') {
      data.paging.page_size = 15
    }
    state.templates = data
  },
  [FETCH_TEMPLATE] (state, data) {
    state.template = data
  },
  [FETCH_RULEOPS] (state, data) {
    state.ruleOps = data
  }
}

const actions = {
  fetchValidators: (context) => {
    if (context.state.main.length > 0) {
      return Promise.resolve()
    }
    return services.etlRules.getValidators()
    .then((data) => {
      context.commit(FETCH_VALIDATORS, data)
    })
  },
  fetchOperations: (context) => {
    if (context.state.operations.length > 0) {
      return Promise.resolve()
    }
    return services.etlRules.getOperations()
    .then((data) => {
      context.commit(FETCH_OPERATIONS, data)
    })
  },
  fetchTemplates: (context, payload) => {
    return services.etlRules.getTemplates(payload)
    .then((data) => {
      context.commit(FETCH_TEMPLATES, data)
    })
  },
  fetchTemplate: (context, id) => {
    return services.etlRules.getTemplate(id)
    .then((data) => {
      context.commit(FETCH_TEMPLATE, data)
    })
  },
  fetchRuleOps: (context) => {
    return services.etlRules.getRuleOps()
    .then((data) => {
      console.log(data)
      context.commit(FETCH_RULEOPS, data)
    })
  }
}

export default {
  state,
  actions,
  getters,
  mutations
}
