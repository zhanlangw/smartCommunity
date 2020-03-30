import React, { PureComponent, Fragment } from 'react';
import { Table, Button, Input, Typography, Divider, Pagination, Row, Drawer, Modal, Form, Tree, DatePicker, message  } from 'antd';
import { connect } from 'dva';
import route from 'umi/router';
import { pageSize_10 as count } from '@/utils/utils';
import style from './List.less';
import router from 'umi/router';
import moment from 'moment'

const { Text } = Typography;
const { TreeNode } = Tree;
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
@connect(({ userManagement }) => ({
  userManagement
}))
class List extends PureComponent {
  state={
    start: 1, // 分页初始页
    visible: false, // 筛选抽屉效果
    visibleModel: false,// <---process 模态窗口 --->
    userVisibleModel: false,// <---userManagement 模态窗口 --->
    loading: false,   // <--- 删除按钮Loading
    selectedRowKeys: [], // <--- list列表中 多选参数
    selectedRows: [], // --->
    userItem: false,  // <---userManagement 角色详情参数
    userItem_id:null, // --->
    checkedPreKeys: [],
    checkedDevKeys: [],
    addCheckedDevKeys: [],
    addCheckedPreKeys: [],
    expandedDevKeys: null,
    expandedPreKeys: null, // --->
    startValue: null, // <----时间筛选
    endValue: null,
    endOpen: false,// ------------->
    filter:{}, // 筛选条件
  };

  componentDidMount() {
    this.props.onRef(this);
  }

  getList = payload => {
    const { dispatch, type } = this.props;
    dispatch({
      type,
      payload,
    });
  };

  onChange = page => {
    this.setState({
      start: page,
    }, () => {
      const { start } = this.state;
      const payload = {
        ...this.state.filter,
        start: (page - 1) * count,
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
        start: 0,
        count,
      };
      resetFields();
      this.getList(payload);
    });
  };


  // 删除按钮
  handleClickDel = () => {
    const { dispatch, type, delButton } = this.props;
    let selectedRows = this.state.selectedRows.map(item => { return item.id });
    dispatch({
      type: delButton,
      payload: {
        ids: selectedRows.join(',')
      },
      callback: () => {
        this.setState({
          selectedRowKeys: [],
          start: 1,
        }, () => {
          let payload = {
            name,
            ...this.state.filter,
            start: 0,
            count,
          };
          this.getList(payload)
        });
      }
    })
  };

  // 新增提交按钮
  handleAddProcess = () => {
    const { dispatch, form: { validateFields, resetFields }, addButton } = this.props;
    validateFields((err, value) => {
      if (!err) {
        const payload = {
          desc: value.desc,
          name: value.name,
        };
        dispatch({
          type: addButton,
          payload,
          callback: (res) => {
            this.setState({
              visibleModel: false
            },() => {
              this.setState({start: 0});
              resetFields();
              router.push(`process/item/${res.id}`)
            })
          }
        })
      }
    })
  };

  // 流程模板新增弹窗
  processAddModel = () => {
    const { visibleModel } = this.state;
    const { form: { getFieldDecorator  } } = this.props;
    return (
      <Fragment>
        <Modal
          title="新建流程模板"
          centered
          visible={visibleModel}
          onCancel={() => this.setState({ visibleModel: false,  })}
          footer={[
            <Button key="submit" type="primary" onClick={this.handleAddProcess} style={{marginRight: 15}}>
              确定
            </Button>,
            <Button key="back" onClick={() => this.setState({ visibleModel: false})}>
              取消
            </Button>
          ]}
        >
          <Form>
            <Form.Item label="流程名称" {...formItemLayout}>
              {getFieldDecorator('name', {
                rules: [{ required: true, message: '请输入流程名称!' }],
              })(
                <Input placeholder='请输入流程名称'/>,
              )}
            </Form.Item>
            <Form.Item label="备注" {...formItemLayout}>
              {getFieldDecorator('desc', {
              })(
                <TextArea rows={4} style={{ resize: 'none' }} placeholder='请输入备注'/>,
              )}
            </Form.Item>
          </Form>
        </Modal>
      </Fragment>
    )
  };

  // 角色管理中 多选树形结构
  onCheckPermissionIds = (checkedKeys, info) => {
    let allCheckedKeys = [...checkedKeys, ...info.halfCheckedKeys];
    this.setState({ checkedPreKeys: checkedKeys, addCheckedPreKeys:  checkedKeys});

    // this.props.form.setFieldsValue({ permissionIds: allCheckedKeys });
  };

