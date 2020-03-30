import React, {Component} from 'react'

export default class countDown extends Component {
  constructor(props) {
    super(props);
    this.state = {
      day: 0,
      hour: 0,
      minute: 0,
      second: 0
    }
  }
  componentDidMount() {
    if(this.props.endTime){
      this.countFun(this.props.endTime);
    }
  }
  //组件卸载取消倒计时
  componentWillUnmount(){
    clearInterval(this.timer);
  }

  countFun = (time) => {
    this.timer = setInterval(() => {
      //防止倒计时出现负数
      if (time > 1000) {
        time -= 1000;
        let day = Math.floor((time / 1000 / 3600) / 24);
        let hour = Math.floor((time / 1000 / 3600) % 24);
        let minute = Math.floor((time / 1000 / 60) % 60);
        let second = Math.floor(time / 1000 % 60);
        this.setState({
          day:day,
          hour:hour < 10 ? "0" + hour : hour,
          minute:minute < 10 ? "0" + minute : minute,
          second:second < 10 ? "0" + second : second
        })
      } else {
        clearInterval(this.timer);
        //倒计时结束时触发父组件的方法
        //this.props.timeEnd();
      }
    }, 1000);
  };

  render() {
    return (
      //{this.state.day}天{this.state.hour}小时{this.state.minute}分钟
      <span style={{ fontSize: '12px', color: '#4C5FC1' }} >{this.state.second}</span>
    )
  }
}
