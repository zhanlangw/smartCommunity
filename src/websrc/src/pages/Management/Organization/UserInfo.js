import React, {PureComponent, Fragment} from 'react';
import {connect} from 'dva';
import {Row, Col, Card, Input, Form, Checkbox, Radio, Button, Modal, Spin, Select, message} from 'antd';
import TreeSelect from "../../../components/TreeSelect";
import {passwordRgx} from "@/utils/utils";

const Option = Select.Option;
const FormItem = Form.Item;
const formItemLayout = {
  labelCol: {
    span: 7
  },
  wrapperCol: {
    span: 12
  },
};
const formItemLayouts = {
  labelCol: {
    span: 6
  },
  wrapperCol: {
    span: 18
  },
};

@Form.create()
@connect(({organization, userManagement, loading}) => ({
  organization,
  userManagement,
  loading: loading.models.organization,
}))

export default class PeopleInfo extends PureComponent {

  constructor() {
    super();
    this.state = {
      showModal: false,
      thisKey: null,
      password:null,
      oldPassword:null,
      sPassword:null,
      children: null,
      rePasswordVisible: false
    }
  };

  componentDidMount() {
    const { dispatch, onRef } = this.props;
    onRef(this);
    dispatch({
      type: 'userManagement/fetch_getUserList',
      payload: {
        start: 0,
        count: 10000
      }
    });
  }

  componentWillReceiveProps(nextProps){
    const thisKey = this.props.thisKey;
    const nextKey = nextProps.thisKey;
    if(thisKey!==nextKey){
      this.props.form.resetFields();
    }
  }

  handleSubmit = (thisKey, username) => {
    const {dispatch, form, userHanldeSubmit, editType} = this.props;
    form.validateFields(['name','unit_id', 'num', 'roleIds','userType','address', 'telephone','desc','username'],(err, values) => {
      values.pid = values.pid && values.pid.value || undefined;
      delete values.allName;
      if (err) {
        console.log(err);
        return false
      }
      if (editType) {
        const params = {
          ...values,
          unitId: values.unit_id.value,
          id: thisKey,
          username,
        };
        userHanldeSubmit && userHanldeSubmit(params)
      } else {
        const params = {
          ...values,
          unitId: values.unit_id.value,
        };
        userHanldeSubmit && userHanldeSubmit(params);
      }
    })
  };

  handleSubmitCallback = (statue) => {
    if (statue) {
      this.props.form.resetFields();
    }
  };

  handleSelectChange = value => {
    // console.log(value);
  };

  handleRePasswordModal = () => {
    this.setState({
      rePasswordVisible: false,
    })
  };

  handleRePasswordOk = id => {
    const { form, dispatch } = this.props;
    form.validateFields(['password','id'],(err, values) => {
      if(!err){
        const payload = {
           id: values.id,
           password: values.password
        };
        dispatch({
          type: 'organization/fetch_updateUserPassword_action',
          payload,
          callback: () => {
            this.setState({
              rePasswordVisible: false
            })
          }
        })
      } else {
        console.log(err)
      }
    })
  };