  onExpandPermissionIds = (expandedKeys) => {
    this.setState({
      expandedPreKeys: expandedKeys,
      autoExpandParent: false,
    });
  };

  renderPreTreeNodes = (data) => {
    return data.map((item) => {
      if (item.tree) {
        return (
          <TreeNode
            title={item.name}
            key={item.id}
            value={item.id}
            dataRef={item}
          >
            {this.renderPreTreeNodes(item.tree)}
          </TreeNode>
        );
      }
      return (
        <TreeNode
          title={item.name}
          key={item.id}
          value={item.id}
          dataRef={item}
        />
      )
    });
  };

  // 角色管理 权限树形结构 多选
  onCheckDeviceIds = (checkedKeys, info) => {
    let allCheckedKeys = [...checkedKeys, ...info.halfCheckedKeys];
    this.setState({ checkedDevKeys: checkedKeys, addCheckedDevKeys: checkedKeys });
    // this.props.form.setFieldsValue({ deviceIds: allCheckedKeys })
    // console.log()
  };

  onExpandDeviceIds = (expandedKeys) => {
    this.setState({
      expandedDevKeys: expandedKeys,
      autoExpandParent: false,
    });
  };

  renderDevTreeNodes = (data) => {
    return data.map((item) => {
      if (item.tree) {
        return (
          <TreeNode
            title={item.name}
            key={item.id}
            value={item.id}
            dataRef={item}
          >
            {this.renderDevTreeNodes(item.tree)}
          </TreeNode>
        );
      }
      return (
        <TreeNode
          title={item.name}
          key={item.id}
          value={item.id}
          dataRef={item}
        />
      )
    });
  };

