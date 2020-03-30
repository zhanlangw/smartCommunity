import React, { PureComponent, Fragment } from 'react';
import { Button, Row, Typography, Modal, Form, Input, Upload, Icon, Cascader, message, Card,Spin } from 'antd';
import { connect } from 'dva';
import MenuTable from '@/components/newMenuTable/List';
import {beforeUpload, payload_0_10, validator, validatorDes} from '@/utils/utils';
import { getUrl, token } from '@/utils/utils';
import style from './style.less';
import if_face from'@/assets/if_face.png';
import left_face from'@/assets/left_face.png';
import right_face from'@/assets/right_face.png';
import del from '@/assets/del.png';
import Ellipsis from '@/components/Ellipsis';
import addImage from '@/assets/addImage.png';

const { Text } = Typography;
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
@connect(({ blacklist, category, loading }) => ({
  blacklist,
  category,
  loading: loading.models.blacklist
}))
class index extends PureComponent {
  state = {
    blacklistAddOrUpdModelVisible: false,
    blacklistAddOrUpd: true,
    loading: false,
    imageUrl: null,
    options: [],
    bigNotId: false,
    imageModelVisible: false,
  };

  componentDidMount () {
    const { dispatch, blacklist: { blacklist_item } } = this.props;
    const timeOptions = [];
    dispatch({
      type: 'blacklist/fetch_getBlacklistList',
      payload: { ...payload_0_10 },
    });

    dispatch({
      type: 'category/fetch_getBigCategoryList',
      callback: res => {
        if(res.status === 0) {
          res && res.value.map(item => {
            let payload = {
              value: item.id,
              label: item.name,
              isLeaf: false,
            };
            timeOptions.push(payload)
          });
          dispatch({
            type: 'blacklist/GET_OPTIONS_LIST',
            payload: timeOptions,
          })
        }
      }
    })
  }

  handleCascaderOnChange = (e, item) => {
    if(this.state.bigNotId){
      let typeId = e[e.length-1];
      this.setState({
        typeId
      })
    }
  };

  handelBlacklistAddOrUpd = () => {
    const {
      blacklistAddOrUpd,
      imageUrl,
      imageLeftSideUrl ,
      imageRightSideUrl,
      typeId,
      addImageUrl,
      addImageRightSideUrl,
      addImageLeftSideUrl
    } = this.state;
    const { form: { validateFields, resetFields }, dispatch } = this.props;
    validateFields((err, payload) => {
      if (!err) {
        if (!imageUrl) {
          message.error('请上传图片')
        } else {
          if (typeId) {
            if (blacklistAddOrUpd) {
              payload.typeId = typeId;
              payload.imagePath = addImageUrl;
              payload.leftImagePath = addImageRightSideUrl;
              payload.rightImagePath = addImageLeftSideUrl;
              delete payload.newImagePath;
              dispatch({
                type: 'blacklist/fetch_addBlacklistItem',
                payload,
                callback: res => {
                  if (res.status === 0) {
                    this.setState({
                      blacklistAddOrUpdModelVisible: false,
                      state: 1
                    }, () => {
                      this.onRef.setState({
                        start: 1
                      })
                      resetFields();
                      this.setState({
                        imageUrl: null,
                        imageRightSideUrl: null,
                        imageLeftSideUrl: null,
                        addImageUrl: null,
                        addImageRightSideUrl: null,
                        addImageLeftSideUrl: null
                      })
                    })
                  }
                }
              })
            } else {
              payload.typeId = typeId;
              payload.imagePath = imageUrl;
              imageLeftSideUrl ? payload.leftImagePath = imageLeftSideUrl : delete payload.leftImagePath;
              imageRightSideUrl ? payload.rightImagePath = imageRightSideUrl : delete payload.rightImagePath;
              delete payload.newImagePath;
              dispatch({
                type: 'blacklist/fetch_updBlacklistItem',
                payload,
                callback: res => {
                  if (res.status === 0) {
                    this.setState({
                      blacklistAddOrUpdModelVisible: false,
                    }, () => {
                      this.onRef.setState({
                        start: 1
                      })
                      resetFields();
                      this.setState({
                        imageUrl: null,
                        imageRightSideUrl: null,
                        imageLeftSideUrl: null,
                        addImageUrl: null,
                        addImageRightSideUrl: null,
                        addImageLeftSideUrl: null
                      })
                    })
                  }
                }
              })
            }
          } else {
            message.error('请选择正确的案卷类型')
          }

        }
      }
    })
  };

