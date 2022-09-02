import router from "@/router"
import $ from "jquery"
export default {
  state: {
    status: "matching", // matching 匹配界面， playing 对战界面
    socket: null,
    opponentUsername: "",
    opponentAvatar: "",
    gameMap: null,
    aId: 0,
    aSx: 0,
    aSy: 0,
    bId: 0,
    bSx: 0,
    bSy: 0,
    gameObject: null,
    loser: "none", // none all A B
  },
  getters: {},
  mutations: {
    updateSocket(state, socket) {
      state.socket = socket
    },
    updateOpponent(state, opponent) {
      state.opponentUsername = opponent.username
      state.opponentAvatar = opponent.avatar
    },
    updateStatus(state, status) {
      state.status = status
    },
    updateGame(state, Game) {
      state.gameMap = Game.map
      state.aId = Game.a_id
      state.aSx = Game.a_sx
      state.aSy = Game.a_sy
      state.bId = Game.b_id
      state.bSx = Game.b_sx
      state.bSy = Game.b_sy
    },
    updateGameObject(state, gameObject) {
      state.gameObject = gameObject
    },
    updateLoser(state, loser) {
      state.loser = loser
    },
  },
  actions: {},

  modules: {},
}
