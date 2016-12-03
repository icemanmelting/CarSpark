
name := "CarSpark"

version := "1.0"

scalaVersion := "2.10.6"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.2"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.6.2"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "1.6.2"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka" % "1.6.2"
libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" % "1.6.2"