<script setup>
import { ref } from "vue";
import { useTokenStore } from "@/stores/token";
const tokenStore = useTokenStore();

import { resetPasswordService } from "@/api/user";

const isButtonDisabled = ref(false);

const form = ref({
  oldPassword: "",
  newPassword: "",
  rePassword: "",
});

const checkRePassword = (rule, value, callback) => {
  if (value === "") {
    callback(new Error("请再次确认密码"));
  } else if (value !== form.value.newPassword) {
    callback(new Error("请确保两次输入的密码一样"));
  } else callback();
};

const rules = {
  oldPassword: [
    { required: true, message: "请输入原密码", trigger: "blur" },
    {
      pattern: /^\S{5,16}$/,
      message: "密码必须是5-16位的非空字符串",
      trigger: "blur",
    },
  ],
  newPassword: [
    { required: true, message: "请输入新密码", trigger: "blur" },
    {
      pattern: /^\S{5,16}$/,
      message: "密码必须是5-16位的非空字符串",
      trigger: "blur",
    },
  ],
  rePassword: [{ validator: checkRePassword, trigger: "blur" }],
};

import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
const router = useRouter();

const resetPassword = async () => {
  if (isButtonDisabled.value) return;

  isButtonDisabled.value = true;

  try {
    let result = await resetPasswordService(
      form.value.newPassword,
      form.value.oldPassword,
      form.value.rePassword
    );
    ElMessage.success(result.message ? result.message : "重设成功");
    setTimeout(() => {
      tokenStore.removeToken();
      router.push("/login");
    }, 3000);
  } catch (error) {
    setTimeout(() => {
      isButtonDisabled.value = false;
    }, 3000);
  }
};
</script>
<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>重置密码</span>
      </div>
    </template>
    <el-row>
      <el-col :span="12">
        <el-form :model="form" :rules="rules" label-width="100px" size="large">
          <el-form-item label="原密码" prop="oldPassword">
            <el-input v-model="form.oldPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="form.newPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item label="再次输入" prop="rePassword">
            <el-input v-model="form.rePassword" type="password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              @click="resetPassword"
              :disabled="isButtonDisabled"
              >提交修改</el-button
            >
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </el-card>
</template>
