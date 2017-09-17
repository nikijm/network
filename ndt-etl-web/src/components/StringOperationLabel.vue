<template>
  <div class="string-operation-Label" :class="{disabled: isDisabled}" @click="openDialog">
    <span v-if="operations.length == 0 && isDisabled">
    </span>
    <span v-else-if="operations.length == 0">
      <button class="btn btn-primary btn-sm">点击添加规则</button>
    </span>
    <span v-else>
      {{readableOperations}}
    </span>
  </div>
</template>

<script>
export default {
  props: {
    operations: { type: Array, default: () => { return [] } },
    test: {type: String, default: ''},
    isDisabled: { type: Boolean, default: false }
  },
  data () {
    return {
      activeOperation: {
        paramInputs: []
      }
    }
  },
  computed: {
    readableOperations () {
      let r = (str) => {
        if (str === ' ') {
          return '空格'
        }
        return str
      }
      let arr = this.operations.map((operation) => {
        if (operation.name === '替换') {
          if (operation.params[1].length === 0) {
            return '删除' + r(operation.params[0])
          }
          return '将' + r(operation.params[0]) + '替换为' + r(operation.params[1])
        } else if (operation.name === '插入') {
          return '在' + r(operation.params[0]) + '位置插入' + r(operation.params[1])
        } else if (operation.name === '只替换一个') {
          if (operation.params[1].length === 0) {
            return '删除第一个' + r(operation.params[0])
          }
          return '将第一个' + r(operation.params[0]) + '替换为' + r(operation.params[1])
        } else if (operation.params.length === 1) {
          return operation.name + r(operation.params[0])
        } else if (operation.params.length === 0) {
          return operation.name
        } else {
          return JSON.stringify(operation)
        }
      })
      return arr.join(',')
    }
  },
  methods: {
    openDialog () {
      this.$bus.$emit('string-operation-dialog-open', {
        input: this.test,
        operations: this.operations,
        callback: (operations) => {
          this.operations.splice(0)
          operations.forEach((operation) => {
            if (operation.name === '') {
              return
            }
            this.operations.push(operation)
          })
        }
      })
    }
  },
  components: {
  }
}
</script>

<style>
.string-operation-select {
  display: flex;
}
.string-operation-select > .el-select {
  min-width: 80px;
}
.string-operation-select > .el-select, .string-operation-select > .el-input:not(:last-child) {
  margin-right: 1%
}
.disabled { pointer-events: none; }
</style>
