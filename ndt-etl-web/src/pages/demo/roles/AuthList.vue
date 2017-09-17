<template>
  <div>
    <page title="角色列表" :isLoading="rolePageLoad">
      <div slot="tools">
        <button class="el-button el-button--primary" @click="openResourcesDialog()">新增角色</button>
      </div>
      <div slot="content">
        <va-table class="role-table">
          <tr>
            <th>序号</th>
            <th>角色名称</th>
            <th>操作</th>
          </tr>
          <tr v-for="(item, index) in allRoleList" v-if="allRoleList">
            <td>{{ (currentPage - 1) * pageSize + index + 1 }}</td>
            <td>{{ item.name }}</td>
            <td>
              <a href="javascript:;" @click="openResourcesDialog(item)">修改</a>
              &nbsp;&nbsp;&nbsp;&nbsp;
              <a href="javascript:;" @click="deleteRole(item)">删除</a>
            </td>
          </tr>
          <tr v-if="!allRoleList">
            <td colspan="2">暂时没有数据！</td>
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
  </div>
</template>
<style scoped>
  .role-table th, .role-table td {
    text-align: center;
  }
</style>
<script>
  import { mapGetters, mapActions } from 'vuex'
  import Page from '../../../components/Page.vue'
  import VATable from '../../../components/VATable.vue'
  export default {
    data () {
      return {
        rolePageLoad: true,
        currentPage: 1,
        pageSize: 1,
        total: 1
      }
    },
    components: {
      'va-table': VATable,
      'page': Page
    },
    computed: {
      ...mapGetters([
        'allRoleList',
        'allResourcesList'
      ])
    },
    mounted () {
      this.getAllRoleData()
      this.getAllResourcesList().then((res) => {
        this.allResourcesListData = this.allResourcesList ? JSON.parse(this.allResourcesList) : []
        this.statusCallback(res)
      })
    },
    methods: {
      ...mapActions([
        'getAllRoleList',
        'getAllResourcesList',
        'deleteOneRole'
      ]),
      /**
       * 获取所有角色的数据
       */
      getAllRoleData () {
        // page_size: 4
        this.getAllRoleList({current: this.currentPage, page_size: 4}).then((res) => {
          this.statusCallback(res, function (that) {
            if (res && res.data) {
              that.total = res.data.paging.total
              that.pageSize = res.data.paging.page_size
            }
            that.rolePageLoad = false
          })
        })
      },
      /**
       * 分页跳转
       */
      handleCurrentChange (val) {
        this.currentPage = val
        this.getAllRoleData()
      },
      /**
       * 打开资源管理页面
       */
      openResourcesDialog (item) {
        if (!item) {
          // 这儿是新增一个角色
          this.$router.push({ path: '/addAuth' })
        } else {
          // 这儿是修改一个角色
          let authInfo = {
            item: item,
            allItems: this.allResourcesListData
          }
          sessionStorage.setItem('authInfo', JSON.stringify(authInfo))
          this.$router.push({ path: '/editAuth/' + item.id })
        }
      },
      /**
       * 删除一个角色
       */
      deleteRole (data) {
        let _this = this
        this.$confirm('此操作将删除该角色, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let param = {
            id: data.id
          }
          _this.deleteOneRole(param).then((res) => {
            // 删除后再次获取所有角色列表
            _this.statusCallback(res, function (that) {
              that.getAllRoleData()
              that.$message({
                type: 'success',
                message: '删除成功!'
              })
            })
          })
        }).catch(() => {
          // 取消操作，不做任何响应操作
        })
      },
      /**
       * 请求返回的结果的处理
       */
      statusCallback (res, fn) {
        if (!res) {
          // 请求的响应没有返回，比如用commit进行的操作
          typeof fn === 'function' && fn(this)
          return
        }
        if (res.status) {
          switch (res.status) {
            case 500:
              this.$message({
                message: '服务器或网络错误',
                type: 'error'
              })
              break
            case 404:
              this.$message({
                message: '资源未找到',
                type: 'error'
              })
              break
            case 200:
              typeof fn === 'function' && fn(this)
              break
            default:
              // 其他错误
              if (res.data) {
                let err = res.data.message
                if (err) {
                  this.$message({
                    message: err,
                    type: 'error'
                  })
                } else {
                  this.$message({
                    message: '未知错误',
                    type: 'error'
                  })
                }
              } else {
                this.$message({
                  message: '未知错误',
                  type: 'error'
                })
              }
          }
        } else {
          if (res.data) {
            let err = res.data.message
            if (err) {
              this.$message({
                message: err,
                type: 'error'
              })
            } else {
              this.$message({
                message: '未知错误',
                type: 'error'
              })
            }
          } else {
            this.$message({
              message: '未知错误',
              type: 'error'
            })
          }
        }
      }
    }
  }
</script>
