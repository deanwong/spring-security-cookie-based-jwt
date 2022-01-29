<template>
  <v-dialog
      v-model="dialog"
      max-width="600px"
    >
      <Login mode="dialog">
        <template v-slot:actions>
            <v-btn
              text
              @click="close"
            >
              Close
            </v-btn>
        </template>
      </Login>
    </v-dialog>
</template>

<script>
import Login from "~/components/Login.vue";

export default {
  components: {
    Login,
  },
  data () {
    return {
      dialog: false,
    }
  },
  created: function () {
    this.$store.watch(state => state.auth.loggedIn, () => {
      if (this.$store.state.auth.loggedIn && this.dialog) {
        this.close()
      }
    })
    this.$store.watch(state => state.login.dialog, () => {
      this.dialog = this.$store.state.login.dialog
    })
  },
  methods: {
    close() {
      this.dialog = false
      this.$store.commit('login/setDialog', false)
    }
  }
}
</script>