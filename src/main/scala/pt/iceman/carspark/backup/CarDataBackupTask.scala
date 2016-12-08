package pt.iceman.carspark.backup

import com.datastax.spark.connector._
import com.datastax.spark.connector.cql.CassandraConnector
import org.apache.spark.sql.SparkSession
import pt.iceman.carspark.CarBatchTask

/**
  * Created by iCeMan on 02/12/2016.
  */
class CarDataBackupTask extends CarBatchTask {
  override def run(args: Array[String]): Unit = {
    if (args.length < 1) return

    val ss = SparkSession.builder().config(conf).getOrCreate()
    val connectorToOldCluster = CassandraConnector(conf)
    val connectorToNewCluster = CassandraConnector(conf.set("spark.cassandra.connection.host", "localhost"))

    args.foreach { table =>
      val rddFromOldCluster = {
        implicit val c = connectorToOldCluster
        ss.sparkContext.cassandraTable[CassandraRow]("maindata", table)
      }

      {
        implicit val c = connectorToNewCluster
        rddFromOldCluster.saveToCassandra("maindata", table)
      }
    }

    ss.stop()
  }
}

object CarDataBackupTask {
  def main(args: Array[String]): Unit = {
    val hostname = args.headOption.getOrElse("localhost")
    val back = new CarDataBackupTask()
    back.init(hostname)
    back.run(args.tail)
  }
}
