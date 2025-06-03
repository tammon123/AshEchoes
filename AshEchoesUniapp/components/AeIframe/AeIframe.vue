<template>
	<view class="iframe-box" @click="exit" v-if='url'>
		<span class='tips'  @click="exit">点击这里可以退出</span>
		<iframe class='iframe-wiki' :src="url" frameborder="0"></iframe>
	</view>
</template>

<script setup>
	
	const props = defineProps(['url'])
	const emits = defineEmits(['exit'])
	
	function exit() {
		emits('exit', true)
	}
	
</script>

<style lang="scss">

	.iframe-box {
		position: fixed;
		width: 100%;
		max-width: 750rpx;
		height: 100%;
		z-index: 10000;
		display: flex;
		flex-direction: column;
		background-color: rgba(0,0,0,.7);
		
		.tips {
			position: absolute;
			left: 50%;
			color: red;
			bottom: 5%;
			transform: translateX(-50%);
			font-size: 30rpx;
			font-weight: 600;
			line-height: 10%;
		}
		
		.iframe-wiki {
			position: absolute;
			width: 100%;
			height: 90%;
		}
		
	}

</style>