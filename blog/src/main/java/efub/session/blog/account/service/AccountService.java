package efub.session.blog.account.service;

import efub.session.blog.account.domain.Account;
import efub.session.blog.account.domain.AccountDocument;
import efub.session.blog.account.dto.AccountUpdateRequestDto;
import efub.session.blog.account.dto.SignUpRequestDto;
import efub.session.blog.account.repository.AccountDocumentRepository;
import efub.session.blog.account.repository.AccountRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
     // Redis와 통신
     // Redis에 저장할 Hash 객체 선언
    private static final String ACCOUNT_CACHE_KEY = "Account:";  //key의 접두사

    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash(); // 초기화
    }

    /*Mongo DB용 repository */
    private final AccountDocumentRepository accountDocumentRepository;

    public Long signUp(SignUpRequestDto requestDto) {
        if (existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 email입니다." + requestDto.getEmail());
        }
        Account account = accountRepository.save(requestDto.toEntity());

        //Redis 해시에 이메일과 닉네임 저장


        // 만료 시간 설정


        // Mongo DB에 저장


        return account.getAccountId();
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    /* Redis에서 ID로 email 조회 */
    @Transactional(readOnly = true)
    public String findEmailByIdfromRedis(Long id){
        String redisKey = ACCOUNT_CACHE_KEY + id;

        //Redis 해시에서 값 조회

        if(hashEntries.isEmpty()){ // Redis에 값이 없으면
            // Mysql DB에서 조회
            Account account =accountRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 Account를 찾을 수 없습니다.id="+id));

            // Mysql DB에서 조회한 정보를 Redis에 저장

            redisTemplate.expire(redisKey, 30 , TimeUnit.MINUTES); // 만료시간 설정

            return account.getEmail();
        }

        String email = (String) hashEntries.get("email");

        return email;
    }

    /* Mongo db에서 ID로 nickname 조회 */

    @Transactional(readOnly = true)
    public String findNicknameByIdfromMongod(Long id){


        return  accountDocument.getNickname();
    }


    @Transactional(readOnly = true)
    public Account findAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 Account를 찾을 수 없습니다.id="+id));
    }

    public Long update(Long accountId, AccountUpdateRequestDto requestDto) {
        Account account = findAccountById(accountId);
        account.updateAccount(requestDto.getBio(), requestDto.getNickname());

        String redisKey = ACCOUNT_CACHE_KEY + accountId;
        //Redis에서 닉네임만 update


        // mongo db에서 닉네임 update
        String _id = accountId.toString();
        AccountDocument accountDocument = accountDocumentRepository.findById(_id)
                .orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 Account를 찾을 수 없습니다.id="+accountId));

        accountDocument.update(requestDto.getNickname());
        accountDocumentRepository.save(accountDocument);


        return account.getAccountId();
    }

    @Transactional
    public void withdraw(Long accountId) {
        Account account = findAccountById(accountId);
        account.withdrawAccount();
    }

    public void delete(Long accountId) {
        Account account = findAccountById(accountId);
        // Redis에서 삭제
        String redisKey = ACCOUNT_CACHE_KEY + accountId;

        accountRepository.delete(account);

        //Mongodb에서 삭제
         String _id = accountId.toString();
        if (!accountDocumentRepository.existsById(_id)) {
            new EntityNotFoundException("해당 id를 가진 Account를 찾을 수 없습니다.id="+_id);
            }
             // 삭제

    }

    @Transactional(readOnly = true)
    public Account findAccountByEmail(String email){
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 email 를 가진 계정을 찾을 수 없습니다. email = " + email));
    }

}