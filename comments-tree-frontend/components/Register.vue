<template>
  <v-card class="reg-form">
    <v-form ref="regForm" v-model="valid" lazy-validation>
      <v-text-field
        v-model="username"
        :rules="[usernameRules.required, usernameRules.valid, usernameExistRule]"
        v-bind:label="$t('label_username')"
        required
        @keydown="resetUsernameExist"
      ></v-text-field>

      <v-text-field
        v-model="email"
        :rules="[emailRules.required, emailRules.valid, emailExistRule]"
        v-bind:label="$t('label_email')"
        required
        @keydown="resetEmailExist"
      ></v-text-field>

      <v-text-field
        v-model="password"
        :rules="[passwordRules.required, passwordRules.valid]"
        :type="'password'"
        v-bind:label="$t('label_password')"
        required
      ></v-text-field>

      <v-text-field
        v-model="passwordConfirm"
        :rules="[passwordRules.required, passwordRules.valid, passwordConfirmationRule]"
        :type="'password'"
         v-bind:label="$t('label_confirm_password')"
        required
      ></v-text-field>

      <v-card-actions>
        <v-btn :disabled="!valid" color="success" class="mr-4" @click="register"> {{$t('submit')}} </v-btn>
      </v-card-actions>
    </v-form>
  </v-card>
</template>

<style scoped>
.reg-form {
  padding: 20px;
}
</style>

<script>
import utils from '~/assets/js/utils'

export default {
  data: () => ({
    valid: true,
    username: "",
    usernameRules: {
      required: (v) => !!v || $nuxt.$t('err_msg_username_required'),
      valid: (v) => /^[a-zA-Z0-9]{5,20}$/.test(v) || $nuxt.$t('err_msg_username_valid'),
    },
    usernameExist: false,
    email: "",
    emailRules: {
      required: (v) => !!v || $nuxt.$t('err_msg_email_required'),
      valid: (v) => /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/.test(v) || $nuxt.$t('err_msg_email_valid'),
    },
    emailExist: false,
    password: "",
    passwordRules: {
      required: (v) => !!v || $nuxt.$t('err_msg_pwd_required'),
      valid: (v) => /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?.&_-])[\w$@$!%*?.&-]{8,20}$/.test(v) || $nuxt.$t('err_msg_pwd_valid'),
    },
    passwordConfirm: "",
  }),
  computed: {
    passwordConfirmationRule() {
      return () => (this.password === this.passwordConfirm) || $nuxt.$t('err_msg_pwd_same')
    },
    usernameExistRule() {
      return !this.usernameExist || $nuxt.$t('err_msg_username_exist')
    },
    emailExistRule() {
      return !this.emailExist || $nuxt.$t('err_msg_email_exist')
    }
  },
  methods: {
    async register() {
      let formValid = this.$refs.regForm.validate()
      if (!formValid) {
        return
      }
      try {
        let response = await this.$axios.post('/api/auth/register', {
          username: this.username,
          email: this.email,
          password: this.password
        });
        this.$router.push("/login");
      } catch (err) {
        let code = utils.getApiErrCode(err)
        switch (code) {
          // EXIST_USERNAME
          case 10004:
            this.usernameExist = true
            return
          // EXIST_EMAIL
          case 10005:
            this.emailExist = true
            return
          default:
        }
        throw err
      }
    },
    resetUsernameExist() {
      if (this.usernameExist) {
        this.usernameExist = false
      }
    },
    resetEmailExist() {
      if (this.emailExist) {
        this.emailExist = false
      }
    },
  }
};
</script>
