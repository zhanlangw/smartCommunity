import React, { PureComponent, Fragment } from 'react';
import { Card, Spin, Button, Input, Form, Select, Radio, DatePicker, Upload, Icon, message } from 'antd';
import { connect } from 'dva';
import {getUrl, token, phoneRgx, validatorDes, beforeUpload} from '@/utils/utils';
import { getAuthority } from '@/utils/authority';
import admin from "@/assets/admin.png";
import moment from 'moment';
import TreeSelect from '@/components/TreeSelect';
import style from './index.less';
import request from "@/utils/request";

const Option = Select.Option;
const FormItem = Form.Item;
const RadioGroup = Radio.Group;
const formProps = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 6 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 15 },
  },
};

@Form.create()
@connect(({ personal, config, loading }) => ({
  personal,
  config,
  loading: loading.models.personal
}))
class index extends PureComponent {
  state={};

  componentDidMount() {
    const { dispatch } = this.props;
    const id = getAuthority().id;
    dispatch({
      type:'personal/fetch_getPersonalUserItem',
      payload:{
        id
      },
      callback: res => {
        if (res.status === 0) {
          this.setState({
            newFile: res.value.imagePath
          })
        }
      }
    })
  }

  fileChange = (e) => {
    const file = e && e.file;
    console.log(e);
    if (file.response && file.response.status === 0){
      this.setState({
        file: file.response && file.response.value || {}
      });
    }
  };


   // beforeUpload =(file)=> {
   //   request('/api/basis/item', {headers: token}) .then(res => {
   //     if (res.status === 0) {
   //       const isLt = file.size / 1024 / 1024 < 1;
   //       if (!isLt) {
   //         message.error('上传文件超过指定大小');
   //       }
   //       return isLt;
   //     }
   //   });
   // };


  handleSubmit = (e) => {
    const { form: { validateFields }, dispatch} = this.props;
    const { file, newFile } = this.state;
    validateFields((err, values) => {
      if(!err){
       if(file || newFile){
         const token = getAuthority().id;
         values.birthday = values.birthday && moment(values.birthday).format('YYYY/MM/DD') || undefined;
         values.imagePath = file || newFile || undefined;
         values.id = token;
         dispatch({
           type: 'personal/fetch_updPersonalUserItem',
           payload: {
             ...values
           },
           callback: res => {
               // window.location.reload(true)
             const id = getAuthority().id;
             dispatch({
               type:'global/fetch_getUserItem',
               payload: {
                 id
               }
             })
           }
         })
       }else{
         message.error('请修改头像')
       }
      }else {
        console.log(err)
      }
    })
  };

  render() {
    const { personal: { personal_user_item }, form: { getFieldDecorator }, loading } = this.props;
    const { file } = this.state;
    // const { data, img } = info;
    //const token = getAuthority() && getAuthority().value && getAuthority().value.token || '';
    return (
      <Fragment>
        <Card bordered={false} style={{ height: '92vh' }}>
          {/* <Spin spinning={loading}> */}
            <div style={{ width: 400, margin: '0 auto' }}>
              <div style={{ margin: 12, textAlign: 'center' }}>
              <img
                src={ file ? getUrl(file) : personal_user_item && personal_user_item.imagePath ? getUrl(personal_user_item.imagePath) : admin }
                alt=""
                style={{ height: 100, width: 100, borderRadius: '50%' }}
              />
                <div style={{ marginTop: 12 }}>
                  <Upload
                    onChange={this.fileChange}
                    name="file"
                    accept=".jpg,.jpeg,.png"
                    action={getUrl("/api/file/upload?fileType=5")}
                    showUploadList={false}
                    beforeUpload={beforeUpload}
                    headers={{
                      "Authorization": token,
                    }}
                  >
                    <Button type="primary">
                      更换头像
                    </Button>
                  </Upload>
                </div>
              </div>
            </div>
            <Form className={style.personalFormWarp}>
              <FormItem
                label='姓名'
                {...formProps}
              >
                {getFieldDecorator('name', {
                  rules:[
                    {required: true, message: '请输入名称'},
                    {min:2, message:"姓名长度需要在2和20之间"},
                    {max:20, message:"姓名长度需要在2和20之间"}
                  ],
                  initialValue: personal_user_item && personal_user_item.name,
                  getValueFromEvent: (event) => {
                    return event.target.value.replace(' ','')
                  },
                })(
                  <Input/>
                )}
              </FormItem>

              <FormItem
                label='性别'
                {...formProps}
              >
               {getFieldDecorator('gender', {
                 rules:[{required: true, message: '请选择性别'}],
                 initialValue: personal_user_item && personal_user_item.gender
                })(
                  <Select placeholder='请选择性别'>
                    <Option value='男'>男</Option>
                    <Option value='女'>女</Option>
                  </Select>
                )}
              </FormItem>
              <FormItem
                {...formProps}
                label='生日'
              >
                {getFieldDecorator('birthday', {
                  initialValue: personal_user_item && moment(personal_user_item.startTime) || undefined
                })(
                  <DatePicker placeholder='请选择' />
                )}
              </FormItem>
              <FormItem
                label='年龄'
                {...formProps}
              >
                {getFieldDecorator('age', {
                  rules:[
                   {max:3 , message:'长度限定为3'}
                  ],
                  initialValue: personal_user_item && personal_user_item.age,
                  getValueFromEvent (event) {
                    return event.target.value.replace(/\D/g, '')
                  }
                })(
                  <Input type='text' />
                )}
              </FormItem>
              <FormItem
                label='手机号'
                {...formProps}
              >
                {getFieldDecorator('telephone', {
                  rules:[
                    {pattern:phoneRgx , message:'请输入正确的手机号'}
                  ],
                  initialValue: personal_user_item && personal_user_item.telephone,
                  getValueFromEvent (event) {
                    return event.target.value.replace(/\D/g, '')
                  }
                })(
                  <Input type='text' />
                )}
              </FormItem>
              <FormItem
                label='邮箱'
                {...formProps}
              >
                {getFieldDecorator('email', {
                  rules: [{
                    type: 'email', message:'请输入正确的邮箱号'
                  }],
                  initialValue: personal_user_item && personal_user_item.email,
                })(
                  <Input />
                )}
              </FormItem>
              <FormItem
                label='地址'
                {...formProps}
              >
                {getFieldDecorator('address', {
                  rules:[
                    {
                      validator:validatorDes,
                    }
                  ],
                  initialValue: personal_user_item && personal_user_item.address,
                })(
                  <Input />
                )}
              </FormItem>
              <FormItem
                label='所在部门'
                {...formProps}
              >
                {getFieldDecorator('unit', {
                  initialValue: personal_user_item && personal_user_item.unit.name,
                })(
                  <TreeSelect disabled />
                )}
              </FormItem>
            </Form>
            <div style={{ marginTop: 24, textAlign: 'center' }}>
              <Button onClick={this.handleSubmit} type="primary">保存</Button>
              {/* <Button onClick={this.close} style={{ marginLeft: 24 }}>关闭</Button> */}
            </div>
          {/* </Spin> */}
        </Card>
      </Fragment>
    );
  }
}
export default index;
