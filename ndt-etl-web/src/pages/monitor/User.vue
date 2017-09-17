<template>
<div v-if="allAdmin">
  <page title="管理员" v-if="showAllUsers">
      <div slot="tools" v-if="allAdmin">
        <button class="btn btn-primary" @click="showAdd">添加</button>
      </div>
      <div slot="content" v-if="allAdmin">
        <table class="table table-hover table-fixed">
          <thead>
          <tr>
            <th width="5%">ID</th>
            <th width="15%">用户名</th>
            <th width="15%">手机号</th>
            <th width="18%">邮箱</th>
            <th>用户角色</th>
            <th width="10%">状态</th>
            <th width="10%">操作</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="item in allAdmin">
            <td>{{ item.id }}</td>
            <td>{{ item.name }}</td>
            <td>{{ item.phone }}</td>
            <td>{{ item.email }}</td>
            <td><span v-for="item in item.roles">{{item.name}}<span v-if="item.name">,&nbsp;</span></span></td>
            <td v-if=" item.is_active == true"><span class="glyphicon glyphicon-ok text-success"></span></td>
            <td v-else><span class="glyphicon glyphicon-remove text-danger"></span></td>
            <td>
            <button  @click="showUpdata(item)" class="btn btn-primary btn-xs">编辑</button>
            <button  @click="deleteU(item)"  class="btn btn-danger btn-xs">删除</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </page>
    <page title="修改用户信息" v-if="showUpdateUser">
      <div slot="content">
        <el-form :model="updataShowData" ref="updataShowData" :rules="updateRuls" label-width="100px">
        <el-row>
            <el-col :span="9">
              <el-form-item label="ID" prop="id">
                <el-input v-model="updataShowData.id" :disabled="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="用户名" prop="name">
                <el-input v-model="updataShowData.name" :disabled="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="密码" prop="password">
                <el-input v-model="updataShowData.password" type="password"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="手机号" prop="phone">
                <el-input v-model="updataShowData.phone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="updataShowData.email" :maxlength="80"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
             <el-form-item label="用户角色" prop="roleIds">
              <el-checkbox-group v-model="updataShowData.roleIds">
                <el-checkbox :label="item.id" v-for="item in allroles">{{item.name}}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-row>
            <el-col :span="6">
               <el-form-item prop="state">
              <el-checkbox-group v-model="updataShowData.is_active">
                <el-checkbox label="已启用" name="is_active" v-if="updataShowData.is_active==true"></el-checkbox>
                <el-checkbox label="未启用" name="is_active" v-if="updataShowData.is_active==false"></el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div slot="footer">
        <div class="ml-10">
          <button class="btn btn-success btn-main" @click="updateOneUser('updataShowData')">{{titleUpdata}}</button>
          <button class="btn btn-default" @click="closeUpdata('updataShowData')">返回</button>
        </div>
      </div>
    </page>
    <page title="添加用户信息" v-if="addUser">
      <div slot="content">
        <el-form :model="addUserData" :rules="rules" ref="addUserData" label-width="100px">
          <el-row>
            <el-col :span="11">
              <el-form-item label="用户名" prop="name">
                <el-input v-model="addUserData.name"></el-input>
                <span class="pwdSpan">输入4-16位以字母开头、可带数字、“_”、“.”的字串</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="密码" prop="password">
                <el-input v-model="addUserData.password" type="password"></el-input>
                <span class="pwdSpan">请输入9-20位字母数字,可选特殊符号#@!~%^&*-+,不包含空格单双引号</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="手机号" prop="phone">
                <el-input v-model="addUserData.phone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="addUserData.email" :maxlength="80"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
           <el-form-item label="用户角色" prop="roleIds">
              <el-checkbox-group v-model="addUserData.roleIds">
                <el-checkbox :label="item.id" v-for="item in allroles">{{item.name}}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-row>
            <el-col :span="8">
                <el-form-item label="状态" prop="is_active">
              <el-radio-group v-model="addUserData.is_active">
                <el-radio :label = true>已启用</el-radio>
                <el-radio :label = false>未启用</el-radio>
              </el-radio-group>
            </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div slot="footer">
        <div class="ml-10">
           <button class="btn btn-success btn-main" @click="addNewUsers('addUserData')">{{titleAdd}}</button>
          <button class="btn btn-default" @click="closeAdd('addUserData')">返回</button>
        </div>
      </div>
    </page>
