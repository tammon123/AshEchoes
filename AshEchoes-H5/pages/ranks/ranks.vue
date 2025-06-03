<template>
	<view class='ranks-body' id='parant-body'>
		<view class="ranks-content">
			<view class="mask-icons" v-if="members[headMemberCurrent]">
				<img class='mask-icon' v-if="members[headMemberCurrent].element"
					:src="element[members[headMemberCurrent].element].icon" mode='aspectFill' alt="">
				<img class='mask-icon' v-if="members[headMemberCurrent].role"
					:src="roleIcons[members[headMemberCurrent].role]" mode='aspectFill' alt="">
			</view>
			<!-- 预加载 -->
			<img style="visibility: hidden;width: 0;height: 0;position: absolute;left: 0;" v-for="item of element"
				:src="item.icon" alt="">
			<view class="ranks-content-head">
				<view @click='handleOpenMemberBox(headMemberCurrent)' v-if='members[headMemberCurrent].pic'
					class="left-character-img">
					<img class='lc-img' mode="heightFix" :src="members[headMemberCurrent].pic" alt="">
				</view>
				<view v-else @click='handleOpenMemberBox(headMemberCurrent)' class="left-character-desc">
					<img class='lcd-img' mode='aspectFill' src="/static/icons/plus.png" alt="">
				</view>

				<view v-if='members[headMemberCurrent]' class="right-character-desc">
					<view class="rcd-title">
						<view @click='members[headMemberCurrent].pageCurrent=index'
							v-for="(i, index) of ['基础信息','角色增益']" :key='index'
							:class="members[headMemberCurrent].pageCurrent==index?'item item-active':'item'">
							<span>{{i}}</span>
						</view>
					</view>
					<view class="rcd-content" v-if="members[headMemberCurrent]">
						<view v-if="members[headMemberCurrent].pageCurrent==0" class="rcdc-content rcdc-content0">
							<view class="rcdc-left-content">
								<view class="line line-notice">
									<view class="left-line">{{members[headMemberCurrent].name}}</view>
								</view>
								<view class="line">
									<view class="left-line">{{members[headMemberCurrent].rank}}</view>
									<view class="right-line">{{members[headMemberCurrent].role}}</view>
								</view>
								<view class='rcdc-data-lines'>
									<view class='line-content' style="color: #d17a7b">
										<img mode='aspectFill' class='rc2lc-img'
											src="https://r.qianqiu.info/app/icons/attack.png" alt="">
										<span>{{members[headMemberCurrent].attack}}</span>
									</view>
									<view class='line-content' style='color: #bbb45a'>
										<img mode='aspectFill' class='rc2lc-img'
											src="https://r.qianqiu.info/app/icons/mastery.png" alt="">
										<span>{{members[headMemberCurrent].mastery}}</span>
									</view>
									<view class='line-content' style="color:#5eb8aa">
										<img mode='aspectFill' class='rc2lc-img'
											src="https://r.qianqiu.info/app/icons/health.png" alt="">
										<span>{{members[headMemberCurrent].health}}</span>
									</view>
								</view>

							</view>
							<view class="rcdc-right-content">
								<view class='line'>
									开花：
									<uv-input border="bottom"
										:customStyle="{'padding': 0,'height':'40rpx','width':'150rpx'}"
										style="padding: 0 !important;height:40rpx;width:100rpx" color='red'
										inputAlign="center" class='head-flower-item' type='number'
										v-model="members[headMemberCurrent].flower">
										<template v-slot:suffix>花</template>
									</uv-input>
								</view>
								<view class='line'>
									潜能：
									<uv-input border="bottom"
										:customStyle="{'padding': 0,'height':'40rpx','width':'150rpx','borderColor':'red'}"
										style="padding: 0 !important;height:40rpx;width:100rpx" color='red'
										inputAlign="center" class='head-flower-item' type='number'
										v-model="members[headMemberCurrent].plevel">
										<template v-slot:suffix>级</template>
									</uv-input>
								</view>
								<view class="leader-skill-card" v-if="headMemberCurrent == 0">
									<template v-for="(skill, index) in members[0].leaderSkills" :key='skill.skillId'>
										<view @longpress="skillToWikilDetail(skill.name)"
											@contextmenu.prevent="skillToWikilDetail(skill.name)" class="skill"
											v-if="!skill.hidden">
											<img v-if="skill.icons" :src="skill.icons" alt="技能小图标" class="skill-icon">
											<img v-else src="/static/icons/c-skill.png" alt="技能小图标" class="skill-icon">
											<view class="skill-info">
												<view class="name">
													{{skill.name}}
												</view>
												<view class="desc">
													<view @click="handleLeaderSkillLevel(skill, 1)"
														:class="skill.level==1?'lv val':'lv'">
														1
													</view>
													<view @click="handleLeaderSkillLevel(skill, 2)"
														:class="skill.level==2?'lv val':'lv'">
														2
													</view>
													<view @click="handleLeaderSkillLevel(skill, 3)"
														:class="skill.level==3?'lv val':'lv'">
														3
													</view>
													<view @click="handleLeaderSkillLevel(skill, 0)"
														:class="skill.level==0?'lv val':'lv'">
														0
													</view>
												</view>
											</view>
										</view>
									</template>
								</view>
							</view>
						</view>
						<view v-if="members[headMemberCurrent].pageCurrent==1" class="rcdc-content rcdc-content1">
							<view class='rcdc-content1-lines'
								v-for="(i, index) in members[headMemberCurrent].selfSkills">
								<view class="line line-notice">
									<view class="left-line">{{i.name}}</view>
								</view>
								<view class='line'>
									<p>范围：<span style="color: #3c98f8;">{{i.scoped}}</span></p>
								</view>
								<view class='line'>
									<p>数值：<span style="color:red">{{i.vald}}</span></p>
								</view>
								<view class='line'>
									<p>乘区：<span style="font-weight: 600;color:#3467ff;">{{i.damaged}}</span>
									</p>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="ranks-content-area">
				<view class="title-line-nav">队员设置</view>
				<view class="ranks-content-friend">
					<view class="fcf-line1">
						<view class="friend-card" @longpress="charToWikiDetail(member.name)"
							@contextmenu.prevent="charToWikiDetail(member.name)" v-for="(member, index) in members"
							:key='member.characterId'>
							<view class='rcfc-avatar-box'>
								<img @click.stop='handleDeleteMemberPre(index)' class='rab-delete-icon'
									v-if="members[index].characterId && index==headMemberCurrent"
									src="https://r.qianqiu.info/app/icons/delete.png" mode='aspectFill' alt="">
								<img @click="handleOpenMemberBox(index)" v-if="member.avatar" mode='aspectFill'
									class="avatar" :src='member.avatar'></img>
								<img @click="handleOpenMemberBox(index)" v-else src="/static/icons/upload.png"
									mode='aspectFill' alt="点击选择队员" class="avatar">
							</view>
							<view @click="handleOpenMemberBox(index)" v-if="headMemberCurrent==index" class="name">
								{{member.name?member.name:'选择'}}
							</view>

							<view @click="handleOpenMemberBox(index)" v-else class="name">
								{{member.name||'待选择'}}
							</view>

							<view v-if="headMemberCurrent==index" class="name1">
								当前选中
							</view>
							<view v-else class="chareq-preview-box">
								<view class="chareq-box" v-for="(chareq, eqindex) in chareqsChoose[index]">
									<img v-if="chareq.icons" :src="chareq.icons" alt="" class="avatar">
								</view>
							</view>
						</view>
					</view>
					<view class="fcf-line2">
						<view class="chareq-box" v-for="(chareq, eqindex) in chareqsChoose[headMemberCurrent]">
							<view class="char-card" @longpress="chareqToWikiDetail(chareq.name)"
								@click='openChareqSelect(eqindex)'>
								<img @click.stop='handleDeleteChareqPre(eqindex)' class='rab-delete-icon'
									v-if="chareq.icons" src="https://r.qianqiu.info/app/icons/delete.png"
									mode='aspectFill' alt="">
								<img v-if="chareq.icons" :src="chareq.icons" alt="" class="avatar">
								<img v-else src="/static/icons/upload.png" mode='aspectFill' alt="点击选择潜像"
									class="avatar">
								<view class="name">
									{{chareq.name?chareq.name:eqindex==0?'1号潜像':eqindex+1+'号潜像'}}
								</view>
							</view>
						</view>
					</view>
				</view>
				<view class="title-line-nav" style="margin-top: 20rpx;">基点技能</view>
				<view class="rcib-sp-skill-box">
					<view @longpress="skillToWikilDetail(skill.name)"
						@contextmenu.prevent="skillToWikilDetail(skill.name)" class="rcib-sp-skill"
						v-for="(skill, index) in spSkills" :key="index">
						<img @click='handleOpenSpSkillBox(index)' v-if="skill.icons" :src="skill.icons" alt="技能小图标"
							class="skill-icon">
						<img @click='handleOpenSpSkillBox(index)' v-else src="/static/icons/c-skill.png" alt="技能小图标"
							class="skill-icon">
						<view class="skill-info">
							<view class="name" @click='handleOpenSpSkillBox(index)'
								:style="skill.name?{}:{'color': '#3a91ec','text-decoration':'underline'}">
								{{skill.name||'选择传承技能'}}
							</view>
							<view class="desc">
								<view class="lv-desc">Lv:</view>
								<view @click="handleChangeSpSkillLevel(skill, 1)" :class="skill.level==1?'lv val':'lv'">
									1
								</view>
								<view @click="handleChangeSpSkillLevel(skill, 2)" :class="skill.level==2?'lv val':'lv'">
									2
								</view>
								<view @click="handleChangeSpSkillLevel(skill, 3)" :class="skill.level==3?'lv val':'lv'">
									3
								</view>
								<view @click="handleChangeSpSkillLevel(skill, 0)" :class="skill.level==0?'lv val':'lv'">
									0
								</view>
							</view>
						</view>
					</view>
				</view>
				<view class="title-line-nav" style="margin-top: 20rpx;margin-bottom: 0;">烙痕设置</view>
				<view class="ranks-content-memory">
					<view class="rcm-memory-cards-box">
						<view class="rcm-memory-card" @longpress="memoryToWikiDetail(memory.name)"
							@contextmenu.prevent="memoryToWikiDetail(memory.name)" v-for="(memory, index) in memorys"
							:key="memory.memoryId">
							<view v-if="headMemoryCurrent==index" @click='handleOpenMemoryBox(index)'
								class="memory-choose-mask">
								<view class="choose-text">当前选中</view>
							</view>
							<view class='rcm-mc-box' @click='handleOpenMemoryBox(index)'>
								<img v-if='memory.img' class='rmc-mb-icon rmc-mb-icon-level' mode='aspectFill'
									:src="memoryLevelIcons[memory.rank]" alt="">
								<view v-if='memory.img' class="memory-card-bg">
									<img :src="memoryCategoryAttr[memory.category].bg" mode='heightFix' alt=""
										class="memory-card-bg-img">
								</view>
								<img v-if='memory.img' class='rcm-memory-img' mode='aspectFill' :src="memory.img"
									alt="">
								<img @click.stop='handleDeleteMemoryPre(index)' class='rcm-delete-icon'
									v-if="memory.memoryId && index==headMemoryCurrent"
									src="https://r.qianqiu.info/app/icons/delete.png" mode='aspectFill' alt="">
								<img v-if="!memory.memoryId" class='rcm-memory-no-img' mode='aspectFill'
									src="/static/icons/plus.png" alt="">
								<span v-if='memory.img' class='rcm-memory-name'>{{memory.name}}</span>
								<span v-else class='rcm-memory-name1'>选择烙痕</span>
							</view>
						</view>
					</view>
					<view class="rcm-memory-content" v-if="memorys[headMemoryCurrent].category">
						<view class="rcm-memory-skills" v-if="memorys[headMemoryCurrent].skills">
							<view class="rcm-memory-skill-card">
								<template v-for="(skill, index) in memorys[headMemoryCurrent].skills"
									:key="skill.skillId">
									<view @longpress="skillToWikilDetail(skill.name)"
										@contextmenu.prevent="skillToWikilDetail(skill.name)" class="skill"
										v-if="!skill.hidden">
										<img v-if="skill.icons" :src="skill.icons" alt="技能小图标" class="skill-icon">
										<img v-else src="/static/icons/c-skill.png" alt="技能小图标" class="skill-icon">
										<view class="skill-info">
											<view class="name">{{skill.name}}</view>
											<view class="desc">
												<view class="lv-desc">Lv:</view>
												<view @click="handleChangeSkillLevel(skill, 1)"
													:class="skill.level==1?'lv val':'lv'">1</view>
												<view @click="handleChangeSkillLevel(skill, 2)"
													:class="skill.level==2?'lv val':'lv'">2</view>
												<view @click="handleChangeSkillLevel(skill, 3)"
													:class="skill.level==3?'lv val':'lv'">3</view>
												<view @click="handleChangeSkillLevel(skill, 0)"
													:class="skill.level==0?'lv val':'lv'">0</view>
											</view>
										</view>
									</view>
								</template>
							</view>
						</view>
					</view>
				</view>
				<view @click='expandShowInputBox=!expandShowInputBox' class="parent-line-title">
					<view class="title-line-nav" style="margin-bottom: 0;">数据填写</view>
					<img mode='aspectFill' class='line-title-icon' src="/static/icons/xia.png" alt="">
				</view>
				<view v-if='expandShowInputBox' style="text-align: center;margin-top: 20rpx;color: #3467ff;">
					建议使用计算模板来填充基础数据
				</view>
				<view class="ranks-content-input-box"
					:style="!expandShowInputBox?{'height':'0rpx','padding':'0rpx'}:{'marginBottom': '20rpx'}">
					<view class="input">
						<uv-input inputAlign="right" type='number' color='black' v-model="attribute.attack">
							<template v-slot:prefix>
								<uv-text text="刻印攻击:" type="primary" margin="0 3px 0 0"></uv-text>
							</template>
						</uv-input>
					</view>
					<view class="input">
						<uv-input inputAlign="right" type='number' color='black' v-model="attribute.eattack">
							<template v-slot:prefix>
								<uv-text text="监督攻击:" margin="0 3px 0 0" type="primary"></uv-text>
							</template>
						</uv-input>
					</view>
					<view class="input">
						<uv-input inputAlign="right" type='number' color='black' v-model="attribute.mastery">
							<template v-slot:prefix>
								<uv-text text="刻印专精:" margin="0 3px 0 0" type="primary"></uv-text>
							</template>
						</uv-input>
					</view>
					<view class="input">
						<uv-input inputAlign="right" type='number' color='black' v-model="attribute.emastery">
							<template v-slot:prefix>
								<uv-text text="监督专精:" margin="0 3px 0 0" type="primary"></uv-text>
							</template>
						</uv-input>
					</view>
					<view class="input">
						<uv-input inputAlign="right" type='number' color='black' v-model="attribute.terminal">
							<template v-slot:prefix>
								<uv-text text="刻印终端:" margin="0 3px 0 0" type="primary"></uv-text>
							</template>
						</uv-input>
					</view>
					<view class="input">
						<uv-input inputAlign="right" type='number' color='black' v-model="attribute.eterminal">
							<template v-slot:prefix>
								<uv-text text="监督终端:" margin="0 3px 0 0" type="primary"></uv-text>
							</template>
						</uv-input>
					</view>
					<view class="input">
						<uv-input inputAlign="right" type='number' color='black' v-model="attribute.health">
							<template v-slot:prefix>
								<uv-text text="刻印体质:" margin="0 3px 0 0" type="primary"></uv-text>
							</template>
						</uv-input>
					</view>
					<view class="input">
						<uv-input inputAlign="right" type='number' color='black' v-model="attribute.ehealth">
							<template v-slot:prefix>
								<uv-text text="监督体质:" margin="0 3px 0 0" type="primary"></uv-text>
							</template>
						</uv-input>
					</view>
					<view class="input">
						<uv-input inputAlign="right" type='number' color='black' v-model="attribute.defence">
							<template v-slot:prefix>
								<uv-text text="刻印防御:" margin="0 3px 0 0" type="primary"></uv-text>
							</template>
						</uv-input>
					</view>
					<view class="input">
						<uv-input inputAlign="right" type='number' color='black' v-model="attribute.edefence">
							<template v-slot:prefix>
								<uv-text text="监督防御:" margin="0 3px 0 0" type="primary"></uv-text>
							</template>
						</uv-input>
					</view>

					<view class="input">
						<uv-input inputAlign="right" type='number'>
							<template v-slot:prefix>
								<uv-text text="终端增幅:" margin="0 3px 0 0" type="primary"></uv-text>
							</template>
							<template v-slot:suffix>
								<uv-switch activeColor='#3c9cff' v-model="attribute.zf"></uv-switch>
							</template>
						</uv-input>
					</view>
					<view class="input">
						<uv-input inputAlign="right" type='number'>
							<template v-slot:prefix>
								<uv-text text="暴击100%:" margin="0 3px 0 0" type="primary"></uv-text>
							</template>
							<template v-slot:suffix>
								<uv-switch activeColor='#3c9cff' v-model="attribute.abj"></uv-switch>
							</template>
						</uv-input>
					</view>
					<view class="input">
						<uv-input inputAlign="right" type='number'>
							<template v-slot:prefix>
								<uv-text text="同调特性:" margin="0 3px 0 0" type="primary"></uv-text>
							</template>
							<template v-slot:suffix>
								<uv-switch activeColor='#3c9cff' v-model="attribute.td"></uv-switch>
							</template>
						</uv-input>
					</view>
					<view class="input">
						<uv-input inputAlign="right" type='number' color='black' v-model="attribute.rl">
							<template v-slot:prefix>
								<uv-text text="声望等级:" margin="0 3px 0 0" type="primary"></uv-text>
							</template>
							<template v-slot:suffix>
								<span style="color: #3788dc">级</span>
							</template>
						</uv-input>
					</view>
					<view class="input">
						<uv-input inputAlign="right" type='number' color='black' v-model="attribute.dfy">
							<template v-slot:prefix>
								<uv-text text="基础防御:" margin="0 3px 0 0" type="error"></uv-text>
							</template>
							<template v-slot:suffix>
								<span style="color: #ec696a">%</span>
							</template>
						</uv-input>
					</view>
					<view class="input">
						<uv-input inputAlign="right" type='text' color='black' v-model="attribute.dkx">
							<template v-slot:prefix>
								<uv-text text="敌方抗性:" margin="0 3px 0 0" type="error"></uv-text>
							</template>
						</uv-input>
					</view>
					<view class="input">
						<uv-input inputAlign="right" placeholder='例:炎=30' color='black' v-model="attribute.drd">
							<template v-slot:prefix>
								<uv-text text="弱点:" margin="0 3px 0 0" type="error"></uv-text>
							</template>
						</uv-input>
					</view>
					<view class="input">
						<uv-input inputAlign="right" type='number' color='black' v-model="attribute.djs">
							<template v-slot:prefix>
								<uv-text text="独立减伤:" margin="0 3px 0 0" type="error"></uv-text>
							</template>
							<template v-slot:suffix>
								<span style="color: #ec696a">%</span>
							</template>
						</uv-input>
					</view>

					<view class="input" v-if="hasSmsg">
						<uv-input inputAlign="right" type='number' color='black' v-model="attribute.smsg">
							<template v-slot:prefix>
								<uv-text text="生命颂歌:" margin="0 3px 0 0" type="warning"></uv-text>
							</template>
							<template v-slot:suffix>
								<span style="color: #f9ae3d">层</span>
							</template>
						</uv-input>
					</view>
				</view>
				<view @click='expandShowDamageBox=!expandShowDamageBox' class="parent-line-title">
					<view class="title-line-nav" style="margin-bottom: 0;">乘区概览</view>
					<view class="plt-right">
						<span class="plt-name" @click='handleClickCurrentDamageOverview(index)'
							:class="currentDamageOverviewIndex==index?'active-plt':''"
							v-for="(item, index) in charNameList">
							{{item.name}}
						</span>
					</view>
				</view>
				<view class="rank-damage-overview-area">
					<template v-for="(item, index) in overviewDataLimit">
						<view class='rdoa-item' @click='handleClickChangeOverview(item)'>
							<view class="ri-left">{{item.key}}<span class='ril-tips'
									style='color:red'>(上限{{dotTo100Int(item.limit)}}%)</span></view>
							<view class="ri-center">
								<view class="ric-progress">

									<view v-if="item.total != '0'" class="ric-mask"
										:style="{'width': dotTo100Float(dotTo100Float(item.total, dotTo100Int(item.limit))/dotTo100Float(item.limit, dotTo100Int(item.limit)), dotTo100Int(item.limit)) +'%'}">
									</view>

								</view>
								<view class="ric-text" :style="item.total==0?{'color':'red'}:{}">
									{{dotTo100Int(item.total)}}%
								</view>
							</view>
							<view class="ri-right">
								<img mode='aspectFill' class='line-title-icon' src="/static/icons/xia.png" alt="">
							</view>
						</view>
						<view class="rdoa-item-expand" v-if="overviewDataExpand.key==item.key">
							<template v-for="detail in item.detail">
								<view class='rie-item'>
									<view class="ri-text">{{detail.split('_')[0]}}：<span
											class='ri-val'>{{dotTo100Int(detail.split('_')[1])}}%</span></view>
								</view>
							</template>
						</view>
					</template>
					<template v-for="(item, index) in overviewDataCommon">
						<view class='rdoa-item' @click='handleClickChangeOverview(item)'>
							<view class="ri-left">{{item.key}}<span class='ril-tips'>(无限制)</span></view>
							<view class="ri-center">
								<view class="ric-progress" style="background-color: #dde0e5;">
								</view>
								<view v-if="item.key=='刻印攻击增加值'" class="ric-text"
									:style="item.total==0?{'color':'red'}:{}">
									{{dotTo100Int(item.total, item.key)}}
								</view>
								<view v-else class="ric-text" :style="item.total==0?{'color':'red'}:{}">
									{{dotTo100Int(item.total, item.key)}}%
								</view>
							</view>
							<view class="ri-right">
								<img mode='aspectFill' class='line-title-icon' src="/static/icons/xia.png" alt="">
							</view>
						</view>
						<view class="rdoa-item-expand" v-if="overviewDataExpand.key==item.key">
							<template v-for="detail in item.detail">
								<view class='rie-item'>
									<view class="ri-text">{{detail.split('_')[0]}}：<span
											class='ri-val'>{{dotTo100Int(detail.split('_')[1], item.key)}}%</span>
									</view>
								</view>
							</template>
						</view>
					</template>
				</view>
				<view class="parent-line-title">
					<view class="title-line-nav" style="margin-bottom: 0;">伤害计算</view>
				</view>
				<uv-sticky offsetTop="0" bgColor='#dde0e5'>
					<uv-tabs :activeStyle='{"color": "black","fontWeight": "600","fontSize": "28rpx"}'
						inactiveStyle='color:#333' :scrollable='false' :list="charNameList"
						@click="handleClickChoosePage"></uv-tabs>
					<uv-tabs :activeStyle='{"color": "black","fontWeight": "600","fontSize": "28rpx"}'
						inactiveStyle='color:#333' :scrollable='false' :list="attackTypeList"
						@click="handleClickChooseSkillPage"></uv-tabs>
				</uv-sticky>
				<view v-if="!finishCompute">
					<view
						style='color:red;font-size: 30rpx;letter-spacing: 1rpx;font-weight: 600;width: 100%;height: 400rpx;display: flex;align-items: center;justify-content: center;'>
						请先选择角色及烙痕，并点击底部计算按钮
					</view>
				</view>
				<view v-if="finishCompute" class='ranks-content-compute-area'>
					<template v-for="(alName, index) in Object.keys(attackList)">
						<view v-if="alName.indexOf('总结:') != 0" class="line-title rcca-nav-item"
							@click="changeAttackIndex(index)">
							<span>{{alName}}</span>
							<img mode='aspectFill' class='line-title-icon' src="/static/icons/xia.png" alt="">
						</view>
						<template v-if="alName.indexOf('总结:') != 0">
							<view class="damage" v-if="showAttackIndex==index">
								<view class="damage-title">属性-攻击</view>
								<view class="damage-item" v-for="item of Object.keys(attackList[alName].attackInfo)">
									<view>
										{{item.split("_")[0]}}
										<span class='val'>{{attackList[alName].attackInfo[item].split("_")[0]}}</span>
										{{attackList[alName].attackInfo[item].split("_")[1]}}
									</view>
								</view>
							</view>
							<view class="damage" v-if="showAttackIndex==index">
								<view class="damage-title">属性-专精</view>
								<view class="damage-item" v-for="item of Object.keys(attackList[alName].masteryInfo)">
									<view>
										{{item.split("_")[0]}}
										<span class='val'>{{attackList[alName].masteryInfo[item].split("_")[0]}}</span>
										{{attackList[alName].masteryInfo[item].split("_")[1]}}
									</view>
								</view>
							</view>
							<view class="damage" v-if="showAttackIndex==index">
								<view class="damage-title">属性-体质</view>
								<view class="damage-item" v-for="item of Object.keys(attackList[alName].healthInfo)">
									<view>
										{{item.split("_")[0]}}
										<span class='val'>{{attackList[alName].healthInfo[item].split("_")[0]}}</span>
										{{attackList[alName].healthInfo[item].split("_")[1]}}
									</view>
								</view>
							</view>
							<view class="damage" v-if="showAttackIndex==index">
								<view class="damage-title">乘区-暴击</view>
								<view class="damage-item" v-for="item of Object.keys(attackList[alName].critInfo)">
									<view>
										{{item.split("_")[0]}}
										<span class='val'>{{attackList[alName].critInfo[item].split("_")[0]}}</span>
										{{attackList[alName].critInfo[item].split("_")[1]}}
									</view>
								</view>
							</view>
							<view class="damage" v-if="showAttackIndex==index">
								<view class="damage-title">乘区-增益</view>
								<view class="damage-item" v-for="item of Object.keys(attackList[alName].buffInfo)">
									<view>
										{{item.split("_")[0]}}
										<span class='val'>{{attackList[alName].buffInfo[item].split("_")[0]}}</span>
										{{attackList[alName].buffInfo[item].split("_")[1]}}
									</view>
								</view>
							</view>
							<view class="damage" v-if="showAttackIndex==index">
								<view class="damage-title">乘区-弱化</view>
								<view class="damage-item" v-for="item of Object.keys(attackList[alName].debuffInfo)">
									<view :style="item.split('_')[0].indexOf('敌方')==-1?{}:{'color':'red'}">
										{{item.split("_")[0]}}
										<span class='val'>{{attackList[alName].debuffInfo[item].split("_")[0]}}</span>
										{{attackList[alName].debuffInfo[item].split("_")[1]}}
									</view>
								</view>
							</view>
							<view class="suggest" v-if="attackList[alName].suggesion.length && showAttackIndex==index">
								<view class="damage-title">建议</view>
								<view class="suggest-line" v-for="item of attackList[alName].suggesion">
									<span class='warning'>{{item}}</span>
								</view>
							</view>
							<view class="result" v-if="showAttackIndex==index">
								<view class="damage-title">结果</view>
								<view class="result-line">
									<span class='val'>{{attackList[alName].finalCompute}}</span>
								</view>
								<view class="result-line">
									最终伤害:<span class='val'>{{attackList[alName].finalDamage}}</span>
								</view>
							</view>
						</template>
						<template v-if="alName.indexOf('总结:') == 0">
							<view class="rcca-nav-item">
								<span style="color: #ff0000">{{alName.replaceAll("总结:","")}}</span>
							</view>
							<view class="result">
								<p class='result-line'
									style="display: flex;align-items: center;height: 50rpx;line-height: 50rpx: bold"
									v-for="i in attackList[alName].split('、')">
									<span>{{i.split(':')[0]}}</span>&nbsp;<span
										style="color:#ff0000;">{{i.split(':')[1]}}</span>
								</p>
							</view>
						</template>

					</template>
					<view class="rcca-nav-item">
						<span style="color: #ff0000">以上数据为单次伤害峰值，请小监督结合攻速/技能冷却自行判断实际效果</span>
					</view>
					<!-- <view class="result">
						<p class='result-line'
							style="display: flex;align-items: center;height: 50rpx;line-height: 50rpx: bold"
							>
							<span
								style="color:#ff0000;">{{totalDamage}}</span>
						</p>
					</view> -->
				</view>
			</view>
		</view>
		// #ifdef WEB
		<TopNavBar child='true'></TopNavBar>
		// #endif
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
		<uv-popup z-index="99" mode="bottom" ref="chareqPopup" custom-style="height: 1100rpx;overflow:auto;">
			<view class='search-box'>
				<uv-search shape='square' @search="searchchareq" @custom='searchchareq' margin="30rpx"
					v-model="chareqQueryParam" :showAction="true" actionText="搜索" :animation="true"></uv-search>
				<view style='padding: 0 30rpx;box-sizing: border-box;color:red;font-weight: 600;letter-spacing: 1rpx;'>
					可同时输入多个搜索关键字，多个技能名称中间用逗号隔开(中英文逗号均可)。例：联动，缓释，奇迹
				</view>
			</view>
			<view class="popup-skill-box">
				<view class="card-list-box">
					<view @click="handleChoosechareq(skill)" class="card skill-card"
						v-for="(skill,index) in showChareqs" :key='skill.skillId' wx:key='skill.skillId'>
						<view class='pc-img-box sp-skill-img'>
							<img :src="skill.icons" mode='widthFix' alt="" class="pc-img">
						</view>
						<view class="pc-name">{{skill.name}}</view>
					</view>
				</view>
			</view>
		</uv-popup>
		<uv-popup z-index="99" mode="bottom" ref="spSkillPopup" custom-style="height: 1100rpx;overflow:auto;">
			<view class='search-box'>
				<uv-search shape='square' @search="searchSkill" @custom='searchSkill' margin="30rpx"
					v-model="skillQueryParam" :showAction="true" actionText="搜索" :animation="true"></uv-search>
				<view style='padding: 0 30rpx;box-sizing: border-box;color:red;font-weight: 600;letter-spacing: 1rpx;'>
					可同时输入多个搜索关键字，多个技能名称中间用逗号隔开(中英文逗号均可)。例：联动，缓释，奇迹
				</view>
			</view>
			<view class="popup-skill-box">
				<view class="card-list-box">
					<view @click="handleChooseSkill(skill)" class="card skill-card"
						v-for="(skill,index) in showSmallSkills" :key='skill.skillId' wx:key='skill.skillId'>
						<view class='pc-img-box sp-skill-img'>
							<img :src="skill.icons" mode='widthFix' alt="" class="pc-img">
						</view>
						<view class="pc-name">{{skill.name}}</view>
					</view>
				</view>
			</view>
		</uv-popup>
		<uv-popup z-index="98" mode="bottom" ref="computeTemplate" custom-style="height: 1100rpx;overflow:auto;">
			<view class="rank-template-body">
				<view class="rtv-left">
					<template v-for="(item, index) in leftTemplateItems" :key='index'>
						<view @click='changeTemplateNav(index)'
							:class="currentTemplateNavIndex==index?'active-rtvl-item':''" class="rtvl-item">{{item}}
						</view>
					</template>
				</view>
				<view v-if="!addTemplate" class="rtv-right">
					<template v-if="currentTemplateNavIndex == 0">
						<view class="rtvl-item" @click='chooseCardsTemplate(ttitle)'
							v-for="ttitle in Object.keys(lrlItems1)">
							<view class="rtvli-title">
								<span class="rtvli-t-left">
									{{ttitle}}
								</span>
								<span v-if="lrlItems1c==ttitle" class="rtvli-t-right">
									已选中
								</span>
								<span v-if="lrlItems1c!=ttitle" @click.stop='deleteCardsTemplate(ttitle)'
									style="color: red" class="rtvli-t-right">
									删除
								</span>
							</view>
							<view class="rtvli-content">
								<template v-for="(item,index) in lrlItems1[ttitle].flower.split('_')">
									<view class="rtv-c-item">
										<view class="rtvc-left">
											{{index==0?'队长':'角色'+index}}
										</view>
										<view class="rtvc-right">
											<span>
												{{lrlItems1[ttitle]['flower'].split('_')[index]}}花
											</span>
											<span>
												L{{lrlItems1[ttitle]['l'].split('_')[index]}}
											</span>
										</view>
									</view>
								</template>
							</view>
						</view>
					</template>
					<template v-if="currentTemplateNavIndex == 1">
						<view @click='chooseCardsTemplate(ttitle)' class="rtvl-item"
							v-for="ttitle in Object.keys(lrlItems2)">
							<view class="rtvli-title">
								<span class="rtvli-t-left">
									{{ttitle}}
								</span>
								<span v-if="lrlItems2c==ttitle" class="rtvli-t-right">
									已选中
								</span>
								<span v-if="lrlItems2c!=ttitle" @click.stop='deleteCardsTemplate(ttitle)'
									style="color: red" class="rtvli-t-right">
									删除
								</span>
							</view>
							<view class="rtvli-content">
								<template v-for="(item,index) in lrlItems2[ttitle].attr.split('_')">
									<view class="rtv-c-item">
										<view class="rtvc-left">
											<span v-if="index==0">体质</span>
											<span v-if="index==1">防御</span>
											<span v-if="index==2">攻击</span>
											<span v-if="index==3">专精</span>
											<span v-if="index==4">终端</span>
										</view>
										<view class="rtvc-right">
											<span>
												{{item}}
											</span>
										</view>
									</view>
								</template>
							</view>
						</view>
					</template>
					<template v-if="currentTemplateNavIndex == 2">
						<view class="rtvl-item" @click='chooseCardsTemplate(ttitle)'
							v-for="ttitle in Object.keys(lrlItems3)">
							<view class="rtvli-title">
								<span class="rtvli-t-left">
									{{ttitle}}
								</span>
								<span v-if="lrlItems3c==ttitle" class="rtvli-t-right">
									已选中
								</span>
								<span v-if="lrlItems3c!=ttitle" @click.stop='deleteCardsTemplate(ttitle)'
									style="color: red" class="rtvli-t-right">
									删除
								</span>
							</view>
							<view class="rtvli-content">
								<template v-for="(item,index) in lrlItems3[ttitle].attr.split('_')">
									<view class="rtv-c-item">
										<view class="rtvc-left">
											<span v-if="index==0">体质</span>
											<span v-if="index==1">防御</span>
											<span v-if="index==2">攻击</span>
											<span v-if="index==3">专精</span>
											<span v-if="index==4">终端</span>
										</view>
										<view class="rtvc-right">
											<span>
												{{item}}
											</span>
										</view>
									</view>
								</template>
							</view>
						</view>
					</template>
					<template v-if="currentTemplateNavIndex == 3">
						<view class="rtvl-item" @click='chooseCardsTemplate(ttitle)'
							v-for="ttitle in Object.keys(lrlItems4)">
							<view class="rtvli-title">
								<span class="rtvli-t-left">
									{{ttitle}}
								</span>
								<span v-if="lrlItems4c==ttitle" class="rtvli-t-right">
									已选中
								</span>
								<span v-if="lrlItems4c!=ttitle" @click.stop='deleteCardsTemplate(ttitle)'
									style="color: red" class="rtvli-t-right">
									删除
								</span>
							</view>
							<view class="rtvli-content enemy-content">
								<template v-for="(item,index) in Object.keys(lrlItems4[ttitle])">
									<view class="rtv-c-item">
										<view class="rtvc-left">
											<span v-if="item=='dfy'">基础防御</span>
											<span v-if="item=='djs'">独立减伤</span>
											<span v-if="item=='dkx'">属性抗性</span>
											<span v-if="item=='drd'">属性弱点</span>
										</view>
										<view class="rtvc-right">
											<view v-if="item=='dfy'">
												{{lrlItems4[ttitle][item]==0?'无':lrlItems4[ttitle][item]+'%'}}
											</view>
											<view v-if="item=='djs'">
												{{lrlItems4[ttitle][item]==0?'无':lrlItems4[ttitle][item]+'%'}}
											</view>
											<view v-if="item=='dkx'">
												<template v-if="lrlItems4[ttitle][item]==0">无</template>
												<template v-if="lrlItems4[ttitle][item]!=0">
													<template v-for="i in lrlItems4[ttitle][item].split(',')">
														<p>{{i}}%</p>
													</template>
												</template>
											</view>
											<view v-if="item=='drd'">
												<template v-if="lrlItems4[ttitle][item]==0">无</template>
												<template v-if="lrlItems4[ttitle][item]!=0">
													<template v-for="i in lrlItems4[ttitle][item].split(',')">
														<p>{{i}}%</p>
													</template>
												</template>
											</view>
										</view>
									</view>
								</template>
							</view>
						</view>
					</template>
					<template v-if="currentTemplateNavIndex == 4">
						<view class="rtvl-item" @click='chooseCardsTemplate(ttitle)'
							v-for="ttitle in Object.keys(lrlItems5)">
							<view class="rtvli-title">
								<span class="rtvli-t-left">
									{{ttitle}}
								</span>
								<span v-if="lrlItems5c==ttitle" class="rtvli-t-right">
									已选中
								</span>
								<span v-if="lrlItems5c!=ttitle" @click.stop='deleteCardsTemplate(ttitle)'
									style="color: red" class="rtvli-t-right">
									删除
								</span>
							</view>
							<view class="rtvli-content enemy-content">
								<view class="rtv-c-item jidian-item" v-for="item in lrlItems5[ttitle]">
									<view class="rtvc-left">
										<img :src="item.icons" class="skill-icon" alt="">
									</view>
									<view class="rtvc-right">
										<span>{{item.name}}</span>
									</view>
								</view>
							</view>
						</view>
					</template>
					<template v-if="currentTemplateNavIndex == 5">
						<view class="rtvl-item" @click='chooseCardsTemplate(ttitle)'
							v-for="ttitle in Object.keys(lrlItems6)">
							<view class="rtvli-title">
								<span class="rtvli-t-left">
									{{ttitle}}
								</span>
								<span v-if="lrlItems6c==ttitle" class="rtvli-t-right">
									已选中
								</span>
								<span v-if="lrlItems6c!=ttitle" @click.stop='deleteCardsTemplate(ttitle)'
									style="color: red" class="rtvli-t-right">
									删除
								</span>
							</view>
							<view class="rtvli-content enemy-content">
								<template v-for="(item,index) in Object.keys(lrlItems6[ttitle])">
									<view class="rtv-c-item">
										<view class="rtvc-left">
											<span v-if="item=='zf'">终端增幅</span>
											<span v-if="item=='abj'">暴击100%</span>
											<span v-if="item=='smsg'">生命颂歌</span>
											<span v-if="item=='td'">同调</span>
											<span v-if="item=='rl'">声望等级</span>
										</view>
										<view class="rtvc-right">
											<template v-if="item=='rl'">
												<p>{{lrlItems6[ttitle][item]}}级</p>
											</template>
											<template v-if="item=='zf'">
												<uv-switch activeColor='#3c9cff' disabled="true"
													v-model="lrlItems6[ttitle][item]"></uv-switch>
												{{lrlItems6[ttitle][item]?'开启':'关闭'}}
											</template>
											<template v-if="item=='abj'">
												<uv-switch activeColor='#3c9cff' disabled="true"
													v-model="lrlItems6[ttitle][item]"></uv-switch>
												{{lrlItems6[ttitle][item]?'开启':'关闭'}}
											</template>
											<template v-if="item=='td'">
												<uv-switch activeColor='#3c9cff' disabled="true"
													v-model="lrlItems6[ttitle][item]"></uv-switch>
												{{lrlItems6[ttitle][item]?'开启':'关闭'}}
											</template>
											<template v-if="item=='smsg'">
												<p>{{lrlItems6[ttitle][item]}}层</p>
											</template>
										</view>
									</view>
								</template>
							</view>
						</view>
					</template>
				</view>
				<view v-if="addTemplate" class="rtv-right">
					<template v-if="currentTemplateNavIndex == 0">
						<view class="rtvl-item">
							<view class="rtvli-title">
								<span class="rtvli-t-left">
									添加
								</span>
							</view>
							<view class="rtvli-content add-content">
								<view class="rtv-c-item">
									<view class="rtvc-left">
										标题
									</view>
									<view class="rtvc-right">
										<input class='rr r-title' type="text" v-model="lrlItems1Input['title']"></input>
									</view>
								</view>
								<template v-for="(item,index) in 8">
									<view class="rtv-c-item">
										<view class="rtvc-left">
											{{index==0?'队长':'角色'+index}}
										</view>
										<view class="rtvc-right">
											<input class='rr r1' type="number"
												v-model="lrlItems1Input[index+'-flower']"></input>花
											L<input class='rr r1' type="number"
												v-model="lrlItems1Input[index+'-lv']"></input>
										</view>
									</view>
								</template>
							</view>
						</view>
					</template>
					<template v-if="currentTemplateNavIndex == 1">
						<view class="rtvl-item">
							<view class="rtvli-content add-content">
								<view class="rtv-c-item">
									<view class="rtvc-left">
										标题
									</view>
									<view class="rtvc-right">
										<input class='rr r-title' type="text" v-model="lrlItems2Input['title']"></input>
									</view>
								</view>
								<template v-for="(item,index) in lrlItems2[lrlItems2c].attr.split('_')">
									<view class="rtv-c-item">
										<view class="rtvc-left">
											<span v-if="index==0">体质</span>
											<span v-if="index==1">防御</span>
											<span v-if="index==2">攻击</span>
											<span v-if="index==3">专精</span>
											<span v-if="index==4">终端</span>
										</view>
										<view class="rtvc-right">
											<input class='rr r2' type="number" placeholder="请填写数据"
												v-model="lrlItems2Input[index+'-attr']"></input>
										</view>
									</view>
								</template>
							</view>
						</view>
					</template>
					<template v-if="currentTemplateNavIndex == 2">
						<view class="rtvl-item">
							<view class="rtvli-content add-content">
								<view class="rtv-c-item">
									<view class="rtvc-left">
										标题
									</view>
									<view class="rtvc-right">
										<input class='rr r-title' type="text" v-model="lrlItems3Input['title']"></input>
									</view>
								</view>
								<template v-for="(item,index) in lrlItems3[lrlItems3c].attr.split('_')">
									<view class="rtv-c-item">
										<view class="rtvc-left">
											<span v-if="index==0">体质</span>
											<span v-if="index==1">防御</span>
											<span v-if="index==2">攻击</span>
											<span v-if="index==3">专精</span>
											<span v-if="index==4">终端</span>
										</view>
										<view class="rtvc-right">
											<input class='rr r2' type="number" placeholder="请填写数据"
												v-model="lrlItems3Input[index+'-attr']"></input>
										</view>
									</view>
								</template>
							</view>
						</view>
					</template>
					<template v-if="currentTemplateNavIndex == 3">
						<view class="rtvl-item">
							<view class="rtvli-content add-content">
								<view class="rtv-c-item">
									<view class="rtvc-left">
										标题
									</view>
									<view class="rtvc-right">
										<input class='rr r-title' type="text" v-model="lrlItems4Input['title']"></input>
									</view>
								</view>
								<template v-for="(item,index) in Object.keys(lrlItems4[lrlItems4c])">
									<view class="rtv-c-item">
										<view class="rtvc-left">
											<span v-if="item=='dfy'">基础防御</span>
											<span v-if="item=='djs'">独立减伤</span>
											<span v-if="item=='dkx'">属性抗性</span>
											<span v-if="item=='drd'">属性弱点</span>
										</view>
										<view class="rtvc-right">
											<template v-if="item=='dfy'">
												<input class='rr r2' type="number" placeholder="仅填写纯数字,例:40"
													v-model="lrlItems4Input['dfy']"></input>
											</template>
											<template v-if="item=='djs'">
												<input class='rr r2' type="number" placeholder="仅填写纯数字,例:40"
													v-model="lrlItems4Input['djs']"></input>
											</template>
											<template v-if="item=='dkx'">
												<input class='rr r2' type="text" placeholder="例:炎=40,物理=30"
													v-model="lrlItems4Input['dkx']"></input>
											</template>
											<template v-if="item=='drd'">
												<input class='rr r2' type="text" placeholder="例:炎=40,物理=30"
													v-model="lrlItems4Input['drd']"></input>
											</template>
										</view>
									</view>
								</template>
							</view>
						</view>
					</template>
					<template v-if="currentTemplateNavIndex == 4">
						<view class="rtvl-item">
							<view class="rtvli-content add-content">
								<view class="rtv-c-item">
									<view class="rtvc-left">
										标题
									</view>
									<view class="rtvc-right">
										<input class='rr r-title' type="text" v-model="lrlItems5Input['title']"></input>
									</view>
								</view>
								<view class="rtv-c-item jidian-item" v-for="index in 4">
									<view class="rtvc-left">
										<template v-if="lrlItems5Input['skills'][index]">
											<img :src="lrlItems5Input['skills'][index].icons" class="skill-icon" alt="">
										</template>
										<template v-else>
											<span @click='chooseTemplateSkills(index)'>点击选择</span>
										</template>
									</view>
									<view class="rtvc-right">
										<template v-if="lrlItems5Input['skills'][index]">
											<span>{{lrlItems5Input['skills'][index].name}}</span>
										</template>
										<template v-else>
											<span>点击左侧选择</span>
										</template>
									</view>
								</view>
							</view>
						</view>
					</template>
					<template v-if="currentTemplateNavIndex == 5">
						<view class="rtvl-item">
							<view class="rtvli-content add-content">
								<view class="rtv-c-item">
									<view class="rtvc-left">
										标题
									</view>
									<view class="rtvc-right">
										<input class='rr r-title' type="text" v-model="lrlItems6Input['title']"></input>
									</view>
								</view>
								<template v-for="(item,index) in Object.keys(lrlItems6[lrlItems6c])">
									<view class="rtv-c-item">
										<view class="rtvc-left">
											<span v-if="item=='zf'">终端增幅</span>
											<span v-if="item=='abj'">暴击100%</span>
											<span v-if="item=='smsg'">生命颂歌</span>
											<span v-if="item=='td'">同调</span>
											<span v-if="item=='rl'">声望等级</span>
										</view>
										<view class="rtvc-right">
											<template v-if="item=='rl'">
												<input class='rr r2' type="number" placeholder="声望等级"
													v-model="lrlItems6Input['rl']"></input>
											</template>
											<template v-if="item=='zf'">
												<uv-switch activeColor='#3c9cff'
													v-model="lrlItems6Input['zf']"></uv-switch>
											</template>
											<template v-if="item=='abj'">
												<uv-switch activeColor='#3c9cff'
													v-model="lrlItems6Input['abj']"></uv-switch>
											</template>
											<template v-if="item=='td'">
												<uv-switch activeColor='#3c9cff'
													v-model="lrlItems6Input['td']"></uv-switch>
											</template>
											<template v-if="item=='smsg'">
												<input class='rr r2' type="number" placeholder="生命颂歌层数"
													v-model="lrlItems6Input['smsg']"></input>
											</template>
										</view>
									</view>
								</template>
							</view>
						</view>
					</template>
					<view v-if="currentTemplateNavIndex != 4" style="color: red;margin-bottom: 10rpx;font-weight: 600;">
						未填写数据默认为0</view>
					<view @click='addSubmit' class="add-template-submit">
						提交
					</view>
				</view>
				<view @click='toAddTemplate' class="add-btn" :class="addTemplate?'active-add-btn':''">
					{{addTemplate?'选择':'添加'}}{{leftTemplateItems[currentTemplateNavIndex]}}预设
				</view>
			</view>
		</uv-popup>
		<view class="bottom-nav-line">
			<view class="left">
				<view class="btn" @click="showTemplatePopup">
					<img src="/static/icons/template.png" alt="" class="bottom-nav-line-icon">
					<view class="bottom-nav-line-text">计算模板</view>
				</view>
			</view>
			<view class="notice">点击左侧模板快速填写数据</view>
			<view class="right">
				<view class="submit-btn" @click="handleClickToCompute(false)">
					<img mode='aspectFill' class='calc' src="/static/icons/calc.png" alt="">
					<span class='btn1-name'>计算</span>
				</view>
			</view>
		</view>
		<uv-modal :zIndex='99999' ref="modal" :title="modalData.title" :showCancelButton='modalData.showCancelButton'
			:confirmText="modalData.confirmText" :cancelText='modalData.cancelText' :content='modalData.message'
			@confirm="sureC" @cancel="cancelC"></uv-modal>
		<Toast></Toast>
		<uv-toast ref='toast'></uv-toast>
		<AeIframe @exit='wikiUrl=""' :url='wikiUrl'></AeIframe>
	</view>
