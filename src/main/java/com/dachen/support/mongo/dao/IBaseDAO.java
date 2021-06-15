package com.dachen.support.mongo.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/6.
 */
public interface IBaseDAO<T> {

    public T save(T obj);

    public T insert(T obj);

    public T update(final Class<T> clazz, String id, Map<String, Object> updateFieldMap);

    public T get(final Class<T> clazz, String condition, Object value);

    public T getByPK(final Class<T> clazz, String pk);

    public void delete(final Class<T> clazz, List<String> pks);

    public void delete(final Class<T> clazz, String pk);

    /**
     * 批量新增
     */
    void save(List<T> obj);

    public T inc(final Class<T> clazz, String id, String key, Integer value);
}
