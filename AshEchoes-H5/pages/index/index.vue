<template>
	<view class="index-body" id='parant-body'>
		<view @click='toAppImportPage' v-if="isApp" class="app-import-icon" id='importBtn'>
			<img class='import-icon' mode='heightFix' src="https://r.qianqiu.info/app/icons/import1.png" alt="">
		</view>
		<view class='top-navbar-body'>
			<view class='head_title-new'>
				<view class="top">
					<img class='logo' mode="heightFix" src='https://r.qianqiu.info/app/logo.png' alt="" />
					<view class="login-box" @click="toLogin">
						<span class='login' v-if="uid">
							{{nickname||'已登录'}}
						</span>
						<span class='no-login' v-else>
							未登录
						</span>
					</view>
				</view>
				<view class="bottom">
					欢迎您，亲爱的小监督~
				</view>
			</view>
		</view>
		<uv-toast ref='toast'></uv-toast>
		<view class="index-content">
			<view class='head'>
				<view class="head-card" @click="toChouka">
					<view class="img-box">
						<img mode="heightFix" src="https://r.qianqiu.info/app/index/21.png" alt="" class="card-img">
					</view>
					<view class="hci-title">抽卡分析</view>
				</view>
				<view class="head-card" @click="toMore">
					<view class="img-box">
						<img mode="heightFix" src="https://r.qianqiu.info/app/index/2.png" alt="" class="card-img">
					</view>
					<view class="hci-title">配队助手</view>
				</view>
				<view class="head-card" @click="toRank">
					<view class="img-box">
						<img mode="heightFix" src="https://r.qianqiu.info/app/index/12.png" alt="" class="card-img">
					</view>
					<view class="hci-title">伤害计算</view>
				</view>
				<view class="head-card" @click='toArticle'>
					<view class="img-box">
						<img mode="heightFix" src="https://r.qianqiu.info/app/index/4.png" alt="" class="card-img">
					</view>
					<view class="hci-title">萌新攻略</view>
				</view>
				<view class="head-card" @click="toStory">
					<view class="img-box">
						<img mode="heightFix" src="https://r.qianqiu.info/app/index/22.png" alt="" class="card-img">
					</view>
					<view class="hci-title">剧情线</view>
				</view>
				<view class="head-card" @click="toMd">
					<view class="img-box">
						<img mode="heightFix" src="https://r.qianqiu.info/app/index/11.png" alt="" class="card-img">
					</view>
					<view class="hci-title">监督刻印</view>
				</view>
				<view class="head-card" @click='toWikiChar'>
					<view class="img-box">
						<img mode="heightFix" src="https://r.qianqiu.info/app/index/7.png" alt="" class="card-img">
					</view>
					<view class="hci-title">同调者</view>
				</view>
				<view class="head-card" @click='toWikiMemory'>
					<view class="img-box">
						<img mode="heightFix" src="https://r.qianqiu.info/app/index/8.png" alt="" class="card-img">
					</view>
					<view class="hci-title">记忆烙痕</view>
				</view>
				<view class="head-card" @click='toNotice'>
					<view class="img-box">
						<img mode="heightFix" src="https://r.qianqiu.info/app/index/9.png" alt="" class="card-img">
					</view>
					<view class="hci-title">更新日志</view>
				</view>
				<view class="head-card" @click='toSuggest'>
					<view class="img-box">
						<img mode="heightFix" src="https://r.qianqiu.info/app/index/6.png" alt="" class="card-img">
					</view>
					<view class="hci-title">意见反馈</view>
				</view>
				<view class="head-card" @click='toXiexie'>
					<view class="img-box">
						<img mode="heightFix" src="https://r.qianqiu.info/app/index/5.png" alt="" class="card-img">
					</view>
					<view class="hci-title">致谢</view>
				</view>
				<view class="head-card" @click='toWait'>
					<view class="img-box">
						<img mode="heightFix" src="https://r.qianqiu.info/app/index/17.png" alt="" class="card-img">
					</view>
					<view class="hci-title">未完待续</view>
				</view>
			</view>
			<view class='index-version' @click="hideClear">
				<span class="index-v-title">
					资讯区
				</span>
				<view class="version-info">
					<p class='vi-line'>
						<span style="color: red;" class='info-span'>{{noticeInfo}}</span>
					</p>
					<template v-if="newVersionInfo">
						<p class='vi-line'>
							<span style="color: #3467FF;" class='info-span'>{{newVersionInfo}}</span>
						</p>
					</template>
					<p class='vi-line'>
						<span class='info-span'>{{vInfo||'暂无更新公告'}}</span>
					</p>
				</view>
			</view>
			<!-- #ifdef WEB -->
			<view class='index-split'></view>
			<view class='index-friend'>
				<span class="index-v-title">
					相关链接
				</span>
				<template v-for="(item, index) in friendUrl">
					<view class="friend-info">
						<view class="left" @click="toFriendUrl(item, index)">
							<span class='li-split'>·</span>
							<span class='friend-span'>「{{item.n}}」</span>
						</view>
						<view class="right">
							<span>{{item.l}}</span>
						</view>
					</view>
				</template>
			</view>
			<!-- #endif -->
			<!-- #ifdef WEB-->
			<view class="copyright">
				<p>Copyright © 2024 AES ALL RIGHTS RESERVED.</p>
				<p>京ICP备2024086401号-1</p>
			</view>
			<!-- #endif -->
		</view>
		<AeIframe :url='wikiUrl' @exit='wikiUrl=""'></AeIframe>
		<uv-modal :zIndex='99999' ref="modal" :title="modalData.title" :showCancelButton='true' @confirm="sureTo"
			confirmText='前往' cancelText='取消'>
			<view class="i-modal-content">
				<p class='imc-item'>您即将前往：<span class='sc-aim'>{{toForItem.n}}</span></p>
			</view>
		</uv-modal>
		<uv-modal :zIndex='99999' ref="loginModal" :title="loginModalData.title" :showCancelButton='true'
			@confirm="sureC" confirmText='好的' cancelText='不' :content='loginModalData.content'></uv-modal>
		<uv-popup ref="loginPopup" mode="top">
			<view class="login-index-box">
				<view class="input-box" v-if="!findAccountPsw">
					<template v-if="!uid">
						<p class='input-title'>登录账号</p>
						<uv-input :customStyle="{'borderColor': 'black !important'}" placeholder="请在这里输入账号"
							shape="square" color='black' v-model="data.account"></uv-input>
						<p v-if="!loginPage" class='input-title'>昵称</p>
						<uv-input v-if="!loginPage" :customStyle="{'borderColor': 'black !important'}"
							placeholder="请在这里输入账号" shape="square" color='black' v-model="data.nickname"></uv-input>
						<p class='input-title'>登录密码</p>
						<uv-input :customStyle="{'borderColor': 'black !important'}" placeholder="请在这里输入密码"
							shape="square" color='black' :type="loginPage?'password':'text'"
							v-model="data.password"></uv-input>
						<p v-if="!loginPage" class='input-title'>重复密码</p>
						<uv-input v-if="!loginPage" :customStyle="{'borderColor': 'black !important'}"
							placeholder="请在这里重复输入密码" shape="square" color='black' v-model="data.rpwd"></uv-input>
						<p v-if="!loginPage" class='input-title'>找回邮箱<span style='color:red'>(账号找回时使用)</span></p>
						<uv-input v-if="!loginPage" :customStyle="{'borderColor': 'black !important'}"
							placeholder="请在这里输入找回邮箱" shape="square" color='black' v-model="data.email"></uv-input>
					</template>
					<template v-else>
						<p class='input-title'>显示昵称</p>
						<uv-input :customStyle="{'borderColor': 'black !important'}" placeholder="请在这里输入小助手的显示昵称"
							shape="square" color='black' v-model="data.nickname"></uv-input>
					</template>
				</view>
				<view class="input-box" v-if="findAccountPsw">
					<p class='input-title'>输入邮箱</p>
					<uv-input :customStyle="{'borderColor': 'black !important'}" placeholder="请在这里输入找回邮箱" shape="square"
						color='black' v-model="data.email">
						<template v-slot:suffix>
							<uv-code ref="uCode" @end='uCodeEnd' @change='uCodeChange' seconds="60"></uv-code>
							<uv-button @click="getCode" :text="codeText" type="success" size="mini"></uv-button>
						</template>
					</uv-input>
					<p class='input-title'>输入验证码</p>
					<uv-input :customStyle="{'borderColor': 'black !important'}" placeholder="请输入验证码" shape="square"
						color='black' type="text" v-model="data.code"></uv-input>
					<p class='input-title'>设置新密码</p>
					<uv-input :customStyle="{'borderColor': 'black !important'}" placeholder="请在这里输入密码" shape="square"
						color='black' type="text" v-model="data.password"></uv-input>
					<p class='input-title'>在输入一次密码</p>
					<uv-input :customStyle="{'borderColor': 'black !important'}" placeholder="请在这里重复输入密码" shape="square"
						color='black' v-model="data.rpwd"></uv-input>

				</view>
				<view class="submit-btns">
					<template v-if="uid &&!findAccountPsw">
						<view class="mine-submit" @click='logout'>退出登录</view>
						<view class="mine-submit" @click='submitModifyData'>确认修改</view>
					</template>
					<template v-if="!uid && !findAccountPsw">
						<view v-if="loginPage" class="mine-submit" @click='changeRegister'>去注册</view>
						<view v-if="!loginPage" class="mine-submit" @click='changeLogin'>去登录</view>
						<view v-if="loginPage" class="mine-submit find-submit" @click='findPsw'>找回密码</view>
						<view class="mine-submit" @click='submitLoginData'>{{loginPage?'登录':'注册'}}</view>
					</template>
					<template v-if="findAccountPsw">
						<view class="mine-submit" @click='findAccountPsw=false'>返回</view>
						<view class="mine-submit" @click='sureFindPsw'>提交修改</view>
					</template>
				</view>
			</view>
		</uv-popup>
	</view>
