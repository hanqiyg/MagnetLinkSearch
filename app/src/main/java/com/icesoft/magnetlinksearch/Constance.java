package com.icesoft.magnetlinksearch;

public class Constance {
    public static final int FAVORITE_FROM = 0;
    public static final int FAVORITE_LIMIT = 10;
    public static final String KEY_SIMPLE_QUERY = "KEY_SIMPLE_QUERY";
    public static final String KEY_SIMPLE_FROM  = "KEY_SIMPLE_FROM";
    public static final String KEY_SIMPLE_SIZE  = "KEY_SIMPLE_SIZE";
    public static final String KEY_SIMPLE_TOTAL = "KEY_SIMPLE_TOTAL";

    public static int QUERY_FROM = 0;
    public static int QUERY_SIZE = 10;
    public static final String PATH = "/test/_search";
    public static final String PREFERENCE_FILE_NAME = "mPreferences";
    public static final String PREFERENCE_KEY_INFOHASH = "infohashhex";
    public static final String PREFERENCE_KEY_QUERY_URL = "url";
    public static final String PREFERENCE_KEY_QUERY_JSON = "json";
    public static final String CONTEXT_JSON =
        "{" +
                "\"query\":     {\"query_string\": {\"fields\": [\"name\"],\"query\":\"%s\"}}," +
                "\"_source\":   [ \"_id\",\"name\",\"total_size\",\"timestamp\",\"file_count\"]," +
                "\"highlight\": {\"fields\": {\"name\": {}}}," +
                "\"from\":      %d," +
                "\"size\":      %d " +
        "}";
    public static final String ID_SEARCH =
            "{"                             +
                "\"query\" : {"             +
                    "\"term\" : {"          +
                        "\"_id\" : \"%s\""  +
                                "}"         +
                            "}"             +
            "}";
}
/*
*
GET /test/_search
{
  "query" : {
    "term" : {
      "_id" : "1df3e1ad19b0f53ae16a2d210cb91fc63b3722ad"
    }
  }
}
{
  "took" : 0,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 1,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "test",
        "_type" : "_doc",
        "_id" : "1df3e1ad19b0f53ae16a2d210cb91fc63b3722ad",
        "_score" : 1.0,
        "_source" : {
          "file_count" : "2",
          "name" : "Hellboy.II.The.Golden.Army.2008.iTALiAN.AC3.BRRip.XviD-T4P3",
          "total_size" : "1472984080",
          "files" : [
            {
              "name" : "Hellboy.II.The.Golden.Army.2008.iTALiAN.AC3.BRRip.XviD-T4P3.CD2.avi",
              "length" : "737087096"
            },
            {
              "name" : "Hellboy.II.The.Golden.Army.2008.iTALiAN.AC3.BRRip.XviD-T4P3.CD1.avi",
              "length" : "735896984"
            }
          ],
          "timestamp" : "2019-10-31T05:41:10.419752Z"
        }
      }
    ]
  }
}
*/