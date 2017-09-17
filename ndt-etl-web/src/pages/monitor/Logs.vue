<template>
  <page :title="title" :isLoading="isLoading">
    <div slot="tools">
      <el-select v-model="ajaxData.level" style="float:left;padding-right:10px" placeholder="按级别过滤" @change='changeLevel'>
        <el-option
          v-for="item in toptions"
          :label="item.value" :value="item.index">
        </el-option>
      </el-select>
      <el-input v-model="ajaxData.content" placeholder="请输入搜索内容" class="tools-button"  @keyup.native='changeSearch($event)'>
        <el-button slot="append" icon="search" @click='searchContent'></el-button>
      </el-input>
    </div>
    <div slot="content">
      <table class="table table-hover">
        <thead>
        <tr>
          <th width="180">时间</th>
          <th width="120">操作用户</th>
          <th width="60">级别</th>
          <th>内容</th>
          <th>URL地址</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="item in totalLogs" @click='jumpDetail(item.id)'>
          <td class='break'>{{item.dateTimeView}}</td>
          <td class='break'>{{item.userName}}</td>
          <td class='break'>
            <span class='badge info' v-show='item.logLevel===1'>记录</span>
            <span class='badge warning' v-show='item.logLevel===2'>警告</span>
            <span class='badge error' v-show='item.logLevel===3'>错误</span>
            <span class='badge fatal' v-show='item.logLevel===4'>内存溢出</span>
          </td>
          <td class='break ellipsis route-jump' @click='jumpDetail(item.id)'>
            {{item.descriptionLog}}
          </td>
          <td class='break'>{{item.requestUri}}</td>
        </tr>
        </tbody>
      </table>
      <div class="col-md-12" style='text-align: center;' v-show='isLoading'>数据请求中</div>
      <div class="col-md-12" style='text-align: center;' v-show='noData<=0'>暂无数据</div>
      <el-pagination
        @current-change="toPage"
        :current-page="logsPaging.current"
        layout="prev, pager, next"
        :total="logsPaging.total"
        :page-size="logsPaging.page_size">
      </el-pagination>
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
      //  下拉
      toptions:
      [
        { value: '所有级别', index: 0 },
        { value: '记录', index: 1 },
        { value: '警告', index: 2 },
        { value: '错误', index: 3 },
        { value: '内存溢出', index: 4 }
      ],
      noData: 1,
      title: '日志',
      ajaxData: {
        page: null,
        content: '',
        level: ''
      }
    }
  },
  methods: {
    ...mapActions([
      'fetchTotalLogs'//  发起查询日志的action
    ]),
    changeLevel () {
      this.ajaxData.page = 1
      this.loadLog()
    },
    changeSearch (event) {
      if (event.keyCode === 13) {
        this.searchContent()
      }
    },
    toPage (num) {
      this.ajaxData.page = num
      this.loadLog()
    },
    searchContent () {
      this.ajaxData.page = 1
      this.loadLog()
    },
    loadLog () {
      this.fetchTotalLogs(this.ajaxData).then((res) => {
        statusCallback(res, function (that) {
          if (that.totalLogs) {
            that.noData = that.totalLogs.length
          }
        }, this)
      })
    },
    jumpDetail (num) {
      this.$router.push('/logdetail?id=' + num)
    }
  },
  components: {
    'va-table': VATable,
    'page': Page
  },
  computed: {
    ...mapGetters([
      'totalLogs',
      'logsPaging',
      'isLoading'
    ])
  },
  mounted () {
    this.loadLog()
  }
}
</script>
<style scoped>
  .break{word-wrap: break-word;-ms-word-wrap:break-word ;}
  .badge.info{
    background-color: #40A7FF;
  }
  .badge.warning{
    background-color: #FF9A00;
  }
  .badge.error{
      background-color: #FC3723;
  }
  .badge.fatal{
      background-color:#FC0023;
  }
  .ellipsis {
    min-width:85px;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow:hidden;
  }
</style>
