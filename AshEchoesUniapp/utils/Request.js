import {
	BASE_URL,
	BASE_PRE
} from "./Constant.js"

function req(url, data, suc, fail, {
	baseUrl = BASE_URL + BASE_PRE,
	method = "GET"
} = {}) {
	if (!data) {
		data = {}
	}
	uni.request({
		url: baseUrl + url,
		data,
		method,
		success: (res) => {
			if (res.data.code != 0) {
				if (fail) {
					let msg =  res.data.msg || "请求错误，请联系开发人员"
					fail(msg)
				}
				return
			}
			if (suc) {
				suc(res.data.data || res.data)
			}
		},
		fail: (fa) => {
			if (fail) {
				fail(fa)
			}
		}
	})
}

export {
	req
}