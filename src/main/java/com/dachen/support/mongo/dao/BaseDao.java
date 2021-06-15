package com.dachen.support.mongo.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dachen.common.exception.ServiceException;
import com.dachen.integral.data.base.BasePO;
import com.dachen.support.annotation.Model;
import com.dachen.util.Pagination;
import com.mongodb.*;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.AdvancedDatastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 钟良
 * @desc
 * @date:2017/6/30 10:20 Copyright (c) 2017, DaChen All Rights Reserved.
 */
public abstract class BaseDao implements InitializingBean {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "dsForRW")
	protected AdvancedDatastore dsForRW;

	protected Class<?> entityClass;

	public BaseDao() {
		
		Model annotation = this.getClass().getAnnotation(Model.class);
		this.entityClass = annotation.value();
		
	}

	public void afterPropertiesSet() throws Exception {

	}

	public <T> T findById(String id) {
		Query<T> query = this.createQueryByPK(id);
		return query.get();
	}

	public <T> List<T> findByIds(List<String> ids) {
		Query<T> query = this.createQueryByPKs(ids);
		return query.asList();
	}

	public <T> Query<T> createQueryByPKs(List<String> idList) {
		if (CollectionUtils.isEmpty(idList)) {
			throw new ServiceException("idList is empty!!!");
		}
		Query<T> query = this.createQuery();
		List<ObjectId> fParams = new ArrayList<>();
		for (String id : idList) {
			fParams.add(new ObjectId(id));
		}
		query.field(Mapper.ID_KEY).in(fParams);
		/*
		 * Criteria[] idParams = new Criteria[idList.size()]; for (int i = 0; i
		 * < idList.size(); i++) { idParams[i] =
		 * query.criteria(Mapper.ID_KEY).equal(new ObjectId(idList.get(i))); }
		 * query.or(idParams);
		 */
		// query.field(Mapper.ID_KEY).in(idList); // 不能用in，ObjectId.in strings？
		return query;
	}

	public <T> Query<T> createQueryByPK(String id) {
		if (StringUtils.isBlank(id)) {
			throw new ServiceException("id is blank!!!");
		}
		Query<T> query = this.createQuery();
		query.field(Mapper.ID_KEY).equal(new ObjectId(id));
		return query;
	}

	@SuppressWarnings("unchecked")
	public <T> Query<T> createQuery() {
		return (Query<T>) this.dsForRW.createQuery(this.entityClass);
	}

	@SuppressWarnings("unchecked")
	public <T> UpdateOperations<T> createUpdateOperations() {
		return (UpdateOperations<T>) this.dsForRW.createUpdateOperations(this.entityClass);
	}

	/**
	 * 此方法不推荐使用！有可能存在的脏数据覆盖新数据的问题。
	 * 如果要更新某些字段，请使用update(String id, Map<String, Object> map)方法
	 * 如果确实要调用此方法，请先调用findById获取最新的entity再调用saveEntity
	 *
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public <T> String saveEntity(T entity) {
		if (null == entity) {
			throw new ServiceException("entity is null!!!");
		}
		return dsForRW.save(entity).getId().toString();
	}

	public <T> void saveEntities(List<T> entities) {
		if (CollectionUtils.isEmpty(entities)) {
			return;
		}
		dsForRW.save(entities);
	}

	/**
	 * 此方法不推荐使用！有可能存在的脏数据覆盖新数据的问题。
	 * 如果要更新某些字段，请使用update(String id, Map<String, Object> map)方法
	 * 如果确实要调用此方法，请先调用findById获取最新的entity再调用saveEntity
	 *
	 * @param entity
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T saveEntityAndFind(T entity) {
		String id = dsForRW.save(entity).getId().toString();
		return (T) findById(id);
	}

	public <T> List<String> saveEntityBatch(List<T> entityList) {
		if (CollectionUtils.isEmpty(entityList)) {
			return null;
		}
		List<String> idList = new ArrayList<String>(entityList.size());
		Iterator<Key<T>> iterator = this.dsForRW.save(entityList).iterator();
		while (iterator.hasNext()) {
			idList.add(iterator.next().getId().toString());
		}
		return idList;
	}

	
	public <T> List<T> saveEntityBatchAndFind(List<T> entityList) {
		if (CollectionUtils.isEmpty(entityList)) {
			return null;
		}
		Iterable<Key<T>> ret = this.dsForRW.save(entityList);
		Iterator<Key<T>> iter = ret.iterator();
		List<String> idList = new ArrayList<String>(entityList.size());
		while (iter.hasNext()) {
			String id = iter.next().getId().toString();
			idList.add(id);
		}
		return findByIds(idList);
	}

	
	public <T> boolean update(String id, Map<String, Object> map) {
		if (CollectionUtils.isEmpty(map)) {
			return false;
		}

		Query<T> query = this.createQueryByPK(id);

		boolean needUpdate = false;

		UpdateOperations<T> ops = this.createUpdateOperations();
		// use entrySet rather than keySet to iterate
		Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Object> entry = iterator.next();
			String key = entry.getKey();
			Object val = entry.getValue();
			if (null != val) {
				ops.set(key, val);
				needUpdate = true;
			}
			// if (StringUtils.isNotBlank(key)) {
			// ops.set(key, Objects.toString(val, "")); // 支持值置空（此语句有错，by
			// xiaowei, 170406）
			// needUpdate = true;
			// }
		}

		if (!needUpdate) {
			return false;
		}

		UpdateResults ur = dsForRW.update(query, ops);
		return ur.getUpdatedCount() > 0;
	}

	
	public <T> int update(final Query<T> q, final UpdateOperations<T> ops) {
		// TODO: 判断T是否存在updateCreate字段，如有，更新之
		return this.dsForRW.update(q, ops).getUpdatedCount();
	}

	
	public <T> int update(final Query<T> q, final UpdateOperations<T> ops, final boolean createIfMissing) {
		// TODO: 判断T是否存在updateCreate字段，如有，更新之
		return this.dsForRW.update(q, ops, createIfMissing).getUpdatedCount();
	}

	
	public <T> int update(String id, String fieldName, Object fieldValue) {
		Query<T> query = this.createQueryByPK(id);

		UpdateOperations<T> ops = this.createUpdateOperations();
		// ops.set(fieldName, Objects.toString(fieldValue, "")); // 不能toString,
		// fieldValue类型不一定是String，也可能是Integer
		if (null == fieldValue) {
			ops.set(fieldName, "");
		} else {
			ops.set(fieldName, fieldValue);
		}

		return this.update(query, ops);
	}

	
	public <T> int update(List<String> idList, String fieldName, Object fieldValue) {
		Query<T> query = this.createQueryByPKs(idList);

		UpdateOperations<T> ops = this.createUpdateOperations();
		// ops.set(fieldName, Objects.toString(fieldValue, ""));
		if (null == fieldValue) {
			ops.set(fieldName, "");
		} else {
			ops.set(fieldName, fieldValue);
		}

		return this.update(query, ops);
	}

	
	public <T> int deleteByQuery(Query<T> query) {
		return dsForRW.delete(query).getN();
	}

	
	public <T> int deleteByIds(List<String> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return 0;
		}
		Query<T> query = this.createQueryByPKs(ids);
		return this.deleteByQuery(query);
	}

	
	public <T> boolean deleteById(String id) {
		Query<T> query = this.createQueryByPK(id);
		WriteResult wr = dsForRW.delete(query);
		return wr.getN() > 0;
	}

	@SuppressWarnings("unchecked")
	public <R> List<R> findField(DBObject dbObject, String fieldName) {
		DBCollection dbCollection = dsForRW.getCollection(this.entityClass);
		if (dbCollection != null) {
			DBObject fieldObject = new BasicDBObject(fieldName, 1);
			DBCursor dbCursor = dbCollection.find(dbObject, fieldObject);
			try {
				List<R> resultList = new LinkedList<>();
				while (dbCursor.hasNext()) {
					DBObject object = dbCursor.next();
					if (object.containsField(fieldName)) {
						resultList.add((R) object.get(fieldName));
					}
				}
				return resultList;
			} finally {
				dbCursor.close();
			}
		}
		return Collections.EMPTY_LIST;
	}

	
	public <T> long count(DBObject dbObject) {
		Query<T> query = this.createQuery();
		if (dbObject != null) {
			for (String key : dbObject.keySet()) {
				query.field(key).equal(dbObject.get(key));
			}
		}
		return query.count();
	}

	public <T extends BasePO> String saveOrUpdate(T entity) {
		if(StringUtils.isEmpty(entity.getId())){
			return dsForRW.insert(entity).getId().toString();
		}else{
			UpdateOperations<T> updateOperations=this.createUpdateOperations();
			Query<T> query=this.createQueryByPK(entity.getId());
			LinkedHashMap<String, Object> updateMap= JSON.parseObject(JSON.toJSONString(entity),new TypeReference<LinkedHashMap<String, Object>>(){});

			for(String key:updateMap.keySet()){
				if(Objects.equals(key, "id")){continue;}
				updateOperations.set(key, updateMap.get(key));
			}
			UpdateResults updateResults=dsForRW.update(query, updateOperations);
			if(updateResults.getUpdatedCount()>0){
				return entity.getId();
			}else{
				throw new ServiceException(String.format("修改失败,业务ID:%s,class:{}",entity.getId(),entity.getClass().getName()));
			}
		}
	}
	
	public <T> Pagination<T> getPageData(DBObject filter, String sort, int pageSize, int pageIndex){
		return this.getPageData(filter, sort, pageSize, pageIndex,true);
	}
	
	public <T> Pagination<T> getPageData(DBObject filter, String sort, int pageSize, int pageIndex, boolean pageShow){
		if(pageSize<=0){
			pageSize = 20;
		}
		Query<T> query=this.createQuery();
		if(filter!=null){
			for(String condition:filter.keySet()){
				query.filter(condition, filter.get(condition));
			}
		}
		long totalCount = query.count();
		if(StringUtils.isNotEmpty(sort)){
			query.order(sort);
		}
		List<T> resultList=null;
		if(pageShow){
			FindOptions findOptions=new FindOptions();
			findOptions.skip(pageIndex*pageSize).limit(pageSize);
			resultList= query.asList(findOptions);
		}else{
			resultList= query.asList();
		}
		Pagination<T> result = new Pagination<>( );
		result.setTotal(totalCount);
		result.setPageData(resultList);
		result.setPageIndex(pageIndex);
		result.setPageSize(pageSize);
		if(!pageShow){
			if(totalCount>0){
				result.setPageSize((int)totalCount);
			}
		}
		return result;
	}
}
