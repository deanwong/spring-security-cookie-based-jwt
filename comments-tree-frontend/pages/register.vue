<template>
  <v-row justify="center" align="center">
    <v-col cols="12" sm="8" md="8">
      <Register />
    </v-col>
  </v-row>
</template>
<style scoped>
.h-60 {
  height: 60vh;
}
</style>
<script>
import Register from "~/components/Register.vue";

export default {
  components: {
    Register,
  },
};
</script>
