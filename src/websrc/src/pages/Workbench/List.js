/* eslint-disable prefer-const */
/* eslint-disable max-len */
/* eslint-disable no-plusplus */
/* eslint-disable jsx-a11y/alt-text */
/* eslint-disable @typescript-eslint/camelcase */
/* eslint-disable no-var */
import React, { Component, Fragment } from 'react';
import {
  Icon,
  Table,
  Button,
  Input,
  Typography,
  Row,
  ConfigProvider,
  Modal,
  Form,
  Tree,
  Card,
  Spin,
  Col,
  Select,
  Tooltip,
  Carousel,
  message,
  Upload,
  Progress,
  Badge,
} from 'antd';
import { connect } from 'dva';
import Ellipsis from '@/components/Ellipsis';
import style from './workbench.less';
import {
  beforeUpload,
  payload_0_10 as pagePayload,
  returnSourceText,
  returnTimeTypeText,
  validator
} from "@/utils/utils";
import { getUrl, returnWorkTypeText, returnWorkTypeStyle, token } from "@/utils/utils";
import Link from 'umi/link';
import request from "@/utils/request";
import { stringify } from 'qs';
import file_del from "@/assets/file_del.png";
import forImage from "@/assets/hasImage.png";
import QueueAnim from "rc-queue-anim";
import forVideo from "@/assets/hasVideo.png";
import nullStart from '@/assets/null.png';
import noArrowsLeft from "@/assets/noLeft.png";
import arrowsLeft from "@/assets/left.png";
import towLeft from "@/assets/left2.png";
import towRight from "@/assets/right2.png";
import noArrowsRight from "@/assets/noRight.png";
import arrowsRight from "@/assets/right.png";
import del from "@/assets/del.png";
import workbench_left from '@/assets/workbench_left.png';
import workbench_right from '@/assets/workbench_right.png';
import { getAuthority } from '@/utils/authority';

const { Text, Paragraph } = Typography;
const { TreeNode } = Tree;
const { Option } = Select;
const { TextArea } = Input;

const smallDefaultPageSize_5 = {
  defaultPageSize: 5,
  size: 'small',
};

const smallDefaultPageSize_8 = {
  defaultPageSize: 8,
  size: 'small',
};

const formCarouselLayout = {
  labelCol: {
    xl: { span: 5 },
    xxl: { span: 5 },
  },
  wrapperCol: {
    xl: { span: 18 },
    xxl: { span: 18 },
  },
};
const formItemLayout = {
  labelCol: {
    xs: { span: 5 },
    sm: { span: 5 },
  },
  wrapperCol: {
    xs: { span: 17 },
    sm: { span: 17 },
  },
};
const customizeRenderEmpty = () => (
  <div style={{ textAlign: 'center' }}>
    <img src={nullStart} alt="" style={{ marginTop: 10 }} />
    <p style={{ color: "rgba(153,170,195,1)" }}>暂无数据</p>
  </div>
);


const columns = [
  {
    title: '标题',
    dataIndex: 'title',
    key: 'title',
    width: "30%",
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
    title: 'categoryName',
    dataIndex: 'categoryName',
    key: 'categoryName',
    width: 120,
    render: val => <Ellipsis lines={1}><Tooltip title={val}><span>{val}</span></Tooltip></Ellipsis>,
  },
  {
    title: 'workType',
    dataIndex: 'workType',
    key: 'workType',
    width: 60,
    render: val => <span style={returnWorkTypeStyle(val)}>{returnWorkTypeText(val)}</span>
  },
  {
    title: 'address',
    dataIndex: 'address',
    key: 'address',
    width: 150,
    render: val => <Ellipsis lines={1}><Tooltip title={val}><span>{val}</span></Tooltip></Ellipsis>,
  },
  {
    title: 'createTime',
    dataIndex: 'createTime',
    key: 'createTime',
    width: 170,
    render: val => <Ellipsis lines={1} style={{ textAlign: 'right', paddingRight: 10 }}><Tooltip title={val}><span>{val}</span></Tooltip></Ellipsis>,
  },
];
@Form.create()
@connect(({
  workbench,
  global,
  loading,
  loadingWork,
}) => ({
  global,
  workbench,
}))
class List extends Component {
  state = {
    length: null,
    arrayIndex: 0,
    fileItemModelVisible: false,
    selectedRowKeys: [], // <--- list列表中 多选参数
    selectedRows: [], // --->
    expandedPreKeys: null,  // <---树形结构参数
    checkedPreKeys: [],//--->
    image: [],
    video: [],
    itemType: null,
    time: {
      type: null
    },
    timeType: 'HOUR',
    imagePercent: [],
    videoPercent: [],
    workButton: [],
    previewModalVisible: false,

  };

  componentDidMount() {
    const { dispatch } = this.props;

    dispatch({
      type: 'global/fetch_get_unit'
    });
    dispatch({
      type: 'global/fetch_getFileBounceList',
      payload: {
        ...pagePayload
      },
    });
    dispatch({
      type: 'global/fetch_getFileDelayedList',
      payload: {
        ...pagePayload
      },
    });
    dispatch({
      type: 'global/fetch_getFileDisposalList',
      payload: {
        ...pagePayload
      },
    });
    dispatch({
      type: 'global/fetch_getFileInspectList',
      payload: {
        ...pagePayload
      },
    });
    dispatch({
      type: 'global/fetch_getFileEventList',
      payload: {
        start: 0,
        count: 1
      },
    })

    this.list_time = setInterval(() => {
      dispatch({
        type: 'global/fetch_get_unit'
      });
      dispatch({
        type: 'global/fetch_getFileBounceList',
        payload: {
          ...pagePayload
        },
      });
      dispatch({
        type: 'global/fetch_getFileDelayedList',
        payload: {
          ...pagePayload
        },
      });
      dispatch({
        type: 'global/fetch_getFileDisposalList',
        payload: {
          ...pagePayload
        },
      });
      dispatch({
        type: 'global/fetch_getFileInspectList',
        payload: {
          ...pagePayload
        },
      });
    }, 60000);

    this.event_time = setInterval(() => {
      dispatch({
        type: 'global/fetch_getFileEventList',
        payload: {
          start: 0,
          count: 1
        },
      })
    }, 30000);
  }

  // setWorkbenchList = () => {
  //   const { arrayIndex } = this.state;
  //   const { global: { event_list }, dispatch } = this.props;
  //   let payload = event_list && event_list.value[arrayIndex-1];
  //   dispatch({
  //     type: 'workbench/setWorkbench_list',
  //     payload,
  //   });
  //   this.setState({
  //     length: event_list && event_list.totalCount
  //   });
  // };

  previous = () => {
    const { global: { eventPage }, dispatch } = this.props;
    if (!eventPage.start <= 0) {
      dispatch({
        type: 'global/fetch_getFileEventList',
        payload: {
          start: eventPage.start - eventPage.count,
          count: eventPage.count
        },
      })
    }
  };

