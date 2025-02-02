<!-- eslint-disable vue/multi-word-component-names -->
<script>
export default {
  name: "orderControl",
  data(){

    return{
      tableData:[],
      scenic_name: '',
      name: '',
      isCheck:false,

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
          name: this.name,
          checks: this.isCheck ? 'Y':''
        }
      }).then(res => res.data).then(res => {
        console.log(res.data)
        this.tableData = res.data;
        this.total = res.total;
      })
    },
    reset(){
      this.scenic_name ='';
      this.name ='';
      this.getData();
    },
    delUser(pid){
      this.$axios.get(this.$httpUrl + "/order/delete?pid=" + pid).then(res => res.data)
          .then(res => {
            if (res.code === 200){
              this.$message({
                message: '删除成功',
                type: 'success'
              });
              this.getData();
            }else {
              this.$message.error('删除失败');
            }
          })
    },
    upChecks(pid, type){
      this.$axios.post(this.$httpUrl + "/order/checks",{
        type: type,
        pid:pid
      }).then(res => res.data)
          .then(res => {
            if (res.code === 200){
              this.$message({
                message: '已审核',
                type: 'success'
              });
              this.getData();
            }else {
              this.$message.error('审核失败');
            }
          })
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
    <div style="display: flex;align-items: center;">
      <el-input v-model="scenic_name" placeholder="景区名" style="width: 200px; margin: 0 10px;"></el-input>
      <el-input v-model="name" placeholder="用户名" style="width: 150px; margin: 0 10px;"></el-input>
      <el-button icon="el-icon-search" @click="getData" type="primary"></el-button>
      <el-button icon="el-icon-refresh" @click="reset" type="info"></el-button>
      <el-switch
        v-model="isCheck"
         @change="getData"
        active-text="审核模式"
        active-color="#13ce66"
        style="margin-left: 50px;">
      </el-switch>
    </div>
    <el-empty v-if="tableData.length===0" :description="isCheck?'都处理完啦，无未审核订单':'暂无订单数据'"></el-empty>
    <el-table
        v-if="tableData.length>0"
        :data="tableData"
        stripe
        style="width: 100%">
      <el-table-column
          prop="id"
          label="交易时间">
        <template slot-scope="scope">
          {{ new Date(scope.row.id * 1000).toLocaleString() }}
        </template>
      </el-table-column>

      <el-table-column
          prop="scenicId"
          label="景区ID">
      </el-table-column>

      <el-table-column
          prop="no"
          label="用户账号">
      </el-table-column>

      <el-table-column
          prop="name"
          label="用户名称">
      </el-table-column>

      <el-table-column
          prop="scenicName"
          label="景区">
      </el-table-column>

      <el-table-column
          prop="tickets"
          label="交易金额">
        <template slot-scope="scope">
          {{ scope.row.tickets }}元
        </template>
      </el-table-column>

      <el-table-column
          v-if="!isCheck"
          label="状态">
        <template slot-scope="scope">
          <el-tag effect="plain" :type="scope.row.checks ==='y' ? 'success' : (scope.row.checks ==='n' ? 'warning' : 'info')">
            <i :class="scope.row.checks ==='y' ? 'el-icon-check' : (scope.row.checks ==='n' ? 'el-icon-close' : 'el-icon-loading')"> </i>
            {{scope.row.checks ==='y' ? '已完成' : (scope.row.checks ==='n' ? '已退单' : '待批准')}}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" prop="operate" width="270">
        <template slot-scope="scope">
          <div style="display: flex;">
            <div v-if="scope.row.checks === 'w' & isCheck">
            <el-button plain type="success" size="small" @click="upChecks( scope.row.pid,'y')" icon="el-icon-check">批准</el-button>
            <el-button plain type="info" size="small" @click="upChecks( scope.row.pid,'n')" icon="el-icon-close">拒绝</el-button>
            </div>
          <el-popconfirm
              confirm-button-text='好的'
              cancel-button-text='点错了'
              icon="el-icon-info"
              icon-color="red"
              title="确定删除吗？"
              @confirm ="delUser(scope.row.pid)"
              style="margin-left: 5px;"
          >
            <el-button slot="reference" type="danger" size="small" icon="el-icon-delete"></el-button>
          </el-popconfirm>
          </div>
        </template>
      </el-table-column>
    </el-table>

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
</template>

<style scoped>

</style>