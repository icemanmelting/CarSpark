package pt.iceman.carspark

import com.datastax.driver.core.{Cluster, Session}

/**
  * Created by iCeMan on 08/12/2016.
  */
trait Cassandra {
  var cluster: Cluster = _
  var session: Session = _

  def init (hostName: String) = {
    val builder = Cluster.builder()
    builder.addContactPoint(hostName)
    cluster = builder.build()
    session = cluster.connect("maindata")
  }
}
