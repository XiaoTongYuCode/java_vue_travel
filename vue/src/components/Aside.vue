<!-- eslint-disable vue/multi-word-component-names -->
<script>
export default {
  name: "Aside",
  data(){
    return{
      user:'',

    }
  },
  methods:{
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
  },
}
</script>

<template>
  <el-menu router>
    <el-menu-item index="/home">
      <i class="el-icon-s-home"></i>
      <span slot="title">首页</span>
    </el-menu-item>
    <el-menu-item index="/line">
      <i class="el-icon-s-home"></i>
      <span slot="title">路线</span>
    </el-menu-item>
    <el-menu-item index="/userHome" >
      <i class="el-icon-user-solid"></i>
      <span slot="title">我的主页</span>
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
      <el-menu-item index="/ScenicControl">
        <i class="el-icon-office-building"></i>
        <span slot="title">景区管理</span>
      </el-menu-item>
      <el-menu-item index="/hotelControl">
        <i class="el-icon-office-building"></i>
        <span slot="title">景区酒店管理</span>
      </el-menu-item>
      <el-menu-item index="/commentControl" >
        <i class="el-icon-chat-line-square"></i>
        <span slot="title">景区评论管理</span>
      </el-menu-item>
    </el-submenu>
    
    <el-menu-item @click="logOff">
      <i class="el-icon-switch-button"></i>
      <span slot="title">退出登陆</span>
    </el-menu-item>
  </el-menu>
</template>

<style scoped>

</style>