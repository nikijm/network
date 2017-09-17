<template>
  <page title="数据转换" :isLoading="isLoading"  class="multisection">
    <div slot="content">
    <section>
      <table class="table table-hover table-nowrap">
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
          <td v-text="fetchSheetData.upload.fileName"></td>
          <td v-text="fetchSheetData.upload.id"></td>
          <upload-user-name :sheet="fetchSheetData.upload" />
          <td v-text="fetchSheetData.upload.uploadDate"></td>
          <td v-text="fetchSheetData.status"></td>
        </tr>
         </tbody>
      </table>
      </section>
       <section>
      <h3 style="display:inline-block">转换规则</h3>
      <el-select v-model="mirrors.name" filterable placeholder="请选择业务表" style="float: right; margin-top: 10px;">
        <el-option
        v-for="item in fetchMirrorsData"
        :label="showComment(item)"
        :value="item.name">
        </el-option>
      </el-select>
      <table style="width:auto" class="table table-hover table-nowrap">
        <thead>
        <tr>
          <th width="400">原列</th>
          <th width="400">原数据示例</th>
          <th>目标列</th>
        </tr>
          </thead>
          <tbody>
        <tr v-for="(table, key, index) in listData">
          <td v-text="key"></td>
          <td v-text="table"></td>
          <td>
            <el-select v-model="mirrors.columns[index]" filterable placeholder="请选择目标列">
              <el-option label="" value=""></el-option>
              <el-option
                v-for="item in items"
                :label="showComment(item)"
                :value="item.name">
              </el-option>
            </el-select>
          </td>
        </tr>
      </tbody>
    </table>
      <progress-dialog :visible.sync="dialogVisible" :isSuccess="isSuccess" :isError="isError" SuccessText="转换成功" :errorText="errorText" :progressText="progressText" @close="close">
      </progress-dialog>
       </section>
    </div>
    <div slot="footer">
      <button class="btn btn-default btn-main" @click="goBack">返回</button>
      <check-data :message-id="sheetId" message-type="unTransform" text="查看清洗后数据"></check-data>
      <button class="btn btn-success btn-main" @click="transform">转换</button>
    </div>
  </page>
</template>

<script>
import Page from '../../components/Page.vue'
import VATable from '../../components/VATable.vue'
import { mapGetters, mapActions } from 'vuex'
import CheckData from '../../components/CheckData.vue'
import ProgressDialog from '../../components/ProgressDialog.vue'
import { services } from '../../vuex/api'
import router from '../../router.js'
import UploadUserName from '../../components/Upload-user-name.vue'

