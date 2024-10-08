package com.lingfenglong.hotel.constants;

public class HotelConstants {
    public static final String HOTEL_INDEX = "hotel";
    public static final String HOTEL_MAPPINGS = """
            {
              "mappings": {
                "properties": {
                  "id": {
                    "type": "keyword"
                  },
                  "name": {
                    "type": "text",
                    "analyzer": "ik_max_word",
                    "copy_to": "all"
                  },
                  "address": {
                    "type": "keyword",
                    "index": false
                  },
                  "price": {
                    "type": "integer"
                  },
                  "score": {
                    "type": "integer"
                  },
                  "brand": {
                    "type": "keyword",
                    "copy_to": "all"
                  },
                  "city": {
                    "type": "keyword"
                  },
                  "starName": {
                    "type": "keyword"
                  },
                  "business": {
                    "type": "keyword",
                    "copy_to": "all"
                  },
                  "location": {
                    "type": "geo_point"
                  },
                  "pic": {
                    "type": "keyword",
                    "index": false
                  },
                  "all": {
                    "type": "text",
                    "analyzer": "ik_max_word"
                  }
                }
              }
            }""";
}