  next = () => {
    const { global: { eventPage, event_list }, dispatch } = this.props;
    if (eventPage.start < event_list.totalCount - eventPage.count) {
      dispatch({
        type: 'global/fetch_getFileEventList',
        payload: {
          start: eventPage.start + eventPage.count,
          count: eventPage.count
        },
      })
    }
  };

  // ----------------------------------- 案卷 -------------------------------------------------

  // table 点击事件
  handleOnRowClick = (id, type, itemType) => {
    const { dispatch } = this.props;
    dispatch({
      type: 'global/fetch_getFileItemData',
      payload: {
        id,
        type,
      },
      callback: res => {
        if (res.status === 0) {
          this.setState({
            image: res.value.afterImagePaths,
            video: res.value.afterVideoPaths
          })
        }
      }
    });
    this.setState({
      fileItemModelVisible: true,
      getFileLogId: id,
      getFileLogType: type,
      itemType: itemType || false,
    });
    this.modelLeftList(id, type)
  };

  // 上传图片
  fileImageChange = (info) => {
    const { file, event, fileList } = info;
    let imagePercent = [];
    if (file.response && file.response.status === 0) {
      fileList.map(item => {
        imagePercent.push(item.percent.toFixed(2))
      });
      this.setState({
        image: this.state.image.concat(file.response.value) || {},
        imagePercent
      });
    }
  };

  // 上传视频
  fileVideoChange = (info) => {
    const { file, event, fileList } = info;
    let videoPercent = [];
    if (file.response && file.response.status === 0) {
      fileList.map(item => {
        videoPercent.push(item.percent.toFixed(2))
      });
      this.setState({
        video: this.state.video.concat(file.response.value) || {},
        videoPercent,
      });
    }
  };

  // 删除图片
  imageDelUploadFile = (index) => {
    const { image } = this.state;
    let arr = image;
    arr.splice(index, 1);
    this.setState({
      image: arr
    })
  };

  // 删除视频
  videoDelUploadFile = (index) => {
    const { video } = this.state;
    let arr = video;
    arr.splice(index, 1);
    this.setState({
      video: arr
    })
  };

  // 点击删除
  IconRight_click = (id) => {
    const { dispatch } = this.props;
    const { getFileLogType } = this.state;
    const modal = Modal.confirm();
    modal.update({
      title: '删除提示',
      okText: '确定',
      cancelText: '返回',
      centered: true,
      content: (
        <Text> 是否确定要删除 </Text>
      ),
      onCancel: () => {
        modal.destroy();
      },
      onOk: () => {
        dispatch({
          type: 'global/fetch_delHasFiles',
          payload: {
            ids: id,
          },
          callback: (res) => {
            if (res.status === 0) {
              this.getListAll();
            }
          }
        });
        modal.destroy();
      }
    });
  };

  // 弹窗左边日志列表
  modelLeftList = (id, type) => {
    const { dispatch } = this.props;
    dispatch({
      type: 'global/fetch_getFileLogList',
      payload: {
        id,
        type,
      }
    })
  };

  // 特殊处理 获取全部列表(需要优化)
  getListAll = () => {
    const { dispatch } = this.props;
    this.setState({
      image: [],
      video: [],
      selectedRowKeys: [],
      workPathVisible: false,
      TimeDelayVisible: false,
      RETURN_OR_REVIEWVisible: false,
      unitTreeVisible: false,
      fileItemModelVisible: false,
    }, () => {
      this.props.form.resetFields();
    });
    dispatch({
      type: 'global/fetch_getFileBounceList',
      payload: {
        ...pagePayload
      },
    });
    dispatch({
      type: 'global/fetch_getFileDelayedList',
      payload: {
        ...pagePayload
      },
    });
    dispatch({
      type: 'global/fetch_getFileDisposalList',
      payload: {
        ...pagePayload
      },
    });
    dispatch({
      type: 'global/fetch_getFileHandleList',
      payload: {
        ...pagePayload
      },
    });
    dispatch({
      type: 'global/fetch_getFileInspectList',
      payload: {
        ...pagePayload
      },
    });
    dispatch({
      type: 'global/fetch_getFileEventList',
      payload: {
        start: 0,
        count: 100
      },
      callback: () => {
        sessionStorage.setItem('arrayIndex', 1);
        this.setWorkbenchList();
      }
    })
  };

  // 办理提交
  fileSubmit = () => {
    const { form: { validateFields }, dispatch } = this.props;
    const { file_item, time, checkedPreKeys, image, unitIds, userIds, video, timeType, itemType } = this.state;
    validateFields((err, values) => {
      if (!err) {
        const payload = {
          id: file_item.workId,
          todoId: file_item.id,
          pathId: time.id,
        };
        switch (time.type) {
          case 'RETURN':
            if (values.hasDesc) {
              payload.desc = values.hasDesc;
            } else {
              message.error('请描述原因')
            }
            break;
          case 'DELAY':
            if (values.time && values.delayDesc) {
              payload.timeType = timeType;
              payload.time = values.time;
              payload.desc = values.delayDesc;
            } else {
              message.error('请输入延时时间或是描述')
            }
            break;
          case 'REVIEW':
            if (values.hasDesc) {
              payload.desc = values.hasDesc;
            } else {
              message.error('请描述原因')
            }
            break;
          case 'AGREE':
            if (itemType && itemType) {
              if (checkedPreKeys.length >= 1) {
                payload.userIds = userIds && userIds;
                payload.unitIds = unitIds && unitIds;
              } else {
                message.error('请选择办理人')
              }
            }
            break;
          case 'ADDUSER':
            if (checkedPreKeys.length >= 1) {
              payload.userIds = userIds;
              payload.unitIds = unitIds;
            } else {
              message.error('请选择办理人')
            }
            break;
          default:
            break;
        }
        if (time.type !== 'REVIEW') {
          dispatch({
            type: 'global/fetch_fileSubmit',
            payload,
            callback: res => {
              if (res.status === 0) {
                this.getListAll();
              }
            }
          });
        } else {
          if (image.length >= 1) {
            payload.afterImagePaths = image;
            payload.afterVideoPaths = video;
            dispatch({
              type: 'global/fetch_fileSubmit',
              payload,
              callback: res => {
                if (res.status === 0) {
                  this.getListAll();
                }
              }
            })
          } else {
            this.setState({
              RETURN_OR_REVIEWVisible: false,
              workPathVisible: false,
            }, () => {
              message.error('请选择您要上传的文件')
            });
          }
        }
      } else {
        console.log({ ...err })
      }
    })
  };

  // 多选树形结构
  onCheckPermissionIds = (checkedKeys, info) => {
    let arr = [];
    let newArr = [];
    info.checkedNodes.map(item => {
      if (item.props.dataRef.type === 'USER') {
        arr.push(item.key)
      }
    });
    info.checkedNodes.map(item => {
      if (item.props.dataRef.type === 'UNIT') {
        newArr.push(item.key)
      }
    });
    let allCheckedKeys = [...checkedKeys, ...info.halfCheckedKeys];
    this.setState({
      checkedPreKeys: checkedKeys,
      userIds: arr,
      unitIds: newArr
    });
  };

