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
                <td class="view">{{ resultDetailVos.smenuId }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">메뉴명</th>
                <td><el-input v-model="resultDetailVos.smenuNm" size="mini" type="text" class="txt w98" disabled /></td>
              </tr>
              <tr>
                <th class="first" scope="row">메뉴레벨</th>
                <td><el-input v-model="resultDetailVos.nmenuLvlSeq" size="mini" type="text" class="txt w98" disabled /></td>
              </tr>
              <tr>
                <th class="first" scope="row">메뉴계층유형</th>
                <td class="view">
                  <span>
                    <el-radio v-model="resultDetailVos.smenuHierTypeCd" label="UH0001">그룹</el-radio>
                  </span>
                  <span class="ml10">
                    <el-radio v-model="resultDetailVos.smenuHierTypeCd" label="UH0002">메뉴</el-radio>
                  </span>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">상위메뉴</th>
                <td class="view">{{ resultDetailVos.sUpMenuNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">메뉴표시순서</th>
                <td><el-input v-model="resultDetailVos.nmenuIndcOdrg" size="mini" type="text" class="txt w98" disabled /></td>
              </tr>
              <tr>
                <th class="first" scope="row">화면 ID</th>
                <td><el-input v-model="resultDetailVos.sscrnId" type="text" size="mini" class="txt w98" disabled /></td>
              </tr>
              <tr>
                <th class="first" scope="row">화면명</th>
                <td>

                  <el-input v-model="resultDetailVos.sscrnNm" class="txt w-100" size="mini" disabled>
                    <template #suffix>
                      <el-button
                        slot="trigger"
                        size="small"
                        style="font-size: larger; border: none; float: right"
                        icon="el-icon-search"
                        class="font-weight-bolder"
                        @click="fnViewSearchScrnId()"
                      />
                    </template>
                  </el-input>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">화면 URL</th>
                <td><el-input v-model="resultDetailVos.sscrnUrlAdr" size="mini" type="text" class="txt w98" disabled /></td>
              </tr>
              <tr>
                <th class="first" scope="row">메뉴사용여부</th>
                <td class="view">
                  <span>
                    <el-radio v-model="resultDetailVos.smenuUseYn" label="Y">사용</el-radio>
                  </span>
                  <span class="ml10">
                    <el-radio v-model="resultDetailVos.smenuUseYn" label="N">미사용</el-radio>
                  </span>
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">메뉴설명</th>
                <td><textarea v-model="resultDetailVos.scomment" class="w98 h60" rows="6"></textarea></td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="float-right">
          <el-button size="small" @click="fnUpdateTbMenuBas()">
            저장
          </el-button>
        </div>
      </el-col>
      <ModalSearchTbScrnBas ref="ModalSearchTbScrnBas" @selected-value="onSetScrnNm" />
    </div>

  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import { ipmsModelApis, apiRequestModel, apiRequestJson, ipmsJsonApis } from '@/api/ipms'
import ModalSearchTbScrnBas from '@/views-ipms/modal/search/ModalSearchTbScrnBas.vue'
const routeName = 'MenuManagement'

export default {
  name: routeName,
  components: { ModalSearchTbScrnBas },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      resultDetailVos: {
        smenuId: '',
        smenuNm: '',
        nmenuLvlSeq: '',
        smenuHierTypeCd: '',
        sUpMenuNm: '',
        nmenuIndcOdrg: '',
        sscrnId: '',
        sscrnNm: '',
        sscrnUrlAdr: '',
        smenuUseYn: '',
        scomment: '',
      },
      resultListVos: null,
      nodeClickVo: null,
      menuData: [
        {
          title: 'IP 배정관리',
          isFolder: true,
          isLazy: true,
          upperFolder: '/',
          nmenuLvlSeq: 1,
          key: 'M00001',
          children: []
        },
        {
          title: 'IP 할당관리',
          isFolder: true,
          isLazy: true,
          upperFolder: '/',
          nmenuLvlSeq: 1,
          key: 'M00002',
          children: []
        },
        {
          title: 'IP 정보관리',
          isFolder: true,
          isLazy: true,
          upperFolder: '/',
          nmenuLvlSeq: 1,
          key: 'M00003',
          children: []
        },
        {
          title: 'IP 통계관리',
          isFolder: true,
          isLazy: true,
          upperFolder: '/',
          nmenuLvlSeq: 1,
          key: 'M00004',
           children: []
        },
        {
          title: 'DB관리',
          isFolder: true,
          isLazy: true,
          upperFolder: '/',
          nmenuLvlSeq: 1,
          key: 'M00090',
           children: []
        },
        {
          title: '게시판',
          isFolder: true,
          isLazy: true,
          upperFolder: '/',
          nmenuLvlSeq: 1,
          key: 'M00086',
           children: []
        },
        {
          title: '운용정보관리',
          isFolder: true,
          isLazy: true,
          upperFolder: '/',
          nmenuLvlSeq: 1,
          key: 'M00005',
           children: []
        }
      ],
      defaultProps: {
        children: 'children', // 하위 메뉴 필드
        label: 'title', // 메뉴에 표시할 이름
      }
    }
  },
  methods: {
    handleNodeClick(param) {
     console.log(param)
     this.nodeClickVo = param
     this.fnSelectDetailMenuBas(this.nodeClickVo)
     this.fnSelectLisMenuBas(this.nodeClickVo)
    },
    async fnSelectDetailMenuBas(nodeClickVo) { /* 메뉴 상세 */
      const param = {
        smenuId: nodeClickVo.key
      }
      const res = await apiRequestJson(ipmsJsonApis.selectDetailMenuBas, param)
      this.resultDetailVos = res
    },
     async fnSelectLisMenuBas(nodeClickVo) { /* 메뉴 하위 리스트 */
      const param = {
        smenuId: nodeClickVo.key,
        nmenuLvlSeq: nodeClickVo.nmenuLvlSeq
      }
      const res = await apiRequestJson(ipmsJsonApis.selectListMenuBas, param)
      this.resultListVos = res
    },
    fnViewSearchScrnId() { /* 화면 검색 */
      this.$refs.ModalSearchTbScrnBas.open()
    },
    onSetScrnNm(param) {
      this.resultDetailVos.sscrnNm = param.scrnNm
    },
    async fnUpdateTbMenuBas() { /* 메뉴 저장 */
      if (this.resultDetailVos.sscrnId === '' || this.resultDetailVos.sscrnId === null) {
        this.$message('변경할 메뉴를 선택 후 변경이 가능합니다. 메뉴를 선택하세요.')
        return
      }

      if (this.resultDetailVos.smenuHierTypeCd === 'UH0002') {
        if (this.resultDetailVos.sscrnId === '' || this.resultDetailVos.sscrnId === null) {
          this.$message('메뉴 계층 유형이 메뉴일때 화면을 입력해야 합니다. 화면을 선택하세요.')
          return
        }
      } else {
        this.resultDetailVos.sscrnId = 'S00000'
      }

      try {
        const menuBasVo = {
          smenuId: this.resultDetailVos.smenuId,
          sscrnId: this.resultDetailVos.sscrnId,
          smenuHierTypeCd: this.resultDetailVos.smenuHierTypeCd,
          smenuUseYn: this.resultDetailVos.smenuUseYn,
          scomment: this.resultDetailVos.scomment,
          smodify: this.$store.state.user.info.Uid
        }
        const res = await apiRequestJson(ipmsJsonApis.updateTbMenuBasVo, menuBasVo)
        if (res.commonMsg === 'SUCCESS') {
          this.$message('메뉴를 정상적으로 변경 하였습니다.')
        } else {
          this.$message(`${res.commonMsg}`)
        }
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>
<style lang="scss" scoped>
  // .el-tree-node:before, .el-tree-node:after{
  //   border: none !important;
  // }
</style>
