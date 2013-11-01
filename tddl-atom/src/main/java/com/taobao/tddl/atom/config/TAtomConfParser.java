package com.taobao.tddl.atom.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.taobao.tddl.atom.jdbc.ConnRestrictEntry;
import com.taobao.tddl.common.utils.TStringUtil;
import com.taobao.tddl.common.utils.logger.Logger;
import com.taobao.tddl.common.utils.logger.LoggerFactory;

/**
 * TAtom数据源的推送配置解析类
 * 
 * @author qihao
 */
public class TAtomConfParser extends TAtomConfHelper {

    private static Logger logger = LoggerFactory.getLogger(TAtomConfParser.class);

    public static TAtomDsConfDO parserTAtomDsConfDO(String globaConfStr, String appConfStr) {
        TAtomDsConfDO pasObj = new TAtomDsConfDO();
        if (TStringUtil.isNotBlank(globaConfStr)) {
            Properties globaProp = parserConfStr2Properties(globaConfStr);
            if (!globaProp.isEmpty()) {
                String ip = TStringUtil.trim(globaProp.getProperty(TAtomConfParser.GLOBA_IP_KEY));
                if (TStringUtil.isNotBlank(ip)) {
                    pasObj.setIp(ip);
                }
                String port = TStringUtil.trim(globaProp.getProperty(TAtomConfParser.GLOBA_PORT_KEY));
                if (TStringUtil.isNotBlank(port)) {
                    pasObj.setPort(port);
                }
                String dbName = TStringUtil.trim(globaProp.getProperty(TAtomConfParser.GLOBA_DB_NAME_KEY));
                if (TStringUtil.isNotBlank(dbName)) {
                    pasObj.setDbName(dbName);
                }
                String dbType = TStringUtil.trim(globaProp.getProperty(TAtomConfParser.GLOBA_DB_TYPE_KEY));
                if (TStringUtil.isNotBlank(dbType)) {
                    pasObj.setDbType(dbType);
                }
                String dbStatus = TStringUtil.trim(globaProp.getProperty(TAtomConfParser.GLOBA_DB_STATUS_KEY));
                if (TStringUtil.isNotBlank(dbStatus)) {
                    pasObj.setDbStatus(dbStatus);
                }
            }
        }
        if (TStringUtil.isNotBlank(appConfStr)) {
            Properties appProp = TAtomConfParser.parserConfStr2Properties(appConfStr);
            if (!appProp.isEmpty()) {
                String userName = TStringUtil.trim(appProp.getProperty(TAtomConfParser.APP_USER_NAME_KEY));
                if (TStringUtil.isNotBlank(userName)) {
                    pasObj.setUserName(userName);
                }
                String oracleConType = TStringUtil.trim(appProp.getProperty(TAtomConfParser.APP_ORACLE_CON_TYPE_KEY));
                if (TStringUtil.isNotBlank(oracleConType)) {
                    pasObj.setOracleConType(oracleConType);
                }
                String minPoolSize = TStringUtil.trim(appProp.getProperty(TAtomConfParser.APP_MIN_POOL_SIZE_KEY));
                if (TStringUtil.isNotBlank(minPoolSize) && TStringUtil.isNumeric(minPoolSize)) {
                    pasObj.setMinPoolSize(Integer.valueOf(minPoolSize));
                }
                String maxPoolSize = TStringUtil.trim(appProp.getProperty(TAtomConfParser.APP_MAX_POOL_SIZE_KEY));
                if (TStringUtil.isNotBlank(maxPoolSize) && TStringUtil.isNumeric(maxPoolSize)) {
                    pasObj.setMaxPoolSize(Integer.valueOf(maxPoolSize));
                }
                String idleTimeout = TStringUtil.trim(appProp.getProperty(TAtomConfParser.APP_IDLE_TIMEOUT_KEY));
                if (TStringUtil.isNotBlank(idleTimeout) && TStringUtil.isNumeric(idleTimeout)) {
                    pasObj.setIdleTimeout(Long.valueOf(idleTimeout));
                }
                String blockingTimeout = TStringUtil.trim(appProp.getProperty(TAtomConfParser.APP_BLOCKING_TIMEOUT_KEY));
                if (TStringUtil.isNotBlank(blockingTimeout) && TStringUtil.isNumeric(blockingTimeout)) {
                    pasObj.setBlockingTimeout(Integer.valueOf(blockingTimeout));
                }
                String preparedStatementCacheSize = TStringUtil.trim(appProp.getProperty(TAtomConfParser.APP_PREPARED_STATEMENT_CACHE_SIZE_KEY));
                if (TStringUtil.isNotBlank(preparedStatementCacheSize)
                    && TStringUtil.isNumeric(preparedStatementCacheSize)) {
                    pasObj.setPreparedStatementCacheSize(Integer.valueOf(preparedStatementCacheSize));
                }

                String writeRestrictTimes = TStringUtil.trim(appProp.getProperty(TAtomConfParser.APP_WRITE_RESTRICT_TIMES));
                if (TStringUtil.isNotBlank(writeRestrictTimes) && TStringUtil.isNumeric(writeRestrictTimes)) {
                    pasObj.setWriteRestrictTimes(Integer.valueOf(writeRestrictTimes));
                }

                String readRestrictTimes = TStringUtil.trim(appProp.getProperty(TAtomConfParser.APP_READ_RESTRICT_TIMES));
                if (TStringUtil.isNotBlank(readRestrictTimes) && TStringUtil.isNumeric(readRestrictTimes)) {
                    pasObj.setReadRestrictTimes(Integer.valueOf(readRestrictTimes));
                }
                String threadCountRestrict = TStringUtil.trim(appProp.getProperty(TAtomConfParser.APP_THREAD_COUNT_RESTRICT));
                if (TStringUtil.isNotBlank(threadCountRestrict) && TStringUtil.isNumeric(threadCountRestrict)) {
                    pasObj.setThreadCountRestrict(Integer.valueOf(threadCountRestrict));
                }
                String timeSliceInMillis = TStringUtil.trim(appProp.getProperty(TAtomConfParser.APP_TIME_SLICE_IN_MILLS));
                if (TStringUtil.isNotBlank(timeSliceInMillis) && TStringUtil.isNumeric(timeSliceInMillis)) {
                    pasObj.setTimeSliceInMillis(Integer.valueOf(timeSliceInMillis));
                }

                String conPropStr = TStringUtil.trim(appProp.getProperty(TAtomConfParser.APP_CON_PROP_KEY));
                Map<String, String> connectionProperties = parserConPropStr2Map(conPropStr);
                if (null != connectionProperties && !connectionProperties.isEmpty()) {
                    pasObj.setConnectionProperties(connectionProperties);
                    String driverClass = connectionProperties.get(TAtomConfParser.APP_DRIVER_CLASS_KEY);
                    if (!TStringUtil.isBlank(driverClass)) {
                        pasObj.setDriverClass(driverClass);
                    }
                }

                // 解析应用连接限制, 参看下面的文档
                String connRestrictStr = TStringUtil.trim(appProp.getProperty(TAtomConfParser.APP_CONN_RESTRICT));
                List<ConnRestrictEntry> connRestrictEntries = parseConnRestrictEntries(connRestrictStr,
                    pasObj.getMaxPoolSize());
                if (null != connRestrictEntries && !connRestrictEntries.isEmpty()) {
                    pasObj.setConnRestrictEntries(connRestrictEntries);
                }
            }
        }
        return pasObj;
    }

