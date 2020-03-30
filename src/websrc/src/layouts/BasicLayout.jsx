/**
 * Ant Design Pro v4 use `@ant-design/pro-layout` to handle Layout.
 * You can view component api by:
 * https://github.com/ant-design/ant-design-pro-layout
 */
import ProLayout from '@ant-design/pro-layout';
import React, {useEffect, useState, useRef} from 'react';
import Authorized from '@/utils/Authorized';
import Link from 'umi/link';
import {Layout, Icon, Typography, Statistic, Spin} from 'antd';
import RightContent from '@/components/GlobalHeader/RightContent';
import { connect } from 'dva';
import { formatMessage } from 'umi-plugin-react/locale';
import style from './UserLayout.less';
import router from "umi/router";

import newLogo from '../assets/newlogo.png';
import logoTitle from '@/assets/logoTitle.png';
import noRight from '@/assets/noRight.png';
import logo from '@/assets/newlogo.png';

import GetFileItem from '@/components/getFileItem';
import CounDown from '@/components/CountDown';
import request from '@/utils/request';
import {getUrl, returnSourceText, returnWorkTypeText, token} from "@/utils/utils";

const { Text } = Typography;
const { Countdown } = Statistic;
const deadline = 15;
/**
 * use Authorized check all menu item
 */
const { Content, Header, Footer } = Layout;

const menuDataRender = menuList =>
  menuList.map(item => {
    const localItem = { ...item, children: item.children ? menuDataRender(item.children) : [] };
    return Authorized.check(item.authority, localItem, null);
  });

// const footerRender = (_, defaultDom) => {
//   if (isAntDesignPro()) {
//     return defaultDom;
//   }

//   return (
//     <>
//       {defaultDom}
//       <div
//         style={{
//           padding: '0px 24px 24px',
//           textAlign: 'center',
//         }}
//       >
//         <a href="https://www.netlify.com" target="_blank" rel="noopener noreferrer">
//           <img
//             src="https://www.netlify.com/img/global/badges/netlify-color-bg.svg"
//             width="82px"
//             alt="netlify logo"
//           />
//         </a>
//       </div>
//     </>
//   );
// };

