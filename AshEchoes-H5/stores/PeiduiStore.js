import {
	defineStore
} from 'pinia';

export const usePeiduiStore = defineStore('user', {
	state: () => {
		return {
			chooseCharacter: ""
		}
	}
});