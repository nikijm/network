<template>
  <span>
    <button type="button" class="btn btn-xs btn-primary" @click="showLogs" v-if="showButton">
      查看详情
    </button>
    <el-dialog title="日志详情" :visible="computedVisible" @open="$emit('open')" @update:visible="updateVisible">
      <template v-if="logsData.operation === '清洗' || logsData.operation === '转换'">
        <p>
          源表:
          {{logsData.source}}
        </p>
        <p>
          目标表:
          {{logsData.target}}
        </p>
      </template>
      <va-table>
        <tr>
          <th v-if="logsData.operation !== '转换'">列名</th>
          <th v-if="logsData.operation === '转换'">源列</th>
          <th v-if="logsData.operation === '转换'">目标列</th>
          <th v-if="logsData.operation === '验证'">验证规则</th>
          <th v-if="logsData.operation === '清洗'">清洗规则</th>
        </tr>
        <tr v-for="log in logsArr">
          <td v-if="logsData.operation === '转换'">{{log.from}}</td>
          <td>{{log.name}}</td>
          <td v-if="logsData.operation === '验证'">
            <validatorSelect :validator="log.validator" :isDisabled="true"/>
          </td>
          <td v-if="logsData.operation === '清洗'">
            <string-operation-label :operations="log.operations" :isDisabled="true"></string-operation-label>
          </td>
        </tr>
      </va-table>
      <el-pagination
        @current-change="toPage"
        :current-page.sync="currPage"
        :page-size="15"
        layout="prev, pager, next"
        :total="logs ? logs.columns.length : 0">
      </el-pagination>
    </el-dialog>
  </span>
</template>

<script>
import VATable from './VATable.vue'
import StringOperationLabel from './StringOperationLabel.vue'
import validatorSelect from './validatorSelect.vue'

export default {
  props: {
    showButton: { type: Boolean, default: true },
    // 父组件可以通过此参数控制窗口开关
    dialogVisible: { type: Boolean, default: null },
    logsData: { type: Object, default: function () { return {} } }
  },
  components: {
    'va-table': VATable,
    'string-operation-label': StringOperationLabel,
    'validatorSelect': validatorSelect
  },
  watch: {
    logsData: function () {
      this.logs = JSON.parse(this.logsData.request)
      this.toPage(1)
    }
  },
  data () {
    return {
      innerVisible: false,
      logs: {},
      operations: [
        {
          'name': '替换',
          'params': [
            '+86',
            ''
          ]
        }
      ],
      logsArr: [],
      currPage: 1
    }
  },
  computed: {
    computedVisible () {
      if (this.dialogVisible === null) {
        return this.innerVisible
      }
      return this.dialogVisible
    }
  },
  created () {
    this.logs = JSON.parse(this.logsData.request)
  },
  methods: {
    toPage (val) {
      let i = val * 15 - 15
      let j = val * 15
      this.logsArr = this.logs.columns.slice(i, j)
    },
    showLogs () {
      this.updateVisible(true)
      this.currPage = 1
      this.toPage(1)
    },
    updateVisible (val) {
      this.innerVisible = val
      this.$emit('update:dialogVisible', val)
    }
  }
}
</script>
<style scoped>
  validatorSelect {
    color: #000;
  }
  p {
    line-height: 2;
    font-size: 16px;
    display: inline-block;
    width: 49%;
    position: relative;
    top: -10px;
  }
</style>
