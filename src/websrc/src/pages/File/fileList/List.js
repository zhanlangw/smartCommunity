/* eslint-disable react/no-unused-state */
import React, { PureComponent, Fragment } from 'react';
import { Tabs, Card, Badge, Typography, Form } from 'antd';
import { connect } from 'dva';
import style from '../file.less';

import Handle from './handle';
import Inspect from "./inspect";
import Delayed from "./delayed";
import Bounce from "./bounce";
import Disposal from "./disposal";
import {payload_0_10 as payload} from "@/utils/utils";

const { TabPane } = Tabs;
const { Text } = Typography;

@Form.create()
@connect(({
  global,
}) => ({
  global,
}))
class List extends PureComponent {
  state = {
    tabsKey: '1',

  };

  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: 'global/fetch_getFileBounceList',
      payload,
    });
    dispatch({
      type: 'global/fetch_getFileDelayedList',
      payload,
    });
    dispatch({
      type: 'global/fetch_getFileDisposalList',
      payload,
    });
    dispatch({
      type: 'global/fetch_getFileHandleList',
      payload,
    });
    dispatch({
      type: 'global/fetch_getFileInspectList',
      payload,
    });
    this.setState({
      tabsKey: `${this.props.location.query.tabsKey}` || '1'
    })
  }

  handleTabsChange = (value) => {
    const { dispatch } = this.props;
    this.setState({
      tabsKey: value
    });
    dispatch({
      type: 'global/SET_DRAWER_VISIBLE',
      payload: false
    });
    dispatch({
      type: 'global/SET_TYPE_API',
      payload: value
    });
    this.props.form.resetFields();
  };

  render() {
    const {
      global: { handle_list, bounce_list, delayed_list, disposal_list, inspect_list }
    } = this.props;
    const { tabsKey } = this.state;
    return (
      <div style={{backgroundColor:" #ffffff", minHeight: "91vh"}}>
        <Card bordered="false" className={style.fileWarpCard} style={{backgroundColor:" #ffffff"}}>
          {
            tabsKey !== 'undefined' ? (
              <Tabs
                activeKey={tabsKey}
                tabBarStyle={{ width: '100%', padding: 0, margin: 0 }}
                onChange={this.handleTabsChange}
              >
                <TabPane tab={<Badge count={ handle_list && handle_list.totalCount } offset={[5,-5]}>待处置</Badge>} key="global/fetch_getFileHandleList" >
                  <Handle />
                </TabPane>
                <TabPane tab={<Badge count={ inspect_list && inspect_list.totalCount } offset={[5,-5]}>待核查</Badge>} key="global/fetch_getFileInspectList" >
                  <Inspect />
                </TabPane>
                <TabPane tab={<Badge count={ delayed_list && delayed_list.totalCount } offset={[5,-5]}>申请延时</Badge>} key="global/fetch_getFileDelayedList" >
                  <Delayed />
                </TabPane>
                <TabPane tab={<Badge count={ bounce_list && bounce_list.totalCount } offset={[5,-5]}>退件</Badge>} key="global/fetch_getFileBounceList" >
                  <Bounce />
                </TabPane>
                <TabPane tab='已处置' key="global/fetch_getFileDisposalList" >
                  <Disposal />
                </TabPane>
              </Tabs>
            ) : (
              <Tabs
                defaultActiveKey={'global/fetch_getFileHandleList'}
                tabBarStyle={{ width: '100%', padding: 0, margin: 0 }}
                onChange={this.handleTabsChange}
              >
                <TabPane tab={<Badge count={ handle_list && handle_list.totalCount } offset={[5,-5]}>待处置</Badge>} key="global/fetch_getFileHandleList" >
                  <Handle />
                </TabPane>
                <TabPane tab={<Badge count={ inspect_list && inspect_list.totalCount } offset={[5,-5]}>待核查</Badge>} key="global/fetch_getFileInspectList" >
                  <Inspect />
                </TabPane>
                <TabPane tab={<Badge count={ delayed_list && delayed_list.totalCount } offset={[5,-5]}>申请延时</Badge>} key="global/fetch_getFileDelayedList" >
                  <Delayed />
                </TabPane>
                <TabPane tab={<Badge count={ bounce_list && bounce_list.totalCount } offset={[5,-5]}>退件</Badge>} key="global/fetch_getFileBounceList" >
                  <Bounce />
                </TabPane>
                <TabPane tab='已处置' key="global/fetch_getFileDisposalList" >
                  <Disposal />
                </TabPane>
              </Tabs>
            )
          }
        </Card>
      </div>
    );
  }
}
export default List;
