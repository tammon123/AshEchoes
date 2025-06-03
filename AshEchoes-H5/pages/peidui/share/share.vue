<template>
	<view class="share-body" id='parant-body'>
		// #ifdef WEB
		<TopNavBar child='true'></TopNavBar>
		// #endif
		<AeIframe @exit='wikiUrl=""' :url='wikiUrl'></AeIframe>
		<view v-if="!readyShare" class="share-content">
			<view class="share-c-top">
				<view class="sct-top">
					<view class="left-title-input">
						<uv-input :disabled="sharePage" fontSize="32rpx" :placeholderStyle="{color: 'rgba(0,0,0,.5)'}"
							:customStyle="{fontWeight:'600', backgroundColor:'rgba(0,0,0,0)'}" placeholder="请输入队伍名称"
							border="none" v-model="data.title"></uv-input>
					</view>
					<view class="right-author-input">
						<uv-input :disabled="sharePage" :customStyle="{backgroundColor:'rgba(0,0,0,0)'}"
							placeholder="请输入投稿人名称" :placeholderStyle="{color: 'rgba(0,0,0,.5)'}" border="none"
							v-model="data.nickname"></uv-input>
					</view>
				</view>
				<view class="sct-bottom">
					<uv-textarea :disabled="sharePage" :textStyle="{fontSize:'24rpx'}"
						:placeholderStyle="{color: 'rgba(0,0,0,.5)'}" :countStyle="{backgroundColor:'rgba(0,0,0,0)'}"
						:customStyle="{height: '210rpx',backgroundColor:'rgba(0,0,0,0)',padding:'5rpx','width': '670rpx','height':'170rpx','white-space': 'pre-wrap'}"
						v-model="data.remark" autoheight :count='!sharePage' maxlength="200"
						placeholder="配队说明:不超过200字"></uv-textarea>
				</view>
			</view>
			<view class="share-c-bottom">
				<view class="char">
					<view class="char-title-line">
						<view class="scb-c-title">同调者</view>
						<view v-if="!sharePage" class='chareq-title'><span>潜像编辑</span><uv-switch activeColor='#3c9cff'
								v-model="charEqEdit" size="20"></uv-switch></view>
					</view>
					<view class="char-box" :class="charEqEdit?'char-box2':''">
						<template v-for="(item, index) in chars" :key='index'>
							<view class="card-line">
								<view @longpress="charToWikiDetail(item.name)"
									@contextmenu.prevent="charToWikiDetail(item.name)" class="char-card"
									v-if="(sharePage && item.avatar) || !sharePage">
									<img @click.stop='handleDeleteMemberPre(index)' class='rab-delete-icon'
										v-if="item.avatar && !sharePage"
										src="https://r.qianqiu.info/app/icons/delete.png" mode='aspectFill' alt="">
									<img @click='openCharSelect(index)' v-if="item.avatar" :src="item.avatar" alt=""
										class="avatar">
									<img @click='openCharSelect(index)' v-else src="/static/icons/upload.png"
										mode='aspectFill' alt="点击选择队员" class="avatar">
									<view class="name"
										:style="item.element?{color: element[item.element].fcolor}:{color:'white'}">
										{{item.name?item.name:index==0?'队长':index+1+'号位'}}
									</view>
									<template v-if="!charEqEdit">
										<view class="chareq-preview-box">
											<view class="chareq-box" v-for="(chareq, eqindex) in chareqsChoose[index]">
												<img v-if="chareq.icons" :src="chareq.icons" alt="" class="avatar">
											</view>
										</view>
									</template>
								</view>
								<template v-if="charEqEdit">
									<view class="chareq-box" v-for="(chareq, eqindex) in chareqsChoose[index]">
										<view class="char-card" @longpress="chareqToWikiDetail(chareq.name)"
											@click='openChareqSelect(index, eqindex)'>
											<img @click.stop='handleDeleteChareqPre(index, eqindex)'
												class='rab-delete-icon' v-if="chareq.icons && !sharePage"
												src="https://r.qianqiu.info/app/icons/delete.png" mode='aspectFill'
												alt="">
											<img v-if="chareq.icons" :src="chareq.icons" alt="" class="avatar">
											<img v-else src="/static/icons/upload.png" mode='aspectFill' alt="点击选择潜像"
												class="avatar">
											<view class="name"
												:style="item.element?{color: element[item.element].fcolor}:{color:'white'}">
												{{chareq.name?chareq.name:eqindex==0?'1号潜像':eqindex+1+'号潜像'}}
											</view>
										</view>
									</view>
								</template>
							</view>
						</template>
					</view>
				</view>
				<view class="jidian">
					<view class="scb-j-title">基点技能</view>
					<view class="skill-box">
						<view class="skill-card" @longpress="skillToWikilDetail(item.name)"
							@contextmenu.prevent="skillToWikilDetail(item.name)" v-for="(item, index) in skills"
							:key='index' @click="openSkillSelect(index)">
							<img v-if="item.icons" :src="item.icons" alt="" mode='aspectFill' class='skill-card-img'>
							<img v-else src="/static/icons/c-skill.png" alt="" mode='aspectFill' class='skill-card-img'>
							<view class="skill-card-text">{{item.name||'请选择'}}</view>
						</view>
					</view>
				</view>
				<view class="memory">
					<view class="scb-m-title">记忆烙痕</view>
					<view class="memory-box">
						<view class="memory-card" @longpress="memoryToWikiDetail(item.name)"
							@contextmenu.prevent="memoryToWikiDetail(item.name)" v-for="(item, index) in memorys"
							:key='index' @click='openMemorySelect(index)'>
							<img v-if="item.img" :src="item.img" alt="" mode='heightFix' class='memory-card-img'>
							<img v-else src="/static/icons/plus.png" alt="" mode='heightFix' class='memory-card-img-no'>
							<view v-if="item.img" class="memory-card-level-box">
								<img :src="memoryLevelIcons[item.rank]" alt="" class="memory-card-level-box-img">
							</view>
							<view v-if="item.img" class="memory-card-bg">
								<img :src="memoryCategoryAttr[item.category].bg" alt="" class="memory-card-bg-img">
							</view>
							<view class="memory-card-text" :class="item.img?'memory-card-text-choose':''">
								{{item.name||'请选择'}}
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<view v-if="readyShare" id='pagePoster' class="ready-share share-content">
			<view class="share-c-top">
				<view class="sct-top">
					<view class="left-title-input">
						<uv-input disabled fontSize="32rpx" :placeholderStyle="{color: 'rgba(0,0,0,.5)'}"
							:customStyle="{fontWeight:'600', backgroundColor:'rgba(0,0,0,0)'}" placeholder="请输入队伍名称"
							border="none" v-model="data.title"></uv-input>
					</view>
					<view class="right-author-input">
						<uv-input disabled :customStyle="{backgroundColor:'rgba(0,0,0,0)'}" placeholder="请输入投稿人名称"
							:placeholderStyle="{color: 'rgba(0,0,0,.5)'}" border="none"
							v-model="data.nickname"></uv-input>
					</view>
				</view>
				<view class="sct-bottom">
					<view
						:style="{fontSize:'24rpx',minHeight:'70rpx',maxHeight: '210rpx',backgroundColor:'rgba(0,0,0,0)',padding:'5rpx'}">
						{{data.remark}}
					</view>
				</view>
			</view>
			<view class="share-c-bottom">
				<view class="char">
					<view class="scb-c-title">同调者</view>
					<view class="char-box">
						<template v-for="(item, index) in chars" :key='index'>
							<view class="card-line">
								<view @click='openCharSelect(index)' class="char-card" v-if="item.avatar">
									<img v-if="item.avatar" :src="item.avatar" alt="" class="avatar">
									<view class="name"
										:style="item.element?{color: element[item.element].fcolor}:{color:'white'}">
										{{item.name?item.name:index==0?'队长':index+1+'号位'}}
									</view>
									<template v-if="!charEqEdit">
										<view class="chareq-preview-box">
											<view class="chareq-box" v-for="(chareq, eqindex) in chareqsChoose[index]">
												<img v-if="chareq.icons" :src="chareq.icons" alt="" class="avatar">
											</view>
										</view>
									</template>
								</view>
							</view>
						</template>
					</view>
				</view>
				<view class="jidian">
					<view class="scb-j-title">基点技能</view>
					<view class="skill-box">
						<view class="skill-card" v-for="(item, index) in skills" :key='index'
							@click="openSkillSelect(index)">
							<img v-if="item.icons" :src="item.icons" alt="" mode='aspectFill' class='skill-card-img'>
							<img v-else src="/static/icons/c-skill.png" alt="" mode='aspectFill' class='skill-card-img'>
							<view class="skill-card-text">{{item.name||'请选择'}}</view>
						</view>
					</view>
				</view>
				<view class="memory">
					<view class="scb-m-title">记忆烙痕</view>
					<view class="memory-box">
						<view class="memory-card" v-for="(item, index) in memorys" :key='index'
							@click='openMemorySelect(index)'>
							<img v-if="item.img" :src="item.img" alt="" mode='heightFix' class='memory-card-img'>
							<img v-else src="/static/icons/plus.png" alt="" mode='heightFix' class='memory-card-img-no'>
							<view v-if="item.img" class="memory-card-level-box">
								<img :src="memoryLevelIcons[item.rank]" alt="" class="memory-card-level-box-img">
							</view>
							<view v-if="item.img" class="memory-card-bg">
								<img :src="memoryCategoryAttr[item.category].bg" alt="" class="memory-card-bg-img">
							</view>
							<view class="memory-card-text" :class="item.img?'memory-card-text-choose':''">
								{{item.name||'请选择'}}
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="share-c-title">
				<view class="share-c-title-left">
					<img src="https://r.qianqiu.info/app/logo.png" alt="" class="share-logo">
					<view>
						微信公众号：白荆小助手
					</view>
				</view>
				<ikun-qrcode width="160" height="160" unit="rpx" color="#000000" :data="qrUrl" />
			</view>
		</view>
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
					<view @longpress="charToWikiDetail(char.name)" @contextmenu.prevent="charToWikiDetail(char.name)"
						@click="handleChooseMember(char)" class="card" v-for="(char,index) in showSelectChars"
						:key='char.characterId' wx:key='char.characterId'>
						<view class='pc-img-box'>
							<img :src="char.avatar" mode='widthFix' alt="" class="pc-img">
						</view>
						<view class="pc-name" :style="{'color':element[char.element].fcolor}">{{char.name}}</view>
					</view>
				</view>
			</view>
		</uv-popup>
		<uv-popup mode="bottom" ref="spSkillPopup" custom-style="height: 1100rpx;overflow:auto;">
			<view class='search-box'>
				<uv-search shape='square' @search="searchSkill" @custom='searchSkill' margin="30rpx"
					v-model="skillQueryParam" :showAction="true" actionText="搜索" :animation="true"></uv-search>
				<view style='padding: 0 30rpx;box-sizing: border-box;color:red;font-weight: 600;letter-spacing: 1rpx;'>
					可同时输入多个搜索关键字，多个技能名称中间用逗号隔开(中英文逗号均可)。例：联动，缓释，奇迹
				</view>
			</view>
			<view class="popup-skill-box">
				<view class="card-list-box">
					<view @longpress="skillToWikilDetail(skill.name)"
						@contextmenu.prevent="skillToWikilDetail(skill.name)" @click="handleChooseSkill(skill)"
						class="card skill-card" v-for="(skill,index) in showSmallSkills" :key='skill.skillId'
						wx:key='skill.skillId'>
						<view class='pc-img-box sp-skill-img'>
							<img :src="skill.icons" mode='widthFix' alt="" class="pc-img">
						</view>
						<view class="pc-name">{{skill.name}}</view>
					</view>
				</view>
			</view>
		</uv-popup>
		<uv-popup mode="bottom" ref="chareqPopup" custom-style="height: 1100rpx;overflow:auto;">
			<view class='search-box'>
				<uv-search shape='square' @search="searchchareq" @custom='searchchareq' margin="30rpx"
					v-model="chareqQueryParam" :showAction="true" actionText="搜索" :animation="true"></uv-search>
				<view style='padding: 0 30rpx;box-sizing: border-box;color:red;font-weight: 600;letter-spacing: 1rpx;'>
					可同时输入多个搜索关键字，多个技能名称中间用逗号隔开(中英文逗号均可)。例：联动，缓释，奇迹
				</view>
			</view>
			<view class="popup-skill-box">
				<view class="card-list-box">
					<view @longpress="chareqToWikiDetail(skill.name)"
						@contextmenu.prevent="chareqToWikiDetail(skill.name)" @click="handleChoosechareq(skill)"
						class="card skill-card" v-for="(skill,index) in showChareqs" :key='skill.skillId'
						wx:key='skill.skillId'>
						<view class='pc-img-box sp-skill-img'>
							<img :src="skill.icons" mode='widthFix' alt="" class="pc-img">
						</view>
						<view class="pc-name">{{skill.name}}</view>
					</view>
				</view>
			</view>
		</uv-popup>
		<uv-popup mode="bottom" ref="memoryPopup" custom-style="height: 1100rpx;overflow:auto">
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
					<view @longpress="memoryToWikiDetail(char.name)"
						@contextmenu.prevent="memoryToWikiDetail(char.name)" @click="handleChooseMemory(char)"
						class="card" v-for="(char,index) in showSelectMemorys" :key='char.characterId'
						wx:key='char.characterId'>
						<view class='pc-img-box'>
							<img :src="char.img" mode='widthFix' alt="" class="pc-img">
						</view>
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
		</uv-popup>

		<uv-toast ref='toast'></uv-toast>
		<uv-modal :zIndex='99999' ref="modal" :title="modalData.title" :showCancelButton='true' @confirm="sureC"
			confirmText='好的' cancelText='不' :content='modalData.content'></uv-modal>
		<view class="bottom-nav-line" :style="readyShare?{'display':'none'}:{}">
			<view class="left">
				<view class="btn" @click="handleClickToSave" v-if="mine!=1">
					<img mode='aspectFill' class='save bottom-nav-line-icon' src="/static/icons/save.png" alt="">
					<span class='bottom-nav-line-text'>收藏</span>
				</view>
				<view class="btn" :qrUrl='qrUrl' :change:qrUrl='share.qrCodeComplete' @click="sharePreHandle">
					<img mode='aspectFill' class='share bottom-nav-line-icon' src="/static/icons/share.png" alt="">
					<span class='bottom-nav-line-text'>分享</span>
				</view>
				<view class="btn" @click="handleClickToLikes" v-if="mine!=1&&modify!=1&&ccardId!=0">
					<img v-if="likes==0" mode='aspectFill' class='save bottom-nav-line-icon'
						src="https://r.qianqiu.info/icons/nolikes.png" alt="">
					<img v-else mode='aspectFill' class='save bottom-nav-line-icon'
						src="https://r.qianqiu.info/icons/likes.png" alt="">
					<span class='bottom-nav-line-text' :style="likes==1?{'color':'#3067fe'}:{}">点赞</span>
				</view>
			</view>
			<view class="right">
				<view class="submit-btn btn-tg" @click="handleClickToCompute">
					<span class='btn1-name'>前往计算</span>
				</view>
				<view v-if='!sharePage' class="submit-btn " @click="handleClickTougao">
					<img mode='aspectFill' class='calc' src="/static/icons/tg.png" alt="">
					<span class='btn1-name'>{{modify==1?'修改':"投稿"}}</span>
				</view>
			</view>
		</view>
		<!-- #ifdef WEB  -->
		<view class="share-mask" @click="cancelShareImg" v-if="shareImg">
			<img class='share-img' @click.stop='' :src="shareImg" mode='aspectFill' alt="">
			<view class="share-cancel-btn" @click="cancelShareImg">取消</view>
			<view class="share-download-btn" @click.stop='downloadShareImg'>下载</view>
			<view class="share-mask-text" @click='cancelShareImg'>点击周边阴影可退出分享</view>
		</view>
		<!-- #endif -->
	</view>
