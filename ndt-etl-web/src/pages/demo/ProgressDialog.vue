<!--message-->
<template>
  <page title="操作进度对话框" :isLoading="false">
    <div slot="content">
      <el-button type="primary" @click="successDialog">操作成功演示</el-button>

      <el-button type="danger" @click="errorDialog">操作出错演示</el-button>

      <progress-dialog :visible.sync="dialogVisible" :isSuccess="isSuccess" :isError="isError" SuccessText="恭喜你，操作成功" errorText="很可惜，操作失败">
        <h2 slot="error">操作失败真可惜</h2>
        <h2 slot="success">操作成功很恭喜</h2>
      </progress-dialog>
      </div>
    </div>
  </page>
</template>

<script>
import Page from '../../components/Page.vue'
import ProgressDialog from '../../components/ProgressDialog.vue'
export default {
  data () {
    return {
      dialogVisible: false,
      isSuccess: false,
      isError: false
    }
  },
  components: {
    'page': Page,
    'progress-dialog': ProgressDialog
  },
  methods: {
    successDialog () {
      this.isSuccess = false
      this.isError = false
      this.dialogVisible = true
      setTimeout(() => { this.isSuccess = true }, 2000)
    },
    errorDialog () {
      this.isSuccess = false
      this.isError = false
      this.dialogVisible = true
      setTimeout(() => { this.isError = true }, 2000)
    }
  }
}
</script>
