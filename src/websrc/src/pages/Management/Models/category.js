import {
  getBigCategoryList,
  addBigCategoryItem,
  getBigCategoryItem,
  updBigCategoryItem,
  delBigCategoryItem,
  getSmallCategoryList,
  addSmallCategoryItem,
  getSmallCategoryItem,
  updSmallCategoryItem,
  delSmallCategoryItem,
} from '@/services/category';
import { message } from 'antd';
import { payload_0_10 } from '@/utils/utils'

export default {

  namespace: 'category',

  state: {
    bigCategory_list: null,
    bigCategory_item: null,
    smallCategory_list: null,
    smallCategory_item: null,
  },

  reducers: {
    GET_BIG_CATEGORY_LIST(state, { payload }) {
      return { ...state, bigCategory_list: payload };
    },
    GET_BIG_CATEGORY_ITEM(state, { payload }) {
      return { ...state, bigCategory_item: payload };
    },
    GET_SMALL_CATEGORY_LIST (state, { payload }) {
      return { ...state, smallCategory_list: payload };
    },
    GET_SMALL_CATEGORY_ITEM (state, { payload }) {
      return { ...state, smallCategory_item: payload };
    },
  },

  effects: {
    // --------------------------------------大类-------------------------------------
    *fetch_getBigCategoryList({ payload, callback }, { call, put }) {
      const response = yield call(getBigCategoryList, payload)
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_BIG_CATEGORY_LIST',
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
    *fetch_getBigCategoryItem({ payload, callback }, { call, put }) {
      const response = yield call(getBigCategoryItem, payload)
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_BIG_CATEGORY_ITEM',
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
    *fetch_addBigCategoryItem ({ payload, callback }, { call, put }) {
      const response = yield call(addBigCategoryItem, payload)
      try {
        if (response.status === 0) {
          message.success(response.message)
          yield put({
            type: 'fetch_getBigCategoryList',
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
    *fetch_updBigCategoryItem ({ payload, callback }, { call, put }) {
      const response = yield call(updBigCategoryItem, payload)
      try {
        if (response.status === 0) {
          message.success(response.message)
          yield put({
            type: 'fetch_getBigCategoryList',
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
    *fetch_delBigCategoryItem ({ payload, callback }, { call, put }) {
      const response = yield call(delBigCategoryItem, payload)
      try {
        if (response.status === 0) {
          message.success(response.message)
          yield put({
            type: 'fetch_getBigCategoryList',
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

    //----------------------------------------------------小类------------------------------------------------
    *fetch_getSmallCategoryList ({ payload, callback }, { call, put }) {
      const response = yield call(getSmallCategoryList, payload)
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_SMALL_CATEGORY_LIST',
            payload: response.value,
          });
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
    *fetch_getSmallCategoryItem ({ payload, callback }, { call, put }) {
      const response = yield call(getSmallCategoryItem, payload)
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_SMALL_CATEGORY_ITEM',
            payload: response.value,
          });
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
    *fetch_addSmallCategoryItem ({ payload, callback }, { call, put }) {
      const response = yield call(addSmallCategoryItem, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'fetch_getSmallCategoryList',
            payload: {
              ...payload_0_10,
              id: payload.bigCategoryId
            }
          });
        } else {
          message.error(response.message);
        }
        if (callback) {
          yield call(callback, response);
        }
      } catch (e) {
        message.error('请求失败，请稍后再试！');
      }
    },
    *fetch_updSmallCategoryItem ({ payload, callback }, { call, put }) {
      const response = yield call(updSmallCategoryItem, payload);
      try {
        if (response.status === 0) {
          message.success(response.message)
          yield put({
            type: 'fetch_getSmallCategoryList',
            payload: {
              ...payload_0_10,
              id: payload.bigCategoryId
            }
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
    *fetch_delSmallCategoryItem ({ payload, callback }, { call, put }) {
      const response = yield call(delSmallCategoryItem, payload);
      try {
        if (response.status === 0) {
          message.success(response.message);
          yield put({
            type: 'fetch_getSmallCategoryList',
            payload: {
              ...payload_0_10,
              id: payload.id
            }
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
    }

    //------------------------------------------------------------------------------------------------------->
  },

  // subscriptions: {
  //  init({dispatch}){
  //     dispatch({ });
  //  }
  // },

};