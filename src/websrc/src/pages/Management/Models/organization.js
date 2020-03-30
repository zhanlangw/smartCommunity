import {
  routerRedux
} from 'dva/router';
import {message} from 'antd';
import {
  import_file,
  sort,
  delorg,
  getOrganizationTreeAction,//组织机构树
  dept_item,
  post_item,
  user_item,
  delPostAction,
  updatePostAction,
  addPostAction,
  addUnitItem,
  getOrganizationItemAciton,//组织机构详情
  getUserListAction,//用户列表
  delOriganizationAction,//删除组织机构
  updateOriganizationAction,//修改组织机构
  addUserAction,//添加用户
  addOriganizationAction,//添加组织机构
  updateUserAction,//修改用户
  getAddUserTemplateAction,//新增用户模板
  getAddOriginTemplateAction,//新增组织机构模板
  updateUserPasswordAction,//修改用户名密码,
  delUserAction,//删除用户
} from '@/services/organization';
import { treeDateLoad } from "@/utils/utils";


const buidData = (tree,res=[]) =>{
  tree.forEach(e=>{
    if(e.children){
      const children = e.children;
      delete e.children;
      buidData(children,res);
    }
    res.push(e)
  })
  return res;
}

function transTreeData(list){
  if(list.length>0){
    let curPid= 0 //pid=0，为最上层节点 ，即无父节点
    let parent=findChild(curPid,list);//数组
    return parent;
  }else{
    return [];
  }
}

//找子节点
function findChild (curPid,list){
  let _arr = [];
  list.forEach(e=>{
    if(e.uid == curPid){
      e.children = findChild(e.id || e.key,list);
      _arr.push(e);
    }
  })
  return _arr;
}

