<template>
  <div :class="{ [name]: true }">
    <el-dialog
      v-if="animationVisible"
      v-el-drag-dialog
      :visible.sync="visible"
      :width="domElement.maxWidth + `px`"
      :height="domElement.minHeight + `px`"
      :fullscreen.sync="fullscreen"
      :modal-append-to-body="false"
      :append-to-body="true"
      :modal="modal"
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="nia-edit-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-check mr-2 text-base" />
        포트 수정 및 삭제
        <hr>
      </span>

      <div class="des-class mr-2">
        <table class="basic">
          <tr>
            <th>I/F ID</th>
            <td class="disable">
              {{ selectedRow.if_id }}
            </td>
            <th>I/F명</th>
            <td class="disable">
              {{ selectedRow.if_nm }}
            </td>
          </tr>
          <tr>
            <th>I/F 타입</th>
            <td class="disable">
              {{ selectedRow.if_type }}
            </td>
            <th>IP 주소</th>
            <td class="disable">
              {{ selectedRow.ip_addr }}
            </td>
          </tr>
          <tr>
            <th>대역폭</th>
            <td class="disable">
              {{ selectedRow.if_speed }}
            </td>
            <th>사용여부</th>
            <td class="disable">
              <el-select v-model="selectedRow.if_oper_status_info ">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </td>
          </tr>
        </table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" plain @click.native="close()">
          {{ '수정' }}
        </el-button>
        <el-button size="small" plain class="exit-btn" @click.native="close()">
          {{ $t('exit') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import _ from 'lodash'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import CellRenderDataSetButtons from '@/views-dataHub/components/cellRenderer/CellRenderDataSetButtons'

const routeName = 'ModalPortEdit'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CellRenderDataSetButtons, CompInquiryPannel },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      nodeList: [],
      selectedRow: null,
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 100, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
      },
      options: [{
          value: 'up',
          label: '사용'
        }, {
          value: 'down',
          label: '미사용'
        }],
    }
  },
  computed: {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.maxHeight = 1600
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.selectedRow = model?.row
    },
    onChangePage(curPage) {
      this.paginationInfo.currentPage = curPage
      this.onLoadNodeList()
    },
    onClose() { /* for Override */ },
    }

  }
</script>
