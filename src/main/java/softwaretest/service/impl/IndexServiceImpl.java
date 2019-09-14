package softwaretest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwaretest.dao.LoginInfoMapper;
import softwaretest.entity.LoginInfo;
import softwaretest.service.IndexService;

import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private LoginInfoMapper loginInfoMapper;

    @Override
    public List<LoginInfo> getLoginList() {
        return loginInfoMapper.selectAll();
    }
}
