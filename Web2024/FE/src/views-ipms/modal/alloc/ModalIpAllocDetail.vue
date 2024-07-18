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
        IP할당 상세정보
        <hr>
      </span>
      <div id="content" class="layer w-100 h-100">
        <div class="content_result mt0">
          <h4>IP 블록 정보</h4>
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
                  <template v-if="ipAllocOperMstVos">
                    {{ ipAllocOperMstVos.ssvcLineTypeNm }} - {{ ipAllocOperMstVos.ssvcGroupNm }} - {{ ipAllocOperMstVos.ssvcObjNm }}
                  </template>
                </td>
                <th scope="row">공인/사설</th>
                <td class="view">
                  <template v-if="ipAllocOperMstVos">
                    {{ ipAllocOperMstVos.sipCreateTypeNm }}
                  </template>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">할당상태</th>
                <td class="view">
                  <template v-if="ipAllocOperMstVos">
                    {{ ipAllocOperMstVos.sassignLevelNm }}
                  </template>
                </td>
                <th scope="row">서비스</th>
                <td class="view">
                  <template v-if="ipAllocOperMstVos">
                    {{ ipAllocOperMstVos.sassignTypeNm }}
                  </template>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">IP 버전</th>
                <td class="view">
                  <template v-if="ipAllocOperMstVos">
                    {{ ipAllocOperMstVos.sipVersionTypeNm }}
                  </template>
                </td>
                <th scope="row">IP 주소</th>
                <td class="view">
                  <template v-if="ipAllocOperMstVos">
                    {{ ipAllocOperMstVos.pipPrefix }}
                  </template>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">다중회선</th>
                <td class="view">
                  <template v-if="ipAllocOperMstVos">
                    {{ ipAllocOperMstVos.nipAllocMstCnt }}
                  </template>
                </td>
                <th scope="row">감사상태</th>
                <td class="view">
                  <template v-if="ipAllocOperMstVos">
                    {{ ipAllocOperMstVos.svalidCheck }}
                  </template>
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">비고</th>
                <td colspan="3">
                  <textarea v-model="ipAllocOperMstVos.scomment" class="w98" rows="3" maxlength="4000"></textarea>
                </td>
              </tr>
            </tbody>
          </table>
          <div class="btn_area mt5">
            <!-- <a href="#none" id="updateScommentBtn" @click="fnScommentUpdateClick">
              <input
                type="image"
                :src="btnModifyOffSrc"
                alt="수정"
                @mouseover="menuOver"
                @mouseout="menuOut"
              />
            </a> -->
          </div>
        </div>

        <div class="content_result">
          <h4>IP 블록 세부 정보</h4>
          <table class="tbl_data mt5">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">시작 IP</th>
                <td>
                  <template v-if="ipAllocOperMstVos">
                    {{ ipAllocOperMstVos.sfirstAddr }}
                  </template>
                </td>
                <th scope="row">끝 IP</th>
                <td>
                  <template v-if="ipAllocOperMstVos">
                    {{ ipAllocOperMstVos.slastAddr }}
                  </template>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">총 IP 수</th>
                <td>
                  <template v-if="ipAllocOperMstVos">
                    {{ formatNumber(ipAllocOperMstVos.ncnt) }}
                  </template>
                </td>
                <th scope="row">단위블록수</th>
                <td>
                  <template v-if="ipAllocOperMstVos">
                    {{ formatNumber(ipAllocOperMstVos.nclassCnt, 0, 10) }}
                  </template>
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">사용 IP 수</th>
                <td>
                  <template v-if="ipAllocOperMstVos">
                    {{ formatNumber(ipAllocOperMstVos.nuseIpCnt) }}
                  </template>
                </td>
                <th scope="row">가용 IP 수</th>
                <td>
                  <template v-if="ipAllocOperMstVos">
                    {{ formatNumber(ipAllocOperMstVos.nfreeIpCnt) }}
                  </template>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="content_result">
          <h4>IP 할당 정보</h4>
          <table class="tbl_list mt5" summary="IP 할당 정보">
            <caption>IP 할당 정보</caption>
            <thead>
              <tr>
                <th class="first" scope="col">수용국</th>
                <th scope="col">장비명</th>
                <th scope="col">대표 IP</th>
                <th scope="col">I/F명</th>
                <th scope="col">수용회선명</th>
                <th scope="col">전용번호</th>
                <th scope="col">해지</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-if="ipAllocOperMstVos.sassignLevelCd === 'IA0004' || ipAllocOperMstVos.nipAllocMstSeq === -1"
                class="subbg last"
              >
                <td class="first" colspan="8">조회된 결과 목록이 존재하지 않습니다.</td>
              </tr>
              <template v-else>
                <tr
                  v-for="(item, index) in ipAllocOperMstVos"
                  :key="item.nipAllocMstSeq"
                  style="cursor: pointer;"
                  :class="{'subbg': index % 2 !== 0, 'last': index === ipAllocOperMstVos.length - 1}"
                  @click="fnViewSubDetailAlcIPMst(item.nipAllocMstSeq)"
                >
                  <td class="first ellipsis" :title="item.sofficename">
                    {{ item.sofficename }}
                  </td>
                  <td class="ellipsis" :title="item.ssubscnealias">
                    {{ item.ssubscnealias }}
                  </td>
                  <td class="ellipsis" :title="item.ssubscmstip">
                    {{ item.ssubscmstip }}
                  </td>
                  <td class="ellipsis" :title="item.ssubsclgipportdescription">
                    {{ item.ssubsclgipportdescription }}
                  </td>
                  <td class="ellipsis" :title="item.sconnAlias">
                    {{ item.sconnAlias }}
                  </td>
                  <td class="ellipsis" :title="item.sllnum">
                    {{ item.sllnum }}
                  </td>
                  <td class="btn_text">
                    <el-button @click="fnDeleteAlcIPMstClick(item.nipAllocMstSeq, item.nwhoisSeq)">
                      해지
                    </el-button>
                  </td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="ipAllocOperMstVos" size="mini">IP블럭 중복체크</el-button>
        <template v-if="ipAllocOperMstVos.sassignLevelCd === 'IA0004'">
          <el-button size="mini" icon="el-icon-menu">할당</el-button>
          <el-button size="mini" icon="el-icon-menu">반납</el-button>
        </template>
        <el-button size="mini" class="el-icon-close" @click="close()">
          닫기
        </el-button>
      </div>
      <ModalIpAllocCircuitDetail ref="ModalIpAllocCircuitDetail" />
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import ModalIpAllocCircuitDetail from '@/views-ipms/modal/alloc/ModalIpAllocCircuitDetail.vue'

