#!/bin/bash
rq=$1
fchIni=$2
fchFin=$3
curl -XGET 'http://ibisesdata.corp.iberia.es:9200/logstash-business-ndc-checkout-traces-*/sse-orm.createorder.resiber-orm.1.error/_search?size=25&from=0' -u support:HtaCFjwHTZGa98Rf -d '{
	
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
            			"request": {
              				"query": "'${rq}'"
            			}
          			}
        		}
			],
			"must_not": []
		}
	}
}'