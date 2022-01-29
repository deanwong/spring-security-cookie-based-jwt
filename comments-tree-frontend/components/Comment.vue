<template>
  <v-card outlined class="comment-box">
    <v-list-item three-line>
      <v-list-item-content>
        <div class="text-overline mb-1">
          <v-icon>mdi-calendar</v-icon> {{ toLocalTime(comment.dateCreated) }}
        </div>
        <v-list-item-title class="mb-2">
          <v-icon>mdi-account-circle</v-icon>
          {{ comment.author }}
        </v-list-item-title>
        <v-list-item-subtitle>
          <v-icon>mdi-message</v-icon>
          {{ comment.content }}
        </v-list-item-subtitle>
      </v-list-item-content>
    </v-list-item>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn text @click="showReply = !showReply">
        <v-icon>mdi-message-reply-outline</v-icon>
        {{$t('reply')}}
      </v-btn>
    </v-card-actions>
    <v-expand-transition>
      <div v-show="showReply">
        <v-divider></v-divider>
        <v-card-text>
          <v-form ref="replyForm" v-model="valid" lazy-validation>
            <v-textarea
              outlined
              counter="200"
             v-bind:placeholder="$t('reply') + '...'"
              :rules="replyRules"
              v-model="newReply"
            ></v-textarea>
            <v-btn :disabled="!valid" color="success" class="mr-4" @click="reply(comment)">
              {{$t('submit')}}
            </v-btn>
          </v-form>
        </v-card-text>
      </div>
    </v-expand-transition>
    <v-container class="comment-container" v-if="comment.replies">
      <v-slide-y-transition group tag="v-list">
      <v-row dense  v-for="reply in comment.replies"
            v-bind:key="reply.id">
          <v-col
            cols="12"
          >
            <Comment :comment="reply" />
          </v-col>
      </v-row>
      </v-slide-y-transition>
    </v-container>
  </v-card>
</template>

<style scoped>
.comment-box {
  padding: 20px 0 0 20px;
}
.comment-container {
  padding: 10px 0 0 10px;
}
</style>

<script>
import moment from "moment";
import { mapMutations } from 'vuex'

export default {
  data: () => ({
    valid: true,
    showReply: false,
    replyRules: [
      (v) => (v && v.length <= 200 && v.length >= 3) || $nuxt.$t('err_msg_comment_valid'),
    ],
    newReply: "",
  }),
  props: {
    comment: {
      type: Object,
      required: true,
    },
  },
  methods: {
    async reply(comment) {
      let formValid = this.$refs.replyForm.validate()
      if (!formValid) {
        return
      }
      if (!this.$auth.loggedIn) {
        this.openLoginDialog()
        return
      }
      try {
        let response = await this.$axios.post(
          "/api/comments/" + comment.id + "/",
          {
            pid: comment.id,
            content: this.newReply,
          }
        );
        if (!comment.replies) {
          comment.replies = [];
        }
        comment.replies.unshift(response.data);
        this.newReply = ""
        this.showReply = false;
      } catch (err) {
        throw err;
      }
    },
    toLocalTime(time) {
      return moment(time).format("YYYY-MM-DD HH:mm:ss");
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
