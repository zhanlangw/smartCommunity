import {
  updPersonalUserItem,
  getPersonalUserItem
} from '@/services/personal';
import { message } from 'antd';

export default {

  namespace: 'personal',

  state: {
    personal_user_item:null
  },

  reducers: {
    GET_PERSONAL_USER_ITEM(state, { payload }) {
      return { ...state, personal_user_item: payload };
    },
  },

  effects: {
    *fetch_updPersonalUserItem({ payload, callback }, { call, put }) {
      const response = yield call(updPersonalUserItem, payload);
       try {
         if (response.status === 0) {
           message.success(response.message);
           if (callback) {
             yield  call(callback,response.value);
           }
         } else {
           message.error('保存失败')
         }
       } catch (e) {
          console.log(e)
          message.error('请求失败，请稍后再试！');
       }
    },
    *fetch_getPersonalUserItem ({ payload, callback }, { call, put }) {
      const response = yield call(getPersonalUserItem, payload)
      try {
        if (response.status === 0) {
          yield put({
            type:'GET_PERSONAL_USER_ITEM',
            payload: response.value
          })
        } else {
          message.error(response.message);
        }
        if(callback){
          yield call(callback,response);
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
  //  }
  // },

};
