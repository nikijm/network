<template>
  <page class="tableMaintenance">
    <div slot="content">
      <div class="row">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="缓存数据表" name="cache"></el-tab-pane>
          <el-tab-pane label="清洗数据表" name="clean"></el-tab-pane>
          <el-tab-pane label="转换数据表" name="convert"></el-tab-pane>
          <el-tab-pane label="校验数据表" name="verify"></el-tab-pane>
        </el-tabs>
      </div>  
      <div class="row rowContent" ref="row">
        <div class="col-md-12">
          <div class="col-md-2">
            <button class="btn btn-primary btn-main" style="width:100%" @click="showDialog('table')">添加数据表</button>
            <el-table style="width:100%" :data="fetchTableMaintenceData.tableDefs" @cell-click="showDataSheet">
              <el-table-column prop="name" label="数据表"></el-table-column>
            </el-table>
          </div>
          <div class="col-md-8">
            <el-form ref="form1" :model="form1" label-width="100px">
              <el-form-item class="col-md-6"label="业务类型">
                <el-select  placeholder="选择业务类型" v-model="businessTypeValue" disabled>
                  <el-option
                    v-for="item in businessTypeName"
                    :label="item.name"
                    :value="item.etlBusinessTypeId">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item class="col-md-6" label="显示名称">
                <el-input v-model="tabledetail.name" disabled></el-input>
              </el-form-item>
              <el-form-item class="col-md-6" label="物理表名">
                <el-input v-model="tabledetail.tableName" disabled></el-input>
              </el-form-item>
              <el-form-item class="col-md-6" label="是否启用">
                <el-switch on-text="启用" off-text="不启用" on-value="Y" off-value="N" v-model="isUseState" disabled></el-switch>
              </el-form-item>
              
              <el-form-item class="col-md-6" label="说明">
                <el-input v-model="tabledetail.description" disabled></el-input>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <div style="clear:both"></div>
        <hr style="height:1px;border:none;border-top:1px solid #C9C9C9;"/>

        <div class="col-md-12">
          <div class="col-md-2">
            <button class="btn btn-primary btn-main" style="width:100%" @click="showDialog('filed')">添加字段</button>
            <el-table style="width:100%" max-height="300" :data="tabledetail.columns" @cell-click="showSheetField">
              <el-table-column
                prop="name"
                label="字段">
              </el-table-column>
            </el-table>
          </div>
          <div class="col-md-8">
            <el-form ref="form2" :model="form2" label-width="100px">
              <el-form-item class="col-md-6" label="显示名称">
                <el-input v-model="dataTable.name" :readonly="true" disabled></el-input>
              </el-form-item>
              <el-form-item class="col-md-6" label="物理列名">
                <el-input v-model="dataTable.columnName" :readonly="true" disabled></el-input>
              </el-form-item>
              <el-form-item class="col-md-6" label="数据类型">
                <el-input v-model="dataTable.dataType" :readonly="true" disabled></el-input>
              </el-form-item>
              <el-form-item class="col-md-6" label="数据长度">
                <el-input v-model="dataTable.fieldLength" :readonly="true" disabled></el-input>
              </el-form-item>
              <el-form-item class="col-md-6" label="是否启用">
                <el-switch on-text="启用" off-text="不启用" on-value="Y" off-value="N" v-model="isUseState1" disabled></el-switch>
              </el-form-item>
              <el-form-item class="col-md-6" label="是否模糊匹配">
                <el-switch on-text="启用" off-text="不启用" on-value="Y" off-value="N" v-model="isUseState2" disabled></el-switch>
              </el-form-item>
              <el-form-item class="col-md-6" label="序号">
                <el-input v-model="dataTable.seqNo" :readonly="false"></el-input>
              </el-form-item>
              <el-form-item class="col-md-6" label="说明">
                <el-input v-model="dataTable.description" disabled></el-input>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
        <el-dialog :title="this.name" :visible.sync="dialogFormVisible">
          <el-form :model="addform" label-width="100px" :rules="rules" ref="ruleForm">
            <el-form-item class="col-md-6" label="业务类型" v-if="this.type == 'table'" prop="businessTypeValue">
              <el-select placeholder="选择业务类型" v-model="addform.businessTypeValue">
                <el-option
                  v-for="item in businessTypeName"
                  :key="item.etlBusinessTypeId"
                  :label="item.name"
                  :value="item.etlBusinessTypeId">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item class="col-md-6" label="显示名称" v-if="this.type == 'table'" prop="name">
              <el-input v-model="addform.name"></el-input>
            </el-form-item>
            <el-form-item class="col-md-6" label="物理表名" v-if="this.type == 'table'" prop="tableName">
              <el-input v-model="addform.tableName"></el-input>
            </el-form-item>
            <el-form-item class="col-md-6" label="表说明" v-if="this.type == 'table'">
              <el-input v-model="addform.description"></el-input>
            </el-form-item>
            <hr style="height:1px;border:none;border-top:1px solid #C9C9C9;" v-if="this.type == 'table'"/>
            <div class="col-md-12">
              <div class="col-md-3">
                <button class="btn btn-primary btn-main" style="width:100%" type="button" @click="addTempField" >添加</button>
                <el-table style="width:100%" max-height="300" :data="addTableData.columns" @cell-click="showAddField">
                  <el-table-column prop="name" label="字段"></el-table-column>
                </el-table>
              </div>
              <div class="col-md-9">
                <el-form-item class="col-md-6" label="字段名称">
                  <el-input v-model="addform.fieldNick"></el-input>
                </el-form-item>
                <el-form-item class="col-md-6" label="物理列名">
                  <el-input v-model="addform.fieldName"></el-input>
                </el-form-item>
                <el-form-item class="col-md-6" label="数据类型">
                  <el-input v-model="addform.fieldDataType"></el-input>
                </el-form-item>
                <el-form-item class="col-md-6" label="数据长度">
                  <el-input v-model="addform.fieldLength"></el-input>
                </el-form-item>
                <el-form-item class="col-md-6" label="字段序号">
                  <el-input v-model="addform.fieldSeqNo"></el-input>
                </el-form-item>
                <el-form-item class="col-md-6" label="字段说明">
                  <el-input v-model="addform.fieldDescription"></el-input>
                </el-form-item>
                <el-form-item class="col-md-6" label="创建清洗表" v-if="activeName=='cache' && name=='添加数据表'">
                  <el-checkbox-group v-model="addform.createCleanTable">
                    <el-checkbox name="createCleanTable"></el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
                <el-form-item class="col-md-6" label="创建校验表" v-if="activeName=='convert' && name=='添加数据表'">
                  <el-checkbox-group v-model="addform.createVerifyTable">
                    <el-checkbox name="createVerifyTable"></el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </div>
            </div>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="cancel">取 消</el-button>
            <el-button @click="resetForm">重置</el-button>
            <el-button type="primary" @click="postAddTable">确 定</el-button>
          </div>
        </el-dialog>
    </div>
    <div slot="footer" style="text-align:center" class="col-md-8" v-if='modifyShow'>
      <button class="btn btn-default">返回</button>
      <button class="btn btn-success" @click="modify">修改</button>
    </div>
  </page>
