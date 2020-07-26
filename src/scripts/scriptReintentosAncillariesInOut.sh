#!/bin/bash
curl -XGET 'http://ibisesdata.corp.iberia.es:9200/logstash-business-ndc-checkout-traces-*/_search?size=999&from=0&pretty' -u support:HtaCFjwHTZGa98Rf -d '{

  "query": {
		"bool": {
			"must": [
			{
				"query_string": {
					"query": "Qm9va2luZ0AyMDRtUEIzQnFqN1ZibmVtdzdNZGhPMDAx"
				}
			}
			]
		}
	}
  
}'>pruebaAncillaries.txt
