import React, { Component, Fragment } from 'react';
import { connect } from 'dva';
import { Col, Layout, Menu, Icon, Row, Typography, Card, Tabs, Calendar, Button, message, Spin } from 'antd';
import styles from './index.less';
import con from '@/assets/file-process.png';
import garyStart from '@/assets/start.png';
import startActive from '@/assets/start-active.png'
import shear from '@/assets/shear.png';
import screen from '@/assets/screen.png';
import control from '@/assets/control.png';
import request from 'umi-request';
import fullScreen from '@/assets/fullScreen.png';
import { getUrl, token } from "@/utils/utils";
import device_max from '@/assets/device_max.png';
import device_def from '@/assets/device_def.png';
import device_small from '@/assets/device_small.png';
import itemStyle from "echarts/src/model/mixin/itemStyle";
import Ellipsis from "@/components/Ellipsis";
import hint from '@/assets/hint.png';

const { SubMenu } = Menu;
const { Content, Sider } = Layout;
const { Text } = Typography;
const { TabPane } = Tabs;

@connect(({ global, monitoring }) => ({
  global,
  monitoring,
}))
class Index extends Component {
  constructor(props) {
    super(props);
  };

  state = {
    tabsPageData: [],
    tabsPageDataItem: [],
    pageItemOfList: true,
    activeKey: '1',
    videoMaxStart: false,
    addVideoCardUnMount: false,
    contentLoading: false,
    playFlag: true,
    stateToken: token,
  };


  reVideoList = () => {
    let newTabsPageData = sessionStorage.getItem('device') && JSON.parse(sessionStorage.getItem('device')) || [];
    if (newTabsPageData.length) {
      this.setState({
        contentLoading: true
      }, () => {
        newTabsPageData.map((item, index) => {
          request('/api/video/play', { params: { id: item.id, type: 1 }, headers: this.state.stateToken })
            .then(res => {
              if (res.status === 0) {
                this.setState({
                  tabsPageData: this.state.tabsPageData.concat(res.value)
                }, () => {
                  this.addVideoCard();
                })
              } else {
                this.setState({
                  contentLoading: false
                });
                message.error('请重新打开监控')
              }
            })
        });
      });
    }
  };

  componentDidMount() {
    const { dispatch } = this.props;
    const { tabsPageData } = this.state;
    window.videoObjItem = false;
    dispatch({
      type: 'global/fetch_getDeviceTree',
    });

    let newTabsPageData = sessionStorage.getItem('device') && JSON.parse(sessionStorage.getItem('device')) || [];
    if (newTabsPageData.length) {
      this.setState({
        contentLoading: true
      }, async () => {
        await newTabsPageData.map((item, index) => {
          request('/api/video/play', { params: { id: item.id, type: 1 }, headers: this.state.stateToken })
            .then(res => {
              if (res.status === 0) {
                this.setState({
                  tabsPageData: this.state.tabsPageData.concat(res.value)
                }, () => {
                  this.addVideoCard();
                })
              } else {
                message.error('请重新打开监控')
              }
            })
        });
      });
    }

    window.onunload = () => {
      if (this.state.addVideoCardUnMount) {
        // console.log(videoObjItem);
        videoObjItem && videoObjItem.dispose();
        request('/api/video/play_close', { params: { id: this.state.addVideoCardUnMount && videoObjItem && videoObjItem.options_.pageValue, type: 1 }, headers: this.state.stateToken }).then();
      } else {
        this.state.tabsPageData.map((item) => {
          VideoObjList[item.id].dispose();
          request('/api/video/play_close', { params: { id: item.id, type: 1 }, headers: this.state.stateToken }).then();
        });
      }
    };
    window.onbeforeunload = () => {
      if (this.state.addVideoCardUnMount) {
        // console.log(videoObjItem);
        videoObjItem && videoObjItem.dispose();
        request('/api/video/play_close', { params: { id: this.state.addVideoCardUnMount && videoObjItem && videoObjItem.options_.pageValue, type: 1 }, headers: this.state.stateToken }).then();
      } else {
        this.state.tabsPageData.map((item) => {
          VideoObjList[item.id].dispose();
          request('/api/video/play_close', { params: { id: item.id, type: 1 }, headers: this.state.stateToken }).then();
        });
      }
    };
  }

