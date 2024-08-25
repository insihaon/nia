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
            <colgroup>
              <col width="15%" />
              <col width="85%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">장비명</th>
                <td>
                  <input
                    id="insertShostNm"
                    v-model="resultVo.insertShostNm"
                    type="text"
                    class="txt w98"
                    name="insertShostNm"
                    title="장비명"
                    maxlength="25"
                  />
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">수용국</th>
                <td>
                  <div class="search w99">
                    <el-input
                      v-model="resultVo.insertOfficeNm"
                      size="mini"
                      readonly
                    >
                      <template #suffix>
                        <el-button
                          size="mini"
                          icon="el-icon-search"
                          class="font-weight-bolder"
                          style="font-size: larger;border: none;padding: 0;top: -6px;position: relative;"
                          @click="fnSearchOfficeList()"
                        />
                      </template>
                    </el-input>
                  </div>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">IP 정보</th>
                <td class="d-flex">
                  <el-select id="insertIpVersion" v-model="resultVo.insertIpVersion" class="w15" name="insertIpVersion" @change="onChangeIpVersion">
                    <el-option
                      v-for="item in [{ code: 'CV0001', name: 'IPv4' }, { code: 'CV0002', name: 'IPv6' }]"
                      v-if="item.code !== 'CV0000'"
                      :key="item.code"
                      :value="item.code"
                      :label="item.name"
                    />
                  </el-select>

                  <input
                    id="insertIpHostInet"
                    v-model="resultVo.insertIpHostInet"
                    type="text"
                    class="txt w69"
                    name="insertIpHostInet"
                    title="IP 주소 입력창"
                    maxlength="43"
                  />

                  <input
                    id="sBitMask"
                    v-model="resultVo.sBitMask"
                    type="text"
                    class="txt w10"
                    name="sBitMask"
                    title="BitMask"
                    maxlength="43"
                    style="width: 80px"
                    disabled="disabled"
                  />
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">모델명</th>
                <td>
                  <input
                    id="insertModelNm"
                    v-model="resultVo.insertModelNm"
                    type="text"
                    class="txt w98"
                    name="insertModelNm"
                    title="모델명"
                    maxlength="30"
                  />
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">용도</th>
                <td>
                  <input
                    id="insertComment"
                    v-model="resultVo.insertComment"
                    type="text"
                    class="txt w98"
                    name="insertComment"
                    title="용도"
                    maxlength="1000"
                  />
                </td>
              </tr>
            </tbody>
          </table>

          <!-- Buttons Section -->
          <!-- <div class="btn_area mt10">
            <span>
              <a href="#none">
                <input
                  type="image"
                  :src="getImageUrl('/resources/images/content/btn_save_off.gif')"
                  value="등록"
                  name="btnInsertSubmit"
                  id="btnInsertHostSubmit"
                  @mouseover="menuOver"
                  @mouseout="menuOut"
                  @click="handleSubmit"
                />
              </a>
            </span>
            <span>
              <a href="#none">
                <input
                  type="image"
                  :src="getImageUrl('/resources/images/content/btn_close_off.gif')"
                  value="닫기"
                  name="btnInsertHostCls"
                  id="btnInsertHostCls"
                  @mouseover="menuOver"
                  @mouseout="menuOut"
                  @click="handleClose"
                />
              </a>
            </span>
          </div> -->
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
      // const userId = this.$store.state.user.info.Uid
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
        // screateId: userId,
        // smodifyId: userId
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
  top: 8px;
}
</style>
