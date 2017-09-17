<!--Pagination 分页-->
<template>
  <page title="分页" :isLoading="false">
    <div slot="content">
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="prev, pager, next"
        :total="total">
      </el-pagination>
      <p class="pd20">参考http://element.eleme.io/#/zh-CN/component/pagination</p>
    </div>
  </page>
</template>

<script>
import Page from '../../components/Page.vue'
export default {
  data () {
    return {
      currentPage: 1,
      pageSize: 100,
      total: 1000
    }
  },
  components: {
    'page': Page
  },
  methods: {
    handleCurrentChange (val) {
      console.log(`当前页: ${val}`)
    }
  }
}
</script>
