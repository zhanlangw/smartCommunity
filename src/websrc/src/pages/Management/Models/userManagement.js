import { 
  getUserList, 
  addUserItem,
  DelUserItem,
  updUserItem,
  getUserItem,
  getPermissionTree,
  getDeviceTree,
} from '@/services/userManagement';
import { message } from 'antd';
import { payload_0_10 as pagePayload } from '@/utils/utils';

export default {

  namespace: 'userManagement',

  state: {
    user_list: null,
    permission_tree: null,
    device_tree: null,
    user_item: null,
  },

  reducers: {
    GET_USER_LIST(state, { payload }) {
      return { ...state, user_list: payload };
    },
    GET_PERMISSION_TREE(state, { payload }) {
      return { ...state, permission_tree: payload };
    },
    GET_DEVICE_TREE(state, { payload }) {
      return { ...state, device_tree: payload };
    },
    GET_USER_ITEM(state, { payload }) {
      console.log(payload)
      return { ...state, user_item: payload };
    }
  },

  effects: {
    *fetch_getUserList({ payload, callback }, { call, put }) {
      const response = yield call(getUserList, payload);
       try {
         if (response.status === 0) {
           yield put({
             type: 'GET_USER_LIST',
             payload: response.value
           });
           if (callback) {
             yield call(callback);
           }
          } else {
            message.error(response.message);
          }
       } catch (e) {
         message.error('请求失败，请稍后再试！');
       }
    },
    *fetch_addUserList ({ payload, callback }, { call, put }) {
      const response = yield call(addUserItem, payload);
      try {
        if (response.status === 0) {
          message.success(response.message)
          yield put({
            type: 'fetch_getUserList',
            payload: pagePayload
          });
          if (callback) {
            yield call(callback);
          }
        } else {
          message.error(response.message);
        }
      } catch (e) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_getPermissionTree ({ payload, callback }, { call, put }) {
      const response = yield call(getPermissionTree, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_PERMISSION_TREE',
            payload: response.value
          });
          if (callback) {
            yield call(callback);
          }
        } else {
          message.error(response.message);
        }
      } catch (e) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_getDeviceTree ({ payload, callback }, { call, put }) {
      const response = yield call(getDeviceTree, payload)
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_DEVICE_TREE',
            payload: response.value
          });
          if (callback) {
            yield call(callback);
          }
        } else {
          message.error(response.message);
        }
      } catch (e) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_DelUserItem({ payload, callback }, { call, put }) {
      const response = yield call(DelUserItem, payload);
      try {
        if (response.status === 0) {
          message.success(response.message);
          if (callback) {
            yield call(callback);
          }
          } else {
            message.error(response.message);
        }
      } catch (e) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_getUserItem({payload, callback}, {call, put}) {
      const response = yield call(getUserItem, payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'GET_USER_ITEM',
              payload: response.value,
            });
           } else {
             message.error(response.message);
          }
          if (callback) {
            yield call(callback, response);
          }
        } catch (e) {
          message.error('请求失败，请稍后再试！');
        }
    },
    *fetch_updUserItem ({ payload, callback }, { call, put }) {
      const response = yield call(updUserItem, payload)
      try {
        if (response.status === 0) {
          message.success(response.message)
          yield put({
            type: 'fetch_getUserList',
            payload: pagePayload
          });
          if (callback) {
            yield call(callback, response.value);
          }
        } else {
          message.error(response.message);
        }
      } catch (e) {
        message.error('请求失败，请稍后再试！');
      }
    }
  },

  // subscriptions: {
  //  init({dispatch}){
  //     dispatch({ });
  //  }
  // },

};