  fileChange = info => {
    let fileList = [...info.fileList];
    fileList = fileList.slice(-2);
    fileList = fileList.map(file => {
      if (file.response) {
        // Component will show file.url as link
        file.url = file.response.url;
      }
      return file;
    });

    if (info.file.status === 'uploading') {
      this.setState({ loading: true });
      return;
    }
    if (info.file.status === 'done') {
      this.setState({
        imageUrl: info.file.response.value,
        addImageUrl: info.file.response.value,
        loading: false,
      })
    }
  };

  fileChangeLeftSide = info => {
    let fileList = [...info.fileList];
    fileList = fileList.slice(-2);
    fileList = fileList.map(file => {
      if (file.response) {
        // Component will show file.url as link
        file.url = file.response.url;
      }
      return file;
    });

    if (info.file.status === 'uploading') {
      this.setState({ loadingL: true });
      return;
    }
    if (info.file.status === 'done') {
      this.setState({
        imageLeftSideUrl: info.file.response.value,
        addImageLeftSideUrl: info.file.response.value,
        loadingL: false,
      })
    }
  };

  fileChangeRightSide = info => {
    let fileList = [...info.fileList];
    fileList = fileList.slice(-2);
    fileList = fileList.map(file => {
      if (file.response) {
        // Component will show file.url as link
        file.url = file.response.url;
      }
      return file;
    });

    if (info.file.status === 'uploading') {
      this.setState({ loadingR: true });
      return;
    }
    if (info.file.status === 'done') {
      this.setState({
        imageRightSideUrl: info.file.response.value,
        addImageRightSideUrl: info.file.response.value,
        loadingR: false,
      })
    }
  };

  handleCascaderLoadData = selectedOptions => {
    const { dispatch, blacklist } = this.props;
    dispatch({
      type: 'category/fetch_getSmallCategoryList',
      payload: {
        start: 0,
        count: 100,
        id: selectedOptions[selectedOptions.length - 1].value,
      },
      callback: res => {
        if(res.status === 0 ) {
          let targetOption = selectedOptions[selectedOptions.length - 1];
          let options = blacklist.options_list;
          let arr = [];
          let has = options.map(item=> {
            if (item.value === targetOption.value){
              if (res && res.value.totalCount) {
                this.setState({
                  bigNotId: true
                });
                res && res.value.value.map(item => {
                  let payload = {
                    value: item.id,
                    label: item.name,
                  };
                  arr.push(payload)
                });
                item.children = arr;
              }else {
                this.setState({
                  bigNotId: false,
                  typeId: undefined
                })
              }
            }
            return item
          });
          dispatch({
            type: 'blacklist/GET_OPTIONS_LIST',
            payload: has,
          })
        }
      }
    })
  };


