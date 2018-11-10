package br.com.urbanassist.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Variable;

import br.com.urbanassist.model.WifiData;
import br.com.urbanassist.util.Constants;
import br.com.urbanassist.util.FileManager;
import br.com.urbanassist.util.WifiPositioningSystem;

public class WifiDataDAO {

	private static OntologyResolver ontologyResolver = new OntologyResolver();

	private static ArrayList<WifiData> createWifiDataList(Set<Map<Variable, Term>> result) {

		HashMap<String, WifiData> wifiDataMap = new HashMap<>();
		WifiData newWifiData;

		for (Map<Variable, Term> map : result) {

			String thingStr = null, attribute = null, value = null;

			for (Variable variable : map.keySet()) {

				if (variable.getName().equals("wifiData"))
					thingStr = map.get(variable).toString();

				if (variable.getName().equals("attr"))
					attribute = map.get(variable).toString().replaceAll(".*#", "");

				if (variable.getName().equals("value"))
					value = map.get(variable).toString();
			}

			if (!wifiDataMap.containsKey(thingStr))
				newWifiData = new WifiData();
			else
				newWifiData = wifiDataMap.get(thingStr);

			if (attribute.equals("ofThing"))
				newWifiData.setIDThing(Integer.parseInt(value.replaceAll(".*#thing", "")));

			// if (attribute.equals("hasDate"))
			// newWifiData.setDate(Integer.parseInt(value));

			if (attribute.equals("hasMap")) {
				String[] wifiMap = value.split("\\|");
				newWifiData.put(wifiMap[0], Integer.parseInt(wifiMap[1]));
			}

			wifiDataMap.put(thingStr, newWifiData);
		}

		return new ArrayList<WifiData>(wifiDataMap.values());
	}

	private static int getNumberResults() {
		return runQuery("?wifiData[ofThing hasValue ?value] memberOf WifiData").size();
	}

	public static void insert(WifiData wifiData) {

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("\r\n\r\ninstance wifiData");
		stringBuilder.append(getNumberResults() + 1);
		stringBuilder.append(" memberOf WifiData\r\n");

		stringBuilder.append("ofThing hasValue thing");
		stringBuilder.append(wifiData.getIDThing());
		stringBuilder.append("\r\n");

		stringBuilder.append("hasMap hasValue {");

		for (Map.Entry<String, Integer> wifiMap : wifiData.getWifiMap().entrySet()) {
			stringBuilder.append("\"" + wifiMap.getKey() + "|" + wifiMap.getValue() + "\", ");
		}

		stringBuilder.setLength(stringBuilder.length() - 2);
		stringBuilder.append("}");

		FileManager.writeString(Constants.ONTOLOGY_FILE, stringBuilder.toString(), true);		
		ontologyResolver = new OntologyResolver();
		WifiPositioningSystem.wsml2arff(select());
	}

	private static Set<Map<Variable, Term>> runQuery(String query) {

		return ontologyResolver.runProgram(query);
	}

	public static ArrayList<WifiData> select() {
		return createWifiDataList(runQuery("?wifiData[?attr hasValue ?value] memberOf WifiData"));
	}
}
