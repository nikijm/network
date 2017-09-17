<template>
  <page title="测试" :isLoading="isLoading">
    <router-link tag="a" class="el-button el-button--primary" to="/modal" slot="tools">
      添加文章
    </router-link>
    <div slot="content">
      <va-table>
        <tr>
          <th>标题</th>
          <th>作者</th>
          <th>地区</th>
          <th>置顶</th>
          <th>置顶</th>
          <th>置顶</th>
          <th width="20%">操作</th>
        </tr>
        <tr>
          <td>test</td>
          <td>test</td>
          <td>test</td>
          <td>test</td>
          <td>test</td>
          <td>test</td>
          <td>
            <router-link tag="a" class="btn btn-xs btn-default" to="/modal">
              <i class="fa fa-comment"></i>
              发消息
            </router-link>
          </td>
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
  </page>
</template>

<script>
import Page from '../../components/Page.vue'
import VATable from '../../components/VATable.vue'
export default {
  data () {
    return {
      isLoading: true,
      currentPage: 1,
      pageSize: 100,
      total: 1000
    }
  },
  components: {
    'page': Page,
    'va-table': VATable
  },
  methods: {
    load () {
      return new Promise((resolve, reject) => {
        setTimeout(reject, 1000)
      })
    },
    handleCurrentChange (val) {
      console.log(`当前页: ${val}`)
    }
  },
  created () {
    this.load().catch(this.$promptReload)
  }
}
</script>
