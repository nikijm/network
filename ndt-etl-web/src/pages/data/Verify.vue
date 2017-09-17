<template>
  <page title="数据校验" :isLoading="isLoading">
    <div slot="content">
      <h3 style="display:inline-block">请选择索引列</h3>
      <el-select v-model="column" filterable placeholder="请选择索引列" style="margin-top: 10px;"
      @change="getVerifyData">
        <el-option
        v-for="(table, key) in this.tableKeys"
        :label="table"
        :value="table">
        </el-option>
      </el-select>
      <div>
        <div style="width:49%;display:inline-block;vertical-align:top;">
          <h4>已校验数据</h4>
          <div style="overflow:auto;">
            <table class="table table-hover table-nowrap">
            <thead>
              <tr>
                <th v-for="(value, key) in this.listData[0]" width="200px">
                  {{key}}
                </th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="list in this.listData">
                <td v-for="(value, key) in list">
                  {{value}}
                </td>
              </tr>
            </tbody>
          </table>
          </div>
        </div>
        <div style="width:49%;display:inline-block;vertical-align:top;">
          <h4>业务表数据</h4>
          <div style="overflow:auto;">
            <table class="table table-hover table-nowrap">
              <thead>
              <tr v-if="this.mirrorData.length === 0">
                <td >
                  无数据
                </td>
              </tr>
              <tr v-else>
                <th v-for="(value, key) in this.mirrorData[0]" width="200px">
                  {{key}}
                </th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="list in this.mirrorData">
                <td v-for="(value, key) in list">
                  {{value}}
                </td>
              </tr>
              </tbody>
          </table>
          </div>
        </div>
      </div>
      <el-pagination
        @current-change="toPage"
        :current-page="fetchListData.paging.current"
        :page-size="fetchListData.paging.page_size"
        layout="prev, pager, next"
        :total="fetchListData.paging.total">
      </el-pagination>
    </div>
    <div slot="footer">
      <button class="btn btn-default btn-main" @click="goBack">返回</button>
      <button class="btn btn-success btn-main" @click="verify">校验</button>
    </div>
  </page>
</template>

<script>
import Page from '../../components/Page.vue'
import VATable from '../../components/VATable.vue'
import { mapGetters, mapActions } from 'vuex'
import { services } from '../../vuex/api'

export default {
  data () {
    return {
      mirrorData: [],
      listData: [],
      tableKeys: [],
      verifyPayload: {
        column: '',
        in: []
      },
      verifyColumn: [
        'SERNO',
        'REGORGCODE',
        'REGORGNAME',
        'GRANTORGNAME',
        'ADDRESS',
        'SRCORGCODE',
        'OrgCode'
      ],
      listLength: 0,
      isLoading: false,
      column: '',
      sheetId: '',
      payload: {
        limit: '',
        page: 1
      }
    }
  },
  components: {
    'page': Page,
    'va-table': VATable
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
    this.getSheet()
    this.getListForm(this.payload)
    this.getMirrors()
  },
  computed: {
    ...mapGetters([
      'fetchSheetData',
      'fetchListData',
      'fetchMirrorsData'
    ])
  },
  methods: {
    ...mapActions([
      'FETCH_SHEET',
      'FETCH_TLIST',
      'FETCH_MIRRORS'
    ]),
    getSheet () {
      this.FETCH_SHEET(this.sheetId).then(() => {
        if (this.fetchSheetData.status !== '已转换') {
          this.$msgbox({
            type: 'error',
            message: '该工作表不可校验！'
          })
          .then(() => {
            this.$router.replace('/transformSheets')
          })
        }
      })
    },
    getMirrors () {
      this.FETCH_MIRRORS().then(() => {
      })
    },
    getListForm () {
      this.FETCH_TLIST(this.payload).then(() => {
        this.listLength = Object.keys(this.fetchListData.data).length
        for (var i = 0; i < this.listLength; i++) {
          delete this.fetchListData.data[i].ID
          this.listData[i] = this.fetchListData.data[i]
        }
        this.tableKeys = Object.keys(this.listData[0])
        for (var j = 0; j < this.tableKeys.length; j++) {
          if (this.verifyColumn.indexOf(this.tableKeys[j]) < 0) {
            this.tableKeys.splice(j)
          }
        }
      })
    },
    handleCurrentChange (val) {
      this.payload.page = val
      this.getListForm()
    },
    toPage (val) {
      this.payload.page = val
      this.getListForm()
    },
    verify () {
      this.$confirm('是否确认校验无误', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        services.sheets.verify(this.sheetId).then((data) => {
          this.$router.replace('/verifiedSheets')
        })
      }).catch(() => {})
    },
    getVerifyData () {
      this.isLoading = true
      var id = this.fetchSheetData.targetId
      this.verifyPayload.column = ''
      this.verifyPayload.in = []
      this.verifyPayload.column = this.column
      for (var i = 0; i < this.listLength; i++) {
        this.verifyPayload.in[i] = this.listData[i][this.column]
      }
      services.sheets.getMirrorsTableData(id, this.verifyPayload).then((res) => {
        this.mirrorData = res
        if (this.mirrorData.length === 0) {
          this.$message('业务表没有相关数据')
        }
        this.isLoading = false
      })
    },
    goBack () {
      this.$router.go(-1)
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
