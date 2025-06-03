<template>
	<view class='chapter-2-body'>
		<scroll-view class='chapter-scroll-view' :scroll-y='true' enable-passive>
			<view class="chapter-title" id='scTop2'>Chapter2：第二章主线开始</view>
			<view class="chapter-content">
				本章节监督等级预估为：9-18级。
			</view>
			<view class="chapter-warning">
				本小节涉及的点：<span class='key-word' style="text-decoration: underline;"
					@click="td(10, '激励讲解')">激励</span>、<span class='key-word' style="text-decoration: underline;"
					@click="td(8, '白荆穹顶')">
					白荆穹顶</span>、同调者站位策略、标准漫巡，体力每天5次买满，如果当天下线前要溢出了（指999）并且不知道怎么用，全部用来刷技能芯片（外勤-光刻协议），每种初级芯片刷就完事儿。
			</view>
			<view class="chapter-content">
				进入主线战斗，中间会提示我们有评分更高的刻印更换，点击更换为我们刚才跑的刻印就好。
			</view>
			<view class="chapter-nav-title" @click="td(7, '借人')">
				战斗相关：借角色|烙痕
			</view>
			<view class="chapter-content">
				战斗前我们选择以下角色：
			</view>
			<view class="chapter-content">
				苏筱队长、云无月、当前UP角色、罗吒、芙蕖（借高花高等级角色）。
			</view>
			<view class="chapter-warning">
				注意：进入战斗的时候把苏筱换掉，上场战斗角色换成：云无月、当前UP角色、罗吒、芙蕖 即可。
			</view>
			<view class="chapter-content">
				接下来，一路自动挂机即可，推图过程中，可能提示你：烙痕分数过低。这个时候不要管他，继续自动。
			</view>
			<view class="chapter-content">
				预计在关卡S2-8的时候，自动就过不了了，这个时候我们就准备升级我们的刻印配置，重新跑一次漫巡。
			</view>
			<view class="chapter-content">
				回到主页，点击并且进入漫巡页面。前往漫巡万象-实验室，选择人物，我们选择云无月就行。
			</view>
			<view class="chapter-content">
				选择烙痕：假日余暇、生死在握、代号百草、谎言之下、千本之门、底线大作战(借90级满破烙痕)
			</view>
			<view class="chapter-content">
				界面上有提示，让我们升级烙痕等级，先把攻击卡拉满，然后考虑其他的。如果没有材料了就不管，然后点击开始漫巡。
			</view>
			<view class="chapter-content">
				漫巡过程中会让我们选择技能平台还是属性平台，这里统一记住，萌新期间我们只需要考虑。第一次选技能平台，后续<span class="key-word">全部选择属性平台</span>。
			</view>
			<img style="margin: 10rpx 0;height: 350rpx;width: 100%;" mode='aspectFill'
				src='https://r.qianqiu.info/app/guide/c2-1.jpg'></img>
			<view class="chapter-content">
				路中选择加属性的时候，有攻击就攻击，没有就专精。一路走下去，深度一直往高了加就行。然后会出现一个新东西-记忆强化，有就选择开始强化。漫巡过程中的选择优先级为：烙痕唤醒>记忆强化>属性选择。
			</view>
			<view class="chapter-warning">
				注意：漫巡过程中人物是可以通过WSAD移动的，感觉打不过的时候别原地发呆，可以通过不停的移动攻击来打怪。就算失败了也无所谓，并不会对我们造成什么严重影响。
			</view>
			<view class="chapter-content">
				这次我们漫巡的路线长了，烙痕唤醒的次数增加了，在战斗的时候，界面左上角是不是多了一个新东西？
			</view>
			<img style="margin: 10rpx 0;height: 350rpx;width: 100%;" mode='aspectFill'
				src='https://r.qianqiu.info/app/guide/c2-2.jpg'></img>
			<view class="chapter-nav-title" @click="td(10, '激励讲解')">
				战斗相关：重点机制：激励
			</view>
			<view class="chapter-warning">
				重点来了：跑漫巡，激励一定要带满！！！(具体的激励讲解请点击上方的链接)
			</view>
			<view class="chapter-content">
				激励是这个游戏最直接的伤害乘区，我们战斗的时候一定要把激励叠满（20层），每一层加10%的伤害，20层就是200%。
			</view>
			<view class="chapter-warning">
				再次强调：激励是重中之重
			</view>
			<view class="chapter-content">
				战斗中罗吒的屏障好了就放，第一时间叠激励，烙痕唤醒选择的话，第一优先是选择激励卡的唤醒。
			</view>
			<view class="chapter-content">
				漫巡途中遭遇战可能出现打不过的情况，打不过就打不过。没问题，跳过即可。
			</view>
			<view class="chapter-nav-title" @click="td(10,'角色站位')">
				战斗相关：角色场上站位
			</view>
			<view class="chapter-nav-title" @click="td(10, '角色索敌')">
				战斗相关：角色索敌/攻击范围
			</view>
			<view class="chapter-content">
				最后来到了关底boss处刑人，这里要注意一下，所有队友的站位。开局的时候：<span
					class="key-word">先调整站位</span>。云无月跟boss贴脸，其他角色跟云无月分开站，不要跟承伤位贴贴。
			</view>
			<view class="chapter-content">
				BOSS一阶段很好打，主要是注意二阶段BOSS放出的飞弹以及小怪。因为我们没有带拦截空中道具的角色(比如：狄砚)，所以我们等飞弹落地，第一时间集火即可。
			</view>
			<view class="chapter-content">
				BOSS打完后，选技能还是跟之前一样。先把顶部是红色标签的大技能选完，在考虑选其他的小技能，目前阶段小技能随便选就行。
			</view>
			<view class="chapter-warning">
				如果技能点不够，我们就先加满技能描述中含有激励字眼的大技能。我们带的这套卡组激励技能为：屏障·炎、蚀之激励、统筹备战。
			</view>
			<view class="chapter-warning">
				这里我们带三个激励烙痕的目的是，为了让激励属性叠起来更快。后期就只会带两个激励了，过度期间没关系的。
			</view>
			<view class="chapter-content">
				漫巡跑完后，再次回到刚刚折戟沉沙的主线图。进入战斗前的选人界面，换上我们刚刚跑完的云无月刻印，重新开始推图。
			</view>
			<view class="chapter-content">
				队伍角色还是：云无月、当期UP、罗吒、芙蕖（借的高等级角色）
			</view>
			<view class="chapter-content">
				路上会碰到一个小BOSS，将云无月拉到boss脸上即可。
			</view>
			<view class="chapter-content">
				一路自动来到boss，途中不会在存在卡关的问题了。这就是<span class="key-word">激励</span>带来的提升！
			</view>
			<view class="chapter-content">
				关底BOSS是杜望，我们开局的时候，把角色分为两组。左边和右边各占两人，中间位置不要站人(boss打人挺疼)，如下图所示。
			</view>
			<img style="margin: 10rpx 0;height: 350rpx;width: 100%;" mode='aspectFill'
				src='https://r.qianqiu.info/app/guide/c2-3.jpg'></img>
			<view class="chapter-content">
				至此，第二章打完，回到主页。
			</view>
			<view class="chapter-nav-title" @click="td(8, '白荆穹顶')">
				养成相关：白荆穹顶
			</view>
			<view class="chapter-content">
				这个时候系统提示我们白荆穹顶开放了，我们跟着指引进去看看。
			</view>
			<view class="chapter-nav-title" @click="td(10, '战术终端')">
				战斗相关：战术终端
			</view>
			<view class="chapter-content">
				进入穹顶页面之后，我们直接点击中间最中间那个区域。里面是一个技能树的图，我们在底部选择战术终端页面。优先加点战术终端页面下的内容，这里我们应该可以研究两个项目，全点上即可。完成后退出白荆穹顶区域，继续推图。更详细的说明请点击上方链接查看。
			</view>
			<view class="chapter-content">
				回到主页后，我们点击任务列表，领取完成所有奖励，能拿到一个当前关键SSR烙痕-幕影重重。现在我们只差第三章送的烙痕了，等拿到后前期配队就完全体了。
			</view>
			<view class="chapter-content">
				再回来看看阶段任务，发现阶段任务给的奖励和经验都很丰富，所以我们第一目标就是清理所有的阶段任务，一直往后面清理即可。
			</view>
			<view class="chapter-warning">
				如果涉及到角色养成，我这里只推荐四个角色。云无月>当期UP>芙蕖>罗吒，其他角色能以后你有游戏理解后自己慢慢养成即可。
			</view>
			<view class="chapter-content">
				在阶段任务中，能看到下一章主线要18级才开放了。目前阶段我们要做的事情就很简单了，哪里有红点我们就去哪里，把红点全部消除掉。
			</view>
			<view class="chapter-content">
				清理红点的时候，会接触到一个特殊的玩法。天隙探测，这个玩法里面没法借人，所以我们就都用自己的角色即可。没有<span
					class="character-name">芙蕖</span>的话可以考虑买商城的芙蕖礼包，没有当期UP的话可以直接用
				<span class="character-name">比戈尼娅</span>(充值任意金额送)。
			</view>
			<view class="chapter-content">
				还有一个可能不容易注意的地方-战斗教学，在主页点击右下角的外勤即可看到以下页面。
			</view>
			<img style="margin: 10rpx 0;height: 350rpx;width: 100%;" mode='aspectFill'
				src='https://r.qianqiu.info/app/guide/c2-4.jpg'></img>
			<view class="chapter-content">
				建议把这里的战斗教学全部做完，里面有经验以及钻石拿。
			</view>
			<view class="chapter-content">
				还有一个藏得更深的奖励，在主页，左上角。见下图：
			</view>
			<img style="margin: 10rpx 0;height: 350rpx;width: 100%;" mode='aspectFill'
				src='https://r.qianqiu.info/app/guide/c2-5.jpg'></img>
			<view class="chapter-content">
				这个刻印总分是可以点进去的，里面奖励很丰富。有经验、钻石抽卡券
			</view>
			<img style="margin: 10rpx 0;height: 350rpx;width: 100%;" mode='aspectFill'
				src='https://r.qianqiu.info/app/guide/c2-6.jpg'></img>
			<view class="chapter-content">;
				这个刻印总分很好提升，使用<span class="key-word">不同角色</span>跑漫巡，最后生成的烙痕分数就会加在这个地方。小监督呀~努力跑漫巡把。
			</view>
			<view class="chapter-content">
				接下来就是提升等级的枯燥时刻了，加油~跟着任务里的阶段目标走，有时间多去跑跑<span class="key-word">漫巡</span>，完成<span
					class="key-word">每日任务</span>，每周任务以及资源本的<span class="key-word">首次开荒</span>。
			</view>
			<view class="chapter-content">
				这里插一句，跑慢巡的时候，在意识幻城这个路线，可能会遇见一个辐射图，这个图难度偏高，遇到了建议跳过，见下图：
			</view>
			<img style="margin: 10rpx 0;height: 350rpx;width: 100%;" mode='aspectFill'
				src='https://r.qianqiu.info/app/guide/c2-7.jpg'></img>
			<view class="chapter-warning">
				关键词：
				<span class='key-word' style="text-decoration: underline;" @click="td(10, '激励讲解')">激励</span>、
				<span class='key-word' style="text-decoration: underline;" @click="td(8, '白荆穹顶')">白荆穹顶</span>、
				<span class='key-word' style="text-decoration: underline;" @click="td(7, '借人')">借人/烙痕</span>、
				<span class='key-word' style="text-decoration: underline;" @click="td(10, '战术终端')">战术终端</span>、
				<span class='key-word' style="text-decoration: underline;" @click="td(10, '角色索敌')">角色索敌</span>、
				<span class='key-word' style="text-decoration: underline;" @click="td(10,'角色站位')">战斗角色站位</span>
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
			page: 'chapter-2'
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
	.chapter-2-body {
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