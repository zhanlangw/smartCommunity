import React, { Fragment, PureComponent } from 'react';
import { connect } from 'dva';
import { Card, Form, Input, Select, Radio, Typography, Col, Row, InputNumber, Modal, TreeSelect, Button } from 'antd';
import { withPropsAPI } from 'gg-editor';
import upperFirst from 'lodash/upperFirst';
import { withRouter } from 'react-router-dom'
// import TreeSelect from '@/components/TreeSelect';
// import UnitTreeSelect from '@/components/UnitTreeSelect';
import styles from './index.less';

const { TreeNode } = TreeSelect;
const { Item } = Form;
const { Option } = Select;
const { Text } = Typography;
const { TextArea } = Input;

const inlineFormItemLayout = {
  labelCol: {
    sm: { span: 8 },
  },
  wrapperCol: {
    sm: { span: 13 },
  },
};

const formItemLayout = {
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
}

@withRouter
@connect(({ organization, Process }) => ({
  organization,
  Process
}))
class DetailForm extends PureComponent {
  state = {
    showRangeModal: false,
    formatRange: null,
    RadioValue: "WORKER",
  }

  get item() {
    const { propsAPI } = this.props;

    return propsAPI.getSelected()[0];
  }

  componentDidMount() {
    const { dispatch, match: {params} } = this.props;
   // const { id } = this.item.getModel()
    dispatch({
      type: 'organization/fetch_getOrganizationTree_action',
      payload: {
        id: null
      },
    })

    // dispatch({
    //   type: 'Process/fetch_getProcessLinkItem',
    //   payload: {
    //     id: web_id_map[e.item.model.id],
    //   }
    // })

  }

  handleSubmit = e => {
    if (e && e.preventDefault) {
      e.preventDefault();
    }

    const { form, propsAPI } = this.props;
    const { getSelected, executeCommand, update } = propsAPI;

    setTimeout(() => {
      form.validateFieldsAndScroll((err, values) => {
        if (err) {
          return;
        }

        const item = getSelected()[0];
        if (!item) {
          return;
        }

        executeCommand(() => {
          console.log(values)
          update(item, {
            ...values,
          });
        });
      });
    }, 0);
  };

  // 改变线的形状
  // renderEdgeShapeSelect = () => (
  //     <Select onChange={this.handleSubmit}>
  //       <Option value="flow-smooth">折线</Option>
  //       <Option value="flow-polyline">Polyline</Option>
  //       <Option value="flow-polyline-round">Polyline Round</Option>
  //     </Select>
  // );
  //附件控制
  handleSizeChange = e => {
    this.setState({ size: e.target.value });
  };

  // 办理人范围 点击设置时模态窗口
  closeRangeModal = (e) => {
    const { showRangeModal } = this.state;
    this.setState({ showRangeModal: !showRangeModal });
  }

