/* eslint-disable @typescript-eslint/camelcase */
import React, { PureComponent, Fragment } from 'react';
import { Button, Input, Modal, Form, Spin  } from 'antd';
import { connect } from 'dva';
import MenuTable from '@/components/MenuTable/List';
import { payload_0_10 as payload } from '@/utils/utils';

const { TextArea } = Input;
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
@connect(({ Process, loading }) => ({
  Process,
  loading: loading.models.Process,
}))
class List extends PureComponent {
  state={
    visible: false,
  };

  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: 'Process/fetch_getProcessList',
      payload: payload,
    });
  };

  updateSubmit = (id) => {
    const { dispatch, form: {validateFields} } = this.props;
    validateFields((err, payload) => {
      if (!err) {
        payload.id = this.state.id;
        dispatch({
          type: 'Process/fetch_updProcessItem',
          payload,
          callback: res => {
            dispatch({
              type: 'Process/fetch_getProcessList',
              payload: {
                start: 0,
                count: 10
              },
              callback: res => {
                this.setState({
                  visible: false
                }, () => {this.onRef.setState({
                  start: 1
                })})
              }
            });
          }
        });
      }
    });
  };

  handleVisible = e => {
    this.setState({
      visible: !this.state.visible,
    });
  };

  setOnRef = (ref) => {
    this.onRef = ref
  };

  render() {
    const { Process: { process_list, process_chart }, form:{getFieldDecorator} } = this.props;
    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '备注',
        dataIndex: 'desc',
        key: 'desc',
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
        render: (text, record) => {
          return (
            <Button
              type='primary'
              onClick={(e) => {
                e.stopPropagation();
                this.props.dispatch({
                  type: 'Process/fetch_getProcessChart',
                  payload: {
                    id: text
                  },
                });
                this.setState({
                  id: text,
                  visible: true
                });
              }}
            >
              修改
            </Button>
          )
        }
      }
    ];

    return (
      <div style={{backgroundColor:" #ffffff", minHeight: "91vh"}}>
        <Spin
          spinning={this.props.loading}
        >
          <MenuTable
            columns={columns} // table header
            dataSource={process_list} // table 数据源
            type="Process/fetch_getProcessList" // table 要请求的接口
            delButton="Process/fetch_delProcessItem" // 删除接口
            addButton="Process/fetch_addProcessItem"// 新增接口
            addButtonText="新建模板"
            onRef={this.setOnRef}
            // filter //是否需要筛选框
          />
        </Spin>
       {this.state.visible && <Modal
          title="修改流程模板"
          centered
          visible={this.state.visible}
          onCancel={this.handleVisible}
          footer={[
            <Button key="submit" type="primary" onClick={this.updateSubmit} style={{marginRight: 15}}>
              确定
            </Button>,
            <Button key="back" onClick={this.handleVisible}>
              取消
            </Button>
          ]}
        >
          <Form.Item label="流程名称" {...formItemLayout}>
            {getFieldDecorator('name', {
              rules: [{ required: true, message: 'Please input your name!' }],
              initialValue: process_chart && process_chart.name
            })(
              <Input/>,
            )}
          </Form.Item>
          <Form.Item label="备注" {...formItemLayout}>
            {getFieldDecorator('desc', {
              initialValue: process_chart && process_chart.desc,
            })(
              <TextArea rows={4} style={{ resize: 'none' }} />,
            )}
          </Form.Item>
        </Modal>}
      </div>
    );
  }
}
export default List;