export default {
  namespace: 'organization',

  state: {
    unit_tree:[],
    dept_item:{},
    post_item:{},
    user_item:{},

    depart_info:[],
    search_list:{
      data:[],
      total:0
    },
    userList_params:{
      page:1,
      pageSize: 10,
      search:'',
    }
  },

  effects: {
    *fetch_import ({ payload, callback},{call,put}){
      const response = yield call(import_file,payload);
      try {
        const {status} = response;
        if(status===0){
          message.success('导入成功!');
          // yield put({
          //   type: 'RETREE',
          // })
          if (callback) {
            yield call(callback);
          }
        }else{
          message.error(response.message);
        }
      }catch (e) {
        message.error('请求失败，请稍后再试');
      }
    },
    *fetch_sort({payload,callback},{call,put}){
      const response = yield call(sort,payload);
      try {
        const {status} = response;
        if(status===0){
          if(callback){
            yield call(callback,response.value);
          }
        }else{
          message.error(response.message);
        }
      }catch (e) {
        message.error('请求失败，请稍后再试');
      }
    },
    //获取组织机构树
    *fetch_getOrganizationTree_action({payload,callback},{call,put}){
      const response = yield call (getOrganizationTreeAction,payload);
      console.log(response);
      try {
        if (response.status === 0) {
          yield put({
              type:'unit_tree',
              payload:response.value
          })
          // if(!payload.id){
          //   yield put({
          //     type:'unit_tree',
          //     payload:response.value
          //   })
          // }else{
          //   yield put({
          //     type: 'unit_tree1',
          //     payload: {
          //       response:response.value,
          //       uid:payload.id,
          //     },

          //   });
          if(callback){
            yield call(callback,response.value);
          }
          // }

        }else{
          message.error(response.message);
        }
      }catch (e) {
        console.log(e)
        message.error('请求失败，请稍后再试');
      }
    },
    //部门详情
    *fetch_dept_item({payload,callback,params},{call,put}){
      const response = yield call(dept_item,payload);
      try {
        const {status} = response;
        if(status===0){
          yield put({
            type:'dept_item',
            payload:response.value
          })
        }else{
          message.error(response.message);
        }
      }catch (e) {
        message.error('请求失败，请稍后再试');
      }
    },
    //岗位详情
    *fetch_post_item({payload,callback,params},{call,put}){
      const response = yield call(post_item,payload);
      try {
        const {status} = response;
        if(status===0){
          yield put({
            type:'post_item',
            payload:response.value
          })

        }else{
          message.error(response.message);
        }
      }catch (e) {
        message.error('请求失败，请稍后再试');
      }
    },
    //人员详情
    *fetch_user_item({payload,callback,params},{call,put}){
      const response = yield call(user_item,payload);
      try {
        const {status} = response;
        if(status===0){
          yield put({
            type:'user_item',
            payload: response.value
          })
          if(callback) {
            yield  call(callback, response.value)
          }
        }else{
          message.error(response.message);
        }
      }catch (e) {
        console.log(e)
        message.error('请求失败，请稍后再试');
      }
    },

    //获取组织机构详情
    *fetch_getOrganizationItem_action({payload,callback,params},{call,put}){
      const response = yield call(getOrganizationItemAciton,payload);
      try {
        const {status} = response;
        if(status===0){
          yield put({
            type:'depart_info',
            payload:response.value
          })

        }else{
          message.error(response.message);
        }
      }catch (e) {
        message.error('请求失败，请稍后再试');
      }
    },

    //组织机构模板
    *fetch_getAddOriginTemplate_action({payload,callback,params},{call,put}){
      const response = yield call(getAddOriginTemplateAction,payload);
      try {
        const {status} = response;
        if(status===0){
          yield put({
            type:'depart_info',
            payload:response.value
          })

        }else{
          message.error(response.message);
        }
      }catch (e) {
        message.error('请求失败，请稍后再试');
      }
    },

    //用户模板
    * fetch_getAddUserTeplate_action({payload,callback,params},{call,put}){
      const response = yield call(getAddUserTemplateAction,payload);
      try {
        const {status} = response;
        if(status===0){
          yield put({
            type:'depart_info',
            payload:response.value
          })

        }else{
          message.error(response.message);
        }
      }catch (e) {
        message.error('请求失败，请稍后再试');
      }
    },

    //用户列表
    *fetch_getUserList_action({payload,callback,params},{call,put}){
      const response = yield call(getUserListAction,payload);
      try {
        if(response.status===0){
          yield put({
            type:'search_list',
            payload:{
              response:response.value,
              params:payload,
            }
          })

        }else{
          message.error(response.message);
        }
      }catch (e) {
        message.error('请求失败，请稍后再试');
      }
    },
    *fetch_delorg({payload,params,callback},{call,put}){
      const response = yield call(delorg,payload);
      try {
        const {status} = response;
        if(status===0){
          message.success('删除成功');
          if(callback){
            yield call(callback,response);
          }
        }else{
          message.error(response.message);
        }
      }catch (e) {
        message.error('请求失败，请稍后再试');
      }
    },
    //删除组织机构
    *fetch_delOriganization_action({payload,params,callback},{call,put}){
      const response = yield call(delOriganizationAction,payload);
      try {
        const {status} = response;
        if(status===0){
          message.success('删除成功');
          yield put({
            type: 'DEL_UNIT_ACTION',
            payload,
          })
          if(callback){
            yield call(callback,response);
          }

        }else{
          message.error(response.message);
        }
      }catch (e) {
        message.error('请求失败，请稍后再试');
      }
    },
    //删除岗位
    *fetch_delPostAction_action({payload,params,callback},{call,put}){
      const response = yield call(delPostAction,payload);
      try {
        const {status} = response;
        if(status===0){
          message.success('删除成功');
          if(callback){
            yield call(callback,response);
          }
        }else{
          message.error(response.message);
        }
      }catch (e) {
        message.error('请求失败，请稍后再试');
      }
    },
    //删除用户
    * fetch_delUser_action({payload,params,callback},{call,put}){
      const response = yield call(delUserAction,payload);
      try {
        const {status} = response;
        if(status===0){
          message.success('删除成功');
          yield put({
            type: 'DEL_USER_ACTION',
            payload,
          })
          if(callback){
            yield call(callback,response);
          }
        }else{
          message.error(response.message);
        }
      }catch (e) {
        console.log(e)
        message.error('请求失败，请稍后再试');
      }
    },

    //添加岗位
    *fetch_addPostAction_action({payload,callback,params},{call,put}){
      const response = yield call(addPostAction,payload);
      try {
        const {status} = response;
        if(status===0){
          message.success('添加成功');
          if(callback){
            yield call(callback,response);
          }

        }else{
          message.error(response.message);
        }
      }catch (e) {
        message.error('请求失败，请稍后再试');
      }
    },
    //添加用户
    *fetch_addUser_action({payload,callback,params},{call,put}){
      const response = yield call(addUserAction,payload);
      try {
        const {status} = response;
        if(status===0){
          message.success('添加成功');
          yield put({
            type:'ADD_USER_ACTION',
            payload: response.value
          })
        }else{
          message.error(response.message);
        }
      }catch (e) {
        message.error('请求失败，请稍后再试');
      }
    },
    *fetch_updateUserPassword_action({payload,callback},{call,put}){
      const response = yield call(updateUserPasswordAction,payload);
      try {
        const {status}= response;
        if(status===0){
          if(callback){
            yield call(callback,response);
          }
          message.success(response.message);
        }else{
          message.error(response.message)
        }
      }catch (e) {
        message.error('请求失败，请稍后再试')
      }
    },

    //添加组织机构
    *fetch_addOriganization_action({payload,callback,params},{call,put}){
      const response = yield call(addOriganizationAction,payload);
      try {
        const {status} = response;
        if(status===0){
          message.success('新增成功');
          yield put({
            type:'ADD_UNIT_ACTION',
            payload: response.value
          })
          if(callback){
            yield call(callback,response);
          }
        }else{
          message.error(response.message);
        }
      }catch (e) {
        message.error('请求失败，请稍后再试');
      }
    },
    //修改岗位
    *fetch_updatePostAction_action({payload,callback,params},{call,put}){
      const response = yield call(updatePostAction,payload);
      try {
        const {status} = response;
        if(status===0){
          message.success('保存成功');
          if(callback){
            yield call(callback,response,payload);
          }
        }else{
          message.error(response.message);
        }
      }catch (e) {
        message.error('请求失败，请稍后再试');
      }
    },
    //修改用户
    *fetch_updateUser_action({payload,callback,params},{call,put}){
      const response = yield call(updateUserAction,payload);
      try {
        const {status} = response;
        if(status===0){
          message.success('保存成功');
          yield put({
            type:'reload_tree',
            payload:{
              id:payload.id,
              targetID:payload.unit_id.value,
              data:payload
            }
          })
          if(callback){
            yield call(callback,response,payload);
          }
        }else{
          message.error(response.message);
        }
      }catch (e) {
        console.log(e)
        message.error('请求失败，请稍后再试');
      }
    },
    //修改组织机构
    *fetch_updateOriganization_action({payload,callback,params},{call,put}){
      const response = yield call(updateOriganizationAction,payload);
      console.log(payload)
      try {
        const {status} = response;
        if(status===0){
          message.success('保存成功');
          yield put({
            type:'reload_tree',
            payload:{
              id:payload.id,
              targetID:payload.pid,
              data:payload
            }
          })
        }else{
          message.error(response.message);
        }
      }catch (e) {
        console.log(e)
        message.error('请求失败，请稍后再试');
      }
    },
  },

  reducers: {

    reset(state,{ payload }){
      return {
        unit_tree:[],
        dept_item:{},
        post_item:{},
        user_item:{},

        depart_info:[],
        search_list:{
          data:[],
          total:0
        },
        userList_params:{
          page:1,
          pageSize:10,
          search:'',
        }
      }
    },
    unit_tree1(state, {payload}) {
      let tree = state.unit_tree;
      let date = treeDateLoad(tree,payload.response,payload.uid);
      state.unit_tree = date;

      return {
        ...state,
        unit_tree: payload,
      };
    },

    unit_tree(state,{payload}){
      return{
        ...state,
        unit_tree: payload
      }
    },

    dept_item(state,{payload}){
      let dept_item = state.dept_item;
      dept_item[payload.id] = payload;
      return{
        ...state,
      }
    },
    post_item(state,{payload}){
      let post_item = state.post_item;
      post_item[payload.id] = payload;
      return{
        ...state,
      }
    },
    user_item(state,{payload}){
      let user_item = state.user_item;
      user_item[payload.id] = payload;
      return{
        ...state,
      }
    },

    depart_info(state,{payload}){
      return{
        ...state,
        depart_info:payload
      }
    },
    reload_tree(state,{payload}){
      const { targetID, data} = payload;
      const { unit_tree } = state;
      const list = buidData(unit_tree);
      const new_list = list.map(e=>{
        if(e.key === data.id){
          return{
            // ...data,
            title:data.name,
            uid: targetID,
            isLeaf: e.isLeaf,
            type: e.type,
            key: e.key
          }
        }else{
          return e;
        }
      })
      const new_tree = transTreeData(new_list.map(e=>{
        if(!e.uid){
          return {...e,
            uid:0}
        }else{
          return e;
        }
      }));
      return{
        ...state,
        unit_tree:new_tree,
        depart_info:payload
      }
    },
    search_list(state,{payload}){
      return{
        ...state,
        search_list:payload.response,
        userList_params:payload.params,
      }
    },
    DEL_USER_ACTION(state, {payload}) {
      const list = buidData(state.unit_tree);
      console.log(payload)
      console.log(list)
      const new_list = list.filter(e=>{
        if(e.key === payload.id) return false
        return true
      })
      const new_tree = transTreeData(new_list.map(e=>{
        if(!e.uid){
          return {...e,
            uid:0}
        }else{
          return e;
        }
      }));
      console.log(new_tree)
      return {
        ...state,
        unit_tree: new_tree
      }
    },
    DEL_UNIT_ACTION(state, {payload}) {
      const list = buidData(state.unit_tree);
      console.log(payload)
      console.log(list)
      const new_list = list.filter(e=>{
        if(e.key === payload.id) return false
        return true
      })
      new_list.filter(e => {
        if(e.uid === payload.id) return false
        return true
      })
      const new_tree = transTreeData(new_list.map(e=>{
        if(!e.uid){
          return {...e,
            uid:0}
        }else{
          return e;
        }
      }));
      console.log(new_tree)
      return {
        ...state,
        unit_tree: new_tree
      }
    },
    ADD_USER_ACTION(state, {payload}) {
      const list = buidData(state.unit_tree);
      list.push({
        isLeaf: true,
        type:'USER',
        key: payload.id,
        uid: payload.unit.id,
        title: payload.name
      })
      const new_tree = transTreeData(list.map(e=>{
        if(!e.uid){
          return {...e,
            uid:0
          }
        }else{
          return e;
        }
      }));
      return {
        ...state,
        unit_tree: new_tree
      }
    },
    ADD_UNIT_ACTION(state, { payload }) {
      const list = buidData(state.unit_tree);
      list.push({
        title: payload.name,
        type:'UNIT',
        key: payload.id,
        uid: payload.pid,
        isLeaf: true,
      })
      console.log(list)
      const new_tree = transTreeData(list.map(e=>{
        if(!e.uid){
          return {...e,
            uid:0
          }
        }else{
          return e;
        }
      }));
      console.log(new_tree)
      return {
        ...state,
        unit_tree: new_tree
      }
    },
    // RETREE(state, { payload }) {
    //   return {
    //     ...state,
    //     unit_tree: []
    //   }
    // }
  },
};
