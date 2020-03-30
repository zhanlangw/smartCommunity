import {
  getConfigItem,
  updConfigItem
} from './services';
import { message } from 'antd';

export default {

  namespace: 'config',

  state: {
    config_item: null,
  },

  reducers: {
    GET_CONFIG_ITEM(state, { payload }) {
      return { ...state, config_item: payload };
    },
  },

  effects: {
    *fetch_getConfigItem({ payload, callback }, { call, put }) {
      const response = yield call(getConfigItem, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_CONFIG_ITEM',
            payload: response.value,
          });
          if (callback) {
            yield call(callback, response);
          }
        } else {
          message.error(response.message);
        }
      } catch (e) {
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_updConfigItem({ payload, callback }, { call, put }) {
      const response = yield call(updConfigItem, payload);
      try {
        if (response.status === 0) {
          message.success(response.message);
          yield put({
            type: 'fetch_getConfigItem',
            payload: payload.id
          });
          if (callback) {
            yield call(callback, response);
          }
        } else {
          message.error(response.message);
        }
      } catch (e) {
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    },

  },

 // subscriptions: {
 //  init({dispatch}){
 //     dispatch({ });
 // }
 // },

};