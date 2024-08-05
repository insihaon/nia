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
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        라우팅 중복 상세정보
        <hr>
      </span>
      <div id="content" class="layer">
        <div id="div01" class="content_result mt0">
          <div class="tit_group">
            <h4 class="mt5">라우팅 중복 상세정보</h4>
          </div>
          <table id="contentTable" class="tbl_list mt5" summary="조회결과">
            <caption>조회결과</caption>
            <colgroup>
              <col width="8%" />
              <col width="9%" />
              <col width="9%" />
              <col width="9%" />
              <col width="9%" />
              <col width="9%" />
              <col width="15%" />
            </colgroup>
            <thead>
              <tr>
                <th scope="col">서비스망</th>
                <th scope="col">본부</th>
                <th scope="col">주노드</th>
                <th scope="col">노드</th>
                <th scope="col">IP블록</th>
                <th scope="col">As-Path</th>
                <th scope="col">Community</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="resultListVo.totalCount === 0" class="subbg last">
                <td class="first" colspan="7">조회된 결과 목록이 존재하지 않습니다.</td>
              </tr>
              <template v-else>
                <tr v-for="(item, index) in resultListVo.tbIpAssignMstVos" :key="index" :class="[{ last: index === resultListVo.tbIpAssignMstVos.length - 1 }, { subbg: index % 2 !== 0 }]">
                  <td class="ellipsis" :title="item.ssvcLineTypeNm">{{ item.ssvcLineTypeNm }}</td>
                  <td class="ellipsis" :title="item.ssvcGroupNm">{{ item.ssvcGroupNm }}</td>
                  <td class="ellipsis" :title="item.ssvcHighNm">{{ item.ssvcHighNm }}</td>
                  <td class="ellipsis" :title="item.ssvcObjNm">{{ item.ssvcObjNm }}</td>
                  <td class="ellipsis" :title="item.pipPrefix">{{ item.pipPrefix }}</td>
                  <td class="ellipsis" :title="item.asPath">{{ item.asPath }}</td>
                  <td class="ellipsis" :title="item.scommunity">{{ item.scommunity }}</td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>
        <!-- <div class="btn_area mt10">
          <a href="#">
            <input type="image" :src="closeButtonImageSrc" value="닫기" id="btnIpAllocDetCls" @mouseover="menuOver" @mouseout="menuOut" />
          </a>
        </div> -->
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" icon="el-icon-menu">병합</el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">
          닫기
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'

const routeName = 'ModalDetailSummary'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      resultListVo: {
        totalCount: 0,
        tbIpAssignMstVos: []
      },
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      if (model.row) {
        const tbIpAssignMstVo = {
          ssvcLineTypeCd: model.row.ssvcLineTypeCd,
          nipAssignMstSeq: model.row.nipAssignMstSeq
        }
        this.loadData(tbIpAssignMstVo)
      }
    },
    onClose() {
    },
    menuOver(event) {
      // 메뉴 오버 이벤트 처리
    },
    menuOut(event) {
      // 메뉴 아웃 이벤트 처리
    },
    loadData(tbIpAssignMstVo) {
      /*
      // url: 'ipmgmt/assignmgmt/viewDetailSummary.model'
        try {
          const res = await api(tbIpAssignMstVo)
          this.resultListVo = res.resultListVo
        } catch (error) {
          this.error(error)
        }
      */
      // 서버에서 데이터 로드 (예: axios 사용)
      // axios.get('/api/data').then(response => {
      //   this.resultListVo = response.data.resultListVo;
      // });
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
