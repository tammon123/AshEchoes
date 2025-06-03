<template>
	<view class='chapter-start-body'>
		<uv-modal ref="modal" :title="modalData.title" confirmText='前往' cancelText='不去' showCancelButton :content='modalData.message'
			@confirm="confirm"></uv-modal>
		<scroll-view class='chapter-scroll-view' :scroll-y='true' enable-passive>
			<view class='chapter-title' id='scTop0'>
				引言：
			</view>
			<view class="chapter-warning">
				最近更新时间：2024年9月21日
			</view>
			<view class="chapter-warning">
				如有建议/疑问，欢迎小监督加QQ群反馈：<span class="key-word"
					style="color:red;text-decoration: underline;">865686593</span>
			</view>
			<view class="chapter-content">
				此页面为我在论坛中收集的高频问题，统一在这里汇总解答，可点击链接前往具体解答内容。
			</view>
			<view class="chapter-content">
				除了以下汇总的问题外，我还整理了一份萌新监督出入坑到度过新手期的全流程攻略： <span class="key-word" style="text-decoration: underline;"
					@click="td(1, '第一章')">第一章</span>、
				<span class="key-word" style="text-decoration: underline;" @click="td(2,'第二章')">第二章</span>、
				<span class="key-word" style="text-decoration: underline;" @click="td(3,'第三章')">第三章</span>、<span
					@click="td(4,'规划养成')" class="key-word" style="text-decoration: underline;">规划养成</span>攻略内容较长，可选择观看。
			</view>
			<view class="chapter-nav" @click="td(5,'萌新疑问汇总')">
				<view class="nav-top">
					<view class="nav-icons"></view>
					<view class="nav-content">
						萌新疑问汇总
					</view>
				</view>
				<view class="nav-tips">
					<view class="nav-tip-item">
						：玩家常用交流名词(黑话)解释
					</view>
					<view class="nav-tip-item">
						：我需要刷初始号吗？买号还是自建呢？
					</view>
					<view class="nav-tip-item" style="color: red;font-weight: 600;">
						：我该抽哪个卡池呢？
					</view>
					<view class="nav-tip-item" style="color: red;font-weight: 600;">
						：我看大佬的推荐配队，但是我差很多角色和烙痕，怎么办？有下位替代吗？
					</view>
					<view class="nav-tip-item">
						：我错过了强力人权角色怎么办？有没有低星战神？
					</view>
					<view class="nav-tip-item">
						：肝度高吗？氪度高吗？数值膨胀严重吗？
					</view>
					<view class="nav-tip-item">
						：游戏福利怎么样？官方整活吗？
					</view>
					<view class="nav-tip-item">
						：这游戏什么类型？入门门槛高吗？有什么优点吗？
					</view>
					<view class="nav-tip-item">
						：输出地位高还是辅助地位高？有哪些强力的输出和辅助呢？
					</view>
				</view>
			</view>
			<view class="chapter-nav" @click="td(6,'避坑指南')">
				<div class="nav-top">
					<view class="nav-icons"></view>
					<view class="nav-content">
						避坑指南
					</view>
				</div>
				<view class="nav-tips">
					<view class="nav-tip-item">
						：氪金推荐
					</view>
					<view class="nav-tip-item">
						：前期资源使用
					</view>
					<view class="nav-tip-item">
						：同调者同名突破性价比
					</view>
					<view class="nav-tip-item">
						：烙痕同名突破性价比
					</view>
				</view>
			</view>
			<view class="chapter-nav" @click="td(7,'萌新配队配卡推荐')">
				<div class="nav-top">
					<view class="nav-icons"></view>
					<view class="nav-content">
						萌新配队配卡推荐
					</view>
				</div>
				<view class="nav-tips">
					<view class="nav-tip-item">
						：配队推荐
					</view>
					<view class="nav-tip-item">
						：配卡推荐
					</view>
					<view class="nav-tip-item">
						：借角色推荐
					</view>
					<view class="nav-tip-item">
						：借烙痕推荐
					</view>
				</view>
			</view>
			<view class="chapter-nav" @click="td(8,'游戏内养成玩法')">
				<div class="nav-top">
					<view class="nav-icons"></view>
					<view class="nav-content">
						游戏内养成玩法
					</view>
				</div>
				<view class="nav-tips">
					<view class="nav-tip-item">
						：养成建议
					</view>
					<view class="nav-tip-item">
						：角色培养与职业介绍
					</view>
					<view class="nav-tip-item">
						：监督刻印
					</view>
					<view class="nav-tip-item">
						：茶契
					</view>
					<view class="nav-tip-item">
						：白荆穹顶
					</view>
					<view class="nav-tip-item">
						：天隙勘测
					</view>
					<view class="nav-tip-item">
						：联合作战
					</view>
					<view class="nav-tip-item">
						：拟合回归
					</view>
					<view class="nav-tip-item">
						：工会战
					</view>
					<view class="nav-tip-item">
						：怎么快速升级
					</view>
					<view class="nav-tip-item">
						：快速获取抽卡资源
					</view>
				</view>
			</view>
			<view class="chapter-nav" @click="td(9,'漫巡讲解')">
				<div class="nav-top">
					<view class="nav-icons"></view>
					<view class="nav-content">
						漫巡讲解
					</view>
				</div>
				<view class="nav-tips">
					<view class="nav-tip-item">
						：漫巡是什么？
					</view>
					<view class="nav-tip-item">
						：漫巡过程中的选择优先级？
					</view>
					<view class="nav-tip-item">
						：怎么跑？选属性路线还是技能路线
					</view>
				</view>
			</view>
			<view class="chapter-nav" @click="td(10,'战斗相关')">
				<div class="nav-top">
					<view class="nav-icons"></view>
					<view class="nav-content">
						战斗相关
					</view>
				</div>
				<view class="nav-tips">
					<view class="nav-tip-item">
						：激励
					</view>
					<view class="nav-tip-item">
						：同调者移动方式
					</view>
					<view class="nav-tip-item">
						：同调者索敌范围
					</view>
					<view class="nav-tip-item">
						：小怪关卡逃课教程
					</view>
					<view class="nav-tip-item">
						：同调者场中站位
					</view>
					<view class="nav-tip-item">
						：终端技能用法
					</view>
				</view>
			</view>
			<view class="chapter-nav" @click="td(11,'进阶玩法')">
				<div class="nav-top">
					<view class="nav-icons"></view>
					<view class="nav-content">
						进阶玩法
					</view>
				</div>
				<view class="nav-tips">
					<view class="nav-tip-item">
						：元素反应、蚀爆
					</view>
					<view class="nav-tip-item">
						：进阶队伍搭配
					</view>
					<view class="nav-tip-item">
						：伤害计算乘区
					</view>
					<view class="nav-tip-item">
						：分进合击战
					</view>
				</view>
			</view>
		</scroll-view>
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
			page: 'chapter-start'
		})
	})
	const emits = defineEmits(['nav'])

	function confirm(val) {
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
	.chapter-start-body {
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

		.chapter-content {
			text-indent: 2em;
			line-height: 50rpx;
			margin-top: 20rpx;
			color: black;
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

		.chapter-nav {
			margin-top: 20rpx;
			display: flex;
			flex-direction: column;

			.nav-top {
				display: flex;
				flex-direction: row;
				align-items: center;
				line-height: 80rpx;

				.nav-icons {
					width: 15rpx;
					height: 15rpx;
					border-radius: 15rpx;
					background-color: #2669a7;
				}

				.nav-content {
					margin-left: 20rpx;
					font-weight: 600;
					letter-spacing: 1rpx;
					color: #ff5500;
					text-decoration: underline #ff5500;
				}
			}

			.nav-tips {
				text-indent: 35rpx;
				font-size: 30rpx;
				color: #696969;

				.nav-tip-item {
					line-height: 50rpx;
					color: #2669a7;
					text-decoration: underline;
				}

			}

		}
	}
</style>