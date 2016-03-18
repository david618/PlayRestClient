package controllers

import java.io.{InputStreamReader, BufferedReader}

import com.fasterxml.jackson.core.JsonParseException
import com.google.gson.Gson
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import play.api._
import play.api.libs.json.{JsNull, JsValue, Json}
import play.api.mvc._

import scala.collection.mutable
import scala.concurrent.Future

case class ErrorMessage(error: String)

object Application extends Controller {

  // Create an empty msgs String
  val msgs = mutable.Set.empty[String]

  var cnt = 0

  val client = HttpClientBuilder.create().build()


  val url = Play.current.configuration.getString("target.url").getOrElse("")

  def get_count = Action {
    Ok(String.valueOf(cnt))
  }


  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def rest_in_json = Action(parse.tolerantText) { request =>
    // parse.tolerantText will allow any text to come in no checks

    cnt +=1

    // Read request body
    val body = request.body
    //println("Input: " + body)

    // Initialize b as None
    //var json = None: Option[JsValue]
    var json = Json.parse("{}")
    try {
      // Try to parse the text
      //json = Some(Json.parse(body))
      json = Json.parse(body)

      // So the json is valid send it forward
      val jsonString = Json.stringify(json)
//      println(jsonString)

      sendMessage(Json.stringify(json))

      Ok(Json.prettyPrint(json))
    } catch {
      case ex:JsonParseException => {
        val msg = new ErrorMessage(ex.getMessage)
        val msgJson = new Gson().toJson(msg)
        BadRequest(msgJson)
      }
      case ex: Throwable => {
//        println(ex.getClass.getName)
//        println(ex.getMessage)
        val msg = new ErrorMessage(ex.getClass.getName + " : " + ex.getMessage)
        val msgJson = new Gson().toJson(msg)
        BadRequest(msgJson)
      }
    }


  }

  def sendMessage(msg: String) = {
    // create our object as a json string
    //val msgJson = new Gson().toJson(msg)

    // add name value pairs to a post object
    val post = new HttpPost(url)

    post.setHeader("Content-Type", "application/json")

    val input = new StringEntity(msg)
    input.setContentType("application/json")
    post.setEntity(input)

    //  val nameValuePairs = new ArrayList[NameValuePair]()
    //  nameValuePairs.add(new BasicNameValuePair("JSON", spockAsJson))
    //  post.setEntity(new UrlEncodedFormEntity(nameValuePairs))

    // send the post request
    val response = client.execute(post)
//    println("--- HEADERS ---")
//    response.getAllHeaders.foreach(arg => println(arg))

//    println(response.toString())

//    val rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))

//    val rslt = Stream.continually(rd.readLine()).takeWhile(_ != null)
//    println("Response" + rslt.mkString("\n"))


//    println(response.getStatusLine.getStatusCode)

    post.releaseConnection()
    response.close()
//    client.close()

//    rd.close()

  }



}