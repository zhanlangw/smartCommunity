package cn.bytecloud.smartCommunity.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;

public class QueryUtil {
    public static Query createQuery(String...args){
        DBObject dbObject = new BasicDBObject();
        DBObject fieldObject = new BasicDBObject();
        for (String arg : args) {
            fieldObject.put(arg, true);
        }
        fieldObject.put("_id", true);
        return new BasicQuery(dbObject, fieldObject);
    }
}
