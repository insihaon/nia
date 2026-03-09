import { deepClone } from '@/utils'

export const defaultNescode = 'checkToggle_defaultCode'

export function findTagIndex(state, tagData) {
  const existTagList = state.influenceCircitTagData.tagList
  const existFindedTagIndex = existTagList?.findIndex((tag) => {
    return tag.tagData.nescode === tagData.nescode
  })
  return existFindedTagIndex
}

export function checkDefault(state) {
  if (state.influenceCircitTagData.tagList.length === 1) {
    if (state.influenceCircitTagData.tagList[0].tagData.nescode === defaultNescode) {
      return true
    }
  }
  return false
}

const state = {
  influenceCircitTagData: {
    currentTagCode: defaultNescode,
    tagList: [
      {
        tagData: {
          nealias: '장치 미정',
          nescode: defaultNescode,
        }
      }
    ]
  }
}

const mutations = {
  async DELETE_INFLUENCECIRCIT_PAGE_TAG(state, deleteTagIndex) {
    state.influenceCircitTagData.tagList.splice(deleteTagIndex, 1)
    state.influenceCircitTagData.tagList = deepClone(state.influenceCircitTagData.tagList)
    return deleteTagIndex
  },

  SET_TAG_CURRENT_CODE(state, code) {
    state.influenceCircitTagData.currentTagCode = code
  },

  SAVE_INFLUENCECIRCIT_PAGE_TAG_DATA(state, tagData) {
    state.influenceCircitTagData.tagList.push({ tagData: tagData })
    state.influenceCircitTagData.currentTagCode = tagData.nescode
  },

  EDIT_INFLUENCECIRCIT_PAGE_TAG_DATA(state, map) {
    state.influenceCircitTagData.tagList[map.tagIndex].tagData = map.tagData
    state.influenceCircitTagData.tagList = deepClone(state.influenceCircitTagData.tagList)
  },

  RESET_PAGE_TAG_DATA(state) {
    state.influenceCircitTagData = {
      currentTagCode: defaultNescode,
      tagList: []
    }

    setTimeout(() => {
      state.influenceCircitTagData = {
        currentTagCode: defaultNescode,
        tagList: [
          {
            tagData: {
              nealias: '장치 미정',
              nescode: defaultNescode,
            }
          }
        ]
      }
    }, 100)
  }
}

const actions = {
  async DELETE_INFLUENCECIRCIT_PAGE_TAG({ state, commit }, tagData) {
    if (state.influenceCircitTagData.tagList.length === 1) return
    const deleteTagIndex = findTagIndex(state, tagData)
    await commit('DELETE_INFLUENCECIRCIT_PAGE_TAG', deleteTagIndex)

    if (deleteTagIndex === 0) {
      commit('SET_TAG_CURRENT_CODE', state.influenceCircitTagData.tagList[0].tagData.nescode)
      // state.influenceCircitTagData.currentTagCode = state.influenceCircitTagData.tagList[0].tagData.nescode
    } else {
      commit('SET_TAG_CURRENT_CODE', state.influenceCircitTagData.tagList[deleteTagIndex - 1].tagData.nescode)
      // state.influenceCircitTagData.currentTagCode = state.influenceCircitTagData.tagList[deleteTagIndex - 1].tagData.nescode
    }
  },

  SAVE_INFLUENCECIRCIT_PAGE_TAG_DATA({ commit, state }, tagData) {
    const tagIndex = findTagIndex(state, tagData)
    if (tagIndex === -1) {
      if (checkDefault(state)) {
        commit('EDIT_INFLUENCECIRCIT_PAGE_TAG_DATA', { tagData: tagData, tagIndex: 0 })
        commit('SET_TAG_CURRENT_CODE', tagData.nescode)
      } else {
        commit('SAVE_INFLUENCECIRCIT_PAGE_TAG_DATA', tagData)
        commit('SET_TAG_CURRENT_CODE', tagData.nescode)
      }
    } else {
      commit('SET_TAG_CURRENT_CODE', tagData.nescode)
    }
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
