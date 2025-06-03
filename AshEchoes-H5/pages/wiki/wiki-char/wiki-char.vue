<template>
	<view class="wiki-char-body" id='parant-body'>
		<uv-toast ref='toast'></uv-toast>
		<view class="wiki-char-content">
			<view class="popup-member-box">
				<view class="role-box">
					<view class="role-item" :style="rcurrent==item?{opacity: '1'}:{}" @click="handleChangeRole(item)"
						v-for="(item, index) in Object.keys(role)">
						<img :style="index==0?{'height': '40rpx','width':'40rpx',marginTop:'3rpx'}:{}"
							class='role-item-img' :src="role[item]" alt="">
						<span>{{item}}</span>
					</view>
					<view class="role-item" :style="ecurrent==item?{opacity: '1'}:{}" @click="handleChangeElement(item)"
						v-for="(item, index) in Object.keys(element)">
						<img :style="index==0?{'height': '40rpx','width':'40rpx',marginTop:'3rpx'}:{}"
							class='role-item-img' :src="element[item].icon" alt="">
						<span>{{item}}</span>
					</view>
				</view>
				<view class="card-list-box">
					<view @click="handleChooseMember(char)" class="card" v-for="(char,index) in showSelectChars"
						:key='char.characterId' wx:key='char.characterId' :style="ranks[char.rank]">
					
						<view class='pc-img-box'>
							<img :src="char.avatar" mode='widthFix' alt="" class="pc-img">
						</view>
						<view class="element-icon-box">
							<img class='element-icon' :src="role[char.role]" alt="">
							<img class='element-icon' :src="element[char.element].icon" alt="">
						</view>
						<view class="pc-name" :style="{'color':element[char.element].fcolor}">{{char.name}}</view>
					</view>
				</view>
			</view>
		</view>
		// #ifdef WEB
				<TopNavBar child='true'></TopNavBar>
				// #endif
		<AeIframe @exit='wikiUrl=""' :url='wikiUrl'></AeIframe>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';
	// #ifdef WEB
		import TopNavBar from '@/components/TopNavbar/TopNavbar.vue'
		const showNav = ref(false)
		function showNavFunc(vvvv) {
			showNav.value = vvvv
		}
		// #endif
	import {
		getcharsApi
	} from "@/myapi/ashEchoes.js"
	const toast = ref()
	
	const allChars = ref([])
	const memberPopup = ref()
	const showSelectChars = ref([])
	const ecategory = ["炎", "水", "雷", "霜", "风", "物理", "蚀"]
	const element = {
		"全元素": {
			"icon": "https://r.qianqiu.info/app/icons/noe.png",
			"color": "rgba(255, 255, 255,.25)",
			"fcolor": "rgba(255, 255, 255,1)",
		},
		"炎": {
			"icon": "https://r.qianqiu.info/app/icons/element-yan.png",
			"color": "rgba(183, 101, 93,.5)",
			"fcolor": "rgba(183, 101, 93,1)",
		},
		"水": {
			"icon": "https://r.qianqiu.info/app/icons/element_shui.png",
			"color": "rgba(140, 208, 235,.5)",
			"fcolor": "rgba(140, 208, 235,1)",
		},
		"雷": {
			"icon": "https://r.qianqiu.info/app/icons/element_lei.png",
			"color": "rgba(214, 199, 132,.5)",
			"fcolor": "rgba(214, 199, 132,1)",
		},
		"霜": {
			"icon": "https://r.qianqiu.info/app/icons/element_shuang.png",
			"color": "rgba(64, 128, 203,.5)",
			"fcolor": "rgba(64, 128, 203,1)",
		},
		"风": {
			"icon": "https://r.qianqiu.info/app/icons/element_feng.png",
			"color": "rgba(132, 186, 173,.5)",
			"fcolor": "rgba(132, 186, 173,1)",
		},
		"蚀": {
			"icon": "https://r.qianqiu.info/app/icons/element_shi.png",
			"color": "rgba(132, 105, 173,.5)",
			"fcolor": "rgba(132, 105, 173,1)",
		},
		"物理": {
			"icon": "https://r.qianqiu.info/app/icons/element_wuli.png",
			"color": "rgba(247, 249, 252,.5)",
			"fcolor": "rgba(247, 249, 252,1)",
		},
		"蚀": {
			"icon": "https://r.qianqiu.info/app/icons/element_shi.png",
			"color": "rgba(132, 105, 173,.5)",
			"fcolor": "rgba(132, 105, 173,1)",
		}
	}
	const role = {
		"全职业": "https://r.qianqiu.info/app/icons/noe.png",
		"尖锋": "https://r.qianqiu.info/app/icons/jianfeng.png",
		"铁御": "https://r.qianqiu.info/app/icons/tieyu.png",
		"轻卫": "https://r.qianqiu.info/app/icons/qingwei.png",
		"游徒": "https://r.qianqiu.info/app/icons/youtu.png",
		"筑术师": "https://r.qianqiu.info/app/icons/zhushu.png",
		"护佑者": "https://r.qianqiu.info/app/icons/huyou.png",
		"战术家": "https://r.qianqiu.info/app/icons/zhanshu.png",
	}
	const ranks = {
		"6星": {"background": "linear-gradient(135deg, rgba(147, 40, 29, 1), rgba(174, 55, 60, 1))"},
		"5星": {"background": "linear-gradient(135deg, rgba(179, 139, 75, 1), rgba(228, 210, 152, 1))"},
		"4星": {"background": "linear-gradient(135deg, rgba(60, 59, 112, 1), rgba(37, 31, 45, 1))"},
		"1星": {"background": "linear-gradient(135deg, rgba(245, 245, 245, 0.8), rgba(255, 250, 240, 0.8))"},
	}
	
	const indexCurrent = ref(0)
	const ecurrent = ref("全元素")
	const rcurrent = ref("全职业")
	const rankCurrent = ref(0)
	
	function openCharSelect(index) {
		if (sharePage.value) return
		indexCurrent.value = index
		memberPopup.value.open()
	}
	//筛选职业
	function handleChangeRole(v) {
		rcurrent.value = v
		filterData()
	}
	//筛选元素
	function handleChangeElement(v) {
		ecurrent.value = v
		filterData()
	}
	
	function handleDeleteMemberPre(index) {
		chars.value[index] = {}
	}
	
	function filterData() {
		let tempArr = allChars.value
		if (rcurrent.value != '全职业') {
			tempArr = allChars.value.filter(e => e.role == rcurrent.value)
		}
		if (ecurrent.value != '全元素') {
			tempArr = tempArr.filter(e => e.element == ecurrent.value)
		}
		showSelectChars.value = tempArr
	}
	//获取所有角色
	getcharsApi('simple/0', (r) => {
		allChars.value = r
		filterData()
	})
	const wikiUrl = ref()
	function handleChooseMember(v) {
		wikiUrl.value = "https://wiki.biligame.com/bjhl/同调者/"+v.name
	}
	import {
		useSystemStore
	} from "@/stores/SystemStore.js"
	import {
		getCurrentInstance,
		onMounted
	} from 'vue'
	const {
		proxy
	} = getCurrentInstance()
	onMounted(() => {
		proxy.$nextTick(() => {
			let body = document.getElementById('parant-body').style
			body.height='calc(100% - '+useSystemStore().height+'%)'
			body.minHeight='calc(100% - '+useSystemStore().originHeight+'%)'
		})
	})
