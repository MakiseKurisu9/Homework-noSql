<script setup>
import { Plus, Upload } from "@element-plus/icons-vue";
import { ref } from "vue";
import avatar from "@/assets/default.png";
const uploadRef = ref();
import userInfoStore from "@/stores/userInfo";
import { useTokenStore } from "@/stores/token";

const tokenStore = useTokenStore();
const userStore = userInfoStore();

//用户头像地址
const imgUrl = ref(userStore.info.userPic);

const uploadSuccess = (result) => {
  imgUrl.value = result.data;
};
import { updateAvatarService } from "@/api/user";
import { ElMessage } from "element-plus";

let isButtonDisabled = ref(false);

const updateAvatar = async () => {
  if (isButtonDisabled.value) return;

  isButtonDisabled.value = true;

  let result = await updateAvatarService(imgUrl.value);
  ElMessage.success(result.message ? result.message : "修改成功");
  userStore.info.userPic = imgUrl.value;
  setTimeout(() => {
    isButtonDisabled.value = false;
  }, 3000);
};
</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>更换头像</span>
      </div>
    </template>
    <el-row>
      <el-col :span="12">
        <el-upload
          ref="uploadRef"
          class="avatar-uploader"
          :show-file-list="false"
          :auto-upload="true"
          action="/api/upload"
          name="file"
          :headers="{ Authorization: tokenStore.token }"
          :on-success="uploadSuccess"
        >
          <img v-if="imgUrl" :src="imgUrl" class="avatar" />
          <img v-else src="@/assets/default.png" width="278" />
        </el-upload>
        <br />
        <el-button
          type="primary"
          :icon="Plus"
          size="large"
          @click="uploadRef.$el.querySelector('input').click()"
        >
          选择图片
        </el-button>
        <el-button
          type="success"
          :icon="Upload"
          size="large"
          @click="updateAvatar"
          :disabled="isButtonDisabled"
        >
          上传头像
        </el-button>
      </el-col>
    </el-row>
  </el-card>
</template>

<style lang="scss" scoped>
.avatar-uploader {
  :deep() {
    .avatar {
      width: 278px;
      height: 278px;
      display: block;
    }

    .el-upload {
      border: 1px dashed var(--el-border-color);
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: var(--el-transition-duration-fast);
    }

    .el-upload:hover {
      border-color: var(--el-color-primary);
    }

    .el-icon.avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 278px;
      height: 278px;
      text-align: center;
    }
  }
}
</style>
