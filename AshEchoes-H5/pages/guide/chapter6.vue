<template>
	<view class='chapter-6-body'>
		<scroll-view class='chapter-scroll-view' :scroll-y='true' enable-passive>
			<view class="chapter-title" id='scTop4'>避坑指南</view>
			<view class="chapter-warning">氪金推荐</view>
			<view class="chapter-content">
				氪金优先级。<span class="key-word">大小月卡</span>>大月卡plus(<span class="key-word">需要往期烙痕就氪</span>)>晶选68礼包>=>活动限时礼包（328>168>68）>=其他礼包（启航钜惠）>=有猫精选（X等级执行官成长助力）
			</view>
			<view class="chapter-warning">前期资源使用</view>
			<view class="chapter-content">
				新手期内很缺大量资源，因此。不要乱拉烙痕和角色的养成。另外前一周，每天的体力全部买满。
			</view>
			<view class="chapter-warning">同调者突破性价比</view>
			<view class="chapter-content">
				同调者核心进化（抽到相同角色提升）因为核心进化像点亮花瓣，所以一般叫<span class="key-word">开花</span>，亮一瓣就是1花，亮三瓣就是3花，满花是五瓣全亮，1花和3花有特性的提升，其他都仅仅是数值提升，不建议抽取2花，4花，5花，（备注，4花和5花加一起的两次数值提升等同于前面一次数值的提升）
			</view>
			<view class="chapter-warning">烙痕突破性价比</view>
			<view class="chapter-content">
				要么<span class="key-word">一张</span>，要么<span class="key-word">满破</span>，因为1-2，2-3，3-4，都只有很小的数值提升且没有质变，满突破额外多一条属性《在首个烙痕唤醒点，可直接解锁核心技能2级》可以理解唤醒3次就能拿到3级唤醒技能，非满破烙痕卡要唤醒5次才能拿到3级唤醒技能。且0破和满破的属性有些许提升
			</view>
		</scroll-view>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';import {
		pApi
	} from "@/myapi/ashEchoes.js"
	import {
		onLoad
	}from "@dcloudio/uni-app"
	onLoad(()=>{
		pApi({
			page: 'chapter-6'
		})
	})
</script>

<style lang="scss">
	.chapter-6-body {
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