const BasicLayout = props => {
  const { dispatch, children, settings } = props;
   /**
   * constructor
   */
  const [file, setFile] = useState();
  const [display, setDisplay] = useState('block');
  const [messageList, setMessageList] = useState([]);
  const [fileItem, setFileItem] = useState(null);
  const [loading, setLoading] = useState(true);

  setTimeout(()=>{
    setDisplay('none');
  }, 17000);

  useEffect(()=> {
    setLoading(true)
    request('/api/work/home/list', {headers: token}).then((res)=>{
      if (res.status === 0) {
        setMessageList(res.value);
      }
    });

    request('/api/basis/item', {headers: token})
      .then(res => {
        if (res.status === 0) {
          if(res.value.imagePath) {
            setFile(
              <div
                style={{
                  height: 64,
                  marginLeft: 18,
                  marginTop: -3,
                }}
               //logo跳转工作台
                onClick={() => router.push('/workbench')}
              >
                <img
                  src={getUrl(res.value && res.value.imagePath)}
                  alt=""
                  style={{
                    // width: 40,
                    height: 50,
                    marginRight: 5
                  }}
                />
              </div>
            );
          } else {
            setFile (
              <div
                style={{
                  height: 64,
                  marginLeft: 18,
                  marginTop: -3,
                }}
                  //logo跳转工作台
                onClick={() => router.push('/workbench')}
              >

                <img
                  src={newLogo}
                  alt=""
                  style={{
                    width: 40,
                    height: 40,
                    marginRight: 5
                  }}
                />
                <img
                  src={logoTitle}
                  alt=""
                  style={{
                    width: 72,
                    height: 24
                  }}
                />
              </div>
            )
          }

          setLoading(false);

        } else {
          setLoading(false)
        }
      });
    dispatch({
      type: 'global/fetch_get_tree',
    });
  }, []);

  useState(() => {
    if (dispatch) {
      dispatch({
        type: 'settings/getSetting',
      });
    }
  });
  /**
   * init variables
   */

  const returnTypeStyle = type => {
    switch(type) {
      case 'TODO':
        return '#F06565';
      case 'REVIEW':
        return '#4C5FC1';
      case 'DELAY':
        return '#F0B765';
      case 'RETURN':
        return '#3BA9AD';
      default:
        break;
    }
  };

  const returnTypeText = type => {
    switch(type) {
      case 'TODO':
        return '待处置';
      case 'REVIEW':
        return '待核查';
      case 'DELAY':
        return '申请延时';
      case 'RETURN':
        return '退件';
      default:
        break;
    }
  };


  const handleMenuCollapse = payload =>
    dispatch &&
    dispatch({
      type: 'global/changeLayoutCollapsed',
      payload,
    });

  return (
    <ProLayout
      logo={(
        <Spin spinning={loading}>
         {file}
        </Spin>
      )}
      onCollapse={handleMenuCollapse}
      menuItemRender={(menuItemProps, defaultDom) => (
        <Link style={{ color: "#fff"}} to={menuItemProps.path}>{defaultDom}</Link>
      )}
      breadcrumbRender={(routers = []) => [
        {
          path: '/',
          breadcrumbName: formatMessage({
            id: 'menu.home',
            defaultMessage: 'Home',
          }),
        },
        ...routers,
      ]}
      footerRender={()=> {
          if (messageList.length) {
             return (
              <div ref='messageModalWarp' style={{display,position: 'fixed', right: 0, bottom: 0, width: 472, borderRadiusTop: 4}} className={style.messageModalWarp}>
                <header style={{height: 40, backgroundColor:'#4C5FC1', textAlign:'left',borderTopLeftRadius: 4, borderTopRightRadius: 4}}>
                  <div style={{fontSize: 16, padding: 9, color: '#fff'}}>
                    <Icon type="exclamation-circle" theme="twoTone" style={{fontSize: 18, marginRight: 10 }}/>
                    消息提醒
                    <Text style={{float: "right", color: "#fff", cursor:"pointer"}} onClick={()=> setDisplay("none")}>X</Text>
                  </div>
                </header>
                <main className={style.messageMainWarp} style={{backgroundColor: '#fff'}}>
                  {
                    messageList.map((item, index) => {
                      return (
                        <div
                          key={index}
                          onClick={(e)=> {
                            if (item.type === 'RETURN') {
                              fileItem.handleOnRowClick(item.id,  1, true)
                            } else {
                              fileItem.handleOnRowClick(item.id,  1)
                            }
                          }}
                          style={{cursor:"pointer", display: 'flex', width:452, height:80, background:"rgba(244,244,244,1)", borderRadius:5, margin: "10px auto"}}
                        >
                          <span style={{width: 62, height: 62, marginTop: 9, marginLeft: 10, backgroundColor:`${returnTypeStyle(item.type)}`, color: '#fff', borderRadius: '50%', lineHeight: '62px', textAlign: 'center'}}>{returnTypeText(item.type)}</span>
                          <dl style={{marginTop: 13, width: "80%", padding: '0 10px'}}>
                            <dt style={{marginBottom: 12}}>{item.title}</dt>
                            <dd><span style={{marginRight: 10}}>{item.creator}</span><span>{item.createTime}</span><span style={{float: 'right', color:"rgba(58,135,228,1)"}}>{returnWorkTypeText(item.workType)}</span></dd>
                          </dl>
                          <img src={noRight} alt="" style={{width: 11, height: 20, marginTop: 30}}/>
                        </div>
                      )
                    })
                  }
                </main>
                <footer style={{textAlign: "center", marginBottom: 10, color: "#4C5FC1", backgroundColor: "#fff"}}>
                  <CounDown endTime={15000}/>s后自动关闭
                </footer>
                <GetFileItem onRef = {(ref) => {setFileItem(ref)}}/>
              </div>
            )
          }
        }
      }
      menuDataRender={menuDataRender}
      formatMessage={formatMessage}
      rightContentRender={rightProps => <RightContent {...rightProps} />}
      {...props}
      {...settings}
    >
    {/*{children}*/}
    </ProLayout>
  );
};

export default connect(({ global, settings }) => ({
  collapsed: global.collapsed,
  settings,
}))(BasicLayout);
