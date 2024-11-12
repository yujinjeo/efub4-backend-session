package efub.session.blog.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import efub.session.blog.account.domain.QAccount;
import efub.session.blog.post.domain.Post;
import efub.session.blog.post.domain.QPost;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository{

    // query dsl 의존성 주입받기
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> search(String keyword, String writerNickname) {
        // q 도메인 객체 생성하기
        QPost post=QPost.post;
        QAccount account=QAccount.account;

        // Boolean builder : 3가지 경우의 수 모두 처리, where 절 뒤의 조건 작성하는 것
        BooleanBuilder builder = new BooleanBuilder();
        // 작성자 닉네임에 대한 조건 추가
        if (writerNickname!=null && !writerNickname.isEmpty()){
            builder.and(post.account.nickname.eq(writerNickname));
        }
        // 키워드에 대한 조건 추가
        if (keyword!=null && !keyword.isEmpty()){
            builder.and(post.title.containsIgnoreCase(keyword))
                    .or(post.content.containsIgnoreCase(keyword));
        }

        return queryFactory
                .selectFrom(post)
                .join(post.account, account).fetchJoin()
                .where(builder)
                .fetch();
    }
}
