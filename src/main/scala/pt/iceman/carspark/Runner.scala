package pt.iceman.carspark

import pt.iceman.carspark.backup.CarDataBackup


object Runner {
  def main(args: Array[String]) {
    val back = new CarDataBackup()
    back.init()
    back.run(Array(
      "car_settings",
      "speed_data",
      "temperature_data",
      "car_logs",
      "car_trip"
    ))
  }
}