  blacklistAddOrUpdModel = () => {
    const {
      blacklistAddOrUpdModelVisible,
      blacklistAddOrUpd,
      imageUrl,
      imageRightSideUrl,
      imageLeftSideUrl,
      addImageUrl,
      addImageRightSideUrl,
      addImageLeftSideUrl
    } = this.state;
    const { form: { getFieldDecorator }, blacklist: { blacklist_item, options_list } } = this.props;
    let typeIds = [];
    if (blacklist_item && blacklist_item.type) {
      typeIds.push(blacklist_item.type.bigId);
      typeIds.push(blacklist_item.type.id);
    }
    const uploadButton = (
      <div style={{ position: 'relative'}}>
        <img src={if_face} alt="" style={{
          width:70,
          height:70,
          position: 'absolute',
          left: 0,
          bottom: 0,
          top: 0,
          right: 0,
          margin: 'auto',
        }}/>
        <img
          src={addImage}
          alt=""
          style={{
            width:50,
            height:50,
            position: 'absolute',
            left: 0,
            bottom: 0,
            top: 0,
            right: 0,
            margin: 'auto',
          }}
        />
      </div>
    );
    const uploadLeftButton = (
      <div style={{ position: 'relative'}}>
        <img src={left_face} alt="" style={{
          width:70,
          height:70,
          position: 'absolute',
          left: 0,
          bottom: 0,
          top: 0,
          right: 0,
          margin: 'auto',
        }}/>
        <img
          src={addImage}
          alt=""
          style={{
            width:50,
            height:50,
            position: 'absolute',
            left: 0,
            bottom: 0,
            top: 0,
            right: 0,
            margin: 'auto',
          }}
        />
      </div>
    );
    const uploadRightButton = (
      <div style={{ position: 'relative'}}>
        <img src={right_face} alt="" style={{
          width:70,
          height:70,
          position: 'absolute',
          left: 0,
          bottom: 0,
          top: 0,
          right: 0,
          margin: 'auto',
        }}/>
        <img
          src={addImage}
          alt=""
          style={{
            width:50,
            height:50,
            position: 'absolute',
            left: 0,
            bottom: 0,
            top: 0,
            right: 0,
            margin: 'auto',
          }}
        />
      </div>
    );
    return (
      <Modal
        title={blacklistAddOrUpd ? '新建黑名单' : '修改黑名单'}
        visible={blacklistAddOrUpdModelVisible}
        footer={[
          <Button
            key="submit"
            type="primary"
            onClick={this.handelBlacklistAddOrUpd}
            style={{marginRight: 15}}
            loading={this.props.loading}
          >
            确定
          </Button>,
          <Button
            key="back"
            loading={this.props.loading}
            onClick={() => this.setState({
              blacklistAddOrUpdModelVisible: false,
              addImageUrl: undefined,
              addImageRightSideUrl: undefined,
              addImageLeftSideUrl: undefined})
            }
          >
            取消
          </Button>,
        ]}
        onCancel={() =>{
          if (this.props.loading) {
            return false;
          } else {
            this.setState({
              blacklistAddOrUpdModelVisible: false,
              addImageUrl: undefined,
              addImageRightSideUrl: undefined,
              addImageLeftSideUrl: undefined
            })
          }
        }}
      >
        <Form>
          {
            blacklistAddOrUpd ? '' : (
              <Form.Item {...formItemLayout} label='id' style={{ display: "none" }}>
                {getFieldDecorator('id', {
                  initialValue: blacklistAddOrUpd ? undefined : blacklist_item && blacklist_item.id || undefined
                })(
                  <Input />,
                )}
              </Form.Item>
            )
          }
          <Form.Item {...formItemLayout} label='名称'>
            {getFieldDecorator('name', {
              rules: [
                {required: true, message: '请输入名称'},
                {
                  validator:validator,
                }
              ],
              initialValue: blacklistAddOrUpd ? undefined : blacklist_item && blacklist_item.name || undefined
            })(
              <Input placeholder='请输入名称'/>,
            )}
          </Form.Item>
          <Form.Item {...formItemLayout} label='案卷类型'>
            {getFieldDecorator('type', {
              rules: [
                {required: true, message: '请选择案卷类型'},
              ],
              initialValue: blacklistAddOrUpd ? undefined : typeIds && typeIds || undefined
            })(
              <Cascader
                placeholder='请选择案卷类型'
                options={options_list && options_list}
                loadData={this.handleCascaderLoadData}
                onChange={this.handleCascaderOnChange}
                changeOnSelect
              />
            )}
          </Form.Item>
          <Form.Item {...formItemLayout} label='图片'>
            <Upload
              listType="picture-card"
              // className="avatar-uploader"
              onChange={this.fileChange}
              name="file"
              accept=".jpg,.jpeg,.png"
              action={getUrl("/api/file/upload?fileType=5")}
              showUploadList={false}
              beforeUpload={beforeUpload}
              headers={{
                "Authorization": token,
              }}
            >
              {blacklistAddOrUpd ? addImageUrl ? <img src={getUrl(addImageUrl)} alt="avatar" style={{ width: 104, height: 104 }} /> : uploadButton : <img src={imageUrl ? getUrl(imageUrl) : blacklist_item && getUrl(blacklist_item.imagePath) } alt="avatar" style={{ width: 104, height: 104 }} />}
            </Upload>
          </Form.Item>
          <Form.Item {...formItemLayout} label='侧面'  style={{position: "relative"}}>
            <Row type='flex'>
              <Upload
                listType="picture-card"
                // className="avatar-uploader"
                onChange={this.fileChangeLeftSide}
                name="file"
                accept=".jpg,.jpeg,.png"
                action={getUrl("/api/file/upload?fileType=5")}
                showUploadList={false}
                beforeUpload={beforeUpload}
                headers={{
                  "Authorization": token,
                }}
              >
                {blacklistAddOrUpd ? addImageLeftSideUrl ? <img src={getUrl(addImageLeftSideUrl)} alt="avatar" style={{ width: 104, height: 104 }} /> : uploadLeftButton : imageLeftSideUrl ? <img src={getUrl(imageLeftSideUrl)} alt="avatar" style={{ width: 104, height: 104 }} /> : uploadLeftButton}
              </Upload>
              <img
                src={del} alt=""
                className={style.leftClose}
                onClick={()=> {
                  this.setState({
                    addImageLeftSideUrl: null,
                    imageLeftSideUrl: null
                  })
                }}
              />
              <Upload
                listType="picture-card"
                // className="avatar-uploader"
                onChange={this.fileChangeRightSide}
                name="file"
                accept=".jpg,.jpeg,.png"
                action={getUrl("/api/file/upload?fileType=5")}
                showUploadList={false}
                beforeUpload={beforeUpload}
                headers={{
                  "Authorization": token,
                }}
              >
                {blacklistAddOrUpd ? addImageRightSideUrl ? <img src={getUrl(addImageRightSideUrl)} alt="avatar" style={{ width: 104, height: 104 }} /> : uploadRightButton : imageRightSideUrl ? <img src={getUrl(imageRightSideUrl)} alt="avatar" style={{ width: 104, height: 104 }} /> : uploadRightButton}
              </Upload>
              <img
                src={del} alt=""
                className={style.rightClose}
                onClick={()=> {
                  this.setState({
                    addImageRightSideUrl: null,
                    imageRightSideUrl: null
                  })
                }}
              />
            </Row>
          </Form.Item>
          <Form.Item {...formItemLayout} label='描述'>
            {getFieldDecorator('desc', {
              rules: [
                {
                  validator:validatorDes,
                }
              ],
              initialValue: blacklistAddOrUpd ? undefined : blacklist_item && blacklist_item.desc || undefined
            })(
              <TextArea rows={4} style={{ resize: 'none' }} placeholder='请描述'/>
            )}
          </Form.Item>
        </Form>
      </Modal>
    )
  };

