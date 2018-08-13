
[![license - GPL](https://img.shields.io/aur/license/yaourt.svg)](https://www.gnu.org/licenses/gpl-3.0.txt)
# QaldBuilder is a class to handle qald question format easily.

## How to use it with maven:

### Add repository:
```xml
<repositories>
  	<repository>
  		<name>Qald Builder</name>
  		<id>qaldBuilder</id>
  		<url>https://github.com/weekmo/qaldbuilder/raw/master/mvn-repo/</url>
  	</repository>
 </repositories>
```

### Add dependency:
```xml
</dependency>
  	<dependency>
  		<groupId>org.hobbit</groupId>
  		<artifactId>qaldbuilder</artifactId>
  	<version>1.1.0</version>
  </dependency>
 </dependencies>
```
## Examples:
### Example 1:
```java
import org.hobbit.qaldbuilder.QaldBuilder;
public class Test {
  public static void main(String[] args) {
    String sparqlQuery="SELECT DISTINCT ?ans Where {<http://dbpedia.org/resource/Stirling_Castle> <http://dbpedia.org/ontology/builder> ?x . ?x <http://dbpedia.org/ontology/birthPlace> ?ans}";
    QaldBuilder qaldBuilder = new QaldBuilder();
    qaldBuilder.setQuery(sparqlQuery);
    // Sparql service provider eg: http://dbpedia.org/sparql
    qaldBuilder.setAnswers("http://dbpedia.org/sparql");
    System.out.println(qaldBuilder.getQaldQuestion());
  }
    
```
### Output 1:
```json
{ 
  "dataset" : { } ,
  "questions" : [ 
      { 
        "query" : {"sparql" : "SELECT DISTINCT ?ans Where {<http://dbpedia.org/resource/Stirling_Castle> <http://dbpedia.org/ontology/builder> ?x . ?x <http://dbpedia.org/ontology/birthPlace> ?ans}" } ,
        "answers" : [ 
            { 
              "head" : {"vars" : [ "ans" ] } ,
              "results" : {
                  "bindings" : [ 
                      { 
                        "ans" : {
                            "type" : "uri" ,
                            "value" : "http://dbpedia.org/resource/Scotland"
                          }
                      } ,
                      { 
                        "ans" : {
                            "type" : "uri" ,
                            "value" : "http://dbpedia.org/resource/Stirling"
                          }
                      } ,
                      { 
                        "ans" : {
                            "type" : "uri" ,
                            "value" : "http://dbpedia.org/resource/Stirling_Castle"
                          }
                      } ,
                      { 
                        "ans" : {
                            "type" : "uri" ,
                            "value" : "http://dbpedia.org/resource/Kingdom_of_Scotland"
                          }
                      } ,
                      { 
                        "ans" : {
                            "type" : "uri" ,
                            "value" : "http://dbpedia.org/resource/Linlithgow"
                          }
                      } ,
                      { 
                        "ans" : {
                            "type" : "uri" ,
                            "value" : "http://dbpedia.org/resource/Linlithgow_Palace"
                          }
                      }
                    ]
                }
            }
          ]
      }
    ]
}
```
### Example 2:
```java
if(qaldBuilder.getAnswers()!=null) {
  for(String ans:qaldBuilder.getAnswers()) {
    System.out.println(ans);
  }
}
```
### Output 2:
```command
"http://dbpedia.org/resource/Scotland"
"http://dbpedia.org/resource/Stirling"
"http://dbpedia.org/resource/Stirling_Castle"
"http://dbpedia.org/resource/Kingdom_of_Scotland"
"http://dbpedia.org/resource/Linlithgow"
"http://dbpedia.org/resource/Linlithgow_Palace"
```
