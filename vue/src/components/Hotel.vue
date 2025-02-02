<!-- eslint-disable vue/multi-word-component-names -->
<script>
export default {
  name: "Hotel",
  data(){
    return{
      tableData:[],
      Name:'',
      scenicName:'',

      distance_sort: true,

      pageSize:10,
      pageNum:1,
      total:0,
    }
  },
  methods:{
    loadData(){
      this.$axios.post(this.$httpUrl + "/hotel/list",{
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          name: this.Name,
          scenicName: this.scenicName,
        }
      }).then(res => res.data).then(res => {
        this.tableData = res.data;
        this.total = res.total;

        if(this.distance_sort){
          this.tableData = this.tableData.sort((a, b) => a.distance - b.distance);
        }else {
          this.tableData = this.tableData.sort((a, b) => a.tickets - b.tickets);
        }
      })
    },
    reset(){
      this.Name='';
      this.scenicName='';
      this.pageSize=10;
      this.pageNum=1;
      this.loadData();
    },
    goTo(id){
      // window.open('/scenic/' + id);
      this.$router.replace('/scenic/' + id);
    },



    handleSizeChange(val) {
      this.pageNum = 1;
      this.pageSize = val;
      this.loadData();
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.loadData();
    }

  },
  created() {
    this.loadData();
  }
}
</script>

<template>
  <div>
    <div style="display: flex;margin: 20px 0; align-items: center;justify-content: center;">
      <el-input v-model="Name" placeholder="房源名称" style="width: 150px; margin: 0 10px;"></el-input>
      <el-input v-model="scenicName" placeholder="景区名称" style="width: 150px; margin-right: 10px;"></el-input>
      <el-button icon="el-icon-search" @click="loadData" type="primary"></el-button>
      <el-button icon="el-icon-refresh" @click="reset" type="info"></el-button>
      <el-switch
          style="display: block;margin-left: 10px;"
          v-model="distance_sort"
          @change="loadData"
          active-text="距离优先"
          inactive-text="价格优先">
      </el-switch>
    </div>
    <el-empty v-if="tableData.length===0" description="暂 无 房 源"></el-empty>

    <div style="width: 100%; display: flex;flex-wrap: wrap;">
      <el-card class="box-card" v-for="(item,index) in tableData" :key="index" :body-style="{'padding':'0'}">
        <div style="position: relative;">
          <el-image :src="item.img?item.img: 'https://img0.baidu.com/it/u=3369353589,1949555918&fm=253&fmt=auto&app=138'"
                    style="width: 100%;height: 100%; max-height: 380px; margin-bottom: 10px;" class="imgs">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
          <div style="position: absolute; bottom: 35px;left: 20px; color: white; font-size: x-large; font-weight: bold;">
            {{ item.name }}
          </div>
          <div style="position: absolute; bottom: 35px;right: 20px;font-size: small;color: white;">
            距景点 {{item.distance}} km
          </div>
        </div>
        <div style="margin-bottom: 50px;padding: 10px;">
          {{ item.txt }}
        </div>
        <div style="position: absolute; width: 100%; bottom: 5px;display: flex; justify-content: space-between;">
          <div style="display: flex;align-items: end;margin-left: 10px;">
            {{item.tickets}}/晚
          </div>
          <div style="margin-right: 10px;display: flex;align-items: center;justify-content: end;">
            <i class="el-icon-location-information"></i>
            {{item.scenicName}}
          </div>
        </div>
      </el-card>
    </div>

    <div style="text-align: center;margin-bottom: 50px;">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[5, 10, 20, 40]"
          :page-size="this.pageSize"
          :current-page="this.pageNum"
          layout="total, sizes, prev, pager, next, jumper"
          :total="this.total">
      </el-pagination>
    </div>
  </div>
</template>

<style scoped>
.box-card{
  position: relative;
  width: 30%;
  margin-left: 2%;
  margin-bottom: 30px;
}
.imgs::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 100%; /* 遮罩覆盖整个图片 */
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0.7) 100%);
  /* 渐变效果：从透明到黑色 */
}
</style>