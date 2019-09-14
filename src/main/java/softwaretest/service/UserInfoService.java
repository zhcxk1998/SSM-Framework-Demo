package softwaretest.service;

import softwaretest.entity.LoginInfo;
import softwaretest.error.BusinessException;
import softwaretest.service.model.UserInfoModel;

import java.util.List;

public interface UserInfoService {
    /* 通过用户ID获取用户对象 */
    UserInfoModel getUserById(Integer id);

    List<UserInfoModel> getUserList();

    void register(UserInfoModel userInfoModel) throws BusinessException;

    void recordLogin(UserInfoModel userInfoModel) throws BusinessException;

    void modifyPassword(UserInfoModel userInfoModel) throws BusinessException;

    UserInfoModel validateModify(String username, String encrptPassword, String idCard) throws BusinessException;

    /**
     * @param username       用户注册用户名
     * @param encrptPassword 用户加密后的密码
     * @throws BusinessException
     */
    UserInfoModel validateLogin(String username, String encrptPassword) throws BusinessException;
}
