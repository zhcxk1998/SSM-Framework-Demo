package softwaretest.error;

public class UnauthorizedException extends BusinessException implements CommonError {
    public UnauthorizedException(CommonError commonError) {
        super(commonError);
    }
}
