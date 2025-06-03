import {
	defineStore
} from 'pinia';

export const useSystemStore = defineStore('system', {
	state: () => {
		return {
			height: 0,
			originHeight: 0,
		}
	}
});