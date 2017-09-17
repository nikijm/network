<template>
  <div v-if="allOutUser.users">
    <page title="外部用户管理">
      <div slot="tools">
        <el-select v-model="selectData.orgId" clearable filterable placeholder="请选择组织机构" @change="changeOrg">
          <el-option
          v-for="item in allOutUser.orgs" :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
      <el-input placeholder="请输入搜索内容" class="tools-button" v-model="selectData.searchKey" @keyup.native='changeSearchs($event)'>
        <el-button slot="append" icon="search" @click="searchContent"></el-button>
      </el-input>
      <button class="btn btn-primary" @click="showAddDialog">添加</button>
    </div>
    <div slot="content">
     <div style="min-width: 100%;">
    <div class="table-head">
      <table class="table table-hover table-fixed" >
          <colgroup>
              <col style="width: 5%;"/>
              <col style="width: 15%;"/>
              <col style="width: 11%;"/>
              <col style="width: 12%;"/>
              <col style="width: 19%;"/>
              <col style="width: 5%;"/>
          </colgroup>
          <thead>
              <tr>
              <th >序号</th>
              <th >组织机构</th>
              <th >用户名</th>
              <th >电话</th>
              <th >电子邮箱</th>
              <th >状态</th>
              <th>备注</th>
              <th>操作</th>
              </tr>
          </thead>
      </table>
      </div>
      <div class="table-body" ref="scrollContent">
      <table class="table table-hover table-fixed">
          <colgroup>
          <col style="width: 5%;"/>
              <col style="width: 15%;"/>
              <col style="width: 10%;"/>
              <col style="width: 12%;"/>
              <col style="width: 19%;"/>
              <col style="width: 5%;"/>
          </colgroup>
          <tbody>
          <tr v-for="(item, index) in allOutUser.users">
            <td>{{ (allOutUser.paging.current - 1) * allOutUser.paging.page_size + index + 1 }}</td>
            <td>{{ item.orgName }}</td>
            <td>{{ item.name }}</td>
            <td><p style="margin:0">{{ item.phone }}</p><p style="margin:0">{{ item.phone2 }}</p></td>
            <td>{{ item.email }}</td>    
            <td v-if=" item.isActive == 'Y'"><span class="glyphicon glyphicon-ok text-success"></span></td>
            <td v-else><span class="glyphicon glyphicon-remove text-danger"></span></td>
            <td>{{ item.description }}</td>
            <td>
              <button class="btn btn-primary btn-xs" @click="showUpdateDialog(item)">编辑</button>
              <button class="btn btn-danger btn-xs" @click="deleteData(item)">删除</button>
            </td>
          </tr>
    <!--       <tr><td>ddd</td></tr>
          <tr><td>ddd</td></tr>
          <tr><td>ddd</td></tr>
          <tr><td>ddd</td></tr>
          <tr><td>ddd</td></tr>
          <tr><td>ddd</td></tr>
          <tr><td>ddd</td></tr>
          <tr><td>ddd</td></tr>
          <tr><td>ddd</td></tr>
          <tr><td>ddd</td></tr>
          <tr><td>ddd</td></tr>
          <tr><td>ddd</td></tr>
          <tr><td>ddd</td></tr>
          <tr><td>ddd</td></tr>
          <tr><td>ddd</td></tr>
          <tr><td>ddd</td></tr> -->
           <p  style='width:900px;text-align: center;' v-show='noData<=0'>暂无数据</p>
         </tbody>
      </table>
      </div>
    </div> 



<!-- 
      <table class="table table-hover table-fixed">
        <thead>
          <tr>
            <th width="5%">序号</th>
            <th width="15%">组织机构</th>
            <th width="10%">用户名</th>
            <th width="10%">电话</th>
            <th width="15%">电子邮箱</th>
            <th width="5%">状态</th>
            <th>备注</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in allOutUser.users">
            <td>{{ (allOutUser.paging.current - 1) * allOutUser.paging.page_size + index + 1 }}</td>
            <td>{{ item.orgName }}</td>
            <td>{{ item.name }}</td>
            <td><p style="margin:0">{{ item.phone }}</p><p style="margin:0">{{ item.phone2 }}</p></td>
            <td>{{ item.email }}</td>    
            <td v-if=" item.isActive == 'Y'"><span class="glyphicon glyphicon-ok text-success"></span></td>
            <td v-else><span class="glyphicon glyphicon-remove text-danger"></span></td>
            <td>{{ item.description }}</td>
            <td>
              <button class="btn btn-primary btn-xs" @click="showUpdateDialog(item)">编辑</button>
              <button class="btn btn-danger btn-xs" @click="deleteData(item)">删除</button>
            </td>
          </tr>
        </tbody>
      </table> -->
      <!-- <div class="col-md-12" style='text-align: center;' v-show='noData<=0'>暂无数据</div> -->
      <el-pagination v-show='noData>0'
      @current-change="changePage"
      :current-page="allOutUser.paging.current"
      layout="prev, pager, next, jumper"
      :total="allOutUser.paging.total"
      :page-size="allOutUser.paging.page_size">
    </el-pagination>
  </div>
