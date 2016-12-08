package pt.iceman.carspark

import pt.iceman.carspark.backup.CarDataBackupTask
import pt.iceman.carspark.dataviewer.{TripDataProcessing, TripDataViewer}


object Runner {
  def main(args: Array[String]) {
    //    CarDataBackupTask.main(
    //      Array(
    //        "192.168.0.178",
    //        "car_settings",
    //        "speed_data",
    //        "temperature_data",
    //        "car_logs",
    //        "car_trips"
    //      )
    //    )

//    TripDataProcessing.main(
    //      Array(
    //        "localhost",
    //        "1d9d0cf0-bcd0-11e6-af1a-c7c11b58be56",
    //        "c7d5edf0-bcd9-11e6-af1a-c7c11b58be56"
    //      )
    //    )

        TripDataViewer.main(
          Array(
            "localhost",
            "1d9d0cf0-bcd0-11e6-af1a-c7c11b58be56",
            "c7d5edf0-bcd9-11e6-af1a-c7c11b58be56"
          )
        )
  }
}

