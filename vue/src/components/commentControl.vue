<!-- eslint-disable vue/multi-word-component-names -->
<script>
export default {
  name: "commentControl",
  data(){

    return{
      tableData:[],
      txt: '',
      loading: false,

      user: JSON.parse(sessionStorage.getItem('User')),
      pageSize:10,
      pageNum:1,
      total:0,

    }
  },
  methods:{
    getData(){
      this.loading = true;
      //从后端获取数据
      this.$axios.post(this.$httpUrl + "/comment/list",{
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          txt: this.txt,
        }
      }).then(res => res.data).then(res => {
        console.log(res.data)
        this.tableData = res.data;
        this.total = res.total;
        this.loading = false;
      })
    },
    reset(){
      this.txt ='';
      this.getData();
    },
    delUser(id){
      this.$axios.get(this.$httpUrl + "/comment/delete?id=" + id).then(res => res.data)
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
    <div style="display: flex;">
      <el-input v-model="txt" placeholder="评论内容" style="width: 200px; margin: 0 10px;"></el-input>
      <el-button icon="el-icon-search" @click="getData" type="primary"></el-button>
      <el-button icon="el-icon-refresh" @click="reset" type="info"></el-button>
    </div>
    <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        style="width: 100%">
      <el-table-column
          prop="type"
          label="评论类型"
          width="200">
      </el-table-column>
      <el-table-column
          prop="scenicName"
          label="景区名称"
          width="200">
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
          prop="txt"
          label="评论内容">
      </el-table-column>


      <el-table-column label="操作" prop="operate">
        <template slot-scope="scope">
          <el-popconfirm
              confirm-button-text='好的'
              cancel-button-text='点错了'
              icon="el-icon-info"
              icon-color="red"
              title="确定删除吗？"
              @confirm ="delUser(scope.row.id)"
              style="margin-left: 5px;"
          >
            <el-button slot="reference" type="danger" size="small">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        style="margin: 20px;"
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