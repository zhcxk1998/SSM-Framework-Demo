package softwaretest.error;

public enum EmBusinessError implements CommonError {

    /* 通用错误类型10001 */
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),

    /* 未知错误 */
    UNKNOWN_ERROR(10002, "未知错误"),

    /* 404为用户不存在 */
    USER_NOT_EXIST(404, "用户不存在"),

    /* 登录失败 */
    USER_LOGIN_FAIL(401, "用户用户名或密码不正确"),

    /* 注册用户名已存在 */
    USER_REGISTER_FAIL(403, "该用户名已存在")

    ;

    private EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
