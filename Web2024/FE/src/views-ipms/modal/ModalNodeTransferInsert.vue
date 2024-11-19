<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="IP주소 노드 이전 등록"
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
    <div class="popupContentTable">
      <div class="popupContentTableTitle">조회조건 선택</div>
      <table>
        <tbody>
          <colgroup>
            <col width="30%" /><col width="70%" />
          </colgroup>
          <tr>
            <th>IP 주소</th>
            <td class="textflex">
              <el-select v-model="sipVersionTypeCd" size="small">
                <!-- options =>  sipVersionTypeCd !== 'CV0000' -->
                <el-option
                  v-for="item in sipVerCdOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
              <el-input
                v-model="searchWrd"
                size="small"
                type="text"
                maxlength="43"
                @input="chkCharCode"
                @keyup.enter.native="fnSelectListIpAssignMst()"
              />
            </td>
            <td>
              <el-button type="primary" size="small" round @click="fnSelectListIpAssignMst()">조회</el-button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTable textcenter">
      <table v-loading="tableLoading" style="margin: 10px 0">
        <tr>
          <th>선택</th>
          <th>서비스망</th>
          <th>본부</th>
          <th>노드</th>
          <th>공인/사설</th>
          <th>서비스</th>
          <th>IP블록</th>
          <th>배정상태</th>
          <th>라우팅중복개수</th>
          <th>해지</th>
        </tr>
        <tr v-if="pagination.data.length === 0">
          <td class="textcenter" colspan="10">조회된 결과 목록이 존재하지 않습니다.</td>
        </tr>
        <template v-else>
          <tr v-for="(row, index) in pagination.data" :key="index">
            <td class="textcenter">
              <el-button type="primary" size="mini" round @click="selectNode(row)">선택</el-button>
            </td>
            <td>{{ row.ssvcLineTypeNm }}</td>
            <td>{{ row.ssvcGroupNm }}</td>
            <td>{{ row.ssvcObjNm }}</td>
            <td>{{ row.sipCreateTypeNm }}/사설</td>
            <td>{{ row.sassignTypeNm }}</td>
            <td>{{ row.pipPrefix }}</td>
            <td>{{ row.sassignLevelNm }}</td>
            <td style="text-decoration: underline;">
              {{ row.nsummaryCnt }}
            </td>
            <td>
              <template v-if="row.sassignLevelCd === 'IA0005' || row.sassignLevelCd === 'IA0006'">
                <el-button type="primary" size="small" round @click="fnDeleteAlcIPMstClick(row)">해지</el-button>
              </template>
            </td>
          </tr>
        </template>
      </table>
      <div v-if="pagination.data.length > 0" class="tableListWrap">
        <div class="tableListPaging" style="justify-content: center;">
          <el-pagination
            :current-page.sync="pagination.currentPage"
            :total="pagination.total"
            :page-size="pagination.pageSize"
            layout="prev, pager, next"
            @current-change="handleChangeCurPage"
            @size-change="handleChangeCurPage"
          />
        </div>
      </div>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">변경 전 계위정보</div>
      <table>
        <tr>
          <th>IP블록</th>
          <td>{{ resultVo.pipPrefix }}</td>
          <th>서비스망</th>
          <td>{{ resultVo.ssvcLineTypeNm }}</td>
        </tr>
        <tr>
          <th>본부</th>
          <td>{{ resultVo.ssvcGroupNm }}</td>
          <th>노드</th>
          <td>{{ resultVo.ssvcObjNm }}</td>
        </tr>
      </table>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">변경 후 계위정보</div>
      <table>
        <tr>
          <th>서비스망</th>
          <td class="flex">
            <el-select v-model="updSsvcLineTypeCd" class="w-100" name="ssvcLineTypeCd" size="small" @change="handleChangeLvl1()">
              <el-option label="전체" value=""><span class="w-100 h-100 d-inline-block">전체</span></el-option>
              <el-option
                v-for="item in ssvcLineTypeNmOp"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </td>
          <th>본부</th>
          <td>
            <el-select v-model="updSsvcGroupCd" class="w-100" :disabled="isDisabled" name="ssvcGroupCd" size="small" @change="handleChangeLvl2()">
              <el-option
                v-for="item in ssvcGroupNmOp"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </td>
          <th>노드</th>
          <td>
            <el-select v-model="updSsvcObjCd" class="w-100" :disabled="isDisabled" name="ssvcObjCd" size="small">
              <el-option
                v-for="item in ssvcObjNmOp"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </td>
        </tr>
      </table>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">변경 사유</div>
      <table>
        <colgroup>
          <col width="30%" /><col width="70%" />
        </colgroup>
        <tbody>
          <tr>
            <th>변경 사유</th>
            <td>
              <el-select v-model="sCommentType" size="small">
                <el-option
                  v-for="item in sCommentOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </td>
          </tr>
          <tr>
            <th>세부 사유(기타 선택시 필수 입력)</th>
            <td>
              <textarea v-model="sComment" size="small" rows="2" type="textarea"></textarea>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-edit" round @click="fnInsertNode()">등록</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import { apiRequestModel, ipmsModelApis, apiRequestJson, ipmsJsonApis } from '@/api/ipms'

const routeName = 'ModalNodeTransferInsert'

export default {
  name: routeName,
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableLoading: false,
      resultVo: { pipPrefix: '', ssvcLineTypeNm: '', ssvcGroupNm: '', ssvcObjNm: '' },
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
      try {
        this.tableLoading = true
        const res = await apiRequestModel(ipmsModelApis.selectListIpAssignMst, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        this.error(error)
      } finally {
        this.tableLoading = false
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnSelectListIpAssignMst()
    },
    chkCharCode(val) {
      this.searchWrd = val.replace(/[^0-9.\/]+/g, '')
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
