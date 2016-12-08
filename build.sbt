
name := "CarSpark"

version := "1.0"

scalaVersion := "2.11.8"

//libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.2"
//libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.6.2"
//libraryDependencies += "org.apache.spark" %% "spark-sql" % "1.6.2"
//libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka" % "1.6.2"
//libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" % "1.6.2"
//libraryDependencies += "com.google.code.reflection-utils" %% "reflection-utils" % "0.0.4"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.0.2"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.0.2"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.0.2"
libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.0-M3"