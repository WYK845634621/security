package com.yikai.security.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yikai.security.entity.Users;
import com.yikai.security.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Number K1171305
 * @Date 2020/11/25 17:55
 */
@Service("userDetailsService")      //因为那边需要注入,所以就指定了name
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("username",s);
        Users users = usersMapper.selectOne(wrapper);
        if (users == null){
            throw new UsernameNotFoundException("用户不存在");
        }

        //权限
        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("admin,common");
        //可加角色
        //源码中的角色以"ROLE_"开头,所以这里必须要以"ROLE_"开头
//        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_saler");


        return new User(users.getUsername(),new BCryptPasswordEncoder().encode(users.getPassword()), auths);
    }
}
