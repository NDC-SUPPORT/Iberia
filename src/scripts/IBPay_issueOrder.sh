#!/bin/bash
fch=$1
hIni=$2
hFin=$3
curl -XGET 'http://ibisesdata.corp.iberia.es:9200/logstash-business-ndc-checkout-traces-*/sse-orm.issueorder-v2.ctrl.1.info/_search?size=999&from=0' -u support:HtaCFjwHTZGa98Rf -d '{

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
  "_source": ["@timestamp", "request", "market", "user", "kpi.parameters.parameter1.sender", "kpi.parameters.parameter1.paymentMethod", "kpi.response.entity.paymentItems.list.object", "kpi.response.entity.order.orderItems.list.object", "kpi.response.entity.errors.list.object"]
  
}'