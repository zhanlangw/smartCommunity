import React, { PureComponent, Fragment } from 'react';
import { Row, Col, Button, Tree , Icon, List, Modal, Form, Input, Radio, Spin } from 'antd';
import { connect } from 'dva';
import MenuTable from './List';
import styles from './style.less';
import TreeSelect from '@/components/TreeSelect';
import {payload_0_10, returnWorkTypeStyle, returnWorkTypeText, validator, abbreviation} from '@/utils/utils'
import Ellipsis from "@/components/Ellipsis";

const { TreeNode } = Tree;
const { TextArea } = Input;
const ListData = [
  {
    title: '添加小类',
    type: 'ADD'
  },
  {
    title: '修改大类',
    type: 'UPD'
  },
  {
    title: '删除大类',
    type:'DEL'
  },
];
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
  @connect(({ category, global, loading }) => ({
  category,
  global,
  cateLoading: loading.models.category
}))
class index extends PureComponent {
  state={
    page: {
      X:0,
      Y:0,
    },
    node: null,
    customizeMenusVisible: false,
    customizeMenusAddOrUpdModalVisible: false,
    customizeMenusAddOrUpd: true,
    smallId: null,
    timeType: '天',
    workType: 'ORDINARY',
  };

  componentDidMount() {
    document.addEventListener("click", ()=> {
      this.setState({
        customizeMenusVisible: false,
      })
    });
    const { dispatch } = this.props;
    dispatch({
      type: 'category/fetch_getBigCategoryList'
    })
  }

  renderTreeNodes = (data) => {
    return data && data.map((item) => {
      return <TreeNode
                value={item.id}
                key={item.id}
                dataRef={item}
                title={item.name}
                icon={<span className={styles.icon_heading} />}
                style={{ marginLeft: -10 , width: '92%', fontSize: 16 }}
              />;
    });
  };

  customizeMenus = () => {
    const { page: { X, Y }, customizeMenusAddOrUpd, node } = this.state;
    const { dispatch } = this.props;
    return (
      <List
        itemLayout="horizontal"
        dataSource={ListData}
        style={{
          textAlign: 'center',
          border: '1px solid #cdcd',
          backgroundColor: '#fff',
          position: 'fixed',
          width:80,
          left: `${X}px`,
          top: `${Y}px`,
          zIndex: 5
        }}
        renderItem={item => (
          <List.Item>
            <List.Item.Meta
              title={item.title}
              onClick={()=>{
                this.setState({
                  customizeMenusVisible:false
                });
                switch (item.type) {
                  case 'ADD':
                    this.setState({
                      smallAddOrUpd: true,
                      smallAddOrUpdModalVisible: true,
                    });
                    break;
                  case 'UPD':
                    dispatch({
                      type: 'category/fetch_getBigCategoryItem',
                      payload: {
                        id: node.id
                      }
                    });
                    this.setState({
                      customizeMenusAddOrUpd: false,
                      customizeMenusAddOrUpdModalVisible: true
                    });
                    break;
                  case 'DEL':
                    dispatch({
                      type: 'category/fetch_delBigCategoryItem',
                      payload: {
                        ids: node.id
                      }
                    });
                    break;
                  default:
                    break;
                }
              }}
            />
          </List.Item>
        )}
      />
    )
  };

  handelCustomizeMenusAddOrUpd = () => {
    const { customizeMenusAddOrUpd, node } = this.state;
    const { form: { validateFields, resetFields }, dispatch } = this.props;
    validateFields((err, payload) => {
      if(!err) {
        if (customizeMenusAddOrUpd) {
          dispatch({
            type: 'category/fetch_addBigCategoryItem',
            payload,
            callback: res => {
              if(res.status === 0) {
                this.setState({
                  customizeMenusAddOrUpdModalVisible: false
                }, () => {
                  resetFields()
                })
              }
            }
          })
        } else {
          payload.id = node.id;
          dispatch({
            type: 'category/fetch_updBigCategoryItem',
            payload,
            callback: res => {
              if (res.status === 0) {
                this.setState({
                  customizeMenusAddOrUpdModalVisible: false
                }, () => {
                  resetFields()
                })
              }
            }
          })
        }
      }else {
        console.log(err)
      }
    })
  };

