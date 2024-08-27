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
        업로드 상세
        <hr>
      </span>
      <div id="content" class="layer">
        <div class="content_result mt0">
          <form id="inputForm" name="inputForm">
            <h4>상세</h4>
            <table class="tbl_list mt5">
              <colgroup>
                <col width="15%" />
                <col width="70%" />
                <col width="15%" />
              </colgroup>
              <thead>
                <tr>
                  <th class="first" scope="col">No</th>
                  <th scope="col">PIP PREFIX</th>
                  <th scope="col">성공 여부</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="resultVo.totalCount === 0" class="subbg last">
                  <td class="first" colspan="5">조회된 결과 목록이 존재하지 않습니다.</td>
                </tr>
                <template v-else>
                  <tr v-for="(item, index) in resultVo.tbIpUploadSubVos" :key="index">
                    <td class="first ellipsis" :title="item.rnum">{{ item.rnum }}</td>
                    <td>{{ item.pipPrefix }}</td>
                    <td>{{ item.ssuccessYn }}</td>
                  </tr>
                </template>
              </tbody>
            </table>
          </form>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-close" @click.native="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalUploadDetail'

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
        totalCount: 0,
        tbIpUploadSubVos: []
      }
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      if (model.seq) {
        this.fnViewDetailIpMst(model.seq)
      }
    },
    onClose() { },
    async fnViewDetailIpMst(seq) {
      // 3계위까지 선택했을 때 요청 / 3계위 없으면 disable
      try {
        const res = await apiRequestModel(ipmsModelApis.viewDetailIpMst, { seq })
        this.$set(this.resultVo, 'tbIpUploadSubVos', res?.result?.data?.tbIpUploadSubVos ?? [])
        this.$set(this.resultVo, 'totalCount', res?.result?.data?.totalCount ?? 0)
      } catch (error) {
        this.error(error)
      }
    },
    handleFileUpload() {}
  },
}
</script>
<style lang="scss" scoped>
</style>
