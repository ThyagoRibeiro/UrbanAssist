package br.com.urbanassist.dao;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Variable;

import br.com.urbanassist.model.Edge;
import br.com.urbanassist.model.Thing;
import br.com.urbanassist.model.User;
import br.com.urbanassist.util.Constants;
import br.com.urbanassist.util.FileManager;

public class DirectionDAO {

	private static OntologyResolver ontologyResolver = new OntologyResolver();

	public static Edge removeEdge(User user, Thing thingOrigin) {

		Edge edge = new Edge();
		Set<Map<Variable, Term>> result = ontologyResolver.runProgram(
				"Direction(?user, ?thingOrigin, ?thingDestination, ?distance, ?degree) and ?user[hasID hasValue "
						+ user.getID() + "] and ?thingOrigin[hasID hasValue " + thingOrigin.getID() + "]");

		for (Map<Variable, Term> map : result) {

			for (Variable key : map.keySet()) {
				if (!key.toString().equals("?user")) {
					if (key.toString().equals("?thingOrigin"))
						edge.setOriginID(Integer.parseInt(map.get(key).toString().replaceAll(".*thing", "")));
					else if (key.toString().equals("?thingDestination"))
						edge.setDestinationID(Integer.parseInt(map.get(key).toString().replaceAll(".*thing", "")));
					else if (key.toString().equals("?distance"))
						edge.setDistance(Integer.parseInt(map.get(key).toString()));
					else
						edge.setDegree(Integer.parseInt(map.get(key).toString()));
				}
			}
		}

		FileManager.deleteString("relationInstance Direction(user" + user.getID() + ", thing" + edge.getOrigin().getID()
				+ ", thing" + edge.getDestination().getID() + ", " + edge.getDistance() + ", " + edge.getDegree() + ")", Constants.ONTOLOGY_FILE);

		ontologyResolver = new OntologyResolver();

		return edge;
	}

	public static void insert(User user, LinkedList<Edge> edgeList) {
		for (Edge edge : edgeList) {
			insert(user, edge);
		}
	}

	public static void insert(User user, Edge edge) {

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("\r\n\r\nrelationInstance Direction(user");
		stringBuilder.append(user.getID());
		stringBuilder.append(", thing");
		stringBuilder.append(edge.getOrigin().getID());
		stringBuilder.append(", thing");
		stringBuilder.append(edge.getDestination().getID());
		stringBuilder.append(", ");
		stringBuilder.append(edge.getDistance());
		stringBuilder.append(", ");
		stringBuilder.append(edge.getDegree());
		stringBuilder.append(")");

		FileManager.writeString(Constants.ONTOLOGY_FILE, stringBuilder.toString(), true);
	}

}
