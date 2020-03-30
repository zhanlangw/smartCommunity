import React,{Fragment} from 'react';
import {connect} from 'dva';
import {Route, Switch, routerRedux} from 'dva/router';
import {Card, Layout, List, message, Form, Spin, Tree, Modal,Input,Upload,Button, Row} from 'antd';
import styles from './styles.less';
import {isInArray, getUrl, token, beforeUpload} from '../../../utils/utils';
import template from '@/assets/用户导入模板.xlsx';
import request from "../../../utils/request";
import { getAuthority } from '../../../utils/authority';
import {stringify} from "qs";
const Search = Input.Search;
const TreeNode = Tree.TreeNode;
import PeopleInfo from './UserInfo';
import DepartmentInfo from './DepartmentInfo';
import OrgInfo from './OrgInfo';
import PostInfo from './PostInfo';
import SearchList from './SearchList';
import { async } from 'q';

const confirm = Modal.confirm;
const {Content, Sider} = Layout;

@Form.create()
@connect(({organization, loading}) => ({
  organization,
  loading: loading.models.organization,
}))
export default class Organization extends React.Component {
  state = {
    client: {
      X: 0,
      Y: 0,
    },
    key: null,
    name: '',
    visible: false,
    expandedKeys: [],
    type: null,
    userType: false,
    thisUserKey: null,
    treeNode: null,
    editType: false,
    addStatus: false,
    treeNodeCompany:null,
    parentUid:null,
    targetUid:null,
  };

  componentDidMount() {
    document.addEventListener("click", this.close);
    const { dispatch } = this.props;
    dispatch({
      type: 'global/fetch_get_tree',
    });
    this.loadTreeInfo();
  }

  loadTreeInfo=()=>{
    this.props.dispatch({
      type: "organization/fetch_getOrganizationTree_action",
      payload:{},
    })
  };

  loadTreeInfoSource=()=>{
    const params = {
      id:this.state.parentUid,
    };
    this.props.dispatch({
      type:"organization/fetch_getOrganizationTree_action",
      payload:{
        ...params,
      },
      callback:(res)=>{
        if(res.status===0){
          this.loadTargetTreeInf();
        }
      }
    })
  };

  loadTargetTreeInf=()=>{
    const params = {
      id:this.state.targetUid
    };
    this.props.dispatch({
      type:"organization/fetch_getOrganizationTree_action",
      payload:{
        ...params,
      },
    })
  }

  componentWillReceiveProps(nextProps) {
    const {expandedKeys} = this.state;
    if (expandedKeys.length === 0) {
      const nextTree = nextProps.organization.unit_tree;
      if (nextTree.length === 0) return;
      const defaultExpandedKeys = [nextTree[0].key];
      this.setState({
        expandedKeys: defaultExpandedKeys,
      })
    }
  }

  // componentWillUnmount = () => {
  //   this.setState = (state, callback) => {
  //     return;
  //   };
  // }

  rightClick = ({event, node}) => {
    const type = node.props.dataRef.type;
    let dataSource;
    if (type === 'UNIT') {
      dataSource = ['新增部门', '新增人员'];
    } else if(type === 'USER') {
     return;
    }
    const clientHeight = document.body.clientHeight;
    const key = node.props.dataRef.key;
    const name = node.props.dataRef.title;
    event.persist();
    let X= event.clientX;
    let Y= event.clientY;
    if((clientHeight-Y)<115){
      Y = Y - (115-(clientHeight-Y))
    }
    this.setState({
      treeNode: node,
      client: {
        X:X,
        Y:Y,
      },
      key: key,
      name: name,
      visible: true,
      dataSource: dataSource,
    })
    // this.props.form.resetFields();
  };

  close = (e) => {
    e.stopPropagation();
    this.setState({
      visible: false,
    })
  };

  handleClick = (item) => {
    const {dispatch} = this.props;
    const {key, name} = this.state;
    if (item === '新增部门') {
      this.setState({
        type: 2,
        editType: false,
        parentKey: key,
        parentName: name,
      });
    }else if (item === '新增岗位') {
      this.props.form.resetFields();
      this.setState({
        type: 3,
        editType: false,
        parentKey: key,
        parentName: name,
      });
    } else {
      this.setState({
        type: 4,
        editType: false,
        parentKey: key,
        parentName: name,
      });
    }
  };


