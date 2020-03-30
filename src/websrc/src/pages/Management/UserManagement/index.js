import React, { Component, Fragment } from 'react';
import { Button, Spin } from 'antd';
import { connect } from 'dva';
import MenuTable from '@/components/MenuTable/List'
import { payload_0_10 as payload } from '@/utils/utils';

@connect(({ userManagement, loading }) => ({
  userManagement,
  loading: loading.models.userManagement
}))
class index extends Component {
  state = {};

  componentDidMount = () => {
    const { dispatch } = this.props;
    dispatch({
      type: 'userManagement/fetch_getUserList',
      payload,
    });
    dispatch({
      type: 'userManagement/fetch_getPermissionTree'
    });
    dispatch({
      type: 'userManagement/fetch_getDeviceTree'
    });
  };

  setOnRef = (ref) => {
    this.onRef = ref
  };

  render() {
    const { userManagement: { user_list }, loading } = this.props;
    const columns = [
      {
        title: '角色名称',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '创建人',
        dataIndex: 'creator',
        key: 'creator',
      },
      {
        title: '创建时间',
        dataIndex: 'createTime',
        key: 'createTime',
      },
      {
        title: '操作',
        dataIndex: 'id',
        key: 'id',
        render: val => {
          const { dispatch } = this.props;
          return (
            <Button
              type='primary'
              onClick={(e) => {
                e.stopPropagation();
                dispatch({
                  type: 'userManagement/fetch_getUserItem',
                  payload: {
                    id: val
                  },
                  callback: res => {
                    if (res.status === 0) {
                      this.onRef.userManagementOnRowClick(res.value)
                    }
                  }
                });
              }}
            >
              修改
            </Button>
          );
        }
      }
    ];
    return (
      <Fragment style={{ backgroundColor: " #ffffff" }}>
        <Spin
          spinning={loading}
        >
          <MenuTable
            columns={columns}
            dataSource={user_list}
            type='userManagement/fetch_getUserList'
            delButton='userManagement/fetch_DelUserItem'
            addButton='userManagement/fetch_addUserList'
            addButtonText='新建角色'
            filter={true}
            onRef={this.setOnRef}
          />
        </Spin>
      </Fragment>
    );
  }
}
export default index;
