<template>
  <page :title="title" :isLoading="isLoading">
    <div slot="tools">
      <el-select v-model="mvalue" placeholder="按机器过滤" @change='changeSelect'>
        <el-option
          v-for="item in totalRules2Machine" :label="item.description"
          :value="item.id">
        </el-option>
      </el-select>
      <el-select v-model="rvalue" placeholder="按资源过滤" @change='changeSelect'>
        <el-option
          v-for="(item, index) in roptions" :label='item.value'
          :value="index">
        </el-option>
      </el-select>
      <el-select v-model="svalue" placeholder="按状态过滤" @change='changeSelect'>
        <el-option
          v-for="(item, index) in soptions" :label="item.value"
          :value="index">
        </el-option>
      </el-select>
      <button class="btn btn-primary"  @click="jumpToCreate">添加</button>
    </div>
    <div slot="content">
      <table class="table table-hover">
        <thead>
        <tr>
          <th>规则名称</th>
          <th>机器</th>
          <th>资源</th>
          <th>报警邮箱</th>
          <th>创建人</th>
          <th>创建时间</th>
          <th>状态</th>
          <th>触发次数</th>
          <th>最近触发</th>
          <th width="60">&nbsp;</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item, index) in totalRules2.rule_list">
          <td class='break'><div class='route-jump' @click='jumpCreate(item.id)'>{{item.name}}</div></td>
          <td class='break'>{{item.description}}</td>
          <td class='break'>{{item.monitorResource}}</td>
          <td class='break'>{{item.alarmEmail}}</td>
          <td class='break'>{{item.createUser}}</td>
          <td class='break'>{{item.createTime}}</td>
          <td class='break'>
            <span class="run-status" v-show='item.isUse===1'><i class="running"></i>&nbsp;运行中</span>
            <span class="run-status" v-show='item.isUse===2'><i class="stop"></i>&nbsp;未运行</span>
          </td>
          <td class='break'>{{item.useCount}}</td>
          <td class='break'>{{item.recentTriggerView}}</td>
          <td class='break center'><button class="btn btn-danger btn-xs" @click="deleteRule(item.id)">删除</button></td>
        </tr>
        </tbody>
      </table>
      <div class="col-md-12" style='text-align: center;' v-show='isLoading'>数据请求中</div>
      <div class="col-md-12" style='text-align: center;' v-show='noData<=0'>暂无数据</div>
    </div>
  </page>
</template>

<script>
import Page from '../../components/Page.vue'
import VATable from '../../components/VATable.vue'
import { mapGetters, mapActions } from 'vuex'
import { statusCallback } from '../../lib/utils'
export default {
  data () {
    return {
      //  机器下拉
      mvalue: '',
      //  资源下拉
      roptions:
      [
        { value: '所有' },
        { value: 'CPU' },
        { value: '内存' },
        { value: '磁盘' },
        { value: '日志' }
      ],
      rvalue: '',
      //  状态下拉
      soptions:
      [
        { value: '所有' },
        { value: '已启用' },
        { value: '未启用' }
      ],
      svalue: '',
      tableData: null,
      noData: 1,
      title: '警报规则'
    }
  },
  methods: {
    ...mapActions([
      'fetchRules2', //  发起查询警报的action，fetchRule被其他开发人员用过了，因此加了个2标识
      'fetchRulesMachine', //  发起查询机器的action
      'fetchDeleteRule'//  发起删除某一条警报的请求
    ]),
    // 构建请求数据的函数
    createAjaxData (that) {
      let req = {}
      req.serverId = that.mvalue !== '' ? parseInt(that.mvalue) : 0
      req.type = that.rvalue !== '' ? parseInt(that.rvalue) : 0
      req.status = that.svalue !== '' ? parseInt(that.svalue) : 0
      return req
    },
    // 点击按钮创建新的警报跳转
    jumpToCreate () {
      this.$router.push('/createrules')
    },
    // 点击某一条发起的创建警报跳转
    jumpCreate (num) {
      this.$router.push('/createrules?id=' + num)
    },
    // 点击某一条发起删除的跳转
    deleteRule (num) {
      let id0 = parseInt(num)
      let vm = this
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.fetchDeleteRule(id0).then(function (res) {
          statusCallback(res, function () {
            let req = vm.createAjaxData(vm)
            vm.fetchRules2(req).then((res) => {
              statusCallback(res, function (that) {
                if (that.totalRules2.rule_list) {
                  that.noData = that.totalRules2.rule_list.length
                }
              }, vm)
            })
          }, this)
        })
      }).catch(() => {
        // 取消操作
      })
    },
    // 根据当前上方的下拉选项的情况进行ajax请求
    changeSelect (evt) {
      let req = this.createAjaxData(this)
      this.$store.dispatch('fetchRules2', req).then((res) => {
        statusCallback(res, function (that) {
          if (that.totalRules2.rule_list) {
            that.noData = that.totalRules2.rule_list.length
          }
        }, this)
      })
    }
  },
  components: {
    'va-table': VATable,
    'page': Page
  },
  computed: {
    ...mapGetters([
      'totalRules2', // 请求回来的所有的警报列表getter
      'totalRules2Machine', // 请求回来的所有的机器列表getter
      'isLoading'
    ])
  },
  mounted () {
    if (location.hash) {
      let str0 = location.hash.split('?')[1]
      if (str0) {
        let arr0 = str0.split('&')
        if (arr0[0].split('=')[1]) {
          this.mvalue = parseInt(arr0[0].split('=')[1])
          this.rvalue = parseInt(arr0[1].split('=')[1])
        }
      }
    }
    this.fetchRulesMachine().then((res) => {
      statusCallback(res)
    })
    let req = this.createAjaxData(this)
    this.fetchRules2(req).then((res) => {
      statusCallback(res, function (that) {
        if (that.totalRules2.rule_list) {
          that.noData = that.totalRules2.rule_list.length
        }
      }, this)
    })
  }
}
</script>