</page>
<el-dialog title="修改" :visible.sync="dialogUpdateVisible">
  <el-form ref="updataShowD" :model="updataShowD" :label-width="formLabelWidth" :rules="updataRules">
    <el-form-item label="组织机构">
      <el-select placeholder="请选择组织机构" v-model="updataShowD.orgId">
      <el-option :label='item.name' :value="item.id" v-for="item in allOutUser.orgs"></el-option>
      </el-select>
    </el-form-item>
    <el-row>
      <el-col :span="20">
        <el-form-item label="用户名" prop="name">
          <el-input :disabled="true" v-model="updataShowD.name" ></el-input>
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
          <el-input v-model="updataShowD.phone" size='small'></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="20">
        <el-form-item label="手机号2" prop="phone2">
          <el-input v-model="updataShowD.phone2" size='small'></el-input>
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
  </el-form>
  <div slot="footer" class="dialog-footer">
    <button class="btn btn-success btn-main" @click="updateData('updataShowD')">保存修改</button>
    <button class="btn btn-default" @click="updataCancel('updataShowD')">返回</button>
  </div>
</el-dialog>
<el-dialog title="增加" :visible.sync="dialogAddVisible">
  <el-form :model="addShowD" :label-width="formLabelWidth" ref="addShowD" :rules="rules">
    <el-form-item label="组织机构" prop="orgId">
      <el-select placeholder="请选择组织机构" v-model="addShowD.orgId">
        <el-option :label='item.name' :value="item.id" v-for="item in allOutUser.orgs"></el-option>
      </el-select>
    </el-form-item>
    <el-row>
      <el-col :span="20">
        <el-form-item label="用户名" prop="name">
          <el-input v-model="addShowD.name" size='small'></el-input>
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
          callback()
        } else {
          callback(new Error('密码格式不正确'))
        }
      }
      let validateName = (rule, value, callback) => {
        if (USERNAME_PATTERN.test(value)) {
          callback()
        } else {
          callback(new Error(''))
        }
      }
      return {
        orgId: '',
        selectData: {
          orgId: '',
          searchKey: '',
          current: null
        },
        pwdcontent: '请输入9-20位字母数字,可选特殊符号#@!~%^&*-+,不包含空格单双引号',
        accContent: '输入4-16位以字母开头、可带数字、“_”、“.”的字串',
        Y: 'Y',
        N: 'N',
        noData: 1,
        dialogUpdateVisible: false,
        dialogAddVisible: false,
        formLabelWidth: '120px',
        updataShowD: {
          externalz: 1,
          name: '',
          id: '',
          description: '',
          password: '',
          isActive: 'Y',
          orgId: '',
          phone: '',
          phone2: '',
          email: ''
        },
        addShowD: {
          externalz: 1,
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
          phone: [
            // {required: true, message: '手机号必填', trigger: 'blur'},
            // {validator: validatePhone, message: '请输入正确的手机号', trigger: 'blur'}
          ],
          email: [
            // {required: true, message: '邮箱地址必填', trigger: 'blur'},
            {max: 80, trigger: 'blur'}
          ]
        },
        screenHeight: document.body.clientHeight
      }
    },
    created () {
      this.loadData()
    },
    mounted () {
      console.log(1)
      this.$refs.scrollContent.style.height = this.screenHeight - 240 + 'px'
      console.log(this.screenHeight - 240 + 'px')
    },
    computed: {
      ...mapGetters([
        'allOutUser'
      ])
    },
    methods: {
      ...mapActions([
        'getOutUser',
        'updataUsers',
        'deleteUsers',
        'addUsers'
      ]),
      showAddDialog () {
        this.dialogAddVisible = true
      },
      // 过滤
      changeOrg () {
        this.selectData.current = 1
        this.loadData()
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
        this.getOutUser(this.selectData).then((res) => {
          statusCallback(res, function (that) {
            if (that.allOutUser.users) {
              that.noData = that.allOutUser.users.length
            }
          }, this)
        })
      },
      addData (formName) {
        let vm = this
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.addUsers(this.addShowD).then((res) => {
              statusCallback(res, function (that) {
                // vm.getOutUser().then((res) => {
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
          externalz: 1,
          id: item.id,
          name: item.name,
          orgId: item.orgName == null ? '' : item.orgId,
          phone: item.phone,
          phone2: item.phone2,
          email: item.email,
          description: item.description,
          password: item.password,
          isActive: item.isActive
        }
      },
      updateData (formName) {
        if (this.updataShowD.orgId) {
          let vm = this
          this.$refs[formName].validate((valid) => {
            if (valid) {
              this.updataUsers(this.updataShowD).then((res) => {
                statusCallback(res, function (that) {
                  // vm.getOutUser().then((res) => {
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
          this.$alert('组织机构必填', '错误', {
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
          this.deleteUsers(id).then((res) => {
            statusCallback(res, function (that) {
              // _this.getOutUser().then((res) => {
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
/*  .el-form-item {
  margin: 0 !important
}
.el-checkbox__inner,.el-radio__inner {
  width: 15px !important;
  height: 15px !important
}
.el-radio-group {
  line-height: 36px !important
}*/
.table-head{padding-right:17px;}
.table-head .table{ margin-bottom: 0 }
.table-body{width:100%;overflow-y:auto;}
.table-head table,.table-body table{width:100%;}
</style>
