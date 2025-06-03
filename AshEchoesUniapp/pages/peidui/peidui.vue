<template>
	<view class="peidui-body"  id='parant-body'>
		// #ifdef WEB
				<TopNavBar  child='true'></TopNavBar>
				// #endif
		<view v-if="!showNav" class="peidui-content">
			<view class="peidui-c-top">
				<view class="left-share pct-item" @click="changePage(0)">
					<span class='pcts-text' :class="currentPage==0?'pcts-text-active':''">玩家配队分享</span>
				</view>
				<view class="right-mine pct-item" @click="changePage(2)">
					<span class='pcts-text' :class="currentPage==2?'pcts-text-active':''">我的收藏/投稿</span>
				</view>
			</view>
			<view class="peidui-c-choose">
				<view class="char-choose-text" @click='cancelShaixuan'
					:style="chooseChar.name?{color: 'red',textDecoration:'underline'}:{}">
					<span>{{chooseChar.name?'不筛选':'筛选：'}}</span>
				</view>
				<view class="char-choose-btn" @click="memberPopup.open()">
					<span>{{chooseChar.name?chooseChar.name:'角色'}}</span>
					<img src="https://r.qianqiu.info/icons/xia.png" alt="" class="more-img">
				</view>
				<view class="" @click='agreeShaixuan'>
					排序方式：<span style="color: red;text-decoration: underline;font-weight: 600;">{{sortAgree?'点赞':'时间'}}</span>
				</view>
			</view>
			<view class="peidui-c-bottom">
				<view class="cb-item" v-for="(item, index) in cards" :key='index' @click='toDesc(item.cardId, item)'>
					<view class="pdcbl-top">
						<view class="pdcblt-left">{{item.title}}</view>
						<view class="pdcblt-right">
							<view class="pr-img-box" v-for="(i,j) in item.charIcons">
								<img class="pr-img-box-img" :src='i' mode='aspectFill' alt="">
							</view>
						</view>
					</view>
					<view class="pdcbl-center">
						<p class='pdc-c-line'>说明：<span class='bold desc'>{{item.remark}}</span>
						</p>
						<p class='pdc-c-line'>角色：<span class='bold char' v-for="(i, j) in item.charNames.split(',')"
								:style="{'color':chooseChar.name==i?'red':''}">{{i+" "}}</span></p>
						<p class='pdc-c-line'>烙痕：<span class='bold memory'
								v-for="(i, j) in item.memoryNames.split(',')">{{i+" "}}</span>
						</p>
						<p class="pdc-c-line author-info">
							投稿：<span class='bold autor'>{{item.nickname}}</span>
						</p>
						<p class="pdc-c-line author-info">
							时间：<span class='bold autor'>{{item.createTime}}</span>
						</p>
						<view class="card-likes">
							<img src="https://r.qianqiu.info/icons/likes.png" alt="">
							<span>{{item.likes}}</span>
						</view>
					</view>
					<view class="btns-boxx">
						<view @click.stop='toModifyCards(item.cardId)' class="cb-item-delete"
							v-if="item.status==0&&item.modify">
							修改投稿
						</view>
						<view @click.stop='deletePreHandle(item, index)'
							:style="item.status==0?{backgroundColor:'white',color:'#3067fe'}:{}" class="cb-item-delete"
							v-if="currentPage == 2">
							{{item.status==0&&item.modify?'删除投稿':'删除收藏'}}
						</view>
					</view>
				</view>
				<uv-load-more :loadmoreText="loadText" @loadmore='seeMore' :customStyle="{margin:'40rpx 0'}" color="red"
					lineColor="#3067fe" dashed line />
			</view>
		</view>
		<view @click='toShareE' class="add-btn">
			<img class='add-img' src="/static/icons/plus.png" alt="">
		</view>
		<uv-toast ref='toast'></uv-toast>
		<uv-modal :zIndex='99999' ref="modal" :title="modalData.title" :showCancelButton='true' @confirm="sureC"
			confirmText='好的' cancelText='不' :content='modalData.content'></uv-modal>
		<uv-popup mode="bottom" ref="memberPopup"
			custom-style="height: 1100rpx;overflow:auto;background-color:#e9edef;">
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
						:key='char.characterId' wx:key='char.characterId'>
						<view class='pc-img-box'>
							<img :src="char.avatar" mode='widthFix' alt="" class="pc-img">
						</view>
						<view class="pc-name" :style="{'color':element[char.element].fcolor}">{{char.name}}</view>
					</view>
				</view>
			</view>
		</uv-popup>
	</view>
</template>

