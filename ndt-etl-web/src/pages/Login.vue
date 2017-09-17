<template>
<div class="login-page">
  <div class="login-title">农贷通运维子系统</div>
  <va-box
        title="管理员登录"
        theme="box-info"
        :isBorder="true">
        <div slot="content">
          <form class="form-horizontal" id="box">
              <input type="text" class="form-control" id="inputEmail3"  v-model="loginInfo.name" placeholder="用户名">
              <input type="password" autocomplete="off" class="form-control" id="inputPassword3"  v-model="loginInfo.password" placeholder="密码" @keyup.enter ="handleLogin">
          </form>
          <div style="text-align: center;padding: 20px 0">
            <button class="btn btn-primary" type="button" @click="handleLogin">登   录</button>
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
  name: 'login',
  data () {
    return {
      loginInfo: {
        name: '',
        password: ''
      },
      isloginActive: true
    }
  },
  created () {
  },
  components: {
    'va-box': VABox,
    'va-input': VAInput,
    'va-button': VAButton
  },
  methods: {
    ...mapActions([
      'userLogin'
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
      this.userLogin(this.loginInfo).then((res) => {
        this.isloginActive = true // 登录请求结束
        statusCallback(res, function () {
          if (res && res.data) {
            sessionStorage.setItem('token', res.data.token)
            sessionStorage.setItem('name', res.data.user.name)
            if (res.data.userType) {
              sessionStorage.setItem('userType', res.data.userType)
              _this.$router.push({ path: '/data/uploads' })
            } else {
              _this.$router.push({ path: '/dashboard' })
            }
            _this.setMenu()
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
    },
    setMenu () {
      let menu = {}
      menu = {
        parent: {
          icon: 'fa fa-dashboard',
          name: '运维'
        },
        icon: 'fa fa-circle-o',
        name: '控制台'
      }
      this.$store.dispatch('setMenu', menu)
      sessionStorage.setItem('menu', JSON.stringify(menu))
    }
  }
}

</script>

<style type="text/css">
 .login-page{
      background: top 100% right 100% no-repeat,linear-gradient(hsla(215,75%,19%,1),#1561a4 33.19%,#195691 81.51%,hsla(212,74%,30%,1));
      background-attachment: fixed;
      background-size: contain;
      height: 100%;
      min-height: 100%;
      text-align: center;
      padding-top:50px;
    }
    .login-title{
      font-size:32px;
      color:#EFEFEF;
      font-weight:500;
      margin:20px 0;
    }
   .login-page .box{
      border-radius: 6px;
      display: inline-block;
      width:400px;
      background:#fafafa
  }
  .login-page .box-title{
    font-weight: 500;
  }
   .login-page .box-body{
    padding:20px 40px;
  }
 .login-page .form-control{
    height: 50px;
    border-radius:0;
    font-size:14px;
  }
  .login-page input:first-child{
    border-bottom:0;
    border-top-left-radius: 6px;
    border-top-right-radius: 6px;
  }
   .login-page input:last-child{
    border-bottom-left-radius: 6px;
    border-bottom-right-radius: 6px;
  }
  .login-page .btn{
    width: 100%;
    padding:10px;
    border-radius: 0;
  }
</style>
