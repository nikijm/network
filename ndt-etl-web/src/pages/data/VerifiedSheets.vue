<template>
  <div>
    <page title="数据比对">
    <div slot="tools">
       <el-select v-model="type" placeholder="选择对比表">
        <el-option
          v-for="(item, index) in typedata" :label="item.name"
          :value="index">
        </el-option>
      </el-select>
    </div>
      <div slot="content" style="width:100%;display: flex;justify-content: space-between;">
        <div style="width: 48%;" >
        <p style="font-weight: 700;font-size: 15px">原始数据</p>
         <div id="div1" style="overflow-y:hidden;" onscroll="document.getElementById('div2').scrollLeft = this.scrollLeft;">
          <div style="height: 400px">
         <!--  <el-table 
            :data="tableData"
            border
            style="width: auto"
            max-height="250">
            <el-table-column v-for="item in tableData"
              fixed
              prop=""
              label="日期"
              width="150">
            </el-table-column>
            </el-table> -->
            <table style="width:auto" class="table table-hover table-nowrap">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>申请表编号</th> 
                  <th>单位(个人)</th>
                  <th>法人</th>
                  <th>地址</th>
                  <th>身份证号</th>
                  <th>登记类型</th>
                  <th>林地所有权</th>
                  <th>森林林木所有权</th>
                  <th>林地使用权</th>
                  <th>林地使用权</th>
                  <th>林地使用权</th>
                  <th>林地使用权</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in tableData">
                  <td v-for="(value,key) in item" v-if='tableData[0]' fixed>{{value}}</td>
                  <td v-else v-for="(value,key) in item">{{value}}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <div style="width: 50%">
        <p style="font-weight: 700;font-size: 15px">清洗后数据</p>
        <div id="div2" style="overflow-x:hidden;"  onscroll="document.getElementById('div1').scrollLeft = this.scrollLeft;document.getElementById('div1').scrollTop = this.scrollTop;">
          <div style="height: 400px">
            <table style="width:auto" class="table table-hover table-nowrap">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>申请表编号</th> 
                  <th>单位(个人)</th>
                  <th>法人</th>
                  <th>地址</th>
                  <th>身份证号</th>
                  <th>登记类型</th>
                  <th>林地所有权</th>
                  <th>森林林木所有权</th>
                  <th>林地使用权</th>
                  <th>林地使用权</th>
                  <th>林地使用权</th>
                  <th>林地使用权</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in tableData">
                  <td v-for="(value,key) in item">{{value}}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </page>
</div>
</template>

<script>
import Page from '../../components/Page'
export default {
  data () {
    return {
      type: '',
      typedata:
      [
        { name: '原始-清洗' },
        { name: '原始-转换' },
        { name: '清洗-转换' }
      ],
      tableData: [{
        id: '1',
        number: '05101820208GDYMSY00001',
        danwei: '陈正国',
        people: '陈正国',
        adress: '彭州市白鹿镇白茶村一组',
        idcard: '510126194106122418',
        regtype: '√',
        lindi: '√',
        senlin: '√',
        lindiuse: '√'
      },
      {
        id: '2',
        number: ' 05101820208GDYMSY00002',
        danwei: '陈付生',
        people: '陈正国',
        adress: '彭州市白鹿镇白茶村一组',
        idcard: '510126194405052413',
        regtype: '√',
        lindi: '√',
        senlin: '√',
        lindiuse: '√'
      },
      {
        id: '3',
        number: ' 05101820208GDYMSY00002',
        danwei: '王洲华',
        people: '陈付生',
        adress: '彭州市白鹿镇白茶村一组',
        idcard: '510126194701042412',
        regtype: '√',
        lindi: '√',
        senlin: '√',
        lindiuse: '√'
      },
      {
        id: '4',
        number: '05101820208GDYMSY00001',
        danwei: '赵天顺',
        people: '赵天',
        adress: '彭州市白鹿镇白茶村一组',
        idcard: '510126194106122418',
        regtype: '√',
        lindi: '√',
        senlin: '√',
        lindiuse: '√'
      },
      {
        id: '4',
        number: '05101820208GDYMSY00001',
        danwei: '赵天顺',
        people: '赵天',
        adress: '彭州市白鹿镇白茶村一组',
        idcard: '510126194106122418',
        regtype: '√',
        lindi: '√',
        senlin: '√',
        lindiuse: '√'
      },
      {
        id: '4',
        number: '05101820208GDYMSY00001',
        danwei: '赵天顺',
        people: '赵天',
        adress: '彭州市白鹿镇白茶村一组',
        idcard: '510126194106122418',
        regtype: '√',
        lindi: '√',
        senlin: '√',
        lindiuse: '√'
      },
      {
        id: '4',
        number: '05101820208GDYMSY00001',
        danwei: '赵天顺',
        people: '赵天',
        adress: '彭州市白鹿镇白茶村一组',
        idcard: '510126194106122418',
        regtype: '√',
        lindi: '√',
        senlin: '√',
        lindiuse: '√'
      },
      {
        id: '4',
        number: '05101820208GDYMSY00001',
        danwei: '赵天顺',
        people: '赵天',
        adress: '彭州市白鹿镇白茶村一组',
        idcard: '510126194106122418',
        regtype: '√',
        lindi: '√',
        senlin: '√',
        lindiuse: '√'
      },
      {
        id: '4',
        number: '05101820208GDYMSY00001',
        danwei: '赵天顺',
        people: '赵天',
        adress: '彭州市白鹿镇白茶村一组',
        idcard: '510126194106122418',
        regtype: '√',
        lindi: '√',
        senlin: '√',
        lindiuse: '√'
      },
      {
        id: '4',
        number: '05101820208GDYMSY00001',
        danwei: '赵天顺',
        people: '赵天',
        adress: '彭州市白鹿镇白茶村一组',
        idcard: '510126194106122418',
        regtype: '√',
        lindi: '√',
        senlin: '√',
        lindiuse: '√'
      },
      {
        id: '4',
        number: '05101820208GDYMSY00001',
        danwei: '赵天顺',
        people: '赵天',
        adress: '彭州市白鹿镇白茶村一组',
        idcard: '510126194106122418',
        regtype: '√',
        lindi: '√',
        senlin: '√',
        lindiuse: '√'
      },
      {
        id: '4',
        number: '05101820208GDYMSY00001',
        danwei: '赵天顺',
        people: '赵天',
        adress: '彭州市白鹿镇白茶村一组',
        idcard: '510126194106122418',
        regtype: '√',
        lindi: '√',
        senlin: '√',
        lindiuse: '√'
      },
      {
        id: '4',
        number: '05101820208GDYMSY00001',
        danwei: '赵天顺',
        people: '赵天',
        adress: '彭州市白鹿镇白茶村一组',
        idcard: '510126194106122418',
        regtype: '√',
        lindi: '√',
        senlin: '√',
        lindiuse: '√'
      },
      {
        id: '4',
        number: '05101820208GDYMSY00001',
        danwei: '赵天顺',
        people: '赵天',
        adress: '彭州市白鹿镇白茶村一组',
        idcard: '510126194106122418',
        regtype: '√',
        lindi: '√',
        senlin: '√',
        lindiuse: '√'
      },
      {
        id: '4',
        number: '05101820208GDYMSY00001',
        danwei: '赵天顺',
        people: '赵天',
        adress: '彭州市白鹿镇白茶村一组',
        idcard: '510126194106122418',
        regtype: '√',
        lindi: '√',
        senlin: '√',
        lindiuse: '√'
      },
      {
        id: '4',
        number: '05101820208GDYMSY00001',
        danwei: '赵天顺',
        people: '赵天',
        adress: '彭州市白鹿镇白茶村一组',
        idcard: '510126194106122418',
        regtype: '√',
        lindi: '√',
        senlin: '√',
        lindiuse: '√'
      }]
    }
  },
  methods: {
  },
  components: {
    'page': Page
  }
}
</script>

