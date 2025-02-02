<!-- eslint-disable vue/multi-word-component-names -->
<script>
import Head from "@/components/Head.vue";

export default {
  name: "IndexPage",
  components: {Head},
  data(){
    return{

    }
  },
}
</script>

<template>
<div>
  <el-container style="height: 100vh; border: 1px solid #eee">
<!--    <el-aside width="200px" style="background-color: rgb(238, 241, 246); min-height: 90vh;">-->
<!--      <Aside style="height: 100%;"></Aside>-->
<!--    </el-aside>-->

    <el-container>
      <el-header style="padding: 0 10px; background-color: #b2ceee;">
        <Head></Head>
      </el-header>

      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</div>
</template>

<style scoped>
*{
  padding: 0;
  margin: 0;
}
.el-header {
  background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}
</style>