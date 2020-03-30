import {
  getSelectList
}from './Services'
import { message } from 'antd'

export default {
  namespace: "evaluate",
  state: {
    selectList: []
  },
  reducers: {
    GET_SELECT_LIST(state, {payload}) {
      return {
        ...state,
        selectList: payload
      }
    }
  },
  effects: {
    *fetch_getSelectList({payload, callback}, {put, call}) {
      const response = yield call(getSelectList, payload)
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_SELECT_LIST',
            payload: response.value
          })

          if(callback) {yield call(callback, response.value)}
        } else {
          message.error(response.message)
        }
      } catch (e) {
        console.log(e)
      }
    },
  }
}
