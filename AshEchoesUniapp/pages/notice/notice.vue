<template>
	<view class="notice-body" id='parant-body'>
		// #ifdef WEB
		<TopNavBar child='true'></TopNavBar>
		// #endif
		<uv-toast ref='toast'></uv-toast>
		<view class="notice-content">
			<template v-for="item in arr">
				<view class="time">{{item.time}}</view>
				<view class="content">
					<view class="line" v-for="ii in item.content"> · {{ii}}</view>
				</view>
			</template>
		</view>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';
	import {updateLogApi} from "@/myapi/ashEchoes.js"
	const toast = ref()
	const arr = ref()
	// #ifdef WEB
	import TopNavBar from '@/components/TopNavbar/TopNavbar.vue'
	const showNav = ref(false)
	
	function showNavFunc(vvvv) {
		showNav.value = vvvv
	}
	// #endif
	updateLogApi(r=>{
		arr.value = JSON.parse(r)
	})
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
	onMounted(() => {
		proxy.$nextTick(() => {
			let body = document.getElementById('parant-body').style
			body.height = 'calc(100% - ' + useSystemStore().height + '%)'
			body.minHeight = 'calc(100% - ' + useSystemStore().originHeight + '%)'
		})
	})
</script>

<style lang='scss'>
	$mainColor: #3067fe;

	.notice-body {
		position: relative;
		width: 100%;
		max-width: 750rpx;
		left: 50%;
		transform: translateX(-50%);
		min-height: 100%;
		background-color: #e9edef;

		.notice-content {
			position: relative;
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
			word-break: break-all;
			overflow-wrap: break-word;
			letter-spacing: 2rpx;

			.time {
				font-size: 30rpx;
				font-weight: 600;
				color: #000;
				line-height: 100rpx;
			}

			.line {
				font-size: 26rpx;
				color: #333;
				line-height: 40rpx;
				font-weight: 600;
				margin-bottom: 20rpx;
			}
		}
	}
</style>