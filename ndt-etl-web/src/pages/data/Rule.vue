<template>
<div>
  <page title="规则模板维护" v-if="ruletemplate">
  <div slot="tools">
        <button class="btn btn-primary" @click="showAddR">添加</button>
  </div>
  <div slot="content">
    <table class="table table-hover table-fixed">
    <thead>
      <tr>
        <th>规则模板名称</th>
        <th>操作</th>
      </tr>
    </thead>
      <tr v-for="(item, index) in fetchTemplatesData.ruleSuits">
        <td>{{ item.name }}</td>
        <td>
          <button class="btn btn-primary btn-xs" @click="showEditR(index)">编辑</button>
          <button class="btn btn-danger btn-xs" @click="deleteTemplate(item.id)">删除</button>
        </td>
      </tr>
    </table>
    <el-pagination
      @current-change="toPage"
      :current-page="fetchTemplatesData.paging.current"
      layout="prev, pager, next, jumper"
      :total="fetchTemplatesData.paging.total"
      :page-size="fetchTemplatesData.paging.page_size">
    </el-pagination>
  </div>
  </page>
  <page title="添加规则模板" v-if="addRt">
    <div slot="content">
        <el-form label-width="100px">
          <el-row>
            <el-col :span="9">
              <el-form-item label="排序">
                <el-input type="number" v-model="template.sequence"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="规则模板名称">
                <el-input v-model="template.name"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="说明">
                <el-input v-model="template.description"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="规则项">
                <div>
                  <div style="width: 160px;height: 200px;border:1px solid #6e6e6e;float: left;overflow-y: scroll;">
                    <a v-for="(item,index) in values" style="margin: 0;height: 20px" @click="selected(index)" >
                      <p class="addp">{{item.name}}:{{item.value}}</p>
                    </a>
                  </div>
                  <div style="float: left;margin-left: 20px;">
                    <p><i class="el-icon-plus" @click="addTO"></i></p>
                    <p><i class="el-icon-minus" @click="deleteTO"></i></p>
                  </div>
                  <div style="float: left;margin-left: 20px;">
                    <p>增加规则项</p>
                    <el-form>
                      <el-form-item label="类型">
                        <el-col :span="14">
                          <el-select v-model="param.opType" placeholder="请选择">
                            <el-option label="验证" value="validate">
                            </el-option>
                            <el-option label="清洗" value="clean">
                            </el-option>
                            <el-option label="校验" value="verity">
                            </el-option>
                          </el-select>
                        </el-col>
                     </el-form-item>
                      <el-form-item label="处理器">
                        <el-col :span="14">
                          <el-select v-model="ruleOps" placeholder="选择处理器" @change="selectRuleOps(ruleOps)">
                            <el-option
                              v-if="param.opType == item.etlop"
                              v-for="(item, index) in fetchRuleOpsData.ruleOps"
                              :label="item.name"
                              :value="index">
                            </el-option>
                          </el-select>
                        </el-col>
                      </el-form-item>
                      <el-form-item   v-for="param in params" :label="param.name">
                        <el-col :span="18">
                          <el-input v-model="param.paramValue"></el-input>
                        </el-col>
                      </el-form-item>
                    </el-form>
                  </div>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
    </div>
    <div slot="footer">
      <div class="ml-10">
         <button class="btn btn-success btn-main" @click="saveTemplate">确定</button>
        <button class="btn btn-default" @click="closeAdd">返回</button>
      </div>
    </div>
  </page>
  <page title="编辑规则模板" v-if="editRt">
    <div slot="content">
        <el-form label-width="100px">
          <el-row>
            <el-col :span="9">
              <el-form-item label="排序">
                <el-input type="number" v-model="template.sequence"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="规则模板名称">
                <el-input v-model="template.name"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="说明">
                <el-input v-model="template.description"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="规则项">
                <div>
                  <div style="width: 160px;height: 200px;border:1px solid #6e6e6e;float: left;overflow-y: scroll;">
                    <a v-for="(item,index) in values" style="margin: 0;height: 20px" @click="selected(index)" >
                      <p class="addp">{{item.name}}:{{item.value}}</p>
                    </a>
                  </div>
                  <div style="float: left;margin-left: 20px;">
                    <p><i class="el-icon-plus" @click="addTO"></i></p>
                    <p><i class="el-icon-minus" @click="deleteTO"></i></p>
                  </div>
                  <div style="float: left;margin-left: 20px;">
                    <p>增加规则项</p>
                    <el-form>
                      <el-form-item label="类型">
                        <el-col :span="14">
                        <el-select v-model="param.opType" placeholder="请选择">
                          <el-option label="验证" value="validate">
                          </el-option>
                          <el-option label="清洗" value="clean">
                          </el-option>
                          <el-option label="校验" value="verity">
                          </el-option>
                        </el-select>
                        </el-col>
                      </el-form-item>
                      <el-form-item label="处理器">
                        <el-col :span="14">
                          <el-select v-model="ruleOps" placeholder="选择处理器" @change="selectRuleOps(ruleOps)">
                            <el-option
                              v-if="param.opType == item.etlop"
                              v-for="(item, index) in fetchRuleOpsData.ruleOps"
                              :label="item.name"
                              :value="index">
                            </el-option>
                          </el-select>
                        </el-col>
                      </el-form-item>
                      <el-form-item   v-for="param in params" :label="param.name">
                        <el-col :span="18">
                          <el-input v-model="param.paramValue"></el-input>
                        </el-col>
                      </el-form-item>
                    </el-form>
                  </div>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
    </div>
    <div slot="footer">
      <div class="ml-10">
         <button class="btn btn-success btn-main" @click="editTemplate">确定</button>
        <button class="btn btn-default" @click="closeEdit">返回</button>
      </div>
    </div>
  </page>
