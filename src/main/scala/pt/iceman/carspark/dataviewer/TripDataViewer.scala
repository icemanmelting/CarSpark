package pt.iceman.carspark.dataviewer

import java.util.{Date, UUID}
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.chart.{LineChart, NumberAxis, XYChart}
import javafx.scene.input.SwipeEvent
import javafx.scene.layout.FlowPane
import javafx.scene.shape.Rectangle
import javafx.stage.Stage

import scala.collection.JavaConversions._
import com.datastax.driver.core._
import pt.iceman.DateAxis
import pt.iceman.carspark.Cassandra

/**
  * Created by iCeMan on 08/12/2016.
  */
class TripDataViewer extends Application with Cassandra {
  override def start(primaryStage: Stage): Unit = {
    val params = getParameters.getRaw
    if (params.size < 1)
      return
    val hostname = params.headOption.getOrElse("localhost")

    init(hostname)

    val charts = params.tail.map { tripId =>
      val tripIdUUID = UUID.fromString(tripId)
      val speedRow = session.execute("SELECT * FROM processed_speed_data WHERE trip_id = ?", tripIdUUID).one()
      val rpmRow = session.execute("SELECT * FROM processed_rpm_data WHERE trip_id = ?", tripIdUUID).one()
      val tempRow = session.execute("SELECT * FROM processed_temperature_data WHERE trip_id = ?", tripIdUUID).one()
      // val gearRow = session.execute("SELECT * FROM processed_gear_data WHERE trip_id = ?", tripIdUUID).one()

      def getLineChartData(r: Row) = {
        val data = r.getSet("data", classOf[TupleValue])

        data.map { tuple =>
          val chartData = new XYChart.Data[Date, Number](tuple.getTimestamp(0), tuple.get(1, classOf[Double]))
          val rect = new Rectangle(0, 0)
          rect.setVisible(false)
          chartData.setNode(rect)
          chartData
        }.toList
      }

      val speedTime = getLineChartData(speedRow)
      val rpmTime = getLineChartData(rpmRow)
      val tempTime = getLineChartData(tempRow)
      //  val gearTime = getLineChartData(gearRow, "ts", "gear")

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

      val speedChart = getLineChart(s"Speed tracking",
        "Speed Time", "Time", "Speed (Km/h)", speedTime)

      val rpmChart = getLineChart(s"Rpm tracking for trip",
        "Rpm Time", "Time", "Rpm", rpmTime)

      val tempChart = getLineChart(s"Temperature tracking for trip",
        "Temperature Time", "Time", "Temperature C", tempTime)

      //      val gearChart = getLineChart(s"Manual Gear tracking for trip",
      //        "Gear Time", "Time", "Gear", gearTime)

      //        val pieChartData = FXCollections.observableArrayList(
      //          new PieChart.Data("First Gear", gear1),
      //          new PieChart.Data("Second Gear", gear2),
      //          new PieChart.Data("Third Gear", gear3),
      //          new PieChart.Data("Forth Gear", gear4),
      //          new PieChart.Data("Fifth Gear", gear5))
      //
      //        val chart = new PieChart(pieChartData)
      //        chart.setTitle("Gear Usage")

      List(speedChart, rpmChart, tempChart)
    }

    var listPos = 0
    val root = new FlowPane
    val scene = new Scene(root, 2200, 686)

    scene.setOnSwipeLeft(new EventHandler[SwipeEvent]() {
      override def handle(event: SwipeEvent) {
        if (listPos >= 0 && listPos < charts.size - 1) {
          try {
            listPos += 1
            root.getChildren.clear()
            root.getChildren.addAll(charts(listPos))
          } catch {
            case e: Exception =>
          }
        }
        event.consume()
      }
    })

    root.getChildren.addAll(charts.head)
    scene.setOnSwipeRight(new EventHandler[SwipeEvent]() {
      override def handle(event: SwipeEvent) {
        if (listPos > 0 && listPos <= charts.size - 1) {
          try {
            listPos -= 1
            root.getChildren.clear()
            root.getChildren.addAll(charts(listPos))
          } catch {
            case e: Exception =>
          }
        }
        event.consume()
      }
    })

    primaryStage.setScene(scene)
    primaryStage.show()
  }

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[TripDataViewer], args: _*)
  }
}

object TripDataViewer {
  def main(args: Array[String]): Unit = {
    val tdv = new TripDataViewer
    tdv.main(args)
  }
}