<!-- <template>
  <Page title="已校验数据" :isLoading="isLoading">
    <div slot="tools">
      <el-input v-model="message" placeholder="请输入文件名或用户名" class="tools-button" @keyup.enter.native="search">
        <el-button slot="append" icon="search" @click="search"></el-button>
      </el-input>
    </div>
    <div slot="content">
      <table class="table table-hover table-fixed">
      <thead>
        <tr>
          <th width="150">编号</th>
          <th >文件名</th>
          <th width="240">上传用户</th>
          <th width="180">上传时间</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item, index) in fetchSheetsData.sheets" :class="index < fetchSheetsData.sheets.length-1 && item.upload.id != fetchSheetsData.sheets[index+1].upload.id ? 'border-bottom': ''">
          <td v-text="item.id"></td>
          <td v-text="item.upload.fileName"></td>
          <upload-user-name :sheet="item.upload" />
          <td v-text="item.upload.uploadDate"></td>
          <td v-text="item.status"></td>
          <td>
            <router-link tag="a" class="btn btn-primary btn-xs" :to="{name: 'sheet',params:{id:item.id}}">
              查看
            </router-link>
          </td>
        </tr>
     </tbody>
      </table>
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="fetchSheetsData.paging.current"
        :page-size="fetchSheetsData.paging.page_size"
        layout="prev, pager, next"
        :total="fetchSheetsData.paging.total">
      </el-pagination>
    </div>
  </Page>
</template>
<script>
import Page from '../../components/Page.vue'
import VATable from '../../components/VATable.vue'
import UploadUserName from '../../components/Upload-user-name.vue'
import { mapGetters, mapActions } from 'vuex'

export default {
  data () {
    return {
      isLoading: true,
      message: '',
      payload: {
        page: 1,
        filter: 'verified',
        search: ''
      }
    }
  },
  created () {
    this.load()
  },
  computed: {
    ...mapGetters([
      'fetchSheetsData'
    ])
  },
  components: {
    'Page': Page,
    'va-table': VATable,
    'upload-user-name': UploadUserName
  },
  methods: {
    ...mapActions([
      'FETCH_SHEETS'
    ]),
    search: function () {
      this.payload.search = this.message
      this.payload.page = 1
      this.load()
    },
    handleCurrentChange (val) {
      this.payload.page = val
      this.load()
    },
    load () {
      this.isLoading = true
      this.FETCH_SHEETS(this.payload)
      .then(() => {
        this.isLoading = false
      })
      .catch(this.$promptReload)
    }
  }
}
</script>
<style scoped>
  .pr20{
    position: relative;
    right: 20px;
  }
</style>
 -->

