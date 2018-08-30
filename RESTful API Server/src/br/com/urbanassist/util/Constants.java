package br.com.urbanassist.util;

public class Constants {

	protected static final String RESOURCES_PATH = System.getProperty("catalina.base") + "/UrbanAssist/resources";
	private static final String WSML_PATH = RESOURCES_PATH + "/wsml";
	private static final String ARFF_PATH = RESOURCES_PATH + "/arff";
	public static final String ARFF_FILE = ARFF_PATH + "/poiData.arff";
	public static final int MIN_RSSI = -300;
	public static final String ONTOLOGY_FILE = WSML_PATH + "/Ontology.wsml";

}
