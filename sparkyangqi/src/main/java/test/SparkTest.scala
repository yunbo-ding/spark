package test
import java.util
import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}
import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer
object SparkTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("dataAnalysis")
      .enableHiveSupport()
      .getOrCreate()
    var rdd: RDD[String] = spark.sparkContext.textFile("/tmp/yangqi.txt")
    var rdd1: RDD[Data] = rdd.flatMap(rddData => {

      val list: util.List[Data] = ListBuffer[Data]().asJava
      try {
        var jsonObj: JSONObject = JSON.parseObject(rddData)
        var jsonArray: JSONArray = jsonObj.getJSONArray("et")
        for (i <- 0 until jsonArray.size()) {
          var eventName: String = jsonArray.getJSONObject(i).getString("event_name")
          var eventId: String = jsonArray.getJSONObject(i).getString("event_id")
          var evenData: String = jsonArray.getJSONObject(i).getString("event_data")
          var uid: String = jsonObj.getString("uid")
          var cpid: String = jsonObj.getString("cpid")
          var data = Data(uid, cpid, eventName, eventId, evenData)
        list.add(data)
        }
      }
      catch {
        case ex: Exception => {
          println("解析错误")
        }
      }
      println(list.asScala)
      list.asScala
    })
//        rdd1.foreach(x=>{
//          println(x)
//        })
    import spark.implicits._
    var rddDs = rdd1.toDF()
   // rddDs.sqlContext.sql("select * from ").show()
    rddDs.createOrReplaceTempView("data")
    rddDs.sparkSession.sql("use ods_compass")
   rddDs.sparkSession.sql("insert overwrite table ods_compass.ods_spark_test partition(dt='20200512') select * from data where uid != '' and  cpid != '' and  eventName != '' and  eventId != '' and eventData != ''")
    // .write.mode(SaveMode.Append).parquet("/result/a")
    //    rddDs.select("").write.parquet("")
    spark.stop()
  }
}

case class Data(var uid: String, var cpid: String, var eventName: String, var eventId: String, var eventData: String)



