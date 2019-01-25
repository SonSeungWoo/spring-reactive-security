package com.seungwoo.mapper;

import com.seungwoo.domain.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-01-22
 * Time: 17:33
 */

@Mapper
public interface AccountMapper {

    @Select("select * from account where username = #{name}")
    Account findByName(String name);

    @Select("select * from account")
    List<Account> findAll();
}
