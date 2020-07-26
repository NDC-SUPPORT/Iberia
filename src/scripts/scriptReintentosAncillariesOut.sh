#!/bin/bash
rq=$1
curl -XGET 'http://ibisesdata.corp.iberia.es:9200/logstash-business-ndc-checkout-traces-*/ass-fass.issue.aq-outbound.v5.info/_search?size=999&from=0&pretty' -u support:HtaCFjwHTZGa98Rf -d '{

  "query": {
		"bool": {
			"must": [
			{
				"match_phrase": {
					"request": "'${rq}'"
				}
			}
			]
		}
	}
  
}'>pruebaAncillaries.txt