  onSelect = (selectedKeys, e) => {
    console.log(selectedKeys)
    console.log(e)
    const {dispatch} = this.props;
    const key = selectedKeys.join(',');
    const {selected} = e;
    if (!selected) {
      return;
    }
    const type = e.node.props.dataRef.type;
    if (type === 'UNIT') {
      this.setState({
        type: 2,
        thisUserKey: key,
        editType: true,
        parentUid:e.node.props.dataRef.uid
      });
      dispatch({
        type: "organization/fetch_dept_item",
        payload: {
          id: key,
        }
      })
    };
    // if (type === 2) {
    //   this.setState({
    //     type: 2,
    //     thisUserKey: key,
    //     editType: true,
    //     parentUid:e.node.props.dataRef.uid
    //   });
    //   dispatch({
    //     type: "organization/fetch_dept_item",
    //     payload: {
    //       id: key,
    //     }
    //   })
    // }
    // if (type === 3) {
    //   this.setState({
    //     type: 3,
    //     thisUserKey: key,
    //     editType: true,
    //     parentUid:e.node.props.dataRef.uid
    //   });
    //   dispatch({
    //     type: "organization/fetch_post_item",
    //     payload: {
    //       id: key,
    //     }
    //   })
    // }
    if (type === 'USER') {
      this.setState({
        type: 4,
        thisUserKey: key,
        editType: true,
        parentUid:e.node.props.dataRef.uid
      });
      dispatch({
        type: "organization/fetch_user_item",
        payload: {
          id: key,
        },
        callback : (res) => {
          let roleChildren = []
          res.role && res.role.map(item => {
            roleChildren.push(item.id)
          });
          const payload = {
            ...res,
            roleChildren
          };
          dispatch({
            type: 'organization/user_item',
            payload
          })
        }
      })
    }
  };


  handleSearch = (val) => {
    const {dispatch, organization: {userList_params}} = this.props;
    if (!val) {
      message.error("请先输入搜索内容");
      return;
    }
    this.setState({
      type: 5,
    });
    dispatch({
      type: 'organization/fetch_getUserList_action',
      payload: {
        // ...userList_params,
        name: val,
        start: 0,
        count: 10,
      }
    })
  };


  changeExpandedKeys = (e) => {
    const {expandedKeys} = this.state;
    const {node: {props}} = e;
    const {key, isLeaf} = props.dataRef;
    if (isInArray(expandedKeys, key)) {
      const newKeys = expandedKeys.filter(i => i !== key);
      this.setState({expandedKeys: newKeys});
    } else {
      if (!isLeaf) {
        expandedKeys.push(key);
        this.setState({expandedKeys});
      }
    }
  };

  onExpand = (onExpandedKeys, e) => {
    this.changeExpandedKeys(e);
  };

  onLoadData = (treeNode) => {
    console.log(treeNode);
    
    const {dispatch, organization} = this.props;
    const _this = this;
    const parentId = treeNode.props.dataRef.key;
    this.setState({
      treeNode: treeNode,
      parentId: parentId,
    });
    let unit_tree = organization.unit_tree;

    const params = {
      id: parentId,
    };
    return request('/api/unit/user/tree', {params,  headers:{ 'Authorization': token}}) .then(res => {
      const children = res.value;
      treeNode.props.dataRef.children = children;
      unit_tree = [...unit_tree];
      dispatch({
        type: "organization/unit_tree",
        payload: unit_tree,
      })
    });
  };

  IconType= (type, uid) => {
    if(type === 'UNIT') {
      if(uid === null) {
        return styles.icon_mechanism
      } else {
        return styles.icon_department;
      }
    } else {
      return styles.icon_user;
    }
  };

  renderTreeNodes = (data) => {
    return data.map((item) => {
      if (item.children && item.children.length>0) {
        return (
          <TreeNode icon={<i className={this.IconType(item.type, item.uid)}></i>} title={item.title} key={item.key} dataRef={item}>
            {this.renderTreeNodes(item.children)}
          </TreeNode>
        );
      }
      return <TreeNode {...item} icon={<i className={this.IconType(item.type)}></i>} dataRef={item}/>;
    });
  };


