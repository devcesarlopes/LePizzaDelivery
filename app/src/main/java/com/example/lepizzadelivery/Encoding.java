package com.example.lepizzadelivery;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Encoding {

    protected String encodeString (String str){
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encoded = encoder.encode(str.getBytes());
        return new String(encoded);
    }

    protected String encodeMap (HashMap<String, String> map){
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encoded = encoder.encode(map.toString().getBytes());
        return new String(encoded);
    }

    protected String decodeString(String str){
        Base64.Decoder decode = Base64.getDecoder();
        byte[] decoded = decode.decode(str);
        return new String(decoded);
    }

    protected void decodeMap (String str){
        Base64.Decoder decode = Base64.getDecoder();
        byte[] decoded = decode.decode(str);
        String mapString = new String(decoded);
        Map<String, String> map = Arrays.stream(mapString.replaceAll("[/}/{]", "").split(","))
                .map(x -> x.split("="))
                .collect(Collectors.toMap(a -> a[0].trim(), a->a[1], (a, b) -> a));
        System.out.println(map);
    }
}
