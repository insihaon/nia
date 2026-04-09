<template>
  <div class="errPage-container">
    <!-- <el-button icon="el-icon-arrow-left" class="pan-back-btn" @click="back"> 뒤로 </el-button> -->
    <el-row>
      <el-col :span="16">
        <h1 class="text-jumbo text-ginormous">
          죄송합니다
        </h1>
        <h6>이 페이지로 이동할 수 있는 권한이 없습니다</h6>
        <h6>관리자에게 문의하세요</h6>
        <!-- <ul class="list-unstyled">
          <li>링크:</li>
          <li class="link-type">
            <router-link to="/">
              홈으로
            </router-link>
          </li>
          <li><a href="#" @click.prevent="dialogVisible=true">이미지 보기</a></li>
        </ul> -->
      </el-col>
      <el-col :span="8">
        <img :src="errGif" loop="infinite" width="313" height="428" alt="Girl has dropped her ice cream." />
      </el-col>
    </el-row>
    <!-- <el-dialog :visible.sync="dialogVisible" title="이미지">
      <img :src="ewizardClap" class="pan-img">
    </el-dialog> -->
  </div>
</template>

<script>
import errGif from '@/assets/401_images/401.gif'

export default {
  name: 'Page401',
  data() {
    return {
      errGif: errGif + '?' + +new Date(),
      ewizardClap: 'https://wpimg.wallstcn.com/007ef517-bafd-4066-aae4-6883632d9646',
      dialogVisible: false
    }
  },
  beforeCreate() {
  },
  created() {
  },
  mounted() {
  },
  methods: {
    back() {
      if (this.$route.query.noGoBack) {
        this.$router.push({ path: '/' })
      } else {
        this.$router.go(-1)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .errPage-container {
    width: 800px;
    max-width: 100%;
    margin: 100px auto;
    .pan-back-btn {
      background: #008489;
      color: #fff;
      border: none!important;
    }
    .pan-gif {
      margin: 0 auto;
      display: block;
    }
    .pan-img {
      display: block;
      margin: 0 auto;
      width: 100%;
    }
    .text-jumbo {
      font-size: 60px;
      font-weight: 700;
      color: #484848;
    }
    .list-unstyled {
      font-size: 14px;
      li {
        padding-bottom: 5px;
      }
      a {
        color: #008489;
        text-decoration: none;
        &:hover {
          text-decoration: underline;
        }
      }
    }
  }
</style>