const routeName = 'ModalIpAllocDetail'

export default {
  name: routeName,
  components: { ModalIpAllocCircuitDetail },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      ipAllocOperMstVos: {/*
        nclassCnt: '128.0000000000',
        ncnt: '32768',
        nfreeIpCnt: '32768',
        nipAllocMstCnt: '1',
        nipAllocMstSeq: '865865',
        nuseIpCnt: '0',
        nwhoisSeq: '657555',
        pipPrefix: '1.100.0.0/17',
        sassignLevelCd: 'IA0006',
        sassignTypeNm: 'POOL-M2M',
        scomment: null,
        sconnAlias: null,
        sfirstAddr: '1.100.0.0',
        sipCreateTypeNm: '공인',
        sipVersionTypeNm: 'IPv4',
        slastAddr: '1.100.127.255',
        sllnum: '',
        ssubsclgipportdescription: null,
        ssubscmstip: '110.70.230.1',
        ssubscnealias: 'GR_C-PGW#20',
        ssvcLineTypeNm: 'MOBILE',
        svalidCheck: '정상',
        ssvcObjNm: 'DATA망(구로)',
        ssvcGroupNm: 'DATA망',
        sassignLevelNm: '할당',
       */}
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      if (model.row) {
        this.ipAllocOperMstVos = model.row
      }
    },
    onClose() {
    },
    formatNumber(value, minFractionDigits = 0, maxFractionDigits = 0) {
      return new Intl.NumberFormat('en-US', {
        minimumFractionDigits: minFractionDigits,
        maximumFractionDigits: maxFractionDigits
      }).format(value)
    },
    menuOver(event) {
      event.target.src = event.target.src.replace('_off', '_on')
    },
    menuOut(event) {
      event.target.src = event.target.src.replace('_on', '_off')
    },
    fnScommentUpdateClick() {
      // Implement your update logic here
    },
    fnViewSubDetailAlcIPMst(nipAllocMstSeq) {
      console.log(nipAllocMstSeq)
      this.$refs.ModalIpAllocCircuitDetail.open({ nipAllocMstSeq })
      // Implement your view detail logic here
    },
    fnDeleteAlcIPMstClick(nipAllocMstSeq, nwhoisSeq) {
      // Implement your delete logic here
    },
    fnViewCheckTacsIpBlock() {
      // Implement your check IP block logic here
    },
    fnAlocCallBtnClick() {
      // Implement your allocation call button logic here
    },
    fnRetUpdateConfirmClick() {
      // Implement your return update confirm logic here
    },
    fnCloseBtnClick() {
      // Implement your close button logic here
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
