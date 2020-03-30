import {
  getFileEventList,
} from '../services';
import { message } from 'antd';

export default {

  namespace: 'event',

  state: {
    event_list: null,
    event_item: null,
  },

  reducers: {
    GET_EVENT_LIST(state, { payload }) {
      return { ...state, event_list: payload };
    },
  },

  effects: {
    *fetch_getFileEventList({ payload, callback }, { call, put }) {
      const response = yield call(getFileEventList, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_EVENT_LIST',
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
  },

 // subscriptions: {
 //  init({dispatch}){
 //     dispatch({ });
 // }
 // },

};