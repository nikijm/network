<template>
  <div>
    <page title="数据清洗">
      <div slot="tools">
       <el-select v-model="ajaxData.etlBusinessTypeId" filterable clearable placeholder="选择分类" @change="changeBussinessTypes" v-if='typeSelectShow'>
       <el-option
        v-for="item in allData.typedata" :label="item.name"
        :value="item.etlBusinessTypeId">
      </el-option>
    </el-select>
 <el-select v-model="ajaxData.orgId" filterable clearable placeholder="选择组织" @change="changeBussinessTypes" v-if='ajaxData.etlBusinessTypeId'>
    <el-option
      v-for="item in allData.orgselect" :label="item.name"
      :value="item.id">
    </el-option>
  </el-select>
   <el-tooltip class="item" effect="light" content="请选择分类" placement="bottom" v-else>   
    <el-select v-model="ajaxData.orgId" filterable disabled clearable placeholder="选择组织" @change="changeBussinessTypes">
    <el-option
      v-for="item in allData.orgselect" :label="item.name"
      :value="item.id">
    </el-option>
  </el-select>
 </el-tooltip>
<!--   <el-input placeholder="请输入单位(个人)或法人" class="tools-button" @keyup.native='changeSearchs($event)' v-model="ajaxData.keyword" v-if='ajaxData.etlBusinessTypeId'>
    <el-button slot="append" icon="search" @click="searchContent"></el-button>
  </el-input>
<el-tooltip class="item" effect="light" content="请选择分类" placement="bottom" v-else>
  <el-input placeholder="请输入单位(个人)或法人" :disabled="true" class="tools-button" @keyup.native='changeSearchs($event)' v-model="ajaxData.keyword" >
    <el-button slot="append" icon="search" @click="searchContent"></el-button>
  </el-input>
   </el-tooltip> -->
</div>
<div slot="content">
  <div style="width:100%;overflow:auto">
    <table style="width:auto;min-width: 100%" class="table table-hover table-nowrap" v-if="fetchCachesData.datas || fetchCachesData.header">
      <thead>
       <!--  <tr v-if="fetchCachesData.datas.length>0 && fetchCachesData.datas!= null">
          <th v-for="(value, key) in fetchCachesData.datas[0]">{{key}}</th>
        </tr> -->
        <tr>
          <th>清洗结果</th>
          <th>原因</th>
          <th>操作</th>
          <th v-for="(value, key) in fetchCachesData.header">{{value}}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in fetchCachesData.datas">
         <td>{{item.RESULT}}</td>
          <td>{{item.REASON}}</td>
          <td></td>
         <td v-for="(value, key) in fetchCachesData.header">{{item[key]}}</td>
        </tr>
      </tbody>
    </table>
      <table class="table table-hover table-nowrap" v-else >
        <thead style="width: 100%;height: 100%;display: flex;justify-content: center;align-items: center;">
        <tr v-if='ajaxData.etlBusinessTypeId'>
          暂无数据
        </tr>
        <div v-else style="width: 100%;height: 100%;">
          <p style='font-weight: 700'>点击查看分类所属信息:</p>
          <span class="typeClass"><a v-for="item in allData.typedata"  style="padding: 10px;" v-text='item.name' @click='clickType(item)'></a></span>
        </div>
      </thead>
      </table>
  </div>
    <el-pagination v-if="fetchCachesData.datas || fetchCachesData.header"
      @current-change="changePage"
      :current-page="fetchCachesData.paging.current"
      layout="prev, pager, next, jumper"
      :total="fetchCachesData.paging.total"
      :page-size="fetchCachesData.paging.page_size">
    </el-pagination>
</div>
</page>
</div>
</template>

<script>
import Page from '../../components/Page'
import { mapActions } from 'vuex'
export default {
  data () {
    return {
      typeSelectShow: false,
      ajaxData: {
        etlBusinessTypeId: '',
        orgId: '',
        keyword: '',
        current: null
      },
      fetchCachesData: [],
      allData: {
        typedata: [],
        orgselect: []
      }
    }
  },
  components: {
    'page': Page
  },
  mounted () {
    this.loadData()
  },
  computed: {
    // ...mapGetters([
    //   'fetchDatasType',
    //   'fetchOrgs',
    //   'fetchCachesData'
    //   // 'allorganizations'
    // ])
  },
  methods: {
    ...mapActions([
      'getCleansData'
      // 'getOrganization'
    ]),
    changeBussinessTypes () {
      if (this.ajaxData.etlBusinessTypeId === '') {
        this.ajaxData.orgId = ''
        this.ajaxData.keyword = ''
        this.typeSelectShow = false
      }
      this.ajaxData.current = 1
      this.loadData()
    },
    clickType (item) {
      this.typeSelectShow = true
      this.ajaxData.etlBusinessTypeId = item.etlBusinessTypeId
      this.ajaxData.current = 1
      this.loadData()
    },
    changeSearchs () {
      if (event.keyCode === 13) {
        this.searchContent()
      }
    },
    changePage (val) {
      this.ajaxData.current = val
      this.loadData()
    },
    searchContent () {
      this.ajaxData.current = 1
      this.loadData()
    },
    loadData () {
      this.getCleansData(this.ajaxData).then((res) => {
        this.allData = {
          typedata: res.bussinessTypes,
          orgselect: res.orgs
        }
        this.fetchCachesData = res
      })
    }
  }
}
</script>
<style >
  .typeClass {
  text-decoration: underline;
}
</style>
