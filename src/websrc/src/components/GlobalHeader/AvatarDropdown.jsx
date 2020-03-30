import { Avatar, Icon, Menu, Spin } from 'antd';
import { FormattedMessage } from 'umi-plugin-react/locale';
import React from 'react';
import { connect } from 'dva';
import router from 'umi/router';
import admin from '../../assets/admin.png';
import HeaderDropdown from '../HeaderDropdown';
import { getAuthority } from '@/utils/authority'
import styles from './index.less';
import request from '@/utils/request';
import {getUrl, token} from "@/utils/utils";

@connect(({ personal }) => ({
  personal,
}))
class AvatarDropdown extends React.Component {
  state = {
    avatarPath: null,
    avatarLoading: false,
  };

  componentDidMount() {
    this.setState({
      avatarLoading: true
    })
    const { dispatch } = this.props;
    const id = getAuthority().id;
    dispatch({
      type:'global/fetch_getUserItem',
      payload: {
        id
      }
    })
    // return request('/api/user/item', {params: {id}, headers: token})
    //   .then(res => {
    //     if(res.status === 0) {
    //       this.setState({
    //         avatarPath: res.value.imagePath,
    //
    //         avatarLoading: false
    //       })
    //     }
    //   })
    // dispatch({
    //   type:'personal/fetch_getPersonalUserItem',
    //   payload:{
    //     id
    //   },
    // })
  }

  onMenuClick = event => {
    const { key } = event;
    if (key === 'logout') {
      const { dispatch } = this.props;
      if (dispatch) {
        dispatch({
          type: 'login/logout',
        });
      }
      return;
    } else if(key==='password') {
      router.push('/password')
    }else {
      router.push(`/personal`);
    }

  };

   //路径请求
   onPasswordClick = event=>{

   }

  loadingAvatar = () => {
    const {global: {userItem}} = this.props;
    if ( userItem ) {
      return <Avatar size="small" className={styles.avatar} src={userItem.imagePath} alt="avatar" />
    } else {
      return <Avatar size="small" className={styles.avatar} src={admin} alt="avatar" />
    }
  }

  render() {
    const { currentUser = {}, menu, personal, global: {userItem}, loading } = this.props;
    const { avatarPath, avatarLoading } = this.state;
    // if (!menu) {
    //   return (
    //     <span className={`${styles.action} ${styles.account}`}>
    //       <Avatar src={admin} onMenuClick={this.onMenuClick} />
    //     </span>
    //   );
    // }
    const menuHeaderDropdown = (
      <Menu className={styles.menu} selectedKeys={[]} onClick={this.onMenuClick}>
        <Menu.Item key="center">
          <Icon type="user" />
          <FormattedMessage id="menu.account.center" defaultMessage="personal" />
        </Menu.Item>
        {/*个人中心*/}
        {/*<Menu.Item key="settings">*/}
        {/*  <Icon type="setting" />*/}
        {/*  <FormattedMessage id="menu.account.settings" defaultMessage="account settings" />*/}
        {/*</Menu.Item>*/}

        <Menu.Item key="password">
          <Icon type="logout" />
          <FormattedMessage id="menu.account.password" defaultMessage="logout" />
        </Menu.Item>
        {/*//新增修改密码*/}

        <Menu.Divider />
        <Menu.Item key="logout">
          <Icon type="logout" />
          <FormattedMessage id="menu.account.logout" defaultMessage="logout" />
        </Menu.Item>
         {/*退出登录*/}
      </Menu>
    );
    return (
      <HeaderDropdown overlay={menuHeaderDropdown}>
        <span className={`${styles.action} ${styles.account}`}>
          {
            // loading ?(
              <Avatar size="small" className={styles.avatar} src={userItem && getUrl(userItem.imagePath) || admin} alt="avatar" />
            // ): (
            //   <Spin
            //     size="small"
            //     spinning={loading}
            //     style={{
            //     marginLeft: 8,
            //     marginRight: 8,
            //     }}
            //   />
            // )
          }
          <span className={styles.name}>{userItem && userItem.name}</span>
        </span>
      </HeaderDropdown>
    )
  }
}

export default connect(({ global, loading }) => ({
  global,
  loading: loading.models.global
}))(AvatarDropdown);
