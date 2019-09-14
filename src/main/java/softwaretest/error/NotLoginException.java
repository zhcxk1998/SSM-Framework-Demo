package softwaretest.error;

public class NotLoginException extends BusinessException implements CommonError {
    public NotLoginException(CommonError commonError) {
        super(commonError);
    }
}
