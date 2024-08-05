<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      id="ipms"
      v-el-drag-dialog
      :visible.sync="visible"
      :width="domElement.maxWidth + `px`"
      :fullscreen.sync="fullscreen"
      :modal-append-to-body="false"
      :append-to-body="true"
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        IP블록병합
        <hr>
      </span>
      <div id="content" class="layer">
        <div class="content_result mt0">
          <h4>할당 병합 정보</h4>
          <table class="tbl_data entry mt5">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">계위</th>
                <td class="view">
                  {{ resultComplexVo.srcIpAssignMstVo.ssvcLineTypeNm }} - {{ resultComplexVo.srcIpAssignMstVo.ssvcGroupNm }} - {{ resultComplexVo.srcIpAssignMstVo.ssvcObjNm }}
                </td>
                <th scope="row">공인/사설</th>
                <td class="view">{{ resultComplexVo.srcIpAssignMstVo.sipCreateTypeNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">IP 버전</th>
                <td class="view">{{ resultComplexVo.srcIpAssignMstVo.sipVersionTypeNm }}</td>
                <th scope="row">IP 주소</th>
                <td class="view">{{ resultComplexVo.srcIpAssignMstVo.pipPrefix }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">시작 IP</th>
                <td class="view">{{ resultComplexVo.srcIpAssignMstVo.sfirstAddr }}</td>
                <th scope="row">끝 IP</th>
                <td class="view">{{ resultComplexVo.srcIpAssignMstVo.slastAddr }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">비고</th>
                <td colspan="3">
                  <textarea id="mrgScomment" v-model="mrgScomment" class="w98" rows="4" name="scomment"></textarea>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="content_result">
          <div class="tit_group">
            <h4 class="mt5">병합 대상 정보</h4>
          </div>
          <table id="baseTable" class="tbl_list mt5" summary="병합 대상 정보">
            <caption>병합 대상 정보</caption>
            <colgroup>
              <col width="10%" />
              <col width="10%" />
              <col width="10%" />
              <col width="10%" />
              <col width="10%" />
              <col width="12%" />
              <col width="16%" />
              <col width="12%" />
              <col width="10%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col">서비스망</th>
                <th scope="col">본부</th>
                <th scope="col">노드</th>
                <th scope="col">공인/사설</th>
                <th scope="col">서비스</th>
                <th scope="col">IP블록</th>
                <th scope="col">배정범위</th>
                <th scope="col" title="단위블록수(IPV4:/24, IPV6:/64)">단위블록수</th>
                <th scope="col">배정상태</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(item, index) in destIpAssignMstVos"
                :key="index"
                :class="{'last': index === destIpAssignMstVos.length - 1, 'subbg': index % 2 !== 0}"
              >
                <td class="first ellipsis">{{ item.ssvcLineTypeNm }}</td>
                <td class="ellipsis" :title="item.ssvcGroupNm">{{ item.ssvcGroupNm }}</td>
                <td class="ellipsis" :title="item.ssvcObjNm">{{ item.ssvcObjNm }}</td>
                <td class="ellipsis" :title="item.sipCreateTypeNm">{{ item.sipCreateTypeNm }}</td>
                <td class="ellipsis" :title="item.sassignTypeNm">{{ item.sassignTypeNm }}</td>
                <td class="ellipsis" :title="item.pipPrefix">{{ item.pipPrefix }}</td>
                <td class="ellipsis" :title="`${item.sfirstAddr} ~ ${item.slastAddr}`">{{ item.sfirstAddr }} ~ {{ item.slastAddr }}</td>
                <td class="ellipsis">
                  {{ formatNumber(item.nclassCnt) }}
                </td>
                <td class="ellipsis" :title="item.sassignLevelNm">{{ item.sassignLevelNm }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" icon="el-icon-menu" @click="fnMergeConfirmBtnClick">병합</el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">
          닫기
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'

const routeName = 'ModalIpAssignMerge'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      resultComplexVo: {
        srcIpAssignMstVo: {
        ssvcLineTypeNm: '',
        ssvcGroupNm: '',
        ssvcObjNm: '',
        sipCreateTypeNm: '',
        sipVersionTypeNm: '',
        pipPrefix: '',
        sfirstAddr: '',
        slastAddr: ''
       },
      },
      mrgScomment: '',
      tbIpAssignMstListVo: [],
      destIpAssignMstVos: [],
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      if (model.tbIpAssignMstListVo) {
        this.tbIpAssignMstListVo = model.tbIpAssignMstListVo
        this.onLoadInfo(model.tbIpAssignMstListVo)
      }
    },
    onClose() {
    },
    async onLoadInfo(tbIpAssignMstListVo) {
      try {
        /*
        // url: '/ipmgmt/assignmgmt/viewInsertMrgAsgnIPMst.model'
        const res = await api(tbIpAssignMstListVo)
        this.resultComplexVo = res.resultComplexVo
        */
      } catch (error) {
        this.error(error)
      }
    },
    formatNumber(value) {
      return new Intl.NumberFormat().format(value)
    },
    fnMergeConfirmBtnClick() {
      // 병합 버튼 클릭 이벤트 처리
      const { pipPrefix, sipVersionTypeCd, ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, sassignLevelCd, sassignTypeCd } = this.resultComplexVo.srcIpAssignMstVo
      const srcIpAssignMstVo = { pipPrefix, sipVersionTypeCd, ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, sassignLevelCd, sassignTypeCd, scomment: this.mrgScomment }
      const tbIpAssignMstComplexVo = { srcIpAssignMstVo, destIpAssignMstVos: [], menuType: 'Aloc' }

      this.destIpAssignMstVos.forEach(row => {
        tbIpAssignMstComplexVo.destIpAssignMstVos.push({ nipAssignMstSeq: row.nipAssignMstSeq })
      })
      /* try {
        url: 'ipmgmt/assignmgmt/insertMrgAsgnIPMst.json'
        const res = await api(tbIpAssignMstComplexVo)
      } catch (error) {
        this.error(error)
      } */

      // // doAjaxSubmit(url, param, "json", "json", "fnInsertMrgAlocIPMstCallback");
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