</template>

<script>
  import Page from '../../components/Page.vue'
  import VATable from '../../components/VATable.vue'
  import { mapGetters, mapActions } from 'vuex'
  import { services } from '../../vuex/api'
  export default {
    data () {
      return {
        modifyShow: false,
        screenHeight: document.body.clientHeight,
        dataTable: [],
        dialogFormVisible: false,
        businessTypeName: [],
        businessTypeValue: '',
        addTableData: {
          name: '',
          tableName: '',
          description: '',
          category: '',
          columns: []
        },
        isUseState: false,
        isUseState1: false,
        isUseState2: false,
        addform: {
          name: '',
          tableName: '',
          businessTypeValue: '',
          description: '',
          fieldNick: '',
          fieldName: '',
          fieldDataType: '',
          fieldLength: '',
          fieldSeqNo: '',
          fieldDescription: '',
          createCleanTable: true,
          createVerifyTable: true
        },
        addFieldData: {
          tableName: '',
          columns: [],
          etlTableId: ''
        },
        type: '',
        name: '',
        form1: {},
        form2: {},
        tabledetail: {},
        activeName: 'cache',
        rules: {
          name: [
            { required: true, message: '请输入表的别名', trigger: 'blur' }
          ],
          tableName: [
            { required: true, message: '请输入表的物理名称', trigger: 'blur' }
          ],
          businessTypeValue: [
            { required: true, message: '请选择业务类型', trigger: 'change' }
          ],
          fieldNick: [
            { required: true, message: '请输入字段的别名', trigger: 'blur' }
          ],
          fieldName: [
            { required: true, message: '请输入字段的物理名称', trigger: 'blur' }
          ],
          fieldDataType: [
            { required: true, message: '请输入字段的数据类型，如char', trigger: 'blur' }
          ],
          fieldLength: [
            { required: true, message: '请输入字段的长度', trigger: 'blur' }
          ],
          fieldSeqNo: [
            { required: true, message: '请输入字段的长度', trigger: 'blur' }
          ]
        }
      }
    },
    created () {
      this.getTaleMainten('cache')
      this.getBusinessList().then((res) => {
        this.businessTypeName = res.data.tableDefs
        this.businessTypeName = this.businessTypeName.map(item => {
          return {
            etlBusinessTypeId: item.businessTypeId + '',
            name: item.name
          }
        })
        /* console.log(this.businessTypeName)
        this.businessTypeName = [
          {
            etlBusinessTypeId: 28,
            name: '个人信息'
          }
        ] */
      })
    },
    mounted () {
      this.$refs.row.style.height = this.screenHeight - 240 + 'px'
    },
    methods: {
      ...mapActions([
        'FETCH_TABLEMAINTENANCE',
        'getBusinessList'
      ]),
      /**
       * 打开添加数据或字段对话框
       */
      showDialog (msg) {
        // 重置数据
        this.dialogFormVisible = true
        this.addTableData.columns = []
        this.addform.name = ''
        this.addform.tableName = ''
        this.addform.description = ''
        this.addform.fieldNick = ''
        this.addform.fieldName = ''
        this.addform.fieldDataType = ''
        this.addform.fieldLength = ''
        this.addform.fieldSeqNo = ''
        this.addform.fieldDescription = ''
        this.addform.createCleanTable = true
        this.addform.createVerifyTable = true
        this.type = msg
        if (msg === 'table') {
          this.name = '添加数据表'
        } else {
          this.name = '添加字段'
        }
      },
      /**
       * 点击数据表名展示表的属性
       */
      showDataSheet (val) {
        this.tabledetail = JSON.parse(JSON.stringify(val))
        console.log(this.tabledetail)
        this.isUseState = this.tabledetail.isActive ? this.tabledetail.isActive : 'N'
        this.businessTypeValue = this.tabledetail.businessTypeId
        // 对columns进行排序
        let temp = ''
        let arr = this.tabledetail.columns
        for (let i = 0; i < arr.length - 1; i++) {
          for (let j = arr.length - 1; j > i; j--) {
            if (arr[j].seqNo < arr[j - 1].seqNo) {
              temp = arr[j]
              arr[j] = arr[j - 1]
              arr[j - 1] = temp
            }
          }
        }
        this.tabledetail.columns = arr
      },
      /**
       * 点击表中字段展示字段的属性
       */
      showSheetField (val) {
        this.dataTable = val
        // 处理switch开关
        this.isUseState1 = this.dataTable.isActive ? this.dataTable.isActive : 'N'
        this.isUseState2 = this.dataTable.isSearch ? this.dataTable.isSearch : 'N'
      },
      /**
       * 添加字段对话框中点击字段名展示相应的数据
       */
      showAddField (val) {
        for (var i = 0; i < this.addTableData.columns.length; i++) {
          if (this.addTableData.columns[i].name === val.name) {
            this.addform.fieldNick = this.addTableData.columns[i].name
            this.addform.fieldName = this.addTableData.columns[i].columnName
            this.addform.fieldDataType = this.addTableData.columns[i].dataType
            this.addform.fieldLength = this.addTableData.columns[i].fieldLength
            this.addform.fieldSeqNo = this.addTableData.columns[i].seqNo
            this.addform.fieldDescription = this.addTableData.columns[i].description
          }
        }
      },
      /**
       * 添加数据表字段
       */
      addTempField () {
        var isError = 0
        if (this.addTableData.columns.length > 0) {
          for (var i = 0; i < this.addTableData.columns.length; i++) {
            if (this.addform.fieldNick === this.addTableData.columns[i].name) {
              isError = 1
            }
            if (this.addform.fieldName === this.addTableData.columns[i].columnName) {
              isError = 2
            }
            if (this.addform.fieldNick === '') {
              isError = 3
            }
            if (this.addform.fieldName === '') {
              isError = 4
            }
            if (this.addform.fieldLength === '') {
              isError = 5
            }
            if (this.addform.fieldDataType === '') {
              isError = 6
            }
          }
        } else {
          if (this.addform.fieldNick === '') {
            isError = 3
          }
          if (this.addform.fieldName === '') {
            isError = 4
          }
          if (this.addform.fieldLength === '') {
            isError = 5
          }
          if (this.addform.fieldDataType === '') {
            isError = 6
          }
        }
        switch (isError) {
          case 0: this.addTableData.columns.push({
            name: this.addform.fieldNick,
            columnName: this.addform.fieldName,
            description: this.addform.fieldDescription,
            fieldLength: this.addform.fieldLength,
            dataType: this.addform.fieldDataType,
            seqNo: this.addform.fieldSeqNo
          })
            break
          case 1: this.$alert('字段名称不能相同！！！', '错误', {
            confirmButtonText: '确定',
            type: 'error'
          })
            break
          case 2: this.$alert('物理列名不能相同！！！', '错误', {
            confirmButtonText: '确定',
            type: 'error'
          })
            break
          case 3: this.$alert('字段名称不能为空！！！', '错误', {
            confirmButtonText: '确定',
            type: 'error'
          })
            break
          case 4: this.$alert('物理列名不能为空！！！', '错误', {
            confirmButtonText: '确定',
            type: 'error'
          })
            break
          case 5: this.$alert('数据长度不能为空！！！', '错误', {
            confirmButtonText: '确定',
            type: 'error'
          })
            break
          case 6: this.$alert('数据类型不能为空！！！', '错误', {
            confirmButtonText: '确定',
            type: 'error'
          })
            break
        }
        this.addform.fieldNick = ''
        this.addform.fieldName = ''
        this.addform.fieldDescription = ''
        this.addform.fieldLength = ''
        this.addform.fieldDataType = ''
        this.addform.fieldSeqNo = ''
      },
      cancel () {
        this.dialogFormVisible = false
      },
      postAddTable () {
        this.$refs['ruleForm'].validate((valid) => {
          if (valid) {
            if (this.type === 'table') {
              // 添加数据维护表
              this.addTableData.category = this.activeName
              this.addTableData.name = this.addform.name
              this.addTableData.tableName = this.addform.tableName
              this.addTableData.etlBusinessTypeId = this.addform.businessTypeValue
              this.addTableData.description = this.addform.description
              this.addTableData.createCleanTable = this.addform.createCleanTable
              this.addTableData.createVerifyTable = this.addform.createVerifyTable
              services.tableMaintenance.postTable(this.addTableData).then((res) => {
                this.dialogFormVisible = false
                this.getTaleMainten(this.activeName)
              }).catch((error) => {
                this.$alert(error.response.data.message, '错误', {
                  confirmButtonText: '确定',
                  type: 'error'
                })
              })
            } else {
              // 添加字段
              if (!this.tabledetail.etlTableId) {
                this.$message.error('请先选一张表')
                return
              }
              this.addFieldData.category = this.activeName
              this.addFieldData.tableName = this.tabledetail.name
              this.addFieldData.columns = this.addTableData.columns
              this.addFieldData.etlTableId = this.tabledetail.etlTableId
              this.addFieldData.etlBusinessTypeId = this.tabledetail.businessType.etlBusinessTypeId
              services.tableMaintenance.putField(this.addFieldData).then((res) => {
                this.dialogFormVisible = false
                this.getTaleMainten(this.activeName)
              }).catch((error) => {
                this.$alert(error.response.data.message, '错误', {
                  confirmButtonText: '确定',
                  type: 'error'
                })
              })
            }
          } else {
            return false
          }
        })
      },
      /**
       * Tabs点击切换加载数据表
       */
      handleClick (tab, event) {
        this.getTaleMainten(this.activeName)
      },
      getTaleMainten (category) {
        let _this = this
        this.FETCH_TABLEMAINTENANCE(category).then((res) => {
          // 把字段的内容值空
          _this.tabledetail.columns = []
          _this.tabledetail = {}
          _this.isUseState = 'N'
          _this.businessTypeValue = ''
        })
      },
      // 修改
      modify () {
        var changedTableData = {}
        changedTableData.name = this.tabledetail.name
        changedTableData.tableName = this.tabledetail.tableName
        changedTableData.description = this.tabledetail.description
        changedTableData.isActive = this.isUseState
        changedTableData.columns = {}
        changedTableData.columns.columnName = this.dataTable.name
        changedTableData.columns.isActive = this.isUseState1
        changedTableData.columns.isSearch = this.isUseState2
        // services.tableMaintenance.modify()
        // .then((res) => {
        //   console.log(res)
        // })
      },
      resetForm () {
        this.$refs['ruleForm'].resetFields()
        this.addform.name = ''
        this.addform.tableName = ''
        this.addform.description = ''
        this.addform.fieldNick = ''
        this.addform.fieldName = ''
        this.addform.fieldDataType = ''
        this.addform.fieldLength = ''
        this.addform.fieldSeqNo = ''
        this.addform.fieldDescription = ''
        this.addform.createCleanTable = true
        this.addform.createVerifyTable = true
      }
    },
    components: {
      'page': Page,
      'va-table': VATable
    },
    computed: {
      ...mapGetters([
        'fetchTableMaintenceData'
      ])
    }
  }
</script>
<style>
  .tableMaintenance .el-form-item {
    margin-top: 0px;
    margin-bottom: 18px;
  }
  .tableMaintenance .rowContent{
    overflow: auto;
  }
  .tableMaintenance .box-header{
    padding: 0px;
    border: none;
    height: 0px;
  }
  .tableMaintenance .el-select >.el-input.is-disabled .el-input__inner{
    color: #bbb;
  }
</style>
