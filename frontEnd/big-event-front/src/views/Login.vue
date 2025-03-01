<script setup>
import { ElMessage } from "element-plus";
import { User, Lock, Checked } from "@element-plus/icons-vue";
import { ref } from "vue";

//控制注册与登录表单的显示， 默认显示注册
const isRegister = ref(false);
const registerData = ref({ username: "", password: "", rePassword: "" });
//test password function
const checkRePassword = (rule, value, callback) => {
  if (value === "") {
    callback(new Error("请再次确认密码"));
  } else if (value !== registerData.value.password) {
    callback(new Error("请确保两次输入的密码一样"));
  } else callback();
};

const rememberMe = ref(false);

const rules = {
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 5, max: 16, message: "长度为5-16位非空字符", trigger: "blur" },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 5, max: 16, message: "长度为5-16位非空字符", trigger: "blur" },
  ],
  rePassword: [{ validator: checkRePassword, trigger: "blur" }],
};
import { useRouter } from "vue-router";
const router = useRouter();
//调用后台接口 完成注册
import { userRegisterService, userLoginService } from "@/api/user.js";
const register = async () => {
  let result = await userRegisterService(registerData.value);
  //   if (result.code === 0) {
  //     alert(result.msg ? result.msg : "注册成功");
  //   } else {
  //     alert("注册失败");
  //   }
  ElMessage.success(result.message ? result.message : "注册成功");
  router.push("/login");
  isRegister.value = false;
};

import { useTokenStore } from "@/stores/token";
const tokenStore = useTokenStore();
const login = async () => {
  let result = await userLoginService(registerData.value);
  //   if (result.code === 0) {
  //     alert(result.msg ? result.msg : "登录成功");
  //   } else {
  //     alert("登录失败");
  //   }
  ElMessage.success(result.message ? result.message : "登录成功");
  tokenStore.setToken(result.data, rememberMe.value);
  //借助路由跳到首页
  router.push("/");
};

//定义函数，清空数据模型的数据
const clear = () => {
  registerData.value.password = "";
  registerData.value.username = "";
  registerData.value.rePassword = "";
};
</script>

<template>
  <el-row class="login-page">
    <el-col :span="12" class="bg"></el-col>
    <el-col :span="6" :offset="3" class="form">
      <!-- 注册表单 -->
      <el-form
        ref="form"
        size="large"
        autocomplete="off"
        v-if="isRegister"
        :model="registerData"
        :rules="rules"
      >
        <el-form-item>
          <h1>注册</h1>
        </el-form-item>
        <el-form-item prop="username">
          <el-input
            :prefix-icon="User"
            placeholder="请输入用户名"
            v-model="registerData.username"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入密码"
            v-model="registerData.password"
          ></el-input>
        </el-form-item>
        <el-form-item prop="rePassword">
          <el-input
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入再次密码"
            v-model="registerData.rePassword"
          ></el-input>
        </el-form-item>
        <!-- 注册按钮 -->
        <el-form-item>
          <el-button
            class="button"
            type="primary"
            auto-insert-space
            v-on:click="register"
          >
            注册
          </el-button>
        </el-form-item>
        <el-form-item class="flex">
          <el-link
            type="info"
            :underline="false"
            @click="isRegister = false"
            v-on:click="clear"
          >
            ← 返回
          </el-link>
        </el-form-item>
      </el-form>
      <!-- 登录表单 -->
      <el-form
        ref="form"
        size="large"
        autocomplete="off"
        v-else
        :model="registerData"
        :rules="rules"
      >
        <el-form-item>
          <h1>登录</h1>
        </el-form-item>
        <el-form-item prop="username">
          <el-input
            :prefix-icon="User"
            placeholder="请输入用户名"
            v-model="registerData.username"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            name="password"
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入密码"
            v-model="registerData.password"
          ></el-input>
        </el-form-item>
        <el-form-item class="flex">
          <div class="flex">
            <el-checkbox
              v-model="rememberMe"
              :true-value="true"
              :false-value="false"
              >记住我</el-checkbox
            >
            <el-link type="primary" :underline="false">忘记密码？</el-link>
          </div>
        </el-form-item>
        <!-- 登录按钮 -->
        <el-form-item>
          <el-button
            class="button"
            type="primary"
            auto-insert-space
            v-on:click="login"
            >登录</el-button
          >
        </el-form-item>
        <el-form-item class="flex">
          <el-link
            type="info"
            :underline="false"
            @click="isRegister = true"
            v-on:click="clear"
          >
            注册 →
          </el-link>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<style lang="scss" scoped>
/* 样式 */
.login-page {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #f5f7fa, #c3cfe2); /* 整体页面渐变背景 */
  margin: 0; /* 去除外边距 */
  padding: 0; /* 去除内边距 */

  .bg {
    position: absolute; /* 使用绝对定位确保贴紧页面左侧 */
    top: 0; /* 从顶部开始 */
    left: 0; /* 紧贴左边 */
    height: 100vh; /* 占满页面高度 */
    width: 50%; /* 左侧占页面宽度的50% */
    border-radius: 0 20px 20px 0; /* 添加右侧圆角 */
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); /* 添加阴影使背景更立体 */
    background: url("@/assets/article_bg.jpg") no-repeat center / cover; /* 背景图片 */
  }

  .form {
    width: 300px; /* 设置表单宽度 */
    padding: 20px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    background: linear-gradient(135deg, #ffffff, #e6e9f0); /* 表单渐变背景 */
    border-radius: 15px; /* 圆角 */
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); /* 添加阴影 */
    z-index: 1; /* 确保表单位于背景图片上方 */
    margin-left: 50%; /* 表单从图片右边开始布局 */
    transform: translateX(-25%); /* 调整表单位置，使其居中于右半部分 */

    .title {
      margin: 0 auto;
      font-size: 24px;
      font-weight: bold;
      color: #333; /* 标题颜色 */
    }

    .button {
      width: 100%;
      padding: 10px;
      background: linear-gradient(135deg, #6a11cb, #2575fc); /* 按钮渐变背景 */
      color: white;
      border: none;
      border-radius: 5px;
      font-size: 16px;
      font-weight: bold;
      cursor: pointer;
      transition: background 0.3s ease; /* 添加渐变过渡效果 */
    }

    .button:hover {
      background: linear-gradient(
        135deg,
        #2575fc,
        #6a11cb
      ); /* 悬停时反转渐变 */
    }

    .flex {
      width: 100%;
      display: flex;
      justify-content: space-between;
      margin-top: 10px;
    }

    input {
      width: 100%;
      padding: 10px;
      margin-bottom: 15px;
      border: 1px solid #ddd;
      border-radius: 5px;
      font-size: 14px;
      box-sizing: border-box;
    }

    input:focus {
      outline: none;
      border-color: #6a11cb; /* 聚焦时的边框颜色 */
      box-shadow: 0 0 5px rgba(106, 17, 203, 0.5);
    }
  }
}
</style>
