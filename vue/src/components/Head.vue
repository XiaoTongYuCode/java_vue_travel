<!-- eslint-disable vue/multi-word-component-names -->
<script>
export default {
  name: "Head",
  data(){
    return{
      user:'',

    }
  },
  methods:{
    toUserHome(){
      this.$router.replace("/UserHome");
    },
    logOff(){
      this.$confirm('要退出了吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$notify({
          title: this.user.name,
          message: '已退出登陆',
          type: 'success'
        });
        sessionStorage.setItem("User",null);
        this.$router.replace("/");
      }).catch(() => {});
    },
  },
  created() {
    this.user = JSON.parse(sessionStorage.getItem("User"));
  }
}
</script>

<template>
  <div style="width: 100%;height: 100%; display: flex; justify-content:space-between;">
    <div style="display: flex;align-items: center; margin-left: 50px;">
      <svg t="1715936092715" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2881" width="30" height="30"><path d="M994.201058 419.031182c-29.824942-34.984932-66.070871-65.655872-107.67279-90.358824-47.869907-28.785944-102.4068-49.596903-161.604684-60.285882-12.605975-2.339995-25.44495-4.100992-38.505925-5.43699-78.694846-8.063984-154.443698 2.649995-221.497567 28.060946 27.799946-26.161949 58.423886-56.874889 88.999826-91.153822C625.189779 119.961766 670.93969 49.096904 691.25265 15.099971c5.909988-9.888981-6.694987-17.899965-21.987957-14.145973-44.401913 10.909979-86.088832 25.83595-123.704759 44.466913-42.615917 20.879959-80.576843 46.589909-112.079781 76.76585-6.737987 6.398988-13.131974 13.049975-19.279962 19.919961-38.751924 43.289915-61.60988 91.251822-69.039865 140.090727-24.032953-31.023939-58.184886-51.793899-101.043803-59.596884-6.004988-1.094998-12.066976-1.959996-18.242964-2.517995-29.025943-2.769995-60.483882-0.105-93.479818 7.488986-29.637942 6.697987-61.097881 17.589966-93.643817 32.409936-11.389978 5.18499-17.269966 16.245968-10.08598 19.352962 24.784952 10.729979 79.100846 31.536938 153.323701 45.019912 26.042949 4.730991 51.0939 7.933985 74.017855 10.077981-38.564925 13.997973-74.519854 39.444923-104.366796 77.215849-6.469987 8.189984-12.492976 16.673967-17.944965 25.56495-25.861949 41.599919-40.78992 89.398825-45.222912 139.384728-3.999992 42.191918-0.207 84.889834 10.31398 125.780754 3.450993 13.416974 20.51396 19.803961 29.982942 11.533978 31.904938-27.859946 94.045816-86.036832 153.799699-164.764679 34.337933-45.242912 62.128879-89.705825 82.929838-126.285753 21.129959 18.172965 40.92792 37.819926 59.407884 58.793885C510.750002 578.236871 564.145898 702.578628 572.089883 838.780362c-1.084998-0.005-2.172996-0.01-3.262994-0.01-154.775698 0-292.503429 66.59687-380.383257 185.227638H949.409146c-41.600919-59.901883-93.879817-97.555809-154.216699-129.129748a491.56204 491.56204 0 0 0-24.209953-12.005976C766.129504 755.951524 722.430589 636.638757 645.506739 539.268947c-35.177931-44.579913-74.757854-84.606835-119.073767-118.708768 42.669917 7.398986 90.389823 14.342972 141.377724 19.569961 137.709731 14.109972 254.737502 11.105978 311.216392 8.155984 16.549968-0.856998 25.206951-17.481966 15.17297-29.253942" fill="#FFAC27" p-id="2882"></path></svg>
      <span style="font-size: larger;margin-left: 10px;">去哪旅行</span>
    </div>
    <el-menu router mode="horizontal"
             background-color="#b2ceee" text-color="black" >
      <el-menu-item index="/NoticePage">
        <i class="el-icon-s-home"></i>
        <span slot="title">公告</span>
      </el-menu-item>
      <el-menu-item index="/home">
        <i class="el-icon-sunset"></i>
        <span slot="title">景点信息</span>
      </el-menu-item>
      <el-menu-item index="/line">
        <i class="el-icon-share"></i>
        <span slot="title">景点攻略</span>
      </el-menu-item>
      <el-menu-item index="/hotel">
        <i class="el-icon-office-building"></i>
        <span slot="title">房源推荐</span>
      </el-menu-item>
      <el-menu-item index="/userHome" >
        <i class="el-icon-user-solid"></i>
        <span slot="title">我的主页</span>
      </el-menu-item>
      <el-menu-item index="/UserOrder" >
        <i class="el-icon-tickets"></i>
        <span slot="title">我的订单</span>
      </el-menu-item>
      <el-submenu index="1" v-if="user.roleId === 0">
        <template slot="title">
          <i class="el-icon-s-tools"></i>
          <span>管理</span>
        </template>
        <el-menu-item index="/orderControl" >
          <i class="el-icon-finished"></i>
          <span slot="title">订单审核</span>
        </el-menu-item>
        <el-menu-item index="/userControl" >
          <i class="el-icon-user"></i>
          <span slot="title">系统用户管理</span>
        </el-menu-item>
        <el-menu-item index="/LineControl">
          <i class="el-icon-share"></i>
          <span slot="title">景点攻略管理</span>
        </el-menu-item>
        <el-menu-item index="/ScenicControl">
          <i class="el-icon-tickets"></i>
          <span slot="title">景点信息管理</span>
        </el-menu-item>
        <el-menu-item index="/hotelControl">
          <i class="el-icon-office-building"></i>
          <span slot="title">房源推荐管理</span>
        </el-menu-item>
        <el-menu-item index="/commentControl" >
          <i class="el-icon-chat-line-square"></i>
          <span slot="title">景点评论管理</span>
        </el-menu-item>
      </el-submenu>

      <el-menu-item @click="logOff">
        <i class="el-icon-switch-button"></i>
        <span slot="title">退出</span>
      </el-menu-item>
      <div style="display: flex; align-items: center;">
        <el-avatar shape="square" :size="35" style="margin: 0 10px;background-color: #cc9c7a;">
          {{ user.name ? user.name.charAt(0) : user.no.charAt(0)}}
        </el-avatar>
        <el-dropdown>
        <span class="el-dropdown-link" style="font-size: medium;">
          {{ this.user.name }}
          <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="toUserHome">个人中心</el-dropdown-item>
            <el-dropdown-item @click.native="logOff">退出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-menu>
  </div>
</template>

<style scoped>

</style>