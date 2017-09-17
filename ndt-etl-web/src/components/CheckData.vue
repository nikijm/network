<template>
  <span>
    <button class="btn btn-primary" @click="showDialog">{{text}}</button>
    <el-dialog :title="dilTitle" :visible.sync="dialogVisible">
      <div style="width:100%;overflow:auto">
        <table style="width:auto" class="table table-hover table-nowrap">
        <thead>
          <tr>
            <th v-for="(value, key) in items.data[0]" width="125px">
              {{key}}
            </th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="list in items.data">
            <td v-for="(value, key) in list">
              {{value}}
            </td>
          </tr>
         </tbody>
    </table>
      </div>
      <el-pagination
        @current-change="toPage"
        :current-page="items.paging.current"
        :page-size="items.paging.page_size"
        layout="prev, pager, next"
        :total="items.paging.total">
      </el-pagination>
    </el-dialog>
  </span>
</template>

<script>
import VATable from './VATable.vue'
import { mapGetters, mapActions } from 'vuex'

export default {
  props: ['text', 'messageId', 'messageType'],
  data () {
    return {
      dilTitle: '原始数据详情',
      dialogVisible: false,
      payload: {
        id: 1,
        page: 1
      },
      items: {
        paging: {
          current: 0,
          total: 0,
          page_size: 15
        },
        data: []
      }
    }
  },
  computed: {
    ...mapGetters([
      'fetchOriginalData',
      'fetchCleanedData'
    ])
  },
  components: {
    'va-table': VATable
  },
  methods: {
    ...mapActions([
      'FETCH_UNCLEAN',
      'FETCH_CLEANED'
    ]),
    showDialog () {
      this.dialogVisible = true
      this.showMsg()
    },
    showMsg () {
      this.payload.id = this.messageId
      this.messageType === 'unClean' ? this.dilTitle = '原始数据详情' : this.dilTitle = '清洗后数据详情'
      if (this.messageType === 'unClean') {
        this.FETCH_UNCLEAN(this.payload).then(() => {
          this.items = this.fetchOriginalData
        })
      } else {
        this.FETCH_CLEANED(this.payload).then(() => {
          this.items = this.fetchCleanedData
        })
      }
    },
    toPage (val) {
      this.payload.page = val
      this.showMsg()
    }
  }
}
</script>
<style scoped>
  th { word-wrap: break-word;}
  td { word-wrap: break-word;}
</style>
