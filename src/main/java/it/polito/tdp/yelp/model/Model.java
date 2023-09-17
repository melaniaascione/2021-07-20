package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;


public class Model {
	
	private Graph<User, DefaultWeightedEdge> grafo ;
	private YelpDao dao;
	
	public Model(){
		this.dao = new YelpDao();
	}
	
	
	public void creaGrafo(int n, int anno) {
		this.grafo = new SimpleWeightedGraph(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, this.dao.getVertici(n));
	
		for(User u1: this.getVertici()) {
			for(User u2: this.getVertici()) {
				if(!u1.equals(u2) && u1.getUserId().compareTo(u2.getUserId())<0) {
					int sim = dao.getArchi(u1, u2, anno);
					if(sim>0) {
						Graphs.addEdge(this.grafo, u1, u2, sim);
					}
				}
			}
		}
		
		System.out.println("Grafo creato");
		System.out.println("ci sono " + this.grafo.vertexSet().size() + " vertici e " + this.grafo.edgeSet().size()+ " archi.");
	}
	
	
	
	//metodo per trovare l'altro nodo a estremo dell'arco di peso massimo
	public List<User> utentiPiuSimili(User u) {
		int max = 0 ;
		for(DefaultWeightedEdge e: this.grafo.edgesOf(u)) {
			if(this.grafo.getEdgeWeight(e)>max) {
				max = (int)this.grafo.getEdgeWeight(e);
			}
		}
		
		List<User> result = new ArrayList<>();
		for(DefaultWeightedEdge e: this.grafo.edgesOf(u)) {
			if((int)this.grafo.getEdgeWeight(e) == max) {
				User u2 = Graphs.getOppositeVertex(this.grafo, e, u);
				result.add(u2);
			}
		}
		return result ;
	}
	
	
	
	//metodo che mi restituisce la lista dei vertici
	public List<User> getVertici(){
		List<User> vertici = new ArrayList<>(this.grafo.vertexSet());
		return vertici;
	}
	
	//metodo che mi restituisce il numero di vertici
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	//metodo che mi restituisce il numero di archi
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
}
