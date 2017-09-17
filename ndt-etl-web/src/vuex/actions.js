import * as types from './mutation-types'
import { services } from './api'

export const fetchProduct = ({ commit }) => {
  // API request
  return services.products.getAll()
  .then((response) => {
    commit(types.FETCH_PRODUCT, response.data)
  })
  .catch((error) => {
    console.error(error)
  })
}
export const fetchDemo = ({ commit }) => {
  // API request
  return services.demo.postDemo()
  .then((response) => {
    commit(types.FETCH_DEMO, response.data)
  })
  .catch((error) => {
    console.error(error)
  })
}

export const fetchLogin = ({ commit }) => {
  // API request
  return services.login.postLogin()
  .then((response) => {
    commit(types.FETCH_LOGIN, response.data)
  })
  .catch((error) => {
    console.error(error)
  })
}
