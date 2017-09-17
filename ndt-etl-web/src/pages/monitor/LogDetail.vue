<template>
  <page :title="title" :isLoading="isLoading">
    <div slot="content">
      <h3 style='padding-bottom:10px;'>{{singleLogs.dateTimeView}}</h3>
      <!--<div style='display:flex;justify-content: flex-start;margin-bottom:20px;margin-top:20px'>-->
      <div class='item'>&nbsp;操作用户： {{singleLogs.userName}}</div>
      <div class='item'>&nbsp;级别：
        <span class='badge info' v-show='singleLogs.logLevel===1'>记录</span>
        <span class='badge warning' v-show='singleLogs.logLevel===2'>警告</span>
        <span class='badge error' v-show='singleLogs.logLevel===3'>错误</span>
        <span class='badge fatal' v-show='singleLogs.logLevel===4'>内存溢出</span>
      </div>
      <div class='item'>&nbsp;URL地址：<span> {{singleLogs.requestUri}} </span></div>
      <!--</div>-->
      <div class='main'>{{singleLogs.descriptionLog}}</div>
      <el-button @click="backToLogs" style='margin-top:30px;'>返回</el-button>
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
      title: '日志详情'
    }
  },
  methods: {
    ...mapActions([
      'fetchSingleLogs'//  发起查询日志的action
    ]),
    backToLogs () {
      this.$router.push('./logs')
    }
  },
  components: {
    'va-table': VATable,
    'page': Page
  },
  computed: {
    ...mapGetters([
      'singleLogs',
      'isLoading'
    ])
  },
  mounted () {
    if (location.hash.split('=')[1]) {
      this.idNum = location.hash.split('=')[1]
      this.fetchSingleLogs(this.idNum).then((res) => {
        if (res.data.rule === null || res.data === undefined) {
          this.$message({
            message: '无效数据信息',
            type: 'error'
          })
          return
        }
        statusCallback(res)
      })
    }
  }
}
</script>
<style scoped>
  .break{word-wrap: break-word;-ms-word-wrap:break-word ;}
  .item{margin-right:20px;color:#666;font-size:14px;margin-top:10px;}
  .badge {margin-top:-3px;}
  .badge.warning{
    background-color: #FF9A00;
  }
  .badge.error{
      background-color: #FC3723;
  }
  .badge.fatal{
      background-color:#FC0023;
  }
  .main{width:100%;min-height:300px;background: #F2F2F2;margin-top:30px;padding:10px;text-indent:2rem;}
</style>
