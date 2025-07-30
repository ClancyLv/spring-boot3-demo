package cn.clancy.service;

import cn.clancy.entity.UserEntity;

import java.util.List;

/**
 * @Author ClancyLv
 * @Date 2025/7/16 14:01
 * @Description 接口层--N-gram分词 + 哈希索引
 * 此方案适用于姓名、地址等非结构化文本的模糊查询。
 * 淘宝、京东、拼多多等平台也是采用该方案
 * 淘宝：https://open.taobao.com/docV3.htm?docId=106213&docType=1
 * 阿里：https://jaq-doc.alibaba.com/docs/doc.htm?treeId=1&articleId=106213&docType=1
 */
public interface NgramService {
    /**
     * 根据手机号后四位查询用户信息。
     *
     * @param usernameQuery 用户名查询
     * @return 查询到的用户实体列表，若无匹配项则返回空列表。
     */
    List<UserEntity> queryByUsername(String usernameQuery);

    /**
     * 保存用户名信息。
     *
     * @param username 用户名
     * @return 保存结果，保存成功返回 true，保存失败返回 false。
     */
    boolean saveUsername(String username);
}
