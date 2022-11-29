package com.pubsub.redis.pubsubmessage.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import org.springframework.data.redis.core.*;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

 
public class RedisTool {

    //缓存时间30分钟
    private final static int expireTime = 60 * 30;

    /**
     * User: 
     * CreateDate: 2017/5/17 11:36
     * description: 刷新缓存时间
     */
    public static void refresh(RedisTemplate redisTemplate, final String key, final int time) {
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:36
     * description: 获取字符串
     */
    public static String getString(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return value == null ? null : value.toString();
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:37
     * description: 获取Double类型
     */
    public static Double getDouble(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return TypeUtils.castToDouble(value);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:37
     * description: 获取double类型
     */
    public static double getDoubleValue(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return value == null ? 0.0D : TypeUtils.castToDouble(value).doubleValue();
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:39
     * description: 获取Integer数据类型
     */
    public static Integer getInteger(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return TypeUtils.castToInt(value);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:39
     * description: 获取Int
     */
    public static int getIntValue(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return value == null ? 0 : TypeUtils.castToInt(value).intValue();
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:41
     * description: 获取BigInteger
     */
    public static BigInteger getBigInteger(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return TypeUtils.castToBigInteger(value);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:41
     * description: Float
     */
    public static Float getFloat(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return TypeUtils.castToFloat(value);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:41
     * description: FloatValue
     */
    public static float getFloatValue(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return value == null ? 0.0F : TypeUtils.castToFloat(value).floatValue();
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:42
     * description: Date
     */
    public static Date getDate(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return TypeUtils.castToDate(value);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:42
     * description: sqlDate
     */
    public static java.sql.Date getSqlDate(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return TypeUtils.castToSqlDate(value);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:42
     * description: timeStamp 时间戳
     */
    public static Timestamp getTimestamp(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return TypeUtils.castToTimestamp(value);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:43
     * description: Boolean
     */
    public static Boolean getBoolean(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return value == null ? null : TypeUtils.castToBoolean(value);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:43
     * description: BooleanValue
     */
    public static boolean getBooleanValue(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return value == null ? false : TypeUtils.castToBoolean(value).booleanValue();
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:44
     * description: Long(
     */
    public static Long getLong(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return TypeUtils.castToLong(value);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:44
     * description: LongValue
     */
    public static long getLongValue(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return value == null ? 0L : TypeUtils.castToLong(value).longValue();
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:44
     * description: Short
     */
    public static Short getShort(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return TypeUtils.castToShort(value);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 11:44
     * description: ShortValue
     */
    public static short getShortValue(RedisTemplate redisTemplate, final String key) {
        Object value = get(redisTemplate, key);
        return value == null ? 0 : TypeUtils.castToShort(value).shortValue();
    }

    /*
    * User: 
    * CreateDate: 2017/5/17 13:07
    * description: 返回集合
    */
    public static <T> List<T> getArrayList(RedisTemplate redisTemplate, final String key, Class<T> clazz) {
        String value = getString(redisTemplate, key);
        return value == null ? null : JSON.parseArray(value, clazz);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 13:06
     * description: 返回实体对象
     */
    public static <T> T getObject(RedisTemplate redisTemplate, final String key, Class<T> clazz) {
        String value = getString(redisTemplate, key);
        return value == null ? null : JSON.parseObject(value, clazz);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 13:06
     * description: 返回jsonobject
     */
    public static JSONObject getJsonObject(RedisTemplate redisTemplate, final String key) {
        String value = getString(redisTemplate, key);
        return value == null ? null : JSON.parseObject(value);
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public static void remove(RedisTemplate redisTemplate, final String... keys) {
        for (String key : keys) {
            remove(redisTemplate, key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public static void removePattern(RedisTemplate redisTemplate, final String pattern) {
        Set<String> keys = redisTemplate.keys(pattern + "*");
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public static void remove(RedisTemplate redisTemplate, final String key) {
        if (exists(redisTemplate, key)) {
            redisTemplate.delete(key);
        }
    }


    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public static boolean existsStr(RedisTemplate countRedisTemplate, final String key) {
        return countRedisTemplate.hasKey(key);
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public static boolean exists(RedisTemplate redisTemplate, final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public static Object get(RedisTemplate redisTemplate, final String key) {
        BoundValueOperations<String, String> boundValOps = redisTemplate.boundValueOps(key);
        return boundValOps.get();
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 16:30
     * description: 写入缓存->转json字符串,无过期时间，慎用此方法
     */
    public static boolean setStrJson(RedisTemplate redisTemplate, final String key, Object val) {
        return set(redisTemplate, key, JSON.toJSONString(val), null);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 16:30
     * description: 写入缓存->转json字符串,默认30分钟过期
     */
    public static boolean setStrJsonExpire(RedisTemplate redisTemplate, final String key, Object val) {
        return set(redisTemplate, key, JSON.toJSONString(val), expireTime);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 16:30
     * description: 写入缓存->转json字符串,无过期时间，慎用此方法
     */
    public static boolean setStrJson(RedisTemplate redisTemplate, final String key, Object val, Integer expireTime) {
        return set(redisTemplate, key, JSON.toJSONString(val), expireTime);
    }

    /*
    * User: 
    * CreateDate: 2017/5/17 18:12
    * description: 更新key对象field的值，无过期时间，如无特殊需求，慎用此方法
    */
    public static boolean setJsonField(RedisTemplate redisTemplate, String key, String field, String value) {
        JSONObject obj = getJsonObject(redisTemplate, key);
        obj.put(field, value);
        return setStrJson(redisTemplate, key, obj);
    }

    /*
     * User: 
     * CreateDate: 2017/5/17 18:12
     * description: 更新key对象field的值,默认30分钟过期
     */
    public static boolean setJsonFieldExpire(RedisTemplate redisTemplate, String key, String field, String value) {
        JSONObject obj = getJsonObject(redisTemplate, key);
        obj.put(field, value);
        return setStrJson(redisTemplate, key, obj, expireTime);
    }

    /*
     * User: 
     * CreateDate: 2017/5/17 18:12
     * description: 更新key对象field的值,自定义过期时间，如无特殊需求，慎用此方法
     */
    public static boolean setJsonField(RedisTemplate redisTemplate, String key, String field, String value, Integer expireTime) {
        JSONObject obj = getJsonObject(redisTemplate, key);
        obj.put(field, value);
        return setStrJson(redisTemplate, key, obj, expireTime);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 18:23
     * description: 递减
     */
    public static Long decr(RedisTemplate redisTemplate, String key, int by) {
        return redisTemplate.boundValueOps(key).increment(-by);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 18:23
     * description: 递减
     */
    public static double decrDouble(RedisTemplate redisTemplate, String key, double by) {
        return redisTemplate.boundValueOps(key).increment(-by);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 18:23
     * description: 递增
     */
    public static Long incr(RedisTemplate redis, String key, int by) {
        return redis.boundValueOps(key).increment(by);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 18:23
     * description: 递增
     */
    public static double incrDouble(RedisTemplate countRedisTemplate, String key, double by) {
        return countRedisTemplate.boundValueOps(key).increment(by);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 18:23
     * description: 递减
     */
    public static int decrInt(RedisTemplate countRedisTemplate, String key, int by) {
        return countRedisTemplate.opsForValue().increment(key, -by).intValue();
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 18:23
     * description: 递增
     */
    public static int incrInt(RedisTemplate countRedisTemplate, String key, int by) {
        return new Double(countRedisTemplate.boundValueOps(key).increment(by)).intValue();
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 16:30
     * description: 写入缓存,无过期时间，慎用此方法
     */
    public static boolean set(RedisTemplate redisTemplate, final String key, String val) {
        return set(redisTemplate, key, val, null);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 16:23
     * description: 默认30分钟过期，如无特殊需求，请调用此常规方法设置缓存
     */
    public static boolean setValExpire(RedisTemplate redisTemplate, final String key, String val) {
        return set(redisTemplate, key, val, expireTime);
    }

    /**
     * 写入缓存，自定义过期时间，慎用此犯法
     *
     * @param key
     * @param val
     * @param expireTime
     * @return
     */
    public static boolean set(RedisTemplate redisTemplate, final String key, Object val, Integer expireTime) {
        boolean result = false;
        try {
            BoundValueOperations<String, String> boundValOps = redisTemplate.boundValueOps(key);
            boundValOps.set(val + "");
            if (expireTime != null) {
                redisTemplate.expire(key, expireTime.intValue(), TimeUnit.SECONDS);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 16:20
     * description: 获取返回list集合,所有
     */
    public static <T> List<T> getList(RedisTemplate redisTemplate, String key) {
        return getListRange(redisTemplate, key, 0, -1);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 16:20
     * description: 获取返回list集合,查询集合范围从start开始到end结束，如果查询所有则0, -1
     */
    public static <T> List<T> getListRange(RedisTemplate redisTemplate, String key, int start, int end) {
        BoundListOperations boundListOps = redisTemplate.boundListOps(key);
        return boundListOps.range(start, end);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 16:20
     * description: 索引获取集合
     */
    public static Object getListIndex(RedisTemplate redisTemplate, String key, Long index) {
        BoundListOperations boundListOps = redisTemplate.boundListOps(key);
        return boundListOps.index(index);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 16:20
     * description: 删除集合元素
     */
    public static Long removeList(RedisTemplate redisTemplate, String key, Long index, Object o) {
        BoundListOperations boundListOps = redisTemplate.boundListOps(key);
        return boundListOps.remove(index, o);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 15:58
     * description: 写入list 集合缓存，无过期时间，请慎用此方法
     */
    public static <T> void setLeftList(RedisTemplate redisTemplate, String key, List<T> list) {
        setLeftList(redisTemplate, key, list, null);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 15:58
     * description: 写入list 集合缓存(30分钟过期)，如无特殊需求，请调用此方法
     */
    public static <T> void setLeftListExpire(RedisTemplate redisTemplate, String key, List<T> list) {
        setLeftList(redisTemplate, key, list, expireTime);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 15:58
     * description: 写入list 集合缓存, 有过期时间，请慎用此方法
     */
    public static <T> void setLeftList(RedisTemplate redisTemplate, String key, List<T> list, Integer expireTime) {
        BoundListOperations boundListOps = redisTemplate.boundListOps(key);
        if (null != list) {
            boundListOps.leftPushAll(list.toArray());
            if (expireTime != null) {
                redisTemplate.expire(key, expireTime.intValue(), TimeUnit.SECONDS);
            }
        }
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 20:36
     * description: 从列表右侧取出第一个元素，并且移除
     */
    public static <T> T popRightList(RedisTemplate redisTemplate, String key) {
        BoundListOperations<String, T> boundListOps = redisTemplate.boundListOps(key);
        return boundListOps.rightPop();
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 20:36
     * description: 从列表左侧取出第一个元素，并且移除
     */
    public static <T> T popLeftList(RedisTemplate redisTemplate, String key) {
        BoundListOperations<String, T> boundListOps = redisTemplate.boundListOps(key);
        return boundListOps.leftPop();
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 15:58
     * description: 写入list 集合缓存，无过期时间，请慎用此方法
     */
    public static <T> void setRightList(RedisTemplate redisTemplate, String key, List<T> list) {
        setRightList(redisTemplate, key, list, null);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 15:58
     * description: 写入list 集合缓存(30分钟过期)，如无特殊需求，请调用此方法
     */
    public static <T> void setRightListExpire(RedisTemplate redisTemplate, String key, List<T> list) {
        setRightList(redisTemplate, key, list, expireTime);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 15:58
     * description: 写入list 集合缓存, 有过期时间，请慎用此方法
     */
    public static <T> void setRightList(RedisTemplate redisTemplate, String key, List<T> list, Integer expireTime) {
        BoundListOperations boundListOps = redisTemplate.boundListOps(key);
        if (null != list && !list.isEmpty()) {
            if (expireTime != null) {
                redisTemplate.expire(key, expireTime.intValue(), TimeUnit.SECONDS);
            }
            boundListOps.rightPushAll(list.toArray());
        }
    }

    /**
     * User: 
     * CreateDate: 2018/11/2 14:04
     * description: 根据索引更新list中value
     */
    public static <T> void setListValueByIndex(RedisTemplate redisTemplate, String key, Integer index, Object o) {
        BoundListOperations boundListOps = redisTemplate.boundListOps(key);
        boundListOps.set(index, o);
    }
    /**
     * User: 
     * CreateDate: 2017/5/17 14:01
     * description: 获取map缓存数据类型
     */
    public static <T> Map<String, T> getMap(RedisTemplate redisTemplate, String key) {
        Map<String, T> map = redisTemplate.boundHashOps(key).entries();
        return map;
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 14:01
     * description: 写入map缓存类型，无过期时间，请慎用此方法
     */
    public static <T> BoundHashOperations<String, String, T> setMap(RedisTemplate redisTemplate, String key, String field, T obj) {
        return setMap(redisTemplate, key, field, obj, null);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 14:01
     * description: 写入map缓存类型，无过期时间，请慎用此方法
     */
    public static <T> BoundHashOperations<String, String, T> setMapExpire(RedisTemplate redisTemplate, String key, String field, T obj) {
        return setMap(redisTemplate, key, field, obj, expireTime);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 14:01
     * description: 写入map缓存类型，自定义过期时间，如无特殊需求，请慎用
     */
    public static <T> BoundHashOperations<String, String, T> setMap(RedisTemplate redisTemplate, String key, String field, T obj, Integer expireTime) {
        BoundHashOperations boundHashOps = null;
        try {
            boundHashOps = redisTemplate.boundHashOps(key);
            boundHashOps.put(field, obj);
            if (expireTime != null) {
                redisTemplate.expire(key, expireTime.intValue(), TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return boundHashOps;
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 14:02
     * description: 写入map缓存类型,无过期时间，如无特殊需求，不建议调用此方法
     */
    public static <T> BoundHashOperations<String, String, T> setMap(RedisTemplate redisTemplate, String key, Map<String, T> map) {
        return setMap(redisTemplate, key, map, null);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 14:02
     * description: 写入map缓存类型,默认过期时间30分钟
     */
    public static <T> BoundHashOperations<String, String, T> setMapExpire(RedisTemplate redisTemplate, String key, Map<String, T> map) {
        return setMap(redisTemplate, key, map, expireTime);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 14:02
     * description: 写入map缓存类型，自定义过期时间，如无特殊需求，不建议调用此方法
     */
    public static <T> BoundHashOperations<String, String, T> setMap(RedisTemplate redisTemplate, String key, Map<String, T> map, Integer expireTime) {
        BoundHashOperations boundHashOps = null;
        try {
            boundHashOps = redisTemplate.boundHashOps(key);
            if (null != map) {
                for (Map.Entry<String, T> entry : map.entrySet()) {
                    boundHashOps.put(entry.getKey(), entry.getValue());
                }
                if (expireTime != null) {
                    redisTemplate.expire(key, expireTime.intValue(), TimeUnit.SECONDS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return boundHashOps;
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 14:02
     * description: 写入map缓存类型,自定义缓存时间，如无特殊需求，不建议调用此方法
     */
    public static <T> BoundHashOperations<String, String, T> setMapInteger(RedisTemplate redisTemplate, String key, Map<Integer, T> map) {
        return setMapInteger(redisTemplate, key, map, null);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 14:02
     * description: 写入map缓存类型,自定义缓存时间，如无特殊需求，不建议调用此方法
     */
    public static <T> BoundHashOperations<String, String, T> setMapIntegerExpire(RedisTemplate redisTemplate, String key, Map<Integer, T> map) {
        return setMapInteger(redisTemplate, key, map, expireTime);
    }

    /**
     * User: 
     * CreateDate: 2017/5/17 14:02
     * description: 写入map缓存类型,自定义缓存时间，如无特殊需求，不建议调用此方法
     */
    public static <T> BoundHashOperations<String, String, T> setMapInteger(RedisTemplate redisTemplate, String key, Map<Integer, T> map, Integer expireTime) {
        BoundHashOperations boundHashOps = null;
        try {
            boundHashOps = redisTemplate.boundHashOps(key);
            if (null != map) {
                for (Map.Entry<Integer, T> entry : map.entrySet()) {
                    boundHashOps.put(entry.getKey(), entry.getValue());
                }
                if (expireTime != null) {
                    redisTemplate.expire(key, expireTime.intValue(), TimeUnit.SECONDS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return boundHashOps;
    }

    /**
     * 发布消息
     *
     * @param redisTemplate
     * @param channel
     * @param message
     */
    public static void sendMessage(RedisTemplate redisTemplate, String channel, String message) {
        redisTemplate.convertAndSend(channel, message);
    }

    /**
     * 新增Zset
     *
     * @param redis
     * @param key
     * @param score
     * @param val
     * @return
     */
    public static boolean addZSet(RedisTemplate redis, String key, long score, String val, Integer expireTime) {
        BoundZSetOperations bound = redis.boundZSetOps(key);
        bound.expire(expireTime, TimeUnit.SECONDS);
        bound.add(val, score);
        return true;
    }

    /**
     * 新增Zset
     *
     * @param redis
     * @param key
     * @param val
     * @return
     */
    public static boolean addZSet(RedisTemplate redis, String key, Set<DefaultTypedTuple> val, Integer expireTime) {
        BoundZSetOperations bound = redis.boundZSetOps(key);
        bound.expire(expireTime, TimeUnit.SECONDS);
        bound.add(val);
        return true;
    }

    /**
     * 新增Zset
     *
     * @param redis
     * @param key
     * @param score
     * @param val
     * @return
     */
    public static boolean addZSetExpire(RedisTemplate redis, String key, long score, String val) {
        BoundZSetOperations bound = redis.boundZSetOps(key);
        bound.expire(expireTime, TimeUnit.SECONDS);
        bound.add(val, score);
        return true;
    }

    /**
     * 删除Zset
     *
     * @param redis
     * @param key
     * @param score
     * @param val
     * @return
     */
    public static boolean removeZSet(RedisTemplate redis, String key, long score, String... val) {
        BoundZSetOperations bound = redis.boundZSetOps(key);
        bound.remove(val);
        return true;
    }

    /**
     * 删除Zset
     *
     * @param redis
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static boolean removeZSetRange(RedisTemplate redis, String key, long start, long end) {
        BoundZSetOperations bound = redis.boundZSetOps(key);
        bound.removeRange(start, end);
        return true;
    }

  
    public static Object hget(RedisTemplate redisTemplate, String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

  
    public static void hset(RedisTemplate redisTemplate, String key, String item, Object value) {
        redisTemplate.opsForHash().put(key, item, value);
    }

  
    public static List<String> hget(RedisTemplate redisTemplate, String key) {
        return redisTemplate.opsForHash().values(key);
    }

  
    public static Long hdel(RedisTemplate redisTemplate, String key, Object... item) {
        return redisTemplate.opsForHash().delete(key, item);
    }
}
