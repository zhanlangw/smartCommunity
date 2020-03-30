import request from '@/utils/request';

export async function  getBarData(params) {
  return  request('/api/stats/work', { params })
}

export async function  getListData(params) {
  return  request('/api/stats/data/broadcast', { params })
}

export async function getSelectList(params) {
  return request('/api/unit/tree', { params })
}

export async  function getLineData(params) {
  return  request('/api/stats/trend', { params })
}

export  async function getRadarData(params) {
  return request('/api/stats/ability', { params })
}

export async function getMapData(params) {
  return request('/api/stats/map', { params })
}

export async function getOnlineData(params) {
  return request('/api/stats/online/rate', { params })
}

