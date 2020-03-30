import request from "@/utils/request";

export async function getSelectList(params) {
  return request('/api/unit/tree', { params })
}
