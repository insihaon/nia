<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="운용정보 등록"
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
    <div class="popupContentTable">
      <div class="popupContentTableTitle">링크 정보</div>
      <table>
        <colgroup>
          <col width="20%" />
          <col width="27%" />
          <col width="23%" />
          <col width="30%" />
        </colgroup>
        <tbody>
          <tr>
            <th>링크IP블록</th>
            <td v-if="fnType === 'insert'" colspan="3" class="textflex">
              <el-input
                v-model="resultVo.insertPifSerialIp"
                type="text"
                size="mini"
                maxlength="43"
                title="IP 주소 입력창"
                @input="onChangeInput"
              />
              <el-select v-model="resultVo.insertNbitMask" size="mini">
                <el-option value="/30">/30</el-option>
                <el-option value="/29">/29</el-option>
              </el-select>
            </td>
            <td v-else>
              {{ resultVo.pifSerialIp }}
            </td>
          </tr>
          <tr>
            <th>자국 수용국</th>
            <td>
              <el-input
                v-model="resultVo.saofficescodeNm"
                size="mini"
                readonly
              >
                <template #suffix>
                  <el-button
                    size="mini"
                    icon="el-icon-search"
                    class="font-weight-bolder"
                    style="font-size: larger;border: none;background: none;"
                    @click="fnSearchOfficeList('a')"
                  />
                </template>
              </el-input>
            </td>
            <th>대국 수용국</th>
            <td>
              <el-input
                v-model="resultVo.szofficescodeNm"
                size="mini"
                readonly
              >
                <template #suffix>
                  <el-button
                    size="mini"
                    icon="el-icon-search"
                    class="font-weight-bolder"
                    style="font-size: larger;border: none;background: none;"
                    @click="fnSearchOfficeList('z')"
                  />
                </template>
              </el-input>
            </td>
          </tr>
          <tr>
            <th>자국 장비명</th>
            <td>
              <el-input
                v-model="resultVo.sanealias"
                type="text"
                size="mini"
                maxlength="200"
                title="장비명"
              />
            </td>
            <th>대국 장비명</th>
            <td>
              <el-input
                v-model="resultVo.sznealias"
                type="text"
                size="mini"
                maxlength="200"
                title="장비명"
              />
            </td>
          </tr>
          <tr>
            <th>자국 장비IP</th>
            <td>
              <el-input
                v-model="resultVo.samstip"
                type="text"
                size="mini"
                maxlength="43"
              />
            </td>
            <th>대국 장비IP</th>
            <td>
              <el-input
                v-model="resultVo.szmstip"
                type="text"
                size="mini"
                maxlength="43"
              />
            </td>
          </tr>
          <tr>
            <th>자국 IF명</th>
            <td>
              <el-input
                v-model="resultVo.saifname"
                type="text"
                size="mini"
                maxlength="300"
              />
            </td>
            <th>대국 IF명</th>
            <td>
              <el-input
                v-model="resultVo.szifname"
                type="text"
                size="mini"
                maxlength="300"
              />
            </td>
          </tr>
          <tr>
            <th>SAID</th>
            <td>
              <el-input
                v-model="resultVo.ssaid"
                type="text"
                size="mini"
                maxlength="30"
              />
            </td>
            <th>전용번호</th>
            <td>
              <el-input
                v-model="resultVo.sllnum"
                type="text"
                size="mini"
                maxlength="30"
              />
            </td>
          </tr>
          <tr>
            <th>수용회선명</th>
            <td colspan="3">
              <el-input
                v-model="resultVo.sconnalias"
                type="text"
                size="mini"
                maxlength="1000"
              />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-edit" round @click="handleClickSubmit">등록</el-button> <!-- btnUpdateLinkInfo -->
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
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

const routeName = 'ModalIpLinkMstInsert'

