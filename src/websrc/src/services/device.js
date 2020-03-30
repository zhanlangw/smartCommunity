import request from '@/utils/request';

//--------------------------------摄像机-----------------------------------

export async function getDeviceList (params) {
  return request(`/api/device/list`, { params });
}

export async function addDeviceItem (params) {
  return request(`/api/device/add`, { method: 'post', data: params });
}

export async function getDeviceItem (params) {
  return request(`/api/device/item`, { params });
}

export async function updDeviceItem (params) {
  return request(`/api/device/upd`, { method: 'post', data: params });
}

export async function delDeviceItem (params) {
  return request(`/api/device/del`, { params });
}

//-----------------------------------------------------------------------