</template>

<script setup>
	import {
		onLoad
	} from "@dcloudio/uni-app"
	import {
		userPostApi,
		userModifyApi,
		getcharsApi,
		getAllSmallSkillsApi,
		postCardsData,
		updateCardsData,
		getMemorysSimpleApi,
		likeCardApi,
		getShareCardsInfo
	} from "@/myapi/ashEchoes.js"
	import {
		ref,
		onMounted
	} from "vue";
	// #ifdef WEB
	import TopNavBar from '@/components/TopNavbar/TopNavbar.vue'
	const showNav = ref(false)
	const wikiUrl = ref("")

	function charToWikiDetail(name) {
		wikiUrl.value = "https://wiki.biligame.com/bjhl/同调者/" + name
	}

	function chareqToWikiDetail(name) {
		wikiUrl.value = "https://wiki.biligame.com/bjhl/相纸图鉴/" + name
	}

	function skillToWikilDetail(name) {
		wikiUrl.value = "https://wiki.biligame.com/bjhl/刻印技能/" + name
	}

	function memoryToWikiDetail(name) {
		wikiUrl.value = "https://wiki.biligame.com/bjhl/记忆烙痕/" + name
	}

	function showNavFunc(vvvv) {
		showNav.value = vvvv
	}
	// #endif
	const sharePage = ref(false)
	const modify = ref(0)
	// 是否是从自己的收藏和投稿页面过来的？
	const mine = ref(0)
	const likes = ref(0)
	const toast = ref()
	const chars = ref([{}, {}, {}, {}, {}, {}, {}, {}])
	const memorys = ref([{}, {}, {}, {}, {}, {}])
	const skills = ref([{}, {}, {}, {}])
	const ccardId = ref(0)
	onLoad(ree => {
		if (ree && ree.cardId) {
			ccardId.value = ree.cardId
			getShareCardsInfo(ree.cardId, r => {
				data.value.title = r.title
				data.value.remark = r.remark
				data.value.nickname = r.nickname
				likes.value = r.likes
				// if (ree.modify == 1) {
				data.value.nickname = r.nickname.split('-')[0]
				// }
				for (let i = 0; i < r.chars.length; i++) {
					chars.value[i] = r.chars[i]
				}
				for (let i = 0; i < r.memories.length; i++) {
					memorys.value[i] = r.memories[i]
				}
				for (let i = 0; i < r.skills.length; i++) {
					skills.value[i] = r.skills[i]
				}
				chareqsChoose.value = r.chareqs
				data.value.cardId = ree.cardId
				if (ree.mine == 1) {
					mine.value = 1
				}
				if (ree.modify == 1) {
					modify.value = 1
					sharePage.value = false
				} else {
					sharePage.value = true
				}
			}, e => {
				toast.value.show({
					type: 'default',
					message: e,
					overlay: false
				})
			})
		}
	})

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

	function handleChooseMember(v) {
		let oCharIndex = -1;
		let nCharIndex = indexCurrent.value;
		for (let i = 0; i < chars.value.length; i++) {
			// 如果当前选择的角色之前有
			if (chars.value[i].characterId == v.characterId) {
				// 判断当前选择下标是否拥有人物
				if (!chars.value[nCharIndex].characterId) {
					// 如果没有人物直接返回
					toast.value.show({
						type: 'default',
						message: "已选择相同的角色了",
						overlay: false
					})
					return
				} else {
					// 如果不一样 准备替换两者的位置
					oCharIndex = i;
					break;
				}
			}
		}
		if (oCharIndex != -1) {
			chars.value[oCharIndex] = chars.value[nCharIndex]
		}
		chars.value[nCharIndex] = v
		rcurrent.value = '全职业'
		ecurrent.value = '全元素'
		memberPopup.value.close()
	}

	//小技能相关
	const currentChooseSpSkill = ref(0)
	const spSkillPopup = ref()
	const skillQueryParam = ref('')
	const allSmallSkills = ref([])
	const showSmallSkills = ref([])

	function openSkillSelect(index) {
		if (sharePage.value) return
		currentChooseSpSkill.value = index
		spSkillPopup.value.open()
	}

	function handleChooseSkill(v) {
		for (let i = 0; i < skills.value.length; i++) {
			if (skills.value[i].name == v.name) {
				toast.value.show({
					type: 'default',
					message: "已选择相同的技能了",
					overlay: false
				})
				return
			}
		}
		skills.value[currentChooseSpSkill.value] = v

		spSkillPopup.value.close()
	}

	function searchSkill() {
		let keyWord = skillQueryParam.value.trim()
		keyWord = keyWord.replaceAll("，", ",")
		let arr = []
		for (let j of allSmallSkills.value) {
			for (let i of keyWord.split(',')) {
				if (j.name.indexOf(i) != -1) {
					arr.push(j)
				}
			}
		}
		showSmallSkills.value = arr
	}
	initAllSmallSkills()

	function initAllSmallSkills() {
		getAllSmallSkillsApi((r) => {
			
			for (let jj of r) {
				if (jj.skillId >= chareqsStart && jj.skillId <= chareqsEnd) {
					allChareqs.value.push(jj)
					showChareqs.value.push(jj)
				}
				if (jj.skillId < chareqsStart){
					allSmallSkills.value.push(jj)
					showSmallSkills.value.push(jj)
				}
			}
		}, (fail) => {
			uni.toastFail("服务器出错了~请联系开发人员~")
		})
	}

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
	function handleChooseMemory(memory) {
		for (let i of memorys.value) {
			if (i.memoryId == memory.memoryId) {
				toast.value.show({
					type: 'default',
					message: "已选择该烙痕，无法重复选择~",
					overlay: false
				})
				return
			}
		}
		memorys.value[currentChooseMemory.value] = memory
		memoryPopup.value.close()
	}
	//删除当前下标的角色
	function handleDeleteMemory(i) {
		//移除当前元素
		memorys.value[i] = {}
	}

	//潜像系统
	const charEqEdit = ref(false)
	const chareqQueryParam = ref("")
	const chareqPopup = ref()
	const allChareqs = ref([])
	const showChareqs = ref([])
	const chareqsStart = 500000
	const chareqsEnd = 600000
	const chareqsChoose = ref([
		[{
			skillId: '',
			icons: '',
			name: ''
		}, {
			skillId: '',
			icons: '',
			name: ''
		}, {
			skillId: '',
			icons: '',
			name: ''
		}],
		[{
			skillId: '',
			icons: '',
			name: ''
		}, {
			skillId: '',
			icons: '',
			name: ''
		}, {
			skillId: '',
			icons: '',
			name: ''
		}],
		[{
			skillId: '',
			icons: '',
			name: ''
		}, {
			skillId: '',
			icons: '',
			name: ''
		}, {
			skillId: '',
			icons: '',
			name: ''
		}],
		[{
			skillId: '',
			icons: '',
			name: ''
		}, {
			skillId: '',
			icons: '',
			name: ''
		}, {
			skillId: '',
			icons: '',
			name: ''
		}],
		[{
			skillId: '',
			icons: '',
			name: ''
		}, {
			skillId: '',
			icons: '',
			name: ''
		}, {
			skillId: '',
			icons: '',
			name: ''
		}],
		[{
			skillId: '',
			icons: '',
			name: ''
		}, {
			skillId: '',
			icons: '',
			name: ''
		}, {
			skillId: '',
			icons: '',
			name: ''
		}],
		[{
			skillId: '',
			icons: '',
			name: ''
		}, {
			skillId: '',
			icons: '',
			name: ''
		}, {
			skillId: '',
			icons: '',
			name: ''
		}],
		[{
			skillId: '',
			icons: '',
			name: ''
		}, {
			skillId: '',
			icons: '',
			name: ''
		}, {
			skillId: '',
			icons: '',
			name: ''
		}]
	])
	let currentChareqIndex = "0_0"

	function openChareqSelect(groupNum, index) {
		console.log(sharePage.value)
		if (sharePage.value) return

		currentChareqIndex = groupNum + "_" + index
		chareqPopup.value.open()
	}

	function handleChoosechareq(item) {
		currentChareqIndex.split("_")[0]
		chareqsChoose.value[currentChareqIndex.split("_")[0]][currentChareqIndex.split("_")[1]] = item
		chareqPopup.value.close()
	}

	function handleDeleteChareqPre(groupNum, index) {
		chareqsChoose.value[groupNum][index] = {
			skillId: '',
			icons: '',
			name: ''
		}
	}

	function searchchareq() {
		let keyWord = chareqQueryParam.value.trim()
		keyWord = keyWord.replaceAll("，", ",")
		let arr = []
		for (let j of allChareqs.value) {
			for (let i of keyWord.split(',')) {
				if (j.name.indexOf(i) != -1) {
					arr.push(j)
				}
			}
		}
		showChareqs.value = arr
	}


	//分享
	const shareImg = ref()
	const readyShare = ref(false)

	function handleClickToSave() {
		if (!uni.guser.getId()) {
			toast.value.show({
				type: 'default',
				message: "收藏功能需前往首页登录~",
				overlay: false
			})
			return
		}
		modalData.value.content = "是否确认收藏？"
		bh.value = "baocunC"
		modal.value.open()
	}

	function sureSave() {
		data.value.status = 2
		postCardDData()
	}


	function handleClickToLikes() {
		likes.value = likes.value == 0 ? 1 : 0
		likeCardApi(ccardId.value, (r) => {})
	}

	function handleClickTougao() {
		if (!uni.guser.getId()) {
			toast.value.show({
				type: 'default',
				message: "投稿功能，点击右上角登录",
				overlay: false
			})
			return
		}
		if (modify.value == 1) {
			modalData.value.content = "是否确认修改投稿？"
		} else {
			modalData.value.content = "是否确认投稿？"
		}
		bh.value = "tougaoC"
		modal.value.open()
	}

	function sureTougao() {
		data.value.status = 0
		postCardDData()
	}

	function handleClickToCompute() {
		if (data.value.cardId) {
			uni.navigateTo({
				url: "/pages/ranks/ranks?cardId=" + data.value.cardId
			})
		} else {
			data.value.status = 3
			postCardDData()
		}
	}

	const data = ref({
		title: '',
		nickname: uni.guser.getNickname(),
		remark: '',
		uid: uni.guser.getId()
	})
	const qrUrl = ref()

	function sharePreHandle() {
		if (!uni.guser.getId()) {
			toast.value.show({
				type: 'default',
				message: "分享功能，请点击右上角登录",
				overlay: false
			})
			return
		}
		data.value.status = 1
		readyShare.value = true
		postCardDData()
	}

	function postCardDData() {
		if (data.value.cardId && data.value.status == 1) {
			qrUrl.value = window.location.origin + '/pages/peidui/share/share?cardId=' + data.value.cardId
			toast.value.show({
				type: 'loading',
				title: '请等待',
				message: "生成中~请等待",
				duration: 10500
			})
			return
		}
		data.value.uid = data.value.uid || uni.guser.getId()
		let charNames = ""
		for (let j of chars.value) {
			if (j.characterId && j.characterId > 1000) {
				charNames += (j.name + ",")
			}
		}
		let memoryNames = ""
		if (data.value.nickname.indexOf('-') != -1 && data.value.status == 0) {
			toast.value.show({
				type: 'default',
				message: "投稿名称禁止-符号",
				overlay: false
			})
			return
		}
		for (let j of memorys.value) {
			if (j.name) {
				memoryNames += (j.name + ",")
			}
		}
		let sspskills = ""
		for (let j of skills.value) {
			if (j.name) {
				sspskills += (j.name + ",")
			}
		}
		let sp2skills = ""
		for (let j of chareqsChoose.value) {
			let tempSp2 = ""
			for (let i of j) {
				if (!i.skillId) {
					tempSp2 += "0_3-"
				} else {
					tempSp2 += i.skillId + "_3-"
				}
			}
			sp2skills += tempSp2.substring(0, tempSp2.length - 1) + ","
		}
		if (!data.value.title) {
			toast.value.show({
				type: 'default',
				message: "请输入队伍名称",
				overlay: false
			})
			return
		}
		if (!data.value.nickname) {
			toast.value.show({
				type: 'default',
				message: "请输入投稿人名称",
				overlay: false
			})
			return
		}
		if (!data.value.remark) {
			toast.value.show({
				type: 'default',
				message: "请输入配队说明",
				overlay: false
			})
			return
		}

		if (charNames.split(",").length < 5) {
			toast.value.show({
				type: 'default',
				message: "至少选择4个角色",
				overlay: false
			})
			return
		}

		if (memoryNames.split(",").length < 7) {
			toast.value.show({
				type: 'default',
				message: "至少选择6张烙痕",
				overlay: false
			})
			return
		}

		if (sspskills.split(",").length < 5) {
			toast.value.show({
				type: 'default',
				message: "需要选择4个基点技能~",
				overlay: false
			})
			return
		}
		if (chareqsChoose.value)
			data.value.charNames = charNames.substring(0, charNames.length - 1)
		data.value.memoryNames = memoryNames.substring(0, memoryNames.length - 1)
		data.value.spskills = sspskills.substring(0, sspskills.length - 1)
		data.value.sp2skills = sp2skills.substring(0, sp2skills.length - 1)
		if (modify.value == 1) {
			updateCardsData(data.value, r => {
				let message = "修改成功，即可在配队列表页查看~"
				toast.value.show({
					type: 'default',
					message: message,
					overlay: false
				})
			}, e => {
				toast.value.show({
					type: 'default',
					message: e + "：如有疑问请联系开发人员解决。QQ群：865686593",
					overlay: false
				})
			})
			return
		}
		postCardsData(data.value, (suc) => {
			if (readyShare.value) {
				qrUrl.value = window.location.origin + '/pages/peidui/share/share?cardId=' + suc.cardId
			} else {
				let message = "投稿成功，可前往配队列表页查看~"
				if (data.value.status == 2) {
					message = "收藏成功，可在配队列表页查看~"
				}
				toast.value.show({
					type: 'default',
					message: message,
					overlay: false
				})
				if (data.value.status == 0) {
					data.value.cardId = suc.cardId
					uni.guser.setNickname(data.value.nickname)
				}
				if (data.value.status == 3) {
					uni.navigateTo({
						url: "/pages/ranks/ranks?cardId=" + suc.cardId
					})
				}
			}
		}, (f) => {
			toast.value.show({
				type: 'default',
				message: f + "：如有疑问请联系开发人员解决。QQ群：865686593",
				overlay: false
			})
		})
	}

	function receiveShareRenderData(val) {
		var posterUrl = val.replace(/[]/g, ''); // 去除base64位中的空格
		shareImg.value = posterUrl
		toast.value.show({
			type: 'default',
			message: "已生成，请查看",
			overlay: false
		})
	}

	function cancelShareImg() {
		shareImg.value = null
		qrUrl.value = null
		readyShare.value = false
	}

	function downloadShareImg() {
		if (/Mobi|Android|iPhone/i.test(navigator.userAgent)) {
			// 当前设备是移动设备
			toast.value.show({
				type: 'default',
				message: "移动设备请长按上方图片，保存并分享~",
				overlay: false
			})
			return
		}
		// #ifdef WEB
		if (typeof(recorder) == 'object') {
			toast.value.show({
				type: 'default',
				message: "移动设备请长按上方图片，保存并分享~",
				overlay: false
			})
			return
		}
		// #endif
		//创建隐藏的可下载链接
		var downloadLink = document.createElement("a");
		downloadLink.href = shareImg.value;
		downloadLink.download = chars.value[0].name + 'share.png';

		// 触发点击
		document.body.appendChild(downloadLink);
		downloadLink.click();
		// 清理
		document.body.removeChild(downloadLink);
		toast.value.show({
			type: 'default',
			message: "分享图片下载成功~",
			overlay: false
		})
	}
	// #ifdef WEB
	window.receiveShareRenderData = receiveShareRenderData
	// #endif

	function sureC() {
		if (bh.value == 'lout') {
			dataInit()
			account.value = ""
			nickname.value = ""
			uid.value = ""
			uni.guser.loginOut()
			// #ifdef WEB
			if (typeof(recorder) == 'object') {
				recorder.logout()
			}
			// #endif
		}
		if (bh.value == 'tougaoC') {
			sureTougao()
		}
		if (bh.value == "baocunC") {
			sureSave()
		}
	}
	//登录
	const uid = ref(uni.guser.getId())
	const account = ref(uni.guser.getAccount())
	const nickname = ref(uni.guser.getNickname())
	const loginPage = ref(true)

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
			body.height = 'calc(100% - ' + useSystemStore().height + '%)'
			body.minHeight = 'calc(100% - ' + useSystemStore().originHeight + '%)'
		})
	})
