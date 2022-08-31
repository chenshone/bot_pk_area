<template>
  <ContentField>
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="register">
          <div class="mb-3">
            <label for="username" class="form-label">用户名</label>
            <input
              type="text"
              class="form-control"
              id="username"
              placeholder="请输入用户名"
              v-model="username"
            />
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">密码</label>
            <input
              type="password"
              class="form-control"
              id="password"
              placeholder="请输入密码"
              v-model="password"
            />
          </div>
          <div class="mb-3">
            <label for="confirmedPassword" class="form-label">确认密码</label>
            <input
              type="password"
              class="form-control"
              id="confirmedPassword"
              placeholder="请再次输入密码"
              v-model="confirmedPassword"
            />
          </div>
          <div class="err_message">{{ error_message }}</div>
          <button type="submit" class="btn btn-primary">注册</button>
        </form>
      </div>
    </div>
  </ContentField>
</template>

<script>
import $ from "jquery"
import ContentField from "@/components/ContentField.vue"
import { ref } from "vue"
import { useRouter } from "vue-router"
export default {
  setup() {
    const router = useRouter()
    let username = ref("")
    let password = ref("")
    let confirmedPassword = ref("")
    let error_message = ref("")
    const register = () => {
      $.ajax({
        url: "http://127.0.0.1:8090/user/account/register",
        type: "post",
        data: {
          username: username.value,
          password: password.value,
          confirmedPassword: confirmedPassword.value,
        },
        success: (resp) => {
          if (resp.message === "success") {
            router.push({ name: "user_account_login" })
          } else {
            error_message.value = resp.message
          }
        },
        error(resp) {
          error_message.value = "未知错误"
        },
      })
    }
    return {
      username,
      password,
      error_message,
      confirmedPassword,
      register,
    }
  },
  components: { ContentField },
}
</script>

<style lang="scss" scoped>
.err_message {
  color: red;
}
.btn {
  width: 100%;
}
</style>
