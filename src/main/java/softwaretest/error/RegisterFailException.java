package softwaretest.error;

public class RegisterFailException extends BusinessException implements CommonError {
    public RegisterFailException(CommonError commonError) {
        super(commonError);
    }
}
