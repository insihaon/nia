import IndexedArray from '@/class/indexedArray'

const state = {
  fullFilterText: '',
  mbaTicketList: null // rca_순단장애
}

function getMbaTicketList(state) {
  if (!state.mbaTicketList) {
    const list = state.mbaTicketList ?? []
    state.mbaTicketList = new IndexedArray(null, 'ticket_id', list)
  }
  return state.mbaTicketList
}

const mutations = {
  SET_FULL_FILTER_TEXT: (state, { key, value }) => {
    state[key] = value
  },
  UPSERT_TICKET_MBA_LIST: (state, { value }) => {
    state.mbaTicketList = getMbaTicketList(state)
    state.mbaTicketList.merge(value, true)
  },
  DELETE_TICKET_MBA_LIST: (state, value) => {
    state.mbaTicketList = state.mbaTicketList.rows.find(row => row.ticket_id === value?.ticket_id)
  }
}

const actions = {
  changeFullFilterText({ commit }, param) {
    commit('SET_FULL_FILTER_TEXT', param)
  },
  insertTicketMbaList({ commit }, value) {
    commit('UPSERT_TICKET_MBA_LIST', { value, execInsert: true })
  },
  upsertTicketMbaList({ commit }, value) {
    commit('UPSERT_TICKET_MBA_LIST', { value, execInsert: false })
  },
  deleteTicketMbaList({ commit }, value) {
    commit('DELETE_TICKET_MBA_LIST', value)
  }
}

const getters = {
  mbaTicketList: (state) => {
    return getMbaTicketList(state)
  },
  fullFilterText: (state) => {
    return state.fullFilterText
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}