    /**
     * HASH 策略的最大槽数量限制。
     */
    public static final int MAX_HASH_RESTRICT_SLOT = 32;

    /**
     * 解析应用连接限制, 完整格式是:
     * "K1,K2,K3,K4:80%; K5,K6,K7,K8:80%; K9,K10,K11,K12:80%; *:16,80%; ~:80%;"
     * 这样可以兼容 HASH: "*:16,80%", 也可以兼容 LIST:
     * "K1:80%; K2:80%; K3:80%; K4:80%; ~:80%;" 配置可以是连接数, 也可以是百分比。
     */
    public static List<ConnRestrictEntry> parseConnRestrictEntries(String connRestrictStr, int maxPoolSize) {
        List<ConnRestrictEntry> connRestrictEntries = null;
        if (TStringUtil.isNotBlank(connRestrictStr)) {
            // Split "K1:number1; K2:number2; ...; *:count,number3; ~:number4"
            String[] entries = TStringUtil.split(connRestrictStr, ";");
            if (null != entries && entries.length > 0) {
                HashMap<String, String> existKeys = new HashMap<String, String>();
                connRestrictEntries = new ArrayList<ConnRestrictEntry>(entries.length);
                for (String entry : entries) {
                    // Parse "K1,K2,K3:number | *:count,number | ~:number"
                    int find = entry.indexOf(':');
                    if (find >= 1 && find < (entry.length() - 1)) {
                        String key = entry.substring(0, find).trim();
                        String value = entry.substring(find + 1).trim();
                        // "K1,K2,K3:number | *:count,number | ~:number"
                        ConnRestrictEntry connRestrictEntry = ConnRestrictEntry.parseEntry(key, value, maxPoolSize);
                        if (connRestrictEntry == null) {
                            logger.error("[connRestrict Error] parse entry error: " + entry);
                        } else {
                            // Remark entry config problem
                            if (0 >= connRestrictEntry.getLimits()) {
                                logger.error("[connRestrict Error] connection limit is 0: " + entry);
                                connRestrictEntry.setLimits(/* 至少允许一个连接 */1);
                            }
                            if (ConnRestrictEntry.MAX_HASH_RESTRICT_SLOT < connRestrictEntry.getHashSize()) {
                                logger.error("[connRestrict Error] hash size exceed maximum: " + entry);
                                connRestrictEntry.setHashSize(ConnRestrictEntry.MAX_HASH_RESTRICT_SLOT);
                            }
                            // Remark Key config confliction
                            for (String slotKey : connRestrictEntry.getKeys()) {
                                if (!existKeys.containsKey(slotKey)) {
                                    existKeys.put(slotKey, entry);
                                } else if (ConnRestrictEntry.isWildcard(slotKey)) {
                                    logger.error("[connRestrict Error] hash config [" + entry + "] conflict with ["
                                                 + existKeys.get(slotKey) + "]");
                                } else if (ConnRestrictEntry.isNullKey(slotKey)) {
                                    logger.error("[connRestrict Error] null-key config [" + entry + "] conflict with ["
                                                 + existKeys.get(slotKey) + "]");
                                } else {
                                    logger.error("[connRestrict Error] " + slotKey + " config [" + entry
                                                 + "] conflict with [" + existKeys.get(slotKey) + "]");
                                }
                            }
                            connRestrictEntries.add(connRestrictEntry);
                        }
                    } else {
                        logger.error("[connRestrict Error] unknown entry: " + entry);
                    }
                }
            }
        }
        return connRestrictEntries;
    }

}