  handleBlacklistListUpdButton = (id) => {
    const { dispatch } = this.props;
    this.setState({
      blacklistUpdId: id,
      blacklistAddOrUpd: false,
      blacklistAddOrUpdModelVisible: true
    });
    dispatch({
      type: 'blacklist/fetch_getBlacklistItem',
      payload: {
        id
      },
      callback: res => {
       if (res.status === 0) {
         this.setOnRef
         this.setState({
           typeId: res.value.type.id,
           imageUrl:res.value.imagePath,
           imageLeftSideUrl: res.value ? res.value.leftImagePath : undefined,
           imageRightSideUrl: res.value ? res.value.rightImagePath: undefined
         })
       }
      }
    })
  };

  imageModel = () => {
    const { imageModelVisible, image } = this.state;
    return (
      <Modal
        visible={imageModelVisible}
        title="预览大图"
        width={1250}
        style={{top: 20}}
        maskClosable
        className={style.previewModalWarp}
        key={8888}
        onCancel={() => this.setState({imageModelVisible : false, image: null})}
        footer={null}
      >
        <div
          style={{
            width:'100%',
            height:824,
            background:`url(${getUrl(image)}) center center / contain no-repeat`,
            // backgroundRepeat: 'no-repeat',
            // backgroundSize: 'contain',
            // backgroundPosition: 'center',
          }}
        />
      </Modal>
    )
  };

