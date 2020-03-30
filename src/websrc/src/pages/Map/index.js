import React, { PureComponent, Fragment } from 'react';
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
  Checkbox,
  Col,
  Select,
  Radio,
  Carousel,
  message,
  Upload,
  Progress,
  DatePicker,
  Badge,
  Popover,
  Descriptions,
} from 'antd';
import { connect } from 'dva';
import address from './address.json';
import styles from "./index.less";
import style from '@/pages/Workbench/workbench.less';
import request from "@/utils/request";
import {
  beforeUpload,
  getUrl,
  payload_0_10 as pagePayload, returnSourceText, returnTimeTypeText,
  returnWorkTypeStyle,
  returnWorkTypeText,
  token
} from "@/utils/utils";
import TreeSelect from '@/components/UnitTreeSelect';
import moment from 'moment';

import file_del from "@/assets/file_del.png";
import forImage from "@/assets/hasImage.png";
import QueueAnim from "rc-queue-anim";
import forVideo from "@/assets/hasVideo.png";
import nullStart from '@/assets/null.png';
import noArrowsLeft from "@/assets/noLeft.png";
import arrowsLeft from "@/assets/left.png";
import noArrowsRight from "@/assets/noRight.png";
import arrowsRight from "@/assets/right.png";
import del from "@/assets/del.png";
import scUser from '@/assets/user.png';
import device from '@/assets/device.png';
import station from '@/assets/station.png';
import alarm from '@/assets/alarm.png';
import mapMax from '@/assets/mapMax.png';
import mapMin from '@/assets/mapMin.png';
import { stringify } from "qs";

const { RangePicker } = DatePicker;
const { Text, Paragraph } = Typography;
const { TreeNode } = Tree;
const { Option } = Select;
const { TextArea } = Input;

const mapIndex = [
  { longitude: 104.000249, latitude: 30.647609, num: 1 },
  { longitude: 103.996206, latitude: 30.646802, num: 2 },
  { longitude: 103.995784, latitude: 30.662405, num: 3 },
  { longitude: 103.989577, latitude: 30.660766, num: 4 },
  { longitude: 103.993206, latitude: 30.654677, num: 5 },
  { longitude: 103.985534, latitude: 30.657287, num: 5 },
  { longitude: 103.98266, latitude: 30.656417, num: 6 },
  { longitude: 103.989199, latitude: 30.653807, num: 7 },
  { longitude: 103.996206, latitude: 30.652425, num: 8 },
  { longitude: 103.989738, latitude: 30.647873, num: 9 },
  { longitude: 103.989541, latitude: 30.643229, num: 10 },
  { longitude: 103.979749, latitude: 30.646211, num: 11 },
  { longitude: 103.982013, latitude: 30.651726, num: 12 },
  { longitude: 103.976408, latitude: 30.652052, num: 13 },
  { longitude: 103.98063, latitude: 30.648417, num: 14 },
  { longitude: 103.985067, latitude: 30.647703, num: 14 },
  { longitude: 103.970766, latitude: 30.650079, num: 15 },
  { longitude: 103.972922, latitude: 30.645481, num: 16 },
];

const formHeaderItemLayout = {
  labelCol: {
    xs: { span: 8 },
    sm: { span: 8 },
  },
  wrapperCol: {
    xs: { span: 16 },
    sm: { span: 16 },
  },
};

const formItemLayout = {
  labelCol: {
    xs: { span: 5 },
    sm: { span: 5 },
  },
  wrapperCol: {
    xs: { span: 16 },
    sm: { span: 17 },
  },
};

@Form.create()
@connect(({ global, config }) => ({
  global,
  config
}))
class Map extends PureComponent {
  state = {
    map_0: [],
    map_1: [],
    map_2: [],
    map_3: [],
    map_4: [],
    map_5: [],
    map_6: [],
    map_7: [],
    map_8: [],
    map_9: [],
    map_10: [],
    map_11: [],
    map_12: [],
    map_13: [],
    map_14: [],
    map_15: [],
    map_16: [],
    map_17: [],
    headerFormStart: 'part',
    checkUser: false,
    checkDevice: false,
    checkStation: false,
    imageModelVisible: false,
    alarmListModalVisible: false,
    isHeader: true,
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
    alarmOpa: 1,
    station: {
      visible: false,
      top: null,
      left: null,
    }
    
  };

