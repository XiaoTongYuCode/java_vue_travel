<!-- eslint-disable vue/multi-word-component-names -->
<script>
export default {
  name: "Line",
  data() {
    return {
      httpUrl: this.$httpUrl,
      user: '',

      scenicData: [],
      Name: '',
      addShow: false,

      dialogImageUrl: '',
      dialogVisible: false,

      pageSize: 10,
      pageNum: 1,
      total: 0,

      addForm: {
        no: '',
        name: '',
        txt: '',
        tickets: 0,
        img: ''
      },
      addRules: {
        name: [
          {required: true, message: '请输入攻略名', trigger: 'blur'}
        ],
        txt: [
          {required: true, message: '请完善攻略', trigger: 'blur'}
        ],
      },
    }
  },
  methods: {
    loadData() {
      this.$axios.post(this.$httpUrl + "/line/list", {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: {
          name: this.Name,
        }
      }).then(res => res.data).then(res => {
        this.scenicData = res.data;
        this.total = res.total;
      })
    },
    reset() {
      this.Name = '';
      this.loadData();
    },
    addScenic() {
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          if (this.$refs.uploadImg.uploadFiles.length > 0)
            this.$refs.uploadImg.submit();
          else this.updateScenic();
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    updateScenic() {
      this.addForm.no = this.user.no;
      this.$axios.post(this.$httpUrl + "/line/add", this.addForm).then(res => res.data)
          .then(res => {
            if (res.code === 200) {
              this.$message({
                message: '添加成功',
                type: 'success'
              });
              this.resetForm();
              this.loadData();
            } else {
              this.$message.error('错了哦，添加失败');
            }
          })
    },
    resetForm() {
      this.addForm = {
        no: '',
        name: '',
        txt: '',
        tickets: 0,
        img: ''
      }
      this.addShow = false;
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    //图片上传成功钩子函数
    handleAvatarSuccess(res) {
      this.addForm.img = res.data;
      this.updateScenic();
    },

    buy(row) {
      if (this.user.money / 100 < row.tickets) {
        this.$message({
          type: 'warning',
          message: '余额不足，请充值'
        });
        return
      }
      this.$confirm('确认购买' + row.name + '行程吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.post(this.$httpUrl + "/order/add", {
          scenicId: row.id,
          scenicName: row.name,
          no: this.user.no,
          name: this.user.name,
          tickets: row.tickets,
        }).then(res => res.data).then(res => {
          if (res.code === 200) {
            this.$notify({
              title: '感谢您的购买，' + this.user.name,
              message: '购买成功！',
              iconClass: 'el-icon-s-ticket'
            });
            this.$axios.get(this.$httpUrl + '/user/only?no=' + this.user.no).then(res => res.data)
                .then(res => {
                  if (res.code === 400) {
                    sessionStorage.setItem("User", JSON.stringify(res.data))
                    this.user = res.data;
                  }
                })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消购买'
        });
      });
    },

    delScenic(id) {
      this.$confirm('此操作将删除此攻略 确认删除？', '提示', {
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
                this.loadData();
              } else {
                this.$message.error('删除失败');
              }
            })
      }).catch(() => {
      });
    },

    post2(id) {
      this.$prompt('发表您的评论', '评论', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputErrorMessage: '不能发空的喔'
      }).then(({value}) => {
        this.$axios.post(this.$httpUrl + '/comment/add', {
          scenicId: id,
          no: this.user.no,
          name: this.user.name,
          txt: value,
          type: '攻略'
        }).then(res => res.data)
            .then(res => {
              if (res.code === 200) {
                this.$notify({
                  title: '感谢您的贡献，' + this.user.name,
                  message: '评论成功',
                  type: 'success'
                });
                this.loadData();
              } else {
                this.$notify.error({
                  title: 'oops！发生了一些问题',
                  message: '服务器开小差啦~请稍后再试'
                });
              }
            })
      }).catch(() => {

      });
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
    this.user = JSON.parse(sessionStorage.getItem("User"));
    this.loadData();

  }
}
</script>

