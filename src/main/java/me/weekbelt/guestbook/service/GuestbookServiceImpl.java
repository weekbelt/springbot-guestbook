package me.weekbelt.guestbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.weekbelt.guestbook.dto.GuestbookDTO;
import me.weekbelt.guestbook.entity.Guestbook;
import me.weekbelt.guestbook.repository.GuestbookRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService{

    private final GuestbookRepository repository;

    @Override
    public Long register(GuestbookDTO dto) {

        log.info("DTO----------------------");
        log.info(String.valueOf(dto));

        Guestbook entity = dtoToEntity(dto);

        log.info(String.valueOf(entity));

        repository.save(entity);
        
        return entity.getGno();
    }
}
