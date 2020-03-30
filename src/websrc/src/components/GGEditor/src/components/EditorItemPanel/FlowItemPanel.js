import React from 'react';
import { Card } from 'antd';
import { ItemPanel, Item } from 'gg-editor';
import styles from './index.less';
import child from '@/assets/child.svg';
import defaultimg from '@/assets/default.svg';
import start from '@/assets/start.svg';
import end from '@/assets/end.svg';


const FlowItemPanel = () => {
  return (
    <ItemPanel className={styles.itemPanel}>
      <Card bordered={false}>
        <Item
          type="node"
          size="72*72"
          shape="flow-circle"
          model={{
            color: '#47BDE3',
            label: '开始',
          }}
          src={start}
        />
        <Item
          type="node"
          size="80*48"
          shape="flow-rect"
          model={{
            color: '#5CB85C',
            label: '常规节点',
          }}
          src={defaultimg}
        />
        <Item
          type="node"
          size="80*72"
          shape="flow-rhombus"
          key="admin"
          model={{
            color: '#F4B956',
            label: '作废',
          }}
          src={child}
        />
        <Item
          type="node"
          size="80*48"
          shape="flow-capsule"
          model={{
            color: '#F8906C',
            label: '结束',
          }}
          src={end}
        />
      </Card>
    </ItemPanel>
  );
};

export default FlowItemPanel;
