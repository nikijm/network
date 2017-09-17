<template>
  <page title="规则映射">
    <div slot="content" >
    <div ref='scrollContent' style="width:100%;overflow: auto;">
      <el-form label-width="100px">
        <el-row>
          <el-col :span="9">
            <el-form-item label="源表类型">
              <el-select placeholder="选择源表类型" v-model="categoryType" @change="changeOptionsType">
                <el-option
                  v-for="item in optionsCategoryType"
                  :label="item.label"
                  :value="item.value"
                  >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="19">
            <el-form-item label="源表">
              <el-select placeholder="选择源表" v-model='tableDefsVal'>
                <el-option
                  v-for="item in tableDefsArr"
                  :label="item.tableName"
                  :value="item.etlTableId"
                  >
                </el-option>
              </el-select>
              <i class="el-icon-minus"></i>
              <label style="margin-right: 15px;">目标表</label>
              <el-select placeholder="选择目标表" v-model='targetTableDefsVal'>
                <el-option
                  v-for="item in targetTableDefsArr"
                  :label="item.tableName"
                  :value="item.etlTableId"
                  >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="规则列映射">
              <div>
                <div class="rule-box">
                  <ul>
                    <li v-for="item in ruleMapArr" @click="ruleMapList(item)" :class="item.state">{{item.name}}</li>
                  </ul>
                </div>
                <div style="float: left;margin-left: 20px;">
                  <p @click="addRuleMap"><i class="el-icon-plus"></i></p>
                  <p @click="delRuleMap"><i class="el-icon-minus"></i></p>
                </div>
                <div style="float: left;margin-left: 20px;">
                  <p @click="addRuleMap">增加规则映射</p>
                  <el-form>
                    <el-form-item label="源字段">
                      <el-col :span="6">
                        <el-select placeholder="选择源字段" v-model="sheetVal">
                          <el-option
                            v-for="item in optionSheet"
                            :label="item.columnName"
                            :value="item.etlColumnId"
                            >
                          </el-option>
                        </el-select>
                      </el-col>
                      <i class="el-icon-minus"></i>
                      <label style="margin-right: 15px;">目标字段</label>
                      <el-select placeholder="选择目标字段" v-model="targetSheetVal">
                        <el-option
                          v-for="item in optionTargetSheet"
                          :label="item.columnName"
                          :value="item.etlColumnId"
                          >
                        </el-option>
                      </el-select>
                    </el-form-item>
                    <el-form-item label="规则模板">
                      <el-select placeholder="选择规则模板" v-model="ruleTempVal">
                        <el-option
                          v-for="item in optionRuleTempArr"
                          :label="item.name"
                          :value="item.name">
                        </el-option>
                      </el-select>
                    </el-form-item>
                    <el-col :span="6">                    
                    <el-form-item label="序列号" class="xlh">
                      <el-input v-model="seqNo" placeholder="请输入序列号"></el-input>
                    </el-form-item>
                    </el-col>
                  </el-form>
                </div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="状态">
              <el-radio-group v-model="addRules.isActive">
                <el-radio :label = "'Y'">已启用</el-radio>
                <el-radio :label = "'N'">未启用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    </div>
    <div slot="footer">
      <div class="ml-10">
        <button class="btn btn-success btn-main" @click="saveRuleMap">保存</button>
      </div>
    </div>
  </page>
