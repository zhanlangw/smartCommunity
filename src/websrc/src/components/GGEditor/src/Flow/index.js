import React, { PureComponent, Fragment } from 'react';
import { Row, Col, Button, Drawer, Form, Input, Select, Radio, Typography, InputNumber, Modal } from 'antd';
import GGEditor, { Flow, withPropsAPI } from 'gg-editor';
import { connect } from 'dva';
import { withRouter } from 'react-router-dom';
import { FlowContextMenu } from '../components/EditorContextMenu';
import { FlowToolbar } from '../components/EditorToolbar';
import { FlowItemPanel } from '../components/EditorItemPanel';
import { FlowDetailPanel } from '../components/EditorDetailPanel';
import styles from './index.less';
import TreeSelect from '@/components/TreeSelect';
import request from '@/utils/request';
import {token, validator} from "@/utils/utils";

const { Option } = Select;
const { Item } = Form;
const { Text } = Typography;
const { TextArea } = Input;
const formItemLayout = {
  labelCol: {
    xl: { span: 6 },
    xxl: { span: 6 },
  },
  wrapperCol: {
    xl: { span: 18 },
    xxl: { span: 18 },
  },
};
const inlineFormItemLayout = {
  labelCol: {
    sm: { span: 8 },
  },
  wrapperCol: {
    sm: { span: 13 },
  },
};
const newformItemLayout = {
  labelCol: {
    sm: { span: 8 },
  },
  wrapperCol: {
    sm: { span: 10 },
  },
};
const radioItemLayout = {
  labelCol: {
    sm: { span: 8 },
  },
  wrapperCol: {
    sm: { span: 16 },
  },
};

@withPropsAPI
@withRouter
@Form.create()
@connect(({ Process, organization }) => ({
  Process,
  organization
}))
class FlowPage extends PureComponent {
  state = {
    node: null,
    detailType: 'node',
    visible: false,
    nodeItem: null,
    showRangeModal: false,
    RadioValue: null,
    checkedPreKeys: [],
  };

  componentDidMount () {
    const { dispatch, match: { params: { id } } } = this.props;
    // 获取Tree树形结构数据
    dispatch({
      type: 'global/fetch_get_tree',
    })
    // })
  }

  // 节点内容 与 抽屉详情 实现双向绑定的效果
  nodeHandleSubmit = e => {
    if (e && e.preventDefault) {
      e.preventDefault();
    }

    const { form, propsAPI, Process: { process_link_item } } = this.props;
    const { getSelected, executeCommand, update } = this.flow.propsAPI;
    setTimeout(() => {
      form.validateFieldsAndScroll((err, values) => {
        if (err) {
          return;
        }
        executeCommand(() => {
          update(this.state.nodeItem, {
            ...values,
          });
        });
      });
    }, 0);
  };

  edgeHandleSubmit = e => {
    if (e && e.preventDefault) {
      e.preventDefault();
    }

    const { form, propsAPI, Process: { process_link_item } } = this.props;
    const { getSelected, executeCommand, update } = this.flow.propsAPI;
    setTimeout(() => {
      form.validateFieldsAndScroll((err, values) => {
        if (err) {
          return;
        }
        executeCommand(() => {
          update(this.state.nodeItem, {
            ...values,
          });
        });
      });
    }, 0);
  };

  //附件控制
  handleSizeChange = e => {
    this.setState({ size: e.target.value });
  };

  // 办理人范围 点击设置时模态窗口
  closeRangeModal = (e) => {
    const { showRangeModal } = this.state;
    this.setState({ showRangeModal: !showRangeModal });
  };

  // 办理人范围中 紧急程度
  handleRadioChange = e => {
    this.setState({
      RadioValue: e.target.value
    })
  };

