<template>
  <input 
    v-if="'selectValidator'==='Validator'"
    :placeholder="placeholder"
    :type="type" 
    :name="validatorName"
    :value="value"
    >
  </input>
</template>

<script>
export default {
  data () {
    return {
      name: 'va-paramInputs',
      selectValidator: {
        type: String,
        default: ''
      },
      Validator: {
        type: String,
        default: ''
      },
      placeholder: {
        type: String,
        default: ''
      },
      type: {
        type: String,
        default: ''
      },
      validatorName: {
        type: String,
        default: ''
      },
      value: {
        type: String,
        default: ''
      }
    }
  }
}
</script>
