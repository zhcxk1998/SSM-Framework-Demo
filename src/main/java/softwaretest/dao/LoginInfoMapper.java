package softwaretest.dao;

import softwaretest.entity.LoginInfo;

import java.util.List;

public interface LoginInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoginInfo record);

    int insertSelective(LoginInfo record);

    int updateByPrimaryKeySelective(LoginInfo record);

    int updateByPrimaryKey(LoginInfo record);

    List<LoginInfo> selectAll();

    LoginInfo selectByPrimaryKey(Integer id);

}