</script>

<style lang='scss'>
	$mainColor: #3067fe;

	.wiki-char-body {
		position: fixed;
		width: 100%;
		max-width: 750rpx;
		left: 50%;
		transform: translateX(-50%);
		min-height: 100%;
		background-color: #e9edef;

		.wiki-char-content {
			position: fixed;
			top: 0rpx;
			// #ifdef WEB
			top: 70rpx;
			// #endif
			left: 0;
			width: 100%;
			height: 100%;
			padding: 20rpx 20rpx;
			box-sizing: border-box;
			overflow: auto;
		}

		.popup-member-box {
			position: relative;
			width: 100%;
			box-sizing: border-box;

			.role-box {
				position: fixed;
				display: grid;
				grid-template-columns: repeat(8, 1fr);
				gap: 10rpx;
				font-size: 20rpx;
				background-color: #e9edef;
				z-index: 999;
				width: 100%;
				left: 0;
				top: 70rpx;
				padding: 20rpx;
				box-sizing: border-box;

				.role-item {
					display: flex;
					flex-direction: column;
					align-items: center;
					justify-content: space-between;
					background-color: #2f3438;
					padding: 4rpx 0;
					box-sizing: border-box;
					color: white;
					width: 80rpx;
					height: 80rpx;
					opacity: .7;

					.role-item-img {
						width: 45rpx;
						height: 45rpx;
					}
				}
			}

			.card-list-box {
				position: relative;
				display: grid;
				grid-template-columns: repeat(4, 1fr);
				gap: 20rpx;
				padding-top: 190rpx;
				padding-bottom: 100rpx;

				.card {
					position: relative;
					width: 100%;
					height: 210rpx;
					background-color: white;
					.element-icon-box {
						position: absolute;
						display: flex;
						flex-direction: column;
						align-items: center;
						justify-content: space-around;
						z-index: 3;
						top: 0;
						left: 0;
						width: 60rpx;
						height: 120rpx;
						background-color: rgba(0, 0, 0, .4);
						.element-icon {
							width: 50rpx;
							height: 50rpx;
						}
					}
					.pc-img-box {
						width: 100%;
						height: 210rpx;
						display: flex;

						.pc-img {
							height: 210rpx;
							width: 100%;
							border-radius: 0 !important;
						}
					}

					.pc-name {
						font-size: 24rpx;
						letter-spacing: 1rpx;
						font-weight: 600;
						width: 100%;
						height: 50rpx;
						display: flex;
						align-items: center;
						justify-content: center;
						background-color: rgba(0, 0, 0, .6);
						position: absolute;
						bottom: 0;
					}
				}
			}
		}

	}
</style>