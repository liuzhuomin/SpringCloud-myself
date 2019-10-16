package xinrui.cloud.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;

/**
 * @author Jihy
 */
@Service public class ShardedJedisPoolService {

	private static Logger logger = LoggerFactory.getLogger(ShardedJedisPoolService.class);

	private String storeServer = "127.0.0.1:6379";
//	private String storeServer = "10.0.0.58:6379";

	private String storePwd = "abc123";
	//private String storePwd = "foobared";

	private Integer cachePoolMaxActive = 200;

	private Integer cachePoolMaxIdle = 50;

	private Long cachePoolMaxWait = 2000L;

	private Boolean cachePoolTestOnBorrow = false;

	private JedisPoolConfig jedisPoolConfig = null;

	private redis.clients.jedis.ShardedJedisPool ShardedJedisPool = null;

	public static final int MIN = 60;

	public static final int TEN_MIN = 600;

	public static final int DAY = 86400;

	public static final int WEEK = 604800;

	public static final int HOUR = 3600;

	public static final int MONTH = 2592000;

	private void initJedisPoolConfig() {
		jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(cachePoolMaxActive);
		jedisPoolConfig.setMaxIdle(cachePoolMaxIdle);
		jedisPoolConfig.setMaxWaitMillis(cachePoolMaxWait);
		jedisPoolConfig.setTestOnBorrow(cachePoolTestOnBorrow);
	}

	@PostConstruct
	public void init() {
		logger.debug("cacheServer = "+storeServer);
		if(StringUtils.isNotBlank(storeServer)) {
			try{
				initJedisPoolConfig();
				logger.debug("cachePwd = "+storePwd);
				String[] hostPorts = storeServer.split(",");
				List<JedisShardInfo> JedisShardInfoList = new ArrayList<JedisShardInfo>();
				for(String portHost : hostPorts){
					String[] hp = portHost.split(":");
					JedisShardInfo jedisShardInfo = new JedisShardInfo(hp[0].trim(),Integer.parseInt(hp[1].trim()));
					if(StringUtils.isNotBlank(storePwd)) {
						jedisShardInfo.setPassword(storePwd);
					}
					JedisShardInfoList.add(jedisShardInfo);
				}
				ShardedJedisPool = new ShardedJedisPool(jedisPoolConfig, JedisShardInfoList);
				logger.info("cache 初始化成功");
			}catch(Exception e){
				logger.error("cache 初始化失败", e);
			}
		}
	}

	/**
	 * 获取缓存服务器连接对象
	 *
	 * @return
	 */
	public ShardedJedis getConn() {
		ShardedJedis jedis = null;
		try {
			jedis = ShardedJedisPool.getResource();
		} catch (Exception e) {
			logger.error("获取缓存连接出现异常", e);
		}
		return jedis;
	}

	public void closeConn(ShardedJedis jedis) {
		if (jedis != null) {
			ShardedJedisPool.returnResource(jedis);
		}
	}



	public Long expire(String key, Integer seconds) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.expire(key, seconds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return 0L;
	}

