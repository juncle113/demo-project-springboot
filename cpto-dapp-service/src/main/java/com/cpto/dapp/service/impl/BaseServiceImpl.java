package com.cpto.dapp.service.impl;

import com.cpto.dapp.service.BaseService;
import com.cpto.dapp.service.ManagerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * BaseServiceImpl
 *
 * @author sunli
 * @date 2018/12/07
 */
@Service
public class BaseServiceImpl implements BaseService {

    @Autowired
    protected ManagerLogService managerLogService;
}