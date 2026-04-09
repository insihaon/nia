import IndexedArray from '@/class/indexedArray'

const state = {
  fullFilterText: '',
  mbaTicketList: null, // rca_순단장애
  pmmTicketList: null // rca_예지보전
}

function getMbaTicketList(state) {
  if (!state.mbaTicketList) {
    const list = state.mbaTicketList ?? []
    state.mbaTicketList = new IndexedArray(null, 'ticket_id', list)
  }
  return state.mbaTicketList
}

function getPmmTicketList(state) {
  if (!state.pmmTicketList) {
    const list = state.pmmTicketList ?? []
    state.pmmTicketList = new IndexedArray(null, 'ticketno', list)
  }
  return state.pmmTicketList
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
    if (state.mbaTicketList?.rows) {
      state.mbaTicketList.rows = state.mbaTicketList.rows.filter(row => row.ticket_id !== value?.ticket_id)
    }
  },
  UPSERT_TICKET_PMM_LIST: (state, { value }) => {
    state.pmmTicketList = getPmmTicketList(state)
    state.pmmTicketList.merge(value, true)
  },
  DELETE_TICKET_PMM_LIST: (state, value) => {
    if (state.pmmTicketList?.rows) {
      state.pmmTicketList.rows = state.pmmTicketList.rows.filter(row => row.ticketno !== value?.ticketno)
    }
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
  },
  insertTicketPmmList({ commit }, value) {
    commit('UPSERT_TICKET_PMM_LIST', { value, execInsert: true })
  },
  upsertTicketPmmList({ commit }, value) {
    commit('UPSERT_TICKET_PMM_LIST', { value, execInsert: false })
  },
  deleteTicketPmmList({ commit }, value) {
    commit('DELETE_TICKET_PMM_LIST', value)
  }
}

const getters = {
  mbaTicketList: (state) => {
    return getMbaTicketList(state)
  },
  fullFilterText: (state) => {
    return state.fullFilterText
  },
  pmmTicketList: (state) => {
    return getPmmTicketList(state)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}
