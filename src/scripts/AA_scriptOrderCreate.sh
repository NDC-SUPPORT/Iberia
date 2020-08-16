#!/bin/bash
fch=$1
hIni=$2
hFin=$3
curl -XGET 'http://ibisesdata.corp.iberia.es:9200/logstash-business-ndc-checkout-traces-*/_search?size=999&from=0' -u support:HtaCFjwHTZGa98Rf -d '{

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
          "match_phrase": {
            "domain": {
              "query": "ndc-dist"
            }
          }
        },
        {
          "match_phrase": {
            "level": {
              "query": "info"
            }
          }
        },
        {
          "match_phrase": {
            "service": {
              "query": "createOrder"
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
      ],

      "must_not": [
        {
			     "exists": {
			       "field": "kpi.response.response.order.list.object"
			     }
        },
        {
			     "query_string": {
				      "default_field": "kpi.response.errors.error.list.object.shortText.string",
				      "query": "SSE_ORM_9155 SSE_COM_900510 NDC_DIST_2167 NDC_DIST_2104 NDC_DIST_FD_0001 SSE_RES_1000 NDC_DIST_2002 NDC_DIST_2003 NDC_DIST_2004 NDC_DIST_2001 NDC_DIST_2005 NDC_DIST_2006 NDC_DIST_1004 NDC_DIST_2011 NDC_DIST_2013 NDC_DIST_2015 NDC_DIST_2016 NDC_DIST_2017 NDC_DIST_2018 NDC_DIST_2019 NDC_DIST_21001 NDC_DIST_21002 NDC_DIST_2102 NDC_DIST_2103 NDC_DIST_2105 NDC_DIST_2106 NDC_DIST_2107 NDC_DIST_2108 NDC_DIST_2109 NDC_DIST_2110 NDC_DIST_2111 NDC_DIST_2112 NDC_DIST_2113 NDC_DIST_2115 NDC_DIST_2117 NDC_DIST_2118 NDC_DIST_2119 NDC_DIST_2120 NDC_DIST_2121 NDC_DIST_2122 NDC_DIST_2123 NDC_DIST_2124 NDC_DIST_2125 NDC_DIST_2126 NDC_DIST_2127 NDC_DIST_2128 NDC_DIST_2129 NDC_DIST_2130 NDC_DIST_2131 NDC_DIST_2132 NDC_DIST_2134 NDC_DIST_2135 NDC_DIST_2136 NDC_DIST_2137 NDC_DIST_2138 NDC_DIST_2139 NDC_DIST_2141 NDC_DIST_2142 NDC_DIST_2143 NDC_DIST_2144 NDC_DIST_2145 NDC_DIST_9001 NDC_DIST_9006 NDC_DIST_9011 PAC_UTILS_E0005 PMT_PPM_8005 PMT_PPM_8009 PMT_PPM_8014 PMT_PPM_8015 PMT_PPM_9009 SEE_ORM_900506 SSE_AVM_10205 SSE_AVM_10210 SSE_AVM_12001 SSE_AVM_14000 SSE_AVM_14001 SSE_AVM_9001 SSE_AVM_900501 SSE_AVM_900503 SSE_AVM_900505 SSE_AVM_900506 SSE_AVM_900507 SSE_AVM_900508 SSE_AVM_900509 SSE_AVM_900510 SSE_AVM_900514 SSE_COM_900502 SSE_COM_900505 SSE_COM_900506 SSE_COM_900507 SSE_ORM_9135 SSE_ORM_1000504 SSE_ORM_900501 SSE_ORM_900502 SSE_ORM_900505 SSE_ORM_900509 SSE_ORM_900516 SSE_ORM_900520 SSE_ORM_900521 SSE_ORM_900522 SSE_ORM_900523 SSE_ORM_900524 SSE_ORM_900539 SSE_ORM_900544 SSE_ORM_900548 SSE_ORM_900557 SSE_ORM_900558 SSE_ORM_900559 SSE_ORM_900562 SSE_ORM_900563 SSE_ORM_900571 SSE_ORM_900574 SSE_ORM_900581 SSE_ORM_900582 SSE_ORM_900583 SSE_ORM_900584 SSE_ORM_900585 SSE_ORM_900586 SSE_ORM_900587 SSE_ORM_900589 SSE_ORM_9006 SSE_ORM_900603 SSE_ORM_900605 SSE_ORM_900610 SSE_ORM_9008 SSE_ORM_9010 SSE_ORM_9023 SSE_ORM_9330 SSE_ORM_9331 SSE_ORM_9335 SSE_ORM_9336 SSE_PRF_9001 SSE_PRF_9002 SSE_PRF_9003 NDC_DIST_2133 NDC_DIST_476 SSE_ORM_900702 SSE_ORM_9134 SSE_ORM_9136 NDC_DIST_9005 PMT_PPM_8023 SSE_ORM_9138 NDC_DIST_2161 NDC_DIST_ERR_VER_001 NDC_DIST_2165 SSE_RES_1001 NDC_DIST_FF_0008"
			      }
        }
      ]
    }
  },

  "_source": ["@timestamp", "request", "version", "kpi.parameters", "exception", "kpi.response.errors"]
  
}'