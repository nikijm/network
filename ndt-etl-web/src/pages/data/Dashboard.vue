<template>
  <page title="所有工作表">
    <div slot="tools">
      <el-select v-model="ajaxData.level" style="float:left;padding-right:10px" placeholder="按组织过滤">
        <el-option
          v-for="item in orgOtions"
          :label="item.value" :value="item.index">
        </el-option>
      </el-select>
      <el-select v-model="ajaxData.level" placeholder="按状态过滤">
        <el-option
          v-for="item in stateOtions"
          :label="item.value" :value="item.index">
        </el-option>
      </el-select>
    </div>
    <div slot="content">
      <div>
        <div class="row">
          <div class="col-md-12">
            <div class="sheet-card" v-for="item in sheetData">
              <div class="card-header">
                <span>{{item.fileName}}</span>
                <div class="ops">
                  <a href="#" title="查看数据">数据</a>
                  <span class="sep"></span>
                  <a href="#" title="对比数据">对比</a>
                </div>
              </div>
              <div class="card-body row">
                <!--active, complete, paused, warning-->
                <div class="col-md-3" :class="stateClass(item, 'cache')">
                  <div class="col-title">
                    <span>缓存</span>
                    <div class="ops" v-if="item.cache.percent && item.cache.percent !== 100">
                      <a href="#" class="badge error"  v-if="item.cache.status === '30'">异常</a>
                      <a href="#" class="glyphicon glyphicon-pause" title="暂停" v-else-if="item.cache.status !== '30' && stateClass(item, 'cache') === 'active'"></a>
                      <a href="#" class="glyphicon glyphicon-play" title="启动" v-else></a>
                    </div>
                  </div>
                  <div class="col-body">
                    <div><a href="#">{{item.cache.tableName}}</a></div>
                    <div class="percentage" :class="item.cache.percent === 100 ? 'done' : ''">{{item.cache.percent}}%</div>
                  </div>
                </div>
                <div class="col-md-3" :class="stateClass(item, 'clean')">
                  <div class="col-title">
                    <span>清洗</span>
                    <div class="ops" v-if="item.clean.percent && item.clean.percent !== 100">
                      <a href="#" class="badge error"  v-if="item.clean.status === '30'">异常</a>
                      <a href="#" class="glyphicon glyphicon-pause" title="暂停" v-else-if="item.clean.status !== '30' && stateClass(item, 'clean') === 'active'"></a>
                      <a href="#" class="glyphicon glyphicon-play" title="启动" v-else></a>
                    </div>
                  </div>
                  <div class="col-body" v-if="item.clean.tableName">
                    <div><a href="#">{{item.clean.tableName}}</a></div>
                    <div><a href="#">清洗规则</a></div>
                    <div class="percentage" :class="item.clean.percent === 100 ? 'done' : ''">{{item.clean.percent}}%</div>
                  </div>
                </div>
                <div class="col-md-3" :class="stateClass(item, 'convert')">
                  <div class="col-title">
                    <span>转换</span>
                    <div class="ops" v-if="item.convert.percent && item.convert.percent !== 100">
                      <a href="#" class="badge error" v-if="item.convert.status === '30'">异常</a>
                      <a href="#" class="glyphicon glyphicon-pause" title="暂停" v-else-if="item.convert.status !== '30' && stateClass(item, 'convert') === 'active'"></a>
                      <a href="#" class="glyphicon glyphicon-play" title="启动" v-else></a>
                    </div>
                  </div>
                  <div class="col-body" v-if="item.convert.tableName">
                    <div><a href="#">{{item.convert.tableName}}</a></div>
                    <div><a href="#">转换规则</a></div>
                    <div class="percentage" :class="item.convert.percent === 100 ? 'done' : ''">{{item.convert.percent}}%</div>
                  </div>
                </div>
                <div class="col-md-3" :class="stateClass(item, 'validate')">
                  <div class="col-title">
                    <span>校验</span>
                    <div class="ops" v-if="item.validate.percent && item.validate.percent !== 100">
                      <a href="#" class="badge error" v-if="item.validate.status === '30'">异常</a>
                      <a href="#" class="glyphicon glyphicon-pause" title="暂停" v-else-if="item.validate.status !== '30' && stateClass(item, 'validate') === 'active'"></a>
                      <a href="#" class="glyphicon glyphicon-play" title="启动" v-else></a>
                    </div>
                  </div>
                  <div class="col-body" v-if="item.validate.tableName">
                    <div><a href="#">校验规则</a></div>
                    <div class="percentage" :class="item.validate.percent === 100 ? 'done' : ''">{{item.validate.percent}}%</div>
                  </div>
                </div>
              </div>
            </div>

            <!--<div class="sheet-card">-->
              <!--<div class="card-header">-->
                <!--2016年市级以上重点龙头企业正式名单-公示板.xlsx-->
                <!--<div class="ops">-->
                  <!--<a href="#" title="查看数据">数据</a>-->
                  <!--<span class="sep"></span>-->
                  <!--<a href="#" title="对比数据">对比</a>-->
                <!--</div>-->
              <!--</div>-->
              <!--<div class="card-body row">-->
                <!--<div class="col-md-3 complete">-->
                  <!--<div class="col-title">缓存</div>-->
                  <!--<div class="col-body">-->
                    <!--<div><a href="#">ETL_BUFF_XXX</a></div>-->
                    <!--<div class="percentage">100%</div>-->
                  <!--</div>-->
                <!--</div>-->
                <!--<div class="col-md-3 complete">-->
                  <!--<div class="col-title">清洗</div>-->
                  <!--<div class="col-body">-->
                    <!--<div><a href="#">ETL_BUFF_XXX</a></div>-->
                    <!--<div><a href="#">清洗规则</a></div>-->
                    <!--<div class="percentage">100%</div>-->
                  <!--</div>-->
                <!--</div>-->
                <!--<div class="col-md-3 complete">-->
                  <!--<div class="col-title">转换</div>-->
                  <!--<div class="col-body">-->
                    <!--<div><a href="#">ETL_BUFF_XXX</a></div>-->
                    <!--<div><a href="#">转换规则</a></div>-->
                    <!--<div class="percentage">100%</div>-->
                  <!--</div>-->
                <!--</div>-->
                <!--<div class="col-md-3 complete">-->
                  <!--<div class="col-title">校验</div>-->
                  <!--<div class="col-body">-->
                    <!--<div><a href="#">ETL_BUFF_XXX</a></div>-->
                    <!--<div><a href="#">校验规则</a></div>-->
                    <!--<div class="percentage">100%</div>-->
                  <!--</div>-->
                <!--</div>-->
              <!--</div>-->
            <!--</div>-->

            <!--<div class="sheet-card">-->
              <!--<div class="card-header">-->
                <!--2016年市级以上重点龙头企业正式名单-公示板.xlsx-->
                <!--<div class="ops">-->
                  <!--<a href="#" title="查看数据">数据</a>-->
                  <!--<span class="sep"></span>-->
                  <!--<a href="#" title="对比数据">对比</a>-->
                <!--</div>-->
              <!--</div>-->
              <!--<div class="card-body row">-->
                <!--<div class="col-md-3 active">-->
                  <!--<div class="col-title">缓存</div>-->
                  <!--<div class="col-body">-->
                    <!--<div><a href="#">ETL_BUFF_XXX</a></div>-->
                    <!--<div class="percentage done">100%</div>-->
                  <!--</div>-->
                <!--</div>-->
                <!--<div class="col-md-3 paused">-->
                  <!--<div class="col-title">清洗<div class="ops"><a href="#" class="glyphicon glyphicon-play" title="启动"></a></div></div>-->
                  <!--<div class="col-body">-->
                    <!--<div><a href="#">ETL_WASH_XXX</a></div>-->
                    <!--<div><a href="#">清洗规则</a></div>-->
                    <!--<div class="percentage"><span>40</span>%</div>-->
                  <!--</div>-->
                <!--</div>-->
                <!--<div class="col-md-3"><div class="col-title">转换</div></div>-->
                <!--<div class="col-md-3"><div class="col-title">校验</div></div>-->
              <!--</div>-->
            <!--</div>-->

            <!--<div class="sheet-card">-->
              <!--<div class="card-header">-->
                <!--2016年市级以上重点龙头企业正式名单-公示板.xlsx-->
                <!--<div class="ops">-->
                  <!--<a href="#" title="查看数据">数据</a>-->
                  <!--<span class="sep"></span>-->
                  <!--<a href="#" title="对比数据">对比</a>-->
                <!--</div>-->
              <!--</div>-->
              <!--<div class="card-body row">-->
                <!--<div class="col-md-3 active">-->
                  <!--<div class="col-title">缓存</div>-->
                  <!--<div class="col-body">-->
                    <!--<div><a href="#">ETL_BUFF_XXX</a></div>-->
                    <!--<div class="percentage done">100%</div>-->
                  <!--</div>-->
                <!--</div>-->
                <!--<div class="col-md-3 warning">-->
                  <!--<div class="col-title">清洗<div class="ops"><a href="#" class="badge error">异常</a><a href="#" class="glyphicon glyphicon-play" title="启动"></a></div></div>-->
                  <!--<div class="col-body">-->
                    <!--<div><a href="#">ETL_WASH_XXX</a></div>-->
                    <!--<div><a href="#">清洗规则</a></div>-->
                    <!--<div class="percentage"><span>20</span>%</div>-->
                  <!--</div>-->
                <!--</div>-->
                <!--<div class="col-md-3"><div class="col-title">转换</div></div>-->
                <!--<div class="col-md-3"><div class="col-title">校验</div></div>-->
              <!--</div>-->
            <!--</div>-->
          </div>
        </div>
      </div>
    </div>
  </page>
