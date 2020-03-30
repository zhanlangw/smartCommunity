import request from '@/utils/request';
import { stringify } from 'qs';

// 获取个人中心
export async function getPersonalUserItem (params) {
  return request(`/api/user/item?${stringify(params)}`);
}

// 修改个人中心
export async function updPersonalUserItem (params) {
  return request(`/api/user/upd`, { method: 'post', data: params });
}