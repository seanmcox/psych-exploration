/**
 * 
 */
package com.shtick.psych;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.shtick.math.statistics.StatsDataSet;
import com.shtick.math.statistics.StatusTracker;
import com.shtick.math.statistics.WeightedDatum;
import com.shtick.math.statistics.pca.PrincipleComponentAnalysis;
import com.shtick.math.statistics.pca.PrincipleComponentSystem;

/**
 * @author scox
 *
 */
public class Main {
	private static File WORKING_DIRECTORY;
	private static final String OS = (System.getProperty("os.name")).toUpperCase();
	static{
		if (OS.contains("WIN")){
		    String workingDirectory = System.getenv("AppData");
		    WORKING_DIRECTORY = new File(workingDirectory);
		}
		else{ // Try Linux or related.
		    String workingDirectory = System.getProperty("user.home");
		    WORKING_DIRECTORY = new File(workingDirectory+"/Library/Application Support");
		    if(!WORKING_DIRECTORY.exists())
			    WORKING_DIRECTORY = new File(workingDirectory+"/.local/share");
		    if(!WORKING_DIRECTORY.exists())
			    WORKING_DIRECTORY = null;
		}
		if((WORKING_DIRECTORY==null)||(!WORKING_DIRECTORY.canWrite())){
			// TODO Find the current application folder.
		}
	}

