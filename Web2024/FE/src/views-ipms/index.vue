<template>
  <div>
    <!-- 상단 검색 -->
    <div class="search-wrap">
      <el-select v-model="option" round>
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <input type="text" placeholder="IP주소 또는 회선번호, SAID를 입력해 주세요 (특수문자 입력 제외)" @keyup.enter="handleClickIpSearch">
      <el-button type="primary" round @click="handleClickIpSearch">
        검색
      </el-button>
    </div>
    <!-- 공지사항 -->
    <Notice ref="notice" :is-dashboard="true" />
    <!-- 탭 -->
    <div class="content-tab-wrap">
      <div v-for="(item, index) in tabList" :key="index" :class="{'active': selectionTab == index }" @click="selectionTab=index">
        {{ item.label }}
      </div>
    </div>
    <template v-for="(tabItem, index) in tabList">
      <div v-if="selectionTab === index" :key="tabItem.label" class="tab-content">
        <component :is="tabItem.component" :is-dashboard="true" />
      </div>
    </template>
  </div>
</template>

<script>
import { Base } from '@/min/Base.min'
import Notice from '@/views-ipms/menus/board/notice.vue'

const routeName = 'IpmsMain'

export default {
  name: routeName,
  components: { Notice },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      activeName: 'first',
      tabList: [
        { label: 'NeOSS 오더', component: () => import('@/views-ipms/menus/ipAllocationMng/neOssOrder') },
        { label: 'i-FOMS Config 감사', component: () => import('@/views-ipms/components/IfomsConfig') },
        { label: '신인증 IP 최적화', component: () => import('@/views-ipms/components/NewCertificationOptimizationIP') },
        { label: '조각 IP 최적화', component: () => import('@/views-ipms/components/PieceIPOptimization') },
      ],
      options: [],
      option: 'CV0001',
      value: '',
      selectionTab: 0
    }
  },
  created () {
    this.options = this.CONSTANTS.ipms.ipInfoOptions
  },
  methods: {
    handleClickIpSearch() {
      this.$store.dispatch('ipms/setToParam', { option: this.option, value: this.value })
      this.$router.push('/ipInfoMng/ipInfoList')
    }
  },
}
</script>
<style lang="scss" scoped>
div.content-tab-wrap{
  background-color:#17232e;
  display: flex;
  border-radius: 25px;
  overflow: hidden;
  >div{
    width:25%;
    text-align: center;
    line-height:50px;
    font-size: 18px;
    border-radius: 25px;
    color:#00839b;
    font-weight: 400;
    cursor:pointer;
    transition: all 0.25s;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    &:hover{
      color:#FFFFFF;
    }
  }
  >div.active{
    background-color:#2d80f7;
    color:#FFFFFF;
    font-weight: 600;
    cursor:pointer;
  }
}

.tab-content{
  margin-top:40px;

  >table{
    border-collapse: collapse;
    width:100%;
    tr{
      >th{
        background-color:rgba(49,51,64,0.5);
        font-size:12px;
        color:#00839b;
      }
      >td{
        font-size:14px;
        color:#00839b;
        font-weight: 500;
        transition: all 0.25s;
        >.btype1,
        >.btype2{
          display: inline-block;
          color:#FFFFFF;
          font-size:13px;
          border:1px solid #ffffff;
          line-height:20px;
          border-radius: 12px;
          padding: 0 10px;
          box-sizing: border-box;
        }
        >.btype1{
          border-color:#0d4f5f;
        }
        >.btype2{
          border-color:#275091;
        }
      }
      >td.acc{
        color:#FFFFFF;
      }
      >td.textcenter{
        text-align: center;
      }
      >th,>td{
        border:1px solid #134d65;
        padding: 10px 5px;
      }
      &:hover>td{
        background-color:rgba(49,51,64,0.4);
      }
      &:nth-child(even){
        background-color:rgba(49,51,64,0.25);
      }
    }
  }
}
</style>
