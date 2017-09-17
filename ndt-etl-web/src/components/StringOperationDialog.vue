<template>
  <el-dialog class="clean-rules-dialog" title="编辑规则" :visible.sync="visible"  @close="close">

    <va-table>
      <tr>
        <th width="60%">规则</th>
        <th>测试</th>
      </tr>
      <tr>
        <td>
          源数据
        </td>
        <td>{{ input }}</td>
      </tr>
      <tr v-for="(operation, index) in operations">
        <td>
          <string-operation-select :operation.sync="operation" @close="remove(index)" :showClose="true" />
        </td>
        <td>{{output[index]}}</td>
      </tr>
      <tr>
        <td>
          <el-button type="primary" icon="plus" size="small" @click="add()">
          </el-button>

        </td>
        <td></td>
      </tr>
    </va-table>
    <el-button type="warning" @click="test">测试
    </el-button>
    <el-button type="primary" @click="confirm">确认
    </el-button>
  </el-dialog>
</template>

<script>
import VATable from './VATable.vue'
import StringOperationSelect from './StringOperationSelect.vue'
import { services } from '../vuex/api'

export default {
  created () {
    this.$bus.$on('string-operation-dialog-open', (args) => {
      this.visible = true
      this.input = args.input
      if (args.operations) {
        this.operations = args.operations.map((operation) => {
          return JSON.parse(JSON.stringify(operation))
        })
        if (this.operations.length === 0) {
          this.add()
        }
      }
      this.callback = args.callback
    })
  },
  data () {
    return {
      visible: false,
      operations: [{name: '', params: []}],
      input: '+86 15555555555',
      output: [],
      callback: function () {}
    }
  },
  computed: {
  },
  methods: {
    close () {
      this.output.length = 0
    },
    add () {
      this.operations.push({name: '', params: []})
    },
    remove (index) {
      this.operations.splice(index, 1)
    },
    test () {
      this.operations = this.operations.filter((operation) => operation.name !== '')
      services.etlRules.testOperations({
        input: this.input,
        operations: this.operations
      }).then((data) => {
        this.output = data.output
      })
    },
    confirm () {
      this.callback(this.operations.slice(0))
      this.visible = false
    }
  },
  components: {
    'va-table': VATable,
    'string-operation-select': StringOperationSelect
  }
}
</script>

<style>

</style>
