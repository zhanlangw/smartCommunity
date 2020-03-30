import React, {Component,Fragment} from 'react';
import {connect} from 'dva';
import {
  Row,
  Col,
  Button,
  Icon,
  Modal,
  Form,
  Input,
  Typography,
  Upload,
  Carousel,
  message,
  Cascader,
  Progress,
  Card
} from 'antd';
import style from "@/pages/File/newMenuTable/List.less";
import file_del from "@/assets/file_del.png";
import TreeSelect from '@/components/TreeSelect';
import {beforeUpload, getUrl, payload_0_10, validator, validatorDes} from "@/utils/utils";
import request from '@/utils/request';
import QueueAnim from 'rc-queue-anim';
import forImage from '@/assets/hasImage.png';
import forVideo from '@/assets/hasVideo.png';
import noArrowsLeft from "@/assets/noLeft.png";
import arrowsLeft from "@/assets/left.png";
import noArrowsRight from "@/assets/noRight.png";
import arrowsRight from "@/assets/right.png";
import del from '@/assets/del.png';
import { getAuthority } from '@/utils/authority';

const {TextArea} = Input;
const { Text, Title } = Typography;
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
class Index extends Component {
  constructor(...args) {
    super(...args);
  }

  state = {
    addFileModalVisible: false,
    path:null,
    data:null,
    previewModalVisible: false,
    image: [],
    video: [],
    typeId: null,
    workType: null,
    imagePercent: [],
    videoPercent:[],
    timelineOptions: []
  };

  // componentWillMount() {
  //   const {dispatch} = this.props;
  //
  // }

  handleCascaderOnChange = (e) => {
    if(this.state.bigNotId){
      let typeId = e[e.length-1];
      this.setState({
        typeId
      })
    }
  };

  handleCascaderLoadData = selectedOptions => {
    const { dispatch } = this.props;
    const { timelineOptions } = this.state;
    let params = {
      start: 0,
      count: 100,
      id: selectedOptions[selectedOptions.length - 1].value,
    };
    return request(`/api/smallcategory/list`, {params, headers:{ 'Authorization': getAuthority().token}})
      .then(res => {
        if(res.status === 0 ) {
          const targetOption = selectedOptions[selectedOptions.length - 1];
          let options = timelineOptions;
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
         this.setState({
           timelineOptions: has
         })
        }
      })
  };

  fileImageChange = (info) => {
    const { file, event, fileList } = info;
    let imagePercent= [];
    if (file.response && file.response.status === 0) {
      fileList.map(item=>{
        imagePercent.push(item.percent.toFixed(2))
      });
      this.setState({
				image: this.state.image.concat(encodeURI(file.response.value)) || {},
        imagePercent
      }, () => {
        console.log(this.state.image);
        console.log(this.state.imagePercent);
      });
    }
  };

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

  imageDelUploadFile = (index) => {
    const {image} = this.state;
    console.log(index);
    let arr = image;
    arr.splice(index, 1);
    this.setState({
      image: arr
    })
  };

  videoDelUploadFile = (index) => {
    const {video} = this.state;
    let arr = video;
    arr.splice(index, 1);
    this.setState({
      video: arr
    })
  };

  handleSubmit = () => {
    const { form: { validateFields }, dispatch } = this.props;
    const { image, video, typeId, unitId }  = this.state;
    validateFields((err, payload) => {
        payload.typeId = typeId;
        payload.unitId = unitId;
      if (!err) {
        payload.beforeImagePaths = image;
        payload.beforeVideoPaths = video;
        payload.source = 'WEB';
        if (payload.type.length >= 2) {
          if (image.length >= 1) {
            dispatch({
              type: 'global/fetch_addFile',
              payload,
              callback: () => {
                dispatch({
                  type: 'global/fetch_getFileHandleList',
                  payload: {
                    ...payload_0_10,
                  },
                  callback: () => {
                    this.setState({
                      visibleModal: false,
                      image: [],
                      video: []
                    });
                    this.props.form.resetFields();
                  }
                })
              }
            })
          } else {
            message.error('请选择您要上传的文件')
          }
        } else {
          message.error('请选择正确的案卷类型')
        }
      }

    });
  };

