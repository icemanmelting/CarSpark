package pt.iceman.carspark

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.cassandra.CassandraSQLContext

/**
  * Created by iCeMan on 02/12/2016.
  */
class CarBatchTask {
  var conf: SparkConf = _
  var sc: SparkContext = _
  var csc: CassandraSQLContext = _

  def init() = {
    conf = new SparkConf()
      .set("spark.cassandra.connection.host", "192.168.0.178")
      .setMaster("local[*]")
      .setAppName("CarSpark")

    sc = new SparkContext(conf)
    csc = new CassandraSQLContext(sc)
    csc.setKeyspace("maindata")
  }

  def run(args: Array[String]): Unit = ???
}
