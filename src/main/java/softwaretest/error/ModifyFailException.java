package softwaretest.error;

public class ModifyFailException extends BusinessException implements CommonError {
    public ModifyFailException(CommonError commonError) {
        super(commonError);
    }
}