  previewModal = () => {
    const { previewModalVisible, imageUrl } = this.state;
    return (
      <Modal
        visible={previewModalVisible}
        title="预览大图"
        width={1250}
        centered
        className={style.previewModalWarp}
        key={8000}
        onCancel={() => this.setState({previewModalVisible : false, imageUrl: null})}
        footer={null}
      >
        <div
          style={{
            width:'100%',
            height:824,
            background:`url(${getUrl(imageUrl)})`,
            backgroundRepeat: 'no-repeat',
            backgroundSize: 'contain',
            backgroundPosition: 'center',
          }}
        />
      </Modal>
    )
  };

  addFileModal = () => {
    const { form: { getFieldDecorator } , blacklist } = this.props;
    const { visibleModal, image, video, timelineOptions } = this.state;
    return (
      <Fragment>
        <Modal
          title="新建案卷"
          style={{ top: 20 }}
          visible={visibleModal}
          className={style.addFileTextWarp}
          onCancel={() => this.setState({ visibleModal: false })}
          width={1250}
          footer={null}
        >
          <Row type='flex'>
            <Col className={style.addFileTextLeft}>
              <Form>
                <Row style={{ textAlign: 'center'}}>
                  <Button
                    style={{ margin: 5, backgroundColor: '#E5E5E5', color: "rgba(102,102,102,1)" }}
                    onClick={() => this.setState({ visibleModal: false })}
                  >
                    关闭
                  </Button>
                  <Button type="primary" style={{ margin: 5 }} onClick={this.handleSubmit} loading={this.props.loading}>提交</Button>
                </Row>
                <Row>
                  <Col span={12}>
                    <Form.Item
                      {...formItemLayout}
                      label='标题'
                    >
                      {getFieldDecorator('title', {
                        rules:[{required: true, message: '请输入标题'}, {max: 40, message: '不能超过40个字符'}],
                        getValueFromEvent: (event) => {
                          return event.target.value.replace(' ','')
                        },
                      })(
                        <Input placeholder='请输入标题'/>
                      )}
                    </Form.Item>
                    <Form.Item
                      {...formItemLayout}
                      label='案卷类型'
                    >
                      {getFieldDecorator('type', {
                        rules:[{required: true, message: '请选择案卷类型'}],
                      })(
                        <Cascader
                          placeholder='请选择案卷类型'
                          options={ blacklist && blacklist.options_list ?  blacklist.options_list : timelineOptions}
                          loadData={this.handleCascaderLoadData}
                          onChange={this.handleCascaderOnChange}
                          changeOnSelect
                        />
                      )}
                    </Form.Item>
                    <Form.Item
                      {...formItemLayout}
                      label='发生地点'
                    >
                      {getFieldDecorator('address', {
                        rules:[
                          {required: true, message: '请输入发生地点'},
                          {validator: validatorDes}
                        ],
                      })(
                        <Input placeholder='请输入发生地点'/>
                      )}
                    </Form.Item>
                  </Col>
                  <Col span={12}>
                    <Form.Item
                      {...formItemLayout}
                      label='所属街区'
                    >
                      {getFieldDecorator('unitId',{
                        rules:[{
                          required: true, message: '请选择所属街区'
                        }],
                      })(
                        <TreeSelect onChange={e => this.setState({unitId: e})} placeholder='请选择所属街区'/>
                      )}
                    </Form.Item>
                    <Form.Item
                      {...formItemLayout}
                      label='创建人'
                    >
                      <span>Root</span>
                    </Form.Item>
                    <Form.Item
                      {...formItemLayout}
                      label='处置前描述'
                    >
                      {getFieldDecorator('desc', {
                        rules:[
                          {required: true, message: '请描述'},
                          {validator: validatorDes}
                        ],
                      })(
                        <TextArea rows={2} style={{ resize: 'none' }} placeholder='请描述'/>
                      )}
                    </Form.Item>
                  </Col>
                </Row>
                <Row span={24} style={{ marginBottom: '30px'}}>
                  <Col span={12}  className={style.handle_CarouselWarp}>
                    <span className={style.welcomeTitle} >处置前图片：</span>
                    <Col span={24} className={`${style.welcomeWarp} ${style.welcomeWarp_32}`}>
                      <Col span={2} offset={5}>
                        {
                          image && image.length <= 1 ? (
                              <img
                                src={noArrowsLeft}
                                className={style.noArrows}
                              />
                            )
                            :
                            (
                              <img
                                src={arrowsLeft}
                                onClick={()=> {
                                  console.log(this);
                                  this.refs.imageWelcome.prev()
                                }
                                }
                                className={style.arrows}
                              />
                            )
                        }
                      </Col>
                      <div className={style.handle_ContentWarp}>
                        {
                          image.length >= 1 ?
                            (
                              <Carousel effect="fade" ref="imageWelcome" className={style.welcomeContent}>
                                {
                                  image.map((item, index)=> {      
                                    console.log(getUrl(item));
                                                            
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
                                            background:`url('${getUrl(item)}') center center / contain no-repeat`,
                                          }}
                                          onClick={(e) => {
                                            this.setState({
                                              imageUrl: item
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
                              <img
                                src={noArrowsRight}
                                className={style.noArrows}
                              />
                            )
                            :
                            (
                              <img
                                src={arrowsRight}
                                onClick={()=> {
                                  console.log(this);
                                  this.refs.imageWelcome.next()
                                }
                                }
                                className={style.arrows}
                              />
                            )
                        }
                      </Col>
                    </Col>
                    <Upload
                      style={{ marginLeft: 218 }}
                      onChange={this.fileImageChange}
                      name="file"
                      accept=".jpg,.jpeg,.png"
                      action={getUrl("/api/file/upload?fileType=5")}
                      showUploadList={false}
                      beforeUpload={beforeUpload}
                      headers={{
                        "Authorization": getAuthority().token,
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
                    <span className={style.welcomeTitle} >处置前视频：</span>
                    <Col span={24} className={`${style.welcomeWarp} ${style.welcomeWarp_32}`}>
                      <Col span={2} offset={5}>
                        {
                          video && video.length <= 1 ? (
                              <img
                                src={noArrowsLeft}
                                className={style.noArrows}
                              />
                            )
                            :
                            (
                              <img
                                src={arrowsLeft}
                                onClick={()=> {
                                  console.log(this);
                                  this.refs.videoWelcome.next()
                                }
                                }
                                className={style.arrows}
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
                                ref="videoWelcome"
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
                              <img
                                src={noArrowsRight}
                                className={style.noArrows}
                              />
                            )
                            :
                            (
                              <img
                                src={arrowsRight}
                                onClick={()=> {
                                  console.log(this);
                                  this.refs.videoWelcome.next()
                                }
                                }
                                className={style.arrows}
                              />
                            )
                        }
                      </Col>
                    </Col>
                    <Upload
                      style={{ marginLeft: 218, marginTop: 20 }}
                      onChange={this.fileVideoChange}
                      name="file"
                      accept=".mp4"
                      action={getUrl("/api/file/upload?fileType=5")}
                      showUploadList={false}
                      beforeUpload={beforeUpload}
                      headers={{
                        "Authorization": getAuthority().token,
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
            <Col span={6} style={{ height: '85', backgroundColor: '#F7F7F7', textAlign: 'center'}}>
              <span style={{color: '#000', fontSize: 15, fontWeight: 500, paddingTop: 5, display: 'block' }}>办理流程</span>
            </Col>
          </Row>
        </Modal>
      </Fragment>
    )
  };

  render() {
    return (
      <Fragment>
        <Text
          style={{ color: '#fff', height: 64, display: 'inline-block' }}
          onClick={() => {
            const timelineOptions = [];
            this.props.dispatch({
              type: 'global/fetch_get_tree',
              callback: (res) => {
                this.setState({ visibleModal: true });
                return request(`/api/bigcategory/list`, {headers:{ 'Authorization': getAuthority().token}})
                  .then(res => {
                    if (res.status === 0) {
                      res.value.map(item => {
                        let payload = {
                          value: item.id,
                          label: item.name,
                          isLeaf: false,
                        };
                        timelineOptions.push(payload);
                      });
                      this.setState({
                        timelineOptions
                      });
                    } else {
                      console.log(res.message)
                    }
                  });
              }
            });
          }}>
          <Icon type="diff" style={{ lineHeight: 4 }} />新建案卷
        </Text>
        { this.state.visibleModal && this.addFileModal() }
        { this.state.previewModalVisible && this.previewModal() }
      </Fragment>
    );
  }
}

export default Index