  customizeMenusAddOrUpdModal = () => {
    const { customizeMenusAddOrUpdModalVisible, customizeMenusAddOrUpd } = this.state;
    const { form: { getFieldDecorator }, category: { bigCategory_item } } = this.props;
    return (
      <Modal
        centered
        title={customizeMenusAddOrUpd? '新增案卷大类' : '修改案卷大类' }
        visible={customizeMenusAddOrUpdModalVisible}
        onCancel={() => this.setState({ customizeMenusAddOrUpdModalVisible: false })}
        footer={[
          <Button key="submit" type="primary" onClick={this.handelCustomizeMenusAddOrUpd} style={{marginRight: 15}}>
            确定
          </Button>,
          <Button key="back" onClick={() => this.setState({ customizeMenusAddOrUpdModalVisible: false })}>
            取消
          </Button>,
        ]}
      >
        <Form>
          <Form.Item {...formItemLayout} label='案卷大类名称'>
            {getFieldDecorator('name', {
              rules: [
                {required: true, message: '请输入案卷大类名称'},
                {
                  validator:validator,
                }
              ],
              initialValue: customizeMenusAddOrUpd ? undefined : bigCategory_item && bigCategory_item.name || undefined
            })(
              <Input placeholder='请输入案卷大类名称'/>,
            )}
          </Form.Item>
          <Form.Item {...formItemLayout} label='简称'>
            {getFieldDecorator('abbre', {
              rules: [
                {required: true, message: '请输入简称'},
                {
                  pattern: abbreviation, message: '只能输入由数组或字母组成的简称'
                }
              ],
              initialValue: customizeMenusAddOrUpd ? undefined : bigCategory_item && bigCategory_item.abbre || undefined
            })(
              <Input placeholder='请输入简称'/>,
            )}
          </Form.Item>
          <Form.Item {...formItemLayout} label='排序'>
            {getFieldDecorator('num', {
              getValueFromEvent: (event) => {
                return event.target.value.replace(/\D/g,'')
              },
              initialValue: customizeMenusAddOrUpd ? undefined : bigCategory_item && bigCategory_item.num || undefined
            })(
              <Input  placeholder='请输入排序编号'/>,
            )}
          </Form.Item>
          <Form.Item {...formItemLayout} label='描述'>
            {getFieldDecorator('desc', {
              initialValue: customizeMenusAddOrUpd ? undefined : bigCategory_item && bigCategory_item.desc || undefined
            })(
              <TextArea rows={4} style={{ resize: 'none' }} placeholder='请描述'/>
            )}
          </Form.Item>
        </Form>
      </Modal>
    )
  };

  handleTreeRightClick = (e) => {
    const { event , node } = e;
    const clientHeight = document.body.clientHeight;
    let Y= event.pageY;
    if((clientHeight-Y)<115){
      Y = Y - (200-(clientHeight-Y))
    }
    this.setState({
      page:{
        X: event.pageX,
        Y
      },
      node: node.props.dataRef,
      customizeMenusVisible: true,
    });
    node.props = {
      selected: true
    }
  };

  handleTreeClick = (e, event) => {
    const { dispatch } = this.props;
    console.log(e);
    if (e.length >= 1) {
      dispatch({
        type: 'category/fetch_getBigCategoryItem',
        payload: {
          id: e.join(',')
        },
        callback: res => {
          this.setState({
            node: res.value,
            pageParams: res.value,
          }, () => {
            this.onRef.onChange(1);
          })
        }
      });
      dispatch({
        type: 'category/fetch_getSmallCategoryList',
        payload: {
          ...payload_0_10,
          id: e.join(',')
        }
      })
    }
  };

  // -----------------------------------------------------以下案卷小类------------------------------------------------->
  handelSmallAddOrUpd = () => {
    const { smallAddOrUpd, node, updSmallId, unitId, workType } = this.state;
    const { form: { validateFields, resetFields }, dispatch } = this.props;
    validateFields((err, payload) => {
      if (!err) {
        console.log(payload);
        if (smallAddOrUpd) {
          payload.bigCategoryId = node.id;
          payload.unitId = unitId;
          dispatch({
            type: 'category/fetch_addSmallCategoryItem',
            payload,
            callback: res => {
              if (res.status === 0) {
                this.onRef.setState({
                  start: 1
                })
                this.setState({
                  smallAddOrUpdModalVisible: false,
                  pageParams: {
                    id: payload.bigCategoryId
                  }
                }, () => {
                  resetFields()
                })
              }
            }
          })
        } else {
          if (workType==='URGENT'){
            payload.unitId = unitId;
          }
          payload.id = updSmallId;
          dispatch({
            type: 'category/fetch_updSmallCategoryItem',
            payload,
            callback: res => {
              if (res.status === 0) {
                this.onRef.setState({
                  start: 1
                })
                this.setState({
                  smallAddOrUpdModalVisible: false,
                  pageParams: {
                    id: payload.bigCategoryId
                  }
                }, () => {
                  resetFields()
                })
              }
            }
          })
        }
      }
    })
  };

