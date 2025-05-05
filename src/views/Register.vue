<template>
  <div class="container">
    <div class="login-box">
      <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px; color: #000">
        欢迎使用狮山考试通！
      </div>
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <el-form-item prop="role">
          <el-select size="large" v-model="data.form.role" placeholder="请选择角色">
            <el-option value="TEACHER" label="教师"></el-option>
            <el-option value="STUDENT" label="学生"></el-option>
          </el-select>
        </el-form-item>
        <!-- 新增邮箱输入框 -->
        <el-form-item prop="email">
          <el-input size="large" v-model="data.form.email" placeholder="请输入邮箱">
            <template #prefix>
              <el-icon><Message /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入学工号"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" style="width: 100%" @click="register">注 册</el-button>
        </el-form-item>
        <div style="text-align: right">
          已有账号？请 <a href="/login">登录</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock, Message } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";

const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error("请确认密码"));
  } else {
    if (value!== data.form.password) {
      callback(new Error("确认密码跟原密码不一致!"));
    }
    callback();
  }
};

// 新增邮箱验证规则
const validateEmail = (rule, value, callback) => {
  const reg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  if (!value) {
    callback(new Error("请输入邮箱"));
  } else if (!reg.test(value)) {
    callback(new Error("请输入有效的邮箱地址"));
  } else {
    callback();
  }
};

const data = reactive({
  form: {
    name: "", // 新增name字段
    email: ""
  },
  rules: {
    email: [
      { required: true, validator: validateEmail, trigger: "blur" }
    ],
    username: [
      { required: true, message: "请输入学工号", trigger: "blur" }
    ],
    password: [
      { required: true, message: "请输入密码", trigger: "blur" }
    ],
    role: [
      { required: true, message: "请选择角色", trigger: "blur" }
    ]
  }
});

const formRef = ref();

const register = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      // 根据角色调整数据
      if (data.form.role === "STUDENT") {
        data.form.id = data.form.username; // 假设学工号作为学生id
      } else if (data.form.role === "TEACHER") {
        data.form.id = data.form.username; // 假设学工号作为教师id
      }
      data.form.name = data.form.name || data.form.username; // 若未输入name，用username代替
      request.post("/register", data.form).then((res) => {
        if (res.code === "200") {
          ElMessage.success("注册成功");
          setTimeout(() => {
            location.href = "/login";
          }, 500);
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};
</script>

<style scoped>
/* 保持原有样式不变 */
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  margin: 0;
  padding: 0;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 1;
}

.container::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('@/assets/imgs/login-picture.JPG');
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  opacity: 0.8;
  z-index: -1;
}

.login-box {
  width: 100%;
  max-width: 400px;
  padding: 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.9);
  position: relative;
  z-index: 2;
}
</style>