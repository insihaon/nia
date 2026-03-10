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
      <table>
        <colgroup>
          <col width="15%" />
          <col width="85%" />
        </colgroup>
        <tbody>
          <tr>
            <th>장비명</th>
            <td>
              <el-input
                v-model="resultVo.insertShostNm"
                type="text"
                size="mini"
                name="insertShostNm"
                title="장비명"
                maxlength="25"
              />
            </td>
          </tr>
          <tr>
            <th>수용국</th>
            <td class="textflex">
              <el-input
                v-model="resultVo.insertOfficeNm"
                size="mini"
                readonly
              >
                <template #suffix>
                  <el-button
                    size="mini"
                    icon="el-icon-search"
                    style="background: none;border: unset;"
                    @click="fnSearchOfficeList()"
                  />
                </template>
              </el-input>
            </td>
          </tr>
          <tr>
            <th>IP 정보</th>
            <td class="textflex">
              <el-select v-model="resultVo.insertIpVersion" size="mini" @change="onChangeIpVersion">
                <template v-if="item.code !== 'CV0000'">
                  <el-option
                    v-for="item in [{ code: 'CV0001', name: 'IPv4' }, { code: 'CV0002', name: 'IPv6' }]"
                    :key="item.code"
                    :value="item.code"
                    :label="item.name"
                  />
                </template>
              </el-select>
              <el-input
                v-model="resultVo.insertIpHostInet"
                type="text"
                size="mini"
                name="insertIpHostInet"
                title="IP 주소 입력창"
                maxlength="43"
              />
              <el-input
                v-model="resultVo.sBitMask"
                type="text"
                size="mini"
                name="sBitMask"
                title="BitMask"
                maxlength="43"
                style="width: 80px"
                disabled="disabled"
              />
            </td>
          </tr>
          <tr>
            <th>모델명</th>
            <td>
              <el-input
                v-model="resultVo.insertModelNm"
                type="text"
                size="mini"
                name="insertModelNm"
                maxlength="30"
              />
            </td>
          </tr>
          <tr>
            <th>용도</th>
            <td>
              <el-input
                v-model="resultVo.insertComment"
                type="text"
                size="mini"
                class="txt w98"
                name="insertComment"
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

const routeName = 'ModalIpHostMstInsert'

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
        insertShostNm: '',
        insertOfficeCd: '',
        insertOfficeNm: '',
        insertIpVersion: 'CV0001',
        insertIpHostInet: '',
        sBitMask: '/32',
        insertModelNm: '',
        insertComment: ''
      },
      resultVo: {},
      selectedLvl: null
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 800
    },
    onOpen(model, actionMode) {
      this.resultVo = this._cloneDeep(this.defaultResultVo)
    },
    onClose() {
    },
    async fnViewInsertHostIPMst(nipLinkMstSeq) {
      try {
        const res = await apiRequestModel(ipmsModelApis.insertHostIPMst, { nipLinkMstSeq })
        this.resultVo = res.result.data
      } catch (error) {
       this.error(error)
      }
    },
    fnSearchOfficeList() { // 수용국 검색 같은 modal 사용
      this.$refs.ModalOrgSearch.open({ viewTitle: '수용국' })
    },
    setSelectedRow({ row, type }) {
      this.selectedLvl = row
      this.$set(this.resultVo, 'insertOfficeNm', row.slvlNm)
      this.$set(this.resultVo, 'insertOfficeCd', row.slvlCd)
    },
    handleClickSubmit() {
      if (this.fnType === 'insert') {
        this.fnInsertSubmit()
      } else {
        this.fnUpdateSubmit()
      }
    },
    onChangeIpVersion(version) {
      if (version === 'CV0001') {
        this.$set(this.resultVo, 'sBitMask', '/32')
      } else if (version === 'CV0002') {
        this.$set(this.resultVo, 'sBitMask', '/128')
      }
    },
    async fnInsertSubmit() {
      const { insertShostNm, insertIpHostInet, insertOfficeCd, sBitMask, insertModelNm, insertComment, insertIpVersion } = this.resultVo
      // const userId = this.$store.state.user.info.suserId
      if (insertShostNm === null || insertShostNm === '') {
        onMessagePopup(this, '호스트 명을 입력하세요.')
        return
      }
      if (insertIpHostInet === null || insertIpHostInet === '') {
        onMessagePopup(this, 'IP를 입력하세요.')
        return
      }
      const tbIpHostMstVo = {
        sipHostTypeCd: 'HT0010',
        sprorityYn: 'Y',
        sipHostNm: insertShostNm,
        srssofficescode: insertOfficeCd,
        sipBlock: insertIpHostInet,
        snetmask: sBitMask,
        pipHostInet: insertIpHostInet + sBitMask,
        pipPrefix: insertIpHostInet + sBitMask,
        smodelname: insertModelNm,
        scomment: insertComment,
        sipVersionTypeCd: insertIpVersion,
        screateId: this.$store.state.user.info.suserId,
        smodifyId: this.$store.state.user.info.suserId
      }
      try {
        const res = await apiRequestJson(ipmsJsonApis.insertHostIPMst, tbIpHostMstVo)
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
  },
}
</script>
<style lang="scss" scoped>
</style>
