<template>
  <header class="main-header">
    <!-- Logo -->
    <span class="logo">
      <!-- mini logo for sidebar mini 50x50 pixels -->
      <span class="logo-mini"><b style='font-size: 14px;'>农贷通</b></span>
      <!-- logo for regular state and mobile devices -->
      <span class="logo-lg">农贷通运维子系统</span>
    </span>

    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top">
      <!-- Sidebar toggle button-->
      <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
        <span class="sr-only">Toggle navigation</span>
      </a>
      <!-- Navbar Right Menu -->
      <div class="navbar-custom-menu">
        <ul class="nav navbar-nav">
          <li><a href="javascript:;" @click="saveWindow"><i class="glyphicon glyphicon-floppy-save"></i>&nbsp;保存到桌面</a></li>
          <li><a href="#"><i class="glyphicon glyphicon-user"></i>&nbsp;{{ currentUser}} {{orgName}}</a></li>
         <!--  <li v-if="currentUser !== 'admin'"><a href="javascript:;"><i class="glyphicon glyphicon-edit"></i>&nbsp;修改密码</a></li> -->
          <li><a href="javascript:;" @click="signOut"><i class="glyphicon glyphicon-log-out"></i>&nbsp;退出</a></li>
        </ul>
      </div>

    </nav>
  </header>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import axios from 'axios'

export default {
  name: 'va-navibar',
  data () {
    return {
      currentUser: sessionStorage.getItem('name'),
      orgName: ''
    }
  },
  computed: {
    ...mapGetters([
      'fetchUploadsData'
    ])
  },
  mounted () {
    this.FETCH_UPLOADS().then(() => {
      this.orgName = this.fetchUploadsData.user.orgName || ''
    })
  },
  methods: {
    ...mapActions([
      'userLoginOut',
      'FETCH_UPLOADS'
    ]),
    /**
     * 退出
     */
    signOut: function () {
      let flag = sessionStorage.getItem('userType')
      this.userLoginOut().then(() => {
        sessionStorage.removeItem('token')
        sessionStorage.removeItem('menuStr')
        sessionStorage.removeItem('name')
        if (flag) {
          // 外部用户退出
          sessionStorage.removeItem('userType')
        }
        this.$router.push('/login')
        this.$message({
          message: '退出成功',
          type: 'success'
        })
      })
    },
    /**
     * 判断浏览器类型 保存到桌面
     */
    saveWindow () {
      let userAgent = navigator.userAgent
      let isOpera = userAgent.indexOf('Opera') > -1
      let isIE = !!window.ActiveXObject || 'ActiveXObject' in window
      let isEdge = userAgent.indexOf('Edge') > -1
      let isFF = userAgent.indexOf('Firefox') > -1
      let isSafari = userAgent.indexOf('Safari') > -1 && userAgent.indexOf('Chrome') === -1
      let isChrome = userAgent.indexOf('Chrome') > -1 && userAgent.indexOf('Safari') > -1
      if (isOpera) {
        console.log('这是Opera浏览器')
      } else if (isIE) {
        console.log('这是IE浏览器')
      } else if (isEdge) {
        console.log('这是Edge浏览器')
      } else if (isFF) {
        console.log('这是Firefox浏览器')
      } else if (isSafari) {
        console.log('这是Safari浏览器')
      } else if (isChrome) {
        console.log('这是Chrome浏览器')
      } else {
        console.log('这是IE浏览器')
      }
      axios.get('/api/desktopUrl')
      .then(() => {
        window.location.href = '/api/desktopUrl'
      })
      .catch(() => {
        this.$alert('保存到桌面失败', '错误', {
          confirmButtonText: '确定',
          type: 'error'
        })
      })
    }
  }
}

</script>
