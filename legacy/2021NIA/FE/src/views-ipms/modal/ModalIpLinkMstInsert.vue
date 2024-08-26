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
        운용정보 등록
        <hr>
      </span>
      <div id="content" class="layer">
        <div class="content_result mt0">
          <table class="tbl_data entry">
            <caption>링크 정보</caption>
            <colgroup>
              <col width="20%" />
              <col width="27%" />
              <col width="23%" />
              <col width="30%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">링크IP블록</th>
                <td v-if="fnType === 'insert'" colspan="3" class="d-flex">
                  <el-input
                    v-model="resultVo.insertPifSerialIp"
                    type="text"
                    class="txt w30"
                    maxlength="43"
                    title="IP 주소 입력창"
                    @input="onChangeInput"
                  />
                  <select v-model="resultVo.insertNbitMask" class="w15">
                    <option value="/30">/30</option>
                    <option value="/29">/29</option>
                  </select>
                </td>
                <td v-else>
                  {{ resultVo.pifSerialIp }}
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">자국 수용국</th>
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
                        style="font-size: larger;border: none"
                        @click="fnSearchOfficeList('a')"
                      />
                    </template>
                  </el-input>
                </td>
                <th scope="row">대국 수용국</th>
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
                        style="font-size: larger;border: none"
                        @click="fnSearchOfficeList('z')"
                      />
                    </template>
                  </el-input>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">자국 장비명</th>
                <td>
                  <input
                    v-model="resultVo.sanealias"
                    type="text"
                    class="txt w97"
                    maxlength="200"
                    title="장비명"
                  />
                </td>
                <th scope="row">대국 장비명</th>
                <td>
                  <input
                    v-model="resultVo.sznealias"
                    type="text"
                    class="txt w97"
                    maxlength="200"
                    title="장비명"
                  />
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">자국 장비IP</th>
                <td>
                  <input
                    v-model="resultVo.samstip"
                    type="text"
                    class="txt w97"
                    maxlength="43"
                  />
                </td>
                <th scope="row">대국 장비IP</th>
                <td>
                  <input
                    v-model="resultVo.szmstip"
                    type="text"
                    class="txt w97"
                    maxlength="43"
                  />
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">자국 IF명</th>
                <td>
                  <input
                    v-model="resultVo.saifname"
                    type="text"
                    class="txt w97"
                    maxlength="300"
                  />
                </td>
                <th scope="row">대국 IF명</th>
                <td>
                  <input
                    v-model="resultVo.szifname"
                    type="text"
                    class="txt w97"
                    maxlength="300"
                  />
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">SAID</th>
                <td>
                  <input
                    v-model="resultVo.ssaid"
                    type="text"
                    class="txt w97"
                    maxlength="30"
                  />
                </td>
                <th scope="row">전용번호</th>
                <td>
                  <input
                    v-model="resultVo.sllnum"
                    type="text"
                    class="txt w97"
                    maxlength="30"
                  />
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">수용회선명</th>
                <td colspan="3">
                  <input
                    v-model="resultVo.sconnalias"
                    type="text"
                    class="txt w99"
                    maxlength="1000"
                  />
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-edit" @click="handleClickSubmit">등록</el-button> <!-- btnUpdateLinkInfo -->
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
      this.$set(this.resultVo, 'insertPifSerialIp', val.replace(/\D/g, ''))
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
      // const userId = this.$store.state.user.info.Uid
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
        sconnalias: sconnalias ?? ''
        // screateId: userId,
        // smodifyId: userId
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
