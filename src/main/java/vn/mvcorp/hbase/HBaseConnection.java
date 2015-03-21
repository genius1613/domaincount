package vn.mvcorp.hbase;

/**
 * Created by tienbm on 03/10/2014.
 */

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;


import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.util.Bytes;
import vn.mvcorp.common.Config;
import vn.mvcorp.common.TableUtil;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;


/**
 * Created by hadoop on 24/09/2014.
 */
public class HBaseConnection {
    private HTable hTable;
    public HBaseConnection() {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", Config.HBASE_SERVER);
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("zookeeper.znode.parent", "/hbase-unsecure");
        try {
            hTable = new HTable(conf, "webpage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewUrl(String url) throws IOException {
        String reversedUrl = TableUtil.reverseUrl(url);
        int interval = 2592000;

        Put put = new Put(Bytes.toBytes(reversedUrl));
        put.add(Bytes.toBytes("f"), Bytes.toBytes("fi"), Bytes.toBytes(interval));
        put.add(Bytes.toBytes("f"), Bytes.toBytes("ts"), Bytes.toBytes(System.currentTimeMillis()));

        put.add(Bytes.toBytes("mk"), Bytes.toBytes("_injmrk_"), Bytes.toBytes("y"));
        put.add(Bytes.toBytes("mk"), Bytes.toBytes("dist"), Bytes.toBytes("0"));

        put.add(Bytes.toBytes("mtdt"), Bytes.toBytes("_csh_"), Bytes.toBytes(1.0f));
        put.add(Bytes.toBytes("s"), Bytes.toBytes("s"), Bytes.toBytes(1.0f));
        hTable.put(put);
    }

    public int getDomainCount() throws IOException {
        Scan s = new Scan();
//        s.addColumn(Bytes.toBytes("f"), Bytes.toBytes("bas"));
        s.setFilter(new FirstKeyOnlyFilter());
        Integer count = 0;
        ResultScanner scanner = hTable.getScanner(s);
        HashSet<String> hashSet = new HashSet();
        URL url;
        try {
            for (Result rs = scanner.next(); rs != null; rs=scanner.next()) {
                count += 1;
                String urls = Bytes.toString(rs.getRow());

                url = new URL(TableUtil.unreverseUrl(urls));
                hashSet.add(url.getHost());
            }

            System.out.println("Found domain : " + hashSet.size());
            for(String domain:hashSet){
                System.out.println(domain);
            }


            return hashSet.size();
        } finally {
            scanner.close();
        }
    }

}
