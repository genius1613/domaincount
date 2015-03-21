package vn.mvcorp.common;

/**
 * Created by tienbm on 03/10/2014.
 */
public class Config {
    public static final String MYSQL_URL = "jdbc:mysql://localhost:3306/logstash";
    public static final String MYSQL_USERNAME = "root";
    public static final String MYSQL_PASSWORD = "123456";
    //Elastic search
    public static final String LOGSTASH_HOST = "http://hadoop4.mvs.vn:9200/";
    //HBase
    public static final String HBASE_SERVER = "sv1.hadoop1.bvqd.vn.vn";

    // Nutch
    public static final String NUTCH = "/home/hadoop/Downloads/apache-nutch-1.8/runtime/local";

    // Redis
    public static final String REDIS_HOST = "hadoop2.mvcorp.vn";
    public static final int REDIS_PORT = 6379;
}
