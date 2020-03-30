import React, {useRef, useEffect, Fragment, useState} from 'react';
import { Select } from 'antd';
import Echarts from 'echarts';
import { connect } from 'dva';

import style from './style.less';
const colorArray = ['rgba(151, 119, 255, 1)','rgba(216, 179, 67, 1)','rgba(61, 128, 232, 1)','rgba(220, 90, 85, 1)','rgba(54, 207, 105, 1)','rgba(236, 189, 157, 1)'];

const { Option } = Select;
let day = [];
for(let i = 0; i<13; i++ ) {
  day.push(i)
}


function Line(props) {

  // const [dataSource, setDataSource] = useState([]);
  const graph = useRef(null);
  const layout = useRef(null);

  const { selectDataSource, dispatch, bigDataItem: { LineData }, handleFn } = props;

  const lineOnSelect = (val) => {
    const Graph = Echarts.init(graph.current);
    Graph.showLoading();
    if(val) {
      dispatch({
        type: 'bigDataItem/fetch_getLineSelect',
        payload: {
          unitId: val
        },
        callback: res => {
          Graph.hideLoading();
        }
      })
    } else {
      dispatch({
        type: 'bigDataItem/fetch_getLineSelect',
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
    handleFn(Graph);

    Graph.clear();
    Graph.setOption({
      tooltip: {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
          type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        },
        textStyle:{
          align:'left'
        },
      },
      grid: {
        top: '20%',
        left: '2%',
        right: '2%',
        bottom: '5%',
        containLabel: true
      },
      legend: {
        icon:'roundRect',
        data: LineData.map(data => {
          return Object.keys(data)[0]
        }),
        top: 10,
        textStyle: {
          color: '#fff'
        }
      },
      // dataZoom : [
      //   {
      //     type: 'slider',//图表下方的伸缩条
      //     show : true,  //是否显示
      //     realtime : true,  //
      //     handleSize: 1,
      //     start : 80,  //伸缩条开始位置（1-100），可以随时更改
      //     end : 100,  //伸缩条结束位置（1-100），可以随时更改
      //     bottom:"auto", //组件离容器下侧的距离,'20%'
      //   },
      // ],
      xAxis: {
        type: 'category',
        boundaryGap: false,
        axisLine: {onZero: false},
        data: LineData.length? LineData.map(data => {
          return Object.values(data)[0].map(item => {
            return`${item.year}/${item.month}`
          })
        })[0]:day,
        axisLabel: {
          textStyle: {
            color: '#fff'
          }
        }
      },
      yAxis: {
        // type: 'value',
        axisLabel: {
          textStyle: {
            color: '#fff'
          }
        }
      },
      series: LineData.length? LineData.map((data, index) => {
        return {
          name:Object.keys(data)[0],
          type:'line',
          stack: '总量',
          itemStyle: {
            normal: {
              color: colorArray[index]
            }
          },
          data:Object.values(data).map(list => {
            return  list.map(item => {
              return item.count
            })
          })[0],
        }
      }) : []
    })

    return function() {
      window.onresize = null
    }
  }, [selectDataSource, LineData])

  return (
    <div
      className={style.BigDataLine}
      ref={layout}
    >
      <header>
        <span className={style.spanStyle}>高发案卷趋势图</span>
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
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-around', height: '78%'}}>
        <div className={style.line_left_icon}></div>
        <div ref={graph} style={{height: '100%', width: '100%' }} />
        <div className={style.line_right_icon}></div>
      </div>
    </div>

  )
}

export default React.memo(connect(({ bigDataItem })=>({
  bigDataItem
}))(Line))
