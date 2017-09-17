<template>
  <div class="string-operation-select">
    <el-select v-model="operation.name" placeholder="请选择字符串操作" >
      <el-option
        v-for="item in allOperations"
        :label="item.name"
        :value="item.name"
        @click.native="onSelect"
        >
      </el-option>
    </el-select>
    <el-input v-for="(item, index) in activeOperation.paramInputs" :placeholder="item.name" v-model="operation.params[index]" />
    <el-button type="danger" icon="close" size="small" @click="$emit('close')" v-if="showClose">
    </el-button>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  created () {
    this.fetchOperations().then(() => {
      // 修复添加，删除，再打开时无输入框的bug
      if (this.operation.name.length > 0) {
        this.activeOperation = this.allOperations.find((item) => item.name === this.operation.name)
        if (!this.activeOperation) {
          this.activeOperation = {
            paramInputs: []
          }
        }
      }
    })
  },
  props: {
    // 输出用于传送到后端的对象
    operation: { type: Object, default: () => { return {} } },
    showClose: { type: Boolean, default: false }
  },
  data () {
    return {
      activeOperation: {
        paramInputs: []
      }
    }
  },
  computed: {
    ...mapGetters({
      allOperations: 'fetchOperationsData'
    })
  },
  methods: {
    ...mapActions([
      'fetchOperations'
    ]),
    onSelect () {
      this.operation.params = this.activeOperation.paramInputs.map(() => '')
    }
  },
  watch: {
    'operation.name': function (newVal) {
      this.activeOperation = this.allOperations.find((item) => item.name === this.operation.name)
      if (!this.activeOperation) {
        this.activeOperation = {
          paramInputs: []
        }
      }
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
</style>
