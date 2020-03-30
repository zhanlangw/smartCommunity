import {
  getFileStatData,
  getSelectList,
}from '../services'
import { message } from 'antd';
import {getLineData} from "@/pages/BigData/Services";



export default {
  namespace: 'fileStat',

  state: {
    fileStatData: [],
    selectList:[]
  },

  reducers: {
    GET_FILE_STAT_DATA(state, {payload}) {
      let json = payload.map(item => {
        return {
          unit:{
            ...item.histogram.unit
          },
          data:{
            ...item.histogram.data
          },
          stats: {
            ...item.stats,
            processing:calculate(item.stats.finishCount,item.stats.totalCount)
          }
        }
      })
      return {
        ...state,
        fileStatData: json.concat()
      }
    },
    GET_SELECT_LIST(state, {payload}) {
      return {
        ...state,
        selectList: payload
      }
    }
  },

  effects: {
    *fetch_getFileStatData({payload, callback}, {put, call, all}) {
      const response = yield all(getFileStatData, payload)
      console.log(response)
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_FILE_STAT_DATA',
            payload: response.value
          })
        } else {
          console.log(response)
          message.error(response.message)
        }
      } catch (e) {
        console.log(e)
      }
    },
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
