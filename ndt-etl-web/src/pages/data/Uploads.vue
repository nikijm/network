<template>
  <Page title="上传文档列表" :isLoading="isLoading">
    <div slot="tools">
     <!--  <el-input v-if="userMsg !== 'user'" v-model="form.search" placeholder="请输入文件名或用户名" class="tools-button" @keyup.enter.native="search">
        <el-button slot="append" icon="search" @click="search"></el-button>
      </el-input>
      <el-input v-else v-model="form.search" placeholder="请输入文件名" class="tools-button" @keyup.enter.native="search">
        <el-button slot="append" icon="search" @click="search"></el-button>
      </el-input> -->
       <el-select v-model="payload.orgId" placeholder="请选择所属机构" clearable :filterable="true" :allow-creat="true" @change="orgChange" >
             <el-option
               v-for="item in fetchUploadsData.orgs"
               :label="item.name"
               :value="item.id">
             </el-option>
      </el-select>
    </div>
    <div slot="content">
    <div style="min-width: 100%;">
    <div class="table-head">
      <table class="table table-hover" style="table-layout: fixed;">
          <colgroup>
              <col style="width: 20%;"/>
              <col style="width: 35%;"/>
          </colgroup>
          <thead>
              <tr>
              <th>上传时间</th>
              <th >机构名称</th>
              <th>文件类型</th>
              <th >上传用户</th>
              <th >文件名</th>
              <th>操作</th>
              </tr>
          </thead>
      </table>
      </div>
      <!-- <div class="row rowContent" ref='row'> -->
      <div class="table-body" ref="row2">
      <table class="table table-hover" style="table-layout: fixed;">
          <colgroup><col style="width: 20%;"/><col style="width: 35%;"/></colgroup>
          <tbody>
          <tr v-for="item in fetchUploadsData.dataFiles">
            <td> {{item.uploadDate}} </td>
            <td> {{item.orgName}} </td>
            <td> {{item.dataFileTypeName}} </td>
            <td> {{item.uploadUserName}} </td>
            <td> {{item.fileName}} </td>
            <td>
              <button class="btn btn-success btn-xs" @click="download(item.etlDatafileId)">下载</button>
            </td>
          </tr>
          </tbody>
      </table>
      </div>
    </div>
    <!--   <table class="table table-hover" style="table-layout: fixed;">
        <thead>
          <tr>
            <th>上传时间</th>
            <th width="30%">文件名</th>
            <th>文件类型</th>
            <th width="180">上传用户</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in fetchUploadsData.dataFiles">
            <td> {{item.uploadDate}} </td>
            <td> {{item.fileName}} </td>
            <td> {{item.dataFileTypeName}} </td>
            <td> {{item.uploadUserName}} </td>
            <td>
              <button class="btn btn-success btn-xs" @click="download(item.etlDatafileId)">下载</button>
            </td>
          </tr>
        </tbody>
      </table> -->
      <el-pagination style='margin-top: 10px' v-show='fetchUploadsData.dataFiles.length>0'
        @current-change="toPage"
        :current-page="fetchUploadsData.paging.current"
        layout="prev, pager, next, jumper"
        :total="fetchUploadsData.paging.total"
        :page-size="fetchUploadsData.paging.page_size">
      </el-pagination>
      <el-dialog title="上传文档详情" :visible.sync="dialogVisible">
        <table class="table table-hover">
          <thead>
          <tr>
            <th>序号</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="item in uploadSheets">
            <td v-text="item.etlDatafileId"></td>
            <td v-text="item.status"></td>
            <td>
              <router-link tag="a" class="btn btn-primary btn-xs" :to="{name: 'sheet',params:{id:item.etlDatafileId}}">
                查看
              </router-link>
            </td>
          </tr>
          <tr v-if="uploadSheets==null">
            <td colspan="3">载入中</td>
          </tr>
          <tr v-if="uploadSheets && uploadSheets.length==0">
            <td colspan="3">无</td>
          </tr>
          </tbody>
        </table>
      </el-dialog>
    </div>
  </Page>

</template>

<script>
import Page from '../../components/Page.vue'
import VATable from '../../components/VATable.vue'
import uploadsService from '../../vuex/api/services/uploads'
import { mapGetters, mapActions } from 'vuex'
import UploadUserName from '../../components/Upload-user-name.vue'

export default {
  data () {
    return {
      form: {
        search: '',
        orgId: ''
      },
      isLoading: true,
      payload: {
        current: 1,
        search: '',
        orgId: ''
      },
      uploadSheets: null,
      dialogVisible: false,
      screenHeight: document.body.clientHeight
    }
  },
  created () {
    this.load()
  },
  mounted () {
    this.$refs.row2.style.height = this.screenHeight - 220 + 'px'
  },
  computed: {
    ...mapGetters([
      'fetchUploadsData'
    ]),
    userMsg () {
      return sessionStorage.getItem('userFlag')
    }
  },
  methods: {
    ...mapActions([
      'FETCH_UPLOADS'
    ]),
    load () {
      this.isLoading = true
      this.FETCH_UPLOADS(this.payload).then(() => {
        this.isLoading = false
      })
      .catch(this.$promptReload)
    },
    search: function () {
      this.payload.searchKey = this.form.search
      this.payload.current = 1
      this.load()
    },
    toPage (val) {
      this.payload.current = val
      this.load()
    },
    view (upload) {
      this.uploadSheets = null
      this.dialogVisible = true
      uploadsService.getUploadSheets(upload.id)
      .then((data) => { this.uploadSheets = data })
    },
    download (id) {
      window.open('/api/uploads/' + id + '/download')
    },
    orgChange () {
      this.payload.current = 1
      // console.log(this.form.orgId)
      this.load()
    }
  },
  components: {
    'Page': Page,
    'va-table': VATable,
    'upload-user-name': UploadUserName
  }
}
</script>
<style scoped>
  .pr20{
    position: relative;
    right: 20px;
  }
  th {
    white-space: nowrap
  }
.table-head{padding-right:17px;}
.table-head .table{ margin-bottom: 0 }
.table-body{width:100%;overflow-y:auto;}
.table-head table,.table-body table{width:100%;}
</style>
