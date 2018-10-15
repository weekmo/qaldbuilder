package org.hobbit.qaldbuilder;

import org.apache.jena.atlas.json.JSON;
import org.hobbit.QaldBuilder;

public class TestClass {
	public static void main(String[] args) {
		QaldBuilder qa = new QaldBuilder(getQaldFormat());
		qa.setQuery("Test query");
		qa.setTriple(2);
		qa.setQuery("test2", 4);
		System.out.println(qa.getQaldQuestion());
	}
	public static String getQaldFormat() {
		return JSON.read("data/qald_format.json").toString();
	}
	public static String getQuestion() {
		return JSON.read("data/question.json").toString();
	}
}
