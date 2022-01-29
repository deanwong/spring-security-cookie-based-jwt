<template>
  <v-app light>
    <Snackbar />
    <v-app-bar app flat>
      <v-btn icon @click="home">
        <v-icon>mdi-home</v-icon>
      </v-btn>
      <v-toolbar-title>{{this.$auth.loggedIn ? this.$auth.user.username + "/" + this.$auth.user.email : "" }} </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn v-if="this.$auth.loggedIn" text @click="logout">
        <v-icon>mdi-logout</v-icon>{{$t('logout')}}
      </v-btn>
      <v-btn v-if="!this.$auth.loggedIn" text @click="login">
        <v-icon>mdi-login</v-icon>{{$t('login')}}
      </v-btn>
    </v-app-bar>
    <v-main>
      <v-container>
        <Nuxt />
      </v-container>
    </v-main>
  </v-app>
</template>
<script>
import Snackbar from '~/components/Snackbar'

export default {
  name: "DefaultLayout",
  components: {
    Snackbar
  },
  data: () => ({
  }),
  methods: {
    async logout() {
      try {
        let response = await this.$auth.logout();
        console.log(response);
        this.$router.push("/login");
      } catch (err) {
        throw err;
      }
    },
    login() {
      this.$router.push("/login");
    },
    home() {
      this.$router.push("/");
    }
  }
};
</script>