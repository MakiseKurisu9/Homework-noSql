import request from "@/utils/request.js";
import { useTokenStore } from "@/stores/token";

export const articleCategoryListService = () => {
  //   const tokenStore = useTokenStore();
  //pinia中定义的响应式数据不需要.value
  return request.get("/category");
};

//add article
export const ArticleCategoryAddService = (categoryData) => {
  return request.post("/category", categoryData);
};

export const articleCategoryUpdateService = (categoryData) => {
  return request.put("/category", categoryData);
};

export const articleCategoryDeleteService = (id) => {
  request.delete("/category?id=" + id);
};

//文章列表查询
export const articleListService = (params) => {
  return request.get("/article", { params: params });
};
export const articleAddService = (articleData) => {
  return request.post("/article", articleData);
};
