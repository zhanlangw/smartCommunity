import React, { Component, Fragment } from 'react';
import {
  Icon,
  Table,
  Button,
  Input,
  Typography,
  Divider,
  Pagination,
  Row,
  Drawer,
  Modal,
  Form,
  Tree,
  Col,
  Select,
  Carousel,
  message,
  Upload,
  Progress, DatePicker, Card,
} from 'antd';
import { connect } from 'dva';
import {
  beforeUpload,
  getUrl,
  pageSize_10 as count,
  payload_0_10 as pagePayload, returnSourceText,
  returnTimeTypeText,
  returnWorkTypeText,
  token, validatorDes
} from '@/utils/utils';
import style from './List.less';
import file_del from '@/assets/file_del.png';
import request from "@/utils/request";
import {stringify} from "qs";
import QueueAnim from "rc-queue-anim";
import forImage from '@/assets/hasImage.png';
import forVideo from '@/assets/hasVideo.png';
import moment from "moment";
import styles from "@/pages/Management/BlackList/style.less";
import arrowsLeft from '@/assets/left.png';
import noArrowsLeft from '@/assets/noLeft.png';
import arrowsRight from '@/assets/right.png';
import noArrowsRight from '@/assets/noRight.png';
import towLeft from "@/assets/left2.png";
import towRight from "@/assets/right2.png";
import del from "@/assets/del.png";

const { Text } = Typography;
const { TreeNode } = Tree;
const { Option } = Select;
const { Search, TextArea } = Input;

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

