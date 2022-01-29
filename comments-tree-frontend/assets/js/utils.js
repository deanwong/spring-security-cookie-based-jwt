const utils = {
  /**
   * Get generic bizException errCode from non-2xx response
   *
   * @param {*} err
   * @returns int code
   */
  getApiErrCode(err) {
    const code = parseInt(err.response && err.response.status);
    if (code === 400) {
      return err.response.data.apierror.errCode;
    }
    return -1;
  },
};

export default utils;