<script setup>
	import {
		getPageCards,
		deleteCards,
		userPostApi,
		userModifyApi,
		getcharsApi
	} from "@/myapi/ashEchoes.js"
	import {
		onMounted,
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
	const currentPage = ref(0)
	const cards = ref([])
	const sortAgree = ref(false)

	function changePage(index) {
		if (currentPage.value == index) return
		if (!uni.guser.getId()) {
			toast.value.show({
				type: 'default',
				message: "请先在右上角登录~",
				overlay: false
			})
			return
		}
		page = 1
		currentPage.value = index
		cards.value = []
		initShowPage()
	}

	function toModifyCards(v) {
		uni.navigateTo({
			url: "/pages/peidui/share/share?cardId=" + v + "&modify=1&mine=1"
		})
	}

	function toDesc(v, item) {
		let url = "/pages/peidui/share/share?cardId=" + v
		if (currentPage.value == 2) {
			url += "&mine=1"
			if (item && item.status == 0 && item.modify) {
				url += "&modify=1"
			}
		}
		uni.navigateTo({
			url: url
		})
	}

	function toShareE() {
		if (!uni.guser.getId()) {
			toast.value.show({
				type: 'default',
				message: "请先点击右上角登录~",
				overlay: false
			})
			return
		}
		uni.navigateTo({
			url: "/pages/peidui/share/share"
		})
	}

	function seeMore() {
		if ("没有了" == loadText.value) {
			toast.value.show({
				type: 'default',
				message: "没有更多内容了",
				overlay: false
			})
			return
		}
		initShowPage()
	}
	//列表加载
	let onlyTg = ref(false)
	let page = 1
	const size = 6
	const loadText = ref("点击加载")
	// 初始化展示数据
	function initShowPage() {
		let param = page + "/" + size + "?keyWord=" + chooseChar.value.name +"&agree="+sortAgree.value
		if (currentPage.value == 2) {
			param += "&uid=" + uni.guser.getId()
		}
		if (onlyTg.value || currentPage.value != 2) {
			param += "&status=" + currentPage.value
		}

		getPageCards(param, r => {
			if (r.length >= 1) {
				cards.value.push(...r)
			}
			if (r.length != size) {
				loadText.value = "没有了"
			} else {
				loadText.value = "查看更多"
				page += 1
			}
		}, e => {
			toast.value.show({
				type: 'default',
				message: e,
				overlay: false
			})
		})
	}
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
	const ecurrent = ref("全元素")
	const rcurrent = ref("全职业")
	const rankCurrent = ref(0)
	import {
		usePeiduiStore
	} from "@/stores/PeiduiStore.js"
	const chooseChar = ref({
		name: usePeiduiStore().chooseCharacter
	})
	
	function cancelShaixuan() {
		if (chooseChar.value.name) {
			page = 1
			chooseChar.value = {
				name: ""
			}
			usePeiduiStore().$patch({
				chooseCharacter: ''
			})
			cards.value = []
			initShowPage()
		}
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
	function agreeShaixuan() {
		sortAgree.value = !sortAgree.value
		cards.value = []
		page = 1
		initShowPage()
	}
	function handleChooseMember(v) {
		chooseChar.value = v
		usePeiduiStore().$patch({
			chooseCharacter: v.name
		})
		rcurrent.value = '全职业'
		ecurrent.value = '全元素'
		memberPopup.value.close()
		cards.value = []
		page = 1
		initShowPage()
	}

	onMounted(() => {
		initShowPage()
	})
	let deleteItem = {}

	function deletePreHandle(item, index) {
		modalData.value.content = "是否删除该条数据？"
		bh.value = "delete"
		modal.value.open()
		deleteItem = item
		deleteItem.index = index
	}

	function sureDelete() {
		deleteCards(deleteItem.cardId + "/" + uni.guser.getId(), r => {
			toast.value.show({
				type: 'default',
				message: "删除成功",
				overlay: false
			})
			cards.value.splice(deleteItem.index, 1)
		}, e => {
			toast.value.show({
				type: 'default',
				message: "删除失败：" + e,
				overlay: false
			})
		})

	}

	function sureC() {
		if (bh.value == 'delete') {
			sureDelete()
		}
	}
	//登录
	//登录
	const nickname = ref(uni.guser.getNickname())
	
	// 开启登录窗口
	const modal = ref()
	const popup = ref()
	const bh = ref('lout')
	const modalData = ref({
		title: "温馨提示",
		content: '是否退出登录？'
	})
	import {
		useSystemStore
	} from "@/stores/SystemStore.js"
	import {
		getCurrentInstance
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

<style scoped lang='scss'>
	$mainColor: #3067fe;

	.peidui-body {
		position: fixed;
		width: 100%;
		max-width: 750rpx;
		left: 50%;
		transform: translateX(-50%);
		min-height: 100%;
		background-color: #e9edef;

		.peidui-content {
			position: fixed;
			top: 0rpx;
			// #ifdef WEB
			top: 70rpx;
			// #endif
			left: 0;
			width: 100%;
			height: 100%;
			// #ifdef WEB
			height: calc(100% - 70rpx);
			// #endif
			padding: 10rpx 30rpx 20rpx;
			box-sizing: border-box;
			overflow: auto;

			.peidui-c-top {
				display: flex;
				flex-direction: row;
				justify-content: space-between;
				height: 80rpx;

				.pct-item {
					width: 49%;
					height: 80rpx;
					background-color: #dce1e5;
					display: flex;
					align-items: center;
					justify-content: center;

					.pcts-text {
						font-size: 34rpx;
						line-height: 60rpx;
						font-weight: 600;
						color: black;
						letter-spacing: 1rpx;
					}

					.pcts-text-active {
						border-bottom: 8rpx solid $mainColor;
					}
				}
			}

			.peidui-c-choose {
				position: relative;
				height: 80rpx;
				width: 690rpx;
				display: flex;
				font-size: 26rpx;
				align-items: center;

				.char-choose-text {
					width: 100rpx;
					line-height: 80rpx;
				}

				.char-choose-btn {
					height: 50rpx;
					margin-right: 15rpx;
					border-radius: 5rpx;
					padding: 0 20rpx;
					background-color: white;
					display: flex;
					align-items: center;
					justify-content: center;
					min-width: 100rpx;
					box-shadow: -2rpx -2rpx 2rpx 3rpx black inset;

					.more-img {
						margin-left: 5rpx;
						margin-top: 5rpx;
						height: 25rpx;
						width: auto;
					}
				}
			}

			.peidui-c-bottom {
				position: absolute;
				top: 175rpx;
				width: 690rpx;
				height: calc(100% - 210rpx);
				background-color: #dce1e5;
				padding: 0 20rpx;
				box-sizing: border-box;
				overflow: auto;

				.cb-item {
					position: relative;
					border-bottom: 4rpx solid $mainColor;
					padding: 20rpx 0;

					.pdcbl-top {
						display: flex;
						flex-direction: row;
						align-items: center;
						justify-content: space-between;
						height: 60rpx;
						width: 100%;

						.pdcblt-left {
							font-weight: 600;
							line-height: 60rpx;
							font-size: 32rpx;
							white-space: nowrap;
							/* 强制同一行内显示所有文本 */
							overflow: hidden;
							/* 隐藏超出的内容 */
							text-overflow: ellipsis;
							width: 300rpx;
						}

						.pdcblt-right {
							flex-direction: row;
							align-items: center;
							justify-content: space-between;
							height: 60rpx;

							.pr-img-box {
								display: inline-block;
								width: 60rpx;
								height: 60rpx;
								border-radius: 60rpx;
								background-color: white;
								margin-left: 10rpx;

								.pr-img-box-img {
									width: 60rpx;
									height: 60rpx;
									border-radius: 60rpx;
								}

							}
						}
					}

					.pdcbl-center {
						position: relative;
						.card-likes{
							position: absolute;
							right: 0;
							bottom: 0;
							display: flex;
							flex-direction: row;
							align-items: center;
							justify-content: center;
							color: $mainColor;
							font-size: 28rpx;
							font-weight: 600;
							img{
								height: 40rpx;
								width: 40rpx;
								margin-right: 10rpx;
							}
						}
						.pdc-c-line {
							position: relative;
							line-height: 50rpx;
							font-size: 24rpx;
							white-space: nowrap;
							/* 强制同一行内显示所有文本 */
							overflow: hidden;
							/* 隐藏超出的内容 */
							text-overflow: ellipsis;

							/* 当发生溢出时显示省略标记(...) */
							.bold {
								font-weight: 600;
							}

							.autor {
								color: $mainColor
							}
							.create-time {
								position: absolute;
								right: 0;
								top: 0;
							}
						}
					}

					.btns-boxx {
						position: absolute;
						right: 0;
						bottom: 20rpx;
						display: flex;
						flex-direction: row;

						.cb-item-delete {
							margin-left: 20rpx;
							background-color: $mainColor;
							padding: 5rpx 15rpx;
							height: 40rpx;
							line-height: 40rpx;
							text-align: center;
							color: white;
							font-weight: 600;
							font-size: 22rpx;
						}

						.cb-item-delete:hover {
							cursor: pointer;
						}
					}

				}
			}
		}

		.popup-member-box {
			position: relative;
			width: 100%;
			padding: 20rpx;
			box-sizing: border-box;
			padding: 20rpx;

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
				top: 0;
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
				display: grid;
				grid-template-columns: repeat(3, 1fr);
				gap: 20rpx;
				margin-top: 190rpx;

				.card {
					position: relative;
					width: 100%;
					height: 270rpx;

					.pc-img-box {
						width: 100%;
						height: 270rpx;

						.pc-img {
							height: 270rpx;
							width: 100%;
							border-radius: 0 !important;
							background-color: white;
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

		.add-btn {
			position: fixed;
			bottom: 40rpx;
			right: 40rpx;
			background-color: $mainColor;
			display: flex;
			align-items: center;
			justify-content: center;
			border-radius: 100rpx;
			width: 100rpx;
			height: 100rpx;
			box-shadow: 5rpx 5rpx 15rpx 15rpx rgba(0, 0, 0, .1);

			.add-img {
				width: 50rpx;
				height: 50rpx;
				border-radius: 50rpx;
			}
		}

		.add-btn:hover {
			cursor: pointer;
		}

	}
</style>