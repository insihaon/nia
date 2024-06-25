<template>
  <el-row class="w-100 h-100" :gutter="20">
    <div class="content_result">
      <el-col :span="12">
        <div class="section_tit">
          <h4>메뉴트리</h4>
        </div>
        <el-row style="border-top: 1px solid #cc2929"></el-row>
        <el-tree
          :data="menuData"
          :props="defaultProps"
          node-key="label"
          accordion
          @node-click="handleNodeClick"
        ></el-tree>
      </el-col>
      <el-col :span="12">
        <div class="section_tit">
          <h4>메뉴트리</h4>
        </div>
        <el-row style="border-top: 1px solid #cc2929"></el-row>
        <table class="form">
          <tr>
            <th class="disable">메뉴ID</th>
            <td>
              <el-input v-model="menu_id" />
            </td>
          </tr>
          <tr>
            <th>메뉴명</th>
            <td class="disable">
              <el-input v-model="menu_nm" />
            </td>
          </tr>
          <tr>
            <th>메뉴레벨</th>
            <td class="disable">
              <el-input v-model="menu_lvl" />
            </td>
          </tr>
          <tr>
            <th>메뉴계층유형</th>
            <td>
              <el-radio-group v-model="menu_layer_type" size="mini" class="d-flex">
                <el-radio label="group">그룹</el-radio>
                <el-radio label="menu">메뉴</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <th>상위메뉴</th>
            <td class="disable">
              <el-input v-model="top_menu" />
            </td>
          </tr>
          <tr>
            <th>메뉴표시순서</th>
            <td class="disable">
              <el-input v-model="menu_order" />
            </td>
          </tr>
          <tr>
            <th>화면ID</th>
            <td class="disable">
              <el-input v-model="screen_id" />
            </td>
          </tr>
          <tr>
            <th>화면명</th>
            <td class="disable">
              <el-input v-model="screen_nm" />
            </td>
          </tr>
          <tr>
            <th>화면ULR</th>
            <td class="disable">
              <el-input v-model="screen_url" />
            </td>
          </tr>
          <tr>
            <th>메뉴사용여부</th>
            <td>
              <el-radio-group v-model="isScreen" size="mini" class="d-flex">
                <el-radio label="Y">사용</el-radio>
                <el-radio label="N">미사용</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <th>메뉴설명</th>
            <td class="disable">
              <el-input
                v-model="menu_desc"
                type="textarea"
                :rows="3"
              >
              </el-input>
            </td>
          </tr>
        </table>
        <div class="my-1">
          <el-button class="float-right px-0">
            <img src="@/assets/images/ipms/content/btn_save02_off.gif" alt="저장" @click="onSaveMenu()" />
          </el-button>
        </div>

      </el-col>
    </div>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
const routeName = 'MenuManagement'

export default {
  name: routeName,
  components: { },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      menu_id: '',
      menu_nm: '',
      menu_lvl: '',
      menu_layer_type: 'group',
      top_menu: '',
      menu_order: '',
      screen_id: '',
      screen_nm: '',
      screen_url: '',
      isScreen: 'Y',
      menu_desc: '',
     menuData: [
        {
          label: 'IP 배정관리',
          children: [
            { label: 'IP 블록관리' },
            { label: 'IP 블록관리(사설)' },
            { label: 'IP 배정' },
            { label: 'IP 선배정' },
            { label: 'IP 미배정 현황' }
          ]
        },
        {
          label: 'IP 할당관리',
          children: [
            { label: 'IP 할당' },
            { label: 'NeOSS오더' },
            { label: 'IP 선번장' },
            { label: 'VPN IP현황' },
            {
              label: '타 서비스 관리',
              children: []
            }
          ]
        },
        {
          label: 'IP 정보관리',
          children: []
        },
        {
          label: 'IP 통계관리',
          children: []
        },
        {
          label: 'DB관리',
          children: []
        },
        {
          label: '게시판',
          children: []
        },
        {
          label: '운용정보관리',
          children: []
        }
      ],
           defaultProps: {
          children: 'children',
          label: 'label'
        }
    }
  },
  methods: {
    onClickSearch() {
      /*
      const param = {
         channelValue: this.channelValue,
         inquiryValue: this.inquiryValue,
         inquiryOptions2: this.inquiryOptions2,
        }
      const res = await api(param)
      */
    },
    handleNodeClick(data) {
      console.log(data)
      this.menu_nm = data.label
    },
    onSaveMenu() {

    }
  }
}
</script>
<style lang="scss" scoped>
  .el-tree-node:before, .el-tree-node:after{
    border: none !important;
  }
</style>
