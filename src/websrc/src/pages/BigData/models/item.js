import {
  getBarData,
  getListData,
  getSelectList,
  getLineData,
  getRadarData,
  getMapData,
  getOnlineData
} from '../Services'
import  { message } from 'antd';
import {useEffect} from "react";


function calculate(state, end) {
  if(state) {
    return (state/end).toFixed(2)
  }else {
    return 0
  }
}

export default {
  namespace: 'bigDataItem',
  state: {
    barData: [],
    ListData: [],
    select_list: [],
    LineData: [],
    radarData:[],
    colorArray: ['rgba(151, 119, 255, 1)','rgba(216, 179, 67, 1)','rgba(61, 128, 232, 1)','rgba(220, 90, 85, 1)','rgba(54, 207, 105, 1)','rgba(236, 189, 157, 1)'],
    mapData:[],
    onlineData:[]
  },
  reducers: {
    GET_BAR_DATA(state, { payload } ) {
      return { ...state, barData: payload }
    },
    GET_LIST_DATA(state, { payload }) {
      return { ...state, ListData: payload}
    },
    GET_SELECT_LIST(state, { payload }) {
      return {
        ...state,
        select_list: payload
      }
    },
    GET_LINE_DATA(state, { payload}) {
      return {
        ...state,
        LineData: payload
      }
    },
    GET_RADAR_DATA(state, {payload}) {
      let json = []
      if(payload.length) {
        let value = payload.map(data => {
          return [
            calculate(data.onceCount,data.totalCount),
            calculate(data.advanceCount,data.totalCount),
            calculate(data.onceCount,data.totalCount),
            calculate(data.finishCount,data.totalCount),
            parseFloat(data.onlineRate)
          ]
        })

        let name = payload.map(data => {
          return data.time
        })

        json = payload.map((data,index) => {
          return {
            value: value[index],
            name: name[index],
            itemStyle: {
              color: state.colorArray[index],
            },
            areaStyle: {
              normal: {
                opacity: 0.2
              }
            },
          }
        })
      }
      return {
        ...state,
        radarData: json
      }
    },
    GET_MAP_DATA(state, { payload }) {
      let json = payload.map(item => {
        return [parseFloat(item.longitude), parseFloat(item.latitude), 1]
      })
      return {
        ...state,
        mapData: json
      }
    },
    GET_ONLINE_DATA(state, {payload}) {
      return {
        ...state,
        onlineData: payload
      }
    }
  },
  effects: {
    * fetch_getBarData({payload, callback}, {call, put}) {
      const response = yield call(getBarData, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_BAR_DATA',
            payload: response.value
          })
          if (callback) {
            yield call(callback, response.value)
          }
        } else {
          message.error(response.message)
        }
      } catch (e) {
        console.log(e)
      }
    },
    * fetch_getListData({payload}, {call, put}) {
      const response = yield call(getListData, payload);
      try {
        if (response.status === 0) {
          yield put({
            type: 'GET_LIST_DATA',
            payload: response.value
          })
        } else {
          message.error(response.message)
        }
      } catch (e) {
        console.log(e)
      }
    },
    *fetch_getSelectList({payload, callback}, {call, put}) {
      const response = yield call(getSelectList, payload);
      try{
        if(response.status === 0) {
           yield put({
             type: 'GET_SELECT_LIST',
             payload: response.value
           })
          if(callback) {
            yield call(callback, response.value)
          }
        } else {
          message.error(response.message)
        }
      } catch(e) {
        console.log(e)
      }
    },
    *fetch_getLineSelect({payload, callback}, {call, put}) {
      const response = yield call(getLineData, payload);
      try{
        if(response.status === 0) {
          yield put({
            type: 'GET_LINE_DATA',
            payload: response.value
          })
          if(callback) {
            yield call(callback, response.value)
          }
        } else {
          message.error(response.message)
        }
      } catch(e) {
        console.log(e)
      }
    },
    *fetch_getRadarData({payload, callback}, {call, put}) {
      const response = yield call(getRadarData, payload);
      try{
        if(response.status === 0) {
          yield put({
            type: 'GET_RADAR_DATA',
            payload: response.value
          })

          if(callback) {
            yield call(callback, response.value)
          }
        } else {
          message.error(response.message)
        }
      } catch(e) {
        console.log(e)
      }
    },
    *fetch_getMapData({payload, callback}, {put, call, all}) {
      const response = yield call(getMapData, payload);
      try{
        if(response.status === 0) {
          yield put({
            type: 'GET_MAP_DATA',
            payload: response.value
          })
        }else{
          message.error(response.message)
        }
      } catch(e) {
        console.log(e)
      }
    },
    *fetch_getOnlineData({payload, callback}, {put, call}) {
      const response = yield call(getOnlineData, payload)
        try{
          if(response.status === 0) {
            yield put({
              type: 'GET_ONLINE_DATA',
              payload: response.value
            })
          } else {
            message.error(response.message)
          }
        } catch (e) {
          message.error(e)
          console.log(e)
        }
    }
  },
}
