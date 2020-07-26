#!/bin/bash
fch=$1
hIni=$2
hFin=$3
curl -XGET 'http://ibisesdata.corp.iberia.es:9200/logstash-business-ndc-checkout-traces-*/ass-fass.issue.issue-emds.v5.info/_search?size=999&from=0&pretty' -u support:HtaCFjwHTZGa98Rf -d '{

  "query": {
    "bool": {
      "must": [
		{
			"range": {
				"@timestamp": {
					"from": "'${fch}'T'${hIni}'.000",
					"to":   "'${fch}'T'${hFin}'.999",
					"time_zone": "Europe/Madrid",
					"include_lower": true,
					"include_upper": true
				}
			}
		},
        {
          "query_string": {
            "default_field": "exception.errorCode",
            "query": "ISSUE_ERROR_0001"
          }
        }
      ]
    }   
  }
  
}'>pruebaAncillaries.txt
