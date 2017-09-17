<template>

  <div class="login-page user-login">
    <div class="login-title">农贷通运维子系统</div>
      <va-box
        title="用户登录"
        theme="box-info"
        :isBorder="true">
        <div slot="content">
          <form class="form-horizontal" id="box">
            <input type="text" class="form-control" v-model="loginInfo.name" placeholder="用户名">
            <input type="password" class="form-control" autocomplete="off" v-model="loginInfo.password" placeholder="密码" @keyup.enter ="handleLogin">
          </form>
          <div style="text-align: center;padding: 20px 0">
            <button class="btn btn-primary" style="width: 300px;" type="button" @click="handleLogin">登   录</button>
            <!--<va-button :isBlock="false" theme="info" align="right" name="忘记密码"></va-button>-->
          </div>
        </div>

      </va-box>
  </div>
</template>

<script>
import VABox from '../components/VABox'
import VAInput from '../components/VAInput'
import VAButton from '../components/VAButton'
import { mapActions } from 'vuex'
import { statusCallback } from '../lib/utils'

export default {
  name: 'userCommonLogin',
  data () {
    return {
      loginInfo: {
        name: '',
        password: ''
      },
      isloginActive: true
    }
  },
  components: {
    'va-box': VABox,
    'va-input': VAInput,
    'va-button': VAButton
  },
  methods: {
    ...mapActions([
      'userCommonLogin'
    ]),

    /**
     * 登录操作
     */
    handleLogin: function () {
      if (!this.validateLoginInfo() || !this.isloginActive) {
        // 验证未通过或者在发起请求中
        return false
      }
      this.isloginActive = false
      let _this = this
      this.userCommonLogin(this.loginInfo).then((res) => {
        this.isloginActive = true // 登录请求结束
        statusCallback(res, function () {
          if (res && res.data) {
            sessionStorage.setItem('token', res.data.token)
            sessionStorage.setItem('name', res.data.user.name)
            sessionStorage.setItem('userFlag', 'user')
            _this.$router.push({ path: '/upload' })
            _this.$message({
              message: '登录成功',
              type: 'success'
            })
          } else {
            _this.$message({
              message: '登录失败',
              type: 'error'
            })
          }
        })
      })
    },
    validateLoginInfo () {
      let msg = ''
      if (!this.loginInfo.name || !this.loginInfo.password) {
        msg = '用户名或密码不能为空'
        this.$message({
          message: msg,
          type: 'error'
        })
        return false
      } else {
        return true
      }
    }
  }
}

</script>

<style type="text/css" scope>
.user-login{
    background: #456878;
  }
</style>
