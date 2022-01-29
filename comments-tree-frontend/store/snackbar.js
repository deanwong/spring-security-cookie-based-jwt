export const state = () => ({
    snack: "",
    level: "info"
  })
  
export const mutations = {
    setSnack (state, {snack, level}) {
        state.snack = snack
        state.level = level
    }
}