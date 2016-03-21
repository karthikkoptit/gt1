/**
 * 
 */
package com.nutrisystem.orange.java.ws.output;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Wei Gao
 * 
 */
@XmlType(propOrder = { "session_id", "num_found", "docs", "status", "error_message", "debug" })
public class SolrSearchOutput extends AbstractOutput {
    private Long numFound;
    private Object docs;

    public Long getNumFound() {
	return numFound;
    }

    @XmlElement(name = "num_found")
    public void setNumFound(Long numFound) {
	this.numFound = numFound;
    }

    public Object getDocs() {
	return docs;
    }

    public void setDocs(Object docs) {
	this.docs = docs;
    }
}
