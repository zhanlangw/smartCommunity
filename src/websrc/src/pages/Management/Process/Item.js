import React, { PureComponent, Fragment } from 'react';
import { Row, Col, Button } from 'antd';
import { connect } from 'dva';
import GGEditor, { Flow } from 'gg-editor';
import { withRouter } from 'react-router-dom';
import FlowPage from '@/components/GGEditor/src/Flow';
import Save from '@/components/GGEditor/src/components/Save';



@withRouter
@connect(({ Process }) => ({
  Process,
}))
class Item extends PureComponent {
  state={};
  componentDidMount() {
    const { dispatch, match: {params: { id }} } = this.props
    dispatch({
      type: 'Process/fetch_getProcessChart',
      payload: {
        id
      }
    })
    dispatch({
      type: 'Process/fetch_webIdMap',
      payload: {
        id,
      },
    })
  }

  render() {
    const { Process: { process_chart } } = this.props
    const newData = process_chart && JSON.parse(process_chart.style)
    return (
      <div style={{backgroundColor:" #ffffff", minHeight: "91vh"}}>
        <GGEditor>
          {
            newData ? <FlowPage data={newData}/> : <FlowPage />
          }
        </GGEditor>
      </div>
    );
  }
}
export default Item;
