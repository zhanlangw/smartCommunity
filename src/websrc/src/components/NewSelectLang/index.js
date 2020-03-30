import React, { PureComponent, Fragment } from 'react';
import { Dropdown, Menu, Icon } from 'antd';
import Link from 'umi/link';
import { connect } from 'dva';
import style from './index.less';

const { SubMenu } = Menu;
const menu = (
  <Menu>

    <Menu.Item style={{padding: '10px 20px'}}>
     <Link to="/management/config">基础配置</Link>
    </Menu.Item>
    <Menu.Item style={{padding: '10px 20px'}}>
      <Link to="/management/organization">组织机构</Link>
    </Menu.Item>
    <Menu.Item style={{padding: '10px 20px'}}>
      <Link to="/management/userManagement">角色管理</Link>
    </Menu.Item>
    <Menu.Item style={{padding: '10px 20px'}}>
      <Link to="/management/station">工作站管理</Link>
    </Menu.Item>
    <Menu.Item style={{padding: '10px 20px'}}>
      <Link to="/management/device">摄像机管理</Link>
    </Menu.Item>
    <Menu.Item style={{padding: '10px 20px'}}>
      <Link to="/management/blacklist">黑名单管理</Link>
    </Menu.Item>
    <Menu.Item style={{padding: '10px 20px'}}>
       <Link to="/management/category">案卷类别</Link>
    </Menu.Item>
    <Menu.Item style={{padding: '10px 20px'}}>
      <Link to="/management/process">流程配置</Link>
    </Menu.Item>
  </Menu>
);

@connect(() => ({
  //
}))
class NewSelectLang extends PureComponent {
  state={}

  render() {
   return (
      <div style={{ height: 64, display: 'inline-block' }}>
        <Dropdown overlay={menu} className={style.Dropdown}>
          <span className={style.Dropdown_span} href="#">
            <Icon type="unordered-list" /> 高级管理
          </span>
        </Dropdown>
      </div>
    );
  }
}
export default NewSelectLang;
