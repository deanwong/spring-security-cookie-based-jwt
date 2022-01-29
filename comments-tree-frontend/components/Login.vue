<template>
  <v-card class="login-form">
    <v-form ref="loginForm" v-model="valid" lazy-validation>
      <v-text-field
        v-model="loginId"
        :rules="[rules.username_required, badCredentialRule]"
         v-bind:label="$t('label_login_id')"
        required
        @keydown="resetValidation"
      ></v-text-field>

      <v-text-field
        v-model="password"
        :rules="[rules.pwd_required, badCredentialRule]"
        :type="'password'"
        v-bind:label="$t('label_password')"
        required
        @keydown="resetValidation"
      ></v-text-field>

      <v-checkbox v-model="rememberMe" v-bind:label="$t('remember_me')"></v-checkbox>

      <v-card-actions>
        <v-btn :disabled="!valid" color="success" class="mr-4" @click="login">
          {{$t('submit')}}
        </v-btn>
        <v-btn text color="info" class="mr-4" to="/register">
          {{$t('register')}}
        </v-btn>
        <slot name="actions"></slot>
      </v-card-actions>

    </v-form>
  </v-card>
</template>

<style scoped>
.login-form {
  padding: 20px;
}
</style>

<script>
import utils from '~/assets/js/utils'

export default {
  data: () => ({
    valid: true,
    loginId: "",
    password: "",
    rememberMe: false,
    rules: {
      username_required: (value) => !!value || $nuxt.$t('err_msg_username_required'),
      pwd_required: (value) => !!value || $nuxt.$t('err_msg_pwd_required'),
    },
    badCredential: false
  }),
  props: {
    comment: {
      mode: String,
      required: true,
      default: "page"
    },
  },
  mounted() {
    if (this.$auth.loggedIn && this.mode === "page") {
      this.$router.push("/");
    }
  },
  computed: {
    badCredentialRule() {
      return !this.badCredential || $nuxt.$t('err_msg_pwd_bad_credential')
    }
  },
  methods: {
    async login() {
      let formValid = this.$refs.loginForm.validate()
      if (!formValid) {
        return
      }
      try {
        await this.$auth.loginWith("cookie", {
          data: {
            loginId: this.loginId,
            password: this.password,
            rememberMe: this.rememberMe
          },
        });
        if (this.mode === "page") {
          this.$router.push("/");
        }
      } catch (err) {
        let code = utils.getApiErrCode(err)
        if (code === 10006) {
          this.badCredential = true
          return
        }
        throw err;
      }
    },
    resetValidation() {
      if (this.badCredential) {
        this.badCredential = false
      }
    }
  },
};
</script>
