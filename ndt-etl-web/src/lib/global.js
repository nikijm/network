// 用户名验证
export const USERNAME_PATTERN = /^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){3,15}$/

// 密码验证
export const PASSWORD_PATTERN = /^((?=.*[a-z])(?=.*\d)|(?=[a-z])(?=.*[#@!~%^&*\-+])|(?=.*\d)(?=.*[#@!~%^&*\-+]))[a-zA-Z\d#@!~%^&*\-+]{9,20}$/

// 手机号码验证正则
export const PHONENO_PATTERN = /^|^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[0])|(18[0,5-9]))\\d{8}$/

// IP 验证
export const IP_PATTERN = /^(?:(?:2[0-4][0-9]\.)|(?:25[0-5]\.)|(?:1[0-9][0-9]\.)|(?:[1-9][0-9]\.)|(?:[0-9]\.)){3}(?:(?:2[0-4][0-9])|(?:25[0-5])|(?:1[0-9][0-9])|(?:[1-9][0-9])|(?:[0-9]))$/

// 名称验证
export const NAME_PATTERN = /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/
