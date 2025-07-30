package cn.clancy.service.impl;

import cn.clancy.entity.UserEntity;
import cn.clancy.service.PartMatchService;
import cn.clancy.utils.EncryptUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ClancyLv
 * @Date 2025/7/16 11:15
 * @Description 服务实现层--部分匹配 + 哈希索引
 * 此方案适用于手机号、身份证号等具有固定结构、且查询模式单一的场景。
 */
@RequiredArgsConstructor
@Service
public class PartMatchServiceImpl implements PartMatchService {
    /**
     * 模拟数据库
     */
    private final static List<UserEntity> USER_LIST = new ArrayList<>();

    @Override
    public List<UserEntity> queryByMobileSuffix(String mobileSuffix) {
        // 参数校验
        if (StrUtil.isBlank(mobileSuffix)) {
            return USER_LIST;
        }
        if (StrUtil.isNotBlank(mobileSuffix) && mobileSuffix.length() != 4) {
            throw new IllegalArgumentException("手机尾号不是四位");
        }

        // 1、计算需要查询的手机尾号的哈希值（与存储时采用相同算法）
        String searchMobileSuffixHash = EncryptUtil.hmac(mobileSuffix);

        // 2、根据手机尾号的哈希值，从数据库中通过索引精确查找到用户信息
        // 这里以示例为主，没有实现查询数据库功能
        List<UserEntity> userList = new ArrayList<>();
        for (UserEntity user : USER_LIST) {
            if (searchMobileSuffixHash.equals(user.getMobileHash())) {
                userList.add(user);
            }
        }

        // 3、二次验证，过滤掉哈希碰撞或非目标数据
        return userList.stream()
                .filter(user -> {
                    // 解密查询出的加密手机号
                    String mobile = EncryptUtil.aesDecrypt(user.getMobile());
                    return mobile.endsWith(mobileSuffix);
                })
                .toList();
    }

    @Override
    public boolean saveMobile(String mobile) {
        if (StrUtil.isBlank(mobile) || mobile.length() < 4) {
            throw new NullPointerException("手机号为空或者不合法");
        }

        // 1、提取手机号后四位
        String mobileSuffix = mobile.substring(mobile.length() - 4);

        // 2、计算手机后四位的哈希值
        String mobileSuffixHash = EncryptUtil.hmac(mobileSuffix);

        // 3、加密完整手机号
        String encryptedMobile = EncryptUtil.aesEncrypt(mobile);

        // 4、构建实体类，并存入数据库
        UserEntity userEntity = UserEntity.builder()
                .mobile(encryptedMobile)
                .mobileHash(mobileSuffixHash)
                .build();
        // 这里以示例为主，没有实现存入数据库功能
        return USER_LIST.add(userEntity);
    }
}
