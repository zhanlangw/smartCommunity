import React, {PureComponent, Fragment} from 'react';
import {connect} from 'dva';
import {Card, Row, Col, Button, Icon, Modal, Form, Input, Upload} from 'antd';
import {beforeUpload, getUrl, phoneRgx, token, validator, validatorDes, longitude,latitude} from "@/utils/utils";//引用经纬度的正则
import admin from "@/assets/logo.png";
import style from "@/pages/personal/index.less";
import moment from "moment";

const FormItem = Form.Item;
const {TextArea} = Input;
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
@connect(({ config }) => ({
  config
}))
class Index extends PureComponent {
  state = {
    file: undefined
  };

  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: 'config/fetch_getConfigItem',
      callback: res => {
        if (res.status === 0) {
          this.setState({
            file: res.value && res.value.imagePath
          })
        }
      }
    })
  }

  fileChange = (e) => {
    const file = e && e.file;
    this.setState({
      file: file.response && file.response.value || {}
    });
  };

  handleSubmit = e => {
    const { form: {validateFields}, dispatch } = this.props;
    const { file } = this.state;
    validateFields((err, payload) => {
      if (!err) {
        payload.imagePath = file || undefined;
        dispatch({
          type: 'config/fetch_updConfigItem',
          payload,
          callback: res => {
            if (res.status === 0) {
              window.location.reload(true)
            }
          }
        })
      }
    })
  };

  render() {
    const { form: { getFieldDecorator }, loading, config: { config_item } } = this.props;
    const { file } = this.state;
    console.log(config_item);
    
    return (
      <Fragment>
        <Card bordered={false} style={{ height: '92vh', paddingTop: 59 }}>
          <div style={{ width: 430, margin: '0 auto', marginBottom: 30 }}>
            <div style={{ margin: 18, textAlign: 'center' }}>
              <div style={{ border: '1px dashed #7d7d7d', width: "100%", height: 134 }}>
                <img
                  src={ file && getUrl(file) || admin }
                  alt=""
                  style={{ height: 100, width: 136,  marginTop: 17 }}
                />
              </div>
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
                    上传Logo
                  </Button>
                </Upload>
              </div>
            </div>
          </div>
          <Form className={style.personalFormWarp} style={{ textAlign: 'center' }}>
            <FormItem
              label='id'
              style={{display: 'none'}}
              {...formProps}
            >
              {getFieldDecorator('id', {
                initialValue: config_item && config_item.id
              })(
                <Input />
              )}
            </FormItem>
            <FormItem
              label='客户名称'
              {...formProps}
            >
              {getFieldDecorator('name', {
                rules: [
                  { required: true, message: '请输入名称' },
                  {validator: validator}
                ],
                initialValue: config_item && config_item.name
              })(
                <Input />
              )}
            </FormItem>
            <FormItem
              label='附件上传大小限制'
              {...formProps}
            >
              {getFieldDecorator('fileMaxSize', {
                getValueFromEvent: (event) => {
                  return event.target.value.replace(/\D/g,'')//只能输入数字
                },
                initialValue: config_item && config_item.fileMaxSize
              })(
                <Input addonAfter={'MB'}/>
              )}
            </FormItem>
            <FormItem
              {...formProps}
              label='超时阀值'
            >
              {getFieldDecorator('timeOut', {
                getValueFromEvent: (event) => {
                  return event.target.value.replace(/\D/g,'')//只能输入数字
                },
                initialValue: config_item && config_item.timeOut
              })(
                <Input  addonAfter={'小时'}/>
              )}
            </FormItem>

            <FormItem
            {...formProps}
            label='地图中心点经度'
            >
             {getFieldDecorator('longitude',{
               rules: [
                  {required: true, message: '请输入经度' },
                  {pattern: latitude, message:'请输入正确经度'}//经度的正则校验
               ],
               initialValue: config_item && config_item.longitude
             })(
                <Input />
             )}
             </FormItem>

              <FormItem
               {...formProps}
                label='地图中心点纬度'
               >
               {getFieldDecorator('latitude', {
                rules:[
                  {required:true,message:'请输入纬度'},
                  {pattern: latitude, message:'请输入正确纬度'}//纬度的正则校验
                ],
                initialValue: config_item && config_item.latitude
                })(
                  <Input  />
                )}
              </FormItem>
            <Button type='primary' onClick={this.handleSubmit}>保存</Button>
          </Form>
        </Card>
      </Fragment>
    );
  }
}

export default Index
