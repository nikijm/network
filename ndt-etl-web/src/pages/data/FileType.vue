<template>
  <div>
    <page title="数据文件类型维护" v-if="showall">
      <div slot="tools">
        <el-select v-model="orgVal" placeholder="请选择组织机构" clearable filterable @change="filterOrg">
          <el-option
            v-for="item in orgOptions"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
        <button class="btn btn-primary" @click="openAddFileTypeDialog">添加</button>
      </div>
      <div slot="content">
        <table class="table table-hover table-fixed">
          <thead>
          <tr>
            <th>机构名</th>
            <th>类型名称</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="item in dataFileTypes">
            <td v-text='item.orgName'></td>
            <td v-text='item.name'></td>
            <td>
              <span v-if="item.isActive == 'Y'" class="glyphicon glyphicon-ok text-success"></span>
              <span v-else class="glyphicon glyphicon-remove text-danger"></span>
            </td>
            <td>
              <button class="btn btn-primary btn-xs" @click="openUpdateFileTypeDialog(item)">编辑</button>
              <span style="padding: 2px"></span>
              <button class="btn btn-danger btn-xs" @click="delFileType(item)">删除</button>
            </td>
          </tr>
          <tr v-if="jst.isNullOrEmpty(dataFileTypes, true)">
            <td colspan="4" align="center">暂时没有数据</td>
          </tr>
          </tbody>
        </table>
      </div>
    </page>
    <page :title="fileTitleState" v-if="showfile">
      <div slot="content">
      <div class="col-md-10">
        <el-form :model="addFile"  ref="addFile" label-width="100px">
         <!--  <el-row>
            <el-col :span="9"> -->
              <el-form-item class="col-md-6" label="机构名">
                <el-select placeholder="选择机构名" v-model="orgVal" style="width: 258.3px">
                  <el-option
                    v-for="item in orgOptions"
                    :label="item.name"
                    :value="item.id"
                    >
                  </el-option>
                </el-select>
              </el-form-item>
         <!--    </el-col>
          </el-row> -->
         <!--  <el-row>
            <el-col :span="9"> -->
              <el-form-item class="col-md-6" label="类型名称">
                <el-input v-model="addFile.name" :maxlength="12" :minlength="4"></el-input>
              </el-form-item>
          <!--   </el-col>
          </el-row> -->
          <!-- <el-row>
            <el-col :span="9"> -->
              <el-form-item class="col-md-6" label="说明">
                <el-input v-model="addFile.description"></el-input>
              </el-form-item>
         <!--    </el-col>
          </el-row> -->
         <!--  <el-row>
            <el-col :span="9"> -->
              <el-form-item class="col-md-6" label="存入后缓存表">
                <el-select placeholder="选择缓存表" v-model="valCacheSheet" style="width: 258.3px">
                  <el-option
                    v-for="item in optionCacheSheet"
                    :label="item.tableName"
                    :value="item.etlTableId"
                    >
                  </el-option>
                </el-select>
              </el-form-item>
        <!--     </el-col>
          </el-row> -->
          <!-- <el-row>
            <el-col :span="9"> -->
              <el-form-item class="col-md-6" label="字段对齐">
                <el-input type="textarea" v-model="addFile.fieldAlign" autosize :autosize="{ minRows: 4, maxRows: 4}" ></el-input>
                <div style="text-align: right;margin-top: 10px;" v-if="this.step === 'add'">
                  <button class="btn btn-primary" @click="fieldAlign()">生成缺省</button>
                </div>
              </el-form-item>
           <!--  </el-col>
          </el-row> -->
        <!--   <el-row>
            <el-col :span="9"> -->
              <el-form-item class="col-md-6" label="清洗后存入表">
                <el-select placeholder="选择清洗表" v-model="valCleanSheet" style="width: 258.3px">
                  <el-option
                    v-for="item in optionCleanSheet"
                    :label="item.tableName"
                    :value="item.etlTableId"
                    >
                  </el-option>
                </el-select>
              </el-form-item>
          <!--   </el-col>
          </el-row> -->
         <!--  <el-row>
            <el-col :span="9"> -->
              <el-form-item class="col-md-6" label="转换后存入表">
                <el-select placeholder="选择转换表" v-model="valTransformSheet" style="width: 258.3px">
                  <el-option
                    v-for="item in optionTransformSheet"
                    :label="item.tableName"
                    :value="item.etlTableId"
                    >
                  </el-option>
                </el-select>
              </el-form-item>
         <!--    </el-col>
          </el-row> -->
          <!-- <el-row>
            <el-col :span="9"> -->
            <el-form-item class="col-md-6" label="状态">
                <el-radio-group v-model="addFile.isActive">
                  <el-radio :label="'Y'">已启用</el-radio>
                  <el-radio :label="'N'">未启用</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item class="col-md-6" label="校验后存入表">
                <el-select placeholder="选择校验表" v-model="valVerifySheet" style="width: 258.3px">
                  <el-option
                    v-for="item in optionVerifySheet"
                    :label="item.tableName"
                    :value="item.etlTableId"
                    >
                  </el-option>
                </el-select>
              </el-form-item>
       <!--      </el-col>
          </el-row> -->
          <!-- <el-row>
            <el-col> -->
              
          <!--   </el-col>
          </el-row> -->
        </el-form>
        </div>
      </div>
      <div slot="footer">
        <div class="ml-10">
          <button class="btn btn-success btn-main" @click="handleFileType()">确定</button>
          <button class="btn btn-default" @click="closeDialog()">返回</button>
        </div>
      </div>
    </page>
  </div>
