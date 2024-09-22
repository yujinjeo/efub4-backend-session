package efub.session.blog.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Builder
public class UserRequestDTO {
    String name;

    public User toEntity() {
        return User.builder()
                .name(this.name)
                .type(UserType.MEMBER)
                .build();
    }
}
