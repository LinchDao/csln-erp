package com.lin.csln.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.csln.common.cache.UserCache;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.UserInfoDTO;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.config.AppSecurityProperties;
import com.lin.csln.dto.sys.user.*;
import com.lin.csln.entity.RoleDO;
import com.lin.csln.entity.ShopDO;
import com.lin.csln.entity.UserDO;
import com.lin.csln.entity.WarehouseDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.enums.RoleEnums;
import com.lin.csln.mapper.UserMapper;
import com.lin.csln.service.*;
import com.lin.csln.utils.JwtTokenUtil;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseReadonlyServiceImpl<UserMapper, UserDO> implements UserService {

    @Resource
    private RoleService roleService;
    @Resource
    private ShopService shopService;
    @Resource
    private WarehouseService warehouseService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private AppSecurityProperties securityProperties;
    @Resource
    private AuthRedisService authRedisService;

    @Override
    public UserDO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getUsername, username);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public UserInfoDTO getUserInfoByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getUsername, username);
        UserDO user = baseMapper.selectOne(queryWrapper);
        if (user == null) {
            return null;
        }
        return getUserInfoById(user.getId());
    }

    @Override
    public UserInfoDTO getUserInfoById(String id) {
        if (!StringUtils.hasText(id)) {
            return null;
        }
        UserDO userDO = baseMapper.selectById(id);
        if (userDO == null) {
            return null;
        }

        List<RoleDO> roles = roleService.listRolesByUserId(id);

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(userDO, userInfoDTO);
        if (!CollectionUtils.isEmpty(roles)) {
            List<String> roleCodes = roles.stream().map(RoleDO::getRoleCode).collect(Collectors.toList());
            userInfoDTO.setRoles(roleCodes);
        }

        return userInfoDTO;
    }

    @Override
    public Map<String, String> getUserNamesByIds(Set<String> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return new HashMap<>();
        }

        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UserDO::getId, userIds);
        wrapper.select(UserDO::getId, UserDO::getRealName);

        List<UserDO> userList = baseMapper.selectList(wrapper);
        return userList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(UserDO::getId, UserDO::getRealName, (oldValue, newValue) -> oldValue));
    }

    @Override
    public PageRespDTO<UserPageRespDTO> pageUser(UserQueryParamDTO queryDTO) {
        IPage<UserPageRespDTO> page = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
        IPage<UserPageRespDTO> result = baseMapper.pageUser(page, queryDTO);
        return PageRespDTO.build(result, queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addUser(UserSaveReqDTO dto) {
        validateRoleIds(dto.getRoleIds());
        checkUnique(dto.getUsername(), dto.getPhone(), null);

        UserDO user = new UserDO();
        user.setUsername(dto.getUsername().trim());
        user.setRealName(dto.getRealName().trim());
        user.setPhone(dto.getPhone().trim());
        user.setShopId(normalizeId(dto.getShopId()));
        user.setWarehouseId(normalizeId(dto.getWarehouseId()));
        user.setStatus(dto.getStatus());
        user.setPassword(encryptPassword(getDefaultPassword()));

        this.save(user);
        userRoleService.replaceRoles(user.getId(), dto.getRoleIds());
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserSaveReqDTO dto) {
        if (!StringUtils.hasText(dto.getId())) {
            throw new BusinessException("id is required for update");
        }
        validateRoleIds(dto.getRoleIds());

        UserDO dbUser = this.getById(dto.getId());
        if (dbUser == null) {
            throw new BusinessException("user not found");
        }

        checkUnique(dto.getUsername(), dto.getPhone(), dto.getId());

        UserDO updateUser = new UserDO();
        updateUser.setId(dto.getId());
        updateUser.setUsername(dto.getUsername().trim());
        updateUser.setRealName(dto.getRealName().trim());
        updateUser.setPhone(dto.getPhone().trim());
        updateUser.setShopId(normalizeId(dto.getShopId()));
        updateUser.setWarehouseId(normalizeId(dto.getWarehouseId()));
        updateUser.setStatus(dto.getStatus());
        this.updateById(updateUser);

        userRoleService.replaceRoles(dto.getId(), dto.getRoleIds());
    }

    @Override
    public UserDetailRespDTO getUserDetail(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException("user id is required");
        }

        UserDO user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("user not found");
        }

        UserDetailRespDTO detail = new UserDetailRespDTO();
        detail.setId(user.getId());
        detail.setUsername(user.getUsername());
        detail.setRealName(user.getRealName());
        detail.setPhone(user.getPhone());
        detail.setShopId(user.getShopId());
        detail.setWarehouseId(user.getWarehouseId());
        detail.setStatus(user.getStatus());
        detail.setCreateTime(formatDateTime(user.getCreateTime()));

        detail.setShopName(resolveShopName(user.getShopId()));
        detail.setWarehouseName(resolveWarehouseName(user.getWarehouseId()));

        List<String> roleIds = userRoleService.listRoleIdsByUserId(userId);
        detail.setRoleIds(roleIds);
        if (roleIds.isEmpty()) {
            detail.setRoleNames(new ArrayList<>());
        } else {
            List<RoleDO> roleList = roleService.listByIds(roleIds);
            Map<String, String> roleNameMap = roleList.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(RoleDO::getId, RoleDO::getRoleName, (a, b) -> a));
            List<String> roleNames = roleIds.stream().map(id -> roleNameMap.getOrDefault(id, "")).collect(Collectors.toList());
            detail.setRoleNames(roleNames);
        }

        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(UserPasswordUpdateDTO dto, String userId) {
        UserDO user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("未找到该用户");
        }

        String oldEncrypted = encryptPassword(dto.getOldPassword());
        if (!oldEncrypted.equals(user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        UserDO updateUser = new UserDO();
        updateUser.setId(userId);
        updateUser.setPassword(encryptPassword(dto.getNewPassword()));
        this.updateById(updateUser);
        UserCache.deleteUserInfo(userId);
        authRedisService.deleteUserSessions(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetUserPassword(String targetUserId, String operatorUserId) {
        if (!StringUtils.hasText(operatorUserId)) {
            throw new BusinessException("未登录或Token过期");
        }
        if (!StringUtils.hasText(targetUserId)) {
            throw new BusinessException("用户ID为必填项");
        }
        if (operatorUserId.equals(targetUserId)) {
            throw new BusinessException("不允许重置当前登录用户密码");
        }

        List<RoleDO> operatorRoles = roleService.listRolesByUserId(operatorUserId);
        boolean isSuperAdmin = operatorRoles.stream()
                .filter(Objects::nonNull)
                .anyMatch(role -> RoleEnums.SUPER_ADMIN.getCode().equals(role.getRoleCode()));
        if (!isSuperAdmin) {
            throw new BusinessException("无权限执行重置密码操作");
        }

        UserDO targetUser = this.getById(targetUserId);
        if (targetUser == null) {
            throw new BusinessException("用户不存在");
        }

        UserDO updateUser = new UserDO();
        updateUser.setId(targetUserId);
        updateUser.setPassword(encryptPassword(getDefaultPassword()));
        this.updateById(updateUser);

        UserCache.deleteUserInfo(targetUserId);
        authRedisService.deleteUserSessions(targetUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(UserStatusUpdateDTO dto) {
        if (dto == null || !StringUtils.hasText(dto.getUserId())) {
            throw new BusinessException("用户ID为必填项");
        }

        Integer status = dto.getStatus();
        if (!Objects.equals(status, GlobalEnums.YES.getCode()) && !Objects.equals(status, GlobalEnums.NO.getCode())) {
            throw new BusinessException("状态应为0或者1");
        }

        UserDO dbUser = this.getById(dto.getUserId());
        if (dbUser == null) {
            throw new BusinessException("用户不存在");
        }

        String currentUserId = JwtTokenUtil.getUserId();
        if (Objects.equals(status, 0) && StringUtils.hasText(currentUserId) && currentUserId.equals(dto.getUserId())) {
            throw new BusinessException("不允许禁用当前登录用户");
        }

        if (Objects.equals(dbUser.getStatus(), status)) {
            return;
        }

        UserDO updateUser = new UserDO();
        updateUser.setId(dto.getUserId());
        updateUser.setStatus(status);
        this.updateById(updateUser);
        if (Objects.equals(status, GlobalEnums.NO.getCode())) {
            authRedisService.deleteUserSessions(dto.getUserId());
        }
    }

    private void checkUnique(String username, String phone, String excludeUserId) {
        LambdaQueryWrapper<UserDO> usernameWrapper = new LambdaQueryWrapper<>();
        usernameWrapper.eq(UserDO::getUsername, username.trim());
        if (StringUtils.hasText(excludeUserId)) {
            usernameWrapper.ne(UserDO::getId, excludeUserId);
        }
        if (baseMapper.selectCount(usernameWrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        LambdaQueryWrapper<UserDO> phoneWrapper = new LambdaQueryWrapper<>();
        phoneWrapper.eq(UserDO::getPhone, phone.trim());
        if (StringUtils.hasText(excludeUserId)) {
            phoneWrapper.ne(UserDO::getId, excludeUserId);
        }
        if (baseMapper.selectCount(phoneWrapper) > 0) {
            throw new BusinessException("手机号已存在");
        }
    }

    private void validateRoleIds(List<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new BusinessException("请至少选择一个角色");
        }
    }

    private String normalizeId(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private String resolveShopName(String shopId) {
        if (!StringUtils.hasText(shopId)) {
            return "";
        }
        ShopDO shop = shopService.getById(shopId);
        return shop == null || !StringUtils.hasText(shop.getShopName()) ? "" : shop.getShopName();
    }

    private String resolveWarehouseName(String warehouseId) {
        if (!StringUtils.hasText(warehouseId)) {
            return "";
        }
        WarehouseDO warehouse = warehouseService.getById(warehouseId);
        return warehouse == null || !StringUtils.hasText(warehouse.getWarehouseName()) ? "" : warehouse.getWarehouseName();
    }

    private String getDefaultPassword() {
        String defaultPassword = securityProperties.getDefaultPassword();
        return StringUtils.hasText(defaultPassword) ? defaultPassword : "123456";
    }

    private String encryptPassword(String plainPassword) {
        String salt = securityProperties.getPasswordSalt();
        boolean saltEnabled = Boolean.TRUE.equals(securityProperties.getSaltEnabled());
        if (saltEnabled) {
            return DigestUtils.md5Hex(plainPassword + salt);
        }
        return DigestUtils.md5Hex(plainPassword);
    }

    private String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
}