  //删除用户信息
  userHanldeDel = (thisKey) => {
    const {dispatch} = this.props;
    const _this = this;
    confirm({
      title: '提示',
      content: '确认删除吗？',
      okText: '确认',
      cancelText: '取消',
      onOk() {
        dispatch({
          type: 'organization/fetch_delUser_action',
          payload: {
            id: thisKey,
          },
          callback: (res) => {
            // _this.onLoadData(_this.state.treeNode);
            _this.setState({
              type:null
            });
            // dispatch({
            //   type:'global/fetch_get_unit',
            // });
            // dispatch({
            //   type:'global/fetch_get_tree',
            // });
            // dispatch({
            //   type: "organization/fetch_getOrganizationTree_action",
            //   payload:{
            //     id:_this.state.parentUid
            //   },
            //   callback:()=>{
            //     _this.setState({
            //       type:null
            //     })
            //   }
            // })
          }
        });
      },
    });
  };

  orgHanldeDel = (thisKey) => {
    const {dispatch} = this.props;
    const _this = this;
    confirm({
      title: '提示',
      content: '删除顶级部门将会删除组织机构数据，确认删除吗？',
      okText: '确认',
      cancelText: '取消',
      onOk() {
        dispatch({
          type: 'organization/fetch_delorg',
          payload: {
            id: thisKey,
          },
          callback: (res) => {
            dispatch({
              type:'global/fetch_get_unit',
            })
            dispatch({
              type:'global/fetch_get_tree',
            })

            _this.setState({
              type:null
            })

            // dispatch({
            //   type: "organization/fetch_getOrganizationTree_action",
            //   payload:{
            //     id:_this.state.parentUid
            //   },
            //
            // })
          }
        });
      },
    });
  }

  //删除部门信息
  departHanldeDel = (thisKey) => {
    const {dispatch} = this.props;
    const _this = this;
    confirm({
      title: '提示',
      content: '确认删除吗？',
      okText: '确认',
      cancelText: '取消',
      onOk() {
        dispatch({
          type: 'organization/fetch_delOriganization_action',
          payload: {
            id: thisKey,
          },
          callback: (res) => {
            // if (res.status === 0) {
            //   // _this.onLoadData(_this.state.treeNode)
            // }
            dispatch({
              type:'global/fetch_get_unit',
            })
            dispatch({
              type:'global/fetch_get_tree',
            })
            _this.setState({
              type:null
            })
            // dispatch({
            //   type: "organization/fetch_getOrganizationTree_action",
            //   payload:{
            //     id:_this.state.parentUid
            //   },
            //   callback:()=>{
            //     _this.setState({
            //       type:null
            //     })
            //   }
            // })
          }
        });
      },
    });
  };

  // reTreeMenu = (node) => {
  //   const {dispatch} = this.props;
  //   if (node.props.dataRef.uid){
  //     dispatch({
  //       type: "organization/fetch_getOrganizationTree_action",
  //       payload:{
  //         id: node.props.dataRef.uid || node.props.dataRef.key
  //       },
  //       callback:()=>{
  //         this.reTreeMenu(this.state.treeNode)
  //       }
  //     })
  //   } else {
  //     dispatch({
  //       type: "organization/fetch_getOrganizationTree_action",
  //       payload:{
  //         id:_this.state.treeNode.props.dataRef.key
  //       },
  //       callback: (res) => {
  //         if (res.status === 0) {
  //           this.User.handleSubmitCallback(true);
  //           this.setState({
  //             type: 7
  //           });
  //           this.loadTreeInfo();
  //           this.setState({
  //             parentUid:payload.pid,
  //             editType: true,
  //           })
  //         }
  //       }
  //     })
  //   }
  // };


