<template>
  <page title="上传" :isLoading="isLoading">
    <div slot="content">
      <el-form :model="uploadForm" label-width="100px">
      <el-row>
      <el-col :span="18">
        <el-form-item label="所属机构">
           <el-select v-model="uploadForm.orgs" placeholder="请选择所属机构" clearable :filterable="true" :allow-creat="true" @change="orgChange" style="width: 398px">
             <el-option
               v-for="item in this.orgs"
               :label="item.name"
               :value="item.id">
             </el-option>
           </el-select>
        </el-form-item>
      </el-col>
      </el-row>
        <el-form-item label="文件类型">
           <el-select v-model="uploadForm.type" placeholder="请选择文件类型" clearable :filterable="true" :allow-creat="true" style="width: 398px">
             <el-option
                v-if="uploadForm.orgs === item.etlOrgId "
                v-for="item in this.fileType"
                :label="item.name"
                :value="item.etlFileTypeId">
             </el-option>
           </el-select>
        </el-form-item>
        <el-form-item label="选择文件">
          <el-radio class="radio" v-model="radio" label="0">
            文件上传<span style='visibility:hidden;'>夹</span> <input type="file" id="file" @change="fileChoose">
          </el-radio>
          <br/>
          <el-radio class="radio" v-model="radio" label="2">
            文件链接 <span style='visibility:hidden;'>夹</span><el-input v-model='fileLink '></el-input>
          </el-radio>
          <br/>
          <el-radio class="radio" v-model="radio" label="1">
            文件夹链接 <el-input v-model='folderLink'></el-input>
          </el-radio>
        </el-form-item>
        <el-form-item>
          <button class="btn btn-primary" type="button" @click="submitForm">提交</button>
          <button class="btn btn-default" type="reset" @click="resetForm">重置</button>
        </el-form-item>
      </el-form>
    </div>
  </page>
</template>
<script>
import Page from '../../components/Page.vue'
import { services } from '../../vuex/api'
export default {
  data () {
    return {
      uploadForm: {
        type: '',
        file: '',
        orgs: ''
      },
      radio: '0',
      orgs: [],
      payload: {
        page: 1,
        search: ''
      },
      isLoading: false,
      fileType: [],
      // 文件夹链接
      folderLink: '',
      // 文件链接
      fileLink: ''
    }
  },
  components: {
    'page': Page
  },
  created () {
    this.getFileTypes()
  },
  methods: {
    submitForm () {
      console.log(this.radio)
      if (this.uploadForm.file === '' && this.radio === '0') {
        this.$alert('请选择文件上传', '错误', {
          confirmButtonText: '确定',
          type: 'error'
        })
      } else {
        const formdata = new FormData()
        let type = this.uploadForm.type
        if (typeof this.uploadForm.file === 'object' && this.radio === '0') {
          formdata.append('file', this.uploadForm.file)
        }
        formdata.append('dataFileTypeId', type)
        formdata.append('orgId', this.uploadForm.orgs)
        formdata.append('uploadType', this.radio)
        formdata.append('folderLink', this.folderLink)
        formdata.append('fileLink', this.fileLink)
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
    getFileTypes () {
      services.uploads.list()
      .then((data) => {
        this.fileType = data.dataFileTypes
        this.orgs = data.orgs
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
    }
  }
}
</script>
<style scoped>
  .checkbox, .radio{
    margin-top: 0px;
    margin-bottom: 10px;
  }
  input[type=file]{
    display: inline-block;
  }
</style>
