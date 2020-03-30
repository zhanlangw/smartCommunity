import React, { useState, useEffect, useRef } from 'react';
import { Row, Card, Typography } from 'antd';
import Echarts from 'echarts';

const { Title, Text } = Typography;

function Bar(props) {

  const [barData, setBarData] = useState(null);
  const block = useRef(null);
  const { dataSource } = props;

  useEffect(()=> {
    const oneBlock = Echarts.init(block.current);
    window.addEventListener('resize', function() {
      oneBlock.resize();
    })

    if(block) {
      oneBlock.clear();
      oneBlock.setOption({
        title: {
          text: '统计高发案卷前十项',
          x: 'center',
          textStyle: {
            color: '#666666',
            fontWeight: 'normal',
            fontFamily: 'MicrosoftYaHei',
          }
        },
        tooltip: {},
        xAxis: {
          data: dataSource.data.map(item => {
            return item.categoryName
          })
        },
        yAxis: {},
        series: [{
          name: '',
          type: 'bar',
          data: dataSource.data.map(item => {
            return item.count
          }),
          barWidth: 40,
          itemStyle: {    // 图形的形状
            color: '#4C5FC1',
          },
          barFontSize: 12,
        }]
      });

      setBarData(oneBlock);
    }

    return () => {
      window.removeEventListener('resize', function() {
        oneBlock.resize();
      })
    }
  }, [dataSource]);

  return (
    <Card bodyStyle={{padding:0, margin:0, paddingBottom: '3vh'}} style={{background: '#fff', marginBottom: 10}} bordered={false}>
      <Title level={4} style={{ textAlign: 'center', margin:'20px', color:'#4C5FC1', fontWeight:'normal' }}>{dataSource.unit.name}</Title>
      <Row span={24} type='flex' justify="space-around">
        <Row span={5} type='flex' style={{height: '43vh', marginBottom: '3vh'}}>
          {/*{*/}
          {/*  blockMenu.map(item=>{*/}
          {/*    return (*/}
          {/*      <div*/}
          {/*        key={item.name}*/}
          {/*        style={{*/}
          {/*          textAlign: 'center',*/}
          {/*          marginLeft: '5%',*/}
          {/*          marginRight: '5%',*/}
          {/*          marginBottom: '5%',*/}
          {/*          width: '40%',*/}
          {/*          height: '30%',*/}
          {/*          backgroundColor: '#F0F2F5',*/}
          {/*        }}*/}
          {/*      >*/}
          {/*        <Text style={{ lineHeight: '518%' }}>{item.name}</Text>*/}
          {/*        <Title level={2} style={{ margin:0, color: '#3A87E4', lineHeight: '61%' }}>{item.unm}</Title>*/}
          {/*      </div>*/}
          {/*    )*/}
          {/*  })*/}
          {/*}*/}
          <div
            style={{
              textAlign: 'center',
              marginLeft: '5%',
              marginRight: '5%',
              marginBottom: '5%',
              width: '40%',
              height: '30%',
              backgroundColor: '#F0F2F5',
            }}
          >
            <Text style={{ lineHeight: '518%' }}>案卷总数</Text>
            <Title level={2} style={{ margin:0, color: '#3A87E4', lineHeight: '61%' }}>{dataSource.stats.totalCount}</Title>
          </div>
          <div
            style={{
              textAlign: 'center',
              marginLeft: '5%',
              marginRight: '5%',
              marginBottom: '5%',
              width: '40%',
              height: '30%',
              backgroundColor: '#F0F2F5',
            }}
          >
            <Text style={{ lineHeight: '518%' }}>已处置案卷总数</Text>
            <Title level={2} style={{ margin:0, color: '#3A87E4', lineHeight: '61%' }}>{dataSource.stats.finishCount}</Title>
          </div>
          <div
            style={{
              textAlign: 'center',
              marginLeft: '5%',
              marginRight: '5%',
              marginBottom: '5%',
              width: '40%',
              height: '30%',
              backgroundColor: '#F0F2F5',
            }}
          >
            <Text style={{ lineHeight: '518%' }}>未处置案卷总数</Text>
            <Title level={2} style={{ margin:0, color: '#3A87E4', lineHeight: '61%' }}>{dataSource.stats.unFinishCount}</Title>
          </div>
          <div
            style={{
              textAlign: 'center',
              marginLeft: '5%',
              marginRight: '5%',
              marginBottom: '5%',
              width: '40%',
              height: '30%',
              backgroundColor: '#F0F2F5',
            }}
          >
            <Text style={{ lineHeight: '518%' }}>超时案卷总数</Text>
            <Title level={2} style={{ margin:0, color: '#3A87E4', lineHeight: '61%' }}>{dataSource.stats.timeOutCount}</Title>
          </div>
          <div
            style={{
              textAlign: 'center',
              marginLeft: '5%',
              marginRight: '5%',
              marginBottom: '5%',
              width: '40%',
              height: '30%',
              backgroundColor: '#F0F2F5',
            }}
          >
            <Text style={{ lineHeight: '518%' }}>处理完成率</Text>
            <Title level={2} style={{ margin:0, color: '#3A87E4', lineHeight: '61%' }}>{dataSource.stats.processing}</Title>
          </div>
          <div
            style={{
              textAlign: 'center',
              marginLeft: '5%',
              marginRight: '5%',
              marginBottom: '5%',
              width: '40%',
              height: '30%',
              backgroundColor: '#F0F2F5',
            }}
          >
            <Text style={{ lineHeight: '518%' }}>高发案卷</Text>
            <Title level={2} style={{ margin:0, color: '#3A87E4', lineHeight: '61%' }}>{dataSource.stats.highIncidenceCount}</Title>
          </div>
        </Row>
        <div style={{ width: '60%', height: '43vh',  border: '1px solid rgba(217,217,217,1)', padding: '1%'}}>
          <div ref={block} style={{ width: '100%', height: '100%'}}/>
        </div>
      </Row>
    </Card>
  )
}

export default React.memo(Bar)
