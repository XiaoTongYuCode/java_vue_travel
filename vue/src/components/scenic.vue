<!-- eslint-disable vue/multi-word-component-names -->
<script>
export default {
  name: "scenic",
  data() {
    return {
      httpUrl: this.$httpUrl,
      scenicData: '',
      scenicId: '',
      user: JSON.parse(sessionStorage.getItem("User")),

      buyForm: {
        num: 1,
        date: '',
        money: 0,
      },

      addShow: false,
      buyshow: false,
      addForm: {
        scenicId: '',
        name: '',
        txt: '',
        img: '',
        distance: 0,
        tickets: 0,
      },
      addRules: {
        name: [
          {required: true, message: '请输入酒店名', trigger: 'blur'}
        ],
        txt: [
          {required: true, message: '请输入简介', trigger: 'blur'}
        ],
        distance: [
          {required: true, message: '请输入景区距离', trigger: 'blur'}
        ],
        tickets: [
          {required: true, message: '请输入房源参考价', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    loadData() {
      this.$axios.post(this.$httpUrl + "/scenic/list", {
        pageSize: 0,
        pageNum: 0,
        param: {
          id: this.scenicId
        }
      }).then(res => res.data).then(res => {
        console.log(res.data)
        this.scenicData = res.data;
      })
    },
    goBack() {
      this.$router.go(-1) // Go back one step in history
    },
    showBuyBtn() {
      this.buyForm = {
        num: 1,
        date: '',
        money: this.scenicData.tickets,
      }
      this.buyshow=true;
    },
    buy() {
      this.buyForm.money = this.buyForm.num*this.scenicData.tickets
      if (this.user.money / 100 < this.buyForm.money) {
        this.$message({
          type: 'warning',
          message: '余额不足，请充值'
        });
        return
      }
      this.$axios.post(this.$httpUrl + "/order/add", {
        scenicId: this.scenicData.id,
        scenicName: this.scenicData.name,
        no: this.user.no,
        name: this.user.name,
        tickets: this.buyForm.money,
        num: this.buyForm.num,
        date: this.buyForm.date,
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
          this.buyshow = false;
        }
      })
    },
    post2() {
      this.$prompt('发表您的评论', '评论', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputErrorMessage: '不能发空的喔'
      }).then(({value}) => {
        this.$axios.post(this.$httpUrl + '/comment/add', {
          scenicId: this.scenicData.id,
          no: this.user.no,
          name: this.user.name,
          txt: value
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

    addHotel() {
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
      this.addForm.scenicId = this.scenicData.id;
      this.$axios.post(this.$httpUrl + "/hotel/add", this.addForm).then(res => res.data)
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
        name: '',
        scenicId: '',
        txt: '',
        img: ''
      }
      this.addShow = false;
    },
    //图片上传成功钩子函数
    handleAvatarSuccess(res) {
      this.addForm.img = res.data;
      this.updateScenic();
    },
  },
  created() {
    this.scenicId = this.$route.params.id;
  },
  beforeMount() {
    this.loadData();
  },
}
</script>

<template>
  <div>
    <el-page-header style="height: 25px; align-items: center; margin-left: 10px; margin-top: 5px;" @back="goBack"
                    content="景区列表">
    </el-page-header>
    <div style="width: 100%; display: flex; justify-content: center; margin-bottom: 50px;">
      <el-card class="box-card">
        <div slot="header" style="display: flex;align-items: center;justify-content: space-between;">
          <span style="font-size: larger;">{{ scenicData.name }}</span>
          <div>
            <el-tag effect="plain">{{ scenicData.type }}</el-tag>
          </div>
        </div>
        <el-image
            :src="scenicData.img?scenicData.img:'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg'"
            style="width: 100%;max-height: 500px;">
          <div slot="error" class="image-slot">
            <i class="el-icon-picture-outline"></i>
          </div>
        </el-image>
        <div style="margin-bottom: 50px;margin-top: 20px;">
          介绍：{{ scenicData.details }}
        </div>
        <div style="display: flex; justify-content:flex-end;">
          <el-button icon="el-icon-upload" size="small"
                     @click="addShow=true;" v-if="user.roleId===0">发布房源
          </el-button>
          <el-button icon="el-icon-s-ticket" size="small" style="margin-left: 10px;"
                     type="warning" @click="showBuyBtn()">购票
          </el-button>
          <el-button icon="el-icon-chat-dot-square" size="small" style="margin-left: 10px;"
                     type="primary" @click="post2()">评论
          </el-button>
        </div>
        <div v-for="(item2,i) in scenicData.comment" :key="i">
          <div
              style="width: 100%; min-height: 55px;display: flex; justify-content: space-between; align-items: center;">
            <div style="display: flex; align-items:center;">
              <el-avatar :size="30" shape="square" style="margin: 0 10px;">
                {{ item2.name ? item2.name.charAt(0) : item2.no.charAt(0) }}
              </el-avatar>
              <div>
                <span style="font-size: 12px; font-weight: lighter; color: #6e6e6e"> {{ item2.name }}</span>
                <br><span style="font-size: 16px;"> {{ item2.txt }}</span>
              </div>
            </div>
            <div style="font-weight: 10; font-size: 14px; ">
              {{ new Date(item2.time * 1000).getMonth() + 1 }}/{{ new Date(item2.time * 1000).getDate() }}
              {{ new Date(item2.time * 1000).getHours() }}:{{ new Date(item2.time * 1000).getMinutes() }}
            </div>
          </div>
        </div>
        <el-divider><i class="el-icon-office-building"></i> 附近景点房源推荐</el-divider>
        <el-empty v-if="scenicData.hotel&&scenicData.hotel.length === 0" :image-size="150"
                  description="暂无房源数据"></el-empty>
        <div v-for="(item, index) in scenicData.hotel" :key="index" class="hotel">
          <el-image style="width: 200px;"
                    :src="item.img?item.img: 'https://img0.baidu.com/it/u=3369353589,1949555918&fm=253&fmt=auto&app=138'"></el-image>
          <div style="margin-left: 10px;width: calc(100% - 220px); display: flex;flex-direction: column;">
            <span style="font-size: x-large;">{{ item.name }}</span><br>
            <span>{{ item.txt }}</span>
          </div>
        </div>
      </el-card>
    </div>
    <el-dialog title="上传房源" :visible.sync="addShow" center fullscreen>
      <div class="addDialog">
        <el-form ref="addForm" :model="addForm" :rules="addRules" style="width:80%;" label-position="right">
          <el-form-item label="房源名" prop="name">
            <el-input v-model="addForm.name" class="inputAdd"></el-input>
          </el-form-item>
          <el-form-item label="房源介绍" prop="txt">
            <el-input v-model="addForm.txt" type="textarea" class="inputAdd"></el-input>
          </el-form-item>
          <el-form-item label="参考价(元)" prop="tickets">
            <el-input type="number" v-model="addForm.tickets" class="inputAdd"></el-input>
          </el-form-item>
          <el-form-item label="景区距离(公里)" prop="distance">
            <el-input type="number" v-model="addForm.distance" class="inputAdd"></el-input>
          </el-form-item>
          <el-form-item label="封面">
            <el-upload
                :action="httpUrl+'/upload/'"
                :limit="1"
                list-type="picture-card"
                ref="uploadImg"
                :on-success="handleAvatarSuccess"
                :auto-upload="false">
              <i class="el-icon-plus"></i>
            </el-upload>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="addHotel()">提交</el-button>
          <el-button @click="resetForm()">关闭</el-button>
      </span>
    </el-dialog>
    <el-dialog title="订购" :visible.sync="buyshow" width="500px" center>
      <div class="addDialog">
        <el-form :model="buyForm" label-position="right" label-width="100px">
          <el-form-item label="日期">
            <el-date-picker
                value-format="yyyy-MM-dd"
                v-model="buyForm.date"
                type="date"
                placeholder="选择日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="数量">
            <el-input-number v-model="buyForm.num" :min="1" label="数量"></el-input-number>
          </el-form-item>
          <el-form-item label="价格">
            共计：{{buyForm.num*this.scenicData.tickets}} 元
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="buy()">支付</el-button>
          <el-button @click="buyshow=false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<style scoped>
.box-card {
  width: 90%;
  margin-top: 20px;
}

.hotel {
  display: flex;
  margin-bottom: 10px;
}
</style>