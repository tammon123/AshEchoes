<script>
	import "@/utils/init.js"
	// #ifdef WEB
	import {
		versionApi
	} from "@/myapi/ashEchoes.js"
	import {
		useSystemStore
	} from "@/stores/SystemStore.js"
	import {
		StorageUtils
	} from "./utils/StorageUtils"
	// #endif
	export default {
		onLaunch: function(param) {
			// #ifdef WEB
			versionApi((r) => {
				const vk = "version-ae"
				const cc = uni.su.get(vk)
				uni.su.set(vk, r)
				if (cc != r) {
					location.reload(true)
				}
			})
			this.$nextTick(() => {
				const su = new StorageUtils()
				if (param.query.nav) {
					su.set("inavHeight", param.query.nav)
				}
				const h = su.get("inavHeight")
				const systemStore = useSystemStore()
				const bodyStyle = document.body.style
				let height = (h * 200)
				let originHeight = (h * 100)
				bodyStyle.paddingTop = height + "%"; // 重置body节点的背景颜色
				systemStore.$patch({
					height: height
				})
				systemStore.$patch({
					originHeight: originHeight
				})
			});
			// #endif
		},
		onShow: function() {

		},
		onHide: function() {}
	}
</script>

<style lang="scss">
	$mainColor: #3067fe;
	@import '@/uni_modules/uv-ui-tools/index.scss';

	img {
		object-fit: cover;
	}

	.copyright {
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
		font-size: 22rpx;
		color: #888;
		line-height: 40rpx;
	}

	::v-deep .uv-popup__content {
		border-radius: 0 !important;
	}

	::v-deep .uv-modal {
		border-radius: 0 !important;
	}

	::v-deep .uv-popup__content {
		background-color: #e9edef !important;
	}

	::v-deep .uv-search__content {
		border-radius: 0 !important;
	}

	::v-deep .uv-subsection--button__bar {
		background-color: rgba(0, 0, 0, 0) !important;
	}

	::v-deep .uv-input--radius {
		border-radius: 0rpx !important;
	}

	html {
		background-color: #e9edef !important;
	}

	body {
		background-color: #e9edef !important;
	}

	uni-page-wrapper {
		background-color: #e9edef !important;
	}

	.login-index-box {
		position: relative;
		width: 100%;
		height: 1000rpx;
		padding: 40rpx;
		box-sizing: border-box;

		.input-title {
			line-height: 80rpx;
			font-weight: bold;
			letter-spacing: 2rpx;
		}

		.submit-btns {
			width: 100%;
			margin-top: 40rpx;
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: space-around;

			.mine-submit {
				font-size: 30rpx;
				font-weight: 600;
				width: 200rpx;
				height: 80rpx;
				background-color: $mainColor;
				color: white;
				display: flex;
				align-items: center;
				justify-content: center;
				margin-left: 20rpx;
				letter-spacing: 2rpx;
			}

			.find-submit {
				background-color: #e9edef;
				color: black;
			}

			.mine-submit:first-child {
				background-color: #f7f7f7;
				color: #3067fe;
			}
		}

	}

	.top-nav-line {
		width: 100%;
		max-width: 750rpx;
		padding: 10rpx;
		height: 70rpx;
		box-sizing: border-box;
		background-color: #e9edef;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
		font-weight: 600;

		.tnl-logo {
			height: 50rpx;
			width: auto;
		}

		.back {
			position: absolute;
			left: 20rpx;
			top: 20rpx;
			top: 50%;
			transform: translateY(-50%);
			width: 30rpx;
			height: 30rpx;
		}

		.right-icon {
			position: absolute;
			right: 30rpx;
			text-decoration: underline;
			color: #3366fe;
		}

		.right-icon:hover {
			cursor: pointer;
		}

		.login-box {
			position: absolute;
			right: 30rpx;
			color: $mainColor;
			font-weight: 600;
			top: 50%;
			transform: translateY(-50%);
			max-width: 100rpx;

			.login {
				text-decoration: underline;
			}

			.no-login {
				color: red;
				text-decoration: underline;
			}
		}

	}

	.uv-cell__body {
		padding-left: 0 !important;
		padding-right: 0 !important;
	}

	.uv-collapse-item__content__text {
		padding-left: 0 !important;
		padding-right: 0 !important;
	}

	.uv-cell__left-icon-wrap {
		margin-right: 0;
	}

	/*每个页面公共css */
</style>