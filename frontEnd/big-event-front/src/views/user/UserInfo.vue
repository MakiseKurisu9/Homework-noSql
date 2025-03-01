<script setup>
import { ref, warn } from "vue";
import userInfoStore from "@/stores/userInfo";
const userStore = userInfoStore();
const userInfo = ref({ ...userStore.info });
const rules = {
  nickname: [
    { required: true, message: "请输入用户昵称", trigger: "blur" },
    {
      pattern: /^\S{2,10}$/,
      message: "昵称必须是2-10位的非空字符串",
      trigger: "blur",
    },
  ],
  email: [
    { required: true, message: "请输入用户邮箱", trigger: "blur" },
    { type: "email", message: "邮箱格式不正确", trigger: "blur" },
  ],
};

import { updateUserInfoService } from "@/api/user";
import { ElMessage } from "element-plus";

const isButtonDisabled = ref(false);

const userForm = ref(null);

const updateUserInfo = async () => {
  await userForm.value.validate();
  /*因为pinia中自带数据，所以如果邮箱检验未通过，浏览器也会发送请求（发送的请求是pinia中的），
  使用validate防止发送不合理的请求
 */
  if (isButtonDisabled.value) return;
  isButtonDisabled.value = true;
  let result = await updateUserInfoService(userInfo.value);
  userStore.removeInfo();
  ElMessage.success(result.message ? result.message : "修改成功");
  userStore.setInfo(userInfo.value);

  setTimeout(() => {
    isButtonDisabled.value = false;
  }, 3000);
};
</script>
<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>基本资料</span>
      </div>
    </template>
    <el-row>
      <el-col :span="12">
        <el-form
          :model="userInfo"
          :rules="rules"
          label-width="100px"
          size="large"
          ref="userForm"
        >
          <el-form-item label="登录名称">
            <el-input v-model="userInfo.username" disabled></el-input>
          </el-form-item>
          <el-form-item label="用户昵称" prop="nickname">
            <el-input v-model="userInfo.nickname"></el-input>
          </el-form-item>
          <el-form-item label="用户邮箱" prop="email">
            <el-input v-model="userInfo.email"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              @click="updateUserInfo"
              :disabled="isButtonDisabled"
              >提交修改</el-button
            >
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </el-card>
</template>
