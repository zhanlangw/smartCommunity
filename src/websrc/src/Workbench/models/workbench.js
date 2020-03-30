import { getList, getListAll, getTestList } from '@/services/workbench';
import {getFileBounceList} from "@/pages/File/services";
import {message} from "antd";

export default {

  namespace: 'workbench',

  state: {
    workbench_list: null,
    arrayIndex: null,
  },

  reducers: {
    setWorkbench_list(state, { payload }) {
       
      return { ...state, workbench_list: payload };
    },
    array_Index(state, {payload}) { 
      return { ...state, arrayIndex: payload };
    }
  },

  effects: {
    // *fetch_listAll({ callback }, { call, put }) {
    //   const response = yield call(getListAll);
    //   yield put({
    //     type: 'list_all',
    //     payload: response,
    //   });
    // },
    // *fetch_test_list({ callback }, { call, put }) {
    //   const response = yield call(getTestList);
    //   yield put({
    //     type: 'test_list',
    //     payload: response,
    //   });
    //   if (callback) {
    //     yield call(callback);
    //   }
    // },
  },

  // subscriptions: {
  //  init({dispatch}){
  //     dispatch({ });
  //  }
  // },

};
