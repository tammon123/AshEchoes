<template>
	<view class='chapter-8-body'>
		<scroll-view class='chapter-scroll-view' :scroll-y='true' enable-passive>
			<view class="chapter-title" id='scTop4'>游戏内养成玩法介绍</view>
			<view class="chapter-warning">养成建议</view>
			<view class="chapter-content">
				抽角色/烙痕，以开图鉴为主，<span class="key-word">宽度大于深度</span>，越多越好。
			</view>
			<view class="chapter-content">
				萌新前期不建议乱投入资源，新手时期养成资源全部<span class="key-word">集中养成</span>一个角色即可。按照我前面推荐的，通用的就是<span
					class="character-name">云无月</span>，或者就是当期UP角色（当期UP角色最好是在论坛或者游戏群了解下角色强度）。其他角色培养进度全看<span
					class="key-word">阶段目标</span>需要怎么做，阶段目标没有要求就不养成。养成角色优先级：<span class="character-name">云无月</span>、<span
					class="character-name">芙蕖</span>、当期UP角色。前期最多养成这三个就够了。
			</view>
			<view class="chapter-warning">角色培养与职业介绍</view>
			<view class="chapter-content">
				角色这里讲起来挺简单的，白荆回廊角色一共有<span class="key-word">三个</span>大的分类。游戏中以形状区分，分别是：<span
					class="key-word">方块、菱形、三角</span>。
			</view>
			<view class="chapter-content">
				<span class="key-word">方块</span>就是战士，防御高输出高。<span class="key-word">三角</span>一般就是远程输出，防御低输出高。<span
					class="key-word">菱形</span>作为治疗/辅助，拥有回血和辅助，部分菱形角色输出也很高。
			</view>
			<view class="chapter-content">
				下面细分<span class="key-word">职业</span>：
			</view>
			<view class="chapter-content">
				<span class="key-word">方块-铁御</span>：防御极高，能吸收成吨伤害。缺点：目前没有输出辅助能力，<span class="key-word">现阶段无法上场</span>。
			</view>
			<view class="chapter-content">
				<span class="key-word">方块-轻卫</span>：防御高，能提供功能辅助。缺点：目前没有直接输出能力，<span class="key-word">很容易下岗</span>。
			</view>
			<view class="chapter-content">
				<span class="key-word">方块-尖峰</span>：防御高，输出极高。缺点：对空麻烦。 白荆回廊又称《<span
					class="key-word">尖峰回廊</span>》，现阶段版本<span class="key-word">T0</span>职业。
			</view>
			<view class="chapter-content" style="color: red;font-weight: 600;">
				重点：注意烙痕唤醒技能和小技能的图标，观察是否有三角/方块/菱形的标志。如果有，那么该技能就只有对应形状的同调者能触发。
			</view>
			<view class="chapter-content">
				<span class="key-word">三角-游徒</span>：防御低，输出高。缺点：没有<span class="key-word">合适的</span>烙痕，早期版本的神，现在<span
					class="key-word">落寞</span>了，等适配烙痕就起飞。
			</view>
			<view class="chapter-content">
				<span class="key-word">三角-筑术师</span>：防御低，输出高。缺点：没有<span class="key-word">合适的</span>烙痕，无限技能的神，<span
					class="key-word">好用</span>，等适配烙痕就起飞。
			</view>
			<view class="chapter-content">
				<span class="key-word">菱形-战术家</span>：防御低，输出未知。缺点：没有<span class="key-word">爆发</span>伤害，能为友方提供<span
					class="key-word">极强</span>的BUFF。
			</view>
			<view class="chapter-content">
				<span class="key-word">菱形-护佑者</span>：防御低。缺点：没有。<span class="key-word">治疗</span>并提供<span
					class="key-word">极强</span>BUFF。
			</view>
			<view class="chapter-content">
				下面介绍<span class="key-word">养成</span>：
			</view>
			<view class="chapter-content">
				<span class="key-word" style="color:red">角色位阶</span>提升：提升角色基础属性，<span
					class="key-word">S5</span>之后自带的技能可以提升至<span class="key-word">满级</span>（跑漫巡时极为重要）。注意：S5升级S6资源<span
					class="key-word">消耗极大</span>，且属性提升<span class="key-word">不显著</span>，需要资源充足的时候再说考虑。
			</view>
			<view class="chapter-content">
				<span class="key-word" style="color:red">角色技能</span>提升：输出角色建议主伤害技能升级至少<span
					class="key-word">9级</span>。技能满级为12级，从9级开始，技能升级<span class="key-word">消耗极大</span>。11级-12级需要特殊材料-<span
					class="key-word">黑光</span>。这个材料只能等每期的<span
					class="key-word">活动送</span>。注：一般铁御的技能不需要提升，升级技能原则：能否提供<span
					class="key-word">高额伤害</span>加成。不满足该条件的技能一律不加，可以多看看角色攻略。
			</view>
			<view class="chapter-content">
				<span class="key-word" style="color:red">角色开花</span>提升：<span
					class="key-word">开花</span>指在卡池中重复抽取同一个角色进行突破，因为突破的UI像花瓣一样，玩家普遍称之为开花。开花只有在1花和3花的时候提升最大，4花5花没有提升。因此，建议一个角色抽到3花即可（3花需要一共抽4只）。
			</view>
			<view class="chapter-content">
				<span class="key-word" style="color:red">潜能突破</span>提升：这个系统属于白荆<span
					class="key-word">我方数值膨胀</span>的一个重要系统，达到<span class="key-word">30级</span>角色位阶提升至<span
					class="key-word">S4</span>时开放。只建议<span class="key-word">队长角色提升到L7</span>，其他角色<span
					class="key-word">不要提升，不要提升</span>。潜能突破材料有三种，一种可以通过资源副本获取，一种时通过角色界面的生涯奖励领取，最重要的是<span
					class="key-word">无措回环</span>，这个跟黑光一样，普遍通过版本活动送。
			</view>
			<view class="chapter-content">
				<span class="key-word" style="color:red">烙痕养成</span>：升级就完事儿了，建议升级至少80级，80-90级资源消耗大。
			</view>
			<view class="chapter-warning">监督刻印</view>
			<view class="chapter-content">
				监督刻印其实很简单，小监督只要将自己<span class="key-word" style="color:red">攻击属性高的烙痕放进去即可</span>。战斗时，会按照<span
					class="key-word" style="color:red">监督刻印属性的20%加成</span>在战斗中，单纯用来给我方战力膨胀的一个系统。满破烙痕加的属性会稍微多一点，玩具罢了。
			</view>
			<view class="chapter-content">
				一句话概括：监督刻印只是个附加属性的玩具而已。
			</view>
			<view class="chapter-warning">茶契</view>
			<view class="chapter-content">
				茶契是一个对<span class="key-word">强度党</span>来说挺无聊但不得不玩的模块，具体表现为，请同调者喝茶，提升默契等级。随着默契指的提高，会给钻石、表情包以及角色培养-<span
					class="key-word">潜能突破</span>的重要材料。
			</view>
			<view class="chapter-content">
				每个角色喜欢和的饮品都不一样，这里我建议百度搜索：白荆回廊wiki，在顶部导航栏（图鉴-角色图鉴-选择同调者-午后茶契）中可以查看。
			</view>
			<view class="chapter-warning">白荆穹顶</view>
			<view class="chapter-content">
				一个目前阶段挺鸡肋的模块，现在只有两个一次性功能：<span class="key-word">穹顶科技升级</span>、<span class="key-word">同调者训练</span>、<span
					class="key-word">训练材料生产</span>。
			</view>
			<view class="chapter-nav-title" @click="td(10, '激励')">
				战斗相关：战术终端使用技巧
			</view>
			<view class="chapter-content">
				穹顶科技升级：点击白荆穹顶最中间的设施，会进入一个类似技能树的界面，切换底部标签-战术终端，<span
					class="key-word">优先加</span>战术终端页面技能，技能树会随着主线剧情的推进而逐渐开放。除了终端终端外还有一些生产技能，这个有就加，不影响。点技能所需技能点<span
					class="key-word">只能</span>通过时间慢慢获取，在不在线不影响获取效率。
			</view>
			<view class="chapter-content">
				体能训练：体能训练，顾名思义。就是给同调者提升属性的，没啥好说的。<span class="key-word">优先训练</span>星同调者即可。
			</view>
			<view class="chapter-content">
				训练材料生产：随着科技升级进度逐渐开放更多生产位置，目前一共三种材料，材料<span class="key-word">对半生产</span>就行，没什么要注意的地方。可以根据体能训练的消耗，来<span
					class="key-word">手动调整</span>生产配比。
			</view>
			<view class="chapter-content">
				注意：白荆穹顶页面右下角有一个<span class="key-word">人员安排</span>按钮，点进去后<span class="key-word">一键排班</span>即可。
			</view>
			<view class="chapter-warning">天隙勘测</view>
			<view class="chapter-content">
				<span
					class="key-word">周常福利本</span>，1-6层属于白给，7-10层萌新需要有一个完整的队伍才好打，建议直接用当期UP和云无月组队。练度要求：至少烙痕在60级以上，人物位阶在S4以上。
			</view>
			<view class="chapter-warning">联合作战</view>
			<view class="chapter-content">
				<span class="key-word">周常福利本</span>，普通驻守属于白给，高危驻守建议<span class="key-word" @click="td(10, '逃课教程')"
					style="color:red;text-decoration: underline;">逃课</span>。300以及360以上难度需要练度：至少<span
					class="key-word">烙痕在70级以上，人物位阶在S5以上</span>。
			</view>
			<view class="chapter-warning">拟合回归</view>
			<span class="key-word">周常福利本</span>，25波的普通难度就是白给，60波超限难度建议需要练度：至少<span
				class="key-word">烙痕在70级以上，人物位阶在S5以上</span>。
			<view class="chapter-warning">工会战</view>
			<span class="key-word">版本福利本</span>，每个版本前14天开放，要求加入工会，无练度要求，纯粹白给资源。
			<view class="chapter-warning">怎么快速升级</view>
			<view class="chapter-content">
				完成阶段目标、用不同角色跑慢巡提高刻印总分获取奖励、清空界面所有红点、每日任务、每周任务、扫荡材料本。
			</view>
			<view class="chapter-warning">快速获取抽卡资源</view>
			<view class="chapter-content">
				完成阶段目标、清空界面所有红点、用不同角色跑慢巡提高刻印总分获取奖励、每日任务、每周任务
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
			page: 'chapter-8'
		})
	})

	const emits = defineEmits(['nav'])

	function confirm() {
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
	.chapter-8-body {
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