  //提交用户信息
  userHanldeSubmit = (value) => {
    const {dispatch} = this.props;
    // const _this = this;
    // let node = this.state.treeNode;
    this.setState({
      targetUid:value.pid
    },()=>{
      if (this.state.editType) {
        dispatch({
          type: 'organization/fetch_updateUser_action',
          payload: {
            ...value
          },
          // callback: (res,payload) => {
          //   if (res.status === 0) {
          //
          //     // dispatch({
          //     //   type:'global/fetch_get_unit',
          //     // });
          //     // dispatch({
          //     //   type:'global/fetch_get_tree',
          //     // });
          //
          //
          //     // dispatch({
          //     //   type: "organization/fetch_user_item",
          //     //   payload: {
          //     //     id: value.id,
          //     //   },
          //     //   callback: (res) => {
          //     //     if (res.status === 0) {
          //     //       dispatch({
          //     //         type:'global/fetch_get_tree'
          //     //       });
          //     //       this.User.handleSubmitCallback(true);
          //     //       this.setState({
          //     //         type: 7
          //     //       })
          //     //     }
          //     //   }
          //     // });
          //     // dispatch({
          //     //   type: "organization/fetch_getOrganizationTree_action",
          //     //   payload:{
          //     //     id: node.props.dataRef.uid || node.props.dataRef.key
          //     //   },
          //     //   callback:()=>{
          //     //     dispatch({
          //     //       type: "organization/fetch_getOrganizationTree_action",
          //     //       payload:{
          //     //         id:_this.state.treeNode.props.dataRef.key
          //     //       },
          //     //       callback: (res) => {
          //     //         if (res.status === 0) {
          //     //           this.User.handleSubmitCallback(true);
          //     //           this.setState({
          //     //             type: 7
          //     //           })
          //     //         }
          //     //       }
          //     //     })
          //     //   }
          //     // });
          //     // _this.loadTreeInfo();
          //     // _this.onLoadData(_this.state.treeNode);
          //     _this.setState({
          //       parentUid:payload.pid,
          //       editType: true,
          //     })
          //   }
          // }
        });
      } else {
        dispatch({
          type: 'organization/fetch_addUser_action',
          payload: {
            ...value
          },
          // callback: (res) => {
          //   if (res.status === 0) {
          //     dispatch({
          //       type:'global/fetch_get_unit',
          //     })
          //     dispatch({
          //       type:'global/fetch_get_tree',
          //     })
          //     dispatch({
          //       type: "organization/fetch_getOrganizationTree_action",
          //       payload:{
          //         id:this.state.treeNode.props.dataRef.uid
          //       },
          //       callback:()=>{
          //         dispatch({
          //           type: "organization/fetch_getOrganizationTree_action",
          //           payload:{
          //             id:this.state.treeNode.props.dataRef.key
          //           },
          //         })
          //       }
          //     })
          //   }
          // }
        });
      }
    });
  };

  //提交部门信息
  departHanldeSubmit = value => {
    const {dispatch} = this.props;
    const _this = this;
    let node = this.state.treeNode;
    this.setState({
      targetUid:value.pid
    });
    if (this.state.editType) {
      dispatch({
        type: 'organization/fetch_updateOriganization_action',
        payload: {
          ...value,
        },
      })
      //   callback: (res,payload) => {
      //     if (res.status === 0) {
      //       if (res.value.id === 'ccc3ffcfc7174d66ab04d92236454ece'){
      //         node.props.dataRef.title = res.value.name;
      //       }
      //       // dispatch({
      //       //   type:"global/tree",
      //       //   payload:[node.props.dataRef],
      //       // });
      //       dispatch({
      //         type:'global/fetch_get_unit',
      //       });
      //       dispatch({
      //         type:'global/fetch_get_tree',
      //       });
      //       dispatch({
      //         type: "organization/fetch_getOrganizationTree_action",
      //         payload:{
      //           id:node.props.dataRef.uid || node.props.dataRef.key
      //         },
      //         callback:()=>{
      //           dispatch({
      //             type: "organization/fetch_getOrganizationTree_action",
      //             payload:{
      //               id:_this.state.treeNode.props.dataRef.key
      //             },
      //             callback: (res) => {
      //               if (res.status === 0) {
      //                 this.Depar.handleSubmitCallback(true);
      //                 this.setState({
      //                   type: 7
      //                 })
      //               }
      //             }
      //           });
      //         }
      //       });
      //       this.loadTreeInfo();
      //       this.setState({
      //         parentUid:payload.pid,
      //         editType: true,
      //       })
      //         // dispatch({
      //         //   type: "organization/fetch_getOrganizationItem_action",
      //         //   payload: {
      //         //     id: value.id,
      //         //   },
      //         //   callback: (res) => {
      //         //     if (res.status === 0) {
      //         //       dispatch({
      //         //         type:'global/fetch_get_tree',
      //         //         callback: res => {
      //         //           if (res.status === 0) {
      //         //             this.Depar.handleSubmitCallback(true);
      //         //             this.setState({
      //         //               type: 7
      //         //             })
      //         //           }
      //         //         }
      //         //       });
      //         //     }
      //         //   }
      //         // });
      //         // _this.loadTreeInfo();
      //         // _this.onLoadData(node);
      //       // _this.loadTreeInfo();
      //       // _this.onLoadData(node);
      //     }
      //     return false;
      //   }
      // });
    } else {
      dispatch({
        type: 'organization/fetch_addOriganization_action',
        payload: {
          ...value
        },
        // callback: (res) => {
        //   if (res.status === 0) {
        //     dispatch({
        //       type:'global/fetch_get_unit',
        //     });
        //     dispatch({
        //       type:'global/fetch_get_tree',
        //     });
        //     dispatch({
        //       type: "organization/fetch_getOrganizationTree_action",
        //       payload:{
        //         id:node.props.dataRef.uid || node.props.dataRef.key
        //       },
        //       callback:()=>{
        //         dispatch({
        //           type: "organization/fetch_getOrganizationTree_action",
        //           payload:{
        //             id:_this.state.treeNode.props.dataRef.key
        //           },
        //           callback: (res) => {
        //             if (res.status === 0) {
        //               this.Depar.handleSubmitCallback(true);
        //               this.setState({
        //                 type: 7
        //               })
        //             }
        //           }
        //         });
        //       }
        //     })
        //
        //   }
        // }
      });
    }
  };


