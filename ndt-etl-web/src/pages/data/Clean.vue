<template>
  <page title="待清洗数据" :isLoading="isLoading" class="multisection">
    <div slot="content">
      <section>
      <table class="table table-hover">
        <thead>
        <tr>
          <th>文件名</th>
          <th>编号</th>
          <th>上传用户</th>
          <th>上传时间</th>
          <th>状态</th>
        </tr>
         </thead>
         <tbody>
        <tr>
          <td>{{fetchSheetData.upload.fileName}}</td>
          <td>{{fetchSheetData.id}}</td>
          <upload-user-name :sheet="fetchSheetData.upload" />
          <td>{{fetchSheetData.upload.uploadDate}}</td>
          <td>{{fetchSheetData.status}}</td>
        </tr>
        </tbody>
      </table>
      </section>
      <section>
      <table class="table table-hover">
        <thead>
        <tr>
          <th>列名</th>
          <th>源数据示例</th>
          <th width="30%" v-if="fetchSheetData.status == '未验证'">验证规则</th>
          <th width="20%">清洗规则</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(table, key, index) in fetchListData.data[0]" v-if="key != 'ID'">
          <td v-text="key"></td>
          <td v-text="table"></td>
          <td v-if="fetchSheetData.status == '未验证'">
            <validatorSelect :validator="validatePayload.columns[index].validator" v-if="validatePayload.columns[index]" />
          </td>
          <td>
            <string-operation-label :test="'' + table" :operations="cleanPayload.columns[index].operations" v-if="cleanPayload.columns[index]"/>
          </td>
        </tr>
        </tbody>
      </table>
      <progress-dialog :visible.sync="dialogVisible" :isSuccess="isSuccess" :isError="isError" SuccessText="清洗成功" :errorText="errorText" :progressText="progressText" @close="close">
        <div class="validate-error" style="width:100%;overflow:auto" slot="error">
          <va-table style="width:auto;min-width:100%;text-align:left">
            <tr>
              <th v-for="(value, key) in validateError.data[0]">
                {{key}}
              </th>
            </tr>
            <tr v-for="item, index in validateError.data">
              <td v-for="(value, key) in item" :class="isValidateError(item.ID, key) ? 'error': ''">
                {{value}}
              </td>
            </tr>
          </va-table>
        </div>
      </progress-dialog>
      </section>
    </div>
    <div slot="footer">
      <button class="btn btn-default" @click="goBack">返回</button>
      <check-data :message-id="sheetId" message-type="unClean" text="查看原始数据"></check-data>
      <button class="btn btn-success" style="margin-left:10px;" @click="clean">清洗</button>
    </div>
  </page>
</template>

<script>
import Page from '../../components/Page.vue'
import VATable from '../../components/VATable.vue'
import validatorSelect from '../../components/validatorSelect.vue'
import StringOperationLabel from '../../components/StringOperationLabel.vue'
import ProgressDialog from '../../components/ProgressDialog.vue'
import CheckData from '../../components/CheckData.vue'
import { mapGetters, mapActions } from 'vuex'
import { services } from '../../vuex/api'
import router from '../../router.js'
import UploadUserName from '../../components/Upload-user-name.vue'

export default {
  data () {
    return {
      sheetId: '',
      payload: {
        limit: '',
        page: 1
      },
      isLoading: false,
      validatePayload: {columns: []},
      cleanPayload: {columns: []},
      dialogVisible: false,
      isSuccess: false,
      isError: false,
      errorText: '操作失败',
      progressText: '操作中',
      validateError: {
        rows: [],
        data: []
      }
    }
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

    this.isLoading = true
    Promise.all([
      this.getsheetlist(this.payload),
      this.getSheet()
    ]).then(() => { this.isLoading = false })
    .then(this.loadTemplate)
  },
  computed: {
    ...mapGetters([
      'fetchSheetData',
      'fetchListData'
    ])
  },
  methods: {
    ...mapActions([
      'FETCH_SHEET',
      'FETCH_TLIST'
    ]),
    getSheet () {
      return this.FETCH_SHEET(this.sheetId).then(() => {
        if (this.fetchSheetData.status !== '未验证' && this.fetchSheetData.status !== '已验证') {
          this.$msgbox({
            type: 'error',
            message: '该工作表不可清洗！'
          })
          .then(() => {
            this.$router.replace('/cleanSheets')
          })
          return Promise.reject()
        }
      })
    },
    getsheetlist () {
      return this.FETCH_TLIST(this.payload).then(() => {
        let firstRow = this.fetchListData.data[0]
        Object.keys(firstRow).forEach((column) => {
          this.validatePayload.columns.push({
            name: column,
            validator: {
              name: '不验证',
              params: []
            }
          })
          this.cleanPayload.columns.push({
            name: column,
            operations: []
          })
        })
      })
    },
    goBack () {
      this.$router.go(-1)
    },
    showValidateErrors (rows) {
      this.validateError.rows = rows
      let ids = []
      rows.forEach((row) => {
        ids.push(row.ID)
      })
      services.sheets.queryData(this.sheetId, {ids: ids}).then((data) => {
        this.validateError.data = data
      })
    },
    isValidateError (id, column) {
      let row = this.validateError.rows.find((row) => row.ID === id)
      if (!row) {
        return false
      }
      return column in row.columns
    },
    clean () {
      this.dialogVisible = true
      this.isSuccess = false
      this.isError = false

      if (this.fetchSheetData.status === '已验证') {
        this.errorText = '清洗失败'
        this.progressText = '清洗中...'
        services.sheets.clean(this.sheetId, this.cleanPayload)
        .then(() => {
          this.isSuccess = true
        })
        .catch(() => {
          this.isError = true
        })
        return
      }

      this.errorText = '验证失败'
      this.progressText = '验证中...'
      this.validateError.rows = []
      this.validateError.data = []
      services.sheets.validate(this.sheetId, this.validatePayload).then((data) => {
        this.getSheet()
        this.errorText = '清洗失败'
        this.progressText = '验证成功，清洗中...'
        return services.sheets.clean(this.sheetId, this.cleanPayload)
      })
      .then(() => {
        this.isSuccess = true
      })
      .catch((err) => {
        this.isError = true
        if (this.errorText === '验证失败') {
          this.showValidateErrors(err.response.data.rows)
        }
      })
    },
    close () {
      if (this.isSuccess) {
        router.replace('/sheet/' + this.sheetId)
      }
    },
    loadTemplate () {
      services.etlRules.queryTemplate(this.fetchSheetData.columns)
      .then((template) => {
        template.columns.forEach((column, index) => {
          this.validatePayload.columns[index + 1].validator = column.validator
          if (column.cleaner) {
            this.cleanPayload.columns[index + 1].operations = column.cleaner
          }
        })
        this.$message({
          message: '已载入规则模板:' + template.name,
          type: 'info'
        })
      })
    }
  },
  components: {
    'page': Page,
    'va-table': VATable,
    'validatorSelect': validatorSelect,
    'string-operation-label': StringOperationLabel,
    'checkData': CheckData,
    'progress-dialog': ProgressDialog,
    'upload-user-name': UploadUserName
  }
}
</script>

<style>
.validate-error .error{
  color: #ff4949;
  font-weight: bold;
  background: rgba(255, 73, 73, 0.18);
}
</style>
