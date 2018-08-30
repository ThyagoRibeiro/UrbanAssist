package br.com.urbanassist.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Variable;

import br.com.urbanassist.model.Attribute;
import br.com.urbanassist.model.Thing;
import br.com.urbanassist.util.Constants;
import br.com.urbanassist.util.FileManager;

public class ThingDAO {

	private static OntologyResolver ontologyResolver = new OntologyResolver();

	public static void addNeighbour(int idOrigin, int idDestination, int distance, int degree) {

		FileManager.writeString(Constants.ONTOLOGY_FILE, "\r\nrelationInstance Successor(thing" + idOrigin + ", thing"
				+ idDestination + ", " + distance + ", " + degree + ")", true);
		ontologyResolver = new OntologyResolver();
	}

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

			newThing.setID(Integer.parseInt(objAttr.get("hasID")));
			newThing.setName(new Attribute(objAttr.get("hasNameText"), objAttr.get("hasNameAudio"),
					objAttr.get("hasNameVideo")));
			newThing.setDescription(new Attribute(objAttr.get("hasDescriptionText"), objAttr.get("hasDescriptionAudio"),
					objAttr.get("hasDescriptionVideo")));
			newThing.setMessage(new Attribute(objAttr.get("hasMessageText"), objAttr.get("hasMessageAudio"),
					objAttr.get("hasMessageVideo")));
			newThing.setAlert(new Attribute(objAttr.get("hasAlertText"), objAttr.get("hasAlertAudio"),
					objAttr.get("hasAlertVideo")));
			newThing.setDisplay(new Attribute(objAttr.get("hasDisplayText"), objAttr.get("hasDisplayAudio"),
					objAttr.get("hasDisplayVideo")));

			String locality;
			if ((locality = objAttr.get("isInLocality")) != null)
				newThing.setLocality(
						LocalityDAO.select(Integer.parseInt(locality.replaceAll(".*#locality", "")), false));

			String attrClass;
			if ((attrClass = objAttr.get("hasClass")) != null) {
				newThing.setAttrClass(AttrClassDAO.select(Integer.parseInt(attrClass.replaceAll(".*#class", ""))));
			}

			String responsible;
			if ((responsible = objAttr.get("hasResponsible")) != null) {
				newThing.setResponsible(
						ResponsibleDAO.select(Integer.parseInt(responsible.replaceAll(".*#responsible", ""))));
			}

			String situation;
			if ((situation = objAttr.get("hasSituation")) != null) {
				newThing.setSituation(SituationDAO.select(Integer.parseInt(situation.replaceAll(".*#situation", ""))));
			}

			objList.add(newThing);
		}

		return objList;
	}

	public static void delete(int idThing) {

		FileManager.deleteInstance("instance thing" + idThing + " memberOf Thing");
		ontologyResolver = new OntologyResolver();
	}
	
	

	public static void deleteNeighbour(int idOrigin, int idDestination) {
		FileManager.deleteStringStartsWith("relationInstance Successor(thing" + idOrigin + ", thing" + idDestination,
				Constants.ONTOLOGY_FILE);
		ontologyResolver = new OntologyResolver();
	}

	private static int getNumberResults() {
		return runQuery("?x memberOf Thing").size();
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

	public static void insert(Thing thing, boolean hasID) {

		StringBuilder stringBuilder = new StringBuilder();

		String value = "";

		if (!hasID) {
			thing.setID(getNumberResults() + 1);
		}

		stringBuilder.append("\r\n\r\ninstance thing");
		stringBuilder.append(thing.getID());
		stringBuilder.append(" memberOf Thing");

		stringBuilder.append("\r\nhasID hasValue ");
		stringBuilder.append(thing.getID());

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

		if (thing.getLocality() != null) {

			stringBuilder.append("\r\nisInLocality hasValue locality");
			stringBuilder.append(thing.getLocality().getID());
			stringBuilder.append("");
		}

		if (thing.getSituation() != null) {

			stringBuilder.append("\r\nhasSituation hasValue situation");
			stringBuilder.append(thing.getSituation().getIdSituation());
			stringBuilder.append("");
		}

		if (thing.getAttrClass() != null) {

			stringBuilder.append("\r\nhasClass hasValue class");
			stringBuilder.append(thing.getAttrClass().getIdClass());
			stringBuilder.append("");
		}

		if (thing.getResponsible() != null) {

			stringBuilder.append("\r\nhasResponsible hasValue responsible");
			stringBuilder.append(thing.getResponsible().getID());
			stringBuilder.append("");
		}

		FileManager.writeString(Constants.ONTOLOGY_FILE, stringBuilder.toString(), true);
		ontologyResolver = new OntologyResolver();
	}

	private static Set<Map<Variable, Term>> runQuery(String query) {
		return ontologyResolver.runProgram(query);
	}

	public static ArrayList<Thing> select() {
		return createThingList(runQuery("?thing[?attribute hasValue ?value] memberOf Thing"));
	}

	public static Thing select(int id) {

		ArrayList<Thing> thingList = createThingList(
				runQuery("?thing[?attribute hasValue ?value] memberOf Thing and ?thing[hasID hasValue " + id + "]"));

		if (thingList.size() > 0)
			return thingList.get(0);
		else
			return null;
	}

	private static Thing select(String thingIdentifier) {
		ArrayList<Thing> thingList = createThingList(
				runQuery("?thing[?attribute hasValue ?value] memberOf Thing and ?thing = " + thingIdentifier));

		if (thingList.size() > 0)
			return thingList.get(0);
		else
			return null;
	}

	public static ArrayList<Thing> selectByKeyword(String keyword) {
		ArrayList<Thing> thingList = new ArrayList<>();

		for (Thing thing : select()) {
			if (thing.getName().getText().toUpperCase().contains(keyword.toUpperCase())
					|| thing.getDescription().getText().toUpperCase().contains(keyword.toUpperCase()))
				thingList.add(thing);
		}

		return thingList;
	}

	public static void update(Thing thing) {

		delete(thing.getID());
		insert(thing, true);
	}

	public static void updateNeighbour(int idOrigin, int idDestination, int distance, int degree) {
		deleteNeighbour(idOrigin, idDestination);
		addNeighbour(idOrigin, idDestination, distance, degree);
	}
}