</template>

<script>
  import Page from '../../components/Page.vue'
  import VATable from '../../components/VATable.vue'
  import { mapGetters, mapActions } from 'vuex'
  // import { statusCallback } from '../../lib/utils'
  // import sheet from '../../../mock-server/data/sheetData.js'
  import axios from 'axios'
  export default {
    data () {
      return {
        //  下拉
        orgOtions: [
          { value: '成都银行', index: 0 },
          { value: '工商银行', index: 1 },
          { value: '建设银行', index: 2 },
          { value: '农业银行', index: 3 }
        ],
        stateOtions: [
          { value: '已缓存', index: 0 },
          { value: '已清洗', index: 1 },
          { value: '已转换', index: 2 },
          { value: '已对比', index: 3 }
        ],
        noData: 1,
        ajaxData: {
          page: null,
          content: '',
          level: ''
        },
        sheetData: []
      }
    },
    components: {
      'va-table': VATable,
      'page': Page
    },
    computed: {
      ...mapGetters([
        'totalLogs'
      ])
    },
    mounted () {
      this.loadSheet()
    },
    methods: {
      ...mapActions([
        'fetchTotalLogs'//  发起查询日志的action
      ]),
      loadSheet () {
        axios.get('/api/sheets').then((res) => {
          this.sheetData = res.data
        }).catch(() => {
          // err
        })
      },
      stateClass (item, type) {
        // 1：进行中，10：成功，5：暂停，30：错误
        if (!item[type]) {
          return ''
        }
        // 这个Excel表所有的都处理好了
        if (item.cache.status === '10' && item.clean.status === '10' && item.convert.status === '10' && item.validate.status === '10') {
          return 'completed'
        }
        let state = ''
        switch (item[type].status) {
          case '1':
          case '10':
            state = 'active'
            break
          case '5':
            state = 'paused'
            break
          case '30':
            state = 'warning'
            break
          default:
            state = ''
        }
        return state
      }
    }
  }
