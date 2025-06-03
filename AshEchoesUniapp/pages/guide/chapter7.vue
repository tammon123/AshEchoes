<template>
	<view class='chapter-7-body'>
		<scroll-view class='chapter-scroll-view' :scroll-y='true' enable-passive>
			<view class="chapter-title" id='scTop4'>萌新配队配卡推荐</view>
			<view class="chapter-warning">配队推荐</view>
			<view class="chapter-content">
				萌新期间要想配队，我只推荐以<span class='character-name'>云无月</span>为核心的<span class="key-word">复调</span>体系(复调：<span
					class='character-name'>云无月</span>可以为友方同调者施加<span class="key-word">攻击特效BUFF</span>，当队友普攻造成伤害时，会<span
					class="key-word">附加一次</span><span class='character-name'>云无月</span>的伤害)。
			</view>
			<view class="chapter-content">
				队长：<span class='character-name'>云无月</span>或当期UP角色，队员：<span class='character-name'>罗吒</span>、<span
					class='character-name'>芙蕖</span>、当期UP角色或<span class='character-name'>云无月</span>，配队详情请点击<span class="key-word" @click="td(2001, '云无月配队')" style="color:red;text-decoration: underline;">云无月萌新配队</span>。
			</view>
			<view class="chapter-content">
				<span class='character-name'>云无月</span>获取方式：
			</view>
			<view class="chapter-content">
				1、萌新开局抽取角色时，抽满30次，可以自选6星角色，<span class="key-word">自选</span><span class='character-name'>云无月</span>即可。
			</view>
			<view class="chapter-content">
				2、运气好，<span class="key-word">抽卡</span>时抽到了。
			</view>
			<view class="chapter-content">
				3、任意6星同调者累计开花7次后，可前往商城<span class="key-word">橘猫尊享兑换</span>。
			</view>
			<img style="margin: 10rpx 0;height: 450rpx;width: 100%;" mode='aspectFill'
				src='https://r.qianqiu.info/app/guide/c3-1.jpg'></img>
			<view class="chapter-content">
				4、重点：进入战斗时可以选择<span class="key-word">借</span>其他玩家的<span class='character-name'>云无月</span>。（部分战斗场景无法借）
			</view>
			<view class="chapter-warning">配卡推荐</view>
			<view class="chapter-content">
				萌新期间只推荐以下烙痕：
			</view>
			<view class="chapter-content">
				底线大作战：SSR品质攻击卡，<span class="key-word">借</span>去他人烙痕即可。
			</view>
			<view class="chapter-content">
				代号百草：SR品质攻击<span class="key-word" @click="td(10, '激励')" style="color:red;text-decoration: underline;">激励</span>卡，查看游戏邮箱领取。
			</view>
			<view class="chapter-content">
				假日余暇：SR品质专精<span class="key-word" @click="td(10, '激励')"  
					style="color:red;text-decoration: underline;">激励</span>卡，完成主线第一章节，在任务列表领取。
			</view>
			<view class="chapter-content">
				生死在握：R品质终端卡，抽烙痕卡池必定获取（<span class="key-word">没有就随便放个攻击卡即可</span>）。
			</view>
			<view class="chapter-content">
				幕影重重：SSR品质体质卡，完成主线第二章节，在任务列表领取。
			</view>
			<view class="chapter-content">
				因果历然：SSR品质防御卡，完成主线第三章节，在任务列表领取。
			</view>

			<view class="chapter-warning">借角色推荐</view>
			<view class="chapter-content">
				战斗开始前，人物选择页面右下格子，可以借取他人角色，这里我们讲一下要借什么品质的好，这里我们用<span class='character-name'>云无月</span>举例。
			</view>
			<img style="margin: 10rpx 0;height: 300rpx;width: 100%;" mode='aspectFill'
				src='https://r.qianqiu.info/app/guide/c3-2.jpg'></img>
			<view class="chapter-content">
				角色名字下方有一个S6-L7，这个S6指的是角色位阶等级，<span class="key-word">位阶等级S6满级</span>。这个L7指的是潜能突破等级，<span class="key-word">潜能突破L9满级</span>。我们借角色的时候一定要是<span class="key-word">S6位阶</span>，潜能等级需要<span class="key-word">大于L5</span>。
			</view>
			<view class="chapter-content">
				在往下面看，有个12-12-4。这两个12指的是技能等级，12就是满级。4指的是角色开花(突破)等级，6是满级。借角色的时候尽量选择技能等级<span class="key-word">大于</span>11级，开花<span class="key-word">大于</span>4即可。
			</view>
			<view class="chapter-content">
				最后就是我红圈的地方，这个是特性强化<span class="key-word">（所有同调者会依次开放）</span>，如果借的是<span class="key-word">开服角色</span>，一定要选择有这个图标的。
			</view>
			<view class="chapter-content">
				开服6星角色：云无月、莫红袖、昊苍、耶芙娜、卯绒绒、瞳、崔远之、北洛、龙晴、紫都。
			</view>
			<view class="chapter-warning">借烙痕推荐</view>
			<view class="chapter-content">
				烙痕借取就简单很多了。
			</view>
			<img style="margin: 10rpx 0;height: 300rpx;width: 100%;" mode='aspectFill'
				src='https://r.qianqiu.info/app/guide/c3-3.jpg'></img>
			<view class="chapter-content">
				SSR品质烙痕烙痕等级最高为90，SR为80。借烙痕一定要借满级的。
			</view>
			<view class="chapter-content">
				另外就是突破等级，我红箭头指向的亮起来的三角形，一定要是满的。
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
			page: 'chapter-7'
		})
	})
	
	const emits = defineEmits(['nav'])
	
	
	function confirm() {
		if(cacheId.value == 2001) {
			uni.navigateTo({
				url: '/pages/ranks/ranks?cardId=466'
			})
		}
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
	.chapter-7-body {
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