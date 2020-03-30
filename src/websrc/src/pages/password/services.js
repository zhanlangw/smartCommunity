import request from "@/utils/request";

export  async function updPassword(params) {
    return request('/api/user/upd/password', { method: 'post', data: params })  //异步post请求
}
