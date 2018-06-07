package br.com.urbanassist.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Variable;

import br.com.urbanassist.model.Attribute;
import br.com.urbanassist.model.Edge;
import br.com.urbanassist.model.Locality;
import br.com.urbanassist.model.Path;
import br.com.urbanassist.model.Thing;
import br.com.urbanassist.model.WifiData;
import br.com.urbanassist.util.ClassificationAlgorithm;
import br.com.urbanassist.util.Classifiers;
import br.com.urbanassist.util.Constants;
import br.com.urbanassist.util.FileManager;

public class ThingDAO {

	private static ArrayList<Thing> createThingList(Set<Map<Variable, Term>> result) {

		ArrayList<Thing> objList = new ArrayList<>();
		HashMap<String, HashMap<String, String>> objMap = new HashMap<>();

		for (Map<Variable, Term> map : result) {

			String thingStr = null, attribute = null, value = null;

			for (Variable variable : map.keySet()) {

				if (variable.getName().equals("thing"))
					thingStr = map.get(variable).toString();

				if (variable.getName().equals("attribute"))
					attribute = map.get(variable).toString().replaceAll(".*#", "");

				if (variable.getName().equals("value"))
					value = map.get(variable).toString();
			}

			if (objMap.containsKey(thingStr)) {
				objMap.get(thingStr).put(attribute, value);
			} else {
				HashMap<String, String> newAttrMap = new HashMap<>();
				newAttrMap.put(attribute, value);
				objMap.put(thingStr, newAttrMap);
			}
		}

		for (java.util.Map.Entry<String, HashMap<String, String>> entry : objMap.entrySet()) {
			HashMap<String, String> objAttr = entry.getValue();

			Thing newThing = new Thing();

			newThing.setID(Integer.parseInt(objAttr.get("\r\nhasID")));
			newThing.setName(new Attribute(objAttr.get("\r\nhasNameText"), objAttr.get("\r\nhasNameAudio"),
					objAttr.get("\r\nhasNameVideo")));
			newThing.setDescricao(new Attribute(objAttr.get("\r\nhasDescriptionText"),
					objAttr.get("\r\nhasDescriptionAudio"), objAttr.get("\r\nhasDescriptionVideo")));
			newThing.setMessage(new Attribute(objAttr.get("\r\nhasMessageText"), objAttr.get("\r\nhasMessageAudio"),
					objAttr.get("\r\nhasMessageVideo")));
			newThing.setAlert(new Attribute(objAttr.get("\r\nhasAlertText"), objAttr.get("\r\nhasAlertAudio"),
					objAttr.get("\r\nhasAlertVideo")));
			newThing.setDisplay(new Attribute(objAttr.get("\r\nhasDisplayText"), objAttr.get("\r\nhasDisplayAudio"),
					objAttr.get("\r\nhasDisplayVideo")));

			// thing.setLocalizacao(LocalityDAO.select(attrsMap.get("locality").toString()));
			// thing.setResponsavel(ResponsibleDAO.select(attrsMap.get("responsible").toString()));
			// thing.setSituacao(SituationDAO.select(attrsMap.get("situation").toString()));

			objList.add(newThing);
		}

		return objList;
	}

	public static ArrayList<Thing> getSuccessors(Thing thing) {

		ArrayList<Thing> objList = new ArrayList<>();

		String query = "?thing[hasSuccessor hasValue ?successor] and ?thing[hasID hasValue " + thing.getID() + "]";

		for (Map<Variable, Term> map : runQuery(query)) {

			for (Variable key : map.keySet()) {

				if (key.toString().equals("?successor"))
					objList.add(select(map.get(key).toString().replaceAll(".*#", "")));
			}

		}
		return objList;
	}

	private static Set<Map<Variable, Term>> runQuery(String query) {
		return OntologyResolver.getInstance().runProgram(Constants.ONTOLOGY_FILE, query);
	}

	public static ArrayList<Thing> select() {
		return createThingList(
				runQuery("?thing[?attribute hasValue ?value] memberOf Thing and ?concept subConceptOf Thing"));
	}

	public static Thing select(int id) {
		return createThingList(
				runQuery("?thing[?attribute hasValue ?value] memberOf Thing and ?thing[hasID hasValue " + id + "]"))
						.get(0);
	}

	private static Thing select(String thingIdentifier) {
		return createThingList(
				runQuery("?thing[?attribute hasValue ?value] memberOf Thing and ?thing = " + thingIdentifier)).get(0);
	}

	public void delete() {
		// TODO Auto-generated method stub

	}

