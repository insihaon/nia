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
                      <el-input v-model="InsertSearchWrd" size="mini" type="text" class="txt w-80" title="IP 주소 입력창" maxlength="43" @input="chkCharCode()" />
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
            <div class="float-right">
              <el-button size="mini" icon="el-icon-document-add" @click="fnSelectListPage()">조회</el-button>
            </div>
          </form>
        </div>

        <div class="content_result">
          <h4>조회결과</h4>
          <table id="contentTable" class="tbl_list mt5" summary="조회결과">
            <caption>조회결과</caption>
            <colgroup>
              <col width="8%" /><col width="8%" /><col width="15%" />
              <col width="8%" /><col width="8%" /><col width="13%" />
              <col width="10%" /><col width="12%" /><col width="10%" />
              <col width="8%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col">선택</th>
                <th class="first" scope="col">서비스망</th>
                <th scope="col">본부</th>
                <th scope="col">노드</th>
                <th scope="col">공인/사설</th>
                <th scope="col">서비스</th>
                <th scope="col">IP블록</th>
                <th scope="col">배정상태</th>
                <th scope="col">라우팅중복개수</th>
                <th scope="col">해지</th>
              </tr>
            </thead>
            <tbody>
              <!-- 조회 결과가 없는 경우 -->
              <template v-if="tbIpAssignMstVos.totalCount === 0">
                <tr class="subbg last">
                  <td class="first" colspan="10">조회된 결과 목록이 존재하지 않습니다.</td>
                </tr>
              </template>
              <!-- 조회 결과가 있는 경우 -->
              <template v-else>
                <tr
                  v-for="(item, index) in tbIpAssignMstVos"
                  :key="item.nipAssignMstSeq"
                  :class="{
                    last: index === tbIpAssignMstVos.length - 1,
                    subbg: index % 2 !== 0
                  }"
                >
                  <td class="first">
                    <el-button size="mini" @click="selectNode(tbIpAssignMstVos[index])">선택</el-button>
                  </td>
                  <td class="ellipsis" :title="item.ssvcLineTypeNm">{{ item.ssvcLineTypeNm }}</td>
                  <td class="ellipsis" :title="item.ssvcGroupNm">{{ item.ssvcGroupNm }}</td>
                  <td class="ellipsis" :title="item.ssvcObjNm">{{ item.ssvcObjNm }}</td>
                  <td class="ellipsis" :title="item.sipCreateTypeNm">{{ item.sipCreateTypeNm }}</td>
                  <td class="ellipsis" :title="item.sassignTypeNm">{{ item.sassignTypeNm }}</td>
                  <td class="ellipsis">{{ item.pipPrefix }}</td>
                  <td class="ellipsis" :title="item.sassignLevelNm">{{ item.sassignLevelNm }}</td>
                  <td class="ellipsis" :title="item.nsummaryCnt" style="text-decoration: underline;">
                    <template v-if="['CL0001', 'CL0002', 'CL0003'].includes(item.ssvcLineTypeCd)">
                      <span v-if="item.nsummaryCnt != '0'">{{ item.nsummaryCnt }}</span>
                      <span v-else>{{ item.nsummaryCnt }}</span>
                    </template>
                    <span v-else>-</span>
                  </td>
                  <td>
                    <template v-if="['IA0006', 'IA0005'].includes(item.sassignLevelCd)" class="btn_area" style="float: none;">
                      <el-button size="mini" @click="fnDeleteAlcIPMstClick(item.nipAssignMstSeq)">해지</el-button>
                    </template>
                  </td>
                  <td v-if="false">{{ item.ssvcLineTypeCd }}</td>
                  <td v-if="false">{{ item.ssvcGroupCd }}</td>
                  <td v-if="false">{{ item.ssvcObjCd }}</td>
                  <td v-if="false">{{ item.nipAssignMstSeq }}</td>
                  <td v-if="false">{{ item.nipAllocMstSeq }}</td>
                  <td v-if="false">{{ item.sassignLevelCd }}</td>
                  <td v-if="false">{{ item.sassignTypeCd }}</td>
                </tr>
              </template>
            </tbody>
          </table>
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
                <td>
                  {{ resultVo.pipPrifix }}
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">서비스망</th>
                <td>
                  {{ resultVo.ssvcLineTypeNm }}
                </td>
              </tr>
              <tr>
                <th scope="row">본부</th>
                <td>
                  {{ resultVo.ssvcGroupNm }}
                </td>
              </tr>
              <tr>
                <th scope="row">노드</th>
                <td>
                  {{ resultVo.ssvcObjNm }}
                </td>
                <td v-if="false"> {{ resultVo.beforeSsvcGroupCd }}</td>
                <td v-if="false"> {{ resultVo.beforeSsvcObjCd }}</td>
                <td v-if="false"> {{ resultVo.nipAssignMstSeq }}</td>
                <td v-if="false"> {{ resultVo.nipAllocMstSeq }}</td>
                <td v-if="false"> {{ resultVo.sassignLevelCd }}</td>
                <td v-if="false"> {{ resultVo.sassignTypeCd }}</td>
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
                      <el-option label="전체" value=""><span class="w-100 h-100 d-inline-block" @click="toggleAll()">전체</span></el-option>
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
              <col width="39%" /><col width="70%" />
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
        <el-button size="mini" class="el-icon-close" @click="fnInsertNode()">{{ $t('등록') }}</el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestModel, ipmsModelApis, apiRequestJson, ipmsJsonApis } from '@/api/ipms'