  addVideoCard = () => {
    let panes = [
      {
        title: '摄像机清单',
        content: (
          <Fragment>
            {
              this.state.tabsPageData && this.state.tabsPageData.map((item, index) => {
                return (
                  <Col
                    span={7}
                    style={{
                      margin: "10px 1%",
                      position: 'relative',
                      width: '31.19%'
                    }}
                    key={item.id}
                  >
                    <Card
                      title={item.name}
                      bordered={false}
                      style={{ height: '100%', width: '100%' }}
                      extra={<Icon type="close" onClick={e => {
                        VideoObjList[item.id].dispose();
                        request('/api/video/play_close', { params: { id: item.id, type: 1 }, headers: this.state.stateToken }).then();
                        let has = this.state.tabsPageData.filter(data => {
                          if (data.id === item.id) return false;
                          return data
                        });
                        sessionStorage.setItem('device', JSON.stringify(has));
                        this.setState({
                          tabsPageData: has
                        }, () => {
                          this.addVideoCard();
                        });
                      }} />}
                      key={item.id}
                    >
                      <section id="videoPlayer">
                        <video
                          id={`video_${item.id}`}
                          className="video-js vjs-big-play-centered vjs-default-skin vjs-fluid"
                          data-setup='{"aspectRatio":"16:9"}'
                          poster=""
                          key={item.id}
                        >
                          <source src={item.address} type="application/x-mpegURL" id="target" />
                        </video>
                      </section>
                    </Card>
                    <div
                      style={{
                        width: "100%",
                        backgroundColor: "rgba(0, 0, 0, 0.9)",
                        height: 30
                      }}
                    >
                      <img
                        src={fullScreen}
                        alt="全屏"
                        style={{
                          cursor: 'pointer',
                          width: 14,
                          margin: '9px 12px 9px 19px'
                        }}
                        onClick={(e) => {
                          e.stopPropagation();
                          this.setState({
                            contentLoading: true
                          });
                          this.state.tabsPageData.map((item) => {
                            VideoObjList[item.id].dispose();
                            request('/api/video/play_close', { params: { id: item.id, type: 1 }, headers: this.state.stateToken }).then();
                          });
                          sessionStorage.setItem('device', JSON.stringify(this.state.tabsPageData));
                          let has = this.state.tabsPageDataItem.some(data => {
                            if (data.id !== item.id) return false;
                            return true;
                          });
                          if (!has) {
                            this.setState({
                              pageItemOfList: false,
                              configId: item.id
                            }, () => {
                              this.props.dispatch({
                                type: 'monitoring/fetch_videoPlay',
                                payload: {
                                  id: item.id,
                                  type: 1
                                },
                                callback: res => {
                                  if (res.status === 0) {
                                    this.addVideoItemCard(res.value);
                                  } else {
                                    this.reVideoList();
                                  }
                                }
                              });
                            })
                          }
                        }}
                      />
                      <img
                        src={shear}
                        alt="截图"
                        style={{ marginLeft: 5, width: 16, cursor: 'pointer' }}
                        onClick={event => {
                          // return request('/api/video/capture', { params: {id: item.id,  type:'1'}, headers: this.state.stateToken})
                          //   .then(res => {
                          //     let url = window.URL.createObjectURL(new Blob([res]));
                          //     console.log(url)
                          //     let link = document.createElement("a");
                          //     link.setAttribute("download", '截图.jpg');
                          //     link.style.display = "none";
                          //     link.href = url;
                          //     document.body.appendChild(link);
                          //     link.click();
                          //   })
                          let link = document.createElement("a");
                          link.setAttribute("download", '截图.jpg');
                          link.style.display = "none";
                          link.href = `htttp://192.168.3.8:8060 //api/video/capture?id=${data.id}`;
                          document.body.appendChild(link);
                          link.click();
                        }}
                      />
                      <Text style={{
                        fontSize: 13,
                        float: "right",
                        lineHeight: "30px",
                        color: "rgba(38,138,232,1)",
                        marginRight: 23
                      }}>人脸</Text>
                    </div>
                  </Col>
                )
              })
            }
          </Fragment>
        ),
        key: '1',
        closable: false,
      },
    ];
    window.VideoObjList = {};
    let options = {
      poster: `${device_def}`,            //未播放时 的封面
      autoplay: false,                    //播放器准备好之后，是否自动播放 【默认false】
      controls: true,                 //是否显示控制栏
      preload: 'auto',                //预加载
      muted: true,                    //静音
      language: "zh-CN",                //初始化语言
    };
    this.setState({
      panes,
      contentLoading: false,
    }, () => {
      this.state.tabsPageData.map((item) => {
        VideoObjList[item.id] = videojs(`video_${item.id}`,
          options,
          function reader() {
          }
        );
      });
    });
  };

