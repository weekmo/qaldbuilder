package org.hobbit.qaldbuilder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.jena.atlas.json.JSON;
import org.apache.jena.atlas.json.JsonArray;
import org.apache.jena.atlas.json.JsonBuilder;
import org.apache.jena.atlas.json.JsonObject;
import org.apache.jena.atlas.json.JsonValue;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSetFormatter;

/**
 * QaldBuilder manages Qald questions with useful function<br/>
 * github.com/weekmo
 * @author Mohammed Abdelgadir
 * @version 1.1.0
 *
 */
public class QaldBuilder {
	private JsonObject qaldFormat,questionObject;
	private JsonBuilder jsonBuilder;
	private String query,lang,questionString,datasetID;
	private int id;
	/**
	 * A constructor adds empty data set object and empty questions array.
	 */
	public QaldBuilder() {
		jsonBuilder = new JsonBuilder();
		jsonBuilder.startObject()
			.key("dataset").startObject().finishObject()
			.key("questions").startArray()
				.startObject().finishObject().finishArray()
		.finishObject();
		qaldFormat = jsonBuilder.build().getAsObject();
		this.questionObject = this.qaldFormat.get("questions").getAsArray().get(0).getAsObject();
		this.query=null;
		this.questionString=null;
		this.lang=null;
		this.datasetID=null;
		this.id = -1;
	}
	/**
	 * A constructor takes Qald formated question as a parameter
	 * @param qaldFormat : Qald Formated Question
	 * 
	 */
	public QaldBuilder(String qaldFormat) {
		jsonBuilder = new JsonBuilder();
		this.qaldFormat=JSON.parse(qaldFormat);
		if(this.qaldFormat.hasKey("questions"))
			this.setQuestionAsJson(this.qaldFormat.get("questions").getAsArray().get(0).getAsObject().toString());
		if(this.qaldFormat.hasKey("dataset"))
			if(this.qaldFormat.get("dataset").getAsObject().hasKey("id"))
				this.setDatasetID(this.qaldFormat.get("dataset").getAsObject().get("id").toString());
	}
	
	/**
	 * Set current Qald formated question
	 * @param question : Qald formated question
	 */
	public void setQuestionAsJson(String question) {
		this.questionObject=JSON.parse(question);
		this.remove("_id");
		
		if(this.questionObject.hasKey("question")) {
			if(!this.questionObject.get("question").getAsArray().isEmpty()) {
				this.questionString = this.questionObject.get("question").getAsArray().get(0).getAsObject().get("string").toString().trim().replace("\"", "");
				this.lang = this.questionObject.get("question").getAsArray().get(0).getAsObject().get("language").toString().trim().replace("\"", "");
				
				this.setQuestionString(this.questionString, lang);
			}
		}
		
		if(this.questionObject.hasKey("id"))
			this.setID(Integer.parseInt(this.questionObject.get("id").toString()));
		
		if(this.questionObject.hasKey("query"))
			this.setQuery(this.questionObject.get("query").getAsObject().get("sparql").toString());
	}
	
	/**
	 * Set question query
	 * @param query
	 */
	public void setQuery(String query) {
		query = query.trim().replace("\"", "");
		this.removeQuery();
		this.jsonBuilder.startObject().key("sparql").value(query).finishObject();
		this.questionObject.put("query", this.jsonBuilder.build());
		this.query=query;
	}
	
	/**
	 * Set answer as Json object
	 * @param answers
	 */
	public void setAnswers(JsonObject answers) {
		this.removeAnswers();
		jsonBuilder.startArray().value(answers).finishArray();
		this.questionObject.put("answers", jsonBuilder.build());
	}
	
