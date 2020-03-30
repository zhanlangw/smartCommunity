import request from '@/utils/request';


//--------------------------------大类-----------------------------------

export async function getBigCategoryList (params) {
  return request(`/api/bigcategory/list`, { params });
}

export async function addBigCategoryItem (params) {
  return request(`/api/bigcategory/add`, { method: 'post', data: params });
}

export async function getBigCategoryItem (params) {
  return request(`/api/bigcategory/item`, { params });
}

export async function updBigCategoryItem (params) {
  return request(`/api/bigcategory/upd`, { method: 'post', data: params });
}

export async function delBigCategoryItem (params) {
  return request(`/api/bigcategory/del`, { params });
}

//-----------------------------------------------------------------------

//--------------------------------小类-----------------------------------

export async function getSmallCategoryList (params) {
  return request(`/api/smallcategory/list`, { params });
}

export async function addSmallCategoryItem (params) {
  return request(`/api/smallcategory/add`, { method: 'post', data: params });
}

export async function getSmallCategoryItem (params) {
  return request(`/api/smallcategory/item`, { params });
}

export async function updSmallCategoryItem (params) {
  return request(`/api/smallcategory/upd`, { method: 'post', data: params });
}

export async function delSmallCategoryItem (params) {
  return request(`/api/smallcategory/del`, { params });
}

//-----------------------------------------------------------------------