package com.partha.ex01workingWithSpark;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.sparkproject.guava.collect.Iterables;

import scala.Tuple2;

/**
 * working with GroupByKey
 * @author partha
 *
 */
public class App06BGroupByKey {

	public static void main(String[] args) {
		List<String> inputData = new ArrayList<>();
		inputData.add("WARN: Tuesday 4 September 0405");
		inputData.add("ERROR: Tuesday 4 September 0408");
		inputData.add("FATAL: Wednesday 5 September 1632");
		inputData.add("ERROR: Friday 7 September 1854");
		inputData.add("WARN: Saturday 8 September 1942");


		SparkConf conf = new SparkConf()
				.setAppName("startingSpark")
				.setMaster("local[*]");
		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaRDD<String> originalMessages = sc.parallelize(inputData); 
	
		JavaPairRDD<String, Long> pairRDD = originalMessages.mapToPair(  rawData -> {
			String[] splittedInput = rawData.split(":");
			String level = splittedInput[0];
			//String date = splittedInput[1]; //since am interested in the count so this value is not important to me
			return new Tuple2<String, Long>(level,1L);			
		});
		
		
		JavaPairRDD<String, Iterable<Long>> groupByKeyRDD = pairRDD.groupByKey();
		
		groupByKeyRDD.collect().forEach( tuple -> System.out.println("key:"+ tuple._1 + "   count:"+ Iterables.size(tuple._2) ));
		
		sc.close();
	}

}
