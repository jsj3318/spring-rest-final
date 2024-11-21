package com.nhnacademy.springrestfinal.converter;

import com.nhnacademy.springrestfinal.domain.MemberCreateCommand;
import com.nhnacademy.springrestfinal.domain.Role;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Csv2MemberConverter extends AbstractHttpMessageConverter<MemberCreateCommand> {
    public Csv2MemberConverter() {
        super(new MediaType("text", "csv"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return MemberCreateCommand.class.isAssignableFrom(clazz);
    }

    @Override
    protected MemberCreateCommand readInternal(Class<? extends MemberCreateCommand> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        //jsj,조승주,1234,25,member
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputMessage.getBody()))) {
            String body = bufferedReader.readLine();
            if( body != null ) {
                String[] info = body.split(",");
                if(info.length == 5){
                    return new MemberCreateCommand(
                            info[0],
                            info[1],
                            info[2],
                            Integer.parseInt(info[3]),
                            Role.fromString(info[4]));
                }
            }
        } catch (Exception e) {
            throw new HttpMessageNotReadableException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected boolean canRead(MediaType mediaType) {
        return mediaType != null && mediaType.getSubtype().equals("csv");
    }

    @Override
    protected void writeInternal(MemberCreateCommand memberCreateCommand, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

    }
}
