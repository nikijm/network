<template>
  <page title="工作表详情" :isLoading="isLoading" class="multisection">
    <div slot="content">
      <section>
        <table class="table table-hover table-fixed">
      <thead>
        <tr>
          <th width="200">文件名</th>
          <th width="100">编号</th>
          <th width="150">上传用户</th>
          <th width="200">上传时间</th>
          <th width="100">状态</th>
          <th width="100">数据量</th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td v-text="fetchSheetData.upload.fileName"></td>
          <td v-text="fetchSheetData.id"></td>
          <upload-user-name :sheet="fetchSheetData.upload" />
          <td v-text="fetchSheetData.upload.uploadDate"></td>
          <td v-text="fetchSheetData.status"></td>
          <td v-text="fetchListData.paging.total"></td>
        </tr>
        </tbody>
      </table>
      </section>
      <section>
      <h4 class="box-title">数据</h4>
      <div style="width:100%;overflow:auto">
        <table style="width:auto" class="table table-hover table-nowrap">
        <thead>
          <tr>
            <th v-for="(value, key) in fetchListData.data[0]">
              {{key}}
            </th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="list in fetchListData.data">
            <td v-for="(value, key) in list">
              {{value}}
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <br/>
      <el-pagination
        @current-change="toPage"
        :current-page="fetchListData.paging.current"
        :page-size="fetchListData.paging.page_size"
        layout="prev, pager, next"
        :total="fetchListData.paging.total">
      </el-pagination>
      </section>
      <section>
    <h4 class="box-title">操作日志</h4>
      <table class="table table-hover table-fixed">
      <thead>
        <tr>
          <th>操作人</th>
          <th>操作</th>
          <th>时间</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="logs in fetchLogsData">
          <upload-user-name :sheet="logs" />
          <td>{{logs.operation}}</td>
          <td>{{getTime(logs.opDate)}}</td>
          <td v-if="logs.operation == '转换' || logs.operation == '清洗' || logs.operation == '验证'">
            <logs-details :logsData="logs"></logs-details>
          </td>
          <td v-else-if="logs.operation == '废弃'">
            <button type="button" class="btn btn-xs btn-primary" @click="showLogs(logs.request)">
              查看详情
            </button>
          </td>
          <td v-else></td>
        </tr>
        </tbody>
      </table>
      <div slot="footer">
        <button class="btn btn-default btn-main" @click="goBack">返回</button>
        <button class="btn btn-primary" v-if="fetchSheetData.status !== '未验证' && fetchSheetData.status !== '已废弃' && fetchSheetData.status !== '已校验'" @click="revertToUnvalidated">
          回退到未验证状态
        </button>
        <button class="btn btn-primary" v-if="fetchSheetData.status !== '未验证' && fetchSheetData.status !== '已验证' && fetchSheetData.status !== '已清洗' && fetchSheetData.status !== '已废弃' && fetchSheetData.status !== '已校验'" @click="revertToCleaned">
          回退到已清洗状态
        </button>
        <button class="btn btn-danger btn-main" v-if="fetchSheetData.status !== '已校验' && fetchSheetData.status !== '已废弃'" @click="trash">
          删除
        </button>
        <router-link tag="button" v-if="fetchSheetData.status == '未验证' || fetchSheetData.status == '已验证' " :to="{name: 'clean',params:{id:fetchSheetData.id}}" class="btn btn-success btn-main">
          清洗
        </router-link>
        <template v-if="fetchSheetData.status == '已清洗' ">
          <router-link class="el-button el-button--primary" tag="button"
         :to="{name: 'transform',params:{id:sheetId}}">
            转换
          </router-link>
          <check-data :message-id="sheetId" message-type="unClean" text="查看原始数据"></check-data>
          <compare-data :message-id="sheetId" message-type="unTransformed" preferences="O-C"></compare-data>
        </template>
        <template v-if="fetchSheetData.status == '已转换' ">
          <router-link class="el-button el-button--primary" tag="button" to="/compareSheets">
            校验
          </router-link>
          <check-data :message-id="sheetId" message-type="unClean" text="查看原始数据"></check-data>
          <check-data :message-id="sheetId" message-type="Cleaned" text="查看清洗后数据"></check-data>
          <compare-data :message-id="sheetId" message-type="transformed" preferences="O-C"></compare-data>
        </template>
        <compare-data v-if="fetchSheetData.status == '已校验'" :message-id="sheetId" message-type="transformed" preferences="O-C"></compare-data>
      </div>
      <progress-dialog :visible.sync="dialogVisible" :isSuccess="isSuccess" :isError="isError" :SuccessText="SuccessText" :errorText="errorText">
      </progress-dialog>
    </section>
    </div>
  </page>
