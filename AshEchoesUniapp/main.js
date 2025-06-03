import App from './App'
// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
	...App
})
app.$mount()
// #endif

// #ifdef VUE3
import {
	createSSRApp
} from 'vue'
import TopNavBar from '@/components/TopNavbar/TopNavbar.vue'
import uvUI from '@/uni_modules/uv-ui-tools'
import * as Pinia from 'pinia';
import Toast from "@/components/Toast/Toast.vue"

export function createApp() {
	const app = createSSRApp(App)
	app.use(Pinia.createPinia())
	app.use(uvUI);
	app.component("toast", Toast)
	app.component("TopNavBar", TopNavBar)
	
	return {
		app,
		Pinia
	}
}
// #endif