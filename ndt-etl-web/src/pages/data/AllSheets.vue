<template>
  <page title="所有工作表" class="allSheets">
    <div slot="tools">
      <el-select v-model="ajaxData.orgId" clearable filterable style="float:left;padding-right:10px" placeholder="组织机构" filterable @change="changeOrg" size='small'>
        <el-option
        v-for="item in orgOptions"
        :label="item.name" :value="item.id">
      </el-option>
    </el-select>
    <el-select v-model="stateVal" clearable filterable placeholder="文件状态" @change="changeStateVal(stateVal)" size='small'>
      <el-option
      v-for="item in stateOptions"
      :label="item.name" :value="item.id">
    </el-option>
  </el-select>
 <!--  <el-input placeholder="请输入文件名或用户名" class="tools-button" v-model='ajaxData.keyword'@keyup.enter.native="changeSearchs" size='small'>
    <el-button slot="append" icon="search" @click="searchContent"></el-button>
  </el-input> -->
  <el-button size='small' @click='recorded'>刷新文件列表</el-button>
</div>
<div slot="content">
  <div class="col-md-12">
    <div class="col-md-6"  style="border-right: 1px solid #EAEAEA">

    <div class="table-head">
      <table class="table table-hover table-fixed">
          <colgroup>
              <col style="width: 50%;"/>
              <col />
          </colgroup>
          <thead>
              <tr>
              <th>文件名</th>
              <th >缓存</th>
              <th>清洗</th>
              <th>转换</th>
              <th>校验</th>
              </tr>
          </thead>
      </table>
      </div>
     <!--  <table class="table table-hover table-fixed" v-loading.body="loading">
        <thead>
          <tr>
            <th width="50%">文件名</th>
            <th>缓存</th>
            <th>清洗</th>
            <th>转换</th>
            <th>校验</th>
          </tr>
        </thead> -->

        <div class="table-body" ref="row2">
          <table class="table table-hover table-fixed" v-loading.body="loading">
          <colgroup>
              <col style="width: 50%;"/>
              <col />
          </colgroup>

        <tbody >
          <tr v-for="item in tableAllData">
            <td v-text="item.fileName"></td>
            <td>
              <div v-if="item.statusCache === -2">
              <!-- <a ><i class="fa fa-adjust err" @click="clickError(item, 'Cache')"></i></a> -->
              <el-tooltip placement="right" effect="light">
                  <div slot="content">
                  <!-- <p style='cursor：pointer' @click="clickRules(item, 'cache')" ><a>规则</a></p> -->
                  <p style='cursor：pointer' @click="clickError(item, 'Cache')" ><a>查看原因</a></p>
                  </div>
                  <i class="fa fa-adjust err" ></i>
                </el-tooltip>

              </div>
              <div v-if="item.statusCache === -1">
                <el-tooltip placement="right" effect="light">
                  <div slot="content">
                  <!-- <p style='cursor：pointer' @click="clickRules(item, 'cache')" ><a>规则</a></p> -->
                  <p style='cursor：pointer' @click="clickStart(item, 'Cache')"><a>启动</a></p>
                  </div>
                  <i class="fa fa-circle-o" ></i>
                </el-tooltip>
              </div>

              <div v-if="item.statusCache === 0">
                <el-tooltip placement="right" effect="light">
                  <div slot="content">
                  <!-- <p style='cursor：pointer' @click="clickRules(item, 'cache')" ><a>规则</a></p> -->
                  <p>进行中</p>
                  </div>
                  <i class="fa fa-adjust"></i>
                </el-tooltip>
              </div>
              <div v-if="item.statusCache === 1">
                <el-tooltip placement="right" effect="light">
                  <div slot="content">
                  <!-- <p style='cursor：pointer' @click="clickRules(item, 'cache')" ><a>规则</a></p> -->
                  <p @click="ClickCacheData(item.etlDatafileId)"><a>数据</a></p>
                  <!-- <p @click="comparesData(item,'cache')"><a>对比</a></p> -->
                  </div>
                  <i class="fa fa-circle"></i>
                </el-tooltip>
              </div>
            </td>
            <td>
              <div v-if="item.statusClean === -2">
                <el-tooltip placement="right" effect="light">
                  <div slot="content">
                  <p style='cursor：pointer' @click="clickRules(item, 'clean')" ><a>规则</a></p>
                  <p @click="clickError(item, 'Clean')">错误</p>
                  </div>
                  <i class="fa fa-adjust err"></i>
                </el-tooltip>
              </div>
              <div v-if="item.statusClean === -1">
                <el-tooltip placement="right" effect="light">
                  <div slot="content">
                  <p v-if='item.statusCache===1' @click="clickStart(item, 'Clean')" ><a>启动</a></p>
                  <p @click="clickRules(item, 'clean')"><a>规则</a></p>
                  </div>
                  <i class="fa fa-circle-o"></i>
                </el-tooltip>
              </div>
              <div v-if="item.statusClean === 0">
                <el-tooltip placement="right" effect="light">
                  <div slot="content">
                  <p style='cursor：pointer' @click="clickRules(item, 'clean')" ><a>规则</a></p>
                  <p>进行中</p>
                  </div>
                  <i class="fa fa-adjust"></i>
                </el-tooltip>
              </div>
              <div v-if="item.statusClean === 1">
                <el-tooltip placement="right" effect="light">
                  <div slot="content">
                  <p style='cursor：pointer' @click="clickRules(item, 'clean')" ><a>规则</a></p>
                  <p @click="comparesData(item,'clean')"><a>数据对比</a></p>
                  </div>
                  <i class="fa fa-circle"></i>
                </el-tooltip>
              </div>
            </td>
            <td>
              <div v-if="item.statusConvert === -2">
                <el-tooltip placement="right" effect="light">
                  <div slot="content">
                  <p style='cursor：pointer' @click="clickRules(item, 'convert')" ><a>规则</a></p>
                  <p @click="clickError(item, 'Convert')">错误</p>
                  </div>
                  <i class="fa fa-adjust err"></i>
                </el-tooltip>
              </div>
              <div v-if="item.statusConvert === -1">
                <el-tooltip placement="right" effect="light">
                  <div slot="content">
                  <p style='cursor：pointer' @click="clickRules(item, 'convert')" ><a>规则</a></p>
                  <p v-if='item.statusCache===1&&item.statusClean===1' @click="clickStart(item, 'Convert')"><a>启动</a></p>
                  </div>
                  <i class="fa fa-circle-o"></i>
                </el-tooltip>
              </div>
              <div v-if="item.statusConvert === 0">
                <el-tooltip placement="right" effect="light">
                  <div slot="content">
                  <p style='cursor：pointer' @click="clickRules(item, 'convert')" ><a>规则</a></p>
                  <p>进行中</p>
                  </div>
                  <i class="fa fa-adjust"></i>
                </el-tooltip>
              </div>
              <div v-if="item.statusConvert === 1">
                <el-tooltip placement="right" effect="light">
                  <div slot="content">
                  <p style='cursor：pointer' @click="clickRules(item, 'convert')" ><a>规则</a></p>
                   <p @click="comparesData(item,'convert')"><a>数据对比</a></p>
                  </div>
                  <i class="fa fa-circle"></i>
                </el-tooltip>
              </div>
            </td>
            <td>
              <div v-if="item.statusValidate === -2">
                <el-tooltip placement="right" effect="light">
                  <div slot="content">
                  <p style='cursor：pointer' @click="clickRules(item, 'validate')" ><a>规则</a></p>
                  <p @click="clickError(item, 'Validate')">错误</p>
                  </div>
                  <i class="fa fa-adjust err"></i>
                </el-tooltip>
              </div>
              <div v-if="item.statusValidate === -1">
                <el-tooltip placement="right" effect="light">
                  <div slot="content">
                  <p style='cursor：pointer' @click="clickRules(item, 'validate')" ><a>规则</a></p>
                  <p v-if='item.statusCache===1&&item.statusClean===1 &&item.statusConvert===1' @click="clickStart(item, 'Validate')"><a>启动</a></p>
                  </div>
                  <i class="fa fa-circle-o"></i>
                </el-tooltip>
              </div>
              <div v-if="item.statusValidate === 0">
                <el-tooltip placement="right" effect="light">
                  <div slot="content">
                  <p style='cursor：pointer' @click="clickRules(item, 'validate')"><a>规则</a></p>
                  <p>进行中</p>
                  </div>
                  <i class="fa fa-adjust"></i>
                </el-tooltip>
              </div>
              <div v-if="item.statusValidate === 1">
                <el-tooltip placement="right" effect="light">
                  <div slot="content">
                  <p style='cursor：pointer' @click="clickRules(item, 'validate')" ><a>规则</a></p>
                   <p @click="comparesData(item,'verify')"><a>数据对比</a></p>
                  </div>
                  <i class="fa fa-circle"></i>
                </el-tooltip>
              </div>
            </td>
          </tr>
        </tbody>
      <!-- </table> -->

      </table>
      </div>
      <!-- 分页 -->
      <el-pagination @current-change="changePage"
      :current-page="paging.current"
      layout="prev, pager, next, jumper"
      :total="paging.total"
      :page-size="paging.page_size">
    </el-pagination>
  </div>
  <div class="col-md-6 data-list">
    <h4 align="center" style="color:#6495ED">数据处理任务调度</h4>


    <div ref='scollContent' class="scrollauto">
    <table class="table table-fixed">
      <thead>
        <tr>
          <th width="50%" style="color:#6495ED">缓存任务</th><th width="30%"></th>
          <th v-if='cacheHingeStatus'><i class="fa fa-pause" title='暂停' @click="changeALL('cacheHingeStatus', 'cache')"></i></th>
          <th v-else><i class="fa fa-play" title='开始' @click="changeALL('cacheHingeStatus', 'cache')"></i></th>
          <th v-show='cacheHingeStatus'><i class="fa fa-stop" title='停止' @click="changeALL('cacheHingeStatus', 'cache')"></i></th>
          <th v-show='!cacheHingeStatus'><i class="fa fa-stop" title='停止' style="color: #C0C0C0"></i></th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="jst.isNullOrEmpty(cacheDataFiles, true)">
          <td colspan="4" align="center">暂时没有缓存任务</td>
        </tr>
        <tr v-else v-for="item in cacheDataFiles">
          <td v-text="item.fileName"></td>
          <td><el-progress :text-inside="true" :stroke-width="15" :percentage="item.percents"></el-progress></td>
          <!-- <span v-show='item.percents!=0'>%</span> -->
          <td v-if='item.suspend'><i class="fa fa-play"  @click="handleplay(item, 'cache')" title="开始"></i></td>
          <td v-else><i class="fa fa-pause"  title="暂停" @click="handlepause(item, 'cache')"></i></td>
          <td v-if='item.stop'><i class="fa fa-stop" title="停止" style='color: #C0C0C0'></i></td>
          <td v-else><i class="fa fa-stop" title="停止" @click="handStop(item, 'cache')"></i></td>
        </tr>
      </tbody>
    </table>
    <table class="table table-fixed">
      <thead>
        <tr>
          <th width="50%" style="color:#6495ED">清洗任务</th><th width="30%"></th>
          <th v-if='cleanHingeStatus'><i class="fa fa-pause" title='暂停' @click="changeALL('cleanHingeStatus', 'clean')"></i></th>
          <th v-else><i class="fa fa-play" title="开始" @click="changeALL('cleanHingeStatus', 'clean')"></i></th>
          <th v-show='cleanHingeStatus'><i class="fa fa-stop" title='停止' @click="changeALL('cleanHingeStatus', 'clean')"></i></th>
          <th v-show='!cleanHingeStatus'><i class="fa fa-stop" title='停止' style="color: #C0C0C0"></i></th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="jst.isNullOrEmpty(cleanDataFiles, true)">
          <td colspan="4" align="center">暂时没有清洗任务</td>
        </tr>
        <tr v-else v-for="item in cleanDataFiles">
          <td v-text="item.fileName"></td>
          <td><el-progress :text-inside="true" :stroke-width="15" :percentage="item.percents"></el-progress></td>
          <td v-if='item.suspend'><i class="fa fa-play" title="开始" @click="handleplay(item, 'clean')"></i></td>
          <td v-else><i class="fa fa-pause"  title="暂停" @click="handlepause(item, 'clean')"></i></td>

          <td v-if='item.stop'><i class="fa fa-stop" title="停止" style='color: #C0C0C0'></i></td>
          <td v-else><i class="fa fa-stop" title="停止" @click="handStop(item, 'clean')"></i></td>
        </tr>
      </tbody>
    </table>
    <table class="table table-fixed">
      <thead>
        <tr>
          <th width="50%" style="color:#6495ED">转换任务</th><th width="30%"></th>
          <th v-if='convertHingeStatus'><i class="fa fa-pause" title='暂停' @click="changeALL('convertHingeStatus', 'convert')"></i></th>
          <th v-else><i class="fa fa-play" title="开始" @click="changeALL('convertHingeStatus', 'convert')"></i></th>
          <th v-show='convertHingeStatus' @click="changeALL('convertHingeStatus', 'convert')"><i class="fa fa-stop" title='停止'></i></th>
          <th v-show='!convertHingeStatus'><i class="fa fa-stop" title='停止' style="color: #C0C0C0"></i></th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="jst.isNullOrEmpty(convertDataFiles, true)">
          <td colspan="4" align="center">暂时没有转换任务</td>
        </tr>
        <tr v-else v-for="item in convertDataFiles">
          <td v-text="item.fileName"></td>
          <td><el-progress :text-inside="true" :stroke-width="15" :percentage="item.percents"></el-progress></td>
          <td v-if='item.suspend'><i class="fa fa-play" title="开始" @click="handleplay(item, 'convert')"></i></td>
          <td v-else><i class="fa fa-pause"  title="暂停" @click="handlepause(item, 'convert')"></i></td>
          <td v-if='item.stop'><i class="fa fa-stop" title="停止" style='color: #C0C0C0'></i></td>
          <td v-else><i class="fa fa-stop" title="停止" @click="handStop(item, 'convert')"></i></td>
        </tr>
      </tbody>
    </table>
    <table class="table table-fixed">
      <thead>
        <tr>
          <th width="50%" style="color:#6495ED">校验任务</th><th width="30%"></th>
          <th v-if='verifyHingeStatus'><i class="fa fa-pause" title='暂停' @click="changeALL('verifyHingeStatus', 'verify')"></i></th>
          <th v-else><i class="fa fa-play" title="开始" @click="changeALL('verifyHingeStatus', 'verify')"></i></th>
          <th v-show='verifyHingeStatus'><i class="fa fa-stop" title='停止' @click="changeALL('verifyHingeStatus', 'verify')"></i></th>
          <th v-show='!verifyHingeStatus'><i class="fa fa-stop" title='停止' style="color: #C0C0C0"></i></th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="jst.isNullOrEmpty(verifyDataFiles, true)">
          <td colspan="4" align="center">暂时没有校验任务</td>
        </tr>
        <tr v-else v-for="item in verifyDataFiles">
          <td v-text="item.fileName"></td>
          <td><el-progress :text-inside="true" :stroke-width="15" :percentage="item.percents"></el-progress></td>
          <td v-if='item.suspend'><i class="fa fa-play" title="开始"  @click="handleplay(item, 'verify')"></i></td>
          <td v-else><i class="fa fa-pause"  title="暂停" @click="handlepause(item, 'verify')"></i></td>
          <td v-if='item.stop'><i class="fa fa-stop" title="停止" style='color: #C0C0C0'></i></td>
          <td v-else><i class="fa fa-stop" title="停止" @click="handStop(item, 'verify')"></i></td>
        </tr>
      </tbody>
    </table>
  </div>
  </div>
