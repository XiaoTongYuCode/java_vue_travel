<script>
export default {
  name: "NoticePage",
  data() {
    return {
      srcList: [
        require('../assets/img/h1.jpg'),
        require('../assets/img/h2.jpg'),
        require('../assets/img/h3.jpg'),
        require('../assets/img/h4.jpg'),
      ],
      showAdd: false,
      user: '',

      notices: [],

      pageSize: 10,
      pageNum: 1,
      total: 0,
      addForm: {
        title: "",
        txt: ""
      },
      rules: {
        title: [
          {required: true, message: '请输入标题', trigger: 'blur'}
        ],
        txt: [
          {required: true, message: '请输入正文', trigger: 'blur'},
        ],
      }
    }
  },
  methods: {
    getData() {
      //从后端获取数据
      this.$axios.post(this.$httpUrl + "/notice/list", {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: {
          // title: ''
        }
      }).then(res => res.data).then(res => {
        this.notices = res.data;
        this.total = res.total;
      })
    },

    addUp() {
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          this.$axios.post(this.$httpUrl + "/notice/add", this.addForm).then(res => res.data)
              .then(res => {
                if (res.code === 200) {
                  this.$message({
                    message: '已发布',
                    type: 'success'
                  });
                  this.addForm = {
                    title: "",
                    txt: ""
                  }
                  this.showAdd = false;
                  this.getData();
                } else {
                  this.$message.error('错了哦，发布失败');
                }
              })
        } else {
          console.log('error submit!!');
          return false;
        }
      });

    },
    del(id) {
      this.$confirm('此操作将删除此公告！确认删除？', '敏感操作', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.get(this.$httpUrl + "/notice/delete?id=" + id).then(res => res.data)
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
    this.user = JSON.parse(sessionStorage.getItem("User"));
    this.getData();
  }
}
</script>

<template>
  <div style="position: relative;">
    <el-carousel :interval="4000" height="300px" style="margin-top: 10px; margin-bottom: 20px;" type="card">
      <el-carousel-item v-for="(item, index) in srcList" :key="index"
                        style="display: flex; justify-content: center; align-items: center;">
        <el-image
            :preview-src-list="srcList"
            :src="item"
            style="width: 550px; height: 100%;">
        </el-image>
      </el-carousel-item>
    </el-carousel>

    <div style="width: 100%;margin-bottom: 30px; display: flex;flex-direction: column;align-items: center;">
      <el-card style="width: 80%;">
        <div slot="header" class="clearfix">
          <div style="display: flex;justify-content:space-between;align-items: center;">
            <div>公告</div>
            <div>
              <el-button @click="showAdd=true" v-if="user.roleId === 0"
                         type="text">发布公告
              </el-button>
            </div>
          </div>
        </div>
        <el-empty v-if="notices.length===0" :image-size="60"></el-empty>
        <div v-for="(item,index) in notices" :key="index" class="notice">
          <div style="display: flex;justify-content: space-between;align-items: center;">
            <h3 class="title">
              {{ item.title }}
              <i class="el-icon-delete delBtn"
                 v-if="user.roleId === 0" @click="del(item.id)"></i>
            </h3>
            <span class="small">
              {{ item.createTime }}
            </span>
          </div>
          <div style="background-color: rgb(247, 248, 249); padding: 10px; border-radius: 5px;">
            {{ item.txt }}
          </div>
        </div>
        <div style="text-align: center;margin: 20px 0;">
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
      </el-card>
    </div>

    <el-dialog title="发布公告" width="600px" center :visible.sync="showAdd" style="text-align:start;">
      <el-form ref="addForm" :rules="rules" :model="addForm">
        <el-form-item label="标题" prop="title">
          <el-input v-model="addForm.title" class="inputUser"></el-input>
        </el-form-item>

        <el-form-item label="正文" prop="txt">
          <el-input v-model="addForm.txt" class="inputUser" type="textarea"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="addUp()">发布</el-button>
          <el-button @click="showAdd=false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<style scoped>
.title {
  margin: 10px 5px;
}

.notice:hover .delBtn {
  opacity: 1;
}

.small {
  font-size: small;
  color: #8a8a8a;
}

.delBtn {
  font-size: 14px;
  opacity: 0;
  color: darkred;
  cursor: pointer;
  transition: opacity 0.5s;
}
</style>