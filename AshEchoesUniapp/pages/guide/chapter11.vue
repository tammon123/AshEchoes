<template>
	<view class='chapter-11-body'>
		<scroll-view class='chapter-scroll-view' :scroll-y='true' enable-passive>
			<view class="chapter-title" id='scTop4'>进阶玩法</view>
			<view class="chapter-warning">元素反应、蚀爆</view>
			<view class="chapter-content">
				水雷（传导，可以眩晕非首领的地面怪物）
			</view>
			<view class="chapter-content">
				水火（汽化，可以降低同调者受到的伤害）
			</view>
			<view class="chapter-content">
				水霜（可以生成霜区域,并有概率秒杀小怪）
			</view>
			<view class="chapter-content">
				火雷（爆燃，造成额外伤害）
			</view>
			<view class="chapter-content">
				火霜（消融，降低敌方防御）
			</view>
			<view class="chapter-content">
				雷霜（电解，降低敌方对我方造成的伤害）
			</view>
			<view class="chapter-content">
				目前元素反应主要分为<span class="key-word">生存和攻击</span>两种。另外<span class="key-word">水，火，霜元素部分同调者可以生成对应的元素区域</span>。
			</view>
			<view class="chapter-content">
				水火、雷霜这两种元素反应为生存相关，触发后能<span class="key-word">极大的提高</span>我方生存能力。
			</view>
			<view class="chapter-content">
				火霜的元素反应可以削减敌方防御，给我们提升<span class="key-word">极高的伤害加成</span>。
			</view>
			<view class="chapter-content">
				蚀元素同调者，可以触发蚀爆反应。每<span class="key-word">蚀爆</span>一次可以增加蚀元素同调者攻击，<span
					class="key-word">最多叠加5次</span>。蚀爆为<span
					class="key-word">蚀元素同调者专属机制</span>，上场蚀元素角色后，战斗界面右上角会出现一个数值，满100就会引爆，对全场所有敌方造成<span
					class="key-word">当前生命值10%的伤害，冷却时间为15秒</span>。
			</view>
			<img style="margin: 10rpx 0;height: 370rpx;width: 100%;" mode='aspectFill'
				src='https://r.qianqiu.info/app/guide/c3-10.jpg'></img>
			<view class="chapter-content">
				物理、风元素，不参与任何元素反应。
			</view>
			<view class="chapter-content">
				其他的元素反应就需要配合烙痕或者队长技能使用，这里举例一个水雷的朗朗乾坤烙痕，其中有一个技能是，触发传导反应，提高100%的伤害。见下图：
			</view>
			<img style="margin: 10rpx 0;height: 250rpx;width: 100%;" mode='aspectFill'
				src='https://r.qianqiu.info/app/guide/c3-9.jpg'></img>
			<view class="chapter-warning">进阶队伍搭配</view>
			<view class="chapter-content">
				这里简单列举一下游戏目前毕业队伍以及烙痕
			</view>
			<view class="chapter-content">
				<span class="key-word">水雷队</span>：队长：景，队员：尤尼，言雀，芙蕖。烙痕：同行，宝石海，朗朗乾坤，底线大作战，意外邂逅，蛋生。
			</view>
			<view class="chapter-content">
				<span class="key-word">火队</span>：队长：昊苍，队员：百里屠苏，伊琅相思，比戈尼娅，言御，莫红袖。烙痕：同行，海临人的好奇心，光阴间隙，底线大作战，无罪之徒 or 空，猫狗。
			</view>
			<view class="chapter-content">
				<span class="key-word">霜队</span>：队长：风晴雪，队员：言御，尤尼，乐无异。烙痕：最好吃的朋友，朗朗乾坤，无罪之徒，意外邂逅，天狼，潮湿前路
			</view>
			<view class="chapter-content">
				<span class="key-word">物理队</span>：队长：岑缨，队员：龙晴雪，芙蕖，乐无异，（铺水角色，铺完换下）烙痕：宝石海，朗朗乾坤，飞驰海岸线，意外邂逅，蛋生，谎言之下。
			</view>
			<view class="chapter-content">
				<span class="key-word">云无月蚀队</span>：队长：云无月，队员：明月尘，鸢，紫都 烙痕：同行，馆中鸟，底线大作战，无罪之徒，蛋生，秘密音律。
			</view>
			<view class="chapter-warning">伤害计算乘区</view>
			<view class="chapter-content">
				了解有哪些伤害乘区，为配卡提供理论依据。乘区计算且可视化展示是我们小程序的一个特色功能，点击前往<span @click="td(2001, '乘区计算')" class="key-word"
					style="color: red;text-decoration: underline;">乘区计算</span>页面。
			</view>
			<view class="chapter-warning">分进合击战</view>
			<view class="chapter-content">
				白荆<span class="key-word">唯一高难玩法</span>，建议没两个以上成型队伍，不要考虑。
			</view>
			<view class="chapter-content">
				50%-150%，一个完整的配对即可。烙痕：80级以上，角色：S5以上。
			</view>
			<view class="chapter-content">
				200%-250%，两个完整的配对即可。烙痕：80级以上，角色：S5以上。
			</view>
			<view class="chapter-content">
				300%，三个完整的配对即可。烙痕：90级以上，角色：S5以上。
			</view>
		</scroll-view>
		<uv-modal ref="modal" :title="modalData.title" confirmText='前往' cancelText='不去' showCancelButton
			:content='modalData.message' @confirm="confirm"></uv-modal>
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
	} from "@dcloudio/uni-app"
	onLoad(() => {
		pApi({
			page: 'chapter-11'
		})
	})

	function confirm() {
		if (cacheId.value = 2001) {
			uni.navigateTo({
				url: "/pages/ranks/ranks"
			})
		}
		if (cacheId.value != -1) {
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
	.chapter-11-body {
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