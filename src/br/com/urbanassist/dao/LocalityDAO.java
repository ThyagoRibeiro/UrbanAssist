package br.com.urbanassist.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Variable;

import br.com.urbanassist.model.Attribute;
import br.com.urbanassist.model.Locality;
import br.com.urbanassist.model.LocalityEdge;
import br.com.urbanassist.model.Edge;
import br.com.urbanassist.util.Constants;
import br.com.urbanassist.util.FileManager;

public class LocalityDAO {

	private static String attrsQuery(String id) {

		if (id == null)
			id = "?ID";

		StringBuilder allAttr = new StringBuilder();

		return allAttr.toString();
	}

	public static void insert(Locality locality) {

		StringBuilder stringBuilder = new StringBuilder();

		String value = "";

		stringBuilder.append("\r\n\r\ninstance locality");
		stringBuilder.append(getNumberResults() + 1);
		stringBuilder.append(" memberOf Locality");

		stringBuilder.append("\r\nhasNameText hasValue \"");
		stringBuilder.append(locality.getNome().getText());
		stringBuilder.append("\"");

		if (!(value = locality.getNome().getAudio()).equals("")) {

			stringBuilder.append("\r\nhasNameAudio hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = locality.getNome().getVideo()).equals("")) {

			stringBuilder.append("\r\nhasNameVideo hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = locality.getDescription().getText()).equals("")) {

			stringBuilder.append("\r\nhasDescriptionText hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = locality.getDescription().getAudio()).equals("")) {

			stringBuilder.append("\r\nhasDescriptionAudio hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = locality.getDescription().getVideo()).equals("")) {

			stringBuilder.append("\r\nhasDescriptionVideo hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		FileManager.writeString(Constants.ONTOLOGY_FILE, stringBuilder.toString(), true);
	}

	private static int getNumberResults() {
		return runQuery("?x memberOf Locality").size();
	}

	private static ArrayList<Locality> createLocalityList(Set<Map<Variable, Term>> result) {

		ArrayList<Locality> lclList = new ArrayList<>();
		HashMap<String, String> attrsMap = new HashMap<>();

		Locality locality = new Locality();
		for (Map<Variable, Term> map : result) {

			for (Variable key : map.keySet()) {
				attrsMap.put(key.toString(), map.get(key).toString());
			}

			locality.setID(Integer.parseInt(attrsMap.get("?ID").toString()));
			locality.setNome(new Attribute(attrsMap.get("?nameText").toString(), attrsMap.get("?nameAudio").toString(),
					attrsMap.get("?nameVideo").toString()));
			locality.setDescricao(new Attribute(attrsMap.get("?descriptionText").toString(),
					attrsMap.get("?descriptionAudio").toString(), attrsMap.get("?descriptionVideo").toString()));

			lclList.add(locality);
		}

		return lclList;
	}

	public static void main(String[] args) {

		Locality locality = new Locality();
		locality.setNome(new Attribute("minha casa", "", ""));

		insert(locality);
	}

	private static Set<Map<Variable, Term>> runQuery(String query) {
		return OntologyResolver.getInstance().runProgram(Constants.ONTOLOGY_FILE, query);
	}

	public static ArrayList<Locality> select() {
		return createLocalityList(runQuery("?x memberOf Locality"));
	}

	public static Locality select(String id) {
		return createLocalityList(runQuery(attrsQuery(id))).get(0);
	}

	public void delete() {
		// TODO Auto-generated method stub

	}

	public void update() {
		// TODO Auto-generated method stub

	}

	public static LinkedList<Edge> traceRoute(Locality origin, Locality destination,
			HashMap<Integer, Integer> heuristicMap) {

		LinkedList<Edge> doorEdgeList = null;

		return doorEdgeList;

	}

	public static ArrayList<Locality> searchLocality(String keyword) {

		ArrayList<Locality> localityList = new ArrayList<>();

		return localityList;
	}

}