  // 树形结构
  renderTreeNodes = (data) => {
    const { type } = this.props;
    return data.map((item) => {
      // let disabled = true;
      // for (let index = 0; index < type.length; index++) {
      //   const e = type[index];
      //   if (item.type === e) {
      //     disabled = false;
      //     console.log(type)
      //   }
      // }
      if (item.children) {
        return (
          <TreeNode value={item.key} title={item.title} key={item.key} dataRef={item}>
            {this.renderTreeNodes(item.children)}
          </TreeNode>
        );
      }
      return <TreeNode value={item.key} {...item} dataRef={item} />;
    });
  };

  nodeDetailType = (type) => {
    switch(type) {
         case 'ROOT':
           return '管理员';
         case 'WORKER':
           return '工作人员';
         default:
            return '部门'
        }
  };

  // 点击环节
  renderNodeDetail = (item) => {
    const { form } = this.props;
    const { showRangeModal, RadioValue, handlerType, process_link_item } = this.state;
    const { organization: { unit_tree } } = this.props;
    return (
      <Form {...inlineFormItemLayout}>
        <Item label="测试" style={{ display: 'none' }}>
          {form.getFieldDecorator('webId', {
            initialValue: process_link_item && process_link_item.webId,
          })(<Input />)}
        </Item>
        <Item label="环节名称">
          {form.getFieldDecorator('label', {
            rules: [
              {
                validator:validator,
              }
            ],
            initialValue: process_link_item && process_link_item.name,
          })(<Input onBlur={this.nodeHandleSubmit} />)}
        </Item>
        <Item label="环节属性">
          {form.getFieldDecorator('attribute', {
            initialValue: process_link_item && process_link_item.attribute
          })(
            <Select
              //mode="multiple"
              style={{ width: '100%' }}
              placeholder="请选择"
            // onChange={handleChange}
            // optionLabelProp="label"
            >
              <Option key="MASTER">主线</Option>
              <Option key="SLAVE">支线</Option>
            </Select>,
          )}
        </Item>
        <Item label="环节类型" style={{ display: 'none'}}>
          {form.getFieldDecorator('type', {
            initialValue: process_link_item && process_link_item.type
          })(
            <Select
              //mode="multiple"
              style={{ width: '100%' }}
              placeholder="请选择"
              // onChange={handleChange}
              // optionLabelProp="label"
            >
              <Option value="START" label="开始环节">开始环节 </Option>
              <Option value="ORDINARY" label="普通环节">普通环节 </Option>
            </Select>,
          )}
        </Item>
        <Item label="操作控制">
          {form.getFieldDecorator('buttons', {
            initialValue: process_link_item && process_link_item.buttons
          })(
            <Select
              mode="multiple"
              style={{ width: '100%' }}
              placeholder="请选择"
            // onChange={handleChange}
            // optionLabelProp="label"
            >
              <Option value="SUBMIT" label="提交">提交 </Option>
              <Option value="WITHDRAW" label="撤回">撤回 </Option>
              <Option value="SPECIAL_DELIVERY" label="特送">特送 </Option>
              <Option value="END" label="结束">结束 </Option>
            </Select>,
          )}
        </Item>
        <Item label="附件控制">
          {form.getFieldDecorator('uploadFlag', {
            initialValue: process_link_item && process_link_item.uploadFlag
          })(
            <Radio.Group onChange={this.handleSizeChange} buttonStyle="solid">
              <Radio.Button value={true} >是</Radio.Button>
              <Radio.Button value={false} >否</Radio.Button>
            </Radio.Group>,
          )}
        </Item>
        <Item label="办理人范围" {...newformItemLayout} >
          {form.getFieldDecorator('handleRange', {
            initialValue: RadioValue ?  this.nodeDetailType(RadioValue) :  process_link_item && this.nodeDetailType(process_link_item.handlerType)
          })(
              <Input disabled />
          )}
          <Button onClick={this.closeRangeModal}>设置</Button>
        </Item>
        <Item label="备注" {...inlineFormItemLayout} >
          {form.getFieldDecorator('desc', {
            initialValue: process_link_item && process_link_item.desc
          })(
            <TextArea rows={4} style={{ resize: 'none' }} />,
          )}
        </Item>
        <Row
          style={{
            position: 'absolute',
            bottom: 0,
            width: '100%',
            borderTop: '1px solid #e8e8e8',
            padding: '10px 16px',
            textAlign: 'right',
            left: 0,
            background: '#fff',
            borderRadius: '0 0 4px 4px',
          }}
        >
          <Button onClick={this.onClose}>取消</Button>
          <Button
            type="primary"
            onClick={this.handleRenderNodeDetail}
            style={{
              marginLeft: 30
            }}
          >
            提交
          </Button>
        </Row>


        {/* ------------------------------------------------model分割线------------------------------------------------------- */}
        <Modal
          centered={true}
          visible={showRangeModal}
          footer={null}
          title="办理人范围设置"
          onCancel={this.closeRangeModal}
        >
          <div style={{ padding: 24 }}>
            <Item
              {...radioItemLayout}
              label='紧急程度'
              style={{ marginBottom: 8 }}
            >
              {form.getFieldDecorator('handlerType', {
                initialValue: process_link_item && process_link_item.handlerType
                // rules: [{ required: true, message: '意见格式不能为空' }]
              })
                (
                  <Radio.Group onChange={this.handleRadioChange}>
                    <Radio value="WORKER">工作人员</Radio>
                    <Radio value="ROOT">管理员</Radio>
                    <Radio value="CUSTOMIZE">部门</Radio>
                  </Radio.Group>
                )}
            </Item>
            {
              RadioValue && RadioValue === "CUSTOMIZE" || process_link_item && process_link_item.handlerType === "CUSTOMIZE"?  (
                <Item
                  {...formItemLayout}
                  label='职能部门'
                  style={{ marginBottom: 8 }}
                >
                  {form.getFieldDecorator('unitId', {
                    initialValue:  process_link_item && process_link_item.unit ? process_link_item.unit.id : undefined
                  })(
                    <TreeSelect />
                  )}
                </Item>
              )
              : ""
            }
          </div>
          <div style={{ textAlign: 'center', paddingBottom: 24 }}>
            <Button
              type="primary"
              onClick={this.closeRangeModal}
            >
              确认
            </Button>
            <Button onClick={this.closeRangeModal} style={{ marginLeft: 16 }}>关闭</Button>
          </div>
        </Modal>
        {/* ------------------------------------------------model分割线------------------------------------------------------- */}
      </Form>
    );
  };

