import React, {PureComponent, Fragment} from 'react';
import {connect} from 'dva';
import {Row, Col, Button, Icon, Modal, Form, Input, Cascader} from 'antd';
import {payload_0_10} from "@/utils/utils";

const {TextArea} = Input;

@Form.create()
@connect(({ blacklist, category }) => ({
  blacklist,
  category,
}))
class Index extends PureComponent {
  state = {};

  componentWillMount() {
    const { dispatch } = this.props;
    const timeOptions = [];
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
          const targetOption = selectedOptions[selectedOptions.length - 1];
          let options = blacklist.options_list;
          let arr = [];
          let has = options.map(item=> {
            if (item.value === selectedOptions[selectedOptions.length - 1].value){
              res && res.value.value.map(item => {
                let payload = {
                  value: item.id,
                  label: item.name,
                };
                arr.push(payload)
              });
              item.children = arr;
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

  handleCascaderOnChange = (e) => {
    const { dispatch } = this.props;
    let typeId = e[e.length-1];
    this.setState({
      typeId
    },()=>{
      dispatch({
        type: 'category/fetch_getSmallCategoryItem',
        payload: {
          id: typeId
        },
        callback: res => {
          this.props.getTypeId(res)
        }
      })
    });
  };

  render() {
    const { blacklist: { options_list } } = this.props;
    return (
      <Cascader
        placeholder='请选择案卷类型'
        options={options_list && options_list}
        loadData={this.handleCascaderLoadData}
        onChange={this.handleCascaderOnChange}
        changeOnSelect
      />
    );
  }
}

export default Index