<template>
  <el-row class="w-100 h-100">
    <div class="d-flex">
      <div class="searchOptionWrap">
        <div class="popupContentTableTitle">메뉴트리</div>
        <table class="content_resut_h">
          <el-tree
            :data="menuData"
            :props="defaultProps"
            node-key="label"
            accordion
            @node-click="handleNodeClick"
          >
          </el-tree>
        </table>
      </div>
      <div class="searchOptionWrap">
        <div class="popupContentTableTitle">메뉴 상세정보</div>
        <table>
          <tbody>
            <tr class="top">
              <th>메뉴 ID</th>
              <td class="view">{{ resultDetailVos.smenuId }}</td>
            </tr>
            <tr>
              <th>메뉴명</th>
              <td><el-input v-model="resultDetailVos.smenuNm" size="small" type="text" disabled /></td>
            </tr>
            <tr>
              <th>메뉴레벨</th>
              <td><el-input v-model="resultDetailVos.nmenuLvlSeq" size="small" type="text" disabled /></td>
            </tr>
            <tr>
              <th>메뉴계층유형</th>
              <td>
                <el-radio v-model="resultDetailVos.smenuHierTypeCd" label="UH0001">그룹</el-radio>
                <el-radio v-model="resultDetailVos.smenuHierTypeCd" label="UH0002">메뉴</el-radio>
              </td>
            </tr>
            <tr>
              <th>상위메뉴</th>
              <td class="view">{{ resultDetailVos.sUpMenuNm }}</td>
            </tr>
            <tr>
              <th>메뉴표시순서</th>
              <td><el-input v-model="resultDetailVos.nmenuIndcOdrg" size="small" type="text" disabled /></td>
            </tr>
            <tr>
              <th>화면 ID</th>
              <td><el-input v-model="resultDetailVos.sscrnId" type="text" size="small" disabled /></td>
            </tr>
            <tr>
              <th>화면명</th>
              <td>
                <el-input v-model="resultDetailVos.sscrnNm" size="small" disabled>
                  <template #suffix>
                    <el-button
                      slot="trigger"
                      type="primary"
                      size="small"
                      icon="el-icon-search"
                      class="font-weight-bolder"
                      round
                      @click="fnViewSearchScrnId()"
                    />
                  </template>
                </el-input>
              </td>
            </tr>
            <tr>
              <th>화면 URL</th>
              <td><el-input v-model="resultDetailVos.sscrnUrlAdr" size="small" type="text" disabled /></td>
            </tr>
            <tr>
              <th>메뉴사용여부</th>
              <td>
                <el-radio v-model="resultDetailVos.smenuUseYn" label="Y">사용</el-radio>
                <el-radio v-model="resultDetailVos.smenuUseYn" label="N">미사용</el-radio>
              </td>
            </tr>
            <tr>
              <th>메뉴설명</th>
              <td><textarea v-model="resultDetailVos.scomment" rows="6"></textarea></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="float-right">
      <el-button type="primary" icon="el-icon-check" size="small" round @click="fnUpdateTbMenuBas()">
        저장
      </el-button>
    </div>
    <ModalSearchTbScrnBas ref="ModalSearchTbScrnBas" @selected-value="onSetScrnNm" />
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
.popupContentTableTitle {
  padding: 10px;
  border-bottom: solid 1px #00d9ff;
}
</style>
