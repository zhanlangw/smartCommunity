import React, {PureComponent, Fragment} from 'react';
import {connect} from 'dva';
import {Row, Col, Button, Icon, Modal, Form, Input, Card, DatePicker} from 'antd';
import style from './style.less';
import moment from 'moment';

import Bar from './Bar';
import Radar from './Radar';
import Line from './Line';
import Map from './Map';
import List from './List';

import mapMax from '@/assets/mapMax.png';
import mapMin from '@/assets/mapMin.png';

const { TextArea } = Input;
const { MonthPicker, RangePicker } = DatePicker;

const dateFormat = 'YYYY/MM/DD';



@Form.create()
@connect(({ bigDataItem, loading }) => ({
  bigDataItem,
  loading: loading.models.bigDataItem
}))
class Item extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      isHeader: true,
    }
    this.Layouts = React.createRef();
    this.HeaderStyle = document.getElementsByTagName(('header'))[0];
    this.mainStyle = document.getElementsByTagName(('main'))[0];
    this.onFullScreen = this.onFullScreen.bind(this);
    this.handleRangePickerSubmit = this.handleRangePickerSubmit.bind(this);
  }

  onFullScreen() {
    this.setState({
      isHeader: !this.state.isHeader
    });

    if (this.state.isHeader) {
      this.HeaderStyle.style.display = 'none';
      this.mainStyle.style.padding = '0px';
      this.Layouts.current.style.height = '100vh';
      this.toggleFullScreen();
    } else {
      this.HeaderStyle.style.display = 'block';
      this.mainStyle.style.paddingTop = '64px';
      this.Layouts.current.style.height = '93.2vh';
      this.toggleFullScreen();
    }
  }

  toggleFullScreen() {
    let doc = document.documentElement,
      state = (document.webkitIsFullScreen || document.isFullScreen),
      requestFunc = (doc.requestFullscreen || doc.webkitRequestFullScreen),
      cancelFunc = (document.cancelFullScreen || document.webkitCancelFullScreen);

    (!state) ? requestFunc.call(doc) : cancelFunc.call(document);
  }

  onReSize = () => {
    this.line.resize()
    this.radar.resize()
    this.bar.resize()
  }

  handleRangePickerSubmit = (val) => {
    const { dispatch } = this.props;
    const payload = {
      startTime: moment(val[0]).format('YYYY/MM/DD'),
      endTime: moment(val[1]).format('YYYY/MM/DD')
    }
    dispatch({
      type: 'bigDataItem/fetch_getBarData',
      payload,
    })
    dispatch({
      type: 'bigDataItem/fetch_getListData',
      payload,
    })
    dispatch({
      type: 'bigDataItem/fetch_getMapData',
      payload,
    })
  }

  componentDidMount() {
    const { dispatch } = this.props;
    const payload = {
      startTime: moment(new Date()).subtract(1,'months').format('YYYY/MM/DD'),
      endTime: moment().format('YYYY/MM/DD')
    }
    window.addEventListener('keydown', function(event) {
      let code = (event.keyCode ? event.keyCode : event.which);
      if(code === 27) {
        window.event.returnValue = false;
        return false;
      }
    })
    window.addEventListener("resize",this.onReSize)

    dispatch({
      type: 'bigDataItem/fetch_getBarData',
      payload,
      callback: res => {

      }
    })
    dispatch({
      type: 'bigDataItem/fetch_getListData',
      payload,
    })
    dispatch({
      type: 'bigDataItem/fetch_getMapData',
      payload,
    })
    dispatch({
      type: 'bigDataItem/fetch_getOnlineData',
    })
    dispatch({
      type: 'bigDataItem/fetch_getSelectList',
      callback: res => {
        dispatch({
          type: 'bigDataItem/fetch_getSelectList',
          payload: {
            id: res[0].key
          },
        })
      }
    })

    this.intervalItem = setInterval(() => {
      dispatch({
        type: 'bigDataItem/fetch_getBarData',
        payload,
        callback: res => {

        }
      })
      dispatch({
        type: 'bigDataItem/fetch_getListData',
        payload,
      })
      dispatch({
        type: 'bigDataItem/fetch_getMapData',
        payload,
      })
      dispatch({
        type: 'bigDataItem/fetch_getOnlineData',
      })
      dispatch({
        type: 'bigDataItem/fetch_getSelectList',
        callback: res => {
          dispatch({
            type: 'bigDataItem/fetch_getSelectList',
            payload: {
              id: res[0].key
            },
          })
        }
      })
    }, 60000);
  }

  componentWillUnmount() {
    clearInterval(this.intervalItem);
    window.removeEventListener("resize", this.onReSize);
  }


  render() {
    const { bigDataItem: { barData, ListData, select_list, mapData, onlineData } } = this.props;

    return (
      <Fragment>
       <div className={style.BigDataLayouts} ref={this.Layouts}>
        <div className={style.BigDataHeader} span={24}>

          <div>机投桥街道办综合治理一体化平台</div>
          <div className={this.state.isHeader ? style.map_max : style.map_min}  onClick={this.onFullScreen}
           style={{position: 'absolute', right: 30, top:-11,  bottom:0, width: 40, height: 40, margin: 'auto'}}/>

        </div>
         <RangePicker
           defaultValue={[moment(moment(new Date()).subtract(1,'months').format('YYYY/MM/DD'), dateFormat), moment(moment().format('YYYY/MM/DD'), dateFormat)]}
           showTime
           onOk={this.handleRangePickerSubmit}
           format={dateFormat}
           style={{display:'block', margin: '0 auto', width: '270px'}}
         />
        <div style={{display: 'flex', overflow: 'hidden', justifyContent: "space-between"}}>
          <div span={17} style={{ width: '70%', marginTop: '1%'}}>
            <Row type='flex' style={{marginBottom: '10px'}}>
              <div style={{width: 'calc(36% - 10px)'}}>
                <List dataSource={ListData}/>
                <Radar selectDataSource={select_list} handleFn={fn => this.radar = fn }/>
              </div>
              <div style={{width:'64%', marginLeft: 10 }}>
                <Map dataSource={mapData} onlineData={onlineData} />
              </div>
            </Row>
            <Col span={24}>
              <Line selectDataSource={select_list} handleFn={fn => this.line = fn }/>
            </Col>
          </div>
          <div style={{ width: 'calc(30% - 10px)', marginTop: '1%', marginLeft: 10 }}>
            <Bar dataSource={barData.reverse()} handleFn={fn => this.bar = fn }/>
          </div>
        </div>
       </div>
      </Fragment>
    );
  }
}

export default Item
