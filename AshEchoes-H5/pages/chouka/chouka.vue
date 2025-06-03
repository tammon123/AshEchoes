<template>
	<view class="chouka-total-body" id='parant-body'>
		// #ifdef WEB
		<TopNavBar child='true'></TopNavBar>
		// #endif

		<view class='chouka-n-body' :style="{'min-height': pageHeight+'px'}">
			// #ifdef WEB
			<view class="blan">&nbsp;</view>
			// #endif
			<view class="chouka-content">
				<template v-if="isLogin">
					<view class="content-head">
						<view class="ch-top-item ch-item">
							<template v-if="pdrSort==1 && currentSortType != 5">
								<template v-if="currentSortType==1 || currentSortType==3">
									<img v-if="rankMine&& rankMine <= 10" mode='heightFix' class='cti-img'
										:src="'https://r.qianqiu.info/icons/rank/c'+(rankMine)+'.png'" alt="">
								</template>
								<template v-if="currentSortType==2 || currentSortType==4">
									<img v-if="rankMine&& (rankTotal-rankMine+1)<= 10" mode='heightFix' class='cti-img'
										:src="'https://r.qianqiu.info/icons/rank/c'+(rankTotal-rankMine+1)+'.png'"
										alt="">
								</template>
							</template>
							<template v-if="pdrSort==0 && currentSortType != 5">
								<template v-if="currentSortType==2 || currentSortType==4">
									<img v-if="rankMine&& rankMine <= 10" mode='heightFix' class='cti-img'
										:src="'https://r.qianqiu.info/icons/rank/c'+(rankMine)+'.png'" alt="">
								</template>
								<template v-if="currentSortType==1 || currentSortType==3">
									<img v-if="rankMine&& (rankTotal-rankMine+1)<= 10" mode='heightFix' class='cti-img'
										:src="'https://r.qianqiu.info/icons/rank/c'+(rankTotal-rankMine+1)+'.png'"
										alt="">
								</template>
							</template>
							<view class="ct-item-left">
								<view class="name">{{nickname || "监督"}}</view>
								<view class="uid">{{(uid&&uid.length>=13)?'小助手ID：'+uid.substring(uid.length-6):''}}
								</view>
							</view>
							<view class="ct-item-right">
								<view class="cir-btns">
									<view class="import-data-btn" @click='importData'>导入:删除</view>
								</view>
							</view>
						</view>
						<view class="ch-center-item ch-item ch-item-line">
							<view class="chi-top">
								<view class="chit-title item">角色</view>
								<view class="item value-item">{{charsInfo['0'].rate}}</view>
								<view class="item value-item">{{charsInfo['0'].avg+'抽'}}</view>
								<view class="item value-item">{{charsInfo['0'].tr}}</view>
								<view class="item value-item">{{charsInfo['0'].pj+'抽'}}</view>
								<view class="item">综合概率</view>
								<view class="item">每6星角色</view>
								<view class="item">不歪率</view>
								<view class="item">每UP角色</view>
							</view>
							<view class="chi-bottom">
								<view class="item">总抽数:</view>
								<view class="item">{{charsInfo['0'].size}}抽</view>
								<view class="item">6星:</view>
								<view class="item">{{charsInfo['0'].r6count}}个</view>
								<view class="item">|</view>
								<view class="item">UP抽数:</view>
								<view class="item">{{charsInfo['0'].r6xdtotal}}抽</view>
								<view class="item">6星:</view>
								<view class="item">{{charsInfo['0'].r6xdcount}}个</view>
							</view>
						</view>
						<view class="ch-bottom-item ch-item-line ch-item">
							<view class="chi-top">
								<view class="chit-title item">烙痕</view>
								<view class="item value-item">{{charsInfo['1'].rate}}</view>
								<view class="item value-item">{{charsInfo['1'].avg+'抽'}}</view>
								<view class="item value-item">{{charsInfo['1'].tr}}</view>
								<view class="item value-item">{{charsInfo['1'].pj+'抽'}}</view>
								<view class="item">综合概率</view>
								<view class="item">每SSR烙痕</view>
								<view class="item">不歪率</view>
								<view class="item">每UP烙痕</view>
							</view>
							<view class="chi-bottom">
								<view class="item">总抽数:</view>
								<view class="item">{{charsInfo['1'].size}}抽</view>
								<view class="item">SSR:</view>
								<view class="item">{{charsInfo['1'].r6count}}个</view>
								<view class="item">|</view>
								<view class="item">UP抽数:</view>
								<view class="item">{{charsInfo['1'].r6xdtotal}}抽</view>
								<view class="item">SSR:</view>
								<view class="item">{{charsInfo['1'].r6xdcount}}个</view>
							</view>
						</view>
					</view>
					<view class="chouka-nav">
						<view class="cn-left">
							<span class='cn-title cn-lleft'
								:style="showRanks?{}:{'borderBottomColor':'#dce1e5'}">排行榜</span>
							<span class="cn-char cn-btn" :class="showRanks&&showRankType==0?'active-cn-btn':''"
								:style="showRanks&&showRankType==0?{'borderBottom':'4rpx solid #3366fd'}:{}"
								@click='changeShowRanks(0)'>角色</span>
							<span class="cn-memory cn-btn" :class="showRanks&&showRankType==1?'active-cn-btn':''"
								:style="showRanks&&showRankType==1?{'borderBottom':'4rpx solid #3366fd'}:{}"
								@click='changeShowRanks(1)'>烙痕</span>
						</view>
						<view class="cn-right" @click='changePoolDetail'
							:style="detailPage?{'backgroundColor':'white'}:{}">
							<span class="cn-title cn-title-btn" :class="detailPage?'cn-title-change':''">
								{{detailPage?'概览页面':'总结页面'}}
							</span>
						</view>
					</view>

					<template v-if="!showRanks">
						<view class="pdr-msg">
							<p class='pdrm-top'>当前UP池为<span
									class='pdrm-notice'>{{currentChoukaType=='卡池'?'卡池':'总览'}}</span>模式,点击切换<span
									@click="changeChoukaType"
									class='pdrm-cancel'>{{currentChoukaType=='卡池'?'总览':'卡池'}}</span>模式
							</p>
							<p class='pdrm-top'>
								可<span class='pdrm-notice'>左右滑动</span>下方导航栏，<span class='pdrm-notice'>查看更多卡池</span>
							</p>
						</view>
						<view v-if="!detailPage" class="content-bottom">
							<view class="cb-tabs cb-new-tabs">
								<view
									:class="currentPoolTypeIndex==index?'cb-tab-item-active cb-tab-item':'cb-tab-item'"
									:key='index' @click="poolTypeChange(item, index)" v-for="(item, index) in poolType">
									{{item.name}}
								</view>
							</view>
							<view class='cb-content'>
								<template v-if="itemPoolGroupDataArr.length == 0">
									<view class="cb-content-item" v-for="(title,index) in Object.keys(itemPoolDataArr)"
										:key='index'>
										<view class="cci-top" v-if="false">
											{{title}}
										</view>
										<view class="cci-bottom" v-if="index==0 && poolShowInfo.dian">
											<view class="left-avatar"
												:class="isCharPool?'':'memory-avatar'">
												<img class='left-avatar-img lai-unkown'
													src="https://r.qianqiu.info/chouka/unkown.jpg" mode='heightFix'
													alt="">
											</view>
											<view class="progress-box">
												<view class="progress"
													:class="poolShowInfo.dian<=limitL?'progress-green':poolShowInfo.dian<=limitM?'progress-yellow':'progress-red'"
													:style="{width: (poolShowInfo.dian/limitH * 100 +'%')}">
													<span class='choushu'
														v-if="poolShowInfo.dian>=24">已垫{{poolShowInfo.dian}}抽</span>
												</view>
												<span class='choushu'
													:style="isCharPool?{position:'absolute',left: '100rpx'}:{position:'absolute',left: '171rpx'}"
													v-if="poolShowInfo.dian<24">已垫{{poolShowInfo.dian}}抽</span>
											</view>
										</view>
										<view class="cci-bottom" v-for="item in itemPoolDataArr[title].arr">
											<view class="left-avatar"
												:class="isCharPool?'':'memory-avatar'">
												<img class='left-avatar-img' :src="item.avatar" mode='heightFix' alt="">
											</view>
											<view class="progress-box">
												<view class="progress"
													:class="item.count<=limitL?'progress-green':item.count<=limitM?'progress-yellow':'progress-red'"
													:style="{width: (item.count/limitH * 100 +'%')}">
													<span class='choushu' v-if="item.count>=16">{{item.count}}抽</span>
												</view>
												<span class='choushu'
													:style="isCharPool?{position:'absolute',left: '100rpx'}:{position:'absolute',left: '171rpx'}"
													v-if="item.count<16">{{item.count}}抽</span>
											</view>
											<view :class='item.init?"tips":""'>{{item.init}}</view>
										</view>
									</view>
								</template>
								<template v-if="itemPoolGroupDataArr.length != 0">
									<view class="cb-content-item" v-for="(item,index) in itemPoolGroupDataArr"
										:key='index'>
										<view class="cci-top upcci-top">
											<view class="ccit-left">
												<view class="llt-top">
													{{item.info.pool}}
												</view>
												<view class="llt-bottom">
													{{item.info.time}}
												</view>
											</view>
											<view class="ccit-right">
												<span class="crt">{{item.info.total}}</span>
												<span class="crt">{{item.info.rate}}</span>
												<span class="crt">{{item.info.avg}}抽</span>
												<span class="crb">总抽数</span>
												<span class="crb">不歪率</span>
												<span class="crb">UP/抽</span>
											</view>
										</view>
										<view class="cci-bottom" v-if="item.info.dian">
											<view class="left-avatar"
												:class="isCharPool?'':'memory-avatar'">
												<img class='left-avatar-img lai-unkown'
													src="https://r.qianqiu.info/chouka/unkown.jpg" mode='heightFix'
													alt="">
											</view>
											<view class="progress-box">
												<view class="progress"
													:class="item.info.dian<=limitL?'progress-green':item.info.dian<=limitM?'progress-yellow':'progress-red'"
													:style="{width: (item.info.dian/limitH * 100 +'%')}">
													<span class='choushu'
														v-if="item.info.dian>=24">已垫{{item.info.dian}}抽</span>
												</view>
												<span class='choushu'
													:style="isCharPool?{position:'absolute',left: '100rpx'}:{position:'absolute',left: '171rpx'}"
													v-if="item.info.dian<24">已垫{{item.info.dian}}抽</span>
											</view>
										</view>
										<view class="cci-bottom" v-for="recor in item.ui">
											<view class="left-avatar"
												:class="isCharPool?'':'memory-avatar'">
												<img class='left-avatar-img' :src="recor.avatar" mode='heightFix'
													alt="">
											</view>
											<view class="progress-box">
												<view class="progress"
													:class="recor.count<=limitL?'progress-green':recor.count<=limitM?'progress-yellow':'progress-red'"
													:style="{width: (recor.count/limitH * 100 +'%')}">
													<span class='choushu' v-if="recor.count>=16">{{recor.count}}抽</span>
												</view>
												<span class='choushu'
													:style="isCharPool?{position:'absolute',left: '100rpx'}:{position:'absolute',left: '171rpx'}"
													v-if="recor.count<16">{{recor.count}}抽</span>
											</view>
											<view :class='recor.init?"tips":""'>{{recor.init}}</view>
										</view>
									</view>
								</template>
							</view>
						</view>
						<view v-if="detailPage" class="content-bottom">
							<view class="cb-tabs cb-new-tabs" style="grid-template-columns: repeat(2, 1fr);">
								<view
									:class="currentDetailTypeIndex==index?'cb-tab-item-active cb-tab-item':'cb-tab-item'"
									:key='index' @click="currentDetailTypeIndex = item.index"
									v-for="(item, index) in detailType">{{item.name}}
								</view>
							</view>
							<view class="pic-box"
								:style="currentDetailTypeIndex==1?'grid-template-columns: repeat(3, 1fr);':''">
								<template v-for="(i, index) in Object.keys(allDetailData)" :key='i'>
									<view v-if="allDetailData[i].t == currentDetailTypeIndex"
										@click="toAvatarElement(index, i)" class="pic-i-box">
										<img :class="allDetailData[i].t==0?'char-box':'memory-box'"
											:src="allDetailData[i].a" mode='heightFix' alt="">
										<view class='count-box'>{{allDetailData[i].c}}</view>
									</view>
								</template>
							</view>
							<view class='detail-content'>
								<template v-for="(i, index) in Object.keys(allDetailData)" :key='i'>
									<view :class="('cc'+index)" v-if="allDetailData[i].t == currentDetailTypeIndex"
										class="detail-card" :style="i==currentChooseDetailItem?{color:'red'}:{}">
										<view class="detail-card-name">
											{{i}}
										</view>
										<view class="detail-card-line" v-for="l in allDetailData[i].r">
											<span class="left">{{l.pool}}</span>
											<span class="right">{{l.time}}</span>
										</view>
									</view>
								</template>
							</view>
						</view>
					</template>
					<template v-if="showRanks">
						<view class="pdr-msg">
							<p class='pdrm-top'>共有<span class='pdrm-notice'>{{rankTotal}}</span>名小监督参与排行<span
									@click="cancelRank" class='pdrm-cancel'>（撤回排行）</span>
							</p>
							<template v-if="rankMine!=0">
								<p class='pdrm-top'>在<span
										class='pdrm-notice'>{{poolSortType[currentSortType].n}}</span>榜单<span
										class='pdrm-notice'>{{pdrSort==1?poolSortType[0].nn:poolSortType[0].n}}</span>中，您的排名为：<span
										class='pdrm-notice'>{{rankMine}}</span>
								</p>
							</template>
							<template v-if="rankMine==0">
								<span class='pdrm-bottom'>暂无数据，您的数据正在同步或抽卡数小于筛选值</span>
							</template>
						</view>

						<view class="content-bottom">
							<view class="title-ranks">
								<span class="left">排行榜明细</span>
								<view class="right">
									<span class="r-tips">筛选：</span>
									<uv-input inputAlign='center'
										:customStyle="{'width':'250rpx','borderBottom':'1rpx solid #3366fe !important'}"
										border="bottom" type="number" v-model="minCount">
										<template v-slot:prefix>
											<uv-text text="最低" margin="0 3px 0 0" type="tips"></uv-text>
										</template>
										<template v-slot:suffix>
											<uv-text text="抽" margin="0 3px 0 0" type="tips"></uv-text>
										</template>
									</uv-input>
								</view>
							</view>
							<view class="cb-tabs cbsr-tabs">
								<template v-for="(item, index) in poolSortType" :key='index'>
									<view
										:style="index==0?{'textDecoration':'underLine','color':'#3366fe','fontWeight':'600','fontSize':'28rpx'}:{}"
										:class="currentSortType==index?'cb-tab-item-active cb-tab-item':'cb-tab-item'"
										@click="changeSort(item, index)">
										{{index==0?pdrSort==0?item.n:item.nn:item.n}}
									</view>
								</template>
							</view>
							<view class='cbsr-content'>
								<template v-for="(item, index) in pdrValus">
									<view class="cbsr-box" :style="rankTi==item.uid?{'backgroundColor':'#a7b8e4'}:{}">
										<view class="cbrs-item">
											<template v-if="pdrSort==1 && currentSortType != 5">
												<template v-if="currentSortType==1 || currentSortType==3">
													<img v-if="index <= 9" mode='heightFix' class='chrs-item-img'
														:src="'https://r.qianqiu.info/icons/rank/c'+(index+1)+'.png'"
														alt="">
												</template>
											</template>
											<template v-if="pdrSort==0 && currentSortType != 5">
												<template v-if="currentSortType==2 || currentSortType==4">
													<img v-if="index <= 9" mode='heightFix' class='chrs-item-img'
														:src="'https://r.qianqiu.info/icons/rank/c'+(index+1)+'.png'"
														alt="">
												</template>
											</template>
											<view class="ci-item ci-index">{{index+1}}</view>
											<view class="ci-item">{{item.tavg}}</view>
											<view class="ci-item">{{item.trate}}%</view>
											<view class="ci-item">{{item.uavg}}</view>
											<view class="ci-item">{{item.utr}}%</view>
											<view class="ci-item">{{item.tcount}}</view>
										</view>
									</view>
								</template>
								<uv-load-more :loadmoreText="loadText" @loadmore='seeMore'
									:customStyle="{margin:'40rpx 0'}" color="red" lineColor="#3067fe" dashed line />
							</view>
						</view>
					</template>
				</template>
			</view>
			<uv-toast position="top" ref='toast'></uv-toast>
			<uv-popup closeable mode='top' ref="modal" customStyle="height: 900rpx;">
				<view class="slot-content">
					<uv-textarea maxlength="-1" height='400rpx' v-model="content"
						placeholder='{"ret":0,"iRet":0,"sMsg":"ok","jData":{"iRet":"0","sMsg":"ok","token":"6666666666666666666666b12f236430f4c23b08b57055384b4b2f75816ff784bc8175b7d5ecb066"},"sAmsSerial":"AMS-BJHL-1014193510-mBbXFq-666158-323819"}'></uv-textarea>
					<view class="tips">
						请检查在链接2中得到的数据是否和上方示例格式<span style='color:red'>一致</span>。
						<br>如果不一致，请在同一个环境<span style='color:red'>(手机QQ、手机微信、PC浏览器)</span>打开链接1登录，成功后打开链接2获取数据。
						<br>导入教程可点击<a href="https://mp.weixin.qq.com/s/GkHZ3tHSvitb7JU2A2hXjA">《导入教程》</a>查看
					</view>
					<view class="btns">
						<view class="btn cancelBtn" @click="cancel">取消</view>
						<view class="btn deleteBtn" @click="goShanchu">删除</view>
						<view class="btn surebtn" @click='confirm'>导入</view>
					</view>
				</view>
			</uv-popup>
			<uv-popup closeable mode='top' ref="deletee" customStyle="height: 400rpx">
				<view class="slot-content">
					<view class='delete-text'>删除所有数据并准备重新导入数据,是否要提交删除申请？</view>
					<view class="btns">
						<view class="btn cancelBtn" @click='sureDelete'>确认</view>
						<view class="btn surebtn" @click="deletec">点错了</view>
					</view>
				</view>
			</uv-popup>
			<uv-popup closeable mode='top' ref="dialog" customStyle="height: 400rpx">
				<view class="slot-content">
					<view class='delete-text'>欧非排行榜功能需上传您的部分抽卡信息才能开启，是否启用？</view>
					<view class="btns">
						<view class="btn surebtn" @click='sureRank'>我要看</view>
						<view class="btn cancelBtn" @click="noRank">点错了</view>
					</view>
				</view>
			</uv-popup>
			<uv-popup closeable mode='top' ref="cancelDialog" customStyle="height: 400rpx">
				<view class="slot-content">
					<view class='delete-text'>是否删除并隐藏我的排行记录？删除后也将无法查看他人排名</view>
					<view class="btns">
						<view class="btn surebtn" @click='sureCancelRank'>确认</view>
						<view class="btn cancelBtn" @click="noCancelRank">点错了</view>
					</view>
				</view>
			</uv-popup>
		</view>
	</view>
