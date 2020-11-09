package pl.medicalcenter.infrastructure.users.email;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Mail {
    public final String from;
    public final String to;
    public final String subject;
    public final String content;
}
