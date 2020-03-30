import React, {PureComponent} from 'react';
import {connect} from 'dva';
import {Row, Col, Button, Icon, Modal, Form, Input, DatePicker, Card, Typography, message} from 'antd';
import moment from 'moment';
import styles from '../file.less';

import Select from './SelectComponent';
import Bar from './Bar';
import request from 'umi-request';
import {FileDownload, getUrl, token} from "@/utils/utils";
import {stringify} from "qs";


const {RangePicker} = DatePicker;


@Form.create()
@connect(({fileStat}) => ({
  fileStat,
}))
class Index extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      barData: [],
      rangePickerValue: null,
      selectComponentFromValue: 'all',
      bottomLoading: false,
      stateToken: token,
      isBreakpoint: true
    }
    this.selectComponentOnSelect = this.selectComponentOnSelect.bind(this);
    this.handleOnSearch = this.handleOnSearch.bind(this);
    this.handleDownload = this.handleDownload.bind(this);
  }


  calculate(state, end) {
    if (state) {
      return (state / end).toFixed(2)
    } else {
      return '0'
    }
  }

  selectComponentOnSelect(value) {
    this.setState({
      selectComponentFromValue: value
    }, () => {
      console.log(this.state.selectComponentFromValue)
    })
  }

  handleOnSearch() {
    const {selectComponentFromValue, rangePickerValue} = this.state;
    const {fileStat: {selectList}} = this.props;
    const self = this;
    if (rangePickerValue) {
      if (selectComponentFromValue !== 'all') {
        return request('/api/stats/histogram',
          {
            params: {
              unitId: selectComponentFromValue.key,
              startTime: moment(rangePickerValue[0]).format('YYYY/MM'),
              endTime: moment(rangePickerValue[1]).format('YYYY/MM'),
            },
            headers: {'Authorization': token}
          })
          .then(res => {
            if (res.status === 0) {
              let json = res.value.map(e => {
                return {
                  unit: {
                    ...e.histogram.unit
                  },
                  data: [
                    ...e.histogram.data
                  ],
                  stats: {
                    ...e.stats,
                    processing: self.calculate(e.stats.finishCount, e.stats.totalCount)
                  }
                }
              })
              this.setState({
                barData: json
              })
            } else {
              message.error('网络出错, 请重试')
            }
          })
      } else {
        this.setState({
          barData: []
        }, () => {
          this.getBarData(selectList, rangePickerValue)
        })
      }
    } else {
      message.error('查询日期不能为空')
    }
  }

  handleDownload() {
    const {selectComponentFromValue, rangePickerValue} = this.state;
    const {dispatch} = this.props;
    const {validateFields} = this.props.form;
    let params;
    let myHeaders = new Headers({
      "Authorization": token,
      Accept: 'application/json',
      'Content-Type': 'application/json; charset=utf-8',
    });
    let myInit = { method: 'GET',
      headers: myHeaders,
    };
    validateFields((err, value) => {
      if (err) return false
      if (rangePickerValue) {
        if (selectComponentFromValue !== 'all') {
          params = {
            unitId: selectComponentFromValue.key,
            endTime: moment(rangePickerValue[1]).format('YYYY/MM'),
            startTime: moment(rangePickerValue[0]).format('YYYY/MM'),
          }
        } else {
          params = {
            endTime: moment(rangePickerValue[1]).format('YYYY/MM'),
            startTime: moment(rangePickerValue[0]).format('YYYY/MM'),
          }
        }
      } else {
        message.error('查询日期不能为空')
      }
      fetch(getUrl(`/api/stats/histogram/export?${stringify(params)}`), myInit)
        .then(response => {
          response.blob().then(myBlob => {
            try {
              FileDownload(myBlob,'案卷统计.xlsx');
            } catch (error) {
              console.log(error)
              message.error('failed')
            }
          })
        })
    })
  }

  componentDidMount() {
    const {dispatch} = this.props;
    const self = this;
    dispatch({
      type: 'fileStat/fetch_getSelectList',
      callback: res => {
        dispatch({
          type: 'fileStat/fetch_getSelectList',
          payload: {
            id: res[0].key
          },
          callback: res => {
            this.getBarData(res);
          }
        })
      }
    })
  }

  getBarData = async (res, time) => {
    const self = this;
    const { stateToken, isBreakpoint} = this.state;
    this.setState({
      bottomLoading: true
    })
    if (time) {
      for (let i = 0; i < res.length; i++) {
        let data = res[i];
        if (isBreakpoint) {
          let resolve = await request('/api/stats/histogram',
            {
              params: {
                unitId: data.key,
                endTime: moment(time[1]).format('YYYY/MM'),
                startTime: moment(time[0]).format('YYYY/MM'),
              },
              headers: {'Authorization': stateToken}
            })

          if(resolve.status === 0) {
            let json = resolve.value.map(e => {
              return {
                unit: {
                  ...e.histogram.unit
                },
                data: [
                  ...e.histogram.data
                ],
                stats: {
                  ...e.stats,
                  processing: self.calculate(e.stats.finishCount, e.stats.totalCount)
                }
              }
            })
            this.setState({
              barData: this.state.barData.concat(json)
            })
          } else {
            return false
          }
        } else {
          return false
        }
      }
      this.setState({
        bottomLoading: false
      })
    } else {
      for (let i = 0; i < res.length; i++) {
        let data = res[i];
        if(isBreakpoint) {
          let resolve = await request('/api/stats/histogram',
            {
              params: {
                unitId: data.key,
                endTime: moment().format('YYYY/MM'),
                startTime: moment().format('YYYY/MM'),
              },
              headers: {'Authorization': stateToken}
            })

          if(resolve.status === 0) {
            let json = resolve.value.map(e => {
              return {
                unit: {
                  ...e.histogram.unit
                },
                data: [
                  ...e.histogram.data
                ],
                stats: {
                  ...e.stats,
                  processing: self.calculate(e.stats.finishCount, e.stats.totalCount)
                }
              }
            })
            this.setState({
              barData: this.state.barData.concat(json)
            })
          } else {
            return false
          }
        }else {
          return false
        }
      }
      this.setState({
        bottomLoading: false
      })
    }
  }

  handlePanelChange = (value, mode) => {
    this.setState({
      rangePickerValue: value,
      mode: [mode[0] === 'date' ? 'month' : mode[0], mode[1] === 'date' ? 'month' : mode[1]],
    });
  };

  handleChange = value => {
    this.setState({rangePickerValue: value}, () => {
      console.log(this.state.rangePickerValue)
    });
  };

  componentWillUnmount() {
    this.setState({
      isBreakpoint: false
    })
  }


  render() {
    const {form: {getFieldDecorator}, fileStat: {fileStatData, selectList}} = this.props;
    const {barData, rangePickerValue, bottomLoading} = this.state;
    return (
   <div className={styles.div_warp}>
        <Card bodyStyle={{padding: 0, margin: 0}} style={{background: '#fff'}} bordered={false}>
          <Row type="flex" justify="center" style={{padding: '50px 0', borderBottom: '1px solid #cdcdcd'}}>
            <Form className={styles.fileStateFormWarp}
                  style={{display: 'flex', justifyContent: 'space-around', width: '50%'}}>
              <RangePicker
                value={rangePickerValue}
                placeholder={[moment().format('YYYY/MM'), moment().format('YYYY/MM')]}
                format="YYYY-MM"
                mode={['month', 'month']}
                showTime
                onPanelChange={this.handlePanelChange}
                onChange={this.handleChange}
              />
              <Select onSelect={this.selectComponentOnSelect} selectDataSource={selectList} style={{width: '35%'}}/>
              <Button type="primary" style={{marginTop: 1}} onClick={this.handleOnSearch}
                      loading={bottomLoading}>查询</Button>
              <Button type="primary" style={{marginTop: 1}} onClick={this.handleDownload}>导出</Button>
            </Form>
          </Row>
        </Card>
        {
          barData.map(data => {
            return (
              <Bar key={data.unit.id} dataSource={data}/>
            )
          })
        }
   </div>
    );
  }
}

export default Index
