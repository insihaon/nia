<template>
  <div :class="{ [name]: true, 'w-full h-full': true }">
    <div class="d-flex">
      <component :is="componentLoader" :selected-ticket="selectedRow" :is-modal="true" :is-show-hist="isShowHist" class="h-full" />
    </div>
    <el-row>
      <el-col align="right" >
        <div class="my-4 mx-4">
          <!-- <el-button size="small" plain class="btn-r" @click.native="onClickShowHist()"> 이력 {{ isShowHist ? '닫기' : '보기' }} </el-button> -->
          <el-button size="mini" type="info" plain class="btn-r" @click.native="isShowHist = !isShowHist"> 이력 {{ isShowHist ? '닫기' : '보기' }} </el-button>
          <el-button size="mini" type="info" icon="el-icon-close" @click.native="$emit('windowClose')">
            {{ $t('exit') }}
          </el-button>
        </div>
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
    wdata: {
      type: Object,
      default() {
        return {}
      }
    },
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
        this.$set(this.wdata, 'width', newVal ? 700 : 600)
        this.$set(this.wdata, 'height', newVal ? 650 : 300)

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
    onClickShowHist() {
      this.alert('이력 페이지로 이동합니다.')
      this.$router.push({ path: '/dataSnapshot/snapshotHistory' })
    }
  },
}
</script>

<style lang="scss" scoped></style>
