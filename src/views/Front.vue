<template>
  <div>
    <div class="front-notice"><el-icon><Bell /></el-icon>公告：{{ data.top }}</div>
    <div class="header">
      <div class="wrapper">
        <!-- logo -->
        <div class="logo">
          <h1><router-link to="/front/home" class="logo-link">在线考试系统</router-link></h1>
        </div>
        <!-- 导航 -->
        <div class="navigator">
          <ul>
            <li><router-link to="/front/home" class="nav-link">首页</router-link></li>
            <li><router-link to="/front/exam" class="nav-link">在线考试</router-link></li>
            <li><router-link to="/front/score" class="nav-link">我的成绩</router-link></li>
            <li><router-link to="/front/person" class="nav-link">个人中心</router-link></li>
          </ul>
        </div>
        <!-- 用户 -->
        <div class="user">
          <div v-if="!data.user.id">
            <el-button @click="router.push('/login')">登录</el-button>
            <el-button @click="router.push('/register')">注册</el-button>
          </div>
          <div v-else>
            <el-dropdown style="cursor: pointer; height: 60px">
              <div style="display: flex; align-items: center">
                <img style="width: 40px; height: 40px; border-radius: 50%;" :src="data.user.avatar" alt="">
                <span style="margin-left: 5px;">{{ data.user.name }}</span><el-icon><arrow-down /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>
    </div>
    <div class="main-body">
      <RouterView @updateUser="updateUser" />
    </div>
  </div>
</template>

<script setup>
import router from "@/router/index.js";
import { reactive } from "vue";
import request from "@/utils/request.js";
import { Bell, ArrowDown } from "@element-plus/icons-vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  top: '',
  noticeData: []
});

const logout = () => {
  localStorage.removeItem('xm-user');
  router.push('/login');
};

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('xm-user') || '{}');
};

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    data.noticeData = res.data;
    let i = 0;
    if (data.noticeData && data.noticeData.length) {
      data.top = data.noticeData[0].content;
      setInterval(() => {
        data.top = data.noticeData[i].content;
        i++;
        if (i === data.noticeData.length) {
          i = 0;
        }
      }, 2500);
    }
  });
};
loadNotice();
</script>

<style scoped>
/* 版心样式 */
.wrapper {
  margin: 0 auto;
  width: 1200px;
}

/* 头部容器 */
.header {
  height: 100px;
  background-color: #9dc0ec;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

/* 头部内容布局 */
.header .wrapper {
  padding-top: 15px;
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

/* 公告样式 */
.front-notice {
  background-color: #f8f9fa;
  padding: 10px;
  text-align: center;
  font-size: 14px;
  color: #333;
  margin-bottom: 10px;
}
</style>