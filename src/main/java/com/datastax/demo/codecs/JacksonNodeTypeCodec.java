package com.datastax.demo.codecs;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.exceptions.InvalidTypeException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Codec to easily transform JSON string representing faceting results into a map
 */
public class JacksonNodeTypeCodec extends TypeCodec<JsonNode> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JacksonNodeTypeCodec() {
        super(DataType.varchar(), JsonNode.class);
    }

    @Override
    public ByteBuffer serialize(JsonNode value, ProtocolVersion protocolVersion) throws InvalidTypeException {

        return TypeCodec.varchar().serialize(value.toString(), protocolVersion);
    }

    @Override
    public JsonNode deserialize(ByteBuffer bytes, ProtocolVersion protocolVersion) throws InvalidTypeException {
        try {
            return objectMapper.readTree(bytes.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JsonNode parse(String value) throws InvalidTypeException {
        try {
            return objectMapper.readTree(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String format(JsonNode value) throws InvalidTypeException {
        return value.toString();
    }

}
