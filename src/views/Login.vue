<template>
  <div class="login-root">
    <div class="login-container">
      <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px; color: #000">
        欢迎使用狮山考试通！
      </div>
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <div class="form-group">
          <label for="id">学工号</label>
          <el-input
              id="id"
              :prefix-icon="User"
              size="large"
              v-model="data.form.id"
              placeholder="请输入学工号"
          ></el-input>
          <div class="error-message" v-if="errors.id">{{ errors.id }}</div>
        </div>

        <div class="form-group">
          <label for="password">密码</label>
          <el-input
              id="password"
              show-password
              :prefix-icon="Lock"
              size="large"
              v-model="data.form.password"
              placeholder="请输入密码"
          ></el-input>
          <div class="error-message" v-if="errors.password">{{ errors.password }}</div>
        </div>

        <div class="form-group">
          <label for="role">角色</label>
          <el-select
              id="role"
              size="large"
              v-model="data.form.role"
              placeholder="请选择角色"
          >
            <el-option value="ADMIN" label="管理员"></el-option>
            <el-option value="TEACHER" label="教师"></el-option>
            <el-option value="STUDENT" label="学生"></el-option>
          </el-select>
        </div>

        <div class="form-footer">
          <el-button size="large" type="primary" style="width: 100%" @click="login">登 录</el-button>
        </div>

        <div class="register">
          <label>还没有账号？</label>
          <a href="/register">注册</a>
          <label>忘记了密码？</label>
          <a href="/reset">重置</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  form: {
    id: "",
    password: "",
    role: "STUDENT",
  },
  rules: {
    id: [
      { required: true, message: "请输入学工号", trigger: "blur" },
    ],
    password: [
      { required: true, message: "请输入密码", trigger: "blur" },
    ],
    role: [
      { required: true, message: "请选择角色", trigger: "blur" },
    ],
  },
});

const formRef = ref();
const errors = reactive({
  id: "",
  password: "",
});
const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post("/login", data.form).then((res) => {
        if (res.code === "200") {
          ElMessage.success("登录成功");
          localStorage.setItem("xm-user", JSON.stringify(res.data));
          setTimeout(() => {
            if ("STUDENT" === res.data.role) {
              location.href = "/front/home";
            } else {
              location.href = "/manager/home";
            }
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
.login-root {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.login-root::before {
  content: '';
  position: absolute;
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

.login-container {
  width: 400px;
  padding: 30px;
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.login-container div {
  margin-bottom: 15px;
}

.login-container label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.login-container .el-input__inner {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}

.login-container .el-select {
  width: 100%;
}

.login-container .el-button {
  width: 100%;
  padding: 10px;
  background-color: #3498db;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  margin-top: 20px;
}

.login-container .el-button:hover {
  background-color: #2980b9;
}

.register {
  text-align: right;
  margin-top: 15px;
}

.register a {
  color: #3498db;
  text-decoration: none;
}
</style>