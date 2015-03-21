package vn.mvcorp.hbase;


import java.io.IOException;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import vn.mvcorp.common.Config;
import vn.mvcorp.common.RedisConnection;
import vn.mvcorp.common.TableUtil;

import java.net.URL;
import java.util.HashSet;

/**
 * Created by tienbm on 03/10/2014.
 */
public class UrlCounting {

    /**
     * Ham mapper thuc hien doc du lieu tu HBase va dua ra  danh sach cac domain va page tren moi input split va tra lai du lieu 2 cap khoa
     * <domainKey, domainName> va <pageKey, page>
     */
    public static class Mapper1 extends TableMapper<ImmutableBytesWritable, IntWritable> {

        private final static IntWritable one = new IntWritable(1);

        @Override
        public void map(ImmutableBytesWritable row, Result values, Context context) throws IOException {
            String url = new URL(TableUtil.unreverseUrl(Bytes.toString(values.getRow()))).getHost();
//            System.out.println(Bytes.toString(values.getRow()));

            ImmutableBytesWritable domainKey = new ImmutableBytesWritable(Bytes.toBytes(url));
            try {
//                domain.set(url);
                context.write(domainKey, one);
            } catch (InterruptedException e) {
                throw new IOException(e);
            }
        }
    }

    /**
     * Ham reduce thuc hien viec dem so luong domain va page trong he thong va ghi vao Redis
     * <"crawledDomain, numberDomain />
     * <crawledPage, numberPage/>
     */

    public static class Reducer1 extends TableReducer<ImmutableBytesWritable, IntWritable, ImmutableBytesWritable> {
//        private RedisConnection redisConnection = new RedisConnection();
        public void reduce(ImmutableBytesWritable key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
//            HashSet<String> hashSet = new HashSet();
            int sum =0;
            for (IntWritable val : values) {
                sum+= val.get();
            }
            Put put = new Put(key.get());
            put.add(Bytes.toBytes("result"), Bytes.toBytes("total"), Bytes.toBytes(sum));
            System.out.println(String.format("stats :   key : %s,  count : %d", new String(key.get()), sum));
//            redisConnection.addCrawlInfo(new String(key.get()), String.valueOf(hashSet.size()) );
            context.write(key, put);
        }
    }

    public static void main(String[] args) throws Exception {
       UrlCounting urlCounting = new UrlCounting();
        urlCounting.doCounting();
    }

    public void doCounting() throws IOException, InterruptedException, ClassNotFoundException{
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", Config.HBASE_SERVER);
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("zookeeper.znode.parent", "/hbase-unsecure");

        Job job = new Job(conf, "Hbase Url Counting");
        job.setJarByClass(UrlCounting.class);
        Scan scan = new Scan();
        scan.setFilter(new FirstKeyOnlyFilter());
        TableMapReduceUtil.initTableMapperJob("webpage", scan, Mapper1.class, ImmutableBytesWritable.class,
                IntWritable.class, job);

        TableMapReduceUtil.initTableReducerJob("urlsummary", Reducer1.class, job);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
