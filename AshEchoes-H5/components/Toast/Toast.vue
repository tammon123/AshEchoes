<template>
	<view>
		<uv-toast ref="toast" :zIndex="99999"></uv-toast>
	</view>
</template>
<script setup>
	import {
		onMounted,
		ref,
		defineEmits,
		watch
	} from 'vue';
	import {
		useToastStore
	} from "@/stores/ToastStore"
	const toast = ref()
	const ts = useToastStore()

	watch([() => ts.toast.show], ([toastShow], [o, o1]) => {
		if (toastShow && toastShow != o) {
			toast.value.show({
				show: toastShow,
				loading: ts.toast.loading,
				type: ts.toast.type,
				icon: ts.toast.icon,
				message: ts.toast.message,
				overlay: ts.toast.overlay,
				zIndex: ts.toast.zIndex,
				position: ts.toast.position,
				duration: ts.toast.duration,
				complete() {
					toastCancel()
				}
			})
		}else if(!toastShow) {
			toastCancel()
		}
	})

	function toastCancel() {
		ts.$patch({
			toast: {
				show: false
			}
		})
	}
	/**
	 * 以下是加载到全局中
	 */
	function toastC(message, duration) {
		if (!duration) {
			duration = 3000
		}
		ts.$patch({
			toast: {
				show: true,
				type: "success",
				icon: false,
				message,
				duration
			}
		})
	}
	
	function toastFail(message, duration) {
		if (!duration) {
			duration = 3000
		}
	
		ts.$patch({
			toast: {
				show: true,
				message,
				duration,
				type: 'error'
			}
		})
	}
	
	
	function toastSuccess(message, duration) {
		if (!duration) {
			duration = 3000
		}
		ts.$patch({
			toast: {
				show: true,
				icon: true,
				message,
				duration,
				type: 'success',
			}
		})
	}
	
	function loading(message, duration) {
		if (!duration) {
			duration = 5000
		}
		ts.$patch({
			toast: {
				show: true,
				message,
				duration,
				type: 'loading'
			}
		})
	}
	
	
	uni.toast = toastC
	uni.toastFail = toastFail
	uni.toastSuccess = toastSuccess
	uni.loading = loading
	
	function toastClear() {
		toastCancel()
	}
	
	uni.toastClear = toastClear
	
</script>