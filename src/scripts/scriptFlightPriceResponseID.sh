#!/bin/bash
id=$1
fchIni=$2
fchFin=$3
curl -XGET 'http://ibisesdata.corp.iberia.es:9200/logstash-business-ndc-checkout-traces-*/ndc-dist.flightprice.soap.2.info/_search?size=25&from=0' -u support:HtaCFjwHTZGa98Rf -d '{
	
	"sort": [{
      	"@timestamp": {
        	"order": "desc"
      	}
    }],
	
	"query": {
		"bool": {
			"must": [
				{
					"range": {
						"@timestamp": {
							"from": "'${fchIni}'T00:00:00.000",
							"to":   "'${fchFin}'T23:59:59.999",
							"time_zone": "Europe/Madrid",
							"include_lower": true,
							"include_upper": true
						}
					}
				},
				{
          			"match_phrase": {
            			"kpi.response.shoppingResponseID.responseID.value.string": {
              				"query": "'${id}'"
            			}
          			}
        		}
			],
			"must_not": []
		}
	}
}'