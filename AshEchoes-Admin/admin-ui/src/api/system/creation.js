import request from '@/utils/request'

// 查询故事二创列表
export function listCreation(query) {
  return request({
    url: '/system/creation/list',
    method: 'get',
    params: query
  })
}

// 查询故事二创详细
export function getCreation(id) {
  return request({
    url: '/system/creation/' + id,
    method: 'get'
  })
}

// 新增故事二创
export function addCreation(data) {
  return request({
    url: '/system/creation',
    method: 'post',
    data: data
  })
}

// 修改故事二创
export function updateCreation(data) {
  return request({
    url: '/system/creation',
    method: 'put',
    data: data
  })
}

// 删除故事二创
export function delCreation(id) {
  return request({
    url: '/system/creation/' + id,
    method: 'delete'
  })
}
