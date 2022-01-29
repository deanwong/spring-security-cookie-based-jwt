export default function ({ $axios, redirect, store }) {
  $axios.onRequest((config) => {});

  $axios.onError((error) => {
    // handle error globally
    const code = parseInt(error.response && error.response.status);
    // when unauthorized popup snackbar and redirect to login 
    if (code === 401) {
      store.commit('snackbar/setSnack', {"snack":"Please login", "level": "error"})
      redirect("/login");
    }
  });
}
