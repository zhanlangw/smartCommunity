import React, { PureComponent, Fragment } from 'react';
import { Button, Icon } from 'antd';
import styles from './style.less';

export default class Query extends React.Component{
    render(){
        const { visible, handleOk, handleReset, handelCancel, hideOk, hideReset, title, okText,width, footer} = this.props;
        const footerVisible = footer===undefined? true: false
        const translateX = visible?'0':'100%';
        return(
          <div className={styles.query} style={{height:`calc(100% - 64px)`,top:64,transform: `translateX(${translateX})`,opacity:visible?1:0,width:width?width:'385px'}}>
            <div className={styles.header}>
              <Icon onClick={handelCancel} type="close" style={{fontSize: 20,marginLeft: 12,marginTop:15,cursor: 'pointer',float:'left'}}/>
              <span style={{fontSize:14}}>{ title || '查询条件'}</span>
            </div>
            <div className={styles.content}>
              { this.props.children }
            </div>
          {
            footerVisible?
            <div className={styles.footer}>
            {
              !hideOk && <Button onClick={handleOk} type="primary">{okText || '查询'}</Button>
            }
              {
              !hideReset &&  <Button onClick={handleReset} type="primary" style={{marginLeft:48}}>重置</Button>
            }
            </div>
            :
            <div></div>
          }
          </div>
        )
    }
}
