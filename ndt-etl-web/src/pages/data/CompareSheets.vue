<template>
  <Page title="待校验数据">
    <div slot="tools">
      <el-input v-model="message" placeholder="请输入文件名或用户名" class="tools-button" @keyup.enter.native="search">
        <el-button slot="append" icon="search" @click="search"></el-button>
      </el-input>
    </div>
    <div slot="content">
      <table :v-loading.body="true" class="table table-hover table-fixed">
      <thead>
        <tr>
          <th>文件名</th>
          <th>编号</th>
          <th>上传用户</th>
          <th>上传时间</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="item in fetchCompareData.sheets">
          <td v-text="item.upload.fileName"></td>
          <td v-text="item.upload.id"></td>
          <td v-text="item.upload.user.name"></td>
          <td v-text="item.upload.uploadDate"></td>
          <td v-text="item.status"></td>
          <td>
            <router-link tag="a" class="btn btn-xs btn-default" :to="{name: 'sheet',params:{id:item.id}}">
              查看
            </router-link>
            <router-link tag="a" class="btn btn-success btn-xs" to="">
              校验
            </router-link>
          </td>
        </tr>
       </tbody>
      </table>
      <el-pagination
        @current-change="toPage"
        :current-page="fetchCompareData.paging.current"
        :page-size="fetchCompareData.paging.page_size"
        layout="prev, pager, next"
        :total="fetchCompareData.paging.total">
      </el-pagination>
    </div>
  </Page>
</template>

<script>
import Page from '../../components/Page.vue'
import VATable from '../../components/VATable.vue'
import { mapGetters, mapActions } from 'vuex'

export default {
  data () {
    return {
      message: '',
      isLoading: true,
      payload: {
        page: 1,
        search: ''
      }
    }
  },
  components: {
    'Page': Page,
    'va-table': VATable
  },
  created () {
    this.compareLoad()
  },
  computed: {
    ...mapGetters([
      'fetchCompareData'
    ])
  },
  methods: {
    ...mapActions([
      'FETCH_LISTCOMPARE'
    ]),
    compareLoad () {
      this.isLoading = true
      this.FETCH_LISTCOMPARE(this.payload)
      .then(() => {
        this.isLoading = false
      })
      .catch(this.$promptReload)
    },
    toPage (val) {
      this.payload.page = val
      this.compareLoad()
    },
    search () {
      this.payload.page = 1
      this.payload.search = this.message
      this.compareLoad()
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