  //改变页码
  changePage = (pagination) => {
    const {organization: {userList_params}} = this.props;
    this.props.dispatch({
      type: 'organization/fetch_getUserList_action',
      payload: {
        ...userList_params,
       start:(pagination.current-1)*pagination.pageSize,
        page: pagination.current
      }
    })
  };

  //点击行
  onClickRow = (record) => {
    console.log(record)
    this.setState({
      type: 4,
      thisUserKey: record.id,
      editType: true,
    });
    this.props.dispatch({
      type: "organization/fetch_user_item",
      payload: {
        id: record.id,
      }
    })
  };

  onDrop = (info) => {
    const { dispatch,organization: {unit_tree} } =this.props;
    const dropPosition = info.dropPosition;
    const par_id = info.dragNode.props.dataRef.uid;// 移动对象的pid
    const this_id = info.dragNode.props.dataRef.key;// 移动对象的id
    const target_pid = info.node.props.dataRef.uid;// 移动目标位置的pid
    const next_id = info.node.props.dataRef.key;// 移动目标位置的id
    if(par_id !== target_pid){
      return;
    }
    let parent;
    function get_parent(tree){
      tree.forEach(e => {
        if(e.key === par_id){
          parent = e;
          return;
        }
        if(e.children){
          get_parent(e.children);
        }
      })
    }
    get_parent(unit_tree);
    const childrenLength = parent.children && parent.children.length || 0;

    let params;
    if(childrenLength === dropPosition){
      params = {
        par_id:par_id,
        this_id:this_id,
        next_id:null,
      }
    }else{
      params = {
        par_id:par_id,
        this_id:this_id,
        next_id:next_id,
      }
    }
    dispatch({
      type: "organization/fetch_sort",
      payload: {
        ...params
      },
      callback:function(res){
        dispatch({
          type: "organization/fetch_getOrganizationTree_action",
          payload:{
            id:par_id
          },
        })
      }
    })
  };

  handleChange = async(fileList) => {
    const { file: { response } } = fileList;
    const { dispatch, organization: { unit_tree } } = this.props;
    let treeNode = {
      props: {
        dataRef: {
          key: null
        }
      }
    }
    const self = this;
    if (response && response.status === 0) {
      await dispatch({
          type:'organization/fetch_import',
          payload:{
            path: response.value
          },
      })
      await dispatch({
        type: 'organization/fetch_getOrganizationTree_action',
        callback: res=> {
          console.log(res[0]);
          
          let params = {
            id: res[0].key
          }
          console.log(params);
          return request('/api/unit/user/tree', { params, headers: { 'Authorization': token } }).then(data => {
            const children = data.value;
            res[0].children = children;
            console.log(res);
            dispatch({
              type: "organization/unit_tree",
              payload: res,
            })
          });
        }
      })
    }else{
      response && message.error(response.message);
    }
  };