  addVideoItemCard = (data) => {
    this.setState({
      addVideoCardUnMount: true
    });
    const self = this;
    if (data) {
      let panesItem = [
        {
          id: data.id,
          title: `${data.name}`,
          content: (
            <Fragment>
              <Row type='flex'>
                <Col style={{ width: '80%' }}>
                  <section id="videoPlayer">
                    <video
                      id={`video_${data.id}`}
                      className="video-js vjs-big-play-centered vjs-default-skin vjs-fluid"
                      data-setup='{"aspectRatio":"16:9"}'
                      poster=""
                    >
                    </video>
                  </section>
                </Col>
                <ul style={{ width: '20%', color: 'rgba(76,95,193,1)', paddingLeft: '2%' }}>
                  <li style={{ marginBottom: "11%" }}>●&nbsp;基本信息
                    <ul style={{ paddingLeft: '1%', marginTop: "2%", lineHeight: "35px" }}>
                      <li style={{ color: "rgba(153,153,153,1)" }}>摄像机名称：<Text style={{ color: "rgba(102,102,102,1)" }}>{data.name}</Text></li>
                      <li style={{ color: "rgba(153,153,153,1)" }}>摄像机编号：<Text style={{ color: "rgba(102,102,102,1)" }}>{data.num}</Text></li>
                      <li style={{ color: "rgba(153,153,153,1)" }}>摄像机类型：<Text style={{ color: "rgba(102,102,102,1)" }}>{data.type}</Text></li>
                      <li style={{ color: "rgba(153,153,153,1)" }}>关联街区名：<Text style={{ color: "rgba(102,102,102,1)" }}>{data.unitName}</Text></li>
                    </ul>
                  </li>


                  {data.type === '球机' && (
                    <li>●&nbsp;云台控制
                      <div style={{ position: 'relative', width: '73%', marginTop: '2%' }}>
                        {/* <img  src={control} style={{width: '100%'}} /> */}
                        <div className={styles.mon_box}>
                          <div className={styles.mon_top}>
                            <img
                              alt="上"
                              // ref={(e)=> this.top = e}
                              style={{
                                width: '100%',
                                height: '49px',
                                backgroundColor: 'red',
                                display: 'block',
                                position: 'absolute',
                                left: '3%',
                                top: 0,
                                right: 0,
                                margin: 'auto',
                                zIndex: 99,
                                borderRadius: '56%',
                                opacity: 0,
                                cursor: 'pointer'
                              }}
                              onMouseDown={e => {
                                this.timer = setInterval(() => {
                                  console.log('top')
                                  return request('/api/video/control_up', { params: { id: data.id }, headers: this.state.stateToken })
                                }, 100);
                              }}
                              onMouseUp={event => {
                                console.log('top')
                                clearInterval(this.timer)
                              }}
                              onClick={event => request('/api/video/control_up', { params: { id: data.id }, headers: this.state.stateToken })}
                            />
                          </div>

                          <div className={styles.mon_down} >
                            <img
                              alt="下"
                              style={{
                                width: '100%',
                                height: '49px',
                                backgroundColor: 'red',
                                display: 'block',
                                position: 'absolute',
                                left: '3%',
                                bottom: 0,
                                right: 0,
                                margin: 'auto',
                                borderRadius: '56%',
                                opacity: 0,
                                zIndex: 99,
                                cursor: 'pointer'
                              }}
                              onClick={event => request('/api/video/control_down', { params: { id: data.id }, headers: this.state.stateToken })}
                              onMouseDown={e => {
                                this.timer = setInterval(() => {
                                  console.log('下')
                                  return request('/api/video/control_down', { params: { id: data.id }, headers: this.state.stateToken })
                                }, 100);
                              }}
                              onMouseUp={event => {
                                console.log('下')
                                clearInterval(this.timer)
                              }}
                            />
                          </div>

                          <div className={styles.mon_left}>
                            <img
                              alt="左"
                              style={{
                                width: '49px',
                                height: '100%',
                                backgroundColor: 'red',
                                display: 'block',
                                position: 'absolute',
                                left: 0,
                                bottom: 0,
                                top: 0,
                                margin: 'auto',
                                borderRadius: '56%',
                                opacity: 0,
                                cursor: 'pointer'
                              }}
                              onClick={event => request('/api/video/control_left', { params: { id: data.id }, headers: this.state.stateToken })}
                              onMouseDown={e => {
                                this.timer = setInterval(() => {
                                  return request('/api/video/control_left', { params: { id: data.id }, headers: this.state.stateToken })
                                }, 100);
                              }}
                              onMouseUp={event => {
                                clearInterval(this.timer)
                              }}
                            />
                          </div>

                          <div className={styles.mon_right}>
                            <img
                              alt="右"
                              style={{
                                width: '49px',
                                height: '100%',
                                backgroundColor: 'red',
                                display: 'block',
                                position: 'absolute',
                                right: 0,
                                bottom: 0,
                                top: 0,
                                margin: 'auto',
                                borderRadius: '56%',
                                opacity: 0,
                                cursor: 'pointer'
                              }}
                              onClick={event => request('/api/video/control_right', { params: { id: data.id }, headers: this.state.stateToken })}
                              onMouseDown={e => {
                                this.timer = setInterval(() => {
                                  return request('/api/video/control_right', { params: { id: data.id }, headers: this.state.stateToken })
                                }, 100);
                              }}
                              onMouseUp={event => {
                                clearInterval(this.timer)
                              }}
                            />
                          </div>
                        </div>
                        <img
                          src={shear} alt=""
                          style={{
                            width: '10%',
                            height: '13%',
                            position: 'absolute',
                            top: 0,
                            bottom: 0,
                            margin: 'auto',
                            right: '-20%',
                            cursor: 'pointer'
                          }}
                          onClick={event => {
                            // if (this.state.videoMaxStart) {
                            //   return request('/api/video/capture', {params: {id: data.id}, headers: this.state.stateToken})
                            //     // .then(res => {
                            //     //   if (res.status === 0) {
                            //     //     console.log(getUrl(res.value));
                            //     //     window.open(getUrl(res.value));
                            //     //   }
                            //     // })
                            // } else {
                            //   message.error('请播放视频')
                            // }
                            let link = document.createElement("a");
                            link.setAttribute("download", '截图.jpg');
                            link.style.display = "none";
                            link.href = `http://192.168.3.8:8060//api/video/capture?id=${data.id}`;
                            document.body.appendChild(link);
                            link.click();
                          }}
                        />
                        <Text
                          style={{
                            position: 'absolute',
                            fontSize: 14,
                            right: '-20.5%',
                            top: '57%',
                            border: 0,
                            cursor: 'pointer'
                          }}
                          onClick={event => {
                            // if (this.state.videoMaxStart) {
                            // return request('/api/video/capture', { params: {id: data.id}, headers: this.state.stateToken})
                            //   .then(res => {
                            //     let url = window.URL.createObjectURL(new Blob([res.data]));
                            //     console.log(url);
                            let link = document.createElement("a");
                            link.setAttribute("download", '截图.jpg');
                            link.style.display = "none";
                            link.href = `http://192.168.3.8:8060//api/video/capture?id=${data.id}`;
                            document.body.appendChild(link);
                            link.click();
                            // })
                            // } else {
                            //   message.error('请播放视频')
                            // }
                          }}
                        >
                          截图
                        </Text>
                        {/*</div>*/}
                      </div>
                    </li>
                  )}

                </ul>
              </Row>
            </Fragment>
          ),
          key: '1',
          closable: false,
        },
      ];
      window.videoObjItem = {};
      let options = {
        pageValue: data.id,
        poster: `${device_max}`,            //未播放时 的封面
        autoplay: false,                    //播放器准备好之后，是否自动播放 【默认false】
        controls: true,                 //是否显示控制栏
        preload: 'auto',                //预加载
        muted: true,                    //静音
        language: "zh-CN",                //初始化语言
        sources: [{
          src: data.address,
          type: "application/x-mpegURL"
        }]
      };
      this.setState({
        panesItem,
        contentLoading: false,
      }, () => {
        window.videoObjItem = videojs(`video_${data.id}`,
          options,
          function reader() {
            this.on('play', () => {
              self.setState({
                videoMaxStart: true
              }, () => {
                console.log(self.state.videoMaxStart)
              })
            });
            this.on('pause', () => {
              // this.dispose();
              // return request('/api/video/play_close', { params: {id: data.id,  type:1}, headers: this.state.stateToken })
            })
          });
        window.videoObjItem.src(data.address);
      })
    }
  };

