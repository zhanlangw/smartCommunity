import React, { Component } from 'react';
import { connect } from 'dva';
import { TreeSelect } from 'antd';
import { stringify } from 'qs';
import request from '../../utils/request';
import {token} from "@/utils/utils";

const TreeNode =  TreeSelect.TreeNode;

@connect(({ global = {} }) => ({
    global,
}))
export default class TreeSelectNode extends Component {
    static defaultProps = {
      multiple:　false,
      labelInValue: false,
      type: [4],
    };

    constructor(props) {
      super(props);

      const value = this.props.value;
      this.state = {
        value: value,
        // defaultExpandedKeys:[]
      };
    }

    componentWillReceiveProps(nextProps) {
      // const { defaultExpandedKeys } = this.state;
      // if(defaultExpandedKeys.length === 0){
      //   const nextTree = nextProps.global.tree;
      //   if(!nextTree || nextTree.length === 0) return;
      //   const expandedKeys = [nextTree[0].key];
      //   this.setState({
      //     defaultExpandedKeys:expandedKeys,
      //   })
      // }
      if (nextProps.value) {
        const value = nextProps.value;
        this.setState({
          value : value
        });
      }else{
        this.setState({
          value : undefined
        });
      }
    };

    handleCurrencyChange = (currency) => {
      this.setState({ value:currency });
      this.triggerChange({ currency });
    };

    triggerChange = (changedValue) => {
      const { multiple } =this.props;
      const current = changedValue.currency;
      const onChange = this.props.onChange;
      if (onChange) {
        if(multiple){
          const res = current;
          onChange(res);
        }else{
          onChange(current);
        }
      }
    };

    renderTreeNodes = (data) => {
      const { type } =this.props;
      return data.map((item) => {
        // let disabled = true;
        // for (let index = 0; index < type.length; index++) {
        //   const e = type[index];
        //   if(item.type === e){
        //     disabled = false;
        //   }
        // }
        if (item.children) {
          return (
            <TreeNode value={item.key} title={item.title} key={item.key} dataRef={item}>
              {this.renderTreeNodes(item.children)}
            </TreeNode>
          );
        }
        return <TreeNode value={item.key} {...item} dataRef={item} title={item.title} />;
      });
    };

     onLoadData = (treeNode) => {
        let { dispatch, global:{ tree } } = this.props;
        const parentId = treeNode.props.dataRef.key;
        const params = {
          id:parentId,
        };
       return request('/api/unit/tree', {params, headers:{ 'Authorization': token}}).then(res=>{
          const children = res.value && res.value;
          treeNode.props.dataRef.children = children;
          tree = [...tree];
          dispatch({
            type:"global/tree",
            payload:tree,
          })
        });
      };


    render() {
        const { value } = this.state;
        const { global:{tree}, placeholder, multiple, labelInValue, disabled } =this.props;
        const defaultExpandedKeys = tree && tree[0] && [tree[0].key];
        return (
          <TreeSelect
            labelInValue={labelInValue}
            style={{ width: '100%' }}
            loadData={this.onLoadData}
            value={value}
            disabled={disabled}
            //treeDefaultExpandedKeys={defaultExpandedKeys}
            placeholder={placeholder}
            onChange={this.handleCurrencyChange}
            multiple = {multiple}
            dropdownStyle={{ maxHeight: 400, overflow: 'auto' }}
          >
            {this.renderTreeNodes(tree && tree)}
          </TreeSelect>
        );
    }
}
