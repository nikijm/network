<template>
  <Page title="报表">
    <div slot="tools">
      
      <el-row>
        <el-col :span="8">
          <el-date-picker
            v-model="date1"
            type="month"
            placeholder="选择开始月份"
            style="width: 100%;">
          </el-date-picker>
        </el-col>
        <el-col class="line text-algin-c" :span="4" style="line-height:36px;">-</el-col>
        <el-col :span="8">
          <el-date-picker
            v-model="date2"
            type="month"
            placeholder="选择结束月份"
            style="width: 100%;">
          </el-date-picker>
        </el-col>
        <el-col :span="4">
          <button type="button" class="el-button el-button--primary ml20" @click="statistics">统计</button>
        </el-col>
      </el-row>
    </div>
    <div slot="content">
      <va-table>
        <tr>
          <th>月份</th>
          <th>上传文件</th>
          <th>数据表</th>
          <th>清洗完成</th>
          <th>转换完成</th>
          <th>检验完成</th>
          <th>完成比例</th>
          <th>处理数据条数</th>
        </tr>
        <tr >
          <td>3月</td>
          <td>35</td>
          <td>22</td>
          <td>55</td>
          <td>44</td>
          <td>83</td>
          <td>60%</td>
          <td>2333</td>
        </tr>
        <tr v-for="item in items">
          <td v-text="item.yf + '月'"></td>
          <td v-text="item.scjj"></td>
          <td v-text="item.sjb"></td>
          <td v-text="item.qxwc"></td>
          <td v-text="item.zgwc"></td>
          <td v-text="item.jywc"></td>
          <td v-text="item.wcbl + '%'"></td>
          <td v-text="item.clsjts"></td>
        </tr>
      </va-table>
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="prev, pager, next"
        :total="total">
      </el-pagination>
    </div>
  </Page>
</template>
 
<script>
import Page from '../../components/Page.vue'
import VATable from '../../components/VATable.vue'

export default {
  data () {
    return {
      currentPage: 1,
      pageSize: 100,
      total: 1000,
      items: [
        {
          'yf': 1,
          'scjj': 20,
          'sjb': 35,
          'qxwc': 66,
          'zgwc': 77,
          'jywc': 50,
          'wcbl': 80,
          'clsjts': 233
        }
      ],
      date1: '',
      date2: ''
    }
  },
  components: {
    'Page': Page,
    'va-table': VATable
  },
  methods: {
    statistics: function () {
      if (this.date1 === '') {
        this.$message({
          message: '请选择统计开始时间',
          type: 'warning'
        })
        return
      }
      if (this.date2 === '') {
        this.$message({
          message: '请选择统计结束时间',
          type: 'warning'
        })
        return
      }
    },
    handleCurrentChange (val) {
    }
  }
}
</script>
<style scoped>
  .ml20{
    margin-left: 20px;
  }
</style>
