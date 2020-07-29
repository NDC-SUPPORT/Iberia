#!/bin/bash
fch=$1
hIni=$2
hFin=$3
curl -XGET 'http://ibisesdata.corp.iberia.es:9200/logstash-business-ndc-checkout-debug-*/sse-orm.mail-manage.native-mail-provider-send.1.debug/_search?size=999&from=0' -u support:HtaCFjwHTZGa98Rf -d '{

  "sort": [
    {
      "@timestamp": {
        "order": "asc"
      }
    }
  ],
  "query": {
    "bool": {
      "must": [
      	{
          "query_string": {
            "query": "kpi.parameters.mailProviderRequest.body.salesforceRequest.salesforceBodyRequest.json_data.string:(*locator*)",
            "analyze_wildcard": true
          }
        },
        {
          "match_phrase": {
            "client": {
              "query": "ndc"
            }
          }
        },
        {
          "range": {
            "elapsedTime": {
              "gte": 60000,
              "lt": null
            }
          }
        },
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
		}
      ]
    }
  },
  "_source": ["request","@timestamp","kpi.parameters.mailProviderRequest.body.salesforceRequest","user"]
  
}'