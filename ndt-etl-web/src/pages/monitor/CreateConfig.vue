<template>
  <page :title="idNum === 0 ? title : title1" :isLoading="isLoading">
    <div slot="content">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
        <el-row>
          <el-col :span="20">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="ruleForm.username"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="20">
            <el-form-item label="密码" prop="password">
              <el-input type='password' v-model="ruleForm.password"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="20">
            <el-form-item label="机器名" prop="description">
              <el-input v-model="ruleForm.description"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="20">
            <el-form-item label="模块名" prop="identification">
              <el-input v-model="ruleForm.identification" :disabled='idNum !== 0'></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!--只有在非创建模式下才可以看到cpu等信息-->
        <div style='display:flex' v-if='idNum !== 0'>
          <el-form-item label="CPU" prop="cpusize">
            {{ruleForm.cpusize}}
          </el-form-item>
          <span style='height:36px;line-height:36px'>核</span>
          <el-form-item label="内存" prop="memorysize" style='margin-left:-20px'>
            {{ruleForm.memorysize}}
          </el-form-item>
          <span style='height:36px;line-height:36px'>GB</span>
          <el-form-item label="磁盘" prop="disksize" style='margin-left:-20px'>
            {{ruleForm.disksize}}
          </el-form-item>
          <span style='height:36px;line-height:36px'>TB</span>
        </div>
        <el-row>
          <el-col :span="20">
            <el-form-item label="IP地址" prop="ipaddress">
              <el-input v-model="ruleForm.ipaddress"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="是否监控" prop="statu">
          <el-radio-group v-model="ruleForm.statu">
            <el-radio label="1">是</el-radio>
            <el-radio label="2">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="serverNote">
          <el-row>
            <el-col :span="12">
              <el-input type="textarea" v-model="ruleForm.serverNote"></el-input>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>
    <div slot="footer">
      <div class="ml-10">
        <button class="btn btn-success btn-main" @click="submitForm('ruleForm','modify')" v-if='idNum !== 0'>修改</button>
        <button class="btn btn-success btn-main" @click="submitForm('ruleForm')" v-if='idNum === 0'>创建</button>
        <button class="btn btn-warning" @click="resetForm('ruleForm')">重置</button>
        <button class="btn btn-default" @click="backToConfig">返回</button>
      </div>
    </div>
  </page>
</template>

<script>
import Page from '../../components/Page.vue'
import { mapGetters, mapActions } from 'vuex'
import { statusCallback } from '../../lib/utils'
export default {
  data () {
    // 定义IP地址的检验器
    let validateIP = (rule, value, callback) => {
      let reg = /^(?:(?:2[0-4][0-9]\.)|(?:25[0-5]\.)|(?:1[0-9][0-9]\.)|(?:[1-9][0-9]\.)|(?:[0-9]\.)){3}(?:(?:2[0-4][0-9])|(?:25[0-5])|(?:1[0-9][0-9])|(?:[1-9][0-9])|(?:[0-9]))$/
      if (reg.test(value)) {
        callback()
      } else {
        callback(new Error('请输入正确的IP地址'))
      }
    }
    return {
      ruleForm: {
        username: '',
        password: '',
        description: '',
        ipaddress: '',
        statu: '1',
        serverNote: '',
        cpusize: '',
        memorysize: '',
        disksize: '',
        identification: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入机器名称', trigger: 'blur' }
        ],
        ipaddress: [
          { required: true, message: '请输入IP地址', trigger: 'blur' },
          { validator: validateIP, message: '请输入正确的IP地址', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        identification: [
          { required: true, message: '请输入模块名', trigger: 'blur' }
        ]
      },
      title: '创建机器',
      title1: '机器详情',
      idNum: 0
    }
  },
  computed: {
    ...mapGetters([
      'singleConfigMachine', // 获取单个配置机器的信息
      'isLoading'
    ])
  },
  components: {
    'page': Page
  },
  methods: {
    ...mapActions([
      'fetchSingleConfigMachine'
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
            // 定义局部变量，不直接从表单取是因为有些数据格式需要调整。
            let ajaxData = {}
            ajaxData.username = vm.ruleForm.username
            ajaxData.password = vm.ruleForm.password
            ajaxData.identification = vm.ruleForm.identification
            ajaxData.description = vm.ruleForm.description
            ajaxData.serverNote = vm.ruleForm.serverNote
            ajaxData.ipaddress = vm.ruleForm.ipaddress
            ajaxData.statu = parseInt(vm.ruleForm.statu)
            if (mark0) {
              // 修改
              let num = parseInt(vm.idNum)
              vm.$store.dispatch('modifyConfigMachine', { ajaxData, num }).then((res) => {
                statusCallback(res, function (that) {
                  that.$message({
                    message: '操作成功',
                    type: 'success'
                  })
                  that.$router.push('./config')
                }, vm)
              })
            } else {
              // 创建
              vm.$store.dispatch('createConfigMachine', ajaxData).then((res) => {
                statusCallback(res, function (that) {
                  that.$message({
                    message: '操作成功',
                    type: 'success'
                  }, vm)
                  that.$router.push('./config')
                }, vm)
              })
            }
          }).catch(() => {
            // 取消操作
          })
        } else {
          // 没有通过验证
          return false
        }
      })
    },
    resetForm (formName) {
      this.$confirm('是否重置?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$refs[formName].resetFields()
          // 确定操作
      }).catch(() => {
          // 取消操作
      })
    },
    backToConfig () {
      let vm = this
      this.$confirm('返回将失去所有改写的数据，是否返回?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        vm.$router.push('/config')
          // 确定操作
      }).catch(() => {
          // 取消操作
      })
    }
  },
  mounted () {
    // 修改前先获取初始值
    if (location.hash.split('=')[1]) {
      this.idNum = location.hash.split('=')[1]
      this.fetchSingleConfigMachine(this.idNum).then((res) => {
        // 如果手动输入id=XX值后台会有对应的400处理。
        statusCallback(res, function (that) {
          let data0 = that.singleConfigMachine
          let vm = that
          for (let key in data0) {
            vm.ruleForm[key] = String(data0[key])
          }
        }, this)
      })
    }
  }
}
</script>

