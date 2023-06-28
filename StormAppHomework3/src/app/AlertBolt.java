package app;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/*
 * AlertBolt riceve le tuple dallo SocketSpout con schema ("ID", "location", "temperature")
 * e se la temperatura risulta maggiore di 35° emette una tupla con 
 * schema ("timestamp", "location", "temperature")
 * sullo stream a cui è sottoscritto AlertLogBolt
 */
public class AlertBolt extends BaseRichBolt{
	
	private OutputCollector collector;

	@Override
	public void execute(Tuple arg0) {
		
		// Variabili locali al metodo
		Long ts =  null;
		Integer id = null, temp = null;
		String loc = null;
		
		// Lettura tupla da SocketSpout ed elaborazione
		id = arg0.getIntegerByField("ID");
		loc = arg0.getStringByField("location");
		temp = arg0.getIntegerByField("temperature");
		
		// Stampa della tupla
		System.out.println("AlertBolt, ricevuto: (" + id + ", " + loc + ", " + temp + ") - Ricevuto da: " + arg0.getSourceComponent() + " - " + arg0.getSourceStreamId());
		
		
		// Filtraggio
		if (temp >= 35){
			
			ts = System.currentTimeMillis();
			
			// Emettere verso AlertLogBolt
			collector.emit(new Values(ts, loc, temp));
			
			// Ack della tupla
			collector.ack(arg0);		
		}
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		collector = arg2;
	}

	/*
	 * Definizione schema output
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		arg0.declare(new Fields("timestamp", "location", "temperature"));		
	}

}
