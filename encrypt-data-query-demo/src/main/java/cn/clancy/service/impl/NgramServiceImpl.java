package cn.clancy.service.impl;

import cn.clancy.entity.UserEntity;
import cn.clancy.service.NgramService;
import cn.clancy.utils.EncryptUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author ClancyLv
 * @Date 2025/7/16 14:07
 * @Description 服务实现层--N-gram分词 + 哈希索引
 * 此方案适用于姓名、地址等非结构化文本的模糊查询。
 * 淘宝、京东、拼多多等平台也是采用该方案
 * 淘宝：https://open.taobao.com/docV3.htm?docId=106213&docType=1
 * 阿里：https://jaq-doc.alibaba.com/docs/doc.htm?treeId=1&articleId=106213&docType=1
 */
@Service
public class NgramServiceImpl implements NgramService {
    /**
     * 模拟数据库
     */
    private final static List<UserEntity> USER_LIST = new ArrayList<>();

    @Override
    public List<UserEntity> queryByUsername(String usernameQuery) {
        // 参数校验
        if (StrUtil.isBlank(usernameQuery)) {
            return USER_LIST;
        }
        if (StrUtil.isNotBlank(usernameQuery) && usernameQuery.length() < 2) {
            throw new IllegalArgumentException("用户名长度不能小于两位");
        }

        // 1、对用户名查询条件进行分词
        Set<String> usernameQueryNgramSet = generateNgrams(usernameQuery, 2);

        // 2、对每个分词进行HMAC哈希，并转成列表
        List<String> usernameQueryHashList = usernameQueryNgramSet.stream()
                .map(EncryptUtil::hmac)
                .toList();

        // 3、根据查询条件的分词列表，模糊匹配所有分词列表
        // 查询逻辑：字段usernameHash中应该包含所有 查询条件分词 才匹配（注意：匹配使用like '%遍历分词%'）
        // 这里以示例为主，没有实现查询数据库功能
        List<UserEntity> userList = new ArrayList<>();
        boolean flag;
        for (UserEntity user : USER_LIST) {
            flag = true;
            String usernameHash = user.getUsernameHash();
            for (String usernameQueryHash : usernameQueryHashList) {
                // 模糊匹配不到分词，标记这条数据不匹配，开始下一条数据比对
                if (!usernameHash.contains(usernameQueryHash)) {
                    flag = false;
                    break;
                }
            }
            // 查询条件所有分词匹配
            if (flag) {
                userList.add(user);
            }
        }

        // 4、二次验证，过滤掉哈希碰撞或非目标数据
        return userList.stream()
                .filter(user -> {
                    // 解密查询出的加密用户名
                    String username = EncryptUtil.aesDecrypt(user.getUsername());
                    return username.contains(usernameQuery);
                })
                .toList();
    }

    @Override
    public boolean saveUsername(String username) {
        if (StrUtil.isBlank(username)) {
            throw new NullPointerException("用户名为空");
        }

        // 1、生成N-gram分词
        Set<String> ngramSet = generateNgrams(username, 2);

        // 2、对每个分词进行HMAC哈希，并用空格连接
        String usernameHash = ngramSet.stream()
                .map(EncryptUtil::hmac)
                .collect(Collectors.joining(" "));

        // 3、加密完整姓名
        String encryptUsername = EncryptUtil.aesEncrypt(username);

        // 4、构建实体类，并存入数据库
        UserEntity userEntity = UserEntity.builder()
                .username(encryptUsername)
                .usernameHash(usernameHash)
                .build();
        // 这里以示例为主，没有实现存入数据库功能
        return USER_LIST.add(userEntity);
    }

    /**
     * 根据输入数据生成指定大小的N-gram分词集合。
     *
     * @param data 输入字符串数据，用于生成N-gram分词。如果输入为空或长度小于N-gram大小，则返回空集合。
     * @param ngramSize 分词的大小，即每个N-gram中包含的字符数。必须为正数。
     * @return 包含生成的N-gram分词的集合。如果输入数据不符合条件，则返回空集合。
     */
    private Set<String> generateNgrams(String data, int ngramSize) {
        Set<String> ngrams = new HashSet<>();
        if (StrUtil.isBlank(data) || ngramSize < 0 || data.length() < ngramSize) {
            return ngrams;
        }
        for (int i = 0; i <= data.length() - ngramSize; i++) {
            ngrams.add(data.substring(i, i + ngramSize));
        }
        return ngrams;
    }
}
