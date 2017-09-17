<template>
  <page :title="title">
    <div slot="content">
      <table class="table table-hover">
        <thead>
        <tr>
          <th>服务模块</th>
          <th>服务描述</th>
          <th>是否运行</th>
          <th width="120">操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item, index) in totalService.data">
          <td class='break'><div class='route-jump' @click='jumpCreate(item.id)'>{{item.serviceName}}</div></td>
          <td class='break'>{{item.description}}</td>
          <td class='break'>
            <span class="run-status" v-show='item.statu===1'><i class="running"></i>&nbsp;运行中</span>
            <span class="run-status" v-show='item.statu===3'><i class="stop"></i>&nbsp;未运行</span>
            <span class="run-status" v-show='item.statu===2'><i class="process"></i>&nbsp;处理中</span>
          </td>
          <td class='break'>
            <button class="btn btn-xs btn-success" v-show='item.statu===3' @click='startRun(item.id)'>启动</button>
            <button class="btn btn-xs btn-warning" v-show='item.statu===1' @click='startRun(item.id)'>重启</button>
            <button class="btn btn-xs btn-danger" v-show='item.statu===1' @click='stopRun(item.id)'>停止</button>
          </td>
        </tr>
        </tbody>
      </table>
      <!--<div class="col-md-12" style='text-align: center;' v-show='isLoading'>数据请求中</div>-->
      <div class="col-md-12" style='text-align: center;' v-show='noData<=0'>暂无数据</div>
    </div>
  </page>
</template>

<script>
import Page from '../../components/Page.vue'
import VATable from '../../components/VATable.vue'
import { mapGetters, mapActions } from 'vuex'
import { statusCallback } from '../../lib/utils'
export default {
  data () {
    return {
      title: '服务状况',
      noData: 1,
      timer: null
    }
  },
  methods: {
    jumpCreate (num) {
      this.$router.push('/createservice?id=' + num)
    },
    startRun (num) {
      let id0 = parseInt(num)
      let vm = this
      this.$confirm('确认启动?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('startService', id0).then(function (res) {
          statusCallback(res, function (that) {
            vm.fetchTotalService().then((res) => {
              statusCallback(res, function (that) {
                that.$message({
                  message: '操作成功',
                  type: 'success'
                })
              }, vm)
            })
          }, this)
        })
      }).catch(() => {
        // 取消操作
      })
    },
    stopRun (num) {
      clearTimeout(this.timer)
      clearTimeout(this.serveTimer)
      let id0 = parseInt(num)
      let vm = this
      this.$confirm('确认停止?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('stopService', id0).then(function (res) {
          statusCallback(res, function (that) {
            vm.fetchTotalService().then((res) => {
              statusCallback(res, function (that) {
                that.timer = setTimeout(() => {
                  that.load()
                }, 1000)// 默认设定为1秒，页面自动请求一次。
                that.$store.commit('SETSERVETIMER', that.timer)// 计时器计数时，防止开发环境热替换出问题，故将timer写入store以便于删除操作
                that.$message({
                  message: '操作成功',
                  type: 'success'
                })
              }, vm)
            })
          }, this)
        })
      }).catch(() => {
        // 取消操作
      })
    },
    load () {
      clearTimeout(this.timer)
      clearTimeout(this.serveTimer)
      this.fetchTotalService().then((res) => {
        statusCallback(res, function (that) {
          if (that.totalService.data) {
            that.noData = that.totalService.data.length
            for (let i = 0; i < that.totalService.data.length; i++) {
              if (that.totalService.data[i].statu === 2) {
                that.timer = setTimeout(() => {
                  that.load()
                }, 1000)// 默认设定为1秒，页面自动请求一次。
                that.$store.commit('SETSERVETIMER', that.timer)// 计时器计数时，防止开发环境热替换出问题，故将timer写入store以便于删除操作
                return
              }
            }
          }
        }, this)
      })
    },
    ...mapActions([
      'fetchTotalService'
    ])
  },
  beforeRouteLeave (to, from, next) {
    clearTimeout(this.timer)
    clearTimeout(this.serveTimer)
    next()
  },
  components: {
    'va-table': VATable,
    'page': Page
  },
  computed: {
    ...mapGetters([
      'totalService',
      'isLoading',
      'serveTimer'
    ])
  },
  mounted () {
    clearTimeout(this.timer)
    clearTimeout(this.serveTimer)
    this.load()
  }
}
</script>
<style scoped>
.run-status i{
  width: 16px;
  height: 16px;
  display: -moz-inline-stack;
  display: inline-block;
  vertical-align: middle;
  zoom: 1;
  border-radius: 16px;
  vertical-align: middle;
  margin-top: -1px;
}
.run-status i.running{
  background-color: #2CAE00;
}
.run-status i.stop{
  background-color: red;
}
.run-status i.process{
  background-color: yellow;
}
.chart-tool{
  padding:10px;
  text-align: right;
  background-color: #eee;
}
.chart-title{
  font-size:16px;
  font-weight: 500;
  text-align: center;
  margin-top:10px;
  margin-bottom: 10px;
}
.nodata{
  position:absolute;
  z-index:2;
  width:100%;
  height:100%;
  text-align: center;
  top:200px
}
.break{word-wrap: break-word;-ms-word-wrap:break-word;}
</style>
