import axios from "axios";
import { ElMessage } from "element-plus";
import { useTokenStore } from "@/stores/token";
//const baseURL = "http://localhost:8080";
const baseURL = "/api";

const instance = axios.create({ baseURL });
//错误信息提示框

// import { useRouter } from "vue-router";
// const router = useRouter();

import router from "@/router";

instance.interceptors.response.use(
  (result) => {
    if (result.data.code === 0) {
      return result.data;
    }
    ElMessage.error(result.data.message ? result.data.message : "服务异常");
    return Promise.reject(result.data);
  },
  (err) => {
    //判断相应码，如果401，则为未登录，需要跳转到登录页面
    if (err.response.status === 401) {
      if (document.querySelector(".el-message")) {
        return Promise.reject(err);
      }
      ElMessage.error("请先登录");
      router.push("/login");
      //主页面3个请求，防止重新弹出提示框
    } else if (err.response.status === 400) {
      ElMessage.error("请先删除相关的文章");
    } else {
      ElMessage.error("服务异常");
    }
    return Promise.reject(err); //异步状态转为失败
  }
);

instance.interceptors.request.use(
  (config) => {
    const localToken = localStorage.getItem("token");
    const sessionToken = sessionStorage.getItem("token");
    if (localToken) {
      config.headers.Authorization = localToken;
    } else {
      config.headers.Authorization = sessionToken;
    }
    console.log("Request Headers:", config.headers);
    return config;
  },
  (err) => {
    Promise.reject(err);
  }
);

export default instance;