  render() {
    const {
      form:{getFieldDecorator},
      organization:{depart_info:dutyList},
      loading,
      depart_info,
      userHanldeDel,
      thisKey,
      parentName,
      parentKey,
      editType,
      userManagement: { user_list }
    } = this.props;
    const data = depart_info||{};
    const { rePasswordVisible } = this.state;
    let data_roleId = [];
    if (data && data.role) {
      data.role.map(item => {
        data_roleId.push(item.id)
      })
    }
    return (
      <div>
        <Form>
        <Card
          bordered={false}
          bodyStyle={{padding: 0}}
        >
         <Spin spinning={loading}>
          <div>
            <FormItem
                {...formItemLayout}
                label='姓名'
              >
                {getFieldDecorator('name',{
                  initialValue: editType ? data.name : undefined,
                  rules: [
                    { required: true, message: '请输入姓名' },
                  ],
                  getValueFromEvent: (event) => {
                    return event.target.value.replace(' ','')
                  },
                })(
                  <Input placeholder='请输入姓名'/>
                )}
            </FormItem>
             <FormItem
                {...formItemLayout}
                label='角色'
              >
                {getFieldDecorator('roleIds',{
                  initialValue: editType && data_roleId && data_roleId || undefined,
                  rules: [
                    { required: true, message: '请选择角色' },
                  ],
                })(
                  <Select
                    mode="multiple"
                    style={{ width: '100%' }}
                    placeholder="请选择角色"
                    onChange={this.handleSelectChange}
                  >
                   {
                     user_list && user_list.value.map( item => {
                      return <Option key={item.id} value={item.id}>{item.name}</Option>
                     })
                   }
                  </Select>
                )}
            </FormItem>
            <Form.Item {...formItemLayout} label='排序'>
              {getFieldDecorator('num', {
                rules: [
                  { required: true, message: '请输入排序' },
                ],
                getValueFromEvent: (event) => {
                  return event.target.value.replace(/\D/g,'')
                },
                initialValue: editType ? data.num : undefined
              })(
                <Input  placeholder='请输入排序编号'/>,
              )}
            </Form.Item>
            <FormItem
              {...formItemLayout}
              label='用户类型'
            >
              {getFieldDecorator('userType', {
                initialValue: editType && data.userType || undefined,
                rules: [
                  { required: true, message: '请选择用户类型' },
                ],
              })(
                <Select placeholder='请选择用户类型'>
                  <Option value='ADMIN'>管理员</Option>
                  <Option value='USER'>职能部门人员</Option>
                  <Option value='WORKER'>工作人员</Option>
                  <Option value='SUPERVISION_USER'>督查人员</Option>
                </Select>
              )}
            </FormItem>
            <FormItem
              {...formItemLayout}
              label='办公地点'
            >
                {getFieldDecorator('address', {
                  initialValue: editType && data.address || undefined,
              })(
                <Input placeholder='请输入办公地点' />
              )}
            </FormItem>
            <FormItem
              {...formItemLayout}
              label='联系电话'
            >
              {getFieldDecorator('telephone', {
                getValueFromEvent: (event) => {
                  return event.target.value.replace(/\D/g,'')
                },
                initialValue: editType ? data.telephone : undefined,
              })(
                <Input placeholder='请输入联系电话' />
              )}
            </FormItem>
            {/* {
              editType?'':
                      <FormItem
                        {...formItemLayout}
                        label='登录密码'
                      >
                        {getFieldDecorator('password',{
                          rules: [
                            { required: true, message: '请输入登录密码' }
                            ,{
                              pattern:/((?=.*\d)(?=.*\D)|(?=.*[a-zA-Z])(?=.*[^a-zA-Z]))(?!^.*[\u4E00-\u9FA5].*$)^\S{6,18}$/,message: '请输入6-18位的至少包含数字,字母,符号两种类型的密码',
                            }
                          ],
                          initialValue:editType && loginName || undefined,
                        })(
                           <Input placeholder='请输入系统账号'/>
                        )}
                    </FormItem>
            } */}
            {/* {
              editType &&  <FormItem
                              {...formItemLayout}
                              label='机构层级'
                            >
                                <span>{allName}</span>
                          </FormItem>
            } */}
            <FormItem
              {...formItemLayout}
              label='父亲节点'
            >
              {getFieldDecorator('unit_id',{
                rules: [
                  { required: true, message: '请选择父亲节点' },
                ],
                initialValue: editType && data.unit && { value: data.unit.id, label: data.unit.name } || (parentKey && { value: parentKey, label: parentName }) || undefined,
              })(
                <TreeSelect labelInValue type={[2,3]} placeholder='请选择'/>
              )}
            </FormItem>
                {
                  editType ? <FormItem
                    {...formItemLayout}
                    label='系统账号'
                  >
                    <span>{data.username}</span>
                  </FormItem> :
                    <FormItem
                      {...formItemLayout}
                      label='系统账号'
                    >
                      {getFieldDecorator('username', {
                        rules: [
                          { required: true, message: '请输入系统账号' },
                        ],
                        getValueFromEvent: (event) => {
                          return event.target.value.replace(' ','')
                        },
                        initialValue: editType && data.username || undefined,
                      })(
                        <Input placeholder='请输入系统账号' />
                      )}
                    </FormItem>
                }
            <FormItem
                {...formItemLayout}
                label='备注'
              >
                  {getFieldDecorator('desc',{
                    initialValue: editType && data.desc || undefined,
                })(
                  <Input.TextArea style={{minHeight:80}} placeholder='请输入备注'/>
                )}
            </FormItem>
          </div>
         </Spin>
        </Card>

          <Row>
            <Col span={24} style={{textAlign: 'center'}}>

              <Button loading={loading} type="primary" onClick={() => this.handleSubmit(thisKey, data.username)}>保存</Button>
              {
                editType ?
                  <span>
                    <Button
                      loading={loading}
                      onClick={() => userHanldeDel && userHanldeDel(thisKey)}
                      style={{marginLeft: 30, marginRight: 30}}>
                      删除
                    </Button>
                    <Button
                      onClick={() => this.setState({ rePasswordVisible: true })}
                    >
                      重置密码
                    </Button>
                    {rePasswordVisible && (
                      <Modal
                        title='重置密码'
                        visible={rePasswordVisible}
                        onCancel={this.handleRePasswordModal}
                        footer={[
                          <Button
                            key="submit"
                            type="primary"
                            onClick={this.handleRePasswordOk}
                            style={{marginRight: 15}}
                          >
                            确定
                          </Button>,
                          <Button key="back" onClick={this.handleRePasswordModal}>
                            取消
                          </Button>,
                        ]}
                      >
                        <Form>
                          <FormItem
                            style={{display: 'none'}}
                            {...formItemLayout}
                            label='id'
                          >
                            {getFieldDecorator('id', {
                              initialValue: editType && data.id || undefined,
                            })(
                              <Input disabled/>
                            )}
                          </FormItem>
                          <FormItem
                            {...formItemLayout}
                            label='输入新密码'
                          >
                            {getFieldDecorator('password', {
                              rules: [
                                {required: true, message: '请输入新的密码'},
                                {pattern: passwordRgx.reg, message: passwordRgx.message},
                              ],
                            })(
                              <Input placeholder='请输入新的密码'/>
                            )}
                          </FormItem>
                        </Form>
                      </Modal>)
                    }
                  </span> : null
              }
            </Col>
          </Row>
        </Form>
      </div>
    )
  }
}