  onClickDelConfirmModel = (id) => {
    const modal = Modal.confirm();
    modal.update({
      title: '删除提示',
      okText: '确定',
      centered: true,
      cancelText: '返回',
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

  handleAddUserManagement = () => {
    const { form: {validateFields, resetFields}, dispatch } = this.props;
    const { addCheckedDevKeys, addCheckedPreKeys, checkedDevKeys, checkedPreKeys, userItem, userItem_id } = this.state;
    validateFields(['name'], (err, values) => {
      if(!err){
        let payload = {
          ...values
        };
        //判断 当前是否为 新增 或者 修改
        if (userItem) {
          if (addCheckedPreKeys.length && addCheckedDevKeys.length){
            payload.permissionIds = addCheckedPreKeys;
            payload.deviceIds = addCheckedDevKeys;
            dispatch({
              type: 'userManagement/fetch_addUserList',
              payload,
              callback: () => {
                this.setState({
                  userVisibleModel: false,
                  start: 1,
                  checkedPreKeys: [],
                  checkedDevKeys: [],
                  addCheckedDevKeys: [],
                  addCheckedPreKeys: []
                });
                resetFields();
                // this.props.form.setFieldsValue({ deviceIds: '' });
                // this.props.form.setFieldsValue({ permissionIds: '' });
              }
            })
          }else {
            message.error('请选择权限')
          }
        }else {
          if (checkedPreKeys.length && checkedDevKeys.length ) {
            payload.permissionIds = checkedPreKeys;
            payload.deviceIds = checkedDevKeys;
            dispatch({
              type: 'userManagement/fetch_updUserItem',
              payload: {
                ...payload,
                id: userItem_id
              },
              callback: () => {
                this.setState({
                  userVisibleModel: false,
                  start: 1,
                  checkedPreKeys: [],
                  checkedDevKeys: [],
                  addCheckedDevKeys: [],
                  addCheckedPreKeys: []
                });
                resetFields();
                // this.props.form.setFieldsValue({ deviceIds: '' });
                // this.props.form.setFieldsValue({ permissionIds: '' });
              }
            })
          }else {
            message.error('请选择权限')
          }
        }
      } else {
        console.log(err)
      }
    })
  };

  userManagementAddModel = () => {
    const { userVisibleModel, expandedDevKeys, expandedPreKeys, userItem } = this.state;
    const { form: { getFieldDecorator }, userManagement: { permission_tree, device_tree, user_item } } = this.props;
    const preTree = permission_tree || [];
    const devTree = device_tree || [];
    return (
      <Fragment>
        <Modal
          title={userItem? "新增角色" : "角色详情"}
          centered
          visible={userVisibleModel}
          onCancel={() => {
            if (userItem) {
              this.setState({userItem:true, userVisibleModel: false, checkedDevKeys: [], checkedPreKeys: [] })
            } else {
              this.setState({userItem:true, userVisibleModel: false })
            }
          }}
          footer={[
            <Button key="submit" type="primary" onClick={this.handleAddUserManagement} style={{marginRight: 15}}>
              确定
            </Button>,
            <Button key="back" onClick={()=> {
              if (userItem) {
                this.setState({userItem:true, userVisibleModel: false, checkedDevKeys: [], checkedPreKeys: [] })
              } else {
                this.setState({userItem:true, userVisibleModel: false })
              }
            }}>
              取消
            </Button>,
          ]}
        >
          <Form>
            <Form.Item label="角色名称" {...formItemLayout}>
              {getFieldDecorator('name', {
                rules: [{ required: true, message: '请输入角色名称' },],
                // onChange: (e) => self.changeScore(e, `thinkDoMark-${row.id}`),
                initialValue: userItem ? undefined : user_item && user_item.name || undefined
              })(
                <Input placeholder='请输入角色名称'/>,
              )}
            </Form.Item>
            {/*<Form.Item label="排序编号" {...formItemLayout}>*/}
            {/*  {getFieldDecorator('num', {*/}
            {/*    initialValue: userItem ? undefined : user_item && user_item.num || undefined,*/}
            {/*    getValueFromEvent: (event) => {*/}
            {/*      return event.target.value.replace(/\D/g, '')*/}
            {/*    },*/}
            {/*  })(*/}
            {/*    <Input type='text' />,*/}
            {/*  )}*/}
            {/*</Form.Item>*/}
            <Form.Item
              label='权限分配'
              {...formItemLayout}
            >
              {
                getFieldDecorator('permissionIds', {
                  rules: [{ required: true, message: '请选择权限分配' },],
                  // initialValue: userItem ? this.state.addCheckedPreKeys : this.state.checkedPreKeys || undefined
                })(
                  <div className={style.formTree}>
                    <Tree
                      checkable
                      //checkStrictly={true}
                      onCheck={this.onCheckPermissionIds}
                      onExpand={this.onExpandPermissionIds}
                      // expandedKeys={expandedPreKeys}
                      checkedKeys={userItem ? this.state.addCheckedPreKeys : this.state.checkedPreKeys || undefined}
                      //onSelect={this.onSelect}
                    >
                      {this.renderPreTreeNodes(preTree)}
                    </Tree>
                  </div>
                )
              }
            </Form.Item>
            <Form.Item
              label='摄像机权限'
              {...formItemLayout}
            >
              {
                getFieldDecorator('deviceIds', {
                  rules: [{ required: true, message: '请选择摄像机权限' },],
                  // initialValue: userItem ? this.state.addCheckedDevKeys : this.state.checkedDevKeys || undefined
                })(
                  <div className={style.formTree}>
                    <Tree
                      checkable
                      //checkStrictly={true}
                      onCheck={this.onCheckDeviceIds}
                      onExpand={this.onExpandDeviceIds}
                      // expandedKeys={expandedDevKeys}
                      checkedKeys={userItem ? this.state.addCheckedDevKeys : this.state.checkedDevKeys || undefined}
                      //onSelect={this.onSelect}
                    >
                      {this.renderDevTreeNodes(devTree)}
                    </Tree>
                  </div>
                )
              }
            </Form.Item>
            <Form.Item label="备注" {...formItemLayout}>
              {getFieldDecorator('desc', {
                initialValue: userItem ? undefined : user_item && user_item.desc || undefined
              })(
                <TextArea rows={4} style={{ resize: 'none' }} />,
              )}
            </Form.Item>
          </Form>
        </Modal>
      </Fragment>
    )
  };

  // ---------------------------------------开始时间<->结束时间---------------------------------------------
  // disabledStartDate = startValue => {
  //   const { endValue } = this.state;
  //   if (!startValue || !endValue) {
  //     return false;
  //   }
  //   return startValue.valueOf() > endValue.valueOf();
  // };
  //
  // disabledEndDate = endValue => {
  //   const { startValue } = this.state;
  //   if (!endValue || !startValue) {
  //     return false;
  //   }
  //   return endValue.valueOf() <= startValue.valueOf();
  // };
  //
  // onDatePickerChange = (field, value) => {
  //   this.setState({
  //     [field]: value,
  //   });
  // };
  //
  // onStartChange = value => {
  //   this.onDatePickerChange('startValue', value);
  // };
  //
  // onEndChange = value => {
  //   this.onDatePickerChange('endValue', value);
  // };
  //
  // handleStartOpenChange = open => {
  //   if (!open) {
  //     this.setState({ endOpen: true });
  //   }
  // };
  //
  // handleEndOpenChange = open => {
  //   this.setState({ endOpen: open });
  // };
  // ---------------------------------------开始时间<->结束时间---------------------------------------------


  // 筛选提交
  // handleDrawerSubmit = () => {
  //   const { dispatch, form: { validateFields, resetFields }, type } = this.props;
  //   validateFields((err, values) => {
  //     this.setState({
  //       filter: {
  //         name: values.filterName || undefined,
  //         creator: values.creator || undefined,
  //         startTime: values.startTime && moment(values.startTime).format("YYYY/MM/DD HH:mm:ss") || undefined,
  //         endTime: values.endTime && moment(values.endTime).format("YYYY/MM/DD HH:mm:ss") || undefined
  //       }
  //     });
  //     let payload = {
  //       name: values.filterName || undefined,
  //       creator: values.creator || undefined,
  //       startTime: values.startTime && moment(values.startTime).format("YYYY/MM/DD HH:mm:ss") || undefined,
  //       endTime: values.endTime && moment(values.endTime).format("YYYY/MM/DD HH:mm:ss") || undefined,
  //       start: 0,
  //       count,
  //     };
  //     this.getList(payload);
  //   })
  // };

  // footer底部的搜索框
  handleFooterSubmit = (name) => {
    const { form: { validateFields }, type } = this.props;
    validateFields((err, values) => {
      this.setState({
        filter: {
          name,
        }
      });
      let payload = {
        name,
        start: 0,
        count,
      };
      this.getList(payload);
    })
  };

  // 抽屉效果状态
  onClose = () => {
    const { form: { resetFields } } = this.props;
    this.setState({
      visible: false,
    });
    resetFields();
  };



  // DrawerForm = () => {
  //   const { type, form: {getFieldDecorator} } = this.props;
  //   const { endOpen } = this.state;
  //   switch(type) {
  //    case 'Process/fetch_getProcessList':
  //      return (
  //      <Form>
  //        <Form.Item
  //          label='名称'
  //          {...formItemLayout}
  //        >
  //          {getFieldDecorator('filterName',{})(
  //            <Input placeholder='请输入名称' />
  //          )}
  //        </Form.Item>
  //        <Form.Item
  //          label='开始时间'
  //          {...formItemLayout}
  //        >
  //          {getFieldDecorator('startTime',{})(
  //            <DatePicker
  //              disabledDate={this.disabledStartDate}
  //              showTime
  //              format="YYYY-MM-DD HH:mm:ss"
  //              placeholder="请选择开始时间"
  //              onChange={this.onStartChange}
  //              onOpenChange={this.handleStartOpenChange}
  //            />
  //          )}
  //        </Form.Item>
  //        <Form.Item
  //          label='结束时间'
  //          {...formItemLayout}
  //        >
  //          {getFieldDecorator('endTime',{})(
  //            <DatePicker
  //              disabledDate={this.disabledEndDate}
  //              showTime
  //              format="YYYY-MM-DD HH:mm:ss"
  //              placeholder="请选择结束时间"
  //              onChange={this.onEndChange}
  //              open={endOpen}
  //              onOpenChange={this.handleEndOpenChange}
  //            />
  //          )}
  //        </Form.Item>
  //      </Form>
  //      );
  //    case 'userManagement/fetch_getUserList':
  //      return (
  //        <Form>
  //          <Form.Item
  //            label='名称'
  //            {...formItemLayout}
  //          >
  //            {getFieldDecorator('filterName',{})(
  //              <Input placeholder='请输入名称' />
  //            )}
  //          </Form.Item>
  //          <Form.Item
  //            label='创建人'
  //            {...formItemLayout}
  //          >
  //            {getFieldDecorator('creator',{})(
  //              <Input placeholder='请输入创建人' />
  //            )}
  //          </Form.Item>
  //          <Form.Item
  //            label='开始时间'
  //            {...formItemLayout}
  //          >
  //            {getFieldDecorator('startTime',{})(
  //              <DatePicker
  //                disabledDate={this.disabledStartDate}
  //                showTime
  //                format="YYYY-MM-DD HH:mm:ss"
  //                placeholder="请选择开始时间"
  //                onChange={this.onStartChange}
  //                onOpenChange={this.handleStartOpenChange}
  //              />
  //            )}
  //          </Form.Item>
  //          <Form.Item
  //            label='结束时间'
  //            {...formItemLayout}
  //          >
  //            {getFieldDecorator('endTime',{})(
  //              <DatePicker
  //                disabledDate={this.disabledEndDate}
  //                showTime
  //                format="YYYY-MM-DD HH:mm:ss"
  //                placeholder="请选择结束时间"
  //                onChange={this.onEndChange}
  //                open={endOpen}
  //                onOpenChange={this.handleEndOpenChange}
  //              />
  //            )}
  //          </Form.Item>
  //        </Form>
  //      );
  //    default:
  //       break;
  //   }
  // };

  userManagementOnRowClick = (res) => {
    console.log(res);
    this.setState({
      userVisibleModel: true,
      userItem: false,
      userItem_id: res.id,
      checkedPreKeys: res.permissionIds,
      checkedDevKeys: res.deviceIds
    }, () => {
      console.log(this.state)
    });
  };

  render() {
    const { dataSource, columns, filter, addButton, type, dispatch, addButtonText } = this.props;
    const { visibleModel, visible, loading, selectedRowKeys, start } = this.state;
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
      <div>
        {/* 新增列表 */}
        <Button
          type="primary"
          style={{ margin: '12px 10px 8px' }}
          onClick={() => this.setState({ userItem: true, visibleModel: true, userVisibleModel: true })}
        >
          {addButtonText}
        </Button>
        {/* 列表 */}
        <Table
          rowKey={record => record.id}
          className={style.TableWarp}
          dataSource={dataSource && dataSource.value}
          rowSelection={rowSelection}
          columns={columns}
          pagination={{
            position: 'none',
          }}
          onRow={record => {
            return {
              onClick: e => {
                  // case 'userManagement/fetch_getUserList':
                  //   this.setState({
                  //     userItem: false
                  //   });
                  //   dispatch({
                  //     type: 'userManagement/fetch_getUserItem',
                  //     payload: {
                  //       id: record.id
                  //     },
                  //     callback: (res) => {
                  //       this.userManagementOnRowClick(res);
                  //     }
                  //   });
                  //   break;
                  type === 'Process/fetch_getProcessList' && route.push(`/management/process/item/${record.id}`);
              },
            };
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
                  {/*  filter ?*/}
                  {/*  (<Text*/}
                  {/*      disabled*/}
                  {/*      style={{ fontSize: 16, marginLeft: 6 }}*/}
                  {/*    >*/}
                  {/*    筛选*/}
                  {/*  </Text>)*/}
                  {/*  :*/}
                  {/*  (<Text*/}
                  {/*      onClick={() => this.setState({ visible: true })}*/}
                  {/*      style={{ fontSize: 16, marginLeft: 6, cursor: 'pointer' }}*/}
                  {/*    >*/}
                  {/*    筛选*/}
                  {/*  </Text>)*/}
                  {/*}*/}
                  {/*<Divider type="vertical" style={{ margin: '0px 3px' }} />*/}
                  <Text
                    onClick={this.handleClickReset}
                    style={{ fontSize: 16, cursor: 'pointer', marginLeft: 18 }}
                  >
                    重置
                  </Text>
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
        {/* 抽屉筛选部分 */}
        {/*<Drawer*/}
        {/*  title="筛选"*/}
        {/*  placement="right"*/}
        {/*  width={340}*/}
        {/*  // closable={false}*/}
        {/*  mask={false}*/}
        {/*  onClose={this.onClose}*/}
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
        {/*      onClick={this.onClose}*/}
        {/*    >*/}
        {/*      关闭*/}
        {/*    </Button>*/}
        {/*    <Button onClick={this.handleDrawerSubmit} type="primary">*/}
        {/*      提交*/}
        {/*    </Button>*/}
        {/*  </div>*/}
        {/*</Drawer>*/}
        {/* 新增模态窗口 */}
        {addButton === 'Process/fetch_addProcessItem' && this.state.visibleModel && this.processAddModel()}
        {addButton === 'userManagement/fetch_addUserList' && this.state.userVisibleModel && this.userManagementAddModel()}
      </div>
    );
  }
}
export default List;
