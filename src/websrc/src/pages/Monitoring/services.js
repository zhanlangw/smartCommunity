import request from "@/utils/request";

export  async function videoPlay(params) {
  return request('/api/video/play', { params })
}

export  async function videoEnd(params) {
  return request('/api/video/play_close', { params })
}