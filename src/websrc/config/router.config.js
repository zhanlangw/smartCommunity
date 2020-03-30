export default [
  {
    path: '/user',
    component: '../layouts/UserLayout',
    routes: [
      { path: '/user', redirect: '/user/login' },
      { path: '/user/login', name: '登录', component: './User/Login' },
      {
        component: '404',
      },
    ],
  },
  {
    path: '/',
    component: '../layouts/BasicLayout',
    Routes: ['src/pages/Authorized'],
    // authority: ['admin', 'user'],
    routes: [
      { path: '/', redirect: '/workbench'},
      {
        path: '/workbench',
        name: 'workbench',
        component: './Workbench/List',
      },
      {
        path: '/bigdata',
        name:'BigData',
        component: './BigData/item',
      },
      {
        path: '/map',
        name: 'Map',
        component: './Map',
      },
      {
        path:'/personal',
        name:'personal',
        component: './personal',
        hideInMenu: true,
      },
      {
        path: './management',
        name: 'Management',
        hideInMenu: true,
        routes: [
          {
            path: '/management/config',
            name: 'config',
            component: './Management/Config',
          },
          {
            path: '/management/process',
            name: 'processList',
            component: './Management/Process/List',
          },
          {
            path: '/management/process/item/:id',
            name: 'processItem',
            component: './Management/Process/Item',
          },
          {
            path: '/management/organization',
            name: 'organization',
            component: './Management/Organization',
          },
          {
            path: '/management/organization/searchList/:text',
            name: 'organizationList',
            component: './Management/Organization/SearchList',
          },
          {
            path: '/management/organization/departmentInfo/:id/:name',
            name: 'organizationDepartmentInfo',
            component: './Management/Organization/DepartmentInfo',
          },
          {
            path: '/management/organization/userInfo/:id/:name',
            name: 'organizationUserInfo',
            component: './Management/Organization/UserInfo',
          },
          {
            path: '/management/userManagement',
            name: 'userManagement',
            component: './Management/userManagement/index',
          },
          {
            path: '/management/category',
            name: 'Category',
            component: './Management/Category/index',
          },
          {
            path: '/management/station',
            name: 'Station',
            component: './Management/Station/index',
          },
          {
            path: '/management/device',
            name: 'Device',
            component: './Management/Device/index',
          },
          {
            path: '/management/blacklist',
            name: 'BlackList',
            component: './Management/BlackList/index',
          },
          {
            component: '404',
          },
        ]
      },

      {
        name: 'File',
        path: '/file',
        routes: [
          {
            path: '/file/eventManagement',
            name: 'eventManagement',
            component: './File/eventManagement/index',
          },
          {
            path: '/file/fileList',
            name: 'fileList',
            component: './File/fileList/List',
          },
          {
            path: '/file/fileStat',
            name: 'fileStat',
            component: './File/fileStat/index',
          },
          {
            path: '/file/fileHistory',
            name: 'fileHistory',
            component: './File/fileHistory/index',
          },
        ],
      },
      // {
      //   path: '/centralized',
      //   name: 'centralized',
      //   component: './Monitoring',
      // },
      {
        path: '/monitoring',
        name: 'Monitoring',
        component: './Monitoring',
      },
      {
        path: 'evaluate',
        name: 'Evaluate',
        component: './Evaluate',
      },
      {
        path: '/globalSearch',
        name: 'globalSearch',
        hideInMenu: true,
        component: './globalSearch/index',
      },
      {
        path: '/password',
        name: 'password',
        component: '../pages/password',
        hideInMenu: true,
      },//新增修改密码路由
      {
        name: 'exception',
        path: '/exception',
        hideInMenu: true,
        routes: [
          {
            path: '/exception/403',
            name: 'not-permission',
            component: './Exception/403',
          },
          {
            path: '/exception/404',
            name: 'not-find',
            component: './Exception/404',
          },
          {
            path: '/exception/500',
            name: 'server-error',
            component: './Exception/500',
          },
        ],
      },
      {
        component: '404',
      },
    ],
  },
];
