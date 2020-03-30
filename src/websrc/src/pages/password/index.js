import React,{ PureComponent, Fragment }  from 'react';
import style from './style.less'
import { connect } from 'dva';
import { Card, Spin, Button, Input, Form, Select, Radio, DatePicker, Upload, Icon, message } from 'antd';
import {dimValueGetter} from "echarts/src/component/marker/markerHelper";
import {getAuthority} from "@/utils/authority";
import request from "@/utils/request";
import { passwordRgx } from '@/utils/utils'

const FormItem = Form.Item;

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
@connect(() => ({}))


class index extends React.Component{
    state={};
    handleSubmit = e => {
        const { form: { validateFields }, dispatch} = this.props;
        e.preventDefault();
        this.props.form.validateFields((err, values) => {
            if(err) return false;
            if(values.password === values.newPasswrod){
                message.error('旧密码不能和新密码一样!');
            }else{
                if (values.newPassword === values.conPassword) {
                    console.log('Received values of form: ', values);
                    let payload = {
                        password: values.password,
                        newPasswrod: values.newPassword
                    } //传值
                    dispatch({
                        type: 'password/fetch_updPassword',  //对应 models/password.js 给后台传值
                        payload,
                    })
                } else {
                    message.error('您输入的新密码和确认密码不匹配!');
                }
            }
        });
    };

    render() {
        const { getFieldDecorator } = this.props.form;
        return(
            <Fragment >
            <Card>
            <Form onSubmit={this.handleSubmit} className="login-form">
            <Form.Item
                label='原密码'
                {...formProps}
                style={{width:500,margin:"auto",marginTop:46}}

            >
            {getFieldDecorator('password', {
                rules: [
                    {
                        required: true,
                        message: '请输入原密码'
                    },
                    {
                        whitespace: true,
                        message: passwordRgx.message
                    }
                ],
            })(
                <Input.Password
                    type="password"
                    placeholder="请输入原密码"
                />
            )}
            </Form.Item>

            <Form.Item
                label='新密码'
                {...formProps}
                style={{width:500,margin:"auto",marginTop:30}}

            >
                {getFieldDecorator('newPassword', {
                    rules: [
                        { required: true, message: '请输入新密码' }, 
                        {
                            pattern: passwordRgx.reg,
                            message: passwordRgx.message
                        },
                        {
                            whitespace: true,
                            message: passwordRgx.message
                        }
                    ]
                })(
                    <Input.Password
                        type="password"
                        placeholder="请输入新密码"/>
                )}
            </Form.Item>
            <Form.Item
                label='确认密码'
                {...formProps}
                style={{width:500,margin:"auto",marginTop:30}}
            >
                {getFieldDecorator('conPassword', {
                    rules: [
                        { required: true, message: '请确认输入新密码' },
                        {
                            pattern: passwordRgx.reg,
                            message: passwordRgx.message
                        },
                        {
                            whitespace: true,
                            message: passwordRgx.message
                        }
                    ],
                })(
                    <Input.Password
                        type="password"
                        placeholder="请再次输入新密码"/>
                )}
            </Form.Item>
            <Button type="primary" htmlType="submit" className="login-form-button"  style={{marginLeft:"49%",marginTop:10}} >确定</Button>
            </Form>
            </Card>
        </Fragment>
    )
    }

}



export default index;