<template>
	<view class='guide-body'  id='parant-body'>
		<view class="guide-content">
			<view class="tabs">
				<uv-tabs :list="list" :current="currentIndex" @click="click"></uv-tabs>
			</view>
			<view class="contents" :style="bheight">
				<view v-if="currentChapter==1001">
					<chapter1001></chapter1001>
				</view>
				<view v-if="currentChapter==1002">
					<chapter1002></chapter1002>
				</view>
				<view v-if="currentChapter==0">
					<start @nav='nav'></start>
				</view>
				<view v-if="currentChapter==1">
					<chapter1 @nav='nav'></chapter1>
				</view>
				<view v-if="currentChapter==2">
					<chapter2 @nav='nav'></chapter2>
				</view>
				<view v-if="currentChapter==3">
					<chapter3 @nav='nav'></chapter3>
				</view>
				<view v-if="currentChapter==4">
					<chapter4 @nav='nav'></chapter4>
				</view>
				<view v-if="currentChapter==5">
					<chapter5 @nav='nav'></chapter5>
				</view>
				<view v-if="currentChapter==6">
					<chapter6 @nav='nav'></chapter6>
				</view>
				<view v-if="currentChapter==7">
					<chapter7 @nav='nav'></chapter7>
				</view>
				<view v-if="currentChapter==8">
					<chapter8 @nav='nav'></chapter8>
				</view>
				<view v-if="currentChapter==9">
					<chapter9 @nav='nav'></chapter9>
				</view>
				<view v-if="currentChapter==10">
					<chapter10 @nav='nav'></chapter10>
				</view>
				<view v-if="currentChapter==11">
					<chapter11 @nav='nav'></chapter11>
				</view>
			</view>
			<view class="foot">
				<view class="next-chapter" @click="toPage(0)">
					回到起始页
				</view>
				<view class="chapter-author">作者：{{author}}</view>
			</view>
		</view>
		// #ifdef WEB
		<TopNavBar child='true'></TopNavBar>
		// #endif
	</view>
</template>

<script setup>
	import {
		getCurrentInstance,
		onMounted,
		ref
	} from 'vue';
	// #ifdef WEB
	import TopNavBar from '@/components/TopNavbar/TopNavbar.vue'
	const showNav = ref(false)
	function showNavFunc(vvvv) {
		showNav.value = vvvv
	}
	// #endif
	import {
		isH5
	} from '@/utils/Platform.js'
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import start from '@/pages/guide/start.vue'
	import chapter1 from '@/pages/guide/chapter1.vue'
	import chapter2 from '@/pages/guide/chapter2.vue'
	import chapter3 from '@/pages/guide/chapter3.vue'
	import chapter4 from '@/pages/guide/chapter4.vue'
	import chapter5 from '@/pages/guide/chapter5.vue'
	import chapter6 from '@/pages/guide/chapter6.vue'
	import chapter7 from '@/pages/guide/chapter7.vue'
	import chapter8 from '@/pages/guide/chapter8.vue'
	import chapter9 from '@/pages/guide/chapter9.vue'
	import chapter10 from '@/pages/guide/chapter10.vue'
	import chapter11 from '@/pages/guide/chapter11.vue'
	onMounted(() => {
		if (isH5()) {
			bheight.value.height = "calc(100% - 190rpx)"
		}
	})
	onLoad((opt) => {
		console.log(opt)
	})
	const bheight = ref({
		height: "calc(100% - 190rpx)"
	})
	const author = ref("洛天依")
	const currentIndex = ref(0)
	const currentChapter = ref(0)
	const scTop = ref("scTop" + currentIndex.value)
	const {
		proxy
	} = getCurrentInstance()

	function nav(r) {
		currentIndex.value = judgeIndex(list.value, r)
	}

	function toPage(r) {
		currentIndex.value = 0
		currentChapter.value = 0
		author.value = "洛天依"
	}

	function click(r) {
		currentIndex.value = judgeName(list.value, r.name)
	}
	const list = ref([{
		name: '起始页',
		index: 0,
		author: '洛天依'
	}, {
		name: '第一章',
		index: 1,
		author: '洛天依'
	}, {
		name: '第二章',
		index: 2,
		author: '洛天依'
	}, {
		name: '第三章',
		index: 3,
		author: '洛天依'
	}, {
		name: '规划养成',
		index: 4,
		author: '洛天依'
	}, {
		name: '萌新疑问汇总',
		index: 5,
		author: '洛天依'
	}, {
		name: '避坑指南',
		index: 6,
		author: '洛天依'
	}, {
		name: '萌新配队配卡推荐',
		index: 7,
		author: '洛天依'
	}, {
		name: '角色养成建议',
		index: 8,
		author: '洛天依'
	}, {
		name: '漫巡讲解',
		index: 9,
		author: '洛天依'
	}, {
		name: '战斗机制讲解',
		index: 10,
		author: '洛天依'
	}, {
		name: '进阶玩法',
		index: 11,
		author: '洛天依'
	}])

	function judgeIndex(arr, index) {
		for (let i = 0; i < arr.length; i++) {
			if (arr[i].index == index) {
				currentChapter.value = arr[i].index
				if (arr[i].author) {
					author.value = arr[i].author
				}
				return i
			}
		}
		return 0
	}

	function judgeName(arr, name) {
		for (let i = 0; i < arr.length; i++) {
			if (arr[i].name == name) {
				currentChapter.value = arr[i].index
				if (arr[i].author) {
					author.value = arr[i].author
				}
				return i
			}
		}
		return 0
	}
	import {
		useSystemStore
	} from "@/stores/SystemStore.js"

	onMounted(() => {
		// #ifdef WEB
		proxy.$nextTick(() => {
			let body = document.getElementById('parant-body').style
			body.height='calc(100% - '+useSystemStore().height+'%)'
			body.minHeight='calc(100% - '+useSystemStore().originHeight+'%)'
		})
		// #endif
	})
</script>

<style lang="scss">
	.guide-body {
		position: fixed;
		width: 100%;
		max-width: 750rpx;
		left: 50%;
		transform: translateX(-50%);
		font-size: 30rpx;
		color: #333;
		min-height: 100%;
		background-color: #e9edef;
		display: flex;
		align-items: center;
		justify-content: center;

		.guide-content {
			position: fixed;
			top:0;
			// #ifdef WEB
			top: 65rpx;
			// #endif
			left: 0;
			width: 100%;
			height: calc(100% - 65rpx);
			padding: 0 20rpx;
			box-sizing: border-box;
			overflow: auto;
			display: flex;
			flex-direction: column;

			.tabs {
				position: relative;
				height: 90rpx;
				margin-bottom: 10rpx;
				color: black;
			}

			.contents {
				position: relative;
				height: calc(100% - 190rpx);
				padding: 0 20rpx;
				box-sizing: border-box;
				overflow: hidden;
			}

			.foot {
				position: relative;
				left: 0;
				bottom: 0;
				display: flex;
				flex-direction: row;
				justify-content: space-between;
				align-items: center;
				height: 90rpx;
				width: 100%;
				line-height: 90rpx;
				padding: 0rpx 40rpx;
				box-sizing: border-box;
				font-size: 32rpx;

				.next-chapter {
					text-align: end;
					color: #2669a7;
					text-decoration: underline;
				}

				.chapter-author {
					text-align: end;
					color: #ff5500;
					font-weight: 600;
				}
			}
		}
	}
</style>