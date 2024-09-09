<template>
  <div class="profile-container">
    <div class="list">
      <el-tag
        v-for="item in profileList"
        :key="item.name"
        closable
        :type="ipms.curProfileByVue[propName] === item.name ? 'danger': 'info'"
        @click="handleClickApply(item)"
        @close="handleClickRemove(item.name)"
      >
        {{ item.name }}
      </el-tag>
    </div>
    <div class="ml-1">
      <el-button icon="el-icon-document-add" size="mini" @click="handleClickSave">프로파일 저장</el-button>
    </div>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import Eventbus from '@/utils/event-bus'
import { EventType } from '@/min/types'
import { onMessagePopup } from '@/utils/index'

const routeName = 'SearchConditionSaver'

export default {
  name: routeName,
  extends: Base,
  props: {
    propName: {
      type: String,
      default: ''
    },
    parameter: {
      type: Object,
      default() { return {} }
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      profileList: []
    }
  },
  mounted () {
    this.initialization()
  },
  methods: {
    initialization() {
      const name = this.propName
      if (name) {
        this.$set(this, 'profileList', JSON.parse(window.localStorage['savedSearchCondition'] || '{}')[name] ?? [])
      }
    },
    async handleClickSave() {
      let savedProfile
      const name = this.propName
      if (name) {
        savedProfile = JSON.parse(window.localStorage['savedSearchCondition'] || '{}')
      }
      const currentViewProfile = savedProfile[name] ? savedProfile[name] : []
      if (currentViewProfile.length >= 3) {
        onMessagePopup(this, '3개 이상 저장할 수 없습니다.')
        return
      }
      const result = await this.prompt('저장할 프로파일명을 입력하세요.', '현재 검색조건 저장')
      if (result.action === 'confirm') {
        if (result.value === null || result.value === '' || result.value.trim().length === 0) {
          onMessagePopup(this, '저장할 프로파일명이 빈 값입니다.')
          return
        }
        if (currentViewProfile.findIndex(d => d.name === result.value) !== -1) {
          onMessagePopup(this, '동일한 프로파일명이 존재합니다.')
          return
        }
        currentViewProfile.push({ name: result.value, parameter: this.parameter })
        savedProfile[name] = currentViewProfile
        window.localStorage['savedSearchCondition'] = JSON.stringify(savedProfile)
        this.initialization()
      }
    },
    handleClickApply(profile) {
      this.$store.dispatch('ipms/setCurProfileByVue', { key: this.propName, profileName: profile.name })
      const parameter = this._cloneDeep(profile.parameter)
      Eventbus.$emit(EventType.setSavedParameter, parameter)
    },
    handleClickRemove(profileName) {
      let savedProfile
      const name = this.propName
      if (name) {
        savedProfile = JSON.parse(window.localStorage['savedSearchCondition'] || '{}')
      }
      if (savedProfile[name]) {
        const removeIdx = savedProfile[name].findIndex(v => v.name === profileName)
        if (removeIdx !== -1) {
          savedProfile[name].splice(removeIdx, 1)
        }
        window.localStorage['savedSearchCondition'] = JSON.stringify(savedProfile)
        this.initialization()
      }
    }
  },
}
</script>
<style lang="scss" scoped>
.el-tag {
  cursor: pointer;
}
.profile-container {
  display: flex;
}
</style>