	/**
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		if(args.length<2)
			throw new IllegalArgumentException("Two directories expected.");
		File input = new File(args[0]);
		File output = new File(args[1]);
		HashSet<String> ignoreColumns = new HashSet<String>();
		if(args.length>2)
			for(String col:args[2].split(","))
				ignoreColumns.add(col);
		if(!(input.exists()&&input.isFile()&&input.canRead()))
			throw new IllegalArgumentException("Input file not found/readable.");
		if(output.exists()&&!output.canWrite())
			throw new IllegalArgumentException("Cannot write to output file.");
		else if(output.getParentFile().exists()&&!output.getParentFile().isDirectory())
			throw new IllegalArgumentException("Output patent directory is not a folder.");
		else if((!output.getParentFile().exists())&&(!output.mkdirs()))
			throw new IllegalArgumentException("Could not create output folder.");
		try (CSVStatsDataSet data = new CSVStatsDataSet(input,ignoreColumns)){
			PrincipleComponentSystem pcs = PrincipleComponentAnalysis.getPrincipleComponents(data, new StatusTracker() {
				
				@Override
				public void updateStatus(String currentTask, double taskProgress, double overallProgress) {
					System.out.println(currentTask);
					System.out.println("Task Progress: "+(taskProgress*100)+"%");
					System.out.println("Overall Progress: "+(overallProgress*100)+"%");
				}
			});
			pcs.print();
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		//generateCorrelationMatrix(input,output,ignoreColumns);
	}
	
	/**
	 * 
	 * @param input
	 * @param output
	 * @param ignoreColumns 
	 * @throws IOException 
	 */
	public static void generateCorrelationMatrix(File input, File output, Set<String> ignoreColumns) throws IOException{
		int[] indexMap;
		String[] headers = null;
		double[] sums;
		double[] means;
		double[] standardDeviations;
		double[][] correlations;
		TreeSet<Correlation> sortedCorrelations = new TreeSet<>(new Comparator<Correlation>() {
			/* (non-Javadoc)
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			@Override
			public int compare(Correlation o1, Correlation o2) {
				return Double.compare(o1.r,o2.r);
			}
		});
		int rowCount=0;
		try(BufferedReader in = new BufferedReader(new FileReader(input))){
			String line = in.readLine();
			String[] parts = line.split("\t");
			headers=parts;
			indexMap = new int[headers.length];
			int filtered=0;
			{
				for(int i=0;i<headers.length;i++) {
					if(ignoreColumns.contains(headers[i])) {
						filtered++;
						indexMap[i] = -1;
						continue;
					}
					indexMap[i]=i-filtered;
					if(filtered==0)
						continue;
					headers[i-filtered]=headers[i];
				}
				String[] newHeaders = new String[headers.length-filtered];
				System.arraycopy(headers, 0, newHeaders, 0, newHeaders.length);
				headers=newHeaders;
			}
			sums=new double[headers.length];
			means=new double[sums.length];
			line=in.readLine();
			while(line!=null) {
				parts = line.split("\t");
				if(parts.length==0)
					continue;
				if((parts.length-filtered)!=headers.length)
					throw new IOException("Row size mismatched header size: "+line);
				for(int i=0;i<parts.length;i++) {
					if(indexMap[i]>=0)
						sums[indexMap[i]]+=Integer.parseInt(parts[i]);
				}
				line=in.readLine();
				rowCount++;
			}
			for(int i=0;i<sums.length;i++)
				means[i]=sums[i]/rowCount;
		}
		try(BufferedReader in = new BufferedReader(new FileReader(input))){
			standardDeviations = new double[sums.length];
			correlations = new double[sums.length][sums.length];
			String line = in.readLine();
			line=in.readLine(); // Skip header
			while(line!=null) {
				String[] parts = line.split("\t");
				if(parts.length==0)
					continue;
				for(int i=0;i<parts.length;i++) {
					if(indexMap[i]<0)
						continue;
					double delta = Integer.parseInt(parts[i])-means[indexMap[i]];
					standardDeviations[indexMap[i]]+=delta*delta;
					for(int j=i;j<parts.length;j++) {
						if(indexMap[j]<0)
							continue;
						correlations[indexMap[i]][indexMap[j]]+=delta*(Integer.parseInt(parts[j])-means[indexMap[j]]);
					}
				}
				line=in.readLine();
				rowCount++;
			}
			for(int i=0;i<standardDeviations.length;i++)
				standardDeviations[i]=Math.sqrt(standardDeviations[i]/(rowCount-1));
			for(int i=0;i<standardDeviations.length;i++) {
				correlations[i][i]=1;
				for(int j=i+1;j<standardDeviations.length;j++) {
					correlations[i][j]/=standardDeviations[i]*standardDeviations[j]*(rowCount-1);
					correlations[j][i]=correlations[i][j];
					if(((correlations[i][j]<1)&&(correlations[i][j]>0.6))
							||(correlations[i][j]<-0.6)) {
						sortedCorrelations.add(new Correlation(headers[i],headers[j],correlations[i][j]));
					}
				}
			}
		}
		PrintStream out = new PrintStream(output);
		printMatrixHeader(out,headers);
		for(int i=0;i<headers.length;i++)
			printMatrixRow(out,headers[i],correlations[i]);
		for(Correlation correlation:sortedCorrelations)
			out.println(correlation);
		out.flush();
		out.close();
	}
	
	private static void printMatrixHeader(PrintStream out, String[] data) {
		for(String datum:data)
			out.print(","+datum);
		out.println();
	}
	
	private static void printMatrixRow(PrintStream out, String left, double[] data) {
		out.print(left);
		for(double datum:data) {
			out.print(","+datum);
		}
		out.println();
	}
	
	private static class Correlation {
		String varA;
		String varB;
		double r;
		
		/**
		 * @param varA
		 * @param varB
		 * @param r
		 */
		public Correlation(String varA, String varB, double r) {
			super();
			this.varA = varA;
			this.varB = varB;
			this.r = r;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return varA+"x"+varB+"="+r;
		}
	}
	
	private static class CSVStatsDataSet extends StatsDataSet implements Closeable {
		private File input;
		private int[] indexMap;
		private String[] headers = null;
		private double[] means;
		private int rowCount;
		private int varCount;
		private double[] sums;
		private CSVStatsDataIterator currentIterator;
		
		public CSVStatsDataSet(File input, Set<String> ignoreColumns) throws IOException{
			this.input=input;
			try(BufferedReader in = new BufferedReader(new FileReader(input))){
				String line = in.readLine();
				String[] parts = line.split("\t");
				headers=parts;
				indexMap = new int[headers.length];
				int filtered=0;
				{
					for(int i=0;i<headers.length;i++) {
						if(ignoreColumns.contains(headers[i])) {
							filtered++;
							indexMap[i] = -1;
							continue;
						}
						varCount++;
						indexMap[i]=i-filtered;
						if(filtered==0)
							continue;
						headers[i-filtered]=headers[i];
					}
					String[] newHeaders = new String[headers.length-filtered];
					System.arraycopy(headers, 0, newHeaders, 0, newHeaders.length);
					headers=newHeaders;
				}
				sums=new double[headers.length];
				means=new double[sums.length];
				line=in.readLine();
				while(line!=null) {
					parts = line.split("\t");
					if(parts.length==0)
						continue;
					if((parts.length-filtered)!=headers.length)
						throw new IOException("Row size mismatched header size: "+line);
					for(int i=0;i<parts.length;i++) {
						if(indexMap[i]>=0)
							sums[indexMap[i]]+=Integer.parseInt(parts[i]);
					}
					line=in.readLine();
					rowCount++;
				}
				for(int i=0;i<sums.length;i++)
					means[i]=sums[i]/rowCount;
			}
		}

		/* (non-Javadoc)
		 * @see java.lang.Iterable#iterator()
		 */
		@Override
		public Iterator<WeightedDatum> iterator() {
			try {
				close();
			}
			catch(IOException t) {
				t.printStackTrace();
			}
			try {
				currentIterator = new CSVStatsDataIterator();
			}
			catch(IOException t) {
				throw new RuntimeException(t);
			}
			return currentIterator;
		}

		/* (non-Javadoc)
		 * @see java.io.Closeable#close()
		 */
		@Override
		public void close() throws IOException {
			if(currentIterator!=null) {
				currentIterator.close();
				currentIterator=null;
			}
		}

		/* (non-Javadoc)
		 * @see com.shtick.math.statistics.StatsDataSet#setMean(int, float)
		 */
		@Override
		public void setMean(int var, float mean) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.shtick.math.statistics.StatsDataSet#size()
		 */
		@Override
		public int size() {
			return rowCount;
		}

		/* (non-Javadoc)
		 * @see com.shtick.math.statistics.StatsDataSet#getVariableCount()
		 */
		@Override
		public int getVariableCount() {
			return varCount;
		}
		
		private class CSVStatsDataIterator implements Iterator<WeightedDatum>{
			private WeightedDatum next;
			private BufferedReader in;
			
			public CSVStatsDataIterator() throws IOException{
				in = new BufferedReader(new FileReader(input));
				in.readLine(); // Skip headers. 
			}

			/* (non-Javadoc)
			 * @see java.util.Iterator#hasNext()
			 */
			@Override
			public boolean hasNext() {
				return getNext()!=null;
			}

			/* (non-Javadoc)
			 * @see java.util.Iterator#next()
			 */
			@Override
			public WeightedDatum next() {
				WeightedDatum retval = getNext();
				next=null;
				return retval;
			}
			
			private WeightedDatum getNext() {
				if(next==null) {
					try { // Read and interpret the next line
						String line = in.readLine();
						while(line!=null) {
							String[] parts = line.split("\t");
							if(parts.length!=0) {
								next = new WeightedDatum();
								next.weight = 1;
								next.datum = new double[varCount];
								for(int i=0;i<parts.length;i++)
									if(indexMap[i]>=0)
										next.datum[indexMap[i]]=Double.parseDouble(parts[i])-means[indexMap[i]];
								break;
							}
							line = in.readLine();
						}
					}
					catch(Throwable t) {
						throw new RuntimeException(t);
					}
				}
				return next;
			}
			
			private void close() throws IOException{
				in.close();
			}
		}
		
	}
}
