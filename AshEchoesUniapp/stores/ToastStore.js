import {
	defineStore
} from 'pinia';
import {
	ref
} from "vue";
export const useToastStore = defineStore('toast', () => {
	const toast = ref({
		show: false,
		type: 'success',
		icon: false,
		title: "",
		message: "消息",
		overlay: false,
		loading: false,
		zIndex: 10090,
		position: "center",
		duration: 2000
	})
	
	return {
		toast
	}
});