</template>
<script>
  import Page from '../../components/Page.vue'
  import { mapActions } from 'vuex'
  import * as jst from '../../lib/utils'
  export default {
    data () {
      return {
        addRules: {
          isActive: 'N'
        },
        seqNo: '',
        // 源表类型
        optionsCategoryType: [{
          value: 'cache',
          label: '缓存'
        },
        {
          value: 'clean',
          label: '清洗'
        },
        {
          value: 'convert',
          label: '转换'
        }],
        categoryType: '',
        // 目标表
        targetTableDefsArr: [],
        targetTableDefsVal: '',
        // 源表
        tableDefsArr: [],
        tableDefsVal: '',
        // 目标字段
        optionTargetSheet: [],
        targetSheetVal: '',
        // 源字段
        optionSheet: [],
        sheetVal: '',
        // 规则模板
        optionRuleTempArr: [],
        ruleTempVal: '',
        // 规则列映射组
        ruleMapArr: [],
        ruleTempMapArr: [],
        // 原字段id集合
        etlTableIdMapArr: [],
        // 目标字段id集合
        etlTableTargetIdMapArr: [],
        ruleUseColumnsList: [],
        screenHeight: document.body.clientHeight,
        // 主键ID
        mappedId: ''
      }
    },
    components: {
      'page': Page
    },
    mounted () {
      this.$refs.scrollContent.style.height = this.screenHeight - 190 + 'px'
      console.log(this.screenHeight - 340 + 'px')
    },
    methods: {
      ...mapActions([
        'getSourceSheetType',
        'getRuleUses',
        'updateRuleUses'
      ]),
          /**
       * 源表类型下拉选择
       */
      changeOptionsType () {
        let _this = this
        // 重置数据
        this.resetRuleData()
        this.getSourceSheetType({category: this.categoryType}).then((res) => {
          jst.statusCallback(res, function () {
            if (res && res.data) {
              _this.tableDefsArr = res.data.tableDefs // 源表
              _this.targetTableDefsArr = res.data.targetTableDefs  // 目标表
            }
          })
        })
      },
      /**
       * 重置数据
       */
      resetRuleData () {
        this.seqNo = ''
        this.tableDefsVal = ''
        this.targetTableDefsVal = ''
        this.sheetVal = ''
        this.targetSheetVal = ''
        this.ruleTempVal = ''
        this.ruleMapArr = []
        this.ruleTempMapArr = []
        this.optionSheet = []
        this.optionTargetSheet = []
        this.optionRuleTempArr = []
        this.ruleUseColumnsList = []
        this.etlTableIdMapArr = []
        this.etlTableTargetIdMapArr = []
        this.mappedId = ''
      },
      /**
       * 规则列内容处理
       */
      handleRule (tableDefsVal, targetTableDefsVal) {
        if (tableDefsVal && targetTableDefsVal) {
          let data = {
            etlTableId: tableDefsVal,
            etlTableTargetId: targetTableDefsVal,
            category: this.categoryType
          }
          let _this = this
          this.getRuleUses(data).then((res) => {
            jst.statusCallback(res, function () {
              if (res && res.data) {
                _this.optionRuleTempArr = res.data.ruleSuits // 规则模板
                // 填充规则列映射
                // res.data.ruleUses
                if (!jst.isNullOrEmpty(res.data.ruleUses, true)) {
                  let ruleSuitsArr = res.data.ruleUses
                  ruleSuitsArr.forEach(function (item) {
                    _this.ruleMapArr.push({
                      name: item.ruleSuit.name,
                      state: '',
                      id: item.ruleSuit.id,
                      etlColumnId: item.etlColumnId,
                      etlColumnTargetId: item.etlColumnTargetId,
                      rules: item.ruleSuit.rules
                    })
                    _this.ruleTempMapArr.push(item.ruleSuit.name)
                    _this.etlTableIdMapArr.push(item.ruleSuit.etlColumnId)
                    _this.etlTableTargetIdMapArr.push(item.ruleSuit.etlColumnTargetId)
                    _this.ruleUseColumnsList.push({
                      etlColumnId: item.etlColumnId,
                      etlColumnTargetId: item.etlColumnTargetId,
                      ruleSuitId: item.ruleSuit.id,
                      seqNo: item.seqNo,
                      id: item.id
                    })
                  })
                }
              }
            })
          })
        }
      },
      /**
       * 添加规则映射
       */
      addRuleMap () {
        let _this = this
        if (this.targetSheetVal && this.sheetVal && this.ruleTempVal) {
          if (!(jst.inArray(this.ruleTempVal, this.ruleTempMapArr) && jst.inArray(this.sheetVal, this.etlTableIdMapArr) && jst.inArray(this.targetSheetVal, this.etlTableTargetIdMapArr))) {
            this.ruleTempMapArr.push(this.ruleTempVal)
            this.etlTableIdMapArr.push(this.sheetVal)
            this.etlTableTargetIdMapArr.push(this.targetSheetVal)
            // 处理id
            let ruleSuitId = ''
            this.optionRuleTempArr.forEach(function (item) {
              if (item.name === _this.ruleTempVal) {
                ruleSuitId = item.id
              }
            })
            // 规则列映射的数据
            this.ruleMapArr.push({
              name: this.ruleTempVal,
              state: '',
              id: ruleSuitId
            })
            this.ruleUseColumnsList.push({
              etlColumnId: this.sheetVal, // 源字段id
              etlColumnTargetId: this.targetSheetVal, // 目标字段id
              ruleSuitId: ruleSuitId, // 规则模版主键
              seqNo: this.seqNo, // 序列号
              id: ''
            })
            console.log(this.ruleUseColumnsList)
            // 重置数据
            this.sheetVal = ''
            this.targetSheetVal = ''
            this.ruleTempVal = ''
          } else {
            this.$message.error('所选源字段和目标字段已添加过该模板【' + this.ruleTempVal + '】，不能重复添加')
          }
        } else {
          this.$message.error('请填写完整')
        }
      },
      /**
       * 删除规则列映射
       */
      delRuleMap () {
        let tempRuleMap = []
        let tempRuleUseCol = []
        let _this = this
        this.ruleUseColumnsList.forEach(function (item) {
          // 删除添加的
        })
        this.ruleMapArr.forEach(function (item) {
          if (!item.state) {
            // 没有选中
            tempRuleMap.push(item)
            _this.ruleUseColumnsList.forEach(function (i) {
              // 删除添加的
              if (item.id === i.ruleSuitId) {
                tempRuleUseCol.push(i)
              }
            })
          }
        })
        this.ruleUseColumnsList = tempRuleUseCol
        this.ruleMapArr = tempRuleMap
      },
      /**
       * 保存规则映射
       */
      saveRuleMap () {
        let data = {
          etlOp: this.categoryType,
          etlTableId: this.tableDefsVal,
          etlTableTargetId: this.targetTableDefsVal,
          ruleUseColumnsList: this.ruleUseColumnsList,
          isActive: this.addRules.isActive
        }
        let _this = this
        if (!jst.isNullOrEmpty(data.ruleUseColumnsList, true)) {
          this.updateRuleUses(data).then((res) => {
            jst.statusCallback(res, function () {
              _this.$message({
                message: '保存成功',
                type: 'success'
              })
              _this.resetRuleData()
            })
          })
        } else {
          _this.$message('数据还没填完整')
        }
      },
      /**
       * 选中某项规则列，设置当前规则项为选中状态，并回显对应的源字段、目标字段、规则模版
       */
      ruleMapList (currentItem) {
        // debugger
        this.ruleMapArr.map(function (item) {
          item.state = ''
        })
        currentItem.state = 'active'
        /* try {
          this.sheetVal = currentItem.etlColumnId
          this.targetSheetVal = currentItem.etlColumnTargetId
          this.ruleTempVal = currentItem.id
        } catch (e) {
          console.error(e)
        } */
      }
    },
    watch: {
      targetTableDefsVal (curVal, oldVal) {
        // 规则列内容
        let _this = this
        this.handleRule(this.tableDefsVal, this.targetTableDefsVal)
        // 填充目标字段
        this.targetTableDefsArr.forEach(function (item) {
          if (item.etlTableId === curVal) {
            _this.optionTargetSheet = item.columns
          }
        })
      },
      tableDefsVal (curVal, oldVal) {
        // 规则列内容
        let _this = this
        this.handleRule(this.tableDefsVal, this.targetTableDefsVal)
        // 填充源字段
        this.tableDefsArr.forEach(function (item) {
          if (item.etlTableId === curVal) {
            _this.optionSheet = item.columns
          }
        })
      }
    }
  }
</script>
<style>
  .rule-box {
    width: 150px;height: 200px;border:1px solid #6e6e6e;float: left;overflow-y: scroll;
  }
  .rule-box li.active {
    background-color: #40A7FF;
    color: #fff;
  }
  .xlh .el-form-item__content{
    display: -webkit-box
  }
  .xlh .el-input__inner{
    width: 150px;
  }
</style>
