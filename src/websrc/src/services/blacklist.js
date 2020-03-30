import request from '@/utils/request';


//--------------------------------黑名单-----------------------------------

export async function getBlacklistList (params) {
  return request(`/api/blacklist/list`, { params });
}

export async function addBlacklistItem (params) {
  return request(`/api/blacklist/add`, { method: 'post', data: params });
}

export async function getBlacklistItem (params) {
  return request(`/api/blacklist/item`, { params });
}

export async function updBlacklistItem (params) {
  return request(`/api/blacklist/upd`, { method: 'post', data: params });
}

export async function delBlacklistItem (params) {
  return request(`/api/blacklist/del`, { params });
}

//-----------------------------------------------------------------------