<template>
  <page title="业务类型" :isLoading="isLoading">
    <div slot="tools">
      <button class="btn btn-primary" @click="openAddTypeDialog">添加</button>
    </div>
    <div slot="content">
      <table class="table table-hover">
        <thead>
        <tr>
          <th width="10%">序号</th>
          <th width="60%">类型名称</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
          <tr v-if="jst.isNullOrEmpty(businessTypesRes, true)">
            <td colspan="3" align="center">没有数据</td>
          </tr>
          <tr v-else v-for="(item, index) in businessTypesRes">
            <td>{{ index + 1 }}</td>
            <td>{{ item.name }}</td>
            <td>
              <a class="btn btn-primary btn-xs" href="javascript:;" @click="openUpdateTypeDialog(item)">编辑</a>
              <span style="padding: 2px"></span>
              <a class="btn btn-danger btn-xs" href="javascript:;" @click="delType(item)">删除</a></td>
          </tr>
        </tbody>
      </table>
      <el-dialog :title="dialogTitle === '1' ? '新增' : '编辑'" :visible.sync="dialogFormVisible" top="20%" :show-close="false" :close-on-click-modal="false">
        <el-form :model="formData" :rules="rules" ref="formData">
          <el-form-item label="类型名称" prop="name">
            <el-input v-model="formData.name" auto-complete="off" :maxlength="12" :minlength="4"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <button class="btn btn-success btn-main" @click="typeAction('formData')">确 定</button>
          <button class="btn btn-default" @click="closeDialog('formData')">取 消</button>
        </div>
      </el-dialog>
    </div>
  </page>
</template>
<script>
import Page from '../../components/Page.vue'
import { mapActions } from 'vuex'
import * as jst from '../../lib/utils'
import * as config from '../../lib/global'
export default {
  data () {
    var checkName = (rule, value, callback) => {
      if (!config.NAME_PATTERN.test(value)) {
        callback(new Error('输入的格式有误'))
      } else {
        callback()
      }
    }
    return {
      jst: jst,
      isLoading: false,
      businessTypesRes: [],
      dialogFormVisible: false,
      dialogTitle: '',
      formData: {
        name: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入业务类型名称', trigger: 'blur' },
          { min: 4, max: 12, message: '长度在 4 到 12 个字符', trigger: 'blur' },
          { required: true, trigger: 'blur', validator: checkName }
        ]
      }
    }
  },
  components: {
    'page': Page
  },
  mounted () {
    this.getAllTypes()
  },
  methods: {
    ...mapActions([
      'getBusinessTypes',
      'addBusinessTypes',
      'updateBusinessTypes',
      'delBusinessTypes'
    ]),

    /**
     *  获取所有类型数据
     */
    getAllTypes () {
      this.getBusinessTypes().then((res) => {
        let _this = this
        jst.statusCallback(res, function () {
          if (res && res.data) {
            _this.businessTypesRes = res.data.businessTypes
          }
        })
      })
    },

    /**
     * 打开新增对话框
     */
    openAddTypeDialog () {
      this.formData.name = ''
      this.dialogTitle = '1'
      this.dialogFormVisible = true
    },
    /**
     * 打开修改对话框
     */
    openUpdateTypeDialog (item) {
      this.dialogTitle = '2'
      this.formData.name = item.name
      this.formData.id = item.etlBusinessTypeId
      this.dialogFormVisible = true
    },
    /**
     * 新增或修改类型名称
     */
    typeAction (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let data = null
          let _this = this
          if (this.dialogTitle === '1') {
            // 新增
            data = {
              name: this.formData.name
            }
            this.addBusinessTypes(data).then((res) => {
              jst.statusCallback(res, function () {
                _this.getAllTypes()
                _this.$message({
                  message: '添加成功',
                  type: 'success'
                })
                _this.dialogFormVisible = false
              })
            })
          } else if (this.dialogTitle === '2') {
            // 修改
            data = {
              name: this.formData.name,
              etlBusinessTypeId: this.formData.id
            }
            this.updateBusinessTypes(data).then((res) => {
              jst.statusCallback(res, function () {
                _this.getAllTypes()
                _this.$message({
                  message: '修改成功',
                  type: 'success'
                })
                _this.dialogFormVisible = false
              })
            })
          }
        }
      })
    },
    /**
     * 删除类型
     */
    delType (item) {
      let _this = this
      this.$confirm('此操作将删除该业务类型, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let data = {
          etlBusinessTypeId: item.etlBusinessTypeId
        }
        _this.delBusinessTypes(data).then((res) => {
          // 删除后再次获取所有业务类型列表
          jst.statusCallback(res, function () {
            _this.getAllTypes()
            _this.$message({
              type: 'success',
              message: '删除成功!'
            })
          })
        })
      }).catch(() => {
        // 取消操作，不做任何响应操作
      })
    },

    closeDialog (formName) {
      this.dialogFormVisible = false
      let _this = this
      setTimeout(function () {
        _this.$refs[formName].resetFields()
      }, 500)
    }
  }
}
</script>