  // 动态获取属性结构
  unitTreeOnLoad = (treeNode) => {
    let { dispatch, global: { unit_tree } } = this.props;
    const parentId = treeNode.props.dataRef.key;
    const params = {
      id: parentId,
    };
    return request('/api/unit/user/tree', { params, headers: { 'Authorization': token } }).then(res => {
      const children = res.value;
      treeNode.props.dataRef.children = children;
      unit_tree = [...unit_tree];
      dispatch({
        type: "global/unit_tree",
        payload: unit_tree,
      })
    });
  };

  // 生成属性结构目录
  renderTreeNodes = data => {
    return (
      data && data.map(item => {
        if (item.children) {
          return (
            <TreeNode title={item.title} key={item.key} dataRef={item}>
              {this.renderTreeNodes(item.children)}
            </TreeNode>
          );
        }
        return <TreeNode {...item} dataRef={item} />;
      })
    );
  };

  //办理人弹窗
  unitTree_Modal = () => {
    const { global: { unit_tree } } = this.props;
    return (
      <Modal
        key={9527}
        title="选择下一办理人"
        centered
        visible={this.state.unitTreeVisible}
        onCancel={() => this.setState({ unitTreeVisible: false })}
        footer={[
          <Button key="submit" type="primary" onClick={this.fileSubmit} style={{ marginRight: 15 }}>
            确定
          </Button>,
          <Button key="back" onClick={() => this.setState({ unitTreeVisible: false })}>
            取消
          </Button>,
        ]}
      >
        <div className={style.formTree}>
          <Tree
            checkable
            onCheck={this.onCheckPermissionIds}
            defaultExpandAll
            loadData={this.unitTreeOnLoad}
          >
            {this.renderTreeNodes(unit_tree)}
          </Tree>
        </div>
      </Modal>
    )
  };

  // 提交 退回时描述原因弹窗
  RETURN_OR_REVIEW = () => {
    const { form: { getFieldDecorator, getFieldValue } } = this.props;
    return (
      <Modal
        key={9529}
        title="请描述原因"
        style={{ top: "40%" }}
        visible={this.state.RETURN_OR_REVIEWVisible}
        onCancel={() => this.setState({ RETURN_OR_REVIEWVisible: false })}
        footer={[
          <Button key="submit" type="primary" onClick={this.fileSubmit} style={{ marginRight: 15 }}>
            确定
          </Button>,
          <Button key="back" onClick={() => this.setState({ RETURN_OR_REVIEWVisible: false })}>
            取消
          </Button>,
        ]}
      >
        <Form.Item>
          {getFieldDecorator('hasDesc', {
            rules: [
              { validator: validator }
            ]
          })(<TextArea rows={3} style={{ resize: "none" }} placeholder='请描述原因' />)}
        </Form.Item>
      </Modal>
    )
  };

  // 填写延时时间
  TimeDelay_Modal = () => {
    const { form: { getFieldDecorator } } = this.props;
    const selectAfter = (
      <Select
        defaultValue='HOUR'
        style={{ width: 80 }}
        onSelect={(e) => this.setState({ timeType: e })}
        value={this.state.timeType}
      >
        <Option value='DAY'>天</Option>
        <Option value='HOUR'>小时</Option>
      </Select>
    );
    return (
      <Modal
        key={9530}
        title="请输入延时时间"
        visible={this.state.TimeDelayVisible}
        onCancel={() => this.setState({ TimeDelayVisible: false })}
        onOk={this.fileSubmit}
        footer={[
          <Button key="submit" type="primary" onClick={this.fileSubmit} style={{ marginRight: 15 }}>
            确定
          </Button>,
          <Button key="back" onClick={() => this.setState({ TimeDelayVisible: false })}>
            取消
          </Button>,
        ]}
      >
        <Form.Item
          label='延迟时间'
          {...formItemLayout}
        >
          {getFieldDecorator('time', {
            getValueFromEvent: (event) => {
              return event.target.value.replace(/\D/g, '')
            },
          })(
            <Input addonAfter={selectAfter} />
          )}
        </Form.Item>
        <Form.Item
          label='描述'
          {...formItemLayout}
        >
          {getFieldDecorator('delayDesc', {
            rules: [
              { validator: validator }
            ]
          })(<TextArea rows={3} style={{ resize: "none" }} placeholder='请描述原因' />)}
        </Form.Item>
      </Modal>
    )
  };

  // 确然当前点击的类型
  workPathButtonClick = (data) => {
    const { itemType } = this.state;
    const time = JSON.parse(data);
    switch (time.type) {
      case 'RETURN':
        this.setState({
          RETURN_OR_REVIEWVisible: true,
          time,
        });
        break;
      case 'DELAY':
        this.setState({
          TimeDelayVisible: true,
          time,
        });
        break;
      case 'REVIEW':
        this.setState({
          RETURN_OR_REVIEWVisible: true,
          time,
        });
        break;
      case 'REFUSE':
        this.setState({
          time,
        }, () => {
          return this.fileSubmit();
        });
        break;
      case 'AGREE':
        if (itemType) {
          this.setState({
            unitTreeVisible: true,
            time,
          });
        } else {
          this.setState({
            time,
          }, () => {
            return this.fileSubmit();
          });
        }
        break;
      case 'ADDUSER':
        this.setState({
          unitTreeVisible: true,
          time,
        });
        break;
      default:
        this.setState({
          time,
        }, () => {
          return this.fileSubmit();
        });
        break;
    }
  };

  // 点击结束
  fileHandleEndClick = (id) => {
    const { dispatch } = this.props;
    const modal = Modal.confirm();
    modal.update({
      title: '结束提示',
      okText: '确定',
      centered: true,
      cancelText: '返回',
      content: (
        <Text> 是否确定要结束 </Text>
      ),
      onCancel: () => {
        modal.destroy();
      },
      onOk: () => {
        dispatch({
          type: 'global/fetch_fileEnd',
          payload: {
            id
          },
          callback: (res) => {
            if (res.status === 0) {
              this.getListAll();
            }
          }
        });
        modal.destroy();
      }
    });
  };

  // 点击撤回
  fileWithdrawClick = (todoId, workId, pathId) => {
    const { dispatch } = this.props;
    const modal = Modal.confirm();
    modal.update({
      title: '撤回提示',
      centered: true,
      okText: '确定',
      cancelText: '返回',
      content: (
        <Text> 是否确定要撤回 </Text>
      ),
      onCancel: () => {
        modal.destroy();
      },
      onOk: () => {
        dispatch({
          type: 'global/fetch_fileWithdraw',
          payload: {
            todoId,
            pathId,
            workId,
          },
          callback: (res) => {
            if (res.status === 0) {
              this.getListAll();
            }
          }
        });
        modal.destroy();
      }
    });
  };

