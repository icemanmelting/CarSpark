package pt.iceman.carspark.dataviewer

import java.util.Date
import org.apache.spark.sql.Row
import pt.iceman.carspark.CarBatchTask
import scala.collection.JavaConversions._
import com.datastax.spark.connector._

/**
  * Created by iCeMan on 05/12/2016.
  */
class TripDataProcessing extends CarBatchTask {
  override def run(args: Array[String]) {
    if (args.length < 1)
      return

    ss.sql(
      """CREATE TEMPORARY VIEW car_trips USING org.apache.spark.sql.cassandra
        | OPTIONS (table "car_trips", keyspace "maindata", cluster "Test Cluster", pushdown "true")""".stripMargin
    )

    ss.sql(
      """CREATE TEMPORARY VIEW speed_data USING org.apache.spark.sql.cassandra
        | OPTIONS (table "speed_data", keyspace "maindata", cluster "Test Cluster", pushdown "true")""".stripMargin
    )

    ss.sql(
      """CREATE TEMPORARY VIEW temperature_data USING org.apache.spark.sql.cassandra
        | OPTIONS (table "temperature_data", keyspace "maindata", cluster "Test Cluster", pushdown "true")""".stripMargin
    )

    val data = args.map { tripId =>
      val df = ss.sql(
        s"""SELECT ct.id, ct.trip_length_km, s.speed, s.rpm, s.gear, s.ts, t.value AS temperature, t.ts AS tempTs FROM car_trips AS ct
           | JOIN speed_data AS s ON (ct.id = s.trip_id)
           | JOIN temperature_data AS t ON (ct.id = t.trip_id)
           | WHERE ct.id = '$tripId'
           | ORDER BY s.ts ASC""".stripMargin
      )

      val firstGear = df.filter("gear = 1").count()
      val secondGear = df.filter("gear = 2").count()
      val thirdGear = df.filter("gear = 3").count()
      val forthGear = df.filter("gear = 4").count()
      val fifthGear = df.filter("gear = 5").count()

      (tripId, df.collect().toList, firstGear, secondGear, thirdGear, forthGear, fifthGear)
    }

    def getLineChartData(r: Row, tsField: String, doubleField: String) = {
      (r.getAs[Date](tsField), r.getAs[Number](doubleField))
    }

    val gearData = data.map {
      case (trip_id, rows, firstGear, secondGear, thirdGear, forthGear, fifthGear) =>
        val gearStats = Map(1 -> firstGear, 2 -> secondGear, 3 -> thirdGear, 4 -> forthGear, 5 -> fifthGear)
        val gearData = rows.map(getLineChartData(_, "ts", "gear"))

        (trip_id, gearStats, gearData.toSet)
    }

    val gearDataRdd = ss.sparkContext.parallelize(gearData)
    gearDataRdd.saveToCassandra("maindata", "processed_gear_data", SomeColumns("trip_id", "gear_stats", "data"))

    val speedData = data.map {
      case (trip_id, rows, _, _, _, _, _) =>
        val speedData = rows.map(getLineChartData(_, "ts", "speed"))
        (trip_id, speedData.toSet)
    }

    val speeDataRdd = ss.sparkContext.parallelize(speedData)
    speeDataRdd.saveToCassandra("maindata", "processed_speed_data", SomeColumns("trip_id", "data"))

    val rpmData = data.map {
      case (trip_id, rows, _, _, _, _, _) =>
        val rpmData = rows.map(getLineChartData(_, "ts", "rpm"))
        (trip_id, rpmData.toSet)
    }

    val rpmDataRdd = ss.sparkContext.parallelize(rpmData)
    rpmDataRdd.saveToCassandra("maindata", "processed_rpm_data", SomeColumns("trip_id", "data"))

    val tempData = data.map {
      case (trip_id, rows, _, _, _, _, _) =>
        val tempData = rows.map(getLineChartData(_, "tempTs", "temperature"))
        (trip_id, tempData.toSet)
    }

    val tempDataRdd = ss.sparkContext.parallelize(tempData)
    tempDataRdd.saveToCassandra("maindata", "processed_temperature_data", SomeColumns("trip_id", "data"))
  }
}

object TripDataProcessing {
  def main(args: Array[String]): Unit = {
    val dv = new TripDataProcessing
    val hostname = args.headOption.getOrElse("localhost")
    dv.init("localhost")
    dv.run(args.tail)
  }
}
