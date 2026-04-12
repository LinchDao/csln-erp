package com.lin.csln.service;

import java.util.Set;

/**
 * 用户权限聚合服务
 */
public interface UserPermissionService {

    Set<String> listPermCodesByUserId(String userId);
}
