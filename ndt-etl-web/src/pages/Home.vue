<template>
  <div>
    <div class="wrapper">
      <va-navibar></va-navibar>
      <va-slider :slideMenuItems="menuItems"></va-slider>
      <va-content-wrap></va-content-wrap>
      <string-operation-dialog />
    </div>
  </div>
</template>

<script>
import VANaviBar from '../NaviBar.vue'
import VASlider from '../Slider.vue'
import VAContentWrap from '../ContentWrap.vue'
import StringOperationDialog from '../components/StringOperationDialog.vue'

export default {
  name: 'home',
  data () {
    return {
      menuItems: []
    }
  },
  components: {
    'va-navibar': VANaviBar,
    'va-slider': VASlider,
    'va-content-wrap': VAContentWrap,
    'string-operation-dialog': StringOperationDialog
  },
  mounted () {
    let menu = JSON.parse(sessionStorage.getItem('menuStr'))
    this.menuItems = menu.menusArr
  }
}
</script>