  renderSubMenu = (data) => {
    const { tabsPageData } = this.state;
    return (
      data && data.map(item => {
        if (item.tree.length >= 1) {
          return (
            <Menu.SubMenu
              key={item.id}
              title={
                <span>
                  <Icon type="appstore" />
                  <span>{item.name}</span>
                </span>
              }
            >
              {this.renderSubMenu(item.tree)}
            </Menu.SubMenu>
          )
        } else {
          if (item.type === 2) {
            return (
              <Menu.Item
                key={item.id}
              >
                <span
                  style={{
                    display: 'flex',
                    marginTop: 15
                  }}
                >
                  <img src={device_small} alt="" style={{
                    width: 100,
                    height: 70,
                    marginRight: 10,
                  }} />
                  <div style={{ margin: '0 10px', width: '150px' }}>
                    <Ellipsis lines={2} length={150} tooltip={true} style={{ display: 'inline-block'}}>{item.name}</Ellipsis>
                    <div style={{ marginTop: -15 }}>
                      <img
                        src={garyStart}
                        alt="播放"
                        style={{ marginRight: 10, zIndex: 3, cursor: 'pointer' }}
                        onClick={(e) => {
                          if (this.state.pageItemOfList) {
                            if (this.state.playFlag) {
                              e.stopPropagation();
                              this.setState({
                                playFlag: false
                              }, () => {
                                let has = tabsPageData.some(data => {
                                  if (data.id !== item.id) return false;
                                  return true;
                                });
                                if (!has) {
                                  this.setState({
                                    contentLoading: true
                                  });
                                  this.props.dispatch({
                                    type: 'monitoring/fetch_videoPlay',
                                    payload: {
                                      id: item.id,
                                      type: 1
                                    },
                                    callback: res => {
                                      if (res.status === 0) {
                                        this.setState({
                                          tabsPageData: tabsPageData.concat(res.value),
                                          playFlag: true,
                                        }, () => {
                                          sessionStorage.setItem('device', JSON.stringify(this.state.tabsPageData));
                                          this.addVideoCard()
                                        })
                                      } else {
                                        this.setState({
                                          contentLoading: false,
                                          playFlag: true,
                                        })
                                      }
                                    }
                                  });
                                } else {
                                  this.setState({
                                    playFlag: true
                                  })
                                }
                              });
                            }
                          }
                        }}
                      />
                      <img
                        src={screen}
                        alt="全屏"
                        style={{ zIndex: 3, cursor: 'pointer' }}
                        onClick={(e) => {
                          e.stopPropagation();
                          console.log(videoObjItem);

                          this.setState({
                            contentLoading: true,
                            addVideoCardUnMount: true,
                          });
                          this.state.tabsPageData.map((data) => {
                            VideoObjList[data.id].dispose();
                            request('/api/video/play_close', { params: { id: data.id, type: 1 }, headers: this.state.stateToken }).then();
                          });
                          if (videoObjItem) {
                            request('/api/video/play_close', {
                              params: { id: videoObjItem.options_.pageValue, type: 1 },
                              headers: this.state.stateToken
                            }).then();
                          }
                          sessionStorage.setItem('device', JSON.stringify(this.state.tabsPageData));
                          let has = this.state.tabsPageDataItem.some(data => {
                            if (data.id !== item.id) return false;
                            return true;
                          });
                          if (!has) {
                            this.setState({
                              pageItemOfList: false,
                              configId: item.id
                            }, () => {
                              this.props.dispatch({
                                type: 'monitoring/fetch_videoPlay',
                                payload: {
                                  id: item.id,
                                  type: 1
                                },
                                callback: res => {
                                  if (res.status === 0) {
                                    this.addVideoItemCard(res.value);
                                  } else {
                                    this.setState({
                                      contentLoading: false,
                                      pageItemOfList: true,
                                      tabsPageData: [],
                                    }, () => {
                                      this.reVideoList();
                                    });
                                  }
                                }
                              });
                            })
                          }
                        }}
                      />
                    </div>
                  </div>
                </span>
              </Menu.Item>
            )
          } else {
            return (
              <Menu.SubMenu
                key={item.id}
                title={
                  <span>
                    <Icon type="appstore" />
                    <span style={{ wordWrap: 'break-word' }}>{item.name}</span>
                  </span>
                }
              >
              </Menu.SubMenu>
            )
          }
        }
      })
    )
  };

