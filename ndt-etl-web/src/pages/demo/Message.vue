<!--message-->
<template>
  <page title="提示信息" :isLoading="false">
    <div slot="content">
      <el-button type="primary" @click="openDialog">点击打开 Dialog</el-button>
      <el-button type="primary" @click="openMessage">点击消息弹框</el-button>
      <el-button type="primary" @click="openTip">点击打开消息提示</el-button>
      <!--dialog-->
      <el-dialog title="收货地址" :visible.sync="dialogTableVisible">
        <el-table :data="gridData">
          <el-table-column property="date" label="日期" width="150"></el-table-column>
          <el-table-column property="name" label="姓名" width="200"></el-table-column>
          <el-table-column property="address" label="地址"></el-table-column>
        </el-table>
      </el-dialog>
      <p class="pd20">
        <p>参考</p>
        <p>【dialog】http://element.eleme.io/#/zh-CN/component/dialog</p>
        <p>【消息提示】http://element.eleme.io/#/zh-CN/component/message</p>
        <p>【消息弹框】http://element.eleme.io/#/zh-CN/component/message-box</p>
      </div>
    </div>
  </page>
</template>

<script>
import Page from '../../components/Page.vue'
export default {
  data () {
    return {
      gridData: [{
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-03',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }],
      dialogTableVisible: false,
      dialogFormVisible: false,
      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      },
      formLabelWidth: '120px'
    }
  },
  components: {
    'page': Page
  },
  methods: {
    openMessage () {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    openDialog () {
      this.dialogTableVisible = true
    },
    openTip () {
      this.$message({
        type: 'error',
        message: '已取消删除'
      })
    }
  }
}
</script>
