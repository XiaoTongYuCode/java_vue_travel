<!-- eslint-disable vue/multi-word-component-names -->
<script>
export default {
  name: "userControl",
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
      tableData:[],
      userName: '',
      user: JSON.parse(sessionStorage.getItem('User')),
      pageSize:10,
      pageNum:1,
      total:0,
      showMod:false,
      showAdd:false,

      modForm:{
        no:'',
        name:'',
        password:'',
        roleId:1
      },
      addForm:{
        no:'',
        name:'',
        password:'',
        roleId:1
      },
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        no: [
          { required: true, message: '请输入名称', trigger: 'blur' },
          { validator: checkNo, trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
      }
    }
  },
  methods:{
    getData(){
      //从后端获取数据
      this.$axios.post(this.$httpUrl + "/user/list",{
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          name: this.userName
        }
      }).then(res => res.data).then(res => {
        console.log(res.data)
        this.tableData = res.data;
        this.total = res.total;
      })
    },
    reset(){
      this.userName ='';
      this.getData();
    },
    mod(user){
      this.modForm = user;
      this.showMod = true;
    },
    resetForm(){
      this.modForm = {};
      this.showMod = false;
    },
    resetAddForm(){
      this.addForm = {
        no:'',
        name:'',
        password:'',
        roleId:1
      };
    },
    modUp(){
      this.$refs['modForm'].validate((valid) => {
        if (valid) {
          //向后端提交修改后的user
          this.$axios.post(this.$httpUrl + "/user/up",this.modForm).then(res => res.data)
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
    addUp(){
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          this.$axios.post(this.$httpUrl + "/user/add",this.addForm).then(res => res.data)
              .then(res => {
                if (res.code === 200){
                  this.$message({
                    message: '添加成功',
                    type: 'success'
                  });
                  this.resetAddForm();
                  this.showAdd = false;
                  this.getData();
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
    delUser(no){
      this.$axios.get(this.$httpUrl + "/user/delete?no=" + no).then(res => res.data)
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
      <el-input v-model="userName" placeholder="用户名" style="width: 400px; margin: 0 10px;"></el-input>
      <el-button icon="el-icon-search" @click="getData" type="primary"></el-button>
      <el-button icon="el-icon-refresh" @click="reset" type="info"></el-button>
      <el-button icon="el-icon-upload" @click="showAdd=true;" type="success">添加用户</el-button>
    </div>
    <el-table
        :data="tableData"
        stripe
        style="width: 100%">
      <el-table-column
          prop="no"
          label="账号">
      </el-table-column>

      <el-table-column
          prop="name"
          label="名称">
        <template slot-scope="scope">
          {{scope.row.name ? scope.row.name : "无"}}
        </template>
      </el-table-column>

      <el-table-column
          prop="money"
          label="余额">
        <template slot-scope="scope">
          {{scope.row.money / 100}}元
        </template>
      </el-table-column>


      <el-table-column label="角色" prop="roleId" width="120">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.roleId === 0 ? 'danger' : 'success'"
              disable-transitions>
            {{ scope.row.roleId === 0 ? '管理员' : '用户' }}
          </el-tag>
        </template>
      </el-table-column>


      <el-table-column label="操作" prop="operate">
        <template slot-scope="scope">
          <el-button size="small" type="success" @click="mod(scope.row)" >编辑</el-button>
          <el-popconfirm
              confirm-button-text='好的'
              cancel-button-text='点错了'
              icon="el-icon-info"
              icon-color="red"
              title="确定删除吗？"
              @confirm ="delUser(scope.row.no)"
              style="margin-left: 5px;"
          >
            <el-button slot="reference" type="danger" size="small">删除</el-button>
          </el-popconfirm>
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

    <el-dialog title="编辑用户" width="550px" :visible.sync="showMod" style="text-align: start;" center>
      <div style="display: flex;justify-content: center; width: 100%;">
        <el-form ref="modForm" :model="modForm" :rules="rules" style="width:100%;">
          <el-form-item label="账号">
            <el-input v-model="modForm.no" disabled class="inputUser"></el-input>
          </el-form-item>

          <el-form-item label="密码" prop="password">
            <el-input v-model="modForm.password" class="inputUser" show-password type="password"></el-input>
          </el-form-item>

          <el-form-item label="昵称" prop="name">
            <el-input v-model="modForm.name" class="inputUser"></el-input>
          </el-form-item>


          <el-form-item label="角色" v-if="user.roleId===0">
            <el-radio-group v-model="modForm.roleId">
              <el-radio-button label=0 >管理员</el-radio-button>
              <el-radio-button label=1 >用户</el-radio-button>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="modUp()">提交</el-button>
          <el-button @click="resetForm()">关闭</el-button>
      </span>
    </el-dialog>

    <el-dialog title="添加用户" width="550px" center :visible.sync="showAdd" style="text-align:start;">
      <el-form ref="addForm" :rules="rules" :model="addForm" >
        <el-form-item label="账号" prop="no">
          <el-input v-model="addForm.no" class="inputUser"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="addForm.password" class="inputUser" show-password type="password"></el-input>
        </el-form-item>

        <el-form-item label="昵称" prop="name">
          <el-input v-model="addForm.name" class="inputUser"></el-input>
        </el-form-item>

        <el-form-item label="角色" v-if="user.roleId===0">
          <el-radio-group v-model="addForm.roleId">
            <el-radio-button label=0 >管理员</el-radio-button>
            <el-radio-button label=1 >用户</el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="addUp()">提交</el-button>
          <el-button @click="resetAddForm()">重置</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<style scoped>
.inputUser{
  width: 400px;
}

</style>