<template>
	<view class="story-desc-body" id='parant-story-desc-body'>
		<view class="story-dialog">
			<view class="sd-content">
				<view class="sdc-head"
					:style="{'backgroundColor':dialogBackColor,'paddingTop': 'calc('+topNavV+' + 30rpx)'}">
					<view class="exit-dialog">
						<img src="https://r.qianqiu.info/app/icons/wback.png" @click='toLastPage' alt="">
					</view>
					<view class="sdch-content">
						<view class="story-img">
							<img v-if="currentStory.banner" :src="currentStory.banner" mode='heightFix' alt="">
							<img v-else src="https://r.qianqiu.info/app/story_default.jpg" alt="">
						</view>
						<view class="sdch-right">
							<view class="sdc-title">
								{{currentStory.title}}
							</view>
							<view class="sdc-world sdc-item">
								{{currentStory.world}}
							</view>
							<view class="sdc-time sdc-item">
								{{currentStory.stime}}
							</view>
						</view>
					</view>

				</view>
				<view class="sdc-content">
					<view class="sdcc-title">引言</view>
					<view class="sdcc-desc" v-html='currentStory.desc'>
					</view>
					<view class="sdcc-title">登场人物</view>
					<view class="sdc-chars" :style="{'color':dialogBackColor}">
						<template v-if="currentStory.chars">
							<span class="sdc-char" v-for="char in currentStory.chars.split(',')">
								{{char}}
							</span>
						</template>
					</view>
					<view class="sdc-authors" v-if="currentStory.author">
						整理人员：
						<template v-for="ii in currentStory.author.split(',')">
							<span sdc-author>
								{{ii}} &nbsp;
							</span>
						</template>
					</view>
					<uv-sticky bgColor="#e9edef">
						<uv-tabs @change='chooseShowText' :lineColor="dialogBackColor"
							:activeStyle="{color: dialogBackColor}" :inactiveStyle="{color:'#6c6c6c'}"
							:list="textTabs"></uv-tabs>
					</uv-sticky>

					<view class='h5-text' v-if="currentStory.author"
						v-html="chooseTypeIndex==1?currentStory.otext:currentStory.summary"></view>
					<view class="h5-text" v-if="!currentStory.author">暂无，欢迎加入QQ群：977786603，共同建设</view>
				</view>
			</view>
			<view class="sd-foot">
				<view class="sdf-nav" @click='toLastStory' :style="{'color':dialogBackColor}">上一章</view>
				<view class="sdc-origin" @click='toLastPage' :style="{'color':dialogBackColor}">
					返回总览页
				</view>
				<view class="sdf-nav" @click='toNextStory' :style="{'color':dialogBackColor}">下一章</view>
			</view>
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
		onLoad
	} from "@dcloudio/uni-app"
	import {
		storyByIdApi
	} from '@/myapi/ashEchoes.js'
	const {
		proxy
	} = getCurrentInstance()
	const toast = ref()
	const currentStory = ref({})
	const dialogBackColor = ref("")
	const textType = ref("")
	const chooseTypeIndex = ref(0)

	function chooseShowText(item) {
		chooseTypeIndex.value = item.index
		initStyle()
	}

	function initStyle() {
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
			document.querySelectorAll('blockquote').forEach(function(ai) {
				ai.style.fontStyle = 'italic';
				ai.style.color = '#333'
				ai.style.backgroundColor = '#f5f2f0'
				ai.style.borderLeft = '5px solid #b4d5ff'
				ai.style.padding = '10px 20px'
				ai.style.margin = '10px 0'
			});
			const container = document.querySelector('.sd-content');
			container.scrollTo({
				top: 0,
				behavior: 'smooth'
			});
		})
	}

	function getContentById(id, type) {
		storyByIdApi(id, {
			type
		}, (r) => {
			dialogBackColor.value = storyTypeColor.value[r.type].color
			if (r.author) {
				r.author = r.author.replace(/[，、]/g, ',');
			}
			if (r.chars) {
				r.chars = r.chars.replace(/[，、]/g, ',');
			} else {
				r.chars = ""
			}
			if (r.title) {
				r.title = r.title.replace("＞", "")
				r.title = r.title.replace(">", "")
			}
			if (r.banner) {
				r.banner = r.banner.replace('http:', 'https:')
			}
			if (r.summary) {
				r.summary = r.summary.replace(/http:\/\//gi, 'https://');
			}
			if (r.otext) {
				r.otext = r.otext.replace(/http:\/\//gi, 'https://');
			}
			currentStory.value = r
			initStyle()
		})
	}

	function toNextStory() {
		if (currentStory.value.nextId != -1) {
			currentStory.value.id = currentStory.value.nextId
			getContentById(currentStory.value.id, store.type)
		} else {
			toast.value.show({
				message: "已经是最后一个了~"
			})
		}
	}

	function toLastStory() {
		if (currentStory.value.lastId != -1) {
			currentStory.value.id = currentStory.value.lastId
			getContentById(currentStory.value.id, store.type)
		} else {
			toast.value.show({
				message: "已经是第一个了~"
			})
		}
	}

	const pagesStack = ref(getCurrentPages().length)

	function toLastPage() {
		if (pagesStack.value == 1) {
			uni.redirectTo({
				url: "/pages/story/story"
			})
		} else {
			uni.navigateBack(-1)
		}
	}

	let queryId = ""
	import {
		useStoryStore
	} from "@/stores/StoryStore.js"
	const store = useStoryStore()
	onMounted(() => {
		getContentById(queryId, store.type)
	})
	onLoad((opt) => {
		queryId = opt.storyId
	})
	import {
		useSystemStore
	} from "@/stores/SystemStore.js"
	const topNavV = ref("0%")
	// #ifdef WEB
	onMounted(() => {
		proxy.$nextTick(() => {
			let body = document.getElementById('parant-story-desc-body').style
			body.minHeight = 'calc(100% - ' + useSystemStore().originHeight + '%)'
			topNavV.value = useSystemStore().originHeight + "%"
		})
	})
	// #endif
	const textTabs = [{
		name: '剧情概括',
	}, {
		name: '剧情梳理',
	}]
	const storyTypeColor = ref({
		'主线剧情': {
			color: '#005e61',
			choose: true
		},
		'间章剧情': {
			color: '#7abd9a',
			choose: true
		},
		'幕间剧情': {
			color: '#80a492',
			choose: true
		},
		'角色故事': {
			color: '#87c0ca',
			choose: true
		},
		'其他剧情': {
			color: '#c0d695',
			choose: true
		}
	})
</script>

<style lang="scss">
	$bgColor: #e9edef;

	.story-desc-body {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		max-width: 750rpx;
		height: 100%;
		left: calc(50% - 375rpx);
		font-size: 26rpx;
		color: #333;
		background-color: $bgColor;
		box-sizing: border-box;
		overflow: auto;

		.story-dialog {
			position: absolute;
			width: 100%;
			height: 100%;
			max-width: 750rpx;
			left: calc(50% - 375rpx);
			z-index: 9;
			background-color: $bgColor;

			.exit-dialog {
				display: flex;
				align-items: center;
				height: 80rpx;

				img {
					width: 50rpx;
					height: 50rpx;
				}
			}

			.sd-foot {
				position: fixed;
				width: 100%;
				max-width: 750rpx;
				left: calc(50% - 375rpx);
				height: 100rpx;
				bottom: 0;
				padding: 20rpx;
				box-sizing: border-box;
				display: grid;
				grid-template-columns: 1fr 3fr 1fr;
				place-items: center;
				color: #4d4a40;
				font-size: 30rpx;
				background-color: $bgColor;
				z-index: 99;

				.sdf-nav {
					color: black;
				}

				.sdf-nav:hover {
					cursor: pointer;
				}

				.sdc-origin {
					color: #3467ff;
					font-weight: 600;
				}
			}

			.sd-content {
				position: absolute;
				width: 100%;
				height: 100%;
				left: 0;
				z-index: 2;
				box-sizing: border-box;
				overflow-y: auto;
			}

			.sdc-head {
				padding: 30rpx;
				padding-bottom: 60rpx;
				box-sizing: border-box;
				color: white;

				.sdch-content {
					display: flex;
					flex-direction: row;
					padding: 10rpx;
					box-sizing: border-box;

					.story-img {
						img {
							width: calc(200rpx * 0.8);
							height: calc(280rpx * 0.8);
							object-position: top;
							border-radius: 10rpx;
						}
					}

					.sdch-right {
						display: flex;
						flex-direction: column;
						align-items: flex-start;
						padding: 0 20rpx;
						box-sizing: border-box;

						.sdc-title {
							font-size: 34rpx;
							line-height: 70rpx;
							font-weight: 600;
						}

						.sdc-item {
							line-height: 60rpx;
							font-size: 28rpx;
						}
					}
				}
			}

			.sdc-content {
				margin-top: -30rpx;
				padding: 30rpx;
				box-sizing: border-box;
				border-top-right-radius: 30rpx;
				border-top-left-radius: 30rpx;
				background-color: $bgColor;

				.sdcc-title {
					font-size: 32rpx;
					color: black;
					font-weight: 600;
					line-height: 60rpx;
				}

				.article-title {
					padding-bottom: 20rpx;
					display: flex;
					flex-direction: row;
					align-items: center;
					justify-content: space-between;

					img {
						width: 45rpx;
						height: 45rpx;
					}

					.reverse-img {
						-webkit-transform: rotate(180deg);
						/* Safari 和 Chrome */
						-moz-transform: rotate(180deg);
						/* Firefox */
						transform: rotate(180deg);
					}
				}

				.sdcc-desc {
					color: #6c6c6c;
					line-height: 40rpx;
				}

				.sdc-chars {
					.sdc-char {
						margin-right: 20rpx;
						line-height: 60rpx;
						background-color: #dedede;
						border-radius: 40rpx;
						padding: 5rpx 20rpx;
						box-sizing: border-box;
						white-space: nowrap;
					}
				}

				.sdc-authors {
					padding-bottom: 10rpx;
					border-bottom: 1rpx solid #999999;
					color: #6c6c6c;
					line-height: 60rpx;
				}

				.h5-text {
					padding: 20rpx;
					box-sizing: border-box;
				}
			}
		}

	}

	::v-deep .uv-sticky {
		top: 0 !important;
	}
</style>