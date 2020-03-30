import request from "@/utils/request";

export async function getGlobalSearchData(params) {
  return request('/api/work/history/list', {params})
}

export async function getUserItem(params) {
  return request('/api/user/item', {params})
}
