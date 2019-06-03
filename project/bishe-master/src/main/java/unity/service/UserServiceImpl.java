package unity.service;

import unity.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unity.pojo.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl {

    @Autowired
    UserMapper userMapper;
    /**
     * 获取网站所有用户
     */
    public List<User> getallUsers(){
        ArrayList<User> list=(ArrayList)userMapper.selectAll();
        return list;
    }

    /**
     * 查询特定用户
     */
    public User getUser(Integer userId){
        User user=userMapper.selectByPrimaryKey(userId);
        return user;
    }

    /**
     * 通过邮箱查询用户
     * @param email
     * @return
     */
    public User getUserByEmail(String email){
        return userMapper.selectByEmail(email);
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    public Integer addUser(User user){
        return userMapper.insert(user);
    }
}