<template>
  <div>
    <div style="display: flex;margin: 20px 0;display: flex;justify-content: center;">
      <el-input v-model="Name" placeholder="攻略名" style="width: 300px; margin: 0 10px;"></el-input>
      <el-button icon="el-icon-search" @click="loadData" type="primary"></el-button>
      <el-button icon="el-icon-refresh" @click="reset" type="info"></el-button>
      <el-button icon="el-icon-upload" @click="addShow=true;" type="success" v-if="user.roleId===0">上传攻略</el-button>
    </div>
    <el-empty v-if="scenicData.length===0" description="暂 无 路 线"></el-empty>

    <div style="width: 100%; display: flex;flex-wrap: wrap; align-items: start;">
      <el-card class="box-card" v-for="(item,index) in scenicData" :key="index" :body-style="{'padding':'0'}">
        <div style="position: relative;">
          <el-image :src="item.img" style="width: 100%;height: 100%; margin-bottom: 10px;" class="imgs">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
          <div
              style="position: absolute; bottom: 35px;left: 20px; color: white; font-size: x-large; font-weight: bold;">
            {{ item.name }}
            <i class="el-icon-delete delBtn" @click="delScenic(item.id)"></i>
          </div>
        </div>
        <div style="margin-bottom: 50px;padding: 10px;">
          {{ item.txt }}
        </div>
        <el-collapse style="margin-bottom: 10px;padding: 0 10px;">
          <el-collapse-item
              :title="'评论 '+item.commentList.length+' 条'"
              :disabled="item.commentList.length===0"
              name="1">
            <div v-for="(item2,i) in item.commentList" :key="i">
              <div
                  style="width: 100%; min-height: 55px;display: flex; justify-content: space-between; align-items: center;">
                <div style="display: flex; align-items:center;">
                  <el-avatar :size="30" shape="square" style="margin: 0 10px;">
                    {{ item2.name ? item2.name.charAt(0) : item2.no.charAt(0) }}
                  </el-avatar>
                  <div>
                    <span style="font-size: 12px; font-weight: lighter; color: #6e6e6e"> {{ item2.name }}</span>
                    <br><span style="font-size: 14px;"> {{ item2.txt }}</span>
                  </div>
                </div>
                <div style="font-weight: 10; font-size: 14px; ">
                  {{ new Date(item2.time * 1000).getMonth() + 1 }}/{{ new Date(item2.time * 1000).getDate() }}
                  {{ new Date(item2.time * 1000).getHours() }}:{{ new Date(item2.time * 1000).getMinutes() }}
                </div>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
        <div style="width: 100%;display: flex; justify-content: space-between;margin-bottom: 10px;">
          <div style="margin-left: 10px;display: flex;align-items: center; font-size: 12px;">
            <el-avatar :size="30" style="margin-right: 5px;">{{ item.userName.charAt(0) }}</el-avatar>
            {{ item.userName }}
          </div>
          <el-button style="margin-right: 10px;" size="small"
                     @click="post2(item.id)">评论
          </el-button>
        </div>
      </el-card>
    </div>
    <div style="text-align: center;margin-bottom: 30px;">
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

    <el-dialog title="上传攻略" :visible.sync="addShow" center fullscreen>
      <div class="addDialog">
        <el-form ref="addForm" :model="addForm" :rules="addRules" style="width:80%;" label-position="right">
          <el-form-item label="攻略名称" prop="name">
            <el-input v-model="addForm.name" class="inputAdd"></el-input>
          </el-form-item>
          <el-form-item label="攻略详情" prop="txt">
            <el-input v-model="addForm.txt" type="textarea" class="inputAdd" :rows="10"></el-input>
          </el-form-item>
<!--          <el-form-item label="票价(元)" prop="tickets">-->
<!--            <el-input type="number" v-model="addForm.tickets" class="inputAdd"></el-input>-->
<!--          </el-form-item>-->
          <el-form-item label="封面">
            <el-upload
                :action="httpUrl+'/upload/'"
                :limit="1"
                list-type="picture-card"
                ref="uploadImg"
                :on-preview="handlePictureCardPreview"
                :on-success="handleAvatarSuccess"
                :auto-upload="false">
              <i class="el-icon-plus"></i>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible">
              <img width="100%" :src="dialogImageUrl" alt="">
            </el-dialog>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="addScenic()">提交</el-button>
          <el-button @click="resetForm()">关闭</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<style scoped>
.box-card {
  position: relative;
  width: 45%;
  margin-left: 2%;
  margin-bottom: 30px;
}

.box-card:hover .delBtn {
  opacity: 1;
  transition: .8s;
}

.delBtn {
  font-size: medium;
  cursor: pointer;
  opacity: 0;
  color: #ffffff;
}

.addDialog {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: larger;
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