<template>
  <div class="hello">
    <div>
      <h2>提示信息组</h2>
      <va-alert
        type="danger"
        :dismissible="true">
        <div slot="header">
          <h4><i class="icon fa fa-ban"></i> Alert!</h4>
        </div>
        <div slot="body">
          Danger alert preview. This alert is dismissable. A wonderful serenity has taken possession of my entire
          soul, like these sweet mornings of spring which I enjoy with my whole heart.
        </div>
      </va-alert>
      <va-alert
        type="info"
        :dismissible="true">
        <div slot="header">
          <h4><i class="icon fa fa-info"></i> Alert!</h4>
        </div>
        <div slot="body">
          Info alert preview. This alert is dismissable.
        </div>
      </va-alert>
      <va-alert
        type="warning"
        :dismissible="true">
        <div slot="header">
          <h4><i class="icon fa fa-warning"></i> Alert!</h4>
        </div>
        <div slot="body">
          Warning alert preview. This alert is dismissable.
        </div>
      </va-alert>
      <va-alert
        type="success"
        :dismissible="true">
        <div slot="header">
          <h4><i class="icon fa fa-check"></i> Alert!</h4>
        </div>
        <div slot="body">
          Success alert preview. This alert is dismissable.
        </div>
      </va-alert>
    </div>
  </div>
</template>

<script>
import VAAlert from '../components/VAAlert.vue'
export default {
  name: 'hello',
  data () {
    return {
      msgHello: 'Hello',
      msgBye: 'Bye',
      loading: false
    }
  },
  components: {
    'va-alert': VAAlert
  },
  methods: {
    clickBtn () {
      this.loading = !this.loading
      setTimeout(() => {
        this.loading = !this.loading
      }, 1000)
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>
