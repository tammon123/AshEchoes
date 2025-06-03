import {
	defineStore
} from 'pinia';

export const useStoryStore = defineStore('story', {
	state: () => {
		return {
			type: "主线剧情,间章剧情,幕间剧情,角色故事,其他剧情",
			top: 0
		}
	}
});