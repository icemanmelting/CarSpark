package pt.iceman.carspark.backup

import com.datastax.spark.connector._
import com.datastax.spark.connector.cql.CassandraConnector
import pt.iceman.carspark.CarBatchTask

/**
  * Created by iCeMan on 02/12/2016.
  */
class CarDataBackup extends CarBatchTask {
  override def run(args: Array[String]): Unit = {
    if (args.length < 1) return

    val connectorToOldCluster = CassandraConnector(conf)
    val connectorToNewCluster = CassandraConnector(conf.set("spark.cassandra.connection.host", "localhost"))

    args.foreach { table =>
      val rddFromOldCluster = {
        implicit val c = connectorToOldCluster
        sc.cassandraTable[CassandraRow](csc.getKeyspace, table)
      }

      {
        implicit val c = connectorToNewCluster
        rddFromOldCluster.saveToCassandra("maindata", table)
      }
    }
  }
}
