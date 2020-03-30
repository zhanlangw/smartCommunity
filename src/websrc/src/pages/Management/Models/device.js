import {
  getDeviceList,
  addDeviceItem,
  getDeviceItem,
  updDeviceItem,
  delDeviceItem
} from '@/services/device';
import { payload_0_10 as pagePayload } from '@/utils/utils';
import { message } from 'antd';

export default {

  namespace: 'device',

  state: {
    device_list: null,
    device_item: null,
  },

  reducers: {
    GET_DEVICE_LIST(state, { payload }) {
      return { ...state, device_list: payload };
    },
    GET_DEVICE_ITEM (state, { payload }) {
      return { ...state, device_item: payload };
    },
  },

  effects: {
    // --------------------------------------摄像机-------------------------------------
    *fetch_getDeviceList ({ payload, callback }, { call, put }) {
      const response = yield call(getDeviceList, payload)
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_DEVICE_LIST',
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
    *fetch_getDeviceItem ({ payload, callback }, { call, put }) {
      const response = yield call(getDeviceItem, payload)
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_DEVICE_ITEM',
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
    *fetch_addDeviceItem ({ payload, callback }, { call, put }) {
      const response = yield call(addDeviceItem, payload)
      try {
        if (response.status === 0) {
          message.success(response.message)
          yield put({
            type: 'fetch_getDeviceList',
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
    *fetch_updDeviceItem ({ payload, callback }, { call, put }) {
      const response = yield call(updDeviceItem, payload)
      try {
        if (response.status === 0) {
          message.success(response.message)
          yield put({
            type: 'fetch_getDeviceList',
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
    *fetch_delDeviceItem ({ payload, callback }, { call, put }) {
      const response = yield call(delDeviceItem, payload);
      try {
        if (response.status === 0) {
          message.success(response.message);
          yield put({
            type: 'fetch_getDeviceList',
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