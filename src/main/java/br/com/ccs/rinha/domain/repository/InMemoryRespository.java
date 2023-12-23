package br.com.ccs.rinha.domain.repository;

import br.com.ccs.rinha.domain.entity.Pessoa;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InMemoryRespository {

    Optional<Pessoa> findByIdEager(UUID id);

    List<Pessoa> findByTermo(String t, PageRequest pageRequest);

    Pessoa save(Pessoa pessoa);

    long count();
}
