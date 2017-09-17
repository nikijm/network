<template>
  <page title="测试">
    <div slot="tools">
      <router-link tag="a" class="el-button el-button--primary" to="/modal">
        添加文章
      </router-link>
    </div>
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
        <tr v-for="item in tableData">
          <td>{{item.title}}</td>
          <td>{{item.author}}</td>
          <td>{{item.area}}</td>
          <td>{{item.add}}</td>
          <td>{{item.update}}</td>
          <td>{{item.action}}</td>
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
      currentPage: 1,
      pageSize: 100,
      total: 1000,
      tableData: [{
        title: 'test',
        author: 'test',
        area: 'test',
        add: 'test',
        update: 'test',
        action: 'test'
      }]
    }
  },
  components: {
    'page': Page,
    'va-table': VATable
  },
  methods: {
    handleCurrentChange (val) {
      console.log(`当前页: ${val}`)
    }
  }
}
</script>