</div>
</template>

<script>
  import Page from '../../components/Page.vue'
  import { mapGetters, mapActions } from 'vuex'
  import etlRules from '../../vuex/api/services/etlRules'

  export default {
    data () {
      return {
        ruletemplate: true,
        addRt: false,
        editRt: false,
        addrules: {
          isActive: 1
        },
        ruleOps: '',
        values: [],
        param: {
          opType: ''
        },
        params: {},
        ruleItem: [],
        jsonArr: [],
        template: {
          sequence: '',
          name: '',
          description: '',
          ruleOps: []
        },
        rulesId: '',
        rulesName: '',
        active: '',
        id: '',
        payload: {
          current: 1,
          searchKey: ''
        }
      }
    },
    created () {
      this.load()
    },
    computed: {
      ...mapGetters([
        'fetchRuleOpsData',
        'fetchTemplatesData'
      ])
    },
    watch: {
      active: function (val, oldVal) {
        $('.addp').eq(oldVal).css('backgroundColor', '#fff')
        $('.addp').eq(oldVal).css('color', '#4c4c4c')
        $('.addp').eq(val).css('backgroundColor', '#40A7FF')
        $('.addp').eq(val).css('color', '#fff')
      }
    },
    methods: {
      ...mapActions([
        'fetchRuleOps',
        'fetchTemplates'
      ]),
      load () {
        this.fetchTemplates(this.payload)
      },
      showAddR () {
        this.ruletemplate = false
        this.addRt = true
        this.fetchRuleOps()
        // 重置列表数据
        this.values = []
        this.template = {
          sequence: '',
          name: '',
          description: '',
          ruleOps: []
        }
        this.ruleOps = ''
        this.param = {
          opType: ''
        }
        this.params = {}
      },
      showEditR (index) {
        this.ruletemplate = false
        this.editRt = true
        this.fetchRuleOps()
        // 重置列表数据
        let data = this.fetchTemplatesData.ruleSuits[index]
        this.template.sequence = data.sequence
        this.template.name = data.name
        this.template.description = data.description
        this.template.ruleOps = []
        this.values = []
        this.param = {
          opType: ''
        }
        this.params = {}
        this.ruleOps = ''
        this.id = data.id
        let rules = JSON.parse(data.rules)
        for (let i = 0; i < rules.length; i++) {
          let template = {
            id: rules[i].id,
            name: rules[i].name,
            etlop: rules[i].etlop,
            params: []
          }
          let ruleParams = JSON.parse(rules[i].params)
          for (let j = 0; j < ruleParams.length; j++) {
            let params = {
              name: ruleParams[j].name,
              paramKey: ruleParams[j].paramKey,
              paramValue: ruleParams[j].paramValue
            }
            let paramsValue = {
              name: ruleParams[j].name,
              value: ruleParams[j].paramValue
            }
            this.values.push(paramsValue)
            template.params.push(params)
          }
        }
      },
      closeAdd () {
        this.ruletemplate = true
        this.addRt = false
      },
      closeEdit () {
        this.ruletemplate = true
        this.editRt = false
      },
      selected (index) {
        this.active = index
      },
      selectRuleOps (ruleOps) {
        let data = this.fetchRuleOpsData.ruleOps[ruleOps]
        console.log('data', data)
        if (data !== undefined && data !== '') {
          try {
            JSON.parse(data.params)
            this.rulesId = data.id
            this.rulesName = data.name
            let param = data.params
            this.params = JSON.parse(param).params
          } catch (err) {
            this.$alert('参数格式错误', '错误', {
              confirmButtonText: '确定',
              type: 'error'
            })
          }
        }
      },
      addTO () {
        let template = {
          id: this.rulesId,
          name: this.rulesName,
          etlop: this.param.opType,
          params: []
        }
        for (let i = 0; i < this.params.length; i++) {
          let params = {
            name: this.params[i].name,
            paramKey: this.params[i].paramKey,
            paramValue: this.params[i].paramValue
          }
          let paramsValue = {
            name: this.params[i].name,
            value: this.params[i].paramValue
          }
          this.values.push(paramsValue)
          template.params.push(params)
        }
        this.template.ruleOps.push(template)
        this.param.opType = ''
        this.ruleOps = ''
        this.params = {}
        this.active = 9999
      },
      deleteTO () {
        this.values.splice(this.active, 1)
        this.template.ruleOps.splice(this.active, 1)
        this.active = 9999
      },
      deleteTemplate (id) {
        this.$confirm('确认删除规则模板?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        .then(() => {
          etlRules.deleteTemplate(id)
          .then(() => {
            this.$alert('删除成功', '成功', {
              confirmButtonText: '确定',
              type: 'success'
            })
            this.load()
          })
          .catch((err) => {
            console.log(err)
          })
        })
      },
      saveTemplate () {
        for (let i = 0; i < this.template.ruleOps.length; i++) {
          this.template.ruleOps[i].params = JSON.stringify(this.template.ruleOps[i].params)
        }
        etlRules.saveTemplate(this.template)
        .then(() => {
          this.closeAdd()
          this.load()
        })
        .catch((err) => {
          console.log(err)
        })
      },
      editTemplate () {
        console.log(this.template)
        if (!this.template) {
          return
        }
        for (let i = 0; i < this.template.ruleOps.length; i++) {
          this.template.ruleOps[i].params = JSON.stringify(this.template.ruleOps[i].params)
        }
        etlRules.updateTemplate(this.id, this.template)
        .then(() => {
          this.closeEdit()
          this.load()
        })
        .catch((err) => {
          console.log(err)
        })
      },
      toPage (val) {
        this.payload.current = val
        this.load()
      }
    },
    components: {
      'page': Page
    }
  }
</script>

<style onlyRule>
.addp {
  padding: 0px 5px;
}
a:hover {
  text-decoration: none;
  cursor: pointer;
}
td { padding: 8px; border-bottom: 1px solid #ddd; }
</style>
