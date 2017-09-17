<template>
  <page :title="idNum===0?title:title1" :isLoading="isLoading">
    <div slot="content">
      <el-form :model="ruleForm" ref="ruleForm" label-width="100px" disabled>
        <el-row>
          <el-col :span="20">
            <el-form-item label="服务名称" prop="serviceName">
              <div style='font-size:16px;font-weight:bold;'>{{singleService.serviceName}}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="服务描述" prop="description">
          <div style='width:100%;padding-right:15px;word-break: break-word;'>
            {{singleService.description}}
          </div>
        </el-form-item>
        <el-form-item label="是否运行" prop="statu">
          <span class="run-status" v-show='singleService.statu===1'><i class="running"></i>&nbsp;运行中</span>
          <span class="run-status" v-show='singleService.statu===2'><i class="process"></i>&nbsp;处理中</span>
          <span class="run-status" v-show='singleService.statu===3'><i class="stop"></i>&nbsp;未运行</span>
        </el-form-item>
      </el-form>
    </div>
    <div slot="footer">
      <div class="ml-10">
        <el-button @click="backToService">返回</el-button>
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
    return {
      ruleForm: {
        serviceName: '',
        description: '',
        statu: ''
      },
      nodata: null, //  无数据时的显示
      idNum: 0, //  保存当前跳转时的id值
      // 根据当前url的情况来判断哪些需要显示，执行哪些功能
      title: '创建服务',
      title1: '服务详情',
      title2: '创建',
      title3: '修改'
    }
  },
  computed: {
    ...mapGetters([
      'singleService',
      'isLoading'
    ])
  },
  components: {
    'page': Page
  },
  methods: {
    ...mapActions([
      'fetchSingleService'
    ]),
    backToService () {
      this.$router.push('/service')
    }
  },
  mounted () {
//  console.log(location.hash.split('=')[1])
//  根据当前url绑定的id值来进行页面的处理
    if (location.hash.split('=')[1]) {
      this.idNum = location.hash.split('=')[1]
      this.fetchSingleService(this.idNum).then((res) => {
        if (res) {
          if (res.data.data === null || res.data === undefined) {
            this.$message({
              message: '无效数据信息',
              type: 'error'
            })
            return
          }
        }
        statusCallback(res)
      })
    }
  }
}
</script>
<style scoped>
.run-status i{
  width: 16px;
  height: 16px;
  display: -moz-inline-stack;
  display: inline-block;
  vertical-align: middle;
  zoom: 1;
  border-radius: 16px;
  vertical-align: middle;
  margin-top: -1px;
}
.run-status i.running{
  background-color: #2CAE00;
}
.run-status i.process{
  background-color: yellow;
}
.run-status i.stop{
  background-color: red;
}
</style>
