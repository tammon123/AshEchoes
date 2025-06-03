<template>
	<view class='chapter-4-body'>
		<scroll-view class='chapter-scroll-view' :scroll-y='true' enable-passive>
			<view class="chapter-title" id='scTop4'>Chapter终章：后续规划养成</view>
			<view class="chapter-content">
				本章节监督等级高于：25级。
			</view>
			<view class="chapter-warning">
				本小节涉及的点：潜能突破、拟合回归、工会战、天隙高难、联合作战高难、分进合计战
			</view>
			<view class="chapter-content">
				上述玩法需要小监督30级才开启~加油升级呀~
			</view>
			<view class="chapter-content">
				升级攻略：完成阶段目标、用不同角色跑慢巡提高刻印总分获取奖励、清空界面所有红点、每日任务、每周任务、扫荡材料本，这些都是有监督经验的，预计2-3天可以升到30级。
			</view>
			<view class="chapter-nav-title" @click="td(8, '潜能突破')">
				养成相关：潜能突破
			</view>
			<view class="chapter-content">
				潜能突破我们只需要给队长云无月突破就行，其他角色暂时可以不用突破，等中后期需要提升的时候再说。
			</view>
			<view class="chapter-nav-title" @click="td(8, '拟合回归玩法')">
				养成相关：拟合回归
			</view>
			<view class="chapter-content">
				拟合回归，分为25层和60层的超限模式，我们只需要考虑打完两次25层的即可，就是单纯送资源的玩法。
			</view>
			<view class="chapter-nav-title" @click="td(8, '大声俱乐部')">
				养成相关：俱乐部
			</view>
			<view class="chapter-content">
				工会战就需要参加俱乐部了，在主界面上方小按钮中参加。这个玩法是每个21天版本的前14天开放，也是纯粹送资源的玩法，可以早点加入俱乐部。俱乐部纯单机玩法，不需要太多社交。
			</view>
			<view class="chapter-nav-title" @click="td(10, '怪海处理办法')">
				战斗相关：怪海的处理方案
			</view>
			<view class="chapter-nav-title" @click="td(8, '天隙勘测')">
				养成相关：天隙高难
			</view>
			<view class="chapter-content">
				天隙高难是7-10层，这几层有一点点难度，建议烙痕等级达到70级再去尝试，人物位阶达到S4以上。如果是大部分小怪的场景，可以使用人物<span
					class="character-name">瓦卡莎</span>+烙痕
				<span class="character-name">拂晓</span>进行<span class='key-word' style="text-decoration: underline;"
					@click="td(10, '怪海处理办法')">逃课</span>处理。（可点击上方<span class='key-word' style="text-decoration: underline;"
					@click="td(10, '怪海处理办法')">怪海的处理方案</span>查看）
			</view>
			<view class="chapter-nav-title" @click="td(8, '联合作战')">
				养成相关：联合作战高难
			</view>
			<view class="chapter-content">
				联合作战高难分为两个阶段，一个是角色驻守的高危模式。高危模式驻守可以使用人物<span class="character-name">瓦卡莎</span>+烙痕
				<span class="character-name">拂晓</span>进行<span class='key-word' style="text-decoration: underline;"
					@click="td(10, '怪海处理办法')">逃课</span>处理。
			</view>
			<view class="chapter-content">
				另外一个就是决战300-360难度，这个就需要硬实力了，没啥好的办法逃课。最好有<span
					class="key-word">一队成型</span>队伍（萌新一般在一个42天版本内，使用当期UP角色能组建一个队伍），配套烙痕等级在<span
					class="key-word">80</span>以上。
			</view>
			<view class="chapter-nav-title" @click="td(11, '分进合击战')">
				养成相关：分进合击战
			</view>
			<view class="chapter-content">
				这个就是真正意思上的高难了，在每个21天版本的最后10天开启。分进合击战标了六个难度，实际上为3个分水岭。
			</view>
			<view class="chapter-content">
				萌新分水岭：50%-150%，能过这个难度区间就说明亲爱的小监督您已经不再是萌新啦~
			</view>
			<view class="chapter-content">
				出师分水岭：200%-250%，过了这个难度说明已经有了深刻的游戏理解，并学会了怎么搭配队伍~
			</view>
			<view class="chapter-content">
				大佬分水岭：300%，伟大，无需多言。
			</view>
			<view class="chapter-content">
				至此，攻略完结。小监督可返回<span @click="td(0, '起始页')" class='key-word' style="text-decoration: underline;color:red"
					>起始页</span>查看其他进阶玩法说明啦~
			</view>
		</scroll-view>
		<uv-modal ref="modal" :title="modalData.title" confirmText='前往' cancelText='不去' showCancelButton :content='modalData.message'
			@confirm="confirm"></uv-modal>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';
	import {
		pApi
	} from "@/myapi/ashEchoes.js"
	import {
		onLoad
	}from "@dcloudio/uni-app"
	onLoad(()=>{
		pApi({
			page: 'chapter-4'
		})
	})
	const emits = defineEmits(['nav'])
	
	function confirm() {
		if(cacheId.value != -1) {
			emits('nav', cacheId.value)
		}
	}
	
	const modal = ref()
	const modalData = ref({
		title: "",
		message: ""
	})
	const cacheId = ref(-1)
	function td(r, text) {
		if (!text) {
			text = ""
		}
		cacheId.value = r
		modalData.value.title = '温馨提示'
		modalData.value.message = "是否前往" + text + "详情页面？"
		modal.value.open()
	}
</script>

<style lang="scss">
	.chapter-4-body {
		position: absolute;
		top: 0%;
		left: 0;
		height: 100%;
		width: 100%;
		margin-bottom: 30rpx;
		font-size: 30rpx;
		line-height: 50rpx;

		.chapter-scroll-view {
			position: absolute;
			top: 0%;
			left: 0;
			height: 100%;
			padding: 0 20rpx 20rpx 20rpx;
			box-sizing: border-box;
		}

		.chapter-title {
			color: #333;
			font-size: 40rpx;
			font-weight: 600;
		}
		
		.character-name {
			color: #1050e7;
			text-decoration: underline #1050e7;
		}
		
		.chapter-content {
			text-indent: 2em;
			margin-top: 20rpx;
			color: #4a4a4a;
		}
		
		.chapter-warning {
			text-indent: 2em;
			margin-top: 20rpx;
			color: #ff5500;
			font-weight: 600;
			font-size: 36rpx;
		}
		
		.chapter-nav-title {
			text-indent: 2em;
			margin-top: 20rpx;
			font-weight: 600;
			color: red;
			text-decoration: underline;
		}

		.key-word {
			color: #ff5500;
			font-weight: 600;
		}
	}
</style>