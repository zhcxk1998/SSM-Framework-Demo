package softwaretest.service;

import softwaretest.entity.LoginInfo;

import java.util.List;

public interface IndexService {
    List<LoginInfo> getLoginList();
}
