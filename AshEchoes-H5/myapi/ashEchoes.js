import {
	BASE_URL
} from "../utils/Constant"
import {
	User
} from "../utils/User"
import {
	getPlatform
} from "@/utils/Platform.js"
let user = new User
// #ifdef WEB
export function versionApi(suc) {
	uni.req("h5/v", {}, suc, () => {})
}

export function getAppVersionApi(suc) {
	uni.req("app/v", {}, suc, () => {})
}
// #endif
export function getVInfoApi(suc) {
	// #ifdef MP-WEIXIN
	uni.req("v.txt?v=" + Date.parse(new Date()) / 1000, {}, suc, () => {}, {
		baseUrl: BASE_URL + "/"
	})
	// #endif
	// #ifdef WEB
	uni.req("h5/info", {}, suc, () => {})
	// #endif
}
export function updateLogApi(suc) {
	uni.req("update/log", {}, suc, () => {})
}
export function getFriendUrlInfoApi(suc) {
	uni.req("friend/list", {}, suc, () => {})
}

//故事
export function storyListApi(data, suc, fail) {
	uni.req("story/list", data, suc, fail)
}
export function storyByIdApi(id, data, suc, fail) {
	uni.req("story/id/" + id, data, suc, fail)
}

export function getMemorysApi(data, suc, fail) {
	uni.req("memory/all", {}, suc, fail)
}
export function getMemorysSimpleApi(data, suc, fail) {
	uni.req("memory/simple", {}, suc, fail)
}
export function getcharsApi(data, suc, fail) {
	uni.req("character/" + data, {}, suc, fail)
}
export function computeDamageApi(param, suc, fail) {
	uni.req("card/" + param, {}, suc, fail)
}
export function thanksApi(suc, fail) {
	uni.req("thanks", {}, suc, fail)
}
export function userPostApi(url, data, suc, fail) {
	uni.req("user/post/" + url, data, suc, fail, {
		method: "POST"
	})
}
export function userModifyApi(data, suc, fail) {
	uni.req("user/modify", data, suc, fail, {
		method: "POST"
	})
}
export function userPswEmailSendApi(data, suc, fail) {
	uni.req("user/psw/sendMail", data, suc, fail, {
		method: "POST"
	})
}
export function userFindPswApi(data, suc, fail) {
	uni.req("user/psw/find", data, suc, fail, {
		method: "POST"
	})
}



export function suggestPostApi(data, suc, fail) {
	uni.req("suggest/post", data, suc, fail, {
		method: "POST"
	})
}
export function suggestPageApi(data, suc, fail) {
	uni.req("suggest/page", data, suc, fail, {
		method: "POST"
	})
}


export function getAllSmallSkillsApi(suc, fail) {
	uni.req("skill/small/all", {}, suc, fail)
}


// 卡池分享或者展示相关
export function getPageCards(query, suc, fail) {
	uni.req("cards/page/" + query, {}, suc, fail)
}
export function deleteCards(query, suc, fail) {
	uni.req("cards/delete/" + query, {}, suc, fail)
}
export function getShareCardsInfo(query, suc, fail) {
	uni.req("cards/share/" + query + "?uid=" + user.getId(), {}, suc, fail)
}
export function likeCardApi(query, suc, fail) {
	uni.req("cards/like/" + query + "/" + user.getId(), {}, suc, fail)
}
export function getShareComputeCardsInfo(query, suc, fail) {
	uni.req("cards/share/compute/" + query, {}, suc, fail)
}
export function postCardsData(data, suc, fail) {
	uni.req("cards/post", data, suc, fail, {
		method: "POST"
	})
}
export function updateCardsData(data, suc, fail) {
	uni.req("cards/update", data, suc, fail, {
		method: "POST"
	})
}
//抽卡计算模板
export function addCardsTemplateApi(data, suc, fail) {
	uni.req("cards/compute/template", data, suc, fail, {
		method: "POST"
	})
}
export function getAllCardsTemplateApi(suc, fail) {
	uni.req("cards/compute/template/all/" + user.getId(), {}, suc, fail)
}
export function getCardsTemplateApi(id, suc, fail) {
	uni.req("cards/compute/template/id/" + id, {}, suc, fail)
}
export function deleteCardsTemplateApi(param, suc, fail) {
	uni.req("cards/compute/template/delete/" + user.getId() + "/" + param.type + "/" + param.title, {}, suc, fail)
}
export function useCardsTemplateApi(param, suc, fail) {
	uni.req("cards/compute/template/use/" + user.getId() + "/" + param.type + "/" + param.title, {}, suc, fail)
}

// 监督刻印数据修改以及列表
export function modifyMineMemoryDirectorCountApi(data, suc, fail) {
	uni.req("memory/director", data, suc, fail, {
		method: "POST"
	})
}
//抽卡数据相关
export function getMineMemoryDirectorCountApi(uid, suc, fail) {
	uni.req("memory/director/" + uid, {}, suc, fail)
}

//抽卡数据相关
export function getPoolAllNameApi(suc, fail) {
	uni.req("pool/name/all/" + user.getId(), {}, suc, fail)
}
export function getPoolAllApi(suc, fail) {
	uni.req("pool/all", {}, suc, fail)
}
export function getMemoryPoolGroupByUid(uid, suc, fail) {
	uni.req("/pool/memory/groupCount/" + uid, {}, suc, fail)
}
export function getPoolDataUserInfoApi(suc, fail) {
	uni.req("pool/userinfo/" + user.getId(), {}, suc, fail)
}
export function getDetailRankApi(suc, fail) {
	uni.req("pool/data/allRank/" + user.getId(), {}, suc, fail)
}
export function getNewPoolDataApi(data, suc, fail) {
	uni.req("pool/data/new/" + user.getId() + data, {}, suc, fail)
}
export function getNewPoolGroupByNameDataApi(data, suc, fail) {
	uni.req("pool/data/group/" + user.getId() + data, {}, suc, fail)
}
export function deletePoolDataApi(suc, fail) {
	uni.req("pool/data/delete/" + user.getId(), {}, suc, fail)
}
export function postPoolDataApi(data, token, suc, fail) {
	uni.req("pool/token/" + user.getId() + "?token=" + token, {}, suc, fail)
}
//排行榜相关接口
//出货率tRate  up出货率uTr   平均抽数tAvg   up平均抽数uAvg
export function getPagePoolDataRankApi(data, suc, fail) {
	uni.req("poolrank/" + data.type + "/" + data.page + "/" + data.size + "/" + data.sort + "/" + data.count + "/" +
		data.beh + "/" + user.getId(), {}, suc, fail)
}
export function agreeRankApi(suc, fail) {
	uni.req("pool/rank/agree/" + user.getId(), {}, suc, fail)
}
export function cancelRankApi(suc, fail) {
	uni.req("pool/rank/cancel/" + user.getId(), {}, suc, fail)
}


//wiki相关

export function charWikiDetailApi(param, suc, fail) {
	uni.req("wiki/char/" + param, {}, suc, fail)
}
export function memoryWikiDetailApi(param, suc, fail) {
	uni.req("wiki/memory/" + param, {}, suc, fail)
}

//已经废弃
export function pApi(data, suc, fail) {}