  handelWorkTypeOnChange = (e) => {
    const { dispatch } = this.props;
    if (e.target.value === 'URGENT'){
      dispatch({
        type: 'global/fetch_get_tree',
      })
    }
    this.setState({
      workType: e.target.value
    })
  };

  handleTimeTypeOnChange = (e) => {
    let timeType = null;
    switch (e.target.value) {
      case 'DAY':
        timeType = '天';
        break;
      case 'HOUR':
        timeType = '小时';
        break;
      default:
        break;
    }
    this.setState({
      timeType,
    })
  };

  handleTimeTypeText = (type) => {
    switch (type) {
      case 'DAY':
        return '天';
      case 'HOUR':
        return '小时';
      default:
        break;
    }
  };

  smallAddOrUpdModal = () => {
    const { smallAddOrUpd, smallAddOrUpdModalVisible, timeType, workType, node } = this.state;
    const { form: { getFieldDecorator }, category: { smallCategory_item }, global: { tree } } = this.props;
    return (
      <Modal
        title={smallAddOrUpd ? '新增案卷小类' : '修改案卷小类'}
        visible={smallAddOrUpdModalVisible}
        centered
        onCancel={() => this.setState({ smallAddOrUpdModalVisible: false, unitId: [] })}
        footer={[
          <Button key="submit" type="primary" onClick={this.handelSmallAddOrUpd} style={{marginRight: 15}}>
            确定
          </Button>,
          <Button key="back" onClick={() => this.setState({ smallAddOrUpdModalVisible: false, unitId: [] })}>
            取消
          </Button>,
        ]}
      >
        <Form>
          <Form.Item {...formItemLayout} label='类别大类'>
            {getFieldDecorator('bigCategoryId', {
              initialValue: smallAddOrUpd ? node && node.id : smallCategory_item && smallCategory_item.bigCategory && smallCategory_item.bigCategory.id || undefined
            })(
              <Input style={{ display: 'none' }} disabled />,
            )}
            <span>{  smallAddOrUpd ? node && node.name : smallCategory_item && smallCategory_item.bigCategory && smallCategory_item.bigCategory.name}</span>
          </Form.Item>
          <Form.Item {...formItemLayout} label='案卷小类名称'>
            {getFieldDecorator('name', {
              rules: [
                {required: true, message: '请输入案卷小类名称'},
                {
                  validator:validator,
                }
              ],
              initialValue: smallAddOrUpd ? undefined : smallCategory_item && smallCategory_item.name || undefined
            })(
              <Input placeholder='请输入案卷小类名称'/>,
            )}
          </Form.Item>
          <Form.Item {...formItemLayout} label='简称'>
            {getFieldDecorator('abbre', {
              rules: [
                {required: true, message: '请输入简称'},
                {
                  pattern: abbreviation, message: '只能输入由数组或字母组成的简称'
                }
              ],
              initialValue: smallAddOrUpd ? undefined : smallCategory_item && smallCategory_item.abbre || undefined
            })(
              <Input placeholder='请输入简称'/>,
            )}
          </Form.Item>
          <Form.Item {...formItemLayout} label='紧急程度'>
            {getFieldDecorator('workType', {
              rules: [{required: true, message: '请选择紧急程度'},],
              initialValue: smallAddOrUpd ? undefined : smallCategory_item && smallCategory_item.workType || undefined
            })(
              <Radio.Group onChange={this.handelWorkTypeOnChange} >
                <Radio value='ORDINARY'>日常</Radio>
                <Radio value='URGENT'>紧急</Radio>
              </Radio.Group>
            )}
          </Form.Item>
          {
            workType ==='URGENT' ? (
              <Form.Item {...formItemLayout} label='职能部门'>
                {getFieldDecorator('unitId', {
                  rules: [{required: true, message: '请选择职能部门'},],
                  initialValue: smallAddOrUpd ? undefined : smallCategory_item && smallCategory_item.unit && smallCategory_item.unit.name || undefined
                })(
                  <TreeSelect onChange={(e) => this.setState({ unitId: e})} placeholder='请选择职能部门'/>,
                )}
              </Form.Item>
            ): ''
          }
          <Form.Item {...formItemLayout} label='办理时长'>
            {getFieldDecorator('timeType', {
              rules: [{required: true, message: '请选办理时长'},],
              initialValue: smallAddOrUpd ? undefined : smallCategory_item && smallCategory_item.timeType || undefined
            })(
              <Radio.Group onChange={this.handleTimeTypeOnChange}>
                <Radio value='DAY' defaultChecked>天</Radio>
                <Radio value='HOUR'>小时</Radio>
              </Radio.Group>
            )}
          </Form.Item>
          <Form.Item {...formItemLayout} label='输入时长'>
            {getFieldDecorator('time', {
              rules: [
                {required: true, message: '请输入输入时长'},
              ],
              initialValue: smallAddOrUpd ? undefined : smallCategory_item && smallCategory_item.time || undefined,
              getValueFromEvent: (event) => {
                return event.target.value.replace(/\D/g,'')
              }
            })(
              //smallAddOrUpd ? timeType || undefined : smallCategory_item && this.handleTimeTypeText(smallCategory_item.timeType) || timeType
              <Input
                placeholder='请输入时长'
                addonAfter={timeType}
              />
            )}
          </Form.Item>
          <Form.Item {...formItemLayout} label='描述'>
            {getFieldDecorator('desc', {
              initialValue: smallAddOrUpd ? undefined : smallCategory_item && smallCategory_item.desc || undefined
            })(
              <TextArea rows={4} style={{ resize: 'none' }} placeholder='请描述'/>
            )}
          </Form.Item>
        </Form>
      </Modal>
    )
  };

