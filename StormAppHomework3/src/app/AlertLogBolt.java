package app;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

import org.apache.storm.shade.org.apache.curator.shaded.com.google.common.io.Files;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

/*
 * AlertLogBolt riceve lo stream proveniente da AlertBolt quindi, le tuple
 * con schema ("timestamp", "location", "temperature") in cui la temperatura
 * risulta maggiore di 35°. L'ALertLogBolt le deve appendere ad un file
 * locale (denominato alertfile.txt).
 */
public class AlertLogBolt extends BaseRichBolt{
	
	private OutputCollector collector;
	private Utils utility;
	private String filename = "alertfile.txt";

	@Override
	public void execute(Tuple arg0) {
		
		// Variabili locali
		String line = null, loc = null;
		Long ts = null;
		Integer temp = null;
		utility = new Utils();
		
		// Lettura tupla da AlertBolt ed elaborazione
		ts = arg0.getLongByField("timestamp");
		loc = arg0.getStringByField("location");
		temp = arg0.getIntegerByField("temperature");
		
		// Stampa della tupla
		System.out.println("AlertLogBolt, ricevuto: (" + ts + ", " + loc + ", " + temp + ") - " + "Ricevuto da: " + arg0.getSourceComponent() + " - " + arg0.getSourceStreamId());
		
		// Scrittura su file (alertfile.txt)
		line = String.valueOf(ts + ", " + loc + ", " + temp + "\n");
		utility.write(line, filename);
		
		collector.ack(arg0);
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		collector = arg2;
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
