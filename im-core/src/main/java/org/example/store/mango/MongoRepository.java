package org.example.store.mango;

import cn.hutool.core.collection.CollUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.conversions.Bson;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class MongoRepository<T> {

    protected MongoCollection<T> collection;
    Class<T> entityClass;

    protected MongoRepository() {
        MongoClient client = MongoConfig.getClient();
        MongoDatabase im = client.getDatabase("im");
        entityClass = getEntityClass();

        collection = im.getCollection(entityClass.getSimpleName(), entityClass);
    }

    public void insert(T data) {
        collection.insertOne(data);
    }

    public List<T> find() {
        FindIterable<T> ts = collection.find();
        return CollUtil.newArrayList(ts);
    }

    public List<T> find(Bson bson){
        FindIterable<T> ts = collection.find(bson, entityClass);
        return CollUtil.newArrayList(ts);
    }

    public T findOne(Bson bson){
        return collection.find(bson, entityClass).first();
    }


    private Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
