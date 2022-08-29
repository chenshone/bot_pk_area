<template>
    <ContentField>
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="login">
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
                            v-model="password"
                        />
                    </div>
                    <div class="err_message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-primary">登录</button>
                </form>
            </div>
        </div>
    </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue"
import { ref } from "vue"
import { useRouter } from "vue-router"
import { useStore } from "vuex"
export default {
    setup() {
        const store = useStore()
        const router = useRouter()
        let username = ref("")
        let password = ref("")
        let error_message = ref("")
        const login = () => {
            error_message.value = ""
            store.dispatch("login", {
                username: username.value,
                password: password.value,
                success() {
                    store.dispatch("getInfo", {
                        success(resp) {
                            router.push({
                                name: "home",
                            })
                            console.log(resp.username)
                        },
                        error(resp) {
                            console.log(resp)
                        },
                    })
                },
                error(resp) {
                    console.log(resp)
                    error_message.value = "用户名或密码错误"
                },
            })
        }
        return {
            username,
            password,
            error_message,
            login,
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
