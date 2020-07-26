#!/bin/bash
rq=$1
fIni=$2
fFin=$3
curl -XGET 'http://ibisesdata.corp.iberia.es:9200/logstash-business-ndc-checkout-debug-*/sse-orm.resiber.res-in.1.debug/_search?size=25&from=0' -u support:HtaCFjwHTZGa98Rf -d '{
	
	"sort": [{
      "@timestamp": {
        "order": "asc"
      }
    }],

	"query": {
		"bool": {
			"must": [
			{
				"range": {
					"@timestamp": {
						"from": "'${fIni}'T00:00:00.000",
						"to":   "'${fFin}'T23:59:59.999",
						"time_zone": "Europe/Madrid",
						"include_lower": true,
						"include_upper": true
					}
				}
			},
			{
				"match_phrase": {
					"request": "'${rq}'"
				}
			}		
			]
		}
	},
	
	"_source": ["request", "kpi.parameters.message.payload.string"]
	
}'