  // 环节提交
  handleRenderNodeDetail = () => {
    const { form: { validateFields, resetFields }, dispatch, match: { params: { id } }, Process: { web_id_map } } = this.props;
    const style = JSON.stringify(this.getSave());
    validateFields((err, value) => {
      if (!err) {
        const payload = {
          ...value,
          processId: id,
          name: value.label,
          id: web_id_map[value.webId]
        };
        dispatch({
          type: 'Process/fetch_updProcessLinkItem',
          payload,
          params: {
            id,
            style,
          }
        });
        this.setState({ visible: false });
        resetFields();
      }
    })
  };

  // 路径提交
  handleRenderEdgeDetail = () => {
    const { form: { validateFields, resetFields }, dispatch, match: { params: { id } }, Process: { web_id_map } } = this.props;
    const style = JSON.stringify(this.getSave());
    validateFields((err, value) => {
      if (!err) {
        const payload = {
          ...value,
          processId: id,
          name: value.label,
          id: web_id_map[value.webId]
        };
        dispatch({
          type: 'Process/fetch_updProcessLinkPath',
          payload,
          params: {
            id,
            style,
          }
        });
        this.setState({ visible: false });
        resetFields();
      }
    })
  };

  // 点击线
  renderEdgeDetail = (item) => {
    const { form } = this.props;
    const { process_path_item } = this.state;
    return (
      <Form {...inlineFormItemLayout}>
        <Item label="webId" style={{ display: "none" }}>
          {form.getFieldDecorator('webId', {
            initialValue: process_path_item && process_path_item.webId
          })(<Input />)}
        </Item>
        <Item label="startNodeId" style={{ display: "none" }}>
          {form.getFieldDecorator('startNodeId', {
            initialValue: process_path_item && process_path_item.startNodeId
          })(<Input />)}
        </Item>
        <Item label="endNodeId" style={{ display: "none" }}>
          {form.getFieldDecorator('endNodeId', {
            initialValue: process_path_item && process_path_item.endNodeId
          })(<Input />)}
        </Item>
        <Item label="路径名称">
          {form.getFieldDecorator('label', {
            rules: [
              {
                validator:validator,
              }
            ],
            initialValue: process_path_item && process_path_item.name
          })(<Input onBlur={this.edgeHandleSubmit} />)}
        </Item>
        <Item label="路径类型">
          {form.getFieldDecorator('type', {
            initialValue: process_path_item && process_path_item.type
          })(
            <Select onChange={this.handleSubmit}>
              <Option value="URGENT">紧急</Option>
              <Option value="ORDINARY">普通</Option>
            </Select>,
          )}
        </Item>
        <Item label="路径属性">
          {form.getFieldDecorator('attribute', {
            initialValue: process_path_item && process_path_item.attribute
          })(
            <Select onChange={this.handleSubmit}>
              <Option value="DELAY">申请延时</Option>
              <Option value="TODO">待处置</Option>
              <Option value="RETURN">退件</Option>
              <Option value="REVIEW">待审核</Option>
              <Option value="ADDUSER">增加办理人</Option>
              <Option value="AGREE">同意</Option>
              <Option value="REFUSE">拒绝</Option>
              <Option value="RETURN_REFUSE">退件拒绝</Option>
            </Select>,
          )}
        </Item>
        <Item label="备注" >
          {form.getFieldDecorator('desc', {
            initialValue: process_path_item && process_path_item.desc
          })(
            <TextArea rows={4} style={{ resize: 'none' }} />,
          )}
        </Item>
        <Row
          style={{
            position: 'absolute',
            bottom: 0,
            width: '100%',
            borderTop: '1px solid #e8e8e8',
            padding: '10px 16px',
            textAlign: 'right',
            left: 0,
            background: '#fff',
            borderRadius: '0 0 4px 4px',
          }}
        >
            <Button
              type="primary"
              onClick={this.handleRenderEdgeDetail}
              style={{
                marginRight: 30
              }}
            >
              提交
            </Button>
            <Button onClick={this.onClose}>取消</Button>
        </Row>
      </Form>
    );
  };

