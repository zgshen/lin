import request from '@/utils/request'
import qs from 'qs'

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~字典数据接口~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// 列表数据
export function dictionaryList(params) {
  return request({
    url: '/common/dict/list',
    method: 'get',
    params
  })
}

// 下拉数据
export function dictType(params) {
  return request({
    url: '/common/dict/type',
    method: 'get',
    params
  })
}

// 添加列表
export function dictSave(params) {
  return request({
    url: '/common/dict/save',
    method: 'post',
    params
  })
}

// 批量删除
export function batchRemove(params) {
  return request({
    transformRequest: [function (data) {
      data = qs.stringify(data)
      return data.replace(/\%5B(.+?)\%5D/g, '%5B%5D')
    }],
    url: '/common/dict/batchRemove',
    method: 'post',
    data:params
  })
}

// 单个删除
export function itemRemove(params) {
  return request({
    url: '/common/dict/remove',
    method: 'post',
    params
  })
}

// 编辑
export function itemUpdate(params) {
  return request({
    url: '/common/dict/update',
    method: 'post',
    params
  })
}


// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~计划任务接口~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// 列表数据
export function jobList(params) {
  return request({
    url: '/common/job/list',
    method: 'get',
    params
  })
}

// 保存添加数据
export function jobSave(params) {
  return request({
    url: '/common/job/save',
    method: 'post',
    params
  })
}

// 批量删除
export function jobRemove(params) {
  return request({
    transformRequest: [function (data) {
      data = qs.stringify(data)
      return data.replace(/\%5B(.+?)\%5D/g, '%5B%5D')
    }],
    url: '/common/job/batchRemove',
    method: 'post',
    data:params
  })
}

// 任务开启与关闭
export function jobChangeJobStatus(params) {
  return request({
    url: '/common/job/changeJobStatus',
    method: 'post',
    params
  })
}

// 编辑
export function jobUpdate(params) {
  return request({
    url: '/common/job/update',
    method: 'post',
    params
  })
}

//单个删除
export function jobItemRemove(params) {
  return request({
    url: '/common/job/remove',
    method: 'post',
    params
  })
}

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~在线用户接口~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//在线用户列表
export function onlinelist(params) {
  return request({
    url: '/sys/online/list',
    method: 'get',
    params
  })
}

//在线用户删除
export function forceLogout(sessionId) {
  return request({
    url: '/sys/online/forceLogout/' + sessionId,
    method: 'post',
  })
}


// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~系统日志接口~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// 系统日志列表
export function logList(params) {
  return request({
    url: '/common/log/list',
    method: 'get',
    params
  })
}

// 批量删除
export function logBatchRemove(params) {
  return request({
    transformRequest: [function (data) {
      data = qs.stringify(data)
      return data.replace(/\%5B(.+?)\%5D/g, '%5B%5D')
    }],
    url: '/common/log/batchRemove',
    method: 'post',
    data:params
  })
}

// 单个删除
export function logRemove(params) {
  return request({
    url: '/common/log/remove',
    method: 'post',
    params
  })
}

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~文件管理接口~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


// 列表数据
export function imgList(params) {
  return request({
    url: '/common/sysFile/list',
    method: 'get',
    params
  })
}

// 文件删除
export function imgRemove(params) {
  return request({
    url: '/common/sysFile/remove',
    method: 'post',
    params
  })
}

// 上传图片
export function imgUpload(params) {
  let formData = new FormData()
  formData.append(' file', params.file)
  return request({
    url: '/common/sysFile/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'x-www-form-urlencoded',
    },
  })
}
