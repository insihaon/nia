<template>
  <div :class="{ [name]: true }">
    <div class="w-full h-full d-flex">
      <component :is="componentLoader" :selected-ticket="selectedRow" :is-modal="true" :is-show-hist="isShowHist" class="h-full" />
    </div>
    <el-row>
      <el-col align="right">
        <el-button size="mini" type="info" plain class="btn-r" @click.native="isShowHist = !isShowHist"> 이력 {{ isShowHist ? '닫기' : '보기' }} </el-button>
        <el-button size="mini" type="info" class="close-btn" icon="el-icon-close" @click.native="$emit('windowClose')">
          {{ $t('exit') }}
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import snapshotHistory from '@/views-nia/dataSnapshot/snapshotHistory.vue'

import _ from 'lodash'

const routeName = 'snapShot'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, snapshotHistory },
  directives: { elDragDialog },
  extends: Modal,
  props: {
    wdata: Object,
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      visible: false,
      containerLoading: false,
      selectedRow: null,
      isShowHist: false,
    }
  },
  computed: {
    componentLoader() {
      return snapshotHistory
    },
  },
  watch: {
    isShowHist(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.$set(this.wdata, 'width', newVal ? 1200 : 600)
        this.$set(this.wdata, 'height', newVal ? 600 : 280)
        // this.wdata.width = newVal ? 1200 : 600
        // this.domElement.maxWidth = newVal ? 1200 : 600

        let x = (window.innerWidth - this.wdata.width) * 0.5 + (this.$store.getters.windows.length - 1) * 20
        let y = (window.innerHeight - this.wdata.height) * 0.5 + (this.$store.getters.windows.length - 1) * 20

        if (x < 0) {
          x = 15
        }
        if (y < 0) {
          y = 85
        }
        this.$set(this.wdata, 'x', x)
        this.$set(this.wdata, 'y', y)
      }
    },
  },
  created () {
    this.selectedRow = this.wdata.params
  },
  methods: {
  },
}
</script>

<style lang="scss" scoped></style>
