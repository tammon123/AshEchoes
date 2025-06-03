<template>
	<view class='chapter-5-body'>
		<scroll-view class='chapter-scroll-view' :scroll-y='true' enable-passive>
			<view class="chapter-title" id='scTop4'>萌新疑问汇总</view>
			<view class="chapter-warning">玩家常用交流名词(黑话)解释</view>
			<view class='chapter-content'>
				<span class="key-word">开花/升花/高花/满花</span>：指的是抽卡或其他方式获取相同角色后，进行突破的行为。因为突破页面的UI看起来像一朵慢慢展开的花，所以玩家普遍称为<span
					class="key-word">开花/升花</span>。<span class="key-word">高花/满花</span>指的就是突破等级高/突破满级的角色。
			</view>
			<view class='chapter-content'>
				<span class="key-word">满破(烙痕)</span>：上面名词说的是角色，这个说的就是烙痕了。一般玩家口中说的满破，指的就是烙痕突破满级。
			</view>
			<view class='chapter-content'>
				<span class="key-word" style="color: red;text-decoration: underline;"
					@click="td(10, '激励')">激励</span>：游戏核心机制，重要伤害来源，能<span class="key-word">提供200%的增伤</span>，可点击前往<span
					class="key-word" style="color: red;text-decoration: underline;"
					@click="td(10, '激励')">激励说明页面</span>，查看详细说明。
			</view>
			<view class='chapter-content'>
				<span class="key-word">方块/三角/菱形</span>：为白荆的职业分类，分别为近战/远程/辅助，详情请点击<span class="key-word"
					style="color: red;text-decoration: underline;" @click="td(8, '角色职业介绍')">角色培养与职业介绍</span>查看。
			</view>
			<view class='chapter-content'>
				<span class="key-word">借人/借卡</span>：白荆的战斗机制允许我们借用其他玩家的高花角色以及满破烙痕，这里的借指的就是这个。具体细节可点击前往<span
					class="key-word" style="color: red;text-decoration: underline;"
					@click="td(7, '萌新借卡建议')">萌新借人/借烙痕推荐</span>说明。
			</view>
			<view class='chapter-content'>
				<span class="key-word">工具人</span>：同调者中<span
					class="key-word">谁伤害最高，谁是话事人，输出大于一切</span>。能给话事人提供辅助能力的，统称<span
					class="key-word">工具人</span>。比如我攻略中推荐的<span class="character-name">云无月</span>配队，其他队友全是<span
					class="character-name">云无月</span>的工具人。非贬义，很多角色想当工具人还没资格呢~
			</view>
			<view class='chapter-content'>
				<span class="key-word">剑心</span>：指战术终端主动技能：<span class="key-word"
					style="color: red;text-decoration: underline;"
					@click="td(10, '战术终端')">同调增幅</span>。烛龙上款游戏《古剑奇谭网络版(市面上最良心的MMO游戏)》中的团队爆发技能，<span
					class="key-word">效果为30秒之内给全体友方提供极强的爆发能力</span>。因两者效果一致，加上<span
					class="key-word">古批过境</span>的原因，称呼逐渐变成了<span class="key-word">剑心</span>/(ㄒoㄒ)/。
			</view>
			<view class='chapter-content'>
				<span class="key-word">复调</span>：同调者<span
					class="character-name">云无月</span>的核心机制，大致就是有云无月的时候，队友的普攻会附加云无月的伤害。伤害频率高且倍率可观，适用与所有场景，萌新之友。因此提到的频率很高。
			</view>
			<view class='chapter-content'>
				<span class="key-word">猴戏</span>：在BOSS战中，我方爆发之前，通过操作手法不停的给场上主要输出加BUFF的手段，或者削弱敌方的手段，统称为猴戏。
			</view>
			<view class='chapter-content'>
				<span class="key-word">消融/电解/汽化/传导/蚀爆</span>：为白荆回廊高级元素反应，详情见：<span class="key-word"
					@click="td(11, '元素反应')" style="color: red;text-decoration: underline;">元素反应</span>
			</view>
			<view class='chapter-content'>
				<span class="key-word">火地板/水地板/霜地板</span>：即火/水/霜元素区域，详情见：<span class="key-word" @click="td(11, '元素反应')"
					style="color: red;text-decoration: underline;">元素反应</span>
			</view>
			<view class='chapter-content'>
				<span class="key-word">摔炮</span>：指一般放在后台具有瞬间爆发能力的角色，需要的时候换他上场开技能爆发一下。目前阶段提及这个词的玩家应该很少了。
			</view>
			<view class="chapter-warning">我需要刷初始号吗？买号还是自建呢？</view>
			<view class="chapter-content">
				<span class="key-word">不需要</span>买初始号，也<span
					class="key-word">不需要</span>刷开局，自建的号既有保障，也能从前期的体验慢慢了解游戏机制和玩法，如果直接买成品号，角色和烙痕虽然多，但是很多东西没有体验教程很多都玩不动，理解起来更费时间
			</view>
			<view class="chapter-warning">我该抽哪个卡池呢？</view>
			<view class="chapter-content">
				白荆回廊卡池分为两种：<span class="key-word">同调者卡池和烙痕卡池</span>。
			</view>
			<view class="chapter-content">
				同调者卡池<span class="key-word">只抽当期UP限定卡池</span>，烙痕卡池<span class="key-word">根据需求抽当期UP卡池和常驻心愿卡池</span>。
			</view>
			<view class="chapter-content">
				同调者卡池：分为<span class="key-word">UP限定卡池</span>(一般在抽卡界面最上方，带有UP字眼标签)和常驻卡池。白荆回廊的UP卡池<span
					class="key-word">可以抽到往期UP角色</span>(42天前)。因此，如果要抽人物，<span class="key-word" style="color:red">
					一定、一定、一定不要抽常驻卡池！</span>不要抽常驻卡池，只抽UP卡池即可。追UP角色开花之前，建议看看游戏社区对该角色的评价，不要盲目。
			</view>
			<view class="chapter-content">
				烙痕卡池：烙痕卡池分为当期UP卡池和常驻心愿卡池。当期UP卡池是否抽取建议看社区评价，一般情况下<span class="key-word"
					style="color:red">抽1张</span>即可。(氪佬建议拉满~)。重点要注意一下常驻心愿卡池，这个我单独拉出来说。
			</view>
			<view class="chapter-content">
				烙痕常驻心愿卡池：见下图，点击选择心愿，然后选择自己想要的烙痕。注：心愿烙痕池，选择得烙痕在<span class="key-word"
					style="color:red">大保底(第二次抽到SSR烙痕)时，必定抽出</span>。第一次很有可能不是选择的心愿烙痕，心愿卡池在<span class="key-word"
					style="color:red">补烙痕的时候使用</span>。
			</view>
			<img style="margin: 10rpx 0;height: 300rpx;width: 100%;" mode='aspectFill'
				src='https://r.qianqiu.info/app/guide/c4-1.jpg'></img>
			<view class="chapter-warning">我看大佬的推荐配队，但是我差很多角色和烙痕，怎么办？有下位替代吗？</view>
			<view class="chapter-content">
				白荆回廊的角色/烙痕，基本<span class="key-word">无法明确上下位</span>关系。
			</view>
			<view class="chapter-content">
				对于萌新来说，因为每个人的角色和烙痕千差万别。除了我在攻略中推荐的<span class="key-word" style="color: red;text-decoration: underline;"
					@click="td(7, '配队')">那一套云无月萌新开荒专用配队</span>外，并没有通用答案。
			</view>
			<view class="chapter-content">
				本小程序有一个配队伤害计算模块，建议萌新前往首页-点击<span class="key-word" style="color: red;text-decoration: underline;"
					@click="td(2001, '前往配队')">开始配队</span>，将大佬推荐的配队阵容选上并伤害计算，查看理论伤害数值。再依次将自己没有的角色/烙痕替换为已有的烙痕，<span
					class="key-word">多尝试计算几次</span>。综合系统给出的<span
					class="key-word">配队建议</span>（点击计算后，拉到页面最下方可以看到），再跟大佬推荐队伍的理论伤害比较，综合考虑怎么替换。<span
					class="key-word">多动动手</span>，你就是白荆高手啦~
			</view>
			<view class="chapter-warning">我错过了强力人权角色怎么办？有没有低星战神？</view>
			<view class="chapter-content">
				错过了也<span class="key-word">不要紧</span>，因为白荆回廊这款游戏<span
					class="key-word">没有强度焦虑</span>，当期的up角色搭配好了前中期可以很轻松的度过。低星战神有，但是<span
					class="key-word">不推荐练</span>。抽当期UP即可，白荆回廊是<span class="key-word">主C第一优先</span>。低星前排好用的就是<span
					class="character-name">田诺</span>。除此之外，目前辅助里面最强最好用的就是3花的<span
					class="character-name">芙蕖</span>，没有3花也能用，可以培养。自选sr推荐<span
					class="character-name">芙蕖</span>，6元礼包也有一个<span
					class="character-name">芙蕖</span>（备注；任意消费可以获得5星霜尖峰，<span
					class="character-name">比戈尼娅</span>，主要用途是叠攻速）冲6元可以得到2个5星角色
			</view>
			<view class="chapter-warning">肝度高吗？氪度高吗？数值膨胀严重吗？</view>
			<view class="chapter-content">
				除了前期做任务，推主线，打版本活动，其他的日常和周常都是很简单的，<span
					class="key-word">不肝</span>。氪度推荐氪大月卡，其次是小月卡，因为大月卡送的sr满破激励烙痕还是挺好用的，<span
					class="key-word">月卡党</span>可以玩的很舒服，<span class="key-word">氪金度低</span>。
				另外白荆的福利算是很慷慨的了，大小月卡玩家在不追花和烙痕突破的情况下可以拿满图鉴的，如果某一次黑了，少拿1-2张烙痕也没影响，因为跑漫巡的烙痕是一定要借一张的。数值膨胀的话，白荆回廊不同于其他游戏，其他游戏是怪物膨胀，你需要抽新的角色来弥补，白荆刚好是相反的，角色和烙痕的数值太强了，怪物不够打。<span
					class="key-word">我方膨胀速度大于敌方膨胀速度</span>
			</view>
			<view class="chapter-warning">游戏福利怎么样？官方整活吗？</view>
			<view class="chapter-content">
				经过以往版本规律总结：所有日常周常版本活动的产出平均到每天，<span class="key-word">零氪大约每天3.8抽，大小月卡每天4.8抽</span>。
				另外，主线版本会有额外的<span class="key-word">40抽</span>；刚过去的半周年则是额外<span class="key-word">25抽</span>。
				在国内手游里面是<span class="key-word">福利第一梯队</span>。官方主打一个听劝，目前所有改动都是为了解决玩家痛点，官方服务态度在国内游戏算很好了。
			</view>
			<view class="chapter-warning">这游戏什么类型？入门门槛高吗？有什么优点吗？</view>
			<view class="chapter-content">
				优点：<span
					class="key-word">剧情很好，没有无病呻吟，没有谜语人，没有私货内容，福利极好</span>。即时策略，在带一点战旗和塔防的元素，还参杂了mmorpg的机制。入门的门槛相比其他二游算高，想玩明白较难，萌新入坑建议跟着我们小程序的萌新全流程攻略走。（<span
					@click="td(1, '全流程攻略')" class="key-word" style="color:red;text-decoration: underline;">点击前往</span>）
			</view>
			<view class="chapter-warning">输出地位高还是辅助地位高？有哪些强力的输出和辅助呢？</view>
			<view class="chapter-content">
				输出和辅助的地位前面也讲过，你所有的队员搭配，烙痕搭配都是围绕着主C来的，这个游戏的<span class="key-word">输出地位最高</span>。
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
			page: 'chapter-5'
		})
	})


	const emits = defineEmits(['nav'])

	function confirm() {
		if (cacheId.value == 2001) {
			uni.navigateTo({
				url: '/pages/ranks/ranks'
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
	.chapter-5-body {
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