package br.com.urbanassist.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Variable;

import br.com.urbanassist.model.Edge;
import br.com.urbanassist.model.Path;
import br.com.urbanassist.model.Thing;

public class EdgeDAO {

	private static OntologyResolver ontologyResolver = new OntologyResolver();

	static PriorityQueue<Path> pQueue = new PriorityQueue<>();

	private static ArrayList<Edge> createEdgeList(Set<Map<Variable, Term>> result) {

		ArrayList<Edge> edgeList = new ArrayList<>();

		for (Map<Variable, Term> map : result) {
			Edge newEdge = new Edge();
			for (Variable key : map.keySet()) {

				if (key.toString().contains("origin"))
					newEdge.setOrigin(
							ThingDAO.select(Integer.parseInt(map.get(key).toString().replaceAll(".*#thing", ""))));
				else if (key.toString().contains("destination"))
					newEdge.setDestination(
							ThingDAO.select(Integer.parseInt(map.get(key).toString().replaceAll(".*#thing", ""))));
				else if (key.toString().contains("distance"))
					newEdge.setDistance(Integer.parseInt(map.get(key).toString()));
				else
					newEdge.setDegree(Integer.parseInt(map.get(key).toString()));
			}
			edgeList.add(newEdge);
		}

		return edgeList;
	}

	public static Edge getEdge(Thing origin, Thing destination) {

		HashMap<String, String> attrsMap = new HashMap<>();
		String query = "Successor(?origin, ?destination, ?distance, ?degree) and ?origin[hasID hasValue "
				+ origin.getID() + "] and ?destination[hasID hasValue " + destination.getID() + "]";

		for (Map<Variable, Term> map : runQuery(query)) {

			for (Variable key : map.keySet()) {
				attrsMap.put(key.toString(), map.get(key).toString());
			}
		}

		return new Edge(origin, destination, Double.parseDouble(attrsMap.get("?distance").toString()),
				Integer.parseInt(attrsMap.get("?degree").toString()));

	}

	public static ArrayList<Edge> getEdges(int thingID) {

		String query = "Successor(?origin, ?destination, ?distance, ?degree) and ?origin[hasID hasValue " + thingID
				+ "]";
		ArrayList<Edge> edgeList = new ArrayList<>();

		edgeList.addAll(createEdgeList(runQuery(query)));

		query = "Successor(?origin, ?destination, ?distance, ?degree) and ?destination[hasID hasValue " + thingID + "]";
		edgeList.addAll(createEdgeList(runQuery(query)));

		return edgeList;
	}

	private static int getHeuristic(int id, HashMap<Integer, Integer> heuristicMap) {

		int heuristic = 0;

		if (heuristicMap.containsKey(id))
			heuristic = heuristicMap.get(id);

		return heuristic;
	}

	private static Set<Map<Variable, Term>> runQuery(String query) {
		return ontologyResolver.runProgram(query);
	}

	public static LinkedList<Edge> traceRoute(Thing origin, Thing destination, HashMap<Integer, Integer> heuristicMap) {

		Path path = null;
		Thing current = null;
		pQueue.add(new Path(0, 0, new LinkedList<Edge>(Arrays.asList(new Edge(null, origin, 0, 0)))));

		while (!pQueue.isEmpty()) {

			path = pQueue.poll();
			current = path.getPath().getLast().getDestination();

			if (current.getID() == destination.getID())
				break;

			List<Thing> successors = ThingDAO.getSuccessors(current);

			if (!successors.isEmpty())
				for (Thing successor : successors) {

					if (successor != null && !path.contains(successor)) {

						Edge newEdge = getEdge(current, successor);
						LinkedList<Edge> newPath = (LinkedList<Edge>) path.getPath().clone();
						newPath.add(new Edge(current, successor, newEdge.getDistance(), newEdge.getDegree()));

						pQueue.add(new Path(path.getCost() + (int) newEdge.getDistance(),
								getHeuristic(successor.getID(), heuristicMap), newPath));
					}
				}
		}

		LinkedList<Edge> edgeList = path.getPath();
		edgeList.removeFirst();

		return edgeList;
	}

}
