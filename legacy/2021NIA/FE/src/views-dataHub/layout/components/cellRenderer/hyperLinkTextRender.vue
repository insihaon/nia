<template>
  <a>
    <div class="cell-container" :class="{ [name]: true }">
      <el-popover
        v-if="params.type === 'reject'"
        ref="popover"
        placement="bottom"
        trigger="click"
        width="500"
      >
        <el-table :data="rowData">
          <el-table-column width="300" property="refuse_msg" label="반려사유" />
        </el-table>
      </el-popover>
      <a v-popover:popover :class="valueCheck" @click="openModal(params)">
        {{ params.value }}
      </a>
    </div>
    <a :class="valueCheck" @click="openModal(params)">
      {{ params.value }}
    </a>
    <!-- <ModalAddDetailColumn ref="ModalAddDetailColumn" /> -->
    <ModalRejectReason ref="ModalRejectReason" />
  </a>
</template>

<script>
import ModalAddDetailColumn from '@/views-dataHub/modal/ModalAddDetailColumn'
import ModalRejectReason from '@/views-dataHub/modal/ModalRejectReason'
const routeName = 'hyperLinkTextRender'

export default {
  name: routeName,
  components: { ModalRejectReason },
  data() {
    return {
      name: routeName,
      isHyperText: false,
      rowData: []
    }
  },
  computed: {
    valueCheck() {
      if (this.params.value === '반려' || this.params.type === 'detail') {
       return 'hyper-text'
      } else {
        return 'hyper-text-none'
      }
    }
  },
  methods: {
    openModal(params) {
      const value = params?.value
      if (value === '승인' || value === '신청') {
        return false
      }
      params.action(params.type, params.data)
      this.rowData = [params?.data]
    }
  }

}
</script>

<style lang="scss" scope>
    .hyper-text{
      color: rgb(96 96 241);
      cursor: pointer;
      // text-decoration-line: underline;
      font-size: 12px;
    }

    .hyper-text-none{
      cursor: no-drop;
      color: #000000;
    }

</style>

