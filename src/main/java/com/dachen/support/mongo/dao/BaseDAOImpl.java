package com.dachen.support.mongo.dao;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.AdvancedDatastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2017/3/6.
 */
public class BaseDAOImpl<T extends BaseInfo> implements IBaseDAO<T> {

    @Resource(name = "dsForRW")
    protected AdvancedDatastore dsForRW;

    @Override
    public T save(T obj) {
        if (obj == null) {
            return null;
        }
        Long time = System.currentTimeMillis();
        if (Objects.isNull(obj.getCreateTime())){
            obj.setCreateTime(time);
        }
        if (Objects.isNull(obj.getUpdateTime())){
            obj.setUpdateTime(time);
        }

        Object key = dsForRW.save(obj).getId();
        obj.setId(key.toString());
        return obj;
    }

    @Override
    public void save(List<T> obj) {
        if (CollectionUtils.isEmpty(obj)) {
            return;
        }
        dsForRW.save(obj);
    }


    @Override
    public T update(final Class<T> clazz, String id, Map<String, Object> updateFieldMap) {

        if (updateFieldMap == null || updateFieldMap.size() == 0) {
            return getByPK(clazz, id);
        }

        Query<T> q = dsForRW.createQuery(clazz).filter("_id", new ObjectId(id));
        UpdateOperations<T> ops = dsForRW.createUpdateOperations(clazz);
        for (Map.Entry<String, Object> eachObj : updateFieldMap.entrySet()) {
            if (eachObj.getValue() != null) {
                ops.set(eachObj.getKey(), eachObj.getValue());
            }
        }
        return dsForRW.findAndModify(q, ops);
    }

    @Override
    public T insert(T obj) {
        if (obj == null) {
            return null;
        }
        Long time = System.currentTimeMillis();
        if (Objects.isNull(obj.getCreateTime())){
            obj.setCreateTime(time);
        }
        if (Objects.isNull(obj.getUpdateTime())){
            obj.setUpdateTime(time);
        }
        dsForRW.insert(obj);
        return obj;
    }


    @Override
    public T get(final Class<T> clazz, String condition, Object value) {
        Query<T> query = dsForRW.createQuery(clazz).filter(condition, value);
        query.limit(1);
        return query.get();
    }

    @Override
    public T getByPK(final Class<T> clazz, String pk) {
        Query<T> query = dsForRW.createQuery(clazz).filter("_id", new ObjectId(pk));
        return query.get();
    }

    @Override
    public void delete(Class<T> clazz, List<String> pks) {
        if (CollectionUtils.isEmpty(pks)) {
            return;
        }

        List<ObjectId> objectIds = new ArrayList<>();
        for (String id : pks) {
            objectIds.add(new ObjectId(id));
        }

        dsForRW.delete(clazz, objectIds);
    }

    @Override
    public void delete(Class<T> clazz, String pk) {
        if (StringUtils.isEmpty(pk)) {
            return;
        }
        dsForRW.delete(clazz, new ObjectId(pk));
    }

    @Override
    public T inc(Class<T> clazz, String id, String key, Integer value) {
        Query<T> q = dsForRW.createQuery(clazz).filter("_id", new ObjectId(id));
        UpdateOperations<T> ops = dsForRW.createUpdateOperations(clazz);
        ops.inc(key, value);
        return dsForRW.findAndModify(q, ops);
    }

}
