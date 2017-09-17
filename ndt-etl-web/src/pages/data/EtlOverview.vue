<template>
  <Page title="数据处理总览" :isLoading="isLoading">
    <div slot="tools">
      <el-input v-model="message" placeholder="请输入文件名或用户名" class="tools-button" @keyup.enter.native="search">
        <el-button slot="append" icon="search" @click="search"></el-button>
      </el-input>
    </div>
    <div slot="content">
      <logs-details class="logs-details" :logsData="log" :dialogVisible.sync="logDialogVisible" :showButton="false" />
      <el-dialog title="处理详情" :visible.sync="progressDialogVisible">
        <va-table>
          <tr>
            <th>项目</th>
            <th>数量</th>
          </tr>
          <tr>
            <td>已处理条数</td>
            <td>{{progressDataCount}}</td>
          </tr>
          <tr>
            <td>处理成功条数</td>
            <td>{{progressDataCount}}</td>
          </tr>
          <tr>
            <td>处理失败条数</td>
            <td>0</td>
          </tr>
          <tr>
            <td>待处理条数</td>
            <td>0</td>
          </tr>
        </va-table>
      </el-dialog>
      <table class="table table-hover table-fixed">
        <thead>
          <tr>
            <th width="100">文件名</th>
            <th width="50">缓存</th>
            <th width="100">工作表编号</th>
            <th width="100">规则</th>
            <th width="50">清洗</th>
            <th width="100">规则</th>
            <th width="50">转换</th>
            <th width="100">校验</th>
            <th width="80">操作</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="item in fetchUploadsWithSheetsData.uploads">
            <tr v-if="item.status == '未缓存'">
              <td> {{item.fileName}} </td>
              <td colspan="8">未缓存</td>
            </tr>
            <template v-if="item.status !== '未缓存' && item.status !== '已废弃'">
              <tr>
                <td :rowspan="item.sheets.length + 1"> {{item.fileName}} </td>
                <td :rowspan="item.sheets.length + 1">
                  <span style="color:#5cb85c">
                    100%
                  </span>
                </td>
                <td colspan="7" v-if="item.sheets.length == 0">
                  无数据
                </td>
              </tr>
              <tr v-for="sheet in item.sheets">
                <td> {{sheet.id}} </td>
                <td>
                  <span v-if="sheet.status == '已清洗' || sheet.status == '已转换' || sheet.status == '已校验'">
                    <el-popover placement="top" width="160" trigger="click">
                      <a class="btn btn-primary btn-xs" @click="loadLog(sheet, '验证')">验证</a>
                      <a class="btn btn-primary btn-xs" @click="loadLog(sheet, '清洗')">清洗</a>
                      <button type="button" class="btn btn-block btn-default btn-xs" slot="reference" style="margin-bottom:5px">查看规则</button>
                    </el-popover>

                    <compare-data
                      :messageId="sheet.id"
                      :messageType="sheet.status === '已转换' || sheet.status === '已校验'? 'transformed': 'unTransformed'"
                      style="text-align:left;"
                      buttonClass="btn btn-block btn-default btn-xs"
                      buttonText="对比数据"
                    />
                  </span>
                </td>
                <td>
                  <a v-if="sheet.status == '已清洗' || sheet.status == '已转换' || sheet.status == '已校验'" style="color:#5cb85c;cursor:pointer" @click="showProgress(sheet)">
                    100%
                  </a>
                </td>
                <td>
                  <span v-if="sheet.status == '已转换' || sheet.status == '已校验'">
                    <button type="button" class="btn btn-block btn-default btn-xs" @click="loadLog(sheet, '转换')" style="margin-bottom:5px">查看规则</button>
                    <compare-data
                      :messageId="sheet.id"
                      :messageType="sheet.status === '已转换' || sheet.status === '已校验'? 'transformed': 'unTransformed'"
                      style="text-align:left;"
                      buttonClass="btn btn-block btn-default btn-xs"
                      buttonText="对比数据"
                      preferences="C-T"
                    />
                  </span>
                </td>
                <td>
                  <a v-if="sheet.status == '已转换' || sheet.status == '已校验'" style="color:#5cb85c;cursor:pointer" @click="showProgress(sheet)">
                    100%
                  </a>
                </td>
                <td>
                  <span v-if="sheet.status == '已校验'" style="color:#5cb85c">
                    100%
                  </span>
                </td>
                <td>
                  <router-link tag="a" class="btn btn-primary btn-xs" :to="{name: 'sheet',params:{id:sheet.id}}">
                    查看
                  </router-link>
                  <router-link tag="a" class="btn btn-success btn-xs"  :to="{name: 'clean',params:{id:sheet.id}}" v-if="sheet.status == '未验证' || sheet.status == '已验证'">
                    清洗
                  </router-link>
                  <router-link tag="a" class="btn btn-success btn-xs" :to="{name: 'transform',params:{id:sheet.id}}" v-if="sheet.status == '已清洗'">
                    转换
                  </router-link>
                  <router-link tag="a" class="btn btn-success btn-xs" :to="{name: 'verify',params:{id:sheet.id}}" v-if="sheet.status == '已转换'">
                    校验
                  </router-link>
                </td>
              </tr>
            </template>
            <tr v-if="item.status == '已废弃'">
              <td> {{item.fileName}} </td>
              <td></td>
              <td colspan="7">已废弃</td>
            </tr>
          </template>
        </tbody>
      </table>
      <!-- <table class="sheets-table"> -->
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="fetchUploadsWithSheetsData.paging.currentPage"
        :page-size="fetchUploadsWithSheetsData.paging.page_size"
        layout="prev, pager, next"
        :total="fetchUploadsWithSheetsData.paging.total">
      </el-pagination>
    </div>
  </Page>
