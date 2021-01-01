package me.weekbelt.guestbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.weekbelt.guestbook.dto.GuestbookDTO;
import me.weekbelt.guestbook.dto.PageRequestDTO;
import me.weekbelt.guestbook.dto.PageResultDTO;
import me.weekbelt.guestbook.entity.Guestbook;
import me.weekbelt.guestbook.repository.GuestbookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

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

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("gno"));
        Page<Guestbook> result = repository.findAll(pageable);

        Function<Guestbook, GuestbookDTO> fn = (this::entityToDto);
        return new PageResultDTO<>(result, fn);
    }
}
