import {
	defineStore
} from 'pinia';

export const useUserStore = defineStore('peidui', {
	state: () => {
		return {
			avatar: uni.guser.getAvatar(),
			nickName: uni.guser.getNickname(),
			defaultPhone: uni.guser.getPhone()
		}
	}
});