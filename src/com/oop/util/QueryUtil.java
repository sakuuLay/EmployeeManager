/**
 * OOP 2018
 * 
 * @author Udara Samaratunge  Department of Software Engineering, SLIIT 
 * 
 * @version 1.0
 * Copyright: SLIIT, All rights reserved
 * 
 */
package com.oop.util;

import com.oop.service.EmployeeServiceImpl;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This load Sql queries available in the EmployeeQuery.xml
 * 
 * @author Udara Samaratunge, SLIIT
 * @version 1.0
 * @see #CommonUtil
 */
public class QueryUtil extends CommonUtil {

	/**
	 * This method read the EmployeeQuery.xml file and retrieve the query by
	 * query id.
	 * 
	 * @param id
	 *            QueryID to retrieve elements
	 * 
	 * @return String formatted query will be returned as output
	 * 
	 * @throws ParserConfigurationException
	 *             - Indicates a serious configuration error.
	 * @throws IOException
	 *             - This class is the general class of exceptions produced by
	 *             failed or interrupted I/O operations.
	 * @throws SAXException
	 *             - Encapsulate a general SAX error or warning.
	 * 
	 * @see EmployeeServiceImpl#addEmployees()
	 * @see EmployeeServiceImpl#createEmployeeTable()
	 * @see EmployeeServiceImpl#displayAllEmployees()
	 * @see EmployeeServiceImpl#removeEmployee(String)
	 * 
	 */
	public static String queryByID(String id) throws SAXException, IOException, ParserConfigurationException {

		NodeList nodeList;
		Element element = null;
		Element found = null;
		/*
		 * Read the EmployeeQuery.xml file and read each query node into node
		 * list. Prefer loading from classpath using the configured queryFilePath
		 * in config.properties so it works both in IDE and when deployed.
		 */
		String queryFile = properties.getProperty(CommonConstants.QUERY_XML);
		if (queryFile == null || queryFile.isEmpty()) {
		    throw new IOException("Query file path not configured (" + CommonConstants.QUERY_XML + ")");
		}

		InputStream in = QueryUtil.class.getResourceAsStream("/" + queryFile);
		if (in == null) {
			in = QueryUtil.class.getResourceAsStream(queryFile);
		}

		if (in != null) {
			nodeList = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in)
					.getElementsByTagName(CommonConstants.TAG_NAME);
		} else {
			// Fallback to original Tomcat wtp path for IDE deployments
			String path = System.getProperty("catalina.base") + "\\wtpwebapps\\OOPEmployeeWebApp\\WEB-INF\\" + queryFile;
			nodeList = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path))
					.getElementsByTagName(CommonConstants.TAG_NAME);
		}

		/*
		 * Extract the node from node list using query id query id is taken from
		 * query node attribute
		 */
		for (int value = 0; value < nodeList.getLength(); value++) {
			element = (Element) nodeList.item(value);
			if (element.getAttribute(CommonConstants.ATTRIB_ID).equals(id)) {
				found = element;
				break;
			}
		}
		if (found == null) {
			throw new IOException("Query id '" + id + "' not found in " + queryFile);
		}
		return found.getTextContent().trim();
	}
}
