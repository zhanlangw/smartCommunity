import request from '@/utils/request';
import {stringify} from "qs";

export async function import_file(params) {
  return request('/api/user/import', { params })
}
//获取组织机构树
export async function getOrganizationTreeAction(params) {
  return request(`/api/unit/user/tree?${stringify(params)}`);
}

export async function getNoUserTree(params) {
  return request(`/api/unit/tree?${stringify(params)}`)
}

export async function sort(params) {
  return request(`/api/mgmt/org/sort?${stringify(params)}`)
}

//部门详情
export async function dept_item(params) {
  return request(`/api/unit/item?${stringify(params)}`)
}

//岗位详情
export async function post_item(params) {
  return request(`/api/mgmt/org/post/item?${stringify(params)}`)
}

//人员详情
export async function user_item(params) {
  return request(`/api/user/itemInUnit?${stringify(params)}`)
}

//组织机构详情
export async function getOrganizationItemAciton(params) {
  return request(`/api/unit/item?${stringify(params)}`)
}
//用户列表
export async function getUserListAction(params) {
  return request(`/api/user/list?${stringify(params)}`)
}

//添加用户获取模板
export async function getAddUserTemplateAction(params) {
  return request(`/api/user/addTemplate?${stringify(params)}`)
}

//添加组织获取模板
export async function getAddOriginTemplateAction(params) {
  return request(`/api/unit/addTemplate?${stringify(params)}`)
}

//删除组织机构
export async function delorg(params) {
  return request(`/api/mgmt/org/corp/del?${stringify(params)}`)
}

//删除组织机构
export async function delOriganizationAction(params) {
  return request(`/api/unit/del?${stringify(params)}`)
}

//删除岗位
export  async function delPostAction(params) {
  return request(`/api/mgmt/org/post/del?${stringify(params)}`)
}

//修改岗位
export async function updatePostAction(params) {
  return request('/api/mgmt/org/post/upd', { method: 'post', data: params })
}
//添加岗位
export async function addPostAction(params) {
  return request('/api/mgmt/org/post/add', { method: 'post', data: params })
}

//删除用户
export  async function delUserAction(params) {
  return request(`/api/user/del?${stringify(params)}`)
}

//修改组织机构
export async function updateOriganizationAction(params) {
  return request('/api/unit/upd', { method: 'post', data: params })
}
//添加用户
export async function addUserAction(params) {
  return request('/api/user/add', { method: 'post', data: params })
}
//添加组织机构
export async function addOriganizationAction(params) {
  return request('/api/unit/add', { method: 'post', data: params })
}
//修改用户
export async function updateUserAction(params) {
  return request('/api/user/updInUnit', { method: 'post', data: params })
}
//修改密码
export async function updateUserPasswordAction(params) {
  return request(`/api/user/password/reset`, { method: 'post', data: params });
}

// 新建部门
export async  function addUnitItem(params) {
  return request(`/api/unit/add`, {method: 'post', data: params})
}
