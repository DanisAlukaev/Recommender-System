import org.apache.spark.SparkContext
import org.apache.spark.mllib.recommendation.Rating

import scala.util.Random
import java.io.File
import java.io.PrintWriter
import scala.util.matching.Regex


class Grader(path: String, sc: SparkContext) {

  // constructor begins

  val films = Random.shuffle(
    sc
      .textFile(path + "/for-grading.tsv")
      .map {
        _.split("\t")
      }
      .map { x => (x(0).toInt, x(1)) }
      .collect()
      .toSeq
  )

  // Need to specify type when it cannot be inferred
  var graded: Seq[Tuple2[Int, Double]] = Seq()

  var position = 0

  val grading_message = "Enter grade from 1 to 5. Enter 0 if you did not see this movie: "

  var step = 0
  var limit = 20

  // Task 2. Load movie preferences
  println("Do you want to load your preferences?")
  val answer = scala.io.StdIn.readLine()
  val answerPattern: Regex = "^(y|yes)$".r

  def loadPreferences() = {
    println("Loading from user_rating.tsv...")
    graded = sc.textFile(path + "/user_rating.tsv")
      .map(line => line.split("\t"))
      .map(x => (x(0).toInt, x(1).toDouble))
      .collect()
  }

  def predictPreferences() = {
    while (step < limit) {
      println(s"\nGraded ${step}/${limit}, Viewed ${position}/${films.length}")
      println(s"${grading_message}\n${films(position)._2}")
      var grade = scala.io.StdIn.readLine()

      try {
        var intGrade = grade.toDouble

        if (intGrade < 0.0 || intGrade > 5.0) {
          throw new NumberFormatException()
        }

        if (intGrade != 0) {
          graded = graded :+ (films(position)._1, intGrade)
          step += 1
          position += 1
        } else {
          position += 1
        }

        if (position == films.length) {
          step = limit + 1
          println("\nNo more movies to grade\n")
        }

        if (step == limit) {
          println("Grade 20 more? y/[n]")
          var ans = scala.io.StdIn.readLine()

          if (ans == "y" || ans == "yes" || ans == "Yes" || ans == "YES") {
            limit += 20
          } else {
            println("\nFinished Grading\n")
          }
        }
      } catch {
        case e: NumberFormatException => println("Try again")
        case e: Exception => println("Unknown Error")
      }
      dumpRatings()
    }
  }
  // End of Task 2. Load movie preferences

  answerPattern.findFirstMatchIn(answer.trim().toLowerCase()) match {
    case Some(_) => loadPreferences()
    case None => predictPreferences()
  }

  def printRatings(): Unit = {
    val id2title = this.films.toMap
    this.graded.foreach(x => println(s"${id2title(x._1)}: ${x._2}"))
  }

  def dumpRatings() = {
    val ratings_file = "user_rating.tsv"
    println(s"Saving ratings to ${ratings_file}\n")
    val writer = new PrintWriter(new File(ratings_file))
    this.graded.foreach(x => writer.write(s"${x._1}\t${x._2}\n"))
    writer.close()
  }

  def toRDD = {
    sc.parallelize(this.graded.map { x => Rating(0, x._1, x._2) })
  }
}