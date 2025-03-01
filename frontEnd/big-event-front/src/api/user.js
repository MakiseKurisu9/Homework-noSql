import request from "@/utils/request.js";
import { useTokenStore } from "@/stores/token";

//提供调用注册接口的函数
export const userRegisterService = (registerData) => {
  //借助UrlSearchParamas完成传递
  const params = new URLSearchParams();
  for (let key in registerData) {
    params.append(key, registerData[key]);
  }
  return request.post("/user/register", params);
};

export const userLoginService = (loginData) => {
  const params = new URLSearchParams();
  for (let key in loginData) {
    params.append(key, loginData[key]);
  }
  return request.post("/user/login", params);
};

export const userInfoService = () => {
  return request.get("/user/userInfo");
};

export const updateUserInfoService = (userInfoData) => {
  return request.put("/user/update", userInfoData);
};

export const updateAvatarService = (avatarUrl) => {
  const params = new URLSearchParams();
  params.append("avatarUrl", avatarUrl);
  return request.patch("/user/updateAvatar", params);
};

export const resetPasswordService = (newPwd, oldPwd, rePwd) => {
  const params = {
    old_pwd: oldPwd,
    new_pwd: newPwd,
    re_pwd: rePwd,
  };
  return request.patch("/user/updatePwd", params);
};