</script>
// #ifdef WEB
<script module="share" lang="renderjs">
	import html2canvas from 'html2canvas'
	export default {
		methods: {
			qrCodeComplete(n, o) {
				let _this = this
				setTimeout(() => {
					_this.onClickShare()
				}, 500)
			},
			// 生成图片需要调用的方法
			onClickShare(event, ownerInstance) {
				setTimeout(() => {
					const dom = document.getElementById('pagePoster') // 需要生成图片内容的 dom 节点
					html2canvas(dom, {
						width: dom.clientWidth, //dom 原始宽度
						height: dom.clientHeight,
						scrollY: 0, // html2canvas默认绘制视图内的页面，需要把scrollY，scrollX设置为0
						scrollX: 0,
						useCORS: true, //支持跨域
						scale: 3 // 设置生成图片的像素比例，默认是1，如果生成的图片模糊的话可以开启该配置项
					}).then((canvas) => {
						// html2canvas 生成成功的图片链接需要转成 base64位的url
						receiveShareRenderData(canvas.toDataURL('image/png'))
					})
				}, 100)
			},
		}
	}
</script>
// #endif
<style scoped lang='scss'>
	$mainColor: #3067fe;

	.share-body {
		position: relative;
		box-sizing: border-box;
		width: 100%;
		max-width: 750rpx;
		left: calc(50% - 375rpx);
		background-color: #e9edef;
		overflow: auto;

		.share-content {
			position: relative;
			width: 100%;
			top: 70rpx;
			padding: 10rpx 30rpx 20rpx;
			margin-bottom: 130rpx;
			box-sizing: border-box;
			overflow: auto;

			.share-c-top {
				background-color: #dce1e5;
				border-left: 10rpx solid $mainColor;

				.sct-top {
					height: 70rpx;
					display: grid;
					grid-template-columns: 2fr 1fr;
					align-items: center;

					.left-title-input {
						white-space: nowrap;
						overflow: hidden;
						text-overflow: ellipsis;
					}

					.right-author-input {
						white-space: nowrap;
						overflow: hidden;
						text-overflow: ellipsis;
					}
				}
			}

			.share-c-bottom {
				margin-top: 20rpx;
				background-color: #dce1e5;
				padding: 10rpx 20rpx 40rpx;

				.char-title-line {
					display: flex;
					justify-content: space-between;

					.chareq-title {
						display: flex;
						align-items: center;
						font-size: 22rpx;
						letter-spacing: 1rpx;

						span {
							margin-right: 10rpx;
						}
					}
				}

				.char {
					border-bottom: 2rpx solid $mainColor;
					padding-bottom: 12rpx;

					.scb-c-title {
						color: white;
						letter-spacing: 4rpx;
						font-weight: 600;
						font-size: 24rpx;
						background-color: $mainColor;
						width: 125rpx;
						padding: 10rpx 10rpx;
						text-align: center;
					}

					.char-box {
						position: relative;
						margin-top: 10rpx;
						display: grid;
						grid-template-columns: repeat(4, 1fr);
						gap: 10rpx;

						.char-card {
							position: relative;
							width: 155rpx;
							background-color: white;

							.rab-delete-icon {
								position: absolute;
								top: 0;
								left: 0;
								width: 30rpx;
								height: 30rpx;
							}

							.avatar {
								width: 155rpx;
								height: 180rpx;
								object-fit: cover;
								display: block;
							}

							.name {
								bottom: 0%;
								left: 0;
								height: 40rpx;
								width: 100%;
								text-align: center;
								line-height: 40rpx;
								font-size: 24rpx;
								font-weight: 600;
								background-color: rgba(0, 0, 0, .7);
							}

							.chareq-preview-box {
								width: 100%;
								height: 50rpx;
								display: grid;
								grid-template-columns: repeat(3, 1fr);
								gap: 20rpx;
								padding: 10rpx;
								box-sizing: border-box;

								.avatar {
									width: 100%;
									height: auto;
								}

								.chareq-box {
									background-color: #dce1e5;
								}
							}
						}
					}

					.char-box2 {
						grid-template-columns: repeat(1, 1fr);

						.card-line {
							display: grid;
							gap: 10rpx;
							grid-template-columns: repeat(4, 1fr);
						}
					}
				}

				.jidian {
					margin-top: 10rpx;
					border-bottom: 2rpx solid $mainColor;
					padding-bottom: 12rpx;

					.scb-j-title {
						color: white;
						letter-spacing: 4rpx;
						font-weight: 600;
						font-size: 24rpx;
						background-color: $mainColor;
						width: 125rpx;
						padding: 10rpx 10rpx;
						text-align: center;
						margin-bottom: 10rpx;
					}

					.skill-box {
						display: grid;
						grid-template-columns: repeat(4, 1fr);
						gap: 10rpx;

						.skill-card {
							height: 75rpx;
							width: 155rpx;
							display: grid;
							grid-template-columns: 1.5fr 2.5fr;
							place-items: center;
							gap: 5rpx;

							.skill-card-img {
								width: 60rpx;
								height: 60rpx;
							}

							.skill-card-text {
								display: -webkit-box;
								/* 或者 display: box; 对于旧版 Firefox */
								-webkit-line-clamp: 2;
								/* 设置显示的行数 */
								-webkit-box-orient: vertical;
								overflow: hidden;
								text-overflow: ellipsis;
								font-size: 20rpx;
								font-weight: 600;
							}
						}
					}
				}

				.memory {
					margin-top: 10rpx;

					.scb-m-title {
						color: white;
						letter-spacing: 4rpx;
						font-weight: 600;
						font-size: 24rpx;
						background-color: $mainColor;
						width: 125rpx;
						padding: 10rpx 10rpx;
						text-align: center;
						margin-bottom: 10rpx;
					}

					.memory-box {
						display: grid;
						grid-template-columns: repeat(2, 1fr);
						gap: 25rpx 10rpx;

						.memory-card {
							position: relative;
							height: 145rpx;
							width: 320rpx;
							display: flex;
							flex-direction: row;
							justify-content: center;

							.memory-card-img {
								height: 145rpx;
								width: 320rpx;
								object-fit: cover;
							}

							.memory-card-img-no {
								height: 72.5rpx;
								width: 160rpx;
								object-fit: contain;
							}

							.memory-card-level-box {
								position: absolute;
								z-index: 1;
								height: 45rpx;
								width: auto;
								top: -7rpx;
								right: 10rpx;

								.memory-card-level-box-img {
									height: 45rpx;
									width: auto;
								}
							}

							.memory-card-bg {
								position: absolute;
								bottom: -23rpx;
								right: -1rpx;
								width: 100%;
								height: 70rpx;
								z-index: 1;
								display: flex;
								overflow-x: hidden;
								overflow-y: auto;
								flex-direction: row-reverse;

								.memory-card-bg-img {
									height: 70rpx;
									object-fit: contain;
								}
							}

							.memory-card-text {
								position: absolute;
								bottom: 0%;
								left: 0;
								height: 50rpx;
								width: 320rpx;
								text-align: center;
								line-height: 50rpx;
								font-size: 24rpx;
								font-weight: 600;
								z-index: 1;
								color: white;
								background-color: rgba(0, 0, 0, .7);
							}

							.memory-card-text-choose {
								background-color: rgba(0, 0, 0, 0);
								line-height: 15rpx;
								font-size: 22rpx;
								display: flex;
								align-items: end;
								justify-content: start;
								padding-left: 10rpx;
							}
						}
					}
				}
			}

			.share-c-title {
				min-height: 200rpx;
				font-size: 30rpx;
				line-height: 80rpx;
				font-weight: bold;
				color: black;
				display: flex;
				flex-direction: row;
				justify-content: space-between;
				align-items: center;
				width: 100%;

				.share-c-title-left {
					width: 360rpx;

					.share-logo {
						width: auto;
						height: 80rpx;
					}
				}

				img {
					max-width: 100%;
					max-height: 100%;
					border-radius: 20rpx;
				}
			}

		}

		.ready-share {
			position: relative;
			z-index: 9;
			background-color: #e9edef;
		}

		.popup-member-box {
			position: relative;
			width: 100%;
			padding: 20rpx;
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
						background-color: rgba(0, 0, 0, .8);
						position: absolute;
						bottom: 0;
					}
				}
			}
		}

		.search-box {
			position: fixed;
			top: -3rpx;
			height: 200rpx;
			background-color: #e9edef;
			z-index: 1;
		}

		.popup-skill-box {
			position: relative;
			width: 100%;
			padding: 20rpx;
			margin-top: 200rpx;
			box-sizing: border-box;

			.card-list-box {
				display: grid;
				grid-template-columns: repeat(5, 1fr);
				gap: 20rpx;

				.skill-card {
					width: 100%;

					.pc-img-box {
						width: 100%;

						.pc-img {
							width: 100%;
							border-radius: 0 !important;
							background-color: black;
						}
					}

					.pc-name {
						text-align: center;
					}
				}
			}
		}

		.popup-memory-box {
			position: relative;
			width: 100%;
			box-sizing: border-box;
			padding: 55rpx 20rpx 0;
			box-sizing: border-box;
			gap: 20rpx;

			.memory-category-box {
				position: fixed;
				left: 0;
				top: 0;
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
				margin-top: 170rpx;

				.card {
					position: relative;
					width: 100%;
					height: 200rpx;

					.pc-img-box {
						width: 100%;
						height: 200rpx;

						.pc-img {
							height: 200rpx;
							width: 100%;
							border-radius: 0 !important;
							background-color: white;
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

		.share-mask {
			position: fixed;
			left: 0;
			top: 0;
			width: 100%;
			height: 100%;
			display: flex;
			justify-content: center;
			z-index: 9999;
			background-color: rgba(0, 0, 0, .7);

			.share-img {
				width: 80%;
				height: 80%;
				margin-top: 10%;
				object-fit: contain;
				z-index: 100;
			}

			.share-mask-text {
				color: white;
				top: 20rpx;
				color: red;
				font-weight: 600;
				position: absolute;
				z-index: 101;
			}

			.share-cancel-btn {
				position: absolute;
				bottom: 300rpx;
				width: 160rpx;
				left: 150rpx;
				height: 80rpx;
				background-color: #dc362e;
				display: flex;
				align-items: center;
				justify-content: center;
				border-radius: 80rpx;
				color: white;
				font-size: 32rpx;
				font-weight: 600;
				z-index: 999;
			}

			.share-download-btn {
				position: absolute;
				bottom: 300rpx;
				right: 150rpx;
				width: 160rpx;
				height: 80rpx;
				background-color: #49a2fe;
				display: flex;
				align-items: center;
				justify-content: center;
				border-radius: 80rpx;
				color: white;
				font-size: 32rpx;
				font-weight: 600;
				z-index: 999;
			}
		}

		.bottom-nav-line {
			position: fixed;
			bottom: 0rpx;
			width: 100%;
			z-index: 10;
			max-width: 750rpx;
			height: 110rpx;
			background-color: rgba(220, 225, 229, .8);
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: space-between;
			padding: 15rpx 30rpx;
			box-sizing: border-box;

			.left {
				display: flex;
				flex-direction: row;
				align-items: center;

				.btn {
					display: flex;
					flex-direction: column;
					width: 100rpx;
					align-items: center;
					justify-content: center;

					.bottom-nav-line-icon {
						width: 40rpx;
						height: 40rpx;
					}

					.bottom-nav-line-text {
						margin-top: 10rpx;
						line-height: 20rpx;
						font-size: 18rpx;
						color: black;
					}
				}

			}

			.right {
				display: flex;
				flex-direction: row;
				align-items: center;

				.submit-btn {
					font-size: 30rpx;
					font-weight: 600;
					width: 200rpx;
					height: 80rpx;
					border-radius: 80rpx;
					background-color: #fee611;
					color: #000;
					display: flex;
					align-items: center;
					justify-content: center;
					margin-left: 20rpx;
					letter-spacing: 2rpx;

					.calc {
						width: 35rpx;
						height: 35rpx;
					}

					.btn1-name {
						margin-left: 15rpx;
						line-height: 80rpx;
					}
				}

				.btn-tg {
					background-color: #f7f7f7;

					.btn1-name {
						margin-left: 0rpx;
					}
				}

			}
		}
	}
</style>