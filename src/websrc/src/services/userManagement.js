import request from '@/utils/request';
import { stringify } from 'qs';

// 获取角色列表
export async function getUserList(params) {
  return request(`/api/role/list?${stringify(params)}`)
}

// 新增角色
export async function addUserItem(params) {
  return request(`/api/role/add`, { method: 'post', data: params })
}

// 获取角色详情
export async function getUserItem(params) {
  return request(`/api/role/item?${stringify(params)}`)
}

// 修改角色
export async function updUserItem (params) {
  return request(`/api/role/upd`, { method: 'post', data: params })
}

// 删除角色
export async function DelUserItem(params) {
  return request(`/api/role/del?${stringify(params)}`)
}

// 获取权限树
export async function getPermissionTree(params) {
  return request(`/api/role/permission/tree?${stringify(params)}`)
}

// 获取摄像头树
export async function getDeviceTree (params) {
  return request(`/api/role/device/tree?${stringify(params)}`)
}