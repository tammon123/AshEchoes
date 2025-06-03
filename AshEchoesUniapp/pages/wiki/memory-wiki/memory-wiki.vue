<template>
	<view class="wiki-memory-body" id='parant-body'>
		<uv-toast ref='toast'></uv-toast>
		<view class="wiki-memory-content">
			<view class="popup-member-box popup-memory-box">
				<view class="memory-category-box">
					<view class="memory-top-box">
						<view class="role-item" :style="mccurrent==item?{opacity: '1'}:{}"
							@click="handleChangeMCategory(item)"
							v-for="(item, index) in Object.keys(memoryCategoryAttr)">
							<img :style="index==0?{'height': '40rpx','width':'40rpx',marginTop:'3rpx'}:{}"
								class='role-item-img' :src="memoryCategoryAttr[item].icon" alt="">
							<span>{{item}}</span>
						</view>
					</view>
					<view class="memory-top-box">
						<view class="role-item" :style="mlcurrent==item?{opacity: '1'}:{}"
							@click="handleChangeMLevel(item)" v-for="(item, index) in Object.keys(memoryLevelIcons)">
							<img :style="index==0?{'height': '40rpx','width':'40rpx',marginTop:'3rpx'}:{}"
								class='role-item-img' :src="memoryLevelIcons[item]" alt="">
							<span>{{item}}</span>
						</view>
					</view>
				</view>
				<view class="card-list-box">
					<view @click="handleChooseMemory(char)" class="card" v-for="(char,index) in showSelectMemorys"
						:key='char.characterId' wx:key='char.characterId'>
						<view class='pc-img-box'>
							<img :src="char.img" mode='widthFix' alt="" class="pc-img">
						</view>
						<!-- <view class="pc-attr-box">
							<view class="pab-item" style="background-color: #58c0b0;">{{char.value.split(",")[0]}}</view>
							<view class="pab-item" style="background-color: #4d8ae8;">{{char.value.split(",")[1]}}</view>
							<view class="pab-item" style="background-color: #d9595e;">{{char.value.split(",")[2]}}</view>
							<view class="pab-item" style="background-color: #ebb052;">{{char.value.split(",")[3]}}</view>
							<view class="pab-item" style="background-color: #bd71ff;">{{char.value.split(",")[4]}}</view>
						</view> -->
						<view class="pc-level-box">
							<img :src="memoryLevelIcons[char.rank]" alt="" class="pc-level-box-img">
						</view>
						<view class="pc-name-bg">
							<img :src="memoryCategoryAttr[char.category].bg" alt="" class="pc-name-bg-img">
						</view>
						<view class="pc-name">{{char.name}}
						</view>
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
	const toast = ref()
	import {
		getMemorysSimpleApi
	} from "@/myapi/ashEchoes.js"

	//烙痕相关
	let allMemorys = []
	const memoryPopup = ref()
	//当前选择的分类
	const memoryBoxCategoryCurrent = ref(0)
	const showSelectMemorys = ref([])

	const memoryCategorys = ["体质", "防御", "攻击", "专精", "终端"]
	const memoryLevelIcons = {
		"全等级": "https://r.qianqiu.info/app/icons/noe.png",
		"SSR": "https://r.qianqiu.info/app/icons/ssr.png",
		"SR": "https://r.qianqiu.info/app/icons/sr.png",
		"R": "https://r.qianqiu.info/app/icons/r.png",
	}
	const memoryCategoryAttr = {
		"全属性": {
			"icon": "https://r.qianqiu.info/app/icons/noe.png",
			"bg": "",
			"color": "rgba(255, 255, 255,.25)",
			"fcolor": "rgba(255, 255, 255,1)",
		},
		"体质": {
			"icon": "https://r.qianqiu.info/app/icons/health.png",
			"fcolor": '#5eb8aa',
			"bg": "https://r.qianqiu.info/app/icons/memory-tz.png",
			"bcolor": 'rgba(94, 184, 170, .25)'
		},
		"防御": {
			"icon": "https://r.qianqiu.info/app/icons/defence.png",
			"fcolor": '#4a8ace',
			"bg": "https://r.qianqiu.info/app/icons/memory-fy.png",
			"bcolor": 'rgba(74, 138, 206,.25)'
		},
		"攻击": {
			"icon": "https://r.qianqiu.info/app/icons/attack.png",
			"fcolor": '#ca7172',
			"bg": "https://r.qianqiu.info/app/icons/memory-gj.png",
			"bcolor": 'rgba(202, 113, 114,.25)'
		},
		"专精": {
			"icon": "https://r.qianqiu.info/app/icons/mastery.png",
			"fcolor": '#bbb45a',
			"bg": "https://r.qianqiu.info/app/icons/memory-zj.png",
			"bcolor": 'rgba(187, 180, 90,.25)'
		},
		"终端": {
			"icon": "https://r.qianqiu.info/app/icons/terminal.png",
			"fcolor": '#8469ad',
			"bg": "https://r.qianqiu.info/app/icons/memory-zd.png",
			"bcolor": 'rgba(132, 105, 173,.25)'
		}
	}
	//当前选中烙痕的下标
	const currentChooseMemory = ref(0)
	// 当前选中展示的烙痕
	const headMemoryCurrent = ref(0)
	// 初始化所有烙痕
	getMemorysSimpleApi({}, (r) => {
		for (let j of Object.keys(r)) {
			allMemorys.push(...r[j])
		}
		filterMemoryData()
	})
	const mlcurrent = ref("全等级")
	const mccurrent = ref("全属性")
	//筛选分类
	function handleChangeMCategory(v) {
		mccurrent.value = v
		filterMemoryData()
	}
	//筛选等级
	function handleChangeMLevel(v) {
		mlcurrent.value = v
		filterMemoryData()
	}

	function filterMemoryData() {
		let tempArr = allMemorys
		if (mccurrent.value != '全属性') {
			tempArr = allMemorys.filter(e => e.category == mccurrent.value)
		}
		if (mlcurrent.value != '全等级') {
			tempArr = tempArr.filter(e => e.rank == mlcurrent.value)
		}
		showSelectMemorys.value = tempArr
	}

	// 打开烙痕选择框框
	function openMemorySelect(index) {
		if (sharePage.value) return
		currentChooseMemory.value = index
		memoryPopup.value.open()
	}
	//烙痕选择事件
	function handleChangeMemoryCategory(index) {
		memoryBoxCategoryCurrent.value = index
		showSelectMemorys.value = allMemorys[memoryCategorys[index]]
	}
	//确认选择该烙痕
	const wikiUrl = ref("")

	function handleChooseMemory(memory) {
		wikiUrl.value = "https://wiki.biligame.com/bjhl/记忆烙痕/"+memory.name
		// uni.navigateTo({
		// 	url: '/pages/wiki/wiki-detail/memory-detail/memory-detail?name=' + memory.name
		// })
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

	.wiki-memory-body {
		position: fixed;
		width: 100%;
		max-width: 750rpx;
		left: 50%;
		transform: translateX(-50%);
		min-height: 100%;
		background-color: #e9edef;

		.wiki-memory-content {
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

		.popup-memory-box {
			position: relative;
			width: 100%;
			box-sizing: border-box;
			box-sizing: border-box;
			gap: 20rpx;

			.memory-category-box {
				position: fixed;
				left: 0;
				top: 70rpx;
				background-color: #e9edef;
				z-index: 999;

				.memory-top-box {
					position: relative;
					display: grid;
					grid-template-columns: repeat(8, 1fr);
					gap: 20rpx;
					font-size: 20rpx;
					width: 100%;
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

				.memory-top-box:last-child {
					padding-top: 0;
				}
			}

			.card-list-box {
				display: grid;
				grid-template-columns: repeat(2, 1fr);
				gap: 30rpx 20rpx;
				padding-top: 200rpx;
				padding-bottom: 100rpx;

				.card {
					position: relative;
					width: 100%;
					height: 180rpx;

					.pc-img-box {
						width: 100%;
						height: 180rpx;

						.pc-img {
							height: 180rpx;
							width: 100%;
							border-radius: 0 !important;
							background-color: white;
						}
					}
					.pc-attr-box {
						position: absolute;
						top: 0rpx;
						display: grid;
						grid-template-columns: repeat(5, 1fr);
						gap: 10rpx;
						.pab-item{
							width: 46rpx;
							height: 46rpx;
							color:white;
							display: flex;
							align-items: center;
							justify-content: center;
						}
					}

					.pc-level-box {
						position: absolute;
						z-index: 2;
						height: 55rpx;
						width: auto;
						top: -7rpx;
						right: 15rpx;

						.pc-level-box-img {
							height: 55rpx;
							width: auto;
						}
					}

					.pc-name-bg {
						position: absolute;
						bottom: -23rpx;
						right: -1rpx;
						width: 100%;
						height: 90rpx;
						z-index: 1;
						display: flex;
						overflow-x: hidden;
						overflow-y: auto;
						flex-direction: row-reverse;

						.pc-name-bg-img {
							height: 90rpx;
							object-fit: contain;
						}
					}

					.pc-name {
						position: absolute;
						display: flex;
						align-items: end;
						justify-content: start;
						font-size: 22rpx;
						letter-spacing: 1rpx;
						font-weight: 600;
						width: 100%;
						height: 50rpx;
						background-color: rgba(0, 0, 0, 0);
						z-index: 2;
						bottom: 5rpx;
						color: white;
						padding-left: 5rpx;
					}
				}
			}
		}
	}
</style>