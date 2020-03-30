import React, { PureComponent, Fragment } from 'react';
import { Button, Typography, Modal, Form, Input, Select, Row, Col, Spin } from 'antd';
import { connect } from 'dva';
import MenuTable from '@/components/newMenuTable/List'
import {payload_0_10, validator, validatorDes, reg} from '@/utils/utils'
import TreeSelect from '@/components/TreeSelect'
import Ellipsis from "@/components/Ellipsis";

const { Text } = Typography;
const { TextArea } = Input;
const { Option } = Select;
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
    xs: { span: 24 },
    sm: { span: 7 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 15 },
  },
};

const TcpFormItemLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 8 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 14 },
  },
};

@Form.create()
  @connect(({ device, loading }) => ({
    device,
    loading: loading.models.device
}))
class index extends PureComponent {
  state = {
    deviceAddOrUpdModelVisible: false,
    deviceAddOrUpd: true,
  };

  componentDidMount () {
    const { dispatch } = this.props;
    dispatch({
      type: 'device/fetch_getDeviceList',
      payload: {
        ...payload_0_10
      }
    });
    dispatch({
      type: 'global/fetch_get_tree',
    });
  }

  handelDeviceAddOrUpd = () => {
    const { deviceAddOrUpd, deviceUpdId, unitId} = this.state;
    const { form: { validateFields, resetFields }, dispatch } = this.props;
    validateFields((err, payload) => {
      if (!err) {
        if (deviceAddOrUpd) {
          dispatch({
            type: 'device/fetch_addDeviceItem',
            payload,
            callback: res => {
              if (res.status === 0) {
                this.setState({
                  deviceAddOrUpdModelVisible: false,
                }, () => {
                  this.onRef.setState({
                    start: 1
                  })
                  resetFields()
                })
              }
            }
          })
        } else {
          payload.id = deviceUpdId;
          payload.unitId = unitId.id;
          dispatch({
            type: 'device/fetch_updDeviceItem',
            payload,
            callback: res => {
              if (res.status === 0) {
                this.setState({
                  deviceAddOrUpdModelVisible: false,
                }, () => {
                  this.onRef.setState({
                    start: 1
                  })
                  resetFields()
                })
              }
            }
          })
        }
      }
    })
  };

