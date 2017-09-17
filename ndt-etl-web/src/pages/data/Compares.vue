<template>
  <div>
    <page title="数据比对">
    <div slot='tools'>
      <el-button @click='backtoAllsheets'>返回</el-button>
      </div>
      <div slot="content" >
      <div style="width:100%;display: flex;justify-content: space-between;">
        <div style="width: 48%;" >
        <p style="font-weight: 700;font-size: 15px">{{title.titleLeft}}</p>
         <div id="div1" style="overflow-y:hidden;margin-bottom: 10px" onscroll="document.getElementById('div2').scrollLeft = this.scrollLeft;">
          <div style="height: 400px">
            <table style="width:auto;height: 400px" class="table table-hover table-nowrap">
              <thead>
                <tr>
                  <th v-for='(value, key) in compareLeft'>{{value}}</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for='item in compareList'>
                  <td v-for="(value,key) in compareLeft" v-show='item[key]'>{{item[key]}}</td>
                </tr>
                <!-- <tr style='text-align: center;'>暂无数据</tr> -->
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <div style="width: 50%">
        <p style="font-weight: 700;font-size: 15px">{{title.titleRight}}</p>
        <div id="div2" style="overflow-x:hidden;margin-bottom: 10px"  onscroll="document.getElementById('div1').scrollLeft = this.scrollLeft;document.getElementById('div1').scrollTop = this.scrollTop;">
          <div style="height: 400px">
            <table style="width:auto;height: 400px" class="table table-hover table-nowrap">
              <thead>
                <tr>
                  <th v-for='(value, key) in compareRight'>{{value}}</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for='item in compareList'>
                  <td v-for="(value,key) in compareRight" v-if='item[key]'>{{item[key]}}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      </div>
      <!-- <div> -->
      <el-pagination 
      @current-change="changePage"
      :current-page="paging.current"
      layout="prev, pager, next, jumper"
      :total="paging.total"
      :page-size="paging.page_size">
    </el-pagination>
    <!-- </div> -->
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
      ajaxData: {
        Params: {},
        current: null
      },
      title: {
        titleLeft: '',
        titleRight: ''
      },
      paging: {},
      compareLeft: {},
      compareRight: {},
      compareList: [],
      type: '',
      noData: 1
    }
  },
  methods: {
    ...mapActions([
      'getComparesData'
    ]),
    changePage (val) {
      this.ajaxData.current = val
      this.loadData()
    },
    backtoAllsheets () {
      this.$router.back()
    },
    loadData () {
      this.getComparesData(this.ajaxData).then((res) => {
        this.paging = res.data.paging
        this.compareList = res.data.compareList
        var compareRight = JSON.parse(JSON.stringify(res.data.header))
        var compareLeft = JSON.parse(JSON.stringify(res.data.header))
        for (var key in compareRight) {
          if (/^S.*C/.test(key)) {
            delete compareRight[key]
          }
        }
        for (var keyS in compareLeft) {
          if (/^T.*T/.test(keyS)) {
            delete compareLeft[keyS]
          }
        }
        this.compareLeft = compareLeft
        this.compareRight = compareRight
        if (res.data.compareList) {
          this.noData = res.data.compareList.length
        }
      })
    }
  },
  mounted () {
    // let oid = this.$route.params
    // sessionStorage.setItem('typeData', JSON.stringify(oid))
    // console.log('oid', oid)
    this.ajaxData.Params = JSON.parse(sessionStorage.getItem('typeData'))
    switch (this.ajaxData.Params.category) {
      case 'clean':
        this.title = {
          titleLeft: '缓存后数据',
          titleRight: '清洗后数据'
        }
        break
      case 'convert':
        this.title = {
          titleLeft: '清洗后数据',
          titleRight: '转换后数据'
        }
        break
      case 'verify':
        this.title = {
          titleLeft: '转换后数据',
          titleRight: '校验后数据'
        }
        break
    }
    this.loadData()
  },
  components: {
    'page': Page
  }
}
</script>
