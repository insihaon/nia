<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      id="ipms"
      v-el-drag-dialog
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
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        운용정보 상세 정보
        <hr>
      </span>
      <div id="content" v-loading="loading" class="layer">
        <!-- 공통항목 Section -->
        <div class="content_result bottom_dotted mt0">
          <h4 class="mt5">공통항목</h4>
          <table class="tbl_data entry mt5" summary="공통항목">
            <caption>공통항목</caption>
            <!-- <colgroup>
              <col width="10%" />
              <col width="35%" />
              <col width="10%" />
              <col width="25%" />
              <col width="10%" />
              <col width="10%" />
            </colgroup> -->
            <tbody>
              <tr class="top last">
                <th class="first" scope="row">IP</th>
                <td class="view">{{ resultVo.pipHostInet }}</td>
                <th scope="row">용도</th>
                <td class="view">{{ resultVo.scomment }}</td>
                <th scope="row">IP 버전</th>
                <td class="view">{{ resultVo.sipVersionTypeNm }}</td>
                <template v-if="fnType === 'update'">
                  <th scope="row">대표여부</th>
                  <td class="view">
                    <select id="updateSprorityYno" v-model="resultVo.sprorityYn">
                      <option value="Y">Y</option>
                      <option value="N">N</option>
                    </select>
                  </td>
                </template>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 세부항목 Section -->
        <div class="content_result">
          <div class="tit_group">
            <h4 class="mt5">세부항목</h4>
          </div>
          <table id="ipHostTable" class="tbl_list mt5" summary="목록">
            <caption>목록</caption>
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
                <th class="first" scope="col" colspan="2">기본 정보</th>
                <th scope="col" colspan="2">시설 정보</th>
                <th scope="col" colspan="2">회선 정보</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <th class="first" scope="row">수용국</th>
                <td v-if="fnType === 'detail'" class="view">{{ resultVo.srssofficesNm }}</td>
                <td v-else class="view">
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
                <th scope="row">I/F명</th>
                <td class="view">{{ resultVo.sipIfNm }}</td>
                <th scope="row">서비스</th>
                <td class="view">{{ resultVo.sassignTypeNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">HOST유형</th>
                <td class="view">{{ resultVo.sipHostTypeNm }}</td>
                <th scope="row">장비명</th>
                <td v-if="fnType === 'detail'" class="view">{{ resultVo.sipHostNm }}</td>
                <td v-else class="view">
                  <el-input v-model="resultVo.sipHostNm" />
                </td>
                <th scope="row">상품명</th>
                <td class="view">{{ resultVo.sexSvcNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">외부연동</th>
                <td class="view">{{ resultVo.sexLinkUseTypeNm }}</td>
                <th scope="row">모델명</th>
                <td v-if="fnType === 'detail'" class="view">{{ resultVo.smodelname }}</td>
                <td v-else class="view">
                  <el-input v-model="resultVo.smodelname" />
                </td>
                <th scope="row">SAID</th>
                <td class="view">{{ resultVo.ssaid }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row"></th>
                <td></td>
                <th scope="row"></th>
                <td></td>
                <th scope="row">전용회선</th>
                <td class="view">{{ resultVo.sllnum }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-edit" @click="fnUpdateHostIpMst">{{ fnType === 'update'? '등록' : '수정' }}</el-button>
        <el-button v-if="fnType === 'detail'" size="mini" class="el-icon-edit" @click="fnDeleteHostIpMst">삭제</el-button>
        <el-button size="mini" class="el-icon-close" @click.native="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
    <ModalOrgSearch ref="ModalOrgSearch" @selected-value="setSelectedRow" />
  </div>
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
          type: 'success'
        }).then(async () => {
          try {
            const res = await apiRequestJson(ipmsJsonApis.deleteHostIPMst, { nipHostMstSeq: row.nipHostMstSeq })
            if (res.commonMsg === 'SUCCESS') {
              onMessagePopup(this, '운용 정보가 정상적으로 삭제 되었습니다.')
              this.fnViewListIpHostMst()
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
::v-deep .el-input {
  input {
    display: inline-block;
    height: 22px;
    line-height: 21px;
    padding: 2px 4px;
    border: #ccc solid 1px;
    color: #434343;
    font-size: 1em;
    vertical-align: middle;
  }
}
::v-deep .el-input__inner:focus {
  border: solid 2px #cc2929;
}
::v-deep .el-input__suffix {
  top: -3px;
}
</style>
