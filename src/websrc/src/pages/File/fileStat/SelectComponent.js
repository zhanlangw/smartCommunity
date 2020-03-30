import React, { useState, useEffect} from 'react';
import { Select, Spin } from 'antd';
import { connect } from 'dva';
import request from "@/utils/request";
import item from "@/pages/BigData/models/item";

const { Option } = Select;

function SelectComponent(props) {

  const { selectDataSource } = props ;

  useEffect(() => {
  }, [selectDataSource]);

  const selectComponentOnSelect = (value, e) => {
    props.onSelect(e.props.item);
  };

  return (
    selectDataSource && (
       <Select
         defaultValue={'all'}
         onSelect={selectComponentOnSelect}
         style={props.style}
         getPopupContainer={triggerNode => triggerNode.parentElement}
       >
         <Option key={'option_all'} value={'all'} item='all'>全部</Option>
         {
           selectDataSource.map(item => {
             return (
               <Option key={item.key} value={item.key} item={item}>{item.title}</Option>
             )
           })
         }
       </Select>
     )
  )
}

export default React.memo(SelectComponent)
