import Vue from 'vue'
import Router from 'vue-router'

// data
import Clean from 'pages/data/Clean'
import Upload from 'pages/data/Upload'
import Uploads from 'pages/data/Uploads'
import UploadList from 'pages/data/UploadList'
import Sheet from 'pages/data/Sheet'
import Report from 'pages/data/Report'
import ImportSheets from 'pages/data/ImportSheets'
import AllSheets from 'pages/data/AllSheets'
import EtlOverview from 'pages/data/EtlOverview'
import CleanSheets from 'pages/data/CleanSheets'
import VerifySheets from 'pages/data/VerifySheets'
import VerifiedSheets from 'pages/data/VerifiedSheets'
import Verify from 'pages/data/Verify'
import Cached from 'pages/data/Cached'
import TransformSheets from 'pages/data/TransformSheets'
import Rule from 'pages/data/Rule'
import RuleSheet from 'pages/data/RuleSheet'
import Transform from 'pages/data/Transform'
import DataDashboard from 'pages/data/Dashboard'
import DataConfig from 'pages/data/Config'
import DataType from 'pages/data/FileType'
import TableMaintenance from 'pages/data/TableMaintenance'
import BusinessTypes from 'pages/data/BusinessTypes'
// monitor
import Logs from 'pages/monitor/Logs'
import LogDetail from './pages/monitor/LogDetail'
import Config from 'pages/monitor/Config'
import Dashboard from 'pages/monitor/Dashboard'
import Rules from 'pages/monitor/Rules'
import Admin from 'pages/monitor/Admin'
import Users from 'pages/monitor/Users'
import Org from 'pages/monitor/Org'
import Details from 'pages/monitor/Details'
import Auth from 'pages/monitor/Auth'
import CreateRules from 'pages/monitor/CreateRules'
import CreateConfig from 'pages/monitor/CreateConfig'
import Service from 'pages/monitor/Service'
import CreateService from 'pages/monitor/CreateService'
import AllSheetsRules from 'pages/data/AllSheetsRules'
import CachesData from 'pages/data/CachesData'
import Compares from 'pages/data/Compares'
// demo
import Form from 'pages/demo/Form'
import Message from 'pages/demo/Message'
import Pagination from 'pages/demo/Pagination'
import LoadingFailure from 'pages/demo/LoadingFailure'
import TableTemplate from 'pages/demo/TableTemplate'
import Select from 'pages/demo/Select'
import ProgressDialog from 'pages/demo/ProgressDialog'
import Demo from 'pages/demo/Demo'

// login
import Login from 'pages/Login'
// home
import Home from 'pages/Home'
import Welcome from 'pages/Welcome'

Vue.use(Router)
export default new Router({
  mode: 'hash',
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/demo',
      name: 'Demo',
      component: Demo
    },
    {
      path: '/',
      component: Home,
      children: [
        // demo
        { path: '/form', component: Form },
        { path: '/message', component: Message },
        { path: '/pagination', component: Pagination },
        { path: '/loadingfailure', component: LoadingFailure },
        { path: '/progressDialog', component: ProgressDialog },
        { path: '/table', component: TableTemplate },
        { path: '/select', component: Select },
        { path: '/welcome', component: Welcome },
        // data
        { path: '/sheet/:id/clean', name: 'clean', component: Clean },
        { path: '/clearData/', component: Clean },
        { path: '/upload', component: Upload },
        { path: '/uploads', component: Uploads },
        { path: '/data/uploads', component: UploadList },
        { path: '/sheet/:id', name: 'sheet', component: Sheet },
        { path: '/sheet', component: Sheet },
        { path: '/report', component: Report },
        { path: '/etlOverview', component: EtlOverview },
        { path: '/allSheets', component: AllSheets },
        { path: '/importSheets', component: ImportSheets },
        { path: '/cleanSheets', component: CleanSheets },
        { path: '/compareSheets', component: VerifySheets },
        { path: '/cached', component: Cached },
        { path: '/dataType', component: DataType },
        { path: '/AllSheetsRules', component: AllSheetsRules },
        { path: '/CachesData', component: CachesData },
        { path: '/Compares', component: Compares, name: 'Compares' },
        // bug 路由由后台生成 待数据库修改后删除下行代码
        { path: '/storeSheets', component: Cached },
        { path: '/transformSheets', component: TransformSheets },
        { path: '/verifiedSheets', component: VerifiedSheets },
        { path: '/sheet/:id/transform', name: 'transform', component: Transform },
        { path: '/sheet/:id/verify', name: 'verify', component: Verify },
        { path: '/rule', component: Rule },
        { path: '/ruleSheet', component: RuleSheet },
        { path: '/ruleTemplates/:id', name: 'ruleTemplate', component: Rule },
        { path: '/transform', component: Transform },
        { path: '/data/dashboard', component: DataDashboard },
        { path: '/data/config', component: DataConfig },
        { path: '/data/tableMaintenance', component: TableMaintenance },
        { path: '/businessTypes', component: BusinessTypes },
        // monitor
        { path: '/logs', component: Logs },
        { path: '/logdetail', component: LogDetail },
        { path: '/config', component: Config },
        { path: '/createconfig', component: CreateConfig },
        { path: '/dashboard', component: Dashboard },
        { path: '/rules', component: Rules },
        { path: '/admin', component: Admin },
        { path: '/users', component: Users },
        { path: '/organizations', component: Org },
        { path: '/details', component: Details },
        { path: '/auth', component: Auth },
        { path: '/createrules', component: CreateRules },
        { path: '/service', component: Service },
        { path: '/createservice', component: CreateService }
      ]
    }
  ]
})
