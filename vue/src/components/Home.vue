<!-- eslint-disable vue/multi-word-component-names -->
<script>

export default {
  name: "Home",
  data(){
    return {
      srcList: [
        require('../assets/img/h1.jpg'),
        require('../assets/img/h2.jpg'),
        require('../assets/img/h3.jpg'),
        require('../assets/img/h4.jpg'),
      ],

      httpUrl:this.$httpUrl,
      user:'',
      scenicData:[],
      scenicName:'',
      scenicType:'',
      addShow:false,

      newType:'',
      TypeModShow:false,
      typeData:[],

      dialogImageUrl: '',
      dialogVisible: false,

      pageSize:10,
      pageNum:1,
      total:0,

      addForm:{
        name:'',
        type:'',
        details:'',
        tickets:0,
        img:''
      },
      addRules: {
        name: [
          { required: true, message: '请输入景点名', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请输入景点类别', trigger: 'blur' }
        ],
        details: [
          { required: true, message: '请输入简介', trigger: 'blur' }
        ],
      },
      options: [{
          value: '城市',
          label: '城市'
        }, {
          value: '山水',
          label: '山水'
        }, {
          value: '乡村',
          label: '乡村'
        }, {
          value: '名胜古迹',
          label: '名胜古迹'
        }, {
          value: '寂静',
          label: '寂静'
        }, {
          value: '繁华',
          label: '繁华'
        }],
    }
  },
  methods:{
    loadData(){
      this.$axios.post(this.$httpUrl + "/scenic/list",{
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          name: this.scenicName,
          type: this.scenicType
        }
      }).then(res => res.data).then(res => {
        console.log(res.data)
        this.scenicData = res.data.scenic;
        this.options = res.data.type.map(item => ({
          value: item.name,
          label: item.name
        }));
        this.typeData = res.data.type;
        this.total = res.total;
      })
    },
    reset(){
      this.scenicName='';
      this.scenicType='';
      this.pageSize=10;
      this.pageNum=1;
      this.loadData();
    },
    goTo(id){
      // window.open('/scenic/' + id);
      this.$router.replace('/scenic/' + id);
    },
    addScenic(){
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          if(this.$refs.uploadImg.uploadFiles.length > 0)
          this.$refs.uploadImg.submit();
          else this.updateScenic();
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    updateScenic(){
      this.$axios.post(this.$httpUrl + "/scenic/add",this.addForm).then(res => res.data)
              .then(res => {
                if (res.code === 200){
                  this.$message({
                    message: '添加成功',
                    type: 'success'
                  });
                  this.resetForm();
                  this.loadData();
                }else {
                  this.$message.error('错了哦，添加失败');
                }
              })
    },
    resetForm(){
      this.addForm={
        name:'',
        type:'',
        details:'',
        tickets:0,
        img:''
      }
      this.addShow=false;
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

    delScenic(id){
      this.$confirm('此操作将删除 景点、其附属酒店、附属评论信息！确认删除？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(( ) => {
        this.$axios.get(this.$httpUrl + "/scenic/delete?id=" + id).then(res => res.data)
          .then(res => {
            if (res.code === 200){
              this.$message({
                message: '删除成功',
                type: 'success'
              });
              this.loadData();
            }else {
              this.$message.error('删除失败');
            }
          })
      }).catch(() => {

      });
    },

    addType(){
      this.$axios.post(this.$httpUrl + "/type/add",{
        name:this.newType
      }).then(res => res.data)
          .then(res => {
            if (res.code === 200){
              this.$message({
                message: '添加成功',
                type: 'success'
              });
              this.newType = '';
              this.loadData();
            }else {
              this.$message.error('添加失败');
            }
          })
    },
    delType(id){
      this.$axios.get(this.$httpUrl + "/type/delete?id=" + id).then(res => res.data)
          .then(res => {
            if (res.code === 200){
              this.$message({
                message: '删除成功',
                type: 'success'
              });
              this.loadData();
            }else {
              this.$message.error('删除失败');
            }
          })
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
  },
}
</script>

<template>
  <div style="position: relative;">
<!--    <el-carousel :interval="4000" height="300px" style="margin-top: 10px; margin-bottom: 20px;" type="card">-->
<!--      <el-carousel-item v-for="(item, index) in srcList" :key="index"-->
<!--                        style="display: flex; justify-content: center; align-items: center;">-->
<!--        <el-image-->
<!--            :preview-src-list="srcList"-->
<!--            :src="item"-->
<!--            style="width: 550px; height: 100%;">-->
<!--        </el-image>-->
<!--      </el-carousel-item>-->
<!--    </el-carousel>-->
    <div style="display: flex;margin: 20px 0;display: flex;justify-content: center;">
      <el-input v-model="scenicName" placeholder="景点名" style="width: 300px; margin: 0 10px;"></el-input>
      <el-input v-model="scenicType" placeholder="类型" style="margin-right: 10px;width: 150px;"></el-input>
      <el-button icon="el-icon-search" @click="loadData" type="primary"></el-button>
      <el-button icon="el-icon-refresh" @click="reset" type="info"></el-button>
      <el-button icon="el-icon-upload" @click="addShow=true;" type="success" v-if="user.roleId===0">上传景点</el-button>
      <el-button @click="TypeModShow=true;" v-if="user.roleId===0">编辑景点类型</el-button>
    </div>
    <div style="width: 100%; display: flex; flex-direction: column; align-items: center;">
      <el-empty v-if="scenicData.length===0" description="暂 无 景 区"></el-empty>
      <el-card class="box-card" v-for="(item,index) in scenicData" :key="index">
        <div slot="header" style="display: flex;align-items: center;justify-content: space-between;">
          <div style="display: flex; align-items: center;">
            <el-tag effect="plain">{{item.type}}</el-tag>
            <span style="font-size: larger;margin-left: 5px;">{{item.name}}</span>
          </div>
          <div style="display: flex;">
            <div v-if="user.roleId===0">
              <i class="el-icon-delete delBtn" @click="delScenic(item.id)"></i>
            </div>
          </div>
        </div>
        <div style="margin-bottom: 10px;display: flex;">
          <el-image :src="item.img?item.img:'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg'" style="width: 30%;margin-right: 20px;">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
          <div style="width: 70%;">
            简介：{{ item.details }}
          </div>
        </div>
        <el-button style="float: right; margin: 10px 10px;width: 100px;" icon="el-icon-right" @click="goTo(item.id)">查看</el-button>
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


    <el-dialog title="上传景点" :visible.sync="addShow" center fullscreen>
      <div class="addDialog">
        <el-form ref="addForm" :model="addForm" :rules="addRules" style="width:80%;" label-position="right">
          <el-form-item label="景点类型" prop="type">
            <el-select v-model="addForm.type" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="景点名称" prop="name">
            <el-input v-model="addForm.name" class="inputAdd"></el-input>
          </el-form-item>
          <el-form-item label="景点简介" prop="details">
            <el-input v-model="addForm.details" type="textarea" class="inputAdd"></el-input>
          </el-form-item>
          <el-form-item label="景点门票(元)" prop="tickets">
            <el-input type="number" v-model="addForm.tickets" class="inputAdd"></el-input>
          </el-form-item>
          <el-form-item label="景点封面">
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

    <el-dialog title="景点类型编辑" :visible.sync="TypeModShow" center>
      <div style="display: flex;justify-content: center;margin-bottom: 10px;">
        <el-input v-model="newType" style="width: 50%;margin-right: 10px;" placeholder="新增类型"></el-input>
        <el-button type="success" size="small" @click="addType">新增</el-button>
      </div>
      <div style="width: 100%;display: flex;flex-wrap: wrap;">

        <div v-for="(item,index) in typeData" :key="index" class="typeItem">
        {{item.name}}
        <el-button type="danger" size="small" icon="el-icon-delete" @click="delType(item.id)"></el-button>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
          <el-button @click="TypeModShow=false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<style scoped>
.box-card{
  width: 80%;
  margin-bottom: 30px;
}
.box-card:hover .delBtn {
  opacity: 1;
  transition: .5s;
}

.delBtn {
  cursor: pointer;
  opacity: 0;
  color: darkred;
}
.addDialog{
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: larger;
}
.typeItem{
  width: 29%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  margin-right: 10px;
  margin-top: 10px;
  padding: 10px;
  font-size: large;
}
</style>