  // 办理人范围中 紧急程度
  handleRadioChange = e => {
    this.setState({
      RadioValue: e.target.value
    })
  }

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
          <TreeNode  value={item.key} title={item.title} key={item.key} dataRef={item}>
            {this.renderTreeNodes(item.children)}
          </TreeNode>
        );
      }
      return <TreeNode  value={item.key} {...item} dataRef={item} />;
    });
  }

  // TreeSelectChange
  handleTreeSelectChange = (value, label, extra) =>{
    let payload = {
      name: label.join(','),
      id: value.join(','),
    }
    this.setState({
      unitIds: this.state.unitIds.concat(payload)
    })
  }

  getSave = () => {
    const { save } = this.props.propsAPI;
    return save();
  }

  // 节点提交
  handleRenderNodeDetail = (e) => {
    const { form: { validateFields }, dispatch, match: {params}, propsAPI } = this.props;
    const { unitIds } = this.state;
   // const { id } = this.item.getModel()
    const save = JSON.stringify(this.getSave());
    console.log(save);
    validateFields((err, value) => {
      if(!err){
        const payload = {
          ...value,
          processId: params.id,
          name: value.label,
          webId: id,
        }

        dispatch({
          type: 'Process/fetch_addProcessLink',
          payload,
          params: {
            id: params.id,
            style: save,
          }
        })
      }
    })
  }

  // 点击节点
  renderNodeDetail = () => {
    const { form, propsAPI, organization: { unit_tree }, Process: { process_link_item, web_id_map } } = this.props;
    const { showRangeModal, RadioValue, handlerType } = this.state;
    //const { label } = this.item.getModel();
    let items = null;
    // this.props.dispatch({
    //   type: 'Process/fetch_getProcessLinkItem',
    //   payload: {
    //     id: web_id_map[this.item.getModel().id]
    //   },
    //   callback: (res) => {
    //     items = res
    //   }
    // })
      return (
        <Form {...inlineFormItemLayout}>
          <Item label="环节名称">
            {form.getFieldDecorator('label', {
              initialValue: items&&items.name,
            })(<Input onChange={this.handleSubmit} />)}
          </Item>
          <Item label="环节类型">
            {form.getFieldDecorator('type', {
              initialValue: items&&items.type
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
              initialValue: items && items.buttons
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
              initialValue: items && items.uploadFlag
            })(
              <Radio.Group onChange={this.handleSizeChange} buttonStyle="solid">
                <Radio.Button value={true} >是</Radio.Button>
                <Radio.Button value={false} >否</Radio.Button>
              </Radio.Group>,
            )}
          </Item>
          <Row>
            <Col offset={3} span={15}>
              <Item label="办理人范围" {...formItemLayout} >
                {form.getFieldDecorator('handleRange', {
                  initialValue: RadioValue
                })(
                  <Input disabled />
                )}
              </Item>
            </Col>
            <Col span={1}>
              <Button onClick={this.closeRangeModal}>设置</Button>
            </Col>
          </Row>

          <Item label="备注" {...inlineFormItemLayout} >
            {form.getFieldDecorator('desc', {
              initialValue: items && items.desc
            })(
              <TextArea rows={4} style={{ resize: 'none' }} />,
            )}
          </Item>
          <Row>
            <Button type="primary" onClick={this.handleRenderNodeDetail}>提交</Button>
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
                  initialValue: RadioValue
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
                RadioValue === "CUSTOMIZE" && (
                  <Item
                    {...formItemLayout}
                    label='职能部门'
                    style={{ marginBottom: 8 }}
                  >
                    {form.getFieldDecorator('unitIds', {
                      //initialValue: 'jack'
                    })(
                      <TreeSelect
                        style={{ width: '100%' }}
                        //value={this.state.value}
                        dropdownStyle={{ maxHeight: 400, overflow: 'auto' }}
                        placeholder="请选择"
                        multiple={true}
                        treeCheckable={false}
                        //treeDefaultExpandAll
                        onChange={this.handleTreeSelectChange}
                      >
                        {this.renderTreeNodes(unit_tree)}
                      </TreeSelect>
                    )}
                  </Item>
                )
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


  handleRenderEdgeDetail = () => {
    const { form: { validateFields }, dispatch, match: { params: { id } }, propsAPI } = this.props;
    validateFields((err, value) => {
      if(!err) {
        console.log(value);
      }
    })
  }

  // 点击线
  renderEdgeDetail = () => {
    const { form } = this.props;
   // const { label = '', shape = 'flow-smooth' } = this.item.getModel();

    return (
      <Form {...inlineFormItemLayout}>
        <Item label="路径名称">
          {form.getFieldDecorator('label', {
          })(<Input onBlur={this.handleSubmit} />)}
        </Item>
        <Item label="路径类型">
          {form.getFieldDecorator('type', {
          })(
             <Select onChange={this.handleSubmit}>
                <Option value="URGENT">紧急</Option>
                <Option value="ORDINARY">普通</Option>
             </Select>,
          )}
        </Item>
        <Item label="路径类型">
          {form.getFieldDecorator('attribute', {
          })(
             <Select onChange={this.handleSubmit}>
              <Option value="DELAY">申请延时</Option>
              <Option value="TODO">待处置</Option>
              <Option value="RETURN">退件</Option>
              <Option value="REVIEW">待审核</Option>
              <Option value="ADDUSER">增加办理人</Option>
              <Option value="AGREE">同意</Option>
              <Option value="REFUSE">拒绝</Option>
             </Select>,
          )}
        </Item>
        <Item label="排序编号">
          {form.getFieldDecorator('sorting', {
          })(
            <InputNumber min={1} />
          )}
        </Item>
        <Item label="备注" >
          {form.getFieldDecorator('desc', {
          })(
            <TextArea rows={4} style={{ resize: 'none' }} />,
          )}
        </Item>
        <Button type="primary" onClick={this.handleRenderEdgeDetail}>提交</Button>
      </Form>
    );
  };

  // 点击分组配置
  renderGroupDetail = () => {
    const { form } = this.props;
   // const { label = '新建分组' } = this.item.getModel();

    return (
      <Item label="Label" {...inlineFormItemLayout}>
        {form.getFieldDecorator('label', {
          initialValue: label,
        })(<Input onBlur={this.handleSubmit} />)}
      </Item>
    );
  };

  reUpperFirst = type => {
    switch (type) {
      case 'Node':
        return '流程信息设置';
      case 'Edge':
        return '路径配置';
      case 'Group':
        return '分组配置';
      default:
    };
  };

  render() {
    const { type, item } = this.props;
    if (!this.item) {
      return null;
    }
    return (
      <Card type="inner" size="small" title={type} bordered={false}>
        <Form onSubmit={this.handleSubmit}>
          {type === 'node' && this.renderNodeDetail()}
          {type === 'edge' && this.renderEdgeDetail()}
          {type === 'group' && this.renderGroupDetail()}
        </Form>
      </Card>
    );
  }
}

export default Form.create()(withPropsAPI(DetailForm));
