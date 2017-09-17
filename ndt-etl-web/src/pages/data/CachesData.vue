<template>
<page title="数据信息列表">
<div slot='tools'>
<el-button @click='backtoAllsheets'>返回</el-button>
</div>
<div slot="content">
<div style="width:100%;height: 100%;overflow: auto">

  <!-- <el-table :data="allDatas" border  resizable style="width: 100%;" :height='screenHeight-200'> 
    <el-table-column :show-overflow-tooltip=true 
    v-for="(value, key) in header"
    :prop= "key"
    :label= "value"
    >
  </el-table-column>
  <el-table-column v-show='noData<=0'>
    暂无数据
  </el-table-column>
</el-table>  -->
<table style="width: auto;min-width: 100%;" class="table table-hover table-nowrap ">
  <thead>
    <tr >
      <th v-for='(value, key) in header'>{{value}}</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for='item in allDatas'>
      <td v-for='(value, key) in header'>{{item[key]}}</td>
    </tr>
    <tr style='text-align: center;' v-show='noData<=0'>暂无数据</tr>
  </tbody>
</table>
</div>
<el-pagination  v-show='noData>0'
      @current-change="changePage"
      :current-page="paging.current"
      layout="prev, pager, next, jumper"
      :total="paging.total"
      :page-size="paging.page_size">
    </el-pagination>
</div>
</page>
</template>
<script>
import Page from '../../components/Page'
import { mapActions } from 'vuex'
export default {
  data () {
    return {
      allDatas: [],
      noData: 1,
      paging: {},
      header: {},
      ajaxData: {
        current: 1,
        id: ''
      },
      screenHeight: document.body.clientHeight
    }
  },
  components: {
    'page': Page
  },
  mounted () {
//  console.log(location.hash.split('=')[1])
//  根据当前url绑定的id值来进行页面的处理
    this.loadData()
  },
  methods: {
    ...mapActions([
      'getallseehtsCachesData'
    ]),
    changePage (val) {
      this.ajaxData.current = val
      this.loadData()
    },
    backtoAllsheets () {
      this.$router.back()
    },
    loadData () {
      if (location.hash.split('=')[1]) {
        this.ajaxData.id = location.hash.split('=')[1]
        this.getallseehtsCachesData(this.ajaxData).then((res) => {
          this.allDatas = res.data.cacheDatas
          this.paging = res.data.paging
          this.header = res.data.header
          if (res.data.cacheDatas) {
            this.noData = res.data.cacheDatas.length
          }
        })
      }
    }
  }
}
</script>