</div>
</template>
<script>
  import VATable from '../../components/VATable.vue'
  import Page from '../../components/Page.vue'
  import { mapGetters, mapActions } from 'vuex'
  import { statusCallback } from '../../lib/utils'
  export default {
    data () {
      let validateName = (rule, value, callback) => {
        let reg = /^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){3,15}$/
        if (reg.test(value)) {
          callback()
        } else {
          callback(new Error('密码输入不正确'))
        }
      }
      let validatepassword = (rule, value, callback) => {
        // let reg = /^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*]+$){9,20}[a-zA-Z\d!@#$%^&*-+|~]+$/
        // let reg = /^((?=.*[a-zA-Z])(?=.*\d)|(?=[a-zA-Z])(?=.*[#@!~%^&*-+])|(?=.*\d)(?=.*[#@!~%^&*-+]))[a-zA-Z\d#@!~%^&*-+]{9,20}$/
        let reg = /^((?=.*[a-z])(?=.*\d)|(?=[a-z])(?=.*[#@!~%^&*\-+])|(?=.*\d)(?=.*[#@!~%^&*\-+]))[a-zA-Z\d#@!~%^&*\-+]{9,20}$/
        if (reg.test(value)) {
          callback()
        } else {
          callback(new Error('密码输入不正确'))
        }
      }
      let validateEmail = (rule, value, callback) => {
        let reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
        if (reg.test(value)) {
          callback()
        } else {
          callback(new Error('邮箱格式不正确'))
        }
      }
      let validatePhone = (rule, value, callback) => {
      //  这里对邮箱的前缀限制为数字字母下划线和横杠
        let reg = /^|^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[0])|(18[0,5-9]))\\d{8}$/
        if (reg.test(value)) {
          callback()
        } else {
          callback(new Error('手机格式不正确'))
        }
      }
      return {
        showAllUsers: true,
        showAddUsers: false,
        showUpdateUser: false,
        addUser: false,
        updataShowData: {
          name: '',
          id: '',
          email: '',
          password: '',
          roleIds: [],
          is_active: true,
          phone: ''
        },
        addUserData: {
          name: '',
          password: '',
          email: '',
          roleIds: [],
          is_active: true,
          phone: ''
        },
        titleUpdata: '保存修改',
        titleAdd: '添加用户',
        rules: {
          name: [
            {required: true, message: '请输入用户名', trigger: 'blur'},
            { validator: validateName, message: '用户名输入不正确', trigger: 'blur' }
          ],
          email: [
            {required: true, message: '请输入邮箱', trigger: 'blur'},
            { max: 80, validator: validateEmail, message: '请输入正确邮箱', trigger: 'blur' }
          ],
          password: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            { validator: validatepassword, message: '密码格式错误', trigger: 'blur' }
          ],
          phone: [
            {validator: validatePhone, message: '请输入正确的手机号', trigger: 'blur'}
          ],
          roleIds: [
            {type: 'array', required: true, message: '请选择用户角色', trigger: 'change'}
          ]
        },
        updateRuls: {
          phone: [
            {validator: validatePhone, message: '请输入正确的手机号', trigger: 'blur'}
          ],
          email: [
          {required: true, message: '邮箱地址必填', trigger: 'blur'},
          { max: 80, validator: validateEmail, message: '请输入正确邮箱', trigger: 'blur' }
          ],
          roleIds: [
            {type: 'array', required: true, message: '用户角色必填', trigger: 'change'}
          ]
        }
      }
    },
    computed: {
      ...mapGetters([
        'allAdmin',
        'allroles'
        // 'allRoleList'
      ])
    },
    created () {
      // 获取所有角色的数据
      this.getAdmin().then((res) => {
      })
      this.getRoles().then((res) => {
      })
    },
    methods: {
      ...mapActions([
        'getAdmin',
        'getRoles',
        'deleteAdmin',
        'updataAdmin',
        'addAdmin'
        // 'getAllRoleList'
      ]),
      showUpdata (item) {
        this.showAllUsers = false
        this.showUpdateUser = true
        this.updataShowData = {
          id: item.id,
          name: item.name,
          phone: item.phone,
          password: item.password,
          email: item.email,
          roleIds: item.roles.map(item => {
            return item.id
          }),
          is_active: item.is_active
        }
      },
      showAdd () {
        this.showAllUsers = false
        this.showUpdateUser = false
        this.addUser = true
      },
      updateOneUser (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.updataAdmin(this.updataShowData).then((res) => {
              statusCallback(res, function (that) {
                that.getAdmin().then((res) => {
                  that.statusCallback(res)
                })
                that.closeUpdata()
                that.$message({
                  message: '更新成功',
                  type: 'success'
                })
              })
            })
          } else {
            return false
          }
        })
      },
      deleteU (data) {
        this.$confirm(`此操作将删除${data.name}该用户, 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let id = {
            id: data.id
          }
          this.deleteAdmin(id).then((res) => {
            statusCallback(res, function (that) {
              that.getAdmin().then((res) => {
                that.statusCallback(res)
              })
              that.$message({
                message: '删除成功',
                type: 'success'
              })
            })
          })
        })
      },
      addNewUsers (addUserData) {
        this.addUserData.is_active = Boolean(this.addUserData.is_active)
        this.$refs[addUserData].validate((valid) => {
          if (valid) {
            this.addAdmin(this.addUserData).then((res) => {
              statusCallback(res, function (that) {
                that.getAdmin().then((res) => {
                  that.statusCallback(res)
                })
                that.closeUpdata()
                that.$message({
                  message: '添加成功',
                  type: 'success'
                })
              })
            })
          } else {
            return false
          }
        })
      },
      closeUpdata (data) {
        this.showAllUsers = true
        this.showUpdateUser = false
        this.addUser = false
        // this.$refs['addUserData'].resetFields()
      },
      closeAdd (formName) {
        this.$refs[formName].resetFields()
        this.showUpdateUser = false
        this.addUser = false
        this.showAllUsers = true
      }
    },
    components: {
      'va-table': VATable,
      'page': Page
    }
  }
</script>
<style onlyUser>
  .role-table th, .role-table td {
    text-align: center;
  }
  td { word-wrap: break-word;}
  .pwdSpan {
    font-size: 10px;
  }
</style>
