package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailDTO {

    private String toWho;
    private String title;
    private String contents;

}
