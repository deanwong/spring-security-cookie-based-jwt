package com.asksrc.comments.mapper;

import com.asksrc.comments.dto.AccountDTO;
import com.asksrc.comments.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
/**
 * @Author wangding
 * @Date 2022/1/25
 */
public interface AccountMapper {

    Account toEntity(AccountDTO accountDTO);

    @Mappings(
            @Mapping(target = "password", ignore = true)
    )
    AccountDTO toDTO(Account account);
}
