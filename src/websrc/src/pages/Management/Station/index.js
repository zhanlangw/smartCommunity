import React, { PureComponent, Fragment } from 'react';
import { Button, Typography, Modal, Form, Input, Spin, Row, Col } from 'antd';
import { connect } from 'dva';
import MenuTable from '@/components/newMenuTable/List'
import {payload_0_10, validator, validatorDes, latitude, longitude} from '@/utils/utils'
import Ellipsis from "@/components/Ellipsis";

const { Text } = Typography;
const { TextArea } = Input;
const formItemLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 6 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 16 },
  },
};
const newFormItemLayout = {
  labelCol: {
    xs: { span: 0 },
    sm: { span: 0 },
  },
  wrapperCol: {
    xs: { span: 22 },
    sm: { span: 22 },
  },
};

@Form.create()
@connect(({ station, loading }) => ({
  station,
  loading: loading.models.station
}))
class index extends PureComponent {
  state={
    stationAddOrUpdModelVisible: false,
    stationAddOrUpd: true,
  };

  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: 'station/fetch_getStationList',
      payload:{
        ...payload_0_10
      }
    })
  }

  handelStationAddOrUpd = () => {
    const { stationAddOrUpd, stationUpdId } = this.state;
    const { form: { validateFields, resetFields }, dispatch } = this.props;
    validateFields((err, payload) => {
      if (!err) {
        if (stationAddOrUpd) {
          dispatch({
            type: 'station/fetch_addStationItem',
            payload,
            callback: res => {
              if (res.status === 0) {
                this.setState({
                  stationAddOrUpdModelVisible: false,
                  state: 1
                }, () => {
                  this.handleClickReset();
                  resetFields()
                })
              }
            }
          })
        } else {
          payload.id = stationUpdId
          dispatch({
            type: 'station/fetch_updStationItem',
            payload,
            callback: res => {
              if (res.status === 0) {
                this.setState({
                  stationAddOrUpdModelVisible: false,
                  state: 1
                }, () => {
                  resetFields()
                })
              }
            }
          })
        }
      }
    })
  };

  stationAddOrUpdModel = () => {
    const { stationAddOrUpdModelVisible, stationAddOrUpd } = this.state;
    const { form: { getFieldDecorator }, station: { station_item } } = this.props
    return (
      <Modal
        title={stationAddOrUpd ? '新建工作站' : '修改工作站'}
        visible={stationAddOrUpdModelVisible}
        centered
        onCancel={() => this.setState({ stationAddOrUpdModelVisible: false })}
        footer={[
          <Button key="submit" type="primary" onClick={this.handelStationAddOrUpd} style={{marginRight: 15}}>
            确定
          </Button>,
          <Button key="back" onClick={() => this.setState({ stationAddOrUpdModelVisible: false })}>
            取消
          </Button>,
        ]}
      >
        <Form>
          <Form.Item {...formItemLayout} label='工作站名称'>
            {getFieldDecorator('name', {
              rules: [
                {required: true, message: '请输入工作站名称'},
                {
                  validator:validator,
                }
              ],
              initialValue: stationAddOrUpd ? undefined : station_item && station_item.name || undefined
            })(
              <Input placeholder='请输入工作站名称'/>,
            )}
          </Form.Item>
          <Form.Item {...formItemLayout} label='地理位置'>
            {getFieldDecorator('address', {
              rules: [
                {required: true, message: '请输入地理位置'},
                {
                  validator:validatorDes,
                }
              ],
              initialValue: stationAddOrUpd ? undefined : station_item && station_item.address || undefined
            })(
              <Input placeholder='请输入地理位置'/>,
            )}
          </Form.Item>
          <Row type='flex' style={{marginLeft: 79 }}>
            <Text style={{ lineHeight: 2.5,  color: 'rgba(0, 0, 0, 0.85)'}}>坐标： </Text>
            <Col span={10}>
            <Form.Item {...newFormItemLayout} >
              {getFieldDecorator('longitude', {
                rules: [
                  {required: true, message: '请输入经度坐标'},
                  {
                    pattern:longitude, message: '非正确的经度坐标'
                  }
                ],
                initialValue: stationAddOrUpd ? undefined : station_item && station_item.longitude || undefined,
              })(
                <Input placeholder='请输入经度坐标' />,
              )}
            </Form.Item>
            </Col>
            <Col span={10}>
              <Form.Item {...newFormItemLayout} >
                {getFieldDecorator('latitude', {
                  rules: [
                    {required: true, message: '请输入纬度坐标'},
                    {
                      pattern:latitude, message: '非正确的纬度坐标'
                    }
                  ],
                  initialValue: stationAddOrUpd ? undefined : station_item && station_item.latitude || undefined,
                })(
                  <Input placeholder='请输入纬度坐标' />,
                )}
              </Form.Item>
            </Col>
          </Row>
          <Form.Item {...formItemLayout} label='描述'>
            {getFieldDecorator('desc', {
              initialValue: stationAddOrUpd ? undefined : station_item && station_item.desc || undefined
            })(
              <TextArea rows={4} style={{ resize: 'none' }} placeholder='请描述'/>
            )}
          </Form.Item>
        </Form>
      </Modal>
    )
  };

  handleStationListUpdButton = (id) => {
    const { dispatch } = this.props
    this.setState({
      stationUpdId: id,
      stationAddOrUpd:false,
      stationAddOrUpdModelVisible: true
    });
    dispatch({
      type:'station/fetch_getStationItem',
      payload: {
        id
      }
    })
    this.onRef.setState({
      start: 1
    })
  };

  setOnRef = (ref) => {
    this.onRef = ref
  };

  handleClickReset = () => {
    this.onRef.handleClickReset()
  };

  render() {
    const { station: { station_list }, loading } = this.props;
    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name',
        width:110,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '地理位置',
        dataIndex: 'address',
        key: 'address',
        width: 110,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '坐标',
        dataIndex: 'coordinates',
        key: 'coordinates',
        width:180,
        render: (text, record) => {
          return (<Text>{`东经${record.longitude}度,北纬${record.latitude}度`}</Text>)
        }
      },
      {
        title: '备注',
        dataIndex: 'desc',
        key: 'desc',
        width:140,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '操作',
        dataIndex: 'id',
        key: 'id',
        width:80,
        render: (text, record) => (
          <Button
            type='primary'
            onClick={() => this.handleStationListUpdButton(text)}
          >
            修改
          </Button>
        ),
      }
    ];
    return (
      <div style={{backgroundColor:" #ffffff", minHeight: "91vh"}}>
        <Button
          type='primary'
          style={{ margin: '12px 10px 8px' }}
          onClick={() => this.setState({ stationAddOrUpdModelVisible: true, stationAddOrUpd: true})}
        >
          新建工作站
        </Button>
        <Spin
          spinning={loading}
        >
        <MenuTable
          columns={columns}
          dataSource={station_list}
          type='station/fetch_getStationList'
          delButton='station/fetch_delStationItem'
          onRef={this.setOnRef}
          filter={true}
        />
        </Spin>
        {this.stationAddOrUpdModel()}
      </div>
    );
  }
}
export default index;
