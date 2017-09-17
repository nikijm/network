<template>
  <router-link tag="li" v-if="route" :to="route" v-on:click.native="setMenu" active-class="active">
    <a href="#">
      <i :class="icon"></i> <span>{{ name }}</span>
      <span class="pull-right-container" v-show="badge">
        <small class="label pull-right" :class="[badge.type==='String'?'bg-green':'label-primary']">{{ badge.data }}</small>
      </span>
    </a>
  </router-link>
  <li :class="getType" v-else>
    {{ isHeader ? name : '' }}
    <a href="#" v-if="!isHeader">
      <i :class="icon"></i> <span>{{ name }}</span>
      <span class="pull-right-container">
        <small v-if="badge && badge.data" class="label pull-right" :class="[badge.type==='String'?'bg-green':'label-primary']">{{ badge.data }}</small>
        <i v-else class="fa fa-angle-left pull-right"></i>
      </span>
    </a>
    <ul class="treeview-menu" v-if="items.length > 0">
      <router-link tag="li" v-for="item in items" :to="item.route" v-if="item.route" v-on:click.native="setMenu(item)" active-class="active">
        <a :class="$route.path == item.route ? 'current' : ''">
          <i :class="item.icon"> {{ item.name }}</i>
        </a>
      </router-link>
      <li v-else>
        <a>
          <i :class="item.icon"> {{ item.name }}</i>
        </a>
      </li>
    </ul>
  </li>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'va-slide-item',
  props: {
    type: {
      type: String,
      default: 'item'
    },
    isHeader: {
      type: Boolean,
      default: false
    },
    icon: {
      type: String,
      default: ''
    },
    name: {
      type: String
    },
    badge: {
      type: Object,
      default () {
        return {}
      }
    },
    items: {
      type: Array,
      default () {
        return []
      }
    },
    route: {
      type: String
    },
    link: {
      type: String,
      default: ''
    }
  },
  created () {

  },
  computed: {
    getType () {
      if (this.isHeader) {
        return 'header'
      }
      return this.type === 'item' ? '' : 'treeview'
    },
    ...mapGetters([
      'currentMenu'
    ])
  },
  methods: {
    setMenu (item) {
      let menu = {}
      if (this.route) {
        menu = {
          parent: {
            icon: this.icon,
            name: this.name
          },
          icon: '',
          name: ''
        }
      } else {
        menu = {
          'parent': {
            'icon': this.icon,
            'name': this.name
          },
          'icon': item.icon,
          'name': item.name
        }
      }
      this.$store.dispatch('setMenu', menu)
      sessionStorage.setItem('menu', JSON.stringify(menu))
    }
  }

}
</script>