</div>
</div>
</page>
</template>

<script>
  import Page from '../../components/Page.vue'
  import VATable from '../../components/VATable.vue'
  import * as jst from '../../lib/utils'
  import { mapActions } from 'vuex'
  export default {
    data () {
      return {
        //  下拉
        loading: true,
        orgOptions: [],
        orgVal: '',
        stateOptions: [],
        stateVal: '',
        tableData: [],
        tableAllData: [],
        tableCategoryData: {
          cacheList: [],
          cleanList: [],
          transformList: [],
          verifyList: []
        },
        sheetPaging: {
          current: 1,
          total: 100,
          page_size: 10
        },
        jst: jst,
        cacheDataFiles: [],
        cacheHingeStatus: '',
        cleanDataFiles: [],
        cleanHingeStatus: '',
        convertDataFiles: [],
        convertHingeStatus: '',
        verifyDataFiles: [],
        verifyHingeStatus: '',
        paging: [],
        keyData: '',
        keyValue: '',
        typeStatu: '',
        operateId: '',
        ajaxData: {
          orgId: '',
          etlDatafileId: '',
          keyword: '',
          current: null
        },
        scheduleStart: {
          operateId: '',
          operateType: '',
          fileId: ''
        },
        rightScheduleStart: {
          operateId: '',
          operateType: '',
          fileId: ''
        },
        pauseOne: {
          operateId: '',
          operateType: '',
          fileId: ''
        },
        stopOne: {
          operateId: '',
          operateType: '',
          fileId: ''
        },
        allChange: {
          operateId: '',
          isStop: ''
        },
        screenHeight: document.body.clientHeight,
        fileLog: {},
        compare: {
          id: '',
          category: ''
        },
        rules: {
          id: '',
          category: ''
        },
        current: ''
      }
    },
    components: {
      'va-table': VATable,
      'page': Page
    },
    computed: {
    },
    created () {
      this.intervalid = setInterval(() => {
        if (this.$route.path !== '/allSheets') {
          clearInterval(this.intervalid)
        }
        this.getDataDispatchs().then((res) => {
          this.cacheDataFiles = res.data.cacheDataFiles
          this.cacheHingeStatus = res.data.cacheHingeStatus
          this.cleanDataFiles = res.data.cleanDataFiles
          this.cleanHingeStatus = res.data.cleanHingeStatus
          this.convertDataFiles = res.data.convertDataFiles
          this.convertHingeStatus = res.data.convertHingeStatus
          this.verifyDataFiles = res.data.verifyDataFiles
          this.verifyHingeStatus = res.data.verifyHingeStatus
        })
      }, 1000)
    },
    watch: {
      keyData: function (val, oldvalue) {
        delete this.ajaxData[oldvalue]
      }
    },
    mounted () {
      this.loadData()
      this.$refs.row2.style.height = this.screenHeight - 240 + 'px'
      this.$refs.scollContent.style.height = this.screenHeight - 240 + 'px'
    },
    methods: {
      ...mapActions([
        'getDataFiles',
        'getDataDispatchs',
        'getOneDataFiles',
        'startOneDataFiles',
        'pauseOneDataFiles',
        'stopOneDataFiles',
        'changeAllDataFiles',
        'errorMessage'
      ]),
      /**
       * 获取所有数据
       */
      changeOrg () {
        this.ajaxData.current = 1
        this.loadData()
      },
      // 翻页
      changePage (val) {
        this.ajaxData.current = val
        this.loadData()
      },
      // changeSearchs () {
      //   if (event.keyCode === 13) {
      //     this.searchContent()
      //   }
      // },
      // // 搜索
      // searchContent () {
      //   this.ajaxData.current = 1
      //   this.loadData()
      // },
      loadData () {
        let _this = this
        if (JSON.parse(sessionStorage.getItem('newCurrent'))) {
          this.ajaxData.current = JSON.parse(sessionStorage.getItem('newCurrent'))
          sessionStorage.removeItem('newCurrent')
        }
        this.getDataFiles(this.ajaxData).then((res) => {
          jst.statusCallback(res, function (that) {
            if (res && res.data) {
              _this.loading = false
              _this.paging = res.data.paging
              _this.orgOptions = res.data.orgs
              _this.stateOptions = res.data.dataFileStatusOptions
              _this.tableAllData = res.data.dataFiles
            }
          })
        })
      },
      // 手动刷新
      recorded () {
        this.loadData()
        this.$message({
          message: '刷新成功',
          type: 'success'
        })
      },
      changeStateVal (item) {
        let data = ''
        data = item.split('_')
        this.keyData = data[0]
        this.keyValue = data[1]
        this.ajaxData[this.keyData] = this.keyValue
        this.loadData()
      },
       // 左边 启动任务
      clickStart (item, type) {
        var operateId = ''
        if (item.statusCache === -1) {
          operateId = 1
        } else if (item.statusClean === -1) {
          operateId = 2
        } else if (item.statusConvert === -1) {
          operateId = 3
        } else {
          operateId = 4
        }
        this.scheduleStart = {
          operateId: operateId,
          operateType: item.fileName,
          fileId: item.etlDatafileId
        }
        this.getOneDataFiles(this.scheduleStart).then((res) => {
          jst.statusCallback(res, function (that) {
            that.loadData()
          //   }, this)
          // } else {
          //   this.$message.error({
          //     message: '启动失败',
          //     type: 'success'
          }, this)
          // }
        })
      },
      // 点击显示错误message
      clickError (item) {
        this.fileLog = {}
        this.errorMessage(item.etlDatafileId).then((res) => {
          this.fileLog = res.data.fileLog.message
          this.$alert(`${this.fileLog}`, '错误提示', {
            confirmButtonText: '确定'
          })
        })
      },
      setSession () {
        this.current = this.ajaxData.current
        sessionStorage.setItem('newCurrent', JSON.stringify(this.current))
      },
       // 数据
      ClickCacheData (num) {
        this.$router.push('/CachesData?id=' + num)
        this.setSession()
      },
      // 规则
      clickRules (item, type) {
        this.rules = {
          id: item.etlDatafileId,
          category: type
        }
        this.setSession()
        sessionStorage.setItem('rulesData', JSON.stringify(this.rules))
        this.$router.push({ path: '/AllSheetsRules' })
      },
      // 对比
      comparesData (item, type) {
        this.compare = {
          id: item.etlDatafileId,
          category: type
        }
        this.setSession()
        sessionStorage.setItem('typeData', JSON.stringify(this.compare))
        this.$router.push({ name: 'Compares', params: this.compare })
      },
      common (type) {
        switch (type) {
          case 'cache':
            this.operateId = 1
            break
          case 'clean':
            this.operateId = 2
            break
          case 'convert':
            this.operateId = 3
            break
          case 'verify':
            this.operateId = 4
            break
        }
      },
      // 右边 启动单个任务
      handleplay (item, type) {
        this.common(type)
        this.rightScheduleStart = {
          operateId: this.operateId,
          operateType: item.fileName,
          fileId: item.etlDatafileId
        }
        this.startOneDataFiles(this.rightScheduleStart).then((res) => {
          if (res.data === 0) {
            jst.statusCallback(res, function (that) {
              // that.loadData()
            }, this)
          } else {
            this.$message.error({
              message: '启动失败',
              type: 'success'
            })
          }
        })
      },
      // 右边 暂停单个任务
      handlepause (item, type) {
        this.common(type)
        this.pauseOne = {
          operateId: this.operateId,
          operateType: item.fileName,
          fileId: item.etlDatafileId
        }
        this.pauseOneDataFiles(this.pauseOne).then((res) => {
          if (res.data === 0) {
            jst.statusCallback(res, function (that) {
              // that.loadData()
            }, this)
          } else {
            this.$message.error({
              message: '暂停失败',
              type: 'success'
            })
          }
        })
      },
      // 右边 停止单个任务
      handStop (item, type) {
        this.common(type)
        this.stopOne = {
          operateId: this.operateId,
          operateType: item.fileName,
          fileId: item.etlDatafileId
        }
        this.stopOneDataFiles(this.stopOne).then((res) => {
          if (res.data === 0) {
            jst.statusCallback(res, function (that) {
              // that.loadData()
            }, this)
          } else {
            this.$message.error({
              message: '停止失败',
              type: 'success'
            })
          }
        })
      },
      changeALL (stute, type, val) {
        if (stute === 'cacheHingeStatus') {
          if (this.cacheHingeStatus === true) {
            this.allChange = {
              operateId: 1,
              isStop: true
            }
          } else {
            this.allChange = {
              operateId: 1,
              isStop: false
            }
          }
        } else if (stute === 'cleanHingeStatus') {
          if (this.cleanHingeStatus === true) {
            this.allChange = {
              operateId: 2,
              isStop: true
            }
          } else {
            this.allChange = {
              operateId: 2,
              isStop: false
            }
          }
        } else if (stute === 'convertHingeStatus') {
          if (this.convertHingeStatus === true) {
            this.allChange = {
              operateId: 3,
              isStop: true
            }
          } else {
            this.allChange = {
              operateId: 3,
              isStop: false
            }
          }
        } else if (stute === 'verifyHingeStatus') {
          if (this.verifyHingeStatus === true) {
            this.allChange = {
              operateId: 4,
              isStop: true
            }
          } else {
            this.allChange = {
              operateId: 4,
              isStop: false
            }
          }
        }
        this.changeAllDataFiles(this.allChange).then((res) => {
          if (res.data === 0) {
            jst.statusCallback(res, function (that) {
            }, this)
          } else {
            this.$message.error({
              message: '失败',
              type: 'error'
            })
          }
        })
      }
    }
  }
</script>
<style>
  .allSheets .fa-circle {
    color: #458B00
  }
  .allSheets .fa-adjust {
     color: #458B00
  }
  .allSheets .fa-adjust.err {
    color: #CD0000
  }
  .allSheets .fa-pause {
    color: #0000FF
  }
  .allSheets .fa-stop {
    color: #CD0000
  }
  .allSheets .fa-play {
    color: #458B00
  }
  .allSheets .box-body{
    padding-top: 0px;
    padding-bottom: 0px;
  }
  .table-head{padding-right:17px;}
.table-head .table{ margin-bottom: 0 }
.table-body{width:100%;overflow-y:auto;}
.table-head table,.table-body table{width:100%;}
.scrollauto {
  overflow-y:auto;
}
</style>
