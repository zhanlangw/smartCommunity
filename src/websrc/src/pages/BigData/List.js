import React, { useRef, useEffect, useState } from 'react';
import { Row } from 'antd';
import { connect } from 'dva';
import style from './style.less';

function List(props) {
  const { dataSource } = props;
  return (
    <div className={style.BigDataList}>
      <span>实时数据播报</span>
      <Row type='flex' justify='space-around' style={{width: '80%',  margin: '0 auto 4vh'}}>
        <div className={style.BigDataItem} style={{width: '25%'}}>
          <div>
            {dataSource.totalCount}
          </div>
          案卷总数
        </div>
        <div className={style.BigDataItem} style={{width: '25%'}}>
          <div>
            {dataSource.finishCount}
          </div>
          已处置
        </div>
      </Row>
      <Row type='flex' justify='space-between' style={{width: '80%', margin: '0 auto',}}>
        <div className={style.BigDataItem}>
          <div>
            {dataSource.unFinishCount}
          </div>
          未处置
        </div>
        <div className={style.BigDataItem}>
          <div>
            {dataSource.timeOutCount}
          </div>
          超时案卷
        </div>
        <div className={style.BigDataItem}>
          <div>
            {dataSource.alarnCount}
          </div>
          告警次数
        </div>
      </Row>
    </div>
  )
}

export default connect(({}) => ({
  //
}))(List);
