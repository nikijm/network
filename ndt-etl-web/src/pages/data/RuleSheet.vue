
 <template>
 <div>
  <page title="规则处理器" v-if="rule">
   <div slot="tools">
        <button class="btn btn-primary" @click="openAddDialog()">添加</button>
    </div>
    <div slot="content">
      <table class="table table-hover table-fixed">
        <thead>
          <tr>
            <th>类型</th>
            <th>名称</th>
            <th>Qualified class name</th>
            <th>参数定义</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in fetchRuleOpsData.ruleOps">
            <td v-if='item.etlop === "validate"'>验证</td>
            <td v-if='item.etlop === "clean"'>清洗</td>
            <td v-if='item.etlop === "verity"'>校验</td>
            <td v-text='item.name'></td>
            <td v-text='item.clz'></td>
            <td v-text='item.params'></td>
            <td>
              <button class="btn btn-primary btn-xs" @click="showRule(item)">编辑</button>
              <button class="btn btn-danger btn-xs" @click="deleteRule(item.id)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </page>
  <page v-if="showAddDialog" title="添加">
    <div slot="content">
      <el-form :model="updateR" ref="updateR" label-width="100px">
        <el-row>
          <el-col :span="9">
            <el-form-item label="类型">
              <el-select v-model="updateR.etlop" placeholder="请选择">
                <el-option label="验证" value="validate">
                </el-option>
                <el-option label="清洗" value="clean">
                </el-option>
                <el-option label="校验" value="verity">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="9">
            <el-form-item label="名称">
              <el-input v-model="updateR.name" ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="9">
            <el-form-item label="Qualified">
              <el-input v-model="updateR.clz" ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="9">
            <el-form-item label="参数定义" props='params'>
              <input class="el-input__inner" v-model="updateR.params" @change='changeParam()'></input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div slot="footer">
      <div class="ml-10">
         <button class="btn btn-success btn-main" @click="addRules">确定</button>
        <button class="btn btn-default" @click="closeu">返回</button>
      </div>
    </div>
  </page>
  <page v-if="showupdate" title="编辑">
   <div slot="content">
     <el-form :model="updateR" ref="updateR" label-width="100px">
       <el-row>
         <el-col :span="9">
           <el-form-item label="类型">
             <el-select v-model="updateR.etlop" placeholder="请选择">
              <el-option label="验证" value="validate">
              </el-option>
              <el-option label="清洗" value="clean">
              </el-option>
              <el-option label="校验" value="verity">
              </el-option>
            </el-select>
           </el-form-item>
         </el-col>
       </el-row>
       <el-row>
         <el-col :span="9">
           <el-form-item label="名称">
             <el-input v-model="updateR.name" ></el-input>
           </el-form-item>
         </el-col>
       </el-row>
       <el-row>
         <el-col :span="9">
           <el-form-item label="Qualified">
             <el-input v-model="updateR.clz" ></el-input>
           </el-form-item>
         </el-col>
       </el-row>
       <el-row>
         <el-col :span="9">
           <el-form-item label="参数定义">
             <el-input v-model="updateR.params" ></el-input>
           </el-form-item>
         </el-col>
       </el-row>
     </el-form>
   </div>
   <div slot="footer">
     <div class="ml-10">
       <button class="btn btn-success btn-main" @click="reviseRules">确定</button>
       <button class="btn btn-default" @click="closeu">返回</button>
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
      rule: true,
      showupdate: false,
      showAddDialog: false,
      updateR: {
        type: '',
        name: '',
        Qualified: '',
        param: ''
      }
    }
  },
  computed: {
    ...mapGetters([
      'fetchRuleOpsData'
    ])
  },
  created () {
    this.load()
  },
  methods: {
    ...mapActions([
      'fetchRuleOps'
    ]),
    load () {
      this.fetchRuleOps()
    },
    changeParam () {
      try {
        JSON.parse(this.updateR.params)
      } catch (err) {
        this.$alert('请输入JSON格式数据', '错误', {
          confirmButtonText: '确定',
          type: 'error'
        })
        this.updateR.params = ''
      }
    },
    openAddDialog () {
      this.updateR = {
        id: '',
        etlop: '',
        name: '',
        clz: '',
        params: ''
      }
      this.showAddDialog = true
      this.rule = false
    },
    showRule (item) {
      this.rule = false
      this.showupdate = true
      this.updateR = {
        id: item.id,
        etlop: item.etlop,
        name: item.name,
        clz: item.clz,
        params: item.params
      }
    },
    closeu () {
      this.rule = true
      this.showupdate = false
      this.showAddDialog = false
    },
    deleteRule (id) {
      this.$confirm('确认删除规则处理器?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then(() => {
        etlRules.deleteRuleOps(id)
        .then((data) => {
          this.$alert('删除成功', '成功', {
            confirmButtonText: '确定',
            type: 'success'
          })
          this.load()
        })
      })
    },
    reviseRules () {
      etlRules.reviseRuleOps(this.updateR)
      .then(() => {
        this.closeu('updateR')
        this.load()
      })
      .catch((err) => {
        this.$alert(err.response.data.message, '失败', {
          confirmButtonText: '确定',
          type: 'warning'
        })
      })
    },
    addRules () {
      etlRules.addRuleOps(this.updateR).then(() => {
        this.closeu()
        this.load()
      })
      .catch((err) => {
        this.$alert(err.response.data.message, '失败', {
          confirmButtonText: '确定',
          type: 'warning'
        })
      })
    }
  },
  components: {
    'page': Page
  }
}
</script>
<style scoped>
  .el-select { width: 100%; }
</style>
