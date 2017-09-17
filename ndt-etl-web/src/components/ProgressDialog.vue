<template>
  <el-dialog class="progress-dialog" :class="status" title="" :visible="visible" :close-on-click-modal="canClose" :close-on-press-escape="canClose" :show-close="canClose" @update:visible="updateVisible" @open="open" @close="$emit('close')">
    <el-progress type="circle" :percentage="percentage" :status="status"></el-progress>
    <p v-if="percentage == 100">{{SuccessText}}</p>
    <p v-if="isError">{{errorText}}</p>
    <p v-if="!isError && percentage < 100">{{progressText}}</p>

    <slot name="error" v-if="isError"></slot>
    <slot name="success" v-if="percentage == 100"></slot>
  </el-dialog>
</template>

<script>
export default {
  props: {
    visible: { type: Boolean, default: false },
    isSuccess: { type: Boolean, default: false }, // 操作是否完成
    isError: { type: Boolean, default: false }, // 操作是否出错
    progressText: { type: String, default: '请求中...' },
    SuccessText: { type: String, default: '请求完成' },
    errorText: { type: String, default: '请求出错' }
  },
  data () {
    return {
      percentage: 0, // 完成百分比
      fake: {
        defaultInterval: 80,
        breakPoint1: 80,
        breakPoint2: 92,
        slowSeed: 2
      }
    }
  },
  computed: {
    status () {
      if (this.percentage === 100) {
        return 'success'
      }
      if (this.isError) {
        return 'exception'
      }
      return ''
    },
    canClose () {
      return this.isError || this.percentage === 100
    }
  },
  methods: {
    updateVisible (val) {
      this.$emit('update:visible', val)
    },
    open () {
      this.percentage = 0
      this.fake.slowSeed = Math.ceil(Math.random() * 5)
      this.fake.breakPoint1 = Math.ceil(Math.random() * 8) * 10
      this.tick()
    },
    tick () {
      // 请求出错，不转了
      if (this.isError) {
        return
      }
      // 请求已完成时，赶紧转完
      if (this.isSuccess) {
        this.percentage = 100
        return
        // this.percentage += Math.ceil(Math.random() * 5)
        // if (this.percentage >= 100) {
        //   this.percentage = 100
        //   return
        // }
        // setTimeout(this.tick, this.fake.defaultInterval)
        // return
      }

      // 请求未完成时，假装在转圈
      let interval
      this.percentage ++

      if (!this.isSuccess) {
        if (this.percentage < this.fake.breakPoint1) {
          interval = this.percentage * (this.percentage / 10 + this.fake.slowSeed)
        } else if (this.percentage < this.fake.breakPoint2) {
          interval = this.percentage * this.percentage
        } else {
          interval = this.percentage * this.percentage * this.percentage
        }
      }

      setTimeout(this.tick, interval)
    }
  }
}
</script>

<style>
.progress-dialog {
  text-align: center;
}
.progress-dialog p{
  font-size: 1.7rem;
  margin-top: 1rem;
  font-weight: bold;
  color: rgb(32, 160, 255);
}
.progress-dialog.success p {
  color: #13ce66;
}
.progress-dialog.exception p {
  color: #ff4949;
}
</style>
