<template>
  <v-container>
    <v-row dense>
      <v-col cols="12">
        <v-form ref="messageForm" v-model="valid" lazy-validation>
          <v-textarea
            outlined
            counter="200"
            v-bind:placeholder="$t('message') + '...'"
            :rules="replyRules"
            v-model="newReply"
          ></v-textarea>
          <v-btn :disabled="!valid" color="success" class="mr-4" @click="message"> {{$t('submit')}} </v-btn>
        </v-form>
      </v-col>
    </v-row>
    <v-slide-y-transition group tag="v-list">
    <v-row dense  v-for="comment in comments" v-bind:key="comment.id">
      <v-col cols="12">
        <Comment :comment="comment" />
      </v-col>
    </v-row>
    </v-slide-y-transition>

    <LoginDialog />
  </v-container>

</template>

<script>
import Comment from "~/components/Comment.vue";
import LoginDialog from "~/components/LoginDialog.vue";
import { mapMutations } from 'vuex'

export default {
  name: "IndexPage",
  components: {
    Comment,
    LoginDialog,
  },
  data: () => ({
    valid: true,
    dataReady: false,
    comments: [],
    replyRules: [v => v && v.length <= 200 &&  v.length >= 3 || $nuxt.$t('err_msg_comment_valid')],
    newReply: ""
  }),
  async mounted() {
    try {
      let response = await this.$axios.get("/api/comments");
      this.comments = response.data;
      this.dataReady = true;
    } catch (err) {
      throw err;
    }
  },
  methods: {
    async message() {
      let formValid = this.$refs.messageForm.validate()
      if (!formValid) {
        return
      }
      if (!this.$auth.loggedIn) {
        this.openLoginDialog()
        return
      }
      try {
        let response = await this.$axios.post("/api/comments/0/", {
          pid: 0,
          content: this.newReply,
        });
        this.comments.unshift(response.data)
        this.newReply = ""
      } catch (err) {
        throw err;
      }
    },
    openLoginDialog: function () {
      this.setDialog(true)
    },
    ...mapMutations({
      setDialog: 'login/setDialog'
    })
  },
  
};
</script>
