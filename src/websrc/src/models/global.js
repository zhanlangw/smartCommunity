import { queryNotices } from '@/services/user';
import { getNoUserTree, getOrganizationTreeAction } from '@/services/organization';
import {
  addFile,
  delForFiles,
  delHasFiles,
  fileAccept,
  fileEnd,
  fileSubmit,
  fileWithdraw,
  delFileHistory,
  getFileBounceList,
  getFileDelayedList,
  getFileDisposalList,
  getFileEventList,
  getFileHandleList,
  getFileInspectList,
  getFileItemData,
  getFileLogList,
  getWorkPath
} from '@/pages/File/services';
import  {
  getGlobalSearchData,
  getUserItem
} from '@/services/global'
import { message } from "antd";
import { getDeviceTree } from "@/services/userManagement";

const GlobalModel = {
  namespace: 'global',
  state: {
    collapsed: false,
    notices: [],
    tree: [],
    unit_tree: null,
    handle_list: null,
    menu_data: null,
    event_list: null,
    fileItemData: null,
    fileLog_list:null,
    drawer_visible: false,
    type_api: null,
    footer_search: null,
    globalSearchData: [],
    userItem: null,
    eventPage: {
      start: 0,
      count: 1
    },
    searchPage: 1,
  },
  effects: {
    *fetchNotices (_, { call, put, select }) {
      const data = yield call(queryNotices);
      yield put({
        type: 'saveNotices',
        payload: data,
      });
      const unreadCount = yield select(
        state => state.global.notices.filter(item => !item.read).length,
      );
      yield put({
        type: 'user/changeNotifyCount',
        payload: {
          totalCount: data.length,
          unreadCount,
        },
      });
    },
    *fetch_get_unit ({ payload }, { call, put, select }) {
      const response = yield call(getOrganizationTreeAction, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'unit_tree',
            payload: response.value
          });
        } else {
          message.error(response.message);
        }
      } catch (error) {
        console.log(error)
        message.error('请求失败，请稍后再试！');
      }
    },
    *clearNotices ({ payload }, { put, select }) {
      yield put({
        type: 'saveClearedNotices',
        payload,
      });
      const count = yield select(state => state.global.notices.length);
      const unreadCount = yield select(
        state => state.global.notices.filter(item => !item.read).length,
      );
      yield put({
        type: 'user/changeNotifyCount',
        payload: {
          totalCount: count,
          unreadCount,
        },
      });
    },
    *fetch_get_tree ({ payload, callback }, { call, put, select }) {
      const response = yield call(getNoUserTree, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'tree',
            payload: response.value
          });
          if (callback) {
            yield call(callback, response.value)
          }
        } else {
          message.error(response.message);
        }
      } catch (error) {
        console.log(error);
        message.error('请求失败，请稍后再试！');
      }
    },
    *changeNoticeReadState ({ payload }, { put, select }) {
      const notices = yield select(state =>
        state.global.notices.map(item => {
          const notice = { ...item };

          if (notice.id === payload) {
            notice.read = true;
          }

          return notice;
        }),
      );
      yield put({
        type: 'saveNotices',
        payload: notices,
      });
      yield put({
        type: 'user/changeNotifyCount',
        payload: {
          totalCount: notices.length,
          unreadCount: notices.filter(item => !item.read).length,
        },
      });
    },
    *fetch_addFile({ payload, callback }, { call, put }) {
      const response = yield call(addFile, payload);
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
        console.log(e)
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_getFileHandleList({ payload, callback }, { call, put }) {
      const response = yield call(getFileHandleList, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_FILE_HANDLE_LIST',
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
    *fetch_getDeviceTree ({ payload, callback }, { call, put }) {
      const response = yield call(getDeviceTree, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_MENU_DATA',
            payload: response.value
          });
          if (callback) {
            yield call(callback);
          }
        } else {
          message.error(response.message);
        }
      } catch (e) {
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_getFileBounceList({ payload, callback }, { call, put }) {
      const response = yield call(getFileBounceList, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_FILE_BOUNCE_LIST',
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
    *fetch_getFileDelayedList({ payload, callback }, { call, put }) {
      const response = yield call(getFileDelayedList, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_FILE_DELAYED_LIST',
            payload: response.value,
          });
        } else {
          message.error(response.message);
        }
        if (callback) {
          yield call(callback, response);
        }
      } catch (e) {
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_getFileDisposalList({ payload, callback }, { call, put }) {
      const response = yield call(getFileDisposalList, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_FILE_DISPOSAL_LIST',
            payload: response.value,
          });
        } else {
          message.error(response.message);
        }
        if (callback) {
          yield call(callback, response);
        }
      } catch (e) {
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_getFileInspectList({ payload, callback }, { call, put }) {
      const response = yield call(getFileInspectList, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_FILE_INSPECT_LIST',
            payload: response.value,
          });
        } else {
          message.error(response.message);
        }
        if (callback) {
          yield call(callback, response);
        }
      } catch (e) {
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_getFileEventList({ payload, callback }, { call, put }) {
      const response = yield call(getFileEventList, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_EVENT_LIST',
            payload: response.value,
            params: payload
          });
        } else {
          message.error(response.message);
        }
        if (callback) {
          yield call(callback, response);
        }
      } catch (e) {
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_getFileItemData({ payload, callback }, {call, put}) {
      const response = yield call(getFileItemData, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_FILE_ITEM_DATA',
            payload: response.value,
          });
        } else {
          message.error(response.message);
        }
        if (callback) {
          yield call(callback, response);
        }
      } catch (e) {
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_delFileHistory({ payload, callback }, { call, put }) {
      const response = yield call(delFileHistory, payload);
      try {
        if (response.status === 0) {
        } else {
          message.error(response.message);
        }
        if (callback) {
          yield call(callback, response);
        }
      } catch (e) {
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_getFileLogList({ payload, callback }, { call, put }) {
      const response = yield call(getFileLogList, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_FILE_LOG_LIST',
            payload: response.value,
          });
        } else {
          message.error(response.message);
        }
        if (callback) {
          yield call(callback, response);
        }
      } catch (e) {
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_delHasFiles({ payload, callback }, { call, put }) {
      const response = yield call(delHasFiles, payload);
      try {
        if (response.status === 0) {
          message.success(response.message);
        } else {
          message.error(response.message);
        }
        if (callback) {
          yield call(callback, response);
        }
      } catch (e) {
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_delForFiles({ payload, callback }, { call, put }) {
      const response = yield call(delForFiles, payload);
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
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_getWorkPath({ payload, callback }, { call, put }) {
      const response = yield call(getWorkPath, payload);
      try {
        if (response.status === 0) {
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
    *fetch_fileSubmit({ payload, callback }, { call, put }) {
      const response = yield call(fileSubmit, payload);
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
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_fileEnd({ payload, callback }, { call, put }) {
      const response = yield call(fileEnd, payload);
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
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_fileAccept({ payload, callback }, { call, put }) {
      const response = yield call(fileAccept, payload);
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
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_fileWithdraw({ payload, callback }, { call, put }) {
      const response = yield call(fileWithdraw, payload);
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
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_getGlobalSearchData({ payload, callback }, {call, put}) {
      const response = yield call(getGlobalSearchData, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_GLOBAL_SEARCH_DATA',
            payload: response.value
          })
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
    *fetch_getUserItem({ payload, callback }, {call, put}) {
      const response = yield call(getUserItem, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_USER_ITEM',
            payload: response.value
          })
          if (callback) {
            yield call(callback, response.value);
          }
        } else {
          message.error(response.message);
        }
      } catch (e) {
        console.log(e);
        message.error('请求失败，请稍后再试！');
      }
    }
  },

  reducers: {
    GET_USER_ITEM(state, {payload}) {
      return {...state, userItem: payload}
    },
    GET_GLOBAL_SEARCH_DATA(state, { payload }) {
      return { ...state, globalSearchData: payload }
    },
    SET_DRAWER_VISIBLE (state, { payload}) {
       return {...state, drawer_visible: payload};
    },
    SET_TYPE_API (state, { payload}) {
      return {...state, type_api: payload};
    },
    SET_FOOTER_SEARCH (state, { payload}) {
      return {...state, footer_search: payload};
    },
    GET_FILE_LOG_LIST (state, { payload }) {
      return { ...state, fileLog_list: payload };
    },
    GET_FILE_ITEM_DATA(state, { payload }) {
      return { ...state, fileItemData: payload };
    },
    GET_EVENT_LIST(state, { payload, params }) {
      return { ...state, event_list: payload, eventPage: params  };
    },
    GET_FILE_INSPECT_LIST(state, { payload }) {
      return { ...state, inspect_list: payload };
    },
    GET_FILE_DISPOSAL_LIST(state, { payload }) {
      return { ...state, disposal_list: payload };
    },
    GET_FILE_DELAYED_LIST(state, { payload }) {
      return { ...state, delayed_list: payload };
    },
    GET_FILE_BOUNCE_LIST(state, { payload }) {
      return { ...state, bounce_list: payload };
    },
    changeLayoutCollapsed (
      state = {
        notices: [],
        collapsed: true,
      },
      { payload },
    ) {
      return { ...state, collapsed: payload };
    },
    tree (state, { payload }) {
      return {
        ...state,
        tree: payload,
      };
    },
    GET_FILE_HANDLE_LIST(state, { payload }) {
      return { ...state, handle_list: payload };
    },
    GET_MENU_DATA(state, { payload }) {
      return { ...state, menu_data: payload };
    },
    unit_tree (state, { payload }) {
      return {
        ...state,
        unit_tree: payload,
      };
    },
    saveNotices (state, { payload }) {
      return {
        collapsed: false,
        ...state,
        notices: payload,
      };
    },
    saveClearedNotices (
      state = {
        notices: [],
        collapsed: true,
      },
      { payload },
    ) {
      return {
        collapsed: false,
        ...state,
        notices: state.notices.filter(item => item.type !== payload),
      };
    },
    setSearchPage (state, { payload }) {
      return {
        ...state,
        searchPage: payload,
      }
    }
  },
  subscriptions: {
    setup ({ history }) {
      // Subscribe history(url) change, trigger `load` action if pathname is `/`
      history.listen(({ pathname, search }) => {
        if (typeof window.ga !== 'undefined') {
          window.ga('send', 'pageview', pathname + search);
        }
      });
    },
  },
};
export default GlobalModel;
