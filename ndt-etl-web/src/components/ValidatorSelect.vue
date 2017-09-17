<template>
  <div class="validator-select">
    <el-select v-model="validator.name" placeholder="请选择验证规则" :disabled="isDisabled" @change="onSelect()" size="small">
      <el-option
        v-for="item in validators"
        :label="item.name"
        :value="item.name">
      </el-option>
    </el-select>
    <el-input v-for="(item, index) in activeValidator.paramInputs" :disabled="isDisabled" :placeholder="item.name" v-model="validator.params[index]" />
    </el-input>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  created () {
    this.fetchValidators().then(() => {
      if (this.validator.name.length > 0) {
        this.activeValidator = this.validators.find((item) => item.name === this.validator.name)
      }
    })
  },
  props: {
    validator: { type: Object, default: () => { return {name: '', params: []} } },
    isDisabled: { type: Boolean, default: false }
  },
  data () {
    return {
      activeValidator: {
        paramInputs: []
      }
    }
  },
  computed: {
    ...mapGetters({
      validators: 'fetchValidatorsData'
    })
  },
  methods: {
    ...mapActions([
      'fetchValidators'
    ]),
    onSelect () {
      this.validator.params = this.activeValidator.paramInputs.map(() => '')
    }
  },
  watch: {
    'validator.name': function () {
      this.activeValidator = this.validators.find((item) => item.name === this.validator.name)
      if (!this.activeValidator) {
        this.activeValidator = {
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
.validator-select {
  display: flex;
}
.validator-select > .el-select {
  min-width: 80px;
}
.validator-select > .el-select, .validator-select > .el-input:not(:last-child) {
  margin-right: 1%
}
.el-select >.el-input.is-disabled .el-input__inner {
  color: #333;
}
.validator-select >.el-input.is-disabled .el-input__inner {
  color: #333;
}
</style>
