<template>
  <div :class="this['className']">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: 'column',
  props: {
    'className': {
      type: String,
      default: ''
    }
  },
  created () {

  }
}
</script>
