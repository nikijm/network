<template>
  <page :title="idNum===0?title:title1" :isLoading="false">
    <div slot="content">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
        <el-row>
          <el-col :span="20">
            <el-form-item label="规则名称" prop="name">
              <el-input v-model="ruleForm.name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <div style='display:flex'>
          <el-col :span="15">
            <el-form-item label="邮箱" prop="alarmEmail">
              <el-input v-model="ruleForm.alarmEmail"></el-input>
            </el-form-item>
          </el-col>
          <el-form-item label="" prop="alarmEmail2" style='margin-left:-60px'>
            <el-select v-model="ruleForm.alarmEmail2" placeholder="请选择资源类型">
              <el-option label="@163.com" value="@163.com"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <el-form-item label="适用机器" prop="machineType" v-if='idNum === 0'>
          <el-checkbox-group v-model="ruleForm.machineType">
            <el-checkbox v-for='item in totalRules2Machine2' :label='item.id' name="machineType" >{{item.description}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="适用机器" prop="machineType2" v-if='idNum !== 0'>
          <div style='width:100%;padding-right:15px;word-break: break-word;'>
            {{ruleForm.machineType2}}
          </div>
        </el-form-item>
        <div style='display:flex'>
          <el-form-item label="规则设置" prop="type">
            <el-select v-model="ruleForm.type" placeholder="请选择资源类型" @change='resetJudge' :disabled='idNum !== 0'>
              <el-option label="CPU" value="1"></el-option>
              <el-option label="内存" value="2"></el-option>
              <el-option label="磁盘" value="3"></el-option>
              <el-option label="日志" value="4"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="" prop="expressionType" style='margin-left:-60px'>
            <el-select v-model="ruleForm.expressionType" placeholder="请选择判断方式" :disabled='idNum !== 0'>
              <el-option label="大于" value="1" v-show='ruleForm.type==="1"||ruleForm.type==="2"||ruleForm.type==="3"||ruleForm.type===""'></el-option>
              <!--<el-option label="小于" value="2" v-show='ruleForm.type==="1"||ruleForm.type==="2"||ruleForm.type==="3"||ruleForm.type===""'></el-option>-->
              <el-option label="包含" value="3" v-show='ruleForm.type==="4"||ruleForm.type===""'></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="" prop="content" style='margin-left:-60px'>
            <el-col :span="20">
              <el-input v-model="ruleForm.content" placeholder='请填写判断数据'></el-input>
            </el-col>
          </el-form-item>
        </div>
        <el-form-item label="是否运行" prop="isUse">
          <el-radio-group v-model="ruleForm.isUse">
            <el-radio label="1">是</el-radio>
            <el-radio label="2">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="触发间隔" prop="triggerInterval">
          <el-select v-model="ruleForm.triggerInterval" placeholder="请选择触发间隔">
            <el-option label="30分钟" value="1"></el-option>
            <el-option label="1小时" value="2"></el-option>
            <el-option label="2小时" value="3"></el-option>
            <el-option label="3小时" value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="descriptionRule">
          <el-row>
            <el-col :span="12">
              <el-input type="textarea" v-model="ruleForm.descriptionRule"></el-input>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>
    <div slot="footer">
      <div class="ml-10">
        <button class="btn btn-success btn-main" @click="submitForm('ruleForm','modify')" v-if='idNum !== 0'>修改</button>
        <button class="btn btn-success btn-main" type="primary" @click="submitForm('ruleForm')" v-if='idNum === 0'>创建</button>
        <button class="btn btn-warning" @click="resetForm('ruleForm')">重置</button>
        <button class="btn btn-default" @click="backToRules">返回</button>
      </div>
    </div>
    <!--提交时将数据组装成一个，还需要携带当前用户的token信息等。才可以匹配写入正确的人手上-->
  </page>
</template>

<script>
import Page from '../../components/Page.vue'
import { mapGetters, mapActions } from 'vuex'
import { statusCallback } from '../../lib/utils'
export default {
  data () {
    //  定义email的验证
    let validateEmail = (rule, value, callback) => {
      //  这里对邮箱的前缀限制为数字字母下划线和横杠
      let reg = /^([a-zA-Z0-9_/-])*$/
      if (reg.test(value)) {
        callback()
      } else {
        callback(new Error('邮箱格式不正确'))
      }
    }
    //  定义数字范围的验证
    let validateNumber = (rule, value, callback) => {
      let reg = /^[1-9]\d*$/
      if (this.ruleForm.type === '1' || this.ruleForm.type === '2' || this.ruleForm.type === '3') {
        if (reg.test(value) && parseInt(value) >= 1 && parseInt(value) <= 100) {
          callback()
        } else {
          callback(new Error('请输入正整数'))
        }
      } else {
        callback()
      }
    }
    return {
      //  保存本地数据，用以驱动页面，不直接调用getter的原因是，该对象将被本地修改然后发向后台。
      ruleForm: {
        name: '',
        alarmEmail: '', // 前缀信息
        alarmEmail2: '@163.com', // 后缀信息
        machineType: [], // 创建时的是机器数组
        machineType2: '', // 获取的时是单个
        isUse: '1',
        type: '',
        expressionType: '',
        content: '',
        descriptionRule: '',
        triggerInterval: '',
        token: sessionStorage.getItem('token'),
        machineAndId: ''
      },
      //  定义默认的校验规则
      rules: {
        name: [
          { required: true, message: '请输入规则名称', trigger: 'blur' }
        ],
        machineType: [
          { type: 'array', required: true, message: '请至少选择一个适用机器', trigger: 'change' }
        ],
        type: [
          { required: true, message: '请选择资源类型', trigger: 'change' }
        ],
        expressionType: [
          { required: true, message: '请选择判断方式', trigger: 'change' }
        ],
        triggerInterval: [
          { required: true, message: '请选择触发间隔', trigger: 'change' }
        ],
        content: [
          { required: true, message: '请填写判断数据', trigger: 'blur' },
          { validator: validateNumber, message: '请填入1-100正整数', trigger: 'blur' }
        ],
        isUse: [
          { required: true, message: '请选择是否运行', trigger: 'blur' }
        ],
        alarmEmail: [
          { required: true, message: '请填写报警邮箱前缀', trigger: 'blur' },
          { validator: validateEmail, message: '邮箱格式不正确', trigger: 'blur' }
        ]
      },
      nodata: null, //  无数据时的显示
      idNum: 0, //  保存当前跳转时的id值
      // 根据当前url的情况来判断哪些需要显示，执行哪些功能
      title: '创建报警规则',
      title1: '报警规则详情',
      mark: 0//  是否resetJudge事件对content值生效
    }
  },
  computed: {
    ...mapGetters([
      'singleRule',
      'totalRules2Machine2'
    ])
  },
  components: {
    'page': Page
  },
  methods: {
    ...mapActions([
      'fetchSingleRule',
      'fetchRulesMachine2'
    ]),
    submitForm (formName, mark0) {
      let vm = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$confirm('确认提交?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let ajaxData = {}
            ajaxData.type = parseInt(vm.ruleForm.type)
            ajaxData.isUse = parseInt(vm.ruleForm.isUse)
            ajaxData.expressionType = parseInt(vm.ruleForm.expressionType)
            ajaxData.triggerInterval = parseInt(vm.ruleForm.triggerInterval)
            ajaxData.name = vm.ruleForm.name
            ajaxData.alarmEmail = vm.ruleForm.alarmEmail + vm.ruleForm.alarmEmail2
            ajaxData.descriptionRule = vm.ruleForm.descriptionRule
            ajaxData.content = vm.ruleForm.content
            if (mark0) {
              // 修改
              ajaxData.machineAndId = parseInt(vm.ruleForm.machineAndId)
              let num = parseInt(vm.idNum)
              vm.$store.dispatch('modifyRules', {ajaxData, num}).then((res) => {
                statusCallback(res, function (that) {
                  that.$message({
                    message: '操作成功',
                    type: 'success'
                  })
                  vm.$router.push('./rules')
                }, vm)
              })
            } else {
               // 创建
              ajaxData.machineType = vm.ruleForm.machineType
              vm.$store.dispatch('createNewRules', ajaxData).then((res) => {
                statusCallback(res, function (that) {
                  that.$message({
                    message: '操作成功',
                    type: 'success'
                  })
                  vm.$router.push('./rules')
                }, vm)
              })
            }
          }).catch(() => {
            // 取消操作
          })
        } else {
          // 验证未通过
          return false
        }
      })
    },
    // 点击重置按钮的函数
    resetForm (formName) {
      this.$confirm('是否重置?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$refs[formName].resetFields()
      }).catch(() => {
        // 取消操作
      })
    },
    // 资源type改变时调用的函数重置方式和值
    resetJudge () {
      if (this.ruleForm.type === '1' || this.ruleForm.type === '2' || this.ruleForm.type === '3') {
        this.ruleForm.expressionType = '1'
      } else if (this.ruleForm.type === '4') {
        this.ruleForm.expressionType = '3'
      } else {
        this.ruleForm.expressionType = ''
      }
      if (this.mark) {
        // 当mark不为0的时候才执行重置内容功能
        this.ruleForm.content = ''
      } else {
        this.mark = 1//  第一次进入时不执行resetJudge函数中的清楚内容功能，以后是人为的切换才进行内容清空。
      }
    },
    backToRules () {
      let vm = this
      this.$confirm('返回将失去所有改写的数据，是否返回?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        vm.$router.push('/rules')
        // 确定操作
      }).catch(() => {
        // 取消操作
      })
    }
  },
  mounted () {
    this.fetchRulesMachine2().then((res) => {
      statusCallback(res)
    })// 不带有所有这个选项的机器组
//  根据当前url绑定的id值来进行页面的处理
    if (location.hash.split('=')[1]) {
      this.idNum = location.hash.split('=')[1]
      this.fetchSingleRule(this.idNum).then((res) => {
        // 手动输入id值后会默认200状态码，但是返回为null
        if (res.data.rule === null || res.data === undefined) {
          this.$message({
            message: '无效数据信息',
            type: 'error'
          })
          return
        }
        statusCallback(res, function (that) {
          let data0 = that.singleRule.rule
          let vm = that
          for (let key in data0) {
            vm.ruleForm[key] = String(data0[key])
          }
          if (data0.alarmEmail) {
            vm.ruleForm.alarmEmail = data0.alarmEmail.split('@')[0]
            vm.ruleForm.alarmEmail1 = '@' + data0.alarmEmail.split('@')[1]
          }
          vm.ruleForm.machineType2 = data0.description
        }, this)
      })
    } else {
      this.mark = 1//  手动创建时，创建进页面时防止第一次resetJudge切换失效（没有调用第一次回显示），先将开关打开。
    }
  }
}
</script>
