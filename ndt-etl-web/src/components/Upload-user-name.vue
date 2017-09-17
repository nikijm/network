<template>
    <td v-text="this.name"></td>
</template>

<script>
export default {
  props: ['sheet'],
  data () {
    return {
      payload: {
        page: 1,
        search: ''
      }
    }
  },
  computed: {
    name: function () {
      var name = ''
      if (this.sheet.admin) {
        if (this.sheet.admin.name != null) {
          name = this.sheet.admin.name + '（管理员）'
        } else {
          if (this.sheet.user) {
            if (this.sheet.user.name != null) {
              name = this.sheet.user.name
            }
          }
        }
      } else {
        if (this.sheet.user) {
          if (this.sheet.user.name != null) {
            name = this.sheet.user.name
          }
        }
      }
      return name
    }
  }
}
</script>
