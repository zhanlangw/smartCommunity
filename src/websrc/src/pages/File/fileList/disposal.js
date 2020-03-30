import React, {PureComponent, Fragment} from 'react';
import {connect} from 'dva';
import MenuTable from '../newMenuTable/List';
import {
  payload_0_10 as payload, returnSourceText,
  returnStatusStyle,
  returnStatusText,
  returnWorkTypeStyle,
  returnWorkTypeText
} from "@/utils/utils";
import {Spin, Form, Badge, Typography, Tooltip} from 'antd';
import Ellipsis from "@/components/Ellipsis";

const { Text, Paragraph } = Typography;

@Form.create()
@connect(({ global, loading }) => ({
  global,
  loading: loading.effects['global/fetch_getFileDisposalList'],
}))
class disposal extends PureComponent {
  state = {};

  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: 'global/fetch_getFileDisposalList',
      payload,
    })
  }


  render() {
    const { global: { disposal_list }, loading } = this.props;
    const columns = [
      {
        title: '标题',
        dataIndex: 'title',
        key: 'title',
        width: 300,
        render: (val, record) => {
          return (
            <Ellipsis lines={1}>
              <span>
                <Badge status={record.workType === 'URGENT' ? 'error' : 'default'} />
              </span>
              <Tooltip title={val}>
                <span>{val}</span>
              </Tooltip>
            </Ellipsis>
          )
        },
      },
      {
        title: '案卷编号',
        dataIndex: 'num',
        key: 'num',
        width: 150,
        render: val => <Ellipsis lines={1}><Tooltip title={val}><span>{val}</span></Tooltip></Ellipsis>,
      },
      {
        title: '案卷状态',
        dataIndex: 'workStatus',
        key: 'workStatus',
        width: 60,
        render: val => <Ellipsis lines={1}><span style={returnStatusStyle(val)}>{returnStatusText(val)}</span></Ellipsis>,
      },
      {
        title: '案卷小类',
        dataIndex: 'categoryName',
        key: 'categoryName',
        width: 110,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '紧急程度',
        dataIndex: 'workType',
        key: 'workType',
        width: 60,
        render: val => <Ellipsis lines={1}><span style={returnWorkTypeStyle(val)}>{returnWorkTypeText(val)}</span></Ellipsis>,
      },
      {
        title: '来源',
        dataIndex: 'source',
        key: 'source',
        width: 100,
        render: val => <Ellipsis lines={1}>{returnSourceText(val)}</Ellipsis>,
      },
      // {
      //   title: '发生地点',
      //   dataIndex: 'address',
      //   key: 'address',
      //   width: 130,
      //   render: val => <Ellipsis tooltip={val} lines={1}>{val}</Ellipsis>,
      // },
      {
        title: '创建人',
        dataIndex: 'creator',
        key: 'creator',
        width: 90,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '创建时间',
        dataIndex: 'createTime',
        key: 'createTime',
        width: 150,
        render: val => <Ellipsis lines={1}><Tooltip title={val}><span>{val}</span></Tooltip></Ellipsis>,
      },
      {
        title: '结案时间',
        dataIndex: 'finishTime',
        key: 'finishTime',
        width: 150,
        render: val => <Ellipsis lines={1}><Tooltip title={val}><span>{val}</span></Tooltip></Ellipsis>,
      },
    ];
    return (
      <Fragment>
        <Spin
          spinning={loading}
        >
          <MenuTable
            columns = {columns}
            dataSource = {disposal_list}
            type='global/fetch_getFileDisposalList'
            delButton='global/fetch_delHasFiles'
          />
        </Spin>
      </Fragment>
    );
  }
}

export default disposal