  deviceAddOrUpdModel = () => {
    const { deviceAddOrUpdModelVisible, deviceAddOrUpd } = this.state;
    const { form: { getFieldDecorator }, device: { device_item } } = this.props
    return (
      <Modal
        title={deviceAddOrUpd ? '新建摄像机' : '修改摄像机'}
        visible={deviceAddOrUpdModelVisible}
        width={1150}
        onCancel={() => this.setState({ deviceAddOrUpdModelVisible: false })}
        footer={[
          <Button key="submit" type="primary" onClick={this.handelDeviceAddOrUpd} style={{marginRight: 15}}>
            确定
          </Button>,
          <Button key="back" onClick={() => this.setState({ deviceAddOrUpdModelVisible: false })}>
            取消
          </Button>,
        ]}
      >
        <Form>
          <Row>
            <Col span={12}>
              <Form.Item {...formItemLayout} label='摄像机名称'>
                {getFieldDecorator('name', {
                  rules: [
                    {required: true, message: '请输入摄像机名称'},
                    {
                      validator:validator,
                    }
                  ],
                  initialValue: deviceAddOrUpd ? undefined : device_item && device_item.name || undefined
                })(
                  <Input placeholder='请输入摄像机名称' />,
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
                  initialValue: deviceAddOrUpd ? undefined : device_item && device_item.address || undefined
                })(
                  <Input placeholder='请输入地理位置'/>,
                )}
              </Form.Item>
              <Form.Item {...formItemLayout} label='关联街区'>
                {getFieldDecorator('unitId', {
                  rules: [{ required: true, message: '请选择关联街区' }],
                  initialValue: deviceAddOrUpd ? undefined : device_item && device_item.unit && device_item.unit.name || undefined,
                  //getValueFromEvent (event) {
                  //  return event.target.value.replace(/\D/g, '')
                  //}
                })(
                  <TreeSelect placeholder='请选择关联街区' onChange={(e) => this.setState({ unitId: {id: e}})}/>,
                )}
              </Form.Item>
              <Form.Item {...formItemLayout} label='摄像机类型'>
                {getFieldDecorator('type', {
                  rules: [{ required: true, message: '请选择摄像机类型' }],
                  initialValue: deviceAddOrUpd ? undefined : device_item && device_item.type || undefined,
                })(
                  <Select placeholder='请选择摄像机类型'>
                    <Option value="BALL">球机</Option>
                    <Option value="GUN">枪机</Option>
                  </Select>
                )}
              </Form.Item>
              <Form.Item {...formItemLayout} label='IP地址' style={{margin: 0}}>
                {getFieldDecorator('ip', {
                  rules: [
                    { required: true, message: '请输入IP地址' },
                    { pattern: reg, message:'请参考模板输入正确IP地址' }
                  ],
                  initialValue: deviceAddOrUpd ? undefined : device_item && device_item.ip || undefined
                })(
                  <Input placeholder='请输入IP地址'/>
                )}
                <Text type="secondary">参考模板: 192.168.1.100</Text>
              </Form.Item>
              <Form.Item {...formItemLayout} label='RTSP地址'>
                {getFieldDecorator('rtspAddress', {
                  initialValue: deviceAddOrUpd ? undefined : device_item && device_item.rtspAddress || undefined
                })(
                  <Input placeholder='请输入RTSP地址'/>
                )}
              </Form.Item>
              <Form.Item {...formItemLayout} label='用户名'>
                {getFieldDecorator('username', {
                  rules: [
                    {required: true, message: '请输入用户名'},
                    {
                      validator:validator,
                    }
                  ],
                  initialValue: deviceAddOrUpd ? undefined : device_item && device_item.username || undefined
                })(
                  <Input placeholder='请输入用户名'/>
                )}
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item {...formItemLayout} label='摄像机编号'>
                {getFieldDecorator('num', {
                  rules: [{ required: true, message: '请输入摄像机编号' }, {max: 20, message:'长度不能超过20'}],
                  initialValue: deviceAddOrUpd ? undefined : device_item && device_item.num || undefined,
                  getValueFromEvent (event) {
                    return event.target.value.replace(/\D/g, '')
                  }
                })(
                  <Input placeholder='请输入摄像机编号'/>,
                )}
              </Form.Item>
              <Row span={24} style={{left: 72}}>
                <Col span={10}>
                  <Form.Item {...newFormItemLayout} label='经度'>
                    {getFieldDecorator('longitude', {
                      rules: [{ required: true, message: '请输入经度坐标' }, { pattern: /^-?((0|1?[0-7]?[0-9]?)(([.][0-9]{1,6})?)|180(([.][0]{1,6})?))$/, message: '请输入正确的经度坐标' }],
                      initialValue: deviceAddOrUpd ? undefined : device_item && device_item.longitude || undefined,
                    })(
                      <Input placeholder='请输入经度坐标'/>,
                    )}
                  </Form.Item>
                </Col>
                <Col span={10} style={{left: '-7px'}}>
                  <Form.Item {...newFormItemLayout} label='纬度'>
                    {getFieldDecorator('latitude', {
                      rules: [{ required: true, message: '请输入纬度坐标' }, { pattern: /^-?((0|[1-8]?[0-9]?)(([.][0-9]{1,6})?)|90(([.][0]{1,6})?))$/, message: '请输入正确的纬度坐标' }],
                      initialValue: deviceAddOrUpd ? undefined : device_item && device_item.latitude || undefined,
                    })(
                      <Input placeholder='请输入纬度坐标'/>,
                    )}
                  </Form.Item>
                </Col>
              </Row>
              <Form.Item {...formItemLayout} label='厂商'>
                {getFieldDecorator('vendor', {
                  rules: [{ required: true, message: '请选择厂商' }],
                  initialValue: deviceAddOrUpd ? undefined : device_item && device_item.vendor || undefined
                })(
                 <Select placeholder='请选择厂商'>
                   <Option value={'1'}>大华</Option>
                   <Option value={'2'}>海康</Option>
                   <Option value={'3'}>宇视</Option>
                   <Option value={'4'}>天地伟业</Option>
                 </Select>
                )}
              </Form.Item>
              <Form.Item {...formItemLayout} label='摄像机功能'>
                {getFieldDecorator('features', {
                  rules: [{ required: true, message: '请输入摄像机功能' }],
                  initialValue: deviceAddOrUpd ? undefined : device_item && device_item.features || undefined,
                })(
                  <Select placeholder='请输入摄像机功能'>
                    <Option value="INTELLIGENT">智能</Option>
                    <Option value="ORDINARY">普通</Option>
                  </Select>
                )}
              </Form.Item>
              <Row span={24} style={{left: 72}}>
                  <Col span={10}>
                  <Form.Item {...TcpFormItemLayout} label='TCP端口'>
                      {getFieldDecorator('tcpPort', {
                        rules: [{ required: true, message: '请输入TCP端口号' }],
                        initialValue: deviceAddOrUpd ? undefined : device_item && device_item.tcpPort || undefined,
                        getValueFromEvent (event) {
                          return event.target.value.replace(/\D/g, '')
                        }
                      })(
                        <Input placeholder='请输入TCP端口号'/>,
                      )}
                    </Form.Item>
                  </Col>
                  <Col span={10} style={{left: '-7px'}}>
                    <Form.Item {...newFormItemLayout} label='状态'>
                    {getFieldDecorator('status', {
                      rules: [{ required: true, message: '请输入端口号' }],
                      initialValue: deviceAddOrUpd ? undefined : device_item && device_item.status || undefined,
                      })(
                        <Select placeholder='请选择状态'>
                          <Option value="ONLINE">在线</Option>
                          <Option value="LESS">离线</Option>
                        </Select>
                      )}
                    </Form.Item>
                  </Col>
              </Row>
              <Form.Item {...formItemLayout} label='RTSP端口号'>
                {getFieldDecorator('rtspPort', {
                  rules: [
                    {required: true, message: '请输入RTSP端口号'},
                    {
                      validator:validatorDes,
                    }
                  ],
                  initialValue: deviceAddOrUpd ? undefined : device_item && device_item.rtspPort || undefined
                })(
                  <Input placeholder='请输入RTSP端口号'/>
                )}
              </Form.Item>
              {/*<Row span={24} style={{left: 72}}>*/}
              {/*  <Col span={10}>*/}
              {/*    <Form.Item {...newFormItemLayout} label='主键ID'>*/}
              {/*      {getFieldDecorator('key', {*/}
              {/*        rules: [{ required: true, message: '请输入主键ID' }, {validator:validator}],*/}
              {/*        initialValue: deviceAddOrUpd ? undefined : device_item && device_item.key || undefined,*/}
              {/*      })(*/}
              {/*        <Input placeholder='请输入主键ID'/>,*/}
              {/*      )}*/}
              {/*    </Form.Item>*/}
              {/*  </Col>*/}
              {/*  <Col span={10} style={{left: '-7px'}}>*/}
              {/*    <Form.Item {...newFormItemLayout} label='URL'>*/}
              {/*      {getFieldDecorator('url', {*/}
              {/*        rules: [{ required: true, message: '请输入url' }, {validator:validator}],*/}
              {/*        initialValue: deviceAddOrUpd ? undefined : device_item && device_item.url || undefined,*/}
              {/*      })(*/}
              {/*        <Input placeholder='请输入url'/>,*/}
              {/*      )}*/}
              {/*    </Form.Item>*/}
              {/*  </Col>*/}
              {/*</Row>*/}
              <Form.Item {...formItemLayout} label='密码'>
                {getFieldDecorator('password', {
                  rules: [{ required: true, message: '请输入密码' }],
                  initialValue: deviceAddOrUpd ? undefined : device_item && device_item.password || undefined
                })(
                  <Input type='password' placeholder='请输入密码'/>,
                )}
              </Form.Item>
            </Col>
          </Row>
        </Form>
      </Modal>
    )
  };

  handleDeviceAppButton = () => {
    this.setState({ deviceAddOrUpdModelVisible: true, deviceAddOrUpd: true });
  };

  handleDeviceUpdButton = (id) => {
    const { dispatch } = this.props;
    this.setState({
      deviceUpdId: id,
      deviceAddOrUpd: false,
      deviceAddOrUpdModelVisible: true
    });
    dispatch({
      type: 'device/fetch_getDeviceItem',
      payload: {
        id
      },
      callback: res => {
        if (res.status === 0) {
          this.setState({
            unitId: res.value.unit
          })
        }
      }
    })
  };

  setOnRef = (ref) => {
    this.onRef = ref
  };

  handleClickReset = () => {
    this.onRef.handleClickReset()
  };

  render () {
    const { device: { device_list }, loading } = this.props;
    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name',
        width:110,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '编号',
        dataIndex: 'num',
        key: 'num',
        width:80,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '地理位置',
        dataIndex: 'address',
        key: 'address',
        width:120,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '坐标',
        dataIndex: 'coordinates',
        key: 'coordinates',
        width:170,
        render: (val, record) => {
          return (<Ellipsis lines={1}>东经{record.longitude}度,北纬{record.latitude}度</Ellipsis>)
        }
      },
      {
        title: '关联街区',
        dataIndex: 'unitName',
        key: 'unitName',
        width:110,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '摄像机功能',
        dataIndex: 'features',
        key: 'features',
        width: 80,
        render: (val, record) => {
          return (<Ellipsis lines={1}><Text>{val === 'INTELLIGENT' ? '智能' : '普通'}</Text></Ellipsis>)
        }
      },
      {
        title: '摄像机类型',
        dataIndex: 'type',
        key: 'type',
        width: 80,
        render: (val, record) => {
          return (<Ellipsis lines={1}><Text>{val === 'BALL' ? '球机' : '枪机'}</Text></Ellipsis>)
        }
      },
      {
        title: 'IP地址',
        dataIndex: 'ip',
        key: 'ip',
        width:110,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '操作',
        dataIndex: 'id',
        key: 'id',
        width: 80,
        render: (text, record) => (
          <Button
            type='primary'
            onClick={() => this.handleDeviceUpdButton(text)}
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
          onClick={this.handleDeviceAppButton}
        >
          新建摄像机
        </Button>
        <Spin
          spinning={loading}
        >
          <MenuTable
            columns={columns}
            dataSource={device_list}
            type='device/fetch_getDeviceList'
            delButton='device/fetch_delDeviceItem'
            onRef={this.setOnRef}
            filter={true}
          />
        </Spin>
        { this.state.deviceAddOrUpdModelVisible && this.deviceAddOrUpdModel()}
      </div>
    );
  }
}
export default index;
