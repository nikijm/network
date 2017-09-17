<template>
  <div v-if="allorganizations">
    <page title="组织机构" v-if="showAllD">
      <div slot="tools">
        <el-input placeholder="请输入搜索内容" class="tools-button" v-model="param.searchKey" @keyup.enter.native='load'>
          <el-button slot="append" icon="search" @click="load"></el-button>
        </el-input>
        <button class="btn btn-primary" @click="showAddDialog">添加</button>
      </div>
      <div slot="content">
        <div style="width:100%;overflow: auto">

      <!--   <div class="table-head">
        <table class="table table-hover table-fixed" >
          <colgroup>
              <col style="width: 4%;"/>
              <col style="width: 9%;"/>
              <col style="width: 10%;"/>
              <col style="width: 9%;"/>
              <col style="width: 10%;"/>
              <col style="width: 8%;"/>
              <col style="width: 10%;"/>
              <col style="width: 6%;"/>
              <col style="width: 9%;"/>
          </colgroup>
          <thead>
              <tr>
              <th >序号</th>
              <th >组织机构</th>
              <th >组织机构代码</th>
              <th >机构地址</th>
              <th >机构电话</th>
              <th >联系人</th>
              <th >联系电话</th>
              <th >组织状态</th>
              <th >操作</th>
              </tr>
          </thead>
      </table>
      </div>
      <div class="table-body" ref="scrollContent">
      <table class="table table-hover table-fixed">
          <colgroup>
           <col style="width: 4%;"/>
              <col style="width: 9%;"/>
              <col style="width: 9%;"/>
              <col style="width: 9%;"/>
              <col style="width: 10%;"/>
              <col style="width: 7%;"/>
              <col style="width: 10%;"/>
              <col style="width: 4%;"/>
              <col style="width: 10%;"/>
          </colgroup>
          <tbody>
         <tr v-for="(item, index) in allorganizations.organizations">
              <td>{{ (allorganizations.paging.current - 1) * allorganizations.paging.page_size + index + 1 }}</td>
              <td>{{ item.name }}</td>
              <td>{{ item.code }}</td>
              <td>{{ item.address }}</td>
              <td>{{ item.phone }} <br /> {{item.phone2 }}</td>
              <td>{{ item.attn }}</td>
              <td>{{ item.attnPhone}} <br /> {{item.attnPhone2 }}</td>
              <td v-if=" item.isActive == 'Y'"><span class="glyphicon glyphicon-ok text-success"></span></td>
              <td v-else><span class="glyphicon glyphicon-remove text-danger"></span></td>
              <td>
                <button class="btn btn-primary btn-xs" @click="showDialog(item)">编辑</button>
                <button class="btn btn-danger btn-xs" @click="DeleteOrg(item)">删除</button>
              </td>
            </tr>
          </tbody>
      </table>
      </div> -->
    <!-- </div> -->


          <table class="table table-hover table-fixed">
          <thead>
            <tr>
              <th width="5%">序号</th>
              <th width="9%">组织机构</th>
              <th width="9%">组织机构代码</th>
              <th width="9%">机构地址</th>
              <th width="9%">机构电话</th>
              <th width="10%">联系人</th>
              <th width="10%">联系电话</th>
              <th width="5%">组织状态</th>
              <th width="10%">操作</th>
            </tr>
            </thead>
          <tbody>
            <tr v-for="(item, index) in allorganizations.organizations">
              <td>{{ (allorganizations.paging.current - 1) * allorganizations.paging.page_size + index + 1 }}</td>
              <td>{{ item.name }}</td>
              <td>{{ item.code }}</td>
              <td>{{ item.address }}</td>
              <td>{{ item.phone }} <br /> {{item.phone2 }}</td>
              <td>{{ item.attn }}</td>
              <td>{{ item.attnPhone}} <br /> {{item.attnPhone2 }}</td>
              <td v-if=" item.isActive == 'Y'"><span class="glyphicon glyphicon-ok text-success"></span></td>
              <td v-else><span class="glyphicon glyphicon-remove text-danger"></span></td>
              <td>
                <button class="btn btn-primary btn-xs" @click="showDialog(item)">编辑</button>
                <button class="btn btn-danger btn-xs" @click="DeleteOrg(item)">删除</button>
              </td>
            </tr>
         </tbody>
        </table>
        <el-pagination
          @current-change="toPage"
          :current-page="allorganizations.paging.current"
          layout="prev, pager, next, jumper"
          :total="allorganizations.paging.total"
          :page-size="allorganizations.paging.page_size">
        </el-pagination>
        </div>
      </div>
    </page>
    <page title="修改" v-if="dialogUpdateVisible">
      <div slot="content">
        <el-form :label-width="formLabelWidth" :model="updataOrg" ref="updataOrg" :rules="rulesUpdate">
          <el-row>
            <el-col :span="18">
              <el-form-item label="ID">
                <el-input :disabled="true"  v-model="updataOrg.orgId"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="组织机构代码" prop="code" >
                <el-input v-model="updataOrg.code" :maxlength="40"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="组织机构" prop="name">
                <el-input v-model="updataOrg.name" :maxlength="60"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="机构地址">
                <el-input v-model="updataOrg.address" :maxlength="60"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="手机号" prop="Phone">
                <el-input v-model="updataOrg.phone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="固定电话">
                <el-input v-model="updataOrg.phone2"></el-input>
                <span class="pwdSpan">请加上区号并以-连接</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="邮箱">
                <el-input v-model="updataOrg.email" :maxlength="80"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="备注" prop="description">
                <el-input v-model="updataOrg.description" :maxlength="200"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="组织状态" prop="isActive">
                <el-radio-group v-model="updataOrg.isActive">
                  <el-radio :label='Y'>已启用</el-radio>
                  <el-radio :label='N'>未启用</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="联系人" prop="attn">
                <el-input v-model="updataOrg.attn"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="手机号" prop="attnPhone">
                <el-input v-model="updataOrg.attnPhone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="手机号2">
                <el-input v-model="updataOrg.attnPhone2"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="联系人邮箱">
                <el-input v-model="updataOrg.attnEmail" :maxlength="80"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <button class="btn btn-success btn-main" @click="updateO('updataOrg')">保存修改</button>
        <button class="btn btn-default" @click="updata_cancel('updataOrg')">返回</button>
      </div>
    </page>

    <page title="添加" v-if="dialogAddOrgVisible">
      <div slot="content">
        <el-form :label-width="formLabelWidth" :model="AddOData" ref="AddOData" :rules="rules">
          <el-row>
            <el-col :span="18">
              <el-form-item label="组织机构" prop="name">
                <el-input v-model="AddOData.name" :maxlength="60"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="组织机构代码" prop="code">
                <el-input v-model="AddOData.code" :maxlength="40"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="机构地址" prop="address">
                <el-input v-model="AddOData.address" :maxlength="60"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="手机号" prop="phone">
                <el-input v-model="AddOData.phone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="固定电话" prop="phone2">
                <el-input v-model="AddOData.phone2"></el-input>
                <span class="pwdSpan">请加上区号并以-连接</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="AddOData.email" :maxlength="80"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="备注" prop="description">
                <el-input v-model="AddOData.description" :maxlength="200"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="组织状态" prop="isActive">
                <el-radio-group v-model="AddOData.isActive">
                  <el-radio :label='Y'>已启用</el-radio>
                  <el-radio :label='N'>未启用</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <hr/>
          <el-row>
            <el-col :span="18">
              <el-form-item label="联系人" prop="attn">
                <el-input v-model="AddOData.attn"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="手机号" prop="attn_phone">
                <el-input v-model="AddOData.attn_phone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="手机号2" prop="attn_phone2">
                <el-input v-model="AddOData.attn_phone2"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="联系人邮箱" prop="attn_email">
                <el-input v-model="AddOData.attn_email" :maxlength="80"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <button class="btn btn-success btn-main" @click="submitForm('AddOData')">确认添加</button>
         <button class="btn btn-default" @click="Add_cancel('AddOData')">返回</button>
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
      /* let validatePassword = (rule, value, callback) => {
        let reg = /^((?=.*[a-z])(?=.*\d)|(?=[a-z])(?=.*[#@!~%^&*\-+])|(?=.*\d)(?=.*[#@!~%^&*\-+]))[a-zA-Z\d#@!~%^&*\-+]{9,20}$/
        if (reg.test(value)) {
          callback()
        } else {
          callback(new Error('密码输入不正确'))
        }
      } */
      /* let validateName = (rule, value,]|[._]){3,15}$/
        if (reg.test(value)) { callback) => {
        let reg = /^[a-zA-Z]{1}([a-zA-Z0-9
          callback()
        } else {
          callback(new Error('密码输入不正确'))
        }
      } */
      let validateEmail = (rule, value, callback) => {
        let reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
        if (reg.test(value) || value === undefined || value === '') {
          callback()
        } else {
          callback(new Error('邮箱格式不正确'))
        }
      }
      let validatePhone = (rule, value, callback) => {
        let reg = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[0])|(18[0-9]))\d{8}$/
        if (reg.test(value)) {
          callback()
        } else {
          callback(new Error('手机格式不正确'))
        }
      }
      let validatePhone2 = (rule, value, callback) => {
        let reg = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[0])|(18[0-9]))\d{8}$/
        if (reg.test(value) || value === undefined || value === '') {
          callback()
        } else {
          callback(new Error('手机格式不正确'))
        }
      }
      let validateTelephont = (rule, value, callback) => {
        let reg = /^0\d{2,3}-\d{7,8}(-\d{1,6})?$/
        if (reg.test(value) || value === undefined || value === '') {
          callback()
        } else {
          callback(new Error('固定电话格式不正确'))
        }
      }
      return {
        showAllD: true,
        dialogUpdateVisible: false,
        dialogAddOrgVisible: false,
        formLabelWidth: '120px',
        param: {
          current: 1,
          searchKey: ''
        },
        srcP: {
          src: ''
        },
        Y: 'Y',
        N: 'N',
        AddOData: {
          attn_id: '',
          name: '',
          description: '',
          isActive: 'Y',
          code: '',
          address: '',
          user_attn: '',
          user_description: '',
          user_password: '',
          user_phone: '',
          user_phone2: '',
          user_email: ''
        },
        updataOrg: {
          logo: '',
          orgId: '',
          name: '',
          code: '',
          description: '',
          isActive: 'Y',
          attn: '',
          attnPhone: '',
          attnPhone2: '',
          attnEmail: ''
        },
        rules: {
          name: [
            {required: true, message: '请输入组织机构', trigger: 'blur'},
            {max: 60, message: '请输入小于60个字符', trigger: 'blur'}
          ],
          description: [
            {max: 200, message: '请输入小于200个字符', trigger: 'blur'}
          ],
          code: [
            {required: true, message: '请输入组织机构代码', trigger: 'blur'},
            {max: 100, message: '请输入小于100个字符', trigger: 'blur'}
          ],
          attn: [
            {required: true, message: '请输入联系人', trigger: 'blur'}
          ],
          isActive: [
            {required: true, message: '请选择状态'}
          ],
          phone: [
            {required: true, message: '请输入手机号', trigger: 'blur'},
            {validator: validatePhone, message: '请输入正确的手机号', trigger: 'blur'}
          ],
          phone2: [
            {message: '请输入电话', trigger: 'blur'},
            {validator: validateTelephont, message: '请输入正确的固定电话', trigger: 'blur'}
          ],
          attn_phone: [
            {required: true, message: '请输入手机号', trigger: 'blur'},
            {validator: validatePhone, message: '请输入正确的手机号', trigger: 'blur'}
          ],
          attn_phone2: [
            {message: '请输入手机号', trigger: 'blur'},
            {validator: validatePhone2, message: '请输入正确的手机号', trigger: 'blur'}
          ],
          attn_email: [
            {message: '联系人邮箱', trigger: 'blur'},
            {max: 80, validator: validateEmail, message: '请输入正确邮箱', trigger: 'blur'}
          ],
          email: [
            {message: '请输入邮箱', trigger: 'blur'},
            {max: 80, validator: validateEmail, message: '请输入正确邮箱', trigger: 'blur'}
          ]
        },
        rulesUpdate: {
          name: [
            {required: true, message: '组织机构必填', trigger: 'blur'},
            {max: 60, message: '请输入小于60个字符', trigger: 'blur'}
          ],
          code: [
            {required: true, message: '组织机构代码必填', trigger: 'blur'},
            {max: 40, message: '请输入小于40个字符', trigger: 'blur'}
          ],
          phone: [
            {required: true, message: '请输入手机号', trigger: 'blur'},
            {validator: validatePhone, message: '请输入正确的手机号', trigger: 'blur'}
          ],
          attnPhone: [
            {required: true, message: '手机号必填', trigger: 'blur'},
            {validator: validatePhone, message: '请输入正确的手机号', trigger: 'blur'}
          ],
          attn: [
            {required: true, message: '请输入联系人', trigger: 'blur'}
          ]
        },
        screenHeight: document.body.clientHeight
      }
    },
    created () {
      this.load()
    },
    mounted () {
      this.$refs.scrollContent.style.height = this.screenHeight - 220 + 'px'
    },
    computed: {
      ...mapGetters([
        'allorganizations'
      ])
    },
    methods: {
      ...mapActions([
        'getOrganization',
        'addOrg',
        'updateOrg',
        'deleteOrg',
        'upload',
        'uploadLogo'
      ]),
      load () {
        this.getOrganization(this.param).then((res) => {
        })
      },
      showDialog (item) {
        this.showAllD = false
        this.dialogUpdateVisible = true
        this.dialogAddOrgVisible = false
        this.updataOrg = {
          orgId: item.orgId,
          name: item.name,
          code: item.code,
          description: item.description,
          isActive: item.isActive,
          address: item.address,
          phone: item.phone,
          phone2: item.phone2,
          email: item.email,
          attn: item.attn,
          attnPhone: item.attnPhone,
          attnPhone2: item.attnPhone2,
          attnEmail: item.attnEmail
        }
      },
      showAddDialog () {
        this.dialogAddOrgVisible = true
        this.dialogUpdateVisible = false
        this.showAllD = false
      },
      updateO (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if (this.updataOrg.name === '' || this.updataOrg.code === '') {
              this.$alert('带*号必填', '错误', {
                confirmButtonText: '确定',
                type: 'error'
              })
            } else {
              if (this.updataOrg.email === '') {
                this.updataOrg.email = null
              }
              let updataNew = {
                orgId: this.updataOrg.orgId,
                name: this.updataOrg.name,
                code: this.updataOrg.code,
                attn: this.updataOrg.attn,
                description: this.updataOrg.description,
                isActive: this.updataOrg.isActive,
                address: this.updataOrg.address,
                phone: this.updataOrg.phone,
                phone2: this.updataOrg.phone2,
                email: this.updataOrg.email,
                attnPhone: this.updataOrg.attnPhone,
                attnPhone2: this.updataOrg.attnPhone2,
                attnEmail: this.updataOrg.attnEmail
              }
              this.updateOrg(updataNew).then((res) => {
                statusCallback(res, function (that) {
                  that.getOrganization().then((res) => {
                    statusCallback(res)
                  })
                  that.updata_cancel()
                  that.$message({
                    message: '更新成功',
                    type: 'success'
                  })
                }, this)
              })
            }
          }
        })
      },
      updata_cancel (event) {
        this.showAllD = true
        this.dialogUpdateVisible = false
        this.dialogAddOrgVisible = false
      },
      add_clear () {
        this.showAllD = true
        this.dialogUpdateVisible = false
        this.dialogAddOrgVisible = false
        this.$refs['AddOData'].resetFields()
      },
      Add_cancel (formName) {
        this.$refs[formName].resetFields()
        this.showAllD = true
        this.dialogUpdateVisible = false
        this.dialogAddOrgVisible = false
        this.AddOData.file = ''
      },
      submitForm (formName) {
        this.$refs[formName].validate((valid) => {
          console.log(valid)
          if (valid) {
            if (this.AddOData.email === '') {
              this.AddOData.email = null
            }
            if (this.AddOData.attn_email === '') {
              this.AddOData.attn_email = null
            }
            if (this.AddOData.code !== '') {
              const orgData = {}
              orgData.code = this.AddOData.code
              orgData.name = this.AddOData.name
              orgData.phone = this.AddOData.phone
              orgData.phone2 = this.AddOData.phone2
              orgData.email = this.AddOData.email
              orgData.address = this.AddOData.address
              orgData.description = this.AddOData.description
              orgData.isActive = this.AddOData.isActive
              orgData.attn = this.AddOData.attn
              orgData.attnPhone = this.AddOData.attn_phone
              orgData.attnPhone2 = this.AddOData.attn_phone2
              orgData.attnEmail = this.AddOData.attn_email
              this.addOrg(orgData).then((res) => {
                statusCallback(res, function (that) {
                  that.getOrganization().then((res) => {
                    statusCallback(res)
                  })
                  that.add_clear()
                  that.$message({
                    message: '添加成功',
                    type: 'success'
                  })
                }, this)
              })
            } else {
              this.$alert('带*号必填', '错误', {
                confirmButtonText: '确定',
                type: 'error'
              })
            }
          }
        })
      },
      DeleteOrg (item) {
        if (item.user_isActive) {
          this.$confirm(`请先删除组织内用户`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
          return
        }
        this.$confirm(`此操作将删除${item.name}该组织机构, 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let id = {
            id: item.orgId
          }
          this.deleteOrg(id).then((res) => {
            statusCallback(res, function (that) {
              that.getOrganization().then((res) => {
                statusCallback(res)
              })
              that.$message({
                message: '删除成功',
                type: 'success'
              })
            }, this)
          })
        })
      },
      toPage (val) {
        this.param.current = val
        this.load()
      }
    },
    components: {
      'va-table': VATable,
      'page': Page
    }
  }
</script>

<style>
  #smallPic {
    width: 32px;
    height: 32px;
  }
  th {  white-space: nowrap }
  #bigImage {
    width: 100%;
    height: 100%;
  }
  .table-head{padding-right:17px;}
.table-head .table{ margin-bottom: 0 }
.table-body{width:100%;overflow-y:auto;}
.table-head table,.table-body table{width:100%;}
</style>