</script>
<style scoped>
  .sheet-card {
    margin-bottom: 10px;
    border-bottom: 1px solid #eee;
    background: #fff;
  }
  .sheet-card .card-header {
    padding: 10px;
    background: #fafafa;
    border-bottom: 1px solid #ddd;
    position: relative;
  }
  .card-header .ops{
    position:absolute;
    right:10px;
    top:12px;
    font-size:12px;
  }
  .col-title{
    padding:10px;
    font-size:14px;
    text-align: center;
    border-bottom:1px solid #eee;
    position:relative;
  }
  .col-title > .ops{
    position: absolute;
    top:10px;
    right:10px;
  }
  .col-body{
    padding:5px 10px;
    font-size:10px;
    position: relative
  }
  .col-body a, .col-body a:visited{
    color:#999!important;
  }
  .col-body .percentage{
    position: absolute;
    right:10px;
    top:10px;
    font-size:24px;
    font-weight: 500;
    font-family: Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif;
    color:#999;
  }
  .col-body .percentage span {
    color: inherit;
  }
  .card-body .col-md-3{
    height:100px;
    border-bottom:4px solid #eee;
  }
  .active .percentage {
    color: #58B957;
  }
  .active .percentage.done{
    color:#bbb;
  }
  .card-body .col-md-3.active{
    border-color: #58B957;
  }
  .card-body .col-md-3.paused{
    border-color: #333;
  }
  .card-body .col-md-3.warning{
    border-color: #DB524B;
  }
  .card-body .col-md-3.complete{
    border-color: #39A5FF;
  }
  .card-body .col-md-3.completed{
    border-color: #39A5FF;
  }
  .paused .percentage{
    color:#333;
  }
  .warning .percentage{
    color: #DB524B;
  }
  .complete .percentage{
    color:#39A5FF;
  }
  .completed .percentage{
    color:#39A5FF;
  }
  .card-body .col-md-3:not(:last-child){
    border-right:1px solid #eee;
  }
  .sheet-card .card-body{
    margin-left: 0;
    margin-right: 0;
  }
  .card-body .col-md-3.paused {
    border-bottom-color:  #333;
  }
  .card-body .badge.error {
    background-color: #FC3723;
    margin-right: 5px;
  }
</style>
