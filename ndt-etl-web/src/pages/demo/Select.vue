<!--Pagination 分页-->
<template>
  <page title="多选框" :isLoading="false">
    <div slot="content">
      <div class="col-xs-4">
        <multiselect
          v-model="selected"
          :multiple="true"
          :options="options">
        </multiselect>
        <p class="pd20">参考https://github.com/monterail/vue-multiselect</p>
      </div>
    </div>
  </page>
</template>

<script>
import Page from '../../components/Page.vue'
import Multiselect from 'vue-multiselect'
import 'vue-multiselect/dist/vue-multiselect.min.css'
export default {
  data () {
    return {
      selected: null,
      options: ['list', 'of', 'options']
    }
  },
  components: {
    'multiselect': Multiselect,
    'page': Page
  }
}
</script>
