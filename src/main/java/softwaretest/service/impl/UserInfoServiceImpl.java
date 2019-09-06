package softwaretest.service.impl;

//import com.alibaba.druid.util.StringUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softwaretest.dao.LoginInfoMapper;
import softwaretest.dao.UserInfoMapper;
import softwaretest.dao.UserPasswordMapper;
import softwaretest.entity.LoginInfo;
import softwaretest.entity.UserInfo;
import softwaretest.entity.UserPassword;
import softwaretest.error.*;
import softwaretest.service.UserInfoService;
import softwaretest.service.model.UserInfoModel;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserPasswordMapper userPasswordMapper;

    @Autowired
    private LoginInfoMapper loginInfoMapper;

    @Override
    public UserInfoModel getUserById(Integer id) {
        /* 调用UserInfoMapper获取到对应的用户对象 */
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        if (userInfo == null) {
            return null;
        }

        /* 通过用户id获取对应的用户加密密码信息 */
        UserPassword userPassword = userPasswordMapper.selectByUserId(userInfo.getId());

        return convertFromDataObject(userInfo, userPassword);
    }

    @Override
    public List<UserInfoModel> getUserList() {
        List<UserInfo> userInfoList = userInfoMapper.selectAll();

        List<UserInfoModel> userInfoModelList = userInfoList.stream().map(userInfo -> {
            UserPassword userPassword = userPasswordMapper.selectByUserId(userInfo.getId());
            UserInfoModel userInfoModel = this.convertFromDataObject(userInfo, userPassword);
            return userInfoModel;
        }).collect(Collectors.toList());
        return userInfoModelList;
    }

    @Override
    public List<LoginInfo> getLoginList() {
        return loginInfoMapper.selectAll();
    }

    @Override
    @Transactional
    public void register(UserInfoModel userInfoModel) throws BusinessException {
        if (userInfoModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if (StringUtils.isEmpty(userInfoModel.getUserName())
                || StringUtils.isEmpty(userInfoModel.getEncrptPassword())
                || StringUtils.isEmpty(userInfoModel.getIdCard())) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if (userInfoMapper.selectByUsername(userInfoModel.getUserName()) != null) {
            throw new RegisterFailException(EmBusinessError.USER_REGISTER_FAIL);
        }

        /* 实现model转换成dataobject方法 */
        UserInfo userInfo = convertFromModel(userInfoModel);
        userInfoMapper.insertSelective(userInfo);

        /* 设置用户信息表对应的id，以便用户密码表中能正确设置user_id的外键 */
        userInfoModel.setId(userInfo.getId());


        UserPassword userPassword = convertPasswordFromModel(userInfoModel);
        userPasswordMapper.insertSelective(userPassword);
    }

    @Override
    public UserInfoModel validateLogin(String username, String encrptPassword) throws BusinessException {
        /* 通过用户用户名获取用户信息 */
        UserInfo userInfo = userInfoMapper.selectByUsername(username);
        if (userInfo == null) {
            throw new NotFoundException(EmBusinessError.USER_NOT_EXIST);
        }
        UserPassword userPassword = userPasswordMapper.selectByUserId(userInfo.getId());
        UserInfoModel userInfoModel = convertFromDataObject(userInfo, userPassword);
        /* 比对用户信息内加密的密码是否和传输进来的密码相匹配 */
        if (!StringUtils.equals(encrptPassword, userInfoModel.getEncrptPassword())) {
            throw new UnauthorizedException(EmBusinessError.USER_LOGIN_FAIL);
        }

        recordLogin(userInfoModel);

        return userInfoModel;
    }

    /* 记录登录成功的信息 */
    private void recordLogin(UserInfoModel userInfoModel) throws BusinessException {
        if (userInfoModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserId(userInfoModel.getId());
        loginInfo.setLoginTime(String.valueOf(new Date().getTime()));

        loginInfoMapper.insertSelective(loginInfo);
    }

    private UserPassword convertPasswordFromModel(UserInfoModel userInfoModel) {
        if (userInfoModel == null) {
            return null;
        }
        UserPassword userPassword = new UserPassword();
        userPassword.setEncrptPassword(userInfoModel.getEncrptPassword());
        userPassword.setUserId(userInfoModel.getId());
        return userPassword;
    }

    private UserInfo convertFromModel(UserInfoModel userInfoModel) {
        if (userInfoModel == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoModel, userInfo);
        return userInfo;
    }

    private UserInfoModel convertFromDataObject(UserInfo userInfo, UserPassword userPassword) {
        if (userInfo == null) {
            return null;
        }

        UserInfoModel userInfoModel = new UserInfoModel();
        BeanUtils.copyProperties(userInfo, userInfoModel);

        if (userPassword != null) {
            userInfoModel.setEncrptPassword(userPassword.getEncrptPassword());

        }
        return userInfoModel;
    }
}
