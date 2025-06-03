// vite.config.js
 
import { defineConfig } from 'vite';
import uni from '@dcloudio/vite-plugin-uni';
 
export default defineConfig({
	plugins: [uni()],
	server: {
		port: 881,
		open: false,
		proxy: {
			'/api': {
				target: 'http://192.168.100.102:8765/',
				// target: 'https://bjhl.qianqiu.info/api/',
				// target: 'http://localhost:8765/',
				changeOrigin: true,
				rewrite: (path) => path.replace(/^\/api/, '')
			}
		}
	}
});