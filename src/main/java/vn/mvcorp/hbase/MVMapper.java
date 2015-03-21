package vn.mvcorp.hbase;

/**
 * Created by tienbm on 03/10/2014.
 */

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;


public abstract class MVMapper  extends
        Mapper<ImmutableBytesWritable, Result, ImmutableBytesWritable, IntWritable> {

}