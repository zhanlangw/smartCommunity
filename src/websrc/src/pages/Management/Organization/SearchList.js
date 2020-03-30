import React, {PureComponent, Fragment} from 'react';
import {connect} from 'dva';
import {Link, Route, Switch, routerRedux} from 'dva/router';
import {Row, Col, Card, Input, Button, Table, Modal} from 'antd';
import moment from 'moment';
import {returnUserTypeText} from "@/utils/utils";

const columns = [{
  title: '姓名',
  dataIndex: 'name',
}, {
  title: '登录账号',
  dataIndex: 'username',
}, {
  title: '用户类型',
  dataIndex: 'userType',
  render: val => returnUserTypeText(val)
}, {
  title: '所在岗位',
  dataIndex: 'unit',
}];

@connect(({mechanism, loading}) => ({
  mechanism,
  loading: loading.models.mechanism,
}))
export default class SearchList extends PureComponent {

  handleChange = (pagination, filters, sorter) => {
    const {dispatch, match: {params}, mechanism} = this.props;
    const page = pagination.current;
    const pageSize = mechanism.pageSize;
    dispatch({
      type: "mechanism/fetch_search",
      payload: {
        search: params.key,
        page: page,
        pageSize: pageSize,
      }
    })
  };

  changePage = (pagination) => {
    const {changePage} = this.props;
    changePage && changePage(pagination)
  };
  
  ClickRow = (record) => {
    const {onClickRow} = this.props;
    onClickRow && onClickRow(record);
  };

  render() {
    const {search_list, page, dispatch, loading, paginationProps} = this.props;
    const data = search_list || [];
    return (
      <Table
        bordered
        loading={loading}
        pagination={paginationProps}
        rowKey={record => record.id}
        columns={columns}
        dataSource={data}
        onChange={this.changePage}
        onRow={(record) => { // 点击行
          return {
            onClick: () => {
              this.ClickRow(record);
            }
          }
        }}
      />
    );
  }
}
