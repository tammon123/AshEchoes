<template>
	<view class='skill-popup-body' v-show="props.sshow" @click="hiddenSkillInfo">
		<uv-popup ref="skillComponentPopup" :customStyle="{'width': 'calc(100% - 40rpx)'}">
			<view class='skill-info-show-box'>
				<view class="sisb-title">
					<span class="skill-name">{{props.sdata.技能名称}}</span>
					<span class="skill-ttype">{{props.sdata.技能分类描述}}</span>
				</view>
				<view class="skill-icons-box">
					<img :src="props.sdata.img" alt="" class="skill-icons">
				</view>
				<view class="skill-info-box">
					<view class="left-title">稀有度</view>
					<view class="right-value rank-value">{{props.sdata.稀有度.toUpperCase()}}</view>
					<view class="left-title">生效职业</view>
					<view class="right-value">
						<span class="role-box" v-for="item in props.sdata.职业.split('、')">{{item}}</span>
					</view>
					<view class="left-title">技能描述</view>
					<view class="right-value right-desc" v-html="props.sdata.描述"></view>
					<view class="left-title">所需技能点</view>
					<view class="right-value">{{props.sdata.点亮所需技能点}}</view>
					<view class="left-title">来源</view>
					<view class="right-value skill-from-value">
						<view v-if="props.sdata.来源.chars.length" class="char-from skill-from">
							<view class="rvf-title">
								<p  style="margin-right: 10rpx;">◆队长唤醒技能</p>
								<view class="rvfv">
									<view class="rvfc-item"  @click='toChar(item)'  v-for="(item,index) in props.sdata.来源.chars">
										<span style="margin-right: 10rpx;">&nbsp;&nbsp;◇</span>
										<img :src="charRoleIcons[item.role]" alt="">
										<img :src="charElementIcons[item.element]" alt="">
										<span class='char-name'>{{item.name}}</span>
									</view>
								</view>
							</view>
						</view>
						<view v-if="props.sdata.来源.memory.length" class="memory-from skill-from">
							<view class="rvf-title">
								<p  style="margin-right: 10rpx;">◆烙痕唤醒技能</p>
								<view class="rvfv">
									<view class="rvfc-item" @click='toMemory(item)' v-for="(item,index) in props.sdata.来源.memory">
										<span style="margin-right: 10rpx;">&nbsp;&nbsp;◇</span>
										<img :src="memoryCategoryAttr[item.category]" alt="">
										<img :src="memoryLevelIcons[item.rank]" alt="">
										<span class='char-name'>{{item.name}}</span>
									</view>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</uv-popup>
	</view>
</template>

