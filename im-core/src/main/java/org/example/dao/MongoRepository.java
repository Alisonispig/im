package org.example.dao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.conversions.Bson;
import org.example.config.MongoConfig;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class MongoRepository<T> {

    protected MongoCollection<T> collection;
    Class<T> entityClass;
    protected String keyName = "";
    protected String mongoKey = "_id";

    protected MongoRepository() {
        MongoClient client = MongoConfig.getClient();
        MongoDatabase im = client.getDatabase("im");
        entityClass = getEntityClass();

        collection = im.getCollection(entityClass.getSimpleName(), entityClass);
        getKeyName();
    }

    public void insert(T data) {
        collection.insertOne(data);
    }

    public List<T> find() {
        FindIterable<T> ts = collection.find();
        return CollUtil.newArrayList(ts);
    }

    public List<T> find(Bson bson) {
        FindIterable<T> ts = collection.find(bson, entityClass);
        return CollUtil.newArrayList(ts);
    }

    public List<T> find(Bson filter, Bson sort, Integer page, Integer number) {
        FindIterable<T> limit = collection.find(filter).sort(sort).skip(page * number).limit(number);
        return CollUtil.newArrayList(limit);
    }

    public List<T> findSort(Bson filter, Bson sort) {
        FindIterable<T> ts = collection.find(filter, entityClass).sort(sort);
        return CollUtil.newArrayList(ts);
    }

    public T findOne(Bson bson) {
        return collection.find(bson, entityClass).first();
    }

    public T findOne(Bson bson,Bson sort) {
        return collection.find(bson, entityClass).sort(sort).first();
    }

    public T findById(String key) {
        if (StrUtil.isBlank(keyName)) {
            throw new NullPointerException("主键Key未指定");
        }
        return collection.find(eq(mongoKey, key), entityClass).first();
    }

    public int count(Bson bson){
       return (int)collection.countDocuments(bson);
    }

    public T findOneLimit(Bson bson, Bson sort, int limit) {
        return collection.find(bson).sort(sort).limit(limit).first();
    }

    public void saveOrUpdateById(T data) {
        Object fieldValue = ReflectUtil.getFieldValue(data, keyName);
        Assertions.notNull("[主键值]", fieldValue);
        if (findById((String) fieldValue) == null) {
            insert(data);
        } else {
            updateById(data);
        }
    }

    public void saveOrUpdate(Bson bson, T data) {
        if (findOne(bson) == null) {
            insert(data);
        } else {
            replace(bson, data);
        }
    }

    public void replace(Bson bson, T data) {

        collection.replaceOne(bson, data);
    }

    public void updateMany(Bson bson, Bson update) {
        collection.updateMany(bson,update);
    }

    public void updateById(T data) {
        Object fieldValue = ReflectUtil.getFieldValue(data, keyName);
        Assertions.notNull("[主键值]", fieldValue);
        collection.replaceOne(eq(mongoKey, fieldValue), data);
    }

    public void delete(Bson bson) {
        collection.deleteMany(bson);
    }

    private Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private void getKeyName() {
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            BsonId annotation = field.getAnnotation(BsonId.class);
            if (annotation != null) {
                keyName = field.getName();
            }
        }
    }


}
