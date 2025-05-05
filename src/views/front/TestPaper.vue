<template>
  <div style="width: 40%; margin: 30px auto">
    <div style="font-size: 22px; font-weight: bold; text-align: center">{{ data.testPaperData.name }}</div>
    <div style="margin-top: 15px; color: #666666; text-align: center">
      <span>课程名称：{{ data.testPaperData.courseName }}</span>
      <span style="margin: 0 30px">授课教师：{{ data.testPaperData.teacherName }}</span>
      <span>考试时间：{{ data.testPaperData.time }} 分钟</span>
    </div>
    <div style="margin-top: 10px; text-align: center; color: red">
      考试倒计时：{{ data.hour }} 时：{{ data.minutes }} 分：{{ data.seconds }} 秒
    </div>
    <div style="margin-top: 50px">
      <div v-for="item in data.testPaperData.questions" style="margin-bottom: 20px">
        <div style="font-weight: bold; font-size: 16px; background-color: #ddf1ec; line-height: 30px; padding: 5px; margin-bottom: 10px">
          {{ item.name }}（{{ item.typeScore }} 分）
        </div>
        <!--   题目下面要根据具体的题型来设计具体的样式了     -->
        <!--   typeId === 1：单选题     -->
        <div v-if="item.typeId === 6">
          <el-radio-group v-model="item.newAnswer">
            <el-radio value="A">A. {{ item.optionA }}</el-radio>
            <el-radio value="B">B. {{ item.optionB }}</el-radio>
            <el-radio value="C">C. {{ item.optionC }}</el-radio>
            <el-radio value="D">D. {{ item.optionD }}</el-radio>
          </el-radio-group>
        </div>
        <!--   typeId === 2：多选题     -->
        <div v-if="item.typeId === 7">
          <el-checkbox-group v-model="item.checkList">
            <el-checkbox :label="'A. ' +  item.optionA" value="A" />
            <el-checkbox :label="'B. ' +  item.optionB" value="B"  />
            <el-checkbox :label="'C. ' +  item.optionC" value="C" />
            <el-checkbox :label="'D. ' +  item.optionD" value="D" />
          </el-checkbox-group>
        </div>
        <!--   typeId === 3：判断题     -->
        <div v-if="item.typeId === 3">
          <el-radio-group v-model="item.newAnswer">
            <el-radio value="正确">A. 正确</el-radio>
            <el-radio value="错误">B. 错误</el-radio>
          </el-radio-group>
        </div>
        <!--   typeId === 4：填空题     -->
        <div v-if="item.typeId === 8">
          <el-input style="width: 50%" v-model="item.newAnswer" placeholder="请输入您的答案"></el-input>
        </div>
        <!--   typeId === 5：简答题     -->
        <div v-if="item.typeId === 9">
          <el-input type="textarea" :rows="8" v-model="item.newAnswer" placeholder="请输入您的答案"></el-input>
        </div>
      </div>
      <div style="text-align: center; margin: 50px">
        <el-button style="padding: 20px 40px" type="primary" @click="submitPaper">提交试卷</el-button>
      </div>
    </div>
  </div>
</template>
<script setup>
import {reactive, onMounted,onBeforeUnmount} from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

const data = reactive({
  testPaperId: router.currentRoute.value.query.id,
  testPaperData: {},
  hour: 0,
  minutes: 0,
  seconds: 0,
})

let time;
let warningCount = 0;  // 记录切屏次数
let forcedSubmit = false;
const loadTestPaper = () => {
  data.testPaperId = router.currentRoute.value.query.id
  request.get('/testPaper/selectById/' + data.testPaperId).then(res => {
    if (res.code === '200') {
      data.testPaperData = res.data
      // 考试开始时提示一次
      ElMessageBox.alert('请注意：考试过程中禁止切屏或切换窗口，否则将被视为异常行为。', '防作弊提醒', {
        confirmButtonText: '我已知晓',
        type: 'warning'
      });


      let maxTime = data.testPaperData.maxTime
      time = setInterval(() => {
        if (maxTime >= 0) {
          let remain = Math.floor(maxTime % 3600)
          data.hour = Math.floor(maxTime / 3600)
          data.minutes = Math.floor( remain / 60)
          data.seconds = Math.floor(remain % 60)
          maxTime--
        } else {
          ElMessage.error('时间到')
          // 做一下实际的业务，在这个项目里面就是提交试卷了
          submitPaper()
        }
      }, 1000)
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const handleBlur = () => {
  warningCount++;

  if (warningCount < 3) {
    ElMessageBox.alert(`检测到第 ${warningCount} 次切屏行为，切屏超过 3 次将强制交卷！`, '切屏警告', {
      confirmButtonText: '返回考试',
      type: 'error'
    });
  }

  if (warningCount >= 3 && !forcedSubmit) {
    forcedSubmit = true;
    ElMessageBox.alert('切屏次数过多，试卷已被强制提交。', '强制交卷', {
      confirmButtonText: '确定',
      type: 'error'
    }).then(() => {
      submitPaper();
    });
  }
}

const handleVisibilityChange = () => {
  if (document.hidden) {
    handleBlur();
  }
}

onMounted(() => {
  loadTestPaper();
  window.addEventListener('blur', handleBlur);
  document.addEventListener('visibilitychange', handleVisibilityChange);
});

onBeforeUnmount(() => {
  window.removeEventListener('blur', handleBlur);
  document.removeEventListener('visibilitychange', handleVisibilityChange);
  clearInterval(time);
})

const submitPaper = () => {
  clearInterval(time);
  request.post('/score/add', data.testPaperData).then(res => {
    if (res.code === '200') {
      ElMessage.success('试卷已提交');
      setTimeout(() => {
        location.href = '/front/score';
      }, 500);
    } else {
      ElMessage.error(res.msg);
    }
  })
}

</script>
<style scoped>
.el-radio-group {
  display: block;
}
.el-radio {
  display: block;
}
.el-checkbox {
  display: block;
}
</style>