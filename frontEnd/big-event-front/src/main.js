import "./assets/main.scss";

import { createApp } from "vue";
import App from "./App.vue";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import router from "@/router";
import { createPinia } from "pinia";
import { createPersistedState } from "pinia-persistedstate-plugin";
import locale from "element-plus/dist/locale/zh-cn";
import { useTokenStore } from "./stores/token";

const app = createApp(App);
const pinia = createPinia();
const persist = createPersistedState();
pinia.use(persist);
app.use(pinia);
const tokenStore = useTokenStore();
tokenStore.loadToken();
app.use(router);
app.use(ElementPlus, { locale }).mount("#app");
