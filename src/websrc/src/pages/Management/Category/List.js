import React, { PureComponent, Fragment } from 'react';
import {Table, Button, Input, Typography, Divider, Pagination, Row, Drawer, Modal, Form, Tree, DatePicker, Select, Spin} from 'antd';
import { connect } from 'dva';
import { pageSize_10 as count } from '@/utils/utils';
import style from './List.less';
import moment from "moment";

const { Text } = Typography;
const { TreeNode } = Tree;
const { Option } = Select;
const { Search, TextArea } = Input;
const formItemLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 6 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 16 },
  },
};

@Form.create()
@connect(({ global, loading }) => ({
  global,
  loading: loading.models.global
}))
class List extends PureComponent {
  state={
    start: 1, // 分页初始页
    visible: false, // 筛选抽屉效果
    selectedRowKeys: [], // <--- list列表中 多选参数
    selectedRows: [], // --->
    startValue: null, // <----时间筛选
    endValue: null,
    endOpen: false,// ------------->
  };

  componentDidMount() {
    this.props.onRef(this)
  }

  // addClick = () => {
  //   this.setState({
  //     start: 1
  //   })
  // };

  getList = payload => {
    const { dispatch, type, fatherBigListItem, smallPaginationParams } = this.props;
    payload.id = smallPaginationParams.id || fatherBigListItem.id;
    console.log(payload);
    dispatch({
      type,
      payload,
    });
  };

  onChange = page => {
    const { fatherBigListItem, smallPaginationParams } = this.props;
    this.setState({
      start: page,
    }, () => {
      console.log(page)
      const { start } = this.state;
      const payload = {
        ...this.state.filter,
        start: (page - 1) * count,
        id: smallPaginationParams.id || fatherBigListItem.id,
        count,
      };
      this.getList(payload);
    });
  };

  // 重置按钮
  handleClickReset = () => {
    const { form: {resetFields} } = this.props;
    this.setState({
      start: 1,
    }, () => {
      const { start } = this.state;
      this.setState({
        filter: {},
        name: undefined,
      });
      const payload = {
        start,
        count,
      };
      resetFields();
      this.getList(payload);
    });
  };

  // footer底部的搜索框
  handleFooterSubmit = (name) => {
    const { form: { validateFields }, type } = this.props;
    validateFields((err, values) => {
      this.setState({
        name
      });
      let payload = {
        name,
        start: 0,
        count,
      };
      this.getList(payload);
    })
  };

  onClickDelConfirmModel = (id) => {
    const modal = Modal.confirm();
    modal.update({
      title: '删除提示',
      okText: '确定',
      cancelText: '返回',
      centered: true,
      content: (
        <Text> 是否确定要删除</Text>
      ),
      onCancel:()=>{
        modal.destroy();
      },
      onOk:()=> {
        this.handleClickDel();
        modal.destroy();
      }
    });
  };

  handleClickDel = () => {
    const { dispatch, type, delButton, fatherBigListItem } = this.props;
    let selectedRows = this.state.selectedRows.map(item => { return item.id });
    dispatch({
      type: delButton,
      payload: {
        ids: selectedRows.join(','),
        id: fatherBigListItem && fatherBigListItem.id,
      },
      callback: () => {
        this.setState({
          selectedRowKeys: [],
          start: 1,
        });
      }
    })
  };

  // ---------------------------------------开始时间<->结束时间---------------------------------------------
  disabledStartDate = startValue => {
    const { endValue } = this.state;
    if (!startValue || !endValue) {
      return false;
    }
    return startValue.valueOf() > endValue.valueOf();
  };

  disabledEndDate = endValue => {
    const { startValue } = this.state;
    if (!endValue || !startValue) {
      return false;
    }
    return endValue.valueOf() <= startValue.valueOf();
  };

  onDatePickerChange = (field, value) => {
    this.setState({
      [field]: value,
    });
  };

  onStartChange = value => {
    this.onDatePickerChange('startValue', value);
  };

  onEndChange = value => {
    this.onDatePickerChange('endValue', value);
  };

