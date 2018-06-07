package br.com.urbanassist.dao;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Ontology;
import org.omwg.ontology.Variable;
import org.wsml.reasoner.api.WSMLReasoner;
import org.wsml.reasoner.api.WSMLReasonerFactory;
import org.wsml.reasoner.api.inconsistency.InconsistencyException;
import org.wsml.reasoner.impl.DefaultWSMLReasonerFactory;
import org.wsml.reasoner.impl.WSMO4JManager;
import org.wsmo.common.IRI;
import org.wsmo.common.TopEntity;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.factory.Factory;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.wsml.Parser;
import org.wsmo.wsml.ParserException;

public class OntologyResolver {

	private static final OntologyResolver INSTANCE = new OntologyResolver();

	public static OntologyResolver getInstance() {
		return INSTANCE;
	}

	private LogicalExpressionFactory leFactory = null;
	private Parser wsmlparser = null;

	private WSMO4JManager wsmoManager = null;

	private WSMLReasoner getReasoner() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(WSMLReasonerFactory.PARAM_BUILT_IN_REASONER, WSMLReasonerFactory.BuiltInReasoner.IRIS);
		WSMLReasoner reasoner = DefaultWSMLReasonerFactory.getFactory().createWSMLFlightReasoner(params);
		return reasoner;
	}

	private Ontology parseAndLoadOntology(String ontologyFile)
			throws IOException, ParserException, InvalidModelException {

		try {
			final TopEntity[] identifiable = wsmlparser.parse(new FileReader(ontologyFile));

			return (Ontology) identifiable[0];

		} catch (Exception e) {
			System.out.println("Unable to parse ontology: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Executes a query.
	 * 
	 * @param reasoner
	 *            Reasoner which will execute the query
	 * @param o
	 *            Ontology over which the query will be answered
	 * @param queryString
	 *            WSML query represented as a string
	 * @return Result after the query evaluation
	 * @throws Exception
	 */
	private Set<Map<Variable, Term>> performQuery(WSMLReasoner reasoner, Ontology o, String queryString)
			throws Exception {
		LogicalExpression query = this.leFactory.createLogicalExpression(queryString, o);
		// Executes query request
		Set<Map<Variable, Term>> result = reasoner.executeQuery((IRI) o.getIdentifier(), query);
		return result;
	}

	public Set<Map<Variable, Term>> runProgram(String ontologyLocation, String query) {
		setUpFactories();
		
		Ontology ontology = null;
		try {
			ontology = parseAndLoadOntology(ontologyLocation);
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (ParserException e2) {
			e2.printStackTrace();
		} catch (InvalidModelException e2) {
			e2.printStackTrace();
		}

		WSMLReasoner reasoner = getReasoner();

		try {
			reasoner.registerOntology(ontology);
		} catch (InconsistencyException e1) {
			e1.printStackTrace();
		}

		Set<Map<Variable, Term>> result = null;
		try {
			result = performQuery(reasoner, ontology, query);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private void setUpFactories() {
		wsmoManager = new WSMO4JManager();
		leFactory = wsmoManager.getLogicalExpressionFactory();
		wsmlparser = Factory.createParser(null);
	}
}
