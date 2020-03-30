import React, { useRef, useEffect } from 'react';
import Echarts from 'echarts';
import { connect } from 'dva';
import style from './style.less';
import {Select} from "antd";

const { Option } = Select;


function Radar(props) {

  const graph = useRef(null);
  const layout = useRef(null);
  const { selectDataSource, dispatch, handleFn, bigDataItem: { radarData } } = props;


  const lineOnSelect = val => {
    const Graph = Echarts.init(graph.current);
    Graph.showLoading();
    if(val) {
      dispatch({
        type: 'bigDataItem/fetch_getRadarData',
        payload: {
          unitId: val
        },
        callback: res => {
          Graph.hideLoading();
        }
      })
    } else {
      dispatch({
        type: 'bigDataItem/fetch_getRadarData',
        callback: res => {
          Graph.hideLoading();
        }
      })
    }
  }

  useEffect(() => {
    lineOnSelect();
  }, [])

  useEffect(() => {
    const Graph = Echarts.init(graph.current);
    handleFn(Graph)

    Graph.clear();
    Graph.setOption({
      tooltip: {
        confine: true,
        enterable: true, //鼠标是否可以移动到tooltip区域内
      },
      legend: {
        type: 'scroll',
        orient: 'vertical',
        right: 10,
        top: 50,
        bottom: 20,
        data: radarData.map(data => {
          return data.name
        }),
        textStyle: {
          color: '#fff'
        }
      },
      grid: {
        position: 'center',
        containLabel: true
      },
      radar: {
        // shape: 'circle',
        center: ['40%', '50%'],
        radius: '50%',
        name: {
          textStyle: {
            color: '#fff',
            borderRadius: 3,
            padding: [1,1]
          }
        },
        indicator: [
          { name: '按时处置率'},
          { name: '提前完成率'},
          { name: '一次完成率'},
          { name: '处置率'},
          { name: '到勤率'},
        ]
      },
      series: [{
        name: '雷达图',
        type: 'radar',
        data: radarData
      }]
    })
  }, [radarData, selectDataSource])

  return (
    <div
      className={style.BigDataRadar}
      ref={layout}
    >
      <header>
        <span className={style.spanStyle}>综合执法能力</span>
        {
          selectDataSource.length > 1 && (
            <Select
              size= "small"
              defaultValue={''}
              dropdownClassName={style.BigDataSelect}
              getPopupContainer={triggerNode => triggerNode.parentNode}
              onSelect={lineOnSelect}
            >
              <Option key='option_0' value={''} style={{color: '#fcfcfc'}}>全部</Option>
              {
                selectDataSource.map(option => {
                  return <Option key={option.key} value={option.key} style={{color: '#fcfcfc'}}>{option.title}</Option>
                })
              }
            </Select>
          )
        }
      </header>
      <div ref={graph} style={{width: '100%', height: '100%'}} />
    </div>
  )
}

export default React.memo(connect(({ bigDataItem })=>({
  bigDataItem
}))(Radar))
