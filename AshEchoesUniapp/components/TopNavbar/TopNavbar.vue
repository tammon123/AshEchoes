<template>
	<view class='top-navbar-body'>
		<view class='head_title-new' v-if="!child">
			<view class="top">
				<img class='logo' mode="heightFix" src='https://r.qianqiu.info/app/logo.png'
					alt="" />
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

		<view class="top-nav-line" v-if="child" :style="{'backgroundColor': bgColor}">
			<img @click='toHome' src="https://r.qianqiu.info/app/logo.png" alt="" class="tnl-logo">
			<img @click='toBack' v-if="pagesStack!=1" class='back' src="/static/icons/back.png" alt="">
			<view class="login-box" @click="toLogin" v-if="showAccount">
				<span class='login' v-if="uid">
					{{nickname||'已登录'}}
				</span>
				<span class='no-login' v-else>
					未登录
				</span>
			</view>
		</view>
		<uv-toast ref='toast'></uv-toast>
		<uv-modal :zIndex='99999' ref="modal" :title="modalData.title" :showCancelButton='true' @confirm="sureC"
			confirmText='好的' cancelText='不' :content='modalData.content'></uv-modal>
		<uv-popup @change='showPopup' ref="popup" mode="top">
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
		ref
	} from "vue";
	import {
		userPostApi,
		userModifyApi,
		userFindPswApi,
		userPswEmailSendApi
	} from "@/myapi/ashEchoes.js"

	const props = defineProps(['child', 'showAccount','bgColor'])
	const emits = defineEmits(['exit'])

	function showPopup(v) {
		emits('exit', v.show)
	}

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
	const modal = ref()
	const popup = ref()
	const toast = ref()
	const bh = ref('lout')
	const modalData = ref({
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
			popup.value.close()
			// #ifdef WEB
			if (typeof(recorder) == 'object') {
				recorder.logout("logout")
			}
			// #endif
		}
	}

	function toLogin() {
		popup.value.open()
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
			popup.value.close()
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
		modalData.value.content = "是否退出登录？"
		bh.value = "lout"
		modal.value.open()
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
			popup.value.close()
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
		popup.value.close()
		const dda = loginPage.value
		loginPage.value = true
		// #ifdef WEB
		if (typeof(recorder) == 'object') {
			if (!dda) {
				bh.value = 'rrr'
				modalData.value.content = "注册成功，可回到APP导入数据页面继续~"
				modal.value.open()
			}
			recorder.uuid(r.uuid)
		}
		// #endif
	}

	function toHome() {
		uni.reLaunch({
			url: "/pages/index/index"
		})
	}
	const pagesStack = ref(getCurrentPages().length)

	function toBack() {
		uni.navigateBack()
	}
</script>

<style lang="scss">
	$mainColor: #3067fe;

	.top-navbar-body {
		position: absolute;
		top: 0rpx;
		left: 0;
		width: 100%;
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