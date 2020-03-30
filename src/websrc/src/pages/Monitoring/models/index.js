import {
  videoPlay,
  videoEnd
} from '../services';
import { message } from 'antd';

export default {

  namespace: 'monitoring',

  state: {
  },

  reducers: {
  },

  effects: {
    *fetch_videoPlay({ payload, callback }, { call, put }) {
      const response = yield call(videoPlay, payload);
      try {
        if (response.status === 0) {
         } else {
           message.error(response.message);
        }
        if (callback) {
          yield call(callback, response);
        }
      } catch (e) {
        console.log(e)
      }
    },
    *fetch_videoEnd({ payload, callback }, { call, put }) {
      const response = yield call(videoEnd, payload);
      try {
        if (response.status === 0) {
          if (callback) {
            yield call(callback, response);
          }
        } else {
          message.error(response.message);
        }
      } catch (e) {
        console.log(e)
      }
    },
  },

 // subscriptions: {
 //  init({dispatch}){
 //     dispatch({ });
 // }
 // },

};