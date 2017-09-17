<template>
  <page :title="title" :isLoading="isLoading">
    <div slot="tools">
      <el-select v-model="mvalue" @change='setTab'>
        <el-option
          v-for="item in totalConfigMachine.hostlist" :label="item.description"
          :value="item.id">
        </el-option>
      </el-select>
    </div>
    <div slot="content">
      <div class="row" style="padding:15px 10px;background-color:#F9f9f9;margin:0 0 10px 0;overflow:hidden;font-size:12px;">
        <div class="col-md-2">{{singleConfigMachine.description}}</div>
        <div class="col-md-2">状态：
          <span class="run-status" v-if='singleConfigMachine.statu===1 '><i class='running'></i>&nbsp;监控中</span>
          <span class="run-status" v-if='singleConfigMachine.statu===2'><i class='stop'></i>&nbsp;未监控</span></div>
        <div class="col-md-2">IP地址：{{singleConfigMachine.ipaddress}}</div>
        <div class="col-md-2">CPU：{{singleConfigMachine.cpusize}}核</div>
        <div class="col-md-2">内存： {{singleConfigMachine.memorysize}}G</div>
        <div class="col-md-2">磁盘： {{singleConfigMachine.disksize}}T</div>
      </div>
      <div class="row">
        <div class="col-md-6" style="border-right:1px solid #eee; border-bottom:1px solid #eee;">
          <div class="chart-tool">
            <div class="btn-group" role="group" aria-label="...">
              <button type="button" class="btn btn-default btn-sm" @click='setFigure(mvalue,"cpu","one_hour")'>1小时</button>
              <button type="button" class="btn btn-default btn-sm" @click='setFigure(mvalue,"cpu","one_day")'>1天</button>
              <button type="button" class="btn btn-default btn-sm" @click='setFigure(mvalue,"cpu","seven_days")'>7天</button>
              <button type="button" class="btn btn-default btn-sm" @click='setFigure(mvalue,"cpu","fifteen_days")'>15天</button>
            </div>
            <button type="button" class="btn btn-primary btn-sm" @click='jump(mvalue,1)'>{{info}}</button>
          </div>
          <div style='height:400px;background: white;'>
            <div class='nodata' v-show='connectErr'>连接失败，请检查网络</div>
            <div id='cpu' v-show='!connectErr'></div>
          </div>
        </div>
        <div class="col-md-6" style="border-bottom:1px solid #eee;">
          <div class="chart-tool">
            <div class="btn-group" role="group" aria-label="...">
              <button type="button" class="btn btn-default btn-sm" @click='setFigure(mvalue,"memory","one_hour")'>1小时</button>
              <button type="button" class="btn btn-default btn-sm" @click='setFigure(mvalue,"memory","one_day")'>1天</button>
              <button type="button" class="btn btn-default btn-sm" @click='setFigure(mvalue,"memory","seven_days")'>7天</button>
              <button type="button" class="btn btn-default btn-sm" @click='setFigure(mvalue,"memory","fifteen_days")'>15天</button>
            </div>
            <button type="button" class="btn btn-primary btn-sm" @click='jump(mvalue,2)'>{{info}}</button>
            <button type="button" class="btn btn-success btn-sm" @click='clear(singleConfigMachine.id)'>{{info2}}</button>
          </div>
          <div style='height:400px;background: white;'>
            <div class='nodata' v-show='connectErr'>连接失败，请检查网络</div>
            <div id='memory' v-show='!connectErr'></div>
          </div>
        </div>
        <div class="col-md-6" style="border-right:1px solid #eee;padding-top:10px;">
          <div class="chart-tool">
            <div class="btn-group" role="group" aria-label="...">
              <button type="button" class="btn btn-default btn-sm" @click='setFigure(mvalue,"disk","one_hour")'>1小时</button>
              <button type="button" class="btn btn-default btn-sm" @click='setFigure(mvalue,"disk","one_day")'>1天</button>
              <button type="button" class="btn btn-default btn-sm" @click='setFigure(mvalue,"disk","seven_days")'>7天</button>
              <button type="button" class="btn btn-default btn-sm" @click='setFigure(mvalue,"disk","fifteen_days")'>15天</button>
            </div>
            <button type="button" class="btn btn-primary btn-sm" @click='jump(mvalue,3)'>{{info}}</button>
          </div>
          <div style='height:400px;background: white;'>
            <div class='nodata' v-show='connectErr'>连接失败，请检查网络</div>
            <div id='disk' v-show='!connectErr'></div>
          </div>
        </div>
        <div class="col-md-6">
        </div>
      </div>
    </div>
  </page>
</template>

