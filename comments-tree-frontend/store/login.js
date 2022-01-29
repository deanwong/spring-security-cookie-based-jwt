export const state = () => ({
    dialog: false,
  })
  
export const mutations = {
    setDialog (state, dialog) {
        state.dialog = dialog
    }
}