const routeName = 'ModalNodeTransferInsert'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      resultVo: {
        pipPrifix: '',
        ssvcLineTypeNm: '',
        ssvcGroupNm: '',
        ssvcObjNm: '',
      },
      tableColumns: [
        { prop: 'nipBlockMstSeq', label: '', align: 'center', propIsCheckBox: true, columnVisible: false, showOverflow: true },
        { prop: 'sipCreateTypeNm', label: '공인/사설', align: 'center', sortable: true, propIsCheckBox: true, columnVisible: true, showOverflow: true },
        { prop: 'sipCreateSeqNm', label: '생성차수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'pipPrefix', label: 'IP 블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sfirstAddr', label: '시작 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      resultListVo: [],
      tbIpAssignMstVos: [],
      sipVersionTypeCd: '',
      sipVerCdOptions: [{
          value: 'CV0001',
          label: 'IPv4'
        },
        {
          value: 'CV0002',
          label: 'IPv6'
      }],
      InsertSearchWrd: '',
      sCommentOptions: [
        { value: 'typ001', label: '기업고객 설변에 따른 노드 이전' },
        { value: 'typ002', label: '기업고객 해지에 원소속 노드 반납' },
        { value: 'typ003', label: 'DB 정비에 따른 조치' },
        { value: 'typ004', label: 'IP주소 재분배/조정' },
        { value: 'typ005', label: '단순 반납 실수' },
        { value: 'typ006', label: '기타' }
      ],
      upSsvcCdOptions: [],
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
      ssvcGroupNmOp: [{
        label: '-------',
        value: '000000'
      }],
      ssvcObjNmOp: [{
        label: '-------',
        value: '000000'
      }],
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
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      // this.fnSelectListPage()
    },
    chkCharCode() {
      const regExp = /[^0-9]/g
      this.InsertSearchWrd = this.InsertSearchWrd.replace(regExp, '')
    },
    async selectNode(row) {
      if (row.sassignLevelCd === 'IA0006') {
        this.$message('선택한 대상이 할당(할당예약)상태 입니다.')
        return
      }
      try {
        const tbIpAssignMstVo = {
          nipAssignMstSeq: row.nipAssignMstSeq
        }
        const res = await apiRequestModel(ipmsModelApis.viewfnSelectNode, tbIpAssignMstVo)
        if (res.data) {
          this.fnSelectNodeCallBack(res.data)
        }
      } catch (error) {
        console.error(error)
      }
    },
    fnSelectNodeCallback(row) {
      const successMsg = 'SUCCESS'
      if (row.commonMsg === successMsg) {
          this.$confirm('현재 운용자의 권한범위 외 IP블록입니다. 계속 진행하시겠습니까?', '확인', {
              confirmButtonText: 'OK',
              cancelButtonText: 'Cancel',
              type: 'warning'
          }).then(() => {
            this.resultVo = row
          }).catch(() => {
            /*  */
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
        const res = await apiRequestModel(ipmsModelApis.viewfnDeleteAlcIPMst, ipAllocOperMstVo)
        if (res.commonMsg) {
          this.fnSelectListPage()
        }
      } catch (error) {
        this.$message.error({ message: `해지에 실패했습니다.` })
        console.error(error)
      }
    },
    async fnSelectListPage() {
      try {
        const searchVo = {
          sortType: 'PIP_PREFIX',
          pageIndex: 1,
          pageUnit: 5,
          sipVersionTypeCd: this.sipVersionTypeCd ?? '',
          InsertSearchWrd: this.InsertSearchWrd ?? ''
        }
        const res = await apiRequestModel(ipmsModelApis.viewInsertNode, searchVo)
        if (res.result.data) {
          this.tbIpAssignMstVos = res.result.data
        }
      } catch (error) {
        console.error(error)
      }
    },
    fnInsertNode() {
      if (this.resultVo.pipPrifix === '') {
        this.$message('변경 전 데이터가 선택되지 않았습니다.')
        return
      }
      if (this.resultVo.ssvcLineTypeNm === '') {
        this.$message('변경 후 서비스망을 선택해 주시길 바랍니다.')
        return
      }
      if (this.resultVo.sCommentType === '') {
        this.$message('변경 사유를 선택해 주시길 바랍니다.')
        return
      }
      if (this.resultVo.sCommentType === 'typ006' && this.resultVo.sComment === '') {
        this.$message('세부 사유를 입력해 주시길 바랍니다.')
        return
      }

      this.$confirm('등록 하시겠습니까?', '노드 이전 승인', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          const { pipPrefix, beforeSsvcLineTypeCd, beforeSsvcGroupCd, nipAssignMstSeq, afterSsvcLineTypeCd, afterSsvcGroupCd, afterSsvcObjCd, sassignTypeCd, sassignLevelCd, sCommentType, sComment } = this.resultVo
          const tbIpAssignMstComplexVo = {
            pipPrefix: pipPrefix,
            beforeSsvcLineTypeCd: beforeSsvcLineTypeCd,
            beforeSsvcGroupCd: beforeSsvcGroupCd,
            nipAssignMstSeq: nipAssignMstSeq,
            afterSsvcLineTypeCd: afterSsvcLineTypeCd,
            afterSsvcGroupCd: afterSsvcGroupCd,
            afterSsvcObjCd: afterSsvcObjCd,
            sassignTypeCd: sassignTypeCd,
            sassignLevelCd: sassignLevelCd,
            sCommentType: sCommentType,
            sComment: sComment
          }
          const res = await apiRequestJson(ipmsJsonApis.insertNode, tbIpAssignMstComplexVo)
           if (res.commonMsg === 'SUCCESS') {
            this.$message.success({ message: `${res.commonMsg}` })
            this.$emit('reloadData')
            }
          } catch (error) {
            this.$message.error({ message: `등록에 실패했습니다.` })
            console.log(error)
          }
        })
    },
    async handleChangeLvl1() {
      const tbLvlBasVo = { ssvcLineTypeCd: this.updSsvcLineTypeCd }
      const res = await apiRequestJson(ipmsJsonApis.selectAuthCenterList, tbLvlBasVo)
      this.ssvcGroupNmOp = res?.tbLvlBasVos?.filter(v => v.ssvcGroupNm !== '전체').map(v => { return { value: v.ssvcGroupCd, label: v.ssvcGroupNm } })
    },
    async handleChangeLvl2() {
      const tbLvlBasVo = {
        ssvcLineTypeCd: this.updSsvcLineTypeCd,
        ssvcGroupCd: this.updSsvcGroupCd,
      }
      const res = await apiRequestJson(ipmsJsonApis.selectAuthNodeList, tbLvlBasVo)
      this.ssvcObjNmOp = res?.tbLvlBasVos?.filter(v => v.ssvcObjCd !== '전체').map(v => { return { value: v.ssvcObjCd, label: v.ssvcObjNm } })
    },
    toggleAll() {
      this.updSsvcGroupCd = ''
      this.updSsvcObjCd = ''
    },
    onClose() { },
  },
}
</script>
<style lang="scss" scoped>
</style>
