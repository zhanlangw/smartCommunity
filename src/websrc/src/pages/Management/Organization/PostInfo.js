import React, {PureComponent, Fragment} from 'react';
import {connect} from 'dva';
import {Row, Col, Card, Input, Form, Checkbox, Radio, Button, Modal, Spin, Select, message} from 'antd';
import TreeSelect from "../../../components/TreeSelect";

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
@connect(({organization, loading}) => ({
  organization,
  loading: loading.models.organization,
}))

export default class PostInfo extends PureComponent {

  constructor() {
    super();
    this.state = {
      showModal: false,
      thisKey: null,
      password:null,
      oldPassword:null,
      sPassword:null
    }
  };

  componentDidMount() {
    const { onRef } = this.props;
    onRef(this);
  }

  componentWillReceiveProps(nextProps){
    const thisKey = this.props.thisKey;
    const nextKey = nextProps.thisKey;
    console.log(thisKey)
    console.log(nextKey);
    if(thisKey!==nextKey){
      this.props.form.resetFields();
    }
  }

  hanldeSubmit = (thisKey) => {
    const {dispatch, form, postHanldeSubmit, editType} = this.props;
    form.validateFields((err, values) => {
      if (err) {
        return;
      }
      values.pid = values.pid && values.pid.value || undefined;
      if (editType) {
        postHanldeSubmit && postHanldeSubmit({
          ...values,
          id:thisKey
        })
      } else {
        postHanldeSubmit && postHanldeSubmit(values)
        // this.props.form.resetFields()
      }
    })
  };

  handleSubmitCallback = (statue) => {
    if (statue) {
      this.props.form.resetFields();
    }
  };


  render() {
    const {form:{getFieldDecorator}, loading, depart_info, postHanldeDel, thisKey, parentName, parentKey, editType} = this.props;
    const data = depart_info||{};
    const {
      age,
      avatarId,
      birth,
      dutyId,
      email,
      id,
      inContacts,
      inOrgTree,
      nameShort,
      location,
      contact,
      loginName,
      mobile,
      name,
      parent,
      pid,
      remarks,
      sex,
      num,
      telephone,
      tenantId,
      token,
      valid,
     } = data;

    return (
      <div>
        <Form>
        <Card
          bordered={false}
          bodyStyle={{padding: 0}}
        >
         <Spin  spinning={loading}>
         <div>
            <FormItem
                {...formItemLayout}
                label='名称'
              >
                {getFieldDecorator('name',{
                  initialValue:editType && name || undefined,
                  rules: [
                    { required: true, message: '请输入名称' },
                  ],
                  getValueFromEvent: (event) => {
                    return event.target.value.replace(' ','')
                  },
                })(
                  <Input placeholder='请输入名称'/>
                )}
            </FormItem>
            <FormItem
                {...formItemLayout}
                label='简称'
              >
                {getFieldDecorator('nameShort',{
                  initialValue:editType && nameShort || undefined,
                })(
                  <Input placeholder='请输入简称'/>
                )}
            </FormItem>
           <Form.Item {...formItemLayout} label='排序'>
             {getFieldDecorator('num', {
               getValueFromEvent: (event) => {
                 return event.target.value.replace(/\D/g,'')
               },
               initialValue: editType ? num : undefined
             })(
               <Input  placeholder='请输入排序编号'/>,
             )}
           </Form.Item>
            <FormItem
                {...formItemLayout}
                label='办公地点'
              >
                {getFieldDecorator('location',{
                  initialValue:editType && location || undefined,
                })(
                  <Input placeholder='请输入办公地点'/>
                )}
            </FormItem>
            <FormItem
                {...formItemLayout}
                label='联系电话'
              >
                {getFieldDecorator('contact',{
                  initialValue:editType && contact || undefined,
                })(
                  <Input type='number' placeholder='请输入联系电话'/>
                )}
            </FormItem>
            <FormItem
                {...formItemLayout}
                label='父亲节点'
              >
                {getFieldDecorator('pid',{
                  initialValue:editType && pid && {value:pid,label:parent} || (parentKey && {value:parentKey,label:parentName})  || undefined,
                })(
                  <TreeSelect labelInValue type={[2]} placeholder='请选择父亲节点'/>
                )}
            </FormItem>
            <FormItem
                {...formItemLayout}
                label='备注'
              >
                {getFieldDecorator('remarks',{
                  initialValue:editType && remarks || undefined,
                })(
                  <Input.TextArea style={{minHeight:80}} placeholder='请输入名称'/>
                )}
            </FormItem>
          </div>
         </Spin>
        </Card>

          <Row>
            <Col span={24} style={{textAlign: 'center'}}>

              <Button loading={loading} type="primary" onClick={() => this.hanldeSubmit(thisKey)}>保存</Button>
              {
                editType ?
                  <span><Button loading={loading} onClick={() => postHanldeDel && postHanldeDel(thisKey)}
                                style={{marginLeft: 30}}>
                  删除
                </Button></span> : null
              }
            </Col>
          </Row>
        </Form>
      </div>
    )
  }
}
