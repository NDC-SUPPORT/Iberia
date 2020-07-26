#!/bin/bash
fchIni=$1
fchFin=$2
curl -XGET 'http://live-cloud.corp.iberia.es:9200/logstash-business-ndc-checkout-traces-*/_search?from=0' -H 'Content-Type: application/json' -u ndc_support:supp0rt#Kiban4 -d '{
	
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
			}
		  ],
		  "filter": [
			{
				"match_phrase": {
					"domain": {
						"query": "ndc-dist"
					}
				}
			},
			{
				"match_phrase": {
					"service": {
						"query": "getOffers"
					}
				}
			},
			{
				"match_phrase": {
					"operation": {
						"query": "soap"
					}
				}
			},
			{
				"match_phrase": {
					"level": {
						"query": "info"
					}
				}
			}
		  ]
		}
	},
	
	"size": 0,
	"aggs": {
		"usuarios": {
			"terms": {
				"field": "user.keyword",
				"order": {
					"_count": "desc"
				},
				"size": 999
			}
		}
  }
}'