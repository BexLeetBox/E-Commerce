
import Vuex from 'vuex'


export default new Vuex.Store({
  state: {
    IsloggedIn: false
  },
  mutations: {
    loggedIn(state) {
      state.IsloggedIn = true;
    },
    loggedOut(state){
        state.IsloggedIn = false;
    }
  },
  actions: {
    loggedIn(context) {
      context.commit('loggedIn')
    },
    loggedOut(context) {
        context.commit('loggedOut')
      }
  },
  getters: {
    getLoggedInStatus(state) {
      return state.IsloggedIn
    }
  }
})
