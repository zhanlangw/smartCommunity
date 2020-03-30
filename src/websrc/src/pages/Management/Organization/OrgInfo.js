import React, { PureComponent, Fragment } from 'react';
import { connect } from 'dva';
import { Row, Col, Card, Input, Form, Checkbox, Button, Modal, Spin } from 'antd';
import TreeSelect from "../../../components/TreeSelect";

const confirm = Modal.confirm;
const FormItem = Form.Item;
const formItemLayout = {
  labelCol: {
    span: 7
  },
  wrapperCol: {
    span: 8
  },
};

@Form.create()
@connect(({ organization, loading }) => ({
  organization,
  loading: loading.models.organization,
}))

export default class OrgInfo extends PureComponent {

  componentWillReceiveProps(nextProps){
    const thisKey = this.props.thisKey;
    const nextKey = nextProps.thisKey;
    if(thisKey!==nextKey){
      this.props.form.resetFields();
    }
  }


//   hanldeDel = (parentId) => {
//     const { dispatch, match:{params} } = this.props;
//     const id = params.id;


//     confirm({
//       title: '提示',
//       content: '确认删除吗？',
//       okText: '确认',
//       //okType: 'danger',
//       cancelText: '取消',
//       onOk() {
//         dispatch({
//           type: 'organization/fetch_delOriganization_action',
//           payload: {
//             id:id,
//             //pid:pid,
//           },
//         });
//       },
//     });
//   }

  hanldeSubmit= (thisKey) => {
    const { dispatch, form,orgHanldeSubmit  } = this.props;
    form.validateFields((err, values) => {
      if(err){
        return;
      }
      orgHanldeSubmit({
        ...values,
        id:thisKey
      });
    })
  }

  render() {
    const { form:{getFieldDecorator}, loading,info,thisKey, orgHanldeDel } = this.props;
    const data = info||{};
    const {
      contact,
      id,
      inContacts,
      inOrgtree,
      location,
      name,
      nameShort,
      parent,
      pid,
      portalUrl,
      remarks,
      tenantId,
      type,
      valid,
      websiteUrl,
     } = data;
    return (
      <div>
        <Card
          bordered={false}
          bodyStyle={{padding: 0}}
        >
         <Spin spinning={loading}>
          <div>
            <FormItem
                {...formItemLayout}
                label='名称'
              >
                {getFieldDecorator('name',{
                  initialValue:name || undefined,
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
                  rules: [
                    { required: true, message: '请输入简称' },
                  ],
                  initialValue:nameShort || undefined,
                })(
                  <Input placeholder='请输入简称'/>
                )}
            </FormItem>
            <FormItem
                {...formItemLayout}
                label='办公地点'
              >
                {getFieldDecorator('location',{
                  initialValue:location || undefined,
                })(
                  <Input placeholder='请输入办公地点'/>
                )}
            </FormItem>
            <FormItem
                {...formItemLayout}
                label='联系电话'
              >
                {getFieldDecorator('contact',{
                  initialValue:contact || undefined,
                })(
                  <Input type='number' placeholder='请输入联系电话'/>
                )}
            </FormItem>
            <FormItem
                {...formItemLayout}
                label='备注'
              >
                {getFieldDecorator('remarks',{
                  initialValue:remarks || undefined,
                })(
                  <Input.TextArea style={{minHeight:80}} placeholder='请输入名称'/>
                )}
            </FormItem>
          </div>
         </Spin>
        </Card>

        <Row>
          <Col span={24} style={{ textAlign: 'center' }}>
            <Button loading={loading} type="primary" onClick={()=>this.hanldeSubmit(thisKey)}>保存</Button>
            <Button onClick={()=>orgHanldeDel&&orgHanldeDel(thisKey)} style={{marginLeft: 32}}>
              删除
            </Button>
          </Col>
        </Row>
      </div>
    )
  }
}