	public void setex(String key, String value, int seconds) {
		ShardedJedis j = null;
		try {
			j = getConn();
			j.setex(key, seconds, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
	}







	public String get(String key) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.get(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return null;
	}

	public List<String> get(String... key) {
		ShardedJedis j = null;
		try {
			j = getConn();
			List<String> list = new ArrayList<String>();
			for (String k : key) {
				String v = j.get(k);
				if (StringUtils.isNotEmpty(v)) {
                    list.add(v);
                }
			}
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return null;
	}

	public void del(String key) {
		ShardedJedis j = null;
		try {
			j = getConn();
			j.del(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
	}

	public void lpush(String key, int seconds, String... values) {
		ShardedJedis j = null;
		try {
			j = getConn();
			j.lpush(key, values);
			j.expire(key, seconds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
	}

	public void rpush(String key, int seconds, String... values) {
		ShardedJedis j = null;
		try {
			j = getConn();
			j.rpush(key, values);
			j.expire(key, seconds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
	}

	public List<String> lrange(String key, long start, long end) {
		ShardedJedis j = null;
		try {
			j = getConn();
			List<String> list = j.lrange(key, start, end);
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return null;
	}

	public String lindex(String key, long index) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.lindex(key, index);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return null;
	}

	public Long lrem(String key, long count, String value) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.lrem(key, count, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return 0L;
	}

	public long llen(String key) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.llen(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return -1;
	}

	public String lpop(String key) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.lpop(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return null;
	}

	public List<String> lpop(String key, int size) {
		ShardedJedis j = null;
		try {
			j = getConn();
			List<String> list = null;
			for (int i = 0; i < size; i++) {
				String str = j.lpop(key);
				if (list == null) {
                    list = new ArrayList<String>(size);
                }
				list.add(str);
			}

			return list;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return null;
	}

	public Long counterIncr(String key, int expire) {
		ShardedJedis j = null;
		try {
			Long count = null;
			j = getConn();
			count = j.incr(key);
			if (count == 1) {
				j.expire(key, expire);
			}
			return count;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return null;
	}

	public Long counterIncrBy(String key, Long value, int expire) {
		ShardedJedis j = null;
		try {
			Long count = null;
			j = getConn();
			count = j.incrBy(key, value);
			if (count == 1) {
				j.expire(key, expire);
			}
			return count;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return null;
	}

	public Long getTTL(String key) {
		ShardedJedis j = null;
		try {
			Long expireSeconds = null;
			j = getConn();
			expireSeconds = j.ttl(key);
			return expireSeconds;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return null;
	}

	public Long zadd(String key, double score, String member) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.zadd(key, score, member);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return 0L;
	}
	public Set<String> zrangeByScore(String key, double min, double max) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.zrangeByScore(key, min, max);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return null;
	}

	public Long zcard(String key) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.zcard(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return 0L;
	}

	public Long scard(String key) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.scard(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return 0L;
	}

	public String spop(String key) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.spop(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return null;
	}

	public Long srem(String key, String ... members) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.srem(key, members);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return 0L;
	}

	public Long sadd(String key, String ...members) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.sadd(key, members);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return 0L;
	}

	public Set<String> smembers(String key) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.smembers(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return new HashSet<String>();
	}

	public String srandmember(String key) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.srandmember(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return null;
	}

	public Set<String> zrevrange(String key, long start, long end) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.zrevrange(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return new LinkedHashSet<String>();
	}

	public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.zrevrangeWithScores(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return new LinkedHashSet<Tuple>();
	}

	public Set<String> zrange(String key, long start, long end) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.zrange(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return new LinkedHashSet<String>();
	}

	public Long zremrangeByRank(String key, long start, long end) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.zremrangeByRank(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return 0L;
	}

	public Long zrevrank(String key, String member) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.zrevrank(key, member);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return 0L;
	}

	public Long zcount(String key, Double min, Double max) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.zcount(key, min, max);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return 0L;
	}

	public Long zcount(String key, String min, String max) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.zcount(key, min, max);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return 0L;
	}

	public Double zscore(String key, String member) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.zscore(key, member);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return 0D;
	}

	public Double zincrby(String key, double score, String member) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.zincrby(key, score, member);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return 0D;
	}

	public long zrem(String key, String... members) {
		ShardedJedis j = null;
		try {
			j = getConn();
			return j.zrem(key, members);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return 0;
	}

	/**
	 * 判断sismember是否是有序集合key里面的member
	 * @param key
	 * @param member
	 * @return
	 */
	public boolean zrank(String key, String member){
		ShardedJedis j = null;
		try {
			j = getConn();
			System.out.println("key :" + key + " | member : " + member);
			System.out.println("j.zrank(key, member)：" + j.zrank(key, member));
			return j.zrank(key, member) != null ? true : false;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeConn(j);
		}
		return false;
	}


	public String getStoreServer() {
		return storeServer;
	}

	public void setStoreServer(String storeServer) {
		this.storeServer = storeServer;
	}
}
