package softwaretest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softwaretest.dao.UserInfoMapper;
import softwaretest.entity.UserInfo;


@SpringBootApplication(scanBasePackages = {"softwaretest"})
@RestController
/* 要添加basePackages才能正确扫描，不然会扫描不到 */
@MapperScan(basePackages = {"softwaretest.dao"})
public class App {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @RequestMapping("/")
    public String home() {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(1);
        if (userInfo == null) {
            return "用户不存在";
        } else {
            return userInfo.getUserName();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