  // 点击受理
  fileHandleAcceptClickTrue = (id) => {
    const { dispatch } = this.props;
    const modal = Modal.confirm();
    modal.update({
      title: '受理提示',
      centered: true,
      okText: '确定',
      cancelText: '返回',
      content: (
        <Text> 是否确定要受理 </Text>
      ),
      onCancel: () => {
        modal.destroy();
      },
      onOk: () => {
        dispatch({
          type: 'global/fetch_fileAccept',
          payload: {
            id,
            flag: true
          },
          callback: (res) => {
            if (res.status === 0) {
              dispatch({
                type: 'global/fetch_getFileEventList',
                payload: {
                  start: 0,
                  count: 1
                },
              })
            }
          }
        });
        modal.destroy();
      }
    });
  };

  // 点击忽略
  fileHandleAcceptClickFalse = (id) => {
    const { dispatch } = this.props;
    dispatch({
      type: 'global/fetch_fileAccept',
      payload: {
        id,
        flag: false
      },
      callback: (res) => {
        if (res.status === 0) {
          dispatch({
            type: 'global/fetch_getFileEventList',
            payload: {
              start: 0,
              count: 1
            },
          })
        }
      }
    })
  };

  // 路劲弹窗前按钮名字
  historyButtonName = (type) => {
    switch (type) {
      case 'SUBMIT':
        return '提交';
      case 'WITHDRAW':
        return '撤回';
      case 'END':
        return '结束';
      case 'ACCEPT':
        return '受理';
      case 'DELETE':
        return '忽略';
      case 'SPECIAL_DELIVERY':
        return '特送';
      default:
        break;
    }
  };

  // 路劲弹窗前按钮点击事件
  historyButtonClick = (type, res) => {
    switch (type) {
      case 'SUBMIT':
        return this.workPath(res);
      case 'WITHDRAW':
        return this.fileWithdrawClick(res.id, res.workId);
      case 'END':
        return this.fileHandleEndClick(res.id);
      case 'ACCEPT':
        return this.fileHandleAcceptClickTrue(res.id);
      case 'DELETE':
        return this.fileHandleAcceptClickFalse(res.id);
      default:
        break;
    }
  };

  // 选择路径弹窗
  workPath_Modal = () => {
    const { workButton } = this.state;
    return (
      <Modal
        key={9528}
        title="选择路径"
        centered
        visible={this.state.workPathVisible}
        onCancel={() => this.setState({ workPathVisible: false })}
        footer={null}
      >
        <Fragment>
          <Row span={24} >
            <Col
              span={12}
              classNmae={workButton.length > 2 ? style.workPathModal_big : style.workPathModal_small}
              style={{ textAlign: "center" }}
            >
              <Icon
                type="play-circle"
                style={{
                  fontSize: 83,
                  textAlign: 'center',
                }}
                className={workButton.length > 2 ? style.workPathModalIcon_big : style.workPathModalIcon_small}
              />
            </Col>
            <Col span={12}>
              {
                workButton.length >= 1 && workButton.map((itemStyle, index) => {
                  return (
                    <Col span={24} style={{ paddingLeft: 81 }} key={index}>
                      <Button
                        value={JSON.stringify(itemStyle)}
                        type={itemStyle.name === '提交' ? 'primary' : ''}
                        onClick={(e) => { this.workPathButtonClick(e.target.value) }}
                        style={{
                          textAlign: "center",
                          width: "80px",
                          margin: "10px 0px"
                        }}
                      >
                        {itemStyle.name}
                      </Button>
                    </Col>
                  )
                })
              }
            </Col>
          </Row>
        </Fragment>
      </Modal>
    )
  };

  //点击办理
  workPath = (item) => {
    const { dispatch } = this.props;
    const { itemType } = this.state;
    let data = item;
    dispatch({
      type: 'global/fetch_getWorkPath',
      payload: {
        id: item.id,
      },
      callback: res => {
        if (res.status === 0) {
          this.setState({
            workButton: res.value,
            file_item: data,
            itemType,
          }, () => {
            this.setState({
              workPathVisible: true
            })
          })
        }
      }
    })
  };

  previewModal = () => {
    const { previewModalVisible, imageNewUrl } = this.state;
    return (
      <Modal
        visible={previewModalVisible}
        title="预览大图"
        width={1250}
        className={style.previewModalWarp}
        key={8000}
        centered
        onCancel={() => this.setState({ previewModalVisible: false, imageNewUrl: null })}
        footer={null}
      >
        <div
          style={{
            width: '100%',
            height: 824,
            background: `url('${getUrl(imageNewUrl)}') center center / contain no-repeat`,
            // backgroundRepeat: 'no-repeat',
            // backgroundSize: 'contain',
            // backgroundPosition: 'center',
          }}
        />
      </Modal>
    )
  };

