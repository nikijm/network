<template>
  <page :title="title">
  <div slot='tools'>
<el-button @click='backtoAllsheets'>返回</el-button>
</div>
    <div slot="content" >
      <div class="bigdiv" v-if='allrulesData.length>0'>
          <div class='div_left' >
            <p class="div_P1" >源字段</p>
            <p  v-for='item in allrulesData'>{{item.columnName}}</p>
          </div>
          <div  class='div_center'>
            <p class="div_P1">模板名称</p>
            <div  v-for='item in allrulesData' >
              <p >{{item.ruleSuit.name}}</p>
             <!--  <p><span>处理器:</span></p>
              <div v-for='child in rules'>
              <p v-for='children in rulesParams'><span>{{children.name}}</span>:<span>{{children.paramValue}}</span></p> -->
              <!-- </div> -->
          </div>
          </div>
           <div  class='div_right'>
           <p  class="div_P1">目标字段</p>
           <p  v-for='item in allrulesData'>{{item.etlCoulumnTargetName}}</p>
          </div>
      </div>
      <div v-else style="text-align: center">
        暂无规则详情
      </div>
    </div>
  </page>
</template>

<script>
  import Page from '../../components/Page'
  import { mapActions } from 'vuex'
  export default {
    data () {
      return {
        Params: {},
        allrulesData: [],
        title: '',
        rules: [],
        rulesParams: []
      }
    },
    components: {
      'page': Page
    },
    mounted () {
      this.Params = JSON.parse(sessionStorage.getItem('rulesData'))
      switch (this.Params.category) {
        case 'cache':
          this.title = '缓存规则'
          break
        case 'clean':
          this.title = '清洗规则'
          break
        case 'convert':
          this.title = '转换规则'
          break
        case 'validate':
          this.title = '校验规则'
          break
      }
      this.loadData()
    },
    methods: {
      ...mapActions([
        'getRuleUsers'
      ]),
      backtoAllsheets () {
        this.$router.back()
      },
      loadData () {
        this.getRuleUsers(this.Params).then((res) => {
          this.allrulesData = res.data.dataFileRuleUses
          console.log(res)
          // 处理器
          // for (var items of res.data.dataFileRuleUses) {
          //   this.rules = JSON.parse(items.ruleSuit.rules)
          //   for (var cod of this.rules) {
          //     this.rulesParams = JSON.parse(cod.params)
          //   }
          // }
        })
      }
    }
  }
</script>
<style>
  .bigdiv{
    border: solid 1px #D4D4D4; 
    width: 80%;
    display: flex;
    margin: auto
  }
  .div_left{
    border-right: solid 1px #D4D4D4;
    flex: 1
  }
  .div_P1 {
    padding: 10px 0 10px 0;
    border-bottom: 1px solid #D4D4D4;
    font-weight: 700;
  }
  .div_center {
    flex: 1;
  }
  .div_right {
    border-left: solid 1px #D4D4D4;
    flex: 1;
  }

</style>