</template>
<script>
import Page from '../../components/Page.vue'
import VATable from '../../components/VATable.vue'
import LogsDetails from '../../components/LogsDetails.vue'
import CompareData from '../../components/CompareData.vue'
import { mapGetters, mapActions } from 'vuex'

export default {
  data () {
    return {
      isLoading: true,
      message: '',
      payload: {
        page: 1,
        search: ''
      },
      logSheet: {},
      log: {
        request: '{"columns":[]}'
      },
      logCache: {},
      logDialogVisible: false,
      progressDialogVisible: false,
      progressDataCount: 0,
      progressCache: {}
    }
  },
  created () {
    this.load()
  },
  computed: {
    ...mapGetters([
      'fetchUploadsWithSheetsData',
      'fetchLogsData',
      'fetchListData'
    ])
  },
  components: {
    'Page': Page,
    'va-table': VATable,
    'logs-details': LogsDetails,
    'compare-data': CompareData
  },
  methods: {
    ...mapActions([
      'FETCH_UPLOADS_WITH_SHEETS',
      'FETCH_LOGS',
      'FETCH_TLIST'
    ]),
    search: function () {
      this.payload.search = this.message
      this.payload.page = 1
      this.load()
    },
    handleCurrentChange (val) {
      this.payload.page = val
      this.load()
    },
    load () {
      this.isLoading = true
      this.FETCH_UPLOADS_WITH_SHEETS(this.payload)
      .then(() => {
        this.isLoading = false
      })
      .catch(this.$promptReload)
    },
    download (id) {
      window.open('/api/uploads/' + id + '/download')
    },
    loadLog (sheet, operation) {
      if (sheet.id in this.logCache) {
        this.log = this.logCache[sheet.id]
          .slice(0)
          .reverse()
          .find((log) => log.operation === operation)
        this.logDialogVisible = true
        return
      }
      this.isLoading = true
      this.FETCH_LOGS(sheet.id).then(() => {
        this.isLoading = false
        this.log = this.fetchLogsData
          .slice(0)
          .reverse()
          .find((log) => log.operation === operation)
        this.logCache[sheet.id] = this.fetchLogsData
        this.logDialogVisible = true
      })
    },
    showProgress (sheet) {
      if (sheet.id in this.progressCache) {
        this.progressDataCount = this.progressCache[sheet.id].paging.total
        this.progressDialogVisible = true
        return
      }
      this.isLoading = true
      this.FETCH_TLIST({
        id: sheet.id,
        page: 1
      }).then(() => {
        this.isLoading = false
        this.progressDataCount = this.fetchListData.paging.total
        this.progressCache[sheet.id] = this.fetchListData
        this.progressDialogVisible = true
      })
    }
  }
}
</script>
<style scoped>
  .pr20{
    position: relative;
    right: 20px;
  }
  th, td{
    text-align: center;
  }
  .table>tbody>tr>td {
    vertical-align: middle;
  }
  td:nth-child(7){
    text-align: left
  }
  td:nth-child(9){
    text-align: left
  }
  .logs-details > .btn {
    display: none
  }
</style>
