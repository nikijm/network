<template>
  <div v-if="allAdmin.users">
    <page title="运维人员">
      <div slot="tools">
       <el-input placeholder="请输入搜索内容" class="tools-button" v-model="selectData.searchKey" @keyup.native='changeSearchs($event)'>
        <el-button slot="append" icon="search" @click="searchContent"></el-button>
      </el-input>
      <button class="btn btn-primary" @click="showAddDialog">添加</button>
    </div>
    <div slot="content">
      <table class="table table-hover table-fixed">
        <thead>
          <tr>
            <th width="5%">序号</th>
            <th width="10%">用户名</th>
            <th width="10%">电话</th>
            <th width="15%">电子邮箱</th>
            <th>用户角色</th>
            <th width="5%">状态</th>
            <th>备注</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item,index) in allAdmin.users">
            <td>{{ (allAdmin.paging.current - 1) * allAdmin.paging.page_size + index + 1 }}</td>
            <td>{{ item.name }}</td>
            <td><p style="margin:0">{{ item.phone }}</p><p style="margin:0">{{ item.phone2 }}</p></td>
            <td>{{ item.email }}</td>
            <td><span v-for="(items, index) in item.roles">{{items.name}}<span v-if="index!=item.roles.length-1">,&nbsp;</span></span></td>
            <td v-if=" item.isActive == 'Y'"><span class="glyphicon glyphicon-ok text-success"></span></td>
            <td v-else><span class="glyphicon glyphicon-remove text-danger"></span></td>
            <td>{{ item.description }}</td>
            <td>
              <button class="btn btn-primary btn-xs" @click="showUpdateDialog(item)">编辑</button>
              <button class="btn btn-danger btn-xs" @click="deleteData(item)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="col-md-12" style='text-align: center;' v-show='noData<=0'>暂无数据</div>
      <el-pagination
      @current-change="changePage"
      :current-page="allAdmin.paging.current"
      layout="prev, pager, next, jumper"
      :total="allAdmin.paging.total"
      :page-size="allAdmin.paging.page_size">
    </el-pagination>
  </div>
</page>
<el-dialog title="修改" :visible.sync="dialogUpdateVisible">
  <el-form ref="updataShowD" :model="updataShowD" :label-width="formLabelWidth" :rules="updataRules">
    <el-row>
      <el-col :span="20">
        <el-form-item label="用户名" prop="name">
          <el-input :disabled="true" v-model="updataShowD.name"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="20">
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="updataShowD.password"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="20">
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="updataShowD.phone"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="20">
        <el-form-item label="手机号2" prop="phone2">
          <el-input v-model="updataShowD.phone2"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="20">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="updataShowD.email" :maxlength="80"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="20">
        <el-form-item label="备注" prop="description">
          <el-input v-model="updataShowD.description" :maxlength="200"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-form-item label="用户角色" prop="roleIds">
      <el-checkbox-group v-model="updataShowD.roleIds">
        <el-checkbox :label="item.id" v-for="item in allAdmin.roles">{{item.name}}</el-checkbox>
      </el-checkbox-group>
    </el-form-item>
    <el-row>
      <el-row>
        <el-col :span="20">
          <el-form-item label="用户状态" prop="isActive">
            <el-radio-group v-model="updataShowD.isActive">
              <el-radio :label='Y'>已启用</el-radio>
              <el-radio :label='N'>未启用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-row>
  </el-form>
  <div slot="footer" class="dialog-footer">
    <button class="btn btn-success btn-main" @click="updateData('updataShowD')">保存修改</button>
    <button class="btn btn-default" @click="updataCancel('updataShowD')">返回</button>
  </div>
</el-dialog>
<el-dialog title="增加" :visible.sync="dialogAddVisible">
  <el-form :model="addShowD" :label-width="formLabelWidth" ref="addShowD" :rules="rules">
    <el-row>
      <el-col :span="20">
        <el-form-item label="用户名" prop="name">
          <el-input v-model="addShowD.name"></el-input>
          <span class="pwdSpan">{{accContent}}</span>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="20">
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="addShowD.password"></el-input>
          <span class="pwdSpan">{{pwdcontent}}</span>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="20">
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="addShowD.phone"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="20">
        <el-form-item label="手机号2" prop="phone2">
          <el-input v-model="addShowD.phone2"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="20">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="addShowD.email" :maxlength="80"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="20">
        <el-form-item label="备注" prop="description">
          <el-input v-model="addShowD.description" :maxlength="200"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-form-item label="用户角色" prop="roleIds">
      <el-checkbox-group v-model="addShowD.roleIds">
        <el-checkbox :label="item.id" v-for="item in allAdmin.roles">{{item.name}}</el-checkbox>
      </el-checkbox-group>
    </el-form-item>
    <el-row>
      <el-col :span="20">
        <el-form-item label="用户状态" prop="isActive">
          <el-radio-group v-model="addShowD.isActive">
            <el-radio :label='Y'>已启用</el-radio>
            <el-radio :label='N'>未启用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
  <div slot="footer" class="dialog-footer">
    <button class="btn btn-success btn-main" @click="addData('addShowD')">确认添加</button>
    <button class="btn btn-default" @click="addCancel('addShowD')">返回</button>
  </div>
</el-dialog>
</div>
</template>

