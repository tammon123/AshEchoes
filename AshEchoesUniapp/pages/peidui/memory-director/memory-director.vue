<template>
	<view class='md-body' id='parant-body'>
		// #ifdef WEB
		<TopNavBar :showAccount='false' @exit='showNavFunc' child='true'></TopNavBar>
		// #endif
		<AeIframe @exit='wikiUrl=""' :url='wikiUrl'></AeIframe>
		<template v-if="!showNav">
			<view class="md-content">
				<view class="head-content">
					<view class="hc-title">
						①烙痕设置
					</view>
					<view class="hc-top-tips">
						<uv-button type="info" @click='openMemoryBox' text="查看烙痕"></uv-button>
						<view style="text-align: left;color: #333;font-size: 24rpx;">
							点击左侧<span @click='openMemoryBox' style="color: black;text-decoration: underline;font-weight: 600;">查看烙痕</span>按钮，再点击烙痕右下角图标或文字可以<span style="color:red">手动设置烙痕等级</span>
						</view>
					</view>
					<view class="ranks-content-input-box">
						<view class="input">
							<uv-input inputAlign="right" type='number' color='black' disabled
								>
								<template v-slot:prefix>
									<uv-text text="活动卡满破:" margin="0 3px 0 0" type="primary"></uv-text>
								</template>
								<template v-slot:suffix>
									<uv-switch activeColor='#3c9cff' @change='hignLevelSwitchCallback'  v-model="hignLevelSwitch.s1"></uv-switch>
								</template>
							</uv-input>
						</view>
						<view class="input">
							<uv-input inputAlign="right" type='number' color='black' disabled
								>
								<template v-slot:prefix>
									<uv-text text="大月卡满破:" margin="0 3px 0 0" type="primary"></uv-text>
								</template>
								<template v-slot:suffix>
									<uv-switch activeColor='#3c9cff' @change='hignLevelSwitchCallback'  v-model="hignLevelSwitch.s2"></uv-switch>
								</template>
							</uv-input>
						</view>
						<view class="input">
							<uv-input inputAlign="right" type='number' color='black' disabled
								>
								<template v-slot:prefix>
									<uv-text text="主线卡满破:" margin="0 3px 0 0" type="primary"></uv-text>
								</template>
								<template v-slot:suffix>
									<uv-switch activeColor='#3c9cff' @change='hignLevelSwitchCallback'  v-model="hignLevelSwitch.s3"></uv-switch>
								</template>
							</uv-input>
						</view>
						<view class="input">
							<uv-input inputAlign="right" type='number' color='black' disabled
								>
								<template v-slot:prefix>
									<uv-text text="抽取卡满破:" margin="0 3px 0 0" type="primary"></uv-text>
								</template>
								<template v-slot:suffix>
									<uv-switch activeColor='#3c9cff' @change='hignLevelSwitchCallback'  v-model="hignLevelSwitch.s4"></uv-switch>
								</template>
							</uv-input>
						</view>
					</view>
					
					
					<view class="hc-title">
						②属性优先级设置
					</view>
					<view style="text-align: left;margin-top: 20rpx;margin-bottom: 20rpx;font-size: 22rpx;color:black;">
						按顺序打开开关即可设置优先属性，计算优先级跟计算系数对应
					</view>
					<view class="ranks-content-input-box">
						<view class="input">
							<uv-input inputAlign="right" type='number' color='black' disabled
								v-model="attribute.iattack">
								<template v-slot:prefix>
									<uv-text text="攻击:" type="primary" margin="0 3px 0 0"></uv-text>
								</template>
								<template v-slot:suffix>
									<uv-switch activeColor='#3c9cff' @change='switchCallback' :active-value="3"
										:inactive-value="-3" v-model="attribute.attack"></uv-switch>
								</template>
							</uv-input>
						</view>
						<view class="input">
							<uv-input inputAlign="right" type='number' color='black' disabled
								v-model="attribute.imastery">
								<template v-slot:prefix>
									<uv-text text="专精:" margin="0 3px 0 0" type="primary"></uv-text>
								</template>
								<template v-slot:suffix>
									<uv-switch activeColor='#3c9cff' @change='switchCallback' :active-value="4"
										:inactive-value="-4" v-model="attribute.mastery"></uv-switch>
								</template>
							</uv-input>
						</view>
						<view class="input">
							<uv-input inputAlign="right" type='number' color='black' disabled
								v-model="attribute.ihealth">
								<template v-slot:prefix>
									<uv-text text="体质:" type="primary" margin="0 3px 0 0"></uv-text>
								</template>
								<template v-slot:suffix>
									<uv-switch activeColor='#3c9cff' @change='switchCallback' :active-value="1"
										:inactive-value="-1" v-model="attribute.health"></uv-switch>
								</template>
							</uv-input>
						</view>
						<view class="input">
							<uv-input inputAlign="right" type='number' color='black' disabled
								v-model="attribute.idefence">
								<template v-slot:prefix>
									<uv-text text="防御:" type="primary" margin="0 3px 0 0"></uv-text>
								</template>
								<template v-slot:suffix>
									<uv-switch activeColor='#3c9cff' @change='switchCallback' :active-value="2"
										:inactive-value="-2" v-model="attribute.defence"></uv-switch>
								</template>
							</uv-input>
						</view>
						<view class="input">
							<uv-input inputAlign="right" type='number' color='black' disabled
								v-model="attribute.iterminal">
								<template v-slot:prefix>
									<uv-text text="终端:" margin="0 3px 0 0" type="primary"></uv-text>
								</template>
								<template v-slot:suffix>
									<uv-switch activeColor='#3c9cff' @change='switchCallback' :active-value="5"
										:inactive-value="-5" v-model="attribute.terminal"></uv-switch>
								</template>
							</uv-input>
						</view>
						<view class="input">
							<uv-button type="primary" @click='toCompute' text="生成监督刻印"></uv-button>
						</view>
						
					</view>
					<view style="text-align: left;margin-top: 10rpx;margin-bottom: 20rpx;font-size: 22rpx;color:black;">
						【高级设置】属性计算优先级系数设置，非必要无需修改
					</view>
					
					<view class="input" style="margin-bottom: 10rpx;">
						<uv-input  inputAlign="right" type='text' color='black' 
							v-model="computeRate">
							<template v-slot:prefix>
								<uv-text text="计算系数:" type="primary" margin="0 3px 0 0"></uv-text>
							</template>
						</uv-input>
					</view>
				</view>
				<view class="hc-title">
					③监督刻印展示
				</view>
				<view class="md-memory-content">
					<view class="bigbox-memory-card" style="background-color: rgba(158, 88, 91, 0.5);">
						<view class="card-title">
							攻击
						</view>
						<template v-for="(memory,memoryIndex) in attackArr" :key='memory.memoryId'>
							<view class="card">
								<view class='pc-img-box'>
									 
									<img v-if="memory.img" :src="memory.img" mode='widthFix' alt="" class="pc-img" @click="memoryToWikiDetail(memory.name)">
									<!-- <img v-else src="https://r.qianqiu.info/app/icons/plus.png" mode='widthFix' alt=""
										class="pc-no-img">-->
								</view>
								<view class='base-value' v-if="memory.value">
									<view style="background-color: rgba(92, 152, 145, .85);" class="bv-h bv">
										{{memory.value.split(',')[0]}}
									</view>
									<view class="bv-d bv" style="background-color: rgba(51, 111, 182, .85);">
										{{memory.value.split(',')[1]}}
									</view>
									<view class="bv-a bv" style="background-color: rgba(158, 88, 91, .85);">
										{{memory.value.split(',')[2]}}
									</view>
									<view class="bv-m bv" style="background-color: rgba(156, 135, 73, .85);">
										{{memory.value.split(',')[3]}}
									</view>
									<view class="bv-t bv" style="background-color: rgba(114, 100, 165, .85);">
										{{memory.value.split(',')[4]}}
									</view>
								</view>
								<view class="float-tips" v-if="memory.count">
									<view v-if="memoryIndex==0">
										<view v-if="memory.rank=='R'">600%</view>
										<view v-if="memory.rank=='SR'">{{680+memory.count*20}}%</view>
										<view v-if="memory.rank=='SSR'">{{760+memory.count*40}}%</view>
									</view>
									<view v-else>
										<view v-if="memory.rank=='R'">60%</view>
										<view v-if="memory.rank=='SR'">{{68+memory.count*2}}%</view>
										<view v-if="memory.rank=='SSR'">{{76+memory.count*4}}%</view>
									</view>
								</view>
								<view class="choose-name"
									:style="{'background':'linear-gradient(to right, '+memoryCategoryAttr['攻击'].lgcolor1+' 0%, '+memoryCategoryAttr['攻击'].lgcolor2+' 30%, '+memoryCategoryAttr['攻击'].lgcolor3+' 100%)'}">
									{{memory.name}}
									<view class="memory-level-icon" v-if="memory.count">
										<template v-if="(memory.count+0) == 0">
											未持有
										</template>
										<template v-else>
											<template v-for="item in (memory.count - 1)">
												<img class='li-img' src="https://r.qianqiu.info/app/icons/m-c2.png"
													alt="">
											</template>
											<template v-for="item in (6 - memory.count)">
												<img class='li-img' src="https://r.qianqiu.info/app/icons/m-c1.png"
													alt="">
											</template>
										</template>
									</view>
								</view>
							</view>
						</template>
					</view>
					<view class="bigbox-memory-card" style="background-color: rgba(156, 135, 73, 0.5);">
						<view class="card-title">
							专精
						</view>
						<template v-for="(memory,memoryIndex) in masteryArr" :key='memory.memoryId'>
							<view class="card">
								<view class='pc-img-box'>
									<img v-if="memory.img" :src="memory.img" mode='widthFix' alt="" class="pc-img" @click="memoryToWikiDetail(memory.name)">
									<!-- <img v-else src="https://r.qianqiu.info/app/icons/plus.png" mode='widthFix' alt=""
										class="pc-no-img">-->
								</view>
								<view class='base-value' v-if="memory.value">
									<view style="background-color: rgba(92, 152, 145, .85);" class="bv-h bv">
										{{memory.value.split(',')[0]}}
									</view>
									<view class="bv-d bv" style="background-color: rgba(51, 111, 182, .85);">
										{{memory.value.split(',')[1]}}
									</view>
									<view class="bv-a bv" style="background-color: rgba(158, 88, 91, .85);">
										{{memory.value.split(',')[2]}}
									</view>
									<view class="bv-m bv" style="background-color: rgba(156, 135, 73, .85);">
										{{memory.value.split(',')[3]}}
									</view>
									<view class="bv-t bv" style="background-color: rgba(114, 100, 165, .85);">
										{{memory.value.split(',')[4]}}
									</view>
								</view>
								<view class="float-tips" v-if="memory.count">
									<view v-if="memoryIndex==0">
										<view v-if="memory.rank=='R'">600%</view>
										<view v-if="memory.rank=='SR'">{{680+memory.count*20}}%</view>
										<view v-if="memory.rank=='SSR'">{{760+memory.count*40}}%</view>
									</view>
									<view v-else>
										<view v-if="memory.rank=='R'">60%</view>
										<view v-if="memory.rank=='SR'">{{68+memory.count*2}}%</view>
										<view v-if="memory.rank=='SSR'">{{76+memory.count*4}}%</view>
									</view>
								</view>
								<view class="choose-name"
									:style="{'background':'linear-gradient(to right, '+memoryCategoryAttr['专精'].lgcolor1+' 0%, '+memoryCategoryAttr['专精'].lgcolor2+' 30%, '+memoryCategoryAttr['专精'].lgcolor3+' 100%)'}">
									{{memory.name}}
									<view class="memory-level-icon" v-if="memory.count">
										<template v-if="(memory.count+0) == 0">
											未持有
										</template>
										<template v-else>
											<template v-for="item in (memory.count - 1)">
												<img class='li-img' src="https://r.qianqiu.info/app/icons/m-c2.png"
													alt="">
											</template>
											<template v-for="item in (6 - memory.count)">
												<img class='li-img' src="https://r.qianqiu.info/app/icons/m-c1.png"
													alt="">
											</template>
										</template>
									</view>
								</view>
							</view>
						</template>
					</view>

					<view class="bigbox-memory-card" style="background-color: rgba(92, 152, 145, 0.5);">
						<view class="card-title">
							体质
						</view>
						<template v-for="(memory,memoryIndex) in healthArr" :key='memory.memoryId'>
							<view class="card">
								<view class='pc-img-box'>
									<img v-if="memory.img" :src="memory.img" mode='widthFix' alt="" class="pc-img" @click="memoryToWikiDetail(memory.name)">
									<!-- <img v-else src="https://r.qianqiu.info/app/icons/plus.png" mode='widthFix' alt=""
										class="pc-no-img">-->
								</view>
								<view class='base-value' v-if="memory.value">
									<view style="background-color: rgba(92, 152, 145, .85);" class="bv-h bv">
										{{memory.value.split(',')[0]}}
									</view>
									<view class="bv-d bv" style="background-color: rgba(51, 111, 182, .85);">
										{{memory.value.split(',')[1]}}
									</view>
									<view class="bv-a bv" style="background-color: rgba(158, 88, 91, .85);">
										{{memory.value.split(',')[2]}}
									</view>
									<view class="bv-m bv" style="background-color: rgba(156, 135, 73, .85);">
										{{memory.value.split(',')[3]}}
									</view>
									<view class="bv-t bv" style="background-color: rgba(114, 100, 165, .85);">
										{{memory.value.split(',')[4]}}
									</view>
								</view>
								<view class="float-tips" v-if="memory.count">
									<view v-if="memoryIndex==0">
										<view v-if="memory.rank=='R'">600%</view>
										<view v-if="memory.rank=='SR'">{{680+memory.count*20}}%</view>
										<view v-if="memory.rank=='SSR'">{{760+memory.count*40}}%</view>
									</view>
									<view v-else>
										<view v-if="memory.rank=='R'">60%</view>
										<view v-if="memory.rank=='SR'">{{68+memory.count*2}}%</view>
										<view v-if="memory.rank=='SSR'">{{76+memory.count*4}}%</view>
									</view>
								</view>
								<view class="choose-name"
									:style="{'background':'linear-gradient(to right, '+memoryCategoryAttr['体质'].lgcolor1+' 0%, '+memoryCategoryAttr['体质'].lgcolor2+' 30%, '+memoryCategoryAttr['体质'].lgcolor3+' 100%)'}">
									{{memory.name}}
									<view class="memory-level-icon" v-if="memory.count">
										<template v-if="(memory.count+0) == 0">
											未持有
										</template>
										<template v-else>
											<template v-for="item in (memory.count - 1)">
												<img class='li-img' src="https://r.qianqiu.info/app/icons/m-c2.png"
													alt="">
											</template>
											<template v-for="item in (6 - memory.count)">
												<img class='li-img' src="https://r.qianqiu.info/app/icons/m-c1.png"
													alt="">
											</template>
										</template>
									</view>
								</view>
							</view>
						</template>
					</view>
					<view class="bigbox-memory-card" style="background-color: rgba(51, 111, 182, 0.5);">
						<view class="card-title">
							防御
						</view>
						<template v-for="(memory,memoryIndex) in defenceArr" :key='memory.memoryId'>
							<view class="card">
								<view class='pc-img-box'>
									<img v-if="memory.img" :src="memory.img" mode='widthFix' alt="" class="pc-img" @click="memoryToWikiDetail(memory.name)">
									<!-- <img v-else src="https://r.qianqiu.info/app/icons/plus.png" mode='widthFix' alt=""
										class="pc-no-img">-->
								</view>
								<view class='base-value' v-if="memory.value">
									<view style="background-color: rgba(92, 152, 145, .85);" class="bv-h bv">
										{{memory.value.split(',')[0]}}
									</view>
									<view class="bv-d bv" style="background-color: rgba(51, 111, 182, .85);">
										{{memory.value.split(',')[1]}}
									</view>
									<view class="bv-a bv" style="background-color: rgba(158, 88, 91, .85);">
										{{memory.value.split(',')[2]}}
									</view>
									<view class="bv-m bv" style="background-color: rgba(156, 135, 73, .85);">
										{{memory.value.split(',')[3]}}
									</view>
									<view class="bv-t bv" style="background-color: rgba(114, 100, 165, .85);">
										{{memory.value.split(',')[4]}}
									</view>
								</view>
								<view class="float-tips" v-if="memory.count">
									<view v-if="memoryIndex==0">
										<view v-if="memory.rank=='R'">600%</view>
										<view v-if="memory.rank=='SR'">{{680+memory.count*20}}%</view>
										<view v-if="memory.rank=='SSR'">{{760+memory.count*40}}%</view>
									</view>
									<view v-else>
										<view v-if="memory.rank=='R'">60%</view>
										<view v-if="memory.rank=='SR'">{{68+memory.count*2}}%</view>
										<view v-if="memory.rank=='SSR'">{{76+memory.count*4}}%</view>
									</view>
								</view>
								<view class="choose-name"
									:style="{'background':'linear-gradient(to right, '+memoryCategoryAttr['防御'].lgcolor1+' 0%, '+memoryCategoryAttr['防御'].lgcolor2+' 30%, '+memoryCategoryAttr['防御'].lgcolor3+' 100%)'}">
									{{memory.name}}
									<view class="memory-level-icon" v-if="memory.count">
										<template v-if="(memory.count+0) == 0">
											未持有
										</template>
										<template v-else>
											<template v-for="item in (memory.count - 1)">
												<img class='li-img' src="https://r.qianqiu.info/app/icons/m-c2.png"
													alt="">
											</template>
											<template v-for="item in (6 - memory.count)">
												<img class='li-img' src="https://r.qianqiu.info/app/icons/m-c1.png"
													alt="">
											</template>
										</template>
									</view>
								</view>
							</view>
						</template>
					</view>
					<view class="bigbox-memory-card" style="background-color: rgba(114, 100, 165, 0.5);">
						<view class="card-title">
							终端
						</view>
						<template v-for="(memory,memoryIndex) in terminalArr" :key='memory.memoryId'>
							<view class="card">
								<view class='pc-img-box'>
									<img v-if="memory.img" :src="memory.img" mode='widthFix' alt="" class="pc-img" @click="memoryToWikiDetail(memory.name)">
									<!-- <img v-else src="https://r.qianqiu.info/app/icons/plus.png" mode='widthFix' alt=""
										class="pc-no-img">-->
								</view>
								<view class='base-value' v-if="memory.value">
									<view style="background-color: rgba(92, 152, 145, .85);" class="bv-h bv">
										{{memory.value.split(',')[0]}}
									</view>
									<view class="bv-d bv" style="background-color: rgba(51, 111, 182, .85);">
										{{memory.value.split(',')[1]}}
									</view>
									<view class="bv-a bv" style="background-color: rgba(158, 88, 91, .85);">
										{{memory.value.split(',')[2]}}
									</view>
									<view class="bv-m bv" style="background-color: rgba(156, 135, 73, .85);">
										{{memory.value.split(',')[3]}}
									</view>
									<view class="bv-t bv" style="background-color: rgba(114, 100, 165, .85);">
										{{memory.value.split(',')[4]}}
									</view>
								</view>
								<view class="float-tips" v-if="memory.count">
									<view v-if="memoryIndex==0">
										<view v-if="memory.rank=='R'">600%</view>
										<view v-if="memory.rank=='SR'">{{680+memory.count*20}}%</view>
										<view v-if="memory.rank=='SSR'">{{760+memory.count*40}}%</view>
									</view>
									<view v-else>
										<view v-if="memory.rank=='R'">60%</view>
										<view v-if="memory.rank=='SR'">{{68+memory.count*2}}%</view>
										<view v-if="memory.rank=='SSR'">{{76+memory.count*4}}%</view>
									</view>
								</view>
								<view class="choose-name"
									:style="{'background':'linear-gradient(to right, '+memoryCategoryAttr['终端'].lgcolor1+' 0%, '+memoryCategoryAttr['终端'].lgcolor2+' 30%, '+memoryCategoryAttr['终端'].lgcolor3+' 100%)'}">
									{{memory.name}}
									<view class="memory-level-icon" v-if="memory.count">
										<template v-if="(memory.count+0) == 0">
											未持有
										</template>
										<template v-else>
											<template v-for="item in (memory.count - 1)">
												<img class='li-img' src="https://r.qianqiu.info/app/icons/m-c2.png"
													alt="">
											</template>
											<template v-for="item in (6 - memory.count)">
												<img class='li-img' src="https://r.qianqiu.info/app/icons/m-c1.png"
													alt="">
											</template>
										</template>
									</view>
								</view>
							</view>
						</template>
					</view>
				</view>
			</view>

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
								@click="handleChangeMLevel(item)"
								v-for="(item, index) in Object.keys(memoryLevelIcons)">
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
								<img @click="memoryToWikiDetail(char.name)" :src="char.img" mode='widthFix' alt="" class="pc-img">
							</view>
							<view class='base-value'>
								<view style="background-color: rgba(92, 152, 145, .85);" class="bv-h bv">
									{{char.value.split(',')[0]}}
								</view>
								<view class="bv-d bv" style="background-color: rgba(51, 111, 182, .85);">
									{{char.value.split(',')[1]}}
								</view>
								<view class="bv-a bv" style="background-color: rgba(158, 88, 91, .85);">
									{{char.value.split(',')[2]}}
								</view>
								<view class="bv-m bv" style="background-color: rgba(156, 135, 73, .85);">
									{{char.value.split(',')[3]}}
								</view>
								<view class="bv-t bv" style="background-color: rgba(114, 100, 165, .85);">
									{{char.value.split(',')[4]}}
								</view>
							</view>
							<view class="choose-name"
								:style="{'background':'linear-gradient(to right, '+memoryCategoryAttr[mccurrent].lgcolor1+' 0%, '+memoryCategoryAttr[mccurrent].lgcolor2+' 30%, '+memoryCategoryAttr[mccurrent].lgcolor3+' 100%)'}">
								{{char.name}}
								<view class="memory-level-icon" @click.stop.prevent="handleClickModifyCount(char)">
									<template v-if="(char.count+0) == 0">
										未持有
									</template>
									<template v-else>
										<template v-for="item in (char.count - 1)">
											<img class='li-img' src="https://r.qianqiu.info/app/icons/m-c2.png" alt="">
										</template>
										<template v-for="item in (6 - char.count)">
											<img class='li-img' src="https://r.qianqiu.info/app/icons/m-c1.png" alt="">
										</template>
									</template>
								</view>
							</view>
						</view>
					</view>
				</view>
			</uv-popup>
			<uv-modal :closeOnClickOverlay='false' :showCancelButton='true' cancel-text="取消" ref="modifyCountPopup"
				title="请输入真实的烙痕获取数" confirm-text="确认修改" @confirm="handleClickModifyCountCallback">
				<uv-input type='number' placeholder="0破请填写1,满破填写6,没有填写0" border="surround" v-model="countValue"></uv-input>
			</uv-modal>
			<uv-toast ref='toast'></uv-toast>
		</template>
	</view>
