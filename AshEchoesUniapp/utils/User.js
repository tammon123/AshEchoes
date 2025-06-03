import {
    LOGIN_PHONE,
    CACHE_EXPIRE_TIME,
    LOGIN_ID
} from "./Constant.js";
const AVATAR = "avatar"
const NICKNAME = "nickName"
const ACCOUNT = "ae-acc"
class User {

    constructor() {
    }

    setToken(value) {
        uni.su.set(LOGIN_TOKEN, value, CACHE_EXPIRE_TIME)
    }

    getToken() {
        return uni.su.get(LOGIN_TOKEN) || ''
    }

    setPhone(value) {
        uni.su.set(LOGIN_PHONE, value, CACHE_EXPIRE_TIME)
    }

    getPhone() {
        return uni.su.get(LOGIN_PHONE) || ''
    }

    setId(value) {
        uni.su.set(LOGIN_ID, value, CACHE_EXPIRE_TIME)
    }

    getId() {
        return uni.su.get(LOGIN_ID) || ''
    }

    getAccount() {
        return uni.su.get(ACCOUNT) || ''
    }

    setAccount(value) {
        uni.su.set(ACCOUNT, value, CACHE_EXPIRE_TIME)
    }

    setNickname(value) {
        uni.su.set(NICKNAME, value, CACHE_EXPIRE_TIME)
    }
	
	getAvatar() {
		return uni.su.get(AVATAR) || ''
	}
	
	getNickname() {
		return uni.su.get(NICKNAME) || ''
	}
	
	loginOut() {
		uni.su.remove(LOGIN_ID)
		uni.su.remove(LOGIN_PHONE)
		uni.su.remove(AVATAR)
		uni.su.remove(NICKNAME)
		uni.su.remove(ACCOUNT)
	}


}

export {
    User
}