export default {
  name: routeName,
  components: { ModalOrgSearch },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      fnType: 'insert',
      defaultResultVo: {
        nipLinkMstSeq: '',
        pifSerialIp: '',
        insertPifSerialIp: '',
        insertNbitMask: '/30',
        saofficescodeNm: '',
        szofficescodeNm: '',
        saofficescode: '',
        szofficescode: '',
        sanealias: '',
        sznealias: '',
        samstip: '',
        szmstip: '',
        saifname: '',
        szifname: '',
        ssaid: '',
        sllnum: '',
        sconnalias: ''
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
      if (model.dataReset) {
        this.resultVo = this._cloneDeep(this.defaultResultVo)
      }
      if (model?.fnType) {
        this.fnType = model.fnType
        if (this.fnType === 'update' && model?.nipLinkMstSeq) {
          this.fnViewUpdateIPLinkMst(model.nipLinkMstSeq)
        }
      }
    },
    onClose() {
    },
    onChangeInput(val) {
      this.$set(this.resultVo, 'insertPifSerialIp', val.replace(/[^0-9.]+/g, ''))
    },
    async fnViewUpdateIPLinkMst(nipLinkMstSeq) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewUpdateIPLinkMst, { nipLinkMstSeq })
        this.resultVo = res.result.data
      } catch (error) {
       this.error(error)
      }
    },
    fnSearchOfficeList(type /* a or z */) { // 수용국 검색 같은 modal 사용
      this.$refs.ModalOrgSearch.open({ type, viewTitle: '수용국' })
    },
    setSelectedRow({ row, type }) {
      this.selectedLvl = row
      const setNmKey = type === 'a' ? 'saofficescodeNm' : 'szofficescodeNm'
      const setCodeKey = type === 'a' ? 'saofficescode' : 'szofficescode'
      this.$set(this.resultVo, setNmKey, row.slvlNm)
      this.$set(this.resultVo, setCodeKey, row.slvlCd)
    },
    handleClickSubmit() {
      if (this.fnType === 'insert') {
        this.fnInsertSubmit()
      } else {
        this.fnUpdateSubmit()
      }
    },
    async fnInsertSubmit() {
      const validate = this.getCheckValidate()
      if (!validate) return
      try {
        // ipmgmt/linkmgmt/insertLinkIPMst.json
        const res = await apiRequestJson(ipmsJsonApis.insertLinkIPMst, this.getParameter())
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '운용정보를  정상적으로 등록 하였습니다.')
          this.resultVo = this._cloneDeep(this.defaultResultVo)
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    async fnUpdateSubmit() {
      const validate = this.getCheckValidate()
      if (!validate) return

      this.confirm(`해당 정보로 할당 테이블에 ${this.resultVo.allocCnt}건이 링크 할당이 되어 있습니다. 운용정보 변경시 할당정보도 변경이 됩니다. 운용정보를 변경 하시겠습니까?`, '확인', {
        cancelButtonText: '취소',
        confirmButtonText: '확인',
      }).then(async() => {
        try {
        // ipmgmt/linkmgmt/updateLinkIPMst.json
          const res = await apiRequestJson(ipmsJsonApis.updateLinkIPMst, this.getParameter())
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
        }
      })
      .catch(action => {
      })
    },
    getCheckValidate() {
      let emptyText = ''
      const { insertPifSerialIp, insertNbitMask, saofficescode, sanealias, samstip, saifname } = this.resultVo

      if (this.fnType === 'insert' && (insertPifSerialIp === null || insertPifSerialIp === '')) {
        emptyText = '링크IP블록'
      } else if (this.fnType === 'insert' && (insertNbitMask === null || insertNbitMask === '')) {
        emptyText = '비트마스크'
      } else if (saofficescode === null || saofficescode === '') {
        emptyText = '자국수용국'
      } else if (sanealias === null || sanealias === '') {
        emptyText = '자국장비명'
      } else if (samstip === null || samstip === '') {
        emptyText = '자국장비IP'
      } else if (saifname === null || saifname === '') {
        emptyText = '자국IF명'
      }

      if (emptyText !== '') {
        onMessagePopup(this, `${emptyText}을/를 ${emptyText === '비트마스크' ? '선택하세요' : '입력하세요'}`)
        return false
      }
      return true
    },
    getParameter() {
      const fnType = this.fnType
      const {
        nipLinkMstSeq,
        insertPifSerialIp,
        insertNbitMask,
        saofficescode,
        sanealias,
        samstip,
        saifname,
        sznealias,
        szmstip,
        szifname,
        szofficescode,
        ssaid,
        sllnum,
        sconnalias
      } = this.resultVo
      // const userId = this.$store.state.user.info.suserId
      const tbIpLinkMstVo = {
        sanealias: sanealias ?? '',
        samstip: samstip ?? '',
        saifname: saifname ?? '',
        // salocationcode: '',
        saofficescode: saofficescode ?? '',
        sznealias: sznealias ?? '',
        szmstip: szmstip ?? '',
        szifname: szifname ?? '',
        // szlocationcode: '',
        szofficescode: szofficescode ?? '',
        ssaid: ssaid ?? '',
        sllnum: sllnum ?? '',
        sconnalias: sconnalias ?? '',
        screateId: this.$store.state.user.info.suserId,
        smodifyId: this.$store.state.user.info.suserId
      }
      if (fnType === 'insert') {
        Object.assign(tbIpLinkMstVo, { pifSerialIp: insertPifSerialIp + insertNbitMask })
      } else if (fnType === 'update') {
        Object.assign(tbIpLinkMstVo, { nipLinkMstSeq })
      }
      return tbIpLinkMstVo
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