  // 关闭详情抽屉
  onClose = () => {
    this.setState({
      visible: false
    })
  };

  // 获取流程图模板样式
  getSave = () => {
    const { save } = this.flow.propsAPI;
    return save();
  };

  // 流程图模板点击节点时触发
  flowNodeClick = e => {
    const { Process: { web_id_map }, dispatch } = this.props;
    return request(`/api/node/item`, { params: { id: web_id_map[e.item.model.id] }})
      .then(res => {
        this.setState({
          detailType: e.item.type,
          visible: true,
          nodeItem: e.item,
          process_link_item: res.value,
        })
      });
  };

  // 流程图点击线时触发
  flowEdgeClick = e => {
    const { Process: { web_id_map }, dispatch } = this.props;
    return request(`/api/path/item`, { params: { id: web_id_map[e.item.model.id] }, headers:{ 'Authorization': token}})
      .then(res => {
        this.setState({
          detailType: e.item.type,
          visible: true,
          nodeItem: e.item,
          process_path_item: res.value,
        })
      });
  };

  // 操作流程图模板指令时触发
  flowAfterChange = e => {
    const { dispatch, match: { params: { id } }, Process: { web_id_map } } = this.props;
    const style = JSON.stringify(this.getSave());
    switch (e.action) {
      case "add":
        // 添加环节
        if (e.item.type === 'node') {
          const { label, shape } = e.model;
          let newShape = null;
          switch (shape) {
            case 'flow-circle':
              newShape = 'START';
              break;
            case 'flow-capsule':
              newShape = 'END';
              break;
            default:
              newShape = 'ORDINARY';
              break;
          }
          const payload = {
            name: label,
            webId: e.model.id,
            uploadFlag: true,
            handlerType: 'WORKER',
            attribute:'MASTER',
            unitIds: [],
            processId: id,
            type: newShape,
            buttons: ['SUBMIT'],
          };
          dispatch({
            type: 'Process/fetch_addProcessLink',
            payload,
            params: {
              style,
              id,
            }
          })
        } else if (e.item.type === 'edge') {
          const { model: { source, target } } = e;
          const payload = {
            name: "test",
            startNodeId: web_id_map[source],
            endNodeId: web_id_map[target],
            type: "ORDINARY",
            processId: id,
            desc: null,
            webId: e.item.id,
            attribute: "TODO",
          };
          dispatch({
            type: 'Process/fetch_addProcessPath',
            payload,
            params: {
              style,
              id,
            }
          })
        }
        break;
      case "remove":
        if (e.item.type === 'node') {
          this.onClose();
          dispatch({
            type: 'Process/fetch_delProcessLink',
            payload: {
              ids: web_id_map[e.item.id]
            },
            params: {
              style,
              id
            }
          })
        } else if (e.item.type === 'edge') {
          this.onClose();
          const { Process: { web_id_map } } = this.props;
          dispatch({
            type: 'Process/fetch_delProcessPath',
            payload: {
              ids: web_id_map[e.item.id]
            },
            params: {
              style,
              id
            }
          })
        }
        break;
      case "update":
        if (e.item.type === "node") {
          dispatch({
            type: 'Process/fetch_ProcessChart',
            payload: {
              id,
              style
            }
          })
        } else if (e.item.type === "edge") {
          dispatch({
            type: 'Process/fetch_ProcessChart',
            payload: {
              id,
              style
            }
          })
        }
        break;
      default:
        break;
    }
  };