  handleStartOpenChange = open => {
    if (!open) {
      this.setState({ endOpen: true });
    }
  };

  handleEndOpenChange = open => {
    this.setState({ endOpen: open });
  };
  // ---------------------------------------开始时间<->结束时间---------------------------------------------

  // 筛选提交
  // handleDrawerSubmit = () => {
  //   const { dispatch, form: { validateFields, resetFields }, type } = this.props;
  //   validateFields((err, values) => {
  //     this.setState({
  //       filter: {
  //         name: values.filterName || undefined,
  //         creator: values.creator || undefined,
  //         workType: values.workType || undefined,
  //         startTime: values.startTime && moment(values.startTime).format("YYYY/MM/DD HH:mm:ss") || undefined,
  //         endTime: values.endTime && moment(values.endTime).format("YYYY/MM/DD HH:mm:ss") || undefined
  //       }
  //     });
  //     let payload = {
  //       name: values.filterName || undefined,
  //       creator: values.creator || undefined,
  //       workType: values.workType || undefined,
  //       startTime: values.startTime && moment(values.startTime).format("YYYY/MM/DD HH:mm:ss") || undefined,
  //       endTime: values.endTime && moment(values.endTime).format("YYYY/MM/DD HH:mm:ss") || undefined,
  //       start: 1,
  //       count,
  //     };
  //     this.getList(payload);
  //   })
  // };

  // DrawerForm = () => {
  //   const { form: {getFieldDecorator} } = this.props;
  //   const { endOpen } = this.state;
  //   return (
  //     <Form>
  //       <Form.Item
  //         label='名称'
  //         {...formItemLayout}
  //       >
  //         {getFieldDecorator('filterName',{})(
  //           <Input placeholder='请输入名称' />
  //         )}
  //       </Form.Item>
  //       <Form.Item
  //         label='创建人'
  //         {...formItemLayout}
  //       >
  //         {getFieldDecorator('creator',{})(
  //           <Input placeholder='请输入创建人' />
  //         )}
  //       </Form.Item>
  //       <Form.Item
  //         label='紧急程度'
  //         {...formItemLayout}
  //       >
  //         {getFieldDecorator('workType',{})(
  //           <Select placeholder='请选择紧急程度'>
  //             <Option value='URGENT'>紧急</Option>
  //             <Option value='ORDINARY'>普通</Option>
  //           </Select>
  //         )}
  //       </Form.Item>
  //       <Form.Item
  //         label='开始时间'
  //         {...formItemLayout}
  //       >
  //         {getFieldDecorator('startTime',{})(
  //           <DatePicker
  //             disabledDate={this.disabledStartDate}
  //             showTime
  //             format="YYYY-MM-DD HH:mm:ss"
  //             placeholder="请选择开始时间"
  //             onChange={this.onStartChange}
  //             onOpenChange={this.handleStartOpenChange}
  //           />
  //         )}
  //       </Form.Item>
  //       <Form.Item
  //         label='结束时间'
  //         {...formItemLayout}
  //       >
  //         {getFieldDecorator('endTime',{})(
  //           <DatePicker
  //             disabledDate={this.disabledEndDate}
  //             showTime
  //             format="YYYY-MM-DD HH:mm:ss"
  //             placeholder="请选择结束时间"
  //             onChange={this.onEndChange}
  //             open={endOpen}
  //             onOpenChange={this.handleEndOpenChange}
  //           />
  //         )}
  //       </Form.Item>
  //     </Form>
  //   );
  // };

  // 抽屉效果状态
  // onCloseDrawer = () => {
  //   const { form: { resetFields } } = this.props;
  //   this.setState({ visible: false }, ()=> resetFields);
  // };

