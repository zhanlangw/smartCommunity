import defaultSettings from './defaultSettings'; // https://umijs.org/config/

import os from 'os';
import slash from 'slash2';
import webpackPlugin from './plugin.config';
import webpackRouter from './router.config';
const { pwa, primaryColor } = defaultSettings; // preview.pro.ant.design only do not use in your production ;
// preview.pro.ant.design 专用环境变量，请不要在你的项目中使用它。

const { APP_TYPE, TEST, NODE_ENV } = process.env;
const plugins = [
  [
    'umi-plugin-react',
    {
      antd: true,
      dva: {
        hmr: true,
      },
      locale: {
        // default false
        enable: true,
        // default zh-CN
        default: 'zh-CN',
        // default true, when it is true, will use `navigator.language` overwrite default
        baseNavigator: true,
      },
      dynamicImport: {
        loadingComponent: './components/PageLoading/index',
        webpackChunkName: true,
        level: 3,
      },
      pwa: pwa
        ? {
            workboxPluginMode: 'InjectManifest',
            workboxOptions: {
              importWorkboxFrom: 'local',
            },
          }
        : false,
      ...(!TEST && os.platform() === 'darwin'
        ? {
            dll: {
              include: ['dva', 'dva/router', 'dva/saga', 'dva/fetch'],
              exclude: ['@babel/runtime', 'netlify-lambda'],
            },
            hardSource: false,
          }
        : {}),
    },
  ],
  [
    'umi-plugin-pro-block',
    {
      moveMock: false,
      moveService: false,
      modifyRequest: true,
      autoAddMenu: true,
    },
  ],
]; // 针对 preview.pro.ant.design 的 GA 统计代码
// preview.pro.ant.design only do not use in your production ; preview.pro.ant.design 专用环境变量，请不要在你的项目中使用它。

// if (ANT_DESIGN_PRO_ONLY_DO_NOT_USE_IN_YOUR_PRODUCTION === 'site') {
//   plugins.push([
//     'umi-plugin-ga',
//     {
//       code: 'UA-72788897-6',
//     },
//   ]);
// }

// const uglifyJSOptions =
//   NODE_ENV === 'production'
//     ? {
//         uglifyOptions: {
//           // remove console.* except console.error
//           compress: {
//             drop_console: true,
//             pure_funcs: ['console.error'],
//           },
//         },
//       }
//     : {};
export default {
  // add for transfer to umi
  plugins,
    define: {
      APP_TYPE:
        APP_TYPE || '',
    },
  treeShaking: true,
  targets: {
    ie: 11,
  },
  // 路由配置
  routes: webpackRouter,
  // Theme for antd
  // https://ant.design/docs/react/customize-theme-cn
  theme: {
    'primary-color': primaryColor,
    // 'menu-bg': '#4C5FC1',
    'layout-header-background': '#4C5FC1',
    'modal-header-bg': '#4C5FC1',
    // 'menu-dark-submenu-bg': '#ff8040',
    // 'menu-dark-arrow-color': '#8080c0',
    // 'menu-dark-highlight-color': 'red',
    // 'menu-dark-item-active-color': '#ff 8040',
  },

  proxy: {
    '/api': {
      target: 'http://192.168.3.8:8060',
      changeOrigin: true,
    },
  },
  history: 'hash',
  hash: true,
  ignoreMomentLocale: true,
  lessLoaderOptions: {
    javascriptEnabled: true,
  },
  disableRedirectHoist: true,
  cssLoaderOptions: {
    modules: true,
    getLocalIdent: (context, localIdentName, localName) => {
      if (
        context.resourcePath.includes('node_modules') ||
        context.resourcePath.includes('ant.design.pro.less') ||
        context.resourcePath.includes('global.less')
      ) {
        return localName;
      }

      const match = context.resourcePath.match(/src(.*)/);

      if (match && match[1]) {
        const antdProPath = match[1].replace('.less', '');
        const arr = slash(antdProPath)
          .split('/')
          .map(a => a.replace(/([A-Z])/g, '-$1'))
          .map(a => a.toLowerCase());
        return `antd-pro${arr.join('-')}-${localName}`.replace(/--/g, '-');
      }

      return localName;
    },
  },
  base: '/',
  publicPath: '/',
  // runtimePublicPath: true,
  manifest: {
    basePath: '/',
  },
  chainWebpack: webpackPlugin,
};
