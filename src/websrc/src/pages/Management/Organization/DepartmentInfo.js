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
    span: 12
  },
};

@Form.create()
@connect(({ organization, loading }) => ({
  organization,
  loading: loading.models.organization,
}))

export default class DepartmentInfo extends PureComponent {

  componentWillReceiveProps(nextProps){
    const thisKey = this.props.thisKey;
    const nextKey = nextProps.thisKey;
    if(thisKey!==nextKey){
      this.props.form.resetFields();
    }
  }

  componentDidMount() {
    const {onRef} = this.props;
    onRef(this);
  }

  hanldeDel = (parentId) => {
    const { dispatch, match:{params} } = this.props;
    const id = params.id;
    confirm({
      title: '提示',
      content: '确认删除吗？',
      okText: '确认',
      //okType: 'danger',
      cancelText: '取消',
      onOk() {
        dispatch({
          type: 'organization/fetch_delOriganization_action',
          payload: {
            id:id,
            //pid:pid,
          },
        });
      },
    });
  };

  hanldeSubmit= (thisKey) => {
    const { dispatch, form,departHanldeSubmit,editType, addStatus } = this.props;
    form.validateFields(async(err, values) => {
      if(err){
        return;
      }
      values.pid = values.pid && values.pid.value || undefined;
      if(editType){
        const params = {
          ...values,
          id: thisKey
        };
       departHanldeSubmit&&departHanldeSubmit(params);
      }else{
        departHanldeSubmit && departHanldeSubmit({...values});
      }
    })
  };

  handleSubmitCallback = (statue) => {
    if (statue) {
      this.props.form.resetFields();
    }
  };

  render() {
    const { form:{getFieldDecorator}, loading,depart_info,thisKey,departHanldeDel,parentName,parentKey,editType, } = this.props;
    const data = depart_info || {};
    const {
     contact,
     id,
     inContacts,
     inOrgtree,
     location,
     name,
     num,
     nameShort,
     parent,
     pid,
     portalUrl,
     remarks,
     tenantId,
     pName,
     abbre,
     address,
     desc,
     telephone,
     type,
     valid,
     websiteUrl,
    } = data ;
    return (
      <div style={{width: 800}}>
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
                  initialValue:editType ? name : '',
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
                {getFieldDecorator('abbre',{
                  rules: [
                    { required: true, message: '请输入简称' },
                  ],
                  initialValue: editType ? abbre : undefined,
                })(
                  <Input placeholder='请输入简称'/>
                )}
            </FormItem>
            <Form.Item {...formItemLayout} label='排序'>
              {getFieldDecorator('num', {
                getValueFromEvent: (event) => {
                  return event.target.value.replace(/\D/g,'')
                },
                rules: [
                  { required: true, message: '请输入排序编号' }
                ],
                initialValue: editType ? num : undefined
              })(
                <Input  placeholder='请输入排序编号'/>,
              )}
            </Form.Item>
            <FormItem
                {...formItemLayout}
                label='办公地点'
              >
                {getFieldDecorator('address',{
                  initialValue: editType ? address : undefined,
                })(
                  <Input placeholder='请输入办公地点'/>
                )}
            </FormItem>
            <FormItem
                {...formItemLayout}
                label='联系电话'
              >
                {getFieldDecorator('telephone',{
                  initialValue: editType ? telephone : undefined,
                })(
                  <Input placeholder='请输入联系电话'/>
                )}
            </FormItem>
            <FormItem
              {...formItemLayout}
              label='父亲节点'
            >
              {getFieldDecorator('pid',{
                rules: [
                  { required: true, message: '请选择父亲节点' },
                ],
                initialValue:editType && data && { value: data.pid, label: data.pName } || (parentKey && { value: parentKey, label: parentName }) || undefined,
              })(
                <TreeSelect labelInValue type={[1,2]} placeholder='请选择父亲节点' disabled={ id === 'ccc3ffcfc7174d66ab04d92236454ece' && true }/>
              )}
            </FormItem>
            <FormItem
                {...formItemLayout}
                label='备注'
              >
                {getFieldDecorator('desc',{
                  initialValue: editType ? desc : undefined,
                })(
                  <Input.TextArea style={{minHeight:80}} placeholder='请输入名称'/>
                )}
            </FormItem>
          </div>
         </Spin>
        </Card>

        <Row>
          <Col span={24} style={{ textAlign: 'center'}}>

                  <Button loading={loading} type="primary" onClick={()=>this.hanldeSubmit(thisKey, id)}>保存</Button>
            {
              editType &&
                  <Button loading={loading} onClick={()=>departHanldeDel&&departHanldeDel(thisKey)} style={{marginLeft: 30}}>
                    删除
                  </Button>
            }
          </Col>
        </Row>
      </div>
    )
  }
}
