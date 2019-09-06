package softwaretest.error;

public class NotFoundException extends BusinessException implements CommonError {
    public NotFoundException(CommonError commonError) {
        super(commonError);
    }
}
