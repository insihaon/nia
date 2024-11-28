<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="운용정보 상세 정보"
    :visible.sync="visible"
    :width="domElement.maxWidth + `px`"
    :fullscreen.sync="fullscreen"
    :modal-append-to-body="true"
    :append-to-body="true"
    :close-on-click-modal="closeOnClickModal"
    :loading="loading"
    class="ipms-dialog"
    :class="{ [name]: true }"
  >
    <!-- 공통항목 Section -->
    <div class="popupContentTable">
      <div class="popupContentTableTitle">공통항목</div>
      <table>
        <colgroup>
          <col width="15%" />
          <col width="20%" />
          <col width="10%" />
          <col width="20%" />
          <col width="10%" />
          <col width="25%" />
        </colgroup>
        <tbody>
          <tr>
            <th>IP</th>
            <td>{{ resultVo.pipHostInet }}</td>
            <th>용도</th>
            <td>{{ resultVo.scomment }}</td>
            <th>IP 버전</th>
            <td>{{ resultVo.sipVersionTypeNm }}</td>
            <template v-if="fnType === 'update'">
              <th>대표여부</th>
              <td>
                <el-select v-model="resultVo.sprorityYn" style="width: 70px">
                  <el-option value="Y" label="Y" />
                  <el-option value="N" label="N" />
                </el-select>
              </td>
            </template>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 세부항목 Section -->
    <div class="popupContentTable">
      <div class="popupContentTableTitle">세부항목</div>
      <table>
        <colgroup>
          <col width="15%" />
          <col width="20%" />
          <col width="10%" />
          <col width="20%" />
          <col width="10%" />
          <col width="25%" />
        </colgroup>
        <thead>
          <tr>
            <th colspan="2">기본 정보</th>
            <th colspan="2">시설 정보</th>
            <th colspan="2">회선 정보</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <th>수용국</th>
            <td v-if="fnType === 'detail'">{{ resultVo.srssofficesNm }}</td>
            <td v-else>
              <!-- {{ resultVo.srssofficesNm }} -->
              <el-input
                v-model="resultVo.srssofficesNm"
                size="mini"
                readonly
              >
                <template #suffix>
                  <el-button
                    size="mini"
                    icon="el-icon-search"
                    class="font-weight-bolder"
                    style="font-size: larger;border: none"
                    @click="fnSearchOfficeList()"
                  />
                </template>
              </el-input>
            </td>
            <th>I/F명</th>
            <td>{{ resultVo.sipIfNm }}</td>
            <th>서비스</th>
            <td>{{ resultVo.sassignTypeNm }}</td>
          </tr>
          <tr>
            <th>HOST유형</th>
            <td>{{ resultVo.sipHostTypeNm }}</td>
            <th>장비명</th>
            <td v-if="fnType === 'detail'">{{ resultVo.sipHostNm }}</td>
            <td v-else>
              <el-input v-model="resultVo.sipHostNm" />
            </td>
            <th>상품명</th>
            <td>{{ resultVo.sexSvcNm }}</td>
          </tr>
          <tr>
            <th>외부연동</th>
            <td>{{ resultVo.sexLinkUseTypeNm }}</td>
            <th>모델명</th>
            <td v-if="fnType === 'detail'">{{ resultVo.smodelname }}</td>
            <td v-else>
              <el-input v-model="resultVo.smodelname" />
            </td>
            <th>SAID</th>
            <td>{{ resultVo.ssaid }}</td>
          </tr>
          <tr>
            <th></th>
            <td></td>
            <th></th>
            <td></td>
            <th>전용회선</th>
            <td>{{ resultVo.sllnum }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" class="el-icon-edit" round @click="fnUpdateHostIpMst">{{ fnType === 'update'? '등록' : '수정' }}</el-button>
      <el-button v-if="fnType === 'detail'" type="primary" size="small" icon="el-icon-delete" round @click="fnDeleteHostIpMst">삭제</el-button>
      <el-button type="primary" size="small" class="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
    <ModalOrgSearch ref="ModalOrgSearch" @selected-value="setSelectedRow" />
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils'
import ModalOrgSearch from '@/views-ipms/modal/search/ModalOrgSearch.vue'

import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalIpHostMstDetail'

export default {
  name: routeName,
  components: { ModalOrgSearch },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      loading: false,
      fnType: 'detail',
      defaultResultVo: {
        nipHostMstSeq: '',
        pipHostInet: '', // Initialize with default or fetched data
        scomment: '',
        sipVersionTypeNm: '',
        srssofficesNm: '',
        srssofficescode: '',
        sipIfNm: '',
        sassignTypeNm: '',
        sipHostTypeNm: '',
        sipHostNm: '',
        sexSvcNm: '',
        sexLinkUseTypeNm: '',
        smodelname: '',
        ssaid: '',
        sllnum: ''
      },
      resultVo: {},
      selectedLvl: null
    }
  },
  mounted () {
    this.resultVo = this._cloneDeep(this.resultVo)
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      if (model?.fnType) {
        this.fnType = model.fnType
      }
      if (model?.nipHostMstSeq) {
        this.fnViewDetailIPHostMst(model.nipHostMstSeq)
      }
    },
    onClose() {
    },
    async fnViewDetailIPHostMst(nipHostMstSeq) {
      this.loading = true
      try {
        const res = await apiRequestModel(ipmsModelApis.viewDetailIPHostMst, { nipHostMstSeq })
        this.resultVo = res.result.data
      } catch (error) {
       this.error(error)
      } finally {
        this.loading = false
      }
    },
    fnSearchOfficeList() { // 수용국 검색 같은 modal 사용
      this.$refs.ModalOrgSearch.open({ viewTitle: '수용국' })
    },
    setSelectedRow({ row, type }) {
      this.selectedLvl = row
      this.$set(this.resultVo, 'srssofficesNm', row.slvlNm)
      this.$set(this.resultVo, 'srssofficescode', row.slvlCd)
    },
    fnUpdateHostIpMst() {
      if (this.fnType === 'detail') {
        this.fnType = 'update'
      } else {
        this.fnUpdateSubmit()
      }
    },
    async fnUpdateSubmit() {
      if (this.resultVo.sipHostNm === null || this.resultVo.sipHostNm === '') {
        onMessagePopup(this, '호스트명을 입력하세요.')
        return
      }
      const { nipHostMstSeq, pipHostInet, srssofficescode, sipHostNm, smodelname, scomment, sprorityYn } = this.resultVo
      const tbIpHostMstVo = {
        nipHostMstSeq,
        pipHostInet,
        srssofficescode,
        sipHostNm,
        smodelname,
        scomment,
        sprorityYn,
        // smodifyId: this.$store.state.user.info.suserId,
      }
      this.confirm('운용정보를 변경 하시겠습니까?', '확인', {
        cancelButtonText: '취소',
        confirmButtonText: '확인',
      }).then(async() => {
        try {
          this.loading = true
          const res = await apiRequestJson(ipmsJsonApis.updateHostIPMst, tbIpHostMstVo)
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '운용정보 변경이 정상적으로 처리되었습니다.')
            this.resultVo = this._cloneDeep(this.defaultResultVo)
            this.$emit('reload')
            this.close()
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          this.error(error)
        } finally {
          this.loading = false
        }
      })
      .catch(action => {
      })
    },
    async fnDeleteHostIpMst(row) {
      this.confirm('해당 운용정보를 삭제 하시겠습니까?', '확인', {
          confirmButtonText: '확인',
          cancelButtonText: '취소',
          type: 'info'
        }).then(async () => {
          try {
            const res = await apiRequestJson(ipmsJsonApis.deleteHostIPMst, { nipHostMstSeq: row.nipHostMstSeq })
            if (res.commonMsg === 'SUCCESS') {
              onMessagePopup(this, '운용 정보가 정상적으로 삭제 되었습니다.')
              this.$emit('reload')
              this.close()
            } else {
              onMessagePopup(this, res.commonMsg)
            }
          } catch (error) {
            this.error(error)
          }
        })
        .catch(action => {
        })
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
