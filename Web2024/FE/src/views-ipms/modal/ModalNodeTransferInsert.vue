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
      :modal="modal"
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        IP주소 노드 이전 등록
        <hr>
      </span>

      <div id="content" class="layer">

        <div class="content_result">
          <form id="insertsearchForm" name="insertsearchForm" method="POST" onsubmit="return false">
            <table class="tbl_data entry" summary="IP주소 검색">
              <caption>조회조건선택</caption>
              <colgroup>
                <col width="39%" />
                <col width="61%" />
              </colgroup>
              <tbody class="top last">
                <tr>
                  <th scope="row">IP 주소</th>
                  <td class="ip_add">
                    <span>
                      <el-select id="sipVersionTypeCd" v-model="sipVersionTypeCd" class="w-20" size="mini">
                        <!-- options =>  sipVersionTypeCd !== 'CV0000' -->
                        <el-option
                          v-for="item in sipVerCdOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        >
                        </el-option>
                      </el-select>
                    </span>
                    <span>
                      <el-input
                        v-model="searchWrd"
                        size="mini"
                        type="text"
                        class="txt w-80"
                        maxlength="43"
                        @input="chkCharCode"
                        @keyup.enter="fnSelectListIpAssignMst()"
                      />
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
            <div class="float-right">
              <el-button size="mini" icon="el-icon-search" @click="fnSelectListIpAssignMst()">조회</el-button>
            </div>
          </form>
        </div>
        <div class="content_result">
          <compTable
            ref="compTable"
            :prop-name="name"
            :prop-table-height="150"
            :prop-data="pagination.data"
            :prop-pagination-data.sync="pagination"
            :prop-column="tableColumns"
            :prop-is-pagination="true"
            prop-pagination-layout="prev, pager, next"
            :prop-is-check-box="false"
            :prop-max-select="pagination.data.length"
            prop-grid-menu-id="inputSpeed"
            :prop-grid-indx="1"
            :prop-on-page-change="handleChangeCurPage"
            :prop-on-page-size-change="handleChangeCurPage"
          >
            <template slot="text-description">
              <span>
                조회결과
              </span>
            </template>
          </compTable>
        </div>
        <div class="content_result mt5" style="padding-top: 7px;">
          <h4>변경 전 계위정보</h4>
          <table class="tbl_data entry" summary="변경후">
            <caption>조회조건선택</caption>
            <colgroup>
              <col width="39%" /><col width="61%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">IP블록</th>
                <td>{{ resultVo.pipPrefix }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">서비스망</th>
                <td>{{ resultVo.ssvcLineTypeNm }}</td>
              </tr>
              <tr>
                <th scope="row">본부</th>
                <td>{{ resultVo.ssvcGroupNm }}</td>
              </tr>
              <tr>
                <th scope="row">노드</th>
                <td>{{ resultVo.ssvcObjNm }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="content_result mt5" style="padding-top: 7px;">
          <h4>변경 후 계위정보</h4>
          <table class="tbl_data entry" summary="변경후">
            <caption>조회조건선택</caption>
            <colgroup>
              <col width="39%" /><col width="61%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">서비스망</th>
                <td>
                  <div class="inline-block w99 w-100">
                    <el-select id="updSsvcLineTypeCd" v-model="updSsvcLineTypeCd" class="w-100" name="ssvcLineTypeCd" size="mini" @change="handleChangeLvl1()">
                      <el-option label="전체" value=""><span class="w-100 h-100 d-inline-block">전체</span></el-option>
                      <el-option
                        v-for="item in ssvcLineTypeNmOp"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      />
                    </el-select>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">본부</th>
                <td>
                  <div class="inline-block w99 w-100">
                    <el-select id="updSsvcGroupCd" v-model="updSsvcGroupCd" class="w-100" :disabled="isDisabled" name="ssvcGroupCd" size="mini" @change="handleChangeLvl2()">
                      <el-option
                        v-for="item in ssvcGroupNmOp"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      />
                    </el-select>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">노드</th>
                <td>
                  <div class="inline-block w99 w-100">
                    <el-select id="updSsvcObjCd" v-model="updSsvcObjCd" class="w-100" :disabled="isDisabled" name="ssvcObjCd" size="mini">
                      <el-option
                        v-for="item in ssvcObjNmOp"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      />
                    </el-select>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="content_result mt5" style="padding-top: 7px;">
          <h4>변경 사유</h4>
          <table class="tbl_data entry" summary="변경후">
            <caption>변경사유 선택</caption>
            <colgroup>
              <!-- <col width="39%" /><col width="70%" /> -->
              <col width="39%" /><col width="61%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">변경 사유</th>
                <td>
                  <div class="inline-block w-100">
                    <el-select id="sCommentType" v-model="sCommentType" class="w-100" size="mini">
                      <el-option
                        v-for="item in sCommentOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      >
                      </el-option>
                    </el-select>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">세부 사유</th>
                <td>
                  <div class="inline-block w-100">
                    <textarea id="sComment" v-model="sComment" size="mini" rows="2" type="textarea" class="w98"></textarea>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button size="mini" type="primary" icon="el-icon-edit" @click="fnInsertNode()">등록</el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import CompTable from '@/components/elTable/CompTable.vue'
import { apiRequestModel, ipmsModelApis, apiRequestJson, ipmsJsonApis } from '@/api/ipms'

const routeName = 'ModalNodeTransferInsert'

export default {
  name: routeName,
  components: { CompTable },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      resultVo: { pipPrefix: '', ssvcLineTypeNm: '', ssvcGroupNm: '', ssvcObjNm: '' },
      tableColumns: [
        { prop: '', label: '선택', align: 'center', width: 50, propIsCheckBox: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              on: { click: () => {
                this.selectNode(row)
            } } }, '선택')
          }
         },
        { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', propIsCheckBox: true, columnVisible: false, showOverflow: true },
        { prop: 'ssvcGroupNm', label: '본부', align: 'center', sortable: true, propIsCheckBox: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcObjNm', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sipCreateTypeNm', label: '공인/사설', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sassignTypeNm', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'pipPrefix', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sassignLevelNm', label: '배정상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'nsummaryCnt', label: '라우팅중복개수', align: 'center', width: 165, sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '해지', align: 'center', columnVisible: true, showOverflow: true, width: 50,
          formatter: (row, col, value, index) => {
            if (row.sassignLevelCd === 'IA0005' || row.sassignLevelCd === 'IA0006') {
              return this.$createElement('el-button', {
                on: { click: () => {
                  this.fnDeleteAlcIPMstClick(row)
              } } }, '해지')
            } else {
              return ''
            }
          }
         },
      ],
      resultListVo: [],
      tbIpAssignMstVos: [],
      sipVersionTypeCd: 'CV0001',
      sipVerCdOptions: [{ value: 'CV0001', label: 'IPv4' }, { value: 'CV0002', label: 'IPv6' }],
      searchWrd: '',
      sCommentOptions: [
        { value: 'typ001', label: '기업고객 설변에 따른 노드 이전' },
        { value: 'typ002', label: '기업고객 해지에 원소속 노드 반납' },
        { value: 'typ003', label: 'DB 정비에 따른 조치' },
        { value: 'typ004', label: 'IP주소 재분배/조정' },
        { value: 'typ005', label: '단순 반납 실수' },
        { value: 'typ006', label: '기타' }
      ],
      ssvcLineTypeNmOp: [
        { label: 'KORNET', value: 'CL0001' },
        { label: 'PREMIUM', value: 'CL0002' },
        { label: 'MOBILE', value: 'CL0003' },
        { label: 'GNS', value: 'CL0004' },
        { label: 'VPN', value: 'CL0005' },
        { label: 'ICC', value: 'CL0006' },
        { label: '미분류', value: 'CL0007' },
        { label: 'SCHOOLNET', value: 'CL0008' }
      ],
      ssvcGroupNmOp: [{ label: '-------', value: '000000' }],
      ssvcObjNmOp: [{ label: '-------', value: '000000' }],
      sComment: '',
      sCommentType: '',
      updSsvcLineTypeCd: '',
      updSsvcGroupCd: '',
      updSsvcObjCd: '',
    }
  },
  computed: {
    isDisabled() {
      return this.updSsvcLineTypeCd === '' ?? true
    }
  },
  mounted() {
    this.pagination.pageSize = 5
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
    },
    onClose() { },
    async fnSelectListIpAssignMst() {
      const parameter = { sipVersionTypeCd: this.sipVersionTypeCd ?? '', searchWrd: this.searchWrd ?? '', sortType: 'PIP_PREFIX', }
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      const target = ({ vue: this.$refs.compTable })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.selectListIpAssignMst, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnSelectListIpAssignMst()
    },
    chkCharCode(val) {
      this.searchWrd = val.replace(/[^0-9.]+/g, '')
    },
    async selectNode(row) {
      if (row.sassignLevelCd === 'IA0006') {
        onMessagePopup(this, '선택한 대상이 할당(할당예약)상태 입니다.')
        return
      }
      this.resultVo = this._cloneDeep(row)
    },
    fnSelectNodeCallback(row) {
      const successMsg = 'SUCCESS'
      if (row.commonMsg === successMsg) {
          this.$confirm('현재 운용자의 권한범위 외 IP블록입니다. 계속 진행하시겠습니까?', '알림', {
              confirmButtonText: '확인',
              cancelButtonText: '취소',
              type: 'warning'
          }).then(() => {
            const { pipPrifix, ssvcLineTypeNm, ssvcGroupNm, ssvcObjNm } = row
            this.resultVo.pipPrifix = pipPrifix
            this.resultVo.ssvcLineTypeNm = ssvcLineTypeNm
            this.resultVo.ssvcGroupNm = ssvcGroupNm
            this.resultVo.ssvcObjNm = ssvcObjNm
          }).catch((error) => {
            this.error(error)
          })
      } else {
          this.resultVo = row
      }
    },
    async fnDeleteAlcIPMstClick(row) { // 해지
       try {
        const ipAllocOperMstVo = {
          nipAssignMstSeq: row.nipAssignMstSeq,
          menuType: 'NodeReq'
        }
        const res = await apiRequestJson(ipmsJsonApis.viewfnDeleteAlcIPMst, ipAllocOperMstVo)
        if (res.commonMsg === 'SUCCESS') {
          this.fnSelectListIpAssignMst()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    fnInsertNode() {
      if (this.resultVo.pipPrefix === '') {
        onMessagePopup(this, '변경 전 데이터가 선택되지 않았습니다.')
        return
      }
      if (this.resultVo.ssvcLineTypeNm === '') {
        onMessagePopup(this, '변경 후 서비스망을 선택해 주시길 바랍니다.')
        return
      }
      if (this.resultVo.sCommentType === '') {
        onMessagePopup(this, '변경 사유를 선택해 주시길 바랍니다.')
        return
      }
      if (this.resultVo.sCommentType === 'typ006' && this.resultVo.sComment === '') {
        onMessagePopup(this, '세부 사유를 입력해 주시길 바랍니다.')
        return
      }
      this.$confirm('IP노드 이전 신청 하시겠습니까?', '알림', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        let res
        try {
          /* before */
          const { pipPrefix, nipAssignMstSeq, ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, sassignTypeCd, sassignLevelCd } = this.resultVo
          const tbIpAssignMstComplexVo = {
            pipPrefix: pipPrefix,
            beforeSsvcLineTypeCd: ssvcLineTypeCd,
            beforeSsvcGroupCd: ssvcGroupCd,
            beforeSsvcObjCd: ssvcObjCd,
            nipAssignMstSeq: nipAssignMstSeq,
            afterSsvcLineTypeCd: this.updSsvcLineTypeCd,
            afterSsvcGroupCd: this.updSsvcGroupCd,
            afterSsvcObjCd: this.updSsvcObjCd,
            sassignTypeCd: sassignTypeCd,
            sassignLevelCd: sassignLevelCd,
            sCommentType: this.sCommentType,
            sComment: this.sComment
          }
           res = await apiRequestJson(ipmsJsonApis.insertNode, tbIpAssignMstComplexVo)
           if (res.commonMsg === 'SUCCESS') {
              onMessagePopup(this, 'IP노드 이전 신청이 정상적으로 등록되었습니다.')
              this.$emit('reload')
              this.close()
            } else if (res.commonMsg === 'duplicate') {
              onMessagePopup(this, '이미 등록된 이전 신청입니다')
            } else {
              onMessagePopup(this, res.commonMsg)
            }
          } catch (error) {
            this.log(error)
          }
        })
    },
    handleChangeLvl1() {
      if (this.updSsvcLineTypeCd === '') {
        this.updSsvcGroupCd = ''
        this.updSsvcObjCd = ''
        return
      }
      this.loadCenterList()
      this.loadNodeList()
    },
    handleChangeLvl2() {
      this.loadNodeList()
    },
    async loadCenterList() {
      const tbLvlBasVo = { ssvcLineTypeCd: this.updSsvcLineTypeCd }
      const res = await apiRequestJson(ipmsJsonApis.selectAuthCenterList, tbLvlBasVo)
      this.ssvcGroupNmOp = res?.tbLvlBasVos?.filter(v => v.ssvcGroupNm !== '전체').map(v => { return { value: v.ssvcGroupCd, label: v.ssvcGroupNm } })
      this.updSsvcGroupCd = '000000'
      this.updSsvcObjCd = '000000'
      this.ssvcObjNmOp = [{ label: '-------', value: '000000' }]
    },
    async loadNodeList() {
      const tbLvlBasVo = {
        ssvcLineTypeCd: this.updSsvcLineTypeCd,
        ssvcGroupCd: this.updSsvcGroupCd,
      }
      const res = await apiRequestJson(ipmsJsonApis.selectAuthNodeList, tbLvlBasVo)
      this.ssvcObjNmOp = res?.tbLvlBasVos?.filter(v => v.ssvcObjNm !== '전체').map(v => { return { value: v.ssvcObjCd, label: v.ssvcObjNm } })
      this.updSsvcObjCd = '000000'
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
