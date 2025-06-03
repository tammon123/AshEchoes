//H5
// #ifdef WEB
export const BASE_PRE = "/api/"
export const BASE_URL = location.origin
// #endif
//小程序 测试
// export const BASE_PRE = "/"
// export const BASE_URL = "http://localhost:8765"
// 生产环境
// #ifdef MP-WEIXIN
export const BASE_PRE = "/api/"
export const BASE_URL = "https://bjhl.qianqiu.info"
// #endif
export const LOGIN_PHONE = "ae-p" // 手机号
export const LOGIN_ID = "ae-id" // id
export const CACHE_EXPIRE_TIME = 60 * 60 * 24 * 3650 // 默认保存时间 90天
export const PLATFORM = "p_p"
export const WECHAT_MINIPROGRAM = "wp"
export const H5 = "h5"