const formDrawerLayout = {
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
@connect(({ global, history }) => ({
  global,
  history,
}))
class List extends Component {

  state={
    start: 1, // 分页初始页
    visible: false, // 筛选抽屉效果
    selectedRowKeys: [], // <--- list列表中 多选参数
    selectedRows: [], // --->
    expandedPreKeys: null,  // <---树形结构参数
    checkedPreKeys: [],//--->
    startValue: null, // <----时间筛选
    endValue: null,
    endOpen: false,// ------------->
    image: [],
    video: [],
    previewModalVisible: false,
    imageModelVisible:false,
    time: {
      type: null
    },
    timeType:'HOUR',
    imagePercent: [],
    videoPercent:[],
    workButton: [],
    unitTreeVisible: false,
    workPathVisible: false,
    TimeDelayVisible: false,
    fileItemModelVisible: false,
    RETURN_OR_REVIEWVisible:false,
    handleListLogModelVisible: false,
  };

  componentDidMount() {
    const { dispatch } = this.props;
      dispatch({
      type: 'global/fetch_get_unit'
    })
  }


  // 获取当前type的list
  getList = payload => {
    const { dispatch, type } = this.props;
    console.log("222222222222222222222222222",type)
    dispatch({
      type,
      payload,
    });
  };

  // 点击列表
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
    let imagePercent= [];
    if (file.response && file.response.status === 0) {
      fileList.map(item=>{
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
    let videoPercent= [];
    if (file.response && file.response.status === 0) {
      fileList.map(item=>{
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
    const {image} = this.state;
    let arr = image;
    arr.splice(index, 1);
    this.setState({
      image: arr
    })
  };

  // 删除视频
  videoDelUploadFile = (index) => {
    const {video} = this.state;
    console.log(index)
    let arr = video;
    arr.splice(index, 1);
    this.setState({
      video: arr
    })
  };

  // table的分页
  onChange = page => {
    this.setState({
      start: page,
    }, () => {
      const { start, filter } = this.state;
      const payload = {
        start: (page - 1) * count,
        count,
        ...filter,
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
      onOk:()=> {
        this.handleClickDel();
        modal.destroy();
      },
      onCancel:()=>{
        modal.destroy();
      },
    });
  };

  // 删除逻辑
  handleClickDel = () => {
    const { dispatch, type, delButton } = this.props;
    const { title } = this.state;
    this.setState({ loading: true });
    let selectedRows = this.state.selectedRows.map(item => { return item.id });
    console.log(delButton);
    dispatch({
      type: delButton,
      payload: {
        ids: selectedRows.join(','),
      },
      callback: () => {
        this.setState({
          selectedRowKeys: [],
          loading: false,
          start: 1,
        }, () => {
          let payload = {
            title,
            ...this.state.filter,
            start: 0,
            count,
          };
          this.getList(payload)
        });
      }
    })
  };

  // 点击弹窗右上角删除
  IconRight_click = (id) => {
    const { dispatch } = this.props;
    const { getFileLogType, itemType } = this.state;
    const modal = Modal.confirm();
    modal.update({
      title: '删除提示',
      okText: '确定',
      centered: true,
      cancelText: '返回',
      content: (
        <Text> 确定要要删除</Text>
      ),
      onCancel:()=>{
        modal.destroy();
      },
      onOk:()=> {
        dispatch({
          type: 'global/fetch_delHasFiles',
          payload: {
            ids: id,
          }
        });
        if (itemType === 'history') {
          this.setState({
            image:[],
            video:[],
            start:1,
            selectedRowKeys: [],
            workPathVisible: false,
            TimeDelayVisible:false,
            RETURN_OR_REVIEWVisible: false,
            unitTreeVisible: false,
            fileItemModelVisible: false,
          },()=> {
            this.props.form.resetFields();
          });
          dispatch({
            type: 'history/fetch_getFileHistoryList',
            payload: {
              ...pagePayload
            }
          })
        } else {
          // console.log("11111111111111111")
          this.setState({
              start:1
          },()=>{
              this.getListAll();
          })

        }
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
      image:[],
      video:[],
      start: 1,
      selectedRowKeys: [],
      workPathVisible: false,
      TimeDelayVisible: false,
      RETURN_OR_REVIEWVisible: false,
      unitTreeVisible: false,
      fileItemModelVisible: false,
    },()=> {
      this.props.form.resetFields();
    });
    dispatch({
      type: 'global/fetch_getFileBounceList',
      payload:{
        ...pagePayload
      },
    });
    dispatch({
      type: 'global/fetch_getFileDelayedList',
      payload:{
        ...pagePayload
      },
    });
    dispatch({
      type: 'global/fetch_getFileDisposalList',
      payload:{
        ...pagePayload
      },
    });
    dispatch({
      type: 'global/fetch_getFileHandleList',
      payload:{
        ...pagePayload
      },
    });
    dispatch({
      type: 'global/fetch_getFileInspectList',
      payload:{
        ...pagePayload
      },
    });
  };

  // 办理提交
  fileSubmit = () => {
    const { form: { validateFields }, dispatch } = this.props;
    const { file_item, time, checkedPreKeys, image, unitIds, userIds, video, timeType, itemType} = this.state;
    validateFields((err, values)=> {
      if (!err) {
        const payload = {
          id: file_item.workId,
          todoId: file_item.id,
          pathId: time.id,
        };
        switch(time.type) {
          case 'RETURN':
            if (values.hasDesc){
              payload.desc = values.hasDesc;
            }else{
              message.error('请描述原因')
            }
            break;
          case 'DELAY':
            if (values.time && values.delayDesc){
              payload.timeType = timeType;
              payload.time = values.time;
              payload.desc = values.delayDesc;
            }else{
              message.error('请输入延时时间或是描述')
            }
            break;
          case 'REVIEW':
            if (values.hasDesc) {
              payload.desc = values.hasDesc;
            }else {
              message.error('请描述原因')
            }
            break;
          case 'AGREE':
            if (itemType && itemType){
              if (checkedPreKeys.length >= 1 ){
                payload.userIds = userIds && userIds;
                payload.unitIds = unitIds && unitIds;
              }else {
                message.error('请选择办理人')
              }
            }
            break;
          case 'ADDUSER':
            if (checkedPreKeys.length >= 1 ){
              payload.userIds = userIds;
              payload.unitIds = unitIds;
            }else {
              message.error('请选择办理人')
            }
            break;
          default:
            break;
        }
        console.log(payload)
        if (time.type !== 'REVIEW'){
          console.log(payload);
          dispatch({
            type: 'global/fetch_fileSubmit',
            payload,
            callback: res => {
              if (res.status === 0) {
                this.getListAll();
              }
            }
          });
        }else {
          if ( image.length >= 1 ) {
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
          }else {
            this.setState({
              RETURN_OR_REVIEWVisible: false,
              workPathVisible: false,
            }, ()=> {
              message.error('请选择您要上传的文件')
            });
          }
        }
      }else {
        console.log({...err})
      }
    })
  };

  // 多选树形结构
  onCheckPermissionIds = (checkedKeys, info) => {
    let arr =[];
    let newArr =[];
    info.checkedNodes.map(item=>{
      if (item.props.dataRef.type === 'USER') {
        arr.push(item.key)
      }
    });
    info.checkedNodes.map(item=>{
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
    let { dispatch, global:{ unit_tree } } = this.props;
    const parentId = treeNode.props.dataRef.key;
    const params = {
      id:parentId,
    };
    return request('/api/unit/user/tree', { params,  headers:{ 'Authorization': token}}).then(res=>{
      const children = res.value;
      treeNode.props.dataRef.children = children;
      unit_tree = [...unit_tree];
      dispatch({
        type:"global/unit_tree",
        payload:unit_tree,
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
        onCancel={()=> this.setState({unitTreeVisible: false})}
        footer={[
          <Button key="submit" type="primary" onClick={this.fileSubmit} style={{marginRight: 15}}>
            确定
          </Button>,
          <Button key="back" onClick={()=> this.setState({unitTreeVisible: false})}>
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
        style={{top: "40%"}}
        visible={this.state.RETURN_OR_REVIEWVisible}
        onCancel={()=> this.setState({RETURN_OR_REVIEWVisible: false})}
        footer={[
          <Button key="submit" type="primary" onClick={this.fileSubmit} style={{marginRight: 15}}>
            确定
          </Button>,
          <Button key="back" onClick={()=> this.setState({RETURN_OR_REVIEWVisible: false})}>
            取消
          </Button>,
        ]}
      >
        <Form.Item>
          {getFieldDecorator('hasDesc',{
            rules: [
              {validator: validatorDes}
            ]
          })(<TextArea rows={3} style={{ resize: "none"}} placeholder='请描述原因'/>)}
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
        onSelect={(e)=> this.setState({timeType: e})}
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
        centered
        visible={this.state.TimeDelayVisible}
        onOk={this.fileSubmit}
        onCancel={()=> this.setState({TimeDelayVisible: false})}
        footer={[
          <Button key="submit" type="primary" onClick={this.fileSubmit} style={{marginRight: 15}}>
            确定
          </Button>,
          <Button key="back" onClick={()=> this.setState({TimeDelayVisible: false})}>
            取消
          </Button>,
        ]}
      >
        <Form.Item
          label='延迟时间'
          {...formItemLayout}
        >
          {getFieldDecorator('time',{
            getValueFromEvent: (event) => {
              return event.target.value.replace(/\D/g,'')
            },
          })(
            <Input addonAfter={selectAfter} />
          )}
        </Form.Item>
        <Form.Item
          label='描述'
          {...formItemLayout}
        >
          {getFieldDecorator('delayDesc',{
            rules: [
              {validator: validatorDes}
            ]
          })(<TextArea rows={3} style={{ resize: "none"}} placeholder='请描述原因'/>)}
        </Form.Item>
      </Modal>
    )
  };

  // 确然当前点击的类型
  workPathButtonClick = (data) => {
    const { itemType } = this.state;
    const time = JSON.parse(data);
    console.log(itemType);
    switch(time.type) {
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
        },() => {
          return this.fileSubmit();
        });
        break;
      case 'AGREE':
        if (itemType) {
          this.setState({
            unitTreeVisible: true,
            time,
          });
        }else {
          this.setState({
            time,
          },() => {
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
        },() => {
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
      cancelText: '返回',
      centered: true,
      content: (
        <Text> 是否确定要结束 </Text>
      ),
      onOk:()=> {
        dispatch({
          type: 'global/fetch_fileEnd',
          payload: {
            id
          },
          callback: () => {
            this.getListAll();
          }
        });
        modal.destroy();
      },
      onCancel:()=>{
        modal.destroy();
      },
    });
  };

  // 点击撤回
  fileWithdrawClick = (todoId, workId, pathId) => {
    const { dispatch } = this.props;
    const modal = Modal.confirm();
    console.log(pathId);
    modal.update({
      title: '撤回提示',
      okText: '确定',
      centered:true,
      cancelText: '返回',
      content: (
        <Text> 是否确定要撤回 </Text>
      ),
      onOk:()=> {
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
      },
      onCancel:()=>{
        modal.destroy();
      },
    });
  };

  // 点击受理
  fileHandleAcceptClickTrue = (id) => {
    const { dispatch } = this.props;
    const modal = Modal.confirm();
    modal.update({
      title: '受理提示',
      okText: '确定',
      centered: true,
      cancelText: '返回',
      content: (
        <Text> 是否确定要受理</Text>
      ),
      onOk:()=> {
        dispatch({
          type: 'global/fetch_fileAccept',
          payload: {
            id,
            flag: true,
            callback: (res) => {
              if (res.status === 0) {
                this.getListAll();
              }
            }
          }
        });
        modal.destroy();
      },
      onCancel:()=>{
        modal.destroy();
      },
    });
  };

  // 点击忽略
  fileHandleAcceptClickFalse = (id) => {
    const { dispatch } = this.props;
    const modal = Modal.confirm();
    modal.update({
      title: '忽略提示',
      okText: '确定',
      centered:true,
      cancelText: '返回',
      content: (
        <Text> 是否确定要忽略</Text>
      ),
      onOk:()=> {
        dispatch({
          type: 'global/fetch_fileAccept',
          payload: {
            id,
            flag: false,
            callback: (res) => {
              if (res.status === 0) {
                this.getListAll();
              }
            }
          }
        })
      },
      onCancel: () => {
        modal.destroy();
      },
    })
  };

  // 案卷历史按钮名字
  historyButtonName = (type) => {
    switch(type) {
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
    console.log(res);
    switch(type) {
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
        onCancel={()=> this.setState({ workPathVisible: false})}
        footer={null}
      >
        <Fragment>
          <Row span={24} >
            <Col
              span={12}
              classNmae={ workButton.length > 2 ? style.workPathModal_big : style.workPathModal_small}
              style={{ textAlign: "center" }}
            >
              <Icon
                type="play-circle"
                style={{
                  fontSize: 83,
                  textAlign: 'center',
                }}
                className={ workButton.length > 2 ? style.workPathModalIcon_big : style.workPathModalIcon_small }
              />
            </Col>
            <Col span={12}>
              {
                workButton.length >=1 && workButton.map((itemStyle, index) => {
                  return (
                    <Col span={24} style={{ paddingLeft: 81 }} key={index}>
                      <Button
                        value={JSON.stringify(itemStyle)}
                        type={itemStyle.name === '提交'? 'primary' : ''}
                        onClick={(e)=>{this.workPathButtonClick(e.target.value)}}
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
      payload:{
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

  // 预览大图
  previewModal = () => {
    const { previewModalVisible, imageNewUrl } = this.state;
    return (
      <Modal
        visible={previewModalVisible}
        title="预览大图"
        width={1250}
        centered
        className={style.previewModalWarp}
        key={8000}
        onCancel={() => this.setState({previewModalVisible : false, imageNewUrl: null})}
        footer={null}
      >
        <div
          style={{
            width:'100%',
            height:824,
            background:`url('${getUrl(imageNewUrl)}') center center / contain no-repeat`,
            // backgroundRepeat: 'no-repeat',
            // backgroundSize: 'contain',
            // backgroundPosition: 'center',
          }}
        />
      </Modal>
    )
  };

  fileItemModel= () => {
    const { fileItemModelVisible, image, video } = this.state;
    const { form: { getFieldDecorator }, global: { fileItemData, fileLog_list}} = this.props;
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
          <Row  type='flex'>
            <Col className={style.addFileTextLeft} >
              <Form  className={style.formWarp}>
                <Row style={{ textAlign: 'center'}}>
                  <Button
                    style={{ margin: 5, backgroundColor: '#E5E5E5', color: "rgba(102,102,102,1)" }}
                    onClick={() => this.setState({ fileItemModelVisible: false })}
                  >
                    关闭
                  </Button>
                  {
                    fileItemData && fileItemData.button.map( (item, index) => {
                      return (
                        <Button
                          type='primary'
                          style={{ margin: 5}}
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
                  <Col span={1} offset={23} style={{ marginLeft: "93.833333%"}}>
                    <Text
                      style={{ margin: 5, cursor: "pointer" }}
                      onClick={() => {
                        if (fileItemData && fileItemData.workId) {
                          this.IconRight_click(fileItemData.workId);
                          this.setState({disposal_list_Model_Visible: false})
                        }
                      }}
                    >
                      <img src={file_del} alt="删除"/>
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
                        <Input disabled style={{ color: "#000" }}/>
                      )}
                    </Form.Item>
                    <Form.Item
                      {...formItemLayout}
                      label='案卷小类'
                    >
                      {getFieldDecorator('type', {
                        initialValue: fileItemData && fileItemData.type,
                      })(
                        <Input disabled style={{ color: "#000" }}/>
                      )}
                    </Form.Item>
                    <Form.Item
                      {...formItemLayout}
                      label='发生地点'
                    >
                      {getFieldDecorator('address', {
                        initialValue: fileItemData && fileItemData.address,
                      })(
                        <Input disabled style={{ color: "#000" }}/>
                      )}
                    </Form.Item>
                    <Form.Item
                      label='状态'
                      {...formItemLayout}
                    >
                      {getFieldDecorator('status',{
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
                        <Input disabled style={{ color: "#000" }}/>
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
                        <Input disabled style={{ color: "#000" }}/>
                      )}
                    </Form.Item>
                    <Form.Item
                      label='紧急程度'
                      {...formItemLayout}
                    >
                      {getFieldDecorator('workType',{
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
                        <Input disabled style={{ color: "#000" }}/>
                      )}
                    </Form.Item>
                    <Form.Item
                      {...formItemLayout}
                      label='创建时间'
                    >
                      {getFieldDecorator('createTime', {
                        initialValue: fileItemData && fileItemData.createTime,
                      })(
                        <Input disabled style={{ color: "#000" }}/>
                      )}
                    </Form.Item>
                    <Form.Item
                      {...formItemLayout}
                      label='处置前描述'
                    >
                      {getFieldDecorator('desc', {
                        initialValue: fileItemData && fileItemData.desc,
                      })(
                        <TextArea rows={2} style={{ resize: 'none', color: '#000'}} disabled/>
                      )}
                    </Form.Item>
                  </Col>
                </Row>
                <Row span={24} style={{ marginBottom: '20px'}}>
                  <Col span={12}  className={style.handle_CarouselWarp}>
                    <span className={style.newWelcomeTitle} >处置前图片：</span>
                    <Col span={24}  className={`${style.welcomeWarp} ${style.welcomeWarp_24}`}>
                      <Col span={2} offset={5}>
                        {
                          fileItemData && fileItemData.beforeImagePaths.length <= 1 ? (
                              <div
                                // src={noArrowsLeft}
                                className={styles.noLeft}
                              />
                            )
                            :
                            (
                              <div
                                // src={arrowsLeft}
                                onClick={()=> {
                                  this.refs.beforeImageWelcome.next()
                                }
                                }
                                className={styles.arrowsLeft}
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
                                  fileItemData && fileItemData.beforeImagePaths.map((item, index)=> {
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
                                            }, ()=> {
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
                            <img src={forImage} alt="" style={{width: 50, height: 50, marginTop: 70}}/>
                        }
                      </div>
                      <Col span={2}>
                        {
                          fileItemData && fileItemData.beforeImagePaths.length <= 1 ? (
                              <div
                                // src={noArrowsRight}
                                className={styles.noRight}
                              />
                            )
                            :
                            (
                              <div
                                // src={arrowsRight}
                                onClick={()=> {
                                  this.refs.beforeImageWelcome.next()
                                }
                                }
                                className={styles.arrowsRgiht}
                              />
                            )
                        }
                      </Col>
                    </Col>
                  </Col>
                  <Col span={12}  className={style.handle_CarouselWarp}>
                    <span className={style.newWelcomeTitle} >处置后图片：</span>
                    <Col span={24} className={`${style.welcomeWarp} ${style.welcomeWarp_24}`} >
                      <Col span={2} offset={5}>
                        {
                          image && image.length <= 1 ? (
                              <div
                                // src={noArrowsLeft}
                                className={styles.noLeft}
                              />
                            )
                            :
                            (
                              <div
                                // src={arrowsLeft}
                                onClick={()=> {
                                  console.log(this);
                                  this.refs.afterImageWelcome.prev()
                                }}
                                className={styles.arrowsLeft}
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
                                  image && image.map((item, index)=> {
                                    return(
                                      <div
                                        key={`div${index}`}
                                      >
                                        <img
                                          key={`image${index}`}
                                          alt=""
                                          style={{
                                            height: 185,
                                            width: 243,
                                            background: `url('${getUrl(item)}') center center / contain no-repeat`,
                                          }}
                                          onClick={(e) => {
                                            this.setState({
                                              imageNewUrl: item
                                            }, ()=> {
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
                            <img src={forImage} alt="" style={{width: 50, height: 50, marginTop: 70}}/>
                        }
                      </div>
                      <Col span={2}>
                        {
                          image && image.length <= 1 ? (
                              <div
                                // src={noArrowsRight}
                                className={styles.noRight}
                              />
                            )
                            :
                            (
                              <div
                                // src={arrowsRight}
                                onClick={()=> {
                                    console.log(this);
                                    this.refs.afterImageWelcome.next()
                                  }
                                }
                                className={styles.arrowsRgiht}
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
                      delay={[1,1000]}
                    >
                      {
                        this.state.imagePercent.map((item,index)=> {
                          if( item === 0 || item >= 100) {
                            return null
                          }else{
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
                  <Col span={12}  className={style.handle_CarouselWarp}>
                    <span className={style.newWelcomeTitle} >处置前视频：</span>
                    <Col span={24} className={`${style.welcomeWarp} ${style.welcomeWarp_24}`}>
                      <Col span={2} offset={5}>
                        {
                          fileItemData && fileItemData.beforeVideoPaths.length <= 1 ? (
                              <div
                                // src={noArrowsLeft}
                                // alt=""
                                className={styles.noLeft}
                              />
                            )
                            :
                            (
                              <div
                                // src={arrowsLeft}
                                // alt=""
                                onClick={()=> {
                                  this.refs.beforeVideoWelcome.next()
                                }
                                }
                                className={styles.arrowsLeft}
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
                                  fileItemData.beforeVideoPaths.map((item, index)=> {
                                    return <video width="245" height="180" controls="controls" key={index} src={getUrl(item)} />
                                  })
                                }
                              </Carousel>
                            )
                            :
                            <img src={forVideo} alt="" style={{width: 50, height: 50, marginTop: 70}}/>
                        }
                      </div>
                      <Col span={2}>
                        {
                          fileItemData && fileItemData.beforeVideoPaths.length <= 1 ? (
                              <div
                                // alt=""
                                // src={noArrowsRight}
                                className={styles.noRight}
                              />
                            )
                            :
                            (
                              <div
                                // alt=""
                                // src={arrowsRight}
                                onClick={()=> {
                                  this.refs.beforeVideoWelcome.next()
                                }
                                }
                                className={styles.arrowsRgiht}
                              />
                            )
                        }
                      </Col>
                    </Col>
                  </Col>
                  <Col span={12}  className={style.handle_CarouselWarp}>
                    <span className={style.welcomeTitle} >处置后视频：</span>
                    <Col span={24} className={`${style.welcomeWarp} ${style.welcomeWarp_24}`}>
                      <Col span={2} offset={5}>
                        {
                          video && video.length <= 1 ? (
                              <div
                                // alt=''
                                // src={noArrowsLeft}
                                className={styles.noLeft}
                              />
                            )
                            :
                            (
                              <div
                                // alt=''
                                // src={arrowsLeft}
                                onClick={()=> {
                                  console.log(this);
                                  this.refs.afterVideoWelcome.next()
                                }
                                }
                                className={styles.arrowsLeft}
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
                                  video.map((item, index)=> {
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
                            <img src={forVideo} alt="" style={{width: 50, height: 50, marginTop: 70}}/>
                        }
                      </div>
                      <Col span={2} >
                        {
                          video && video.length <= 1 ? (
                              <div
                                // alt=''
                                // src={noArrowsRight}
                                className={styles.noRight}
                              />
                            )
                            :
                            (
                              <div
                                // alt=''
                                // src={arrowsRight}
                                onClick={()=> {
                                  console.log(this);
                                  this.refs.afterVideoWelcome.next()
                                }
                                }
                                className={styles.arrowsRgiht}
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
                      delay={[1,1000]}
                    >
                      {
                        this.state.videoPercent.map((item,index)=> {
                          if( item === 0 || item >= 100) {
                            return null
                          }else{
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
              <span style={{color: '#000', fontSize: 15, fontWeight: 500, paddingTop: 5, display: 'block', marginLeft: '44%' }}>办理流程</span>
              {
                fileLog_list && fileLog_list.map( (item, index) => {
                  index += 1;
                  return (
                    <Col className={style.ColLogChildrenWarp} key={index}>
                      <div className={style.logIndex}>{index}</div>
                      <Col className={style.logContentWarp}>
                        <Row style={{ paddingTop: 5 }}>
                          <Text style={{ margin: 5, color: '#4C5FC1', textAlign:'left' }}>
                            {item.title}
                            <Text style={{float: 'right', marginRight: 8}}>{item.time && item.time}{item.timeType && returnTimeTypeText(item.timeType)}</Text>
                          </Text>
                          <Row type="flex" justify="space-between" style={{ margin: "0 5px"}}>
                            <Text style={{marginRight:  4}}>{item.username}</Text>
                            {
                              item.withdrawFlag && (
                                <Button
                                  size='small'
                                  type="primary"
                                  onClick={()=> this.fileWithdrawClick(item.todoId, fileItemData.workId, item.pathId)}
                                >
                                  撤回
                                </Button>
                              )
                            }
                          </Row>
                          <Text style={{ marginLeft: 8}}>{item.createTime}</Text>
                          {
                            !item.endflag ? (
                              <div className={style.logContentTop}>
                                {item.desc}
                              </div>
                            ): ""
                          }
                        </Row>
                        {
                          !item.endflag ? (
                            <Fragment>
                              {item.finishFlag ? "" : ( <div className={style.divBorderTop}> {/* */} </div>)}
                              <div
                                style={item.finishFlag ? {backgroundColor: '#EAEAEA', border: '#FFF', padding: 5} : {backgroundColor: '#FFF', padding: 5}}
                              >
                                <Text style={{fontSize: 12, color: "rgba(76,95,193,1)", whiteSpace: "nowrap"}}>下一办理人：</Text>{item.nextHandlers}
                              </div>
                            </Fragment>
                          ): ""
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
  handleDrawerSubmit = () => {
    const { form: { validateFields }, dispatch, global: {type_api}, type} = this.props;
    validateFields((err, values) => {
      this.setState({
        filter: {
          title: values.title || undefined,
          categoryName: values.categoryName || undefined,
          num: values.num || undefined,
          workType:  values.workType || undefined,
          source: values.drawerSource || undefined,
          status: values.drawerStatus || undefined,
          startTime: values.startTime && moment(values.startTime).format("YYYY/MM/DD HH:mm:ss") || undefined,
          endTime: values.endTime && moment(values.endTime).format("YYYY/MM/DD HH:mm:ss") || undefined
        }
      });
      let payload = {
        title: values.title || undefined,
        categoryName: values.categoryName || undefined,
        num: values.num || undefined,
        workType:  values.workType || undefined,
        source: values.drawerSource || undefined,
        status: values.drawerStatus || undefined,
        startTime: values.startTime && moment(values.startTime).format("YYYY/MM/DD HH:mm:ss") || undefined,
        endTime: values.endTime && moment(values.endTime).format("YYYY/MM/DD HH:mm:ss") || undefined,
        start: 0,
        count,
      };
      // this.getList(payload);
      dispatch({
        type: type === 'history/fetch_getFileHistoryList' ? type : type_api,
        payload: payload
      })
    })
  };

  // footer底部的搜索框
  handleFooterSubmit = (name) => {
    const { form: { validateFields }, type } = this.props;
    validateFields((err, values) => {
      this.setState({
        name,
      });
      let payload = {
        searchField: name,
        start: 0,
        count,
      };
      this.getList(payload);
    })
  };

  drawerForm = () => {
    const { type, form: { getFieldDecorator }, global: {type_api} } = this.props;
    const { endOpen } = this.state;
    return(
      <Form>
        <Form.Item
          label='名称'
          {...formDrawerLayout}
        >
          {getFieldDecorator('title',{})(
            <Input placeholder='请输入名称' />
          )}
        </Form.Item>
        <Form.Item
          label='类别名称'
          {...formDrawerLayout}
        >
          {getFieldDecorator('categoryName',{})(
            <Input placeholder='请输入类别名称' />
          )}
        </Form.Item>
        <Form.Item
          label='编号'
          {...formDrawerLayout}
        >
          {getFieldDecorator('num',{
          })(
            <Input placeholder='请输入编号' />
          )}
        </Form.Item>
        <Form.Item
          label='紧急程度'
          {...formDrawerLayout}
        >
          {getFieldDecorator('workType',{})(
            <Select placeholder="请选择紧急程度">
              <Option value='URGENT'>紧急</Option>
              <Option value='ORDINARY'>普通</Option>
            </Select>
          )}
        </Form.Item>
        {
          type_api !== 'global/fetch_getFileDisposalList' && (
            <Form.Item
              label='来源'
              {...formDrawerLayout}
            >
              {getFieldDecorator('drawerSource',{})(
                <Select placeholder="请选择来源">
                  <Option value='APP'>APP</Option>
                  <Option value='SYSTEM'>摄像机抓拍</Option>
                  <Option value='WEB'>管理员创建</Option>
                </Select>
              )}
            </Form.Item>
          )
        }
        {
          type_api !== 'global/fetch_getFileBounceList' && type_api !== 'global/fetch_getFileDisposalList' &&  type_api !== 'global/fetch_getFileInspectList' && (
            <Form.Item
              label='状态'
              {...formDrawerLayout}
            >
              {getFieldDecorator('drawerStatus',{})(
                <Select placeholder="请选择状态">
                  <Option value='ORDINARY'>正常</Option>
                  <Option value='TIME_OUT'>超时</Option>
                  <Option value='COMING_SOON_TIME_OUT'>即将超时</Option>
                </Select>
              )}
            </Form.Item>
          )
        }
        <Form.Item
          label='开始时间'
          {...formDrawerLayout}
        >
          {getFieldDecorator('startTime',{})(
            <DatePicker
              disabledDate={this.disabledStartDate}
              showTime
              format="YYYY-MM-DD HH:mm:ss"
              placeholder="请选择开始时间"
              onChange={this.onStartChange}
              onOpenChange={this.handleStartOpenChange}
            />
          )}
        </Form.Item>
        <Form.Item
          label='结束时间'
          {...formDrawerLayout}
        >
          {getFieldDecorator('endTime',{})(
            <DatePicker
              disabledDate={this.disabledEndDate}
              showTime
              format="YYYY-MM-DD HH:mm:ss"
              placeholder="请选择结束时间"
              onChange={this.onEndChange}
              open={endOpen}
              onOpenChange={this.handleEndOpenChange}
            />
          )}
        </Form.Item>
      </Form>
    );
  };

    componentWillUnmount() {
      this.props.dispatch({
        type: 'global/SET_DRAWER_VISIBLE',
        payload: false
      });
    }

  render() {
    const { dataSource, columns, filter, type, dispatch, global: { drawer_visible } } = this.props;
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
        <Table
          className={style.TableWarp}
          dataSource={dataSource && dataSource.value}
          rowSelection={rowSelection}
          columns={columns}
          rowKey={record => record.id}
          pagination={{position: 'none'}}
          onRow={record => {
            return {
              onClick: e => {
                switch(type) {
                 case 'global/fetch_getFileDisposalList':
                   this.handleOnRowClick(record.id, 2);
                   break;
                 case 'history/fetch_getFileHistoryList':
                   this.handleOnRowClick(record.id, 2, 'history');
                    break;
                  case 'global/fetch_getFileBounceList':
                   this.handleOnRowClick(record.id, 1, 'true');
                    break;
                 default:
                   this.handleOnRowClick(record.id, 1);
                   break;
                }
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
                  {
                    filter ?
                    (
                      <Text
                        disabled
                        style={{ fontSize: 16, marginLeft: 6 }}
                      >
                        筛选
                      </Text>
                    )
                    :
                    (
                      <Text
                        style={{ fontSize: 16, marginLeft: 6, cursor: 'pointer' }}
                        onClick={() => {
                          dispatch({
                            type: 'global/SET_DRAWER_VISIBLE',
                            payload: true
                          });
                          this.props.form.resetFields();
                        }}
                      >
                        筛选
                      </Text>
                    )
                  }
                  <Divider type="vertical" style={{ margin: '0px 5px' }} />
                  <Text
                    onClick={this.handleClickReset}
                    style={{ fontSize: 16, cursor: 'pointer' }}
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
        <Drawer
          title="筛选"
          placement="right"
          mask={false}
          width={350}
          className={style.DraweWarp}
          onClose={() => {
            dispatch({
              type: 'global/SET_DRAWER_VISIBLE',
              payload: false
            });
            this.props.form.resetFields()
          }}
          visible={drawer_visible}
        >
          {this.drawerForm()}
          <div
            style={{
              position: 'absolute',
              bottom: 0,
              width: '100%',
              borderTop: '1px solid #e8e8e8',
              padding: '10px 16px',
              textAlign: 'center',
              left: 0,
              background: '#fff',
              borderRadius: '0 0 4px 4px',
            }}
          >
            <Button
              style={{
                marginRight: 15,
              }}
              onClick={this.handleDrawerSubmit}
              type="primary"
            >
              查询
            </Button>
            <Button
              onClick={()=> {
                // dispatch({
                //   type: 'global/SET_DRAWER_VISIBLE',
                //   payload: false
                // });
                this.handleClickReset();
              }}
            >
              重置
            </Button>
          </div>
        </Drawer>
        {/* 弹窗*/}
        { this.state.unitTreeVisible && this.unitTree_Modal() }
        { this.state.workPathVisible && this.workPath_Modal() }
        { this.state.RETURN_OR_REVIEWVisible && this.RETURN_OR_REVIEW() }
        { this.state.TimeDelayVisible && this.TimeDelay_Modal() }
        { this.state.fileItemModelVisible && this.fileItemModel() }
        { this.state.previewModalVisible && this.previewModal() }
      </Fragment>
    );
  };
}
export default List;