	public static void insert(Thing thing) {

		StringBuilder stringBuilder = new StringBuilder();

		String value = "";

		stringBuilder.append("\r\n\r\ninstance thing");
		stringBuilder.append(getNumberResults() + 1);
		stringBuilder.append(" memberOf ");
		stringBuilder.append(thing.getConcept().getText());

		stringBuilder.append("\r\nhasNameText hasValue \"");
		stringBuilder.append(thing.getName().getText());
		stringBuilder.append("\"");

		if (!(value = thing.getName().getAudio()).equals("")) {

			stringBuilder.append("\r\nhasNameAudio hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = thing.getName().getVideo()).equals("")) {

			stringBuilder.append("\r\nhasNameVideo hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = thing.getDescription().getText()).equals("")) {

			stringBuilder.append("\r\nhasDescriptionText hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = thing.getDescription().getAudio()).equals("")) {

			stringBuilder.append("\r\nhasDescriptionAudio hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = thing.getDescription().getVideo()).equals("")) {

			stringBuilder.append("\r\nhasDescriptionVideo hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = thing.getMessage().getText()).equals("")) {

			stringBuilder.append("\r\nhasMessageText hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = thing.getMessage().getAudio()).equals("")) {

			stringBuilder.append("\r\nhasMessageAudio hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = thing.getMessage().getVideo()).equals("")) {

			stringBuilder.append("\r\nhasMessageVideo hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = thing.getAlert().getText()).equals("")) {

			stringBuilder.append("\r\nhasAlertText hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = thing.getAlert().getAudio()).equals("")) {

			stringBuilder.append("\r\nhasAlertAudio hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = thing.getAlert().getVideo()).equals("")) {

			stringBuilder.append("\r\nhasAlertVideo hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = thing.getDisplay().getText()).equals("")) {

			stringBuilder.append("\r\nhasDisplayText hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = thing.getDisplay().getAudio()).equals("")) {

			stringBuilder.append("\r\nhasDisplayAudio hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = thing.getDisplay().getVideo()).equals("")) {

			stringBuilder.append("\r\nhasDisplayVideo hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		FileManager.writeString(Constants.ONTOLOGY_FILE, stringBuilder.toString(), true);
	}

	private static int getNumberResults() {
		return runQuery("?x memberOf Thing").size();
	}

	public void update() {
		// TODO Auto-generated method stub

	}

	public static Thing discoverThing(WifiData wifiData, ClassificationAlgorithm algorithm) {

		HashMap<Integer, Double> probabilityMap = Classifiers.buildClassifier(wifiData, algorithm);

		java.util.Map.Entry<Integer, Double> entry = Collections.min(probabilityMap.entrySet(),
				new Comparator<java.util.Map.Entry<Integer, Double>>() {
					public int compare(java.util.Map.Entry<Integer, Double> entry1,
							java.util.Map.Entry<Integer, Double> entry2) {
						return entry1.getValue().compareTo(entry2.getValue());
					}
				});

		return ThingDAO.select(entry.getKey());
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

		return new Edge(origin, destination, Integer.parseInt(attrsMap.get("?distance").toString()),
				Integer.parseInt(attrsMap.get("?degree").toString()));

	}

	static PriorityQueue<Path> pQueue = new PriorityQueue<>();

	public static LinkedList<Edge> traceRoute(Thing origin, Thing destination, HashMap<Integer, Integer> heuristicMap) {

		// long start = System.currentTimeMillis();

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

					if (!path.contains(successor)) {

						Edge newEdge = ThingDAO.getEdge(current, successor);
						LinkedList<Edge> newPath = (LinkedList<Edge>) path.getPath().clone();
						newPath.add(new Edge(current, successor, newEdge.getDistance(), newEdge.getDegree()));

						pQueue.add(new Path(path.getCost() + newEdge.getDistance(),
								getHeuristic(successor.getID(), heuristicMap), newPath));
					}
				}
		}

		// System.out.println((System.currentTimeMillis() - start) / 1000);

		LinkedList<Edge> edgeList = path.getPath();
		edgeList.removeFirst();

		return edgeList;
	}

	private static int getHeuristic(int id, HashMap<Integer, Integer> heuristicMap) {

		int heuristic = 0;

		if (heuristicMap.containsKey(id))
			heuristic = heuristicMap.get(id);

		return heuristic;
	}

	public static ArrayList<Thing> searchThings(String keyword) {

		ArrayList<Thing> thingList = new ArrayList<>();  
		
		return thingList;
	}

}