<script>
  import VATable from '../../components/VATable.vue'
  import Page from '../../components/Page.vue'
  import { statusCallback } from '../../lib/utils'
  import { mapGetters, mapActions } from 'vuex'
  import { USERNAME_PATTERN, PASSWORD_PATTERN } from '../../lib/global'
  export default {
    data () {
      let validatePassword = (rule, value, callback) => {
        if (PASSWORD_PATTERN.test(value)) {
          // this.pwdcontent = ''
          callback()
        } else {
          // this.pwdcontent = '密码格式错误,请重新输入'
          callback(new Error(''))
        }
      }
      let validateName = (rule, value, callback) => {
        if (USERNAME_PATTERN.test(value)) {
          // this.accContent = ''
          callback()
        } else {
          // this.accContent = '用户名格式错误,请重新输入'
          callback(new Error(''))
        }
      }
      return {
        Sorg: '',
        Y: 'Y',
        N: 'N',
        selectData: {
          searchKey: '',
          current: null
        },
        pwdcontent: '请输入9-20位字母数字,可选特殊符号#@!~%^&*-+,不包含空格单双引号',
        accContent: '输入4-16位以字母开头、可带数字、“_”、“.”的字串',
        noData: 1,
        dialogUpdateVisible: false,
        dialogAddVisible: false,
        formLabelWidth: '120px',
        updataShowD: {
          name: '',
          id: '',
          roleIds: [],
          description: '',
          password: '',
          isActive: 'Y',
          orgId: '',
          phone: '',
          phone2: '',
          email: ''
        },
        addShowD: {
          roleIds: [],
          name: '',
          id: '',
          description: '',
          password: '',
          phone: '',
          phone2: '',
          email: '',
          isActive: 'Y',
          orgId: ''
        },
        rules: {
          name: [
            {required: true, message: '请输入用户名', trigger: 'blur'},
            {validator: validateName, message: '用户名输入不正确', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {validator: validatePassword, message: '密码格式错误', trigger: 'blur'}
          ],
          orgId: [
            {required: true, message: '请选择组织机构'}
          ],
          roleIds: [
            {type: 'array', required: true, message: '请选择用户角色', trigger: 'change'}
          ],
          isActive: [
            {required: true, message: '请选择状态'}
          ],
          description: [
            {max: 200, message: '请输入小于200个字符', trigger: 'blur'}
          ],
          email: [
            {max: 80, trigger: 'blur'}
          ]
        },
        updataRules: {
          email: [
            {max: 80, trigger: 'blur'}
          ]
        }
      }
    },
    mounted () {
      this.loadData()
    },
    computed: {
      ...mapGetters([
        'allAdmin'
      ])
    },
    methods: {
      ...mapActions([
        'getAdmin',
        'updataAdmin',
        'deleteAdmin',
        'addAdmin'
      ]),
      showAddDialog () {
        this.dialogAddVisible = true
      },
      changeSearchs () {
        if (event.keyCode === 13) {
          this.searchContent()
        }
      },
      // 搜索
      searchContent () {
        this.selectData.current = 1
        this.loadData()
      },
      // 翻页
      changePage (val) {
        this.selectData.current = val
        this.loadData()
      },
      //  数据请求
      loadData () {
        this.getAdmin(this.selectData).then((res) => {
          statusCallback(res, function (that) {
            if (that.allAdmin.users) {
              that.noData = that.allAdmin.users.length
            }
          }, this)
        })
      },
      addData (formName) {
        let vm = this
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.addAdmin(this.addShowD).then((res) => {
              statusCallback(res, function (that) {
                // vm.loadData().then((res) => {
                //   statusCallback(res)
                // })
                vm.loadData()
                vm.$refs['addShowD'].resetFields()
                vm.dialogAddVisible = false
                vm.$message({
                  message: '添加成功',
                  type: 'success'
                })
              }, this)
            })
          } else {
            return false
          }
        })
      },
      showUpdateDialog (item) {
        this.dialogUpdateVisible = true
        this.updataShowD = {
          id: item.id,
          name: item.name,
          orgId: item.orgId,
          roleIds: item.roles.map(item => {
            return item.id
          }),
          phone: item.phone,
          phone2: item.phone2,
          email: item.email,
          description: item.description,
          password: item.password,
          isActive: item.isActive
        }
      },
      updateData (formName) {
        if (this.updataShowD.roleIds.length > 0) {
          let vm = this
          this.$refs[formName].validate((valid) => {
            if (valid) {
              this.updataAdmin(this.updataShowD).then((res) => {
                statusCallback(res, function (that) {
                  // vm.loadData().then((res) => {
                  //   statusCallback(res)
                  // })
                  vm.loadData()
                  vm.dialogUpdateVisible = false
                  vm.$message({
                    message: '更新成功',
                    type: 'success'
                  })
                }, this)
              })
            }
          })
        } else {
          this.$alert('用户角色必填', '错误', {
            confirmButtonText: '确定',
            type: 'error'
          })
        }
      },
      addCancel (formName) {
        this.$refs[formName].resetFields()
        this.dialogAddVisible = false
      },
      updataCancel (formName) {
        this.$refs[formName].resetFields()
        this.dialogUpdateVisible = false
      },
      deleteData (item) {
        let _this = this
        this.$confirm(`此操作将删除${item.name}该用户, 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let id = {
            id: item.id
          }
          this.deleteAdmin(id).then((res) => {
            statusCallback(res, function (that) {
              // _this.loadData().then((res) => {
              //   statusCallback(res)
              // })
              _this.loadData()
              _this.$message({
                message: '删除成功',
                type: 'success'
              })
            }, this)
          })
        })
      }
    },
    components: {
      'va-table': VATable,
      'page': Page
    }
  }
</script>

<style scoped>
th {  white-space: nowrap }
.pwdSpan {
  font-size: 10px;
}
/*.el-form-item {
  margin: 0 !important
}
.el-checkbox__inner,.el-radio__inner {
  width: 15px !important;
  height: 15px !important
}
.el-radio-group {
  line-height: 36px !important
}*/
</style>
