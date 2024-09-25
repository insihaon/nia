<template>
  <el-row class="w-100 h-100" :gutter="20">
    <div class="content_result">

      <el-col :span="12">
        <div class="content_result_h">
          <div class="section_tit">
            <h3>메뉴트리</h3>
          </div>
          <el-row style="border-top: 1px solid #cc2929"></el-row>
          <el-tree
            :data="menuData"
            :props="defaultProps"
            node-key="label"
            accordion
            @node-click="handleNodeClick"
          >
          </el-tree>
        </div>
      </el-col>

      <el-col :span="12">
        <div class="content_result_h fr">
          <div class="section_tit">
            <h3>메뉴 상세정보</h3>
          </div>
          <table class="tbl_data entry mt5" summary="조회조건선택">
            <caption>조회조건선택</caption>
            <colgroup>
              <col width="25%" />
              <col width="80%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">메뉴 ID</th>
                <td class="view">{{ menuData.menuId }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">메뉴명</th>
                <td><input v-model="menuData.menuName" type="text" class="txt w98" disabled /></td>
              </tr>
              <tr>
                <th class="first" scope="row">메뉴레벨</th>
                <td><input v-model="menuData.menuLevel" type="text" class="txt w98" disabled /></td>
              </tr>
              <tr>
                <th class="first" scope="row">메뉴계층유형</th>
                <td class="view">
                  <span>
                    <el-radio v-model="menuData.menuType" label="UH0001">그룹</el-radio>
                  </span>
                  <span class="ml10">
                    <el-radio v-model="menuData.menuType" label="UH0002">메뉴</el-radio>
                  </span>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">상위메뉴</th>
                <td class="view">{{ menuData.parentMenuName }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">메뉴표시순서</th>
                <td><input v-model="menuData.menuOrder" type="text" class="txt w98" disabled /></td>
              </tr>
              <tr>
                <th class="first" scope="row">화면 ID</th>
                <td><input v-model="menuData.screenId" type="text" class="txt w98" disabled /></td>
              </tr>
              <tr>
                <th class="first" scope="row">화면명</th>
                <td>
                  <div class="search">
                    <input v-model="menuData.screenName" type="text" class="txt w473" disabled />
                    <a href="javascript:void(0)" @click="searchScreen">
                      <img
                        src="/resources/images/content/btn_data_search_disabled_off.gif"
                        alt="search"
                        class="sc_btn"
                        @mouseover="menuOver"
                        @mouseout="menuOut"
                      />
                    </a>
                  </div>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">화면 URL</th>
                <td><input v-model="menuData.screenUrl" type="text" class="txt w98" disabled /></td>
              </tr>
              <tr>
                <th class="first" scope="row">메뉴사용여부</th>
                <td class="view">
                  <span>
                    <el-radio v-model="menuData.menuUseYn" label="Y">사용</el-radio>
                  </span>
                  <span class="ml10">
                    <el-radio v-model="menuData.menuUseYn" label="N">미사용</el-radio>
                  </span>
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">메뉴설명</th>
                <td><textarea v-model="menuData.menuDesc" class="w98 h60" rows="6"></textarea></td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="float-right">
          <el-button size="small" @click="onSaveMenu()">
            저장
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
              children: [
                { label: 'HOST 유형 관리' },
              ]
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
