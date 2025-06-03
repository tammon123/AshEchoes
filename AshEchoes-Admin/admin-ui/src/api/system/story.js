import request from '@/utils/request'

// 查询故事线列表
export function listStory(query) {
  return request({
    url: '/system/story/list',
    method: 'get',
    params: query
  })
}

// 查询故事线详细
export function getStory(id) {
  return request({
    url: '/system/story/' + id,
    method: 'get'
  })
}

// 新增故事线
export function addStory(data) {
  return request({
    url: '/system/story',
    method: 'post',
    data: data
  })
}

// 修改故事线
export function updateStory(data) {
  return request({
    url: '/system/story',
    method: 'put',
    data: data
  })
}

// 删除故事线
export function delStory(id) {
  return request({
    url: '/system/story/' + id,
    method: 'delete'
  })
}