</template>

<script setup>
	import {
		getCurrentInstance,
		ref,
		onMounted
	} from 'vue'
	const {
		proxy
	} = getCurrentInstance()
	import {
		compute
	} from './compute.js'
	const wikiUrl = ref("")
	function memoryToWikiDetail(name) {
		wikiUrl.value = "https://wiki.biligame.com/bjhl/记忆烙痕/" + name
	}
	const hignLevelSwitch = ref({
		s1: true,
		s2: true,
		s3: true,
		s4: false,
	})
	const computeRate = ref("1_0.8_0.6_0.5_0.5")
	const attribute = ref({
		attack: -3,
		iattack: 0,
		mastery: -4,
		imastery: 0,
		health: -1,
		ihealth: 0,
		defence: -2,
		idefence: 0,
		terminal: -5,
		iterminal: 0
	})
	let chooseSwitch = [0, 0, 0, 0, 0]

	function initIndexAttribute() {
		attribute.value.iattack = 0
		attribute.value.imastery = 0
		attribute.value.ihealth = 0
		attribute.value.idefence = 0
		attribute.value.iterminal = 0
	}
	function initSwitchBtn() {
		attribute.value.attack = -3
		attribute.value.mastery = -4
		attribute.value.health = -1
		attribute.value.defence = -2
		attribute.value.terminal = -5
		chooseSwitch = [0, 0, 0, 0, 0]
	}

	function switchCallback(va) {
		initIndexAttribute()
		const valValue = va < 0 ? -va : va
		for (let i in chooseSwitch) {
			// 判断开启还是关停，如果大于0，就找到数组值为0的下标
			if (va > 0) {
				if (chooseSwitch[i] == 0) {
					chooseSwitch[i] = va
					break
				}
			} else {
				// 获取这个值的下标
				if (chooseSwitch[i] == valValue) {
					chooseSwitch[i] = 0
					break
				}
			}
		}
		// 下方将值为0后置
		let tempArr = []
		for (let j of chooseSwitch) {
			if (j != 0) {
				tempArr.push(j)
			}
		}
		if (tempArr.length < chooseSwitch.length) {
			for (let j = 0; j < chooseSwitch.length - tempArr.length; j++) {
				tempArr.push(0)
			}
		}
		for (let i in tempArr) {
			chooseSwitch[i] = tempArr[i]
		}

		for (let i in tempArr) {
			let vvv = tempArr[i]
			if (vvv == 3) {
				attribute.value.iattack = parseInt(i) + 1
			}
			if (vvv == 4) {
				attribute.value.imastery = parseInt(i) + 1
			}
			if (vvv == 1) {
				attribute.value.ihealth = parseInt(i) + 1
			}
			if (vvv == 2) {
				attribute.value.idefence = parseInt(i) + 1
			}
			if (vvv == 5) {
				attribute.value.iterminal = parseInt(i) + 1
			}
		}
	}
	const modifyCountPopup = ref()
	const countValue = ref(-1)
	const toast = ref()
	let currentModifyItem = {}
	const healthArr = ref([{}, {}, {}])
	const defenceArr = ref([{}, {}, {}])
	const attackArr = ref([{}, {}, {}])
	const masteryArr = ref([{}, {}, {}])
	const terminalArr = ref([{}, {}, {}])
	function toCompute() {
		compute(allMemorys, chooseSwitch,(result)=>{
			initSwitchBtn()
			initIndexAttribute()
			attribute.value.iattack = result['攻击']
			attribute.value.imastery = result['专精']
			attribute.value.ihealth = result['体质']
			attribute.value.idefence = result['防御']
			attribute.value.iterminal = result['终端']
		}, (r)=>{
			healthArr.value = r
		},(r)=>{
			defenceArr.value = r
		},(r)=>{
			attackArr.value = r
		},(r)=>{
			masteryArr.value = r
		},(r)=>{
			terminalArr.value = r
		},computeRate.value)
	}

	function handleClickModifyCount(item) {
		currentModifyItem = item
		countValue.value = item.count
		modifyCountPopup.value.open()
	}

	function handleClickModifyCountCallback() {
		if (countValue.value < 0) {
			countValue.value = 0
		}
		if (countValue.value > 6) {
			countValue.value = 6
		}
		let id = uni.guser.getId()
		if (!id) {
			toast.value.show({
				type: 'default',
				message: "未登录，无法修改",
				overlay: false
			})
			return
		}
		modifyMineMemoryDirectorCountApi({
			count: countValue.value,
			uid: id,
			memoryId: currentModifyItem.memoryId
		}, () => {
			toast.value.show({
				type: 'default',
				message: "修改成功",
				overlay: false
			})
			for (let j of allMemorys) {
				if (j.memoryId = currentModifyItem.memoryId) {
					j.count = countValue.value
					break
				}
			}
			initMemoryData()
		}, fail => {
			toast.value.show({
				type: 'default',
				message: fail,
				overlay: false
			})
		})
	}
	function hignLevelSwitchCallback(){
		initMemoryData()
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
		"SSR": "https://r.qianqiu.info/app/icons/ssr.png",
		"SR": "https://r.qianqiu.info/app/icons/sr.png",
		"R": "https://r.qianqiu.info/app/icons/r.png",
	}
	const memoryCategoryAttr = {
		"体质": {
			"icon": "https://r.qianqiu.info/app/icons/health.png",
			"fcolor": '#5eb8aa',
			"bg": "https://r.qianqiu.info/app/icons/memory-tz.png",
			"bcolor": 'rgba(94, 184, 170, .25)',
			"lgcolor1": '#262c2e',
			"lgcolor2": '#3d6762',
			"lgcolor3": '#53928a'
		},
		"防御": {
			"icon": "https://r.qianqiu.info/app/icons/defence.png",
			"fcolor": '#4a8ace',
			"bg": "https://r.qianqiu.info/app/icons/memory-fy.png",
			"bcolor": 'rgba(74, 138, 206,.25)',
			"lgcolor1": '#262c2e',
			"lgcolor2": '#294668',
			"lgcolor3": '#306cb5'
		},
		"攻击": {
			"icon": "https://r.qianqiu.info/app/icons/attack.png",
			"fcolor": '#ca7172',
			"bg": "https://r.qianqiu.info/app/icons/memory-gj.png",
			"bcolor": 'rgba(202, 113, 114,.25)',
			"lgcolor1": '#262c2e',
			"lgcolor2": '#5c3d3e',
			"lgcolor3": '#a0585c'
		},
		"专精": {
			"icon": "https://r.qianqiu.info/app/icons/mastery.png",
			"fcolor": '#bbb45a',
			"bg": "https://r.qianqiu.info/app/icons/memory-zj.png",
			"bcolor": 'rgba(187, 180, 90,.25)',
			"lgcolor1": '#262c2e',
			"lgcolor2": '#575136',
			"lgcolor3": '#9b8649'
		},
		"终端": {
			"icon": "https://r.qianqiu.info/app/icons/terminal.png",
			"fcolor": '#8469ad',
			"bg": "https://r.qianqiu.info/app/icons/memory-zd.png",
			"bcolor": 'rgba(132, 105, 173,.25)',
			"lgcolor1": '#262c2e',
			"lgcolor2": '#524d73',
			"lgcolor3": '#6f61a3'
		}
	}
	const mlcurrent = ref("SSR")
	const mccurrent = ref("体质")
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
		showSelectMemorys.value = []
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

	//烙痕选择事件
	function handleChangeMemoryCategory(index) {
		memoryBoxCategoryCurrent.value = index
		showSelectMemorys.value = allMemorys[memoryCategorys[index]]
	}
	let type = 0
	function openMemoryBox(type, index) {
		memoryPopup.value.open()
	}
	//确认选择该烙痕
	function handleChooseMemory(memory) {
	}
	import {
		getMemorysSimpleApi,
		getMemoryPoolGroupByUid,
		getMineMemoryDirectorCountApi,
		modifyMineMemoryDirectorCountApi
	} from "@/myapi/ashEchoes.js"
	// 初始化所有烙痕
	function initAllMemory(suc) {
		let id = uni.guser.getId()
		getMemorysSimpleApi({}, (r) => {
			for (let j of Object.keys(r)) {
				for (let ee of r[j]) {
					ee.count = 0
					if (ee.rank == "R" || !id) {
						ee.count = 6
					}
				}

				allMemorys.push(...r[j])
			}
			if (suc) {
				suc()
			}
		})
	}

	function initMemoryData() {
		allMemorys = []
		initAllMemory(() => {
			let id = uni.guser.getId()
			if (id) {
				getMineMemoryDirectorCountApi(id, (mineData) => {
					getMemoryPoolGroupByUid(id, (r) => {
						for (let j of r) {
							if (j.count > 6) {
								j.count = 6
							}
						}
						if (mineData.length) {
							for (let iii of mineData) {
								let has = false
								for (let jjj of r) {
									if (iii.memoryId == jjj.id) {
										if(iii.count < jjj.count) {
											iii.count = jjj.count;
											has = true
										}
										break;
									}
								}
								if (!has) {
									iii.id = iii.memoryId
									r.push(iii)
								}
							}
						}
						// 创建一个基于 id 的查找表
						const lookup = new Map(r.map(item => [item.id, item]));
						
						// 合并数组，保留原始数据结构，并使用 memoryId 和 id 进行匹配
						const mergedAllMemorys = allMemorys.map(memory => {
							const matchingRItem = lookup.get(memory.memoryId);
							return matchingRItem ? {
								...memory,
								...matchingRItem
							} : {
								...memory
							};
						});
						for(let jj of mergedAllMemorys) {
							if(hignLevelSwitch.value.s1 && jj.origin=='活动') {
								jj.count = 6
							}
							if(hignLevelSwitch.value.s2 && jj.origin=='大月卡') {
								jj.count = 6
							}
							if(hignLevelSwitch.value.s3 && jj.origin=='主线') {
								jj.count = 6
							}
							if(hignLevelSwitch.value.s4 && jj.origin=='卡池') {
								jj.count = 6
							}
						}
						allMemorys = mergedAllMemorys;
						
						filterMemoryData()
					})
				})

			} else {
				filterMemoryData()
			}
		})
	}

	// #ifdef WEB
	import TopNavBar from '@/components/TopNavbar/TopNavbar.vue'
	const showNav = ref(false)
	import {
		useSystemStore
	} from "@/stores/SystemStore.js"

	function showNavFunc(vvvv) {
		showNav.value = vvvv
	}
	onMounted(() => {
		proxy.$nextTick(() => {
			let body = document.getElementById('parant-body').style
			body.height = 'calc(100% - ' + useSystemStore().height + '%)'
			body.minHeight = 'calc(100% - ' + useSystemStore().originHeight + '%)'
		})
		initMemoryData()
		attribute.value.attack = 3
		switchCallback(3)
	})
	// #endif
