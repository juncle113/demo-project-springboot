package com.cpto.dapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * BaseRepository
 *
 * @author sunli
 * @date 2018/12/19
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

    /**
     * 返回查询到的非空数据，否则抛出数据未找到异常
     *
     * @param id 查询主键
     * @return 查询到的数据
     * @throws IllegalArgumentException 无效的参数异常
     */
    default T findNotNullById(ID id) {

        Optional<T> optional = findById(id);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException();
        }

        return optional.get();
    }
}