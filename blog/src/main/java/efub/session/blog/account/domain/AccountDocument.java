package efub.session.blog.account.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document("efubblog")
public class AccountDocument {



    @Builder
    public AccountDocument (String id ,String email , String password , String nickname){
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void update(String newNickname){
        this.nickname= newNickname;
    }
}
