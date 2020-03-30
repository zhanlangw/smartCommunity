import React, { useRef, useState, useEffect } from 'react';
import {} from 'antd';
import Echarts from 'echarts';
import { connect } from 'dva';
import style from './style.less';

function Bar(props) {

  const graph = useRef(null);
  const layout = useRef(null);
  const { dataSource,handleFn } = props;

  // const handleResize = () => {
  //   const Graph = Echarts.init(graph.current);
  //   let resizeWorldMapContainerLine = function () {//用于使chart自适应高度和宽度,通过窗体高宽计算容器高宽
  //     graph.current.style.width = layout.current.style.width +'px';
  //     graph.current.style.height = layout.current.style.height +'px';
  //   };
  //
  //
  //   window.onresize = function () {//用于使chart自适应高度和宽度
  //     resizeWorldMapContainerLine();//重置容器高宽
  //     Graph.resize();
  //   }
  // }

  useEffect(() => {
    const Graph = Echarts.init(graph.current);
    handleFn(Graph)

      Graph.clear();
      Graph.setOption({
      tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
          type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        },
        textStyle:{
          align:'left'
        },
        position: function(pos, params, el, elRect, size) {
          let spanList = el.getElementsByTagName('span');
          for(let i=0; i<spanList.length; i++) {
            spanList[i].style.padding = '0' ;
          }
        }
      },
      legend: {
        data: ['超时案卷', '未处置案卷','已处置案卷'],
        top: 25,
        textStyle: {
          color: '#fff'
        },
      },
      radar:{
        radius: '100%',
      },
      grid: {
        left: '4%',
        right: 2,
        bottom: 2,
        containLabel: true
      },
      xAxis:  {
        type: 'value',
        show: false,
        axisLabel: {
          textStyle: {
            color: '#fff'
          }
        }
      },
      yAxis: {
        type: 'category',
        data:dataSource.map(item => {
          return item.unitName
        }),
        axisTick: {show: false},
        axisLine: {show: false},
        axisLabel: {
          textStyle: {
            color: '#fff',
            padding:[1,10,1,1]
          }
        }
      },
      series: [
        {
          name: '超时案卷',
          type: 'bar',
          stack: '总量',
          barWidth: 30,//固定柱子宽度
          label: {
            normal: {
              show: true,
              position: 'insideRight'
            }
          },
          itemStyle: {
            normal: {
              color: "rgba(241,145,73,1)"
            },
          },
          data:dataSource.map(item => {
            return item.timeOutCount
          }),
        },
        {
          name: '未处置案卷',
          type: 'bar',
          stack: '总量',
          barWidth: 30,//固定柱子宽度
          label: {
            normal: {
              show: true,
              position: 'insideRight'
            }
          },
          itemStyle: {
            normal: {
              color: "rgba(0,114,255,1)"
            }
          },
          data:dataSource.map(item => {
            return item.unFinishCount
          }),
        },
        {
          name: '已处置案卷',
          type: 'bar',
          stack: '总量',
          barWidth: 30,//固定柱子宽度
          label: {
            normal: {
              show: true,
              position: 'insideRight'
            }
          },
          itemStyle: {
            normal: {
              color: "rgba(151,119,255,1)"
            }
          },
          data:dataSource.map(item => {
            return item.finishCount
          }),
        },
        {
          name: '案卷总数',
          type: 'bar',
          stack: '总量',
          barWidth: 30,//固定柱子宽度
          label: {
            normal: {
              show: true,
              position: 'insideRight'
            }
          },
          itemStyle: {
            normal: {
              color: "rgba(151,119,255,0)"
            }
          },
          data:dataSource.map(item => {
            return item.finishCount+item.unFinishCount
          }),
        },
      ]
    });
  }, [dataSource])

  return (
    <div className={style.BigDataBar} ref={layout}>
      <span>街区工作统计</span>
      <div ref={graph} style={{width: '100%', height: '95%'}}/>
    </div>
  )
}

export default Bar;