</template>

<script>
import Page from '../../components/Page.vue'
import { mapActions } from 'vuex'
import * as jst from '../../lib/utils'
export default {
  data () {
    return {
      position: 'top',
      jst: jst,
      fileTitleState: '',
      step: '',
      showfile: false,
      showall: true,
      orgSelectVal: '',
      orgSelectOptions: [], // 筛选组织机构
      orgVal: '',
      orgOptions: [], // 组织机构
      valCacheSheet: '',
      optionCacheSheet: [], // 缓存表
      valCleanSheet: '',
      optionCleanSheet: [], // 清洗表
      valTransformSheet: '',
      optionTransformSheet: [], // 转换表
      valVerifySheet: '',
      optionVerifySheet: [], // 验证表
      addFile: {
        name: '',
        isActive: 'Y',
        fieldAlign: ''
      },
      dataFileTypes: []
    }
  },
  mounted () {
    this.getAllFileTypes()
  },
  methods: {
    ...mapActions([
      'getDataFileTypes',
      'getFieldAlign',
      'addDataFileType',
      'updateDataFileType',
      'deleteDataFileType'
    ]),
    /**
     * 打开新增对话框
     */
    openAddFileTypeDialog () {
      this.fileTitleState = '数据文件类型添加'
      this.valCacheSheet = ''
      this.valCleanSheet = ''
      this.valTransformSheet = ''
      this.valVerifySheet = ''
      this.addFile.isActive = 'Y'
      this.addFile.fieldAlign = ''
      this.addFile.name = ''
      this.addFile.description = ''
      this.orgVal = ''
      this.showfile = true
      this.showall = false
      this.step = 'add'
    },
    /**
     * 打开修改对话框update
     */
    openUpdateFileTypeDialog (item) {
      this.fileTitleState = '数据文件类型更新'
      this.valCacheSheet = item.tableCacheName
      this.valCleanSheet = item.tableCleanName
      this.valTransformSheet = item.tableConvertName
      this.valVerifySheet = item.tableValidateName
      this.addFile.isActive = item.isActive
      this.addFile.fieldAlign = item.fieldsAlign
      this.addFile.name = item.name
      this.addFile.description = item.description
      this.addFile.etlFileTypeId = item.etlFileTypeId
      this.addFile.etlOrgId = item.etlOrgId
      this.addFile.tableCacheId = item.tableCacheId
      this.addFile.tableCleanId = item.tableCleanId
      this.addFile.tableConvertId = item.tableConvertId
      this.addFile.tableValidateId = item.tableValidateId
      this.orgVal = item.orgName
      this.showfile = true
      this.showall = false
      this.step = 'update'
      // update
    },
    closeDialog () {
      this.showfile = false
      this.showall = true
      this.orgVal = ''
    },
    /**
     * 获取所有数据文件类型
     */
    getAllFileTypes (param) {
      this.getDataFileTypes(param).then((res) => {
        let _this = this
        jst.statusCallback(res, function () {
          if (res && res.data) {
            _this.dataFileTypes = res.data.dataFileTypes
            _this.orgOptions = res.data.orgs
            _this.optionCacheSheet = res.data.cacheTables
            _this.optionCleanSheet = res.data.cleanTables
            _this.optionTransformSheet = res.data.convertTables
            _this.optionVerifySheet = res.data.validateTables
          }
        })
      })
    },
    /**
     * 添加或修改数据文件类型
     */
    handleFileType () {
      let _this = this
      let data = {
        etlOrgId: this.orgVal,
        name: this.addFile.name,
        description: this.addFile.description,
        tableCacheId: this.valCacheSheet,
        tableCleanId: this.valCleanSheet,
        tableConvertId: this.valTransformSheet,
        tableValidateId: this.valVerifySheet,
        isActive: this.addFile.isActive,
        fieldsAlign: this.addFile.fieldAlign
      }
      // 数据验证
      // 具体操作
      if (this.step === 'add') {
        // add
        this.addDataFileType(data).then((res) => {
          jst.statusCallback(res, function () {
            if (res && res.data) {
              // ok
              _this.$message({
                message: '添加成功',
                type: 'success'
              })
              _this.getAllFileTypes()
              _this.closeDialog()
            }
          })
        })
      } else if (this.step === 'update') {
        // updata
        data.etlFileTypeId = this.addFile.etlFileTypeId
        data.etlOrgId = this.addFile.etlOrgId
        data.tableCacheId = this.addFile.tableCacheId
        data.tableCleanId = this.addFile.tableCleanId
        data.tableConvertId = this.addFile.tableConvertId
        data.tableValidateId = this.addFile.tableValidateId
        this.updateDataFileType(data).then((res) => {
          jst.statusCallback(res, function () {
            jst.statusCallback(res, function () {
              _this.$message({
                message: '添加成功',
                type: 'success'
              })
              _this.getAllFileTypes()
              _this.closeDialog()
            })
          })
        })
      }
    },
    /**
     * 删除数据文件类型
     */
    delFileType (item) {
      let _this = this
      this.$confirm('此操作将删除该数据文件类型, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // del
        _this.deleteDataFileType({id: item.etlFileTypeId}).then((res) => {
          jst.statusCallback(res, function () {
            _this.$message({
              message: '删除成功',
              type: 'success'
            })
            _this.getAllFileTypes()
          })
        })
      }).catch(() => {
        // 取消操作，不做任何响应操作
      })
    },
    /**
     * 按照组织机构筛选内容
     */
    filterOrg () {
      this.getAllFileTypes({orgId: this.orgVal})
    },
    /**
     * 获取字段对齐（生成缺省）
     */
    fieldAlign () {
      let id = this.valCacheSheet
      if (!id) {
        return
      }
      let _this = this
      this.getFieldAlign({id: id}).then((res) => {
        // 获取成功的处理
        jst.statusCallback(res, function () {
          if (res.data.defaultFieldsAlign) {
            // console.log(res.data.defaultFieldsAlign)
            _this.addFile.fieldAlign = res.data.defaultFieldsAlign
          }
        })
      })
    }
  },
  components: {
    'page': Page
  }
}
</script>
<style scoped>
  .el-form-item{
    margin-bottom: 18px;
  }
</style>>
