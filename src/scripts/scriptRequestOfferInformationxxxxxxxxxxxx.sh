#!/bin/bash
rq=$1
fchIni=$2
fchFin=$3
curl -XPOST 'http://ibisesearch.corp.iberia.es/logstash-booking_traces-live-*/_search?size=50&from=0' -d '{

	"sort" : [
        { "@timestamp" : "desc" }
    ],

	"query": {
		"match": {
			"request": {
				"query": "'${rq}'",
				"type": "phrase"
			}
		}
	},
	
	"filter": {
		"bool": {
			"must": [
				{
					"query": {
						"query_string": {
							"analyze_wildcard": true,
							"query": "(_exists_:kpi_sse-avm_getOfferInformation_ctrl_1.response.entity.offer.slices.segments)"
						}
					}		
				},
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
				}
			],
			"must_not": []
		}
	}
}'