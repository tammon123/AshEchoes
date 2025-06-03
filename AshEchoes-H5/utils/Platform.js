import {
	PLATFORM,
	WECHAT_MINIPROGRAM,
	H5
} from "@/utils/Constant.js"
import {
	StorageUtils
} from "./StorageUtils.js";
const su = new StorageUtils()
export function isWechat() {
	return WECHAT_MINIPROGRAM == su.get(PLATFORM)
}

export function isH5() {
	return H5 == su.get(PLATFORM)
}

export function isWxH5() {
	if (navigator) {
		return navigator.userAgent.indexOf("MicroMessenger") != -1
	}
	return false
}

export function getPlatform() {
	return su.get(PLATFORM)
}

function initPlatform() {
	// #ifdef WEB
	su.set(PLATFORM, H5)
	// #endif
	// #ifdef MP-WEIXIN
	su.set(PLATFORM, WECHAT_MINIPROGRAM)
	// #endif
}
initPlatform()