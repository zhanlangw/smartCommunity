import {
  getFileHistoryList,
  delFileHistory
} from '../services';
import { message } from 'antd';

export default {

  namespace: 'history',

  state: {
    History_list: null,
  },

  reducers: {
    GET_HISTORY_LIST(state, { payload }) {
      return { ...state, history_list: payload };
    },
  },

  effects: {
    *fetch_getFileHistoryList({ payload, callback }, { call, put }) {
      const response = yield call(getFileHistoryList, payload);
        // console.log("111111111111111111111111111111111111",response);
        try {
        if (response.status === 0) {
          yield put({
            type: 'GET_HISTORY_LIST',
            payload: response.value,
          });
          if (callback) {
            yield call(callback, response);
          }
         } else {
           message.error(response.message);
        }
      } catch (e) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_delFileHistory({ payload, callback }, { call, put }) {
      const response = yield call(delFileHistory, payload);
      try {
        if (response.status === 0) {
          message.success(response.message);
          if (callback) {
            yield call(callback, response);
          }
        } else {
          message.error(response.message);
        }
      } catch (e) {
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