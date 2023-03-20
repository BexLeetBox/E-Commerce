import Vuex from 'vuex'

export default new Vuex.Store({
  state: {
    isLoggedIn: false,
    currentPage: '/sell', 
  },
  mutations: {
    setLoginStatus(state, status) {
      state.isLoggedIn = status;
    },
    setCurrentPage(state, page) {
      state.currentPage = page;
    },
  },
  actions: {
    setLoginStatus({commit}, status) {
      commit('setLoginStatus', status)
    },
    setCurrentPage({commit}, page) {
      commit('setCurrentPage', page)
    },
  },
  getters: {
    getLoginStatus(state) {
      return state.isLoggedIn
    },
    getCurrentPage(state) {
      return state.currentPage;
    }
  }
})
