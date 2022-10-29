package cos.blog.model;

import lombok.Data;

@Data //parsing 할때 이름이 같아야 한다 !
public class OAuthToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;
}
