import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

/**
 * The Homework 2 Question 1
 * @author Charmal
 */
/**
 * @author charmal
 *
 */
public class Question2 extends Configured implements Tool {

	/**
	 * Title Occurrence mapper class
	 *
	 */
	public static class TitleOccurrenceMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

		private final Logger logger = Logger.getLogger(Question2.class);

		/**
		 * Initial setup method.
		 * 
		 * @param context
		 */
		@Override
		protected void setup(Context context) {
			logger.info("Initializing Log4j.");
			try {
			} catch (Exception ex) {
				logger.error(ex.getMessage());
			}
		}

		String record;

		/**
		 * Map task for the
		 * 
		 * @param key
		 * @param value
		 * @param context
		 * @throws IOException
		 * @throws InterruptedException
		 */
		@Override
		protected void map(LongWritable key, Text value, Mapper.Context context)
				throws IOException, InterruptedException {

			record = value.toString();

			String[] fields = record.split("::");

			// Check whether the length of the array is 5.
			if (fields.length == 5) {

				String title = fields[4];

				context.write(new Text(title), new IntWritable(1));

			}

		}

	}

	/**
	 * Reducer class used to reduce the title occurrence.
	 */
	public static class TitleOccurrenceReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

		Map<Text, Integer> unsortedMap = new HashMap<Text, Integer>();

		/**
		 * Reduce method - Put all the key and values in to a Hashmap to
		 * 
		 * @param key
		 * @param values
		 * @param context
		 * @throws IOException
		 * @throws InterruptedException
		 */
		@Override
		protected void reduce(Text key, Iterable<IntWritable> values,
				Reducer<Text, IntWritable, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {

			Integer sum = 0;

			for (IntWritable value : values) {
				sum = sum + value.get();
			}

			unsortedMap.put(new Text(key), sum);
		}

		/**
		 * The cleanup
		 * 
		 * @param context
		 * @throws IOException
		 * @throws InterruptedException
		 */
		@Override
		protected void cleanup(org.apache.hadoop.mapreduce.Reducer.Context context)
				throws IOException, InterruptedException {

			Map<Text, Integer> sortedMap = Sorting.sortByValue(unsortedMap);

			int counter = 0;

			for (Map.Entry<Text, Integer> entry : sortedMap.entrySet()) {

				if (counter == 10) {
					break;
				}

				context.write(entry.getKey(), entry.getValue());

				counter++;
			}

		}

	}

	/**
	 * The run method
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = new Configuration();

		String input = args[0];
		String output = args[1];

		Job job = new Job(conf, "Top 10 Title's Occurance");
		job.setJarByClass(Question2.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setMapperClass(TitleOccurrenceMapper.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setReducerClass(TitleOccurrenceReducer.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.setInputPaths(job, new Path(input));

		Path outPath = new Path(output);
		FileOutputFormat.setOutputPath(job, outPath);
		outPath.getFileSystem(conf).delete(outPath, true);

		job.waitForCompletion(true);
		return (job.waitForCompletion(true) ? 0 : 1);
	}

	/**
	 * The main method
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new Question2(), args);
		System.exit(exitCode);
	}

}
