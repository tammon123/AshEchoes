<template>
	<view class="story-index-body" id='parant-story-body'>
		// #ifdef WEB
		<uv-sticky bgColor="#e9edef">
			<view class="head-area">
				<TopNavBar child='true' bgColor='#e9edef'></TopNavBar>
			</view>
		</uv-sticky>
		// #endif
		<view class="world-tree">
			<img class='wt-img' src="https://r.qianqiu.info/app/story-tree-back.png" alt="">
			<view class="world-tree-back"></view>
		</view>
		<view class="story-right-tips">
			<view class="srt-content">
				<view class="srtt">↓可点击下方词条筛选↓</view>
				<view class="srtc-items" v-for="item in Object.keys(storyTypeColor)" @click="chooseStoryType(item)">
					<view class="srtci-left"
						:style="{'border': '4rpx solid '+storyTypeColor[item].color, 'backgroundColor':storyTypeColor[item].choose?storyTypeColor[item].color:''}">
						{{storyTypeColor[item].choose?'':''}}
					</view>
					<view class="srtci-right">{{item}}</view>
				</view>
			</view>
		</view>
		<view class='story-content-body'>
			<template :key='index' v-for="(item,index) in showStory">
				<view class='story-item' :id="'si'+index">
					<view class="right-content" @click="showContentDialog(item, 'si'+index)">
						<view class="rc-title" :style="{'color':storyTypeColor[item.type].color}">
							<view class="rct-left" :style="{'backgroundColor':storyTypeColor[item.type].color}"></view>
							<view class="rct-right">
								{{item.title}}
							</view>
						</view>
						<view class="rc-content">
							<view class="rc-time">
								{{item.stime}}
							</view>
							<view v-html="item.desc"></view>
						</view>
					</view>
				</view>
			</template>
		</view>
		<uv-toast ref='toast'></uv-toast>
	</view>
</template>

<script setup>
	import {
		ref,
		onMounted,
		getCurrentInstance
	} from 'vue';
	import {
		storyListApi,
		storyByIdApi
	} from '@/myapi/ashEchoes.js'
	import {
		useStoryStore
	} from "@/stores/StoryStore.js"
	import {
		onShow
	} from "@dcloudio/uni-app"
	const toast = ref()
	const storyList = ref([])
	const showStory = ref([])
	const showOrigin = ref(false)
	const currentStory = ref({})
	const dialogBackColor = ref("")
	const store = useStoryStore()

	function showContentDialog(item, eid) {
		let type = filterStoryTypes()
		proxy.$nextTick(() => {
			const element = document.getElementById('parant-story-body');
			store.$patch({
				type: type,
				top: element.scrollTop
			})
			uni.navigateTo({
				url: '/pages/story/storyDesc/storyDesc?storyId=' + item.id + "&type=" + type
			})
		})
	}

	function changeOriginText() {
		showOrigin.value = !showOrigin.value
		proxy.$nextTick(() => {
			document.querySelectorAll('p').forEach(p => {
				if (p.querySelector('img')) {
					p.style.textIndent = "0"
				}
			});
			document.querySelectorAll('p img').forEach(img => {
				img.style.height = 'auto';
				img.style.width = '100%';
			});
		})
	}

	function chooseStoryType(key) {
		storyTypeColor.value[key].choose = !storyTypeColor.value[key].choose
		initAllStory()
	}

	function filterStory() {
		showStory.value = storyList.value
		if (store.top) {
			proxy.$nextTick(() => {
				const container = document.getElementById('parant-story-body');
				if (container) {
					container.scrollBy({
						top: store.top, // 滚动的垂直距离，正值向下滚动，负值向上滚动
						left: 0, // 水平方向不滚动
						behavior: 'smooth' // 可选，如果需要平滑滚动效果，则设置为'smooth'
					});
				}
				store.$patch({
					top: 0
				})
			})
		}
	}
	const {
		proxy
	} = getCurrentInstance()

	onShow(() => {
		initAllStory()
	})
	import {
		useSystemStore
	} from "@/stores/SystemStore.js"

	function filterStoryTypes() {
		let arr = Object.keys(storyTypeColor.value)
		let type = ""
		for (let key of arr) {
			if (storyTypeColor.value[key].choose) {
				type += (key + ",")
			}
		}
		return type
	}

	function initAllStory() {
		let type = filterStoryTypes()
		storyListApi({
			type
		}, (res) => {
			storyList.value = res
			filterStory()
		}, (err) => {
			toast.value.show({
				message: err
			})
		})
	}
	// #ifdef WEB
	onMounted(() => {
		proxy.$nextTick(() => {
			let body = document.getElementById('parant-story-body').style
			body.height = 'calc(100% - ' + useSystemStore().height + '%)'
			body.minHeight = 'calc(100% - ' + useSystemStore().originHeight + '%)'
		})
	})
	// #endif
	const storyTypeColor = ref({
		'主线剧情': {
			color: '#005e61',
			choose: true
		},
		'间章剧情': {
			color: '#7abd9a',
			choose: false
		},
		'幕间剧情': {
			color: '#80a492',
			choose: false
		},
		'角色故事': {
			color: '#87c0ca',
			choose: false
		},
		'其他剧情': {
			color: '#c0d695',
			choose: false
		}
	})