export default {
  data () {
    return {
      isLoading: false,
      message: '',
      sheetId: '',
      payload: {
        limit: '',
        page: 1
      },
      mirrors: {
        name: '',
        columns: []
      },
      items: {},
      transformPayload: {
        targetId: 0,
        target: '',
        columns: []
      },
      listData: {},
      listLength: 0,
      num: 0,
      dialogVisible: false,
      isSuccess: false,
      isError: false,
      errorText: '操作失败',
      progressText: '操作中'
    }
  },
  components: {
    'page': Page,
    'va-table': VATable,
    'checkData': CheckData,
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
    Promise.all([
      this.getSheet(),
      this.getListForm(this.payload),
      this.getMirrors()
    ]).then(this.loadTemplate)
  },
  computed: {
    ...mapGetters([
      'fetchSheetData',
      'fetchListData',
      'fetchMirrorsData'
    ])
  },
  watch: {
    'mirrors.name': function (newval) {
      for (var i = 0; i < this.fetchMirrorsData.length; i++) {
        if (this.fetchMirrorsData[i].name === newval) {
          this.items = this.fetchMirrorsData[i].columns
          this.mirrorsId = this.fetchMirrorsData[i].id
        }
      }
      for (var j = 0; j < this.listLength; j++) {
        this.mirrors.columns[j] = ''
      }
    }
  },
  methods: {
    ...mapActions([
      'FETCH_SHEET',
      'FETCH_TLIST',
      'FETCH_MIRRORS'
    ]),
    getSheet () {
      return this.FETCH_SHEET(this.sheetId).then(() => {
        if (this.fetchSheetData.status !== '已清洗') {
          this.$msgbox({
            type: 'error',
            message: '该工作表不可转换！'
          })
          .then(() => {
            this.$router.replace('/cleanSheets')
          })
        }
      })
    },
    getListForm () {
      return this.FETCH_TLIST(this.payload).then(() => {
        delete this.fetchListData.data[0].ID
        this.listData = this.fetchListData.data[0]
        this.listLength = Object.keys(this.listData).length
      })
    },
    getMirrors () {
      return this.FETCH_MIRRORS().then(() => {
      })
    },
    isRepeat (arr) {
      var arrStr = JSON.stringify(arr)
      for (var i = 0; i < arr.length; i++) {
        if (arr[i] !== '' && arr[i] !== null) {
          if (arrStr.indexOf(arr[i]) !== arrStr.lastIndexOf(arr[i])) {
            return arr[i]
          }
        }
      }
      return false
    },
    isChecked () {
      if (this.mirrors.name === '') {
        this.$alert('请选择业务表', '提示', {
          confirmButtonText: '确定',
          type: 'error'
        })
        return false
      } else {
        for (var j = 0; j < this.listLength; j++) {
          if (this.mirrors.columns[j] !== '') {
            this.num++
          }
        }
        if (this.num === 0) {
          this.$alert('请选择目标列', '提示', {
            confirmButtonText: '确定',
            type: 'error'
          })
          return false
        } else {
          let repeatResult = this.isRepeat(this.mirrors.columns)
          if (repeatResult !== false) {
            this.$alert('目标列不能重复：' + repeatResult, '提示', {
              confirmButtonText: '确定',
              type: 'error'
            })
            return false
          } else {
            return true
          }
        }
      }
    },
    getTransformPayload () {
      this.transformPayload.target = ''
      this.transformPayload.columns = []
      var tableKeys = Object.keys(this.listData)
      this.transformPayload.targetId = this.mirrorsId
      this.transformPayload.target = this.mirrors.name
      for (var i = 0; i < this.listLength; i++) {
        if (this.mirrors.columns[i] !== '' && this.mirrors.columns[i] !== null) {
          this.transformPayload.columns.push({
            name: this.mirrors.columns[i],
            from: tableKeys[i]
          })
        }
      }
    },
    transform () {
      this.isChecked()
      this.getTransformPayload()
      if (this.isChecked()) {
        if (this.num < this.listLength) {
          this.$confirm('您选择的目标列少于原列，是否确定！', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            if (this.num < this.items.length) {
              this.$confirm('您选择的目标列少于业务表列，是否确认！', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                this.doTranstorm()
              }).catch(() => {})
            } else {
              this.doTranstorm()
            }
          }).catch(() => {})
        } else {
          if (this.num < this.items.length) {
            this.$confirm('您选择的目标列少于业务表列，是否确认！', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.doTranstorm()
            }).catch(() => {})
          } else {
            this.doTranstorm()
          }
        }
      }
    },
    doTranstorm () {
      this.isError = false
      this.isSuccess = false
      this.dialogVisible = true
      this.errorText = '转换失败'
      this.progressText = '转换中...'
      services.sheets.transform(this.sheetId, this.transformPayload).then((data) => {
        this.progressText = '转换中...'
        this.isSuccess = true
      })
      .catch(() => {
        this.isError = true
      })
    },
    close () {
      if (this.isSuccess) {
        router.replace('/sheet/' + this.sheetId)
      }
    },
    goBack () {
      this.$router.go(-1)
    },
    loadTemplate () {
      services.etlRules.queryTemplate(this.fetchSheetData.columns)
      .then((template) => {
        this.mirrors.name = template.target
        setTimeout(() => {
          template.columns.forEach((column, index) => {
            this.mirrors.columns[index] = column.to
          })
          this.$message({
            message: '已载入规则模板:' + template.name,
            type: 'info'
          })
          this.$forceUpdate()
        }, 500)
      })
    },
    showComment (item) {
      if (item.comment) {
        return item.name + ' (' + item.comment + ')'
      } else {
        return item.name
      }
    }
  }
}
</script>
<style scoped>
  .pr20{
    position: relative;
    right: 20px;
  }
  h3 {
    font-size: 18px;
    font-weight: 500;
    line-height: 1.1;
    color: inherit;
    margin-left: 5px;
  }
</style>
