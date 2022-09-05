<template>
  <Playground v-if="$store.state.pk.status === 'playing'" />
  <MatchView v-if="$store.state.pk.status === 'matching'" />
  <ResultBoard v-if="$store.state.pk.loser !== 'none'" />
</template>

<script>
import Playground from "@/components/Playground.vue"
import MatchView from "@/components/MatchView.vue"
import ResultBoard from "@/components/ResultBoard.vue"
import { onMounted, onUnmounted } from "vue"
import { useStore } from "vuex"
export default {
  components: {
    Playground,
    MatchView,
    ResultBoard,
  },
  setup() {
    const store = useStore()
    const socketUrl = `ws://127.0.0.1:8090/websocket/${store.state.user.token}`
    let socket = null

    store.commit("updateLoser", "none")
    store.commit("updateIsRecord", false)

    onMounted(() => {
      socket = new WebSocket(socketUrl)

      socket.onopen = () => {
        store.commit("updateOpponent", {
          username: "我的对手",
          avatar:
            "https://cdn.jsdelivr.net/gh/chenshone/myPictureHost@main/code_class/20220901211257.png",
        })
        store.commit("updateSocket", socket)
        console.log("connected")
      }
      socket.onmessage = (msg) => {
        const data = JSON.parse(msg.data)
        if (data.event === "start-matching") {
          store.commit("updateOpponent", {
            username: data.opponent_username,
            avatar: data.opponent_avatar,
          })
          store.commit("updateGame", data.game)
          setTimeout(() => {
            store.commit("updateStatus", "playing")
          }, 200)
        } else if (data.event === "move") {
          const game = store.state.pk.gameObject
          const [snake0, snake1] = game.snakes
          snake0.setDirection(data.a_direction)
          snake1.setDirection(data.b_direction)
        } else if (data.event === "result") {
          const game = store.state.pk.gameObject
          const [snake0, snake1] = game.snakes
          if (data.loser === "all" || data.loser === "A") {
            snake0.status = "die"
          }
          if (data.loser === "all" || data.loser === "B") {
            snake1.status = "die"
          }
          store.commit("updateLoser", data.loser)
        }
      }
      socket.onclose = () => {
        console.log("closed")
      }
    })

    onUnmounted(() => {
      socket.close()
      store.commit("updateStatus", "matching")
    })
    return {}
  },
}
</script>

<style lang="scss" scoped></style>
