import request from '@/utils/request'

// 查询故事关键字列表
export function listKeyword(query) {
  return request({
    url: '/system/keyword/list',
    method: 'get',
    params: query
  })
}

// 查询故事关键字详细
export function getKeyword(id) {
  return request({
    url: '/system/keyword/' + id,
    method: 'get'
  })
}

// 新增故事关键字
export function addKeyword(data) {
  return request({
    url: '/system/keyword',
    method: 'post',
    data: data
  })
}

// 修改故事关键字
export function updateKeyword(data) {
  return request({
    url: '/system/keyword',
    method: 'put',
    data: data
  })
}

// 删除故事关键字
export function delKeyword(id) {
  return request({
    url: '/system/keyword/' + id,
    method: 'delete'
  })
}
