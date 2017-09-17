<template>
  <div>
    <page title="数据缓存">
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
      </div>
<div slot="content">
  <div style="width:100%;height:100%;overflow: auto;"  >
<el-table :data="fetchCachesData.datas" border  resizable style="width: 100%;" v-if="fetchCachesData.datas || fetchCachesData.header" :height='screenHeight-200'> 
  <el-table-column fixed :show-overflow-tooltip=true label="清洗结果" ></el-table-column>
  <el-table-column fixed  :show-overflow-tooltip=true label="原因"></el-table-column>
  <el-table-column fixed :show-overflow-tooltip=true label="操作"></el-table-column>
  <el-table-column :show-overflow-tooltip=true v-if="index===0||index===1"
    v-for="(value, key, index) in fetchCachesData.header"
    :prop= "key"
    :label= "value" fixed
    width="200">
  </el-table-column>
    <el-table-column :show-overflow-tooltip=true v-if="index>1"
    v-for="(value, key, index) in fetchCachesData.header"
    :prop= "key"
    :label= "value"
   >
  </el-table-column>
</el-table> 
   <!--  <table  style="width: auto;min-width: 100%;" class="table table-hover table-nowrap " v-if="fetchCachesData.datas || fetchCachesData.header">
      <thead >
        <tr >
          <th>清洗结果</th>
          <th>原因</th>
          <th>操作</th>
          <th  v-for="(value, key) in fetchCachesData.header" >{{value}}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in fetchCachesData.datas" class="trFixed">
          <td>{{item.RESULT}}1</td>
          <td>{{item.REASON}}2</td>
          <td>3</td>
          <td v-for="(value, key) in fetchCachesData.header">{{item[key]}}</td>
        </tr>
        <tr style='text-align: center;' v-show='noData<=0'>暂无数据</tr>
      </tbody>
    </table> -->
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
    <el-pagination v-if="fetchCachesData.datas || fetchCachesData.header" v-show='noData>0'
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
      },
      values: [],
      newData: {},
      noData: 1,
      screenHeight: document.body.clientHeight
    }
  },
  components: {
    'page': Page
  },
  mounted () {
    this.loadData()
    this.$refs.scrollContent.style.height = this.screenHeight - 200 + 'px'
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
      'getCachesData'
    ]),
    clickType (item) {
      this.typeSelectShow = true
      this.ajaxData.etlBusinessTypeId = item.etlBusinessTypeId
      this.ajaxData.current = 1
      this.loadData()
    },
    changeBussinessTypes () {
      if (this.ajaxData.etlBusinessTypeId === '') {
        this.ajaxData.orgId = ''
        this.ajaxData.keyword = ''
        this.typeSelectShow = false
      }
      this.ajaxData.current = 1
      this.loadData()
    },
    /* changeSearchs () {
      if (event.keyCode === 13) {
        this.searchContent()
      }
    }, */
    changePage (val) {
      this.ajaxData.current = val
      this.loadData()
    },
    searchContent () {
      this.ajaxData.current = 1
      this.loadData()
    },
    loadData () {
      this.getCachesData(this.ajaxData).then((res) => {
        if (res.datas) {
          this.noData = res.datas.length
        }
        // sessionStorage.setItem('typeData', JSON.stringify(res))
        // this.$store.commit('FETCH_DATA', res.bussinessTypes)
        // this.$store.commit('FETCH_ORG', res.orgs)
        this.allData = {
          typedata: res.bussinessTypes,
          orgselect: res.orgs
        }
        this.fetchCachesData = res
        // if (res.datas) {
        //   this.values = []
        //   this.fetchCachesData.datas.map(item => {
        //     var newData = JSON.parse(JSON.stringify(res.header))
        //     for (var key in newData) {
        //       for (var keyd in item) {
        //         if (key === keyd) {
        //           newData[key] = item[keyd]
        //           console.log('bb', newData)
        //         }
        //       }
        //     }
        //     this.values.push(newData)
        //   })
        // }
      })
    }
  }
}
</script>
<style>
.typeClass {
  text-decoration: underline;
}
.el-table th>.cell{
  white-space: nowrap;
}
.el-table td>.cell{
  white-space: nowrap;
}
</style>