  componentWillMount() {
    this.props.dispatch({
      type: 'global/fetch_get_unit',
    })
    this.props.dispatch({
      type: 'config/fetch_getConfigItem',
    })
  }

  componentDidMount() {
    request('api/basis/item', { headers: token })
      .then(res => {
        if (res.status === 0) {
          console.log(res.value);
          const {
            map_0,
            map_1,
            map_2,
            map_3,
            map_4,
            map_5,
            map_6,
            map_7,
            map_8,
            map_9,
            map_10,
            map_11,
            map_12,
            map_13,
            map_14,
            map_15,
            map_16,
            map_17,
          } = this.state;
          let maps = {};
          // 添加不同颜色的遮蔽物
          address.value.map((item, index) => {
            maps[`map${index}`] = item
          });
          maps.map0.map((item, index) => {
            map_0[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map1.map((item, index) => {
            map_1[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map2.map((item, index) => {
            map_2[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map3.map((item, index) => {
            map_3[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map4.map((item, index) => {
            map_4[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map5.map((item, index) => {
            map_5[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map6.map((item, index) => {
            map_6[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map7.map((item, index) => {
            map_7[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map8.map((item, index) => {
            map_8[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map9.map((item, index) => {
            map_9[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map10.map((item, index) => {
            map_10[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map11.map((item, index) => {
            map_11[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map12.map((item, index) => {
            map_12[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map13.map((item, index) => {
            map_13[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map14.map((item, index) => {
            map_14[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map15.map((item, index) => {
            map_15[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map16.map((item, index) => {
            map_16[index] = new BMap.Point(item.longitude, item.latitude);
          });
          maps.map17.map((item, index) => {
            map_17[index] = new BMap.Point(item.longitude, item.latitude);
          });

          // 无蒙层Polyline 蒙层Polygon
          // 通过map调用API
          let newmap_0 = new BMap.Polygon(
            map_0,
            {
              fillColor: '#8F82BC',
              fillOpacity: 0.2,
              strokeColor: '#8F82BC',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_1 = new BMap.Polygon(
            map_1,
            {
              fillColor: '#8F82BC',
              fillOpacity: 0.2,
              strokeColor: '#8F82BC',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_2 = new BMap.Polygon(
            map_2,
            {
              fillColor: '#9AAC22',
              fillOpacity: 0.2,
              strokeColor: '#9AAC22',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_3 = new BMap.Polygon(
            map_3,
            {
              fillColor: '#9AAC22',
              fillOpacity: 0.2,
              strokeColor: '#9AAC22',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_4 = new BMap.Polygon(
            map_4,
            {
              fillColor: '#22AC38',
              fillOpacity: 0.2,
              strokeColor: '#22AC38',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_5 = new BMap.Polygon(
            map_5,
            {
              fillColor: '#22AC38',
              fillOpacity: 0.2,
              strokeColor: '#22AC38',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_6 = new BMap.Polygon(
            map_6,
            {
              fillColor: '#22AC38',
              fillOpacity: 0.2,
              strokeColor: '#22AC38',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_7 = new BMap.Polygon(
            map_7,
            {
              fillColor: '#F19EC2',
              fillOpacity: 0.2,
              strokeColor: '#F19EC2',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_8 = new BMap.Polygon(
            map_8,
            {
              fillColor: '#F19EC2',
              fillOpacity: 0.2,
              strokeColor: '#F19EC2',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_9 = new BMap.Polygon(
            map_9,
            {
              fillColor: '#EEA066',
              fillOpacity: 0.2,
              strokeColor: '#EEA066',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_10 = new BMap.Polygon(
            map_10,
            {
              fillColor: '#EEA066',
              fillOpacity: 0.2,
              strokeColor: '#EEA066',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_11 = new BMap.Polygon(
            map_11,
            {
              fillColor: '#48B5B9',
              fillOpacity: 0.2,
              strokeColor: '#48B5B9',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_12 = new BMap.Polygon(
            map_12,
            {
              fillColor: '#48B5B9',
              fillOpacity: 0.2,
              strokeColor: '#48B5B9',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_13 = new BMap.Polygon(
            map_13,
            {
              fillColor: '#EB6100',
              fillOpacity: 0.2,
              strokeColor: '#EB6100',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_14 = new BMap.Polygon(
            map_14,
            {
              fillColor: '#EB6100',
              fillOpacity: 0.2,
              strokeColor: '#EB6100',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_15 = new BMap.Polygon(
            map_15,
            {
              fillColor: '#EB6100',
              fillOpacity: 0.2,
              strokeColor: '#EB6100',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_16 = new BMap.Polygon(
            map_16,
            {
              fillColor: '#448ACA',
              fillOpacity: 0.2,
              strokeColor: '#448ACA',
              strokeWeight: 3,
              strokeOpacity: 1,
            });
          let newmap_17 = new BMap.Polygon(
            map_17,
            {
              fillColor: '#448ACA',
              fillOpacity: 0.2,
              strokeColor: '#448ACA',
              strokeWeight: 3,
              strokeOpacity: 1,
            });

          let map = new BMap.Map(this.refs.map); // 创建Map实例
          this.BdMap = map;
          map.centerAndZoom(new BMap.Point(res.value.longitude, res.value.latitude), 16);
          const top_left_control = new BMap.ScaleControl({ anchor: BMAP_ANCHOR_TOP_LEFT });// 左上角，添加比例尺
          const top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
          const top_right_navigation = new BMap.NavigationControl({ anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL }); //右上角，仅包含平移和缩放按钮

          // 添加文字
          let mapIndexObj = {};
          let mapIndexIable = {};
          mapIndex.map((item, index) => {
            index = index + 1;
            mapIndexObj[index] = new BMap.Point(item.longitude, item.latitude);
            mapIndexIable[index] = new BMap.Label(`${item.num}`, { position: mapIndexObj[index], offset: new BMap.Size(-6, -7) });  // 创建文本标注对象
            mapIndexIable[index].setStyle({
              fontSize: "16px",
              border: 'none',
              backgroundColor: "rgba(0,0,0,0)",
              fontFamily: "微软雅黑"
            });
            map.addOverlay(mapIndexIable[index]);
          });
          map.setCurrentCity('成都'); // 设置地图显示的城市 此项是必须设置的
          map.enableScrollWheelZoom(true); // 开启鼠标滚轮缩放
          map.enableKeyboard();
          map.enableAutoResize();

          map.addOverlay(newmap_0);
          map.addOverlay(newmap_1);
          map.addOverlay(newmap_2);
          map.addOverlay(newmap_3);
          map.addOverlay(newmap_4);
          map.addOverlay(newmap_5);
          map.addOverlay(newmap_6);
          map.addOverlay(newmap_7);
          map.addOverlay(newmap_8);
          map.addOverlay(newmap_9);
          map.addOverlay(newmap_10);
          map.addOverlay(newmap_11);
          map.addOverlay(newmap_12);
          map.addOverlay(newmap_13);
          map.addOverlay(newmap_14);
          map.addOverlay(newmap_15);
          map.addOverlay(newmap_16);
          map.addOverlay(newmap_17);

          map.addControl(top_left_navigation);
          map.addControl(top_right_navigation);
          map.enableScrollWheelZoom(true);
          this.onChangeAlarm();
        }
      })
  }

  // alert

  // 告警
  onChangeAlarm = () => {
    let obj = {};
    let _this = this;
    let allOverlay = _this.BdMap.getOverlays();
    allOverlay.filter(item => {
      if (item._key && item._key === 'alarm') {
        _this.BdMap.removeOverlay(item);
        console.log(item)
      }
    })
    request('/api/work/alarm/list', { headers: { 'Authorization': token } })
      .then(res => {
        if (res.status === 0) {
          this.setState({
            alarmList: res.value
          });
          res.value.map((item, index) => {
            let point = new BMap.Point(item.longitude, item.latitude);
            obj[index] = function ComplexCustomOverlay(point, key) {
              this._point = point;
              this._key = key;
            };
            obj[index].prototype = new BMap.Overlay();
            obj[index].prototype.initialize = function () {
              this._map = _this.BdMap;
              let img = this._img = document.createElement('img');
              img.style.position = 'absolute';
              img.src = `${alarm}`;
              img.style.display = 'block';
              img.Zindex = 20;
              img.style.cursor = 'pointer';
              img.style.width = '25px';
              img.style.height = '25px';
              img.addEventListener('click', function () {
                _this.setState({
                  alarmListModalVisible: true,
                  imgIndex: index,
                })
              });
              this._map.getPanes().labelPane.appendChild(img);
              return img
            };
            obj[index].prototype.draw = function () {
              let map = this._map;
              let img = this._img;
              let pixel = map.pointToOverlayPixel(this._point);
              this._img.style.left = pixel.x + img.width / 2 + "px";
              this._img.style.top = pixel.y + img.height / 2 + "px";
            };
            let myCompOverlay = new obj[index](point, 'alarm');
            _this.BdMap.addOverlay(myCompOverlay);//将标注添加到地图中
          })
        }
      });
  };

  // 站点
  onChangeStation = (e) => {
    let _this = this;
    let obj = {};
    this.setState({
      checkStation: e.target.checked
    });
    request('/api/station/maplist', { headers: { 'Authorization': token } })
      .then(res => {
        if (res.status === 0) {
          if (e.target.checked) {
            res.value.map((item, index) => {
              let point = new BMap.Point(item.longitude, item.latitude);
              obj[index] = function ComplexCustomOverlay(point, key) {
                this._point = point;
                this._key = key;
              };
              obj[index].prototype = new BMap.Overlay();
              obj[index].prototype.initialize = function () {
                this._map = _this.BdMap;
                let img = this._img = document.createElement('img');
                img.style.position = 'absolute';
                img.src = `${station}`;
                img.style.width = '25px';
                img.style.height = '25px';
                img.Zindex = 20;
                img.style.cursor = 'pointer';
                img.addEventListener('click', function(){
                  console.log(item)
                  debugger
                  _this.setState({
                    station: {
                      visible: true,
                    }
                  })
                  _this.state.station.visible && _this.stationInfo(item)
                })
                this._map.getPanes().labelPane.appendChild(img);
                return img
              };
              obj[index].prototype.draw = function () {
                let map = this._map;
                let img = this._img;
                let pixel = map.pointToOverlayPixel(this._point);
                this._img.style.left = pixel.x - img.width / 2 + "px";
                this._img.style.top = pixel.y - img.height / 2 + "px";
                _this.setState({
                  station: {
                    top: pixel.y - img.height / 2 + "px",
                    left: pixel.x - img.width / 2 + "px",
                  }
                })
              };
              let myCompOverlay = new obj[index](point, 'station');
              _this.BdMap.addOverlay(myCompOverlay);//将标注添加到地图中
            })
          } else {
            let allOverlay = _this.BdMap.getOverlays();
            allOverlay.filter(item => {
              if (item._key && item._key === 'station') {
                _this.BdMap.removeOverlay(item);
                console.log(item)
              }
            })
          }
        }
      });
  };

  stationInfo = item => {
    debugger
    const { station: {top, left} } = this.state
    const content = (
      <Descriptions>
        <Descriptions.Item label="名字">{item.name}</Descriptions.Item>
        <Descriptions.Item label="位置">{item.address}</Descriptions.Item>
        {
          item.message &&
          <Descriptions.Item label="备注">{item.message}</Descriptions.Item>
        }
      </Descriptions>
    );
    return (
      <Popover
        content={content}
        title="Title"
        trigger="click"
        style={{ position: 'absolute', top: top, left: left }}
        visible={this.state.station.visible}
        // onVisibleChange={this.handleVisibleChange}
      >
        <Button type="primary">Click me</Button>
      </Popover>
    )
  }

  handleVisibleChange = visible => {
    this.setState({ visible });
  };

  // 摄像机
  onChangeDevice = (e) => {
    let _this = this;
    let obj = {};
    this.setState({
      checkDevice: e.target.checked
    });
    request('/api/device/map/list', { headers: { 'Authorization': token } })
      .then(res => {
        if (res.status === 0) {
          if (e.target.checked) {
            res.value.map((item, index) => {
              let point = new BMap.Point(item.longitude, item.latitude);
              obj[index] = function ComplexCustomOverlay(point, key) {
                this._point = point;
                this._key = key;
              };
              obj[index].prototype = new BMap.Overlay();
              obj[index].prototype.initialize = function () {
                this._map = _this.BdMap;
                let img = this._img = document.createElement('img');
                img.style.position = 'absolute';
                img.src = `${device}`;
                img.style.width = '26px';
                img.style.height = '30px';
                this._map.getPanes().labelPane.appendChild(img);
                return img
              };
              obj[index].prototype.draw = function () {
                let map = this._map;
                let img = this._img;
                let pixel = map.pointToOverlayPixel(this._point);
                this._img.style.left = pixel.x - img.width / 2 + "px";
                this._img.style.top = pixel.y - img.height / 2 + "px";
              };
              let myCompOverlay = new obj[index](point, 'device');
              _this.BdMap.addOverlay(myCompOverlay);//将标注添加到地图中
            })
          } else {
            let allOverlay = _this.BdMap.getOverlays();
            allOverlay.filter(item => {
              if (item._key && item._key === 'device') {
                _this.BdMap.removeOverlay(item);
                console.log(item)
              }
            })
          }
        }
      });
  };

  // 督查人员
  onChangeUser = (e) => {
    let _this = this;
    let obj = {};
    this.setState({
      checkUser: e.target.checked
    });
    request('/api/useraddress/supervision', { headers: { 'Authorization': token } })
      .then(res => {
        if (res.status === 0) {
          if (e.target.checked) {
            res.value.map((item, index) => {
              let point = new BMap.Point(item.longitude, item.latitude);
              obj[index] = function ComplexCustomOverlay(point, key) {
                this._point = point;
                this._key = key;
              };
              obj[index].prototype = new BMap.Overlay();
              obj[index].prototype.initialize = function () {
                this._map = _this.BdMap;
                let img = this._img = document.createElement('img');
                img.style.position = 'absolute';
                img.src = `${scUser}`;
                img.style.width = '15px';
                img.style.height = '40px';
                this._map.getPanes().labelPane.appendChild(img);
                return img
              };
              obj[index].prototype.draw = function () {
                let map = this._map;
                let img = this._img;
                let pixel = map.pointToOverlayPixel(this._point);
                this._img.style.left = pixel.x - img.width / 2 + "px";
                this._img.style.top = pixel.y - img.height / 2 + "px";
              };
              let myCompOverlay = new obj[index](point, 'user');
              _this.BdMap.addOverlay(myCompOverlay);//将标注添加到地图中
            })
          } else {
            let allOverlay = _this.BdMap.getOverlays();
            console.log(allOverlay);
            allOverlay.filter(item => {
              if (item._key && item._key === 'user') {
                _this.BdMap.removeOverlay(item);
              }
            })
          }
        }
      });
  };

  onChangeRadioButton = (e) => {

    if (e.target.value === "part") {
      let allOverlay = this.BdMap.getOverlays();
      allOverlay.filter(item => {
        if (item._track && item._track === 'track') {
          this.BdMap.removeOverlay(item);
        }
      })
    }

    this.setState({
      headerFormStart: e.target.value
    })
  };

  //地图轨迹点刷新清空
  handleSubmit = (e) => {
    let _this = this;
    const { form: { validateFields } } = this.props;
    validateFields((err, payload) => {
      if (!err) {
        let allOverlay = this.BdMap.getOverlays();
        allOverlay.filter(item => {
          if (item._track && item._track === 'track') {
            this.BdMap.removeOverlay(item);
          }
        })

        let map = [];
        payload.startTime = moment(payload.rangeTime[0]).format('YYYY/MM/DD HH:MM:SS');
        payload.endTime = moment(payload.rangeTime[1]).format('YYYY/MM/DD HH:MM:SS');
        delete payload.rangeTime;
        request('/api/useraddress/list', { params: { ...payload }, headers: { 'Authorization': token } })
          .then(res => {
            if (res.status === 0) {
              res.value.map((item, index) => {
                map[index] = new BMap.Point(item.longitude, item.latitude);
                let newMap = new BMap.Polyline(
                  map,
                  {
                    fillColor: 'red',
                    fillOpacity: 0.2,
                    strokeColor: 'red',
                    strokeWeight: 4,
                    strokeOpacity: 0.8,
                  });
                _this.userTrack = newMap;
                newMap._track = 'track';
                this.BdMap.addOverlay(newMap);
              })
            }
          });
        // let allOverlay = this.BdMap.getOverlays();
      }
    });
  };

  imageModel = () => {
    const { imageModelVisible, listImage } = this.state;
    return (
      <Modal
        title="高清大图"
        width={1250}
        maskClosable
        style={{ top: 30 }}
        className={styles.imageModel}
        onCancel={() => this.setState({ imageModelVisible: false })}
        visible={imageModelVisible}
        footer={null}
      >
        <div
          style={{
            width: '100%',
            height: 824,
            background: `url('${getUrl(listImage)}') center center / contain no-repeat`,
          }}
        />
      </Modal>
    )
  };

  returnSourceText = type => {
    switch (type) {
      case 'SYSTEM':
        return '摄像机抓拍';
      case 'APP':
        return '手机APP上报';
      default:
        return '管理员创建';
    }
  };

  alarmListModal = () => {
    const { alarmList, imgIndex } = this.state;
    console.log(imgIndex);
    console.log(alarmList);
    console.log(alarmList[imgIndex]);
    const columns = [
      {
        title: '现场图',
        dataIndex: 'image',
        key: 'image',
        render: (text, record) => (
          <Card
            style={{ width: '40px', height: '30px', marginTop: -8, marginLeft: -3, zIndex: 10 }}
            bodyStyle={{ padding: 0 }}
            onClick={(e) => {
              e.stopPropagation();
              text && this.setState({ imageModelVisible: true, listImage: text })
            }}
          >
            <img src={getUrl(text)} alt="" style={{ width: '40px', height: '30px' }} />
          </Card>
        ),
      },
      {
        title: '类别',
        dataIndex: 'categoryName',
        key: 'categoryName',
      },
      {
        title: '主体',
        dataIndex: 'source',
        key: 'source',
        render: val => <span>{this.returnSourceText(val)}</span>,
      },
      {
        title: '上报时间',
        dataIndex: 'createTime',
        key: 'createTime',
      },
    ];
    return (
      <Modal
        visible={this.state.alarmListModalVisible}
        title="告警列表"
        footer={null}
        maskClosable
        centered
        width={1000}
        className={styles.alarmListModal}
        onCancel={() => this.setState({ alarmListModalVisible: false })}
      >
        <Table
          dataSource={alarmList && alarmList.length && [alarmList[imgIndex]]}
          columns={columns}
          onRow={record => {
            return {
              onClick: e => {
                this.handleOnRowClick(record.id, 2);
                this.setState({
                  alarmListModalVisible: false
                })
              }
            }
          }}
          pagination={{
            position: 'none'
          }}
        />
      </Modal>
    )
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
          console.log(res.value);
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
    let imagePath = [];
    let imagePercent = [];
    if (fileList) {
      fileList.map(item => {
        imagePercent.push(item.percent.toFixed(2))
      });
      this.setState({ imagePercent });
    }
    if (file.response) {
      imagePath.push(file.response.value);
    }
    if (imagePath) {
      this.setState({
        image: this.state.image.concat(imagePath) || {}
      });
    }
  };

  // 上传视频
  fileVideoChange = (info) => {
    const { file, event, fileList } = info;
    let videoPath = [];
    let videoPercent = [];
    if (fileList) {
      fileList.map(item => {
        videoPercent.push(item.percent.toFixed(2))
      });
      this.setState({ videoPercent });
    }
    if (file.response) {
      videoPath.push(file.response.value);
    }
    if (videoPath) {
      this.setState({
        video: this.state.video.concat(videoPath) || {}
      }, () => {
        console.log(this.state.video)
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
              this.onChangeAlarm()
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
        console.log(payload)
        if (time.type !== 'REVIEW') {
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
        visible={this.state.unitTreeVisible}
        centered
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
        centered
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
    console.log(itemType);
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
      okText: '确定',
      centered: true,
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
      okText: '确定',
      centered: true,
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
              this.getListAll();
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
          this.getListAll();
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
        centered
        width={1250}
        className={style.previewModalWarp}
        key={8000}
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
                            <img
                              src={noArrowsLeft}
                              className={style.noArrows}
                            />
                          )
                            :
                            (
                              <img
                                src={arrowsLeft}
                                onClick={() => {
                                  this.refs.beforeImageWelcome.next()
                                }
                                }
                                className={style.arrows}
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
                                    console.log(item);
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
                                            background: `url('${getUrl(item)}') center center / contain no-repeat`
                                          }}
                                          onClick={e => {
                                            console.log('12312313');
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
                            <img
                              src={noArrowsRight}
                              className={style.noArrows}
                            />
                          )
                            :
                            (
                              <img
                                src={arrowsRight}
                                onClick={() => {
                                  this.refs.beforeImageWelcome.next()
                                }
                                }
                                className={style.arrows}
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
                            <img
                              src={noArrowsLeft}
                              className={style.noArrows}
                            />
                          )
                            :
                            (
                              <img
                                src={arrowsLeft}
                                onClick={() => {
                                  console.log(this);
                                  this.refs.afterImageWelcome.prev()
                                }
                                }
                                className={style.arrows}
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
                            <img
                              src={noArrowsRight}
                              className={style.noArrows}
                            />
                          )
                            :
                            (
                              <img
                                src={arrowsRight}
                                onClick={() => {
                                  console.log(this);
                                  this.refs.afterImageWelcome.next()
                                }
                                }
                                className={style.arrows}
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
                            <img
                              src={noArrowsLeft}
                              alt=""
                              className={style.noArrows}
                            />
                          )
                            :
                            (
                              <img
                                src={arrowsLeft}
                                alt=""
                                onClick={() => {
                                  this.refs.beforeVideoWelcome.next()
                                }
                                }
                                className={style.arrows}
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
                            <img
                              alt=""
                              src={noArrowsRight}
                              className={style.noArrows}
                            />
                          )
                            :
                            (
                              <img
                                alt=""
                                src={arrowsRight}
                                onClick={() => {
                                  this.refs.beforeVideoWelcome.next()
                                }
                                }
                                className={style.arrows}
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
                            <img
                              alt=''
                              src={noArrowsLeft}
                              className={style.noArrows}
                            />
                          )
                            :
                            (
                              <img
                                alt=''
                                src={arrowsLeft}
                                onClick={() => {
                                  console.log(this);
                                  this.refs.afterVideoWelcome.next()
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
                            <img
                              alt=''
                              src={noArrowsRight}
                              className={style.noArrows}
                            />
                          )
                            :
                            (
                              <img
                                alt=''
                                src={arrowsRight}
                                onClick={() => {
                                  console.log(this);
                                  this.refs.afterVideoWelcome.next()
                                }
                                }
                                className={style.arrows}
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

  toggleFullScreen() {
    let doc = document.documentElement,
      state = (document.webkitIsFullScreen || document.isFullScreen),
      requestFunc = (doc.requestFullscreen || doc.webkitRequestFullScreen),
      cancelFunc = (document.cancelFullScreen || document.webkitCancelFullScreen);

    (!state) ? requestFunc.call(doc) : cancelFunc.call(document);
  }

  render() {
    const { form: { getFieldDecorator } } = this.props;
    const { headerFormStart } = this.state;
    return (
      <Fragment>
        <Row type='flex' style={{ margin: '10px 0', paddingLeft: 30 }}>
          <Radio.Group defaultValue='part' buttonStyle="solid" onChange={this.onChangeRadioButton}>
            <Radio.Button size='large' value='part'>分布图</Radio.Button>
            <Radio.Button size='large' value='user'>人员轨迹</Radio.Button>
          </Radio.Group>
          {
            headerFormStart === 'part' && (
              <Row style={{ paddingTop: 5, marginLeft: 50 }}>
                <Checkbox
                  value='part'
                  checked={this.state.checkStation}
                  onChange={this.onChangeStation}
                >
                  工作台
                </Checkbox>
                <Checkbox
                  value='device'
                  checked={this.state.checkDevice}
                  onChange={this.onChangeDevice}
                >
                  摄像机
                </Checkbox>
                <Checkbox
                  value='user'
                  checked={this.state.checkUser}
                  onChange={this.onChangeUser}
                >
                  工作人员
                </Checkbox>
              </Row>
            )
          }
          {
            headerFormStart === 'user' && (
              <Fragment>
                <Form style={{ width: 700 }} className={styles.headerFormWrap}>
                  <Row type='flex' span={24}>
                    <Col span={9}>
                      <Form.Item label='执法人员' {...formHeaderItemLayout}>
                        {getFieldDecorator('userId', {
                          rules: [{ required: true, message: '请选择执法人员' }]
                        })(<TreeSelect placeholder='请选择执法人员' />)}
                      </Form.Item>
                    </Col>
                    <Col span={14} offset={1}>
                      <Form.Item {...formHeaderItemLayout}>
                        {getFieldDecorator('rangeTime', {
                          rules: [{ required: true, message: '请选择时间' }]
                        })(<RangePicker showTime={{ format: 'HH:mm:ss' }} />)}
                      </Form.Item>
                    </Col>
                  </Row>
                </Form>
                <Button type='primary' style={{ position: "relative" }} onClick={this.handleSubmit}>查询</Button>
              </Fragment>
            )
          }
          <div
            className={this.state.isHeader ? styles.my_map : styles.map_min}
            ref='mapMax'
            style={{ position: 'absolute', right: 13, top: 69, width: 40, height: 40 }}
            onClick={() => {
              this.setState({
                isHeader: !this.state.isHeader
              });

              let header = document.getElementsByTagName(('header'))[0];
              let main = document.getElementsByTagName(('main'))[0];
              let map = this.refs.map;
              let mapMax = this.refs.mapMax;
              if (this.state.isHeader) {
                header.style.display = 'none';
                main.style.padding = '0px';
                map.style.height = '94vh';
                mapMax.style.top = '5px';
                this.toggleFullScreen();
              } else {
                header.style.display = 'block';
                main.style.paddingTop = '64px';
                map.style.height = '87vh';
                mapMax.style.top = '69px';
                this.toggleFullScreen();
              }
            }}
          />
        </Row>
        <div ref="map" style={{ width: '100%', height: '87vh' }} />
        {this.state.alarmListModalVisible && this.alarmListModal()}
        {this.state.imageModelVisible && this.imageModel()}
        {this.state.unitTreeVisible && this.unitTree_Modal()}
        {this.state.workPathVisible && this.workPath_Modal()}
        {this.state.RETURN_OR_REVIEWVisible && this.RETURN_OR_REVIEW()}
        {this.state.TimeDelayVisible && this.TimeDelay_Modal()}
        {this.state.fileItemModelVisible && this.fileItemModel()}
        {this.state.previewModalVisible && this.previewModal()}
      </Fragment>
    );
  }
}
export default Map;