<script setup>
	import {
		ref,
		watch
	} from 'vue';
	const skillComponentPopup = ref(false)
	const props = defineProps(['sdata', 'sshow'])
	const emits = defineEmits(['hidden'])
	
	function toChar(v) {
		uni.navigateTo({
			url: "/pages/wiki/wiki-detail/char-detail/char-detail?name="+v.name
		})
	}
	function toMemory(v) {
		uni.navigateTo({
			url: "/pages/wiki/wiki-detail/memory-detail/memory-detail?name="+v.name
		})
	}
	
	const charRoleIcons = {
		"尖锋": "https://r.qianqiu.info/app/icons/jianfeng.png",
		"铁御": "https://r.qianqiu.info/app/icons/tieyu.png",
		"轻卫": "https://r.qianqiu.info/app/icons/qingwei.png",
		"游徒": "https://r.qianqiu.info/app/icons/youtu.png",
		"筑术师": "https://r.qianqiu.info/app/icons/zhushu.png",
		"护佑者": "https://r.qianqiu.info/app/icons/huyou.png",
		"战术家": "https://r.qianqiu.info/app/icons/zhanshu.png",
	}
	const charElementIcons = {
		"炎": "https://r.qianqiu.info/app/icons/element-yan.png",
		"风": "https://r.qianqiu.info/app/icons/element_feng.png",
		"蚀": "https://r.qianqiu.info/app/icons/element_shi.png",
		"霜": "https://r.qianqiu.info/app/icons/element_shuang.png",
		"水": "https://r.qianqiu.info/app/icons/element_shui.png",
		"雷": "https://r.qianqiu.info/app/icons/element_lei.png",
		"物理": "https://r.qianqiu.info/app/icons/element_wuli.png"
	}
	const memoryLevelIcons = {
		"SSR": "https://r.qianqiu.info/app/icons/ssr.png",
		"SR": "https://r.qianqiu.info/app/icons/sr.png",
		"R": "https://r.qianqiu.info/app/icons/r.png"
	}
	const memoryCategoryAttr = {
		"体质": "https://r.qianqiu.info/app/icons/health.png",
		"防御": "https://r.qianqiu.info/app/icons/defence.png",
		"攻击": "https://r.qianqiu.info/app/icons/attack.png",
		"专精": "https://r.qianqiu.info/app/icons/mastery.png",
		"终端": "https://r.qianqiu.info/app/icons/terminal.png"
	}
	
	function hiddenSkillInfo() {
		emits('hidden', false)
	}

	watch(() => props.sdata, (n, o) => {
		console.log(n)
	})

	watch(() => props.sshow, (n, o) => {
		if(n) {
			skillComponentPopup.value.open('center')
		}else {
			skillComponentPopup.value.close()
		}
	})
</script>

<style lang="scss">
	.skill-popup-body {
		position: fixed;
		width: 100%;
		height: calc(100% - 70rpx);
		top: 70rpx;
		background-color: rgba(0, 0, 0, .7);
	}
	.skill-info-show-box {
		position: relative;
		max-height: 1000rpx;
		overflow: auto;
		.sisb-title {
			position: relative;
			background-color: #585b60;
			color: white;
			height: 60rpx;
			line-height: 60rpx;
			text-align: center;
			.skill-name {
				font-weight: 600;
				font-size: 26rpx;
			}
			.skill-ttype {
				position: absolute;
				right: 20rpx;
				background-color: #b1b1b1;
				line-height: 44rpx;
				box-sizing: border-box;
				padding: 0 20rpx;
				top: 8rpx;
			}
		}
		
		.skill-icons-box {
			height: 100rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			border-bottom: 1rpx solid #585b60;
			.skill-icons {
				width: 80rpx;
				height: 80rpx;
			}
		}
		
		.skill-info-box {
			display: grid;
			grid-template-columns: 1fr 3fr;
			letter-spacing: 1rpx;
			font-size: 24rpx;
			.left-title {
				border-bottom: 1rpx solid #585b60;
				display: flex;
				align-items: center;
				justify-content: center;
				text-align: center;
				border-right: 1rpx solid #585b60;
				font-size: 28rpx;
			}
			.right-value {
				border-bottom: 1rpx solid #585b60;
				min-height: 60rpx;
				padding: 20rpx;
				box-sizing: border-box;
				display: flex;
				align-items: center;
			}
			.rank-value {
				font-size: 32rpx;
				font-weight: 600;
				color: #3467ff;
			}
			.right-desc {
				display: block;
			}
			.role-box {
				background-color: #b1b1b1;
				margin-right: 10rpx;
				color: white;
				padding: 10rpx 20rpx;
			}
			.skill-from-value {
				display: flex;
				flex-direction: column;
				align-items: start;
				.rvf-title {
					font-size: 28rpx;
					margin-bottom: 10rpx;
				}
				.rvfv {
					img {
						width: 50rpx;
						height: 50rpx;
					}
					.rvfc-item {
						display: flex;
						flex-direction: row;
						align-items: center;
						margin-bottom: 10rpx;
						.char-name {
							color: #3467ff;
							font-weight: 600;
							font-size: 24rpx;
							text-decoration: underline;
						}
						.char-name:hover {
							cursor: pointer;
						}
					}
				}
			}
		}
	}
</style>