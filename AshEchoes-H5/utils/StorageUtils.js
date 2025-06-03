import {
	CACHE_EXPIRE_TIME
} from "./Constant.js"

class StorageUtils {
	constructor() {}

	set(key, value, time) {
		if (!time) {
			time = CACHE_EXPIRE_TIME
		}
		let data = {
			value: value,
			expire: Math.round(new Date() / 1000) + time
		}
		uni.setStorageSync(key, JSON.stringify(data))
	}

	get(key) {
		let data = uni.getStorageSync(key)
		if(!data) {
			return undefined
		}
		data = JSON.parse(data)
		if (data.expire && data.expire >= Math.round(new Date() / 1000)) {
			return data.value
		} else {
			this.remove(key)
			return undefined
		}
	}

	remove(key) {
		uni.removeStorageSync(key)
	}

}

export {
	StorageUtils
}