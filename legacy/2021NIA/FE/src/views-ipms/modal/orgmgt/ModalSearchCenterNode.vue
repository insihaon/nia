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
        센터/지역본부 검색
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <table class="tbl_data entry" summary="조회조건선택">
            <caption>조회조건선택</caption>
            <colgroup>
              <col width="25%" /><col width="75%" />
            </colgroup>
            <tbody>
              <tr class="top last">
                <th class="first" scope="row">센터/지역본부명</th>
                <td>
                  <el-input v-model="searchLvlNm" type="text" class="txt w97" />
                </td>
              </tr>
            </tbody>
          </table>
          <div class="btn_area mt5">
            <el-button icon="el-icon-search" size="mini" @click="fnSelectOrgBas()">
              {{ $t('조회') }}
            </el-button>
          </div>
        </div>
        <div class="content_result">
          <h4>센터/지역본부  조회결과</h4>
          <table id="baseTable" class="tbl_list mt5" summary="목록">
            <caption>목록</caption>
            <colgroup>
              <col width="10%" /><col width="18%" /><col width="72%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col">선택</th>
                <th scope="col">센터/지역본부 ID</th>
                <th scope="col">센터/지역본부 명</th>
                <th scope="col" style="display: none">센터/지역본부 명</th>
              </tr>
            </thead>
            <tbody>
              <tr class="subbg last">
                <td class="first" colspan="3">조회된 결과 목록이 존재하지 않습니다.</td>
              </tr>
            </tbody>
          </table>

          <table id="dummyTable" class="tbl_list mt5" summary="목록" style="display: none;">
            <caption>목록</caption>
            <colgroup>
              <col width="10%" /><col width="18%" /><col width="72%" />
            </colgroup>
            <tbody>
              <tr>
                <td class="first"><input type="radio" class="radio" name="radioSelectOrg" onclick="fnselectSearchLvlCd(this);"></td>
                <td class="ellipsis" title=""></td>
                <td class="left ellipsis" title=""></td>
                <td class="ellipsis" title="" style="display: none"></td>
              </tr>
            </tbody>
          </table>
        </div>

      </div>

      <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click="fnSelectOrgBas()">
          {{ $t('선택') }}
        </el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import _ from 'lodash'

const routeName = 'ModalSearchCenterNode'

export default {

  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      searchLvlNm: ''
    }
  },
  computed: {

  },
  mounted() {

  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 600
    },
    onOpen(model, actionMode) {

    },
    fnSelectOrgBas() {

    },
    onClose() {
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
