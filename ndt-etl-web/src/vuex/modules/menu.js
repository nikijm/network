const state = {
  main: {
    parent: {
      icon: 'fa fa-dashboard',
      name: sessionStorage.getItem('menu') ? JSON.parse(sessionStorage.getItem('menu')).parent.name : '欢迎'
    },
    icon: 'fa fa-circle-o',
    name: sessionStorage.getItem('menu') ? JSON.parse(sessionStorage.getItem('menu')).name : '欢迎'
  }
}

const mutations = {
  setMenu (state, menu) {
    state.main = menu
  }
}

const actions = {
  setMenu (context, menu) {
    context.commit('setMenu', menu)
  }
}

export default {
  state,
  mutations,
  actions
}
