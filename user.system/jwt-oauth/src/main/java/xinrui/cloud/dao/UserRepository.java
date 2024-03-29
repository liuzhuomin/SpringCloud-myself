package xinrui.cloud.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xinrui.cloud.domain.User;

/**
 * <B>Title:</B>UserRepostory</br>
 * <B>Description:</B> 用户基础orm</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/26 20:41
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    @Query(value="select * from tu_user where username=?",nativeQuery = true)
    User findUserByUserName(String username);
}