</template>

<script>
import Page from '../../components/Page.vue'
import VATable from '../../components/VATable.vue'
import CheckData from '../../components/CheckData.vue'
import CompareData from '../../components/CompareData.vue'
import LogsDetails from '../../components/LogsDetails.vue'
import { mapGetters, mapActions } from 'vuex'
import revertService from '../../vuex/api/services/sheets'
import ProgressDialog from '../../components/ProgressDialog.vue'
import UploadUserName from '../../components/Upload-user-name.vue'
export default {
  data () {
    return {
      isLoading: false,
      sheetId: 1,
      dialogVisible: false,
      isSuccess: false,
      isError: false,
      SuccessText: '操作成功',
      errorText: '操作失败',
      payload: {
        id: 1,
        page: 1
      }
    }
  },
  components: {
    'page': Page,
    'va-table': VATable,
    'check-data': CheckData,
    'logs-details': LogsDetails,
    'compare-data': CompareData,
    'progress-dialog': ProgressDialog,
    'upload-user-name': UploadUserName
  },
  created () {
    if (!this.$route.params.id) {
      this.$message({
        type: 'error',
        message: '页面访问失败'
      })
      this.$router.go(-1)
      return
    }
    this.payload.id = this.$route.params.id
    this.sheetId = this.$route.params.id
    this.sheetLoad()
  },
  computed: {
    ...mapGetters([
      'fetchSheetData',
      'fetchListData',
      'fetchLogsData'
    ])
  },
  methods: {
    ...mapActions([
      'FETCH_SHEET',
      'FETCH_TLIST',
      'FETCH_LOGS'
    ]),
    toPage (val) {
      this.payload.page = val
      this.sheetLoad()
    },
    sheetLoad () {
      if (this.isLoading) {
        return
      }
      this.isLoading = true
      Promise.all([this.FETCH_SHEET(this.sheetId), this.FETCH_TLIST(this.payload), this.FETCH_LOGS(this.sheetId)])
      .then(() => {
        this.isLoading = false
      })
      .catch(() => {
        this.$promptReload()
      })
    },
    goBack () {
      this.$router.go(-1)
    },
    getTime (time) {
      return new Date(parseInt(time)).toLocaleString().replace(/:\d{1,2}$/, ' ')
    },
    revertToUnvalidated () {
      this.$confirm('确认回退到未验证状态！', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.isSuccess = false
        this.isError = false
        this.dialogVisible = true
        revertService.revertToUnvalidated(this.sheetId)
        .then((data) => {
          this.SuccessText = '成功回退到未验证状态'
          this.isSuccess = true
          this.sheetLoad()
        })
        .catch(() => {
          this.errorText = '回退未验证状态失败'
          this.isError = true
        })
      })
    },
    revertToCleaned () {
      this.$confirm('确认回退到已清洗状态！', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.isSuccess = false
        this.isError = false
        this.dialogVisible = true
        revertService.revertToCleaned(this.sheetId)
        .then((data) => {
          this.SuccessText = '成功回退到已清洗状态'
          this.isSuccess = true
          this.sheetLoad()
        })
        .catch(() => {
          this.errorText = '回退已清洗状态失败'
          this.isError = true
        })
      })
    },
    trash () {
      this.$prompt('请输入废弃原因', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({value}) => {
        this.isSuccess = false
        this.isError = false
        this.dialogVisible = true
        let trashData = {}
        trashData.id = this.sheetId
        trashData.note = value
        revertService.trash(trashData)
        .then((data) => {
          this.SuccessText = '成功废弃工作表'
          this.isSuccess = true
          this.sheetLoad()
        })
        .catch(() => {
          this.errorText = '废弃工作表失败'
          this.isError = true
        })
      })
    },
    showLogs (data) {
      let note = JSON.parse(data)
      this.$alert(note['note'], '日志详情', {
        confirmButtonText: '确定'
      })
    }
  }
}
</script>
<style scoped>
  table { min-width: 100%;}
  th {  white-space: nowrap; min-width: 80px;}
  /*td { word-wrap: nowrap;}*/
</style>
