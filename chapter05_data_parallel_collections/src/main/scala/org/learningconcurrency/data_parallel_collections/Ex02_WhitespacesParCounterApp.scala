package org.learningconcurrency.data_parallel_collections

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}

object Ex02_WhitespacesParCounterApp extends Ex02_CountWhitespacesPar with JFXApp {

  stage = new scalafx.application.JFXApp.PrimaryStage {
    title = "White spaces parallel count"
    scene = new Scene {
      root = {
        val xyData: Vector[(Double, Double)] = calculateGraph(probabilities, stringToStringWithWhiteSpaces)

        //prepare series
        val series = new XYChart.Series[Number, Number] {
          name = "Data Series 1"
          data() ++= xyData.map {
            case (x, y) => XYChart.Data[Number, Number](x, y)
          }
        }

        // setup Line chart
        new LineChart(NumberAxis("Probability"), NumberAxis("Running time (ms)")) {
          title = "Basic Line Chart"
          data() += series
        }
      }
    }
  }
}