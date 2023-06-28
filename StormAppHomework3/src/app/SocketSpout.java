package app;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class SocketSpout extends BaseRichSpout{
	
	private BufferedReader buf;
	private SpoutOutputCollector collector;
	private StringTokenizer lt;
	
	/*
	 * Emissione dei dati generati attraverso il collector
	 */
	@Override
	public void nextTuple() {
		
		// Variabili locali al metodo
		String line = null, loc = null;
		Integer id = null, temp = null;
		
		// Lettura valori tramite socket (leggo una linea per volta)
		try {
			line = buf.readLine();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		// Tokenizzo la stringa (linea)
		lt = new StringTokenizer(line);
		while(lt.hasMoreTokens()){
			
			// Spostamento tra i token
			lt.nextToken(); lt.nextToken(); lt.nextToken(); lt.nextToken(); lt.nextToken();
			id = Integer.parseInt(lt.nextToken());
			loc = lt.nextToken();
			
			// Spostamento tra i token
			lt.nextToken();
			temp = Integer.parseInt(lt.nextToken());
		}
		
		// Generazione ed emissione tupla
		collector.emit(new Values(id, loc, temp));
		System.out.println("SocketSpout, emesso: (" + id + ", " + loc + ", " + temp + ") - Eseguito da: " + Thread.currentThread().getName());
	}

	/*
	 * Inizializzaione dello spout
	 */
	@Override
	public void open(Map arg0, TopologyContext arg1, SpoutOutputCollector arg2) {
		collector = arg2;
		try {
			Socket s = new Socket("127.0.0.1", 7777);
			buf = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Dichiarazione dello schema di output dello stream.
	 * Ogni tupla dello stream sarà caratterizzata da 
	 * un ID (int), una location (String) e una temperatura (int)
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		arg0.declare(new Fields("ID", "location", "temperature"));
	}

}
