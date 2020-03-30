import {
  getStationList,
  addStationItem,
  getStationItem,
  updStationItem,
  delStationItem
} from '@/services/station';
import { payload_0_10 as pagePayload } from '@/utils/utils';
import { message } from 'antd';

export default {

  namespace: 'station',

  state: {
    station_list: null,
    station_item: null,
  },

  reducers: {
    GET_STATION_LIST(state, { payload }) {
      return { ...state, station_list: payload };
    },
    GET_STATION_ITEM(state, { payload }) {
      return { ...state, station_item:payload };
    },
  },

  effects: {
    // --------------------------------------站点-------------------------------------
    *fetch_getStationList ({ payload, callback }, { call, put }) {
      const response = yield call(getStationList, payload)
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_STATION_LIST',
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
    *fetch_getStationItem ({ payload, callback }, { call, put }) {
      const response = yield call(getStationItem, payload)
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_STATION_ITEM',
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
    *fetch_addStationItem ({ payload, callback }, { call, put }) {
      const response = yield call(addStationItem, payload)
      try {
        if (response.status === 0) {
          message.success(response.message)
          yield put({
            type: 'fetch_getStationList',
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
    *fetch_updStationItem ({ payload, callback }, { call, put }) {
      const response = yield call(updStationItem, payload)
      try {
        if (response.status === 0) {
          message.success(response.message)
          yield put({
            type: 'fetch_getStationList',
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
    *fetch_delStationItem ({ payload, callback }, { call, put }) {
      const response = yield call(delStationItem, payload)
      try {
        if (response.status === 0) {
          message.success(response.message)
          yield put({
            type: 'fetch_getStationList',
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