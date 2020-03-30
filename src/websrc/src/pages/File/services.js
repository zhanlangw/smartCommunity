import  request from '@/utils/request';

//---------------------------------------案卷列表_待处置-----------------------------------------------------------------
export async function getFileHandleList(params) {
  return request(`/api/work/todo/list`, { params })
}

//----------------------------------------------------------------------------------------------------------------------

//---------------------------------------案卷列表_待核查-----------------------------------------------------------------
export async function getFileInspectList(params) {
  return request(`/api/work/review/list`, { params })
}

//----------------------------------------------------------------------------------------------------------------------

//---------------------------------------案卷列表_申请延时---------------------------------------------------------------
export async function getFileDelayedList(params) {
  return request(`/api/work/delay/list`, { params })
}

//----------------------------------------------------------------------------------------------------------------------

//---------------------------------------案卷列表_退件-------------------------------------------------------------------
export async function getFileBounceList(params) {
  return request(`/api/work/return/list`, { params })
}
//----------------------------------------------------------------------------------------------------------------------

//---------------------------------------案卷列表_已处置-----------------------------------------------------------------
export async function getFileDisposalList(params) {
  return request(`/api/work/finish/list`, { params })
}

//----------------------------------------------------------------------------------------------------------------------

//---------------------------------------案卷列表_事件-------------------------------------------------------------------
export async function getFileEventList(params) {
  return request(`/api/work/accept/list`, { params })
}
//----------------------------------------------------------------------------------------------------------------------

//---------------------------------------案卷列表_历史-------------------------------------------------------------------
export async function getFileHistoryList(params) {
  return request(`/api/work/history/list`, { params })
}
//----------------------------------------------------------------------------------------------------------------------


//--------------------------------------------------同一删除接口---------------------------------------------------------
//删除已办
export async function delHasFiles(params) {
  return request(`/api/work/del`, { params })
}
//获取日志列表
export async function getFileLogList(params) {
  return request(`/api/log/list`, { params })
}
//新增案卷
export async function addFile(params) {
  return request(`/api/work/add`, { method: 'post', data: params })
}
//删除代办
export async function delForFiles(params) {
  return request(`/api/work/todo/del`, { params })
}
//获取路径
export async function getWorkPath(params) {
  return request(`/api/work/path`, { params })
}
//案卷提交
export async function fileSubmit(params) {
  return request(`/api/work/submit`, { method: 'post', data: params})
}

//结束
export async function fileEnd(params) {
  return request(`/api/work/end`, { params })
}
//受理
export async function fileAccept(params) {
  return request(`/api/work/accept`, { params })
}
//撤回
export async function fileWithdraw(params) {
  return request(`/api/work/withdraw`, { params })
}
// 删除历史案卷
export async function delFileHistory(params) {
  return request(`/api/work/finish/del`, { params })
}
// 获取详情
export async function getFileItemData(params) {
  return request(`/api/work/item`, { params })
}
// 案卷统计
export async function getFileStatData(params) {
  return request('/api/stats/histogram', {params})
}
export async function getSelectList(params) {
  return request('/api/unit/tree', { params })
}
//----------------------------------------------------------------------------------------------------------------------