</template>

<script setup>
	import {
		getCurrentInstance,
		onMounted,
		ref,
		watch
	} from 'vue';
	const toast = ref()
	// #ifdef WEB
	import TopNavBar from '@/components/TopNavbar/TopNavbar.vue'
	const showNav = ref(false)
	//确认选择该烙痕
	const wikiUrl = ref("")

	function charToWikiDetail(name) {
		wikiUrl.value = "https://wiki.biligame.com/bjhl/同调者/" + name
	}

	function skillToWikilDetail(name) {
		wikiUrl.value = "https://wiki.biligame.com/bjhl/刻印技能/" + name
	}

	function chareqToWikilDetail(name) {
		wikiUrl.value = "https://wiki.biligame.com/bjhl/相纸图鉴/" + name
	}

	function memoryToWikiDetail(name) {
		wikiUrl.value = "https://wiki.biligame.com/bjhl/记忆烙痕/" + name
	}

	function showNavFunc(vvvv) {
		showNav.value = vvvv
	}
	// #endif
	import {
		getShareComputeCardsInfo,
		getcharsApi,
		getMemorysApi,
		computeDamageApi,
		getAllSmallSkillsApi,
		getCardsTemplateApi,
		getAllCardsTemplateApi,
		addCardsTemplateApi,
		useCardsTemplateApi,
		deleteCardsTemplateApi,
		pApi
	} from "@/myapi/ashEchoes.js"

	const computeTemplate = ref()
	const leftTemplateItems = ['开花潜能', '刻印属性', '监督刻印', '敌方属性', '基点技能', '拓展设置']
	const lrlItems1 = ref({
		"预设": {
			'flower': "0_0_0_0_0_0_0_0",
			'l': "7_0_0_0_0_0_0_0"
		}
	})
	const lrlItems2 = ref({
		"预设": {
			'attr': '1000_1000_1500_1500_1000'
		}
	})
	const lrlItems3 = ref({
		"预设": {
			'attr': '1500_1000_1500_1500_1500'
		}
	})
	const lrlItems4 = ref({
		"预设": {
			'dfy': '40',
			'djs': '0',
			'dkx': '0',
			'drd': '0'
		}
	})
	const lrlItems5 = ref({
		"预设": [{
				"skillId": 150051,
				"level": 0,
				"name": "能量缓释",
				"hidden": false,
				"icons": "https://r.qianqiu.info/skill/icons/150051.png?v=20241121"
			},
			{
				"skillId": 110052,
				"level": 0,
				"name": "奉献",
				"hidden": false,
				"icons": "https://r.qianqiu.info/skill/icons/110052.png?v=20241121"
			},
			{
				"skillId": 110031,
				"level": 0,
				"name": "生命颂歌",
				"hidden": false,
				"icons": "https://r.qianqiu.info/skill/icons/110031.png?v=20241121"
			}
		]
	})
	const lrlItems6 = ref({
		"预设": {
			'rl': 0,
			'zf': false,
			'abj': false,
			'td': false,
			'smsg': 0
		}
	})
	const lrlItems1c = ref("预设")
	const lrlItems2c = ref("预设")
	const lrlItems3c = ref("预设")
	const lrlItems4c = ref("预设")
	const lrlItems5c = ref("预设")
	const lrlItems6c = ref("预设")
	const lrlItems1Input = ref({})
	const lrlItems2Input = ref({})
	const lrlItems3Input = ref({})
	const lrlItems4Input = ref({})
	const lrlItems5Input = ref({
		"skills": []
	})
	const lrlItems6Input = ref({})
	const currentTemplateNavIndex = ref(0)
	const addTemplate = ref(false)
	const currentTemplateSpSkill = ref(-1)

	function getMineAllTemplate(acallback) {
		if (!uni.guser.getId()) {
			acallback(false)
			return
		}
		getAllCardsTemplateApi(r => {
			if (r) {
				let keys = Object.keys(r)
				for (let j of keys) {
					let titles = Object.keys(r[j])
					if (j == 0) {
						for (let title of titles) {
							let arr = r[j][title];
							lrlItems1.value[title] = JSON.parse(arr.param)
							if (arr.use == 1) {
								lrlItems1c.value = title
							}
						}
					} else if (j == 1) {
						for (let title of titles) {
							let arr = r[j][title];
							lrlItems2.value[title] = JSON.parse(arr.param)
							if (arr.use == 1) {
								lrlItems2c.value = title
							}
						}
					} else if (j == 2) {
						for (let title of titles) {
							let arr = r[j][title];
							lrlItems3.value[title] = JSON.parse(arr.param)
							if (arr.use == 1) {
								lrlItems3c.value = title
							}
						}
					} else if (j == 3) {
						for (let title of titles) {
							let arr = r[j][title];
							lrlItems4.value[title] = JSON.parse(arr.param)
							if (arr.use == 1) {
								lrlItems4c.value = title
							}
						}
					} else if (j == 4) {
						for (let title of titles) {
							let arr = r[j][title];
							lrlItems5.value[title] = JSON.parse(arr.param)
							if (arr.use == 1) {
								lrlItems5c.value = title
							}
						}
					} else if (j == 5) {
						for (let title of titles) {
							let arr = r[j][title];
							lrlItems6.value[title] = JSON.parse(arr.param)
							if (arr.use == 1) {
								lrlItems6c.value = title
							}
						}
					}
				}
				acallback(true)
			}
		})
	}

	function chooseTemplateSkills(index) {
		currentTemplateSpSkill.value = index
		spSkillPopup.value.open()
	}

	function addSubmit() {
		cb = "addTemplate"
		confirm('', "是否添加" + leftTemplateItems[currentTemplateNavIndex.value] + "配置", true, "是的", "点错了")
	}

	function sureAddTemplate() {
		let dda = {}
		let title = ''
		if (currentTemplateNavIndex.value == 0) {
			title = lrlItems1Input.value.title
			let flowers = [0, 0, 0, 0, 0, 0, 0, 0]
			let ls = [0, 0, 0, 0, 0, 0, 0, 0]
			for (let i of Object.keys(lrlItems1Input.value)) {
				if (i == 'title') {
					continue
				}
				let v = parseInt(i.split("-")[0])
				if (i.indexOf('flower') != -1) {
					let vv = parseInt(lrlItems1Input.value[i])
					if (vv <= 0) vv = 0
					if (vv > 5) vv = 5
					flowers[v] = vv
				} else if (i.indexOf('lv') != -1) {
					let vv = parseInt(lrlItems1Input.value[i])
					if (vv <= 0) vv = 0
					if (vv > 9) vv = 9
					ls[v] = vv
				}
			}
			let param = {
				'flower': '',
				'l': ''
			}
			for (let i in flowers) {
				param.flower = param.flower + flowers[i]
				if (i < 7) {
					param.flower = param.flower + "_"
				}
			}
			for (let i in ls) {
				param.l = param.l + ls[i]
				if (i < 7) {
					param.l = param.l + "_"
				}
			}

			dda.param = JSON.stringify(param)
		} else if (currentTemplateNavIndex.value == 1) {
			title = lrlItems2Input.value.title
			let attr = [0, 0, 0, 0, 0]

			for (let i of Object.keys(lrlItems2Input.value)) {
				if (i == 'title') {
					continue
				}
				let v = parseInt(i.split("-")[0])
				let vv = parseInt(lrlItems2Input.value[i])
				if (vv <= 0) vv = 0
				attr[v] = vv
			}
			let param = ""
			for (let i in attr) {
				param = param + attr[i]
				if (i < 4) {
					param = param + "_"
				}
			}
			dda.param = JSON.stringify({
				"attr": param
			})
		} else if (currentTemplateNavIndex.value == 2) {
			title = lrlItems3Input.value.title
			let attr = [0, 0, 0, 0, 0]

			for (let i of Object.keys(lrlItems3Input.value)) {
				if (i == 'title') {
					continue
				}
				let v = parseInt(i.split("-")[0])
				let vv = parseInt(lrlItems3Input.value[i])
				if (vv <= 0) vv = 0
				attr[v] = vv
			}
			let param = ""
			for (let i in attr) {
				param = param + attr[i]
				if (i < 4) {
					param = param + "_"
				}
			}
			dda.param = JSON.stringify({
				"attr": param
			})
		} else if (currentTemplateNavIndex.value == 3) {
			title = lrlItems4Input.value.title
			dda.param = JSON.stringify({
				'dfy': lrlItems4Input.value.dfy || 0,
				'djs': lrlItems4Input.value.djs || 0,
				'dkx': lrlItems4Input.value.dkx || 0,
				'drd': lrlItems4Input.value.drd || 0
			})
		} else if (currentTemplateNavIndex.value == 4) {
			title = lrlItems5Input.value.title
			let arr = []
			for (let i of lrlItems5Input.value.skills) {
				if (i) {
					arr.push(i)
				}
			}
			dda.param = JSON.stringify(arr)
		} else if (currentTemplateNavIndex.value == 5) {
			title = lrlItems6Input.value.title
			dda.param = JSON.stringify({
				'rl': lrlItems6Input.value.rl || 0,
				'zf': lrlItems6Input.value.zf || false,
				'td': lrlItems6Input.value.td || false,
				'abj': lrlItems6Input.value.abj || false,
				'smsg': lrlItems6Input.value.smsg || 0
			})
		}

		if (!title || title.length >= 16) {
			uni.toastFail("标题禁止为空，且不能超过16字")
			return
		}
		if (title == '预设') {
			uni.toastFail("名称禁止命名为 预设")
			return
		}
		dda.title = title
		dda.uid = uni.guser.getId()

		if (!dda.uid) {
			uni.toastFail("请先前往首页登录~")
			return
		}
		dda.type = currentTemplateNavIndex.value
		addCardsTemplateApi(dda, (r) => {
			uni.toast("添加成功")
			if (currentTemplateNavIndex.value == 0) lrlItems1.value[title] = JSON.parse(dda.param)
			if (currentTemplateNavIndex.value == 1) lrlItems2.value[title] = JSON.parse(dda.param)
			if (currentTemplateNavIndex.value == 2) lrlItems3.value[title] = JSON.parse(dda.param)
			if (currentTemplateNavIndex.value == 3) lrlItems4.value[title] = JSON.parse(dda.param)
			if (currentTemplateNavIndex.value == 4) lrlItems5.value[title] = JSON.parse(dda.param)
			if (currentTemplateNavIndex.value == 5) lrlItems6.value[title] = JSON.parse(dda.param)
		}, e => {
			uni.toastFail(e)
		})
		toAddTemplate()
	}

	function chooseCardsTemplate(title) {
		if (currentTemplateNavIndex.value == 0) {
			lrlItems1c.value = title
			let flowerData = lrlItems1.value[lrlItems1c.value]
			for (let i = 0; i < members.value.length; i++) {
				members.value[i].flower = flowerData.flower.split("_")[i]
				members.value[i].plevel = flowerData.l.split("_")[i]
			}
		}
		if (currentTemplateNavIndex.value == 1) {
			lrlItems2c.value = title
		}
		if (currentTemplateNavIndex.value == 2) {
			lrlItems3c.value = title
		}
		if (currentTemplateNavIndex.value == 3) {
			lrlItems4c.value = title
		}
		if (currentTemplateNavIndex.value == 4) {
			lrlItems5c.value = title
		}
		if (currentTemplateNavIndex.value == 5) {
			lrlItems6c.value = title
		}
		initTemplateData(false)
		let param = {
			title,
			type: currentTemplateNavIndex.value
		}

		useCardsTemplateApi(param, r => {}, e => {
			uni.toastFail(e)
		})
	}

	let deleteTitle = ""

	function deleteCardsTemplate(ttitle) {
		if (ttitle == '预设') {
			uni.toastFail("预设方案无法删除")
			return
		}
		cb = "deleteTemplate"
		deleteTitle = ttitle
		confirm('', "是否删除名称为：" + ttitle + "的" + leftTemplateItems[currentTemplateNavIndex.value] + "配置", true, "是的", "点错了")
	}

	function sureDeleteTemplate() {
		let param = {
			title: deleteTitle,
			type: currentTemplateNavIndex.value
		}
		deleteCardsTemplateApi(param, r => {
			if (currentTemplateNavIndex.value == 0) delete lrlItems1.value[deleteTitle]
			if (currentTemplateNavIndex.value == 1) delete lrlItems2.value[deleteTitle]
			if (currentTemplateNavIndex.value == 2) delete lrlItems3.value[deleteTitle]
			if (currentTemplateNavIndex.value == 3) delete lrlItems4.value[deleteTitle]
			if (currentTemplateNavIndex.value == 4) delete lrlItems5.value[deleteTitle]
			if (currentTemplateNavIndex.value == 5) delete lrlItems6.value[deleteTitle]
			uni.toast("删除成功")
			deleteTitle = ""
		}, e => {
			uni.toastFail(e)
		})
	}

	function toAddTemplate() {
		addTemplate.value = !addTemplate.value
	}

	function showTemplatePopup() {
		if (!uni.guser.getId()) {
			uni.toastFail("请先前往首页登录~")
			return
		}
		computeTemplate.value.open()
	}

	function changeTemplateNav(index) {
		currentTemplateNavIndex.value = index
	}
	// 所有角色
	const members = ref([{}, {}, {}, {}, {}, {}, {}, {}])
	const currentDamageOverviewIndex = ref(-1)
	const charNameList = ref([{
		name: "待选角色",
		disabled: true
	}, {
		name: "待选角色",
		disabled: true
	}, {
		name: "待选角色",
		disabled: true
	}, {
		name: "待选角色",
		disabled: true
	}])
	const headMemberCurrent = ref(0)
	const currentChooseMember = ref(0)
	const allChars = ref([])
	const showSelectChars = ref([])
	const memberPopup = ref()
	const ecategory = ["炎", "水", "雷", "霜", "风", "物理", "蚀"]
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

	function handleClickCurrentDamageOverview(index) {
		currentDamageOverviewIndex.value = index
		handleClickToCompute(true)
		computeParam += "&overview=true&coverview=" + index
		goCompute()
	}

	// 获取当前下表队员的信息
	function initCurrentMemberInfo(charId, beginInitIndex) {
		getcharsApi(charId + "/" + beginInitIndex, (r) => {
			let char = r[0]
			if (beginInitIndex <= 3) {
				charNameList.value[beginInitIndex].name = char.name
				charNameList.value[beginInitIndex].disabled = false
			}
			// 初始化数据
			if ((char.attack + "").indexOf("%") == -1) {
				char.attack = Math.round(char.attack * 100) + Math.round(char.eattack * 100) + "%"
				char.mastery = Math.round(char.mastery * 100) + Math.round(char.emastery * 100) + "%"
				char.health = Math.round(char.health * 100) + Math.round(char.ehealth * 100) + "%"
			}
			let flowerData = lrlItems1.value[lrlItems1c.value]
			char.flower = flowerData.flower.split("_")[beginInitIndex]
			char.plevel = flowerData.l.split("_")[beginInitIndex]
			if (beginInitIndex >= 0) {
				members.value[beginInitIndex] = char
				members.value[beginInitIndex].pageCurrent = 0
			} else {
				members.value[headMemberCurrent.value] = char
				members.value[headMemberCurrent.value].pageCurrent = 0
			}
			if (beginInitIndex == 0) {
				initLeaderSkills(char, beginInitIndex)
			}
		})
	}
	const cards = ref([])
	const cardCurrent = ref(0)

	let deleteIndex = -1;
	// 唤醒是否删除的弹窗
	function handleDeleteMemberPre(index) {
		deleteIndex = index
		cb = 'removeMember'
		confirm("", "是否取消" + members.value[index].name + '上场', true, '是的', '点错了')
	}
	//删除当前下标的角色
	function handleDeleteMember() {
		let index = deleteIndex
		if (deleteIndex <= 3) {
			charNameList.value[deleteIndex] = {
				name: "待选角色",
				disabled: true
			}
		}
		//移除当前元素
		members.value[index] = {}
		//如果index 和 当前激活的角色下表为同一个，就减1
		if (index == headMemberCurrent.value) {
			for (let i = 0; i < members.value.length; i++) {
				if (members.value[i].characterId) {
					headMemberCurrent.value = i;
					break;
				}
			}
		}
	}
	//打开角色选择器
	function handleOpenMemberBox(index) {
		if (headMemberCurrent.value != index && members.value[index].characterId) {
			headMemberCurrent.value = index
			return
		}
		currentChooseMember.value = index
		memberPopup.value.open()
	}
	//确认选择角色
	//通过第二个参数判断是否为初始化的参数
	function handleChooseMember(v, beginInitIndex) {
		let oCharIndex = -1;
		let nCharIndex = beginInitIndex || currentChooseMember.value;
		for (let i = 0; i < members.value.length; i++) {
			// 如果当前选择的角色之前有
			if (members.value[i].characterId == v.characterId) {
				// 判断当前选择下标是否拥有人物
				if (!members.value[nCharIndex].characterId) {
					// 如果没有人物直接返回
					uni.toastFail(v.name + '已经在人物列表中了~')
					return
				} else {
					// 如果不一样 准备替换两者的位置
					oCharIndex = i;
					break;
				}
			}
		}
		// 换位置
		if (oCharIndex != -1) {
			members.value[oCharIndex] = members.value[nCharIndex]
			if (oCharIndex < 4) {
				charNameList.value[oCharIndex].name = members.value[nCharIndex].name
			}
		}
		headMemberCurrent.value = currentChooseMember.value
		if (!beginInitIndex && beginInitIndex != 0) {
			beginInitIndex = currentChooseMember.value
		}
		// 初始化当前选择角色的名字
		initCurrentMemberInfo(v.characterId, beginInitIndex)
		// 初始化新队长的技能
		if (oCharIndex == 0) {
			initCurrentMemberInfo(members.value[0].characterId, 0)
		}
		memberPopup.value.close()
	}

	//已经选择的烙痕
	const memorys = ref([{}, {}, {}, {}, {}, {}])
	let allMemorys = []
	const memoryPopup = ref()
	//当前选择的分类
	const memoryBoxCategoryCurrent = ref(0)
	const showSelectMemorys = ref([])
	const memoryCategorys = ["体质", "防御", "攻击", "专精", "终端"]
	const roleIcons = {
		"尖锋": "https://r.qianqiu.info/app/icons/jianfeng.png",
		"铁御": "https://r.qianqiu.info/app/icons/tieyu.png",
		"轻卫": "https://r.qianqiu.info/app/icons/qingwei.png",
		"游徒": "https://r.qianqiu.info/app/icons/youtu.png",
		"筑术师": "https://r.qianqiu.info/app/icons/zhushu.png",
		"护佑者": "https://r.qianqiu.info/app/icons/huyou.png",
		"战术家": "https://r.qianqiu.info/app/icons/zhanshu.png",
	}
	const memoryLevelIcons = {
		"全等级": "https://r.qianqiu.info/app/icons/noe.png",
		"SSR": "https://r.qianqiu.info/app/icons/ssr.png",
		"SR": "https://r.qianqiu.info/app/icons/sr.png",
		"R": "https://r.qianqiu.info/app/icons/r.png",
	}
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
	//当前选中烙痕的下标
	const currentChooseMemory = ref(0)
	// 当前选中展示的烙痕
	const headMemoryCurrent = ref(0)


	// 打开烙痕选择框框
	function handleOpenMemoryBox(index) {
		if (headMemoryCurrent.value != index && memorys.value[index].memoryId) {
			headMemoryCurrent.value = index
			return
		}
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
				uni.toastFail("已选择该烙痕，无法重复选择~")
				return
			}
		}
		for (let i of memorys.value) {
			let flag = false
			if (i.skills && i.skills.length) {
				for (let j of i.skills) {
					if (j.name == '生命颂歌') {
						hasSmsg.value = true
						flag = true
						break;
					}
				}
				if (flag) break;
			}

		}
		memorys.value[currentChooseMemory.value] = memory
		headMemoryCurrent.value = currentChooseMemory.value
		memoryPopup.value.close()
	}
	let momeryDeleteIndex = 0
	// 删除烙痕前置
	function handleDeleteMemoryPre(index) {
		momeryDeleteIndex = index
		cb = 'removeMemory'
		confirm("", "是否删除" + memorys.value[index].name + '烙痕', true, '是的', '点错了')
	}
	//删除当前下标的角色
	function handleDeleteMemory() {
		let index = momeryDeleteIndex
		//移除当前元素
		memorys.value[index] = {}
		//如果index 和 当前激活的角色下表为同一个，就减1
		if (index == headMemoryCurrent.value) {
			for (let i = 0; i < memorys.value.length; i++) {
				if (memorys.value[i].memoryId) {
					headMemoryCurrent.value = i;
					break;
				}
			}
		}
	}

	//技能选择页面
	const expandShowInputBox = ref(false)
	const spSkills = ref([{}, {}, {}, {}])
	//查看角色队长技能
	function initLeaderSkills(val, beginInitIndex) {
		if (members.value.length && members.value[0].leaderSkills && members.value[0].leaderSkills.length) {
			for (let j of members.value[0].leaderSkills) {
				j.level = 3
			}
		}
	}
	//当前选中烙痕的下标
	const currentChooseSpSkill = ref(0)
	const spSkillPopup = ref()
	const skillQueryParam = ref('')
	const allSmallSkills = ref([])
	const showSmallSkills = ref([])
	const hasSmsg = ref(false)

	function handleOpenSpSkillBox(index) {
		currentChooseSpSkill.value = index
		spSkillPopup.value.open()
	}


	function initSpSkill(spNames) {
		if (spNames) {
			let count = 0;
			for (let j of spNames.split(",")) {
				for (let i of allSmallSkills.value) {
					if (j == i.name) {
						if (j == '生命颂歌') {
							hasSmsg.value = true
						}
						spSkills.value[count] = i
						spSkills.value[count].level = 3
						count += 1;
						if (count == 4) {
							return
						}
					}
				}
			}
			return
		}
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

	function handleChooseSkill(skill) {
		if (currentTemplateSpSkill.value == -1) {
			skill.level = 3
			if (skill.name == '生命颂歌') {
				hasSmsg.value = true
			}
			spSkills.value[currentChooseSpSkill.value] = skill
		} else {
			// 如果是模板那边过来的
			lrlItems5Input.value.skills[currentTemplateSpSkill.value] = skill
		}
		currentTemplateSpSkill.value = -1
		spSkillPopup.value.close()
	}
	// 联动调整所有涉及的小技能等级
	function handleChangeSkillLevel(skill, level) {
		for (let i = 0; i < memorys.value.length; i++) {
			changeSkillLevel(skill, level, i)
		}
		handleLeaderSkillLevel(skill, level, true)
		handleChangeSpSkillLevel(skill, level, true)
	}

	function changeSkillLevel(skill, level, index) {
		if (memorys.value[index].skills && memorys.value[index].skills.length) {
			for (let i = 0; i < memorys.value[index].skills.length; i++) {
				if (memorys.value[index].skills[i].name == skill.name) {
					memorys.value[index].skills[i].level = level
				}
			}
		}
	}

	function handleChangeSpSkillLevel(skill, level, flag) {
		for (let i = 0; i < spSkills.value.length; i++) {
			if (spSkills.value[i].name == skill.name) {
				spSkills.value[i].level = level
			}
		}
		// 基点技能、烙痕技能全部设置为相同的等级
		if (!flag) {
			handleLeaderSkillLevel(skill, level, true)
			for (let i = 0; i < memorys.value.length; i++) {
				changeSkillLevel(skill, level, i)
			}
		}
	}
	//调整队长技能等级
	function handleLeaderSkillLevel(skill, level, flag) {
		for (let i = 0; i < members.value[0].leaderSkills.length; i++) {
			if (members.value[0].leaderSkills[i].name == skill.name) {
				// 队长技能肯定3级
				members.value[0].leaderSkills[i].level = level
			}
		}
		// 基点技能、烙痕技能全部设置为相同的等级
		if (!flag) {
			handleChangeSpSkillLevel(skill, level, true)
			for (let i = 0; i < memorys.value.length; i++) {
				changeSkillLevel(skill, level, i)
			}
		}
	}
	const attribute = ref({
		attack: 1500,
		eattack: 1500,
		mastery: 1000,
		emastery: 1000,
		health: 1000,
		ehealth: 1000,
		defence: 1000,
		edefence: 1000,
		terminal: 1000,
		eterminal: 1000,
		abj: false,
		rl: 0,
		pl: 7,
		dkx: '',
		dfy: '',
		djs: '',
		drd: '',
		smsg: 1,
		zf: false,
		td: false
	})
	const currentChooseChar = ref(0)
	const currentChooseSkillType = ref(0)
	const removeMemberId = ref()
	const allComputeResult = ref([])
	const overviewDataCommon = ref([])
	const overviewDataLimit = ref([])
	const overviewDataExpand = ref({})

	function dotTo100Int(value, key) {
		if (key == "刻印攻击增加值") {
			return value
		}
		value = parseFloat(value)
		return Math.round(100.00 * value)
	}

	function dotTo100Float(value, limit) {
		value = parseFloat(value)
		value = Math.round(100.00 * value)
		return value > limit ? limit : value
	}

	function handleClickChangeOverview(item) {
		if (item.key == overviewDataExpand.value.key) {
			overviewDataExpand.value = {}
			return
		}
		overviewDataExpand.value = item
	}
	let computeParam = ""
	const finishCompute = ref(false)

	function handleClickChoosePage(v) {
		if (finishCompute && !v.disabled) {
			currentChooseChar.value = v.index
			computeShowInfo(v.index, currentChooseSkillType.value)
		}
	}

	function handleClickChooseSkillPage(v) {
		if (finishCompute) {
			currentChooseSkillType.value = v.index
			computeShowInfo(currentChooseChar.value, v.index)
		}
	}
	let isOverview = false
	//计算前置
	function handleClickToCompute(ignoreSure) {
		if (!members.value[0].characterId) {
			uni.toastFail("请选择队长~")
			return
		}
		let charIds = ''
		let plevels = ''
		for (let i of members.value) {
			if (i.characterId && i.characterId > 1000) {
				charIds += i.characterId + ","
				plevels += i.plevel + ","
			}
		}
		charIds = charIds.substring(0, charIds.length - 1)
		plevels = plevels.substring(0, plevels.length - 1)
		let memoryIds = ""
		let count = 0;
		for (let i of memorys.value) {
			if (i.memoryId && i.memoryId > 1000) {
				memoryIds += i.memoryId + ","
				count++;
			}
		}
		if (count < 1) {
			uni.toastFail("至少选择1张烙痕~")
			return
		}
		memoryIds = memoryIds.substring(0, memoryIds.length - 1)
		let att = attribute.value.health + "," + attribute.value.defence + "," + attribute.value.attack + "," + attribute
			.value.mastery + "," + attribute.value.terminal
		let eatt = Math.round(attribute.value.ehealth / 5) + "," + Math.round(attribute.value.edefence / 5) + "," + Math
			.round(attribute.value.eattack / 5) + "," +
			Math.round(attribute.value.emastery / 5) + "," + Math.round(attribute.value.eterminal / 5)
		let skillLevel = ""
		let memorySkills = []
		for (let i of memorys.value) {
			if (i.skills && i.skills.length) {
				let j = i.skills
				for (let nn of j) {
					if (nn.level) {
						memorySkills.push(nn)
					}
				}
			}
		}
		for (let i of memorySkills) {
			if (i) {
				skillLevel += (i.skillId + "_" + i.level) + ","
			}
		}
		for (let i of spSkills.value) {
			if (i.skillId) {
				skillLevel += (i.skillId + "_" + i.level) + ","
			}
		}
		for (let i of members.value[0].leaderSkills) {
			if (i) {
				skillLevel += (i.skillId + "_" + i.level) + ","
			}
		}
		skillLevel = skillLevel.substring(0, skillLevel.length - 1)
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
		sp2skills = sp2skills.substring(0, sp2skills.length - 1)
		if (attribute.value.pl > 9) {
			attribute.value.pl = 9
		}
		if (attribute.value.pl < 1) {
			attribute.value.pl = 1
		}
		if (attribute.value.rl > 9) {
			attribute.value.rl = 10
		}
		if (attribute.value.rl < 1) {
			attribute.value.rl = 1
		}
		if (!attribute.value.rl) {
			attribute.value.rl = 1
		}
		if (!attribute.value.dkx) {
			attribute.value.dkx = 0
		}
		if (!attribute.value.djs) {
			attribute.value.djs = 0
		}
		if (!attribute.value.dfy) {
			attribute.value.dfy = 0
		}
		if (!attribute.value.drd) {
			attribute.value.drd = 0
		}
		if (attribute.value.drd != '0' && attribute.value.drd) {
			let drds = attribute.value.drd.split("=")
			if (attribute.value.drd && drds.length != 2) {
				uni.toastFail("数据格式有误，例:炎属性弱点为80，应该填写为 炎=80")
				return
			}
			if (attribute.value.drd && drds[0] != '炎' && drds[0] != '水' && drds[0] != '雷' && drds[0] != '霜' && drds[0] !=
				'风' && drds[0] != '物理' && drds[0] != '蚀') {
				uni.toastFail("元素仅为:炎、水、雷、霜、风、物理、蚀")
				return
			}
		}
		if (attribute.value.dkx != '0' && attribute.value.dkx) {
			let dkxs = attribute.value.dkx.split("=")
			if (attribute.value.dkx && dkxs.length != 2) {
				uni.toastFail("数据格式有误，例:炎属性抗性为80，应该填写为 炎=80")
				return
			}
			if (attribute.value.dkx && dkxs[0] != '炎' && dkxs[0] != '水' && dkxs[0] != '雷' && dkxs[0] != '霜' && dkxs[0] !=
				'风' && dkxs[0] != '物理' && dkxs[0] != '蚀') {
				uni.toastFail("元素仅为:炎、水、雷、霜、风、物理、蚀")
				return
			}
		}
		if (!attribute.value.smsg || attribute.value.smsg < 0) {
			attribute.value.smsg = 0
		}
		if (!attribute.value.td) {
			attribute.value.td = false
		}
		computeParam = charIds + "/" + memoryIds + "?attribute=" + att + "&eattribute=" + eatt + "&skillLevel=" +
			skillLevel + "&sp2skillLevel=" + sp2skills +
			"&rLevel=" + attribute.value.rl + "&pLevel=" + plevels + "&smsg=" + attribute.value.smsg + "&abj=" + attribute
			.value.abj + "&td=" + attribute.value.td
		let flowers = ""
		for (let i = 0; i < (members.value.length + 1); i++) {
			if (members.value[i]) {
				let flower = members.value[i].flower
				if (flower > 5) {
					flower = 0
				}
				if (flower < 0) {
					flower = 0
				}
				if (!flower) {
					flower = 0
				}
				flowers += flower + ","
			}
		}
		flowers = flowers.substring(0, flowers.length - 1)
		computeParam += "&flower=" + flowers
		computeParam += "&dkx=" + attribute.value.dkx + "&dfy=" + attribute.value.dfy + "&djs=" + attribute.value.djs +
			"&zf=" + attribute.value.zf + "&drd=" + attribute.value.drd
		isOverview = ignoreSure
		if (ignoreSure) {
			return
		}
		cb = 'compute'
		confirm('', "开始计算？", true, "是的", "取消")
	}

	function goCompute() {
		computeDamageApi(computeParam, (res) => {
			if (isOverview) {
				overviewDataHandle(res)
				return
			}
			handleClickCurrentDamageOverview(0)
			allComputeResult.value = res
			allComputeResult.value
			// 默认展开普攻以及第一个角色
			computeShowInfo(currentChooseChar.value, currentChooseSkillType.value)
			finishCompute.value = true
		}, (fail) => {
			uni.toastFail("计算出错了~请联系开发人员~")
		})
	}

	function overviewDataHandle(res) {
		let overviewData = {}
		overviewDataLimit.value = []
		overviewDataCommon.value = []
		for (let key of Object.keys(res)) {
			if (res[key].total != '0' || res[key].limit != '0') {
				overviewData[key] = res[key]
			}
		}
		for (let key of Object.keys(overviewData)) {
			let dda = overviewData[key]
			dda.key = key
			if (res[key].limit != '0') {
				overviewDataLimit.value.push(dda)
			} else {
				overviewDataCommon.value.push(dda)
			}
		}
	}
	const attackList = ref([])
	// 默认不展开
	const showAttackIndex = ref(-1)

	function changeAttackIndex(index) {
		if (showAttackIndex.value == index) {
			showAttackIndex.value = -1
			return
		}
		showAttackIndex.value = index
	}
	const compareItemIndex = ref(-1)
	const totalDamage = ref(0)

	function computeShowInfo(charIndex, skillIndex) {
		showAttackIndex.value = -1
		let res = allComputeResult.value
		let arr = Object.keys(res)
		let charac = res[arr[charIndex]]
		let at = attackTypeList.value[skillIndex].name
		attackList.value = charac[at]
		totalDamage.value = charac['总计']
		compareItemIndex.value = -1
	}

	function handleExpandCompareRow(iiindex) {
		if (iiindex == compareItemIndex.value) {
			compareItemIndex.value = -1
		} else {
			compareItemIndex.value = iiindex
		}
	}

	const modal = ref()
	let cb = ''
	const modalData = ref({
		title: '温馨提醒',
		message: '',
		showCancelButton: true,
		confirmText: '确认',
		cancelText: '取消'
	})

	function confirm(title, message, showCancelButton, confirmText, cancelText) {
		if (title) modalData.value.title = title
		if (message) modalData.value.message = message
		modalData.value.showCancelButton = showCancelButton
		if (confirmText) modalData.value.confirmText = confirmText
		if (cancelText) modalData.value.cancelText = cancelText
		modal.value.open()
	}

	function cancelC() {}

	function sureC() {
		if ('removeMember' == cb) {
			handleDeleteMember()
		}
		if ('removeMemory' == cb) {
			handleDeleteMemory()
		}
		if ('compute' == cb) {
			goCompute()
		}
		if ('addTemplate' == cb) {
			sureAddTemplate()
		}
		if ('deleteTemplate' == cb) {
			sureDeleteTemplate()
		}
	}

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
		"霜": {
			"icon": "https://r.qianqiu.info/app/icons/element_shuang.png",
			"color": "rgba(64, 128, 203,.5)",
			"fcolor": "rgba(64, 128, 203,1)",
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
		"物理": {
			"icon": "https://r.qianqiu.info/app/icons/element_wuli.png",
			"color": "#24262b",
			"fcolor": "#fff",
		}
	}
	const attackTypeList = ref([{
		name: "普攻"
	}, {
		name: "指令"
	}, {
		name: "异核"
	}, {
		name: "自动"
	}])

	import {
		onLoad
	} from "@dcloudio/uni-app"

	function initCardData(r) {
		//初始化队长
		for (let i = r.chars.length - 1; i >= 0; i--) {
			currentChooseMember.value = i
			handleChooseMember(r.chars[i], i)
		}
		for (let i = r.memories.length - 1; i >= 0; i--) {
			currentChooseMemory.value = i
			handleChooseMemory(r.memories[i])
		}
		chareqsChoose.value = r.chareqs
		initSpSkill(r.spskills)
		proxy.$nextTick(() => {
			setTimeout(() => {
				handleClickCurrentDamageOverview(0)
			}, 250)
		})
	}
	let cardId = ""
	onLoad((opt) => {
		if (opt.cardId) {
			cardId = opt.cardId
		}
	})
	// 初始化所有烙痕
	function initAllMemory(suc) {
		getMemorysApi({}, (r) => {
			for (let j of Object.keys(r)) {
				allMemorys.push(...r[j])
			}
			filterMemoryData()
			if (suc) {
				suc()
			}
		})
	}
	//获取所有角色
	function initAllCharacter(suc) {
		getcharsApi('simple/0', (r) => {
			allChars.value = r
			showSelectChars.value = r.filter(e => e.element == ecategory[0])
			if (suc) {
				suc()
			}
		})
	}

	function initAllSmallSkills(callback) {
		getAllSmallSkillsApi((r) => {
			
			for (let jj of r) {
				if (jj.skillId >= chareqsStart && jj.skillId <= chareqsEnd) {
					allChareqs.value.push(jj)
					showChareqs.value.push(jj)
				}
				if(jj.skillId < chareqsStart) {
					allSmallSkills.value.push(jj)
					showSmallSkills.value.push(jj)
				}
			}
			callback()
		}, (fail) => {
			uni.toastFail("服务器出错了~请联系开发人员~")
		})
	}

	function initPageData() {
		initAllSmallSkills(ree => {
			getMineAllTemplate(hasTemplate => {
				// 分组ID不为空
				if (cardId) {
					initTemplateData(true)
					getShareComputeCardsInfo(cardId, (r) => {
						initCardData(r)
					}, (f) => {
						uni.toastFail("读取分享信息失败~请重试")
					})
				} else {
					initTemplateData(false)
				}
			})
		})
	}
	onMounted(r => {
		// 按照顺序初始化
		initAllMemory(() => {
			initAllCharacter(() => {
				initPageData()
			})
		})
	})

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

	function openChareqSelect(index) {
		currentChareqIndex = headMemberCurrent.value + "_" + index
		chareqPopup.value.open()
	}

	function handleChoosechareq(item) {
		chareqsChoose.value[currentChareqIndex.split("_")[0]][currentChareqIndex.split("_")[1]] = item
		chareqPopup.value.close()
	}

	function handleDeleteChareqPre(index) {
		chareqsChoose.value[headMemberCurrent.value][index] = {
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

	function initTemplateData(share) {
		// 开花数据在初始化角色的时候设置  不在这里设置
		// const item1 = lrlItems1.value[lrlItems1c.value]
		const item2 = lrlItems2.value[lrlItems2c.value].attr
		const item3 = lrlItems3.value[lrlItems3c.value].attr

		attribute.value.health = item2.split("_")[0]
		attribute.value.defence = item2.split("_")[1]
		attribute.value.attack = item2.split("_")[2]
		attribute.value.mastery = item2.split("_")[3]
		attribute.value.terminal = item2.split("_")[4]
		attribute.value.ehealth = item3.split("_")[0]
		attribute.value.edefence = item3.split("_")[1]
		attribute.value.eattack = item3.split("_")[2]
		attribute.value.emastery = item3.split("_")[3]
		attribute.value.eterminal = item3.split("_")[4]
		const item4 = lrlItems4.value[lrlItems4c.value]
		attribute.value.dfy = item4.dfy || 0
		attribute.value.dkx = item4.dkx || 0
		attribute.value.djs = item4.djs || 0
		attribute.value.drd = item4.drd || 0
		const item5 = lrlItems5.value[lrlItems5c.value]
		if (!share) {
			if (item5.length) {
				for (let i = 0; i < item5.length; i++) {
					if (item5[i].name == '生命颂歌') {
						hasSmsg.value = true
					}
					spSkills.value[i] = item5[i]
					spSkills.value[i].level = 3
				}
			}
		}
		let item6 = lrlItems6.value[lrlItems6c.value]
		attribute.value.abj = item6.abj
		attribute.value.zf = item6.zf
		attribute.value.td = item6.td || false
		attribute.value.rl = item6.rl
		attribute.value.smsg = item6.smsg
	}
	import {
		useSystemStore
	} from "@/stores/SystemStore.js"

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
<style lang="scss">
	::v-deep .uv-cell__title-text {
		color: white !important;
	}

	.ranks-body {
		position: fixed;
		width: 100%;
		max-width: 750rpx;
		left: 50%;
		transform: translateX(-50%);
		font-size: 26rpx;
		color: #333;
		min-height: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: #e9ecf1;

		.ranks-content {
			position: fixed;
			top: 55rpx;
			left: 0;
			width: 100%;
			height: calc(100% - 175rpx);
			// #ifdef MP-WEIXIN
			top: 0;
			height: calc(100% - 110rpx);
			// #endif
			padding: 20rpx;
			box-sizing: border-box;
			overflow: auto;

			.mask-icons {
				position: absolute;
				width: 60rpx;
				height: 120rpx;
				top: 30rpx;
				left: 30rpx;
				z-index: 999;
				display: flex;
				flex-direction: column;
				justify-content: space-between;

				.mask-icon {
					width: 60rpx;
					height: 60rpx;
				}
			}


			.ranks-content-head {
				position: relative;
				width: 100%;
				height: 392rpx;
				display: flex;
				justify-content: space-between;
				margin-bottom: 20rpx;

				.left-character-img {
					height: 100%;
					width: 280rpx;
					border: 4rpx solid #3467ff;

					.lc-img {
						width: 280rpx;
						height: 100%;
						object-fit: contain;
					}
				}

				.left-character-desc {
					position: relative;
					left: 0%;
					top: 0;
					border: 4rpx solid #3467ff;
					height: 392rpx;
					width: 280rpx;
					display: flex;
					align-items: center;
					justify-content: center;

					.lcd-img {
						width: 70rpx;
						height: 70rpx;
					}
				}

				.right-character-desc {
					position: relative;
					height: 392rpx;
					width: calc(100% - 300rpx);
					display: flex;
					flex-direction: column;
					justify-content: space-between;
					border: 4rpx solid #dde0e5;

					.rcd-title {
						position: relative;
						display: grid;
						grid-template-columns: repeat(2, 1fr);
						place-items: center;
						height: 60rpx;
						width: 100%;
						background-color: #dde0e5;
						color: #333;

						.item {
							position: relative;
							display: inline-block;
							color: rgb(190, 190, 190);
							font-size: 30rpx;
							height: 60rpx;
							display: flex;
							align-items: center;
							justify-content: center;
							flex-direction: column;
							color: #333;
						}

						.item:hover {
							cursor: pointer;
						}

						.item-active {
							color: black;
							font-weight: bold;
							transition: all linear .25s;
						}

						.item-active::after {
							position: absolute;
							content: '';
							height: 4rpx;
							bottom: 4rpx;
							width: 100%;
							background-color: #3566fe;
							transition: all linear .25s;
						}
					}

					.rcd-content {
						position: relative;
						height: 330rpx;
						width: 100%;
						box-sizing: border-box;
						background-color: #ffffff;
						overflow: auto;

						.rcdc-content {
							color: white;
							display: flex;
							height: 100%;
							flex-direction: column;
							padding: 10rpx;
							box-sizing: border-box;

							.rcdc-left-content {
								width: 160rpx;
								display: flex;
								flex-direction: column;
								justify-content: center;
							}

							.rcdc-right-content {
								width: auto;
								display: flex;
								flex-direction: column;
								justify-content: center;

								.line {
									max-width: 180rpx;
								}
							}

							.rcdc-data-lines {
								position: relative;
								max-width: 140rpx;

								.line-content {
									display: flex;
									width: 100%;
									flex-direction: row;
									align-items: center;
									justify-content: space-between;
									line-height: 60rpx;

									.rc2lc-img {
										width: 40rpx;
										height: 40rpx;
									}
								}
							}

							.line {
								display: flex;
								justify-content: space-between;
								max-width: 140rpx;
								line-height: 45rpx;

								.right-line {
									text-align: end;
								}
							}

							.line-notice {
								margin-top: 0;
								max-width: 160rpx;
								font-size: 32rpx;
								font-weight: bold;
								margin-bottom: 10rpx;
							}

							.leader-skill-card {
								position: relative;
								width: 100%;
								display: flex;
								flex-direction: column;
								color: black;

								.skill {
									position: relative;
									display: flex;
									flex-direction: row;
									align-items: center;
									width: 100%;
									height: 90rpx;
									margin-top: 10rpx;


									.skill-icon {
										width: 65rpx;
										height: 65rpx;
									}

									.skill-info {
										width: 140rpx;
										display: flex;
										flex-direction: column;
										margin-left: 10rpx;
										height: 80rpx;
										justify-content: space-between;

										.name {
											font-size: 26rpx;
											font-weight: 600;
											white-space: nowrap;
											/* 强制同一行内显示所有文本 */
											overflow: hidden;
											/* 隐藏超出的内容 */
											text-overflow: ellipsis;
											/* 当发生溢出时显示省略标记(...) */
											max-width: 230rpx;
											line-height: 40rpx;
										}

										.desc {
											position: relative;
											display: grid;
											grid-template-columns: repeat(4, 1fr);
											font-size: 30rpx;
											line-height: 40rpx;
											place-items: center;

											.lv {
												min-width: 30rpx;
												color: gray;
											}

											.val {
												min-width: 30rpx;
												color: #ff4500;
											}
										}
									}
								}

								.skill:first-child {
									margin-top: 5rpx;
								}
							}
						}

						.rcdc-content0 {
							display: grid;
							grid-template-columns: 1fr 1.5fr;
							gap: 10rpx;
							color: black;

							.left-line {
								color: black;
							}
						}

						.rcdc-content1 {
							color: black;

							.line-notice {
								max-width: 100%;
							}

							.line {
								max-width: 100%;
							}
						}
					}
				}
			}

			.ranks-content-area {
				margin-top: 10rpx;
				background-color: #dde0e5;
				padding: 15rpx;

				.title-line-nav {
					display: inline-block;
					padding: 7rpx 20rpx;
					letter-spacing: 2rpx;
					font-size: 24rpx;
					color: white;
					margin-bottom: 15rpx;
					background-color: #3467ff;
				}
			}

			.ranks-content-friend {
				position: relative;
				color: white;
				padding-bottom: 40rpx;
				border-bottom: 1px solid #3467ff;

				.fcf-line1 {
					width: 100%;
					display: grid;
					grid-template-columns: repeat(8, 1fr);
					gap: 5rpx;
				}

				.fcf-line2 {
					margin-top: 40rpx;
					height: 200rpx;
					display: grid;
					grid-template-columns: repeat(3, 1fr);
					place-content: center;
					place-items: center;

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
							white-space: nowrap;
							overflow: hidden;
							text-overflow: ellipsis;
						}

					}
				}

				.friend-card {
					position: relative;
					width: 100%;
					display: flex;
					flex-direction: column;
					align-items: center;

					.rcfc-avatar-box {
						width: 100%;
						height: 100rpx;
						background-color: white;
						display: flex;
						align-items: center;
						justify-content: center;

						.rab-delete-icon {
							position: absolute;
							width: 30rpx;
							height: 30rpx;
							left: -15rpx;
							top: -15rpx;
						}

						.avatar {
							height: 100rpx;
							width: 100%;
							object-fit: cover;
							display: block;
						}
					}

					.name {
						position: absolute;
						bottom: 30rpx;
						width: 100%;
						text-align: center;
						background-color: rgba(0, 0, 0, .5);
						font-size: 16rpx;
						font-weight: bold;
						white-space: nowrap;
						overflow: hidden;
						text-overflow: ellipsis;
						line-height: 30rpx;
					}

					.name1 {
						width: 100%;
						text-align: center;
						background-color: #3467ff;
						font-size: 16rpx;
						font-weight: bold;
						white-space: nowrap;
						overflow: hidden;
						text-overflow: ellipsis;
						line-height: 30rpx;
					}

					.chareq-preview-box {
						height: 30rpx;
						line-height: 30rpx;
						display: grid;
						grid-template-columns: repeat(3, 1fr);
						place-content: center;
						place-items: center;
						gap: 2rpx;

						.chareq-box {
							display: flex;
							width: 25rpx;
							height: 25rpx;
							align-items: center;
							justify-content: center;
							background-color: #ffffff;
						}

						.avatar {
							width: 25rpx;
							height: 25rpx;
						}
					}
				}
			}

			.rcib-sp-skill-box {
				position: relative;
				display: grid;
				grid-template-columns: 1fr 1fr;
				padding-bottom: 20rpx;
				border-bottom: 1px solid #3467ff;

				.rcib-sp-skill {
					position: relative;
					display: flex;
					flex-direction: row;
					align-items: center;
					width: 100%;
					height: 90rpx;
					color: black;

					.skill-icon {
						width: 70rpx;
						height: 70rpx;
					}

					.skill-info {
						max-width: 230rpx;
						display: flex;
						flex-direction: column;
						margin-left: 10rpx;
						height: 80rpx;
						justify-content: space-between;

						.name {
							font-size: 26rpx;
							font-weight: 600;
							white-space: nowrap;
							overflow: hidden;
							text-overflow: ellipsis;
							max-width: 230rpx;
							line-height: 40rpx;
						}

						.desc {
							position: relative;
							display: flex;
							justify-content: space-between;
							flex-direction: row;
							font-size: 30rpx;
							line-height: 40rpx;

							.lv-desc {
								min-width: 50rpx;
								font-size: 30rpx;
							}

							.lv {
								min-width: 40rpx;
							}

							.val {
								min-width: 40rpx;
								color: orangered;
							}
						}
					}
				}
			}

			.parent-line-title {
				width: 100%;
				display: flex;
				flex-direction: row;
				align-items: center;
				justify-content: space-between;
				margin-top: 20rpx;

				.plt-right {
					width: 500rpx;
					display: flex;
					flex-direction: row;

					.plt-name {
						margin-right: 20rpx;
					}

					.plt-name:last-child {
						margin-right: 0;
					}
				}

				.active-plt {
					color: #3467ff;
					font-size: 28rpx;
					font-weight: 600;
					text-decoration: underline;
				}
			}

			.ranks-content-memory {
				position: relative;
				width: 100%;
				margin-top: 20rpx;
				box-sizing: border-box;
				color: black;
				padding-bottom: 20rpx;
				border-bottom: 1rpx solid #3467ff;

				.rcm-memory-content {
					position: relative;
					width: 100%;
					background-color: white;
					z-index: 99;
					overflow: auto;
					margin-top: 0rpx;

					.rcm-memory-skills {
						position: relative;
						overflow: auto;

						.rcm-memory-skill-card {
							border-radius: 20rpx;
							display: grid;
							grid-template-columns: repeat(2, 1fr);
							place-items: center;

							.skill {
								position: relative;
								display: flex;
								flex-direction: row;
								align-items: center;
								width: 315rpx;
								height: 90rpx;

								.skill-icon {
									width: 70rpx;
									height: 70rpx;
								}

								.skill-info {
									width: 250rpx;
									display: flex;
									flex-direction: column;
									margin-left: 10rpx;
									height: 80rpx;
									justify-content: space-between;

									.name {
										font-size: 26rpx;
										font-weight: 600;
										white-space: nowrap;
										/* 强制同一行内显示所有文本 */
										overflow: hidden;
										/* 隐藏超出的内容 */
										text-overflow: ellipsis;
										/* 当发生溢出时显示省略标记(...) */
										max-width: 230rpx;
										line-height: 40rpx;
									}

									.desc {
										position: relative;
										display: flex;
										justify-content: space-between;
										flex-direction: row;
										font-size: 30rpx;
										line-height: 40rpx;
										color: gray;

										.lv-desc {
											min-width: 50rpx;
											font-size: 30rpx;
										}

										.lv {
											min-width: 40rpx;
										}

										.val {
											min-width: 40rpx;
											color: orangered;
										}
									}
								}
							}
						}

					}

				}

				.rcm-memory-cards-box {
					position: relative;
					width: 100%;
					height: 300rpx;
					display: grid;
					grid-template-columns: repeat(3, 1fr);
					place-items: start;
					grid-gap: 0 10rpx;

					.rcm-memory-card {
						position: relative;
						width: 100%;
						display: flex;
						flex-direction: column;
						align-items: center;
						justify-content: flex-start;

						.rcm-mc-box {
							position: relative;
							width: 100%;
							height: 100rpx;
							background-color: rgba(60, 60, 60, .5);
							display: flex;
							flex-direction: column;
							align-items: center;

							.rmc-mb-icon {
								position: absolute;
								height: 50rpx;
								width: 50rpx;
							}

							.rcm-delete-icon {
								position: absolute;
								left: -15rpx;
								top: -15rpx;
								width: 30rpx;
								height: 30rpx;
								z-index: 2;
							}

							.rmc-mb-icon-level {
								width: 50rpx;
								height: 50rpx;
								top: -20rpx;
								right: 2rpx;
								z-index: 2;
							}


							.rcm-memory-img {
								height: 100rpx;
								max-width: 220rpx;
							}

							.rcm-memory-no-img {
								position: absolute;
								top: 12rpx;
								height: 50rpx;
								width: 50rpx;
							}

							.rcm-memory-name1 {
								position: absolute;
								bottom: 0;
								color: white;
							}

							.rcm-memory-name {
								position: absolute;
								bottom: -10rpx;
								left: 10rpx;
								width: 100%;
								font-weight: 600;
								white-space: nowrap;
								overflow: hidden;
								text-overflow: ellipsis;
								color: white;
								font-weight: 600;
								font-size: 22rpx;
								z-index: 2;
							}

							.memory-card-bg {
								position: absolute;
								bottom: -23rpx;
								right: 0;
								width: 100%;
								height: 60rpx;
								z-index: 1;
								display: flex;
								overflow-x: hidden;
								overflow-y: auto;
								flex-direction: row-reverse;

								.memory-card-bg-img {
									height: 60rpx;
									object-fit: contain;
								}
							}
						}
					}

					.memory-choose-mask {
						position: absolute;
						z-index: 1;
						width: 100%;
						height: calc(100% + 25rpx);
						border: 6rpx solid #3467ff;

						.choose-text {
							position: absolute;
							bottom: 0;
							width: 100%;
							text-align: center;
							background-color: #3467ff;
							color: white;
							font-size: 16rpx;
							font-weight: 600;
						}
					}
				}
			}

			.ranks-content-input-box {
				position: relative;
				width: 100%;
				margin-top: 20rpx;
				padding: 0 1rpx;
				box-sizing: border-box;
				transition: all linear .25s;
				overflow: hidden;
				display: grid;
				place-items: center;
				grid-template-columns: repeat(2, 1fr);
				gap: 15rpx;
				padding-bottom: 20rpx;
				border-bottom: 1rpx solid #3467ff;

				.input {
					width: 100%;
					border: 1rpx solid black;
					background-color: #e9edef;
				}
			}

			.rank-damage-overview-area {
				margin-top: 20rpx;
				padding-bottom: 20rpx;
				box-sizing: border-box;
				color: black;
				border-bottom: 1rpx solid #3467ff;

				.rdoa-item-expand {
					margin: 20rpx;
					box-sizing: border-box;
					display: grid;
					grid-template-columns: repeat(2, 1fr);
					gap: 20rpx;
					font-size: 22rpx;

					.rie-item {
						display: flex;
						align-items: center;
						justify-content: space-between;
						line-height: 36rpx;

						.ri-text {}

						.ri-val {
							font-weight: 600;
							color: #3467ff;
						}
					}
				}

				.rdoa-item {
					display: grid;
					grid-template-columns: 4.5fr 4.5fr .7fr;
					gap: 10rpx;
					font-size: 24rpx;
					align-items: center;
					min-height: 50rpx;

					.ri-left {
						font-weight: 600;

						.ril-tips {
							font-weight: 500;
						}
					}

					.ri-center {
						display: grid;
						grid-template-columns: 7fr 2fr;
						gap: 10rpx;
						align-items: center;
						font-weight: 600;
						color: #3467ff;

						.ric-progress {
							position: relative;
							width: 100%;
							height: 80%;
							background-color: #e9edef;
							border: 1rpx solid #3467ff;
							transition: all .5 linear;

							.ric-mask {
								position: absolute;
								height: 100%;
								top: 0;
								left: 0;
								background-color: #3467ff;
							}
						}

						.ric-text {
							display: flex;
							flex-direction: row;
							align-items: center;
							justify-content: end;
						}
					}

					.ri-right {
						display: flex;
						flex-direction: row;
						align-items: center;
						justify-content: end;
						opacity: .5;
					}
				}

			}

			.ranks-content-compute-area {
				margin-top: 20rpx;
				box-sizing: border-box;
				color: black;

				.rcca-nav-item {
					font-size: 26rpx;
					line-height: 50rpx;
					border-color: black;
					font-weight: 600;
				}

				.damage-title {
					font-size: 28rpx;
					font-weight: bold;
					line-height: 50rpx;
					color: #3c9cff;
				}

				.damage {
					margin-top: 10rpx;
					padding-bottom: 20rpx;
					border-bottom: 1px solid #3c9cff;

					.damage-item {
						font-size: 24rpx;
						line-height: 45rpx;

						.waring {
							color: red;
							font-weight: 600;
						}

						.val {
							color: orangered;
						}

						.mn {
							color: skyblue;
						}
					}
				}

				.compare-item {
					.compare-damage {
						margin-top: 10rpx;
						padding-bottom: 10rpx;
						display: flex;
						align-items: center;
						justify-content: space-between;
					}

					.damage-item {
						font-size: 26rpx;
						letter-spacing: 1rpx;
						line-height: 45rpx;
					}

					.compare-end {
						display: flex;
						align-items: center;
						justify-content: space-between;
					}

					.waring {
						color: red;
						font-weight: 600;
					}

					.val {
						color: #3bbe5d;
						font-weight: 600;
					}

					img {
						margin-left: 20rpx;
						opacity: .2;
					}

					.compare-detail {
						letter-spacing: 1rpx;
						padding: 0 60rpx 0 40rpx;
						font-size: 24rpx;

						.damage-item {
							font-size: 24rpx;
						}
					}
				}

				.damage:first-child {
					margin-top: 0;
				}

				.suggest {
					margin-top: 20rpx;
					padding-bottom: 20rpx;
					border-bottom: 1px solid #3c9cff;

					.suggest-line {
						font-size: 26rpx;
						line-height: 45rpx;

						.warning {
							color: red;
							font-weight: bold;
						}

						.val {
							font-size: bold;
						}
					}
				}

				.result {
					padding-bottom: 20rpx;
					border-bottom: 1px solid black;
					color: black;

					.notice {
						line-height: 35rpx;
						color: red;
						font-weight: bold;
					}

					.result-line {
						font-size: 26rpx;
						line-height: 45rpx;

						.warning {
							color: red;
							font-weight: bold;
						}

						.val {
							font-size: bold;
						}
					}
				}
			}

		}

		.popup-card-box {
			position: relative;
			width: 100%;
			padding: 20rpx;
			box-sizing: border-box;
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

		.line-title {
			position: relative;
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: space-between;
			font-size: 30rpx;
			line-height: 80rpx;
			font-weight: bold;
			color: #000;
			overflow: hidden;
			border-bottom: 1px solid #000;
		}

		.line-title-icon {
			width: 40rpx;
			height: 40rpx;
		}

		.ccc-content {
			position: absolute;
			left: 0;
			padding: 20rpx;
			box-sizing: border-box;
			z-index: -1;
			width: 100%;
			box-sizing: border-box;
			background-image: url("https://r.qianqiu.info/app/bac-img.jpg");
			background-repeat: no-repeat;
			background-size: cover;

			.friend {
				margin: 20rpx 0;
			}

			.ccc-content-title {
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

				.ccc-left {
					width: 360rpx;
				}

				img {
					max-width: 100%;
					max-height: 100%;
					border-radius: 20rpx;
				}
			}
		}

		.bottom-nav-line {
			position: fixed;
			width: 100%;
			max-width: 750rpx;
			height: 110rpx;
			bottom: 0rpx;
			background-color: rgba(230, 232, 243, .7);
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
						margin-top: 15rpx;
						line-height: 20rpx;
						font-size: 20rpx;
						color: black;
						font-weight: 600;
					}
				}

			}

			.notice {
				line-height: 80rpx;
				height: 80rpx;
				font-size: 24rpx;
				color: #3467ff;
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
					background-color: #3467ff;
					color: white;
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

		.rank-template-body {
			position: relative;
			height: 100%;
			padding: 20rpx;
			box-sizing: border-box;
			display: grid;
			gap: 20rpx;
			grid-template-columns: 1fr 2fr;

			.rtv-left {
				border-top: 1rpx solid black;

				.rtvl-item {
					line-height: 60rpx;
					text-align: center;
					font-weight: 600;
					border-bottom: 1rpx solid black;
					border-left: 1rpx solid black;
					border-right: 1rpx solid black;
				}

				.active-rtvl-item {
					background-color: #3467ff;
					color: white;
				}
			}

			.rtv-right {
				position: relative;
				overflow: auto;
				display: flex;
				flex-direction: column;
				align-items: center;

				.add-template-submit {
					background-color: #3467ff;
					color: white;
					padding: 15rpx 40rpx;
					box-shadow: 10rpx 10rpx 5rpx rgba(52, 103, 255, .3);
				}

				.rtvl-item {
					width: 100%;
					padding-bottom: 20rpx;

					.rtvli-title {
						line-height: 50rpx;
						font-weight: 600;
						font-size: 30rpx;
						display: flex;
						flex-direction: row;
						align-items: center;
						justify-content: space-between;
						width: 100%;

						.rtvli-t-left {
							max-width: 70%;
							white-space: nowrap;
							/* 强制同一行内显示所有文本 */
							overflow: hidden;
							/* 隐藏超出的内容 */
							text-overflow: ellipsis;
						}

						.rtvli-t-right {
							text-align: end;
							color: #3467ff;
						}
					}

					.rtvli-content {
						display: grid;
						grid-template-columns: 1fr 1fr;
						border-top: 1rpx solid #686868;
						border-left: 1rpx solid #686868;

						.rtv-c-item {
							display: flex;
							flex-direction: row;
							align-items: center;
							justify-content: space-between;
							line-height: 45rpx;
							border-bottom: 1rpx solid #686868;
							border-right: 1rpx solid #686868;

							.rtvc-left {
								width: 40%;
								height: 100%;
								font-size: 26rpx;
								background-color: #8c8e90;
								color: white;
								display: flex;
								align-items: center;
								justify-content: center;
								align-items: center;
								border-right: 1rpx solid #686868;
							}

							.rtvc-right {
								width: 60%;
								font-size: 24rpx;
								text-align: center;
								display: flex;
								flex-direction: row;
								justify-content: center;
								align-items: center;

								input {
									border-bottom: 1rpx solid #3467ff;
									line-height: 40rpx;
								}

								.rr {
									font-size: 22rpx;
								}

								.r1 {
									width: 50rpx;
								}

								.r2 {
									width: 80%;
								}

								.r-title {
									width: 80%;
								}
							}
						}

						.jidian-item {
							.rtvc-left {
								color: #3467ff;
								text-decoration: underline;
								font-size: 24rpx;
								height: 90rpx;

								.skill-icon {
									width: 80rpx;
									height: 80rpx;
								}
							}
						}
					}

					.enemy-content {
						grid-template-columns: 1fr;
					}

					.add-content {
						grid-template-columns: 1fr;

						.rtv-c-item {
							.rtvc-right {
								padding: 10rpx 0;
							}
						}
					}
				}
			}

			.add-btn {
				background-color: #3467ff;
				color: white;
				font-weight: 600;
				letter-spacing: 1rpx;
				bottom: 40rpx;
				right: 40rpx;
				position: absolute;
				padding: 20rpx 40rpx;
				box-shadow: 10rpx 10rpx 5rpx rgba(52, 103, 255, .3);
			}

			.active-add-btn {
				color: #3467ff;
				background-color: white;
			}
		}
	}

	::v-deep .uv-sticky {
		top: -5rpx !important;
	}
</style>