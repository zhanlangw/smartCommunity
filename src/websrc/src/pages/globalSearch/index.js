import React, {useState, useEffect} from 'react';
import {connect} from 'dva';
import {Badge, Button, Card, Table, Pagination, Spin} from 'antd';
import Ellipsis from "@/components/Ellipsis";
import style from './index.less';
import {
  getUrl, pageSize_10 as count,
  returnSourceText,
  returnStatusStyle,
  returnStatusText,
  returnWorkTypeStyle,
  returnWorkTypeText, token
} from "@/utils/utils";
import GetFileItem from "@/components/getFileItem";



const columns = [
  {
    title: '标题',
    dataIndex: 'title',
    key: 'title',
    width: 230,
    render: (val, record) => {
      return (
        <Ellipsis tooltip={val} lines={1}>
          <span><Badge status={record.workType === 'URGENT' ? 'error' : 'default'}/>{val}</span>
        </Ellipsis>
      )
    },
  },
  {
    title: '案卷编号',
    dataIndex: 'num',
    key: 'num',
    width: 110,
    render: val => <Ellipsis tooltip={val} lines={1}>{val}</Ellipsis>,

  },
  {
    title: '案卷状态',
    dataIndex: 'status',
    key: 'status',
    width:60,
    render: val => <Ellipsis tooltip={val} lines={1}><span style={returnStatusStyle(val)}>{returnStatusText(val)}</span></Ellipsis>,
  },
  {
    title: '案卷小类',
    dataIndex: 'categoryName',
    key: 'categoryName',
    width:110,
    render: val => <Ellipsis tooltip={val} lines={1}>{val}</Ellipsis>,
  },
  {
    title: '紧急程度',
    dataIndex: 'workType',
    key: 'workType',
    width: 60,
    render: val => <Ellipsis tooltip={val} lines={1}><span style={returnWorkTypeStyle(val)}>{returnWorkTypeText(val)}</span></Ellipsis>,
  },
  {
    title: '来源',
    dataIndex: 'source',
    key: 'source',
    width: 100,
    render: val => <Ellipsis tooltip={val} lines={1}>{returnSourceText(val)}</Ellipsis>,
  },
  {
    title: '发生地点',
    dataIndex: 'address',
    key: 'address',
    width: 130,
    render: val => <Ellipsis tooltip={val} lines={1}>{val}</Ellipsis>,
  },
  {
    title: '创建人',
    dataIndex: 'creator',
    key: 'creator',
    width:90,
    render: val => <Ellipsis tooltip={val} lines={1}>{val}</Ellipsis>,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    width: 130,
    render: val => <Ellipsis tooltip={val} lines={1}>{val}</Ellipsis>,
  },
];

const Index = props => {
  const { dispatch, loading, global: { globalSearchData, searchPage } } = props;
  const [fileItem, setFileItem] = useState({});
  const [filePagination, setFilePagination] = useState({
    current: 1,
    pageSize: 10,
    searchField: props.location.query.searchField
  });
  useEffect(() => {
    let payload = {
      start: 0,
      count: 10,
      searchField: props.location.query.searchField,
    };
    props.dispatch({
      type: 'global/fetch_getGlobalSearchData',
      payload,
    })
  }, []);

  const filePaginationOnChange = page => {
    const payload = {
      start: (page - 1) * 10,
      count: 10,
      searchField: props.location.query.searchField
    };
    setFilePagination({
      current: page,
      pageSize: 10,
      searchField: props.location.query.searchField
    })
    props.dispatch({
      type: 'global/fetch_getGlobalSearchData',
      payload,
      callback: function() {
        dispatch({
          type: 'global/setSearchPage',
          payload: page,
        })
      }
    })
  };

  return (
    <div className={style.globalSearchTableWarp}>
      <Spin spinning={loading}>
        <Table
          dataSource={globalSearchData.value}
          className={style.globalSearchTable}
          columns={columns}
          rowKey={record => record.id}
          pagination={{
            position: 'none'
            // ...filePagination
          }}
          onRow={ record => {
            return {
              onClick: e => {
                fileItem.handleOnRowClick(record.id, 2)
              }
            }
          }}
        >
        </Table>
        <div className={style.globalSearchTablePaginationWarp}>
          <Pagination
            style={{left: 0}}
            total={globalSearchData.totalCount}
            current={searchPage}
            pageSize={filePagination.pageSize}
            onChange={filePaginationOnChange}
          />
        </div>
      </Spin>
      <GetFileItem onRef = {(ref) => {setFileItem(ref)}} searchField={filePagination.searchField}/>
    </div>
  )
};
export default connect(({ global, loading }) => ({
  global,
  loading: loading.models.global
}))(Index)
