import Vue from 'vue'
import router from '../router.js'

/**
 * 状态码处理
 * @param res 返回的结果
 * @param fn 成功中执行的回调
 */
export const statusCallback = (res, fn, that) => {
  let vm = Vue.prototype
  if (!res) {
    vm.$message({
      message: '后台数据没有返回结果',
      type: 'error'
    })
    // console.error('后台数据没有返回结果')
    return
  }
  if (res.status) {
    switch (res.status) {
      case 500:
        vm.$message({
          message: '服务器或网络错误',
          type: 'error'
        })
        break
      case 401:
        vm.$message({
          message: '无权限',
          type: 'error'
        })
        break
      case 40317:
        vm.$message({
          message: '登录超时',
          type: 'error'
        })
        // 登录超时返回到登录页面
        router.push({ path: '/login' })
        break
      case 404:
        vm.$message({
          message: '资源未找到',
          type: 'error'
        })
        break
      case 200:
        // 响应成功
        typeof fn === 'function' && fn(that)
        break
      default:
        // 其他错误
        if (res.data) {
          if (res.data.errors) {
            if (res.data.errors.error) {
              let err = res.data.errors.error
              vm.$message({
                message: err,
                type: 'error'
              })
              return
            }
          }
          let err = res.data.message
          if (err) {
            vm.$message({
              message: err,
              type: 'error'
            })
          } else {
            vm.$message({
              message: '未知错误',
              type: 'error'
            })
          }
        } else {
          vm.$message({
            message: '未知错误',
            type: 'error'
          })
        }
    }
  } else {
    if (res.data) {
      let err = res.data.message
      if (err) {
        vm.$message({
          message: err,
          type: 'error'
        })
      } else {
        vm.$message({
          message: '未知错误',
          type: 'error'
        })
      }
    } else {
      vm.$message({
        message: '未知错误',
        type: 'error'
      })
    }
  }
}

/**
 * 判断是否为数组
 * @param v
 * @returns {boolean}
 */
export const isArray = (v) => {
  return v instanceof Array || v.constructor === Array || Object.prototype.toString.call(v) === '[object Array]'
}

/**
 * 判断元素是否在数组中
 * @param needle 目标元素
 * @param haystack 数组
 * @returns {boolean}
 */
export const inArray = (needle, haystack) => {
  if (!isArray(haystack)) {
    throw Error('the second argument is not a Array')
  } else {
    let len = haystack.length
    for (let i = 0; i < len; i++) {
      if (haystack[i] === needle) return true
    }
  }
  return false
}

/**
 * 判断是否为空，为空返回true，否则返回false。可以做递归检查
 * @param v
 * @param recursive
 * @returns {boolean}
 */
export const isNullOrEmpty = (v, recursive) => {
  if (v !== 'undefined' && v !== null) {
    switch (typeof v) {
      case 'object':
        if (!recursive) return false
        if (isArray(v)) {
          return !(v.length > 0 && v[0] !== 'undefined' && v[0] !== null)
        } else {
          for (var property in v) {
            if (v.hasOwnProperty(property)) {
              return isNullOrEmpty(v[property], recursive)
            }
          }
        }
        break
      case 'string':
        if (v.length > 0 && v !== '') {
          return false
        }
        break
      case 'number':
        if (v > 0) {
          return false
        }
        break
    }
    return true
  } else {
    return true
  }
}
