import React, { PureComponent, Fragment } from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import { NodePanel, EdgePanel, GroupPanel, MultiPanel, CanvasPanel, DetailPanel } from 'gg-editor';
import DetailForm from './DetailForm';
import styles from './index.less';

@connect(() => ({
  //
}))
class FlowDetailPanel extends PureComponent {
  state={}

  render() {
    const { item , type } = this.props
    return (
      <DetailForm type={type} items={item} />
    );
  }
}
export default FlowDetailPanel;
