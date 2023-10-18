package br.com.ccs.rinha.domain.repository;

import br.com.ccs.rinha.domain.entity.Pessoa;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, UUID> {

    @Query("from Pessoa p join fetch p.stack where p.id= :id")
    Optional<Pessoa> findByIdEager(UUID id);

    @Query(value = "from Pessoa p join fetch p.stack s " +
            "where p.nome ilike concat('%',:t,'%') " +
            "or p.apelido ilike concat('%', :t, '%') " +
            "or s ilike concat('%', :t, '%')")
    @Transactional(readOnly = true)
    List<Pessoa> findByTermo(String t, PageRequest pageRequest);
}
