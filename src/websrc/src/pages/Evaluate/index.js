import React, { PureComponent } from "react";
import { connect } from 'dva';
import {DatePicker, Row, Button, Col, Table, Form, message} from 'antd';
import style from './style.less';
import moment from 'moment';
import request from 'umi-request';
import _ from 'lodash';
import {FileDownload, getUrl, token} from "@/utils/utils";
import {stringify} from "qs";

const { MonthPicker } = DatePicker;

@Form.create()
@connect(({evaluate})=>({
  evaluate
}))
export default class Index extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      dataSource: [],
      isBreakpoint: true, //断开持续请求,
      bottomLoading: false,
      stateToken: token,
    }
    this.handleOnSearch = this.handleOnSearch.bind(this);
    this.handleDownload = this.handleDownload.bind(this);
  }

  handleOnSearch() {
    const { dispatch, evaluate: { selectList } } = this.props;
    const { validateFields } = this.props.form;
    validateFields((err, value)=> {
      if(err) return false
      this.setState({
        dataSource: []
      }, () => {
        this.getData(selectList, moment(value.item).format('YYYY/MM'))
      })
    })
  }

  handleDownload() {
    const { validateFields } = this.props.form;
    const { dispatch } = this.props;
    validateFields((err, value)=> {
      if(err) return false
      let myHeaders = new Headers({
        "Authorization": token,
        Accept: 'application/json',
        'Content-Type': 'application/json; charset=utf-8',
      });
      let myInit = { method: 'GET',
        headers: myHeaders,
      };
      let params = {
        time: moment(value.item).format('YYYY/MM')
      }
      fetch(getUrl(`/api/stats/score/export?${stringify(params)}`), myInit)
        .then(response => {
          let downHeader = response.headers.get('Content-Disposition');
          response.blob().then(myBlob => {
            try {
              FileDownload(myBlob,'案卷评价.xlsx');
            } catch (error) {
              console.log(error)
              message.error('failed')
            }
          })
        })
    })
  }

  getData = async(res, time) => {
    const { isBreakpoint, stateToken } = this.state;
    this.setState({
      bottomLoading: true
    })
    if(time) {
      for (let i = 0; i < res.length; i++) {
        let data = res[i];
        if(isBreakpoint) {
        let newSourceData = await request('/api/stats/score',
          { params: {
              unitId: data.key,
              time:moment(time).format('YYYY/MM'),
            },
            headers:{ 'Authorization': stateToken},
          })
          if(newSourceData.status === 0) {
            this.setState({
              dataSource: this.state.dataSource.concat(newSourceData.value[0])
            })
          } else {
            return false;
          }
        } else {
          return false
        }
      }
      this.setState({
        bottomLoading: false
      }, () => {
        let params = this.state.dataSource.map(item => {
          return {
            unitId: item.unitId,
            score: item.score
          }
        })
        request('/api/stats/score/ranking', {method: 'post', data: params })
          .then(res => {
            let dataSource = _.cloneDeep(this.state.dataSource);
            if(res.status === 0) {
              res.value.map(item => {
                dataSource.map(data => {
                  if(data.unitId === Object.keys(item)[0]) {
                    return data.rank = item
                  }
                })
              })
              this.setState({
                dataSource,
              })
            }
          })
      })
    }else {
      for (let i = 0; i < res.length; i++) {
        let data = res[i];
        if(isBreakpoint) {
        let newSourceData = await request('/api/stats/score',
          { params: {
              unitId: data.key,
              time:moment().format('YYYY/MM'),
            },
            headers:{ 'Authorization': stateToken},
          })
          if(newSourceData.status === 0) {
            this.setState({
              dataSource: this.state.dataSource.concat(newSourceData.value[0])
            })
          } else {
            return false;
          }
        } else {
          return false
        }
      }
      this.setState({
        bottomLoading: false
      }, () => {
        let params = this.state.dataSource.map(item => {
            return {
              unitId: item.unitId,
              score: item.score
            }
        })
        request('/api/stats/score/ranking', {method: 'post', data: params })
          .then(res => {
            let dataSource = _.cloneDeep(this.state.dataSource);
            if(res.status === 0) {
              res.value.map(item => {
                dataSource.map(data => {
                  if(data.unitId === Object.keys(item)[0]) {
                    return data.rank = item
                  }
                })
              })
              this.setState({
                dataSource,
              })
            }
          })
      })
    }
  }

  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: 'evaluate/fetch_getSelectList',
      callback: res => {
        dispatch({
          type: 'evaluate/fetch_getSelectList',
          payload: {
            id: res[0].key
          },
          callback: res => {
            this.getData(res);
          }
        })
      }
    })
  }

  componentWillUnmount() {
    this.setState({
      isBreakpoint: false
    })
  }

  render() {
    const { getFieldDecorator } = this.props.form;
    const { evaluate: { selectList }} = this.props;
    const { dataSource, bottomLoading } = this.state;
    return (
      <div className={style.div_warp}>
        <Form className={style.headerForm}>
          <Form.Item style={{marginTop: '-1px'}}>
            {getFieldDecorator('item', {
              rules: [
                {required: true, message: '请选择日期'}
              ]
            })(<MonthPicker placeholder={moment().format('YYYY-MM')}/>)}
          </Form.Item>
          <Button type='primary' style={{ margin: "0 1.5% 0 2.5%" }} onClick={this.handleOnSearch} loading={bottomLoading}>查询</Button>
          <Button type='primary' onClick={this.handleDownload}>导出</Button>
        </Form>
        {
          dataSource.map(item => {
            return (
              <Row className={style.evaluate_content_warp} key={item.unitName}>
                <Col className={style.evaluate_content_title}>{item.unitName}</Col>
                <Col className={style.evaluate_content_header}>
                  <span className={style.evaluate_content_header_left}>考评分值：<span>{item.score}</span></span>
                  排名：<span style={item.rank ? {fontSize: 26} : {fontSize: 20}}>{item.rank ? item.rank[item.unitId] : "统计中..."}</span>
                </Col>
                <Row type='flex' justify='space-between' className={style.evaluate_content_main}>
                  <Row className={style.evaluate_content_main_row}>
                    <Col className={style.main_row_title}>
                      案卷总数
                    </Col>
                    <Col className={style.main_row_content}>
                      {item.totalCount}
                    </Col>
                  </Row>
                  <Row className={style.evaluate_content_main_row}>
                    <Col className={style.main_row_title}>
                      已处置案卷总数
                    </Col>
                    <Col className={style.main_row_content}>
                      {item.finishCount}
                    </Col>
                  </Row>
                  <Row className={style.evaluate_content_main_row}>
                    <Col className={style.main_row_title}>
                      未处置案卷总数
                    </Col>
                    <Col className={style.main_row_content}>
                      {item.unFinishCount}
                    </Col>
                  </Row>
                  <Row className={style.evaluate_content_main_row}>
                    <Col className={style.main_row_title}>
                      超时案卷总数
                    </Col>
                    <Col className={style.main_row_content}>
                      {item.timeOutCount}
                    </Col>
                  </Row>
                  <Row className={style.evaluate_content_main_row}>
                    <Col className={style.main_row_title}>
                      处置率
                    </Col>
                    <Col className={style.main_row_content}>
                      {item.finishRate}
                    </Col>
                  </Row>
                  <Row className={style.evaluate_content_main_row}>
                    <Col className={style.main_row_title}>
                      按期处置率
                    </Col>
                    <Col className={style.main_row_content}>
                      {item.onTimeRate}
                    </Col>
                  </Row>
                  <Row className={style.evaluate_content_main_row}>
                    <Col className={style.main_row_title}>
                      返工率
                    </Col>
                    <Col className={style.main_row_content}>
                      {item.returnRate}
                    </Col>
                  </Row>
                  <Row className={style.evaluate_content_main_row}>
                    <Col className={style.main_row_title}>
                      出勤率
                    </Col>
                    <Col className={style.main_row_content}>
                      {item.onlineRate}
                    </Col>
                  </Row>
                </Row>
              </Row>
            )
          })
        }
      </div>
    )
  }
}