  componentWillUnmount() {
    console.log('componentWillUnmount');
    console.log(videoObjItem);

    if (this.state.addVideoCardUnMount) {
      videoObjItem && videoObjItem.dispose() && videoObjItem.dispose();
      request('/api/video/play_close', { params: { id: this.state.addVideoCardUnMount && videoObjItem && videoObjItem.options_.pageValue, type: 1 }, headers: this.state.stateToken }).then();
    } else {
      this.state.tabsPageData.map((item) => {
        VideoObjList[item.id].dispose();
        request('/api/video/play_close', { params: { id: item.id, type: 1 }, headers: this.state.stateToken }).then();
      });
    }
    this.setState({
      addVideoCardUnMount: false
    })
  }

  render() {
    const { global: { menu_data } } = this.props;
    return (
      <Fragment>
        <Spin
          spinning={this.state.contentLoading}
        >
          <Layout style={{ height: '93vh' }}>
            <Sider
              className={this.state.pageItemOfList ? styles.LayoutSider : styles.layout_sider_details}
              width={300}
              style={{ backgroundColor: '#454956' }}
            >
              <Menu
                mode="inline"
                style={{ height: '100%', borderRight: 0 }}
              >
                {
                  menu_data && menu_data.map((item, index) => {
                    return item && item.tree.length >= 1 ? this.renderSubMenu(item.tree) : this.renderMenuItem(item.tree);
                  })
                }
              </Menu>
            </Sider>
            <Layout style={this.state.pageItemOfList ? { marginLeft: 24 } : { marginLeft: 5 }} className={styles.layoutContentWarp}>
              <Content
                className={styles.contentWarp}
                style={{
                  background: '#fff',
                  margin: 0,
                  height: '100%'
                }}
              >
                {
                  // this.state.tabsPageData.length >= 1?

                  this.state.pageItemOfList ? (
                    this.state.tabsPageData.length >= 1 ?
                      this.state.panes && (
                        <Tabs
                          hideAdd
                          activeKey={this.state.activeKey}
                          type="editable-card"
                        >
                          {
                            this.state.panes.map((pane, index) => {
                              return (
                                <TabPane tab={pane.title} key={pane.key} closable={pane.closable}>
                                  {pane.content}
                                </TabPane>
                              )
                            })
                          }
                        </Tabs>
                      )
                      :
                      (
                        <div className={styles.no_dataSource_content}>
                          <div>
                            <img src={hint} alt="" style={{ display: 'inline-block' }} />
                            <span style={{ display: 'inline-block' }}>从左边菜单选择需要观看的视频</span>
                          </div>
                        </div>
                      )
                  ) : (
                      this.state.panesItem && (<Tabs
                        hideAdd
                        tabBarExtraContent={
                          <Button
                            type='primary'
                            size='small'
                            onClick={() => {
                              this.setState({ pageItemOfList: true, panesItem: [], addVideoCardUnMount: false, tabsPageData: [] }, () => {
                                videoObjItem.dispose();
                                request('/api/video/play_close', { params: { id: videoObjItem.options_.pageValue, type: 1 }, headers: this.state.stateToken })
                                  .then(res => {
                                    if (res.status === 0) {
                                      this.reVideoList();
                                    }
                                  });
                              });
                            }}
                          >
                            返回
                          </Button>
                        }
                        activeKey={this.state.activeKey}
                        type="editable-card"
                      >
                        {
                          this.state.panesItem.map((pane, index) => {
                            return (
                              <TabPane tab={pane.title} key={pane.key} closable={pane.closable}>
                                {pane.content}
                              </TabPane>
                            )
                          })
                        }
                      </Tabs>)
                    )

                  //:
                  // (
                  //   <div className={styles.no_dataSource_content}>
                  //     <div>
                  //       <img src={hint} alt="" style={{ display: 'inline-block' }}/>
                  //       <span style={{ display: 'inline-block' }}>从左边菜单选择需要观看的视频</span>
                  //    </div>
                  //  </div>
                  //)
                }
              </Content>
            </Layout>
          </Layout>
        </Spin>
      </Fragment>
    );
  }
}

export default Index
