import request from '@/utils/request';


//--------------------------------站点-----------------------------------

export async function getStationList (params) {
  return request(`/api/station/list`, { params });
}

export async function addStationItem (params) {
  return request(`/api/station/add`, { method: 'post', data: params });
}

export async function getStationItem (params) {
  return request(`/api/station/item`, { params });
}

export async function updStationItem (params) {
  return request(`/api/station/upd`, { method: 'post', data: params });
}

export async function delStationItem (params) {
  return request(`/api/station/del`, { params });
}

//-----------------------------------------------------------------------