<template>
  <div :class="{ [name]: true, 'w-full h-full': true }">
    <ul class="city-buttons">
      <li
        v-for="key in Object.keys(systemMonitoringMap)"
        v-show="!systemMonitoringMap[key].hide"
        :key="key"
      >
        <div class="atom">
          <label>
            {{ key }}
          </label>
          <input
            type="checkbox"
            :value="systemMonitoringMap[key].show"
            :checked="systemMonitoringMap[key].show"
            @input="switchState(key)"
          >
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { mapState } from 'vuex'

const routeName = 'systemMonitoringFilter'

export default {
  name: routeName,
  components: { },
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
    }
  },
  computed: {
    ...mapState({
      systemMonitoringMap: state => state.systemMonitoring.systemMonitoringMap
    })
  },
  watch: {
  },
  created () {
    this.selectedRow = this.wdata.params
  },
  methods: {
    switchState(key) {
      this.$store.dispatch('systemMonitoring/switchShowState', key)
    }
  },
}
</script>

<style lang=scss scoped>

.systemMonitoringFilter{
  caret-color: transparent; /* 깜빡이는 커서 숨김 */
}

.city-buttons {
  list-style: none;
  padding: 1rem;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;

  li {
    background: #f8fafc;
    border-radius: 0.5rem;
    padding: 0.75rem;
    transition: all 0.2s ease;

    &:hover {
      background: #f1f5f9;
      transform: translateY(-1px);
    }
  }
}

.atom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
}

label {
  font-size: 0.875rem;
  font-weight: 500;
  color: #334155;
  flex: 1;
}

input[type="checkbox"] {
  --input-width: 2.5rem;
  --active-color: rgb(37 99 235);
  --circle-margin: 0.2rem;
  --circle-size: 1.125rem;
  cursor: pointer;
  appearance: none;
  position: relative;
  background-color: rgb(229 231 235 / 1);
  border-radius: 100em;
  width: var(--input-width);
  height: 1.25rem;
  transition: all 0.2s ease;

  &::before {
    content: "";
    position: absolute;
    left: var(--circle-margin);
    top: 50%;
    transform: translateY(-50%);
    width: var(--circle-size);
    height: var(--circle-size);
    border-radius: 50%;
    background-color: white;
    transition: all 0.2s ease;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  }

  &:checked {
    background-color: var(--active-color);
    &::before {
      left: calc(100% - var(--circle-size) - var(--circle-margin));
    }
  }

  &:hover {
    opacity: 0.9;
  }
}
</style>
