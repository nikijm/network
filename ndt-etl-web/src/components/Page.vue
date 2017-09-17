<template>
  <div class="page">

    <div class="main" v-if="isBox">

      <div class="box-header">

        <slot name="icon-title"></slot>
        <h3 class="box-title">{{ title }}</h3>
        <div class="box-tools" v-if="$slots.tools">
          <slot name="tools"></slot>
        </div>

      </div>

      <div class="box-body">
        <slot name="content"></slot>
      </div>

      <div v-if="$slots.footer" class="box-footer">
        <slot name="footer"></slot>
      </div>

      <div v-if="this.isLoading" class="overlay">
        <i class="fa fa-refresh fa-spin"></i>
      </div>
    </div>
    <div v-else>
      <slot name="content"></slot>
    </div>
    
  </div>
</template>

<script>
export default {
  props: {
    isLoading: { type: Boolean, default: false },
    isBox: { type: Boolean, default: true },
    title: { type: String }
  },
  methods: {
  }
}
</script>
