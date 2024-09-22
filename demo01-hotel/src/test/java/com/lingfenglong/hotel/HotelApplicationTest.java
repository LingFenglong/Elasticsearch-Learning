package com.lingfenglong.hotel;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.DeleteOperation;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingfenglong.hotel.constants.HotelConstants;
import com.lingfenglong.hotel.dao.HotelRepository;
import com.lingfenglong.hotel.entity.Hotel;
import com.lingfenglong.hotel.entity.HotelDoc;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HotelApplicationTest {
    RestClient restClient;
    ElasticsearchTransport elasticsearchTransport;
    ElasticsearchClient elasticsearchClient;

    @Autowired
    private ObjectMapper jacksonObjectMapper;
    @Autowired
    private HotelRepository hotelRepository;

    @Test
    void createHotelIndexTest() throws IOException {
        elasticsearchClient
                .indices()
                .create(request -> request
                        .index(HotelConstants.HOTEL_INDEX)
                        .withJson(new StringReader(HotelConstants.HOTEL_MAPPINGS))
                );

        BooleanResponse exists = elasticsearchClient
                .indices()
                .exists(request -> request
                        .index(HotelConstants.HOTEL_INDEX));

        assertTrue(exists.value());
    }

    @Test
    void deleteHotelIndexTest() throws IOException {
        elasticsearchClient
                .indices()
                .delete(request -> request
                        .index(HotelConstants.HOTEL_INDEX));

        BooleanResponse exists = elasticsearchClient
                .indices()
                .exists(request -> request
                        .index(HotelConstants.HOTEL_INDEX));

        assertFalse(exists.value());
    }

    @Test
    void getHotelIndexTest() throws IOException {
        GetIndexResponse response = elasticsearchClient
                .indices()
                .get(request -> request
                        .index(HotelConstants.HOTEL_INDEX));

        System.out.println(response);
    }

    @Test
    void addDocumentTest() {
        hotelRepository.findAll()
                .stream()
                .map(HotelDoc::new)
                .forEach(hotelDoc -> {
                    try {
                        elasticsearchClient.index(request -> request
                                .index(HotelConstants.HOTEL_INDEX)
                                .id(hotelDoc.id())
                                .document(hotelDoc)
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Test
    void getDocumentTest() throws IOException {

        GetResponse<HotelDoc> response = elasticsearchClient.get(request -> request
                        .index(HotelConstants.HOTEL_INDEX)
                        .id("61083")
                , HotelDoc.class);

        System.out.println(response);
    }

    @Test
    void updateDocumentTest() throws IOException {
        HotelDoc hotelDoc = elasticsearchClient
                .get(request -> request
                        .index(HotelConstants.HOTEL_INDEX)
                        .id("61083")
                    , HotelDoc.class)
                .source();

        assert hotelDoc != null;
        HotelDoc newHotelDoc = new HotelDoc(hotelDoc.id(), hotelDoc.name(), hotelDoc.address(), hotelDoc.price(), hotelDoc.score(), hotelDoc.brand(), "北京", hotelDoc.starName(), hotelDoc.business(), hotelDoc.location(), hotelDoc.pic());
        UpdateResponse<HotelDoc> response = elasticsearchClient.update(request -> request
                        .index(HotelConstants.HOTEL_INDEX)
                        .id(hotelDoc.id())
                        .doc(newHotelDoc)
                , HotelDoc.class);

        HotelDoc found = elasticsearchClient
                .get(request -> request
                                .index(HotelConstants.HOTEL_INDEX)
                                .id("61083")
                        , HotelDoc.class)
                .source();

        System.out.println(response);
        System.out.println(found);
    }

    @Test
    void deleteDocumentTest() throws IOException {
        DeleteResponse response = elasticsearchClient
                .delete(request -> request
                        .index(HotelConstants.HOTEL_INDEX)
                        .id("61083"));
        System.out.println(response);

        HotelDoc found = elasticsearchClient
                .get(request -> request
                                .index(HotelConstants.HOTEL_INDEX)
                                .id("61083")
                        , HotelDoc.class)
                .source();
        System.out.println(found);
    }

    @Test
    void addBulkDocumentTest() throws IOException {
        elasticsearchClient.bulk(request -> {
            hotelRepository.findAll()
                    .stream()
                    .map(HotelDoc::new)
                    .forEach(hotelDoc -> request.operations(opt -> opt.index(index -> index
                            .id(hotelDoc.id())
                            .document(hotelDoc)
                    )));
            return request.index(HotelConstants.HOTEL_INDEX);
        });
    }

    @Test
    void deleteBulkDocumentTest() throws IOException {
        elasticsearchClient.bulk(request -> {
            hotelRepository.findAll()
                    .stream()
                    .map(Hotel::getId)
                    .map(Object::toString)
                    .forEach(id -> request.operations(opt -> opt.delete(delete -> delete
                            .id(id)
                    )));
            return request.index(HotelConstants.HOTEL_INDEX);
        });
    }

    @BeforeEach
    void setUp() {
        restClient = RestClient
                .builder(HttpHost.create("http://localhost:9200"))
                .build();

        elasticsearchTransport = new RestClientTransport(
                restClient, new JacksonJsonpMapper(jacksonObjectMapper)
        );

        elasticsearchClient = new ElasticsearchClient(elasticsearchTransport);
    }

    @AfterEach
    void tearDown() throws IOException {
        restClient.close();
    }
}