// package com.lingfenglong.hotel;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.lingfenglong.hotel.constants.HotelConstants;
// import com.lingfenglong.hotel.dao.HotelRepository;
// import com.lingfenglong.hotel.entity.HotelDoc;
// import org.apache.http.HttpHost;
// import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
// import org.elasticsearch.action.index.IndexRequest;
// import org.elasticsearch.client.indices.CreateIndexRequest;
// import org.elasticsearch.client.RequestOptions;
// import org.elasticsearch.client.RestClient;
// import org.elasticsearch.client.RestHighLevelClient;
// import org.elasticsearch.client.indices.GetIndexRequest;
// import org.elasticsearch.common.xcontent.XContentType;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
//
// import java.io.IOException;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// @SpringBootTest
// class HotelApplicationTest1 {
//     private static RestHighLevelClient client;
//
//     @Autowired
//     private HotelRepository hotelRepository;
//     @Autowired
//     private ObjectMapper jacksonObjectMapper;
//
//     @Test
//     void createHotelIndexTest() throws IOException {
//         CreateIndexRequest createIndexRequest =
//                 new CreateIndexRequest(HotelConstants.HOTEL_INDEX)
//                 .source(HotelConstants.HOTEL_MAPPINGS, XContentType.JSON);
//         client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
//
//         GetIndexRequest getIndexRequest = new GetIndexRequest(HotelConstants.HOTEL_INDEX);
//         assertTrue(client.indices().exists(getIndexRequest, RequestOptions.DEFAULT));
//     }
//
//     @Test
//     void deleteHotelIndexTest() throws IOException {
//         DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(HotelConstants.HOTEL_INDEX);
//         client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
//
//         GetIndexRequest getIndexRequest = new GetIndexRequest(HotelConstants.HOTEL_INDEX);
//         assertFalse(client.indices().exists(getIndexRequest, RequestOptions.DEFAULT));
//     }
//
//     @Test
//     void addDocumentTest() {
//         hotelRepository.findAll()
//                 .stream()
//                 .map(HotelDoc::new)
//                 .forEach(hotelDoc -> {
//                     try {
//                         IndexRequest indexRequest = new IndexRequest(HotelConstants.HOTEL_INDEX)
//                                 .id(hotelDoc.id().toString())
//                                 .source(jacksonObjectMapper.writeValueAsString(hotelDoc), XContentType.JSON);
//                         client.index(indexRequest, RequestOptions.DEFAULT);
//                     } catch (IOException e) {
//                         throw new RuntimeException(e);
//                     }
//                 });
//     }
//
//     @BeforeEach
//     void setUp() {
//         client = new RestHighLevelClient(
//                 RestClient.builder(
//                         HttpHost.create("http://localhost:9200")
//                 )
//         );
//     }
//     @AfterEach
//     void tearDown() throws IOException {
//         client.close();
//     }
// }