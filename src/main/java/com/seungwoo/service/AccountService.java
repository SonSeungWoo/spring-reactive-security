package com.seungwoo.service;


import com.seungwoo.domain.Account;

import java.util.List;

/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-01-22
 * Time: 17:28
 */
public interface AccountService {

    List<Account> findAll();

}

