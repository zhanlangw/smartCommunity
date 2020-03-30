import {
  getProcessList,
  AddProcessItem,
  DelProcessItem,
  AddProcessLink,
  ProcessChart,
  getProcessChart,
  webIdMap,
  getProcessLinkItem,
  AddProcessLinkPath,
  getProcessPathItem,
  DelProcessPath,
  DelProcessLink,
  updProcessLinkItem,
  updProcessLinkPath,
  updProcessItem
} from '@/services/Process';
import { callStatusInfo } from '@/utils/utils';
import { message } from 'antd';
import { payload_0_10 as pagePayload } from '@/utils/utils';

export default {
  namespace: 'Process',

  state: {
    process_list: null,
    process_chart: null,
    process_nodeItem: null,
    web_id_map: null,
    process_link_item: null,
    process_path_item: null,
  },

  reducers: {
    getProcessList(state, { payload }) {
      return { ...state, process_list: payload};
    },
    GET_PROCESS_CHART(state, { payload }) {
      return {...state, process_chart: payload}
    },
    WEB_ID_MAP(state, { payload }) {
      return {...state, web_id_map: payload}
    },
    GET_PROCESS_LINK_ITEM(state, { payload }) {
      return {...state, process_link_item: payload}
    },
    GET_PROCESS_PATH_ITEM(state, { payload }) {
      return {...state, process_path_item: payload}
    }
  },

  effects: {
    *fetch_getProcessList({ payload, callback }, { call, put }) {
      const response = yield call(getProcessList, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'getProcessList',
            payload: response.value,
          });
          if (callback) {
            yield call(callback);
          }
        } else {
          message.error(response.message);
        }
      } catch (e) {
        console.log(e)
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_addProcessItem ({ payload, callback }, { call, put }) {
      const response = yield call(AddProcessItem, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'fetch_getProcessList',
            payload: pagePayload,
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
    },
    *fetch_delProcessItem ({ payload, callback }, { call, put}) {
      const response = yield call(DelProcessItem, payload);
      try {
        if (response.status === 0) {
          if (callback) {
            yield call(callback);
          }
        } else {
          message.error(response.message);
        }
      } catch (e) {
        message.error('请求失败，请稍后再试!');
      }
    },
    *fetch_getProcessChart({ payload }, {call, put}){
      const response = yield call(getProcessChart, payload);
        try {
          if (response.status === 0) {
            yield put({
              type: 'GET_PROCESS_CHART',
              payload: response.value,
            });
           } else {
             message.error(response.message);
          }
        } catch (e) {
          message.error('请求失败，请稍后再试！');
        }
    },
    *fetch_ProcessChart ({ payload }, {call, put}) {
      const response = yield call(ProcessChart, payload)
      try {
        if (response.status === 0){
          yield put({
            type: "fetch_getProcessChart",
            payload: {
              id: payload.id
            }
          })
          yield put({
            type: "fetch_webIdMap",
            payload: {
              id: payload.id
            }
          })
        }else{
          message.error(response.message)
        }
      } catch (e) {

      }
    },
    *fetch_webIdMap ({ payload }, { call, put }) {
      const response = yield call(webIdMap, payload)

      try {
        if (response.status === 0) {
          yield put({
            type: 'WEB_ID_MAP',
            payload: response.value
          })
        } else {
          message.error(response.message)
        }
      } catch (e) {

      }
    },
    *fetch_addProcessLink({ payload, params }, { call, put}) {
      const response = yield call(AddProcessLink, payload);

      try {
        if (response.status === 0) {
          message.success(response.message);
          yield put({
            type: 'fetch_ProcessChart',
            payload: params
          })
        }else{
          message.error(response.message);
        }
      } catch (e) {
        console.log(e)
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_getProcessLinkItem ({ payload, callback }, { call, put }) {
      const response = yield call(getProcessLinkItem, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_PROCESS_LINK_ITEM',
            payload: response.value
          })
          if(callback) {
            yield call(callback,response.value)
          }
        } else {
          message.error(response.message);
        }
      } catch (e) {
        console.log(e)
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_delProcessLink ({ payload, params }, { call, put }) {
      const response = yield call(DelProcessLink, payload);
      try {
        if (response.status === 0) {
          message.success(response.message);
          yield put({
            type: 'fetch_ProcessChart',
            payload: params
          })
        } else {
          message.error(response.message);
        }
      } catch (e) {
        console.log(e)
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_addProcessPath ({ payload, params, callback }, { call, put }) {
      const response = yield call(AddProcessLinkPath, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'fetch_ProcessChart',
            payload: params
          })
          if (callback) {
            yield call(callback, response.value)
          }
        } else {
          message.error(response.message);
        }
      } catch (e) {
        console.log(e)
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_getProcessPathItem ({ payload, callback }, { call, put }) {
      const response = yield call(getProcessPathItem, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_PROCESS_PATH_ITEM',
            payload: response.value
          })
          if (callback) {
            yield call(callback, response.value)
          }
        } else {
          message.error(response.message);
        }
      } catch (e) {
        console.log(e)
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_delProcessPath ({ payload, params }, { call, put }) {
      const response = yield call(DelProcessPath, payload);
      try {
        if (response.status === 0) {
          message.success(response.message);
          yield put({
            type: 'fetch_ProcessChart',
            payload: params
          })
        } else {
          message.error(response.message);
        }
      } catch (e) {
        console.log(e)
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_updProcessLinkItem ({ payload, params }, { call, put }) {
      const response = yield call(updProcessLinkItem, payload);

      try {
        if (response.status === 0) {
          message.success(response.message);
          yield put({
            type: 'fetch_ProcessChart',
            payload: params
          })
        } else {
          message.error(response.message);
        }
      } catch (e) {
        console.log(e)
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_updProcessLinkPath ({ payload, params, callback }, { call, put }) {
      const response = yield call(updProcessLinkPath, payload);
      try {
        if (response.status === 0) {
          message.success(response.message);
          yield put({
            type: 'fetch_ProcessChart',
            payload: params
          });
          if (callback) {
            yield call(callback, response.value)
          }
        } else {
          message.error(response.message);
        }
      } catch (e) {
        console.log(e)
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_updProcessItem ({ payload, callback }, { call, put }) {
      const response = yield call(updProcessItem, payload);
      try {
        if (response.status === 0) {
          message.success(response.message);
        } else {
          message.error(response.message);
        }
        if (callback) {
          yield call(callback, response.value)
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
