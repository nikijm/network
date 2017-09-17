<template>
  <span>
    <button :class="computedButtonClass" @click="showDialog">{{computedButtonText}}</button>
    <el-dialog
      title="对比数据"
      :visible.sync="dialogVisible"
      size="full" class="test">
      <div class="compare_select">
        <el-select v-model="select_value" placeholder="请选择">
          <el-option
            v-for="item in options"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div v-if="compare1" class="table_box">
        <h4 class="title">原始数据</h4>
        <div :style="height">
          <va-table style="background-color:#fff;">
            <tr>
              <th v-for="(value, key) in itemsOriginal.data[0]" width="125px">
                {{key}}
              </th>
            </tr>
            <tr v-for="list in itemsOriginal.data">
              <td v-for="(value, key) in list">
                {{value}}
              </td>
            </tr>
          </va-table>
        </div>
      </div>
      <div v-if="compare2" class="table_box">
        <h4 class="title">清洗后数据</h4>
        <div :style="height">
          <va-table style="background-color:#fff;">
            <tr>
              <th v-for="(value, key) in itemsCleaned.data[0]" width="125px">
                {{key}}
              </th>
            </tr>
            <tr v-for="list in itemsCleaned.data">
              <td v-for="(value, key) in list">
                {{value}}
              </td>
            </tr>
          </va-table>
        </div>
      </div>
      <div v-if="compare3" class="table_box">
        <h4 class="title">转换后数据</h4>
        <div :style="height">
          <va-table style="background-color:#fff;">
            <tr>
              <th v-for="(value, key) in items.data[0]" width="125px">
                {{key}}
              </th>
            </tr>
            <tr v-for="list in items.data">
              <td v-for="(value, key) in list">
                {{value}}
              </td>
            </tr>
          </va-table>
        </div>
      </div>
      <div class="pagination">
        <el-pagination
          @current-change="toPage"
          :current-page="itemsOriginal.paging.current"
          :page-size="itemsOriginal.paging.page_size"
          layout="prev, pager, next"
          :total="itemsOriginal.paging.total">
        </el-pagination>
      </div>
    </el-dialog>
  </span>
</template>

<script>
import VATable from './VATable.vue'
import { mapGetters, mapActions } from 'vuex'

export default {
  // preferences 参数选项O-C:首选项为原始数据与清洗后数据 O-T:首选项为原始数据与转换后数据 C-T:首选项为清洗后数据与转换后数据
  props: ['messageId', 'messageType', 'preferences', 'buttonClass', 'buttonText'],
  data () {
    return {
      height: 'max-height:500px;overflow:auto;',
      dialogVisible: false,
      payload: {
        id: 1,
        page: 1
      },
      itemsOriginal: {
        paging: {
          current: 0,
          total: 0,
          page_size: 15
        },
        data: []
      },
      itemsCleaned: {
        paging: {
          current: 0,
          total: 0,
          page_size: 15
        },
        data: []
      },
      items: {
        paging: {
          current: 0,
          total: 0,
          page_size: 15
        },
        data: []
      },
      options: [],
      select_value: '原始-清洗',
      compare1: true,
      compare2: true,
      compare3: false
    }
  },
  computed: {
    ...mapGetters([
      'fetchOriginalData',
      'fetchCleanedData',
      'fetchListData'
    ]),
    computedButtonText () {
      return this.buttonText ? this.buttonText : '对比数据变化'
    },
    computedButtonClass () {
      return this.buttonClass ? this.buttonClass : 'btn btn-primary'
    }
  },
  components: {
    'va-table': VATable
  },
  watch: {
    'select_value': function (newValue, oldValue) {
      if (newValue === '选项1') {
        this.compare1 = true
        this.compare2 = true
        this.compare3 = false
      } else if (newValue === '选项2') {
        this.compare1 = true
        this.compare2 = false
        this.compare3 = true
      } else if (newValue === '选项3') {
        this.compare1 = false
        this.compare2 = true
        this.compare3 = true
      }
    }
  },
  methods: {
    ...mapActions([
      'FETCH_UNCLEAN',
      'FETCH_CLEANED',
      'FETCH_TLIST'
    ]),
    toPage (val) {
      this.payload.page = val
      this.showMsg()
    },
    showDialog () {
      this.dialogVisible = true
      this.showMsg()
      this.chooseType()
    },
    showMsg () {
      this.payload.id = this.messageId
      this.FETCH_UNCLEAN(this.payload).then(() => {
        this.itemsOriginal = this.fetchOriginalData
      })
      this.FETCH_CLEANED(this.payload).then(() => {
        this.itemsCleaned = this.fetchCleanedData
      })
      this.FETCH_TLIST(this.payload).then(() => {
        this.items = this.fetchListData
      })
      let bodyH = document.body.offsetHeight
      let h = bodyH - 280
      this.height = 'max-height:' + h + 'px;overflow:auto;'
    },
    chooseType () {
      if (this.messageType === 'unTransformed') {
        this.options = [{
          value: '选项1',
          label: '原始-清洗'
        }]
      } else if (this.messageType === 'transformed') {
        this.options = [{
          value: '选项1',
          label: '原始-清洗'
        }, {
          value: '选项2',
          label: '原始-转换'
        }, {
          value: '选项3',
          label: '清洗-转换'
        }]
        if (this.preferences === 'O-T') {
          this.select_value = '原始-转换'
          this.compare1 = true
          this.compare2 = false
          this.compare3 = true
        } else if (this.preferences === 'C-T') {
          this.select_value = '清洗-转换'
          this.compare1 = false
          this.compare2 = true
          this.compare3 = true
        }
      }
    }
  }
}
</script>
<style scoped>
.table_box {
  display:inline-block;
  vertical-align:top;
  margin: 1%;
  width:47%;
}
.table_box {
  border-radius: 8px 8px 0 0;
  border: 1px solid #ddd;
}
.title {
  background-color: #20A0FF;
  color: #fff;
  text-align: center;
  margin: 0;
  line-height: 40px;
  border-radius: 8px 8px 0 0
}
th {
  word-wrap: break-word;
}
.table_box>.table {
  margin-bottom: 0px;
}
.compare_select {
  display: block;
  margin-left: 10px;
}
</style>
