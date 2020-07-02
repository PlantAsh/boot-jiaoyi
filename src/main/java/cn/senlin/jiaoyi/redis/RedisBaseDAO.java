package cn.senlin.jiaoyi.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis操作基础类，包含一些简单操作
 *
 * @author wusen
 * @date 2020-04-09
 */
@Component
public class RedisBaseDAO {
    Logger log = LoggerFactory.getLogger(RedisBaseDAO.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 锁拥有时间
     */
    private static int LOCK_EXPIRE = 1000 * 5;

    /**
     * 最终加强分布式锁
     *
     * @param key key值
     * @return 是否获取到
     */
    public boolean lock(String key) {
        // 利用lambda表达式
        return (Boolean) redisTemplate.execute((RedisCallback<Object>) redisConnection -> {
            long expireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
            Boolean acquire = redisConnection.setNX(key.getBytes(), String.valueOf(expireAt).getBytes());
            if (acquire) {
                return true;
            } else {
                byte[] value = redisConnection.get(key.getBytes());
                if (Objects.nonNull(value) && value.length > 0) {
                    long expireTime = Long.parseLong(new String(value));
                    if (expireTime < System.currentTimeMillis()) {
                        // 如果锁已经过期
                        byte[] oldValue = redisConnection.getSet(key.getBytes(), String.valueOf(System.currentTimeMillis() + LOCK_EXPIRE + 1).getBytes());
                        // 防止死锁
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    /**
     * 删除锁
     *
     * @param key
     */
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("RedisBaseDAO 删除锁异常：", e);
        }
    }

    /**
     * 指定缓存失效时间
     *
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisBaseDAO 指定缓存失效时间异常：", e);
            return false;
        }
    }

    /**
     * 根据key获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("RedisBaseDAO 根据key获取过期时间异常：", e);
            return -1;
        }
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("RedisBaseDAO 判断key是否存在异常：", e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        try {
            if (key != null && key.length > 0) {
                if(key.length == 1) {
                    redisTemplate.delete(key[0]);
                } else {
                    redisTemplate.delete(CollectionUtils.arrayToList(key));
                }
            }
        } catch (Exception e) {
            log.error("RedisBaseDAO 删除缓存异常：", e);
        }
    }

    /*------------------String操作---------------------*/

    /**
     * 获取String缓存
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        try {
            return key == null ? null : redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("RedisBaseDAO 获取String缓存异常：", e);
            return null;
        }
    }

    /**
     * 添加String缓存
     *
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        return set(key, value, 0);
    }

    /**
     * 添加String缓存并设置过期时间
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisBaseDAO 添加String缓存并设置过期时间异常：", e);
            return false;
        }
    }

    /**
     * String递增/递减，整数型
     *
     * @param key 键
     * @return
     */
    public long increment(String key, long delta) {
        try {
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            log.error("RedisBaseDAO String递增/递减，整数型异常：", e);
            return 0;
        }
    }

    /**
     * String递增/递减，浮点型
     *
     * @param key 键
     * @return
     */
    public double increment(String key, double delta) {
        try {
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            log.error("RedisBaseDAO String递增/递减，浮点型异常：", e);
            return 0;
        }
    }

    /*------------------hash操作---------------------*/

    /**
     * HashGet
     *
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        try {
            return redisTemplate.opsForHash().get(key, item);
        } catch (Exception e) {
            log.error("RedisBaseDAO HashGet异常：", e);
            return null;
        }
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object,Object> hmget(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            log.error("RedisBaseDAO 获取hashKey对应的所有键值异常：", e);
            return null;
        }
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String,Object> map){
        return hmset(key, map, 0);
    }

    /**
     * HashSet 并设置时间
     *
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String,Object> map, long time){
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisBaseDAO HashSet并设置时间异常：", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("RedisBaseDAO 向一张hash表中放入数据,如果不存在将创建异常：", e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        try {
            redisTemplate.opsForHash().delete(key, item);
        } catch (Exception e) {
            log.error("RedisBaseDAO 删除hash表中的值异常：", e);
        }
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        try {
            return redisTemplate.opsForHash().hasKey(key, item);
        } catch (Exception e) {
            log.error("RedisBaseDAO 判断hash表中是否有该项的值异常：", e);
            return false;
        }
    }

    /**
     * hash递增/递减，整数型
     *
     * @param key 键
     * @param item 项
     * @param delta 要增加/减少几
     * @return
     */
    public long hincrement(String key, String item, long delta) {
        try {
            return redisTemplate.opsForHash().increment(key, item, delta);
        } catch (Exception e) {
            log.error("RedisBaseDAO hash递增/递减，整数型异常：", e);
            return 0;
        }
    }

    /**
     * hash递增/递减，浮点型
     *
     * @param key 键
     * @param item 项
     * @param delta 要增加/减少几
     * @return
     */
    public double hincrement(String key, String item, double delta) {
        try {
            return redisTemplate.opsForHash().increment(key, item, delta);
        } catch (Exception e) {
            log.error("RedisBaseDAO hash递增/递减，浮点型异常：", e);
            return 0;
        }
    }

    /*------------------List操作---------------------*/

    /**
     * 获取list缓存的内容
     *
     * @param key 键
     * @param start 开始
     * @param end 结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> lRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("RedisBaseDAO 获取list缓存的内容异常：", e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("RedisBaseDAO 获取list缓存的长度异常：", e);
            return 0;
        }
    }

    /**
     * 通过索引获取list中的值
     *
     * @param key 键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("RedisBaseDAO 通过索引获取list中的值异常：", e);
            return null;
        }
    }

    /**
     * list将值放入队尾
     *
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean rlPush(String key, Object value) {
        return rlPush(key, value, 0);
    }

    /**
     * list将值放入队尾并设置过期时间
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean rlPush(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisBaseDAO list将值放入队尾并设置过期时间异常：", e);
            return false;
        }
    }

    /**
     * list将值放入队头
     *
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean llPush(String key, Object value) {
        return llPush(key, value, 0);
    }

    /**
     * list将值放入队头并设置过期时间
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean llPush(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisBaseDAO list将值放入队头并设置过期时间异常：", e);
            return false;
        }
    }

    /**
     * 将list放入缓存，按顺序插入队尾
     *
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean rlPush(String key, List<Object> value) {
        return rlPush(key, value, 0);
    }

    /**
     * 将list放入缓存并设置过期时间，按顺序插入队尾
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean rlPush(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisBaseDAO 将list放入缓存并设置过期时间，按顺序插入队尾异常：", e);
            return false;
        }
    }

    /**
     * 将list放入缓存，按顺序插入队头
     *
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean llPush(String key, List<Object> value) {
        return llPush(key, value, 0);
    }

    /**
     * 将list放入缓存并设置过期时间，按顺序插入队头
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean llPush(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisBaseDAO 将list放入缓存并设置过期时间，按顺序插入队头异常：", e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lSet(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("RedisBaseDAO 根据索引修改list中的某条数据异常：", e);
            return false;
        }
    }

    /**
     * 移除list中N个值为value的数据
     *
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            log.error("RedisBaseDAO 移除list中N个值为value的数据异常：", e);
            return 0;
        }
    }

    /*------------------Set操作---------------------*/

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sMembers(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("RedisBaseDAO 根据key获取Set中的所有值异常：", e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询是否存在
     *
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("RedisBaseDAO 根据value从一个set中查询是否存在异常：", e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sAdd(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("RedisBaseDAO 将数据放入set缓存异常：", e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("RedisBaseDAO 获取set缓存的长度异常：", e);
            return 0;
        }
    }

    /**
     * set移除值为value
     *
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long sRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            log.error("RedisBaseDAO set移除值为value异常：", e);
            return 0;
        }
    }

    /*------------------ZSet操作---------------------*/

    /**
     * 获取zset正序集合
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public Set<Object> zRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            log.error("RedisBaseDAO 获取zset正序集合异常：", e);
            return null;
        }
    }

    /**
     * 获取zset倒序集合
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public Set<Object> zReverseRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().reverseRange(key, start, end);
        } catch (Exception e) {
            log.error("RedisBaseDAO 获取zset倒序集合异常：", e);
            return null;
        }
    }

    /**
     * zset新增值
     *
     * @param key 键
     * @param value 值
     * @param score 分数
     * @return 是否成功
     */
    public boolean zAdd(String key, Object value, double score) {
        try {
            return redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            log.error("RedisBaseDAO zset新增值异常：", e);
            return false;
        }
    }

    /**
     * zset新增有序集合
     *
     * @param key 键
     * @param tuples 有序集合
     * @return 返回成功数量
     */
    public long zAdd(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        try {
            return redisTemplate.opsForZSet().add(key, tuples);
        } catch (Exception e) {
            log.error("RedisBaseDAO zset新增有序集合异常：", e);
            return 0;
        }
    }

    /**
     * zset移除一个或多个元素
     *
     * @param key 键
     * @param values 值 可传多个
     * @return 返回成功数量
     */
    public long zRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForZSet().remove(key, values);
        } catch (Exception e) {
            log.error("RedisBaseDAO zset移除一个或多个元素异常：", e);
            return 0;
        }
    }

    /**
     * zset移除指定索引范围的元素
     *
     * @param key 键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return 返回成功数量
     */
    public long zRemoveRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().removeRange(key, start, end);
        } catch (Exception e) {
            log.error("RedisBaseDAO zset移除指定索引范围的元素异常：", e);
            return 0;
        }
    }

    /**
     * zset移除指定分数区间的元素
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 返回成功数量
     */
    public long zRemoveRangeByScore(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
        } catch (Exception e) {
            log.error("RedisBaseDAO zset移除指定索引范围的元素异常：", e);
            return 0;
        }
    }

    /**
     * zset增加/减少元素的score
     *
     * @param key
     * @param value
     * @param score
     * @return 返回操作后的值
     */
    public double zIncrementScore(String key, Object value, double score) {
        try {
            return redisTemplate.opsForZSet().incrementScore(key, value, score);
        } catch (Exception e) {
            log.error("RedisBaseDAO zset增加/减少元素的score异常：", e);
            return 0;
        }
    }

    /**
     * zset返回集合中元素的正序排名
     *
     * @param key 键
     * @param value 值
     * @return
     */
    public long zRank(String key, Object value) {
        try {
            return redisTemplate.opsForZSet().rank(key, value);
        } catch (Exception e) {
            log.error("RedisBaseDAO zset返回集合中元素的正序排名异常：", e);
            return -1;
        }
    }

    /**
     * zset返回集合中元素的倒序排名
     *
     * @param key 键
     * @param value 值
     * @return
     */
    public long zReverseRank(String key, Object value) {
        try {
            return redisTemplate.opsForZSet().reverseRank(key, value);
        } catch (Exception e) {
            log.error("RedisBaseDAO zset返回集合中元素的倒序排名异常：", e);
            return -1;
        }
    }

    /**
     * zset返回集合指定分数区间内的成员个数
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return
     */
    public long zCount(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().count(key, min, max);
        } catch (Exception e) {
            log.error("RedisBaseDAO zset返回集合指定分数区间内的成员个数异常：", e);
            return 0;
        }
    }

    /**
     * zset获取集合的成员数
     *
     * @param key 键
     * @return
     */
    public long zCard(String key) {
        try {
            return redisTemplate.opsForZSet().zCard(key);
        } catch (Exception e) {
            log.error("RedisBaseDAO zset获取集合的成员数异常：", e);
            return 0;
        }
    }

}
