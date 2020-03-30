import React, {PureComponent, Fragment} from 'react';
import {connect} from 'dva';
import {Typography, Form, Input, Badge, Tooltip} from 'antd';
import MenuTable from '../newMenuTable/List';
import {payload_0_10 as payload, returnSourceText, returnWorkTypeStyle, returnWorkTypeText} from "@/utils/utils";
import style from '../file.less';
import Spin from "antd/es/spin";
import Ellipsis from "@/components/Ellipsis";

const {TextArea} = Input;
const { Text, Paragraph } = Typography;

@Form.create()
@connect(({ history, loading }) => ({
  history,
  loading: loading.models.history
}))
class index extends PureComponent {
  state = {};


  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: 'history/fetch_getFileHistoryList',
      payload,
    })
  }


  render() {
    const { history: { history_list }, loading } = this.props;
    console.log(history)
    const columns = [
      {
        title: '标题',
        dataIndex: 'title',
        key: 'title',
        width:300,
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
        width:110,
        render: val => <Ellipsis lines={1}><Tooltip title={val}><span>{val}</span></Tooltip></Ellipsis>,
      },
      {
        title: '案卷小类',
        dataIndex: 'categoryName',
        key: 'categoryName',
        width:110,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '紧急程度',
        dataIndex: 'workType',
        key: 'workType',
        width:100,
        render: val => <Ellipsis lines={1}><span style={returnWorkTypeStyle(val)}>{returnWorkTypeText(val)}</span></Ellipsis>,
      },
      {
        title: '来源',
        dataIndex: 'source',
        key: 'source',
        width:110,
        render: val => <Ellipsis lines={1}>{returnSourceText(val)}</Ellipsis>,
      },
      // {
      //   title: '发生地点',
      //   dataIndex: 'address',
      //   key: 'address',
      //   width:130,
      //   render: val => <Ellipsis tooltip={val} lines={1}>{val}</Ellipsis>,
      // },
      {
        title: '办理人',
        dataIndex: 'creator',
        key: 'creator',
        width:110,
        render: val => <Ellipsis lines={1}><Tooltip title={val}><span>{val}</span></Tooltip></Ellipsis>,
      },
      {
        title: '办理时间',
        dataIndex: 'createTime',
        key: 'createTime',
        width:170,
        render: val => <Ellipsis lines={1}><Tooltip title={val}><span>{val}</span></Tooltip></Ellipsis>,
      },
    ];
    return (
      <div style={{backgroundColor:" #ffffff", minHeight: "91vh"}}>
        <Spin
          spinning={loading}
        >
          <MenuTable
            columns = {columns}
            dataSource = {history_list}
            type='history/fetch_getFileHistoryList'
            delButton='history/fetch_delFileHistory'
          />
        </Spin>
      </div>
    );
  }
}

export default index