  fileItemModel = () => {
    const { fileItemModelVisible, image, video } = this.state;
    const { form: { getFieldDecorator }, global: { fileItemData, fileLog_list } } = this.props;
    return (
      <Fragment>
        <Modal
          title="案卷详情"
          visible={fileItemModelVisible}
          onCancel={() => this.setState({ fileItemModelVisible: false })}
          width={1400}
          className={style.itemFileTextWarp}
          footer={null}
        >
          <Row type='flex'>
            <Col className={style.addFileTextLeft} >
              <Form className={style.formWarp}>
                <Row style={{ textAlign: 'center' }}>
                  <Button
                    style={{ margin: 5, backgroundColor: '#E5E5E5', color: "rgba(102,102,102,1)" }}
                    onClick={() => this.setState({ fileItemModelVisible: false })}
                  >
                    关闭
                  </Button>
                  {
                    fileItemData && fileItemData.button.map((item, index) => {
                      return (
                        <Button
                          type='primary'
                          style={{ margin: 5 }}
                          value={item}
                          key={index}
                          onClick={(e) => this.historyButtonClick(e.target.value, fileItemData)}
                        >
                          {this.historyButtonName((item))}
                        </Button>
                      )
                    })
                  }
                </Row>
                <Row>
                  <Col span={1} offset={23} style={{ marginLeft: "93.833333%" }}>
                    <Text
                      style={{ margin: 5, cursor: "pointer" }}
                      onClick={() => {
                        if (fileItemData && fileItemData.workId) {
                          this.IconRight_click(fileItemData.workId);
                          this.setState({ disposal_list_Model_Visible: false })
                        }
                      }}
                    >
                      <img src={file_del} alt="删除" />
                    </Text>
                  </Col>
                </Row>
                <Row>
                  <Col span={12}>
                    <Form.Item
                      {...formItemLayout}
                      label='标题'
                    >
                      {getFieldDecorator('title', {
                        initialValue: fileItemData && fileItemData.title,
                      })(
                        <Input disabled style={{ color: "#000" }} />
                      )}
                    </Form.Item>
                    <Form.Item
                      {...formItemLayout}
                      label='案卷小类'
                    >
                      {getFieldDecorator('type', {
                        initialValue: fileItemData && fileItemData.type,
                      })(
                        <Input disabled style={{ color: "#000" }} />
                      )}
                    </Form.Item>
                    <Form.Item
                      {...formItemLayout}
                      label='发生地点'
                    >
                      {getFieldDecorator('address', {
                        initialValue: fileItemData && fileItemData.address,
                      })(
                        <Input disabled style={{ color: "#000" }} />
                      )}
                    </Form.Item>
                    <Form.Item
                      label='状态'
                      {...formItemLayout}
                    >
                      {getFieldDecorator('status', {
                        initialValue: fileItemData && fileItemData.status,
                      })(
                        <Select disabled style={{ color: "#000" }}>
                          <Option value='ORDINARY'>正常</Option>
                          <Option value='TIME_OUT'>超时</Option>
                          <Option value='COMING_SOON_TIME_OUT'>即将超时</Option>
                        </Select>
                      )}
                    </Form.Item>
                    <Form.Item
                      {...formItemLayout}
                      label='创建人'
                    >
                      {getFieldDecorator('creator', {
                        initialValue: fileItemData && fileItemData.creator,
                      })(
                        <Input disabled style={{ color: "#000" }} />
                      )}
                    </Form.Item>
                  </Col>
                  <Col span={12}>
                    <Form.Item
                      {...formItemLayout}
                      label='案卷编号'
                    >
                      {getFieldDecorator('num', {
                        initialValue: fileItemData && fileItemData.num,
                      })(
                        <Input disabled style={{ color: "#000" }} />
                      )}
                    </Form.Item>
                    <Form.Item
                      label='紧急程度'
                      {...formItemLayout}
                    >
                      {getFieldDecorator('workType', {
                        initialValue: fileItemData && fileItemData.workType,
                      })(
                        <Select disabled style={{ color: "#000" }}>
                          <Option value='URGENT'>紧急</Option>
                          <Option value='ORDINARY'>普通</Option>
                        </Select>
                      )}
                    </Form.Item>
                    <Form.Item
                      {...formItemLayout}
                      label='来源'
                    >
                      {getFieldDecorator('source', {
                        initialValue: fileItemData && fileItemData.source && returnSourceText(fileItemData.source),
                      })(
                        <Input disabled style={{ color: "#000" }} />
                      )}
                    </Form.Item>
                    <Form.Item
                      {...formItemLayout}
                      label='创建时间'
                    >
                      {getFieldDecorator('createTime', {
                        initialValue: fileItemData && fileItemData.createTime,
                      })(
                        <Input disabled style={{ color: "#000" }} />
                      )}
                    </Form.Item>
                    <Form.Item
                      {...formItemLayout}
                      label='处置前描述'
                    >
                      {getFieldDecorator('desc', {
                        initialValue: fileItemData && fileItemData.desc,
                      })(
                        <TextArea rows={2} style={{ resize: 'none', color: '#000' }} disabled />
                      )}
                    </Form.Item>
                  </Col>
                </Row>
                <Row span={24} style={{ marginBottom: '20px' }}>
                  <Col span={12} className={style.handle_CarouselWarp}>
                    <span className={style.newWelcomeTitle} >处置前图片：</span>
                    <Col span={24} className={`${style.welcomeWarp} ${style.welcomeWarp_24}`}>
                      <Col span={2} offset={5}>
                        {
                          fileItemData && fileItemData.beforeImagePaths.length <= 1 ? (
                            <div
                              // src={noArrowsLeft}
                              className={style.noLeft}
                            />
                          )
                            :
                            (
                              <div
                                // src={arrowsLeft}
                                onClick={() => {
                                  this.refs.beforeImageWelcome.next()
                                }
                                }
                                className={style.work_left}
                              />
                            )
                        }
                      </Col>
                      <div className={style.handle_ContentWarp}>
                        {
                          fileItemData && fileItemData.beforeImagePaths.length >= 1 ?
                            (
                              <Carousel
                                effect="fade"
                                ref="beforeImageWelcome"
                                className={style.welcomeContent}
                              >
                                {
                                  fileItemData && fileItemData.beforeImagePaths.map((item, index) => {
                                    return (
                                      <div
                                        key={index}
                                      >
                                        <img
                                          key={`image${index}`}
                                          alt=""
                                          style={{
                                            height: 185,
                                            width: 243,
                                            background: `url('${getUrl(item)}') center center / contain no-repeat`,
                                          }}
                                          onClick={e => {
                                            this.setState({
                                              imageNewUrl: item
                                            }, () => {
                                              this.setState({
                                                previewModalVisible: true
                                              })
                                            })
                                          }}
                                        />
                                      </div>
                                    )
                                  })
                                }
                              </Carousel>
                            )
                            :
                            <img src={forImage} alt="" style={{ width: 50, height: 50, marginTop: 70 }} />
                        }
                      </div>
                      <Col span={2}>
                        {
                          fileItemData && fileItemData.beforeImagePaths.length <= 1 ? (
                            <div
                              // src={noArrowsRight}
                              className={style.noRight}
                            />
                          )
                            :
                            (
                              <div
                                // src={arrowsRight}
                                onClick={() => {
                                  this.refs.beforeImageWelcome.next()
                                }
                                }
                                className={style.arrowsRgiht}
                              />
                            )
                        }
                      </Col>
                    </Col>
                  </Col>
                  <Col span={12} className={style.handle_CarouselWarp}>
                    <span className={style.newWelcomeTitle} >处置后图片：</span>
                    <Col span={24} className={`${style.welcomeWarp} ${style.welcomeWarp_24}`} >
                      <Col span={2} offset={5}>
                        {
                          image && image.length <= 1 ? (
                            <div
                              className={style.noLeft}
                            />
                          )
                            :
                            (
                              <div
                                onClick={() => {
                                  this.refs.afterImageWelcome.prev()
                                }
                                }
                                className={style.arrowsLeft}
                              />
                            )
                        }
                      </Col>
                      <div className={style.handle_ContentWarp}>
                        {
                          image && image.length >= 1 ?
                            (
                              <Carousel effect="fade" ref="afterImageWelcome" className={style.welcomeContent}>
                                {
                                  image && image.map((item, index) => {
                                    return (
                                      <div
                                        key={`div${index}`}
                                      >
                                        <img
                                          key={`image${index}`}
                                          alt=""
                                          style={{
                                            height: 185,
                                            width: 243,
                                            background: `url('${getUrl(item)}') center center / contain no-repeat`
                                          }}
                                          onClick={(e) => {
                                            this.setState({
                                              imageNewUrl: item
                                            }, () => {
                                              this.setState({
                                                previewModalVisible: true
                                              })
                                            })
                                          }}
                                        />
                                        <img
                                          src={del}
                                          className={style.welComeClose}
                                          alt=""
                                          value={index}
                                          key={index}
                                          onClick={e => this.imageDelUploadFile(index)}
                                        />
                                      </div>
                                    )
                                  })
                                }
                              </Carousel>
                            )
                            :
                            <img src={forImage} alt="" style={{ width: 50, height: 50, marginTop: 70 }} />
                        }
                      </div>
                      <Col span={2}>
                        {
                          image && image.length <= 1 ? (
                            <div
                              // src={noArrowsRight}
                              className={style.noRight}
                            />
                          )
                            :
                            (
                              <div
                                // src={arrowsRight}
                                onClick={() => {
                                  this.refs.afterImageWelcome.next()
                                }
                                }
                                className={style.arrowsRgiht}
                              />
                            )
                        }
                      </Col>
                    </Col>
                    <Upload
                      style={{ marginLeft: 231 }}
                      onChange={this.fileImageChange}
                      name="file"
                      accept=".jpg,.jpeg,.png"
                      action={getUrl("/api/file/upload?fileType=5")}
                      showUploadList={false}
                      beforeUpload={beforeUpload}
                      headers={{
                        "Authorization": token,
                      }}
                    >
                      <Button type="primary" disabled={image.length >= 5}>
                        <Icon type='upload' /> 上传
                      </Button>
                    </Upload>
                    <QueueAnim
                      delay={[1, 1000]}
                    >
                      {
                        this.state.imagePercent.map((item, index) => {
                          if (item === 0 || item >= 100) {
                            return null
                          } else {
                            return (
                              <Progress
                                key={index}
                                strokeColor={{
                                  '0%': '#108ee9',
                                  '100%': '#87d068',
                                }}
                                percent={Number(item)}
                              />
                            )
                          }
                        })
                      }
                    </QueueAnim>
                  </Col>
                </Row>
                <Row span={24}>
                  <Col span={12} className={style.handle_CarouselWarp}>
                    <span className={style.newWelcomeTitle} >处置前视频：</span>
                    <Col span={24} className={`${style.welcomeWarp} ${style.welcomeWarp_24}`}>
                      <Col span={2} offset={5}>
                        {
                          fileItemData && fileItemData.beforeVideoPaths.length <= 1 ? (
                            <div
                              // src={noArrowsLeft}
                              alt=""
                              className={style.noLeft}
                            />
                          )
                            :
                            (
                              <div
                                // src={arrowsLeft}
                                // alt=""
                                onClick={() => {
                                  this.refs.beforeVideoWelcome.next()
                                }
                                }
                                className={style.arrowsLeft}
                              />
                            )
                        }
                      </Col>
                      <div className={style.handle_ContentWarp}>
                        {
                          fileItemData && fileItemData.beforeVideoPaths.length >= 1 ?
                            (
                              <Carousel effect="fade" ref="beforeVideoWelcome" className={style.welcomeContent}>
                                {
                                  fileItemData.beforeVideoPaths.map((item, index) => {
                                    return <video width="245" height="180" controls="controls" key={index} src={getUrl(item)} />
                                  })
                                }
                              </Carousel>
                            )
                            :
                            <img src={forVideo} alt="" style={{ width: 50, height: 50, marginTop: 70 }} />
                        }
                      </div>
                      <Col span={2}>
                        {
                          fileItemData && fileItemData.beforeVideoPaths.length <= 1 ? (
                            <div
                              // alt=""
                              // src={noArrowsRight}
                              className={style.noRight}
                            />
                          )
                            :
                            (
                              <div
                                // alt=""
                                // src={arrowsRight}
                                onClick={() => {
                                  this.refs.beforeVideoWelcome.next()
                                }
                                }
                                className={style.arrowsRgiht}
                              />
                            )
                        }
                      </Col>
                    </Col>
                  </Col>
                  <Col span={12} className={style.handle_CarouselWarp}>
                    <span className={style.welcomeTitle} >处置后视频：</span>
                    <Col span={24} className={`${style.welcomeWarp} ${style.welcomeWarp_24}`}>
                      <Col span={2} offset={5}>
                        {
                          video && video.length <= 1 ? (
                            <div
                              // alt=''
                              // src={noArrowsLeft}
                              className={style.noLeft}
                            />
                          )
                            :
                            (
                              <div
                                // alt=''
                                // src={arrowsLeft}
                                onClick={() => {
                                  this.refs.afterVideoWelcome.next()
                                }
                                }
                                className={style.arrowsLeft}
                              />
                            )
                        }
                      </Col>
                      <div className={style.handle_ContentWarp}>
                        {
                          video.length >= 1 ?
                            (
                              <Carousel
                                effect="fade"
                                ref="afterVideoWelcome"
                                className={style.welcomeContent}
                              >
                                {
                                  video.map((item, index) => {
                                    return (
                                      <div key={`warp${index}`}>
                                        <img
                                          src={del}
                                          className={style.welComeClose}
                                          alt=""
                                          value={index}
                                          key={index}
                                          onClick={e => this.videoDelUploadFile(index)}
                                        />
                                        <video width="245" height="180" controls="controls" key={`video${index}`} src={getUrl(item)} />
                                      </div>
                                    )
                                  })
                                }
                              </Carousel>
                            )
                            :
                            <img src={forVideo} alt="" style={{ width: 50, height: 50, marginTop: 70 }} />
                        }
                      </div>
                      <Col span={2} >
                        {
                          video && video.length <= 1 ? (
                            <div
                              // alt=''
                              // src={noArrowsRight}
                              className={style.noRight}
                            />
                          )
                            :
                            (
                              <div
                                // alt=''
                                // src={arrowsRight}
                                onClick={() => {
                                  this.refs.afterVideoWelcome.next()
                                }
                                }
                                className={style.arrowsRgiht}
                              />
                            )
                        }
                      </Col>
                    </Col>
                    <Upload
                      style={{ marginLeft: 230, marginTop: 20 }}
                      onChange={this.fileVideoChange}
                      name="file"
                      accept=".mp4"
                      action={getUrl("/api/file/upload?fileType=5")}
                      showUploadList={false}
                      beforeUpload={beforeUpload}
                      headers={{
                        "Authorization": token,
                      }}
                    >
                      <Button type="primary" disabled={video.length >= 5}>
                        <Icon type='upload' /> 上传
                      </Button>
                    </Upload>
                    <QueueAnim
                      delay={[1, 1000]}
                    >
                      {
                        this.state.videoPercent.map((item, index) => {
                          if (item === 0 || item >= 100) {
                            return null
                          } else {
                            return (
                              <Progress
                                key={index}
                                strokeColor={{
                                  '0%': '#108ee9',
                                  '100%': '#87d068',
                                }}
                                percent={Number(item)}
                              />
                            )
                          }
                        })
                      }
                    </QueueAnim>
                  </Col>
                </Row>
              </Form>
            </Col>
            <Col className={style.ColLogWarp} >
              <span style={{ color: '#000', fontSize: 15, fontWeight: 500, paddingTop: 5, display: 'block', marginLeft: '44%' }}>办理流程</span>
              {
                fileLog_list && fileLog_list.map((item, index) => {
                  index += 1;
                  return (
                    <Col className={style.ColLogChildrenWarp} key={index}>
                      <div className={style.logIndex}>{index}</div>
                      <Col className={style.logContentWarp}>
                        <Row style={{ paddingTop: 5 }}>
                          <Text style={{ margin: 8, color: '#4C5FC1', textAlign: 'left' }}>
                            {item.title}
                            <Text style={{ float: 'right', marginRight: 8 }}>{item.time && item.time}{item.timeType && returnTimeTypeText(item.timeType)}</Text>
                          </Text>
                          <Row type="flex" justify="space-between" style={{ margin: "0 8px" }}>
                            <Text style={{ marginRight: 4 }}>{item.username}</Text>
                            {
                              item.withdrawFlag && (
                                <Button
                                  size='small'
                                  type="primary"
                                  onClick={() => this.fileWithdrawClick(item.todoId, fileItemData.workId, item.pathId)}
                                >
                                  撤回
                                </Button>
                              )
                            }
                          </Row>
                          <Text style={{ marginLeft: 8 }}>{item.createTime}</Text>
                          {
                            !item.endflag ? (
                              <div className={style.logContentTop}>
                                {item.desc}
                              </div>
                            ) : ""
                          }
                        </Row>
                        {
                          !item.endflag ? (
                            <Fragment>
                              {item.finishFlag ? "" : (<div className={style.divBorderTop}> {/* */} </div>)}
                              <div
                                style={item.finishFlag ? { backgroundColor: '#EAEAEA', border: '#FFF', padding: 8 } : { backgroundColor: '#FFF', padding: 8 }}
                              >
                                <Text style={{ fontSize: 12, color: "rgba(76,95,193,1)", whiteSpace: "nowrap" }}>下一办理人：</Text> {item.nextHandlers}
                              </div>
                            </Fragment>
                          ) : ""
                        }
                      </Col>
                    </Col>
                  )
                })
              }
            </Col>
          </Row>
        </Modal>
      </Fragment>
    )
  };