  render() {
    const { dataSource, columns, filter, fatherBigListItem, loading } = this.props;
    const { visible, selectedRowKeys, start } = this.state;
    const rowSelection = {
      onChange: (selectedRowKeys, selectedRows) => {
        this.setState({
          selectedRowKeys,
          selectedRows
        });
      },
      selectedRowKeys,
    };
    const total = dataSource && dataSource.totalCount;
    let hasSelected = selectedRowKeys.length > 0;
    return (
      <Fragment>
        {/* 列表 */}
        <Spin spinning={loading}>
        <Table
          className={style.TableWarp}
          dataSource={dataSource && dataSource.value}
          rowSelection={rowSelection}
          columns={columns}
          rowKey={record => record.id}
          pagination={{
            position: 'none',
          }}
          // 底部功能栏
          footer={() => (
            <Row type="flex" justify="space-between">
              <Row type="flex">
                <Button
                  type="primary"
                  onClick={this.onClickDelConfirmModel}
                  disabled={!hasSelected}
                  style={{ marginLeft: 7, marginRight: 15 }}
                >
                  删除
                </Button>
                <Search
                  placeholder="请输入查询名称"
                  value={this.state.name}
                  onChange={e => this.setState({ name: e.target.value })}
                  onSearch={(value) => {this.handleFooterSubmit(value)}}
                  style={{ width: 200 }}
                />
                <div style={{ marginTop: 5 }}>
                  {/*{*/}
                  {/*  fatherBigListItem && fatherBigListItem.id ?*/}
                  {/*  (*/}
                  {/*    <Text*/}
                  {/*      onClick={() => this.setState({ visible: true })}*/}
                  {/*      style={{ fontSize: 16, marginLeft: 6, cursor: 'pointer' }}*/}
                  {/*    >*/}
                  {/*      筛选*/}
                  {/*    </Text>*/}
                  {/*  )*/}
                  {/*  :*/}
                  {/*  (*/}
                  {/*    <Text*/}
                  {/*      disabled*/}
                  {/*      style={{ fontSize: 16, marginLeft: 6 }}*/}
                  {/*    >*/}
                  {/*      筛选*/}
                  {/*    </Text>*/}
                  {/*  )*/}
                  {/*}*/}
                  {/*<Divider type="vertical" style={{ margin: '0px 3px' }} />*/}
                  {
                    fatherBigListItem && fatherBigListItem.id ? (
                      <Text
                        onClick={this.handleClickReset}
                        style={{ marginLeft: 10, fontSize: 16, cursor: 'pointer' }}
                      >
                        重置
                      </Text>
                    ) : (
                      <Text
                        style={{ marginLeft: 10,  fontSize: 16, cursor: 'pointer' }}
                        disabled
                      >
                        重置
                      </Text>
                    )
                  }
                </div>
              </Row>
              {/* 分页 */}
              <Pagination
                onChange={this.onChange}
                total={total}
                pageSize={count}
                current={start}
              />
            </Row>
          )}
        />
        </Spin>
        {/* 抽屉筛选部分 */}
        {/*<Drawer*/}
        {/*  title="筛选"*/}
        {/*  placement="right"*/}
        {/*  width={350}*/}
        {/*  // closable={false}*/}
        {/*  mask={false}*/}
        {/*  onClose={this.onCloseDrawer}*/}
        {/*  visible={visible}*/}
        {/*>*/}
        {/*  {this.DrawerForm()}*/}
        {/*  <div*/}
        {/*    style={{*/}
        {/*      position: 'absolute',*/}
        {/*      bottom: 0,*/}
        {/*      width: '100%',*/}
        {/*      borderTop: '1px solid #e8e8e8',*/}
        {/*      padding: '10px 16px',*/}
        {/*      textAlign: 'right',*/}
        {/*      left: 0,*/}
        {/*      background: '#fff',*/}
        {/*      borderRadius: '0 0 4px 4px',*/}
        {/*    }}*/}
        {/*  >*/}
        {/*    <Button*/}
        {/*      style={{*/}
        {/*        marginRight: 8,*/}
        {/*      }}*/}
        {/*      onClick={this.onCloseDrawer}*/}
        {/*    >*/}
        {/*      关闭*/}
        {/*    </Button>*/}
        {/*    <Button onClick={this.handleDrawerSubmit} type="primary">*/}
        {/*      提交*/}
        {/*    </Button>*/}
        {/*  </div>*/}
        {/*</Drawer>*/}
      </Fragment>
    );
  }
}
export default List;
