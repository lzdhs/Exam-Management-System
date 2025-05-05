<template>
  <div class="manager-container">
    <div class="header">
      <div class="wrapper">
        <!-- logo -->
        <div class="logo">
          <h1><router-link to="/manager/home" class="logo-link">狮山考试通后台</router-link></h1>
        </div>
        <!-- 导航 -->
        <div class="navigator">
          <ul>
            <li><router-link to="/manager/home" class="nav-link">首页</router-link></li>
          </ul>
        </div>
        <!-- 用户 -->
        <div class="user">
          <el-dropdown style="cursor: pointer">
            <div style="padding-right: 20px; display: flex; align-items: center">
              <img style="width: 40px; height: 40px; border-radius: 50%;" :src="data.user.avatar" alt="">
              <span style="margin-left: 5px; color: white">{{ data.user.name }}</span><el-icon color="#fff"><arrow-down /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/manager/person')">个人资料</el-dropdown-item>
                <el-dropdown-item @click="router.push('/manager/password')">修改密码</el-dropdown-item>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    <!-- 下面部分开始 -->
    <div style="display: flex; height: calc(100vh - 80px); overflow-y: auto;">
      <div class="manager-main-left">
        <el-menu :default-active="router.currentRoute.value.path"
                 :default-openeds="['1', '2']"
                 router
        >
          <el-menu-item index="/manager/home">
            <el-icon><HomeFilled /></el-icon>
            <span>系统首页</span>
          </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item v-if="data.user.role === 'ADMIN'" index="/manager/notice">系统公告</el-menu-item>
            <el-menu-item v-if="data.user.role === 'ADMIN'" index="/manager/examPlan">考试安排</el-menu-item>
            <el-menu-item v-if="data.user.role === 'ADMIN'" index="/manager/questionType">题型信息</el-menu-item>
            <el-menu-item index="/manager/course">课程信息</el-menu-item>
            <el-menu-item index="/manager/question">题库信息</el-menu-item>
            <el-menu-item index="/manager/testPaper">试卷信息</el-menu-item>
            <el-menu-item index="/manager/score">
              {{ data.user.role === 'ADMIN' ? '成绩管理' : '阅卷打分'}}
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2" v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/teacher">教师信息</el-menu-item>
            <el-menu-item index="/manager/student">学生信息</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
      <div class="manager-main-right">
        <RouterView @updateUser="updateUser" />
      </div>
    </div>
    <!-- 下面部分结束 -->
  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router/index.js";
import { ElMessage } from "element-plus";
import { ArrowDown, HomeFilled, Menu } from "@element-plus/icons-vue";

const data = reactive({
  user: {
    avatar: "@/assets/imgs/broserlogo.png" ,// 默认头像
    name: "管理员", // 默认名称
    role: "ADMIN", // 默认角色
  },
});

const logout = () => {
  localStorage.removeItem("xm-user");
  router.push("/login");
};

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem("xm-user") || "{}");
};
</script>

<style scoped>
/* 版心样式 */
.wrapper {
  margin: 0 auto;
  width: 1200px;
}

/* 头部容器 */
.header {
  height: 80px; /* 缩短顶部高度 */
  background-color: #9dc0ec;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

/* 头部内容布局 */
.header .wrapper {
  padding-top: 10px; /* 调整内边距 */
  display: flex;
  align-items: center;
  height: 100%;
}

/* Logo样式 */
.logo-link {
  display: block;
  width: 234px;
  height: 50px;
  background-image: url('@/assets/imgs/LOGO copy.png');
  background-size: contain;
  background-repeat: no-repeat;
  font-size: 0;
  text-decoration: none;
}

/* 导航菜单 */
.navigator {
  margin-left: 30px;
  flex-grow: 1;
}

.navigator ul {
  display: flex;
  gap: 24px;
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav-link {
  padding: 0 8px;
  color: #333;
  font-size: 16px;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.3s ease;
  border-bottom: 2px solid transparent;
}

.nav-link:hover {
  color: #ffffff;
  border-bottom-color: #ffffff;
}

/* 用户信息区域 */
.user {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-link {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  color: #333;
  transition: all 0.3s ease;
}

.icon-yonghu {
  font-size: 24px;
  color: #666;
  transition: inherit;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
}

/* 悬停效果 */
.user-link:hover .icon-yonghu,
.user-link:hover .user-name {
  color: #f7f9fa;
  transform: scale(1.05);
}
</style>