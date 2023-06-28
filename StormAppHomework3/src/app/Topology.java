package app;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

public class Topology {

	public static void main(String[] args) throws Exception {
		
		// Creazione istanze config per configurazione cluster
		Config cfg = new Config();
		// Disabilito debug
		cfg.setDebug(false);
		
		/*
		 * Creazione topologia DAG
		 */
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("SocketSpout", new SocketSpout());
		builder.setBolt("SamplingBolt", new SamplingBolt()).shuffleGrouping("SocketSpout");
		builder.setBolt("AlertBolt", new AlertBolt()).shuffleGrouping("SocketSpout");
		builder.setBolt("AlertLogBolt", new AlertLogBolt()).shuffleGrouping("AlertBolt");
		
		/*
		 * Invio della topologia
		 */

		// 1 - StormSubmitter
		StormSubmitter.submitTopology("Topology_SS", cfg, builder.createTopology());
		
		// 2 - Local cluster
		//LocalCluster cluster = new LocalCluster(); //Simula un cluster Storm all'interno di una JVM
		//cluster.submitTopology("Topology", cfg, builder.createTopology());
		// Attesa
		//Thread.sleep(12000);
		// Arrestto topologia
		//cluster.shutdown();

	}

}