</template>

<script setup>
	import {
		getCurrentInstance,
		onMounted,
		ref,
		watch
	} from 'vue';
	// #ifdef WEB
	import TopNavBar from '@/components/TopNavbar/TopNavbar.vue'
	// #endif
	import {
		getPagePoolDataRankApi,
		deletePoolDataApi,
		getPoolDataUserInfoApi,
		postPoolDataApi,
		getDetailRankApi,
		agreeRankApi,
		cancelRankApi,
		getPoolAllApi,
		getNewPoolDataApi,
		getNewPoolGroupByNameDataApi
	} from '@/myapi/ashEchoes.js'

	const showRanks = ref(false)
	const currentSortType = ref(1)
	const poolSortType = [{
		n: '降序',
		nn: '升序'
	}, {
		n: '平均/抽',
		v: 'tAvg'
	}, {
		n: '综合概率',
		v: 'tRate'
	}, {
		n: 'UP/抽',
		v: 'uAvg'
	}, {
		n: 'UP不歪率',
		v: 'uTr'
	}, {
		n: '总抽数',
		v: 'tCount'
	}]
	const isLogin = ref(uni.guser.getId())
	const dialog = ref()
	const cancelDialog = ref()

	function cancelRank() {
		cancelDialog.value.open()
	}

	function sureCancelRank() {
		cancelRankApi()
		page = 1
		noMore.value = false
		loadText.value = "加载更多"
		pdrValus.value = []
		showRanks.value = false
		cancelDialog.value.close()
		allow = 0
	}

	function noCancelRank() {
		cancelDialog.value.close()
	}

	let showRankType = ref(0)

	function changeShowRanks(v) {
		if (allow == 0) {
			dialog.value.open()
		} else {
			if (showRanks.value && showRankType.value == v) {
				page = 1
				noMore.value = false
				loadText.value = "加载更多"
				pdrValus.value = []
				showRanks.value = false
				return
			}
			showRankType.value = v
			sureRank()
		}
	}

	function noRank() {
		dialog.value.close()
	}

	function sureRank() {
		if (allow == 0) {
			allow = 2
			agreeRankApi()
		}
		page = 1
		noMore.value = false
		loadText.value = "加载更多"
		pdrValus.value = []
		dialog.value.close()
		showRanks.value = true
		getPagePoolDataRank()
	}
	let beh = "tAvg"
	let pdrSort = ref(1)
	let minCount = ref(1000)
	let allow = 0

	watch(() => minCount.value, (n, o) => {
		if (n > 0) {
			page = 1
			noMore.value = false
			loadText.value = "加载更多"
			pdrValus.value = []
			dialog.value.close()
			showRanks.value = true
			getPagePoolDataRank()
		}
	})

	function changeSort(v, index) {
		if (index == 0) {
			pdrSort.value = pdrSort.value == 0 ? 1 : 0
		} else if (index == currentSortType.value) {
			return
		}
		if (index != 0) {
			currentSortType.value = index
		}
		if (v.v) {
			beh = v.v
		}
		pdrValus.value = []
		page = 1
		noMore.value = false
		loadText.value = "加载更多"
		getPagePoolDataRank()
	}

	let page = 1
	const size = 30
	const noMore = ref(false)
	const pdrValus = ref([])
	const loadText = ref("加载更多")

	function seeMore() {
		if (!noMore.value) {
			getPagePoolDataRank()
		}
	}
	const rankTotal = ref(0)
	const rankMine = ref(0)
	const rankTi = ref("")
	const rankRate = ref(0)

	function getPagePoolDataRank() {
		let data = {
			page,
			size,
			beh,
			type: showRankType.value,
			sort: pdrSort.value,
			count: minCount.value || 0
		}
		getPagePoolDataRankApi(data, (r) => {
			rankTotal.value = r.t
			rankMine.value = r.r
			rankRate.value = r.e
			rankTi.value = r.ti
			if (r['ar'].length == size) {
				page += 1
			} else {
				noMore.value = true
				loadText.value = "没有更多了~"
			}

			if (r.length != 0) {
				pdrValus.value.push(...r['ar'])
			}
		}, (e) => {
			toast.value.show({
				type: 'default',
				message: e,
				overlay: false
			})
		})
	}

	const modal = ref()
	const deletee = ref()
	const content = ref()
	const toast = ref()
	const pageHeight = ref()
	const pageWidth = ref()
	const limitL = ref(29);
	const limitM = ref(59)
	const limitH = ref(77)

	function importData() {
		modal.value.open()
	}

	function goShanchu() {
		modal.value.close()
		deletee.value.open()
	}

	function deletec() {
		deletee.value.close()
		modal.value.close()
	}

	function sureDelete() {
		modal.value.close()
		deletee.value.close()
		deletePoolDataApi(r => {
			toast.value.show({
				type: 'default',
				message: "已经提交删除申请，请等待5-10S后再重新导入数据~",
				overlay: false
			})
		}, r => {
			toast.value.show({
				type: 'default',
				message: "删除失败，请联系开发人员~",
				overlay: false
			})
		})
	}

	function cancel() {
		modal.value.close()
	}

	function confirm() {
		if (isJsonString(content.value)) {
			if (!JSON.parse(content.value).jData || !JSON.parse(content.value).jData.token) {
				toast.value.show({
					type: 'default',
					message: "数据格式有误",
					overlay: false
				})
				return
			}
			modal.value.close()
			toast.value.show({
				type: 'loading',
				title: '请等待',
				message: "正在处理导入请求~请耐心等待",
				duration: 120000,
				overlay: false
			})
			postPoolDataApi(uni.guser.getId(), JSON.parse(content.value).jData.token, () => {
				toast.value.hide()
				toast.value.show({
					type: 'default',
					message: "数据导入请求成功，正在后台处理中，请稍等1秒-60秒后，刷新或者重进页面即可",
					duration: 6000,
					overlay: false
				})
			}, (e) => {
				toast.value.show({
					type: 'default',
					message: e || "正在努力抓取数据中~请等待1-60秒",
					overlay: false
				})
			})
			modal.value.close()
		} else {
			toast.value.show({
				type: 'default',
				message: "格式错误，请重试",
				overlay: false
			})
		}
	}

	function isJsonString(str) {
		try {
			JSON.parse(str);
		} catch (e) {
			return false;
		}
		return true;
	}
	const uid = uni.guser.getId() || ""
	const nickname = uni.guser.getNickname() || "匿名小监督"
	const charsInfo = ref({
		"userinfo": {
			"nickname": "",
			"avatar": ""
		},
		"0": {
			"size": 0,
			"r6count": 0,
			"rate": "0%"
		},
		"1": {
			"size": 0,
			"r6count": 0,
			"rate": "0%"
		}
	})
	const memorysInfo = ref({})
	const dianInfo = ref({})
	const poolType = [{
		name: "定向共鸣",
		index: 0
	}, {
		name: "定向潜航",
		index: 1
	}, {
		name: "联合共鸣/阶梯卡池",
		index: 4
	}, {
		name: "常态共鸣",
		index: 2
	}, {
		name: "寻迹潜航/先觉潜航",
		index: 3
	}, {
		name: "识海甄录·消夏归航",
		index: 6
	}, ]

	const currentPoolTypeIndex = ref(0)
	const itemPoolDataArr = ref([])
	const itemPoolGroupDataArr = ref([])
	const realPoolDataArr = ref([])
	const poolShowInfo = ref({})
	const currentChoukaType = ref("卡池")

	function showAllPoolData() {
		let type = poolType[currentPoolTypeIndex.value].index
		let c = 0
		if (type == 1 || type == 5 || type == 3) {
			limitL.value = 28
			limitM.value = 39
			limitH.value = 40
			isCharPool.value = false
			c = 1
		} else {
			isCharPool.value = true
			limitL.value = 29
			limitM.value = 59
			limitH.value = 77
		}
		if (currentChoukaType.value == "卡池") {
			getNewPoolGroupByNameData(type, c)
		} else {
			getNewPoolData(type, c)
		}
	}

	function changeChoukaType() {
		if (currentChoukaType.value == "卡池") {
			currentChoukaType.value = "总览"
		} else {
			currentChoukaType.value = "卡池"
		}
		showAllPoolData()
	}

	function getNewPoolData(type, c, callback) {
		getNewPoolDataApi("/" + type, r => {
				poolShowInfo.value = r.info
				if (currentPoolTypeIndex.value <= 1) {
					const tt = realPoolDataArr.value.filter(e => (parseInt(e.type) - 1) == c).reverse()
					for (let t of tt) {
						t.arr = r.ui[t.name]
					}
					const obj = {}
					for (let index in tt) {
						obj[tt[index].name] = tt[index]
					}
					itemPoolDataArr.value = obj
				} else {
					for (let index in r.ui) {
						if (r.ui[index].length) {
							r.ui[index].arr = r.ui[index]
						}
					}
					itemPoolDataArr.value = r.ui
				}
				itemPoolGroupDataArr.value = []
			},
			e => {
				itemPoolGroupDataArr.value = []
				itemPoolDataArr.value = []
				toast.value.show({
					type: 'default',
					message: e,
					overlay: false
				})
			})
	}

	function getNewPoolGroupByNameData(type, c, callback) {
		getNewPoolGroupByNameDataApi("/" + type, r => {
				poolShowInfo.value = r.info
				if (currentPoolTypeIndex.value <= 1) {
					const tt = realPoolDataArr.value.filter(e => (parseInt(e.type) - 1) == c).reverse()
					const obj = []
					for (let index in tt) {
						let tstr = tt[index].time.split(" ~ ");
						let time = tstr[0].split(" ")[0] + " - " + tstr[1].split(" ")[0]
						let objData = {
							ui: [],
							info: {
								avg: 0,
								dian: 0,
								pool: tt[index].name,
								time: time,
								rate: 0,
								total: 0,
								win: 0
							},
							init: true
						}
						for (let pdName of Object.keys(r)) {
							if (pdName == tt[index].name) {
								r[pdName].info.time = time
								objData = r[pdName]
								obj.push(objData)
								break;
							}
						}
						if (objData.init) {
							if (tt[index].name == "玉照长夜，陈酒新酌 岁暮重明") {
								objData.info.pool = "玉照长夜 岁暮重明"
							}
							obj.push(objData)
						}
					}
					itemPoolDataArr.value = []
					itemPoolGroupDataArr.value = obj
				} else {
					for (let index in r.ui) {
						if (r.ui[index].length) {
							r.ui[index].arr = r.ui[index]
						}
					}
					itemPoolDataArr.value = r.ui
					itemPoolGroupDataArr.value = []
				}
			},
			e => {
				itemPoolGroupDataArr.value = []
				itemPoolDataArr.value = []
				toast.value.show({
					type: 'default',
					message: e,
					overlay: false
				})
			})
	}
	const isCharPool = ref(true)

	function poolTypeChange(c, index) {
		if (index == currentPoolTypeIndex.value) {
			return
		}
		currentPoolTypeIndex.value = index
		showAllPoolData()
	}
	const detailType = [{
		name: "查看所有角色",
		index: 0
	}, {
		name: "查看所有烙痕",
		index: 1
	}]

	const {
		proxy
	} = getCurrentInstance()

	const detailPage = ref(false)
	const currentDetailTypeIndex = ref(false)
	const allDetailData = ref([])
	const currentChooseDetailItem = ref("")

	function toAvatarElement(v, name) {
		uni.pageScrollTo({
			selector: ".cc" + v,
			duration: 300
		})
		currentChooseDetailItem.value = name
	}

	function changePoolDetail() {
		showRanks.value = false
		detailPage.value = !detailPage.value
		if (!detailPage.value) {
			showAllPoolData()
		} else {
			getDetailRankApi(r => {
				allDetailData.value = r
			}, e => {
				toast.value.show({
					type: 'default',
					message: e,
					overlay: false
				})
			})
		}
	}

	function divideAndRound(a, b) {
		// 首先进行除法操作
		var result = a / b;
		// 然后乘以10，进行四舍五入，再除以10得到保留一位小数的结果
		var roundedResult = Math.round(result * 10) / 10;
		return roundedResult;
	}

	function initPoolDataUserInfo() {
		getPoolDataUserInfoApi(r => {
			r[0].avg = divideAndRound(r[0].size, r[0].r6count)
			r[1].avg = divideAndRound(r[1].size, r[1].r6count)
			charsInfo.value = r
			allow = r.allow
		}, e => {
			toast.value.show({
				type: 'default',
				message: e,
				overlay: false
			})
		})
	}

	onMounted(() => {
		let id = uni.guser.getId()
		pageHeight.value = uni.getSystemInfoSync().screenHeight
		pageWidth.value = uni.getSystemInfoSync().screenWidth
		if (!id) {
			toast.value.show({
				type: 'default',
				message: "未获取到用户ID，请登录或者注册",
				overlay: false
			})
			uni.navigateTo({
				url: '/pages/index/index'
			})
		}
		getPoolAllApi(r => {
			realPoolDataArr.value = r
			initPoolDataUserInfo()
			showAllPoolData()
		})
	})
	import {
		useSystemStore
	} from "@/stores/SystemStore.js"
	// #ifdef WEB
	onMounted(() => {
		proxy.$nextTick(() => {
			let body = document.getElementById('parant-body').style
			body.height = 'calc(100% - ' + useSystemStore().height + '%)'
			body.minHeight = 'calc(100% - ' + useSystemStore().originHeight + '%)'
		})
	})
	// #endif
