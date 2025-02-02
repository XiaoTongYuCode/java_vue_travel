<!-- eslint-disable vue/multi-word-component-names -->
<script>
export default {
  name: "hotelControl",
  data() {

    return {
      tableData: [],
      name: '',

      user: JSON.parse(sessionStorage.getItem('User')),
      pageSize: 10,
      pageNum: 1,
      total: 0,

      modShow: false,
      modForm: {
        id: 0,
        name: '',
        distance: 0,
        tickets: 0,
        txt: ''
      },

    }
  },
  methods: {
    getData() {
      const loading = this.$loading({
        lock: true,
        text: '玩命加载中....',
      });
      //从后端获取数据
      this.$axios.post(this.$httpUrl + "/hotel/list", {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: {
          name: this.name,
        }
      }).then(res => res.data).then(res => {
        console.log(res.data)
        this.tableData = res.data;
        this.total = res.total;
        loading.close();
      })
    },
    reset() {
      this.name = '';
      this.getData();
    },
    delUser(id) {
      this.$axios.get(this.$httpUrl + "/hotel/delete?id=" + id).then(res => res.data)
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
    },

    mod(hotel) {
      this.modForm.id = hotel.id;
      this.modForm.name = hotel.name;
      this.modForm.txt = hotel.txt;
      this.modForm.tickets = hotel.tickets;
      this.modForm.distance = hotel.distance;
      this.modShow = true;
    },
    doMod() {
      //向后端提交修改后的user
      this.$axios.post(this.$httpUrl + "/hotel/up", this.modForm).then(res => res.data)
          .then(res => {
            if (res.code === 200) {
              this.$message({
                message: '修改成功',
                type: 'success'
              });
              this.modForm = {
                id: 0,
                name: '',
                distance: 0,
                tickets: 0,
                txt: ''
              };
              this.modShow = false;
              this.getData();
            } else {
              this.$message.error('错了哦，修改失败');
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
      <el-input v-model="name" placeholder="房源名" style="width: 200px; margin: 0 10px;"></el-input>
      <el-button icon="el-icon-search" @click="getData" type="primary"></el-button>
      <el-button icon="el-icon-refresh" @click="reset" type="info"></el-button>
    </div>
    <el-table
        :data="tableData"
        stripe
        style="width: 100%">
      <!--      <el-table-column-->
      <!--          prop="scenic_name"-->
      <!--          label="景区名称"-->
      <!--          width="200">-->
      <!--      </el-table-column>-->
      <el-table-column
          prop="scenicId"
          label="景区ID"
          width="200">
      </el-table-column>

      <el-table-column
          label="封面">
        <template slot-scope="scope">
          <el-image style="width: 200px;"
                    :src="scope.row.img?scope.row.img: 'https://img0.baidu.com/it/u=3369353589,1949555918&fm=253&fmt=auto&app=138'"></el-image>
        </template>
      </el-table-column>

      <el-table-column
          prop="name"
          label="房源名">
      </el-table-column>

      <el-table-column
          prop="tickets"
          label="参考价(元/晚)">
      </el-table-column>

      <el-table-column
          prop="distance"
          label="景区距离(公里)">
      </el-table-column>

      <el-table-column
          prop="txt"
          label="简介">
      </el-table-column>


      <el-table-column label="操作" prop="operate">
        <template slot-scope="scope">
          <el-button type="success" size="small" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm
              confirm-button-text='好的'
              cancel-button-text='点错了'
              icon="el-icon-info"
              icon-color="red"
              title="确定删除吗？"
              @confirm="delUser(scope.row.id)"
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

    <el-dialog title="编辑" width="550px" :visible.sync="modShow" style="text-align: start;"
               :close-on-click-modal="false" center>
      <div style="display: flex;justify-content: center; width: 100%;">
        <el-form ref="modForm" :model="modForm" style="width:100%;">
          <el-form-item label="房源名">
            <el-input v-model="modForm.name" class="inputUser"></el-input>
          </el-form-item>
          <el-form-item label="参考价(元)">
            <el-input type="number" v-model="modForm.tickets" class="inputUser"></el-input>
          </el-form-item>
          <el-form-item label="景区距离(公里)">
            <el-input type="number" v-model="modForm.distance" class="inputUser"></el-input>
          </el-form-item>

          <el-form-item label="房源简介">
            <el-input v-model="modForm.txt" class="inputUser" type="textarea"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="doMod()">提交</el-button>
          <el-button @click="modShow = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<style scoped>

</style>