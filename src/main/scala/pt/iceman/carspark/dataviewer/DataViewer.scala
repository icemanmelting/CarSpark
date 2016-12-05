package pt.iceman.carspark.dataviewer

import java.util.Date
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.chart.{LineChart, NumberAxis, XYChart}
import javafx.scene.layout.{AnchorPane, FlowPane}
import javafx.stage.Stage

import org.apache.spark.sql.cassandra.CassandraSQLContext
import org.apache.spark.{SparkConf, SparkContext}
import pt.iceman.DateAxis

/**
  * Created by iCeMan on 05/12/2016.
  */
object DataViewer {
  def main(args: Array[String]) {
    Application.launch(classOf[DataViewer], args: _*)
  }
}

class DataViewer extends Application {
  override def start(primaryStage: Stage) {

    val conf = new SparkConf()
      .set("spark.cassandra.connection.host", "localhost")
      .setMaster("local[*]")
      .setAppName("CarSpark")

    val sc = new SparkContext(conf)
    val csc = new CassandraSQLContext(sc)

    csc.setKeyspace("maindata")

    val df = csc.sql(
      """SELECT ct.id, s.speed, s.rpm, s.ts, t.value, t.ts AS tempTs FROM car_trips AS ct
        | JOIN speed_data AS s ON (ct.id = s.trip_id)
        | JOIN temperature_data AS t ON (ct.id = t.trip_id)
        | WHERE ct.id = '43a7ec30-bb13-11e6-b98e-c7c11b58be56'
        | ORDER BY s.ts ASC
      """.stripMargin)

    val rows = df.collect()

    val speedTime = rows.map { r =>
      new XYChart.Data[Date, Number](r.getAs[Date]("ts"), r.getAs[Double]("speed"))
    }.toList

    val rpmTime = rows.map { r =>
      new XYChart.Data[Date, Number](r.getAs[Date]("ts"), r.getAs[Double]("rpm"))
    }.toList

    val tempTime = rows.map { r =>
      new XYChart.Data[Date, Number](r.getAs[Date]("tempTs"), r.getAs[Double]("value"))
    }.toList


    def getLineChart(title: String, seriesName: String, xLabel: String, yLabel: String, values: List[XYChart.Data[Date, Number]]) = {
      val xAxis = new DateAxis()
      val yAxis = new NumberAxis()
      xAxis.setLabel(xLabel)
      yAxis.setLabel(yLabel)

      val lineChart = new LineChart[Date, Number](xAxis, yAxis)
      lineChart.setTitle(title)

      val series = new XYChart.Series[Date, Number]()
      series.setName(seriesName)

      values.foreach(xy => series.getData.add(xy))

      lineChart.getData.add(series)
      lineChart
    }

    val root = new FlowPane()
    val speedChart = getLineChart("Speed tracking for trip: 43a7ec30-bb13-11e6-b98e-c7c11b58be56",
      "Speed Time", "Time", "Speed (Km/h)", speedTime)

    root.getChildren.add(speedChart)

    val rpmChart = getLineChart("Rpm tracking for trip: 43a7ec30-bb13-11e6-b98e-c7c11b58be56",
      "Rpm Time", "Time", "Rpm", rpmTime)

    root.getChildren.add(rpmChart)

    val tempChart = getLineChart("Temperature tracking for trip: 43a7ec30-bb13-11e6-b98e-c7c11b58be56",
      "Temperature Time", "Time", "Temperature C", tempTime)

    root.getChildren.add(tempChart)

    val scene = new Scene(root, 2280, 686)

    primaryStage.setScene(scene)
    primaryStage.show()
  }
}
