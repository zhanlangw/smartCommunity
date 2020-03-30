import request from '@/utils/request';
import { stringify } from 'qs';

// 获取列表
export async function getProcessList(params) {
  return request(`/api/process/list?${stringify(params)}`);
}

// 列表新增
export async function AddProcessItem (params) {
  return request(`/api/process/add/`, { method: 'post', data: params });
}

// 列表删除
export async function DelProcessItem (params) {
  return request(`/api/process/del?${stringify(params)}`);
}

// 获取流程图
export async function getProcessChart (params) {
  return request(`/api/process/item?${stringify(params)}`)
}

// 修改流程图
export async function ProcessChart (params) {
  return request(`/api/process/updstyle`, { method: 'post', data: params })
}

// id映射
export async function webIdMap (params) {
  return request(`/api/process/ids?${stringify(params)}`)
}

// 环节配置添加
export async function AddProcessLink (params) {
  return request(`/api/node/add`, { method: 'post', data: params })
}

// 环节配置详情
export async function getProcessLinkItem (params) {
  return request(`/api/node/item?${stringify(params)}`)
}

// 环节配置删除
export async function DelProcessLink(params) {
  return request(`/api/node/del?${stringify(params)}`)
}

// 路径配置添加
export async function AddProcessLinkPath (params) {
  return request(`/api/path/add`, { method: 'post', data: params })
}

// 路径配置详情
export async function getProcessPathItem (params) {
  return request(`/api/path/item?${stringify(params)}`)
}

// 路径配置删除
export async function DelProcessPath (params) {
  return request(`/api/path/del?${stringify(params)}`)
}


// 环节配置修改
export async function updProcessLinkItem (params) {
  return request(`/api/node/upd`, { method: 'post', data: params })
}


// 路径配置修改
export async function updProcessLinkPath (params) {
  return request(`/api/path/upd`, { method: 'post', data: params })
}

export async function updProcessItem (params) {
  return request(`/api/process/upd`, { method: 'post', data: params })
}

