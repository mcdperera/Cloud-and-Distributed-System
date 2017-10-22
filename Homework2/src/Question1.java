
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @author Charmal
 *
 */
public class Question1 {

	/**
	 * Web site mapper class
	 *
	 */
	public static class WebsiteMapper extends Mapper<Object, Text, Text, DoubleWritable> {
		public void map(Object Key, Text value, Context context) throws IOException, InterruptedException {

			String tempString = value.toString();
			String[] tempArray = tempString.split("::");
			Text title = null;
			double size = 0;

			/* setting the instance records */
			try {
				if (tempArray.length == 5) {

					title = new Text(tempArray[4]);
					size = Double.parseDouble(tempArray[3]);

					context.write(new Text(title), new DoubleWritable(size));
				}

			} catch (NumberFormatException e) {
				System.out.println("Record is not proper");
			}

		}
	}

	/**
	 * Web site Reducer class.
	 *
	 */
	public static class WebsiteReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

		Map<Text, DoubleWritable> countMap = new HashMap<Text, DoubleWritable>();

		@Override
		public void reduce(Text key, Iterable<DoubleWritable> values, 
				Reducer<Text, DoubleWritable, Text, DoubleWritable>.Context context) 
				throws IOException, InterruptedException {

			Double sum = (double) 0;

			for (DoubleWritable value : values) {
				sum = sum + value.get();
			}
			
			countMap.put(new Text(key), new DoubleWritable(sum));
		}

		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {

			Map<Text, DoubleWritable> sortedMap = Sorting.sortByValue(countMap);

			int counter = 0;

			for (Map.Entry<Text, DoubleWritable> entry : sortedMap.entrySet()) {

				if (counter == 10) {
					break;
				}

				context.write(new Text(entry.getKey()), entry.getValue());
				
				counter++;

			}

		}

	}

	/**
	 * The main method
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "BiggestWebsite");
		job.setJobName("Top 10 Biggest websites");

		job.setJarByClass(Question1.class);
		job.setMapperClass(WebsiteMapper.class);

		job.setReducerClass(WebsiteReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}