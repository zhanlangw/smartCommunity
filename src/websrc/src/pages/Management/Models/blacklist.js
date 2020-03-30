import {
  getBlacklistList,
  addBlacklistItem,
  getBlacklistItem,
  updBlacklistItem,
  delBlacklistItem
} from '@/services/blacklist';
import { payload_0_10 as pagePayload } from '@/utils/utils';
import { message } from 'antd';

message.config({
  maxCount: 2,
});

export default {

  namespace: 'blacklist',

  state: {
    blacklist_list: null,
    blacklist_item: null,
    options_list: null
  },

  reducers: {
    GET_BLACKLIST_LIST (state, { payload }) {
      return { ...state, blacklist_list: payload };
    },
    GET_BLACKLIST_ITEM (state, { payload }) {
      return { ...state, blacklist_item: payload };
    },
    GET_OPTIONS_LIST(state, { payload }) {
      return { ...state, options_list: payload };
    }
  },

  effects: {
    // --------------------------------------摄像机-------------------------------------
    *fetch_getBlacklistList ({ payload, callback }, { call, put }) {
      const response = yield call(getBlacklistList, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_BLACKLIST_LIST',
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
    *fetch_getBlacklistItem ({ payload, callback }, { call, put }) {
      const response = yield call(getBlacklistItem, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_BLACKLIST_ITEM',
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
    *fetch_addBlacklistItem ({ payload, callback }, { call, put }) {
      const response = yield call(addBlacklistItem, payload);
      try {
        if (response.status === 0) {
          message.success(response.message, 5);
          yield put({
            type: 'fetch_getBlacklistList',
            payload: pagePayload
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
    *fetch_updBlacklistItem ({ payload, callback }, { call, put }) {
      const response = yield call(updBlacklistItem, payload);
      try {
        if (response.status === 0) {
          message.success(response.message);
          yield put({
            type: 'fetch_getBlacklistList',
            payload: pagePayload
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
    *fetch_delBlacklistItem ({ payload, callback }, { call, put }) {
      const response = yield call(delBlacklistItem, payload);
      try {
        if (response.status === 0) {
          message.success(response.message)
          yield put({
            type: 'fetch_getBlacklistList',
            payload: pagePayload
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
    // ------------------------------------------------------------------------------------------------------->
  },

};