</template>

<script setup>
	import {
		onMounted,
		ref,
		watch
	} from 'vue';

	import {
		isH5
	} from '@/utils/Platform';

	let hideCCount = 0

	const friendUrl = ref([])
	const isApp = ref(false)
	const toForItem = ref({})
	const modalData = ref({
		title: "温馨提示"
	})

	function sureTo() {
		// #ifdef WEB
		window.location.href = toForItem.value.v
		// #endif
	}

	function toFriendUrl(item, index) {
		if (index <= 1) {
			wikiUrl.value = item.v
		} else {
			toForItem.value = item
			modal.value.open()
		}

	}

	function hideClear() {
		hideCCount += 1
		if (hideCCount >= 10) {
			hideCCount = 0;
			uni.guser.loginOut()
			// #ifdef WEB
			if (typeof(recorder) == 'object') {
				recorder.logout("logout")
			}
			// #endif
			window.location.reload()
		}
	}
	import {
		getVInfoApi,
		getFriendUrlInfoApi,
		// #ifdef WEB
		getAppVersionApi,
		// #endif
		pApi
	} from "@/myapi/ashEchoes.js"
	import {
		onLoad
	} from "@dcloudio/uni-app"
	onLoad((param) => {
		// #ifdef WEB
		if (typeof(recorder) == 'object') {
			if (param.aesVersion != 10 || !param.aesVersion) {
				newVersionInfo.value =
					"当前小助手APP非最新版本，建议前往微信公众号《白荆小助手》或QQ群865686593获取最新版本~"
			}
		}
		// #endif
	})
	const newVersionInfo = ref("")
	const card = ref({
		charNames: '',
		memoryNames: ''
	})
	const h5 = isH5()
	const vInfo = ref("暂无更新公告")
	const noticeInfo = ref("")

	onMounted(() => {
		let param = 1 + "/" + 1 + "?status=0&keyWord="
		getVInfoApi((r) => {
			vInfo.value = r
			if ("暂无更新日志" != r) {
				noticeInfo.value = "抽卡记录导入教程可前往微信公众号《白荆小助手》查看，导入抽卡记录前无需删除数据、无需删除数据、无需删除数据。可以直接导入，支持增量更新。"
			}
		})
		getFriendUrlInfoApi((r) => {
			friendUrl.value = r
		})
	})

	function toArticle() {
		uni.navigateTo({
			url: "/pages/guide/guide"
		})
	}

	function toDesc() {
		uni.navigateTo({
			url: "/pages/peidui/share/share?cardId=" + card.value.cardId
		})
	}
	import AeIframe from "@/components/AeIframe/AeIframe.vue"
	const wikiUrl = ref("")

	function toBjhlWiki() {
		// #ifdef WEB
		wikiUrl.value = "https://wiki.biligame.com/bjhl"
		// #endif
	}

	function toMore() {
		uni.navigateTo({
			url: "/pages/peidui/peidui"
		})
		pApi({
			page: 'to_more'
		})
	}

	function toMd() {
		uni.navigateTo({
			url: "/pages/peidui/memory-director/memory-director"
		})
	}

	function toStory() {
		uni.navigateTo({
			url: "/pages/story/story"
		})
	}

	function toMengxing() {
		uni.navigateTo({
			url: "/pages/guide/guide"
		})
		pApi({
			page: 'to_mengxing'
		})
	}

	function toXiexie() {
		pApi({
			page: 'to_xiexie'
		})
		uni.navigateTo({
			url: "/pages/thanks/thanks"
		})
	}

	function toSuggest() {
		uni.navigateTo({
			url: "/pages/suggest/suggest"
		})
	}

	function toChouka() {

		if (!uni.guser.getId()) {
			toast.value.show({
				type: 'default',
				message: "请先登录~",
				overlay: false
			})
			return
		}
		uni.navigateTo({
			url: "/pages/chouka/chouka"
		})
	}

	function toNotice() {
		uni.navigateTo({
			url: "/pages/notice/notice"
		})
	}

	function toWikiMemory() {
		uni.navigateTo({
			url: "/pages/wiki/memory-wiki/memory-wiki"
		})
	}

	function toWait() {
		toast.value.show({
			message: "敬请期待下次更新~",
			duration: 2500,
			overlay: false
		})
	}

	function toWikiChar() {
		uni.navigateTo({
			url: "/pages/wiki/wiki-char/wiki-char"
		})
	}

	function toRank() {
		pApi({
			page: 'to_ranks'
		})
		uni.navigateTo({
			url: "/pages/ranks/ranks"
		})
	}

	const modal = ref()
	const toast = ref()

	import {
		userPostApi,
		userModifyApi,
		userFindPswApi,
		userPswEmailSendApi
	} from "@/myapi/ashEchoes.js"

	const uid = ref(uni.guser.getId())
	const nickname = ref(uni.guser.getNickname())
	const account = ref(uni.guser.getAccount())
	const loginPage = ref(true)
	const data = ref({
		uid: '',
		account: '',
		code: "",
		password: '',
		nickname: uni.guser.getNickname(),
		rpwd: '',
		email: ''
	})

	function dataInit() {
		data.value.account = ""
		data.value.uid = ""
		data.value.code = ""
		data.value.password = ""
		data.value.nickname = ""
		data.value.rpwd = ""
		data.value.email = ""
	}
	// 开启登录窗口
	const loginModal = ref()
	const loginPopup = ref()
	const bh = ref('lout')
	const loginModalData = ref({
		title: "温馨提示",
		content: '是否退出登录？'
	})

	function sureC() {
		if (bh.value == 'lout') {
			dataInit()
			account.value = ""
			nickname.value = ""
			uid.value = ""
			uni.guser.loginOut()
			loginPopup.value.close()
			// #ifdef WEB
			if (typeof(recorder) == 'object') {
				recorder.logout("logout")
			}
			// #endif
		}
	}

	function toLogin() {
		loginPopup.value.open()
	}

	const uCode = ref(null)
	const codeText = ref("发送验证码")
	const findAccountPsw = ref(false)

	function uCodeChange(v) {
		codeText.value = v
	}

	function uCodeEnd(v) {
		codeText.value = '重新获取'
	}

	function getCode() {
		if (uCode.value.canGetCode) {
			toast.value.show({
				type: 'loading',
				title: '请等待',
				message: "请等待",
				duration: 30000,
				overlay: false
			})
			userPswEmailSendApi(data.value, (r) => {
				toast.value.show({
					type: 'default',
					message: '请前往邮箱：' + data.value.email + " 查看验证码以及所有涉及账号~",
					overlay: false
				})
				uCode.value.start();
			}, (e) => {
				toast.value.show({
					type: 'default',
					message: e,
					overlay: false
				})
			})
		} else {
			toast.value.show({
				type: 'default',
				message: "请等待60S之后再操作~",
				overlay: false
			})
		}
	}

	function sureFindPsw() {
		if (!data.value.email) {
			toast.value.show({
				type: 'default',
				message: "请输入邮箱~",
				overlay: false
			})
			return
		}
		if (!data.value.code) {
			toast.value.show({
				type: 'default',
				message: "请输入验证码~",
				overlay: false
			})
			return
		}
		if (!data.value.password || (data.value.password != data.value.rpwd) || data.value.password.length < 6) {
			toast.value.show({
				type: 'default',
				message: "密码不符合规范(至少6位数)~",
				overlay: false
			})
			return
		}
		toast.value.show({
			type: 'loading',
			title: '请等待',
			message: "请等待",
			duration: 30000,
			overlay: false
		})
		userFindPswApi(data.value, (r) => {
			toast.value.show({
				type: 'default',
				message: r,
				overlay: false
			})
			dataInit()
			loginPopup.value.close()
			findAccountPsw.value = false
		}, (e) => {
			toast.value.show({
				type: 'default',
				message: e,
				overlay: false
			})
		})
	}

	function findPsw() {
		findAccountPsw.value = true
	}
	// 切换登录页面
	function changeLogin() {
		dataInit()
		loginPage.value = !loginPage.value
	}
	// 切换注册页面
	function changeRegister() {
		dataInit()
		loginPage.value = !loginPage.value
	}

	function logout() {
		loginModalData.value.content = "是否退出登录？"
		bh.value = "lout"
		loginModal.value.open()
	}
	//修改数据
	function submitModifyData() {
		toast.value.show({
			type: 'loading',
			title: '请等待',
			message: "请等待",
			duration: 30000,
			overlay: false
		})
		if (!data.value.nickname.length || data.value.nickname.length >= 8) {
			toast.value.show({
				type: 'default',
				message: "昵称长度为1-7位~",
				overlay: false
			})
			return
		}
		data.value.uid = uni.guser.getId()
		userModifyApi(data.value, (r) => {
			uni.guser.setNickname(r.nickname)
			nickname.value = r.nickname
			toast.value.show({
				type: 'default',
				message: "昵称修改成功",
				overlay: false
			})
			loginPopup.value.close()
		}, e => {
			toast.value.show({
				type: 'default',
				message: e,
				overlay: false
			})
		})
	}
	// 提交数据
	function submitLoginData() {
		toast.value.show({
			type: 'loading',
			title: '请等待',
			message: "请等待",
			duration: 30000,
			overlay: false
		})
		if (data.value.account.length < 6) {
			toast.value.show({
				type: 'default',
				message: "账号至少为6位~",
				overlay: false
			})
			return
		} else if (data.value.password.length < 6) {
			toast.value.hide()
			toast.value.show({
				type: 'default',
				message: "密码至少为6位~",
				overlay: false
			})
			return
		}

		if (loginPage.value) {
			userPostApi("login", data.value, (r) => {
				loginEnd(r)
			}, (e) => {
				toast.value.show({
					type: 'default',
					message: e,
					overlay: false
				})
			})
		} else {
			if (!data.value.nickname.length || data.value.nickname.length >= 8) {
				toast.value.show({
					type: 'default',
					message: "昵称长度为1-7位~",
					overlay: false
				})
				return
			}
			if (data.value.password != data.value.rpwd) {
				toast.value.show({
					type: 'default',
					message: "两次输入密码不一致~",
					overlay: false
				})
				return
			}
			userPostApi("register", data.value, (r) => {
				loginEnd(r)
			}, (e) => {
				toast.value.show({
					type: 'default',
					message: e,
					overlay: false
				})
			})
		}
	}

	function loginEnd(r) {
		let message = '登录成功~'
		if (!loginPage.value) {
			message = '注册并登录成功~'
		}
		toast.value.show({
			type: 'default',
			message,
			overlay: false
		})
		nickname.value = r.nickname
		uni.guser.setId(r.uuid)
		uni.guser.setNickname(r.nickname)
		uni.guser.setAccount(data.value.account)
		uid.value = r
		account.value = data.value.account
		dataInit()
		data.value.nickname = r.nickname
		loginPopup.value.close()
		const dda = loginPage.value
		loginPage.value = true
		// #ifdef WEB
		if (typeof(recorder) == 'object') {
			if (!dda) {
				bh.value = 'rrr'
				loginModalData.value.content = "注册成功，可回到APP导入数据页面继续~"
				loginModal.value.open()
			}
			recorder.uuid(r.uuid)
		}
		// #endif
	}
	import {
		useSystemStore
	} from "@/stores/SystemStore.js"
	import {
		getCurrentInstance
	} from 'vue'
	const {
		proxy
	} = getCurrentInstance()
	// #ifdef WEB
	onMounted(() => {
		proxy.$nextTick(() => {
			let body = document.getElementById('parant-body').style
			body.height = 'calc(100% - ' + useSystemStore().height + '%)'
			body.minHeight = 'calc(100% - ' + useSystemStore().originHeight + '%)'
			if (typeof(recorder) == 'object') {
				isApp.value = true
			}
		})
	})

	function toAppImportPage() {
		if (typeof(recorder) == 'object') {
			recorder.toImportDataPage()
		}
	}
	// #endif
