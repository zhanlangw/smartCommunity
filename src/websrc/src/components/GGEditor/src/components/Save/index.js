import React from "react";
import { Button } from 'antd';
import { withPropsAPI } from "gg-editor";

class Save extends React.Component {
  handleClick = () => {
    const { propsAPI } = this.props;

  };

  render() {
    return (
      <div style={{ padding: 8 }}>
        <Button onClick={this.handleClick}>保存</Button>
      </div>
    );
  }
}

export default withPropsAPI(Save);
