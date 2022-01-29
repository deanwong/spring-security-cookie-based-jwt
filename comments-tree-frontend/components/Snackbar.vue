<template>
  <v-snackbar v-model="show" v-bind:color="level" absolute outlined :timeout="timeout">
    {{message}}
    <template v-slot:action="{ attrs }">
      <v-btn color="blue" text v-bind="attrs" @click="show=false">Close</v-btn>
    </template>
  </v-snackbar>
</template>

<script>
export default {
  data () {
    return {
      show: false,
      message: "",
      level: "info",
      timeout: 2000,
    }
  },
  created: function () {
    this.$store.watch(state => state.snackbar.snack, () => {
      const msg = this.$store.state.snackbar.snack
      if (msg !== '') {
        this.show = true
        this.message = this.$store.state.snackbar.snack
        this.level = this.$store.state.snackbar.level || 'info'
        this.$store.commit('snackbar/setSnack', {"snack":"", "level": "info"})
      }
    })
  }
}
</script>