  setOnRef = (ref) => {
    this.onRef = ref
  };

  handleClickReset = () => {
    this.onRef.handleClickReset()
  };

  render () {
    const { blacklist: { blacklist_list }, loading } = this.props;
    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name',
        width:110,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '案卷类型',
        dataIndex: 'type',
        key: 'type',
        width:110,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '人脸头像',
        dataIndex: 'imagePath',
        key: 'imagePath',
        width: 100,
        render: (text, record) => (
          <Card
            style={{ width: '40px', height: '30px', marginTop: -8, marginLeft: -3 }}
            bodyStyle={{ padding: 0}}
            onClick={() => {
              text && this.setState({ imageModelVisible: true, image: text })
            }}
          >
            <img src={getUrl(text)} alt="" style={{ width: '40px', height: '30px' }}/>
          </Card>
        ),
      },
      {
        title: '左侧面',
        dataIndex: 'leftImagePath',
        key: 'leftImagePath',
        width: 100,
        render: (text, record) => (
          <Card
            style={{ width: '40px', height: '30px', marginTop: -8, marginLeft: -3 }}
            bodyStyle={{ padding: 0}}
            onClick={() => {
              text && this.setState({ imageModelVisible: true, image: text })
            }}
          >
            <img src={getUrl(text)} alt="" style={{ width: '40px', height: '30px' }}/>
          </Card>
        ),
      },
      {
        title: '右侧面',
        dataIndex: 'rightImagePath',
        key: 'rightImagePath',
        width: 100,
        render: (text, record) => (
          <Card
            style={{ width: '40px', height: '30px', marginTop: -8, marginLeft: -3 }}
            bodyStyle={{ padding: 0}}
            onClick={() => {
              text && this.setState({ imageModelVisible: true, image: text })
            }}
          >
            <img src={getUrl(text)} alt="" style={{ width: '40px', height: '30px' }}/>
          </Card>
        ),
      },
      {
        title: '描述',
        dataIndex: 'desc',
        key: 'desc',
        width:150,
        render: val => <Ellipsis lines={1}>{val}</Ellipsis>,
      },
      {
        title: '操作',
        dataIndex: 'id',
        key: 'id',
        width:100,
        render: (text, record) => (
          <Button
            type='primary'
            onClick={() => this.handleBlacklistListUpdButton(text)}
          >
            修改
          </Button>
        ),
      }
    ];
    return (
      <Fragment>
        <Button
          type='primary'
          style={{ margin: '12px 10px 8px' }}
          onClick={() => this.setState({ blacklistAddOrUpdModelVisible: true, blacklistAddOrUpd: true })}
        >
          新建黑名单
        </Button>
        <Spin
          spinning={loading}
        >
          <MenuTable
            columns={columns}
            dataSource={blacklist_list}
            type='blacklist/fetch_getBlacklistList'
            delButton='blacklist/fetch_delBlacklistItem'
            onRef={this.setOnRef}
            filter={true}
          />
        </Spin>
        { this.state.blacklistAddOrUpdModelVisible && this.blacklistAddOrUpdModel() }
        { this.state.imageModelVisible && this.imageModel() }
      </Fragment>
    );
  }
}
export default index;
