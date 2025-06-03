<template>
	<view class='thank-body'  id='parant-body'>
		<view class="thank-content">
			<view class="thank-box">
				<view class="tb-title">赞助方</view>
				<view class="thanks-card">
					<view class="tc-left">
						<img v-if='list[0].avatar' class='logo' mode="aspectFill" :src='list[0].avatar' alt="" />
						<img v-else class='logo' mode="aspectFill" src='/static/thanks/yfn.png' alt="" />
					</view>
					<view class="tc-right">
						<view :style="{color: list[0].nc}" class="name">{{list[0].name}}</view>
						<view :style="{color: list[0].rc}" class="reason">{{list[0].reason}}</view>
						<span :style="{color: list[0].pc}" v-for="v in list[0].platform.split(',')"
							class="platform">{{v}}</span>
					</view>
				</view>
			</view>
			<view class="thank-box">
				<view class="tb-title">开发者</view>
				<view class="thanks-card">
					<view class="tc-left">
						<img class='logo' mode="aspectFill" src='/static/thanks/nan.jpg' alt="" />
					</view>
					<view class="tc-right">
						<view class="name">洛天依</view>
						<span :style="{color: list[0].pc}" v-for="v in '千秋俱乐部,国色庄路过的斩风'.split(',')"
							class="platform">{{v}}</span>
					</view>
				</view>
			</view>
			<view class="thank-box">
				<view class="tb-title">UI设计</view>
				<view class="thanks-card">
					<view class="tc-left">
						<img class='logo' mode="aspectFill" :src='list[1].avatar' alt="" />
					</view>
					<view class="tc-right">
						<view :style="{color: list[1].nc}" class="name">{{list[1].name}}</view>
						<span :style="{color: list[1].pc}" v-for="v in list[1].platform.split(',')"
							class="platform">{{v}}</span>
					</view>
				</view>
			</view>
			<view class="thank-box">
				<view class="tb-title">特别鸣谢</view>
				<view class="wiki-img-box">
					<img mode='heightFix' src="https://r.qianqiu.info/app/logo_bjhl.png" alt="" class="wiki-img">
				</view>
			</view>
			<view class="thank-box">
				<view class="tb-title">同行({{list.length+'位小监督~'}})</view>
				<template v-for="i in list" :key='i.name'>
					<view class="thanks-card" v-if="i.thankId >= 20">
						<view class="tc-left">
							<img v-if='i.avatar' class='logo' mode="aspectFill" :src='i.avatar' alt="" />
							<img v-else class='logo' mode="aspectFill" src='/static/thanks/yfn.png' alt="" />
						</view>
						<view class="tc-right">
							<view :style="{color: i.nc}" class="name">{{i.name}}</view>
							<!-- <view :style="{color: i.rc}" class="reason">{{i.reason}}</view> -->
							<span :style="{color: i.pc}" v-for="v in i.platform.split(',')"
								class="platform">{{v}}</span>
						</view>
					</view>
				</template>
			</view>
		</view>
		// #ifdef WEB
		<TopNavBar child='true'></TopNavBar>
		// #endif
	</view>
</template>

<script setup>
	import {
		onMounted,
		ref
	} from 'vue';
	import {
		thanksApi
	} from "@/myapi/ashEchoes.js"
	// #ifdef WEB
	import TopNavBar from '@/components/TopNavbar/TopNavbar.vue'
	const showNav = ref(false)

	function showNavFunc(vvvv) {
		showNav.value = vvvv
	}
	// #endif

	onMounted(() => {
		thanksApi((res) => {
			list.value = res
		}, (fail) => {
			uni.toastFail("服务器出错了~请联系开发人员~")
		})
	})

	const list = ref([])
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
			body.height='calc(100% - '+useSystemStore().height+'%)'
			body.minHeight='calc(100% - '+useSystemStore().originHeight+'%)'
		})
	})
	// #endif
</script>

<style lang='scss'>
	.thank-body {
		position: fixed;
		width: 100%;
		max-width: 750rpx;
		left: 50%;
		transform: translateX(-50%);
		font-size: 26rpx;
		color: #333;
		min-height: 100%;
		background-color: #e9edef;
		display: flex;
		align-items: center;
		justify-content: center;

		.thank-content {
			position: fixed;
			top: 65rpx;
			left: 0;
			width: 100%;
			height: calc(100% - 65rpx);
			// #ifdef MP-WEIXIN
			height: 100%;
			top: 0rpx;
			// #endif
			padding: 20rpx;
			box-sizing: border-box;
			overflow: auto;

			.thank-box {
				margin-bottom: 20rpx;
				padding: 10rpx;
				background-color: #dce1e5;

				.tb-title {
					border-left: 15rpx solid #3366fe;
					padding-left: 10rpx;
					font-weight: 600;
					font-size: 28rpx;
					line-height: 40rpx;
					color: black;
					letter-spacing: 1rpx;
				}

				.wiki-img-box {
					width: 100%;
					height: 80rpx;
					display: flex;
					align-items: center;
					justify-content: center;

					.wiki-img {
						height: 80rpx;
						width: auto;
					}
				}
			}

			.thanks-card {
				position: relative;
				background-color: #dce1e5;
				/* border-radius: 20rpx; */
				margin-top: 10rpx;
				padding: 10rpx;
				display: flex;
				flex-direction: row;
				align-items: center;

				.tc-left {
					width: 80rpx;
					height: 80rpx;
					border-radius: 80rpx;
					display: flex;
					align-items: center;
					justify-content: center;
					margin-right: 20rpx;

					.logo {
						width: 70rpx;
						height: 70rpx;
						border-radius: 70rpx;
					}
				}

				.tc-right {
					.name {
						font-size: 26rpx;
						font-weight: 600;
						letter-spacing: 2rpx;
						margin-bottom: 10rpx;
					}

					.reason {
						font-size: 24rpx;
						line-height: 40rpx;
						color: black;
					}

					.platform {
						position: relative;
						display: inline-block;
						padding: 5rpx 15rpx;
						font-size: 20rpx;
						color: #c5c5c4;
						background-color: white;
						border-radius: 30rpx;
						margin-top: 5rpx;
						margin-right: 10rpx;
					}
				}
			}

			.thanks-card:first-child {
				margin-top: 0;
			}
		}

	}
</style>