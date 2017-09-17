<template>
  <page title="配置">
    <div slot="tools">
      <el-select v-model="ajaxData.level" style="float:left;padding-right:10px" placeholder="按组织过滤">
        <el-option
          v-for="item in orgOtions"
          :label="item.value" :value="item.index">
        </el-option>
      </el-select>
    </div>
    <div slot="content">
      <div>
        <el-radio-group v-model="radio">
          <el-radio :label="0">&nbsp;类型I</el-radio>
          <el-radio :label="1">&nbsp;类型II</el-radio>
          <el-radio :label="2">&nbsp;类型III</el-radio>
          <el-radio :label="3">&nbsp;类型IV</el-radio>
        </el-radio-group>
      </div>
      <div class="info" style="padding:15px;background:#f8f8f8;">
        <div class="row">
            <div class="col-md-4">所有文件：<a href="#">60</a></div>
            <div class="col-md-4">&nbsp;</div>
            <div class="col-md-4">&nbsp;</div>
        </div>
        <div class="row">
            <div class="col-md-4">缓存表：<a href="#">TABLE_BUFF_XXX</a></div>
            <div class="col-md-4">清洗表：<a href="#">TABLE_WASH_XXX</a></div>
            <div class="col-md-4">转换表：<a href="#">TABLE_TRANS_XXX</a></div>
        </div>
        <div class="row">
            <div class="col-md-4">缓存中：<a href="#">10</a></div>
            <div class="col-md-4">清洗中：<a href="#">6</a></div>
            <div class="col-md-4">转换中：<a href="#">12</a></div>
        </div>
      </div>
      <div class="row">
        <table class="table table-hover table-fixed">
          <thead>
              <tr>
                  <th width="20%">列</th>
                  <th width="15%">校验规则</th>
                  <th>清洗规则</th>
                  <th width="20%">目标列</th>
                  <th style="width:60px">&nbsp;</th>
              </tr>
          </thead>
          <tbody>
            <tr>
              <td>
                <input type="text" class="form-control input-sm" value="AD_CLIENT_ID">
              </td>
              <td>
                <validatorSelect :validator="validatePayload.columns.validator" v-if="validatePayload.columns" />
              </td>
              <td>
                <a href="#" class="btn btn-primary btn-sm">设置规则</a>
              </td>
              <td>
                <select class="form-control input-sm">
                    <option value="">AD_CLIENT_ID</option>
                    <option value="">AD_REGION</option>
                    <option value="">AD_NAME</option>
                    <option value="">AD_MOBILE</option>
                </select>
              </td>
              <td>
                  <a href="#" class="btn btn-danger btn-sm">删除</a>
              </td>
            </tr>
            <tr>
              <td>
                <input type="text" class="form-control input-sm" value="AD_CLIENT_ID">
              </td>
              <td>
                  <validatorSelect :validator="validatePayload.columns.validator" v-if="validatePayload.columns" />
              </td>
              <td>
                  <a href="#" class="btn btn-primary btn-sm">设置规则</a>
              </td>
              <td>
                  <select class="form-control input-sm">
                      <option value="">AD_CLIENT_ID</option>
                      <option value="">AD_REGION</option>
                      <option value="">AD_NAME</option>
                      <option value="">AD_MOBILE</option>
                  </select>
              </td>
              <td>
                  <a href="#" class="btn btn-danger btn-sm">删除</a>
              </td>
            </tr>
            <tr>
              <td>
                <input type="text" class="form-control input-sm" value="AD_CLIENT_ID">
              </td>
              <td>
                <validatorSelect :validator="validatePayload.columns.validator" v-if="validatePayload.columns" />
              </td>
              <td>
                  <a href="#" class="btn btn-primary btn-sm">设置规则</a>
              </td>
              <td>
                  <select class="form-control input-sm">
                      <option value="">AD_CLIENT_ID</option>
                      <option value="">AD_REGION</option>
                      <option value="">AD_NAME</option>
                      <option value="">AD_MOBILE</option>
                  </select>
              </td>
              <td>
                  <a href="#" class="btn btn-danger btn-sm">删除</a>
              </td>
            </tr>
            <tr>
              <td>
                <input type="text" class="form-control input-sm" value="AD_CLIENT_ID">
              </td>
              <td>
                  <validatorSelect :validator="validatePayload.columns.validator" v-if="validatePayload.columns" />
              </td>
              <td>
                  <a href="#" class="btn btn-primary btn-sm">设置规则</a>
              </td>
              <td>
                  <select class="form-control input-sm">
                      <option value="">AD_CLIENT_ID</option>
                      <option value="">AD_REGION</option>
                      <option value="">AD_NAME</option>
                      <option value="">AD_MOBILE</option>
                  </select>
              </td>
              <td>
                  <a href="#" class="btn btn-danger btn-sm">删除</a>
              </td>
            </tr>
            <tr>
              <td>
                <input type="text" class="form-control input-sm" value="AD_CLIENT_ID">
              </td>
              <td>
                  <validatorSelect :validator="validatePayload.columns.validator" v-if="validatePayload.columns" />
              </td>
              <td>
                  <a href="#" class="btn btn-primary btn-sm">设置规则</a>
              </td>
              <td>
                  <select class="form-control input-sm">
                      <option value="">AD_CLIENT_ID</option>
                      <option value="">AD_REGION</option>
                      <option value="">AD_NAME</option>
                      <option value="">AD_MOBILE</option>
                  </select>
              </td>
              <td>
                  <a href="#" class="btn btn-danger btn-sm">删除</a>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </page>
</template>

<script>
  import Page from '../../components/Page.vue'
  import VATable from '../../components/VATable.vue'
  import { mapGetters, mapActions } from 'vuex'
  import validatorSelect from '../../components/validatorSelect.vue'
  import StringOperationLabel from '../../components/StringOperationLabel.vue'
  export default {
    data () {
      return {
        //  下拉
        orgOtions: [
          { value: '成都银行', index: 0 },
          { value: '工商银行', index: 1 },
          { value: '建设银行', index: 2 },
          { value: '农业银行', index: 3 }
        ],
        validatePayload: {columns: []},
        radio: '',
        ajaxData: {
          page: null,
          content: '',
          level: ''
        }
      }
    },
    methods: {
      ...mapActions([
      ])
    },
    components: {
      'page': Page,
      'va-table': VATable,
      'validatorSelect': validatorSelect,
      'string-operation-label': StringOperationLabel
    },
    computed: {
      ...mapGetters([
      ])
    }
  }
</script>
