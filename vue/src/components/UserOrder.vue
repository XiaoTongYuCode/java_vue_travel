<!-- eslint-disable vue/multi-word-component-names -->
<script>
export default {
  name: "orderControl",
  data(){

    return{
      tableData:[],
      scenic_name: '',

      user: JSON.parse(sessionStorage.getItem('User')),
      pageSize:10,
      pageNum:1,
      total:0,

    }
  },
  methods:{
    getData(){
      //从后端获取数据
      this.$axios.post(this.$httpUrl + "/order/list",{
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          scenic_name: this.scenic_name,
          no: this.user.no,
        }
      }).then(res => res.data).then(res => {
        this.tableData = res.data;
        this.total = res.total;
      })
    },
    reset(){
      this.scenic_name ='';
      this.name ='';
      this.getData();
    },
    handleSizeChange(val) {
      this.pageNum = 1;
      this.pageSize = val;
      this.getData();
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.getData();
    }
  },
  created() {
    this.getData();
  }
}
</script>

<template>
  <div style="text-align: center;padding: 10px;">
    <div style="display: flex;align-items: center;margin-left: 10px;">
      <el-input v-model="scenic_name" placeholder="订单名" style="width: 200px; margin: 0 10px;"></el-input>
      <el-button icon="el-icon-search" @click="getData" type="primary"></el-button>
      <el-button icon="el-icon-refresh" @click="reset" type="info"></el-button>
    </div>
    <el-empty v-if="tableData.length===0" description="暂无订单数据"></el-empty>
    <el-table
        v-if="tableData.length>0"
        :data="tableData"
        stripe
        style="width: 100%">
      <el-table-column
          prop="id"
          label="时间">
        <template slot-scope="scope">
          {{ new Date(scope.row.id * 1000).toLocaleString() }}
        </template>
      </el-table-column>

      <el-table-column
          prop="scenicName"
          label="订单名">
      </el-table-column>

      <el-table-column
          prop="tickets"
          label="价格">
        <template slot-scope="scope">
          {{ scope.row.tickets }}元
        </template>
      </el-table-column>

      <el-table-column
          v-if="!isCheck"
          label="状态"
          width="120">
        <template slot-scope="scope">
          <el-tag effect="plain" :type="scope.row.checks ==='y' ? 'success' : (scope.row.checks ==='n' ? 'warning' : 'info')">
            <i :class="scope.row.checks ==='y' ? 'el-icon-check' : (scope.row.checks ==='n' ? 'el-icon-close' : 'el-icon-loading')"> </i>
            {{scope.row.checks ==='y' ? '已完成' : (scope.row.checks ==='n' ? '已退单' : '待审核')}}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

<!--    <el-pagination-->
<!--        @size-change="handleSizeChange"-->
<!--        @current-change="handleCurrentChange"-->
<!--        :page-sizes="[5, 10, 20, 40]"-->
<!--        :page-size="this.pageSize"-->
<!--        :current-page="this.pageNum"-->
<!--        layout="total, sizes, prev, pager, next, jumper"-->
<!--        :total="this.total">-->
<!--    </el-pagination>-->

  </div>
</template>

<style scoped>

</style>