</script>

<style scoped lang='scss'>
	$mainColor: #3067fe;

	.index-body {
		position: fixed;
		width: 100%;
		max-width: 750rpx;
		left: 50%;
		transform: translateX(-50%);
		min-height: 100%;
		background-color: #e9edef;
		z-index: 2;

		.app-import-icon {
			width: 120rpx;
			height: 120rpx;
			z-index: 9;
			bottom: 60rpx;
			right: 60rpx;
			opacity: .5;
			background-color: #3664fa;
			box-shadow: 5rpx 5rpx 5rpx #666;
			display: flex;
			align-items: center;
			justify-content: center;
			position: fixed;
			border-radius: 120rpx;

			.import-icon {
				width: 60rpx;
				height: 60rpx;
			}
		}

		.index-content {
			position: fixed;
			top: 84rpx;
			left: 0;
			width: 100%;
			height: calc(100% - 84rpx);
			padding: 20rpx 30rpx;
			box-sizing: border-box;
			overflow: auto;
		}

		.i-modal-content {
			.imc-item {
				line-height: 50rpx;
				font-size: 26rpx;
				color: #333;
			}

			.sc-aim {
				font-weight: 600;
				padding: 0 5rpx;
				color: #3067fe;
				text-decoration: underline;
			}
		}

		.head {
			position: relative;
			display: grid;
			grid-template-columns: repeat(3, 1fr);
			gap: 20rpx;
			/* #ifdef WEB */
			grid-template-rows: repeat(2, 1fr);
			/* #endif */
			margin-top: 30rpx;
			padding-bottom: 20rpx;
			border-bottom: 1rpx solid $mainColor;

			.head-card {
				display: flex;
				flex-direction: column;
				box-shadow: 5rpx 5rpx 25rpx 5rpx #dce1e5;
				/* #ifdef MP-WEIXIN */
				width: 200rpx;
				/* #endif */

				.img-box {
					background-color: #dce1e5;
					height: 118rpx;
					display: flex;
					align-items: center;
					justify-content: center;
					padding: 5rpx 0;
					border: 1rpx solid rgba(191, 205, 226, 1);

					.card-img {
						height: 100rpx;
					}
				}
			}

			.hci-title {
				text-align: center;
				font-size: 28rpx;
				line-height: 45rpx;
			}

		}

		.index-sp-thanks {
			font-size: 34rpx;
			width: 100%;
			letter-spacing: 2rpx;
			padding: 20rpx;
			margin-top: 10rpx;
			font-weight: 600;
			box-sizing: border-box;
			background-color: #dce1e5;

			.value {
				color: #3067fe;
				text-decoration: underline;
			}

			.value:hover {
				cursor: pointer;
			}
		}

		.index-version {
			font-size: 34rpx;
			width: 100%;
			margin-bottom: 10rpx;
			letter-spacing: 2rpx;
			padding: 20rpx;
			margin-top: 10rpx;
			font-weight: 600;
			box-sizing: border-box;
			background-color: #dce1e5;

			.index-v-title {
				width: 110rpx;
				color: black;
				border-bottom: 6rpx solid $mainColor;
			}

			.version-info {
				margin-top: 6rpx;
				min-height: 80rpx;
				padding: 10rpx 0;
				font-size: 24rpx;

				.vi-line {
					padding: 10rpx;
					line-height: 40rpx;
				}

				.vi-line:nth-child(odd) {
					background-color: #eaedef;
				}
			}
		}



		.index-friend {
			font-size: 34rpx;
			width: 100%;
			margin-bottom: 10rpx;
			letter-spacing: 2rpx;
			padding: 20rpx;
			margin-top: 10rpx;
			font-weight: 600;
			box-sizing: border-box;
			background-color: #dce1e5;

			.index-v-title {
				width: 110rpx;
				color: black;
				border-bottom: 6rpx solid $mainColor;
			}

			.friend-info {
				margin-top: 6rpx;
				padding: 10rpx 0 0;
				font-size: 24rpx;
				line-height: 55rpx;
				display: flex;
				flex-direction: row;
				align-items: center;
				justify-content: space-between;


				.li-split {
					display: inline-block;
					color: #3067fe;
				}

				.friend-span {
					color: #3067fe;
				}

				.friend-span:hover {
					cursor: pointer;
				}

				.right {
					color: #666;
					font-size: 24rpx;
					font-weight: 500;
				}
			}
		}

		/* #ifdef WEB */
		.index-split {
			border-bottom: 1rpx solid $mainColor;
			margin: 20rpx 0 10rpx;
		}

		/* #endif*/

	}

	.top-navbar-body {
		position: fixed;
		top: 0rpx;
		left: 0;
		width: 100%;
		padding: 20rpx 30rpx;
		box-sizing: border-box;
		overflow: auto;
		z-index: 2;

		.simple-head-title {
			position: fixed;
			width: 100%;
			max-width: 750rpx;
			padding: 10rpx;
			height: 70rpx;
			box-sizing: border-box;
			top: 0rpx;
			left: 0rpx;
			background-color: #e9ecf1;
			display: flex;
			align-items: center;
			justify-content: center;
			font-size: 28rpx;
			font-weight: 600;

			.tnl-logo {
				height: 50rpx;
				width: auto;
			}

			.back {
				position: absolute;
				left: 20rpx;
				top: 20rpx;
				top: 50%;
				transform: translateY(-50%);
				width: 30rpx;
				height: 30rpx;
			}

			.right-icon {
				position: absolute;
				right: 30rpx;
				text-decoration: underline;
				color: #3366fe;
			}

			.right-icon:hover {
				cursor: pointer;
			}

			.login-box {
				position: absolute;
				right: 30rpx;
				color: $mainColor;
				font-weight: 600;
				top: 50%;
				transform: translateY(-50%);

				.login {
					text-decoration: underline;
				}

				.no-login {
					color: red;
					text-decoration: underline;
				}
			}
		}

		.head_title-new {
			position: relative;
			width: 100%;
			display: flex;
			flex-direction: column;
			align-items: center;

			.top {
				position: relative;
				width: 100%;
				height: 60rpx;
				display: flex;
				justify-content: center;

				.logo {
					width: auto;
					height: 60rpx;
				}

				.login-box {
					position: absolute;
					right: 0;
					color: $mainColor;
					font-weight: 600;
					top: 50%;
					transform: translateY(-50%);
					max-width: 200rpx;
					white-space: nowrap;
					/* 强制同一行内显示所有文本 */
					overflow: hidden;
					/* 隐藏超出的内容 */
					text-overflow: ellipsis;


					.login {
						text-decoration: underline;
					}

					.no-login {
						color: red;
						text-decoration: underline;
					}
				}

				.login-box:hover {
					cursor: pointer;

				}
			}

			.bottom {
				font-size: 22rpx;
				color: #23272a;
			}
		}
	}
</style>