  reUpperFirst = () => {
    const { detailType } = this.state;
    switch (detailType) {
      case 'node':
        return '流程信息设置';
      case 'edge':
        return '路径配置';
      case 'group':
        return '分组配置';
      default:
    }
  };

  render () {
    const { data } = this.props;
    const { visible, detailType } = this.state;
    return (
      <Fragment>
        <GGEditor className={styles.editor} ref={el => { this.flow = el; }}>
          <Row type="flex" className={styles.editorHd}>
            <Col span={24}>
              <FlowToolbar />
            </Col>
          </Row>
          <Row type="flex" className={styles.editorBd}>
            <Col span={4} className={styles.editorSidebar}>
              <FlowItemPanel />
            </Col>
            <Col span={20} className={styles.editorContent}>
              <Flow
                style={{
                  width: '100%',
                  height: '100%',
                }}
                data={data}
                onClick={() => { return false }}
                className={styles.flow}
                onNodeClick={this.flowNodeClick}
                onEdgeClick={this.flowEdgeClick}
                onDragEnd={this.flowNodeDragEnd}
                onAfterChange={this.flowAfterChange}
                graph={{ edgeDefaultShape: 'flow-polyline-round' }}
              />
            </Col>
          </Row>
          <FlowContextMenu />
        </GGEditor>
        <Drawer
          title={this.reUpperFirst()}
          placement="right"
          width={400}
          mask={false}
          onClose={this.onClose}
          visible={visible}
        >
          {detailType === 'node' && this.renderNodeDetail()}
          {detailType === 'edge' && this.renderEdgeDetail()}
          {/*{detailType === 'group' && this.renderGroupDetail()}*/}
        </Drawer>
      </Fragment>
    );
  }
}

export default withPropsAPI(FlowPage);
