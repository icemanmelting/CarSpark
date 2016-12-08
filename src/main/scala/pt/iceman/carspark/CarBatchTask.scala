package pt.iceman.carspark

import com.datastax.driver.core.{Cluster, Session}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * Created by iCeMan on 02/12/2016.
  */
trait CarBatchTask {
  var conf: SparkConf = _
  var ss: SparkSession = _

  def init(hostName: String) = {
    conf = new SparkConf()
      .set("spark.cassandra.connection.host", hostName)
      .setMaster("local[*]")
      .setAppName("CarSpark")

    ss = SparkSession.builder().config(conf).getOrCreate()
  }

  def run(args: Array[String]): Unit = ???
}