</script>

<style lang="scss">
	$bgColor: #e9edef;

	.story-index-body {
		position: fixed;
		width: 100%;
		max-width: 750rpx;
		left: calc(50% - 375rpx);
		font-size: 26rpx;
		color: #333;
		height: 100%;
		background-color: $bgColor;
		box-sizing: border-box;
		overflow: auto;

		.head-area {
			position: absolute;
			top: 0;
			width: 100%;
			max-width: 750rpx;
			height: 70rpx;
			z-index: 5;
		}

		.world-tree {
			position: absolute;
			z-index: 2;

			.wt-img {
				position: absolute;
				width: 380rpx;
				height: auto;
				top: 30rpx;
				left: 0;
			}
		}

		.story-right-tips {
			position: absolute;
			height: 362rpx;
			width: 100%;
			max-width: 750rpx;
			top: 68rpx;
			display: flex;
			flex-direction: row-reverse;
			padding: 20rpx;
			box-sizing: border-box;
			background-color: $bgColor;
			z-index: 1;
			.srtt{
				line-height: 40rpx;
				color:orangered;
			}
			.srt-content {
				padding: 10rpx;
				box-sizing: border-box;
				height: 360rpx;
				width: 330rpx;
				background-color: $bgColor;

				.srtc-items {
					display: flex;
					flex-direction: row;
					height: 50rpx;
					line-height: 50rpx;
					align-items: center;

					.srtci-left {
						border-radius: 25rpx;
						width: 23rpx;
						height: 23rpx;
						display: flex;
						align-items: center;
						justify-content: center;
						font-weight: 600;
						border-color: #ababab !important;
					}

					.srtci-right {
						margin-left: 10rpx;
						font-size: 26rpx;
						font-weight: 600;
						letter-spacing: 2rpx;
					}
				}
			}
		}

		.story-content-body {
			position: relative;
			margin-top: 470rpx;
			padding: 0 20rpx;
			box-sizing: border-box;

			.story-time-item {
				vertical-align: top;
				color: #47443b;
				font-weight: 600;
				font-size: 28rpx;
				padding: 10rpx;
				width: 120rpx;
				display: inline-block;
				text-align: end;
			}

			.story-item {
				position: relative;

				.right-content {
					padding: 10rpx;
					padding-top: 0;
					width: 100%;
					display: inline-block;
					word-wrap: break-word;
					color: black;
					letter-spacing: 1rpx;

					.rc-title {
						font-size: 28rpx;
						font-weight: 600;
						display: flex;
						align-items: center;

						.rct-left {
							width: 24rpx;
							height: 24rpx;
							border-radius: 24rpx;
						}

						.rct-right {
							margin-left: 50rpx;
						}
					}

					.rc-content {
						margin-top: 10rpx;
						margin-left: 10rpx;
						padding-bottom: 25rpx;
						padding-left: 60rpx;
						box-sizing: border-box;
						width: calc(100% - 42rpx);
						border-left: 4rpx solid #ababab;

						.rc-time {
							font-size: 20rpx;
							margin-bottom: 10rpx;
							color: #ababab;
						}
					}

				}
			}
		}
	}

	::v-deep .uv-sticky {
		top: 0 !important;
	}
</style>