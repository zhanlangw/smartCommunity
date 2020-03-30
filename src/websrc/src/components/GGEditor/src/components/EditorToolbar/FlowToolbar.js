import React from 'react';
import { Divider, Icon } from 'antd';
import { Toolbar } from 'gg-editor';
import ToolbarButton from './ToolbarButton';
import styles from './index.less';

const FlowToolbar = () => {
  return (
    <Toolbar className={styles.toolbar}>
      <ToolbarButton command="clear" text="清空" icon="clear"/>
      <ToolbarButton command="undo" text="上一步"/>
      <ToolbarButton command="redo" text="下一步"/>
      <Divider type="vertical" />
      <ToolbarButton command="copy" text="复制"/>
      <ToolbarButton command="paste" text="粘贴"/>
      <ToolbarButton command="delete" text="删除"/>
      <Divider type="vertical" />
      <ToolbarButton command="zoomIn" icon="zoom-in" text="放大" />
      <ToolbarButton command="zoomOut" icon="zoom-out" text="缩小" />
      <ToolbarButton command="autoZoom" icon="fit-map" text="合适大小" />
      <ToolbarButton command="resetZoom" icon="actual-size" text="还原大小" />
      <Divider type="vertical" />
      <ToolbarButton command="toBack" icon="to-back" text="To Back" />
      <ToolbarButton command="toFront" icon="to-front" text="To Front" />
      <Divider type="vertical" />
      <ToolbarButton command="multiSelect" icon="multi-select" text="多选" />
      {/* <ToolbarButton command="addGroup" icon="group" text="添加组" />
      <ToolbarButton command="unGroup" icon="ungroup" text="删除组" /> */}
    </Toolbar>
  );
};

export default FlowToolbar;
