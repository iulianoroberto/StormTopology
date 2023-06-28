package app;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

public class SamplingBolt extends BaseRichBolt{
	
	private OutputCollector collector;
	private int count = 0;
	private Utils utility;
	private String filename = "logfile.txt";

	@Override
	public void execute(Tuple arg0) {
		
		String line = null;
		count ++;
		utility = new Utils();
		
		// Lettura tupla da SocketSpout ed elaborazione
		int id = arg0.getIntegerByField("ID");
		String loc = arg0.getStringByField("location");
		int temp = arg0.getIntegerByField("temperature");
		
		// Stampa valore tupla
		System.out.println("SamplingBolt, ricevuto: (" + id + ", " + loc + ", " + temp + ") - " + "Ricevuto da: " + arg0.getSourceComponent() + " - " + arg0.getSourceStreamId());
		
		// Campionamento
		int hash = Math.abs(id) % 5;
		if(hash == 3){
			// Scrittura su file (alertfile.txt)
			line = String.valueOf(id + ", " + loc + ", " + temp + "\n");
			utility.write(line, filename);		
		}
		
		// ack della tupla
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
