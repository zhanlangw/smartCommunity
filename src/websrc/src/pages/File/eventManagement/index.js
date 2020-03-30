import React, { PureComponent, Fragment } from 'react';
import { connect } from 'dva';
import { Row, Col, Button, Icon, Modal, Form, Input, Card, Badge, Typography, Tooltip } from 'antd';
import MenuTable from '@/components/newMenuTable/List';
import { getUrl, payload_0_10 as payload, returnWorkTypeStyle, returnWorkTypeText } from "@/utils/utils";
import styles from "@/pages/Management/BlackList/style.less";
import Ellipsis from "@/components/Ellipsis";


const { TextArea } = Input;
const { Text, Paragraph } = Typography;

@Form.create()
@connect(({ event }) => ({
  event
}))
class Index extends PureComponent {
  constructor() {
    super()
    this.state = {
      imageModelVisible: false,
    };

  }


  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: 'event/fetch_getFileEventList',
      payload,
    })
  }

  imageModel = () => {
    const { imageModelVisible, image } = this.state;
    return (
      <Modal
        title="高清大图"
        width={1250}
        style={{ top: 30 }}
        maskClosable
        className={styles.previewModalWarp}
        onCancel={() => this.setState({ imageModelVisible: false })}
        visible={imageModelVisible}
        footer={null}
      >
        <div
          style={{
            width: '100%',
            height: 824,
            background: `url(${getUrl(image)}) center center / contain no-repeat`,
            // backgroundRepeat: 'no-repeat',
            // backgroundSize: 'contain',
            // backgroundPosition: 'center',
          }}
        />
      </Modal>
    )
  };

  // 点击受理
  fileHandleAcceptClickTrue = (id) => {
    const { dispatch } = this.props;
    const modal = Modal.confirm();
    modal.update({
      title: '受理提示',
      okText: '确定',
      cancelText: '返回',
      centered: true,
      content: (
        <Text> 是否确定要受理</Text>
      ),
      onOk: () => {
        dispatch({
          type: 'global/fetch_fileAccept',
          payload: {
            id,
            flag: true
          },
          callback: res => {
            if (res.status === 0) {
              this.onRef.onChange(1)
              dispatch({
                type: 'event/fetch_getFileEventList',
                payload,
              })
            }
          }
        });
        modal.destroy();
      },
      onCancel: () => {
        modal.destroy();
      },
    });
  };

  // 点击忽略
  fileHandleAcceptClickFalse = (id) => {
    let that = this;
    const { dispatch } = this.props;
    const modal = Modal.confirm();
    modal.update({
      title: '忽略提示',
      okText: '确定',
      cancelText: '返回',
      centered: true,
      content: (
        <Text>是否确定要忽略</Text>
      ),
      onCancel: () => {
        modal.destroy();
      },
      onOk: () => {
        dispatch({
          type: 'global/fetch_fileAccept',
          payload: {
            id,
            flag: false,
          },
          callback: res => {
            if (res.status === 0) {
              this.onRef.onChange(1)
              dispatch({
                type: 'event/fetch_getFileEventList',
                payload,
              })
            }
          }
        });
      },
    })
  };
  setOnRef = (ref) => {
    // console.log("00000000",ref);
    this.onRef = ref
  };

  handleClickReset = () => {
    this.onRef.handleClickReset()
  };

  render() {
    const { event: { event_list } } = this.props;
    const columns = [
      {
        title: '名称',
        dataIndex: 'title',
        key: 'title',
        width: 300,
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
        title: '主体名称',
        dataIndex: 'blacklistName',
        key: 'blacklistName',
        width: 100,
      },
      {
        title: '案卷小类',
        dataIndex: 'categoryName',
        key: 'categoryName',
        width: 110,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '紧急程度',
        dataIndex: 'workType',
        key: 'workType',
        width: 100,
        render: val => <Ellipsis lines={1}><span style={returnWorkTypeStyle(val)}>{returnWorkTypeText(val)}</span></Ellipsis>,
      },
      {
        title: '现场图',
        dataIndex: 'image',
        key: 'image',
        width: 70,
        render: (text, record) => (
          <Card
            style={{ width: '40px', height: '30px', marginTop: -8, marginLeft: -3 }}
            bodyStyle={{ padding: 0 }}
            onClick={() => {
              this.setState({ imageModelVisible: true, image: text })
            }}
          >
            <img src={getUrl(text)} alt="" style={{ width: '40px', height: '30px' }} />
          </Card>
        ),
      },
      {
        title: '发生地点',
        dataIndex: 'address',
        key: 'address',
        width: 130,
        render: val => <Ellipsis lines={1}><Tooltip title={val}><span>{val}</span></Tooltip></Ellipsis>,
      },
      {
        title: '创建时间',
        dataIndex: 'createTime',
        key: 'createTime',
        width: 150,
        render: val => <Ellipsis lines={1}><Tooltip title={val}><span>{val}</span></Tooltip></Ellipsis>,
      },
      {
        title: '操作',
        dataIndex: 'id',
        key: 'id',
        width: 140,
        render: (text, record) => (
          <span>
            <Button
              type='primary'
              style={{ marginRight: 10 }}
              onClick={() => this.fileHandleAcceptClickTrue(text)}
            >
              受理
            </Button>
            <Button
              onClick={() => this.fileHandleAcceptClickFalse(text)}
            >
              忽略
            </Button>
          </span>
        ),
      }
    ];
    return (
      <div style={{ backgroundColor: " #ffffff", minHeight: "91vh" }}>
        <MenuTable
          columns={columns}
          dataSource={event_list}
          type='event/fetch_getFileEventList'
          delButton='global/fetch_delHasFiles'
          onRef={this.setOnRef}
          triggerRef={this.bindRef}
        />
        {this.imageModel()}
      </div>
    );
  }
}

export default Index