	/**
	 * Set answer(s) by retrieving info from sparql service
	 * @param sparqlService : Sparql service url
	 */
	public void setAnswers(String sparqlService) throws Exception{
		this.removeAnswers();
		try {
			//"http://dbpedia.org/sparql"
			QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlService, query);
			//qexec.setTimeout(2000);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			if(query.startsWith("ASK WHERE")) {
				ResultSetFormatter.outputAsJSON(outputStream, qexec.execAsk());
			}else {
				ResultSetFormatter.outputAsJSON(outputStream,qexec.execSelect());
			}
			this.setAnswers(JSON.parse(outputStream.toString()));
		}catch(Exception ex) {throw ex;}
	}
	
	/**
	 * Set question string and language
	 * @param question : human readable question as string
	 * @param language
	 */
	public void setQuestionString(String question,String language) {
		language=language.trim().replace("\"", "");
		if(this.questionObject.hasKey("question"))
			this.questionObject.remove("question");
		jsonBuilder.startArray().startObject()
			.key("string").value(question)
			.key("language").value(language)
		.finishObject().finishArray();
		this.questionObject.put("question", jsonBuilder.build());
		this.questionString = question;
		this.lang = language;
	}
	
	
	public void setHybrid(String value) {
		value = value.trim().replace("\"", "");
		if(this.questionObject.hasKey("hybrid"))
			this.questionObject.remove("hybrid");
		this.questionObject.put("hybrid", value);
	}
	
	public void setOnlydbo(boolean value) {
		if(this.questionObject.hasKey("onlydbo"))
			this.questionObject.remove("onlydbo");
		this.questionObject.put("onlydbo", value);
	}
	
	public void setAggregation(boolean value) {
		if(this.questionObject.hasKey("aggregation"))
			this.questionObject.remove("aggregation");
		this.questionObject.put("aggregation", value);
	}
	
	/**
	 * Set question ID
	 * @param value
	 */
	public void setID(int value) {
		if(this.questionObject.hasKey("id"))
			this.questionObject.remove("id");
		this.questionObject.put("id", value);
		this.id=value;
	}
	
	/**
	 * Set Dataset ID
	 * @param id
	 */
	public void setDatasetID(String id) {
		id=id.trim().replace("\"", "");
		if(this.qaldFormat.hasKey("dataset"))
			if(this.qaldFormat.get("dataset").getAsObject().hasKey("id"))
				this.qaldFormat.get("dataset").getAsObject().remove("id");
		this.datasetID = id;
		this.qaldFormat.get("dataset").getAsObject().put("id", id);
	}
	
	public void removeQuery() {
		if(this.questionObject.hasKey("query"))
			this.questionObject.remove("query");
	}
	
	public void removeAnswers() {
		if(this.questionObject.hasKey("answers"))
			this.questionObject.remove("answers");
	}
	
	public void remove(String key) {
		if(this.questionObject.hasKey(key))
			this.questionObject.remove(key);
	}
	
	public void add(String key,JsonValue value) {
		this.questionObject.put(key, value);
	}
	
	public void add(String key,String value) {
		this.questionObject.put(key, value);
	}
	
	public void add(String key,long value) {
		this.questionObject.put(key, value);
	}
	
	public void add(String key,boolean value) {
		this.questionObject.put(key, value);
	}
	
	public String getQuery() {
		return this.query;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getQuestionString() {
		return this.questionString;
	}
	
	public String getQuestionLanguage() {
		return this.lang;
	}
	
	public String getDatasetID() {
		return this.datasetID;
	}
	
	/**
	 * Get answers as {@code ArrayList<Stirng>}
	 * @return List of questions
	 * @throws Exception
	 */
	public ArrayList<String> getAnswers() throws Exception{
		ArrayList<String> answers= new ArrayList<String>();
		try {
		if(this.questionObject.hasKey("answers")) {
			JsonObject answer = this.questionObject.get("answers").getAsArray().get(0).getAsObject();
			if(answer.hasKey("boolean")) {
				answers.add(answer.get("boolean").toString());
			}
			else if(answer.hasKey("results")) {
				JsonArray arrAnswers= answer.get("results").getAsObject().get("bindings").getAsArray();
				for(int i=0;i<arrAnswers.size();i++) {
					answers.add(arrAnswers.get(i).getAsObject().values().iterator().next().getAsObject().get("value").toString());
				}
			}
		}
		}catch(Exception ex) {throw ex;}
		return answers;
	}
	/**
	 * Get all question info as Qald format
	 * @return Qald formated question
	 */
	public String getQaldQuestion() {
		this.qaldFormat.get("questions").getAsArray().clear();
		this.qaldFormat.get("questions").getAsArray().add(this.questionObject);
		return this.qaldFormat.toString();
	}
}