  componentWillUnmount() {
    clearInterval(this.list_time);
    clearInterval(this.event_time);
  }


  render() {
    const {
      global: { bounce_list, delayed_list, disposal_list, inspect_list, event_list, eventPage },
      workbench: { workbench_list },
      loading
    } = this.props;
    const { getFieldDecorator } = this.props.form;
    console.log(event_list);

    return (
      <Fragment>
        <ConfigProvider renderEmpty={customizeRenderEmpty}>
          <Row type="flex" justify="space-between" className={style.WorkbenchWarp}>
            <Col span={12} style={{ paddingRight: 8, zIndex: 0 }} className={style.Workbench_LeftCol}>
              <Col span={24}>
                <Card
                  title={(<Badge count={inspect_list && inspect_list.totalCount} offset={[7, -4]}>待核查</Badge >)}
                  extra={(
                    <Link
                      to="/file/fileList?tabsKey=global/fetch_getFileInspectList"
                      style={{ color: 'rgba(153,153,153,1)' }}
                    >
                      更多
                  </Link>
                  )}
                  bordered={false}
                  className={style.global_Card}
                  style={{ height: '305px' }}
                >
                  <Table
                    showHeader={false}
                    dataSource={inspect_list && inspect_list.value}
                    columns={columns}
                    style={{ padding: 0 }}
                    bordered={false}
                    size="middle"
                    rowKey={record => record.id}
                    pagination={{
                      ...smallDefaultPageSize_5,
                      hideOnSinglePage: true,
                      position: 'none',
                    }}
                    onRow={record => {
                      return {
                        onClick: e => {
                          this.handleOnRowClick(record.id, 1)
                        }
                      }
                    }}
                  />
                </Card>
              </Col>
              <Col span={24}>
                <Card
                  title={(<Badge count={delayed_list && delayed_list.totalCount} offset={[7, -4]}>申请延时</Badge >)}
                  extra={(
                    <Link
                      to="/file/fileList?tabsKey=global/fetch_getFileDelayedList"
                      style={{ color: 'rgba(153,153,153,1)' }}
                    >
                      更多
                  </Link>
                  )}
                  bordered={false}
                  className={style.global_Card}
                  style={{ height: '305px' }}
                >
                  <Table
                    showHeader={false}
                    dataSource={delayed_list && delayed_list.value}
                    bordered={false}
                    columns={columns}
                    size="middle"
                    rowKey={record => record.id}
                    pagination={{
                      ...smallDefaultPageSize_5,
                      hideOnSinglePage: true,
                      position: 'none',
                    }}
                    onRow={record => {
                      return {
                        onClick: e => {
                          this.handleOnRowClick(record.id, 1)
                        }
                      }
                    }}
                  />
                </Card>
              </Col>
              <Col span={24}>
                <Card
                  title={(<Badge count={bounce_list && bounce_list.totalCount} offset={[7, -4]}>退件</Badge >)}
                  extra={(
                    <Link
                      to="/file/fileList?tabsKey=global/fetch_getFileBounceList"
                      style={{ color: 'rgba(153,153,153,1)' }}
                    >
                      更多
                  </Link>
                  )}
                  bordered={false}
                  className={style.global_Card}
                  style={{ height: '305px' }}
                >
                  <Table
                    showHeader={false}
                    dataSource={bounce_list && bounce_list.value}
                    bordered={false}
                    columns={columns}
                    rowKey={record => record.id}
                    size="middle"
                    pagination={{
                      ...smallDefaultPageSize_5,
                      hideOnSinglePage: true,
                      position: 'none',
                    }}
                    onRow={record => {
                      return {
                        onClick: e => {
                          this.handleOnRowClick(record.id, 1, true)
                        }
                      }
                    }}
                  />
                </Card>
              </Col>
            </Col>
            <Col span={12} style={{ zIndex: 0 }}>
              {event_list && event_list.value.length ? (
                <Col span={24}>
                  {/*<Spin>*/}
                  <Card
                    title={(<Badge count={event_list.totalCount} offset={[7, -4]}>待受理事件</Badge >)}
                    extra={(
                      <Link
                        to="/file/eventManagement"
                        style={{ color: 'rgba(153,153,153,1)' }}
                      >
                        更多
                    </Link>
                    )}
                    bordered={false}
                    className={`${style.global_Card} ${style.Workbench}`}
                    style={{ height: '481px' }}
                  >
                    <div className={style.Workbench_title} style={{ height: '6vh' }}>{event_list.value[0].title}</div>
                    <Row span={24}>
                      <Col span={1} style={{ marginTop: "17%", textAlign: 'right' }}>
                        <Col span={2} offset={5}>
                          {eventPage.start == 0 ?
                            (
                              <div
                                className={style.work_noleft}
                                onClick={this.previous}
                              />
                            )
                            :
                            (
                              <div
                                className={style.work_left}
                                onClick={this.previous}
                              />
                            )
                          }
                        </Col>

                      </Col>
                      <Col span={20} style={{ margin: '4%', marginTop: 0, marginBottom: 0 }}>
                        <div className={style.formCarouselWarp}>
                          <Form {...formCarouselLayout}>
                            <Row type="flex" justify="space-around">
                              <Col span={12}>
                                <img
                                  src={getUrl(event_list.value[0].image[0])}
                                  className={style.form_ShowImage}
                                  alt=""
                                  onClick={(e) => {
                                    this.setState({ imageNewUrl: event_list.value[0].image[0] }, () => { this.setState({ previewModalVisible: true }) })
                                  }}
                                />
                              </Col>
                              <Col span={12} >
                                {/*<Form.Item label="标题:" style={{ margin: '15px 0'}}>*/}
                                {/*  <Tooltip title={workbench_list && workbench_list.title}>*/}
                                {/*  {getFieldDecorator('id', {*/}
                                {/*    // rules: [{ required: false, message: 'Please input your id!' }],*/}
                                {/*    initialValue: workbench_list && workbench_list.title,*/}
                                {/*  })(*/}
                                {/*    <Input disabled style={{ color: "#000" }}/>*/}
                                {/*  )}*/}
                                {/*  </Tooltip>*/}
                                {/*</Form.Item>*/}
                                <Form.Item label="案卷类型:" style={{ margin: '15px 0' }}>
                                  {getFieldDecorator('categoryName', {
                                    rules: [{ required: false, message: 'Please input your ip!' }],
                                    initialValue: event_list.value[0].categoryName,
                                  })(
                                    <Input disabled style={{ color: "#000" }} />,
                                  )}
                                </Form.Item>
                                <Form.Item label="主体名称:" style={{ margin: '15px 0' }}>
                                  {getFieldDecorator('blacklistName', {
                                    rules: [{ required: false, message: 'Please input your ip!' }],
                                    initialValue: event_list.value[0].blacklistName,
                                  })(
                                    <Input disabled style={{ color: "#000" }} />,
                                  )}
                                </Form.Item>
                                <Form.Item label="紧急程度:" style={{ margin: '15px 0' }}>
                                  {getFieldDecorator('workType', {
                                    rules: [{ required: false, message: 'Please input your ip!' }],
                                    initialValue: returnWorkTypeText(event_list.value[0].workType),
                                  })(
                                    <Input disabled style={{ color: "#000" }} />,
                                  )}
                                </Form.Item>
                                <Form.Item label="发生地点:" style={{ margin: '15px 0' }}>
                                  <Tooltip title={event_list.value[0].address}>
                                    {getFieldDecorator('address', {
                                      rules: [{ required: false, message: 'Please input your ip!' }],
                                      initialValue: event_list.value[0].address,
                                    })(
                                      <Input disabled style={{ color: "#000" }} />,
                                    )}
                                  </Tooltip>
                                </Form.Item>
                                <Form.Item label="创建时间:" style={{ margin: '15px 0' }}>
                                  {getFieldDecorator('createTime', {
                                    rules: [{ required: false, message: 'Please input your time!' }],
                                    initialValue: event_list.value[0].createTime,
                                  })(
                                    <Input disabled style={{ color: "#000" }} />,
                                  )}
                                </Form.Item>
                              </Col>
                            </Row>
                            <Row style={{ textAlign: 'center', marginTop: '1%' }}>
                              <Button
                                type="primary"
                                htmlType="submit"
                                style={{ marginRight: "3%" }}
                                onClick={() => {
                                  if (event_list.value[0].id) {
                                    this.fileHandleAcceptClickTrue(event_list.value[0].id)
                                  }
                                }}
                              >
                                受理
                            </Button>
                              <Button
                                htmlType="submit"
                                onClick={() => {
                                  if (event_list.value[0].id) {
                                    this.fileHandleAcceptClickFalse(event_list.value[0].id)
                                  }
                                }}
                              >
                                忽略
                            </Button>
                            </Row>
                          </Form>
                        </div>
                      </Col>
                      <Col span={1} style={{ marginTop: "16%" }}>
                        {
                          eventPage.start == event_list.totalCount - 1 ?
                          (
                            <div
                              onClick={this.next}
                              className={style.work_noright}
                            />
                          )
                          :
                          (
                            <div
                              onClick={this.next}
                              className={style.work_right}
                            />
                          )
                        }
                      </Col>
                    </Row>
                  </Card>
                  {/*</Spin>*/}
                </Col>
              ) : (
                  <Col span={24}>
                    {/*<Spin>*/}
                    <Card
                      className={`${style.global_Card}`}
                      bodyStyle={{
                        minHeight: 424,
                        textAlign: 'center',
                      }}
                      title={(<Badge count={this.state.length} offset={[7, -4]}>待受理事件</Badge >)}
                      extra={(
                        <Link
                          to="/file/eventManagement"
                          style={{ color: 'rgba(153,153,153,1)' }}
                        >
                          更多
                    </Link>
                      )}
                    >
                      <img
                        src={nullStart}
                        alt="数据为空"
                        style={{ marginTop: 128 }}
                      />
                      <div style={{ color: "rgba(153,170,195,1)" }}>暂无数据</div>
                    </Card>
                    {/*</Spin>*/}
                  </Col>
                )
              }
              <Col span={24} className={style.Workbench_BottomRightCol}>
                <Card
                  title={(<Badge count={disposal_list && disposal_list.totalCount} offset={[7, -4]}>已处置</Badge >)}
                  extra={(
                    <Link
                      to="/file/fileList?tabsKey=global/fetch_getFileDisposalList"
                      style={{ color: 'rgba(153,153,153,1)' }}
                    >
                      更多
                </Link>
                  )}
                  bordered={false}
                  className={style.global_Card}
                  style={{ height: '442px' }}
                >
                  <Table
                    showHeader={false}
                    dataSource={disposal_list && disposal_list.value}
                    columns={columns}
                    size="middle"
                    rowKey={record => record.id}
                    pagination={{
                      ...smallDefaultPageSize_8,
                      hideOnSinglePage: true,
                      position: 'none',
                    }}
                    onRow={record => {
                      return {
                        onClick: e => {
                          this.handleOnRowClick(record.id, 2)
                        }
                      }
                    }}
                  />
                </Card>
              </Col>
            </Col>
          </Row>
          {/* 弹窗*/}
          {this.state.unitTreeVisible && this.unitTree_Modal()}
          {this.state.workPathVisible && this.workPath_Modal()}
          {this.state.RETURN_OR_REVIEWVisible && this.RETURN_OR_REVIEW()}
          {this.state.TimeDelayVisible && this.TimeDelay_Modal()}
          {this.state.fileItemModelVisible && this.fileItemModel()}
          {this.state.previewModalVisible && this.previewModal()}
        </ConfigProvider>
      </Fragment>
    );
  }
}
export default List;
