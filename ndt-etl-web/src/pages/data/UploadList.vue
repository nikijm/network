<template>
  <page title="文档列表" :isLoading="isLoading">
    <div slot="content">
      <el-form :model="uploadForm" label-width="100px">
        <el-row>
          <el-col :span="7">
            <el-form-item label="所属机构">
              {{orgName}}
            </el-form-item>
          </el-col>
          <el-col :span="7">
            <el-form-item label="文件类型">
               <el-select v-model="uploadForm.type" placeholder="请选择文件类型" clearable :filterable="true" :allow-creat="true">
                 <el-option
                    v-for="item in fileType"
                    v-if="orgId === item.etlOrgId"
                    :label="item.name"
                    :value="item.etlFileTypeId">
                 </el-option>
               </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="7">
            <el-form-item label="文件">
              <input type="file" id="file" @change="fileChoose">
            </el-form-item>
          </el-col>
          <el-col :span="3">
            <button class="btn btn-primary" type="button" @click="submitForm">提交</button>
            <button class="btn btn-default" type="reset" @click="resetForm">重置</button>
          </el-col>
        </el-row>
      </el-form>
      <hr />
      <table class="table table-hover" style="table-layout: fixed;">
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
      </table>
      <el-pagination
        @current-change="toPage"
        :current-page="fetchUploadsData.paging.current"
        layout="prev, pager, next, jumper"
        :total="fetchUploadsData.paging.total"
        :page-size="fetchUploadsData.paging.page_size">
      </el-pagination>
    </div>
  </page>
</template>
<script>
import Page from '../../components/Page.vue'
import { services } from '../../vuex/api'
import { mapGetters, mapActions } from 'vuex'

export default {
  data () {
    return {
      uploadForm: {
        type: '',
        file: '',
        orgs: ''
      },
      payload: {
        current: 1,
        searchKey: ''
      },
      isLoading: false,
      fileType: [],
      orgs: [],
      orgName: '',
      orgId: ''
    }
  },
  components: {
    'page': Page
  },
  created () {
    this.load()
  },
  computed: {
    ...mapGetters([
      'fetchUploadsData'
    ])
  },
  methods: {
    ...mapActions([
      'FETCH_UPLOADS'
    ]),
    submitForm () {
      if (this.uploadForm.file === '') {
        this.$alert('请选择文件上传', '错误', {
          confirmButtonText: '确定',
          type: 'error'
        })
      } else {
        const formdata = new FormData()
        let type = this.uploadForm.type
        formdata.append('file', this.uploadForm.file)
        formdata.append('dataFileTypeId', type)
        formdata.append('orgId', this.orgId)
        this.isLoading = true
        services.uploads.upload(formdata).then((res) => {
          this.isLoading = false
          this.$alert('上传成功', '成功', {
            confirmButtonText: '确定',
            type: 'success'
          })
        })
        .catch((error) => {
          this.isLoading = false
          this.$alert(error.response.data.message, '失败', {
            confirmButtonText: '确定',
            type: 'warning'
          })
        })
      }
    },
    load () {
      this.isLoading = true
      this.FETCH_UPLOADS(this.payload)
      .then(() => {
        this.isLoading = false
        this.fileType = this.fetchUploadsData.dataFileTypes
        this.orgs = this.fetchUploadsData.orgs
        // 外部用户默认选定所属机构 并且不能更改
        this.uploadForm.orgs = this.fetchUploadsData.user.orgId
        this.orgName = this.fetchUploadsData.user.orgName
        this.orgId = this.fetchUploadsData.user.orgId
      })
    },
    resetForm (event) {
      event.target.value = null
      this.uploadForm.file = ''
      this.uploadForm.type = ''
    },
    fileChoose (event) {
      this.uploadForm.file = event.target.files[0]
      let fileName = this.uploadForm.file.name
      let ldot = fileName.lastIndexOf('.')
      let type = fileName.substring(ldot + 1)
      // 文件大小 单位M surplusSize为上传文件剩余配额 应从后台获取
      let fileSize = this.uploadForm.file.size / 1024 / 1024
      let surplusSize = 500
      if (fileSize > 500) {
        this.$alert('文件大于500M,请联系管理员处理', '错误', {
          confirmButtonText: '确定',
          type: 'error'
        })
        event.target.value = null
        this.uploadForm.file = ''
        return
      } else if (fileSize > surplusSize) {
        this.$alert('超出今日上传文件配额,请联系管理员处理', '错误', {
          confirmButtonText: '确定',
          type: 'error'
        })
        event.target.value = null
        this.uploadForm.file = ''
        return
      } else if (type !== 'xls' && type !== 'xlsx') {
        this.$alert('请选择xls、xlsx格式的文件上传', '错误', {
          confirmButtonText: '确定',
          type: 'error'
        })
        event.target.value = null
        this.uploadForm.file = ''
      }
    },
    orgChange () {
      this.uploadForm.type = ''
    },
    toPage (val) {
      this.payload.current = val
      this.load()
    },
    download (id) {
      window.location.href = '/api/uploads/' + id + '/download'
    }
  }
}
</script>
<style scoped>
  .btn-primary {
    margin-left: 10px;
  }
</style>
