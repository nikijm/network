module.exports = [
  {
    type: 'tree',
    icon: 'fa fa-cloud-upload',
    name: '数据上传',
    items: [
      {
        type: 'item',
        icon: 'fa fa-upload',
        name: '文档上传',
        route: '/upload'
      },
      {
        type: 'item',
        icon: 'fa fa-list',
        name: '文档列表',
        route: '/uploads'
      }
    ]
  },
  {
    type: 'tree',
    icon: 'fa fa-database',
    name: '数据处理',
    items: [
      {
        type: 'item',
        icon: 'fa fa-download',
        name: '数据缓存',
        route: '/cached'
      },
      {
        type: 'item',
        icon: 'fa fa-filter',
        name: '数据清洗',
        route: '/cleanSheets'
      },
      {
        type: 'item',
        icon: 'fa fa-refresh',
        name: '数据转换',
        route: '/transformSheets'
      },
      {
        type: 'item',
        icon: 'fa fa-check-circle',
        name: '数据校验',
        route: '/compareSheets'
      }
    ]
  },
  {
    type: 'tree',
    icon: 'fa fa-cog',
    name: '数据设置',
    items: [
      {
        type: 'item',
        icon: 'fa fa-plus',
        name: '添加规则',
        route: '/rule'
      },
      {
        type: 'item',
        icon: 'fa fa-list',
        name: '规则管理',
        route: '/ruleSheet'
      },
      {
        type: 'item',
        icon: 'fa fa-pie-chart',
        name: '查看报表',
        route: '/report'
      }
    ]
  },
  {
    type: 'tree',
    icon: 'fa fa-clock-o',
    name: '待整合页面',
    items: [
      {
        type: 'item',
        icon: 'fa fa-circle-o',
        name: '工作表详情',
        route: '/sheet'
      },
      {
        type: 'item',
        icon: 'fa fa-circle-o',
        name: '清洗操作',
        route: '/ClearData'
      },
      {
        type: 'item',
        icon: 'fa fa-circle-o',
        name: '转换操作',
        route: '/transform'
      }
    ]
  },
  {
    type: 'tree',
    icon: 'fa fa-dashboard',
    name: '运维',
    items: [
      {
        type: 'item',
        icon: 'fa fa-circle-o',
        name: '控制台',
        route: '/dashboard'
      },
      {
        type: 'item',
        icon: 'fa fa-circle-o',
        name: '警报规则',
        route: '/rules'
      },
      {
        type: 'item',
        icon: 'fa fa-circle-o',
        name: '日志',
        route: '/logs'
      },
      {
        type: 'item',
        icon: 'fa fa-circle-o',
        name: '配置',
        route: '/config'
      },
      {
        type: 'item',
        icon: 'fa fa-circle-o',
        name: '管理员',
        route: '/user'
      },
      {
        type: 'item',
        icon: 'fa fa-circle-o',
        name: '角色管理',
        route: '/auth'
      },
      {
        type: 'item',
        icon: 'fa fa-circle-o',
        name: '外部用户',
        route: '/users'
      },
    ]
  },
  {
    type: 'tree',
    icon: 'fa fa-book',
    name: '示例',
    items: [
      {
        type: 'item',
        icon: 'fa fa-circle-o',
        name: '表单',
        route: '/form'
      },
      {
        type: 'item',
        icon: 'fa fa-circle-o',
        name: '列表',
        route: '/table'
      },
      {
        type: 'item',
        icon: 'fa fa-circle-o',
        name: '消息',
        route: '/message'
      },
      {
        type: 'item',
        icon: 'fa fa-circle-o',
        name: '分页',
        route: '/pagination'
      },
      {
        type: 'item',
        icon: 'fa fa-circle-o',
        name: '加载失败',
        route: '/loadingfailure'
      }
    ]
  }
]
