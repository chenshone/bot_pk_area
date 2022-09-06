<template>
  <ContentField>
    <div class="row">
      <div class="col-3">
        <div class="card" style="width: 18rem">
          <img
            :src="$store.state.user.avatar"
            class="card-img-top"
            alt="头像"
            style="width: 100%"
          />
          <div class="card-body d-flex justify-content-center">
            <p class="card-text">
              {{ $store.state.user.username }}
            </p>
          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="card">
          <div class="card-header">
            <span style="font-size: 150%">我的bot</span>
            <button
              type="button"
              class="btn btn-primary float-end"
              data-bs-toggle="modal"
              data-bs-target="#BotEditModal"
              @click="addOrUpdate = true"
            >
              创建bot
            </button>
          </div>
          <div class="card-body">
            <table class="table table-striped table-hover">
              <thead>
                <tr>
                  <th>名称</th>
                  <th>创建时间</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="bot in bots" :key="bot.id">
                  <th>{{ bot.title }}</th>
                  <th>{{ bot.createTime }}</th>
                  <th>
                    <div class="btn-group" role="group">
                      <button
                        type="button"
                        class="btn btn-outline-primary"
                        data-bs-toggle="modal"
                        data-bs-target="#BotEditModal"
                        @click="updateOpenModal(bot)"
                      >
                        修改
                      </button>
                      <button
                        type="button"
                        class="btn btn-outline-primary"
                        @click="removeBot(bot.id)"
                      >
                        删除
                      </button>
                    </div>
                  </th>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
    <!-- Modal -->
    <div
      class="modal fade"
      id="BotEditModal"
      tabindex="-1"
      aria-labelledby="addBotModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog modal-xl">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="addBotModalLabel">
              {{ addOrUpdate ? "创建bot" : "修改bot" }}
            </h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
              @click="clearModal"
            ></button>
          </div>
          <div class="modal-body">
            <form>
              <div class="mb-3">
                <label for="titleInput" class="form-label">标题</label>
                <input
                  type="text"
                  class="form-control"
                  id="titleInput"
                  placeholder="请输入标题"
                  v-model="botAdd.title"
                />
              </div>
              <div class="mb-3">
                <label for="descriptionInput" class="form-label">描述</label>
                <textarea
                  type="text"
                  class="form-control"
                  id="descriptionInput"
                  placeholder="请输入描述"
                  rows="3"
                  v-model="botAdd.description"
                />
              </div>
              <div class="mb-3">
                <label for="codeInput" class="form-label">代码</label>
                <VAceEditor
                  @init="editorInit"
                  lang="java"
                  theme="textmate"
                  style="height: 300px"
                  v-model:value="botAdd.content"
                />
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <div class="err_message">{{ botAdd.errorMessage }}</div>
            <button
              v-if="addOrUpdate"
              type="button"
              class="btn btn-primary"
              @click="addBot"
            >
              创建
            </button>
            <button
              v-else
              type="button"
              class="btn btn-primary"
              @click="updateBot"
            >
              修改
            </button>
            <button
              type="button"
              class="btn btn-secondary"
              data-bs-dismiss="modal"
              @click="clearModal"
            >
              取消
            </button>
          </div>
        </div>
      </div>
    </div>
  </ContentField>
</template>

<script>
import $ from "jquery"
import { Modal } from "bootstrap"
import { useStore } from "vuex"
import { ref, reactive } from "vue"
import { VAceEditor } from "vue3-ace-editor"
import ace from "ace-builds"

import ContentField from "@/components/ContentField.vue"
export default {
  components: { ContentField, VAceEditor },
  setup() {
    ace.config.set(
      "basePath",
      "https://cdn.jsdelivr.net/npm/ace-builds@" +
        require("ace-builds").version +
        "/src-noconflict/"
    )
    const store = useStore()
    let bots = ref([])
    let addOrUpdate = ref(true) // true:add, false:update
    let botAdd = reactive({
      id: 0,
      title: "",
      description: "",
      content: "",
      errorMessage: "",
    })

    const getBotList = () => {
      $.ajax({
        url: "http://bpa.chenshone.top/api/user/bot/get-list",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          bots.value = resp
        },
      })
    }
    const addBot = () => {
      botAdd.errorMessage = ""
      $.ajax({
        url: "http://bpa.chenshone.top/api/user/bot/add",
        type: "post",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        data: {
          title: botAdd.title,
          description: botAdd.description,
          content: botAdd.content,
        },
        success(resp) {
          if (resp.message === "success") {
            clearModal()
            Modal.getInstance("#BotEditModal").hide()
            $(".modal-backdrop").remove()
            getBotList()
          } else botAdd.errorMessage = resp.message
        },
        error(resp) {
          botAdd.errorMessage = resp.message
        },
      })
    }

    const updateBot = () => {
      botAdd.errorMessage = ""
      $.ajax({
        url: "http://bpa.chenshone.top/api/user/bot/update",
        type: "post",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        data: {
          id: botAdd.id,
          title: botAdd.title,
          description: botAdd.description,
          content: botAdd.content,
        },
        success(resp) {
          if (resp.message === "success") {
            Modal.getInstance("#BotEditModal").hide()
            $(".modal-backdrop").remove()
            clearModal()
            getBotList()
          } else botAdd.errorMessage = resp.message
        },
        error(resp) {
          botAdd.errorMessage = resp.message
        },
      })
    }
    const removeBot = (botId) => {
      $.ajax({
        url: "http://bpa.chenshone.top/api/user/bot/remove",
        type: "post",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        data: {
          id: botId,
        },
        success(resp) {
          if (resp.message === "success") {
            getBotList()
          }
        },
        error(resp) {},
      })
    }

    const updateOpenModal = (bot) => {
      addOrUpdate.value = false
      botAdd.id = bot.id
      botAdd.title = bot.title
      botAdd.description = bot.description
      botAdd.content = bot.content
      botAdd.errorMessage = ""
    }

    const clearModal = () => {
      botAdd.id = 0
      botAdd.title = ""
      botAdd.description = ""
      botAdd.content = ""
      botAdd.errorMessage = ""
    }

    getBotList()

    return {
      bots,
      botAdd,
      addOrUpdate,
      addBot,
      removeBot,
      updateBot,
      updateOpenModal,
      clearModal,
    }
  },
}
</script>

<style lang="scss" scoped>
.err_message {
  color: red;
}
</style>
