/* eslint-disable @typescript-eslint/camelcase */
/* eslint no-useless-escape:0 import/prefer-default-export:0 */
import {message} from 'antd';
import {getAuthority} from "@/utils/authority";
import request from "@/utils/request";

const isDebug = true;
export const getURLRoot = () => window.location.protocol + '//' + window.location.host;
export const getDebugRoot = () => 'http://192.168.3.8:8060';
export const getApiUrl = () => (isDebug ? getDebugRoot() : getURLRoot());
export const getUrl = (url) => (getApiUrl() + decodeURI(url));

export const token = getAuthority() && getAuthority().token;

export const reg = /^((2(5[0-5]|[0-4]\d))|[0-1]?\d{1,2})(\.((2(5[0-5]|[0-4]\d))|[0-1]?\d{1,2})){3}$/;
export const passwordRgx = {
  reg: /^([A-Za-z0-9\.]){6,20}$/,
  message: '设置的密码必须是6~20位数字与字母的组合\n'
}; // 密码正则
export const emailRgx = /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/;
export const latitude =/^-?((0|1?[0-7]?[0-9]?)(([.][0-9]{1,6})?)|180(([.][0]{1,6})?))$/; //经度坐标
export const longitude =/^-?((0|1?[0-7]?[0-9]?)(([.][0-9]{1,6})?)|180(([.][0]{1,6})?))$/; //纬度坐标
export const phoneRgx = /^(0|86|17951)?(13[0-9]|15[012356789]|166|17[3678]|18[0-9]|14[57])[0-9]{8}$/;
export const abbreviation = /^[a-z0-9]{2}$/i;
export const pageSize = 5;
export const pageSize_10 = 10;

export const payload_0_10 = {
  start: 0,
  count: 10
};

const isUrl = path => reg.test(path);

export function unique(arr) {
  return Array.from(new Set(arr))
}

export function FileDownload(blob, filename) {
  if (window.navigator && window.navigator.msSaveOrOpenBlob) {
    window.navigator.msSaveOrOpenBlob(blob, decodeURI(filename));
  }else{
    const a = document.createElement('a');
    document.body.appendChild(a);
    a.download = decodeURI(filename);
    const href = window.URL.createObjectURL(blob);
    a.href = href;
    a.click();
    document.body.removeChild(a);
  }
}

export function treeDateLoad (unitTree, newTree, id) {
  return unitTree.map((item) => {
    if (item.key === id) {
      return {
        ...item,
        children: newTree,
        //isLeaf:true?false:false,
      }
    } else if (item.children) {
      const children = treeDateLoad(item.children, newTree, id);
      return ({
        ...item,
        children: children,
      });
    } else {
      return item
    }
  })
}

export function callStatusInfo(status) {
  switch (status) {
    case 0:
      message.success('请求成功');
    break;
    case 1001:
      message.success('请求失败');
    break;
    default:
  }
}

export function getPageQuery() {
  return parse(window.location.href.split('?')[1]);
}

export function isInArray (arr, val) {
  var testStr = ',' + arr.join(",") + ",";
  return testStr.indexOf("," + val + ",") != -1;
}

export function getQueryPath(path = '', query = {}) {
  const search = stringify(query);
  if (search.length) {
    return `${path}?${search}`;
  }
  return path;
}

export  function returnWorkTypeText(type) {
  switch(type) {
    case 'URGENT':
      return '紧急';
    case 'ORDINARY':
      return '普通';
    default:
      break;
  }
}

export function returnTimeTypeText(type) {
  switch (type) {
    case 'DAY':
     return '天';
    case 'HOUR':
      return '小时';
    default:
      break;
  }
}

export  function returnWorkTypeStyle(type) {
  if( type === 'URGENT'){
    return {
      color:"rgba(255,0,0,1)",
      whiteSpace: "nowrap"
    };
  }else {
    return {
      whiteSpace: "nowrap"
    };
  }
}

export function returnStatusText(type) {
  switch(type) {
    case 'TIME_OUT':
      return '超时';
    case 'COMING_SOON_TIME_OUT':
      return '即将超时';
    default:
      return '正常';
  }
};

export function returnStatusStyle(type) {
  switch(type) {
    case 'TIME_OUT':
      return {
        color:"rgba(255,0,0,1)"
      };
    case 'COMING_SOON_TIME_OUT':
      return {
        color:"rgba(255,126,0,1)"
      };
  }
};

export function returnSourceText(type) {
  switch(type) {
    case 'SYSTEM':
      return '摄像机抓拍';
    case 'APP':
      return '手机APP上报';
    default:
      return '管理员创建';
  }
};

export function returnUserTypeText(type) {
  switch(type) {
    case 'ADMIN':
      return '管理员';
    case 'USER':
      return '职能部门人员';
    case 'SUPERVISION_USER':
      return '督查人员';
    default:
      return '工作人员';
  }
};

export function beforeUpload(file) {
  request('/api/basis/item', {headers: token}) .then(res => {
    if (res.status === 0) {
      const isLt = file.size / 1024 / 1024 < res.value.fileMaxSize;
      if (!isLt) {
        message.error(`上传文件超过${res.value.fileMaxSize}MB`);
        return false
      }
      return isLt;
    }
  });
};

export function validator(rule, value, callback){
  const reg = /^[\u4e00-\u9fa5]+$/;
  const newValue = `${value}`;
  let chineseCount = 0;
  for (let i = 0; i <=newValue.length; i++) {
    newValue.charAt(i).match(reg) ? chineseCount+=2 : chineseCount+=1
  }
  chineseCount > 21 ? callback('不超过20个字符或10个汉字'): callback()
};

export function validatorDes(rule, value, callback){
  const reg = /^[\u4e00-\u9fa5]+$/;
  const newValue = `${value}`;
  let chineseCount = 0;
  for (let i = 0; i <= newValue.length; i++) {
    newValue.charAt(i).match(reg) ? chineseCount+=2 : chineseCount+=1
  }
  chineseCount > 101 ? callback('不超过100个字符或50个汉字') : callback()
};

export function isAntdPro() {
  return window.location.hostname === 'preview.pro.ant.design';
} // 给官方演示站点用，用于关闭真实开发环境不需要使用的特性

// const isAntDesignProOrDev = () => {
//   const { NODE_ENV } = process.env;

//   if (NODE_ENV === 'development') {
//     return true;
//   }

//   return isAntDesignPro();
// };