</script>

<style lang="scss">
	::v-deep .uv-toast__content {
		position: fixed;
		top: 200rpx !important;
	}

	.chouka-total-body {
		position: relative;
		width: 100%;
		max-width: 750rpx;
		left: 50%;
		transform: translateX(-50%);
		min-height: 100%;
		box-sizing: border-box;
	}

	.chouka-n-body {
		position: relative;
		width: 100%;
		max-width: 750rpx;
		left: 50%;
		transform: translateX(-50%);
		font-size: 26rpx;
		color: #333;
		height: 100%;
		background-color: #e9ecf1;
		padding: 0 20rpx 20rpx;
		box-sizing: border-box;
		overflow: auto;


		.slot-content {
			width: 100%;
			height: 500rpx;
			padding-top: 70rpx;

			.delete-text {
				font-size: 30rpx;
				font-weight: 600;
				color: red;
				padding: 20rpx;
				margin: 30rpx;
			}

			.tips {
				padding: 20rpx;
				font-size: 26rpx;
				line-height: 36rpx;

				.a-link {
					color: #3366fd;
				}
			}

			.btns {
				width: 100%;
				display: flex;
				align-items: center;
				justify-content: space-around;

				.btn {
					width: 150rpx;
					height: 80rpx;
					display: flex;
					align-items: center;
					justify-content: center;
					color: white;
				}

				.surebtn {
					background-color: #3366fd;
					color: white;
				}

				.cancelBtn {
					background-color: white;
					color: #3366fd;
				}

				.deleteBtn {
					background-color: black;
					color: white;
				}
			}
		}

		.chouka-content {
			position: relative;
			width: 100%;
			height: auto;
			background-color: rgba(0, 0, 0, 0);

			.chouka-nav {
				margin-top: 20rpx;
				display: flex;
				flex-direction: row;
				align-items: center;
				width: 100%;
				height: 60rpx;
				line-height: 60rpx;
				justify-content: space-between;

				.cn-left {
					width: 60%;
					background-color: #dce1e5;
					padding: 0 20rpx;
					box-sizing: border-box;
					display: flex;
					flex-direction: row;
					align-items: center;
					justify-content: center;
					height: 60rpx;

					.cn-lleft {
						height: 47rpx;
					}

					.cn-btn {
						height: 40rpx;
						line-height: 40rpx;
						margin-left: 25rpx;
						padding: 3rpx 20rpx;
						color: white;
						background-color: #3366fd;
					}

					.cn-btn:hover {
						cursor: pointer;
					}

					.active-cn-btn {
						color: #3366fd;
						background-color: white;
					}
				}

				.cn-title {
					color: black;
					font-size: 26rpx;
					font-weight: 600;
					border-bottom: 10rpx solid #3366fd;
				}

				.cn-right {
					width: 35%;
					text-align: center;
					background-color: #dce1e5;
					line-height: 60rpx;

					.cn-title-btn {
						height: 50rpx;
						line-height: 50rpx;
						color: black;
						border-bottom: 10rpx solid #dce1e5;
					}

					.cn-title-change {
						color: #3366fd;
						border-bottom: 10rpx solid #3366fd;
					}
				}

				.cn-title-btn:hover {
					cursor: pointer;
				}
			}

			.content-head {
				background-color: #dce1e5;
				// #ifdef WEB
				margin-top: 35rpx;
				// #endif
				width: 100%;
				display: flex;
				flex-direction: column;
				justify-content: space-between;
				padding: 10rpx 20rpx;
				box-sizing: border-box;

				.ch-item {
					position: relative;
					height: 120rpx;
					border-bottom: 4rpx solid #7294f3;
				}

				.ch-item:last-child {
					border-bottom: none;
				}

				.ch-top-item {
					position: relative;
					display: flex;
					flex-direction: row;
					align-items: center;
					justify-content: space-between;

					.cti-img {
						position: absolute;
						left: 30%;
						width: 110rpx;
						height: 110rpx;
						top: 0;
					}

					.ct-item-left {
						width: 40%;
						display: flex;
						flex-direction: column;
						align-items: flex-start;
						justify-content: space-between;

						.name {
							width: 250rpx;
							font-size: 36rpx;
							font-weight: 600;
							white-space: nowrap;
							overflow: hidden;
							text-overflow: ellipsis;
						}

						.uid {}
					}

					.ct-item-right {
						width: 55%;
						height: 100rpx;
						display: flex;
						flex-direction: row-reverse;
						align-items: center;
						justify-content: space-between;

						.cir-btns {
							width: 100%;
							margin-left: 20rpx;
							font-size: 22rpx;
							display: flex;
							flex-direction: row;
							justify-content: flex-end;

							.import-data-btn {
								width: 35%;
								background-color: #3366fe;
								color: white;
								font-weight: 600;
								height: 40rpx;
								padding: 10rpx;
								display: flex;
								align-items: center;
								justify-content: center;
							}

							.import-data-btn:hover {
								cursor: pointer;
							}
						}
					}
				}

				.ch-item-line {
					.item {
						white-space: nowrap;
						overflow: hidden;
						text-overflow: ellipsis;
					}

					.value-item {
						font-size: 28rpx;
						color: #396afd;
					}

					.chi-top {
						display: grid;
						grid-template-columns: .7fr repeat(4, 1fr);
						grid-template-rows: repeat(2, 1fr);
						height: calc(100% / 3 * 2);
						place-items: center;
						font-size: 24rpx;

						.item {
							font-weight: 600;
						}

						.chit-title {
							grid-row: span 2;
							font-size: 32rpx;
							justify-self: start;
						}
					}

					.chi-bottom {
						display: grid;
						grid-template-columns: repeat(9, 1fr);
						height: calc(100% / 3 - 1rpx);
						place-items: center;
						font-size: 20rpx;
					}
				}

			}

			.pdr-msg {
				margin-top: 20rpx;
				text-align: center;
				line-height: 50rpx;
				font-size: 26rpx;

				.pdrm-top {
					text-align: center;

					.pdrm-notice {
						color: red;
						font-weight: 600;
					}
				}

				.pdrm-cancel {
					font-size: 28rpx;
					font-weight: 600;
					color: #3366fd;
				}

				.pdrm-bottom {
					font-size: 24rpx;


				}
			}

			.content-bottom {
				position: relative;
				margin-top: 20rpx;
				margin-bottom: 200rpx;
				background-color: #dce1e5;
				width: auto;
				padding: 20rpx;

				.pic-box {
					width: 100%;
					padding: 20rpx;
					padding-bottom: 0;
					box-sizing: border-box;
					overflow-y: auto;
					display: grid;
					gap: 20rpx;
					grid-template-columns: repeat(5, 1fr);
					place-content: center;

					.pic-i-box {
						position: relative;
						display: flex;
						align-items: center;
						justify-content: center;
						border-radius: 10rpx;
						width: 100%;

						.count-box {
							position: absolute;
							bottom: 0;
							width: 100%;
							text-align: center;
							color: white;
							background-color: rgba(0, 0, 0, .5);
							border-bottom-left-radius: 10rpx;
							border-bottom-right-radius: 10rpx;
							padding: 2rpx 0;
							font-weight: 600;
						}

						.char-box {
							height: 130rpx;
							width: 100%;
							background-color: white;
							border-radius: 10rpx;
						}

						.memory-box {
							height: 90rpx;
							max-width: 200rpx;
							border-radius: 10rpx;
						}
					}

				}

				.cb-tabs {
					position: relative;
					display: grid;
					grid-auto-flow: column;
					place-items: center;
					overflow-x: auto;
					gap: 10rpx;
					font-size: 26rpx;
					height: 50rpx;
					color: #444;

					.cb-tab-item {
						padding: 0 10rpx;
						height: 45rpx;
						transition: all linear .1s;
						white-space: nowrap;
					}

					.cb-tab-item:hover {
						cursor: pointer;
					}

					.cb-tab-item-active {
						border-bottom: 6rpx solid #819ef1;
						font-weight: 600;
						transition: all linear .1s;
						color: black;
					}
				}

				.cb-new-tabs {
					padding: 15rpx 0;
				}

				.cbsr-tabs {
					grid-template-columns: repeat(6, 1fr);
					gap: 0;

					.cb-tab-item {
						text-align: center;
						white-space: nowrap;
						/* 强制同一行内显示所有文本 */
						overflow: hidden;
						/* 隐藏超出的内容 */
						text-overflow: ellipsis;
					}
				}

				.cbsr-content {
					margin-top: 10rpx;

					.cbsr-box {
						background-color: #e9ecf1;
						font-size: 22rpx;
						position: relative;

						.cbrs-item {
							display: grid;
							grid-template-columns: repeat(6, 1fr);
							line-height: 90rpx;
							height: 90rpx;
							position: relative;
							overflow: hidden;

							.chrs-item-img {
								position: absolute;
								top: 5rpx;
								width: 80rpx;
								height: 80rpx;
								left: 40rpx;
							}

							.ci-item {
								z-index: 1;
								display: flex;
								align-items: center;
								justify-content: center;
								text-align: center;
								white-space: nowrap;
								overflow: hidden;
								text-overflow: ellipsis;
								line-height: 60rpx;
							}

							.ci-index {
								color: #3366fd;
								font-weight: 600;
								font-size: 28rpx;
								width: 100%;
								display: flex;
								align-items: center;
								justify-content: flex-start;
								padding-left: 5rpx;
							}
						}
					}

					.cbsr-box:nth-child(even) {
						background-color: #dce1e5;
					}

				}

				.title-ranks {
					height: 60rpx;
					margin-bottom: 20rpx;
					display: flex;
					align-items: center;
					justify-content: space-between;

					.left {
						font-size: 30rpx;
						line-height: 40rpx;
						padding-left: 10rpx;
						letter-spacing: 1rpx;
						font-weight: 600;
						border-left: 10rpx solid #3366fd;
					}

					.right {
						display: flex;
						flex-direction: row;
						align-items: center;

						.r-tips {
							font-weight: 26rpx;
							font-weight: 600;
						}
					}

				}

				.detail-content {
					position: relative;

					.detail-card {
						padding: 20rpx;

						.detail-card-name {
							width: 100%;
							height: 55rpx;
							font-size: 30rpx;
							font-weight: 600;
							line-height: 55rpx;
						}

						.detail-card-line {
							display: flex;
							flex-direction: row;
							align-items: center;
							justify-content: space-between;
							line-height: 55rpx;
							font-size: 24rpx;
						}
					}

					.detail-card:nth-child(even) {
						background-color: #e9ecf1;
					}

				}

				.cb-content {
					position: relative;
					padding: 20rpx;

					.cb-content-item {
						margin-top: 10rpx;

						.cci-top {
							width: 100%;
							font-size: 26rpx;
							font-weight: 600;
							height: 50rpx;
							line-height: 50rpx;

							.ccit-left {
								width: 320rpx;
								padding: 15rpx;
								box-sizing: border-box;
								display: flex;
								flex-direction: column;
								justify-content: space-around;
								font-size: 22rpx;

								.llt-bottom {
									width: 100%;
									font-size: 20rpx;
								}
							}

							.ccit-right {
								width: 320rpx;
								display: grid;
								place-items: center;
								font-size: 22rpx;
								grid-template-rows: 1fr 1fr;
								grid-template-columns: repeat(3, 1fr);

								.crt {
									color: #3366fe;
									font-size: 24rpx;
								}
							}
						}

						.upcci-top {
							display: flex;
							align-items: center;
							justify-content: space-between;
							background-color: rgba(51, 102, 254, .1);
							padding: 10rpx 0;
							height: 70rpx;
							line-height: 40rpx;
						}

						.cci-bottom {
							height: 55rpx;
							margin-top: 10rpx;
							display: grid;
							grid-template-columns: .5fr 5fr .7fr;
							gap: 10rpx;
							place-items: center;

							.name {
								font-weight: 600;
								justify-self: start;
							}

							.left-avatar {
								border-radius: 10rpx;
								height: 55rpx;
								width: 47rpx;
								display: flex;
								align-items: center;
								justify-content: center;
								background-color: white;

								.left-avatar-img {
									border-radius: 10rpx;
									height: 55rpx;
									width: 100%;
									background-color: white;
								}

								.lai-unkown {
									scale: .7;
								}
							}

							.memory-avatar {
								width: 120rpx;

								.left-avatar-img {
									width: 120rpx;
								}

								.lai-unkown {
									width: auto;
									scale: .7;
								}
							}

							.progress-box {
								width: 100%;
								height: 100%;
								display: flex;
								flex-direction: row;
								align-items: center;
								color: white;
								font-weight: bold;
								transition: all linear .5s;
								justify-self: start;

								.progress {
									border-radius: 10rpx;
									height: 100%;
									display: flex;
									align-items: center;
									padding: 0 20rpx;
									box-sizing: border-box;
									transition: all linear .5s;
								}

								.choushu {
									max-height: 55rpx;
									white-space: nowrap;
									/* 强制同一行内显示所有文本 */
									overflow: hidden;
									/* 隐藏超出的内容 */
									text-overflow: ellipsis;
								}

								.progress-red {
									border-radius: 10rpx;
									background:
										repeating-linear-gradient(45deg,
											rgba(229, 77, 66, 0.9),
											rgba(229, 77, 66, 0.9) 20px,
											rgba(229, 77, 66, 0.8) 20px,
											rgba(229, 77, 66, 0.8) 40px);
								}

								.progress-green {
									border-radius: 10rpx;
									background:
										repeating-linear-gradient(45deg,
											rgba(55, 181, 74, 0.9),
											rgba(55, 181, 74, 0.9) 20px,
											rgba(55, 181, 74, 0.8) 20px,
											rgba(55, 181, 74, 0.8) 40px);
								}

								.progress-yellow {
									border-radius: 10rpx;
									background:
										repeating-linear-gradient(45deg,
											rgba(250, 188, 15, 0.9),
											rgba(250, 188, 15, 0.9) 20px,
											rgba(250, 188, 15, 0.7) 20px,
											rgba(250, 188, 15, 0.7) 40px);
								}
							}


							.tips {
								font-size: 26rpx;
								height: 45rpx;
								line-height: 45rpx;
								width: 45rpx;
								font-weight: 600;
								color: #e65c51;
								border: 5rpx solid #e65c51;
								border-radius: 100%;
								text-align: center;
							}
						}
					}

					.cb-content-item:first-child {
						margin-top: 0;
					}
				}
			}
		}
	}

	::v-deep .uv-index-list__letter {
		display: none !important;
	}
</style>