</script>

<style lang='scss'>
	.uv-button {
		border-radius: 0 !important;
	}

	.md-body {
		position: fixed;
		width: 100%;
		max-width: 750rpx;
		left: 50%;
		transform: translateX(-50%);
		font-size: 26rpx;
		color: #333;
		min-height: 100%;
		background-color: #e9edef;
		display: flex;
		align-items: center;
		justify-content: center;

		.md-content {
			position: fixed;
			top: 65rpx;
			left: 0;
			width: 100%;
			height: calc(100% - 65rpx);
			// #ifdef MP-WEIXIN
			height: 100%;
			top: 0rpx;
			// #endif
			padding: 20rpx;
			box-sizing: border-box;
			overflow: auto;
			.hc-title {
				width: 100%;
				background-color: #dce1e5;
				line-height: 60rpx;
				border-left: 10rpx solid #3366ff;
				padding: 5rpx;
				font-size: 36rpx;
				font-weight: 600;
			}
			.head-content {
				.hc-top-tips{
					display: grid;
					grid-template-columns: 1fr 3fr;
					gap: 20rpx;
					height: 90rpx;
					align-items: center;
					margin-top: 10rpx;
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

					.input {
						width: 100%;
						border: 1rpx solid black;
						background-color: #e9edef;
					}

					.btns {
						border: none;
						display: grid;
						grid-template-columns: 1fr 1fr;
						gap: 30rpx;
					}
				}
			}

			.bigbox-memory-card {
				margin-top: 20rpx;
				display: grid;
				width: 100%;
				grid-template-columns: 1fr 1fr;
				gap: 20rpx;

				.card-title {
					display: flex;
					align-items: center;
					justify-content: center;
					font-size: 36rpx;
					color: black;
					letter-spacing: 2rpx;
					font-weight: 600;
				}
			}
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


			}
		}

		.card {
			position: relative;
			width: 100%;
			height: 200rpx;

			.pc-img-box {
				width: 100%;
				height: 200rpx;
				background-color: gray;

				.pc-img {
					height: 200rpx;
					width: 100%;
					border-radius: 0 !important;
					background-color: white;
				}

				.pc-no-img {
					position: absolute;
					left: 50%;
					transform: translateX(-50%);
					width: 70rpx;
					height: 70rpx;
					top: 45rpx;
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

			.base-value {
				position: absolute;
				display: grid;
				gap: 5rpx;
				grid-template-columns: repeat(5, 1fr);
				bottom: 50rpx;
				right: 10rpx;
				z-index: 1;
				color: white;

				.bv {
					display: flex;
					align-items: center;
					justify-content: center;
					width: 40rpx;
					height: 40rpx;
					font-size: 24rpx;
					font-weight: 600;
					background-color: #e9edef;
				}
			}

			.float-tips {
				position: absolute;
				left: 0rpx;
				top: 0rpx;
				background-color: rgba(0, 0, 0, .7);
				color: white;
				width: 100rpx;
				line-height: 40rpx;
				text-align: center;
			}

			.choose-name {
				position: absolute;
				display: flex;
				align-items: end;
				justify-content: start;
				font-size: 22rpx;
				letter-spacing: 1rpx;
				font-weight: 600;
				width: 100%;
				height: 40rpx;
				line-height: 40rpx;
				background-color: white;
				z-index: 2;
				bottom: 0;
				color: white;
				text-indent: .5em;

				.memory-level-icon {
					position: absolute;
					right: 20rpx;
					bottom: 0;
					height: 40rpx;
					display: flex;
					align-items: center;
					color: red;
					text-decoration: underline;

					.li-img {
						width: 30rpx;
						height: 30rpx;
						margin-left: -18rpx;
					}
				}

				.memory-level-icon:hover {
					cursor: pointer;
				}

			}
		}

	}
</style>