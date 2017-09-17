<template>
  <div>
    {{ totalDemo }}
    <column className="col">column</column>
  </div>
</template>

<script>
  import { mapGetters, mapActions } from 'vuex'
  import Column from '../../components/Column'
  export default {
    mounted () {
      this.FETCH_DATA()
    },
    computed: {
      ...mapGetters([
        'totalDemo'
      ])
    },
    methods: {
      ...mapActions([
        'FETCH_DATA'
      ])
    },
    components: {
      'column': Column
    }
  }
</script>
