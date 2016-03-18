import java.io.{InputStreamReader, BufferedReader}
import java.util.ArrayList

import com.google.gson.Gson
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.{HttpClientBuilder, DefaultHttpClient}
import org.apache.http.message.BasicNameValuePair

case class Person(firstName: String, lastName: String, age: Int)

case class ErrorMessage(msg: String)

object HttpJsonPostTest extends App {

  def sendMessage = {
    // create our object as a json string
    val spock = new Person("Leonard", "Nimoy", 82)
    val spockAsJson = new Gson().toJson(spock)

    // add name value pairs to a post object
    val post = new HttpPost("http://localhost:9002/faa-stream2")

    post.setHeader("Content-Type", "application/json")

    val input = new StringEntity(spockAsJson)
    input.setContentType("application.json")
    post.setEntity(input)

    //  val nameValuePairs = new ArrayList[NameValuePair]()
    //  nameValuePairs.add(new BasicNameValuePair("JSON", spockAsJson))
    //  post.setEntity(new UrlEncodedFormEntity(nameValuePairs))

    // send the post request
    val client = HttpClientBuilder.create().build()


    val response = client.execute(post)
    println("--- HEADERS ---")
    response.getAllHeaders.foreach(arg => println(arg))

    println(response.toString())

    val rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
    //  var rslt = new StringBuffer
    //  var line = rd.readLine()

    //  while ((line = rd.readLine()) != null) {
    //    rslt.append(line)
    //  }

    val rslt = Stream.continually(rd.readLine()).takeWhile(_ != null)
    println("Response" + rslt.mkString("\n"))

    response.close()
    client.close()

    rd.close()

  }

  //sendMessage

  val msg1 = ErrorMessage("Test 1 2 3")
  val jsonMsg = new Gson().toJson(msg1)
  println(jsonMsg)

}