  handleSmallListUpdButton = (id) => {
    const { dispatch } = this.props;
    this.setState({ smallAddOrUpdModalVisible: true, smallAddOrUpd: false, updSmallId: id });
    dispatch({
      type: 'category/fetch_getSmallCategoryItem',
      payload: {
        id
      },
      callback: res => {
       if (res.status===0) {
         if (res.value.workType === 'URGENT'){
           this.setState({
             unitId: res.value.unit.id && res.value.unit.id,
             workType: res.value.workType,
             timeType: this.handleTimeTypeText(res.value.timeType)
           })
         }else {
           this.setState({
             workType: res.value.workType,
             timeType: this.handleTimeTypeText(res.value.timeType)
           })
         }
       }
      }
    })
  };

  setOnRef = (ref) => {
    this.onRef = ref
  };

  handleClickReset = () => {
    this.onRef.addClick()
  };

  render() {
    const { category: { bigCategory_list, smallCategory_list } } = this.props;
    const { customizeMenusVisible, node, pageParams } = this.state;
    const columns = [
      {
        title: '案卷小类',
        dataIndex: 'name',
        key: 'name',
        width: 110,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '职能部门',
        dataIndex: 'unitName',
        key: 'unitName',
        width: 120,
        render: val => <Ellipsis lines={1}>{val[0]}</Ellipsis>
      },
      {
        title: '紧急程度',
        dataIndex: 'workType',
        key: 'workType',
        width: 120,
        render: val => <Ellipsis lines={1}><span style={returnWorkTypeStyle(val)}>{returnWorkTypeText(val)}</span></Ellipsis>,
      },
      {
        title: '创建人',
        dataIndex: 'creator',
        key: 'creator',
        width: 110,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '创建时间',
        dataIndex: 'createTime',
        key: 'createTime',
        width: 170,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '操作',
        dataIndex: 'id',
        key: 'id',
        width: 100,
        render: (text, record) => (
          <Button
            type='primary'
            onClick={() => this.handleSmallListUpdButton(text)}
          >
            修改
          </Button>
        ),
      }
    ];
    return (
      <Fragment>
        <Row style={{marginTop: 10}}>
          <Col span={4} className={styles.TreeMenuLeft}>
            <Tree
              showIcon
              switcherIcon={<Icon type="dd" />}
              onRightClick={this.handleTreeRightClick}
              onSelect={this.handleTreeClick}
              style={{ height: '95%'}}
            >
              {this.renderTreeNodes(bigCategory_list)}
            </Tree>
            <Button
              size='large'
              className={styles.TreeMenuButton}
              onClick={() => this.setState({ customizeMenusAddOrUpdModalVisible: true, customizeMenusAddOrUpd: true })}
            >
              新建目录
            </Button>
            {/* 模态窗口 */}
            { customizeMenusVisible && this.customizeMenus()}
            {this.state.customizeMenusAddOrUpdModalVisible && this.customizeMenusAddOrUpdModal()}
            {this.state.smallAddOrUpdModalVisible && this.smallAddOrUpdModal()}
          </Col>
          <div style={{ backgroundColor: "#fff" }}>
          <Col span={18} style={{ marginLeft: '0.5%', width: "81.166%", minHeight: "91vh", backgroundColor: "#fff", borderRadius: 4}} className={styles.menuTableRight}>
            <Spin
              spinning={this.props.cateLoading}
            >
              <MenuTable
                columns={columns}
                dataSource={smallCategory_list}
                type='category/fetch_getSmallCategoryList'
                delButton='category/fetch_delSmallCategoryItem'
                fatherBigListItem={node && node}
                smallPaginationParams = {pageParams && pageParams}
                onRef={this.setOnRef}
              />
            </Spin>
          </Col>
          </div>
        </Row>
      </Fragment>
    );
  }
}
export default index;
