<template>
  <div>
    <page title="修改角色">
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
        <va-table class="role-table">
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
        </va-table>
      </div>
      <div slot="footer">
        <div class="text-algin-c">
          <button class="el-button el-button--primary" @click="updateRole(oneRoleListData)">更改</button>
          <button class="el-button el-button--primary" @click="closeRole()">返回</button>
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
  export default {
    data () {
      return {
        updateResPageload: false,
        showUpdateRolePage: false,
        allResourcesListData: null,
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
      this.openUpdateOneRole()
    },
    methods: {
      ...mapActions([
        'getAllResourcesList',
        'getAllRoleList',
        'updateOneRoleList',
        'deleteOneRole',
        'addOneRole',
        'updateOneRoleName'
      ]),
      /**
       * 打开修改单个角色的资源表
       */
      openUpdateOneRole () {
        let authInfo = sessionStorage.getItem('authInfo')
        if (authInfo) {
          let role = JSON.parse(authInfo)
          this.roleForm.name = role.item.name
          this.id = role.item.id
          // 匹配数据逻辑为：allResourcesListData是基础数据(resources)，item才包括正真的权限，item来自于allRoleList(roles)
          this.oneRoleListData = this.converManagerRole(role.allItems, role.item)
        }
      },
      /**
       * 数据转化处理
       */
      converManagerRole (need, handle) {
        if (!need) {
          return need
        }
        for (var i = 0; i < need.length; i++) {
          var sourceItem = null
          var sourceArr = []
          for (var j = 0; j < handle.permissions.length; j++) {
            if (need[i].key === handle.permissions[j].key) {
              sourceItem = handle.permissions[j]
              sourceArr = sourceItem.available_operations.split(' ')
              break
            }
          }
          // 做勾选
          for (var k = 0; k < need[i].available_operations.length; k++) {
            for (var h = 0; h < sourceArr.length; h++) {
              if (need[i].available_operations[k] === sourceArr[h]) {
                switch (need[i].available_operations[k]) {
                  case 'c':
                    need[i].checkGroup[0] = true
                    break
                  case 'r':
                    need[i].checkGroup[1] = true
                    break
                  case 'u':
                    need[i].checkGroup[2] = true
                    break
                  case 'd':
                    need[i].checkGroup[3] = true
                    break
                }
              }
            }
          }
        }
        return need
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
       * 修改一个角色的资源表
       */
      updateRole (data) {
        this.updateResPageload = true
        // 对数据进行处理成后台需要的格式
        this.updateOneRoleList({id: this.id, name: this.roleForm.name, data: this.oneRoleListData}).then((res) => {
          this.updateResPageload = false
          // 更新成功后再次获取所有角色列表
          this.statusCallback(res, function (that) {
            // 关闭对话框
            if (!that.updateResPageload) {
              that.closeRole(data)
              that.$message({
                message: '更新成功',
                type: 'success'
              })
            }
          })
        })
      },
      /**
       * 关闭资源表，返回角色列表
       */
      closeRole () {
        this.$router.push({ path: '/authList' })
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
