package br.com.urbanassist.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Variable;

import br.com.urbanassist.model.Attribute;
import br.com.urbanassist.model.Edge;
import br.com.urbanassist.model.Locality;
import br.com.urbanassist.util.Constants;
import br.com.urbanassist.util.FileManager;

public class LocalityDAO {

	private static OntologyResolver ontologyResolver = new OntologyResolver();

	private static String attrsQuery(String id) {

		if (id == null)
			id = "?ID";

		StringBuilder allAttr = new StringBuilder();

		return allAttr.toString();
	}

	private static ArrayList<Locality> createLocalityList(Set<Map<Variable, Term>> result, boolean getInnerLocality) {

		ArrayList<Locality> lclList = new ArrayList<>();
		HashMap<String, HashMap<String, String>> objMap = new HashMap<>();

		for (Map<Variable, Term> map : result) {

			String localityStr = null, attribute = null, value = null;

			for (Variable variable : map.keySet()) {

				if (variable.getName().equals("locality"))
					localityStr = map.get(variable).toString();

				if (variable.getName().equals("attribute"))
					attribute = map.get(variable).toString().replaceAll(".*#", "");

				if (variable.getName().equals("value"))
					value = map.get(variable).toString();
			}

			if (objMap.containsKey(localityStr)) {
				objMap.get(localityStr).put(attribute, value);
			} else {
				HashMap<String, String> newAttrMap = new HashMap<>();
				newAttrMap.put(attribute, value);
				objMap.put(localityStr, newAttrMap);
			}
		}

		for (java.util.Map.Entry<String, HashMap<String, String>> entry : objMap.entrySet()) {
			HashMap<String, String> objAttr = entry.getValue();

			Locality newLocality = new Locality();

			newLocality.setID(Integer.parseInt(objAttr.get("hasID")));
			newLocality.setName(new Attribute(objAttr.get("hasNameText"), objAttr.get("hasNameAudio"),
					objAttr.get("hasNameVideo")));
			newLocality.setDescription(new Attribute(objAttr.get("hasDescriptionText"),
					objAttr.get("hasDescriptionAudio"), objAttr.get("hasDescriptionVideo")));

//			if (getInnerLocality) {
//				String isContainedIn;
//				if ((isContainedIn = objAttr.get("isContainedIn")) != null)
//					newLocality.setIsContainedIn(
//							LocalityDAO.select(Integer.parseInt(isContainedIn.replaceAll(".*#locality", "")), false));
//			}
			
			String responsible;
			if ((responsible = objAttr.get("hasResponsible")) != null) {
				newLocality.setResponsible(
						ResponsibleDAO.select(Integer.parseInt(responsible.replaceAll(".*#responsible", ""))));
			}

			lclList.add(newLocality);
		}

		return lclList;
	}

	public static void delete(int idLocality) {
		
		FileManager.deleteInstance("instance locality" + idLocality + " memberOf Locality");
		ontologyResolver = new OntologyResolver();
	}

	private static int getNumberResults() {
		return runQuery("?x memberOf Locality").size();
	}

	public static void insert(Locality locality, boolean hasID) {
		
		StringBuilder stringBuilder = new StringBuilder();

		String value = "";

		if (!hasID) {
			locality.setID(getNumberResults() + 1);
		}
		
		stringBuilder.append("\r\n\r\ninstance locality");
		stringBuilder.append(locality.getID());
		stringBuilder.append(" memberOf Locality");

		stringBuilder.append("\r\nhasID hasValue ");
		stringBuilder.append(locality.getID());
		
		stringBuilder.append("\r\nhasNameText hasValue \"");
		stringBuilder.append(locality.getName().getText());
		stringBuilder.append("\"");

		if (!(value = locality.getName().getAudio()).equals("")) {

			stringBuilder.append("\r\nhasNameAudio hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = locality.getName().getVideo()).equals("")) {

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
		
		if (locality.getIsContainedIn() != null) {

			stringBuilder.append("\r\nisContainedIn hasValue locality");
			stringBuilder.append(locality.getIsContainedIn().getID());
			stringBuilder.append("");
		}

		if (locality.getResponsible() != null) {

			stringBuilder.append("\r\nhasResponsible hasValue responsible");
			stringBuilder.append(locality.getResponsible().getID());
			stringBuilder.append("");
		}
		
		FileManager.writeString(Constants.ONTOLOGY_FILE, stringBuilder.toString(), true);
		ontologyResolver = new OntologyResolver();
	}

	private static Set<Map<Variable, Term>> runQuery(String query) {
		return ontologyResolver.runProgram(query);
	}

	public static ArrayList<Locality> select() {
		return createLocalityList(runQuery("?locality[?attribute hasValue ?value] memberOf Locality"), true);
	}

	public static Locality select(int id, boolean b) {

		ArrayList<Locality> thingList = createLocalityList(runQuery(
				"?locality[?attribute hasValue ?value] memberOf Locality and ?locality[hasID hasValue " + id + "]"),
				true);

		if (thingList.size() > 0)
			return thingList.get(0);
		else
			return null;
	}

	public static ArrayList<Locality> selectByKeyword(String keyword) {

		ArrayList<Locality> localityList = new ArrayList<>();

		for (Locality locality : select()) {
			if (locality.getName().getText().toUpperCase().contains(keyword.toUpperCase())
					|| locality.getDescription().getText().toUpperCase().contains(keyword.toUpperCase()))
				localityList.add(locality);
		}

		return localityList;
	}

	public static LinkedList<Edge> traceRoute(Locality origin, Locality destination,
			HashMap<Integer, Integer> heuristicMap) {

		LinkedList<Edge> doorEdgeList = null;

		return doorEdgeList;

	}

	public static void update(Locality locality) {
		// TODO Auto-generated method stub

		delete(locality.getID());
		insert(locality, true);
	}

}