<script>
import VATable from '../../components/VATable.vue'
import Page from '../../components/Page.vue'
import Highcharts from 'highcharts'
import { mapGetters, mapActions } from 'vuex'
import { statusCallback } from '../../lib/utils'
export default {
  data () {
    return {
      //  设备信息，有待后台接口
      mvalue: null, // 机器id编号
      info: '查看警报规则',
      info2: '释放内存',
      json: [{ series: [{ data: [] }], xAxis: { categories: [] } },
      { series: [{ data: [] }], xAxis: { categories: [] } },
      { series: [{ data: [] }], xAxis: { categories: [] } }
      ],
      ajaxData: [
      //  1:web 2:app 3:db etc...
      { serverName: 0, monitorCompunent: 'cpu', data: { filter_by: 'one_hour' } },
      { serverName: 0, monitorCompunent: 'memory', data: { filter_by: 'one_hour' } },
      { serverName: 0, monitorCompunent: 'disk', data: { filter_by: 'one_hour' } }],
      title: '控制台',
      connectErr: false, // 连接失败时的空白显示
      timer: null// 本地计时器
    }
  },
  components: {
    'va-table': VATable,
    'page': Page
  },
  mounted () {
    clearInterval(this.timer)// 清理本地和getter中的定时器
    clearInterval(this.timerArr)
    this.callData()// 调用出图
    //  调用后台接口返回数据
    this.fetchTotalConfigMachine().then((res) => {
//    console.log(res)
      statusCallback(res, function (that) {
        let num0 = null
        if (that.totalConfigMachine.hostlist) {
          num0 = that.totalConfigMachine.hostlist[0].id// 获取当前机器的第一条作为默认显示
          that.mvalue = num0// 获取当前数据的第一条作为默认显示，设置本地参数mvalue
          for (let i = 0; i < that.ajaxData.length; i++) {
            that.ajaxData[i].serverName = num0
          }
          that.fetchSingleConfigMachine(num0).then((res) => { // 根据当前机器的id值，填充下面的机器信息
            statusCallback(res, function (vm) {
              vm.fetchAll()
              vm.connectErr = false
            }, that)
          })
        }
      }, this)
    })
  },
  computed: {
    ...mapGetters([
      //  0-2分别代表不同机器的cpu，memory，disk
      'totalDashboard0', //  cpu
      'totalDashboard1', //  memory
      'totalDashboard2', //  disk
      'totalConfigMachine', // 读取全部服务器的信息
      'singleConfigMachine', // 读取单个服务器的信息
      'timerArr', // 计时器数组
      'isLoading'
    ])
  },
  beforeRouteLeave (to, from, next) {
    clearInterval(this.timer)
    clearInterval(this.timerArr)
    next()
  },
  methods: {
    fetchAll () {
      // 根据当前的ajaxData，请求数据，根据返回的数据进行格式转换，再重绘highcharts图
      this.fetchDashboard(this.ajaxData[0]).then((res) => {
        statusCallback(res, function (that) {
          that.json[0].series[0].data = that.changeTime(that.totalDashboard0.createtime, that.totalDashboard0.cpu)
          $('#cpu').highcharts(that.json[0])
        }, this)
      })
      this.fetchDashboard(this.ajaxData[1]).then((res) => {
        statusCallback(res, function (that) {
          that.json[1].series[0].data = that.changeTime(that.totalDashboard1.createtime, that.totalDashboard1.memory)
          $('#memory').highcharts(that.json[1])
        }, this)
      })
      this.fetchDashboard(this.ajaxData[2]).then((res) => {
        statusCallback(res, function (that) {
          that.json[2].series[0].data = that.changeTime(that.totalDashboard2.createtime, that.totalDashboard2.disk)
          $('#disk').highcharts(that.json[2])
        }, this)
      })
    },
    setTab () {
      this.fetchSingleConfigMachine(this.mvalue).then((res) => {
        statusCallback(res, function (that) {
          for (let i = 0; i < that.ajaxData.length; i++) {
            that.ajaxData[i].serverName = that.mvalue
            that.ajaxData[i].data.filter_by = 'one_hour'
          }
          that.fetchAll()
          clearInterval(that.timerArr)
          clearInterval(that.timer)
          if (!that.timer) {
            that.timer = setInterval(() => {
              that.fetchAll()
            }, 1 * 60 * 1000)// 默认设定为1分钟，页面自动请求一次。
            that.$store.commit('SETTIMER', that.timer)// 计时器计数时，防止开发环境热替换出问题，故将timer写入store以便于删除操作
          }
        }, this)
      })
    },
    //  setFigure函数，处理点击时候的调用函数。
    //  str1："web","app","db",后台返回的id
    //  str2: "cpu","memory","disk"
    //  str3: "one_hour","one_day","seven_days","fifteen_days"
    setFigure (str1, str2, str3) {
      let vm = this
      for (let i = 0; i < this.ajaxData.length; i++) {
        if (this.ajaxData[i].serverName === str1 && this.ajaxData[i].monitorCompunent === str2) {
          this.ajaxData[i].data.filter_by = str3
          this.fetchDashboard(this.ajaxData[i]).then((res) => {
            statusCallback(res, function (that) {
              if (i === 0) {
                vm.json[0].series[0].data = vm.changeTime(vm.totalDashboard0.createtime, vm.totalDashboard0.cpu)
                $('#cpu').highcharts(vm.json[0])
              } else if (i === 1) {
                vm.json[1].series[0].data = vm.changeTime(vm.totalDashboard1.createtime, vm.totalDashboard1.memory)
                $('#memory').highcharts(vm.json[1])
              } else if (i === 2) {
                vm.json[2].series[0].data = vm.changeTime(vm.totalDashboard2.createtime, vm.totalDashboard2.disk)
                $('#disk').highcharts(vm.json[2])
              }
            }, this)
          })
          //  这里成功回调之后确保数据回来，然后重置json和画图。
        }
      }
    },
    //  处理清理内存的函数
    clear (num) {
      this.clearDashboard(num).then((res) => {
//      console.log(res)
        statusCallback(res, function (that) {
          that.$message({
            message: '清理时间' + res.data.usage.datetime + '，使用百分比' + res.data.usage.percentage + '，剩余空间' + res.data.usage.value,
            type: 'success'
          })
        }, this)
      })
    },
    //  处理跳转的函数，设置规则就会跳到对应的规则页面
    jump (mvalue, val) {
      this.$router.push('/rules?m=' + mvalue + '&t=' + val)
    },
    //  处理返回的数据格式转换的函数
    changeTime (arr1, arr2) {
      let d = []
      let xData = []
      let finalData = []
      for (let i = 0; i < arr1.length; i++) {
        d[i] = new Date(arr1[i])
        xData[i] = [d[i].getFullYear(), d[i].getMonth(), d[i].getDate(), d[i].getHours(), d[i].getMinutes() + Math.floor(d[i].getSeconds() / 30)]
//      console.log(...xData[i])
        finalData[i] = [ Date.UTC(...xData[i]), arr2[i] ]
      }
      return finalData
    },
    ...mapActions([
      'fetchDashboard',
      'clearDashboard',
      'fetchTotalConfigMachine', // 读取全部服务器的信息
      'fetchSingleConfigMachine'// 读取单个服务器的信息
    ]),
    callData () {
      //  创建highcharts的实例,公用的部分单独列，单独的部分循环遍历，修改的是本页面data下面的json的series字段。
      //  是否有放大缩小功能
      let chart = {
        zoomType: 'x'
      }
      // 是否带有原生的标识
      let credits = {
        enabled: false
      }
      // X轴的显示
      let xAxis = {
        type: 'datetime',
        tickInterval: 15 * 60 * 1000,
        dateTimeLabelFormats: {
          millisecond: '%H:%M:%S'// highcharts的默认样式中含有毫秒数
        }
      }
      // Y轴的显示
      let yAxis = {
        title: {
          text: ''
        }
      }
      // 图例
      let legend = {
        enabled: false
      }
      // 图形配置，含有填充效果，点信息等
      let plotOptions = {
        area: {
          fillColor: {
            linearGradient: {x1: 0, y1: 0, x2: 0, y2: 1},
            stops: [
              [0, Highcharts.getOptions().colors[0]],
              [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
            ]
          },
          marker: {
            radius: 2
          },
          lineWidth: 1,
          states: {
            hover: {
              lineWidth: 1
            }
          },
          threshold: null
        }
      }
      // 图形标题
      let title = [
      { text: 'CPU占用率(%)' },
      { text: '内存占用率(%)' },
      { text: '磁盘占用率(%)' }
      ]
      // 创建时的序列，存放数据
      let series = [
        [{
          type: 'area',
          name: 'CPU占用',
          data: [
          ]
        }],
        [{
          type: 'area',
          name: '内存占用',
          data: [
          ]
        }],
        [{
          type: 'area',
          name: '磁盘占用',
          data: [
          ]
        }]
      ]
      // 构建三张highcharts
      for (let i = 0; i < this.json.length; i++) {
        this.json[i].credits = credits
        this.json[i].chart = chart
        this.json[i].title = title[i]
        this.json[i].legend = legend
        this.json[i].xAxis = xAxis
        this.json[i].yAxis = yAxis
        this.json[i].series = series[i]
        this.json[i].plotOptions = plotOptions
      }
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
.run-status{
  color:#2CAE00;
}
.run-status i.running{
  background-color: #2CAE00;
}
.run-status i.stop{
  background-color: red;
}
.chart-tool{
  padding:10px;
  text-align: right;
  background-color: #EFEFEF;
}
.chart-title{
  font-size:16px;
  font-weight: 500;
  text-align: center;
  margin-top:10px;
  margin-bottom: 10px;
}
.table tr td{
  padding:15px 10px;
}
.nodata{
  position:absolute;
  z-index:2;
  width:100%;
  height:100%;
  text-align: center;
  top:200px
}
</style>
