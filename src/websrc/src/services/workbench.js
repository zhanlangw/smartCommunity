import request from '@/utils/request';

export async function getList() {
  return request('/api/notices');
}

export async function getListAll() {
  return request('/api/noticesAll');
}

export async function getTestList(params) {
  return request('/api/v1/dataSource', { method: 'post', data: params });
}
