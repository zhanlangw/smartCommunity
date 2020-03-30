import React, { Fragment } from 'react';
import { connect } from 'dva';
import moment from 'moment';
import { Checkbox, Alert, Icon, Form, Input } from 'antd';
import Login from '@/components/Login';
import loginLogo from '../../assets/logo.png';
import loginTitle from '@/assets/loginTitle.png';
import loginLeft from '@/assets/loginLeft.png';
import styles from './Login.less';
import {getUrl, token} from "@/utils/utils";
import request from "@/utils/request";
import loginTab from '@/assets/loginTab.png';
import code from '@/assets/code.png';
import scan from '@/assets/scan.png';
import QRCode from 'qrcode.react';

const Item = Form.Item;
const { Tab, UserName, Password, Mobile, Captcha, Submit } = Login;

@Form.create()
@connect(({ login, loading }) => ({
  login,
  submitting: loading.models.login,
}))
export default class LoginPage extends React.Component {
  state = {
    see: true,
    // time: moment().format('x')
    switchover: true
  };

  // refreshCaptcha = () => {
  //   const time = moment().format('x');
  //   this.setState({
  //     time
  //   })
  // }

  componentDidMount() {
    return request('/api/basis/item', {headers: token})
      .then(res => {
        if (res.status === 0) {
          this.setState({
            file: res.value && res.value.imagePath,
            name: res.value && res.value.name,
          })
        } else {
          message.error('请求失败,请稍后再试')
        }
      })
  }


  handleSubmit = (err, values) => {
    const { form: { validateFields }, login: { autoLogin } } = this.props;
    if (!err) {
      validateFields((err, params) => {
        if (!err) {
          this.props.dispatch({
            type: 'login/login',
            payload: {
              ...values,
              ...params,
              type: 1,
            },
          });
        }
      });
    }
  };

  // renderMessage = content => {
  //   return <Alert style={{ marginBottom: 24 }} message={content} type="error" showIcon />
  // }

  changeAutoLogin = (e) => {
    const autoLogin = e.target.checked;
    this.props.dispatch({
      type: 'login/changeAutoLogin',
      payload: {
        autoLogin: autoLogin,
      },
    });
  };

  handleSwitchover = () => {
    this.setState({
      switchover: !this.state.switchover
    })
  }

  // changeSee = () => {

  //   const { see } = this.state;
  //   this.setState({
  //     see: !see,
  //   });
  //   console.log(see);
  // }

  render() {
    const { login, submitting, login: { autoLogin }, form: { getFieldDecorator } } = this.props;
    const { see, time, file, name, switchover } = this.state;
    const userInfo = localStorage.getItem('user') && JSON.parse(localStorage.getItem('user'));
    const username = userInfo && userInfo.username;
    const password = userInfo && userInfo.password;
    return (
      <Fragment>
        <div style={{display: 'inline-block', textAlign:'center', marginTop: '5%', marginLeft: '9%', position: 'absolute'}}>
          <img src={loginTitle} alt="" style={{ paddingBottom: '15%'}}/>
          <img src={loginLeft} alt="" style={{width:883, height:418, display: 'block'}}/>
        </div>
        <div className={styles.main}>
          {
            switchover ?
              (
                <Login
                  onSubmit={this.handleSubmit}
                >
                  <div style={{ paddingTop: 20 }}>
                    {/* {
                    login.login_state.state !== 0 &&
                    !login.submitting &&
                    this.renderMessage(login.login_state.message)
                    } */}
                    <div className={styles.top} style={name && name.length < 7 ? {marginLeft: 16 }: {}}>
                      <img src={file && getUrl(file) || loginLogo} className={styles.loginLogo} />
                      <span className={styles.loginBox}>{name && name || "综合治理一体化平台"}</span>
                    </div>
                    <UserName
                      defaultValue={username}
                      name="username"
                      placeholder="请输入用户名"
                      rules={[{ required: true, message: '账号不能为空' }]}
                    />
                    <Item>
                      {getFieldDecorator('password', {
                        rules: [
                          {
                            required: true,
                            message: '请输入正确密码',

                          },
                          {
                            validator: this.password,
                          },
                        ],
                        initialValue: password,
                      })(
                        <Input.Password
                          size="large"
                          placeholder="请输入密码"
                          prefix={<Icon type="lock"
                                        style={{ color: 'rgba(0,0,0,.25)' }}/>}
                        />
                      )}
                    </Item>
                  </div>
                  <div
                    style={{ overflow: 'hidden' }}
                  >
                  <span
                    style={{ float: 'left' }}>
                    <Checkbox
                      style={{ color: '#1890ff' }}
                      defaultChecked={autoLogin}
                      onChange={this.changeAutoLogin}
                    >
                      记住密码
                    </Checkbox>
                  </span>
                  </div>
                  <Submit loading={submitting} style={{ background: '#12c5cc', color: '#fff', fontSize: 18}}>登录</Submit>
                </Login>
              )
              :
              (
                <div className={styles.codeTab}>
                  <div className={styles.codeTab_title}>扫描下载</div>
                  <QRCode value={`${window.location.protocol}//${window.location.host}/uploads/download/download.html`} size={124} style={{ margin: '0 auto' }}/>
                  <div className={styles.codeTab_content}>
                    <img src={scan} alt=""/>
                    <span>打开手机<br/> 扫一扫下载</span>
                  </div>
                </div>
              )
          }
          <div
            style={{position: 'absolute', bottom: 5, right: 5, cursor: 'pointer'}}
            className={ switchover ? styles.loginTab : styles.login_tab_code}
            onClick={this.handleSwitchover}
          >
            <img src={ switchover ? code : loginTab} alt=""/>
          </div>
        </div>
        <div className={styles.footer}>
          <text className={styles.footerText}> Copyright © 2019 成都百特云科技有限公司 All Rights Reserved.</text>
        </div>
      </Fragment>
    );
  }
}
