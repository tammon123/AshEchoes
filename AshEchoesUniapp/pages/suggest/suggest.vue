<template>
	<view class="suggest-body" id='parant-body'>
		<view class="suggest-content">
			<view class="first-line">
				建议及问题反馈：
			</view>
			<view class="second-line">
				<uv-textarea v-model="value" border="bottom" height="360rpx" :customStyle="{backgroundColor: '#e9edef'}"
					:placeholderStyle="{color: '#999'}" :countStyle="{backgroundColor: '#e9edef'}" count maxlength="240"
					placeholder="非官方，非官方，非官方，本小程序是玩家自发制作，与游戏官方无关。请输入至少10个字的小助手相关详细反馈内容,我会每天早上定期查看。如有必要,可前往QQ群：865686593 向开发人员反馈"></uv-textarea>
			</view>
			<view class="thrid-line" @click="reportSu">
				<view :class="canClick?'sure-btn':'sub-btn'">
					提交反馈
				</view>
			</view>
			<!-- #ifdef WEB -->
			<view class="thrid-line" >
				网页端抽卡数据导入教程：<a href="https://mp.weixin.qq.com/s/GkHZ3tHSvitb7JU2A2hXjA">点击查看</a>
			</view>
			<!-- #endif -->
			<view class="qa-content-box">
				<view class="qa-items" v-for="i in pageList">
					<view class="ques">问题：{{i.desc}}</view>
					<view class="ans">
						<view class="bottom-line">
							回复：
							<template v-if="i.ans">
								<span v-html="i.ans"></span>
							</template>
							<template v-else>
								'尚未回复'
							</template>
						</view>
						<view class="top-line">
							<span>{{i.time}}</span>
						</view>
					</view>
				</view>
				<template v-if="pageList.length">
					<uv-load-more :loadmoreText="loadText" @loadmore='seeMore' :customStyle="{margin:'50rpx 0'}"
						color="red" lineColor="#3067fe" dashed line />
				</template>
			</view>
		</view>
		<uv-toast ref='toast'></uv-toast>
		// #ifdef WEB
		<TopNavBar child='true'></TopNavBar>
		// #endif
	</view>
</template>

<script setup>
	import {
		suggestPostApi,
		suggestPageApi
	} from '@/myapi/ashEchoes.js'
	// #ifdef WEB
	import TopNavBar from '@/components/TopNavbar/TopNavbar.vue'
	const showNav = ref(false)

	function showNavFunc(vvvv) {
		showNav.value = vvvv
	}
	// #endif
	import {
		ref,
		watch
	} from 'vue';
	const value = ref()
	const canClick = ref(false)
	watch(() => value.value, (n, o) => {
		if ((n + "").length >= 10) {
			canClick.value = true
		} else {
			canClick.value = false
		}
	})
	const toast = ref()

	let page = 1
	const size = 6
	const loadText = ref("查看更多")
	const pageList = ref([])
	initPageData()

	function initPageData() {
		let desc = ""
		// #ifdef MP-WEIXIN
		desc = "mini"
		// #endif
		suggestPageApi({
			page,
			size,
			desc
		}, r => {
			if (r.length >= 1) {
				pageList.value.push(...r)
			}
			if (r.length != size) {
				loadText.value = "没有了"
			} else {
				loadText.value = "查看更多"
				page += 1
			}
		}, e => {
			toast.value.show({
				type: 'default',
				message: e,
				overlay: false
			})
		})
	}

	function seeMore() {
		if ("没有了" == loadText.value) {
			toast.value.show({
				type: 'default',
				message: "没有更多内容了",
				overlay: false
			})
			return
		}
		initPageData()
	}

	function reportSu() {
		if ((value.value + "").length < 10) {
			toast.value.show({
				type: 'default',
				message: '至少需要10个字~',
				overlay: false
			})
			return
		}
		toast.value.show({
			type: 'loading',
			title: '请等待',
			message: "正在提交~请耐心等待",
			duration: 3000,
			overlay: false
		})
		suggestPostApi({
			uid: uni.guser.getId(),
			desc: value.value
		}, (r => {
			toast.value.show({
				type: 'default',
				message: '提交成功，请等待修复~',
				overlay: false
			})
			value.value = ""
		}), (r => {
			toast.value.show({
				type: 'default',
				message: '处理失败，可加群：865686593，联系开发人员~',
				overlay: false
			})
			value.value = ""
		}))
	}
	import {
		useSystemStore
	} from "@/stores/SystemStore.js"
	import {
		getCurrentInstance,
		onMounted
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
		})
	})
	// #endif
</script>

<style lang="scss">
	.suggest-body {
		position: fixed;
		width: 100%;
		max-width: 750rpx;
		left: 50%;
		transform: translateX(-50%);
		min-height: 100%;
		background-color: #e9edef;

		.suggest-content {
			position: fixed;
			top: 0rpx;
			// #ifdef WEB
			top: 70rpx;
			// #endif
			left: 0;
			width: 100%;
			height: 100%;
			padding: 20rpx 30rpx;
			box-sizing: border-box;
			overflow: auto;

			.first-line {
				height: 100rpx;
				display: flex;
				align-items: center;
				font-weight: 600;
				font-size: 32rpx;
				letter-spacing: 2rpx;
				border-bottom: 1rpx solid #dce1e5;
			}

			.thrid-line {
				margin-top: 40rpx;

				display: flex;
				justify-content: center;
				align-items: center;
				font-weight: 600;
				font-size: 32rpx;
				letter-spacing: 2rpx;

				.sub-btn {
					background-color: #f2f2f2;
					height: 90rpx;
					width: 360rpx;
					display: flex;
					align-items: center;
					justify-content: center;
					border-radius: 10rpx;
					color: #c2c2c2;
				}

				.sure-btn {
					background-color: #07c160;
					height: 90rpx;
					width: 360rpx;
					display: flex;
					align-items: center;
					justify-content: center;
					border-radius: 10rpx;
					color: white;
				}

				.sure-btn:hover {
					cursor: pointer;
				}
			}

			.qa-content-box {
				margin-top: 40rpx;
				margin-bottom: 100rpx;
				padding-bottom: 40rpx;
				background-color: #e9edef;

				.qa-items {
					display: flex;
					flex-direction: column;
					padding: 10rpx 0;
					border-bottom: 1rpx solid #3067fe;
					word-break: break-all;
					overflow-wrap: break-word;

					.ques {
						font-size: 25rpx;
						letter-spacing: 1rpx;
						font-weight: 600;
						color: black;
					}

					.ans {
						font-size: 26rpx;
						letter-spacing: 1rpx;
						margin-top: 10rpx;
						color: #444;

						.top-line {
							display: flex;
							flex-direction: row;
							align-items: center;
							justify-content: end;
							line-height: 36rpx;
							color: #3067fe;
						}

						.bottom-line {
							line-height: 36rpx;

						}
					}
				}
			}
		}
	}
</style>