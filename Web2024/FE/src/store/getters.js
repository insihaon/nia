const getters = {
  sidebar: (state) => state.app.sidebar,
  server: (state) => state.app.server,
  size: (state) => state.app.size,
  screenDevice: (state) => state.app.screenDevice,
  visitedViews: (state) => state.tagsView.visitedViews,
  cachedViews: (state) => state.tagsView.cachedViews,
  token: (state) => state.user.token,
  uid: (state) => state.user.uid,
  blackDtlList: (state) => state.user.blackDtlList,
  name: (state) => state.user.name,
  avatar: (state) => state.user.avatar,
  roles: (state) => state.user.roles,
  permission_routes: (state) => state.permission.routes,
  errorLogs: (state) => state.errorLog.logs,
  serviceLogs: (state) => state.serviceLog.logs,
  filterText: (state) => state.bottombarFilter.filterText,
  oasis: (state) => state.oasis,
  bcn: (state) => state.bcn,
  aam: (state) => state.aam,
  aamPersisted: (state) => state.aamPersisted,
  influenceCircitStore: (state) => state.influenceCircitStore,
  t3d: (state) => state.t3d,
  t3dPersisted: (state) => state.t3dPersisted,
  showHistory: (state) => state.app.showHistory,
  pmmlte: (state) => state.pmmlte,

  // 리사이징 모달
  windows: state => state.workControlModal.windows,
  window_param: state => state.workControlModal.window_param
}
export default getters
