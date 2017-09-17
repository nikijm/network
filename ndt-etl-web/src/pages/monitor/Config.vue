<template>
  <page :title="title" :isLoading="isLoading">
    <div slot="tools">
      <button class="btn btn-primary" type="primary" @click="jumpToCreate">添加</button>
    </div>
    <div slot="content">
      <table class="table table-hover">
        <thead>
          <tr>
            <th v-for='item in tableTitle'>{{item}}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in totalConfigMachine.hostlist">
            <td class='break'><div class='route-jump' @click='jumpCreate(item.id)'>{{item.description}}</div></td>
            <td class='break'>{{item.ipaddress}}</td>
            <td class='break'>
              <span class="run-status" v-show='item.statu===1'><i class="running"></i>&nbsp;监控中</span>
              <span class="run-status" v-show='item.statu===2'><i class="stop"></i>&nbsp;未监控</span>
            </td>
            <td class='break ellipsis'>{{item.serverNote}}</td>
            <td class='break'><button class="btn btn-danger btn-xs"  @click="deleteConfigMachine(item.id)">删除</button></td>
          </tr>
        </tbody>
      </table>
      <div class="col-md-12" style='text-align: center;' v-show='isLoading'>数据请求中</div>
      <div class="col-md-12" style='text-align: center;' v-show='noData<=0'>暂无数据</div>
    </div>
  </page>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import VATable from '../../components/VATable.vue'
import Page from '../../components/Page.vue'
import { statusCallback } from '../../lib/utils'
export default {
  data () {
    return {
      tableTitle: [ '机器', 'IP', '状态', '备注', '删除' ],
      noData: 1,
      title: '配置'
    }
  },
  components: {
    'va-table': VATable,
    'page': Page
  },
  methods: {
    ...mapActions([
      'fetchTotalConfigMachine'// 获取全部配置机器的信息
    ]),
    // 点击创建新的配置机器跳转
    jumpToCreate () {
      this.$router.push('/createconfig')
    },
    // 点击某一条查看详情页面跳转
    jumpCreate (num) {
      this.$router.push('/createconfig?id=' + num)
    },
    // 点击某一条发起删除的配置的
    deleteConfigMachine (num) {
      let id0 = parseInt(num)// 删除的id值
      let vm = this
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('deleteConfigMachine', id0).then(function (res) {
          statusCallback(res, function () {
            // 重新请求数据刷新页面。
            vm.fetchTotalConfigMachine().then((res) => {
              statusCallback(res, function (that) {
                if (that.totalConfigMachine.hostlist) {
                  that.noData = that.totalConfigMachine.hostlist.length
                }
              }, vm)
            })
          })
        })
      }).catch(() => {
        // 取消操作
      })
    }
  },
  computed: {
    ...mapGetters([
      'totalConfigMachine',
      'isLoading'
    ])
  },
  mounted () {
    this.fetchTotalConfigMachine().then((res) => {
      statusCallback(res, function (that) {
        if (that.totalConfigMachine.hostlist) {
          that.noData = that.totalConfigMachine.hostlist.length
        }
      }, this)
    })
  }
}
</script>
<style>
  .route-jump{color: #40A7FF;}
  .route-jump:hover{cursor:pointer;}
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
  .run-status i.stop{
    background-color: red;
  }
  .break{word-wrap: break-word;-ms-word-wrap:break-word ;}
  .ellipsis {
    min-width:85px;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow:hidden;
  }
  .center{
    text-align:center;
  }
</style>
