<template>
	<view class='chapter-9-body'>
		<scroll-view class='chapter-scroll-view' :scroll-y='true' enable-passive>
			<view class="chapter-title" id='scTop4'>漫巡讲解</view>
			<view class="chapter-warning">漫巡是什么？</view>
			<view class="chapter-content">
				我们可以将白荆的队伍理解为角色扮演游戏中的小队，这个小队中有队长，有队员。我们选出了队长去挑战BOSS，那么我们是不是要先去<span class="key-word">获取装备</span>呢？
			</view>
			<view class="chapter-content">
				漫巡就是<span class="key-word">用来获取装备的手段</span>。
			</view>
			<view class="chapter-content">
				漫巡开始前让你选择六张烙痕，就是选择打造什么属性的装备。
			</view>
			<view class="chapter-content">
				烙痕有五种基础属性：体质、防御、攻击、专精、终端。
			</view>
			<view class="chapter-content">
				其中体质防御为<span class="key-word">生存属性</span>，攻击为<span class="key-word">伤害属性</span>，专精为<span
					class="key-word">全能属性</span>（给防御职业加防御，给攻击职业加攻击，给治疗职业加治疗量），<span class="key-word">终端</span>为辅助属性（注：对<span class="key-word">蚀元素同调者</span>而言是伤害属性）以及影响基地血量（没错，这游戏有一点点塔防元素）
			</view>
			<view class="chapter-content">
				不同的烙痕会提供不同的技能，其中技能分为：唤醒技能(主技能)、漫巡技能(小技能)。我们要做的就是根据自己的需求，选择6张烙痕，通过<span class="key-word">漫巡玩法定制我们的装备</span>。
			</view>
			<view class="chapter-content">
				漫巡流程跑完后，会生成一个刻印并让我们选择大小技能。确认后我们<span class="key-word">拿到的这个刻印就是我们获取的装备</span>了。
			</view>
			<view class="chapter-content">
				综上所述：<span class="key-word">漫巡就是用来获取装备的手段</span>。
			</view>
			<view class="chapter-warning">漫巡过程中的选择优先级</view>
			<view class="chapter-nav-title" @click="td(10, '激励')">
				战斗相关：激励
			</view>
			<view class="chapter-content">
				<span class="key-word" @click="td(10, '激励')" style="color: red;text-decoration: underline;">激励</span>卡的烙痕唤醒 > 烙痕唤醒 > 记忆强化 > 同调增幅(属性选择)
			</view>
			<view class="chapter-warning">怎么跑？选属性路线还是技能路线</view>
			<view class="chapter-content">
				统一路线选择<span class="key-word">中庸路线</span>：左左右右（两次技能路线，两次属性路线，技能/属性路线<span class="key-word">对点即可</span>），等小监督有自己的明确需求后在跟换（<span class="key-word">技能路线能得到更多的小技能，但是属性较弱，属性路线则跟技能路线相反</span>）。
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
			page: 'chapter-9'
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
	.chapter-9-body {
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
			font-weight: 600;
			font-size: 40rpx;
		}
		
		.character-name {
			color: #1050e7;
			text-decoration: underline #1050e7;
		}
		
		.chapter-warning {
			text-indent: 0;
			margin-top: 20rpx;
			color: #2669a7;
			font-weight: 600;
			text-decoration: underline;
			line-height: 80rpx;
			font-size: 36rpx;
		}
		
		.chapter-content {
			text-indent: 2em;
			margin-top: 20rpx;
			color: #4a4a4a;
		}
		
		.chapter-nav-title {
			text-indent: 0;
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