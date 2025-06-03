import {
	StorageUtils
} from "./StorageUtils.js";
uni.su = new StorageUtils()

import {
	User
} from "./User.js";
const user = new User()
uni.guser = user
// #ifdef WEB
window.guser = user
// #endif
import {
	req
} from "./Request.js"
// 基础工具
uni.req = req
import {
	isH5,
	isWechat
} from "./Platform.js"
uni.isH5 = isH5()
uni.isWechat = isWechat()