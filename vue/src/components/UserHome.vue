<!-- eslint-disable vue/multi-word-component-names -->
<script xmlns:https="http://www.w3.org/1999/xhtml">

export default {
  name: "userHome",
  components: {},
  data() {
    return {
      imageUrl: require('../assets/img/Background.jpg'),
      centerDialogVisible: false,
      addMoneyShow: false,
      addMoneyNum: 1,
      user: JSON.parse(sessionStorage.getItem('User')),
      Orders: [],
      loading: false,
      form: {
        no: '',
        name: '',
        password: ''
      },
      rules: {
        name: [
          {required: true, message: '请输入名称', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    loadPost() {
      this.$axios.get(this.$httpUrl + '/user/only?no=' + this.user.no).then(res => res.data)
          .then(res => {
            if (res.code === 400) {
              sessionStorage.setItem("User", JSON.stringify(res.data))
              this.user = res.data;
            }
          })
    },
    loadOrder() {
      this.$axios.post(this.$httpUrl + '/order/list', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: {
          no: this.user.no
        }
      }).then(res => res.data)
          .then(res => {
            if (res.code === 200) {
              this.Orders = res.data;
            }
          })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    mod() {
      this.centerDialogVisible = true;
      this.$nextTick(() => {
        this.form.no = this.user.no;
        this.form.name = this.user.name;
        this.form.password = this.user.password;
      })
    },
    save() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.loading = true;
          this.$axios.post(this.$httpUrl + '/user/up', this.form).then(res => res.data)
              .then(res => {
                if (res.code === 200) {
                  this.centerDialogVisible = false;
                  this.$message({
                    showClose: true,
                    message: '修改成功',
                    type: 'success',
                  });
                  this.loadPost();
                  this.loading = false;
                } else {
                  this.$message({
                    showClose: true,
                    message: '服务器开小差啦~请稍后重试',
                    type: 'error'
                  });
                }
              })
          this.resetForm('form');
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    addMoney() {
      const orderNo = Date.now().toString(36) + Math.random().toString(36).substr(2, 10); // 生成64位数字符串作为订单编号
      this.$axios.post(this.$httpUrl + '/api/pay/create', {
        outTradeNo: String(orderNo),
        totalAmount: (this.addMoneyNum).toFixed(2),
        subject: String(this.user.no)+"充值"
      }).then(response => {
        // 打开一个新窗口
        const payWindow = window.open('', '_blank');
        if (!payWindow) {
          alert('请允许弹出窗口以完成支付');
          return;
        }

        // 将支付表单HTML写入新窗口
        payWindow.document.write(response.data);
        payWindow.document.close();

        // 自动提交表单
        const form = payWindow.document.querySelector('form');
        if (form) {
          form.submit();
        } else {
          alert('支付表单加载失败，请重试');
          payWindow.close();
        }
      }).catch(error => {
        console.error('支付请求失败：', error);
        alert('支付请求失败，请重试');
      });

      // this.$axios.post(this.$httpUrl + '/user/addMoney', {
      //   no: this.user.no,
      //   num: this.addMoneyNum * 100
      // }).then(res => res.data)
      //     .then(res => {
      //       if (res.code === 200) {
      //         this.addMoneyShow = false;
      //         this.loadPost();
      //         this.loading = false;
      //       } else {
      //         this.$message({
      //           showClose: true,
      //           message: '服务器开小差啦~请稍后重试',
      //           type: 'error'
      //         });
      //       }
      //     })
      // this.addMoneyNum = 1;
    },
    // 确认关闭
    handleClose(done) {
      this.$confirm('确认关闭？您的信息不会保存')
          .then(() => {
            done();
          })
          .catch(() => {
          });
    },
  },
  created() {
    this.loadOrder();
    this.loadPost();
  }
}
</script>
<template>
  <div>
    <div
        :style="{ 'background-image': 'url(' + imageUrl + ')' }"
        style="display: flex; justify-content: center; align-items: center; flex-direction: column;  padding-bottom: 60px;">
      <div style="margin-top: 40px; margin-bottom: 20px">
        <el-avatar :size="150" style="font-size: 3em;background-color: #cc9c7a;">
          {{ user.name ? user.name.charAt(0) : user.no.charAt(0) }}
        </el-avatar>
      </div>
      <span style="font-size: 30px; font-weight: lighter">{{ this.user.name }}</span>
    </div>

    <div style="display: flex; justify-content:  center; align-items: start; flex-wrap: wrap;margin-top: 20px;">
      <div style="width: 45%; margin-right: 20px;">
        <el-descriptions :column="2" direction="vertical" class="userContent"
                         title="个人信息">
          <el-descriptions-item label="账号">{{ this.user.no }}</el-descriptions-item>
          <el-descriptions-item label="用户名">{{ this.user.name }}</el-descriptions-item>
          <el-descriptions-item label="角色">
            {{ this.user.roleId === 0 ? "管理" : '用户' }}
          </el-descriptions-item>
          <el-descriptions-item label="账户余额" content-style="font-size: large;" label-style="">
            {{ this.user.money / 100 }} 元
          </el-descriptions-item>
        </el-descriptions>
        <el-button icon="el-icon-edit" size="mini" style="position: relative; top:-50px;left: 20px;" type="primary"
                   @click="mod()">修改信息
        </el-button>
        <el-button icon="el-icon-s-finance" size="mini" style="position: relative; top:-50px;left: 20px;" type="success"
                   @click="addMoneyShow = true">余 额 充 值
        </el-button>
      </div>
      <el-card style="width: 45%;">
        <div slot="header" class="clearfix">
          <span>我的订单</span>
        </div>
        <div style="height: 300px;overflow-y: auto;">
          <el-empty :image-size="120" description="暂无订单" v-if="Orders.length===0"></el-empty>
          <div v-for="(item,index) in Orders" :key="index" class="OrderItem">
            <div>
              《{{ item.scenicName }}》票价 {{ item.tickets }} 元
            </div>
            <el-tag effect="plain" style="margin-right: 10px;"
                    :type="item.checks ==='y' ? 'success' : (item.checks ==='n' ? 'warning' : 'info')">
              <i :class="item.checks ==='y' ? '' : (item.checks ==='n' ? '' : 'el-icon-loading')"> </i>
              {{ item.checks === 'y' ? '已完成' : (item.checks === 'n' ? '已退单' : '待审核') }}
            </el-tag>
          </div>
        </div>
      </el-card>

    </div>
    <el-dialog
        :before-close="handleClose"
        :visible.sync="centerDialogVisible"
        center
        title="用户"
        width="30%">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" status-icon>
        <el-form-item label="名称" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-col :span="20">
            <el-input v-model="form.password" show-password></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="save">{{ loading ? '提交中 ...' : '确 定' }}</el-button>
      </span>
    </el-dialog>
    <el-dialog
        :before-close="handleClose"
        :visible.sync="addMoneyShow"
        center
        title="余额充值"
        width="30%">
      <div style="text-align: center;margin: 50px 0;">
        充值
        <el-input-number size="mini" v-model="addMoneyNum" :precision="2" :min="1" label="金额"></el-input-number>
        元
      </div>
      <span slot="footer" class="dialog-footer" style="display: flex;justify-content:center;">
        <el-button @click="addMoneyShow = false">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="addMoney" style="background-color: #409EFF; border-color: #409EFF;">
          <div style="display: flex; align-items: center;">
            <svg t="1725806490208" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                 p-id="2532" width="20" height="20"><path
                d="M512 1024C229.23 1024 0 794.77 0 512S229.23 0 512 0s512 229.23 512 512-229.23 512-512 512z m0-960C264.96 64 64 264.96 64 512s200.96 448 448 448 448-200.96 448-448S759.04 64 512 64z m-64 704h128v-64H448v64z m0-192h128v-192H448v192z"
                fill="#ffffff" p-id="2533"></path></svg>
            支付宝支付
          </div>
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>
<style scoped>
* {
  user-select: none;
}

/deep/ .el-upload {
  width: 100%;
}

/deep/ .el-upload .el-upload-dragger {
  width: 100%;
  height: 240px;
}

.postTitle :hover {
  color: black;
}

.userContent {
  width: 100%;
  height: 400px;
  padding: 20px 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
}

.OrderItem {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 35px;
  margin-bottom: 5px;
}
</style>