  render() {
    const {organization: {unit_tree, dept_item,post_item,user_item, search_list, userList_params}, loading} = this.props;
    const {client: {X, Y}, visible, dataSource, expandedKeys, type, thisUserKey, parentKey, parentName, editType} = this.state;
    const {totalCount, value} = search_list;
    let newWidth = type === 5 ? '100%' : '800px';
    const paginationProps = {
      pageSize: userList_params.pageSize,
      total: totalCount,
      current: userList_params.page,
    };
    // const token = getAuthority() && getAuthority().value && getAuthority().value.token || '';
    return (
      <Card bordered={false} bodyStyle={{padding:0,  backgroundColor: "#fff", height: "93vh"}}>
        <Layout style={{minHeight: 500, height: "91vh", backgroundColor: "#fff"}}>
          <Row>
            <div style={{display:'flex',padding: "8%"}}>
              <div>
                <Upload
                  name="file"
                  onChange={this.handleChange}
                  action={getUrl("/api/file/upload?fileType=5")}
                  // data={{serviceId:'59e6ad4f117832d391b7963a'}}
                  showUploadList={false}
                  accept=".xls,.xlsx"
                  beforeUpload={beforeUpload}
                  headers={{
                    "Authorization": token,
                  }}
                >
                  <Button loading={loading} type="primary">
                    导入文件
                  </Button>
                </Upload>
                <Button style={{marginLeft:12}} loading={loading} type="primary" download='用户导入模板' href={template}>
                  下载模板
                </Button>
              </div>
              {
                visible && <div
                  className={styles.list}
                  style={{
                    position: 'fixed',
                    left: X,
                    top: Y,
                    zIndex: 99,
                    whiteSpace: 'nowrap',
                    background: '#fff'
                  }}>
                  <List
                    size="small"
                    bordered
                    dataSource={dataSource}
                    renderItem={item => (<List.Item>
                      <div onClick={() => this.handleClick(item)}>{item}</div>
                    </List.Item>)}
                  />
                </div>
              }
            </div>
            <Sider
              style={{background: '#fff', marginLeft: 10}}
              className={styles.sider}
              breakpoint="lg"
              collapsedWidth="0"
            >
              <Spin spinning={loading}>
                <Tree
                  showIcon
                  onDrop={this.onDrop}
                  onExpand={this.onExpand}
                  expandedKeys={expandedKeys}
                  onSelect={this.onSelect}
                  loadData={this.onLoadData}
                  onRightClick={this.rightClick}
                >
                  {this.renderTreeNodes( unit_tree && unit_tree)}
                </Tree>
              </Spin>
            </Sider>
          </Row>

          <Layout style={{minHeight: 500, backgroundColor:"#fff", paddingRight: 10, paddingTop: 10}}>
            <Content style={{margin: '0 0 0 24px', border: "1px solid #E6E9F0"}}>
              <Search
                placeholder="请输入关键字"
                onSearch={this.handleSearch}
                style={{ width: 200, float: "right", margin: "10px 20px"}}
              />
              <div style={{width:`${newWidth}` ,margin: "0 auto", marginTop: 60}}>
              {
                type === 1 ? <OrgInfo
                  orgHanldeSubmit={(value) => this.departHanldeSubmit(value)}
                  orgHanldeDel={this.orgHanldeDel}
                  thisKey={thisUserKey}
                  info={dept_item[thisUserKey]}
                  parentKey={parentKey}
                /> : type === 2 ? <DepartmentInfo
                // 部门
                  departHanldeDel={this.departHanldeDel}
                  departHanldeSubmit={(value) => this.departHanldeSubmit(value)}
                  thisKey={thisUserKey}
                  depart_info={dept_item[thisUserKey]}
                  parentName={parentName}
                  parentKey={parentKey}
                  editType={editType}
                  onRef={(ref)=> this.Depar = ref}
                /> : type === 3 ? <PostInfo
                  postHanldeDel={this.postHanldeDel}
                  postHanldeSubmit={(value) => this.postHanldeSubmit(value)}
                  thisKey={thisUserKey}
                  depart_info={post_item[thisUserKey]}
                  parentName={parentName}
                  parentKey={parentKey}
                  editType={editType}
                /> : type === 4 ? <PeopleInfo
                  // 角色
                  userHanldeDel={this.userHanldeDel}
                  userHanldeSubmit={(value) => this.userHanldeSubmit(value)}
                  thisKey={thisUserKey}
                  depart_info={user_item[thisUserKey]}
                  parentName={parentName}
                  parentKey={parentKey}
                  editType={editType}
                  onRef={(ref)=> this.User = ref}
                /> : type === 5 ? <SearchList
                  search_list={value}
                  rowKey={record=>record.key}
                  paginationProps={paginationProps}
                  changePage={(pagination) => this.changePage(pagination)}
                  onClickRow={(value) => this.onClickRow(value)}
                /> : null
              }
              </div>
            </Content>
          </Layout>
        </Layout>
      </Card>
    );
  }
}
