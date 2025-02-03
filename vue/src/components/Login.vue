<!-- eslint-disable vue/multi-word-component-names -->
<script>
import '../assets/star.css';
export default {
  name: "Login",
  data(){
    var checkNo = (rule, value, callback) => {
      console.log(value)
      this.$axios.get(this.$httpUrl + '/user/only?no=' + value).then(res => res.data).then(res => {
        console.log(res)
        if (res.code === 200){
          callback();
        }else {
          return callback(new Error('该账号已存在！'));
        }
      })
    };
    return{
      user:'',
      centerDialogVisible: false,
      form: {
        no: '',
        name: '',
        password: '',
        roleId: '2'
      },
      rulesNew: {
        no: [
          {required: true, message: '请输入账号', trigger: 'blur'},
          {validator: checkNo, trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入名称', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'}
        ]
      },

      loginForm:{
        no:'',
        password:'',
      },

    }
  },
  methods:{
    login(){
      this.$axios.post(this.$httpUrl + "/user/login",this.loginForm)
              .then(res => res.data).then( res => {
            if (res.code === 200){
              this.user = res.data.user;
              this.$notify({
                title: this.user.name,
                message: '欢迎回家',
                type: 'success'
              });
              sessionStorage.setItem("User",JSON.stringify(res.data.user));
              this.$router.replace("/NoticePage");

            }else {
              this.$message({
                message: '账号或密码错误！',
                type: 'error'
              });
            }
          })
    },
    resetAddForm(){
      this.form= {
        no: '',
        name: '',
        password: '',
        roleId: '2'
      }
    },
    addUser(){
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.$axios.post(this.$httpUrl + "/user/add",this.form).then(res => res.data)
              .then(res => {
                if (res.code === 200){
                  this.$message({
                    message: '注册成功~',
                    type: 'success'
                  });
                  this.resetAddForm();
                  this.centerDialogVisible = false;
                }else {
                  this.$message.error('错了哦，添加失败');
                }
              })
        } else {
          console.log('error submit!!');
          return false;
        }
      });

    },

    handleClose(done) {
      this.$confirm('确认关闭？您的信息不会保存')
          .then(() => {
            done();
          })
          .catch(() => {
          });
    },
  }
}
</script>

<template>
<div class="Body">
  <div class="box">
    <div style="display: flex;align-items: center;margin: 20px;">
      <svg t="1715936092715" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2881" width="30" height="30"><path d="M994.201058 419.031182c-29.824942-34.984932-66.070871-65.655872-107.67279-90.358824-47.869907-28.785944-102.4068-49.596903-161.604684-60.285882-12.605975-2.339995-25.44495-4.100992-38.505925-5.43699-78.694846-8.063984-154.443698 2.649995-221.497567 28.060946 27.799946-26.161949 58.423886-56.874889 88.999826-91.153822C625.189779 119.961766 670.93969 49.096904 691.25265 15.099971c5.909988-9.888981-6.694987-17.899965-21.987957-14.145973-44.401913 10.909979-86.088832 25.83595-123.704759 44.466913-42.615917 20.879959-80.576843 46.589909-112.079781 76.76585-6.737987 6.398988-13.131974 13.049975-19.279962 19.919961-38.751924 43.289915-61.60988 91.251822-69.039865 140.090727-24.032953-31.023939-58.184886-51.793899-101.043803-59.596884-6.004988-1.094998-12.066976-1.959996-18.242964-2.517995-29.025943-2.769995-60.483882-0.105-93.479818 7.488986-29.637942 6.697987-61.097881 17.589966-93.643817 32.409936-11.389978 5.18499-17.269966 16.245968-10.08598 19.352962 24.784952 10.729979 79.100846 31.536938 153.323701 45.019912 26.042949 4.730991 51.0939 7.933985 74.017855 10.077981-38.564925 13.997973-74.519854 39.444923-104.366796 77.215849-6.469987 8.189984-12.492976 16.673967-17.944965 25.56495-25.861949 41.599919-40.78992 89.398825-45.222912 139.384728-3.999992 42.191918-0.207 84.889834 10.31398 125.780754 3.450993 13.416974 20.51396 19.803961 29.982942 11.533978 31.904938-27.859946 94.045816-86.036832 153.799699-164.764679 34.337933-45.242912 62.128879-89.705825 82.929838-126.285753 21.129959 18.172965 40.92792 37.819926 59.407884 58.793885C510.750002 578.236871 564.145898 702.578628 572.089883 838.780362c-1.084998-0.005-2.172996-0.01-3.262994-0.01-154.775698 0-292.503429 66.59687-380.383257 185.227638H949.409146c-41.600919-59.901883-93.879817-97.555809-154.216699-129.129748a491.56204 491.56204 0 0 0-24.209953-12.005976C766.129504 755.951524 722.430589 636.638757 645.506739 539.268947c-35.177931-44.579913-74.757854-84.606835-119.073767-118.708768 42.669917 7.398986 90.389823 14.342972 141.377724 19.569961 137.709731 14.109972 254.737502 11.105978 311.216392 8.155984 16.549968-0.856998 25.206951-17.481966 15.17297-29.253942" fill="#FFAC27" p-id="2882"></path></svg>
      <span style="font-size: x-large;">去哪旅行</span>
    </div>
    <el-input v-model="loginForm.no" clearable placeholder="账号" class="inputs" style="margin: 10px;"></el-input>
    <el-input type="password" show-password clearable v-model="loginForm.password" placeholder="密码" class="inputs" style="margin-top: 10px;"></el-input>
    <el-button style="width: 85%;margin-top: 25px;" type="primary" @click="login">登 陆</el-button>
    <div style="font-size: small;color: #333333;margin-top: 15px;">
      还没有账号？ <a style="color: #301d9a;" @click="centerDialogVisible = true">注册一个</a>
    </div>
  </div>
  <div class="ImgBack">
<!--    <div id="stars"></div>-->
<!--    <div id="stars2"></div>-->
<!--    <div id="stars3"></div>-->
  </div>
  <el-dialog
        :before-close="handleClose"
        :visible.sync="centerDialogVisible"
        center
        title="- 新朋友 -"
        width="30%">
      <el-form ref="form" :model="form" :rules="rulesNew" label-width="80px" status-icon style="height: 30vh;">
        <el-form-item label="账号" prop="no">
          <el-col :span="20">
            <el-input v-model="form.no" ></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-col :span="20">
            <el-input v-model="form.password" show-password></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addUser">注 册</el-button>
      </span>
    </el-dialog>
</div>
</template>

<style scoped>
.box{
  width: 300px;
  min-height: 300px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 10px;
  backdrop-filter: blur(2px);
  -webkit-backdrop-filter: blur(2px);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.4);
  text-align: center;
  padding: 20px;

  display: flex;
  align-items: center;
  flex-direction: column;
}

.ImgBack{
  z-index: -100;
  position: fixed;
  background-image: url("../assets/img/loginB.png");
  background-size: cover;
  background-position: center;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.videoBox{
  z-index: -100;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  background-color: #333333;
}
.inputs{
  width: 85%;
}

</style>