<!-- eslint-disable vue/multi-word-component-names -->
<script>
export default {
  name: "LineControl",
  data() {

    return {
      tableData: [],
      name: '',
      loading: false,
      showMod: false,

      user: JSON.parse(sessionStorage.getItem('User')),
      pageSize: 10,
      pageNum: 1,
      total: 0,

      modForm:{},
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        txt: [
          { required: true, message: '请输入简介', trigger: 'blur' }
        ],
      },

    }
  },
  methods: {
    getData() {
      this.loading = true;
      //从后端获取数据
      this.$axios.post(this.$httpUrl + "/line/list",{
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          name: this.name,
        }
      }).then(res => res.data).then(res => {
        this.tableData = res.data;
        this.total = res.total;
        this.loading=false;
      })
    },
    reset() {
      this.name = '';
      this.getData();
    },
    del(id) {
      this.$confirm('此操作将删除此路线 确认删除？', '敏感操作', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.get(this.$httpUrl + "/line/delete?id=" + id).then(res => res.data)
            .then(res => {
              if (res.code === 200) {
                this.$message({
                  message: '删除成功',
                  type: 'success'
                });
                this.getData();
              } else {
                this.$message.error('删除失败');
              }
            })
      }).catch(() => {

      });
    },
    mod(scenic){
      this.modForm = scenic;
      this.showMod = true;
    },
    modUp(){
      this.$refs['modForm'].validate((valid) => {
        if (valid) {
          //向后端提交修改后的user
          this.$axios.post(this.$httpUrl + "/line/up",this.modForm).then(res => res.data)
              .then(res => {
                if (res.code === 200){
                  this.$message({
                    message: '修改成功',
                    type: 'success'
                  });
                  this.modForm = {};
                  this.showMod = false;
                  this.getData();
                }else {
                  this.$message.error('错了哦，修改失败');
                }
              })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
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
      <el-input v-model="name" placeholder="路线名" style="width: 300px; margin: 0 10px;"></el-input>
      <el-button icon="el-icon-search" @click="getData" type="primary"></el-button>
      <el-button icon="el-icon-refresh" @click="reset" type="info"></el-button>
    </div>
    <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        style="width: 100%">
      <el-table-column
          prop="id"
          label="路线ID"
          width="100">
      </el-table-column>
      <el-table-column
          label="创建人"
          width="120">
        <template slot-scope="scope">
          <div style="margin-left: 10px;display: flex;align-items: center; font-size: 12px;">
            <el-avatar :size="30" style="margin-right: 5px;">{{ scope.row.userName.charAt(0) }}</el-avatar>
            {{scope.row.userName}}
          </div>
        </template>
      </el-table-column>
      <el-table-column
          prop="name"
          label="路线名称"
          width="200">
      </el-table-column>
      <el-table-column
          prop="tickets"
          label="票价"
          width="200">
        <template slot-scope="scope">
          {{scope.row.tickets}}元
        </template>
      </el-table-column>
      <el-table-column
          label="路线图"
          width="450">
        <template slot-scope="scope">
          <el-image
              :src="scope.row.img"
              :preview-src-list="[scope.row.img]"
              style="height: 150px;">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
        </template>
      </el-table-column>

      <el-table-column
          prop="txt"
          label="路线简介">
        <template slot-scope="scope">
          <div style="max-height: 150px;overflow-y: auto;">
            {{ scope.row.txt }}
          </div>
        </template>
      </el-table-column>


      <el-table-column label="操作" prop="operate" width="220">
        <template slot-scope="scope">
          <el-button size="small" @click="mod(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="del(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 30px 0;">
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

    <el-dialog title="编辑" width="700px" :visible.sync="showMod" style="text-align: start;" center>
      <div style="display: flex;justify-content: center; width: 100%;">
        <el-form ref="modForm" :model="modForm" :rules="rules" label-width="100px" style="width:100%;">
          <el-form-item label="路线名称" prop="name">
            <el-input v-model="modForm.name" class="inputUser"></el-input>
          </el-form-item>
          <el-form-item label="路线票价(元)" >
            <el-input type="number" v-model="modForm.tickets" class="inputUser"></el-input>
          </el-form-item>

          <el-form-item label="路线简介" prop="txt">
            <el-input v-model="modForm.txt" class="inputUser"  type="textarea" autosize></el-input>
          </el-form-item>

        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="modUp()">提交</el-button>
          <el-button @click="showMod=false">关闭</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<style scoped>
.inputUser{
  width: 500px;
}
</style>