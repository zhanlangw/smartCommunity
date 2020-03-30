import React, { useRef, useEffect, Fragment } from 'react';
import { message } from 'antd';
import Echarts from 'echarts';
import bmap from "echarts/extension/bmap/bmap";
import { connect } from 'dva';
import request from 'umi-request';
import { getAuthority } from '@/utils/authority';

import style from './style.less';
import as from './as';

function Map(props) {

  const graph = useRef(null);
  const { dataSource, onlineData: { userOnlineRate, userCount, deviceOnlineRate, deviceCount } } = props;

  const Left = function() {
    const style= {
      div: {position: 'absolute', width: '9vw', top: 0, left: 0},
      st0: {fill:'#50FEC1'},
      st1: {fill:'none', stroke:'#ADB9E7', strokeMiterlimit:10,},
      st2: {fill:'none', stroke:'#50FEC1', strokeWidth:3, strokeMAiterlimit:10},
      st3: {fontFamily:'SourceHanSansCN-Regular-GBpc-EUC-H'},
      st4: {fontSize:'14px',},
      st5: {fontSize:'25px',},
      st6: {fill:'#808BA0',},
      st7: {fontSize:'30px',},
      st8:{fill:"#fff"},
    }

    return (
      <div style={style.div}>
        <svg
          version="1.1" id="left_svg" xmlns="http://www.w3.org/2000/svg"
          xmlns="http://www.w3.org/1999/xlink"
          x="0px"
          y="0px"
          viewBox="0 0 206 132"
        >
          <g>
            <rect x="111" y="65.1" style={style.st2} width="93" height="0.9"/>
          </g>
          <circle style={style.st1} cx="59.4" cy="66" r="51.6"/>
          <path style={style.st2}
                d="M92.8,107c-8.9,7-20,11.1-32.2,11.1C31.8,118.2,8.4,94.8,8.4,66s23.3-52.2,52.2-52.2c12.2,0,23.3,4.2,32.2,11.1"/>
          <text transform="matrix(1 0 0 1 21.6667 52.6668)"  style={Object.assign({}, style.st0, style.st3,style.st4)}>今日在线率</text>
          <text transform="matrix(1 0 0 1 32.3332 97)"  style={Object.assign({}, style.st0, style.st3,style.st5)}>{userOnlineRate}</text>
          <text transform="matrix(1 0 0 1 116 57)" style={Object.assign({}, style.st6, style.st3,style.st4,style.st8)}>执法人员总数</text>
          <text transform="matrix(1 0 0 1 133 97)" style={Object.assign({}, style.st6, style.st3,style.st7,style.st8)}>{userCount}</text>
        </svg>
      </div>
    )
  };

  const Right = function() {
    const style = {
      div:{position: 'absolute', top: 0, right: 0, width: '9vw'},
      st0:{fill:'#50FEC1'},
      st1:{fill:'none',stroke:'#ADB9E7',strokeMiterlimit:10},
      st2:{fill:'none',stroke:'#50FEC1',strokeWidth:3,strokeMiterlimit:10},
      st3:{fontFamily:'SourceHanSansCN-Regular-GBpc-EUC-H'},
      st4:{fontSize:'14px'},
      st5:{fontSize:'25px'},
      st6:{fill:'#808BA0'},
      st7:{fontSize:'26px'},
      st8:{fill:"#fff"},
      st9:{fill:"#50FEC1"}
    }

    return (
      <div style={style.div}>
        <svg version="1.1"
             id="right_svg" xmlns="http://www.w3.org/2000/svg"
             xmlns="http://www.w3.org/1999/xlink"
             x="0px"
             y="0px"
             viewBox="0 0 206 132"
             xml="preserve">
          <g>
            <rect x="3" y="63.1" style={style.st2} width="93" height="0.9"/>
          </g>
          <circle style={style.st1} cx="147.8" cy="66" r="51.6"/>
          <path style={style.st2}
                d="M181.2,107c-8.9,7-20,11.1-32.2,11.1c-28.8,0.1-52.2-23.3-52.2-52.1s23.3-52.2,52.2-52.2c12.2,0,23.3,4.2,32.2,11.1"
          />
          <text transform="matrix(1 0 0 1 110.0667 52.6668)"  style={Object.assign({}, style.st0, style.st3,style.st4, style.st9)}>今日在线率</text>
          <text transform="matrix(1 0 0 1 120.7332 97)"  style={Object.assign({}, style.st0, style.st3,style.st5, style.st9)}>{deviceOnlineRate}</text>
          <text transform="matrix(1 0 0 1 11.2 57)" style={Object.assign({}, style.st6, style.st3,style.st4, style.st8)}>摄像机总数</text>
          <text transform="matrix(1 0 0 1 16 97)" style={Object.assign({}, style.st6, style.st3,style.st7, style.st8)}>{deviceCount}</text>
        </svg>
      </div>
    )
  };

  useEffect(() => {
    let Graph = Echarts.init(graph.current);
    request('api/basis/item', { headers: { 'Authorization': getAuthority().token } })
      .then(res => {
        console.log(res);
        if(res.status === 0) {
          
          
          Graph.setOption({
            animation: false,
            bmap: {
              center: [res.value.longitude, res.value.latitude],
              zoom: 16,
              roam: true
            },
            visualMap: {
              show: false,
              top: 'top',
              min: 0,
              max: 5,
              seriesIndex: 0,
              calculable: true,
              inRange: {
                color: ['blue', 'blue', 'green', 'yellow', 'red']
              }
            },
            series: [{
              type: 'heatmap',
              coordinateSystem: 'bmap',
              data: dataSource,
              pointSize: 5,
              blurSize: 6
            }]
          }, [dataSource])
          let bmap = Graph.getModel().getComponent('bmap').getBMap();
          // bmap.addControl(new BMap.MapTypeControl());
          bmap.setMapStyle({ style: 'midnight' });
        } else {
          message.error('网络请求出错,请重试')
        }
      })

    // bmap.addControl(new BMap.MapTypeControl());
        // if (!app.inNode) {
        //   // 添加百度地图插件
          // let bmap = Graph.getModel().getComponent('bmap').getBMap();
          // bmap.addControl(new BMap.MapTypeControl());
          // bmap.setMapStyle({style:'midnight'});
        // }
  }, [dataSource])

  return (
    <div style={{position: 'relative'}}>
      <div ref={graph} className={style.BigDataMap}></div>
      <Left />
      <Right />
    </div>

  )
}

export default Map
