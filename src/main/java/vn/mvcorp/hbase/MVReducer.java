package vn.mvcorp.hbase;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
/**
 * Created by tienbm on 03/10/2014.
 */
public abstract class MVReducer<KEYIN, VALUEIN, KEYOUT> extends
        Reducer<KEYIN, VALUEIN, KEYOUT, Writable> {
}
