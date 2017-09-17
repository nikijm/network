<template>
  <div>
    <page title="添加角色">
      <div slot="content">
        <el-form :model="roleForm" :rules="rules" ref="roleForm" label-width="100px">
          <el-row>
            <el-col :span="6">
              <el-form-item label="角色名称" prop="name">
                <el-input v-model="roleForm.name"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <table class="table ">
          <tr>
            <th>资源名称</th>
            <th>创建</th>
            <th>获取</th>
            <th>修改</th>
            <th>删除</th>
          </tr>
          <tr v-for="item in oneRoleListData">
            <td>{{ item.name }}</td>
            <td><el-checkbox v-model="item.checkGroup[0]" value="c" :disabled="!inArray('c', item.available_operations) || !item.checkGroup[1]"></el-checkbox></td>
            <td><el-checkbox v-model="item.checkGroup[1]" value="r" :disabled="!inArray('r', item.available_operations)" @change="handleObtain(item)"></el-checkbox></td>
            <td><el-checkbox v-model="item.checkGroup[2]" value="u" :disabled="!inArray('u', item.available_operations) || !item.checkGroup[1]"></el-checkbox></td>
            <td><el-checkbox v-model="item.checkGroup[3]" value="d" :disabled="!inArray('d', item.available_operations) || !item.checkGroup[1]"></el-checkbox></td>
          </tr>
        </table>
      </div>
      <div slot="footer">
        <div class="text-algin-c">
          <button class="el-button el-button--primary" @click="addRole('roleForm')">添加</button>
          <button class="el-button el-button--primary" @click="closeAddRole()">返回</button>
        </div>
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
  // import { statusCallback } from '../../../lib/utils'
  export default {
    data () {
      return {
        addRolePageLoad: false,
        oneRoleListData: null,
        roleForm: {
          name: ''
        },
        rules: {
          name: [
            { required: true, message: '请输入角色名称', trigger: 'blur' },
            { min: 4, max: 12, message: '长度在 4 到 12 个字符', trigger: 'blur' }
          ]
        }
      }
    },
    components: {
      'va-table': VATable,
      'page': Page
    },
    computed: {
      ...mapGetters([
        'allResourcesList',
        'allRoleList'
      ])
    },
    mounted () {
      // 获取所有资源的基础数据
      this.getAllResourcesList().then((res) => {
        this.oneRoleListData = this.allResourcesList ? JSON.parse(this.allResourcesList) : []
        this.statusCallback(res)
      })
    },
    methods: {
      ...mapActions([
        'getAllResourcesList',
        'addOneRole'
      ]),
      /**
       * 新增单个角色
       */
      addRole (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            // 新增
            this.addRolePageLoad = true
            this.addOneRole({ name: this.roleForm.name, data: this.oneRoleListData }).then((res) => {
              this.addRolePageLoad = false
              this.statusCallback(res, function (that) {
                // that.currentPage = 1
                // that.getAllRoleData()
                if (!that.addRolePageLoad) {
                  that.$message({
                    message: '添加成功',
                    type: 'success'
                  })
                  that.closeAddRole()
                }
              })
            })
          } else {
            // 验证未通过
            this.$message({
              message: '不符合规则，请重新添加',
              type: 'error'
            })
            return false
          }
        })
      },
      /**
       * 关闭新增角色对话框
       */
      closeAddRole () {
        this.$router.push({ path: '/authList' })
      },
      /**
       * 处理获取的逻辑
       */
      handleObtain (item) {
        // 记录原始的资源
        let state = item.checkGroup[1]
        if (!state) {
          // 获取不勾选，他们的资源都不勾选
          item.checkGroup = [false, false, false, false]
          item.available_operations = ['r']
        } else {
          // 不勾选就恢复原来的可修改状态
          item.available_operations = item.original_operations
        }
      },
      /**
       * 重置数据
       */
      resetData (data) {
        if (!data) {
          return
        }
        for (var i = 0; i < data.length; i++) {
          data[i].checkGroup = [false, false, false, false]
        }
      },
      /**
       * 判断是否为数组
       */
      isArray (v) {
        return v instanceof Array || v.constructor === Array || Object.prototype.toString.call(v) === '[object Array]'
      },
      /**
       * 判断元素是否在数组中
       */
      inArray (needle, haystack) {
        if (!this.isArray(haystack)) {
          throw Error('the second argument is not a Array')
        }
        let len = haystack.length
        for (let i = 0; i < len; i++) {
          if (haystack[i] === needle) return true
        }
        return false
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
