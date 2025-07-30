package cn.clancy.service;

import cn.clancy.entity.UserEntity;

import java.util.List;

/**
 * @Author ClancyLv
 * @Date 2025/7/16 11:15
 * @Description 服务层--部分匹配 + 哈希索引
 * 此方案适用于手机号、身份证号等具有固定结构、且查询模式单一的场景。
 */
public interface PartMatchService {
    /**
     * 根据手机号后四位查询用户信息。
     *
     * @param mobileSuffix 手机号后四位，用于筛选用户信息。
     * @return 查询到的用户实体列表，若无匹配项则返回空列表。
     */
    List<UserEntity> queryByMobileSuffix(String mobileSuffix);

    /**
     * 保存手机号信息。
     *
     * @param mobile 手机号
     * @return 保存结果，保存成功返回 true，保存失败返回 false。
     */
    boolean saveMobile(String mobile);
}
