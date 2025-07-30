package cn.clancy.redis.service;

/**
 * @Author ClancyLv
 * @Date 2025/6/20 11:45
 * @Description 服务层--缓存
 */
public interface CacheService {

    /**
     * 根据id查找数据
     * @param id id
     * @return 数据
     */
    String get(Long id);

    /**
     * 保存数据
     * @param id id
     * @param data 数据
     * @return 是否保存成功
     */
    String save(Long id, String data);

    /**
     * 更新数据
     * @param id id
     * @param data 数据
     * @return 是否更新成功
     */
    String update(Long id, String data);

    /**
     * 删除数据
     * @param id id
     */
    void delete(Long id);
}
