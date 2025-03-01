import { defineStore } from "pinia";
import { ref } from "vue";
//第一个参数：名字，唯一性
//第二个参数：函数，函数的内部可以定义状态的所有内容
//返回值 函数
export const useTokenStore = defineStore(
  "token",
  () => {
    const token = ref("");
    const rememberMe = ref(false);
    const setToken = (newToken, remember) => {
      token.value = newToken;
      rememberMe.value = remember;
      console.log("remember???" + rememberMe.value);
      if (rememberMe.value) {
        localStorage.setItem("token", token.value);
      } else {
        sessionStorage.setItem("token", token.value);
      }
    };

    const removeToken = () => {
      token.value = "";
      localStorage.removeItem("token");
      sessionStorage.removeItem("token");
    };

    const loadToken = () => {
      const storedToken =
        localStorage.getItem("token") || sessionStorage.getItem("token");
      if (storedToken) {
        token.value = storedToken;
      }
    };

    return {
      token,
      setToken,
      removeToken,
      loadToken,
    };
  },
  {
    persist: true,
  }
);
