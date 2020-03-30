import request from '@/utils/request';

export async function getConfigItem(params) {
 return request('/api/basis/item', { params })
}

export async  function updConfigItem(params) {
  return request('/api/basis/upd', { method: 'post', data: params })
}